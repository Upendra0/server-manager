/**
 * 
 */
package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.MEDIATION_SERVER_XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value = "serverInstanceMigrationService")
public class ServerInstanceMigrationServiceImpl implements ServerInstanceMigrationService {

	
	private static Logger logger = Logger.getLogger(ServerInstanceMigrationServiceImpl.class);
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	
	@Autowired(required = true)
	@Qualifier(value = "serverService")
	private ServerService serverService;

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	
	/**
	 * Method will read mediation server configuration from engine API.
	 * @throws MigrationSMException 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject readServerConfiguration(CrestelNetConfigurationData crestelNetConfigData,String ipAddress,String utilityPort, String serverType, int instancePort, int staffId, String migrationPrefix, String scriptFileName) throws MigrationSMException {
		logger.debug("Fetching server configuration for mediation server configuration");
		ResponseObject responseObject  = migrationUtil.getSMAndJaxbObjectFromXml(crestelNetConfigData.getNetConfigurationData(), MigrationConstants.MEDIATION_SERVER_XSD, MEDIATION_SERVER_XML);
		if (responseObject.isSuccess()) {
			logger.info("Server instance unmarshalling done successfull!");

			Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject();
			ServerInstance serverInstance = (ServerInstance) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);
			boolean isSuccess = false;

			if (serverInstance != null) {
				Server server;

				responseObject = serverService.getServerByIpAddress(ipAddress);
				if (responseObject.isSuccess()) {
					server = (Server) responseObject.getObject();
					isSuccess = true;
				} else {
					ServerType serverTypeObj = (ServerType) MapCache.getConfigValueAsObject(serverType);
					server = new Server(serverTypeObj, migrationUtil.getRandomName(migrationPrefix + "serv"), ipAddress, Integer.parseInt(utilityPort));
					migrationUtil.setCurrentDateAndStaffId(server, staffId);
					responseObject = serverService.addServer(server);

					if (responseObject.isSuccess()) {
						isSuccess = true;
					}
				}

				if (isSuccess) {
					serverInstance.setServer(server);
					// to persist SI only w/o services, remove List of
					// services temporarily
					serverInstance.setPort(instancePort);
					serverInstance.setScriptName(scriptFileName);
					serverInstance.setId(0);
					serverInstance.setName(migrationUtil.getRandomName(migrationPrefix + "_instance"));
					migrationUtil.setCurrentDateAndStaffId(serverInstance, staffId);// Setting current date and last and created date 
					//Getting data-source configuration
					//responseObject = dataSourceMigration.getDataSourceUnmarshalObject(destinationDir, migrationPrefix); // Un-marshal and dozer conversion,XSD and business validation.

					if (responseObject.isSuccess()) {

						DataSourceConfig dataSource = (DataSourceConfig) responseObject.getObject();
						migrationUtil.setCurrentDateAndStaffId(dataSource, staffId);// Setting current and last data, staff id.
					//	serverInstance.setDatasourceConfig(dataSource);
						if (responseObject.isSuccess()) {
							logger.debug("validation being called for SI " + serverInstance);
							responseObject = serverInstanceService.validateMigrationDetails(serverInstance);

							if (responseObject.isSuccess()) {

								List<Service> saveListService = serverInstance.getServices();
								List<Service> servicelist = new ArrayList<>();
								servicelist.addAll(saveListService);

								serverInstance.setServices(null);
								responseObject = serverInstanceService.addMigratedServerInstance(serverInstance);

								if (responseObject.isSuccess() && responseObject.getObject() != null) {
									ServerInstance srvInsFromDB = (ServerInstance) responseObject.getObject();
									serverInstance = srvInsFromDB;

									logger.debug("Si is persisted now its id is " + serverInstance.getId());
									logger.debug("addin service list to to server instance.");

									// loop to save the services
									for (int i = 0; i < servicelist.size(); i++) {
										Service tempService = servicelist.get(i);
										tempService.setServerInstance(serverInstance);
										servicesService.updateMigService(tempService);
									}

								}
							}
						}
					}// Data-source configuration failed and no need to process ahead.
				}
			} else {
				logger.debug("Server instance found null!");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.MIGRATION_INSTANCE_NULL);
			}
		}
		
		
		
		
		return responseObject;
	}

}
