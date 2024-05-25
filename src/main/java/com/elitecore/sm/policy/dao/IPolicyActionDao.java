package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.SearchPolicyAction;

/**
 * The Interface IPolicyActionDao.
 */
public interface IPolicyActionDao extends GenericDAO<PolicyAction> {

	/**
	 * Gets the policy action by search parameters.
	 *
	 * @param searchPolicyAction the search policy action
	 * @return the policy action by search parameters
	 */
	public Map<String, Object> getPolicyActionBySearchParameters(SearchPolicyAction searchPolicyAction);

	/**
	 * Gets the action by alies.
	 *
	 * @param ruleActionAlies the rule action alies
	 * @param i 
	 * @return the action by alies
	 */
	int getActionByAlies(String ruleActionAlies, int i);

	/**
	 * Gets Policy Action Criteria list by rule id
	 * @param ruleId the rule id
	 * @return criteria list
	 */
	public Map<String, Object> getPolicyActionCriteriaByRuleId(int ruleId);
	
	public Map<String, Object> getPolicyActionCriteria_RuleId(int ruleId);
	

	public List<PolicyAction> getPolicyActionforServerInstance(int serverInstanceId);
	
	public List<PolicyAction> getAllDynamicPolicyActions(int serverId);
	
	public PolicyAction getPolicyActionByAlias(String alias, int serverId);
}
