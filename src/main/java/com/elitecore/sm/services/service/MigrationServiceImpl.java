package com.elitecore.sm.services.service;

import static com.elitecore.sm.common.constants.MigrationConstants.MEDIATION_SERVER_XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.core.services.data.LiveServiceSummary;
import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetDriverData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServiceData;
import com.elitecore.core.util.mbean.data.live.CrestelNetServerDetails;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.dao.DataSourceDao;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.migration.dao.MigrationTrackDao;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.migration.service.AgentMigrationService;
import com.elitecore.sm.migration.service.CollectionServiceMigration;
import com.elitecore.sm.migration.service.DataSourceMigration;
import com.elitecore.sm.migration.service.DistributionServiceMigration;
import com.elitecore.sm.migration.service.IPLogParsingServiceMigration;
import com.elitecore.sm.migration.service.MigrationTrackService;
import com.elitecore.sm.migration.service.NatflowBinaryCollectionServiceMigration;
import com.elitecore.sm.migration.service.NetflowCollectionServiceMigration;
import com.elitecore.sm.migration.service.ParsingServiceMigration;
import com.elitecore.sm.migration.service.SNMPMigration;
import com.elitecore.sm.migration.service.SyslogCollectionServiceMigration;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.dao.ServiceTypeDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServerType;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "migrationService")
public class MigrationServiceImpl implements MigrationService {

	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);

	@Autowired
	private CollectionServiceMigration collectionServiceMigration;

	@Autowired
	private NetflowCollectionServiceMigration netflowCollectionServiceMigration;

	@Autowired
	private ServiceTypeDao servicetypeDao;

	@Autowired
	private ServerDao serverDao;

	@Autowired
	private DataSourceDao dataSourceDao;

	@Autowired
	private DataSourceMigration dataSourceMigration;

	@Autowired
	private DistributionServiceMigration distributionMigrationService;

	@Autowired
	private ParsingServiceMigration parsingMigrationService;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private AgentMigrationService agentMigrationService;

	@Autowired
	private SnmpService snmpService;

	@Autowired(required = true)
	@Qualifier(value = "serverService")
	private ServerService serverService;

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private MigrationUtil migrationUtil;

	private String migrationPrefix;

	@Autowired
	private ServerInstanceDao serverInstanceDao;

	@Autowired
	SystemParameterService systemParamService;

	@Autowired
	NatflowBinaryCollectionServiceMigration natflowBinaryCollectionServiceMigration;

	@Autowired
	private SyslogCollectionServiceMigration syslogCollectionServiceMigration;

	@Autowired
	MigrationTrackService migrationTrackService;

	@Autowired
	@Qualifier(value = "eliteUtilsQualifier")
	protected EliteUtils eliteUtils;

	@Autowired
	private IPLogParsingServiceMigration ipLogParsingServiceMigration;

	@Autowired
	private MigrationTrackDao migrationTrackDao;

	@Autowired
	private SNMPMigration snmpMigration;

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject migrateMediationServerConfig(int serverTypeId, String ipAddress, int instancePort, String scriptFileName, int staffId,
			String migrationPrefix, List<String> serviceNameList) throws MigrationSMException {

		ResponseObject responseObject;

		this.migrationPrefix = migrationPrefix;

		responseObject = serverInstanceService.getServerInstanceByIPAndPort(ipAddress, instancePort);

		if (!responseObject.isSuccess()) {
			logger.debug("Server instance not found so going to read server configuration from provided folder.");
			responseObject = migrationUtil.getFileContentFromMap(EngineConstants.MEDIATION_SERVER);
			if (responseObject.isSuccess()) {
				byte[] fileContent = (byte[]) responseObject.getObject();
				responseObject = migrationUtil.getAndValidateMappingEntityObj(MEDIATION_SERVER_XML);
				if (responseObject.isSuccess()) {
					MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
					responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, "", entityMapping.getXmlName());

					if (responseObject.isSuccess()) {
						logger.info("Server instance unmarshalling done successfull!");

						Map<String, Object> returnObjects = (Map<String, Object>) responseObject.getObject();
						ServerInstance serverInstance = (ServerInstance) returnObjects.get(MigrationConstants.MAP_SM_CLASS_KEY);

						if (serverInstance != null) {
							Server server;
							responseObject = serverService.getServerByIpAndType(ipAddress, serverTypeId);
							if (responseObject.isSuccess()) {

								logger.info("Server details found successfully!");
								server = (Server) responseObject.getObject();
								serverInstance.setServer(server);
								serverInstance.setPort(instancePort);
								serverInstance.setScriptName(scriptFileName);
								serverInstance.setId(0);
								serverInstance.setName(migrationUtil.getRandomName(this.migrationPrefix + "_instance"));
								migrationUtil.setCurrentDateAndStaffId(serverInstance, staffId);
								// Getting data-source configuration
								responseObject = dataSourceMigration.getDataSourceUnmarshalObject(serverInstance);
								if (responseObject.isSuccess()) {

									DataSourceConfig dataSource = (DataSourceConfig) responseObject.getObject();
									serverInstance.setServerManagerDatasourceConfig(dataSource);
									serverInstance.setIploggerDatasourceConfig(dataSource);
									List<Service> saveListService = new ArrayList<>();
									Service service;
									for (String serviceName : serviceNameList) {
										String[] servicesNameTypeArray = serviceName.split("-");
										if (servicesNameTypeArray.length == 2) {
											service = new Service();
											service.setServInstanceId(servicesNameTypeArray[1]);

											List<ServiceType> serviceTypeList = servicetypeDao.getEnableServiceTypeList();
											for (ServiceType serviceTypeObj : serviceTypeList) {
												if (serviceTypeObj.getAlias().equalsIgnoreCase(servicesNameTypeArray[0])) {
													service.setSvctype(serviceTypeObj);
													service.setName(migrationUtil.getRandomName(serviceTypeObj.getType()));
												}
											}
											saveListService.add(service);

										}
									}

									serverInstance.setServices(saveListService);

									responseObject = validateAndConverServiceObject(serverInstance, this.migrationPrefix, staffId);
									if (responseObject.isSuccess()) {
										logger.debug("validation being called for SI " + serverInstance);
										responseObject = serverInstanceService.validateMigrationDetails(serverInstance);

										// List<Service> saveListService=new
										// ArrayList<>();

										if (responseObject.isSuccess()) {
											/*
											 * Service service; for(String
											 * serviceName:serviceNameList){
											 * String[] servicesNameTypeArray =
											 * serviceName.split("-");
											 * if(servicesNameTypeArray
											 * .length==2){ service=new
											 * Service();
											 * service.setServInstanceId
											 * (servicesNameTypeArray[1]);
											 * 
											 * List<ServiceType>
											 * serviceTypeList=servicetypeDao.
											 * getEnableServiceTypeList();
											 * for(ServiceType
											 * serviceTypeObj:serviceTypeList){
											 * if(serviceTypeObj.getAlias().
											 * equalsIgnoreCase
											 * (servicesNameTypeArray[0])){
											 * service
											 * .setSvctype(serviceTypeObj);
											 * service
											 * .setName(migrationUtil.getRandomName
											 * (serviceTypeObj.getType())); } }
											 * saveListService.add(service);
											 * 
											 * 
											 * } }
											 */

											saveListService = serverInstance.getServices();
											serverInstance.setServices(null);
											responseObject = serverInstanceService.addMigratedServerInstance(serverInstance);

											if (responseObject.isSuccess() && responseObject.getObject() != null) {
												ServerInstance srvInsFromDB = (ServerInstance) responseObject.getObject();
												serverInstance = srvInsFromDB;

												logger.debug("Si is persisted now its id is " + serverInstance.getId());

												logger.debug("adding service list to to server instance.");
												// loop to save the services
												for (int i = 0; i < saveListService.size(); i++) {
													Service tempService = saveListService.get(i);
													tempService.setServerInstance(serverInstance);
													servicesService.updateMigService(tempService);
													String serviceKey = tempService.getSvctype().getAlias() + "-" + tempService.getServInstanceId()
															+ "-" + serverInstance.getId();
													MapCache.addConfigObject(serviceKey, tempService);

												}

												responseObject = snmpMigration.getSnmpUnmarshalObject("", migrationPrefix);
												if (responseObject.isSuccess()) {
													if (!ResponseCode.SNMP_NOT_FOUND.equals(responseObject.getResponseCode())) {
														SNMPServerConfig snmpServer = (SNMPServerConfig) responseObject.getObject();
														snmpServer.setServerInstance(serverInstance);
														snmpServer.setType(SNMPServerType.Self);
														snmpService.updateMigSnmpServer(snmpServer);
													}
													responseObject = snmpMigration.getSnmpListnerUnmarshalObject("", migrationPrefix, serverInstance);
													if (!responseObject.isSuccess()) {
														throw new MigrationSMException(responseObject,
																"Failed to validate and convert SNMP Listner entity. Please check logs for more details.");
													}

												} else {
													throw new MigrationSMException(responseObject,
															"Failed to validate and convert SNMP Server entity. Please check logs for more details.");
												}

												responseObject = agentMigrationService.getAgentObject(serverInstance);
												if (!responseObject.isSuccess()) {
													throw new MigrationSMException(responseObject,
															"Failed to migration for agent configuration. Please check logs for more details.");
												}

											} else {
												throw new MigrationSMException(responseObject,
														"Failed to create server instance. Please check logs for more details.");
											}
										} else {
											throw new MigrationSMException(responseObject,
													"Bussiness validation failed. Please check logs for more details.");
										}
									} else {
										throw new MigrationSMException(responseObject,
												"Failed to validate and convert service entity. Please check logs for more details.");
									}
								}
							}
						} else {
							logger.debug("Server instance found null!");
							responseObject.setSuccess(false);
							responseObject.setObject(null);
							responseObject.setResponseCode(ResponseCode.MIGRATION_INSTANCE_NULL);
						}
					}
				}
			}
		} else {
			logger.debug("Server instance already exist for ip " + ipAddress + "  and port  " + instancePort);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ALREADY_AVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Method will iterate services details and dependents for a give server
	 * instance.
	 * 
	 * @param serverInstance
	 * @param migrationPrefix
	 * @param staffId
	 * @return
	 * @throws MigrationSMException
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject validateAndConverServiceObject(ServerInstance serverInstance, String migrationPrefix, int staffId)
			throws MigrationSMException {
		ResponseObject responseObject = new ResponseObject();
		List<Service> serviceList = serverInstance.getServices();

		boolean migrationFailed = false;
		if (serviceList != null && !serviceList.isEmpty()) {

			for (int i = 0; i < serviceList.size(); i++) {
				Service service = serviceList.get(i);

				responseObject = iterateAndSetServiceDetails(serviceList, i, service, serverInstance, migrationPrefix, staffId);

				if (!responseObject.isSuccess()) {
					logger.info("Fail to iterate service details for " + service.getName());
					responseObject.setSuccess(false);
					migrationFailed = true;
				}

				if (migrationFailed) {
					break;
				} else {
					logger.info("Service Iteration done successfully.");
				}
			}
		} else {
			responseObject.setSuccess(true);
			logger.info("No service configured for the server instance " + serverInstance.getId());
		}
		return responseObject;
	}

	/**
	 * Method will set all services object and its dependents to parent entity
	 * based on service type.
	 * 
	 * @param service
	 * @return
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject iterateAndSetServiceDetails(List<Service> serviceInstanceList, int position, Service service,
			ServerInstance serverInstance, String migrationPrefix, int staffId) throws MigrationSMException {

		ResponseObject responseObject = new ResponseObject();
		String serviceType = service.getSvctype().getAlias();

		switch (serviceType) {

		case EngineConstants.COLLECTION_SERVICE:
			responseObject = collectionServiceMigration.iterateAndSetCollectionServiceDetails(serviceInstanceList, position, service, "",
					serverInstance, migrationPrefix);
			break;
		case EngineConstants.NATFLOW_COLLECTION_SERVICE:
			responseObject = netflowCollectionServiceMigration.iterateAndSetNetflowCollectionServiceDetails(serviceInstanceList, position, service,
					"", serverInstance, migrationPrefix);
			break;
		case EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE:
			responseObject = natflowBinaryCollectionServiceMigration.iterateAndSetNetflowBinaryCollectionServiceDetails(serviceInstanceList,
					position, service, "", serverInstance, migrationPrefix);
			break;
		case EngineConstants.SYSLOG_COLLECTION_SERVICE:
			responseObject = syslogCollectionServiceMigration.iterateAndSetSyslogCollectionServiceDetails(serviceInstanceList, position, service, "",
					serverInstance, migrationPrefix);
			break;
		case EngineConstants.IPLOG_PARSING_SERVICE:
			responseObject = ipLogParsingServiceMigration.getIPLogParsingServiceAndDependents(serviceInstanceList, position, service, "",
					serverInstance, staffId, migrationPrefix);
			break;
		case EngineConstants.PARSING_SERVICE:
			responseObject = parsingMigrationService.getParsingServiceAndDependents(serviceInstanceList, position, service, "", serverInstance,
					staffId, migrationPrefix);
			break;
		case EngineConstants.DISTRIBUTION_SERVICE:
			responseObject = distributionMigrationService.getDistributionServiceAndDependents(serviceInstanceList, position, service, serverInstance,
					staffId, migrationPrefix);
			break;
		case EngineConstants.GTPPRIME_COLLECTION_SERVICE:
			responseObject.setSuccess(true);
			break;
		case EngineConstants.PROCESSING_SERVICE:
			responseObject.setSuccess(true);
			break;
		default:
			break;

		}

		return responseObject;
	}

	/**
	 * Validate server details such as utility running or not on IP
	 * 
	 * @param ipaddress
	 * @param serverTypeId
	 * @param utilityPort
	 * @return ResponseObject
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject checkServerAvailability(String ipaddress, int serverTypeId, int utilityPort) {
		ResponseObject responseObject = new ResponseObject();

		// Check if utility running or not
		RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(ipaddress, utilityPort, 3, 5000, 20);
		String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
		if (crestelHome != null) {

			List<Server> serverList = serverDao.getServerListByIpAndType(ipaddress, serverTypeId);
			if (serverList != null && !serverList.isEmpty() && serverList.get(0) != null) {
				responseObject.setObject(serverList.get(0));
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_VALIDATION_SUCCESS);

		} else if (serverMgmtRemoteJMXCall.getErrorMessage() != null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
			responseObject.setArgs(new Object[] { ipaddress, String.valueOf(utilityPort) });

		}

		return responseObject;

	}

	/**
	 * Validate ServerInstance details such as port running or not, version
	 * check, scriptFile present or not and SI with same IP & port already
	 * present in DB etc.
	 * 
	 * @param ipaddress
	 * @param serverTypeId
	 * @param utilityPort
	 * @param port
	 * @param scriptFileName
	 * @return ResponseObject
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject checkServerInstanceDetails(String ipaddress, int serverTypeId, int utilityPort, int port, String scriptFileName) {
		ResponseObject responseObject = new ResponseObject();
		try {
			// Check if utility running or not
			RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(ipaddress, utilityPort, 3, 5000, 20);
			
			
			String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
			if (crestelHome != null) {
				RemoteJMXHelper serverMgmtRemoteJMXVersionCall = new RemoteJMXHelper(ipaddress, port, 3, 5000, 20);
				if (serverMgmtRemoteJMXVersionCall.checkPortRunningOfServerInstance()) {
					if (serverMgmtRemoteJMXCall.checkScriptExists(scriptFileName)) {

						String version = serverMgmtRemoteJMXVersionCall.versionInformation();
						logger.debug("versionInformation inside checkServerInstanceDetails" + version);
						// if ("6.2.8".equalsIgnoreCase(version)) {
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_VALIDATION_SUCCESS);
						/*
						 * } else { responseObject.setSuccess(false);
						 * responseObject.setResponseCode(ResponseCode.
						 * SERVER_VERSION_NOT_SUPPORTED);
						 * responseObject.setArgs(new Object[] { version }); }
						 */
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SCRIPT_FILE_MISSING);
						responseObject.setArgs(new Object[] { scriptFileName, ipaddress });
					}

				}

				else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_NOT_RUNNING);
					responseObject.setArgs(new Object[] { String.valueOf(port), ipaddress });
				}
				
				List<Server> serverList = serverDao.getServerListByIpAndType(ipaddress, serverTypeId);
				if (serverList != null && !serverList.isEmpty()
						&& serverList.get(0) != null) {

					List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceByServerIdAndPort(serverList.get(0).getId(), port);
					if (serverInstanceList != null && !serverInstanceList.isEmpty()
							&& serverInstanceList.get(0) != null) {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_ALREADY_EXISTS_SAME_PORT);
						responseObject.setArgs(new Object[] { String.valueOf(port),
								serverInstanceList.get(0).getName() });
					}
				}

			} else if (serverMgmtRemoteJMXCall.getErrorMessage() != null) {

				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
				responseObject.setArgs(new Object[] { ipaddress, String.valueOf(utilityPort) });
			}
			return responseObject;
		} catch (SMException e) {
			logger.error("SMException in checkServerInstanceDetails", e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.MIGRATION_SERVERINSTANCE_CHECK_FAIL);
			return responseObject;
		}
	}

	@Override
	@Transactional(rollbackFor = { MigrationSMException.class, SMException.class, Exception.class })
	public ResponseObject readServerConfiguration(String ipAddress, String migrationPrefix, int serverType, int instancePort, int staffId,
			String scriptFileName, String servicesList) throws MigrationSMException {

		ResponseObject responseObject = new ResponseObject();

		logger.debug("reading server configuration for engine api for ipaddress :  " + ipAddress + " and  port " + instancePort);
		RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(ipAddress, instancePort, BaseConstants.SERVER_MGMT_TRY_TO_CONNECT,
				BaseConstants.SERVER_CONNECTION_INTERVAL, BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
		Map<String, byte[]> xmlContetMap = new HashMap<>();

		

		CrestelNetServerData eliteNetServerData = remoteJMXHelper.readServerConfiguration();
		
		if (eliteNetServerData != null) {
			List<CrestelNetConfigurationData> netConfigurationList = eliteNetServerData.getNetConfigurationList();
			logger.info("adding mediation server and its dependents file data to map");
			if (netConfigurationList != null && !netConfigurationList.isEmpty()) {

				for (CrestelNetConfigurationData crestelNetConfigurationData : netConfigurationList) {
					if (!StringUtils.isEmpty(crestelNetConfigurationData.getNetConfigurationKey())) {
						xmlContetMap.put(crestelNetConfigurationData.getNetConfigurationKey(), crestelNetConfigurationData.getNetConfigurationData());
					}
				}
			}

			String[] servicesArray = servicesList.split(",");
			List<String> serviceNameList = Arrays.asList(servicesArray);

		
			List<CrestelNetServiceData> netServiceList = new ArrayList<>();
			for (String serviceName : serviceNameList) {
				String[] servicesNameTypeArray = serviceName.split("-");
				if (servicesNameTypeArray.length == 2) {
					CrestelNetServiceData crestelServiceNetData = remoteJMXHelper.readServiceConfiguration(servicesNameTypeArray[0],
							servicesNameTypeArray[1], "6.2.8");
					if (crestelServiceNetData != null) {
						netServiceList.add(crestelServiceNetData);
					}
					
				}
			}

			logger.info("adding Service details and parser plugin details to map.");

			if (netServiceList != null && !netServiceList.isEmpty()) {

				for (CrestelNetServiceData crestelNetServiceData : netServiceList) {
					for (CrestelNetConfigurationData crestelNetConfigurationData : crestelNetServiceData.getNetConfigurationList()) {

						String entityType = crestelNetConfigurationData.getNetConfigurationKey();
						if (!StringUtils.isEmpty(crestelNetConfigurationData.getNetConfigurationKey())
								&& (EngineConstants.ASCII_PARSING_PLUGIN.equalsIgnoreCase(entityType))
								|| EngineConstants.REGEX_PARSING_PLUGIN.equalsIgnoreCase(entityType)
								|| EngineConstants.NATFLOW_PARSING_PLUGIN.equalsIgnoreCase(entityType)) { // Add
																											// or
																											// condition
																											// for
																											// more
																											// plug-in
							xmlContetMap.put(crestelNetConfigurationData.getNetConfigurationKey(),
									crestelNetConfigurationData.getNetConfigurationData());
						} else if (!StringUtils.isEmpty(crestelNetConfigurationData.getNetConfigurationKey())
								&& (EngineConstants.NATFLOW_COLLECTION_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.COLLECTION_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.SYSLOG_COLLECTION_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.IPLOG_PARSING_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.PARSING_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(entityType)
										|| EngineConstants.GTPPRIME_COLLECTION_SERVICE.equalsIgnoreCase(entityType) || EngineConstants.PROCESSING_SERVICE
											.equalsIgnoreCase(entityType))) {

							xmlContetMap.put(crestelNetServiceData.getNetServiceId(), crestelNetConfigurationData.getNetConfigurationData());
						}
					}

					logger.info("adding driver and composer plugin details to map.");
					List<CrestelNetDriverData> netDriverList = crestelNetServiceData.getNetDriverList();
					if (netDriverList != null && !netDriverList.isEmpty()) {
						for (CrestelNetDriverData crestelNetDriverData : netDriverList) {
							for (CrestelNetConfigurationData crestelNetConfigurationData : crestelNetDriverData.getNetConfigurationList()) {
								String entityType = crestelNetConfigurationData.getNetConfigurationKey();

								if (!StringUtils.isEmpty(crestelNetConfigurationData.getNetConfigurationKey())
										&& (EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(entityType))) { // Add
																													// or
																													// condition
																													// for
																													// more
																													// plug-in
																													// developed
																													// plug-in
																													// for
																													// migration
									xmlContetMap.put(crestelNetConfigurationData.getNetConfigurationKey(),
											crestelNetConfigurationData.getNetConfigurationData());
								} else if (!StringUtils.isEmpty(crestelNetConfigurationData.getNetConfigurationKey())
										&& (EngineConstants.FTP_COLLECTION_DRIVER.equalsIgnoreCase(entityType)
												|| EngineConstants.SFTP_COLLECTION_DRIVER.equalsIgnoreCase(entityType)
												|| EngineConstants.LOCAL_COLLECTION_DRIVER.equalsIgnoreCase(entityType)
												|| EngineConstants.FTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(entityType)
												|| EngineConstants.SFTP_DISTRIBUTION_DRIVER.equalsIgnoreCase(entityType) || EngineConstants.LOCAL_DISTRIBUTION_DRIVER
													.equalsIgnoreCase(entityType))) {

									xmlContetMap.put(crestelNetDriverData.getNetDriverId(), crestelNetConfigurationData.getNetConfigurationData());
								}
							}
						}
					}
				}
			}

			if (xmlContetMap != null && !xmlContetMap.isEmpty()) {
				logger.info("instance file details added successfully to map now setting map object to migration util.");
				migrationUtil.setEntityFileContent(xmlContetMap);
				responseObject = migrateMediationServerConfig(serverType, ipAddress, instancePort, scriptFileName, staffId, migrationPrefix,
						serviceNameList);
			} else {
				logger.info("Failed to add server and its dependents file data to map.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.MIGRATION_FAIL);
			}
		} else {
			logger.info("Unable to connect ipaddress  " + ipAddress + " port " + instancePort);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_NOT_RUNNING);
			responseObject.setArgs(new Object[] { String.valueOf(instancePort), ipAddress });

			throw new MigrationSMException(responseObject, "Server instance not running for " + ipAddress + " and " + instancePort
					+ "  please start instance.");
		}

		return responseObject;
	}
	
	/**
	 * Fetch all Services List with running services as well
	 * @param migrationTrackDetails
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	public ResponseObject getAllServicesList(MigrationTrackDetails migrationTrackDetails) {

		Server server = serverDao.findByPrimaryKey(Server.class, migrationTrackDetails.getServer().getId());
		migrationTrackDetails.setServer(server);
		ResponseObject responseObject = checkServerInstanceDetails(migrationTrackDetails.getServer().getIpAddress(), migrationTrackDetails
				.getServer().getServerType().getId(), migrationTrackDetails.getServer().getUtilityPort(),
				migrationTrackDetails.getServerInstancePort(), migrationTrackDetails.getServerInstanceScriptName());
		
		if (responseObject.isSuccess()) {	
			
			Server serverObj = serverDao.findByPrimaryKey(Server.class, migrationTrackDetails.getServer().getId());
			if (serverObj != null) {
				logger.debug("reading server configuration for engine api for ipaddress :  " + serverObj.getIpAddress() + " and port "
						+ migrationTrackDetails.getServerInstancePort());
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverObj.getIpAddress(), serverObj.getUtilityPort(),
						BaseConstants.SERVER_MGMT_TRY_TO_CONNECT, BaseConstants.SERVER_CONNECTION_INTERVAL,
						BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
				Map<String, ArrayList<String>> servicesMap;

				List<String> serviceTypeNameList = new ArrayList<>();
				List<ServiceType> serviceTypeList = servicetypeDao.getEnableServiceTypeList();
				for (ServiceType serviceType : serviceTypeList) {
					serviceTypeNameList.add(serviceType.getType().replaceAll("\\s+", "").toLowerCase());
				}
				servicesMap = remoteJMXHelper.getServicesListByPort(migrationTrackDetails.getServerInstancePort(), serviceTypeNameList);
				logger.debug("servicesMap::" + servicesMap);
				List<String> runningServiceInstances = fetchRunningServicesForPort(serverObj.getIpAddress(),
						migrationTrackDetails.getServerInstancePort());
				JSONObject finalListOfServices = getFinalListOfServices(serviceTypeList, servicesMap, runningServiceInstances);
				responseObject.setObject(finalListOfServices);
				responseObject.setSuccess(true);
			}
		
		}
		return responseObject;
	}
	
	/**
	 * To Fetch all services List and Running Services List for specific port and ip 
	 * @param serviceTypeList
	 * @param allServiceInstances
	 * @param runningServiceInstances
	 * @return JSONObject
	 */
	public JSONObject getFinalListOfServices(List<ServiceType> serviceTypeList, Map<String, ArrayList<String>> allServiceInstances,
			List<String> runningServiceInstances) {
		JSONObject finalServiceListJSON = new JSONObject();
		Map<String, List<String>> finalServicesMap = new HashMap<>();
		for (ServiceType serviceType : serviceTypeList) {

			List<String> serviceInstanceListFinal = new ArrayList<>();
			List<String> serviceInstanceList = allServiceInstances.get(serviceType.getType().replaceAll("\\s+", "").toLowerCase());
			for (String serviceInstanceId : serviceInstanceList) {
				serviceInstanceListFinal.add(serviceType.getAlias() + "-" + serviceInstanceId);
			}
			finalServicesMap.put(serviceType.getType(), serviceInstanceListFinal);
		}
		finalServiceListJSON.put("allServices", finalServicesMap);
		finalServiceListJSON.put("svcTypeList", serviceTypeList);
		finalServiceListJSON.put("runningSvcList", runningServiceInstances);
		return finalServiceListJSON;

	}
	
	/**
	 * Fetch Running Services List using engine api
	 * @param ipAddress
	 * @param instancePort
	 * @return List<String>
	 */
	public List<String> fetchRunningServicesForPort(String ipAddress, int instancePort) {
		List<String> runningServiceList = new ArrayList<>();
		RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(ipAddress, instancePort, BaseConstants.SERVER_MGMT_TRY_TO_CONNECT,
				BaseConstants.SERVER_CONNECTION_INTERVAL, BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
		CrestelNetServerDetails crestelNetData = remoteJMXHelper.readServerDetails();
		List<LiveServiceSummary> liveServiceList = crestelNetData.getServiceSummaryList();
		for (LiveServiceSummary liveService : liveServiceList) {
			runningServiceList.add(liveService.getInstanceId());
		}
		return runningServiceList;
	}
	
	/**
	 * On click of Update, fetch service list to show on UI
	 * @param serverId
	 * @param port
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	public ResponseObject getAllServicesListForUpdate(int serverId, int port) {

		Server server = serverDao.findByPrimaryKey(Server.class, serverId);

		ResponseObject responseObject = new ResponseObject();
		if (server != null) {
			try {
				// Check if utility running or not
				RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),
						BaseConstants.SERVER_MGMT_TRY_TO_CONNECT, BaseConstants.SERVER_CONNECTION_INTERVAL,
						BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
				String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
				if (crestelHome != null) {
					RemoteJMXHelper serverMgmtRemoteJMXVersionCall = new RemoteJMXHelper(server.getIpAddress(), port,
							BaseConstants.SERVER_MGMT_TRY_TO_CONNECT, BaseConstants.SERVER_CONNECTION_INTERVAL,
							BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
					if (serverMgmtRemoteJMXVersionCall.checkPortRunningOfServerInstance()) {

						logger.debug("reading service list and running servicelist for ipaddress :  " + server.getIpAddress() + " and port " + port);
						RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),
								BaseConstants.SERVER_MGMT_TRY_TO_CONNECT, BaseConstants.SERVER_CONNECTION_INTERVAL,
								BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT);
						Map<String, ArrayList<String>> servicesMap;

						List<String> serviceTypeNameList = new ArrayList<>();
						List<ServiceType> serviceTypeList = servicetypeDao.getEnableServiceTypeList();
						for (ServiceType serviceType : serviceTypeList) {
							serviceTypeNameList.add(serviceType.getType().replaceAll("\\s+", "").toLowerCase());
						}
						servicesMap = remoteJMXHelper.getServicesListByPort(port, serviceTypeNameList);
						logger.debug("servicesMap::" + servicesMap);
						List<String> runningServiceInstances = fetchRunningServicesForPort(server.getIpAddress(), port);
						JSONObject finalListOfServices = getFinalListOfServices(serviceTypeList, servicesMap, runningServiceInstances);
						responseObject.setObject(finalListOfServices);
						responseObject.setSuccess(true);

					}

					else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_NOT_RUNNING);
						responseObject.setArgs(new Object[] { String.valueOf(port), server.getIpAddress() });
					}

				} else if (serverMgmtRemoteJMXCall.getErrorMessage() != null) {

					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
					responseObject.setArgs(new Object[] { server.getIpAddress(), String.valueOf(server.getUtilityPort()) });
				}
				return responseObject;
			} catch (SMException e) {
				logger.error("SMException in checkServerInstanceDetails", e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.MIGRATION_SERVERINSTANCE_CHECK_FAIL);
				return responseObject;
			}

		} else {

			// Server Not found in db
			logger.error("Server not found in db");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.MIGRATION_SERVERINSTANCE_CHECK_FAIL);
			return responseObject;
		}

	}

}
