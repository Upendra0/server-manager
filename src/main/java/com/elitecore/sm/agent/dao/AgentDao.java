package com.elitecore.sm.agent.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.common.dao.GenericDAO;

/**
 * 
 * @author avani.panchal
 *
 */
public interface AgentDao extends GenericDAO<Agent>{

	public Map<String, Object> createCriteriaConditions(int searchInstanceId,List<AgentType> agentTypeList);

	public List<Agent> getAgentByServerInstanceIdAndAgentTypeID(int serverInstanceId, int agentTypeId);
	
	public List<Agent> getAgentPaginatedList(Class<Agent> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);
	
	public Agent getAgentFullHierarchy(Agent agent);	
}
