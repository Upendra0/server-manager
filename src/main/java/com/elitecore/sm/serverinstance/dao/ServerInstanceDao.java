package com.elitecore.sm.serverinstance.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.serverinstance.model.ServerInstance;


/**
 * 
 * @author avani.panchal
 *
 */
public interface ServerInstanceDao extends GenericDAO<ServerInstance> {
	
	public List<ServerInstance> getIDByIpPortUtility(String ip,int port,int utilityPort);
	
	public int getServerInstanceCount(String name, int port, int id, String ipAddress);
	
	public ServerInstance getServerInstanceforSync(int id);
	
	public List<Agent> getAgentListforServerInstance(int serverInsId);

	public ServerInstance getServerInstance(int id);

	public List<ServerInstance> getServerInstanceByServerId(int serverId);
	
	public ServerInstance getServerInstanceFullHierarchyWithOutMarshlling(int serverInstanceId) throws SMException ;
	
	public void updateForResetSyncFlagofServerInstance(ServerInstance serverInstance);
	
	public List<ServerInstance> getServerInstancesUsingInCondition(List<String> serverInstancesId);
	
	public void markServerInstanceChildFlagDirty(ServerInstance serverInstance);
	
	public void markServerInstanceFlagDirty(ServerInstance serverInstance);
	
	public List<ServerInstance> getServerInstanceList();

	public List<ServerInstance> getServerInstanceByIpaddressAndPort(int port, String ipAddress);

	public 	List<ServerInstance> getServerInstanceByName(String serverInstanceName); /* check the unique name */

	public List<ServerInstance> getServerInstanceListByDSId(int serverInstanceName);
	
	public List<ServerInstance> getServerInstanceListByAssociatedDSId(int serverInstanceName);
	
	public List<ServerInstance> getAllInstanceByServerTypeId(Integer[] serverIds);
	
	public List<ServerInstance> getServiceTypeServerList(String serviceTypeId);

	public List<ServerInstance> getServerList();

	public List<ServerInstance> getServerInstanceByServerIdAndPort(int serverId, int port);
	
	public void clearSession();

	public Map<String, Object> createCriteriaConditions(String serverTypeId, String searchInstanceName, String searchHost, String searchServerName,
			String searchPort, String searchSyncStatus, String dsid);
	
	public List<ServerInstance> getServerInstanceBySerInsId(int serverInstanceId);
	
	
}

