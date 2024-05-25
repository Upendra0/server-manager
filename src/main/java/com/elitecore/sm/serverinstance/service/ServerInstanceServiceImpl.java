package com.elitecore.sm.serverinstance.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.transform.TransformerException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.proxy.HibernateProxy;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.elitecore.core.server.hazelcast.HazelcastCacheConstants;
import com.elitecore.core.server.hazelcast.HazelcastUtility;
import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServiceData;
import com.elitecore.core.util.mbean.data.live.CrestelNetServerDetails;
import com.elitecore.sm.agent.dao.AgentDao;
import com.elitecore.sm.agent.dao.AgentTypeDao;
import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.configmanager.dao.VersionConfigDao;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.datasource.dao.DataSourceDao;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.datasource.service.DataSourceService;
import com.elitecore.sm.iam.dao.StaffDAO;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.kafka.datasource.dao.KafkaDataSourceDao;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceObjectWrapper;
import com.elitecore.sm.license.dao.LicenseDao;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.model.LicenseTypeEnum;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.policy.dao.IDatabaseQueryActionDao;
import com.elitecore.sm.policy.dao.IDatabaseQueryConditionDao;
import com.elitecore.sm.policy.dao.IDatabaseQueryDao;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.dao.IPolicyGroupDao;
import com.elitecore.sm.policy.dao.IPolicyRuleDao;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.policy.service.IPolicyImportExportService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.dao.AutoConfigServerInstanceDao;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.AutoConfigServerInstance;
import com.elitecore.sm.serverinstance.model.LogsDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.validator.ServerInstanceValidator;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Http2CollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceCategoryEnum;
import com.elitecore.sm.services.model.ServiceTypeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.service.SynchronizationProcessor;
import com.elitecore.sm.snmp.dao.SNMPAlertWrapperDao;
import com.elitecore.sm.snmp.dao.SNMPServiceThresholdDao;
import com.elitecore.sm.snmp.dao.SnmpDao;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;
import com.elitecore.sm.snmp.model.SnmpAlertCustomObject;
import com.elitecore.sm.snmp.model.SnmpAlertCustomObjectWrapper;
import com.elitecore.sm.snmp.model.SnmpServiceThresholdCustom;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.snmp.validator.SnmpValidator;
import com.elitecore.sm.systemparam.dao.SystemParamDataDao;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;

/**
 * 
 * @author vishal.lakhyani
 *
 */

