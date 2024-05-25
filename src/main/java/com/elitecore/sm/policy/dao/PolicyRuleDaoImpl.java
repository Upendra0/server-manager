package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
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
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.PolicyRuleHistory;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.service.PolicyServiceImpl;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

/**
 * 
 * @author chintan.patel
 *
 */
@Repository(value = "policyRuleDao")
public class PolicyRuleDaoImpl extends GenericDAOImpl<PolicyRule> implements IPolicyRuleDao {

	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRulesCriteriaBySearchParams(com.elitecore.sm.policy.model.SearchPolicy)
	 */
	@Override
	public Map<String, Object> getPolicyRulesCriteriaBySearchParams(SearchPolicy policy) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		
		if(StringUtils.isNotEmpty(policy.getName())) {
			conditionList.add(Restrictions.like(BaseConstants.NAME, "%" + StringUtils.trim(policy.getName()) + "%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(policy.getDescription())) {
			conditionList.add(Restrictions.like(BaseConstants.DESCRIPTION, "%" + StringUtils.trim(policy.getDescription()) + "%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(policy.getCategory()) && !("-1".equalsIgnoreCase(policy.getCategory()))) {
			conditionList.add(Restrictions.like(BaseConstants.CATEGORY, "%" + StringUtils.trim(policy.getCategory()) + "%").ignoreCase());
		}		
		if(StringUtils.isNotEmpty(policy.getSeverity()) && !("-1".equalsIgnoreCase(policy.getSeverity()))) {
			conditionList.add(Restrictions.like(BaseConstants.SEVERITY, "%" + StringUtils.trim(policy.getSeverity()) + "%").ignoreCase());
		}		
		if(StringUtils.isNotEmpty(policy.getErrorCode())) {
			conditionList.add(Restrictions.like(BaseConstants.ERRORCODE, "%" + StringUtils.trim(policy.getErrorCode()) + "%").ignoreCase());
		}		
		if(StringUtils.isNotEmpty(policy.getAssociationStatus()) && !StringUtils.equalsIgnoreCase(BaseConstants.ALL, policy.getAssociationStatus())) {
			if(StringUtils.equals(policy.getAssociationStatus(), BaseConstants.ASSOCIATED)) {
				conditionList.add(Restrictions.isNotEmpty("policyGroupRuleRelSet"));
			} else if(StringUtils.equals(policy.getAssociationStatus(), BaseConstants.NONASSOCIATED)) {
				conditionList.add(Restrictions.isEmpty("policyGroupRuleRelSet"));
			}
		}
		conditionList.add(Restrictions.eq("server.id", policy.getServerInstanceId()));
		conditionList.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRulePaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyRule> getPolicyRulePaginatedList(Class<PolicyRule> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {
		
		List<PolicyRule> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
		
		if(StringUtils.equals(sortColumn, BaseConstants.NAME) || 
				StringUtils.equals(sortColumn, BaseConstants.DESCRIPTION) || 
				StringUtils.equals(sortColumn, BaseConstants.ASSOCIATION_STATUS)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
		}

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRulesCriteriaByPolicyGroupId(int)
	 */
	@Override
	public Map<String, Object> getPolicyRulesCriteriaByPolicyGroupId(int policyGroupId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		aliases.put(BaseConstants.POLICY_GROUP_RULE_REL_SET, "policyGroupRuleRel");
		conditionList.add(Restrictions.eq("policyGroupRuleRel.group.id", policyGroupId ));
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		
		return returnMap;
	}
	
	@Override
	public Map<String, Object> getPolicyRulesCriteriaByRuleGroupId(int ruleGroupId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq("group.id", ruleGroupId));
		aliases.put("policyRule", "policyRule");
		
		returnMap.put(PolicyServiceImpl.ALIASES, aliases);
		returnMap.put(PolicyServiceImpl.CONDITIONS, conditionList);
		return returnMap;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#deletePolicyRulesByPolicyGroupId(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void deletePolicyRulesByPolicyGroupId(int policyGroupId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyGroupRuleRel.class);
		criteria.add(Restrictions.eq("policyGroup.id", policyGroupId));
		
		List<PolicyGroupRel> policyGroupRelList = criteria.list();
		
		if(policyGroupRelList != null) {
			for(PolicyGroupRel policyGroupRel : policyGroupRelList) {
				getCurrentSession().delete(policyGroupRel);
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRuleCountByAlias(java.lang.String)
	 */
	@Override
	public long getPolicyRuleCountByAlias(String alias,int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyRule.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.setProjection(Projections.rowCount());
		return (long)criteria.uniqueResult();
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRuleConditionsPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyCondition> getPolicyRuleConditionsPaginatedList(Class<PolicyCondition> classInstance,
			List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn,
			String sortOrder) {
		List<PolicyCondition> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
		
		if(StringUtils.isNotEmpty(sortColumn)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
		}

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
  
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyRuleConditionRel> getPolicyRuleConditionsPaginatedListByRuleID(Class<PolicyRuleConditionRel> classInstance,
			List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn,
			String sortOrder){
		
		List<PolicyRuleConditionRel> resultList;
		Criteria criteria = getCurrentSession().createCriteria( PolicyRuleConditionRel.class );		
		if(StringUtils.isNotEmpty(sortColumn)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
		}
		 if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		 if( limit!= -1 ){
		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		 }
		resultList = criteria.list();
		Collections.sort( resultList );
		return resultList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.policy.dao.IPolicyRuleDao#getPolicyRuleActionsPaginatedList(java.lang.Class, java.util.List, java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyAction> getPolicyRuleActionsPaginatedList(Class<PolicyAction> classInstance,
			List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn,
			String sortOrder) {
		List<PolicyAction> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
		
		if(StringUtils.isNotEmpty(sortColumn)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
		}

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PolicyRuleActionRel> getPolicyRuleActionsPaginatedListByRuleID(Class<PolicyRuleActionRel> classInstance,
			List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn,
			String sortOrder){
		
		List<PolicyRuleActionRel> resultList;
		Criteria criteria = getCurrentSession().createCriteria( classInstance );		
		if(StringUtils.isNotEmpty(sortColumn)) {
			criteria.addOrder(StringUtils.equals(sortOrder, BaseConstants.DESC) ? Order.desc(sortColumn) : Order.asc(sortColumn));
		}
		 if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		if( limit!= -1 ){
		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		}
		resultList = criteria.list();
		Collections.sort( resultList );
		return resultList;
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void save(PolicyRule policyRule){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyRule.getServer());
		getCurrentSession().save(policyRule);
	}
	
	/**
	 * Mark server instance dirty
	 */
	@Override
	public void merge(PolicyRule policyRule){
		serverInstanceDao.markServerInstanceChildFlagDirty(policyRule.getServer());
		getCurrentSession().merge(policyRule);
		getCurrentSession().flush();
		
	}
	
	/**
	 * Fetch PolicyRule for sync
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<PolicyRule> getPolicyRuleforServerInstance(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyRule.class);
		if (serverInstanceId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.SERVER_ID, serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.addOrder(Order.asc("createdDate"));
		return criteria.list();
	}
	
	
	/**
	 * Method will get all rule list by services id and action type
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String,List<Object[]>> getAllRuleByServiceAndAction(Integer[] serviceIds, String actionType,String reasonCategory,String reasonSeverity,String reasonErrorCode) {
		
		//String selectClause =  " SELECT DISTINCT pr.id as ruleid, pr.alias as rulename,grp.id as groupId, grp.alias, fgp.filterGroupType  ";
		
		StringBuilder commonQuery = new StringBuilder();
			commonQuery.append( " FROM  ProcessingService s   ");
			commonQuery.append(" INNER JOIN s.fileGroupingParameter fgp ON fgp.status =:fileGroupStatus");
			commonQuery.append(" INNER JOIN s.svcPathList path ON path.status =:pathStatus ");
			commonQuery.append(" INNER JOIN path.policy p  ON p.status =:policyStatus ");
			commonQuery.append(" INNER JOIN p.policyGroupRelSet pgr ");
			commonQuery.append(" INNER JOIN pgr.group grp ON grp.status =:groupStatus ");
			commonQuery.append(" INNER JOIN grp.policyGroupRuleRelSet rr ");
			commonQuery.append(" INNER JOIN rr.policyRule pr  ON pr.status =:ruleStatus ");
			commonQuery.append(" INNER JOIN pr.policyRuleActionRel par ");
			commonQuery.append(" INNER JOIN par.action pa ON pa.status=:actionStatus ");
		
		String groupWhereClause = " WHERE s.id in (:idList) AND pa.action =:actionType  AND s.status =:serviceStatus AND fgp.filterGroupType ='Groupwise'";
		String ruleWhereClause = " WHERE s.id in (:idList) AND pa.action =:actionType AND s.status =:serviceStatus AND fgp.filterGroupType ='Rulewise'";
		
		if(reasonCategory != null){
			ruleWhereClause += " AND pr.category =:reasonCategory";
		}
		if(!reasonSeverity.equals("-1")){
			ruleWhereClause += " AND pr.severity =:reasonSeverity";
		}
		if(reasonErrorCode != null && reasonErrorCode.length() > 0){
			ruleWhereClause += " AND pr.errorCode =:reasonErrorCode";
		}
		String naWhereClause = " WHERE s.id in (:idList) AND pa.action =:actionType  AND s.status =:serviceStatus  AND fgp.filterGroupType ='NA'";
		
		Map<String,List<Object[]>> finalQueryResult = new HashMap<>();
		
		String groupSelectClause =  " SELECT DISTINCT grp.id as groupId, grp.alias  ";
		
		Query groupQuery = getCurrentSession().createQuery(groupSelectClause + commonQuery.toString() +  groupWhereClause );
			groupQuery.setParameter("fileGroupStatus" , StateEnum.ACTIVE);
			groupQuery.setParameter("pathStatus" , StateEnum.ACTIVE);
			groupQuery.setParameter("policyStatus" , StateEnum.ACTIVE);
			groupQuery.setParameter("groupStatus" , StateEnum.ACTIVE);
			groupQuery.setParameter("ruleStatus" , StateEnum.ACTIVE);
			groupQuery.setParameter("actionStatus" , StateEnum.ACTIVE);
			groupQuery.setParameterList("idList",serviceIds);
			groupQuery.setParameter("actionType" ,actionType);
			groupQuery.setParameter("serviceStatus" , StateEnum.ACTIVE);
		List<Object[]> groupList = groupQuery.list();
		finalQueryResult.put("Group", groupList);
		
		String ruleSelectClause =  " SELECT DISTINCT pr.id as ruleid, pr.alias as rulename";
		
		Query ruleQuery = getCurrentSession().createQuery(ruleSelectClause + commonQuery.toString() +  ruleWhereClause );
		ruleQuery.setParameter("fileGroupStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameter("pathStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameter("policyStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameter("groupStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameter("ruleStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameter("actionStatus" , StateEnum.ACTIVE);
		ruleQuery.setParameterList("idList",serviceIds);
		ruleQuery.setParameter("actionType" ,actionType);
		ruleQuery.setParameter("serviceStatus" , StateEnum.ACTIVE);
		
		if(reasonCategory != null){
			ruleQuery.setParameter("reasonCategory" , reasonCategory);
		}
		if(!reasonSeverity.equals("-1")){
			ruleQuery.setParameter("reasonSeverity" , reasonSeverity);
		}
		if(reasonErrorCode != null && reasonErrorCode.length() > 0){
			ruleQuery.setParameter("reasonErrorCode" , reasonErrorCode);
		}
		
		List<Object[]> ruleList = ruleQuery.list();
		finalQueryResult.put("Rule", ruleList);
		
		String naSelectClause = " SELECT DISTINCT pr.id as ruleid, pr.alias as rulename,grp.id as groupId, grp.alias, fgp.filterGroupType  ";
		
		Query naQuery = getCurrentSession().createQuery(naSelectClause + commonQuery.toString() +  naWhereClause );
		naQuery.setParameter("fileGroupStatus" , StateEnum.ACTIVE);
		naQuery.setParameter("pathStatus" , StateEnum.ACTIVE);
		naQuery.setParameter("policyStatus" , StateEnum.ACTIVE);
		naQuery.setParameter("groupStatus" , StateEnum.ACTIVE);
		naQuery.setParameter("ruleStatus" , StateEnum.ACTIVE);
		naQuery.setParameter("actionStatus" , StateEnum.ACTIVE);
		naQuery.setParameterList("idList",serviceIds);
		naQuery.setParameter("actionType" ,actionType);
		naQuery.setParameter("serviceStatus" , StateEnum.ACTIVE);
		List<Object[]> naList = naQuery.list();
		finalQueryResult.put("NA", naList);
		
		return finalQueryResult;
	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PolicyRule getPolicyRuleByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(PolicyRule.class);
		criteria.add(Restrictions.eq("alias", alias));
		criteria.add(Restrictions.eq("server.id", serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		List<PolicyRule> policyRules = criteria.list();
		return (policyRules != null && !policyRules.isEmpty()) ? policyRules.get(0) : null;
	}

	@Override
	public void saveRuleHistory(PolicyRuleHistory policyRuleHistory) {
		getCurrentSession().save(policyRuleHistory);
	}
	
}
