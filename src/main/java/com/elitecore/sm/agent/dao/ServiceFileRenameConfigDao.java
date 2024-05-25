




package com.elitecore.sm.agent.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author harsh.patel
 *
 */
public interface ServiceFileRenameConfigDao extends GenericDAO<ServiceFileRenameConfig>{

	public Map<String, Object> getFileRenameAgentDetailsCount(); 
	
	public void save(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance);

	public void update(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance);
	
	public void merge(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance);
	
	public List<ServiceFileRenameConfig> getFileRenameAgentPaginatedList(Class<ServiceFileRenameConfig> classInstance, int offset, int limit, String sortColumn, String sortOrder,int serverInstanceId);

	public List<Service> getServicesforFileRenameAgent(int serverInstanceId);

	public ServiceFileRenameConfig getServiceDetailByServiceIdAndAgentId(int serviceId, int agentId);
}
