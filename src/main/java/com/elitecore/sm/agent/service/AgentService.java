package com.elitecore.sm.agent.service;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * 
 * @author avani.panchal
 *
 */
public interface AgentService {

	public long getTotalAgentCount(int serverInstanceId,List<AgentType> agentTypeList);

	public List<AgentType> getAllAgentType();

	public ResponseObject getAgentByServerInstanceIdAndAgentTypeID(int serverInstanceId, int agentTypeId);

	public AgentType getAgentTypeByAlias(String alias);

	public ResponseObject updatePacketStatasticDetail(PacketStatisticsAgent packetStatasticAgent);

	public long getPacketStatasticServiceListtotalCount(int agentId);

	public List<Map<String, Object>> getPacketStatasticServicePaginatedList(int agentId, int startIndex, int limit, String sidx, String sord);

	public List<Map<String, Object>> getAgentPaginatedList(int serverInstanceId, int serviceId, String isServerInstanceSummary, int startIndex,
			int limit, String sidx, String sord,List<AgentType> agentTypeList);

	public ResponseObject loadAgentInfomation(int serverInstanceId, String agentType);

	public ResponseObject changePacketStatasticStatus(int svcAgentId, String svcAgentStatus) throws CloneNotSupportedException;

	public List<ImportValidationErrors> validateAgentForImport(Agent agent, List<ImportValidationErrors> importErrorList);
	
	public Agent iterateOverAgent(Agent agent, ServerInstance serverInstance, Map<Integer, Integer> svcId,boolean isImport,int importMode);

	public ResponseObject updateAgentStatus(int id, String agentStatus) throws CloneNotSupportedException;

	public List<ServicePacketStatsConfig> addNewServicesToPacketStatAgent(List<ServicePacketStatsConfig> svcPktConfigList, PacketStatisticsAgent dbAgent,Map<Integer, Integer> svcId);
	
	public void deletePacketStatAgentServicePktStat(List<Agent> agentList,int serviceId);
	
	public ResponseObject addServiceToFileRenamingAgent(ServiceFileRenameConfig serviceFileRenameConfig);
	
	public ResponseObject updateFileRenameAgentDetail(FileRenameAgent fileRenameAgent);
	
	public long getFileRenameAgentDetailsCount();
	
	public List<ServiceFileRenameConfig> getPaginatedList(int startIndex, int limit, String sidx, String sord,int serverInstanceId);
	
	public ResponseObject updateServiceToFileRenamingAgent(ServiceFileRenameConfig serviceFileRenameConfig);
	
	public ResponseObject deleteServiceFileRenameAgentConfig(int id, int staffId); 
	
	public  ResponseObject getServicesforFileRenameAgent(int serverInstanceId);
	
	public  ServiceFileRenameConfig getServiceFileRenameConfigByPrimaryKey(int serviceFileRenameConfigId);
	
	public ResponseObject createAgent(Agent agent);
	
	public List<ServiceFileRenameConfig> addNewServicesToFileRenameAgent(List<ServiceFileRenameConfig> serviceFileRenameConfig, FileRenameAgent dbAgent,Map<Integer, Integer> svcId);
	
	public void importAgent(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode);
	
}