@org.springframework.stereotype.Service(value = "serverInstanceService")
public class ServerInstanceServiceImpl implements ServerInstanceService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	SynchronizationProcessor syncProcessor = new SynchronizationProcessor();

	@Autowired
	private IDatabaseQueryService databaseQueryService;

	@Autowired
	private IDatabaseQueryConditionDao databaseQueryConditionDao;

	@Autowired
	private IDatabaseQueryActionDao databaseQueryActionDao;

	@Autowired
	private IDatabaseQueryDao databaseQueryDao;

	@Autowired
	private ServerDao serverDao;

	@Autowired
	private SnmpValidator validator;

	@Autowired
	@Qualifier(value = "serverInstanceDao")
	private ServerInstanceDao serverInstanceDao;

	@Autowired(required = true)
	@Qualifier(value = "dataSourceService")
	private DataSourceService dataSourceService;

	@Autowired
	private ServicesDao serviceDao;

	@Autowired(required = true)
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;

	@Autowired(required = true)
	@Qualifier(value = "serverService")
	private ServerService serverService;

	@Autowired
	@Qualifier(value = "AgentDao")
	private AgentDao agentDao;

	@Autowired(required = true)
	@Qualifier(value = "dataSourceDao")
	private DataSourceDao dataSourceDao;

	@Autowired
	private ServerInstanceValidator instanceValidator;

	@Autowired
	private SnmpDao snmpDao;

	@Autowired
	SNMPAlertWrapperDao snmpAlertWrapperDao;

	@Autowired
	SNMPServiceThresholdDao snmpServiceThresholdDao;

	@Autowired
	SnmpService snmpService;

	@Autowired
	LicenseService licenseService;

	@Autowired
	LicenseDao licenseDao;

	@Autowired
	AgentService agentService;

	@Autowired
	SystemParameterService systemParamService;

	@Autowired
	IPolicyService policyService;

	/** The policy dao. */
	@Autowired
	IPolicyDao policyDao;

	/** The policy action dao. */
	@Autowired
	private IPolicyActionDao policyActionDao;

	/** The policy condition dao. */
	@Autowired
	private IPolicyConditionDao policyConditionDao;

	/** The policy group dao. */
	@Autowired
	IPolicyGroupDao policyGroupDao;

	/** The policy rule dao. */
	@Autowired
	IPolicyRuleDao policyRuleDao;

	@Autowired
	AgentTypeDao agentTypeDao;
	
	@Autowired
	ServerInstanceImportExportService serverInstanceImportExportService;

	@Autowired
	IPolicyImportExportService policyImportExportService;
	
	@Autowired
	VersionConfigDao versionConfigDao;
	
	@Autowired
	StaffDAO staffDao;
	
	@Autowired
	SystemParamDataDao systemParamDataDao;
	
	@Autowired
	AutoConfigServerInstanceDao autoConfigServerInstanceDao;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	KafkaDataSourceDao kafkaDataSourceDao;


	@Autowired
	@Qualifier(value = "licenseUtilityQualifier")
	LicenseUtility licenseUtility;
	

	@Autowired
	ServletContext servletcontext;
	
	/**
	 * Check server instance is unique across ipaddress
	 */
	@Transactional(readOnly = true)
	@Override
	public boolean isServerInstanceUnique(ServerInstance serverInstance) {

		logger.info("Is the ServerInstance unique");

		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceByName(serverInstance.getName());

		if (instanceList != null && !instanceList.isEmpty()) {
			logger.info("ServerInstance name is NOT unique");
			return false;
		} else {
			logger.info("ServerInstance name is unique");
			return true;
		}

	}

	@Transactional(readOnly = true)
	@Override
	public ServerInstance getServerInstanceByName(String name) {
		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceByName(name);

		if (instanceList != null && !instanceList.isEmpty()) {
			logger.info("ServerInstance name found in DB total result size is " + instanceList.size());
			return instanceList.get(0);

		}

		return null;
	}

	/**
	 * add server instance in database
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_SERVER_INSTANCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "")
	@Override
	public ResponseObject addServerInstanceInDB(ServerInstance serverInstance, Map<String, Object> mReqParam) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		try {
			List<Agent> newagentTypeList;
			RemoteJMXHelper jmxConnection = null;
			CrestelNetServerData engineServerData = null;
			Map<String, String> licenseDetails =null;
			if(!Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
				jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				engineServerData = jmxConnection.readServerConfiguration();
				licenseDetails = jmxConnection.licenseInfo();
			}
	
			if (engineServerData != null && licenseDetails != null || Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
				// Set default Log Details
				serverInstance.setLogsDetail((LogsDetail) SpringApplicationContext.getBean(LogsDetail.class));

				serverInstance.setCreatedDate(new Date());
				serverInstance.setLastUpdatedDate(serverInstance.getCreatedDate());
				serverInstance.setLastUpdatedByStaffId(serverInstance.getCreatedByStaffId());
				serverInstance.setStatus(StateEnum.ACTIVE);

				serverInstanceDao.save(serverInstance);

				if (serverInstance.getId() != 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_SUCCESS);
					serverInstance.setServer(serverService.getServer(serverInstance.getServer().getId()));
					responseObject.setObject(serverInstance);

				//	addInstanceLicenseDetails(serverInstance, licenseDetails); // Adding
																				// server
																				// instance
																				// license
																				// details
																				// to
																				// to
																				// license
																				// table.

					// Add Agent
					List<AgentType> agentTypeList = agentTypeDao.getAllAgentType();

					if (agentTypeList != null && !agentTypeList.isEmpty()) {
						logger.debug("Agent List Found from DB is : " + agentTypeList.size());
						newagentTypeList = new ArrayList<>();
						for (AgentType agentType : agentTypeList) {
							Class<? extends Agent> agentName;

							logger.debug("inside addServerInstanceInDB : Agent class name:" + agentType.getAgentFullClassName());
							agentName = (Class<? extends Agent>) Class.forName(agentType.getAgentFullClassName());

							Agent agent = agentName.newInstance();

							agent.setServerInstance(serverInstance);
							agent.setAgentType(agentType);
							if ("FILE_RENAME_AGENT".equalsIgnoreCase(agentType.getAlias())) {
								agent.setStatus(StateEnum.INACTIVE);
							}

							agentDao.save(agent);

							if (agent.getId() > 0) {
								logger.debug("Agent created successfully");
								responseObject.setSuccess(true);
								newagentTypeList.add(agent);
							}
						}
						serverInstance.setAgentList(newagentTypeList);
					}
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_FAIL);
				}

			} else if (jmxConnection.getErrorMessage() != null && jmxConnection.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.JMX_API_FAIL);
			}
		} catch (Exception e) {
			throw new SMException(e);
		}
		return responseObject;
	}

	/**
	 * add server instance in database
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_SERVER_INSTANCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "")
	public ResponseObject addInactiveServerInstanceInDB(ServerInstance serverInstance) {
		ResponseObject responseObject = new ResponseObject();

		// Set default Log Details
		serverInstance.setLogsDetail((LogsDetail) SpringApplicationContext.getBean(LogsDetail.class));

		
		serverInstance.setServerManagerDatasourceConfig(null);
		serverInstance.setIploggerDatasourceConfig(null);

		serverInstance.setCreatedDate(new Date());
		serverInstance.setLastUpdatedDate(serverInstance.getCreatedDate());
		serverInstance.setLastUpdatedByStaffId(serverInstance.getCreatedByStaffId());
		serverInstance.setStatus(StateEnum.INACTIVE);

		serverInstanceDao.save(serverInstance);

		if (serverInstance.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_SUCCESS);
			serverInstance.setServer(serverService.getServer(serverInstance.getServer().getId()));
			responseObject.setObject(serverInstance);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_FAIL);
		}
		return responseObject;
	}

	/**
	 * Provides the Server Instance List from database based on Server Id
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ServerInstance> getServerInstanceList() {
		return serverInstanceDao.getServerInstanceList();
	}

	/**
	 * Updates the Server Instance in database.
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_ADVANCE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	@Override
	public ResponseObject updateServerInstance(ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstance != null) {
			serverInstance.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			serverInstance.setLastUpdatedDate(new Date());
			// serverInstance = markServerInstanceDirty(serverInstance)

			serverInstanceDao.merge(serverInstance);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SUCCESS);
			responseObject.setObject(serverInstance);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_FAIL);
		}

		return responseObject;
	}

	/**
	 * Check Server Instance name is unique in case of update
	 * 
	 * @param serveInstanceId
	 * @param serverInstanceName
	 * @return
	 */
	@Transactional
	public boolean isInstanceNameUniqueForUpdate(int serveInstanceId, String serverInstanceName) {
		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceByName(serverInstanceName);
		boolean isUnique = false;
		if (instanceList != null && !instanceList.isEmpty()) {

			for (ServerInstance serverInstance : instanceList) {
				// If ID is same , then it is same instance object
				if (serveInstanceId == (serverInstance.getId())) {
					isUnique = true;
				} else { // It is another instance object , but name is same
					isUnique = false;
				}
			}
		} else if (instanceList != null && instanceList.isEmpty()) {
			isUnique = true;
		}

		return isUnique;
	}

	/**
	 * 
	 * 
	 * server instance by ipaddress and port
	 * 
	 */
	@Override
	@Transactional
	public boolean isInstancePortUnique(int port, String ipaddress) {

		logger.info("return true or false ");

		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceByIpaddressAndPort(port, ipaddress);
		boolean isUnique;
		if (instanceList != null && !instanceList.isEmpty()) {
			isUnique = false;
		} else { //
			isUnique = true;
		}

		return isUnique;
	}

	@Override
	@Transactional
	public ResponseObject getServerInstanceByIPAndPort(String ipaddress, int port) {
		logger.debug("Fetching server instance for ipaddress " + ipaddress + " port " + port);

		ResponseObject responseObject = new ResponseObject();

		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceByIpaddressAndPort(port, ipaddress);
		if (instanceList != null && !instanceList.isEmpty()) {
			logger.info("instance found successfully for ipaddress " + ipaddress + " and port " + port);
			responseObject.setSuccess(true);
			responseObject.setObject(instanceList.get(0));
		} else {
			logger.info("Failed to get instance for ipaddress " + ipaddress + " and port " + port);
			responseObject.setSuccess(false);
			responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_GET_SERVER_INSTANCE);
		}

		return responseObject;
	}

	/**
	 * Fetch server instance for synchronization
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getServerInstanceDepedants(ServerInstance serverInstance) {

		logger.debug("Inside getServerInstanceDepedants() method");
		ResponseObject responseObject = new ResponseObject();

		List<PolicyCondition> policyConditionList = policyConditionDao.getPolicyConditionforServerInstance(serverInstance.getId());
		List<PolicyAction> policyActionList = policyActionDao.getPolicyActionforServerInstance(serverInstance.getId());
		List<PolicyRule> policyRuleList = policyRuleDao.getPolicyRuleforServerInstance(serverInstance.getId());
		List<PolicyGroup> policyGroupList = policyGroupDao.getPolicyGroupforServerInstance(serverInstance.getId());
		List<Policy> policyList = policyDao.getPolicyforServerInstance(serverInstance.getId());
		List<DatabaseQuery> databaseQueriesList = databaseQueryDao.getAllQueriesByServerId(serverInstance.getId());
		for (DatabaseQuery databaseQuery : databaseQueriesList) {
			databaseQuery.setDatabaseQueryActions(databaseQueryActionDao.getAllDatabaseQueryActionsByQueryId(databaseQuery.getId()));
			databaseQuery.setDatabaseQueryConditions(databaseQueryConditionDao.getAllDatabaseQueryConditionsByQueryId(databaseQuery.getId()));
		}
		List<Service> servicesList = servicesService.getServicesforServerInstance(serverInstance.getId());

		List<Agent> agentList = serverInstanceDao.getAgentListforServerInstance(serverInstance.getId());

		List<SNMPServerConfig> snmpServerList = snmpDao.getServerListByServerInstanceId(serverInstance.getId());

		List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstance.getId());
		List<SNMPServerConfig> tempsnmpClientList = new ArrayList<>();

		for (SNMPServerConfig snmpClient : snmpClientList) {
			logger.debug("Iterate for SNMP client:  " + snmpClient.getId());

			List<SNMPAlertWrapper> snmpWrapperList = snmpAlertWrapperDao.getWrapperListByClientId(snmpClient.getId());

			for (SNMPAlertWrapper alertWrapper : snmpWrapperList) {
				logger.debug("Iterate for SNMP Wrapper:  " + alertWrapper.getId());

				List<SNMPServiceThreshold> serviceThresholdList = snmpServiceThresholdDao.getThresholdListByWrapperId(alertWrapper.getId());

				alertWrapper.setServiceThreshold(serviceThresholdList);

			}
			snmpClient.setConfiguredAlerts(snmpWrapperList);
			tempsnmpClientList.add(snmpClient);

		}
		serverInstance.setDatabaseQueryList(databaseQueriesList);
		serverInstance.setPolicyConditionList(policyConditionList);
		serverInstance.setPolicyActionList(policyActionList);
		serverInstance.setPolicyRuleList(policyRuleList);
		serverInstance.setPolicyGroupList(policyGroupList);
		serverInstance.setPolicyList(policyList);
		serverInstance.setServices(servicesList);
		serverInstance.setAgentList(agentList);
		serverInstance.setSelfSNMPServerConfig(snmpServerList);
		serverInstance.setSnmpListeners(tempsnmpClientList);
		responseObject.setSuccess(true);
		responseObject.setObject(serverInstance);

		return responseObject;
	}

	/**
	 * Provides the Server Instance List from database based on Server Id
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ServerInstance> getServerInstanceByServerId(int serverId) {
		return serverInstanceDao.getServerInstanceByServerId(serverId);
	}

	/**
	 * Provides all the server and it's server instances
	 */
	@SuppressWarnings({ "rawtypes" })
	@Transactional(readOnly = true)
	@Override
	public Map<String, List> getAllServerAndItsInstance() {
		Map<String, List> responseObject = new HashMap<>();

		List<Server> serverList = serverDao.getServerList();
		if (serverList != null && !serverList.isEmpty()) {
			responseObject.put(BaseConstants.SERVER_LIST, serverList);

			List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceByServerId(0);
			responseObject.put(BaseConstants.SERVER_INSTANCE_LIST, serverInstanceList);
		}
		return responseObject;
	}

	/**
	 * Provide the total staff count based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalServerInstanceCount(String serverTypeId, String searchInstanceName, String searchHost, String searchServerName,
			String searchPort, String searchSyncStatus, String dsid) {
		Map<String, Object> conditionsAndAliases = serverInstanceDao.createCriteriaConditions(serverTypeId, searchInstanceName, searchHost,
				searchServerName, searchPort, searchSyncStatus, dsid);
		return serverInstanceDao.getQueryCount(ServerInstance.class, (List<Criterion>) conditionsAndAliases.get("conditions"),
				(HashMap<String, String>) conditionsAndAliases.get("aliases"));

	}

	/**
	 * Provides the List of Server Instance based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<ServerInstance> getPaginatedList(String serverTypeId, String searchInstanceName, String searchHost, String searchServerName,
			String searchPort, String searchSyncStatus, int startIndex, int limit, String sidx, String sord, String dsid) {

		Map<String, Object> conditionsAndAliases = serverInstanceDao.createCriteriaConditions(serverTypeId, searchInstanceName, searchHost,
				searchServerName, searchPort, searchSyncStatus, dsid);

		return serverInstanceDao.getPaginatedList(ServerInstance.class, (List<Criterion>) conditionsAndAliases.get("conditions"),
				(HashMap<String, String>) conditionsAndAliases.get("aliases"), startIndex, limit, sidx, sord);

	}

	/**
	 * Reset sync flag for service list and serverinstance
	 */
	@Override
	@Transactional
	public ResponseObject resetSyncFlagForServiceandSI(List<Service> serviceList, ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();
		int serviceSize = serviceList.size();
		for (int i = 0; i < serviceSize; i++) {
			Service service = serviceList.get(i);
			service = serviceDao.findByPrimaryKey(Service.class, service.getId());
			service.setSyncStatus(true);
			serviceDao.updateForResetSyncFlagOfService(service);
		}
		serverInstance.setSyncSIStatus(true);
		serverInstance.setSyncChildStatus(true);

		serverInstanceDao.updateForResetSyncFlagofServerInstance(serverInstance);
		responseObject.setSuccess(true);

		return responseObject;
	}

	/**
	 * Fetch server instance full hierchy for export instance
	 * 
	 * @throws IOException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.EXPORT_SERVER_INSTANCE_CONFIGURATION, actionType = BaseConstants.EXPORT_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject getServerInstanceFullHierarchy(int serverInstanceId, boolean isExportForDelete, String tempPathForExport)
			throws SMException {

		ResponseObject responseObject = new ResponseObject();

		String exportXmlPath = null;

		ServerInstance serverInstance;
		String serverInstanceName;

		if (isExportForDelete) {
			logger.info("Call for Export configuration before Delete");

			responseObject = systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.DELETE);

			if (responseObject.isSuccess()) {

				exportXmlPath = (String) responseObject.getObject();
				logger.info("Going to store export backup file at location :" + exportXmlPath);
				serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
				serverInstanceName = serverInstance.getName().replaceAll(" ", "_");//NOSONAR
				DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
				exportXmlPath = exportXmlPath + File.separator + serverInstanceName + "_" + serverInstance.getPort()
						+ BaseConstants.SUFFIX_FOR_DELETE + dateFormatter.format(new Date()) + ".xml";
			} else {
				logger.info("Export backup path is not valid:");
				return responseObject;
			}

		} else {
			logger.info("Call for Export configuration ");
			serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);

			if (serverInstance != null) {
				DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
				serverInstanceName = serverInstance.getName().replaceAll(" ", "_");//NOSONAR
				exportXmlPath = tempPathForExport + File.separator + serverInstanceName + "_" + serverInstance.getPort() + "_"
						+ dateFormatter.format(new Date()) + ".xml";
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.EXPORT_FAIL);
				responseObject.setObject(null);
			}
		}

		if (serverInstance != null) {

			responseObject = this.getServerInstanceJAXB(exportXmlPath, serverInstance.getId());

		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.EXPORT_FAIL);
			responseObject.setObject(null);
		}

		return responseObject;
	}
	
	/**
	 * Import server instance configuration
	 * 
	 * @param serverInstanceId
	 * @param importFile
	 * @param staffId
	 * @param importMode
	 * @param jaxbXmlPath
	 * @return ResponseObject
	 * @throws SMException
	 */
	@Override
	@Transactional(rollbackFor = SMException.class)
	@Auditable(auditActivity = AuditConstants.IMPORT_SERVER_INSTANCE_CONFIGURATION, actionType = BaseConstants.SM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject importServerInstanceConfig(int importserverInstanceId, File importFile, int staffId, int importMode, String jaxbXMLPath,
			boolean isCopy) throws SMException {
		ResponseObject responseObject;
		List<ImportValidationErrors> importValidationError = null;
		responseObject = unmarshalServerInstanceFromImportedFile(importFile, jaxbXMLPath);

		if (responseObject.isSuccess()) {

			logger.debug("UnMarshlling done successfully , Now validate imported file");

			ServerInstance exportedserverInstance = (ServerInstance) responseObject.getObject();
			
			/*if(exportedserverInstance.getServer() != null && exportedserverInstance.getServer().getServerType() != null) {
				String exportedServerType = exportedserverInstance.getServer().getServerType().getAlias();
				ServerInstance serverInstance = serverInstanceDao.getServerInstance(importserverInstanceId);
				if(serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getServerType() != null) {
					String originalServerType = serverInstance.getServer().getServerType().getAlias();
					if(!(exportedServerType != null && originalServerType != null && exportedServerType.equalsIgnoreCase(originalServerType))) {
						logger.debug("Validation fail for both server instance -> server type are not same");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_IMPORT_FAIL_SERVER_TYPE_DIFFERENT);
						return responseObject;
					} 
				}
			}*/

			if (!isCopy) {
				importValidationError = validateUnMarshallerInstance(exportedserverInstance,importserverInstanceId,importMode);
			}

			if (importValidationError != null && !importValidationError.isEmpty()) {

				logger.debug(" importServerInstanceConfig : Validation Fail for imported file");
				JSONArray finaljArray = new JSONArray();

				for (ImportValidationErrors errors : importValidationError) {

					JSONArray jArray = new JSONArray();
					jArray.put(errors.getModuleName());
					jArray.put(errors.getEntityName());
					jArray.put(errors.getPropertyName());
					jArray.put(errors.getPropertyValue());
					jArray.put(errors.getErrorMessage());
					finaljArray.put(jArray);
				}

				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.IMPORT_VALIDATION_FAIL);
				responseObject.setObject(finaljArray);

			} else {

				logger.debug("Validation Done Successfully for imported file , now import server instance");
				responseObject = importConfigUsingUnmarshllerInstance(importserverInstanceId, exportedserverInstance, staffId, importMode,
						jaxbXMLPath);

				if (responseObject.isSuccess()) {
					logger.debug("Import operation performed successfully");
					responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_SUCCESS);
				} else {
					logger.debug("Import operation Fail");
					if (!(responseObject.getResponseCode() == ResponseCode.SERVICE_RUNNING)) {
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_FAIL);
					}
				}
			}
		} else {
			logger.debug("UnMarshlling Fail For serverinstance");
		}
		return responseObject;
	}

	/**
	 * Provides the Server Instance from database.
	 */
	@Transactional(readOnly = true)
	@Override
	public ServerInstance getServerInstance(int id) {
		return serverInstanceDao.getServerInstance(id);
	}

	/**
	 * Updates the Server Instance Advanceconfig tab in database.
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_ADVANCE_CONFIGURATION, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "serverManagerDatasourceConfig,iploggerDatasourceConfig")
	@Override
	public ResponseObject updateInstanceAdvanceConfig(ServerInstance instance) {

		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverIns = serverInstanceDao.findByPrimaryKey(ServerInstance.class, instance.getId());
		if (serverIns != null) {
			if (isInstanceNameUniqueForUpdate(instance.getId(), instance.getName())) {
				logger.info("Name validation ="
						+ serverInstanceDao.getServerInstanceCount(instance.getName(), serverIns.getPort(), 0, serverIns.getServer().getIpAddress()));
				serverIns.setLastUpdatedByStaffId(instance.getLastUpdatedByStaffId());
				serverIns.setLastUpdatedDate(new Date());
				serverIns.setName(instance.getName());
				serverIns.setDescription(instance.getDescription());
				serverIns.setScriptName(instance.getScriptName());
				serverIns.setMinMemoryAllocation(instance.getMinMemoryAllocation());
				serverIns.setMaxMemoryAllocation(instance.getMaxMemoryAllocation());
				serverIns.setMaxConnectionRetry(instance.getMaxConnectionRetry());
				serverIns.setRetryInterval(instance.getRetryInterval());
				serverIns.setConnectionTimeout(instance.getConnectionTimeout());
				serverIns.setMediationRoot(instance.getMediationRoot());
				serverIns.setReprocessingBackupPath(instance.getReprocessingBackupPath());
				serverIns.setMinDiskSpace(instance.getMinDiskSpace());
				serverIns.setDatabaseInit(instance.isDatabaseInit());
				
			
				DataSourceConfig serverManagerDsConfig = dataSourceDao.findByPrimaryKey(DataSourceConfig.class, instance
						.getServerManagerDatasourceConfig().getId());
				serverIns.setServerManagerDatasourceConfig(serverManagerDsConfig);
				DataSourceConfig iploggerDsConfig = null;
				if (BaseConstants.IPLMS.equals(serverIns.getServer().getServerType().getAlias())) {
					iploggerDsConfig = dataSourceDao.findByPrimaryKey(DataSourceConfig.class, instance.getIploggerDatasourceConfig()
							.getId());
				}
				serverIns.setIploggerDatasourceConfig(iploggerDsConfig);
				
				if(!instance.isFileCdrSummaryDBEnable()){
					serverIns.setFileCdrSummaryDBEnable(instance.isFileCdrSummaryDBEnable());
				}
				if (!serverIns.isDatabaseInit()) {
					List<Service> serviceList = serverIns.getServices();
					for (int index = 0; index < serviceList.size(); index++) {
						Service curService = serviceList.get(index);
						curService.setEnableDBStats(false);
						serviceDao.update(curService);
					}
				}
				serverInstanceDao.update(serverIns);

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SUCCESS);
				responseObject.setObject(serverIns);
			} else {

				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_INSTANCE_NAME);

			}
		}

		else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Import agent into DB
	 * 
	 * @param serverInstance
	 * @param agent
	 * @param staffId
	 * @return
	 */
	@Transactional
	public ResponseObject importAgent(ServerInstance serverInstance, Agent agent) {
		ResponseObject responseObject = new ResponseObject();

		agent.setId(0);
		agent.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		agent.setCreatedDate(new Date());
		agent.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		agent.setLastUpdatedDate(new Date());
		agent.setServerInstance(serverInstance);
		agentDao.save(agent);

		if (agent.getId() != 0) {
			responseObject.setSuccess(true);
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Updates the Server Instance Systemlog tab in database.
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_SYSTEMLOG, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "serverManagerDatasourceConfig,iploggerDatasourceConfig")
	@Override
	public ResponseObject updateInstanceSystemLog(ServerInstance instance) {

		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverIns = serverInstanceDao.findByPrimaryKey(ServerInstance.class, instance.getId());
		if (serverIns != null) {
			serverIns.setLastUpdatedByStaffId(instance.getLastUpdatedByStaffId());
			serverIns.setLastUpdatedDate(new Date());
			serverIns.getLogsDetail().setLevel(instance.getLogsDetail().getLevel());
			serverIns.getLogsDetail().setRollingType(instance.getLogsDetail().getRollingType());
			serverIns.getLogsDetail().setRollingValue(instance.getLogsDetail().getRollingValue());
			serverIns.getLogsDetail().setMaxRollingUnit(instance.getLogsDetail().getMaxRollingUnit());
			serverIns.getLogsDetail().setLogPathLocation(instance.getLogsDetail().getLogPathLocation());
			serverIns.setThresholdSysAlertEnable(instance.isThresholdSysAlertEnable());
			serverIns.setThresholdMemory(instance.getThresholdMemory());
			serverIns.setThresholdTimeInterval(instance.getThresholdTimeInterval());
			serverIns.setLoadAverage(instance.getLoadAverage());
			serverInstanceDao.update(serverIns);

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SUCCESS);
			responseObject.setObject(serverIns);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Updates the Server Instance Statistic tab in database.
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_STATISTICS, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "serverManagerDatasourceConfig,iploggerDatasourceConfig")
	public ResponseObject updateInstanceStatistic(ServerInstance instance, String serviceState) {

		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverIns = serverInstanceDao.findByPrimaryKey(ServerInstance.class, instance.getId());
		if (serverIns != null) {
			serverIns.setLastUpdatedByStaffId(instance.getLastUpdatedByStaffId());
			serverIns.setLastUpdatedDate(new Date());

			serverIns.setFileStorageLocation(instance.getFileStorageLocation());
			serverIns.setFileStatInDBEnable(instance.isFileStatInDBEnable());
			serverIns.setFileCdrSummaryDBEnable(instance.isFileCdrSummaryDBEnable());
						DataSourceConfig serverManagerDsConfig = dataSourceDao.findByPrimaryKey(DataSourceConfig.class, instance
					.getServerManagerDatasourceConfig().getId());

			serverIns.setServerManagerDatasourceConfig(serverManagerDsConfig);
			DataSourceConfig iploggerDsConfig = dataSourceDao
					.findByPrimaryKey(DataSourceConfig.class, instance.getIploggerDatasourceConfig().getId());

			serverIns.setIploggerDatasourceConfig(iploggerDsConfig);

			JSONObject jserviceList = new JSONObject(serviceState);
			logger.debug("JService list =" + jserviceList.toString());

			logger.debug("Service list =" + serviceDao.toString());
			List<Service> serviceList = serverIns.getServices();
			for (int index = 0; index < serviceList.size(); index++) {
				Service curService = serviceList.get(index);
				String curServiceId = String.valueOf(curService.getId());
				if (jserviceList.has(curServiceId)) {
					if (jserviceList.getJSONObject(curServiceId).has("dbState"))
						curService.setEnableDBStats((Boolean) (jserviceList.getJSONObject(curServiceId).get("dbState")));
					if (jserviceList.getJSONObject(curServiceId).has("fileState"))
						curService.setEnableFileStats((Boolean) (jserviceList.getJSONObject(curServiceId).get("fileState")));
					serviceDao.update(curService);
				}
			}

			serverInstanceDao.update(serverIns);

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SUCCESS);
			responseObject.setObject(serverIns);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Copy Server Instance configuration
	 * 
	 * @param copyFromId
	 * @param copyToIds
	 * @param staffId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Auditable(auditActivity = AuditConstants.COPY_CONFIG_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION_BULK_MAP,
			currentEntity = ServerInstance.class, ignorePropList = "")
	@Override
	public Map<String, ResponseObject> copyServerInstanceConfig(int copyFromId, String copyToIds, int staffId, String tempPathForExport,
			String jaxbXmlPath) throws SMException {

		Map<String, ResponseObject> responseMap = new HashMap<>();

		logger.debug("Export Configuration for ServerInstance: " + copyFromId);

		ResponseObject responseObject = exportServerInstanceConfig(copyFromId, false, tempPathForExport);

		if (responseObject != null && responseObject.isSuccess()) {

			Map<String, Object> serverInstanceJAXB = (Map<String, Object>) responseObject.getObject();
			File importConfigFile = (File) serverInstanceJAXB.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);

			// Delete and import Configuration

			String[] serverInstanceIds = copyToIds.split(",");

			for (int j = 0; j < serverInstanceIds.length; j++) {

				String copyToId = serverInstanceIds[j];
				int icopyToId = Integer.parseInt(copyToId);

				ResponseObject responseObjectForInstance = new ResponseObject();

				try {
					logger.debug("Copy form serverInstance: " + copyFromId + " to serverInstance : " + copyToId);

					ServerInstance serverInstance = getServerInstance(icopyToId);

					if (serverInstance.getStatus().equals(StateEnum.ACTIVE)) {
						serverInstance.setLastUpdatedByStaffId(staffId);

						responseObjectForInstance = deleteServerInstanceDepedants(serverInstance, false, jaxbXmlPath, true, false);

						if (responseObjectForInstance.isSuccess()) {

							logger.debug("Import configuration to serverInstance:" + copyToId);
							responseObjectForInstance = importServerInstanceConfig(icopyToId, importConfigFile, staffId,
									BaseConstants.IMPORT_MODE_ADD, jaxbXmlPath, true);
						}

						if (responseObjectForInstance.isSuccess()) {
							responseObjectForInstance.setResponseCode(ResponseCode.SERVER_INSTANCE_COPY_CONFIG_SUCCESS);
							responseObjectForInstance.setSuccess(true);
							responseObjectForInstance.setObject(serverInstance);

							responseMap.put(copyToId, responseObjectForInstance);

						} else if (responseObjectForInstance.getResponseCode() == ResponseCode.SERVICE_RUNNING) {
							responseObjectForInstance.setResponseCode(ResponseCode.SERVICE_RUNNING);
							responseObjectForInstance.setResponseCodeNFV(NFVResponseCode.SERVICE_RUNNING);
							responseMap.put(copyToId, responseObjectForInstance);
						} else {
							responseObjectForInstance.setResponseCode(ResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL);
							responseObjectForInstance.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL);
							responseMap.put(copyToId, responseObjectForInstance);
						}
					} else {
						responseObjectForInstance.setSuccess(false);
						responseObjectForInstance.setResponseCode(ResponseCode.SERVER_INSTANCE_INACTIVE_COPY_CONFIG_FAIL);
						responseObjectForInstance.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INACTIVE_COPY_CONFIG_FAIL);
						responseMap.put(copyToId, responseObjectForInstance);
					}
				} catch (Exception exception) {
					logger.error(exception.getMessage(), exception);
					logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
					responseObjectForInstance.setResponseCode(ResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL);
					responseObjectForInstance.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL);
					responseMap.put(copyToId, responseObjectForInstance);
				}
			}// end of for loop
		} else {
			logger.debug("Problem in export copyFrom server instance configuration");
		}
		return responseMap;
	}

	/**
	 * Method will delete server instance.
	 * 
	 * @param serverInstanceId
	 * @param staffId
	 * @return ResponseObject
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = SMException.class)
	@Override
	public ResponseObject deleteServerInstance(int serverInstanceId, int staffId, String jaxbXmlPath) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		logger.debug("Delete for Server Instance: " + serverInstanceId);
		ServerInstance serverInstance = getServerInstance(serverInstanceId);

	
		if (Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)) || !isServerInstanceRunning(serverInstance)) {
			logger.debug("Server Instance is not Running , export server instance configuration");

			ResponseObject exportresponseObject = exportServerInstanceConfig(serverInstanceId, true, null);
			if (exportresponseObject != null && exportresponseObject.isSuccess()) {

				logger.debug("Export configuration done successfully , now delete entity from SM");

				serverInstance.setLastUpdatedByStaffId(staffId);
				responseObject = deleteServerInstanceDepedants(serverInstance, false, jaxbXmlPath, false, true);
				if (responseObject.isSuccess()) {

					logger.debug("Server Instance depedants are deleted successfully form SM ");
					ServerInstanceService serverInstanceImpl = (ServerInstanceService) SpringApplicationContext.getBean("serverInstanceService"); // getting
																																					// spring																																// context
					if(!(StateEnum.INACTIVE).equals(serverInstance.getStatus()))		{																														// issue.
					responseObject = serverInstanceImpl.deleteServerInstanceConfig(serverInstance);

					if (responseObject.isSuccess()) {

						logger.debug("Server Instance Deleted successfully from SM , now rename port folder at engine");
						
						if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
							HazelcastUtility.getHazelcastUtility().updateCrestelNetServerDataInHazelcastCache(serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getPort()), null, BaseConstants.ACTION_DELETE);
						}	
						updateAutoConfigSIStatus(staffId, serverInstance.getServer().getIpAddress(),  
								serverInstance.getServer().getUtilityPort(),serverInstance.getPort());
						
						responseObject = licenseService.getLicenseDetailsByInstanceId(serverInstance.getId());
						if (responseObject.isSuccess()) {
							License license = (License) responseObject.getObject();
							license.setStatus(StateEnum.DELETED);
							license.setLastUpdatedDate(new Date());
							responseObject = licenseService.updateLicenseDetails(license);
						} else {
							logger.debug("Failed to get license details for server instance so no need to remove license details.");
						}
						
						boolean isSuccess;
						if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
							isSuccess=true;
						}else {
							RemoteJMXHelper jmxCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
									.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
									serverInstance.getConnectionTimeout());
							isSuccess = jmxCall.renamePortFolder(serverInstance.getPort());
							logger.debug("renamePortFolder response: " + isSuccess);
							if (jmxCall.getErrorMessage() == null) {
								logger.debug("Port rename done successfully , now rename startup file");
								isSuccess = jmxCall.renameStartupFile(serverInstance.getPort());
							}else {
								logger.debug("Server Instance utility is not running , rollback trasaction");
								throw new SMException(ResponseCode.P_ENGINE_NOT_RUNNING.getDescription());
							}

						}
						logger.debug("renameStartupFile response: " + isSuccess);

						if (isSuccess) {
							logger.debug("StartUp File rename successfully");

							Map<String, Object> serverInstanceJAXB = (Map<String, Object>) exportresponseObject.getObject();
							File exportXml = (File) serverInstanceJAXB.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
							responseObject.setObject(exportXml.getAbsolutePath());
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_DELETE_SUCCESS);
							responseObject.setSuccess(true);
						} else {
							logger.debug("Problem occure at engine for rename startup file , rollback trasaction");
							throw new SMException(ResponseCode.SERVERINSTANCE_DELETE_FAIL_JMX_API_FAIL.getDescription());
						}
						
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_DELETE_FAIL);
					}
				}else{
						responseObject = serverInstanceImpl.deleteServerInstanceConfig(serverInstance);

						if (responseObject.isSuccess()) {
							Map<String, Object> serverInstanceJAXB = (Map<String, Object>) exportresponseObject.getObject();
							File exportXml = (File) serverInstanceJAXB.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
							responseObject.setObject(exportXml.getAbsolutePath());
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_DELETE_SUCCESS);
						}
						else{
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_DELETE_FAIL);
						}
					}

				}
			} else {
				logger.debug("Fail to export server instance configuration");
				return exportresponseObject;
			}
		} else {
			logger.debug("Server Instance is Running , delete operation not allow");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_RUNNING_DELETE_FAIL);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will delete server instance in server manager database only.
	 * 
	 * @param serverInstanceId
	 * @param staffId
	 * @return ResponseObject
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = SMException.class)
	@Override
	public ResponseObject deleteServerInstanceOnlyInSM(int serverInstanceId, int staffId, String jaxbXmlPath) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		logger.debug("Delete for Server Instance: " + serverInstanceId);
		ServerInstance serverInstance = getServerInstance(serverInstanceId);
		logger.debug("Server Instance is not Running , export server instance configuration");
		ResponseObject exportresponseObject = exportServerInstanceConfig(serverInstanceId, true, null);
		if (exportresponseObject != null && exportresponseObject.isSuccess()) {

			logger.debug("Export configuration done successfully , now delete entity from SM");

			serverInstance.setLastUpdatedByStaffId(staffId);
			responseObject = deleteServerInstanceDepedants(serverInstance, false, jaxbXmlPath, false, true);
			if (responseObject.isSuccess()) {

				logger.debug("Server Instance depedants are deleted successfully form SM ");
				ServerInstanceService serverInstanceImpl = (ServerInstanceService) SpringApplicationContext.getBean("serverInstanceService"); // getting
																																				// spring																																// context
				if(!(StateEnum.INACTIVE).equals(serverInstance.getStatus()))		{																														// issue.
				responseObject = serverInstanceImpl.deleteServerInstanceConfig(serverInstance);

				if (responseObject.isSuccess()) {

					logger.debug("Server Instance Deleted successfully from SM , now rename port folder at engine");
					
					responseObject = licenseService.getLicenseDetailsByInstanceId(serverInstance.getId());
					if (responseObject.isSuccess()) {
						License license = (License) responseObject.getObject();
						license.setStatus(StateEnum.DELETED);
						license.setLastUpdatedDate(new Date());
						responseObject = licenseService.updateLicenseDetails(license);
					} else {
						logger.debug("Failed to get license details for server instance so no need to remove license details.");
					}											
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_DELETE_FAIL);
				}
			}else{
					responseObject = serverInstanceImpl.deleteServerInstanceConfig(serverInstance);

					if (responseObject.isSuccess()) {
						Map<String, Object> serverInstanceJAXB = (Map<String, Object>) exportresponseObject.getObject();
						File exportXml = (File) serverInstanceJAXB.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
						responseObject.setObject(exportXml.getAbsolutePath());
						responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_DELETE_SUCCESS);
					}
					else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_DELETE_FAIL);
					}
				}
			}
		} else {
			logger.debug("Fail to export server instance configuration");
			return exportresponseObject;
		}	
		return responseObject;
	}

	/**
	 * Check server instance is running or not using JMX call
	 * 
	 * @param serverInstance
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isServerInstanceRunning(ServerInstance serverInstance) {
		RemoteJMXHelper jmxCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
				serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		boolean runningStatus;
		String versionInfo = jmxCall.versionInformation();

		if (versionInfo != null && jmxCall.getErrorMessage() == null) {
			logger.info("ServerInstance " + serverInstance.getId() + " is running");
			runningStatus = true;
		} else {
			logger.info("ServerInstance " + serverInstance.getId() + " is not running: ");
			runningStatus = false;
		}
		return runningStatus;

	}

	/**
	 * Delete server instance depedants
	 * 
	 * @param serverInstance
	 * @param isImport
	 * @return
	 * @throws SMException
	 */
	@Transactional
	public ResponseObject deleteServerInstanceDepedants(ServerInstance serverInstance, boolean isImport, String jaxbXmlPath, boolean isCopy,
			boolean isDelete) throws SMException {

		ResponseObject responseObject = getServerInstanceDepedants(serverInstance);

		if (responseObject.isSuccess()) {

			ServerInstance serverInstanceDetail = (ServerInstance) responseObject.getObject();

			List<DatabaseQuery> databaseQueriesList = serverInstanceDetail.getDatabaseQueryList();
			List<PolicyCondition> policyConditionList = serverInstanceDetail.getPolicyConditionList();
			List<PolicyAction> policyActionList = serverInstanceDetail.getPolicyActionList();
			List<PolicyRule> policyRuleList = serverInstanceDetail.getPolicyRuleList();
			List<PolicyGroup> policyGroupList = serverInstanceDetail.getPolicyGroupList();
			List<Policy> policyList = serverInstanceDetail.getPolicyList();

			for (int i = 0; i < databaseQueriesList.size(); i++) {
				databaseQueryService.iterateOverDatabaseQuery(serverInstanceDetail, databaseQueriesList.get(i), isImport);
			}

			for (PolicyCondition exportedPolicyCondition : policyConditionList) {
				policyService.iteratePolicyCondition(serverInstanceDetail, exportedPolicyCondition, null, false);
			}

			for (PolicyAction exportedPolicyAction : policyActionList) {
				policyService.iteratePolicyAction(serverInstanceDetail, exportedPolicyAction, null, false);
			}

			for (PolicyRule exportedPolicyRule : policyRuleList) {
				policyService.iteratePolicyRule(serverInstanceDetail, exportedPolicyRule, null, null, null, false);
			}

			for (PolicyGroup exportedPolicyGroup : policyGroupList) {
				policyService.iteratePolicyGroup(serverInstanceDetail, exportedPolicyGroup, null, null, false);
			}

			for (Policy exportedPolicy : policyList) {
				policyService.iteratePolicy(serverInstanceDetail, exportedPolicy, null, null, false);
			}

			List<SNMPServerConfig> snmpServerList = serverInstanceDetail.getSelfSNMPServerConfig();
			for (int i = 0; i < snmpServerList.size(); i++) {
				SNMPServerConfig snmpServer = snmpServerList.get(i);
				snmpService.iterateSnmpServer(serverInstanceDetail, snmpServer, isImport);
			}
			serverInstanceDetail.setSelfSNMPServerConfig(snmpServerList);
			List<SNMPServerConfig> snmpClientList = serverInstanceDetail.getSnmpListeners();

			for (int i = 0; i < snmpClientList.size(); i++) {
				SNMPServerConfig snmpClient = snmpClientList.get(i);
				List<SNMPAlertWrapper> alertWrapperList = snmpAlertWrapperDao.getWrapperListByClientId(snmpClient.getId());
				logger.debug("Inside deleteServerInstanceDepedants-- Snmp Alert Wrapper Size is::" + alertWrapperList.size());
				for (int j = 0; j < alertWrapperList.size(); j++) {
					SNMPAlertWrapper alertWrapper = alertWrapperList.get(j);
					List<SNMPServiceThreshold> svcThresholdList = snmpServiceThresholdDao.getThresholdListByWrapperId(alertWrapper.getId());
					if (svcThresholdList != null && !svcThresholdList.isEmpty()) {
						alertWrapper.setServiceThreshold(svcThresholdList);
						alertWrapper.setServiceThresholdConfigured(true);
					}
				}
				snmpClient.setConfiguredAlerts(alertWrapperList);

				snmpService.iterateSnmpClient(serverInstanceDetail, snmpClient, null, isImport);

			}
			serverInstanceDetail.setSnmpListeners(snmpClientList);

			List<Service> serviceList = serverInstanceDetail.getServices();

			if (serviceList != null && !serviceList.isEmpty()) {
				responseObject = servicesService.deleteServicesandDepedants(serviceList, serverInstanceDetail, isCopy, jaxbXmlPath);
			}

			if (responseObject.isSuccess()) {
				if (!isCopy) {
					List<Agent> agentList = serverInstanceDetail.getAgentList();
					if (agentList != null && !agentList.isEmpty()) {
						for (int i = 0, size = agentList.size(); i < size; i++) {
							Agent agent = agentList.get(i);
							agentService.iterateOverAgent(agent, serverInstanceDetail, null, isImport, BaseConstants.IMPORT_MODE_OVERWRITE);
						}
						serverInstanceDetail.setAgentList(agentList);
					}
				}

				if (isDelete) {
					// if the DB pointed to this server is the last server
					// pointing to this DSID then mark it DELETED

					if (serverInstance.getServerManagerDatasourceConfig() != null) {
						List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceListByDSId(serverInstance
								.getServerManagerDatasourceConfig().getId());
						if (serverInstanceList != null && serverInstanceList.size() > 1) {
							// other server instances are still pointing to same
							// DB
							// nothing to do with DSConfig entry
						} else {
							// mark the status for the DSID as DELETED
							serverInstanceDetail.getServerManagerDatasourceConfig().setName(
									EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, serverInstanceDetail.getServerManagerDatasourceConfig()
											.getName()));
							serverInstanceDetail.getServerManagerDatasourceConfig().setStatus(StateEnum.DELETED);
							serverInstanceDetail.getServerManagerDatasourceConfig().setLastUpdatedDate(new Date());
							serverInstanceDetail.getServerManagerDatasourceConfig().setLastUpdatedByStaffId(
									serverInstanceDetail.getLastUpdatedByStaffId());

						}
					}

					if (serverInstance.getIploggerDatasourceConfig() != null) {
						List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceListByDSId(serverInstance
								.getIploggerDatasourceConfig().getId());
						if (serverInstanceList != null && serverInstanceList.size() > 1) {
							// other server instances are still pointing to same
							// DB
							// nothing to do with DSConfig entry
						} else {
							// mark the status for the DSID as DELETED
							serverInstanceDetail.getIploggerDatasourceConfig().setName(
									EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, serverInstanceDetail.getIploggerDatasourceConfig()
											.getName()));
							serverInstanceDetail.getIploggerDatasourceConfig().setStatus(StateEnum.DELETED);
							serverInstanceDetail.getIploggerDatasourceConfig().setLastUpdatedDate(new Date());
							serverInstanceDetail.getIploggerDatasourceConfig()
									.setLastUpdatedByStaffId(serverInstanceDetail.getLastUpdatedByStaffId());

						}
					}

					responseObject.setSuccess(true);

				}
			}
		} else {
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Export server instance
	 */
	@Transactional
	@Override
	public ResponseObject exportServerInstanceConfig(int serverInstanceId, boolean isExportBeforeDelete, String tempPathForExport) throws SMException {
		return getServerInstanceFullHierarchy(serverInstanceId, isExportBeforeDelete, tempPathForExport);

	}

	/**
	 * Update SNMP state for server instance in database
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_ALERT_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "iploggerDatasourceConfig,serverManagerDatasourceConfig")
	public ResponseObject updateSNMPStatus(int serverInstanceId, boolean status) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null) {

				logger.debug("Before update snmp status :: " + serverInstance.isSnmpAlertEnable());
				serverInstance.setSnmpAlertEnable(status);
				serverInstanceDao.merge(serverInstance);

				logger.debug("after update snmp status :: " + serverInstance.isSnmpAlertEnable());

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_STATE_CHANGED_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}

		return responseObject;
	}

	/**
	 * Update File state status for server instance in database
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_ADVANCE_CONFIGURATION, actionType = BaseConstants.UPDATE_CUSTOM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject updateFileStateInDB(int serverInstanceId, boolean status) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null) {
				serverInstance.setFileStatInDBEnable(status);

				serverInstanceDao.update(serverInstance);

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.FILE_STATE_CHANGED_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Update web service status for server instance in database
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_ADVANCE_CONFIGURATION, actionType = BaseConstants.UPDATE_CUSTOM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject updateWebServiceStatus(int serverInstanceId, boolean status) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null) {
				serverInstance.setWebservicesEnable(status);

				serverInstanceDao.update(serverInstance);

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.WEBSERVICE_STATE_CHANGED_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	/**
	 * Update rest web service status for server instance in database
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVER_INSTANCE_ADVANCE_CONFIGURATION, actionType = BaseConstants.UPDATE_CUSTOM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject updateRestWebServiceStatus(int serverInstanceId, boolean status) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null) {
				serverInstance.setRestWebservicesEnable(status);

				serverInstanceDao.update(serverInstance);

				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.REST_WEBSERVICE_STATE_CHANGED_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Delete server instance from DB
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_SERVER_INSTANCE, actionType = BaseConstants.DELETE_CUSTOM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject deleteServerInstanceConfig(ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();
		serverInstance.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, serverInstance.getName()));
		serverInstance.setStatus(StateEnum.DELETED);
		serverInstance.setLastUpdatedDate(new Date());

		serverInstanceDao.merge(serverInstance);
		responseObject.setSuccess(true);

		return responseObject;
	}

	/**
	 * Reload server instance using JMX call
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.RELOAD_SERVER_INSTANCE_CONFIGURATION, actionType = BaseConstants.SM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	@Override
	public ResponseObject reloadConfiguration(int serverInstanceId) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				remoteJMXHelper.reloadConfiguration();
				if (remoteJMXHelper.getErrorMessage() == null) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CONFIG_SUCCESS);
					responseObject.setObject(serverInstance);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CONFIG_JMX_CONN_FAIL);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CONFIG_JMX_API_FAIL);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CONFIG_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Soft restart server instance using JMX call
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.SOFT_RESTART_SERVER_INSTANCE_CONFIGURATION, actionType = BaseConstants.SM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	@Override
	public ResponseObject softRestartInstance(int serverInstanceId) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				remoteJMXHelper.softRestart();

				if (remoteJMXHelper.getErrorMessage() == null) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_SUCCESS);
					responseObject.setObject(serverInstance);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_JMX_CONN_FAIL);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_JMX_API_FAIL);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SOFT_RESTART_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	/**
	 * Stop server instance using JMX call
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.STOP_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "")
	@Override
	public ResponseObject stopInstance(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				remoteJMXHelper.stopServer();

				if (remoteJMXHelper.getErrorMessage() == null) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_STOP_SUCCESS);
					responseObject.setObject(serverInstance);

				} else {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_STOP_JMX_CONN_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_STOP_JMX_CONN_FAIL);
				}

			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * @param serverInstanceId
	 * @return Start server instance using JMX call response as response object
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.START_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "")
	@Override
	public ResponseObject startInstance(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();

		boolean fileExists;

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
						.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
						serverInstance.getConnectionTimeout());

				fileExists = remoteJMXHelper.checkScriptExists(serverInstance.getScriptName());

				if (remoteJMXHelper.getErrorMessage() == null) {
					if (fileExists) {
						// Always use startup script which is stored in
						// database 
						logger.info("Script to execute =" + serverInstance.getScriptName());
						remoteJMXHelper.runScript(serverInstance.getScriptName());
						if (remoteJMXHelper.getErrorMessage() == null) {
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_SUCCESS);
							responseObject.setObject(serverInstance);
						} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_JMX_CONN_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_JMX_CONN_FAIL);
						} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_JMX_API_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_JMX_API_FAIL);
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_FAIL);
						}
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE);
						responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE);
					}
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_JMX_CONN_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_JMX_CONN_FAIL);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_JMX_API_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_JMX_API_FAIL);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * @param serverInstanceId
	 * @return ReStart server instance using JMX call response as response
	 *         object
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.RESTART_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "")
	@Override
	public ResponseObject restartInstance(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();

		boolean fileExists;

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {

				// stop server instance first
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				remoteJMXHelper.stopServer();

				try {
					Thread.sleep(BaseConstants.SERVER_CONNECTION_INTERVAL);
					logger.info("Going to sleep after shutdown instance wait to again start");
				} catch (InterruptedException e) { // NOSONAR
					logger.error(e.getMessage(), e);
					logger.debug("Thread wakeup to start server instance after wait time" + e);
				}

				logger.info("Check port process wake up after :" + BaseConstants.SERVER_CONNECTION_INTERVAL + " second.");
				remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

				// Always use startup script which is stored in database
				logger.info("Script to execute =" + serverInstance.getScriptName());

				fileExists = remoteJMXHelper.checkScriptExists(serverInstance.getScriptName());

				if (remoteJMXHelper.getErrorMessage() == null) {
					if (fileExists) {
						remoteJMXHelper.runScript(serverInstance.getScriptName());

						if (remoteJMXHelper.getErrorMessage() == null) {
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_SUCCESS);
							responseObject.setObject(serverInstance);
						} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_JMX_CONN_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_JMX_CONN_FAIL);
						} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_JMX_API_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_JMX_API_FAIL);
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_FAIL);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_FAIL);
						}
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE);
						responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE);
					}
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_JMX_CONN_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_JMX_CONN_FAIL);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_JMX_API_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_JMX_API_FAIL);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RESTART_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_RESTART_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * add new server instance
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_SERVER_INSTANCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = ServerInstance.class,
			ignorePropList = "datasourceConfig")
	@Override
	public ResponseObject addServerInstance(ServerInstance serverInstance, String createMode, Map<String, Object> mReqParam) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		ResponseObject addInstanceResponse = new ResponseObject();
		boolean connError = false;

		// validate copyfrom instance id and import file existance if mode
		// selected
		if (createMode.equals(BaseConstants.CREATE_MODE_COPY)) {
			if (mReqParam.get("copyFromId") == null) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_COPY_FROM_INSTANCE_UNAVAILABLE);
				return responseObject;
			}
		} else if (createMode.equals(BaseConstants.CREATE_MODE_IMPORT)) {
			if (mReqParam.get("importFileName") == null) {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_FILE_UNAVALIABLE);
				return responseObject;
			}
		}

		// If Server Instance is Unique then only process further
		// checking port across the ip address and name across the server
		// manager

		if (isServerInstanceUnique(serverInstance)) {

			if (isInstancePortUnique(serverInstance.getPort(), serverInstance.getServer().getIpAddress())) {
				boolean portAvailable;

				logger.info("isinstancePortUnique");

				if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
					// set script name for server instance
					serverInstance = generateScriptName(serverInstance);
					addInstanceResponse.setSuccess(true);
//					int count=serverInstanceDao.getServerInstanceByServerId(serverInstance.getServer().getId()).size();
					if(!serverInstanceDao.getServerInstanceByServerId(serverInstance.getServer().getId()).isEmpty()) {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_MAX_LIMIT_ALERT);
						serverInstance.setServerHome(BaseConstants.CRESTEL_P_ENGINE_HOME_PATH);
						serverInstance.setJavaHome(BaseConstants.JAVA_HOME_PATH);
						connError = true;
						return responseObject;
					}
				}else {				
					// Check if port avaliable or not
					RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
							.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
							serverInstance.getConnectionTimeout());
	
					portAvailable = serverMgmtRemoteJMXCall.portAvailable(serverInstance.getPort());
					logger.info("portAvailable: " + portAvailable);
	
					// set script name for server instance
					serverInstance = generateScriptName(serverInstance);
					serverInstance.setServerHome(serverMgmtRemoteJMXCall.getCrestelPEngineHome());
					serverInstance.setJavaHome(serverMgmtRemoteJMXCall.getJavaHome());

					// applied port is avaliable to use
					if (portAvailable ) {
						addInstanceResponse = addFreePort(serverInstance);
						serverInstance = (ServerInstance) addInstanceResponse.getObject();
					} else if (serverMgmtRemoteJMXCall.getErrorMessage() == null) {
						addInstanceResponse = addUsedPort(serverInstance);
						serverInstance = (ServerInstance) addInstanceResponse.getObject();
					} else {
						connError = true;
					}

				}
				if (!connError) {
					if (addInstanceResponse.isSuccess()) {
		
						ResponseObject dbAddResponse = addServerInstanceInDB(serverInstance, mReqParam);
						CrestelNetServerData  serverData= new CrestelNetServerData();
						generateNetServerDataFromServerInsance(serverInstance,serverData);
						if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
						    HazelcastUtility.getHazelcastUtility().updateCrestelNetServerDataInHazelcastCache(serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getPort()),serverData,BaseConstants.ACTION_TYPE_ADD);
						}
						saveOrUpdateAutoConfigSI(serverData, serverInstance.getLastUpdatedByStaffId(),
								serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(),
								serverInstance.getPort());
						
						if(!Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
							ResponseObject resObject = startInstance(serverInstance.getId());
							if(resObject.isSuccess()){
								updateStatus(serverInstance.getId());	
							}else{
								//to do
							}
						}
						if (dbAddResponse.isSuccess()) {
							responseObject.setSuccess(true);
							responseObject.setObject(dbAddResponse.getObject());
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_SUCCESS);

							if (createMode.equals(BaseConstants.CREATE_MODE_COPY)) {
								responseObject = addInstanceCopy(serverInstance, responseObject, mReqParam);
							} else if (createMode.equals(BaseConstants.CREATE_MODE_IMPORT)) {
								responseObject = addInstanceImport(serverInstance, responseObject, mReqParam);
							}
						} else {
							if (dbAddResponse.getResponseCode() == ResponseCode.JMX_CONNECTION_FAIL) {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_JMX_CONN_FAIL);
								responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INSERT_JMX_CONN_FAIL);
								responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
							} else if (dbAddResponse.getResponseCode() == ResponseCode.JMX_API_FAIL) {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_JMX_API_FAIL);
								responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INSERT_JMX_API_FAIL);
								responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
							} else if (dbAddResponse.getResponseCode() == ResponseCode.DATA_SOURCE_INSERT_FAIL) {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.DATA_SOURCE_INSERT_FAIL);
								responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
							} else if (dbAddResponse.getResponseCode() == ResponseCode.SERVER_INSTANCE_INSERT_FAIL) {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_FAIL);
								responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INSERT_FAIL);
								responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
							} else if (dbAddResponse.getResponseCode() == ResponseCode.SERVERINSTANCE_ADD_FAIL_NO_DEFAULT_DS) {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_NO_DEFAULT_DS);
								responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
							}
						}
					} else {
						responseObject.setSuccess(false);

						if (addInstanceResponse.getResponseCode() == ResponseCode.SERVER_INSTANCE_INIT_FAIL) {
							// if all steps for server instance create success
							// but at last instance not come up with response
							// than add it as inactive state
					//		serverInstance.setServerHome(serverMgmtRemoteJMXCall.getCrestelPEngineHome());
					//		serverInstance.setJavaHome(serverMgmtRemoteJMXCall.getJavaHome());
							ResponseObject dbAddResponse = addInactiveServerInstanceInDB(serverInstance);
							CrestelNetServerData  serverData= new CrestelNetServerData();
							generateNetServerDataFromServerInsance(serverInstance,serverData);
							if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
								HazelcastUtility.getHazelcastUtility().updateCrestelNetServerDataInHazelcastCache(serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getPort()),serverData,BaseConstants.ACTION_TYPE_ADD);
							}
							saveOrUpdateAutoConfigSI(serverData, serverInstance.getLastUpdatedByStaffId(),
									serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(),
									serverInstance.getPort());
							ResponseObject resObject = startInstance(serverInstance.getId());
							
							if(resObject.isSuccess()){
								updateStatus(serverInstance.getId());	
							}else{
								//to do
							}
							
							if (dbAddResponse.isSuccess()) {
								responseObject.setSuccess(true);
								responseObject.setObject(dbAddResponse.getObject());

								if (createMode.equals(BaseConstants.CREATE_MODE_COPY)) {
									if(resObject.isSuccess()){
										responseObject = addInstanceCopy(serverInstance, responseObject, mReqParam);
									}else{
										responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INACTIVE_COPY);
									}
								}

								else if (createMode.equals(BaseConstants.CREATE_MODE_IMPORT)) {
									File importFile = new File(mReqParam.get("exportPath") + "/" + mReqParam.get("importFileName"));
									if (importFile.exists()) {
										logger.info(" For creste SI Import file avaliable");
										responseObject = importServerInstanceConfig(serverInstance.getId(), importFile,
												serverInstance.getLastUpdatedByStaffId(), BaseConstants.IMPORT_MODE_ADD, mReqParam.get("xmlPath").toString(), false);
										if (!responseObject.isSuccess()) {
											responseObject.setSuccess(false);
											// here there is no custom code for this senario pass
											// response code as it is
											responseObject.setObject(responseObject.getObject());
											responseObject.setResponseCode(responseObject.getResponseCode());
											responseObject.setModuleName(BaseConstants.IMPORT);
										} else {
											responseObject.setSuccess(true);
											responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_IMPORT_SUCCESS);
											responseObject.setModuleName(BaseConstants.IMPORT);
											importFile.delete();
										}
										logger.info("Response ===============" + importFile);
									} else {
										responseObject.setSuccess(true);
										responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_SUCCESS_IMPORT_FILE_OVERSIZE);
										responseObject.setModuleName(BaseConstants.IMPORT);
									}
								}

								else {
									responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INACTIVE_INSERT_SUCCESS);
									responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INACTIVE_INSERT_SUCCESS);
								}

							} else {
								if (dbAddResponse.getResponseCode() == ResponseCode.SERVER_INSTANCE_INSERT_FAIL) {
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_FAIL);
									responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INSERT_FAIL);
									responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
								}
							}
						} else {
							// here we set response code which comes from
							// addInstance func call
							responseObject.setResponseCode(addInstanceResponse.getResponseCode());
							responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
						}
					}
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING);
					responseObject.setResponseCodeNFV(NFVResponseCode.P_ENGINE_NOT_RUNNING);
					responseObject.setArgs(new Object[] { serverInstance.getServer().getIpAddress(),
							String.valueOf(serverInstance.getServer().getUtilityPort()) });
					responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_PORT_ACROSS_NOT_UNIQUE_SERVERNAME);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_PORT_ACROSS_NOT_UNIQUE_SERVERNAME);
				responseObject.setModuleName(BaseConstants.SERVERINSTANCE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SERVER_INSTANCE_NAME);
			responseObject.setResponseCodeNFV(NFVResponseCode.DUPLICATE_SERVER_INSTANCE_NAME);
			responseObject.setModuleName(BaseConstants.SERVERINSTANCE);

		}

		return responseObject;

	}

	/**
	 * @param serverInstance
	 * @param sourceResponse
	 * @param mReqParam
	 * @return Copy server instance for new server instance creation
	 * @throws SMException
	 */
	private ResponseObject addInstanceCopy(ServerInstance serverInstance, ResponseObject sourceResponse, Map<String, Object> mReqParam)
			throws SMException {
		// copy operation

		if (mReqParam.get("copyFromId") != null) {
			Map<String, ResponseObject> responseMap = copyServerInstanceConfig(Integer.parseInt(mReqParam.get("copyFromId").toString()),
					String.valueOf(serverInstance.getId()), serverInstance.getLastUpdatedByStaffId(), mReqParam.get("exportPath").toString(),
					mReqParam.get("xmlPath").toString());
			logger.info("Response =" + responseMap.toString());

			ResponseObject copyResponse = responseMap.get(String.valueOf(serverInstance.getId()));
			if (!copyResponse.isSuccess()) {
				// here we set success to true because even copy oper failed but
				// SI creation success
				sourceResponse.setSuccess(true);
				// Need to check live service senario with service running
				if (copyResponse.getResponseCode() == ResponseCode.SERVICE_RUNNING) {
					sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_COPY_FAIL_SERVICE_RUNNING);
				} else {
					sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_COPY_FAIL);
				}
			} else {
				sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_COPY_SUCCESS);
			}
		} else {
			sourceResponse.setSuccess(false);
			sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_COPY_FROM_INSTANCE_UNAVAILABLE);
			sourceResponse.setObject(null);
		}
		return sourceResponse;
	}

	/**
	 * @param serverInstance
	 * @param sourceResponse
	 * @param mReqParam
	 * @return Import operation in new created server instance
	 * @throws SMException
	 */
	private ResponseObject addInstanceImport(ServerInstance serverInstance, ResponseObject sourceResponse, Map<String, Object> mReqParam)
			throws SMException {

		if (mReqParam.get("importFileName") == null) {
			sourceResponse.setSuccess(false);
			sourceResponse.setObject(null);
			sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_IMPORT_FILE_UNAVALIABLE);
			sourceResponse.setModuleName(BaseConstants.IMPORT);
		} else {
			File importFile = new File(mReqParam.get("exportPath") + "/" + mReqParam.get("importFileName"));
			if (importFile.exists()) {
				logger.info(" For creste SI Import file avaliable");
				ResponseObject importResponse = importServerInstanceConfig(serverInstance.getId(), importFile,
						serverInstance.getLastUpdatedByStaffId(), BaseConstants.IMPORT_MODE_ADD, mReqParam.get("xmlPath").toString(), false);
				if (!importResponse.isSuccess()) {
					sourceResponse.setSuccess(false);
					// here there is no custom code for this senario pass
					// response code as it is
					sourceResponse.setObject(importResponse.getObject());
					sourceResponse.setResponseCode(importResponse.getResponseCode());
					sourceResponse.setModuleName(BaseConstants.IMPORT);
				} else {
					sourceResponse.setSuccess(true);
					sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_IMPORT_SUCCESS);
					sourceResponse.setModuleName(BaseConstants.IMPORT);
					importFile.delete();
				}
				logger.info("Response ===============" + importFile);
			} else {
				sourceResponse.setSuccess(true);
				sourceResponse.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_SUCCESS_IMPORT_FILE_OVERSIZE);
				sourceResponse.setModuleName(BaseConstants.IMPORT);
			}
		}
		return sourceResponse;
	}

	/**
	 * Generate script name for give port
	 * 
	 * @param serverInstancePort
	 * @return
	 */
	private String getScriptFileName(Integer serverInstancePort) {
		logger.debug("==============================================");
		logger.debug("ScriptFileName: " + "startServer_" + serverInstancePort + ".sh");
		logger.debug("==============================================");
		return "startServer_" + serverInstancePort + ".sh";
	}

	/**
	 * Synch server instance
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.SYNCHRONIZE_SERVER_INSTANCE_ACTION, actionType = BaseConstants.SM_ACTION_BULK_MAP,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public Map<String, ResponseObject> syncServerInstance(Map<String, String> syncInputMap) {

		String serverInstancesId;
		String serverInstancesStatus;
		String jaxbXmlPath;
		String xsltFilePath;
		String engineSampleXmlPath;
		List<Object> syncObjectList = new ArrayList<>();
		ResponseObject responseObject = null;
		Map<String, ResponseObject> responseMap = new HashMap<>();
		ServerInstance serverInstanceAuditObj=null;
		if (syncInputMap != null) {
			serverInstancesId = syncInputMap.get(BaseConstants.SERVER_INSTANCES_ID);
			serverInstancesStatus = syncInputMap.get(BaseConstants.SERVER_INSTANCES_STATUS);
			jaxbXmlPath = syncInputMap.get(BaseConstants.JAXB_XML_PATH_CONSTANT);
			xsltFilePath = syncInputMap.get(BaseConstants.XSLT_PATH_CONSTANT);
			engineSampleXmlPath = syncInputMap.get(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT);

			if (serverInstancesId != null && serverInstancesStatus != null) {
				String[] serverInstanceIds = serverInstancesId.split(",");

				for (int j = 0; j < serverInstanceIds.length; j++) {
					responseObject = new ResponseObject();
					String serverInstanceId = serverInstanceIds[j];

					int iserverInstanceId = Integer.parseInt(serverInstanceId);

					try {

						logger.info("Sync for serverInstance: " + serverInstanceId);

						ServerInstance serverInstance = getServerInstance(iserverInstanceId);
						serverInstanceAuditObj=serverInstance;
						if (serverInstance.getStatus().equals(StateEnum.ACTIVE)) {
							if (Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)) || isServerInstanceRunning(serverInstance)) {

							logger.info("Server Instance" + serverInstanceId + " is running");

								// F1 or F2 both flag are false , so sync full
								// server
							if (!serverInstance.isSyncSIStatus() || !serverInstance.isSyncChildStatus()) {

								logger.info("Perform sync operation on Server Instance" + iserverInstanceId);

								List<CrestelNetServiceData> serviceConfigList = new ArrayList<>(0);
								responseObject = getServerInstanceDepedants(serverInstance);
								if (responseObject.isSuccess()) {
									serverInstance = (ServerInstance) responseObject.getObject();								    
								    	responseObject = validateParsingServiceLicense(serverInstance, responseMap, serverInstanceId);
								    	if(responseObject.isSuccess()){
											if(checkStorageLocationForPacketStatisticsAgent(serverInstance)){
												List<CrestelNetConfigurationData> configurationList = getServerConfigSampleList(serverInstance, jaxbXmlPath,
														xsltFilePath, engineSampleXmlPath);
		
												// loop over services for sync
												List<Service> serviceList = serverInstance.getServices();
												if (serviceList != null) {
		
													int serviceSize = serviceList.size();
		
													for (int i = 0; i < serviceSize; i++) {
		
														Service service = serviceList.get(i);
														if (!(StateEnum.DELETED.equals(service.getStatus()))) {
		
															logger.info("Sync for Service Name: " + service.getSvctype().getAlias());
		
															serviceConfigList.add(servicesService.getServicesSampleDataForSync(service.getId(), service
																	.getSvctype().getServiceFullClassName(), jaxbXmlPath, xsltFilePath, engineSampleXmlPath));
														}
													}
												}
		
												if (!configurationList.isEmpty()) {
													CrestelNetServerData serverData = new CrestelNetServerData();
													
													if(!Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
														RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(),
																serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
																serverInstance.getConnectionTimeout());
														String jmxResponse = jmxConnection.syncFullServerToEngine(serverInstance.getServer().getId(), serverInstance
																.getServer().getName(), configurationList, serviceConfigList, serverData);
														List<Service> services = serverInstance.getServices();
														boolean isSyncSuccess = true;
														for (Service service : services) {
															if("HTTP2_COLLECTION_SERVICE".equals(service.getSvctype().getAlias())) {
																service = servicesService.getServiceById(service.getId());
																Http2CollectionService http2Service = (Http2CollectionService) service;
																if(http2Service.getKeystoreFile()!=null) {
																	isSyncSuccess = jmxConnection.syncKeystoreFile(http2Service.getKeystoreFileName(), http2Service.getKeystoreFilePath(), http2Service.getKeystoreFile());
																}
																if(!isSyncSuccess) {
																	jmxResponse = null;
																	jmxConnection.setErrorMessage(BaseConstants.KEYSTORE_FILE_SYNC_FAILURE);
																	break;
																}
															}
														}
														if (BaseConstants.SYNC_SERVERINSTANCE_ENGINE_SUCCESS.equals(jmxResponse)) {
															
															responseObject = resetSyncFlagForServiceandSI(serviceList, serverInstance);
															if (responseObject.isSuccess()) {
																logger.info("Synchronization Perform Successfully" + serverInstanceId);
																this.saveOrUpdateAutoConfigSI(serverData,serverInstance.getLastUpdatedByStaffId(),serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(), serverInstance.getPort());
																responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_SUCCESS);
																responseObject.setObject(serverInstance);
																syncObjectList.add(serverInstance);
																responseMap.put(serverInstanceId, responseObject);																										
																
																responseObject = updateFreePort(serverInstance);
																if (!responseObject.isSuccess()) {
																	responseObject.setSuccess(false);
																	responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);	
																	responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
																	responseMap.put(serverInstanceId, responseObject);
																}
															} else {
																logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
																responseObject.setSuccess(false);
																responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
																responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
																responseMap.put(serverInstanceId, responseObject);
															}
														} else if (jmxConnection.getErrorMessage() != null
																&& jmxConnection.getErrorMessage().equals(BaseConstants.KEYSTORE_FILE_SYNC_FAILURE)) {
															logger.info("Key Store File Sync fail " + serverInstanceId);
															responseObject.setSuccess(false);
															responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
															responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
															responseMap.put(serverInstanceId, responseObject);
			
														} else if (jmxConnection.getErrorMessage() != null
																&& jmxConnection.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
															logger.info("JMX connection fail" + serverInstanceId);
															responseObject.setSuccess(false);
															responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL);
															responseObject.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL);
															responseMap.put(serverInstanceId, responseObject);
			
														} else if (jmxConnection.getErrorMessage() != null
																&& jmxConnection.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
															logger.info("JMX API fail" + serverInstanceId);
															responseObject.setSuccess(false);
															responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL);
															responseObject.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL);
															responseMap.put(serverInstanceId, responseObject);
			
														} else {
															responseObject.setSuccess(false);
															responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
															responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
															responseMap.put(serverInstanceId, responseObject);
														}
													} else {
														generateNetServerDataFromServerInsance(serverInstance, serverData);
														Map<String, Object> statusMap  = HazelcastUtility.getHazelcastUtility().updateCrestelNetServerDataInHazelcastCache(serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getPort()), serverData, BaseConstants.ACTION_TYPE_ADD);
														boolean hazelcastMapUpdateFlag = (boolean)statusMap.get(HazelcastCacheConstants.SUCCESS_STATUS);  
														if (hazelcastMapUpdateFlag) {
															responseObject = resetSyncFlagForServiceandSI(serviceList, serverInstance);
															if (responseObject.isSuccess()) {
																logger.info("Synchronization Perform Successfully" + serverInstanceId);
																this.saveOrUpdateAutoConfigSI(serverData,serverInstance.getLastUpdatedByStaffId(),serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(), serverInstance.getPort());
																responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_SUCCESS);
																responseObject.setObject(serverInstance);
																syncObjectList.add(serverInstance);
																responseMap.put(serverInstanceId, responseObject);																										
																
																
															} else {
																logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
																responseObject.setSuccess(false);
																responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
																responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
																responseMap.put(serverInstanceId, responseObject);
															}
															
														} else {
															responseObject.setSuccess(false);
															responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
															responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
															responseMap.put(serverInstanceId, responseObject);
														}
													}
												}
											}else {
												logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT);
												responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
												responseMap.put(serverInstanceId, responseObject);
											}
								    	}/*else {
								    		logger.info("Invalid License. Please Service Mapped with Appropriate license" + serverInstanceId);
											responseObject.setSuccess(false);
											responseObject.setResponseCode(ResponseCode.LICENSE_FOUND);
											responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
											responseMap.put(serverInstanceId, responseObject);
								    	}*/								    
								} else {
									logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
									responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
									responseMap.put(serverInstanceId, responseObject);
								}
							} else {
								// already sync
								logger.info("Server Instance is already synchronize" + serverInstanceId);
								responseObject.setSuccess(true);
								responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_ALREADY_SYNC);
								responseMap.put(serverInstanceId, responseObject);
							}
							} else {
								logger.info("Server Instance is Not Running" + serverInstanceId);
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
								responseObject.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
								responseMap.put(serverInstanceId, responseObject);
							}
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
							responseObject.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
							responseMap.put(serverInstanceId, responseObject);
						}
					} catch (Exception exception) {
						logger.info("Exception occure" + serverInstanceId);
						logger.error(exception.getMessage(), exception);
						logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
						responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
						responseMap.put(serverInstanceId, responseObject);
					}

				}// end of for loop
				responseObject.setAuditObjectList(syncObjectList);
				responseObject.setObject(serverInstanceAuditObj);
			}
		}
		return responseMap;

	}

	/**
	 * Added by hardik.loriya 
	 * 
	 * @param serverData
	 * @param ipAddress
	 * @param utilityPort
	 * @param port
	 */
	@Override
	@Transactional
	public void saveOrUpdateAutoConfigSI(CrestelNetServerData serverData, int staffId, String ipAddress, int utilityPort, int port) {
		try {
			logger.info("Going to save autoConfigServerInstance. ");
			AutoConfigServerInstance autoConfigServerInstance = autoConfigServerInstanceDao.getAutoConfigSIByIPAndUtilityPortAndSIPort(ipAddress,utilityPort,port);
			if(autoConfigServerInstance != null) {
				byte[] cresteNetData = Utilities.convertObjectToByteArray(serverData);
				autoConfigServerInstance.setCrestelNetServerData(new javax.sql.rowset.serial.SerialBlob(cresteNetData));
				autoConfigServerInstance.setLastUpdatedByStaffId(staffId);
				autoConfigServerInstance.setLastUpdatedDate(new Date());
				autoConfigServerInstanceDao.merge(autoConfigServerInstance);
			} else {
				autoConfigServerInstance = new AutoConfigServerInstance();
				autoConfigServerInstance.setIpaddress(ipAddress);
				autoConfigServerInstance.setUtilityPort(utilityPort);
				autoConfigServerInstance.setSiPort(port);
				autoConfigServerInstance.setCreatedByStaffId(staffId);
				autoConfigServerInstance.setCreatedDate(new Date());
				autoConfigServerInstance.setLastUpdatedByStaffId(staffId);
				autoConfigServerInstance.setLastUpdatedDate(new Date());
				byte[] cresteNetData = Utilities.convertObjectToByteArray(serverData);
				autoConfigServerInstance.setCrestelNetServerData(new javax.sql.rowset.serial.SerialBlob(cresteNetData));
				autoConfigServerInstanceDao.save(autoConfigServerInstance);
			}
		} catch (Exception e) {
			logger.error("Exception Occured while saving or updating autoConfigServerInstance :" + e);
		}
	}
	/*
	 * This methods checks storage location of packet statistics agent for online services 
	 * and parsing service type of Mediation Server
	 * */
	private boolean checkStorageLocationForPacketStatisticsAgent(ServerInstance serverInstance) {
		List<Service> services = serverInstance.getServices();
		if(services != null) {
			for(Integer i = 0;i<services.size();i++) {
				Service service = services.get(i);
				if(StateEnum.ACTIVE.equals(service.getStatus()) &&  service.getSvctype().getTypeOfService().equals(ServiceTypeEnum.MAIN) &&  (service.getSvctype().getServiceCategory().equals(ServiceCategoryEnum.ONLINE) || (serverInstance.getServer().getServerType().getAlias().equals("MEDIATION") && service.getSvctype().getAlias().equals(EngineConstants.PARSING_SERVICE)))) {
					List<Agent> agents = serverInstance.getAgentList();
					for(Agent agent : agents) {
						if(agent instanceof PacketStatisticsAgent) {
							return !(((PacketStatisticsAgent) agent).getStorageLocation() == null ||  ((PacketStatisticsAgent) agent).getStorageLocation().isEmpty());
						}
					}
				}
			}			
		}
		return true;
	}

	private List<SnmpAlertCustomObject> getAlertListWithThreshold(ServerInstance serverInstance, String jaxbXmlPath, String xsltFilePath,
			String engineSampleXmlPath) {
		List<Object> alertListWithThreshold = snmpDao.getAlertListWithThreshold(serverInstance, jaxbXmlPath, xsltFilePath, engineSampleXmlPath);
		logger.debug("After invoking getAlertListWithThreshold query result::" + alertListWithThreshold);
		Iterator<Object> itr = alertListWithThreshold.iterator();
		List<SnmpAlertCustomObject> snmpAlertCustomObjectList = null;

		Map<String, List<SnmpServiceThresholdCustom>> alertThresholdMap = new HashMap<>();
		Map<String, SnmpAlertCustomObject> alertCustomObjectMap = new HashMap<>();
		List<SnmpServiceThresholdCustom> svcThresholdObjList;
		SnmpServiceThresholdCustom alertSvcObj;

		while (itr.hasNext()) {

			Object[] alertObject = (Object[]) itr.next();
			if (alertObject[5] == null || alertObject[5].equals(serverInstance.getId())) {
				SnmpAlertCustomObject alertCustomObject = new SnmpAlertCustomObject();
				alertCustomObject.setAlertId((String) alertObject[0]);
				alertCustomObject.setAlertName((String) alertObject[1]);
				alertCustomObject.setDescription((String) alertObject[2]);

				if (alertObject[3] != null) {
					logger.debug("Service Id value ::" + alertObject[3] + " for alert id : " + alertCustomObject.getAlertId());

					if (alertThresholdMap.get(alertCustomObject.getAlertId()) == null) {
						logger.debug("Service threshold list not found");
						svcThresholdObjList = new ArrayList<>();
					} else {
						logger.debug("Service threshold list found for alert id " + alertCustomObject.getAlertId());
						svcThresholdObjList = alertThresholdMap.get(alertCustomObject.getAlertId());
					}

					alertSvcObj = new SnmpServiceThresholdCustom();
					alertSvcObj.setServiceId((String)alertObject[6]);
					Service service = serviceDao.findByPrimaryKey(Service.class, (Integer) alertObject[3]);
					alertSvcObj.setSvcAlias(service.getSvctype().getAlias());
					if (alertObject[4] != null) {
						alertSvcObj.setServicecThreshold((int) alertObject[4]);
					}
					svcThresholdObjList.add(alertSvcObj);

					logger.debug("Service threshold found for alert id " + alertCustomObject.getAlertId() + " size is : "
							+ svcThresholdObjList.size());
					alertCustomObject.setServicecThresholdList(svcThresholdObjList);
					alertThresholdMap.put(alertCustomObject.getAlertId(), svcThresholdObjList);

				} else if (alertObject[5] != null) {
					logger.debug("Server Instance Id value ::" + alertObject[5] + " for alert id : " + alertCustomObject.getAlertId());

					if (alertThresholdMap.get(alertCustomObject.getAlertId()) == null) {
						logger.debug("Server instance threshold list not found ");
						svcThresholdObjList = new ArrayList<>();
					} else {
						logger.debug("Server instance list found for alert id " + alertCustomObject.getAlertId());
						svcThresholdObjList = alertThresholdMap.get(alertCustomObject.getAlertId());
					}
					alertSvcObj = new SnmpServiceThresholdCustom();
					alertSvcObj.setServiceId((String)alertObject[6]);
					if (alertObject[4] != null) {
						alertSvcObj.setServicecThreshold((int) alertObject[4]);
					}
					svcThresholdObjList.add(alertSvcObj);

					logger.debug("Service threshold found for alert id " + alertCustomObject.getAlertId() + " size is : "
							+ svcThresholdObjList.size());
					alertCustomObject.setServicecThresholdList(svcThresholdObjList);
					alertThresholdMap.put(alertCustomObject.getAlertId(), svcThresholdObjList);

				}
				alertCustomObjectMap.put(alertCustomObject.getAlertId(), alertCustomObject);
				//snmpAlertCustomObjectList.add(alertCustomObject);
				logger.debug("Inside getAlertListWithThreshold::" + alertCustomObject.getAlertId() + "" + alertCustomObject.getAlertName() + ""
						+ alertCustomObject.getDescription() + "" + alertCustomObject.getServerInstanceId());

			} else {
				logger.debug("This is not for current ServerInstance");
			}
		}
		snmpAlertCustomObjectList = new ArrayList<SnmpAlertCustomObject>(alertCustomObjectMap.values());
		return snmpAlertCustomObjectList;

	}

	/**
	 * get list of xml file content in string
	 * 
	 * @param serverInstance
	 * @param jaxbXmlPath
	 * @param xsltFilePath
	 * @param engineSampleXmlPath
	 * @return
	 * @throws SMException
	 * @throws JAXBException
	 */

	private List<CrestelNetConfigurationData> getServerConfigSampleList(ServerInstance serverInstance, String jaxbXmlPath, String xsltFilePath,
			String engineSampleXmlPath) throws SMException, JAXBException {
		List<CrestelNetConfigurationData> configurationList = new ArrayList<>(0);

		SnmpAlertCustomObjectWrapper alertCustomObjectWrapper = new SnmpAlertCustomObjectWrapper();
		List<SnmpAlertCustomObject> alertObjectList = getAlertListWithThreshold(serverInstance, jaxbXmlPath, xsltFilePath, engineSampleXmlPath);
		alertCustomObjectWrapper.setAlertCustomObjectList(alertObjectList);
		
		KafkaDataSourceObjectWrapper kafkaDSObjectWrapper = new KafkaDataSourceObjectWrapper();
		List<KafkaDataSourceConfig> kafkaDSConfigObjectList = kafkaDataSourceDao.getKafkaDataSourceConfigList();
		kafkaDSObjectWrapper.setKafkaDataSourceConfigList(kafkaDSConfigObjectList);

		configurationList.add(getConfiguration(EngineConstants.MEDIATION_SERVER,
				getConfigXmlFileContent(serverInstance, EngineConstants.MEDIATION_SERVER, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.ALERT_CONFIGURATION,
				getSnmpAlertConfigXmlFileContent(alertCustomObjectWrapper, EngineConstants.ALERT_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.KAFKA_CONFIGURATION,
				getKafkaDataSourceConfigXmlFileContent(kafkaDSObjectWrapper, EngineConstants.KAFKA_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.DATABASE_QUERIES_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.DATABASE_QUERIES_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.FILE_DISTRIBUTION_AGENT,
				getFileContent("agent/file-distribution-agent.xml", engineSampleXmlPath)));

		configurationList.add(getConfiguration(EngineConstants.FILE_RENAMING_AGENT,
				getConfigXmlFileContent(serverInstance, EngineConstants.FILE_RENAMING_AGENT, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.ORACLE_DATABASE_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.ORACLE_DATABASE_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.PACKET_STATISTICS_AGENT,
				getConfigXmlFileContent(serverInstance, EngineConstants.PACKET_STATISTICS_AGENT, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.PLUGGABLE_AGGREGATION_CONFIGURATION,
				getFileContent("aggregation/pluggable-aggregation.xml", engineSampleXmlPath)));

		configurationList.add(getConfiguration(EngineConstants.POLICIES_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.POLICIES_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.POLICY_ACTIONS_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.POLICY_ACTIONS_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.POLICY_CONDITIONS_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.POLICY_CONDITIONS_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.POLICY_GROUPS_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.POLICY_GROUPS_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.POLICY_RULES_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.POLICY_RULES_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.SNMP_ALERT_LISTENER_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.SNMP_ALERT_LISTENER_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.SNMP_CONFIGURATION,
				getConfigXmlFileContent(serverInstance, EngineConstants.SNMP_CONFIGURATION, jaxbXmlPath, xsltFilePath)));

		configurationList.add(getConfiguration(EngineConstants.WEB_SERVICE_CONFIGURATION,
				getFileContent("webservice/web-service.xml", engineSampleXmlPath)));

		return configurationList;
	}

	/**
	 * Get XMl for mediation_server.xml
	 * 
	 * @param serverInstance
	 * @throws TransformerException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws SMException
	 */

	private String getConfigXmlFileContent(ServerInstance serverInstance, String xsltFileName, String jaxbXmlPath, String xsltFilePath)
			throws SMException {

		return syncProcessor.processXSLT(ServerInstance.class, serverInstance, xsltFileName, jaxbXmlPath, xsltFilePath);

	}

	private String getSnmpAlertConfigXmlFileContent(SnmpAlertCustomObjectWrapper alertObjectList, String xsltFileName, String jaxbXmlPath,
			String xsltFilePath) throws SMException {

		return syncProcessor.processXSLT(SnmpAlertCustomObjectWrapper.class, alertObjectList, xsltFileName, jaxbXmlPath, xsltFilePath);

	}
	
	private String getKafkaDataSourceConfigXmlFileContent(KafkaDataSourceObjectWrapper kafkaDSConfigObjectList, String xsltFileName, String jaxbXmlPath,
			String xsltFilePath) throws SMException {

		return syncProcessor.processXSLT(KafkaDataSourceObjectWrapper.class, kafkaDSConfigObjectList, xsltFileName, jaxbXmlPath, xsltFilePath);

	}

	/**
	 * Get File Content for sample xml
	 * 
	 * @param fileName
	 * @return
	 */
	@Transactional
	public String getFileContent(String fileName, String engineSampleXmlPath) throws SMException {
		String completefileName = engineSampleXmlPath + File.separator + fileName;

		StringBuilder content = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new java.io.FileReader(completefileName))) {
			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				content.append(sCurrentLine);
			}
		} catch (IOException e) {
			logger.error("Exception Occured:" + e);
			logger.debug("Facing IO execption in reading from file name " + completefileName);
			throw new SMException(e.getMessage());
		}

		return content.toString();
	}

	/**
	 * Get CrestelNetConfigurationData detail
	 * 
	 * @param key
	 * @param xml
	 *            Data
	 * @return
	 */
	private CrestelNetConfigurationData getConfiguration(String key, String xmlData) {
		CrestelNetConfigurationData configuration = new CrestelNetConfigurationData();

		configuration.setNetConfigurationKey(key);
		configuration.setNetConfigurationData(xmlData.getBytes());

		return configuration;
	}

	/**
	 * @param serverInstanceId
	 * @return Load server instance running status using JMX call as response
	 *         object
	 */
	@Transactional
	@Override
	public ResponseObject loadInstanceStatus(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();

		ServerInstance serverInstance = getServerInstance(serverInstanceId);
		JSONObject instanceDetails = new JSONObject();
		String serverIpAddress;
		if (serverInstance != null) {

			serverIpAddress = serverInstance.getServer().getIpAddress();
			logger.debug("Connection for :" + serverIpAddress + " & port :" + serverInstance.getPort());
			RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverIpAddress, serverInstance.getPort(), serverInstance.getMaxConnectionRetry(),
					serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

			Map<String, String> licenseInfo = remoteJMXHelper.licenseInfo();
			if (licenseInfo != null && remoteJMXHelper.getErrorMessage() == null) {
				responseObject.setSuccess(true);
				instanceDetails.put(BaseConstants.STATUS, BaseConstants.ACTIVE);

				String smLicense = (String) MapCache.getConfigValueAsObject(SystemParametersConstant.LICENSE_DETAILS);
				String licenseType = licenseInfo.get(LicenseConstants.VERSION); // Checking
																				// engine
																				// license
																				// version.

				if (LicenseConstants.FULL.equalsIgnoreCase(smLicense)) {

					if (licenseType.equalsIgnoreCase(LicenseConstants.FULL)) {
						calculateLicenseDetails(instanceDetails, licenseInfo, serverIpAddress); // Checking
																								// license
																								// days
																								// counts.
					} else {
						logger.info("Trail license found for sm and engine");
						instanceDetails.put(LicenseConstants.LICENSE_DURATION, "Upload");
						instanceDetails.put(LicenseConstants.LICENSE_ACTION, "apply");
					}
				} else {
					logger.info("Found sm trail license so no need to display upload link");
					instanceDetails.put(LicenseConstants.LICENSE_DURATION, LicenseConstants.NO_LICENSE); // No
																											// need
																											// to
																											// display
																											// engine
																											// license
																											// link
																											// details.
																											// display
																											// proper
																											// user
																											// message.
				}

				CrestelNetServerDetails eliteNetServerDetails = remoteJMXHelper.readServerDetails();
				if (eliteNetServerDetails != null) {

					if (eliteNetServerDetails.getServerStartUpTime() != null) {
						String serverStartTime = dateToString(eliteNetServerDetails.getServerStartUpTime(), "dd/MM/yyyy HH:mm:ss");
						instanceDetails.put(BaseConstants.SERVER_STARTTIME, serverStartTime);
					} else {
						instanceDetails.put(BaseConstants.SERVER_STARTTIME, "-");
					}

					eliteNetServerDetails.getServerStartUpTime();
				} else {
					instanceDetails.put(BaseConstants.SERVER_STARTTIME, "-");
				}

			} else {
				instanceDetails.put(BaseConstants.STATUS, BaseConstants.INACTIVE);
				responseObject.setSuccess(false);
				instanceDetails.put(LicenseConstants.LICENSE_DURATION, LicenseConstants.NO_LICENSE);
				instanceDetails.put(BaseConstants.SERVER_STARTTIME, "-");
			}
		} else {
			responseObject.setSuccess(false);
			instanceDetails.put(BaseConstants.STATUS, BaseConstants.INACTIVE);
			instanceDetails.put(LicenseConstants.LICENSE_DURATION, LicenseConstants.NO_LICENSE);

		}

		responseObject.setObject(instanceDetails);
		return responseObject;
	}

	/**
	 * Method will check license remaining days for trail and full license days.
	 * 
	 * @param jsonObject
	 * @param licenseInfo
	 * @return
	 */
	private JSONObject calculateLicenseDetails(JSONObject jsonObject, Map<String, String> licenseInfo, String serverIpAddress) {
		int days = licenseService.countLicenseDays(licenseInfo.get(LicenseConstants.END_DATE));

		String licenseType = licenseInfo.get(LicenseConstants.VERSION); // Checking
																		// engine
																		// license
																		// version.
		if (licenseType.equalsIgnoreCase(LicenseConstants.FULL)) {
			logger.debug("Found license full license for server  " + serverIpAddress);
			if (days != -1) {
				if (days < 0) {
					jsonObject.put(LicenseConstants.LICENSE_DURATION, "Expired");
				} else if (days <= 14) {
					jsonObject.put(LicenseConstants.LICENSE_DURATION, (days + 1) + " Days left");
				} else {
					jsonObject.put(LicenseConstants.LICENSE_DURATION, "Registered");
				}

				jsonObject.put(LicenseConstants.LICENSE_ACTION, "renew");
			}
		} else {
			logger.debug("Found license trail license for server " + serverIpAddress);
			if (days < 0) {
				jsonObject.put(LicenseConstants.LICENSE_DURATION, "Expired");
			} else if (days <= 9) {
				jsonObject.put(LicenseConstants.LICENSE_DURATION, (days + 1) + " Days left");
			} else {
				jsonObject.put(LicenseConstants.LICENSE_DURATION, "Upload");
			}

			jsonObject.put(LicenseConstants.LICENSE_ACTION, "apply");
		}
		return jsonObject;
	}

	/**
	 * @param serverInstance
	 * @return Set script name for server instance based on input in wizard
	 *         during Server Instance creation
	 */
	public ServerInstance generateScriptName(ServerInstance serverInstance) {

		if (StringUtils.isEmpty(serverInstance.getScriptName())) {
			serverInstance.setScriptName(getScriptFileName(serverInstance.getPort()));
		}
		return serverInstance;
	}

	/**
	 * add server instance when given port is free
	 * 
	 * @param serverInstance
	 * @return Add server instance when given port is free response as response
	 *         object
	 * @throws SMException
	 */
	@Override
	public ResponseObject addFreePort(ServerInstance serverInstance) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		boolean fileCreated;
		//String runScriptResponse;
		RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
				.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		fileCreated = serverMgmtRemoteJMXCall.createServerInstanceScript(serverInstance.getScriptName(), serverInstance.getPort(),
				serverInstance.getMinMemoryAllocation(), serverInstance.getMaxMemoryAllocation(), serverInstance.getServer().getIpAddress());

		if (fileCreated) {
		//	runScriptResponse = serverMgmtRemoteJMXCall.runScript(serverInstance.getScriptName());
		//	if (runScriptResponse != null && "UNKNOWN_OS".equals(runScriptResponse)) {
	//			responseObject.setSuccess(false);
	//			responseObject.setResponseCode(ResponseCode.UNKNOWN_OS);
	//		} else {
				// here after executing run script it might be possible that
				// response comes late so directly execute version check
				// After start script check whether port started or not
	//			RemoteJMXHelper serverInstanceRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(),
	//					serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
	//					serverInstance.getConnectionTimeout());

	//			if (serverInstanceRemoteJMXCall.checkPortRunningOfServerInstance()) {
	//				responseObject.setSuccess(true);
	//				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INIT_SUCCESS);
	//			} else if (serverInstanceRemoteJMXCall.getErrorMessage() != null) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INIT_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_INIT_FAIL);
	//			}
	//		}
		} else {
			if (serverMgmtRemoteJMXCall.getErrorMessage() != null) {
				if (serverMgmtRemoteJMXCall.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL);
				} else if (serverMgmtRemoteJMXCall.getErrorMessage() == BaseConstants.JMX_API_FAILURE) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_FAIL);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_FAIL);
			}
		}
		responseObject.setObject(serverInstance);
		return responseObject;
	}
	
	@Override
	public ResponseObject updateFreePort(ServerInstance serverInstance) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		boolean fileCreated;

		RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
				.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		fileCreated = serverMgmtRemoteJMXCall.createServerInstanceScript(serverInstance.getScriptName(), serverInstance.getPort(),
				serverInstance.getMinMemoryAllocation(), serverInstance.getMaxMemoryAllocation(), serverInstance.getServer().getIpAddress());

		if (fileCreated) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INIT_SUCCESS);
		} else {
			if (serverMgmtRemoteJMXCall.getErrorMessage() != null) {
				if (serverMgmtRemoteJMXCall.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL);	
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL);
				} else if (serverMgmtRemoteJMXCall.getErrorMessage() == BaseConstants.JMX_API_FAILURE) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_FAIL);	
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_CREATE_SCRIPT_FAIL);
			}
		}
		responseObject.setObject(serverInstance);
		return responseObject;
	}

	/**
	 * add server instance when port is not free
	 * 
	 * @param serverInstance
	 * @return Add server instance with free port in database as response object
	 */
	@Override
	public ResponseObject addUsedPort(ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();

		RemoteJMXHelper serverInstanceRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
				serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());

		// check for version info
		String version = serverInstanceRemoteJMXCall.versionInformation();

		if (version != null && version != "") {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ALREADY_AVALIABLE);
			responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_ALREADY_AVALIABLE);
		} else if (serverInstanceRemoteJMXCall.getErrorMessage() != null && !"".equals(serverInstanceRemoteJMXCall.getErrorMessage())) {
			logger.debug("Another service running on port");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.UNKNOWN_SERVICE_RUN_ON_PORT);
			responseObject.setResponseCodeNFV(NFVResponseCode.UNKNOWN_SERVICE_RUN_ON_PORT);
		}
		responseObject.setObject(serverInstance);
		return responseObject;
	}

	/**
	 * Ummarshall server instance from imported file
	 * 
	 * @param importFile
	 * @return Unmarshal operation result as response object
	 */
	@Transactional(rollbackFor = SMException.class)
	public ResponseObject unmarshalServerInstanceFromImportedFile(File importFile, String jaxbXMLPath) {

		ResponseObject responseObject = new ResponseObject();

		final JSONArray finaljArray = new JSONArray();
		try {

			ValidationEventHandler serverInstanceValidator = event -> {

				logger.debug("XSD Validation error occured");

				JSONArray jArray = new JSONArray();
				jArray.put(event.getLocator().getLineNumber());
				jArray.put(event.getMessage());

				finaljArray.put(jArray);
				return true;

			};
			// Unmarshal server instances

			String xsdPAth = jaxbXMLPath + File.separator + BaseConstants.IMPORT_VALIDATION_XSD;
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);//NOSONAR
			Schema schema = sf.newSchema(new File(xsdPAth));

			JAXBContext jaxbContext = JAXBContext.newInstance(ServerInstance.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			jaxbUnmarshaller.setSchema(schema);
			jaxbUnmarshaller.setEventHandler(serverInstanceValidator);
			ServerInstance serverInstance = (ServerInstance) jaxbUnmarshaller.unmarshal(importFile);

			if (finaljArray.length() > 0) {
				logger.debug("XSD Validation fail , so return validation error");

				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.IMPORT_XSD_VALIDATION_FAIL);
				responseObject.setObject(finaljArray);

			} else {
				logger.debug("XSD Validation Done Successfully");
				responseObject.setSuccess(true);
				responseObject.setObject(serverInstance);
			}

		} catch (ClassCastException c) {
			logger.error("Class Cast Exception occure while unmarshlling" + c);
			responseObject.setResponseCode(ResponseCode.IMPORT_UNMARSHALL_FAIL_CLASSCAST);
			responseObject.setSuccess(false);
		} catch (JAXBException e) {
			logger.error("faced error in import", e);
			if (e.getCause() != null) {
				logger.error("Unable to unamarshall server instance xml file due to  :" + e.getCause().getMessage());
				responseObject.setObject(String.valueOf(e.getCause().getMessage()));
			} else {
				logger.error("Unable to unamarshall server instance xml file due to  :" + e.getMessage());
				responseObject.setObject(String.valueOf(e.getMessage()));
			}
			responseObject.setResponseCode(ResponseCode.SERVERiNSTANCE_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
		} catch (SAXException e) {
			logger.error("issue in unmarsalling", e);
			responseObject.setResponseCode(ResponseCode.SERVERiNSTANCE_UNMARSHAL_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional(rollbackFor = SMException.class)
	public ResponseObject importConfigUsingUnmarshllerInstance(int importServerInstanceId, ServerInstance exportedServerInstance, int staffId,
			int importMode, String jaxbXMLPath) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstanceDB = serverInstanceDao.findByPrimaryKey(ServerInstance.class, importServerInstanceId);
		if (serverInstanceDB != null) {
			logger.debug("going to import basic parameters of server instance that includes datasource");
			updateServerInstanceBasicParameterForImport(serverInstanceDB, exportedServerInstance, importMode, staffId);
			responseObject.setSuccess(true);

			if (importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
				logger.debug("overwrite mode, going to delete sever instance dependent");
				responseObject = deleteServerInstanceDepedants(serverInstanceDB, false, jaxbXMLPath, false, false);
			}

			if (responseObject.isSuccess()) {
				importPolicies(serverInstanceDB, exportedServerInstance, importMode);
				responseObject = importServices(serverInstanceDB, exportedServerInstance, importMode);
				snmpService.importSnmpServer(serverInstanceDB, exportedServerInstance, importMode);
				snmpService.importSnmpClient(serverInstanceDB, exportedServerInstance, importMode);
				agentService.importAgent(serverInstanceDB, exportedServerInstance, importMode);
				serverInstanceDao.merge(serverInstanceDB);
			} else {
				logger.debug("Fail to Delete Old service");
				responseObject.setSuccess(false);
			}
		} else {
			logger.debug("Fail to fetch Server instance from database");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	public void updateServerInstanceBasicParameterForImport(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode, int staffId) throws SMException{
		dbServerInstance.setFileStatInDBEnable(exportedServerInstance.isFileStatInDBEnable());
		
		//MED-4530
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			dbServerInstance.setFileStorageLocation(exportedServerInstance.getFileStorageLocation());
		} else {
			if(dbServerInstance.getFileStorageLocation() == null || dbServerInstance.getFileStorageLocation().isEmpty()){
				dbServerInstance.setFileStorageLocation(exportedServerInstance.getFileStorageLocation());
			}
		}
		
		dbServerInstance.setFileCdrSummaryDBEnable(exportedServerInstance.isFileCdrSummaryDBEnable());
		dbServerInstance.setWebservicesEnable(exportedServerInstance.isWebservicesEnable());
		dbServerInstance.setRestWebservicesEnable(exportedServerInstance.isRestWebservicesEnable());
		dbServerInstance.setSnmpAlertEnable(exportedServerInstance.isSnmpAlertEnable());
		// MED-11903 - database init config import
		dbServerInstance.setDatabaseInit(exportedServerInstance.isDatabaseInit());
		dataSourceService.importDatasourceConfig(dbServerInstance, exportedServerInstance);
		dbServerInstance.setThresholdSysAlertEnable(exportedServerInstance.isThresholdSysAlertEnable());
		dbServerInstance.setThresholdTimeInterval(exportedServerInstance.getThresholdTimeInterval());
		dbServerInstance.setThresholdMemory(exportedServerInstance.getThresholdMemory());
		dbServerInstance.setLoadAverage(exportedServerInstance.getLoadAverage());
		dbServerInstance.setLastUpdatedDate(new Date());
		dbServerInstance.setLastUpdatedByStaffId(staffId);
		dbServerInstance.setReprocessingBackupPath(exportedServerInstance.getReprocessingBackupPath());
		
		//dbServerInstance.setMinMemoryAllocation(exportedServerInstance.getMinMemoryAllocation());
		//dbServerInstance.setMaxMemoryAllocation(exportedServerInstance.getMaxMemoryAllocation());
		
		if(dbServerInstance.getLogsDetail().getLogPathLocation()== null || dbServerInstance.getLogsDetail().getLogPathLocation().isEmpty()){
			dbServerInstance.getLogsDetail().setLogPathLocation(exportedServerInstance.getLogsDetail().getLogPathLocation());
		}
		dbServerInstance.getLogsDetail().setLevel(exportedServerInstance.getLogsDetail().getLevel());
		dbServerInstance.getLogsDetail().setMaxRollingUnit(exportedServerInstance.getLogsDetail().getMaxRollingUnit());
		dbServerInstance.getLogsDetail().setRollingType(exportedServerInstance.getLogsDetail().getRollingType());
		dbServerInstance.getLogsDetail().setRollingValue(exportedServerInstance.getLogsDetail().getRollingValue());
	}
	
	public void importPolicies(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("going to import policies");
		List<PolicyCondition> policyConditionList = exportedServerInstance.getPolicyConditionList();
		List<PolicyAction> policyActionList = exportedServerInstance.getPolicyActionList();
		List<PolicyRule> policyRuleList = exportedServerInstance.getPolicyRuleList();
		List<PolicyGroup> policyGroupList = exportedServerInstance.getPolicyGroupList();
		List<Policy> policyList = exportedServerInstance.getPolicyList();
		List<DatabaseQuery> databaseQueries = exportedServerInstance.getDatabaseQueryList();

		Map<String, Integer> databaseQueryMap = new HashMap<>();
		Map<String, Integer> policyConditionMap = new HashMap<>();
		Map<String, Integer> policyActionMap = new HashMap<>();
		Map<String, Integer> policyRuleMap = new HashMap<>();
		Map<String, Integer> policyGroupMap = new HashMap<>();
		Map<String, Integer> policyMap = new HashMap<>();
		
		if(!CollectionUtils.isEmpty(databaseQueries)) {
			int databaseQueriesLength = databaseQueries.size();
			for(int i = databaseQueriesLength-1; i >= 0; i--) {
				DatabaseQuery databaseQuery = databaseQueries.get(i);
				if(databaseQuery != null && !databaseQuery.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importDatabaseQuery(dbServerInstance, databaseQuery, databaseQueryMap, importMode);	
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(policyActionList)) {
			int policyActionLength = policyActionList.size();
			for(int i = policyActionLength-1; i >= 0; i--) {
				PolicyAction policyAction = policyActionList.get(i);
				if(policyAction != null && !policyAction.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importPolicyAction(dbServerInstance, policyAction, policyActionMap, databaseQueryMap, importMode);	
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(policyConditionList)) {
			int conditionLength = policyConditionList.size();
			for(int i = conditionLength-1; i >= 0; i--) {
				PolicyCondition policyCondition = policyConditionList.get(i);
				if(policyCondition != null && !policyCondition.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importPolicyCondition(dbServerInstance, policyCondition, policyConditionMap, databaseQueryMap, importMode);	
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(policyRuleList)) {
			int ruleLength = policyRuleList.size();
			for(int i = ruleLength-1; i >= 0; i--) {
				PolicyRule policyRule = policyRuleList.get(i);
				if(policyRule != null && !policyRule.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importPolicyRule(dbServerInstance, policyRule, policyRuleMap, policyConditionMap, policyActionMap, importMode);	
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(policyGroupList)) {
			int groupLength = policyGroupList.size();
			for(int i = groupLength-1; i >= 0; i--) {
				PolicyGroup policyGroup = policyGroupList.get(i);
				if(policyGroup != null && !policyGroup.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importPolicyGroup(dbServerInstance, policyGroup, policyGroupMap, policyRuleMap, importMode);	
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(policyList)) {
			int policyLength = policyList.size();
			for(int i = policyLength-1; i >= 0; i--) {
				Policy policy = policyList.get(i);
				if(policy != null && !policy.getStatus().equals(StateEnum.DELETED)) {
					policyImportExportService.importPolicyForServerInstance(dbServerInstance, policy, policyMap, policyGroupMap, importMode);	
				}
			}
		}
	}
	
	public ResponseObject importServices(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("going to import services");
		List<Service> serviceList = exportedServerInstance.getServices();
		Hibernate.initialize(dbServerInstance.getServices());
		List<Service> dbServiceList = dbServerInstance.getServices();

		ResponseObject responseObject = new ResponseObject();
		if (!CollectionUtils.isEmpty(serviceList)) {
			int length = serviceList.size();
			for(int i = length-1; i >= 0; i--) {
				Service exportedService = serviceList.get(i);
				if(exportedService != null && !exportedService.getStatus().equals(StateEnum.DELETED)) {
					if(exportedService instanceof Http2CollectionService) {
						Http2CollectionService http2CollService = (Http2CollectionService) exportedService;
						http2CollService.setKeystoreFileName(null);
						http2CollService.setKeystoreFilePath(null);
					}
					logger.debug("Import Operation performed for service : " + exportedService.getName());
					responseObject = serverInstanceImportExportService.importServiceAndDepedants(dbServerInstance, exportedService, exportedServerInstance, importMode);
					if (responseObject.isSuccess() && responseObject.getObject() != null
							&& responseObject.getObject() instanceof Service) {
						Service importedService = (Service) responseObject.getObject();
						updateServiceInServiceList(dbServiceList, importedService);
					}
				}
			}
			if (responseObject.isSuccess()) {
				logger.debug("All Service Imported Successfully , update server instance");
				responseObject.setObject(dbServerInstance);
				responseObject.setSuccess(true);
			} else {
				logger.debug("Fail to import service");
				responseObject.setSuccess(false);
			}
		} else {
			logger.debug("Service not configured");
			responseObject.setObject(dbServerInstance);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	public void updateServiceInServiceList(List<Service> serviceList, Service service) {
		if(serviceList != null && !serviceList.isEmpty() && service != null) {
			int serviceLength = serviceList.size();
			boolean isServiceAvailable = false;
			for(int i = serviceLength-1; i >= 0; i--) {
				Service dbService = serviceList.get(i);
				if(dbService != null && !dbService.getStatus().equals(StateEnum.DELETED)
						&& dbService.getServInstanceId().equalsIgnoreCase(service.getServInstanceId())) {
					serviceList.set(i, service);
					isServiceAvailable = true;
				}
			}
			if(!isServiceAvailable) {
				serviceList.add(service);
			}
		}
	}

	/**
	 * Upload import file for server instance import
	 * 
	 * @param file
	 *            file to import
	 * @param tempImportPath
	 * @return Upload server instance import file response as response object
	 * @throws IOException
	 */
	@Override
	public ResponseObject uploadImportFile(MultipartFile file, String tempImportPath) {

		ResponseObject responseObject = new ResponseObject();
		logger.info("Upload file size =" + file.getSize() + " MB =" + (file.getSize() / 1048576) + " Mapcache ="
				+ MapCache.getConfigValueAsFloat(SystemParametersConstant.UPLOAD_FILE_SIZE, 2048));
		if ((file.getSize() / 1024) > MapCache.getConfigValueAsFloat(SystemParametersConstant.UPLOAD_FILE_SIZE, 2048)) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_CREATE_IMPORT_FILE_OVERSIZE);
		} else {

			try {
				String extension = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));

				String milisec = String.valueOf(Calendar.getInstance().getTimeInMillis()) + extension;
				String filepath = tempImportPath + "/" + milisec;
				File convFile = new File(filepath);

				if (convFile.exists()) {
					convFile.delete();
				}

				convFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(convFile);												//NOSONAR
				fos.write(file.getBytes());
				fos.close();
				logger.info("Temp import file saved on disk successfully at path :" + filepath);
				responseObject.setSuccess(true);

				JSONObject jObj = new JSONObject();
				jObj.put("filename", milisec);
				responseObject.setObject(jObj);
				responseObject.setResponseCode(ResponseCode.SERVERiNSTANCE_IMPORT_UPLOAD_SUCCESS);
			} catch (IOException e) {
				logger.error("File can't be created Error :" + e, e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVERiNSTANCE_IMPORT_UPLOAD_FAIL);
			}

		}
		return responseObject;
	}

	/**
	 * Reload server instance cache using JMX call
	 * 
	 * @param serverInstanceId
	 * @return Reload server instance cache response using JMX call
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.RELOAD_SERVER_INSTANCE_CONFIGURATION, actionType = BaseConstants.SM_ACTION,
			currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject reloadCache(int serverInstanceId, String reloadType,String databaseQuery) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null && serverInstance.getServer() != null && serverInstance.getServer().getIpAddress() != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
						serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				
				if (reloadType != null && databaseQuery != null  && reloadType.equals("dynamic") && !databaseQuery.isEmpty()) {
					List<String> dbQueryList = Arrays.asList(databaseQuery.split(","));
					remoteJMXHelper.reloadLookupDataCache(dbQueryList, new Date(), "full");
				}else if (reloadType != null && reloadType.equals("distDynamic")) {
					remoteJMXHelper.reloadLookupDataCache(null, new Date(), "full");
				}
				else
					remoteJMXHelper.reloadCache();
				
				if (remoteJMXHelper.getErrorMessage() == null) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CACHE_SUCCESS);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CACHE_JMX_CONN_FAIL);
				} else if (remoteJMXHelper.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CACHE_JMX_API_FAIL);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_RELOAD_CACHE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Validate Server Instance and its dependents , for import operation
	 * 
	 * @param exportedserverInstance
	 * @param importserverInstanceId 
	 * @return List<ImportValidationErrors>
	 */
	@Transactional(rollbackFor = Exception.class)
	public List<ImportValidationErrors> validateUnMarshallerInstance(ServerInstance exportedserverInstance, int importserverInstanceId, int importMode) {

		List<ImportValidationErrors> serverInstanceimportErrorList = instanceValidator.validateServerInstanceForImport(exportedserverInstance);

		if (serverInstanceimportErrorList != null && !serverInstanceimportErrorList.isEmpty()) {
			logger.debug("VALIDATION fail for server instance size " + serverInstanceimportErrorList.size());

			return serverInstanceimportErrorList;
		} else {

			List<ImportValidationErrors> importErrorList = new ArrayList<>();

			validateSnmpServer(exportedserverInstance, importErrorList);

			validateSnmpClient(exportedserverInstance, importErrorList);

			validateServices(exportedserverInstance, importErrorList);

			validateAgent(exportedserverInstance, importErrorList);

			validatePolicy(exportedserverInstance, importErrorList,importserverInstanceId, importMode);

			return importErrorList;
		}

	}

	private boolean checkForDuplicateDSName(String dataSourceName) {
		boolean duplicateExists = false;
		List<DataSourceConfig> dsConfigList = dataSourceDao.getDataBaseConfigurationName(dataSourceName);
		if (dsConfigList != null && !dsConfigList.isEmpty())
			duplicateExists = true;

		return duplicateExists;
	}

	@Override
	@Transactional
	public boolean checkDBConfigUniqueName(String name) {
		return checkForDuplicateDSName(name);
	}

	@Override
	public DataSourceConfig updateDataSourceDetails(DataSourceConfig defaultDSConfig, int staffId, String ipAddress) {

		defaultDSConfig.setCreatedByStaffId(staffId);
		defaultDSConfig.setLastUpdatedByStaffId(staffId);

		return defaultDSConfig;
	}

	/**
	 * Update server instance status only for inactive server instance in
	 * database
	 * 
	 * @param serverInstanceId
	 * @return Update server instance response as response object
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public ResponseObject updateStatus(int serverInstanceId) throws SMException {

		ResponseObject responseObject = new ResponseObject();
		try {
			if (serverInstanceId != 0) {
				ServerInstance serverInstance = getServerInstance(serverInstanceId);
				if (serverInstance != null) {

					RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
							serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
					CrestelNetServerData engineServerData = jmxConnection.readServerConfiguration();
					Map<String, String> licenseDetails = jmxConnection.licenseInfo();
					if (engineServerData != null) {

						// Add Agent
						List<AgentType> agentTypeList = agentTypeDao.getAllAgentType();
						List<Agent> newagentTypeList;
						if (agentTypeList != null && !agentTypeList.isEmpty()) {
							logger.debug("Agent List Found from DB is : " + agentTypeList.size());
							newagentTypeList = new ArrayList<>();
							for (AgentType agentType : agentTypeList) {
								Class<? extends Agent> agentName;

								logger.debug("inside addServerInstanceInDB : Agent class name:" + agentType.getAgentFullClassName());
								agentName = (Class<? extends Agent>) Class.forName(agentType.getAgentFullClassName());

								Agent agent = agentName.newInstance();
								agent.setServerInstance(serverInstance);
								agent.setAgentType(agentType);
								agentDao.save(agent);

								if (agent.getId() > 0) {
									logger.debug("Agent created successfully");
									responseObject.setSuccess(true);
									newagentTypeList.add(agent);
								}
							}
							serverInstance.setAgentList(newagentTypeList);
						}
						if (responseObject.isSuccess()) {
							serverInstance.setStatus(StateEnum.ACTIVE);
							serverInstance.setLastUpdatedDate(new Date());
							serverInstanceDao.merge(serverInstance);

						//	addInstanceLicenseDetails(serverInstance, licenseDetails); // Adding
																						// inactive(checked
																						// running
																						// or
																						// not)
																						// server
																						// instance
																						// entry
																						// in
																						// license
																						// table.

							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SUCCESS);
						} else {
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.SERVICE_UPDATE_FAIL);
						}
					} else if (jmxConnection.getErrorMessage() != null && jmxConnection.getErrorMessage().equals(BaseConstants.JMX_API_FAILURE)) {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.JMX_API_FAIL);
					}
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UNAVALIABLE);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
			}

		} catch (Exception e) {
			throw new SMException(e);
		}
		return responseObject;
	}

	/**
	 * @param serverInstanceId
	 * @param port
	 * @return port free or not response as response object
	 */
	@Transactional
	@Override
	public ResponseObject checkPortAvailibility(int serverInstanceId, int port) {

		ResponseObject responseObject = new ResponseObject();

		if (serverInstanceId != 0) {
			ServerInstance serverInstance = getServerInstance(serverInstanceId);
			if (serverInstance != null) {
				RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getServer()
						.getUtilityPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
						serverInstance.getConnectionTimeout());

				boolean portfree = remoteJMXHelper.portAvailable(port);

				if (portfree && remoteJMXHelper.getErrorMessage() == null) {

					responseObject.setSuccess(true);

				} else if (remoteJMXHelper.getErrorMessage() != null) {

					logger.debug("RemoteJMXHelper.getErrorMessage()");

					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.P_ENGINE_NOT_RUNNING); // when
																						// utility
																						// is
																						// down
					responseObject.setArgs(new Object[] { serverInstance.getServer().getIpAddress(),
							String.valueOf(serverInstance.getServer().getUtilityPort()) });

				} else {

					logger.debug(remoteJMXHelper.getErrorMessage());

					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.UNKNOWN_SERVICE_RUN_ON_PORT); // when
																								// the
																								// first
																								// condition
																								// is
																								// false
																								// not
																								// checking
																								// the
																								// another
				}
			} else {
				responseObject.setSuccess(false);

			}
		} else {
			responseObject.setSuccess(false);

		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	public static <T> T initializeAndUnproxy(T entity) {
		if (entity == null) {
			throw new NullPointerException("Entity passed for initialization is null");
		}
		if (entity instanceof HibernateProxy) {
			entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}
		return entity;
	}

	/**
	 * Iterate over server instance and create jaxb xml
	 * 
	 * @param exportXmlPath
	 * @param serverInstanceId
	 * @return
	 * @throws SMException
	 */
	@Override
	public ResponseObject getServerInstanceJAXB(String exportXmlPath, int serverInstanceId) throws SMException {

		JAXBContext jaxbContext;
		ResponseObject responseObject = new ResponseObject();

		if (exportXmlPath != null) {
			try {
				ServerInstance serverInstance = serverInstanceDao.getServerInstanceFullHierarchyWithOutMarshlling(serverInstanceId);
				File exportxml = new File(exportXmlPath);

				jaxbContext = JAXBContext.newInstance(ServerInstance.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(serverInstance, exportxml);

				responseObject = generateJAXBXml(exportxml, exportXmlPath, serverInstance);

			} catch (JAXBException e) {
				logger.error("Export backup path is not valid ", e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.INVALID_EXPORT_BACKUP_PATH);
				responseObject.setArgs(new Object[] { exportXmlPath });
			}

		}
		return responseObject;

	}

	public ResponseObject getSnmpAlertJAXB(String jaxbXmlPath, String engineSampleXmlPath, SnmpAlertCustomObjectWrapper alertObjectList)
			throws SMException, JAXBException {

		logger.debug("Inside getSnmpAlertJAXB:");
		JAXBContext jaxbContext;
		ResponseObject responseObject = new ResponseObject();
		String finalJaxbXmlPath = jaxbXmlPath;
		if (finalJaxbXmlPath != null) {

			finalJaxbXmlPath = finalJaxbXmlPath + File.separator + "Snmp-aler-config.xml";

			File exportxml = new File(finalJaxbXmlPath);

			jaxbContext = JAXBContext.newInstance(SnmpAlertCustomObjectWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(alertObjectList, exportxml);
		}
		return responseObject;

	}

	/**
	 * Write JAXB XML content into stringbuilder
	 * 
	 * @param exportxml
	 * @param exportXmlPath
	 * @return
	 */
	public ResponseObject generateJAXBXml(File exportxml, String exportXmlPath, ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();

		try (BufferedReader br = new BufferedReader(new FileReader(exportxml))) {

			Map<String, Object> serverInstaceJAXB;
			String fileContent;
			StringBuilder sb = new StringBuilder();
			while ((fileContent = br.readLine()) != null) {
				sb.append(fileContent);
			}
			//logger.debug("JAXB XML is " + sb.toString());

			serverInstaceJAXB = new HashMap<>();
			serverInstaceJAXB.put(BaseConstants.SERVER_INSTANCE_EXPORT_FILE, exportxml);
			serverInstaceJAXB.put(BaseConstants.SERVICE_JAXB_OBJECT, serverInstance);
			responseObject.setSuccess(true);
			responseObject.setObject(serverInstaceJAXB);

		} catch (IOException e) {
			logger.error("Export backup path is not valid ", e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.INVALID_EXPORT_BACKUP_PATH);
			responseObject.setArgs(new Object[] { exportXmlPath });
		}
		return responseObject;
	}

	/**
	 * Validate SNMP Server Parameter
	 * 
	 * @param exportedserverInstance
	 * @param importErrorList
	 */
	public void validateSnmpServer(ServerInstance exportedserverInstance, List<ImportValidationErrors> importErrorList) {

		logger.debug("Validate Snmp Server from serverInstance");
		List<SNMPServerConfig> snmpServerList = exportedserverInstance.getSelfSNMPServerConfig();
		if (snmpServerList != null && !snmpServerList.isEmpty()) {

			for (int i = 0, size = snmpServerList.size(); i < size; i++) {
				SNMPServerConfig snmpServer = snmpServerList.get(i);
				if (!(StateEnum.DELETED.equals(snmpServer.getStatus()))) {

					validator.validateSnmpServerConfigParam(snmpServer, null, importErrorList, BaseConstants.SNMP_CONFIG, true);
				}
			}
		} else {
			logger.debug("Snmp Server not configured");
		}
	}

	/**
	 * Validate Snmp Client Parameter
	 * 
	 * @param exportedserverInstance
	 * @param importErrorList
	 */
	public void validateSnmpClient(ServerInstance exportedserverInstance, List<ImportValidationErrors> importErrorList) {

		logger.debug("Validate Snmp Client from serverInstance");
		List<SNMPServerConfig> snmpClientList = exportedserverInstance.getSnmpListeners();
		if (snmpClientList != null && !snmpClientList.isEmpty()) {

			for (int i = 0, size = snmpClientList.size(); i < size; i++) {
				SNMPServerConfig snmpClient = snmpClientList.get(i);
				if (!(StateEnum.DELETED.equals(snmpClient.getStatus()))) {

					validator.validateSnmpClientConfigParamForImport(snmpClient, importErrorList, BaseConstants.SNMP_CONFIG, true);
				}
			}
		} else {
			logger.debug("Snmp Client not configured");
		}
	}

	/**
	 * Validate Service Parameter
	 * 
	 * @param exportedserverInstance
	 * @param importErrorList
	 */
	public void validateServices(ServerInstance exportedserverInstance, List<ImportValidationErrors> importErrorList) {

		logger.debug("Validate Services from serverInstance");

		List<Service> serviceList = exportedserverInstance.getServices();

		if (serviceList != null && !serviceList.isEmpty()) {

			for (int i = 0, size = serviceList.size(); i < size; i++) {
				Service service = serviceList.get(i);
				
				if (!(StateEnum.DELETED.equals(service.getStatus()))) {
						
					servicesService.validateServiceForImport(service, importErrorList,0); //service id will be zero fserverInstanceMapor import and add or overide option at server level import.
				}
			}
		} else {
			logger.debug("Service not configured");
		}

	}

	/**
	 * Validate Agent Parameter
	 * 
	 * @param exportedserverInstance
	 * @param importErrorList
	 */
	public void validateAgent(ServerInstance exportedserverInstance, List<ImportValidationErrors> importErrorList) {

		logger.debug("Validate Agent from serverInstance");

		List<Agent> agentList = exportedserverInstance.getAgentList();

		if (agentList != null && !agentList.isEmpty()) {

			for (int i = 0, size = agentList.size(); i < size; i++) {
				Agent agent = agentList.get(i);
				if (!(StateEnum.DELETED.equals(agent.getStatus()))) {

					agentService.validateAgentForImport(agent, importErrorList);
				}
			}
		} else {
			logger.debug("Agent not configured");
		}

	}

	/**
	 * Validates Policy Parameter
	 * 
	 * @param importedServerInstance
	 *            the imported server instance
	 * @param importErrorList
	 *            the validation error list
	 * @param importserverInstanceId 
	 */
	public void validatePolicy(ServerInstance importedServerInstance, List<ImportValidationErrors> importErrorList, int importserverInstanceId, int importMode) {

		logger.debug("Validate Policy from serverInstance");

		List<Policy> policyList = importedServerInstance.getPolicyList();
		List<PolicyCondition> policyConditionList = importedServerInstance.getPolicyConditionList();
		List<PolicyAction> policyActionList = importedServerInstance.getPolicyActionList();
		List<PolicyRule> policyRuleList = importedServerInstance.getPolicyRuleList();
		List<PolicyGroup> policyGroupList = importedServerInstance.getPolicyGroupList();
		List<DatabaseQuery> databaseQueryList = importedServerInstance.getDatabaseQueryList();
		if (policyList != null && !policyList.isEmpty()) {
			for (Policy policy : policyList) {
				if (!StateEnum.DELETED.equals(policy.getStatus()) && !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					policyService.validatePolictForImport(policy, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy not configured");
		}
		if (policyConditionList != null && !policyConditionList.isEmpty()) {
			for (PolicyCondition policyCondition : policyConditionList) {
				if (!StateEnum.DELETED.equals(policyCondition.getStatus())&& !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					policyService.validatePolicyConditionForImport(policyCondition, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy conditions not configured");
		}
		if (policyActionList != null && !policyActionList.isEmpty()) {
			for (PolicyAction policyAction : policyActionList) {
				if (!StateEnum.DELETED.equals(policyAction.getStatus())&& !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					policyService.validatePolicyActionForImport(policyAction, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy actions not configured");
		}
		if (policyRuleList != null && !policyRuleList.isEmpty()) {
			for (PolicyRule policyRule : policyRuleList) {
				if (!StateEnum.DELETED.equals(policyRule.getStatus())&& !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					policyService.validatePolicyRuleForImport(policyRule, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy actions not configured");
		}
		if (policyGroupList != null && !policyGroupList.isEmpty()) {
			for (PolicyGroup policyGroup : policyGroupList) {
				if (!StateEnum.DELETED.equals(policyGroup.getStatus())&& !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					policyService.validatePolicyGroupForImport(policyGroup, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy actions not configured");
		}
		if (databaseQueryList != null && !databaseQueryList.isEmpty()) {
			for (DatabaseQuery databaseQuery : databaseQueryList) {
				if (!StateEnum.DELETED.equals(databaseQuery.getStatus())&& !(importMode == BaseConstants.IMPORT_MODE_OVERWRITE)) {
					databaseQueryService.validateDatabaseQueryForImport(databaseQuery, importErrorList,importserverInstanceId);
				}
			}
		} else {
			logger.debug("Policy actions not configured");
		}
	}

	/**
	 * Method will get all instances by server type id.
	 * 
	 * @param serverInstanceId
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getAllInstancesByServerType(int id, boolean isCreate) {

		ResponseObject responseObject = new ResponseObject();
		
		Server server = null;
		ServerInstance serverInstance = null;
		
		if(isCreate) {
			//if request comes from create server instance (copy radio)
			server = serverDao.getServer(id);
		} else {
			//if request comes from copy config (server dashboard)
			serverInstance = serverInstanceDao.getServerInstance(id);
		}
		
		// if request comes from server dashboard then server will be null and server instance will be not null
		if(server == null && serverInstance != null) {
			server = serverInstance.getServer();
		}
		
		if (server != null) {
			int serverTypeId = server.getServerType().getId();
			List<Server> serverList = serverDao.getAllServerByServerType(serverTypeId);
			if (serverList != null && !serverList.isEmpty()) {

				Integer[] idList = new Integer[serverList.size()];

				for (int i = 0; i < serverList.size(); i++) {
					idList[i] = serverList.get(i).getId();
				}

				List<ServerInstance> serverInstanceList = serverInstanceDao.getAllInstanceByServerTypeId(idList);
				if (serverInstanceList != null && !serverInstanceList.isEmpty()) {
					responseObject.setSuccess(true);
					responseObject.setObject(serverInstanceList);
				} else {
					responseObject.setSuccess(false);
					responseObject.setObject(ResponseCode.FAIL_SERVER_INSTANCE_DETAILS);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(ResponseCode.FAIL_SERVER_INSTANCE_DETAILS);
			}
		} else {
			logger.info("Failed to get server details for id " + id);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_ID_UNAVALIABLE);
		}
		return responseObject;
	}

	/**
	 * Method will add server instance entry to license table with current
	 * license status.
	 */
	private void addInstanceLicenseDetails(ServerInstance serverInstance, Map<String, String> licenseDetails) {
	
		String tps = null,licenseVersion=null;
		Date startDate = null,endDate = null;
		if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
			tps=LicenseConstants.TRIAL_TPS;
			startDate=new Date();
			licenseVersion=LicenseConstants.FULL;
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(startDate);
			calendar.set(Calendar.DATE, LicenseConstants.FULL_LICENSE_DAYS);
			endDate=calendar.getTime();
			
		}else {
			startDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.START_DATE), LicenseConstants.DATE_FORMAT);
			endDate = DateFormatter.formatDate(licenseDetails.get(LicenseConstants.END_DATE), LicenseConstants.DATE_FORMAT);
			licenseVersion = licenseDetails.get(LicenseConstants.VERSION);
			tps = licenseDetails.get(LicenseConstants.TPS);
		}
		License serverLicenseDetails = new License(serverInstance.getServer().getIpAddress(), startDate, endDate, serverInstance,
				LicenseConstants.LICENSE_ENGINE);
		if (licenseVersion.equalsIgnoreCase(LicenseTypeEnum.FULL.toString())) {
			serverLicenseDetails.setLicenceType(LicenseTypeEnum.FULL);
		} else {
			serverLicenseDetails.setLicenceType(LicenseTypeEnum.TRIAL);
		}
		
		/*
		 * if(tps != null) serverLicenseDetails.setTps(tps);
		 */	
		licenseDao.save(serverLicenseDetails);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ServerInstance> getServerList() {

		return serverInstanceDao.getServerList();
	}

	@Override
	public ResponseObject validateMigrationServerInstance(ServerInstance serverInstance) {
		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList;
		List<ImportValidationErrors> serverInstanceimportErrorList = instanceValidator.validateServerInstanceForImport(serverInstance);

		importErrorList = serverInstanceimportErrorList;
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
			responseObject.setResponseCode(ResponseCode.MIGRATION_XSD_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			responseObject.setSuccess(true);
		}

		return responseObject;
	}

	@Override
	public ResponseObject validateMigrationDetails(ServerInstance serverInstance) {

		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		List<ImportValidationErrors> serverInstanceimportErrorList = instanceValidator.validateServerInstanceForImport(serverInstance);

		if (serverInstanceimportErrorList != null && !serverInstanceimportErrorList.isEmpty()) {
			logger.debug("VALIDATION fail for server instance size " + serverInstanceimportErrorList.size());

			importErrorList = serverInstanceimportErrorList;
		} else {
			logger.info("Validating service parameters details.");
			validateSnmpServer(serverInstance, importErrorList);
			validateSnmpClient(serverInstance, importErrorList);
			validateServices(serverInstance, importErrorList);
			validateAgent(serverInstance, importErrorList);

		}

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
			responseObject.setResponseCode(ResponseCode.MIGRATION_XSD_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			responseObject.setSuccess(true);
		}

		return responseObject;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject addMigratedServerInstance(ServerInstance serverInstance) {
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstancedb = null;
		if (serverInstance != null) {
			serverInstanceDao.merge(serverInstance);
			List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceByIpaddressAndPort(serverInstance.getPort(), serverInstance
					.getServer().getIpAddress());
			if (serverInstanceList != null && !serverInstanceList.isEmpty() && serverInstanceList.get(0) != null) {
				serverInstancedb = serverInstanceList.get(0);
				logger.debug("Id after add is::::" + serverInstancedb.getId());
			}

			responseObject.setObject(serverInstancedb);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_SUCCESS);
			responseObject.setSuccess(true);
		} else {

			responseObject.setResponseCode(ResponseCode.FAIL_SERVER_INSTANCE_DETAILS);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will update synch status.
	 */
	@Override
	@Transactional
	public void updateSynchStatus(ServerInstance serverInstance) {

		serverInstanceDao.merge(serverInstance);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ResponseObject addServerInstance(ServerInstance serverInstance) {
		ResponseObject responseObject = new ResponseObject();
		if (serverInstance != null) {
			serverInstanceDao.save(serverInstance);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_INSERT_SUCCESS);
			responseObject.setSuccess(true);
		} else {
			responseObject.setResponseCode(ResponseCode.FAIL_SERVER_INSTANCE_DETAILS);
			responseObject.setSuccess(false);

		}
		return responseObject;
	}

	public String dateToString(Date date, String pattern) {
		String formattedDateString = "-";
		if (date != null) {
			try {
				formattedDateString = new SimpleDateFormat(pattern).format(date);
			} catch (Exception e) {
				logger.debug("Exception::" + e);
				formattedDateString = date.toString();
			}
		}
		return formattedDateString;
	}

	/**
	 * @author jui.purohit
	 * @param prefix
	 * @return This method will append random string of 5 character after given
	 *         prefix
	 */
	public String getRandomName(String prefix) {
		String characterList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int characterLength = characterList.length();
		int randomStringLength = 5;
		StringBuilder randomString = new StringBuilder();
		randomString.append(prefix);
		randomString.append("_");
		for (int i = randomStringLength - 1; i >= 0; i--) {
			randomString.append(characterList.charAt(getRandomNumber(characterLength)));
		}
		return randomString.toString();
	}

	/**
	 * @author jui.purohit
	 * @param length
	 * @return This method will return random number
	 */
	private int getRandomNumber(int number) {
		Random randomGenerator = new Random();//NOSONAR
		int randomInt = randomGenerator.nextInt(number);
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}

	@Override
	public Service getServiceFromServerInstance(ServerInstance serverInstance, String serviceName) {
		List<Service> serviceList = servicesService.getServicesforServerInstance(serverInstance.getId());//to get associated services(super class)
		if(!CollectionUtils.isEmpty(serviceList)) {
			int length = serviceList.size();
			for(int i = length-1; i >= 0; i--) {
				Service service = serviceList.get(i);
				if(service != null && !service.getStatus().equals(StateEnum.DELETED)
						&& service.getName().equalsIgnoreCase(serviceName)) {
					return servicesService.getServiceandServerinstance(service.getId());//to get child service object
				}
			}
		}
		return null;
	}
	
	@Override
	public Service getServiceFromServerInstance(ServerInstance serverInstance, ServicePacketStatsConfig exportedConfig) {
		String serviceType = exportedConfig.getService().getSvctype().getAlias();
		String serviceInstanceId = exportedConfig.getService().getServInstanceId();
		List<Service> serviceList = servicesService.getServicesforServerInstance(serverInstance.getId());//to get associated services(super class)
		if(!CollectionUtils.isEmpty(serviceList)) {
			int length = serviceList.size();
			for(int i = length-1; i >= 0; i--) {
				Service service = serviceList.get(i);
				if(service != null && !service.getStatus().equals(StateEnum.DELETED)
						&& service.getSvctype().getAlias().equalsIgnoreCase(serviceType)
						&& service.getServInstanceId().equalsIgnoreCase(serviceInstanceId)) {
					return servicesService.getServiceandServerinstance(service.getId());//to get child service object
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public ServerInstance getServerInstanceListBySerInsId(int serverInstanceId) {
		List<ServerInstance> instanceList = serverInstanceDao.getServerInstanceBySerInsId(serverInstanceId);

		if (instanceList != null && !instanceList.isEmpty()) {
			return instanceList.get(0);

		}

		return null;
	}
	
	@Override
	@Transactional
	public ServerInstance getIDByIpPortUtility(String ip,int port,int utilityPort) {
		List<ServerInstance> instanceList = serverInstanceDao.getIDByIpPortUtility(ip,port,utilityPort);
		if (instanceList != null && !instanceList.isEmpty()) {
			return instanceList.get(0);
		}
		return null;
	}
	
	private String getVersionConfigName(int serverInstanceId, String serverInstanceName, String dateFormate) {
		int length = serverInstanceName.length();
		String name = "";
		VersionConfig versionConfig = versionConfigDao.getVersionConfigIndex(serverInstanceId);
		if(versionConfig!=null){
			String versionName = versionConfig.getName();
			int indexV = versionName.lastIndexOf("v");
			if(indexV>0){
				versionName  = versionName.substring(indexV+1);
				int version = Integer.parseInt(versionName);
				version++;
				if(length>=5){
					name = serverInstanceName.substring(0,5).replaceAll(" ", "_")+"_"+dateFormate+"_v"+version;//NOSONAR
				} else {
					name = serverInstanceName.replaceAll(" ", "_")+"_"+dateFormate+"_v"+version;//NOSONAR
				}
			} else {
				if(length>=5){
					name = serverInstanceName.substring(0,5).replaceAll(" ", "_")+"_"+dateFormate+"_v1";//NOSONAR
				} else {
					name = serverInstanceName.replaceAll(" ", "_")+"_"+dateFormate+"_v1";//NOSONAR
				}
			} 
			
		} else {
			if(length>=5){
				name = serverInstanceName.substring(0,5).replaceAll(" ", "_")+"_"+dateFormate+"_v1";//NOSONAR
			} else {
				name = serverInstanceName.replaceAll(" ", "_")+"_"+dateFormate+"_v1";//NOSONAR
			}
		}
		return name;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.PUBLISH_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION,
	currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject publishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId) {

		ResponseObject responseObject = new ResponseObject();
		String tempSyncPublishXmlPath = null;
		logger.info("Call for Sync & Publish Server Instance ");
		try{
			ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
			String serverInstanceName;
			if (serverInstance != null) {
				DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.SYNC_PUBLISH_DATE_TIME_FORMATTER);
				serverInstanceName = serverInstance.getName().replaceAll(" ", "_");//NOSONAR
				tempSyncPublishXmlPath = tempPathForSyncPublish + File.separator + serverInstanceName + "_" + serverInstance.getPort() + "_"
						+ dateFormatter.format(new Date()) + ".xml";
				String dateFormate = dateFormatter.format(new Date());
				responseObject = this.getServerInstanceJAXB(tempSyncPublishXmlPath, serverInstance.getId());
				@SuppressWarnings("unchecked")
				Map<String, Object> serverInstaceMap = (Map<String, Object>) responseObject.getObject();
				File serverInstXmlFile = (File) serverInstaceMap.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
				VersionConfig config = new VersionConfig();
				String versionConfigName =  this.getVersionConfigName(serverInstance.getId(),serverInstance.getName(),dateFormate);
				config.setFile(new javax.sql.rowset.serial.SerialBlob(EliteUtils.convertFileContentToBlob(serverInstXmlFile)));
				config.setServerInstance(serverInstance);
				config.setDescription(description);
				config.setName(versionConfigName);
				config.setCreatedByStaffId(staffId);
				config.setLastUpdatedByStaffId(staffId);
				Staff staff = staffDao.getStaffDetailsById(staffId);
				config.setPublishedBy(staff.getUsername());
				versionConfigDao.save(config);
				serverInstXmlFile.delete();
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_SUCCESS);
				responseObject.setObject(serverInstance);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
				responseObject.setObject(null);
			}
		} catch(Exception e) {//NOSONAR
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void migrateAllPassword(){
		SystemParameterData systemParameterData = systemParamService.getSystemParameterByAlias(SystemParametersConstant.MIGRATE_ALL_PASSWORD);
		if("true".equals(systemParameterData.getValue())){
			dataSourceService.migrateDataSourcePassword();
			systemParamService.migrateEmailPassword();
			snmpService.migrateSnmpClientPassword();
			systemParameterData.setValue("false");
			systemParamDataDao.merge(systemParameterData);
		}
	}
	
	@Transactional
	@Override
	public void generateNetServerDataFromServerInsance(ServerInstance serverInstance, CrestelNetServerData serverData ) {
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
		String engineSampleXmlPath = servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);

		Map<String, String> syncInputMap = new HashMap<>();

		syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, StateEnum.ACTIVE.getValue());
		syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
		syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
		syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);

		ResponseObject responseObject = null;
		if (syncInputMap != null) {
			jaxbXmlPath = syncInputMap.get(BaseConstants.JAXB_XML_PATH_CONSTANT);
			xsltFilePath = syncInputMap.get(BaseConstants.XSLT_PATH_CONSTANT);
			engineSampleXmlPath = syncInputMap.get(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT);

			responseObject = new ResponseObject();
			int iserverInstanceId = serverInstance.getId();
			try {
				logger.info("Sync for serverInstance: " + iserverInstanceId);
				serverInstance = getServerInstance(iserverInstanceId);
				logger.info("Perform sync operation on Server Instance" + iserverInstanceId);
				List<CrestelNetServiceData> serviceConfigList = new ArrayList<>(0);

				responseObject = getServerInstanceDepedants(serverInstance);
				if (responseObject.isSuccess()) {
					serverInstance = (ServerInstance) responseObject.getObject();
					List<CrestelNetConfigurationData> configurationList = getServerConfigSampleList(serverInstance,
							jaxbXmlPath, xsltFilePath, engineSampleXmlPath);

					// loop over services for sync
					List<Service> serviceList = serverInstance.getServices();
					if (serviceList != null) {
						int serviceSize = serviceList.size();
						for (int i = 0; i < serviceSize; i++) {
							Service service = serviceList.get(i);
							if (!(StateEnum.DELETED.equals(service.getStatus()))) {
								logger.info("Sync for Service Name: " + service.getSvctype().getAlias());
								serviceConfigList.add(servicesService.getServicesSampleDataForSync(service.getId(),
										service.getSvctype().getServiceFullClassName(), jaxbXmlPath, xsltFilePath,
										engineSampleXmlPath));
							}
						}
					}

					if (!configurationList.isEmpty()) {
						serverData.setNetServerId(String.valueOf(serverInstance.getServer().getId()));
						serverData.setNetServerName(serverInstance.getServer().getName());
						serverData.setNetConfigurationList(configurationList);
						serverData.setNetServiceList(serviceConfigList);
						for (CrestelNetConfigurationData data : configurationList) {
							logger.debug("********************Print configuration key going in sync to SI"
									+ data.getNetConfigurationKey());
						}

					}
				}
			} catch (SMException | JAXBException e) {//NOSONAR
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
				responseObject.setObject(null);
			}
		}
	}
	
	@Transactional
	public void updateAutoConfigSIStatus(int staffId, String ipAddress, int utilityPort, int port) {
		try {
			logger.info("Going to save autoConfigServerInstance. ");
			AutoConfigServerInstance autoConfigServerInstance = autoConfigServerInstanceDao.getAutoConfigSIByIPAndUtilityPortAndSIPort(ipAddress,utilityPort,port);
			if(autoConfigServerInstance != null) {
				autoConfigServerInstance.setLastUpdatedByStaffId(staffId);
				autoConfigServerInstance.setLastUpdatedDate(new Date());
				autoConfigServerInstance.setStatus(StateEnum.DELETED);
				byte[] cresteNetData = null;
				if(autoConfigServerInstance.getCrestelNetServerData() != null){
					cresteNetData = Utilities.convertObjectToByteArray(autoConfigServerInstance.getCrestelNetServerData());
					autoConfigServerInstance.setCrestelNetServerData(new javax.sql.rowset.serial.SerialBlob(cresteNetData));
				}else{
					cresteNetData = new byte[1];
				}
				autoConfigServerInstance.setCrestelNetServerData(new javax.sql.rowset.serial.SerialBlob(cresteNetData));
				
				autoConfigServerInstanceDao.merge(autoConfigServerInstance);
			} 
		} catch (Exception e) {
			logger.error("Exception Occured while saving or updating autoConfigServerInstance :" + e);
		}
	}
	
	@Override
	@Transactional
	public ResponseObject migrateAllServerInstanceConfigInDB() {
		ResponseObject responseObject = new ResponseObject();
		
		Map<String,List> serverAndServerInstanceMap = this.getAllServerAndItsInstance();
		if(serverAndServerInstanceMap!=null && serverAndServerInstanceMap.size()>0){				
			List<ServerInstance> listSI = serverAndServerInstanceMap.get(BaseConstants.SERVER_INSTANCE_LIST);
			
			for(ServerInstance serverInstance : listSI) {
				this.updateSyncFlagForServiceandSI(serverInstance.getServices(), serverInstance, false);
				
				String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
				String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
				String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
				Map<String,String> syncInputMap=new HashMap<>();
				syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, Integer.toString(serverInstance.getId()));
				syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, serverInstance.getStatus().toString());
				syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
				syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
				syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
				
				ResponseObject response = this.migrateExistingServerInstanceConfigInDB(syncInputMap, serverInstance);
				if(response.isSuccess()) {
					this.updateSyncFlagForServiceandSI(serverInstance.getServices(), serverInstance, true);
				}
			}
		}else{
			logger.info("Server and Server Instance Map is null or zero");
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject migrateExistingServerInstanceConfigInDB(Map<String, String> syncInputMap,
			ServerInstance serverInstance) {
		String serverInstancesId;
		String serverInstancesStatus;
		String jaxbXmlPath;
		String xsltFilePath;
		String engineSampleXmlPath;
		List<Object> syncObjectList = new ArrayList<>();
		ResponseObject responseObject = null;		
		ServerInstance serverInstanceAuditObj=null;
		if (syncInputMap != null) {
			serverInstancesId = syncInputMap.get(BaseConstants.SERVER_INSTANCES_ID);
			serverInstancesStatus = syncInputMap.get(BaseConstants.SERVER_INSTANCES_STATUS);
			jaxbXmlPath = syncInputMap.get(BaseConstants.JAXB_XML_PATH_CONSTANT);
			xsltFilePath = syncInputMap.get(BaseConstants.XSLT_PATH_CONSTANT);
			engineSampleXmlPath = syncInputMap.get(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT);
			if (serverInstancesId != null && serverInstancesStatus != null) {
				responseObject = new ResponseObject();
				String serverInstanceId = serverInstancesId;
				int iserverInstanceId = Integer.parseInt(serverInstanceId);
				try {
					logger.info("Sync for serverInstance: " + serverInstanceId);
					serverInstanceAuditObj=serverInstance;
					if (serverInstance.getStatus().equals(StateEnum.ACTIVE)) {
						logger.info("Server Instance" + serverInstanceId + " is running");
						if (!serverInstance.isSyncSIStatus() || !serverInstance.isSyncChildStatus()) {
							logger.info("Perform sync operation on Server Instance" + iserverInstanceId);
							List<CrestelNetServiceData> serviceConfigList = new ArrayList<>(0);
							responseObject = getServerInstanceDepedants(serverInstance);
							if (responseObject.isSuccess()) {
								serverInstance = (ServerInstance) responseObject.getObject();
								if(checkStorageLocationForPacketStatisticsAgent(serverInstance)){
									List<CrestelNetConfigurationData> configurationList = getServerConfigSampleList(serverInstance, jaxbXmlPath,
											xsltFilePath, engineSampleXmlPath);
									List<Service> serviceList = serverInstance.getServices();
									if (serviceList != null) {
										int serviceSize = serviceList.size();
										for (int i = 0; i < serviceSize; i++) {
											Service service = serviceList.get(i);
											if (!(StateEnum.DELETED.equals(service.getStatus()))) {
												serviceConfigList.add(servicesService.getServicesSampleDataForSync(service.getId(), service
														.getSvctype().getServiceFullClassName(), jaxbXmlPath, xsltFilePath, engineSampleXmlPath));
											}
										}
									}

									if (!configurationList.isEmpty()) {
										CrestelNetServerData serverData = new CrestelNetServerData();
										generateNetServerDataFromServerInsance(serverInstance, serverData);
										  											
										responseObject = resetSyncFlagForServiceandSI(serviceList, serverInstance);
										if (responseObject.isSuccess()) {
											logger.info("Synchronization Perform Successfully" + serverInstanceId);
											this.saveOrUpdateAutoConfigSI(serverData,serverInstance.getLastUpdatedByStaffId(),serverInstance.getServer().getIpAddress(), serverInstance.getServer().getUtilityPort(), serverInstance.getPort());
											responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_SUCCESS);
											responseObject.setObject(serverInstance);
											syncObjectList.add(serverInstance);
																							
											if (!responseObject.isSuccess()) {
												responseObject.setSuccess(false);
												responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);	
												responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
											}
										} else {
											logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
											responseObject.setSuccess(false);
											responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
											responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
										}										
									}
								} else {
									logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT);
									responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
								}
							} else {
								logger.info("Synchronization Not Perform Successfully" + serverInstanceId);
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
								responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
							}
						} else {
							logger.info("Server Instance is already synchronize" + serverInstanceId);
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_ALREADY_SYNC);
						}
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
						responseObject.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS);
					}
				} catch (Exception exception) {
					logger.info("Exception occure" + serverInstanceId);
					logger.error(exception.getMessage(), exception);
					logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVERINSTANCE_SYNCHRONIZATION_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
				}
				responseObject.setAuditObjectList(syncObjectList);
				responseObject.setObject(serverInstanceAuditObj);
			}
		}
		return responseObject;
	}

	@Override
	public ResponseObject updateSyncFlagForServiceandSI(List<Service> serviceList, ServerInstance serverInstance,
			boolean flag) {
		ResponseObject responseObject = new ResponseObject();
		int serviceSize = serviceList.size();
		for (int i = 0; i < serviceSize; i++) {
			Service service = serviceList.get(i);
			service = serviceDao.findByPrimaryKey(Service.class, service.getId());
			service.setSyncStatus(flag);
			serviceDao.updateForResetSyncFlagOfService(service);
		}
		serverInstance.setSyncSIStatus(flag);
		serverInstance.setSyncChildStatus(flag);

		serverInstanceDao.updateForResetSyncFlagofServerInstance(serverInstance);
		responseObject.setSuccess(true);

		return responseObject;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void migrateVersion(){
		SystemParameterData systemParameterData =systemParamService.getSystemParameterByAlias(SystemParametersConstant.VERSION_MIGRATE);
		systemParameterData.setValue(Boolean.TRUE.toString());
		systemParameterData.setStatus(StateEnum.INACTIVE);
		systemParamDataDao.merge(systemParameterData);
		MapCache.addConfigObject(systemParameterData.getAlias(), systemParameterData.getValue());
	
	}
	
	/*
	 * At the time of sync serverinstance and parsing service is configured with  
	 * and datasource is not enabled then revert the sync process
	 * */
	private boolean checkDBInitForParsingLicenseUtilization(ServerInstance serverInstance) {
		List<Service> services = serverInstance.getServices();
		if(services != null) {
			for(Integer i = 0;i<services.size();i++) {
				Service service = services.get(i);
				if(StateEnum.ACTIVE.equals(service.getStatus()) 
						&&  service.getSvctype().getTypeOfService().equals(ServiceTypeEnum.MAIN) 
						&& (service.getSvctype().getAlias().equals(EngineConstants.PARSING_SERVICE) 
								|| service.getSvctype().getAlias().equals(EngineConstants.IPLOG_PARSING_SERVICE))) {
					if(serverInstance.isDatabaseInit())
						return true;
					else
						return false;					
				}
			}			
		}
		return true;
	}
	
	private ResponseObject validateParsingServiceLicense(ServerInstance serverInstance, Map<String, ResponseObject> responseMap, String serverInstanceId) throws IOException {
		ResponseObject responseObject = new ResponseObject();
		boolean isValidCircle=false;
		boolean isDBInitCheckRequired=false;
		List<Service> services = serverInstance.getServices();
		Map<Integer, Map<String, Object>> map;		
		String msg="";
		if(services != null && !services.isEmpty()) {
			for(Service service : services) {
				if(StateEnum.ACTIVE.equals(service.getStatus()) 
						&&  service.getSvctype().getTypeOfService().equals(ServiceTypeEnum.MAIN) 
						&& (service.getSvctype().getAlias().equals(EngineConstants.PARSING_SERVICE))) {
					
					map=new LicenseUtility().extractCircleAndDeviceFromLicense(licenseService.getLicenseList(),servletcontext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator);
					isValidCircle=false;
					List<ParsingPathList> parsingPathLists = pathListDao.getParsingPathListByServiceId(service.getId());;
					if (parsingPathLists != null && !parsingPathLists.isEmpty()) {
						for (ParsingPathList path : parsingPathLists) {
							if (path.getCircle().getId() != 1) {
								if(map.containsKey(path.getCircle().getId())) {
									
									
									
									Map<String, Object> circleMap=map.get(path.getCircle().getId());
									if( circleMap.containsKey(LicenseTypeEnum.DEVICE.toString())) {
										List<String> devices = (List<String>) circleMap.get(LicenseTypeEnum.DEVICE.toString());
										if (devices.contains(path.getParentDevice().getDeviceType().getName())) {
											isValidCircle = true;
											isDBInitCheckRequired = true;
										}else {
											msg+="For Parsing service ("+service.getName()+"), please map pathlist ("+path.getName()+") with circle and device that has valid license association "
													+ "OR apply valid license key for selected circle ("+path.getCircle().getName()+") and device ("+path.getParentDevice().getDecodeType()+").";
										}
									}else {
										isValidCircle=true;
										isDBInitCheckRequired = true;
									}
									
									/*
									  List<String> devices=(List<String>)
									  map.get(path.getCircle().getId()).get(LicenseTypeEnum.FULL);
									  if(devices.contains(path.getParentDevice().getDeviceType().getName())) {
									  isValidCircle=true; isDBInitCheckRequired = true; }
									 */
								}else {
									msg+="For Parsing service ("+service.getName()+"), please map pathlist ("+path.getName()+") with circle that has valid license association "
											+ "OR apply valid license key for selected circle ("+path.getCircle().getName()+").";							
									isValidCircle = false;
									break;
								}
							} else {
								isValidCircle=true;
							}
						}
					}else {
						isValidCircle=true;
					}
				}else {
					responseObject.setSuccess(true);
					return responseObject;
				}
			}			
		} else {
			//case of new server instance without any services inside it. License check is not required.
			isValidCircle=true;
			isDBInitCheckRequired=false;
		}
		//circle is valid
		if(isValidCircle) {
			//valid circle is not default circle then check for db init flag required
			if(isDBInitCheckRequired) {
				if(checkDBInitForParsingLicenseUtilization(serverInstance)) {
					//every flag pass for license and circle so allow to sync the server/service
					responseObject.setSuccess(true);				
				}else {
					//db init must be enable since circle is not default and will be required db look up on engine side
					//so user must redirected to server instace advance configuration screen to configure db int
					logger.info("Synchronization Not Perform Successfully due to datasource not configured for parsing service to capture license utilization" + serverInstanceId);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_DATASOURCE_DISABLED_FOR_LICENSE);
					responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
					responseMap.put(serverInstanceId, responseObject);
				}				
			}else { //db init check is not required because all pathlist have only default circle associated
				responseObject.setSuccess(true);
			}
		}else { // if associated circle/device is not valid and not match with license applied circle / device 
			//skipping db init required check and user will get redirect popup in license manager screen to apply proper license
			logger.info("Invalid License. Please Service Mapped with Appropriate license" + serverInstanceId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_INVALID_LICENSE);
			responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
			responseObject.setMsg(msg);
			responseMap.put(serverInstanceId, responseObject);	
		}
		return responseObject;
	}
	
/**	public Map<Integer, Object> extractCircleAndDeviceFromLicense() {
		Map<Integer, Object> map=new HashMap<Integer, Object>();
		ResponseObject responseObject =licenseService.getLicenseList();
		if(responseObject.isSuccess()) {
			List<License> licenseList =(List<License>)responseObject.getObject();
			for (License license : licenseList) {
				if(license.getStatus().equals(StateEnum.ACTIVE)) {
					if(license.getCircle()!=null) {
						map.put(license.getCircle().getId(), extractLicenseDeviceFromKey(license.getTps()));
					}
				}	
			}
		}
		return map;
	}	
 * @throws IOException 
	**/
	

}