package com.elitecore.sm.agent.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="agentTypeDao")
public class AgentTypeDaoImpl extends GenericDAOImpl<AgentType> implements AgentTypeDao{

	/**
	 * Fetch all Agent Type
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AgentType> getAllAgentType() {
		Criteria criteria=getCurrentSession().createCriteria(AgentType.class);
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		
		return criteria.list();
	}
	
	/**
	 * Fetch Agent Type based on alias
	 * @param alias
	 * @return
	 */
	@Override
	public AgentType getAgentTypeByAlias(String alias){
		Criteria criteria=getCurrentSession().createCriteria(AgentType.class);
		criteria.add(Restrictions.eq("alias",alias));
		
		return ((criteria.list() !=null && !criteria.list().isEmpty()) ? (AgentType)(criteria.list()).get(0) : null);
	}

}
