package com.elitecore.sm.agent.dao;

import java.util.List;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.common.dao.GenericDAO;

/**
 * 
 * @author avani.panchal
 *
 */
public interface AgentTypeDao extends GenericDAO<AgentType>{
	
	public List<AgentType>  getAllAgentType();
	
	public AgentType getAgentTypeByAlias(String alias);

}
