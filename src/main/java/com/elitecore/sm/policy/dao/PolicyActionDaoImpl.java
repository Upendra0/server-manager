package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.SearchPolicyAction;
import com.elitecore.sm.policy.service.PolicyServiceImpl;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

/**
 * The Class PolicyActionDaoImpl.
 */
@Repository
public class PolicyActionDaoImpl extends GenericDAOImpl<PolicyAction> implements IPolicyActionDao {

	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyActionDao#getPolicyActionBySearchParameters(com.elitecore.sm.policy.model.SearchPolicyAction)
	 */
	@Override
	public Map<String, Object> getPolicyActionBySearchParameters(SearchPolicyAction searchPolicyAction) {
		
		logger.debug(">> getPolicyActionBySearchParameters in PolicyActionDaoImpl "  +searchPolicyAction);
		
		Map<String, Object> returnMap = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchPolicyAction.getPolicyActionName())) {
			conditionList.add(Restrictions.like("name", "%" + searchPolicyAction.getPolicyActionName().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyAction.getPolicyActionType()) && 
				!"-1".equals(searchPolicyAction.getPolicyActionType())) {
			conditionList.add(Restrictions.like("type", "%" + searchPolicyAction.getPolicyActionType().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyAction.getPolicyActionDesc())) {
			conditionList.add(Restrictions.like("description", "%" + searchPolicyAction.getPolicyActionDesc().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyAction.getPolicyActionAssoStatus()) && 
				!"-1".equals(searchPolicyAction.getPolicyActionAssoStatus())) {
			if("No".equalsIgnoreCase(searchPolicyAction.getPolicyActionAssoStatus())){
				conditionList.add(Restrictions.isEmpty("policyRuleSet"));
			}else{
				conditionList.add(Restrictions.isNotEmpty("policyRuleSet"));
			}
		}
		conditionList.add(Restrictions.eq("server.id", searchPolicyAction.getServerInstanceId()));
        conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
        
        if( searchPolicyAction.getExistingActionIds() != null && searchPolicyAction.getExistingActionIds().length !=0 ){
        	conditionList.add( Restrictions.not( Restrictions.in("id", searchPolicyAction.getExistingActionIds() )));
		}
        
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getPolicyActionBySearchParameters in PolicyActionDaoImpl ");
		return returnMap;
	}
	
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.common.dao.GenericDAOImpl#getPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyAction> getPaginatedList(Class<PolicyAction> instance, List<Criterion> conditions,
			Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		
		List<PolicyAction> resultList;
		Criteria criteria = getCurrentSession().createCriteria(instance);
	
		logger.debug("Sort column ="+sortColumn);
		if("desc".equalsIgnoreCase(sortOrder)){
				criteria.addOrder(Order.desc(sortColumn));
		}
		else if("asc".equalsIgnoreCase(sortOrder)){
				criteria.addOrder(Order.asc(sortColumn));
		}

		if(conditions!=null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyActionDao#getActionByAlies(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getActionByAlies(String ruleActionAlies,int serverId) {
		
		Criteria criteria = getCurrentSession().createCriteria(PolicyAction.class);
		criteria.add(Restrictions.eq("alias",ruleActionAlies));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.setProjection(Projections.distinct(Projections.property("id")));
		
		List<Integer> ruleActionIdList=criteria.list();
		int ruleActionId = (ruleActionIdList!=null && !ruleActionIdList.isEmpty()) ? (int)ruleActionIdList.get(0):0;
		return ruleActionId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyActionDao#getPolicyActionCriteriaByRuleId(int)
	 */
	@Override
	public Map<String, Object> getPolicyActionCriteriaByRuleId(int ruleId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		 aliases.put("policyRuleActionRel", "policyRuleActionRel");
		
		conditionList.add(Restrictions.eq("policyRuleActionRel.policyRuleAction.id", ruleId));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		return returnMap;
	}
	@Override
	public Map<String, Object> getPolicyActionCriteria_RuleId(int ruleId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
	
		conditionList.add(Restrictions.eq("policyRuleAction.id", ruleId ));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		return returnMap;
	}
	/**
	 * Mark server instance dirty on save
	 */
	@Override
	public void save(PolicyAction policyAction){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyAction.getServer());
		getCurrentSession().save(policyAction);
	}
	
	/**
	 * Mark server instance dirty on merge
	 */
	@Override
	public void merge(PolicyAction policyAction){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyAction.getServer());
		getCurrentSession().merge(policyAction);
		getCurrentSession().flush();
		
	}
	
	/**
	 * Mark server instance dirty on update
	 */
	@Override
	public void update(PolicyAction policyAction){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyAction.getServer());
		getCurrentSession().update(policyAction);
	}

	/**
	 * Fetch PolicyAction for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PolicyAction> getPolicyActionforServerInstance(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyAction.class);
		if (serverInstanceId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyAction> getAllDynamicPolicyActions(int serverId){
		Criteria criteria = getCurrentSession().createCriteria(PolicyAction.class);
		criteria.add(Restrictions.eq("type",BaseConstants.DYNAMIC));
		criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PolicyAction getPolicyActionByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyAction.class);
		criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverId));
		criteria.add(Restrictions.eq("alias",alias));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<PolicyAction> policyActions = criteria.list();
		return (policyActions != null && !policyActions.isEmpty()) ? policyActions.get(0) : null;
	}
	
}
