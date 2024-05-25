package com.elitecore.sm.agent.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.core.agent.CrestelAgentData;
import com.elitecore.sm.agent.dao.AgentDao;
import com.elitecore.sm.agent.dao.AgentTypeDao;
import com.elitecore.sm.agent.dao.ServiceFileRenameConfigDao;
import com.elitecore.sm.agent.dao.ServicePacketStatsConfigDao;
import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.agent.validator.AgentValidator;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

/**
 * 
 * @author avani.panchal
 *
 */
@org.springframework.stereotype.Service(value = "agentService")
public class AgentServiceImpl implements AgentService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	@Qualifier(value = "AgentDao")
	private AgentDao agentDao;

	@Autowired
	private AgentTypeDao agentTypeDao;

	@Autowired
	private ServiceFileRenameConfigDao serviceFileRenameConfigDao;
	
	@Autowired
	private ServicePacketStatsConfigDao servicePacketStatsConfigDao;

	@Autowired
	private ServerInstanceService serverInstanceService;

	@Autowired
	private AgentValidator agentValidator;

	@Autowired
	private ServicesService serviceService;
	
	/**
	 * Provide the total staff count based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalAgentCount(int serverInstanceId,List<AgentType> agentTypeList) {

		Map<String, Object> conditionsAndAliases = agentDao.createCriteriaConditions(serverInstanceId,agentTypeList);

		return agentDao.getQueryCount(Agent.class, (List<Criterion>) conditionsAndAliases.get("conditions"),
				(HashMap<String, String>) conditionsAndAliases.get("aliases"));
	}

	/**
	 * Provides the List of Server Instance based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getAgentPaginatedList(int serverInstanceId, int serviceId, String isServerInstanceSummary, int startIndex,
			int limit, String sidx, String sord,List<AgentType> agentTypeList) {

		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;

		Map<String, Object> conditionsAndAliases = agentDao.createCriteriaConditions(serverInstanceId,agentTypeList);

		List<Agent> agentList = agentDao.getAgentPaginatedList(Agent.class, (List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		if (agentList != null && !agentList.isEmpty()) {
			for (Agent agent : agentList) {
				row = new HashMap<>();
				logger.debug("Inside for loop for agent found from DB :: " + agent.getAgentType().getType());
				row.put("id", agent.getId());
				row.put("typeOfAgent", agent.getAgentType().getType());
				row.put("agentTypeId", agent.getAgentType().getId());

				if ("false".equals(isServerInstanceSummary)) {
					ServicePacketStatsConfig svcDetail = servicePacketStatsConfigDao.getServiceDetailByServiceIdAndAgentId(serviceId, agent.getId());
					ServiceFileRenameConfig svcDetailFileRename = serviceFileRenameConfigDao.getServiceDetailByServiceIdAndAgentId(serviceId, agent.getId());
					if (svcDetail != null) {
						row.put("serviceStatus", String.valueOf(svcDetail.isEnable()));
						row.put("agentStatus", String.valueOf(agent.getStatus()));

						rowList.add(row);
					}
					else if (svcDetailFileRename!=null){
						row.put("serviceStatus", String.valueOf(true));
						row.put("agentStatus", String.valueOf(agent.getStatus()));

						rowList.add(row);
					}
				}
				else{
				row.put("agentStatus", String.valueOf(agent.getStatus()));

				rowList.add(row);
				}
			}
		}
		return rowList;

	}

	/**
	 * Load Agent running information
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject loadAgentInfomation(int serverInstanceId, String agentType) {

		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstance = serverInstanceService.getServerInstance(serverInstanceId);
		
		RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
				serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
		List<CrestelAgentData> agentDataList = jmxConnection.readAgentTasks();

		JSONObject agentObject = new JSONObject();
		boolean isAgentFound = false;
		if (agentDataList != null && !agentDataList.isEmpty()) {
			logger.debug("Found agent data list from JMX Call" + agentDataList.size());
			for (CrestelAgentData agentData : agentDataList) {

				String agentName = agentData.getAgentName();
				logger.debug("Inside for loop for agent found in jmx call:: " + agentName);
				String fileRenameAgent = "File Rename Agent";
				if(agentType.equals(fileRenameAgent)){
					agentType = "File Renaming Agent";
				}
				if (agentType.equals(agentName)) {
					logger.debug("Add Data For Agent Name::: " + agentName);
					agentObject.put(BaseConstants.LAST_EXECUATION_DATE, agentData.getLastExecutionTime() + "");
					agentObject.put(BaseConstants.NEXT_EXECUATION_DATE, agentData.getNextExecutionTime() + "");
					isAgentFound = true;
					break;
				} else {
					isAgentFound = false;
				}
			}

			if (!isAgentFound) {
				logger.debug("Add Manual Data For Agent Name::: " + agentType);

				agentObject.put(BaseConstants.LAST_EXECUATION_DATE, "-");
				agentObject.put(BaseConstants.NEXT_EXECUATION_DATE, "-");
			}
		} else {
			logger.debug("Agent List Found Null Form JMX API");
			agentObject.put(BaseConstants.LAST_EXECUATION_DATE, "-");
			agentObject.put(BaseConstants.NEXT_EXECUATION_DATE, "-");
		}

		responseObject.setSuccess(true);
		responseObject.setObject(agentObject);

		return responseObject;

	}

	/**
	 * Fetch All Agent Type
	 */
	@Override
	@Transactional(readOnly = true)
	public List<AgentType> getAllAgentType() {
		return agentTypeDao.getAllAgentType();
	}

	/**
	 * Return agent object by serverInstanceId and AgentTypeId
	 * 
	 * @param serverInstanceId
	 * @param agentTypeId
	 * @return Agent
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getAgentByServerInstanceIdAndAgentTypeID(int serverInstanceId, int agentTypeId) {
		ResponseObject responseObject = new ResponseObject();

		List<Agent> agentList = agentDao.getAgentByServerInstanceIdAndAgentTypeID(serverInstanceId, agentTypeId);
		
		if (agentList != null && !agentList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(agentList.get(0));
		}
		return responseObject;
	}	

	/**
	 * Fetch Agent Type using alias
	 * 
	 * @param alias
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public AgentType getAgentTypeByAlias(String alias) {
		return agentTypeDao.getAgentTypeByAlias(alias);
	}

	/**
	 * Validate service parameter for import operation
	 * 
	 * @param service
	 * @param importErrorList
	 * @return
	 */
	@Transactional
	@Override
	public List<ImportValidationErrors> validateAgentForImport(Agent agent, List<ImportValidationErrors> importErrorList) {
		logger.debug("Validate Agent Parameter for import");
		agentValidator.validateAgentParam(agent, null, importErrorList, "Agent", true);
		return importErrorList;
	}

	/**
	 * Iterate Over Agent Detail
	 * 
	 * @param agent
	 * @param serverInstance
	 * @param svcId
	 * @param isImport
	 */
	@Transactional(readOnly = true)
	@Override
	public Agent iterateOverAgent(Agent agent, ServerInstance serverInstance, Map<Integer, Integer> svcId, boolean isImport, int importMode) {

		List<ServicePacketStatsConfig> svcPktConfigList;
		List<ServiceFileRenameConfig> serviceFileRenameConfList;
		
		if (importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			if (isImport) { // import call

				logger.debug("Import Agent");
				agent.setId(0);
				agent.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				agent.setCreatedDate(new Date());
				agent.setServerInstance(serverInstance);
				agent.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());

			} else { // delete call
				logger.debug("Delete Agent");
				agent.setStatus(StateEnum.DELETED);
				agent.setLastUpdatedDate(new Date());
			}
		}

		agent.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		agent.setLastUpdatedDate(new Date());

		if (agent instanceof PacketStatisticsAgent) {
			logger.debug("Packet Statistis Agent Found");
			PacketStatisticsAgent packetStatAgent = (PacketStatisticsAgent) agent;

			svcPktConfigList = iterateOverPacketStatAgentDepedants(packetStatAgent, null, svcId, isImport);
			packetStatAgent.setServiceList(svcPktConfigList);
			packetStatAgent.setAgentType(agentTypeDao.getAgentTypeByAlias(BaseConstants.PACKET_STATISTICS_AGENT));

		}
		if (agent instanceof FileRenameAgent) {
			logger.debug("File Rename Agent Found");
			FileRenameAgent fileRenameAgent = (FileRenameAgent) agent;

			serviceFileRenameConfList = iterateOverFileRenameAgentDepedants(fileRenameAgent, null, svcId, isImport);
			fileRenameAgent.setServiceList(serviceFileRenameConfList);
			fileRenameAgent.setAgentType(agentTypeDao.getAgentTypeByAlias(BaseConstants.FILE_RENAME_AGENT));

		}
		return agent;
	}

	/**
	 * Iterate over packet statistic agent dependents
	 * 
	 * @param agent
	 * @param svcId
	 * @param isImport
	 * @return
	 */
	public List<ServicePacketStatsConfig> iterateOverPacketStatAgentDepedants(PacketStatisticsAgent agent, PacketStatisticsAgent dbAgent,
			Map<Integer, Integer> svcId, boolean isImport) {

		List<ServicePacketStatsConfig> svcPktConfigList = agent.getServiceList();
		List<ServicePacketStatsConfig> newSvcPktConfigList=new ArrayList<>();

		if (svcPktConfigList != null && !svcPktConfigList.isEmpty()) {

			for (int i = 0, size = svcPktConfigList.size(); i < size; i++) {
				ServicePacketStatsConfig svcPktStat = svcPktConfigList.get(i);
				if (svcPktStat != null &&  !StateEnum.DELETED.equals(svcPktStat.getStatus())) {
				if (isImport) {

					logger.debug("Services found from imported file:: " + svcPktConfigList.size());

					svcPktStat.setId(0);
					svcPktStat.setAgent(agent);
					
					logger.debug("Import Service Packet Stats Config MAP :: " + svcId);
					if (svcPktStat.getService() != null && !StateEnum.DELETED.equals(svcPktStat.getService().getStatus())) {
						int newSvcId = svcId.get(svcPktStat.getService().getId());
						Service svcObj = serviceService.getServiceandServerinstance(newSvcId);
						if (svcObj != null) {
							svcPktStat.setService(svcObj);
						}
					} else {
						logger.debug("Service found null from imported file");
					}

					svcPktStat.setCreatedByStaffId(agent.getCreatedByStaffId());
					svcPktStat.setCreatedDate(new Date());

				} else {
					logger.debug("delete Service Packet Stats Config");
					svcPktStat.setStatus(StateEnum.DELETED);
				}
				svcPktStat.setLastUpdatedByStaffId(agent.getLastUpdatedByStaffId());
				svcPktStat.setLastUpdatedDate(new Date());
				newSvcPktConfigList.add(svcPktStat);
			}
			}
		}

		return newSvcPktConfigList;
	}

	/**
	 * Update packet statastic details
	 * 
	 * @param packetStatasticAgent
	 * @return
	 */

	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_AGENT_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = PacketStatisticsAgent.class ,ignorePropList= "serviceList,serverInstance")
	public ResponseObject updatePacketStatasticDetail(PacketStatisticsAgent packetStatasticAgent) {
		ResponseObject responseObject = new ResponseObject();


			logger.debug("PacketStatastic Agent Id is::"+packetStatasticAgent.getId());
		
			if (packetStatasticAgent.getId() != 0) {
				Agent agentDB = agentDao.findByPrimaryKey(Agent.class, packetStatasticAgent.getId());
				
				if (agentDB != null && agentDB instanceof PacketStatisticsAgent) {
					logger.debug("Inside updatePacketStatastic Detail  :::"+agentDB.getId()+"::"+agentDB.getAgentType().getAlias());
					PacketStatisticsAgent packetStatasticAgentDB=(PacketStatisticsAgent)agentDB;
						logger.debug("Inside updatePacketStatasticDetail:::"+"packetStatasticAgentDB details"+packetStatasticAgentDB.getId()+"::"+packetStatasticAgentDB.getAgentType().getAlias());
					packetStatasticAgentDB.setExecutionInterval(packetStatasticAgent.getExecutionInterval());
					packetStatasticAgentDB.setStorageLocation(packetStatasticAgent.getStorageLocation());
					packetStatasticAgentDB.setLastUpdatedByStaffId(packetStatasticAgent.getLastUpdatedByStaffId());
					packetStatasticAgentDB.setLastUpdatedDate(new Date());
					logger.debug("Inside updatePacketStatasticDetail:::"+packetStatasticAgentDB.getId()+"::"+packetStatasticAgentDB.getAgentType().getAlias());
					agentDao.merge(packetStatasticAgentDB);
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.PACKET_SATASTIC_AGENT_UPDATE_SUCCESS);

		} else {
			logger.debug("Packet Statastic Agent not found from DB");

			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PACKET_SATASTIC_AGENT_UPDATE_FAIL);
		}
			}
		return responseObject;
	}

	/**
	 * Get total number of records for grid
	 * 
	 * @param agentId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getPacketStatasticServiceListtotalCount(int agentId) {
		Map<String, Object> agentServiceConditions = servicePacketStatsConfigDao.getPacketStatServicePaginatedList(agentId);
		return servicePacketStatsConfigDao.getQueryCount(ServicePacketStatsConfig.class,
				(List<Criterion>) agentServiceConditions.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) agentServiceConditions.get(BaseConstants.ALIASES));
	}

	/**
	 * Put details in Grid Row
	 * 
	 * @param agentId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getPacketStatasticServicePaginatedList(int agentId, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = servicePacketStatsConfigDao.getPacketStatServicePaginatedList(agentId);

		List<ServicePacketStatsConfig> servicePacketStatsList = servicePacketStatsConfigDao.getPacketStatServicePaginatedList(
				ServicePacketStatsConfig.class, (List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		if (servicePacketStatsList != null && !servicePacketStatsList.isEmpty()) {
			for (ServicePacketStatsConfig servicePacketStats : servicePacketStatsList) {
				row = new HashMap<>();

				row.put("id", servicePacketStats.getId());
				row.put(BaseConstants.SERVICEPACKETSTATS_NAME, servicePacketStats.getService().getName());
				row.put(BaseConstants.SERVICEPACKETSTATS_ID, servicePacketStats.getService().getServInstanceId());
				row.put(BaseConstants.SERVICEPACKETSTATS_TYPE, servicePacketStats.getService().getSvctype().getType());
				row.put(BaseConstants.SERVICEPACKETSTATS_CATEGORY, servicePacketStats.getService().getSvctype().getServiceCategory().name());
				row.put(BaseConstants.ENABLE, String.valueOf(servicePacketStats.isEnable()));
				row.put(BaseConstants.SERVER_TYPE, servicePacketStats.getService().getServerInstance().getServer().getServerType().getAlias());
				rowList.add(row);

			}
		}
		return rowList;
	}

	/**
	 * Change PacketStatastic Agent Status
	 * 
	 * @param svcAgentId
	 * @param svcAgentStatus
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_AGENT_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = ServicePacketStatsConfig.class ,ignorePropList= "agent,service")
	public ResponseObject changePacketStatasticStatus(int svcAgentId, String svcAgentStatus) throws CloneNotSupportedException {
		 


		 ResponseObject responseObject = getPacketStatServiceById(svcAgentId);
			if (responseObject.isSuccess()) {
				ServicePacketStatsConfig servicePacketStatObj = (ServicePacketStatsConfig) responseObject.getObject();
				if (servicePacketStatObj != null) {

					if ("true".equals(svcAgentStatus.trim())) {
						servicePacketStatObj.setEnable(true);
					} else {
						servicePacketStatObj.setEnable(false);
					}

					servicePacketStatsConfigDao.merge(servicePacketStatObj);

					responseObject.setResponseCode(ResponseCode.PACKET_SATASTIC_AGENT_UPDATE_SUCCESS);
					responseObject.setSuccess(true);


			} else {
				responseObject.setResponseCode(ResponseCode.PACKET_SATASTIC_AGENT_UPDATE_FAIL);
				responseObject.setSuccess(false);
			}

		} else {
			responseObject.setResponseCode(ResponseCode.PACKET_SATASTIC_AGENT_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Get ServicePacketStatastic Obj by ID
	 * 
	 * @param packatStatServiceId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	public ResponseObject getPacketStatServiceById(int packatStatServiceId) {

		ResponseObject responseObject = new ResponseObject();

		logger.debug("inside getSnmpServerById : Fetch configuration for SNMP Server");
		ServicePacketStatsConfig servicePacketStatObj = servicePacketStatsConfigDao.getPacketStatService(packatStatServiceId);
		if (servicePacketStatObj != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(servicePacketStatObj);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SVC_PACKET_SATASTIC_AGENT_NOT_FOUND);
		}

		return responseObject;

	}

	/**
	 * Change Agent Status
	 * 
	 * @param id
	 * @param agentStatus
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_FILERENAMEAGENT_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = FileRenameAgent.class ,ignorePropList= "serviceList,serverInstance")
	public ResponseObject updateAgentStatus(int id, String agentStatus) throws CloneNotSupportedException {

		logger.debug("Going to update agent status for id " + id + " to status " + agentStatus);
		ResponseObject responseObject = new ResponseObject();

		Agent agent = agentDao.findByPrimaryKey(Agent.class, id);

		if (agent != null) {
			if (StateEnum.ACTIVE.name().equals(agentStatus.trim())) {
				agent.setStatus(StateEnum.ACTIVE);
			} else {
				agent.setStatus(StateEnum.INACTIVE);
			}

			agentDao.merge(agent);
			logger.debug("Agent status has been updated successfully.");
			responseObject.setResponseCode(ResponseCode.AGENT_UPDATE_SUCCESS);
			responseObject.setSuccess(true);

		} else {
			logger.info("Failed to update service status.");
			responseObject.setResponseCode(ResponseCode.AGENT_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Add new services to agent when import and add option is selected
	 */
	@Transactional
	@Override
	public List<ServicePacketStatsConfig> addNewServicesToPacketStatAgent(List<ServicePacketStatsConfig> svcPktConfigList,
			PacketStatisticsAgent dbAgent, Map<Integer, Integer> svcId) {

			List<ServicePacketStatsConfig> dbSvcPktConfigList;
		if(dbAgent.getServiceList() !=null){
			dbSvcPktConfigList = dbAgent.getServiceList();
		}else{
			dbSvcPktConfigList = new ArrayList<>();
		}

		if (svcPktConfigList != null && !svcPktConfigList.isEmpty()) {

			for (int i = 0, size = svcPktConfigList.size(); i < size; i++) {
				ServicePacketStatsConfig svcPktStat = svcPktConfigList.get(i);
				if (svcPktStat != null &&  !StateEnum.DELETED.equals(svcPktStat.getStatus())) {
				logger.debug("Import Service Packet Stats Config :: " + svcPktConfigList.size());
				
				logger.debug("Import Service Packet Stats Config MAP :: " + svcId);
				if (svcPktStat.getService() != null && !StateEnum.DELETED.equals(svcPktStat.getService().getStatus())) {
					svcPktStat.setId(0);
					svcPktStat.setAgent(dbAgent);

					logger.debug("Fetch service Import Service Packet Stats Config MAP :: " + svcPktStat.getService().getId());

					int newSvcId = svcId.get(svcPktStat.getService().getId());
					Service svcObj = serviceService.getServiceandServerinstance(newSvcId);
					if (svcObj != null) {
						svcPktStat.setService(svcObj);
					}
					svcPktStat.setCreatedByStaffId(dbAgent.getCreatedByStaffId());
					svcPktStat.setCreatedDate(new Date());

					svcPktStat.setLastUpdatedByStaffId(dbAgent.getLastUpdatedByStaffId());
					svcPktStat.setLastUpdatedDate(new Date());

					dbSvcPktConfigList.add(svcPktStat);
					
				} else {
					logger.debug("Service found null from imported file");
				}

				
			}
			}
		}

		return dbSvcPktConfigList;
	}
	
	/**
	 * Delete service packet stat config
	 * @param agentList
	 * @param serviceId
	 */
	@Auditable(auditActivity = AuditConstants.DELETE_AGENT, actionType = BaseConstants.DELETE_ACTION, currentEntity = Agent.class ,ignorePropList= "")
	public void deletePacketStatAgentServicePktStat(List<Agent> agentList,int serviceId){
		logger.debug("inside deletePacketStatAgentServicePktStat for delete service packet stat bind with service :  "+ serviceId);
		if(agentList !=null && !agentList.isEmpty()){
			for(int j =0 ,size1=agentList.size();j<size1;j++){
				Agent agent=agentList.get(j);
			if (agent instanceof PacketStatisticsAgent) {
				logger.debug("Packet Statistis Agent Found");
				PacketStatisticsAgent packetStatAgent = (PacketStatisticsAgent) agent;
				List<ServicePacketStatsConfig> svcPktConfigList=packetStatAgent.getServiceList();
				for(int i=0,size=svcPktConfigList.size();i<size;i++){
					ServicePacketStatsConfig svcConfig=svcPktConfigList.get(i);
					Service service=svcConfig.getService();
					if(serviceId == service.getId()){
						logger.debug("inside deletePacketStatAgentServicePktStat found service packet config  ");
						svcConfig.setStatus(StateEnum.DELETED);
						servicePacketStatsConfigDao.merge(svcConfig);
					}
				}
			  }
			}
		}
	}
	
	/**
	 * Iterate over packet statistic agent dependents
	 * 
	 * @param agent
	 * @param svcId
	 * @param isImport
	 * @return
	 */
	public List<ServiceFileRenameConfig> iterateOverFileRenameAgentDepedants(FileRenameAgent agent, FileRenameAgent dbAgent,
			Map<Integer, Integer> svcId, boolean isImport) {

		List<ServiceFileRenameConfig> serviceFileRenameConfigList = agent.getServiceList();
		List<ServiceFileRenameConfig> newSvcPktConfigList=new ArrayList<>();

		if (serviceFileRenameConfigList != null && !serviceFileRenameConfigList.isEmpty()) {

			for (int i = 0, size = serviceFileRenameConfigList.size(); i < size; i++) {
				ServiceFileRenameConfig svcPktStat = serviceFileRenameConfigList.get(i);
				if (svcPktStat != null &&  !StateEnum.DELETED.equals(svcPktStat.getStatus())) {
				if (isImport) {

					logger.debug("Services found from imported file:: " + serviceFileRenameConfigList.size());

					svcPktStat.setId(0);
					svcPktStat.setAgent(agent);
					
					logger.debug("Import Service Packet Stats Config MAP :: " + svcId);
					if (svcPktStat.getService() != null && !StateEnum.DELETED.equals(svcPktStat.getService().getStatus())) {
						int newSvcId = svcId.get(svcPktStat.getService().getId());
						Service svcObj = serviceService.getServiceandServerinstance(newSvcId);
						if (svcObj != null) {
							svcPktStat.setService(svcObj);
						}
					} else {
						logger.debug("Service found null from imported file");
					}

					svcPktStat.setCreatedByStaffId(agent.getCreatedByStaffId());
					svcPktStat.setCreatedDate(new Date());

				} else {
					logger.debug("delete Service Packet Stats Config");
					svcPktStat.setStatus(StateEnum.DELETED);
				}
				svcPktStat.setLastUpdatedByStaffId(agent.getLastUpdatedByStaffId());
				svcPktStat.setLastUpdatedDate(new Date());
				newSvcPktConfigList.add(svcPktStat);
			}
			}
		}

		return newSvcPktConfigList;
	}
	
	/**
	 * Method will Add service to file renaming agent 
	 * @param serviceFileRenameConfig
	 */	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_SERVICE_TO_FILE_RENAME_AGENT, actionType = BaseConstants.CREATE_ACTION, currentEntity = ServiceFileRenameConfig.class ,ignorePropList= "charRenameOpList")
	public ResponseObject addServiceToFileRenamingAgent(ServiceFileRenameConfig serviceFileRenameConfig){
		
		ResponseObject responseObject = new ResponseObject();	
		
		if (serviceFileRenameConfig.getId() == 0){ 				
				ServerInstance serverInstance = serverInstanceService.getServerInstance(serviceFileRenameConfig.getAgent().getServerInstance().getId());
				
				Service service = serviceService.getAllServiceDepedantsByServiceId(serviceFileRenameConfig.getService().getId());
				serviceFileRenameConfig.setService(service);
				logger.info("Inside addServiceToFileRenamingAgent and adding service to file rename agent :");
				serviceFileRenameConfig.setCreatedByStaffId(serviceFileRenameConfig.getCreatedByStaffId());
				serviceFileRenameConfig.setCreatedDate(serviceFileRenameConfig.getCreatedDate());
				serviceFileRenameConfigDao.save(serviceFileRenameConfig,serverInstance);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_ADD_SUCCESS);
		}	
		else {
				logger.info("Fail to add service to file rename agent:");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_ADD_FAIL);
			}
				
		return responseObject;
	}
	
	/**
	 * Method will update service of file renaming agent 
	 * @param serviceFileRenameConfig
	 */	
	@Transactional
	@Override	
	@Auditable(auditActivity = AuditConstants.UPDATE_SERVICE_TO_FILE_RENAME_AGENT, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ServiceFileRenameConfig.class ,ignorePropList= "charRenameOpList,service,agent")
	public ResponseObject updateServiceToFileRenamingAgent(ServiceFileRenameConfig serviceFileRenameConfig){
		
		ResponseObject responseObject = new ResponseObject();	
		int serviceFileRenameConfigId = serviceFileRenameConfig.getId();
		
		if (serviceFileRenameConfigId != 0){
			ServerInstance serverInstance = serverInstanceService.getServerInstance(serviceFileRenameConfig.getAgent().getServerInstance().getId());
			
				logger.info("Inside updateServiceToFileRenamingAgent and updating service configuration :");	
				Service service = serviceService.getAllServiceDepedantsByServiceId(serviceFileRenameConfig.getService().getId());
				serviceFileRenameConfig.setService(service);
				
				serverInstanceService.updateSynchStatus(serverInstance);
				serviceFileRenameConfigDao.update(serviceFileRenameConfig);
				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_UPDATE_SUCCESS);				
			}	
			else {
				logger.info("Fail to update service configuration for file rename agent :");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_UPDATE_FAIL);
			}
		
		return responseObject;
	}
	
	/**
	 * Update file rename agent details(initial delay, execution interval)
	 * 
	 * @param FileRenameAgentEntity
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_FILE_RENAME_AGENT_DETAILS, actionType = BaseConstants.UPDATE_ACTION, currentEntity = FileRenameAgent.class ,ignorePropList= "serviceList,serverInstance,agentType")		
	public ResponseObject updateFileRenameAgentDetail(FileRenameAgent fileRenameAgent) {
		ResponseObject responseObject = new ResponseObject();
		
			logger.debug("FileRenameAgent Id is::"+fileRenameAgent.getId());
		
			if (fileRenameAgent.getId() != 0) {
				Agent agentDB = agentDao.findByPrimaryKey(Agent.class, fileRenameAgent.getId());
				
				if (agentDB != null && agentDB instanceof FileRenameAgent) {
					
					logger.debug("Inside updateFileRenameAgent Detail  :::"+agentDB.getId()+"::"+agentDB.getAgentType().getAlias());
					FileRenameAgent fileRenameAgentDB=(FileRenameAgent)agentDB;
					
					fileRenameAgentDB.setInitialDelay(fileRenameAgent.getInitialDelay());
					fileRenameAgentDB.setExecutionInterval(fileRenameAgent.getExecutionInterval());
					fileRenameAgentDB.setLastUpdatedByStaffId(fileRenameAgent.getLastUpdatedByStaffId());
					fileRenameAgentDB.setLastUpdatedDate(new Date());
					
					logger.debug("Inside updateFileRenameAgentDetails:::"+ fileRenameAgentDB.getId() +"::"+fileRenameAgentDB.getAgentType().getAlias());
					
					agentDao.merge(fileRenameAgentDB);
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_UPDATE_SUCCESS);

				}else {
					logger.info("Fail to update file rename agent details :");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_UPDATE_FAIL);
				}
			}
			else{
				logger.debug("Fail to update file rename agent details, agent id :"+fileRenameAgent.getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_UPDATE_FAIL);				
			}
		return responseObject;
	}
	
	/**
	 * Method will give count of services configured with file rename agent.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getFileRenameAgentDetailsCount(){

		logger.info("Fetching count of services for file rename agent :");
		
		Map<String, Object> fileRenameAgentConditions = serviceFileRenameConfigDao.getFileRenameAgentDetailsCount();
		
		return serviceFileRenameConfigDao.getQueryCount(ServiceFileRenameConfig.class, (List<Criterion>) fileRenameAgentConditions.get("conditions"), (HashMap<String, String>) fileRenameAgentConditions.get("aliases"));
	}
	
	
	/**
	 * Method will fetch service data for file rename agent
	 * 
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<ServiceFileRenameConfig> getPaginatedList(int startIndex, int limit, String sidx, String sord,int serverInstanceId) {

		logger.info("Fetching file service details for file rename agent details :");
		
		return serviceFileRenameConfigDao.getFileRenameAgentPaginatedList(ServiceFileRenameConfig.class, startIndex, limit, sidx, sord,serverInstanceId);
	}
	
	
	/**
	 * Method will delete service from file rename agent 
	 * @param id
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_SERVICE_FROM_FILE_RENAME_AGENT, actionType = BaseConstants.DELETE_ACTION, currentEntity = ServiceFileRenameConfig.class ,ignorePropList= "")
	public ResponseObject deleteServiceFileRenameAgentConfig(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		ServiceFileRenameConfig serviceFileRenameConfig  = serviceFileRenameConfigDao.findByPrimaryKey(ServiceFileRenameConfig.class, id);
		
		if(serviceFileRenameConfig != null ){
			ServerInstance serverInstance = serverInstanceService.getServerInstance(serviceFileRenameConfig.getAgent().getServerInstance().getId());
			serviceFileRenameConfig.setStatus(StateEnum.DELETED);
			serviceFileRenameConfig.setLastUpdatedByStaffId(staffId);
			serviceFileRenameConfig.setLastUpdatedDate(new Date());
			
			List<CharRenameOperation> charRenameOperationList = serviceFileRenameConfig.getCharRenameOpList();
			
			if(charRenameOperationList != null){
	
				for (CharRenameOperation charRenameOperation : charRenameOperationList) {
					charRenameOperation.setStatus(StateEnum.DELETED);
					charRenameOperation.setLastUpdatedByStaffId(staffId);					
					charRenameOperation.setLastUpdatedDate(new Date());
				}
			}
			serviceFileRenameConfig.setCharRenameOpList(charRenameOperationList);
			
			serverInstanceService.updateSynchStatus(serverInstance);
			serviceFileRenameConfigDao.merge(serviceFileRenameConfig);
			
			logger.info("Service from file rename agent deleted successfully.");
			responseObject.setSuccess(true);
			responseObject.setObject(serviceFileRenameConfig);
			responseObject.setResponseCode(ResponseCode.SERVICE_FILE_RENAME_AGENT_DELETE_SUCCESS);
			
		}else{
			logger.info("Fail to delete service from file rename agent");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_FILE_RENAME_AGENT_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Method will fetch services which are not already configured to file rename agent
	 * 
	 * 
	 */
	@Override
	@Transactional
	public  ResponseObject getServicesforFileRenameAgent(int serverInstanceId){
		
		ResponseObject responseObject = new ResponseObject();
		
		List<Service> servList = serviceFileRenameConfigDao.getServicesforFileRenameAgent(serverInstanceId);
			
		Map<Integer, String> dropDownServiceMap = new HashMap<>();
	
		if (servList != null && !servList.isEmpty()) {
				
			for (Service service : servList) {

				int serviceId = service.getId();
				String serviceName = service.getName();
				
				dropDownServiceMap.put(serviceId, serviceName);
			}
					
			responseObject.setSuccess(true);
			responseObject.setObject(dropDownServiceMap);
			logger.info("Data successfully retrieved :");
		} 
		else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_CONFIGURED_SERVICE_NOT_FOUND );
			logger.info("Failed to retrieve the data from service table or no service found in the table :");
		}
		
		return responseObject;
	}
	
	/**
	 * Method will fetch service file rename config object from primary key
	 * 
	 * 
	 */
	@Override
	@Transactional
	public  ServiceFileRenameConfig getServiceFileRenameConfigByPrimaryKey(int serviceFileRenameConfigId){
		
		return serviceFileRenameConfigDao.findByPrimaryKey(ServiceFileRenameConfig.class, serviceFileRenameConfigId);
	}

	/**
	 * Method will create new agent object in data base.
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject createAgent(Agent agent) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Creating new agent object.");
		agentDao.save(agent);
		if (agent.getId() > 0) {
			logger.info("Agent details added successfully!");
			responseObject.setSuccess(true);
		} else {
			logger.info("Failed to add agent details.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_CREAT_AGENT);
		}
		return responseObject;
	}

	
	/**
	 * Method will add service configuration from exported file and add it to current file rename agent configuration.
	 */
	@Override
	@Transactional
	public List<ServiceFileRenameConfig> addNewServicesToFileRenameAgent(List<ServiceFileRenameConfig> serviceFileRenameConfig, FileRenameAgent dbAgent, Map<Integer, Integer> svcId) {
		logger.debug("Adding new service configuration to file rename agent existing configuration.");

		List<ServiceFileRenameConfig> dbServiceFileRenameConfigList;

		if (dbAgent.getServiceList() != null) {
			dbServiceFileRenameConfigList = dbAgent.getServiceList();
		} else {
			dbServiceFileRenameConfigList = new ArrayList<>();
		}

		if (serviceFileRenameConfig != null && !serviceFileRenameConfig.isEmpty()) {
			logger.debug("Found service file rename configuration." );
		
			for (int i = 0, size = serviceFileRenameConfig.size(); i < size; i++) {
				ServiceFileRenameConfig serviceFileRenameConfigObj = serviceFileRenameConfig.get(i);
				
				if (serviceFileRenameConfigObj != null && !StateEnum.DELETED.equals(serviceFileRenameConfigObj.getStatus())) {
					logger.debug("Import Service in file stat config :: " + serviceFileRenameConfig.size());

					logger.debug("Import Service Packet Stats Config MAP :: " + svcId);
					if (serviceFileRenameConfigObj.getService() != null && !StateEnum.DELETED.equals(serviceFileRenameConfigObj.getService().getStatus())) {
						
						serviceFileRenameConfigObj.setId(0);
						serviceFileRenameConfigObj.setAgent(dbAgent);

						logger.debug("Fetch service Import Service Packet Stats Config MAP :: "	+ serviceFileRenameConfigObj.getService().getId());

						int newSvcId = svcId.get(serviceFileRenameConfigObj.getService().getId()); 
						Service svcObj = serviceService.getServiceandServerinstance(newSvcId);
						if (svcObj != null) {
							serviceFileRenameConfigObj.setService(svcObj);
						}
						
						serviceFileRenameConfigObj.setCreatedByStaffId(dbAgent.getCreatedByStaffId());
						serviceFileRenameConfigObj.setCreatedDate(new Date());
						serviceFileRenameConfigObj.setLastUpdatedByStaffId(dbAgent.getLastUpdatedByStaffId());
						serviceFileRenameConfigObj.setLastUpdatedDate(new Date());
						
						List<CharRenameOperation> charRenameOperationList = serviceFileRenameConfigObj.getCharRenameOpList();
						
						if(charRenameOperationList != null && !charRenameOperationList.isEmpty()){
							for (CharRenameOperation charRenameOperation : charRenameOperationList) {
								
								if(!StateEnum.DELETED.equals(charRenameOperation.getStatus())){
									charRenameOperation.setId(0);
									charRenameOperation.setCreatedByStaffId(dbAgent.getCreatedByStaffId());
									charRenameOperation.setCreatedDate(new Date());
									charRenameOperation.setLastUpdatedByStaffId(dbAgent.getLastUpdatedByStaffId());
									charRenameOperation.setLastUpdatedDate(new Date());
									charRenameOperation.setSvcFileRenConfig(serviceFileRenameConfigObj);
								}
							} 
							serviceFileRenameConfigObj.setCharRenameOpList(charRenameOperationList);
						}else{
							logger.debug("Character rename operation list found null!");
						}
						
						dbServiceFileRenameConfigList.add(serviceFileRenameConfigObj);
					} else {
						logger.debug("Service found null from imported file");
					}
				}
			}
		}

		return dbServiceFileRenameConfigList;
	}
	
	@Override
	public void importAgent(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("import : going to add/update agent in server instance : "+dbServerInstance.getName());
		List<Agent> dbAgentList = agentDao.getAgentByServerInstanceIdAndAgentTypeID(dbServerInstance.getId(), 0);
		List<Agent> exportedAgentList = exportedServerInstance.getAgentList();
		if(CollectionUtils.isEmpty(dbAgentList)) {
			importAgentAddMode(exportedAgentList, dbServerInstance);
		} else {
			importAgentUpdateMode(dbAgentList, exportedAgentList, importMode);
		}
	}
	
	public void importAgentAddMode(List<Agent> exportedAgentList, ServerInstance serverInstance) {
		logger.debug("going to add agents in server instance : "+serverInstance.getName());
		if(!CollectionUtils.isEmpty(exportedAgentList)) {
			int length = exportedAgentList.size();
			for(int i = length-1; i >= 0; i--) {
				Agent exportedAgent = exportedAgentList.get(i);
				if(exportedAgent != null && !exportedAgent.getStatus().equals(StateEnum.DELETED)) {
					exportedAgent.setId(0);
					exportedAgent.setCreatedDate(EliteUtils.getDateForImport(false));
					exportedAgent.setLastUpdatedDate(EliteUtils.getDateForImport(false));
					exportedAgent.setServerInstance(serverInstance);
					if(exportedAgent instanceof FileRenameAgent) {
						FileRenameAgent exportedFileRenameAgent = (FileRenameAgent) exportedAgent;
						List<ServiceFileRenameConfig> exportedConfigList = exportedFileRenameAgent.getServiceList();
						if(!CollectionUtils.isEmpty(exportedConfigList)) {
							int configLength = exportedConfigList.size();
							for(int j = configLength-1; j >= 0; j--) {
								ServiceFileRenameConfig exportedConfig = exportedConfigList.get(j);
								if(exportedConfig != null && !exportedConfig.getStatus().equals(StateEnum.DELETED)) {
									addServiceFileRenameConfigForImport(exportedConfig, exportedFileRenameAgent);
								}
							}
						}
					} else if(exportedAgent instanceof PacketStatisticsAgent) {
						PacketStatisticsAgent exportedPacketStatisticsAgent = (PacketStatisticsAgent) exportedAgent;
						List<ServicePacketStatsConfig> exportedConfigList = exportedPacketStatisticsAgent.getServiceList();
						if(!CollectionUtils.isEmpty(exportedConfigList)) {
							int configLength = exportedConfigList.size();
							for(int j = configLength-1; j >= 0; j--) {
								ServicePacketStatsConfig config = exportedConfigList.get(j);
								if(config != null && !config.getStatus().equals(StateEnum.DELETED)) {
									addServicePacketStatsConfigForImport(config, exportedPacketStatisticsAgent);
								}
							}
						}
					}
				}
			}
			serverInstance.setAgentList(exportedAgentList);
		}
	}
	
	public void importAgentUpdateMode(List<Agent> dbAgentList, List<Agent> exportedAgentList, int importMode) {
		logger.debug("going to update agents in server instance");
		if(!CollectionUtils.isEmpty(exportedAgentList)) {
			int length = exportedAgentList.size();
			for(int i = length-1; i >= 0; i--) {
				Agent exportedAgent = exportedAgentList.get(i);
				if(exportedAgent != null && !exportedAgent.getStatus().equals(StateEnum.DELETED)) {
					Agent dbAgent = getAgentFromList(dbAgentList, exportedAgent.getAgentType().getAlias());
					if(dbAgent != null) {
						dbAgent.setInitialDelay(exportedAgent.getInitialDelay());
						dbAgent.setExecutionInterval(exportedAgent.getExecutionInterval());
						if(dbAgent instanceof FileRenameAgent && exportedAgent instanceof FileRenameAgent) {
							logger.debug("going to update file rename agent");
							importFileRenameAgentUpdateMode((FileRenameAgent) dbAgent, (FileRenameAgent) exportedAgent, importMode);
							dbAgentList.add(dbAgent);
						} else if(dbAgent instanceof PacketStatisticsAgent && exportedAgent instanceof PacketStatisticsAgent) {
							logger.debug("going to update packet statistics agent");
							importPacketStatisticsAgentUpdateMode((PacketStatisticsAgent) dbAgent, (PacketStatisticsAgent) exportedAgent, importMode);
							dbAgentList.add(dbAgent);
						}
					}
				}
			}
		}
	}
	
	public void importFileRenameAgentUpdateMode(FileRenameAgent dbAgent, FileRenameAgent exportedAgent, int importMode) {
		logger.debug("going to add service file rename config in file rename agent");
		List<ServiceFileRenameConfig> dbServiceFileRenameconfigList = dbAgent.getServiceList();
		List<ServiceFileRenameConfig> exportedServiceFileRenameconfigList = exportedAgent.getServiceList();
		List<ServiceFileRenameConfig> newServiceFileRenameconfigList = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(exportedServiceFileRenameconfigList)) {
			int length = exportedServiceFileRenameconfigList.size();
			for(int i = length-1; i >= 0; i--) {
				ServiceFileRenameConfig exportedConfig = exportedServiceFileRenameconfigList.get(i);
				if(exportedConfig != null && !exportedConfig.getStatus().equals(StateEnum.DELETED)) {
					ServiceFileRenameConfig dbConfig = getServiceFileRenameConfigFromList(dbServiceFileRenameconfigList, exportedConfig.getService().getName());
					if(dbConfig == null) {
						addServiceFileRenameConfigForImport(exportedConfig, dbAgent);
						if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
							newServiceFileRenameconfigList.add(exportedConfig);
						} else {
							dbServiceFileRenameconfigList.add(exportedConfig);
						}
					}
				}
			}
		}		
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			dbServiceFileRenameconfigList.clear();
			if(CollectionUtils.isEmpty(newServiceFileRenameconfigList)) {
				int length = newServiceFileRenameconfigList.size();
				for(int i = length-1; i >= 0; i--) {
					dbServiceFileRenameconfigList.add(newServiceFileRenameconfigList.get(i));
				}
			}
		}
	}
	
	public void addServiceFileRenameConfigForImport(ServiceFileRenameConfig exportedConfig, FileRenameAgent agent) {
		exportedConfig.setId(0);
		exportedConfig.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedConfig.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedConfig.setAgent(agent);
		List<CharRenameOperation> exportedCharRenameOperationList = exportedConfig.getCharRenameOpList();
		if(!CollectionUtils.isEmpty(exportedCharRenameOperationList)) {
			int charLength = exportedCharRenameOperationList.size();
			for(int j = charLength-1; j >= 0; j--) {
				CharRenameOperation exportedCharRenameOperation = exportedCharRenameOperationList.get(j);
				if(exportedCharRenameOperation != null && !exportedCharRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					exportedCharRenameOperation.setId(0);
					exportedCharRenameOperation.setCreatedDate(EliteUtils.getDateForImport(false));
					exportedCharRenameOperation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
					exportedCharRenameOperation.setComposer(null);
					exportedCharRenameOperation.setSvcFileRenConfig(exportedConfig);
				}
				
			}
		}
		Service dbService = serverInstanceService.getServiceFromServerInstance(agent.getServerInstance(), exportedConfig.getService().getName());
		if(dbService != null) {
			exportedConfig.setService(dbService);
		}
	}
	
	public void addServicePacketStatsConfigForImport(ServicePacketStatsConfig exportedConfig, PacketStatisticsAgent agent) {
		exportedConfig.setId(0);
		exportedConfig.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedConfig.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedConfig.setAgent(agent);
		Service dbService = serverInstanceService.getServiceFromServerInstance(agent.getServerInstance(), exportedConfig);
		if(dbService != null) {
			exportedConfig.setService(dbService);
		}
	}
	
	public void importPacketStatisticsAgentUpdateMode(PacketStatisticsAgent dbAgent, PacketStatisticsAgent exportedAgent, int importMode) {
		logger.debug("going to add service packet stats config in packet statistics agent");
		
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE || StringUtils.isEmpty(dbAgent.getStorageLocation())) {
			dbAgent.setStorageLocation(exportedAgent.getStorageLocation());
		}
		
		List<ServicePacketStatsConfig> dbServicePacketStatsConfigList = dbAgent.getServiceList();
		List<ServicePacketStatsConfig> exportedServicePacketStatsConfigList = exportedAgent.getServiceList();
		List<ServicePacketStatsConfig> newServicePacketStatsConfigList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(exportedServicePacketStatsConfigList)) {
			int length = exportedServicePacketStatsConfigList.size();
			for(int i = length-1; i >= 0; i--) {
				ServicePacketStatsConfig exportedConfig = exportedServicePacketStatsConfigList.get(i);
				if(exportedConfig != null && !exportedConfig.getStatus().equals(StateEnum.DELETED)) {
					ServicePacketStatsConfig dbConfig = getServicePacketStatsConfigFromList(dbServicePacketStatsConfigList, exportedConfig);
					if(dbConfig == null) {
						addServicePacketStatsConfigForImport(exportedConfig, dbAgent);
						if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
							newServicePacketStatsConfigList.add(exportedConfig);
						} else {
							if(dbServicePacketStatsConfigList == null)
								dbServicePacketStatsConfigList = new ArrayList<ServicePacketStatsConfig>();
							dbServicePacketStatsConfigList.add(exportedConfig);
						}
					}
				}
			}
		}
		if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
			dbServicePacketStatsConfigList.clear();
			if(CollectionUtils.isEmpty(newServicePacketStatsConfigList)) {
				int length = newServicePacketStatsConfigList.size();
				for(int i = length-1; i >= 0; i--) {
					dbServicePacketStatsConfigList.add(newServicePacketStatsConfigList.get(i));
				}
			}
		}
	}

	public Agent getAgentFromList(List<Agent> agentList, String agentType) {
		if(!CollectionUtils.isEmpty(agentList)) {
			int length = agentList.size();
			for(int i = length-1; i >= 0; i--) {
				Agent agent = agentList.get(i);
				if(agent != null && !agent.getStatus().equals(StateEnum.DELETED)
						&& agent.getAgentType().getAlias().equalsIgnoreCase(agentType)) {
					return agentList.remove(i);
				}
			}
		}
		return null;
	}
	
	public ServiceFileRenameConfig getServiceFileRenameConfigFromList(List<ServiceFileRenameConfig> configList, String serviceName) {
		if(!CollectionUtils.isEmpty(configList)) {
			int length = configList.size();
			for(int i = length-1; i >= 0; i--) {
				ServiceFileRenameConfig config = configList.get(i);
				if(config != null && !config.getStatus().equals(StateEnum.DELETED)
						&& config.getService().getName().equalsIgnoreCase(serviceName)) {
					return config;
				}
			}
		}
		return null;
	}
	
	public ServicePacketStatsConfig getServicePacketStatsConfigFromList(List<ServicePacketStatsConfig> configList, ServicePacketStatsConfig exportedConfig) {
		String exportedServiceType = exportedConfig.getService().getSvctype().getAlias();
		String exportedServiceInstanceId = exportedConfig.getService().getServInstanceId();
		if(!CollectionUtils.isEmpty(configList)) {
			int length = configList.size();
			for(int i = length-1; i >= 0; i--) {
				ServicePacketStatsConfig config = configList.get(i);
				if(config != null && !config.getStatus().equals(StateEnum.DELETED)
						&& config.getService().getSvctype().getAlias().equalsIgnoreCase(exportedServiceType) 
						&& config.getService().getServInstanceId().equalsIgnoreCase(exportedServiceInstanceId)) {
					return config;
				}
			}
		}
		return null;
	}
}