/**
 * 
 */
package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.DISTRIBUTION_SERVICE_XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.MigrationServiceImpl;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service("distributionMigrationService")
public class DistributionServiceMigrationImpl  implements DistributionServiceMigration{

	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DistributionDriverMigrationService distributionDriverMigrationService;
	
	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);
	

	/**
	 * Method will UN-MARSHALING, all validation and get all distribution service and its dependents.
	 * @param serviceInstanceList
	 * @param position
	 * @param service
	 * @param folderDirPath
	 * @param serverInstance
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject getDistributionServiceAndDependents(List<Service> serviceInstanceList, int position, Service service, ServerInstance serverInstance, int staffId, String migrationPrefix) throws MigrationSMException {
		logger.debug("Fetching distribution service configuration for service id : "
				+ service.getServInstanceId());
		String serviceKey = EngineConstants.DISTRIBUTION_SERVICE + "-"
				+ service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(DISTRIBUTION_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					logger.debug("Distribution service unmarshalling and dozer conversion done successfully!");
					Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject();

					DistributionService distributionService = (DistributionService) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);
					if (distributionService != null) {
						ServiceType serviceType = (ServiceType) MapCache.getConfigValueAsObject(service.getSvctype().getAlias());
						List<Drivers> driverList = new ArrayList<>();
						distributionService.setId(0);
						distributionService.setServerInstance(serverInstance);
						distributionService.setServInstanceId(service.getServInstanceId());

						distributionService.setName(migrationUtil.getRandomName(service.getName()));
						distributionService.setStatus(StateEnum.ACTIVE);// Remove it once custom converter code done.
						distributionService.setSvctype(serviceType);

						migrationUtil.setCurrentDateAndStaffId(distributionService, staffId);

						dispayDistributionFields(distributionService); // print all distribution service parameters. will be remove once all testing done.

						responseObject = distributionDriverMigrationService.getDistributionDriverDetailsAndDependents(driverList, distributionService,staffId, migrationPrefix);

						if (responseObject.isSuccess()) {
							serviceInstanceList.set(position, distributionService);
							distributionService.setMyDrivers(driverList);
							responseObject.setSuccess(true);
						}//Else part failure message set from distribution driver details class.
					}
				}
			}

		}
		return responseObject;
	}

	
	
	/**
	 * Method will print all distribution service related parameters.
	 * @param distributionService
	 */
	private void dispayDistributionFields(DistributionService distributionService) {
		logger.info("****************************************");
		logger.info("Service Name is : " + distributionService.getName());
		logger.info("Service id  is : " + distributionService.getId());
		logger.info("Service serv instance id  is : " + distributionService.getServInstanceId());
		logger.info("Service serv instance id  is : " + distributionService.isEnableFileStats());
		logger.info("Scheduling param time is  : "  + distributionService.getServiceSchedulingParams().getTime());
		logger.info("File groupting type  is : " + distributionService.getFileGroupingParameter().getGroupingType());
		logger.info("****************************************");
	}
	
}
