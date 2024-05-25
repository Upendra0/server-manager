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
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.SearchPolicyCondition;
import com.elitecore.sm.policy.service.PolicyServiceImpl;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

/**
 * The Class PolicyConditionDaoImpl.
 */
@Repository(value = "policyConditionDao")
public class PolicyConditionDaoImpl extends GenericDAOImpl<PolicyCondition> implements IPolicyConditionDao {

	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyConditionDao#getPolicyConditionBySearchParameters(com.elitecore.sm.policy.model.SearchPolicyCondition)
	 */
	@Override
	public Map<String, Object> getPolicyConditionBySearchParameters(SearchPolicyCondition searchPolicyCondition) {
		
		logger.debug(">> getPolicyConditionBySearchParameters in PolicyConditionDaoImpl "  +searchPolicyCondition);
		
		Map<String, Object> returnMap = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if (!StringUtils.isEmpty(searchPolicyCondition.getPolicyConditionName())) {
			conditionList.add(Restrictions.like("name", "%" + searchPolicyCondition.getPolicyConditionName().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyCondition.getPolicyConditionType()) && 
				!"-1".equals(searchPolicyCondition.getPolicyConditionType())) {
			conditionList.add(Restrictions.like("type", "%" + searchPolicyCondition.getPolicyConditionType().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyCondition.getPolicyConditionDesc())) {
			conditionList.add(Restrictions.like("description", "%" + searchPolicyCondition.getPolicyConditionDesc().trim() + "%").ignoreCase());
		}
		
		if (!StringUtils.isEmpty(searchPolicyCondition.getPolicyConditionAssoStatus()) && 
				!"-1".equals(searchPolicyCondition.getPolicyConditionAssoStatus())) {
			if("No".equalsIgnoreCase(searchPolicyCondition.getPolicyConditionAssoStatus())){
				conditionList.add(Restrictions.isEmpty("policyRuleSet"));
			}else{
				conditionList.add(Restrictions.isNotEmpty("policyRuleSet"));
			}
		}
		conditionList.add(Restrictions.eq("server.id", searchPolicyCondition.getServerInstanceId()));
        conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
        
        if( searchPolicyCondition.getExistingConditionIds() != null && searchPolicyCondition.getExistingConditionIds().length !=0 ){
        	conditionList.add( Restrictions.not( Restrictions.in("id", searchPolicyCondition.getExistingConditionIds() )));
		}
        
		returnMap.put("conditions", conditionList);
		
		logger.debug("<< getPolicyConditionBySearchParameters in PolicyConditionDaoImpl ");
		return returnMap;
	}
	
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.common.dao.GenericDAOImpl#getPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyCondition> getPaginatedList(Class<PolicyCondition> instance, List<Criterion> conditions,
			Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		
		List<PolicyCondition> resultList;
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
	 * @see com.elitecore.sm.policy.dao.IPolicyConditionDao#getConditionByAlies(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int getConditionByAlies(String ruleConditionAlies, int serverId) {
		
		Criteria criteria = getCurrentSession().createCriteria(PolicyCondition.class);
		criteria.add(Restrictions.eq("alias",ruleConditionAlies));
		criteria.add(Restrictions.eq("server.id",serverId));
		criteria.setProjection(Projections.distinct(Projections.property("id")));
		
		List<Integer> ruleConditionIdList=criteria.list();
		int ruleConditionId = (ruleConditionIdList!=null && !ruleConditionIdList.isEmpty()) ? (int)ruleConditionIdList.get(0):0;
		return ruleConditionId;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyConditionDao#getPolicyConditionCriteriaByRuleId(int)
	 */
	public Map<String, Object> getPolicyConditionCriteriaByRuleId(int ruleId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
	
		
		aliases.put( "policyRuleConditionRel", "policyRuleConditionRel");
		conditionList.add(Restrictions.eq("policyRuleConditionRel.policyRuleCon.id", ruleId ));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		return returnMap;
	}
	
	public Map<String, Object> getPolicyConditionCriteria_RuleId(int ruleId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
	
		conditionList.add(Restrictions.eq("policyRuleCon.id", ruleId ));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		return returnMap;
	}
	
	/**
	 * Mark server instance dirty on save
	 */
	@Override
	public void save(PolicyCondition policyCondition){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyCondition.getServer());
		getCurrentSession().save(policyCondition);
	}
	
	/**
	 * Mark server instance dirty on merge
	 */
	@Override
	public void merge(PolicyCondition policyCondition){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyCondition.getServer());
		getCurrentSession().merge(policyCondition);
		getCurrentSession().flush();
	}
	
	/**
	 * Mark server instance dirty on update
	 */
	@Override
	public void update(PolicyCondition policyCondition){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyCondition.getServer());
		getCurrentSession().update(policyCondition);
	}
	
	/**
	 * Fetch PolicyCondition for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PolicyCondition> getPolicyConditionforServerInstance(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyCondition.class);
		if (serverInstanceId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}
	
	/**
	 * Gets all the policies whose type is dynamic
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyCondition> getAllDynamicPolicyConditions(int serverId){
		Criteria criteria = getCurrentSession().createCriteria(PolicyCondition.class);
		criteria.add(Restrictions.eq("type",BaseConstants.DYNAMIC));
		criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PolicyCondition getPolicyConditionByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyCondition.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq("server.id",serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<PolicyCondition> policyConditions = criteria.list();
		return (policyConditions != null && !policyConditions.isEmpty()) ? policyConditions.get(0) : null;
	}
}
