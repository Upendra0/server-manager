/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.VendorType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.device.service.VendorTypeService;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.BusinessSubModule;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.FileSequenceMgmt;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.wrapper.PolicyWrapper;
import com.elitecore.sm.productconfig.model.ProfileEntity;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.HostConfiguration;
import com.elitecore.sm.roaming.model.RoamingParameter;
import com.elitecore.sm.roaming.model.TestSimManagement;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerTypeService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.systemaudit.dao.SystemAuditDao;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditUserDetails;
import com.elitecore.sm.systemaudit.model.SearchStaffAudit;
import com.elitecore.sm.systemaudit.model.SystemAudit;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;
import com.elitecore.sm.systemparam.model.SystemParameterData;

/**
 * @author Ranjitsinh Reval
 *
 */

@org.springframework.stereotype.Service(value = "systemAuditService")
public class SystemAuditServiceImpl implements SystemAuditService{

	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private SystemAuditDao systemAuditDao;
	
	/**
	 * Method will get total staff audit List count.
	 * @param searchStaffAudit
	 * @return total count
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public long getTotalServiceInstancesCount(SearchStaffAudit searchStaffAudit) {
		
		Map<String, Object> auditConditions = systemAuditDao.getStaffAuditBySearchParamters(searchStaffAudit);
		return systemAuditDao.getQueryCount(SystemAudit.class, (List<Criterion>) auditConditions.get("conditions"),(HashMap<String, String>) auditConditions.get("aliases"));
	}

	/**
	 * Method will get all system audit list based on sort order, page index.
	 * @param service
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return SystemAuditList
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<SystemAudit> getPaginatedList(SearchStaffAudit searchStaffAudit, int startIndex, int limit, String sidx, String sord) {
		
		Map<String, Object> staffAuditConditions = systemAuditDao.getStaffAuditBySearchParamters(searchStaffAudit);
		List<SystemAudit> staffAuditList = systemAuditDao.getPaginatedList(SystemAudit.class,(List<Criterion>) staffAuditConditions.get("conditions"), (HashMap<String, String>) staffAuditConditions.get("aliases"),
																		  startIndex,limit, sidx, sord);
		
		getAuditActivityDetails(staffAuditList);
		return staffAuditList;
	}
	
	
	/**
	 * Method will iterate all activity details for current system audit list.
	 * @param staffAuditList
	 */
	private void getAuditActivityDetails(List<SystemAudit> staffAuditList){
		for (int i = 0; i < staffAuditList.size(); i++) {
			staffAuditList.get(i).getSystemAuditActivity().getAlias();
			Hibernate.initialize(staffAuditList.get(i).getSystemAuditDetails());
		}
	}
	
	/**
	 * @param ipAddress
	 * @param staffId
	 * @param userName
	 * @param currentAction
	 * @param modifiedPropsList
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public void addAuditDetails(AuditUserDetails auditUserDetails, AuditActivity currentAction,	List<SystemAuditDetails> modifiedPropsList, String actionType, String remarkMessage) {
		
		SystemAudit systemAudit = new SystemAudit();
		systemAudit.setIPAddress(auditUserDetails.getUserIpAddress());
		systemAudit.setStaffId(String.valueOf(auditUserDetails.getLoggedInStaffId()));
		systemAudit.setUserName(auditUserDetails.getLoggedInUserName());
		systemAudit.setActionType(actionType);
		systemAudit.setAuditDate(new Date());
		
		systemAudit.setRemark(remarkMessage);
		
		systemAudit.setSystemAuditActivity(currentAction);
		systemAudit.setSystemAuditEntity(currentAction.getAuditSubEntity().getAuditEntity());
		systemAudit.setSystemAuditSubEntity(currentAction.getAuditSubEntity());
		
		if(modifiedPropsList != null && !modifiedPropsList.isEmpty()){
			for (SystemAuditDetails systemAuditDetails : modifiedPropsList) {
				systemAuditDetails.setSystemAudit(systemAudit);
			}
		 systemAudit.setSystemAuditDetails(modifiedPropsList);
		}
		
		
		
		if((BaseConstants.UPDATE_ACTION.equalsIgnoreCase(actionType) &&   modifiedPropsList == null) &&  AuditConstants.UPDATE_PRODUCT_CONFIGURATION.equalsIgnoreCase(currentAction.getAlias())) {
			logger.info("No need to audit for null diff and update action");
		}else{
			systemAuditDao.addAuditDetails(systemAudit);	
		}
	}


	/**
	 * Method will find old entity from the second level cache with given id.
	 * @param Integer id
	 * @param Class clazz
	 * @return Object
	 */
	@Override
	@Transactional(readOnly = true)
	public BaseModel getOldEntity(Class<?> clazz, Integer id) {
		return systemAuditDao.getOldEntity(clazz, id);
	}

