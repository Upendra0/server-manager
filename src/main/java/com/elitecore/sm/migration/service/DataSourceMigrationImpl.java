package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.datasource.dao.DataSourceDao;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.datasource.service.DataSourceService;
import com.elitecore.sm.datasource.validator.DataSourceConfigurationValidator;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.MigrationUtil;

/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value = "dataSourceMigration")
public class DataSourceMigrationImpl implements DataSourceMigration {

	private static Logger logger = Logger.getLogger(DataSourceMigrationImpl.class);

	@Autowired
	private MigrationUtil migrationUtil;

	@Autowired
	private DataSourceConfigurationValidator validator;

	@Autowired
	@Qualifier(value = "dataSourceService")
	private DataSourceService dataSourceService;

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired(required = true)
	@Qualifier(value = "dataSourceDao")
	private DataSourceDao dataSourceDao;

	/**
	 * Method will UN-MARSHAL data source XML and convert it to SM object with
	 * validation.
	 * 
	 * @param destinationDir
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject getDataSourceUnmarshalObject(ServerInstance serverInstance) throws MigrationSMException {

		logger.debug("Unmarshalling data source config object");
		ResponseObject responseObject ;
		responseObject = migrationUtil.getFileContentFromMap( EngineConstants.ORACLE_DATABASE_CONFIGURATION);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.DATA_SOURCE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					logger.info("JAXB unamarshalling done successfully for data source config object.");
					Map<String, Object> returnObjectsMap = (Map<String, Object>) responseObject.getObject();
					DataSourceConfig dbConfig = (DataSourceConfig) returnObjectsMap.get(MigrationConstants.MAP_SM_CLASS_KEY);
					if (dbConfig != null) {
						List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationByUrlAndUsernameAndType(dbConfig.getUsername(), dbConfig.getConnURL(),dbConfig.getType());
						if (dsConfigList != null && !dsConfigList.isEmpty()) {
							responseObject.setSuccess(true);
							responseObject.setObject(dsConfigList.get(0));
						} else {
							//check if the DS name in defaultDSConfig already exists in table
							boolean duplicateExists = serverInstanceService.checkDBConfigUniqueName(dbConfig.getName());
							if (duplicateExists) {
								dbConfig.setName(dbConfig.getName() + "_"
										+ serverInstance.getPort() + "_"
										+ serverInstance.getServer().getId());
							}
							migrationUtil.setCurrentDateAndStaffId(dbConfig, serverInstance.getCreatedByStaffId());
							responseObject = valiateDataSourceConfigParams(dbConfig); // Validating all data source parameters.
							if (responseObject.isSuccess()) {
								serverInstanceService.updateDataSourceDetails(dbConfig, serverInstance.getCreatedByStaffId(), serverInstance.getServer().getIpAddress());
								responseObject = dataSourceService.addNewDataSourceConfig(dbConfig);
							}
						}
					}
				}// For failure case response object will set messaged from calling method.
			}
		}
		return responseObject;
	}

	/**
	 * Method will validate data source configuration parameters.
	 * @param dbConfig
	 * @return
	 */
	private ResponseObject valiateDataSourceConfigParams(DataSourceConfig dbConfig) {

		logger.debug("Validating data source configuration parameters.");
		ResponseObject responseObject = new ResponseObject();

		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateDataSourceConfigForMigration(dbConfig, importErrorList, dbConfig.getName(), true);
		if (!importErrorList.isEmpty()) {
			logger.debug("Validation Fail for imported file");
			JSONArray finaljArray = new JSONArray();

			for (ImportValidationErrors errors : importErrorList) {

				JSONArray jArray = new JSONArray();
				jArray.put(errors.getModuleName());
				jArray.put(errors.getEntityName());
				jArray.put(errors.getPropertyName());
				jArray.put(errors.getPropertyValue());
				jArray.put(errors.getErrorMessage());
				finaljArray.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_SOURCE_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			responseObject.setSuccess(true);
			responseObject.setObject(dbConfig);
		}

		return responseObject;
	}

}
