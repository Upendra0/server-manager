package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.SearchPolicyCondition;

/**
 * The Interface IPolicyConditionDao.
 */
public interface IPolicyConditionDao extends GenericDAO<PolicyCondition> {

	/**
	 * Gets the policy condition by search parameters.
	 *
	 * @param searchPolicyCondition the search policy condition
	 * @return the policy condition by search parameters
	 */
	public Map<String, Object> getPolicyConditionBySearchParameters(SearchPolicyCondition searchPolicyCondition);

	/**
	 * Gets the condition by alies.
	 *
	 * @param ruleConditionAlies the rule condition alies
	 * @param i 
	 * @return the condition by alies
	 */
	int getConditionByAlies(String ruleConditionAlies, int i);

	/**
	 * Gets Policy Condition Criteria list by rule id
	 * @param ruleId the rule id
	 * @return criteria list
	 */
	public Map<String, Object> getPolicyConditionCriteriaByRuleId(int ruleId);
	
	public Map<String, Object> getPolicyConditionCriteria_RuleId(int ruleId);

	public List<PolicyCondition> getPolicyConditionforServerInstance(int serverInstanceId);
	
	public List<PolicyCondition> getAllDynamicPolicyConditions(int serverId);
	
	public PolicyCondition getPolicyConditionByAlias(String alias, int serverId);
}