	/**
	 * Method will check instance of check for entity and get all required remark $ values.
	 * @param entity
	 * @return
	 */
	@Override
	public ResponseObject checkEntityAndGetName(Object entity) {
		ResponseObject responseObject  = new ResponseObject();
		Map<String, String> entityNameMap = new HashMap<>();
		
		ServerInstanceService serverInstanceService = (ServerInstanceService) SpringApplicationContext.getBean("serverInstanceService");
		ServicesService servicesService = (ServicesService) SpringApplicationContext.getBean("servicesService");
		DriversService driversService = (DriversService) SpringApplicationContext.getBean("driversService");

		DeviceService deviceService = (DeviceService) SpringApplicationContext.getBean("deviceService");
		
		ComposerService composerService = (ComposerService) SpringApplicationContext.getBean("composerService");
		PathListService pathListService = (PathListService)SpringApplicationContext.getBean("pathListService"); 
		ParserMappingService parserMappingService = (ParserMappingService) SpringApplicationContext.getBean("parserMappingService");
		ComposerMappingService composerMappingService = (ComposerMappingService) SpringApplicationContext.getBean("composerMappingService");
		DeviceTypeService deviceTypeService = (DeviceTypeService) SpringApplicationContext.getBean("deviceTypeService");
		VendorTypeService vendorTypeService = (VendorTypeService) SpringApplicationContext.getBean("vendorTypeService");
		ServerTypeService serverTypeService = (ServerTypeService) SpringApplicationContext.getBean("serverTypeService");
		AgentService agentService = (AgentService) SpringApplicationContext.getBean("agentService"); 
		SnmpService snmpService = (SnmpService)SpringApplicationContext.getBean("snmpService");
		logger.info("Object type found "  + entity.getClass());		
		
		if (entity instanceof Server) {
			Server server = (Server) entity;
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, server.getName());
				
		} else if (entity instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) entity;
	
