/**http://stackoverflow.com/questions/10826293/restful-authentication-via-spring
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.agent.validator.AgentValidator;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitsinh
 *
 */
@org.springframework.stereotype.Service("agentMigrationService")
public class AgentMigrationServiceImpl implements AgentMigrationService {

	private static Logger logger = Logger.getLogger(AgentMigrationServiceImpl.class);

	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private AgentService agentService;
	
	@Autowired
	private AgentValidator agentValidator;
	
	@Autowired 
	private ServicesService servicesService;
	
	/** 
	 * Method will get UN-MARSHAL ,validate and get agent object based on type of agent. 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED )
	public ResponseObject getAgentObject(ServerInstance serverInstance) throws MigrationSMException {
		logger.debug("Migration process start for agent.");
		ResponseObject responseObject  =  getPacketStatisticAgent(serverInstance); 
		if(responseObject.isSuccess()){
		 responseObject = getFileRenameAgent(serverInstance);
		}
		return responseObject;
	}

	/**
	 * Method will get packet statistics agent object from XML file content.
	 * @return
	 * @throws MigrationSMException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRED )
	public ResponseObject getPacketStatisticAgent(ServerInstance serverInstance) throws MigrationSMException {
		ResponseObject responseObject ;
		logger.debug("Getting packet statistics object.");
		
		responseObject = migrationUtil.getFileContentFromMap(EngineConstants.PACKET_STATISTICS_AGENT);
		if(responseObject.isSuccess()){
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.PACKET_STATISTICS_AGENT_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					logger.info("JAXB unamarshalling done successfully for packet statistics object.");
					Map<String, Object> returnObjectsMap = (Map<String, Object>) responseObject.getObject();
					PacketStatisticsAgent packetStatistics = (PacketStatisticsAgent) returnObjectsMap.get(MigrationConstants.MAP_SM_CLASS_KEY);
					if(packetStatistics != null ){
						
						setAgentBasicDetails(packetStatistics, serverInstance, EngineConstants.PACKET_STATISTICS_AGENT); // Setting basic details.
						
						List<ServicePacketStatsConfig> serviceList  = packetStatistics.getServiceList();
						List<ServicePacketStatsConfig> serviceListFinal  =new ArrayList<>();
						if(serviceList != null && !serviceList.isEmpty()){
							
							for (int i = 0; i < serviceList.size(); i++) {
								
								ServicePacketStatsConfig servicePacketConfig =  serviceList.get(i); 
								String serviceKey = servicePacketConfig.getService().getSvctype().getAlias() + "-"+serverInstance.getId();
								Service service = (Service) MapCache.getConfigValueAsObject(serviceKey);
								
								if(service != null){
									
									servicePacketConfig.setService(service);
									servicePacketConfig.setStatus(StateEnum.ACTIVE);
									migrationUtil.setCurrentDateAndStaffId(servicePacketConfig, serverInstance.getCreatedByStaffId() );
									servicePacketConfig.setId(0);
									servicePacketConfig.setAgent(packetStatistics);
									serviceListFinal.add(servicePacketConfig);
								}
							}
						}
						packetStatistics.setServiceList(serviceListFinal);
						responseObject  = validateAndSaveAgentDetails(packetStatistics);
						
						displayPacketStatisticsAgentDetails(packetStatistics);
					}
					
					
				}
			}
		}
		return responseObject;
	}
	
	
	
	/**
	 * Method will get file rename agent object from xml file.
	 * @param serverInstance
	 * @return
	 * @throws MigrationSMException
	 */
	@SuppressWarnings({ "unchecked" })
	@Transactional(propagation=Propagation.REQUIRED )
	public ResponseObject getFileRenameAgent(ServerInstance serverInstance) throws MigrationSMException{
		logger.debug("Starting migration process for file rename agent");
		 ResponseObject responseObject = migrationUtil.getFileContentFromMap(MigrationConstants.FILE_RENAMING_AGENT);
		if(responseObject.isSuccess()){
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.FILE_RENAME_AGENT_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					logger.info("JAXB unamarshalling done successfully for packet statistics object.");
					Map<String, Object> returnObjectsMap = (Map<String, Object>) responseObject.getObject();
					FileRenameAgent fileRenameAgent = (FileRenameAgent) returnObjectsMap.get(MigrationConstants.MAP_SM_CLASS_KEY);
					if(fileRenameAgent != null ){
						
						setAgentBasicDetails(fileRenameAgent, serverInstance, BaseConstants.FILE_RENAME_AGENT);
						List<ServiceFileRenameConfig> serviceList  = fileRenameAgent.getServiceList();
						if(serviceList != null && !serviceList.isEmpty()){
							
							for (int i = 0; i < serviceList.size(); i++) {
								
								ServiceFileRenameConfig serviceFileRenameconfig =  serviceList.get(i); 
								String serviceKey = serviceFileRenameconfig.getService().getSvctype().getAlias() + "-"+serverInstance.getId();//Here will get name like Collection_SERVICE-000-91
								Service service = (Service) MapCache.getConfigValueAsObject(serviceKey);
								
								if(service != null){
									serviceFileRenameconfig.setService(service);
									serviceFileRenameconfig.setStatus(StateEnum.ACTIVE);
									migrationUtil.setCurrentDateAndStaffId(serviceFileRenameconfig, serverInstance.getCreatedByStaffId() );
									serviceFileRenameconfig.setId(0);
									serviceFileRenameconfig.setAgent(fileRenameAgent);
									
									List<CharRenameOperation> charRenameOpList = serviceFileRenameconfig.getCharRenameOpList();
									if(charRenameOpList != null && !charRenameOpList.isEmpty()){
										for (CharRenameOperation charRenameOperation : charRenameOpList) {
											
											charRenameOperation.setStatus(StateEnum.ACTIVE);
											migrationUtil.setCurrentDateAndStaffId(charRenameOperation, serverInstance.getCreatedByStaffId() );
											charRenameOperation.setId(0);
											charRenameOperation.setComposer(null);
											charRenameOperation.setSvcFileRenConfig(serviceFileRenameconfig);
											
										}
									}
									
									responseObject  = validateAndSaveAgentDetails(fileRenameAgent);
									if(!responseObject.isSuccess()){
										break;
									}
								}
							}
						}
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FILE_RENAME_NULL);
						throw new MigrationSMException(responseObject, "File Rename Agent object found null.");
					}
				}
			}
		}
		return responseObject;
	}
	
	
	/**
	 * Method will set basic details for agents.
	 * @param agent
	 * @param serverInstance
	 * @param agentType
	 */
	private void setAgentBasicDetails(Agent agent, ServerInstance serverInstance, String agentType){
		AgentType agentTypeObj  = (AgentType) MapCache.getConfigValueAsObject(agentType);
		agent.setAgentType(agentTypeObj);
		agent.setId(0);
		agent.setStatus(StateEnum.ACTIVE);
		agent.setServerInstance(serverInstance);
		migrationUtil.setCurrentDateAndStaffId(agent, serverInstance.getCreatedByStaffId());
	}
	
	/**
	 * Method will validate and add agent detail in database.
	 * @param agent
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED )
	public ResponseObject validateAndSaveAgentDetails(Agent agent) throws MigrationSMException {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to add agent details to database.");
		
		List<ImportValidationErrors> importValidationError = new ArrayList<>();
		agentValidator.validateAgentParam(agent, null, importValidationError, "Agent", true);
		if(!importValidationError.isEmpty()){
			logger.debug("Validation failed for agent business validation!");
			 JSONArray finaljArray=new JSONArray();
			
			 for(ImportValidationErrors errors:importValidationError){
				 JSONArray jArray=new JSONArray();
				 jArray.put(errors.getModuleName());
				 jArray.put(errors.getEntityName());
				 jArray.put(errors.getPropertyName());
				 jArray.put(errors.getPropertyValue());
				 jArray.put(errors.getErrorMessage());
				 finaljArray.put(jArray);
			 }
			 
			 responseObject.setSuccess(false);
			 responseObject.setObject(finaljArray);
			 return responseObject;
		}else{
			logger.info("Validation done successfully for agent details.");
			return  agentService.createAgent(agent);
		}
		
	}
	/**
	 * Method will display all packet statistics parameters,
	 * @param packetStatistics
	 */
	private void displayPacketStatisticsAgentDetails(PacketStatisticsAgent packetStatistics){
		logger.info("Execution interval ::" + packetStatistics.getExecutionInterval());
		logger.info("Initial delay ::" + packetStatistics.getInitialDelay());
		logger.info("Service List size ::" + packetStatistics.getServiceList());
		logger.info("Storage Location  ::" + packetStatistics.getStorageLocation());
		
		
		List<ServicePacketStatsConfig> serviceList  = packetStatistics.getServiceList();
		if(serviceList != null && !serviceList.isEmpty()){
			for (ServicePacketStatsConfig servicePacketStatsConfig : serviceList) {
				logger.info("Service Name ::" + servicePacketStatsConfig.getService().getSvctype().getAlias());
				logger.info("Enable value ::" + servicePacketStatsConfig.isEnable());
			}
		}
	}
	
	
}