			String entityName = serverInstance.getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME, entityName);
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, entityName);
			
		} else if (entity instanceof Service) {
			Service service = (Service) entity;
			ServerInstance instance = serverInstanceService.getServerInstance(service.getServerInstance().getId());
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVICE_TYPE, service.getSvctype().getType());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, instance.getName());
			
		} else if (entity instanceof Drivers) {
			Drivers driver = (Drivers) entity;
			Service service = servicesService.getServiceandServerinstance(driver.getService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, driver.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			
		} else if (entity instanceof NetflowClient) {
			NetflowClient netflowClient = (NetflowClient) entity;
			Service service = servicesService.getServiceandServerinstance(netflowClient.getService().getId());
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, netflowClient.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			
		} else if (entity instanceof ParsingPathList) {
			ParsingPathList pathList = (ParsingPathList) entity;
			Service service = servicesService.getServiceandServerinstance(pathList.getService().getId());
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			try {
				List<Parser> parserList  = pathList.getParserWrappers(); 
				if(parserList != null && !parserList.isEmpty()){
					entityNameMap.put(BaseConstants.PARSER_NAME, parserList.get(0).getName());  // For iplog parsing service pathlist 
																								//and plugin both are created in single action. 
				}
			} catch (Exception e) {
				logger.info("IP Log parsing path list found", e);
			}
			
		} else if (entity instanceof ProcessingPathList){
			ProcessingPathList pathList = (ProcessingPathList) entity;
			Service service = servicesService.getServiceandServerinstance(pathList.getService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			
		} else if (entity instanceof DataConsolidationPathList){
			DataConsolidationPathList pathList = (DataConsolidationPathList) entity;
			Service service = servicesService.getServiceandServerinstance(pathList.getService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			
		} else if (entity instanceof AggregationServicePathList){
			AggregationServicePathList pathList = (AggregationServicePathList) entity;
			Service service = servicesService.getServiceandServerinstance(pathList.getService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			
		}else if (entity instanceof PathList) {
			PathList pathList = (PathList) entity;
			Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
			Service service = servicesService.getServiceandServerinstance(driver.getService().getId());
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.DRIVER_NAME, driver.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());

		} else if (entity instanceof Staff) {
			Staff staff = (Staff) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, staff.getName());
			entityNameMap.put(BaseConstants.STAFF_NAME, staff.getName());
			
		} else if (entity instanceof AccessGroup) {
			AccessGroup accessGroup = (AccessGroup) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, accessGroup.getName());
			
		}else if (entity instanceof Parser) {
			Parser parser = (Parser) entity;
			
			PathList pathList = pathListService.getPathListById(parser.getParsingPathList().getId());
			Service service = servicesService.getServiceandServerinstance(pathList.getService().getId()); 
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());
			entityNameMap.put(BaseConstants.PARSER_NAME, parser.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());


		}else if (entity instanceof Composer) {
			
			Composer composer = (Composer) entity;

			PathList pathList = pathListService.getPathListById(composer.getMyDistDrvPathlist().getId());
			Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
			Service service = servicesService.getServiceandServerinstance(driver.getService().getId()); 
			entityNameMap.put(BaseConstants.ENTITY_NAME, composer.getName());
			entityNameMap.put(BaseConstants.DRIVER_NAME, driver.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());

		}else if (entity instanceof ParserAttribute) {
			
			ParserAttribute parserAttribute = (ParserAttribute) entity;			
			ParserMapping parserMapping = parserMappingService.getParserMappingById(parserAttribute.getParserMapping().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, "");
			if(parserMapping!=null)
				entityNameMap.put(BaseConstants.ENTITY_NAME, parserMapping.getName());			
			
		}else if (entity instanceof ComposerAttribute) {
			
			ComposerAttribute composerAttribute = (ComposerAttribute) entity;
			int composerMappingId = composerAttribute.getMyComposer().getId();
			ComposerMapping composerMapping = composerMappingService.getComposerMappingById(composerMappingId);

			entityNameMap.put(BaseConstants.ENTITY_NAME, composerMapping.getName());
		
		}else if (entity instanceof DataSourceConfig) {
			DataSourceConfig dataSourceConfig = (DataSourceConfig) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, dataSourceConfig.getName());
			
		}else if (entity instanceof KafkaDataSourceConfig) {
			KafkaDataSourceConfig kafkaDataSourceConfig = (KafkaDataSourceConfig) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, kafkaDataSourceConfig.getName());
			
		}else if (entity instanceof Device) {
			Device device = (Device) entity;
			
			ResponseObject deviceTypeResponseObject = deviceTypeService.getDeviceTypeById(device.getDeviceType().getId());
			DeviceType deviceType = (DeviceType) deviceTypeResponseObject.getObject();

			ResponseObject vendorTypeResponseObject = vendorTypeService.getVendorTypeById(device.getVendorType().getId());
			VendorType vendorType = (VendorType) vendorTypeResponseObject.getObject();
			
			entityNameMap.put(BaseConstants.ENTITY_NAME, device.getName());
			entityNameMap.put(BaseConstants.DEVICE_TYPE, deviceType.getName());
			entityNameMap.put(BaseConstants.VENDOR_TYPE, vendorType.getName());
		
		}else if (entity instanceof PacketStatisticsAgent) {
			PacketStatisticsAgent packetStatisticsAgent = (PacketStatisticsAgent) entity;
			ServerInstance instance = serverInstanceService.getServerInstance(packetStatisticsAgent.getServerInstance().getId());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, instance.getName());

		}else if (entity instanceof CharRenameOperation) {
			
			CharRenameOperation charRenameOperation = (CharRenameOperation) entity;
			Composer composerObj = charRenameOperation.getComposer();
			
			if(composerObj != null){
				Composer composer = composerService.getComposerById(charRenameOperation.getComposer().getId());
				PathList pathList = pathListService.getPathListById(composer.getMyDistDrvPathlist().getId());
				Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
				Service service = servicesService.getServiceandServerinstance(driver.getService().getId());
				
				entityNameMap.put(BaseConstants.ENTITY_NAME, composer.getName());	
				entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
				entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			} else if(charRenameOperation.getPathList() != null){
				PathList pathList = pathListService.getPathListById(charRenameOperation.getPathList().getId());
				Drivers driver = driversService.getDriverById(pathList.getDriver().getId());
				Service service = servicesService.getServiceandServerinstance(driver.getService().getId());
				entityNameMap.put(BaseConstants.ENTITY_NAME, pathList.getName());	
				entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
				entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
			}
			else{
				
				ServiceFileRenameConfig serviceFileRenameConfig =  agentService.getServiceFileRenameConfigByPrimaryKey(charRenameOperation.getSvcFileRenConfig().getId());

				Service serviceObj = servicesService.getServiceandServerinstance(serviceFileRenameConfig.getService().getId());
				entityNameMap.put(BaseConstants.SERVICE_NAME, serviceObj.getName());
				entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serviceObj.getServerInstance().getName());
			}
	
		}else if (entity instanceof SNMPServerConfig) {
			
			SNMPServerConfig snmpServerConfig = (SNMPServerConfig) entity;	
			ServerInstance instance = serverInstanceService.getServerInstance(snmpServerConfig.getServerInstance().getId());
			List<SNMPAlertWrapper> configuredAlerts = snmpServerConfig.getConfiguredAlerts();
			if(configuredAlerts.size()>0){
			SNMPAlert snmpAlertById = snmpService.getSnmpAlertById(configuredAlerts.get(0).getId());
			entityNameMap.put(BaseConstants.ALERT_NAME,snmpAlertById.getName());
			}
			entityNameMap.put(BaseConstants.ENTITY_NAME, snmpServerConfig.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, instance.getName());
			
			
		}else if (entity instanceof SNMPAlert) {
			
			SNMPAlert snmpAlert = (SNMPAlert) entity;			
			entityNameMap.put(BaseConstants.ENTITY_NAME, snmpAlert.getName());			
			
		}else if (entity instanceof ProfileEntity) {
			ProfileEntity profileEntity = (ProfileEntity) entity;
			ServerType serverType = serverTypeService.getServerTypeById(profileEntity.getServerType().getId());	
			entityNameMap.put(BaseConstants.ENTITY_NAME, profileEntity.getEntityAlias());
			entityNameMap.put(BaseConstants.SERVER_TYPE, serverType.getName());
			
		}else if (entity instanceof ParserMapping) {
			ParserMapping parserMapping = (ParserMapping) entity;
			String name = parserMapping.getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME, name);
			ResponseObject responseObj = deviceService.getDeviceByDeviceId(parserMapping.getDevice().getId(), "");
			if(responseObj.isSuccess()){
				Device device = (Device) responseObj.getObject();
				entityNameMap.put(BaseConstants.DEVICE_NAME, device.getName());
			}
			
		} else if (entity instanceof ComposerMapping) {
			ComposerMapping composerMapping = (ComposerMapping) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, composerMapping.getName());
			ResponseObject responseObj = deviceService.getDeviceByDeviceId(composerMapping.getDevice().getId(), "");
			if(responseObj.isSuccess()){
				Device device = (Device) responseObj.getObject();
				entityNameMap.put(BaseConstants.DEVICE_NAME, device.getName());
			}		
		}else if (entity instanceof SystemParameterData) {
			SystemParameterData systemParameters = (SystemParameterData) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME,systemParameters.getName());
			
		}else if (entity instanceof Policy) {
			Policy policy = (Policy) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(policy.getServer().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME,policy.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);
			
		}else if (entity instanceof PolicyAction) {
			PolicyAction policyAction = (PolicyAction) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(policyAction.getServer().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME,policyAction.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);
	
		}else if (entity instanceof ServiceFileRenameConfig) {
			ServiceFileRenameConfig serviceFileRenameConfig = (ServiceFileRenameConfig) entity;
			Service service = servicesService.getServiceandServerinstance(serviceFileRenameConfig.getService().getId());
			
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
		
		}else if (entity instanceof FileRenameAgent) {
			FileRenameAgent fileRenameAgent = (FileRenameAgent) entity;
			ServerInstance instance = serverInstanceService.getServerInstance(fileRenameAgent.getServerInstance().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, instance.getName());

		}else if (entity instanceof Agent) {
			Agent agent = (Agent) entity;

			entityNameMap.put(BaseConstants.ENTITY_NAME, agent.getAgentType().getAlias());
			entityNameMap.put(BaseConstants.AGENT_STATUS, agent.getAgentType().getStatus().toString());
	
		}else if (entity instanceof PolicyCondition) {
			PolicyCondition policyCondition = (PolicyCondition) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(policyCondition.getServer().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME,policyCondition.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);

		}else if (entity instanceof PolicyGroup) {
			PolicyGroup policyGroup = (PolicyGroup) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(policyGroup.getServer().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME, policyGroup.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);
			
		}else if (entity instanceof PolicyRule) {
			PolicyRule policyRule = (PolicyRule) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(policyRule.getServer().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME, policyRule.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);
			
		}else if (entity instanceof DataConsolidationMapping) {
			DataConsolidationMapping mapping = (DataConsolidationMapping) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, mapping.getMappingName());
		}else if (entity instanceof DatabaseDistributionDriverAttribute) {
			DatabaseDistributionDriverAttribute driverAttribute = (DatabaseDistributionDriverAttribute) entity;
			Drivers driver = driversService.getDriverById(driverAttribute.getDbDisDriver().getId());
			Service service = servicesService.getServiceandServerinstance(driver.getService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME,driver.getName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
		}else if (entity instanceof DataConsolidation){
			Service service = servicesService.getServiceandServerinstance(((DataConsolidation) entity).getConsService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, ((DataConsolidation) entity).getConsName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
		}else if(entity instanceof DatabaseQuery){
			DatabaseQuery databaseQuery = (DatabaseQuery) entity;
			String serverInstanceName = serverInstanceService.getServerInstance(databaseQuery.getServerInstance().getId()).getName();
			entityNameMap.put(BaseConstants.ENTITY_NAME, databaseQuery.getQueryName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, serverInstanceName);
		}else if(entity instanceof RuleLookupTableData){
			RuleLookupTableData ruleLookupTableData2 = (RuleLookupTableData) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, ruleLookupTableData2.getViewName());
		}else if (entity instanceof AggregationDefinition){
			Service service = servicesService.getServiceandServerinstance(((AggregationDefinition) entity).getAggregationService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME, ((AggregationDefinition) entity).getAggDefName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME, service.getServerInstance().getName());
		}
		else if(entity instanceof PolicyWrapper){
			List<Policy> policyList = ((PolicyWrapper) entity).getPolicyList();
			if(policyList != null && policyList.size() >0){
				Policy policy = policyList.get(0);
				String entityName = policy.getName();
				ServerInstance server = policy.getServer();
				String name = server.getName();
				entityNameMap.put(BaseConstants.ENTITY_NAME,entityName);
				entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME,name);
			}
		}
		else if(entity instanceof DataConsolidationGroupAttribute){
			Service serviceandServerinstance = servicesService.getServiceandServerinstance(((DataConsolidationGroupAttribute)entity).getDataConsolidation().getConsService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME,((DataConsolidationGroupAttribute)entity).getDataConsolidation().getConsName());
			entityNameMap.put(BaseConstants.SERVICE_NAME,serviceandServerinstance.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME,serviceandServerinstance.getServerInstance().getName());
			}
			
		else if(entity instanceof ServicePacketStatsConfig){
			Service service = servicesService.getServiceandServerinstance(((ServicePacketStatsConfig)entity).getService().getId());
			ServerInstance instance = serverInstanceService.getServerInstance(service.getServerInstance().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME,service.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME,instance.getName());
			
		}
			
		else if(entity instanceof DataConsolidationAttribute){
			Service serviceandServerinstance = servicesService.getServiceandServerinstance(((DataConsolidationAttribute)entity).getDataConsolidation().getConsService().getId());
			entityNameMap.put(BaseConstants.ENTITY_NAME,((DataConsolidationAttribute)entity).getDataConsolidation().getConsName());
			entityNameMap.put(BaseConstants.SERVICE_NAME, serviceandServerinstance.getName());
			entityNameMap.put(BaseConstants.SERVER_INSTANCE_NAME,serviceandServerinstance.getServerInstance().getName());
		} 
		
		else if(entity instanceof HostConfiguration){
			HostConfiguration hostConfiguration  = (HostConfiguration)entity;
			hostConfiguration.setId(1);
			entityNameMap.put(BaseConstants.ENTITY_NAME, SystemParametersConstant.HOST_CONFIGURATION);
			
		}
		else if(entity instanceof RoamingParameter){
			RoamingParameter roamingParameter = (RoamingParameter)entity;
			roamingParameter.setId(1);
			entityNameMap.put(BaseConstants.ENTITY_NAME,SystemParametersConstant.ROAMING_PARAMETERS);
		}else if(entity instanceof FileSequenceMgmt){
			FileSequenceMgmt fileSequence = (FileSequenceMgmt)entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME,fileSequence.getElementType());
			}
		else if(entity instanceof TestSimManagement){
			TestSimManagement fileSequence = (TestSimManagement)entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME,fileSequence.getType());
			}
		else if(entity instanceof FileManagementData){
			FileManagementData fileSequence = (FileManagementData)entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME,fileSequence.getServiceType());
			}
		else if (entity instanceof BusinessSubModule) {
			BusinessSubModule businessSubModule = (BusinessSubModule) entity;
			//ServerType serverType = serverTypeService.getServerTypeById(businessSubModule.get);	
			entityNameMap.put(BaseConstants.ENTITY_NAME, businessSubModule.getName());
			//entityNameMap.put(BaseConstants.SERVER_TYPE, serverType.getName());
			
		}
		else if (entity instanceof RoamingFileSequenceMgmt ){
			RoamingFileSequenceMgmt roamingFileSequenceMgmt = (RoamingFileSequenceMgmt)entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, roamingFileSequenceMgmt.getElementType()+roamingFileSequenceMgmt.getFileType());
		}else if (entity instanceof Circle) {
			Circle circle = (Circle) entity;
			entityNameMap.put(BaseConstants.ENTITY_NAME, circle.getName());
		} else if (entity instanceof License) {
			License license = (License) entity;
			if(license.getCircle()!=null)
				entityNameMap.put(BaseConstants.ENTITY_NAME, license.getCircle().getName());
			else
				entityNameMap.put(BaseConstants.ENTITY_NAME, null);
		}
		responseObject.setSuccess(true);
		responseObject.setObject(entityNameMap);
		return responseObject;
	}

	/**
	 * Method will set audit remark message with dynamic entity name.
	 * @param entityNameMap
	 * @param remarkMessage
	 * @return
	 */
	@Override
	public String setRemarks(Map<String, String> entityNameMap, String remarkMessage) {
		
		String remark = remarkMessage ; 
		if(remark != null){
			if (remark.indexOf("$entityName") >= 0) {
				remark = remark.replace("$entityName", "<b>" + entityNameMap.get(BaseConstants.ENTITY_NAME) + "</b>");
			}

			if (remark.indexOf("$serviceName") >= 0) {
				remark = remark.replace("$serviceName", "<b>" + entityNameMap.get(BaseConstants.SERVICE_NAME) + "</b>");
			}

			if(remark.indexOf("$serviceType") >= 0) {
				remark = remark.replace("$serviceType", "<b>" + entityNameMap.get(BaseConstants.SERVICE_TYPE) + "</b>");
			}

			if (remark.indexOf("$serverInstanceName") >= 0) {
				remark = remark.replace("$serverInstanceName", "<b>" + entityNameMap.get(BaseConstants.SERVER_INSTANCE_NAME) + "</b>");
			}

			if (remark.indexOf("$staffName") >= 0) {
				remark = remark.replace("$staffName", "<b>" + entityNameMap.get(BaseConstants.STAFF_NAME) + "</b>");
			}

			if (remark.indexOf("$driverName") >= 0) {
				remark = remark.replace("$driverName", "<b>" + entityNameMap.get(BaseConstants.DRIVER_NAME) + "</b>");
			}
			
			if (remark.indexOf("$deviceName") >= 0) {
				remark = remark.replace("$deviceName", "<b>" + entityNameMap.get(BaseConstants.DEVICE_NAME) + "</b>");
			}
		
			if (remark.indexOf("$deviceType") >= 0) {
				remark = remark.replace("$deviceType", "<b>" + entityNameMap.get(BaseConstants.DEVICE_TYPE) + "</b>");
			}
			
			if (remark.indexOf("$vendorType") >= 0) {
				remark = remark.replace("$vendorType", "<b>" + entityNameMap.get(BaseConstants.VENDOR_TYPE) + "</b>");
			}
			
			if (remark.indexOf("$parserName") >= 0) {
				remark = remark.replace("$parserName", "<b>" + entityNameMap.get(BaseConstants.PARSER_NAME) + "</b>");
			}
			
			if (remark.indexOf("$agentStatus") >= 0) {
				remark = remark.replace("$agentStatus", "<b>" + entityNameMap.get(BaseConstants.AGENT_STATUS) + "</b>");
			}
			
			if (remark.indexOf("$alertName") >= 0) {
				remark = remark.replace("$alertName", "<b>" + entityNameMap.get(BaseConstants.ALERT_NAME) + "</b>");
			}
			
			if (remark.indexOf("$serverType") >= 0) {
				remark = remark.replace("$serverType", "<b>" + entityNameMap.get(BaseConstants.SERVER_TYPE) + "</b>");
			}
			
			
		}else{
			logger.info("Remark message found null.");
		}
		return remark;
	}
	
}
