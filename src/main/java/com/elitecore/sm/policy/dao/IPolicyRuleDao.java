package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.PolicyRuleHistory;
import com.elitecore.sm.policy.model.SearchPolicy;

/**
 * 
 * @author chintan.patel
 *
 */
public interface IPolicyRuleDao extends GenericDAO<PolicyRule> {
	
	/**
	 * Get Policy Rule List by search parameters
	 * 
	 * @param policy the search policy object
	 * @return Search result
	 */
	public Map<String, Object> getPolicyRulesCriteriaBySearchParams(SearchPolicy policy);
	
	/**
	 * Get Paginated list of policy groups
	 * 
	 * @param classInstance the search entity class instance
	 * @param conditions the criteria conditions
	 * @param aliases the criteria aliases
	 * @param offset grid record offset
	 * @param limit grid record limit
	 * @param sortColumn the sorting column
	 * @param sortOrder the sort order asc/desc
	 * @return Paginated policies list
	 */
	public List<PolicyRule> getPolicyRulePaginatedList(Class<PolicyRule> classInstance, List<Criterion> conditions, Map<String, String> aliases,
											   		int offset, int limit, String sortColumn, String sortOrder);
	
	/**
	 * Get Policy Rules associated with policy groups
	 * 
	 * @param policyGroupId the policy group id
	 * @return the associated policy group list
	 */
	@Deprecated
	public Map<String, Object> getPolicyRulesCriteriaByPolicyGroupId(int policyGroupId);
	
	/**
	 * Get Policy Rules associated with policy groups
	 * 
	 * @param ruleGroupId the policy group id
	 * @return the associated policy group list
	 */
	public Map<String, Object> getPolicyRulesCriteriaByRuleGroupId(int ruleGroupId); 
	
	/**
	 * Delete Policy Rules associated with policy group
	 * 
	 * @param policyGroupId the policy group id
	 */
	public void deletePolicyRulesByPolicyGroupId(int policyGroupId);
	
	/**
	 * Get Policy Rule count by alias
	 * 
	 * @param alias the policy rule alias
	 * @param serverId 
	 * @return the count of policy rules with same alias
	 */
	public long getPolicyRuleCountByAlias(String alias, int serverId);
	
	/**
	 * Get Policy Rule Conditions Paginated List
	 * 
	 * @param classInstance the PolicyCondition class instance
	 * @param conditions criteria list
	 * @param aliases alias list
	 * @param offset the offset
	 * @param limit the page record limit
	 * @param sortColumn the sorting column
	 * @param sortOrder the sorting order
	 * @return List of conditions
	 */
	public List<PolicyCondition> getPolicyRuleConditionsPaginatedList(Class<PolicyCondition> classInstance, List<Criterion> conditions, Map<String, String> aliases,
	   		int offset, int limit, String sortColumn, String sortOrder);
	
	/**
	 * Get Policy Rule Actions Paginated List
	 * 
	 * @param classInstance the PolicyAction class instance
	 * @param conditions criteria list
	 * @param aliases alias list
	 * @param offset the offset
	 * @param limit the page record limit
	 * @param sortColumn the sorting column
	 * @param sortOrder the sorting order
	 * @return List of Actions
	 */
	public List<PolicyAction> getPolicyRuleActionsPaginatedList(Class<PolicyAction> classInstance, List<Criterion> conditions, Map<String, String> aliases,
	   		int offset, int limit, String sortColumn, String sortOrder);

	public List<PolicyRule> getPolicyRuleforServerInstance(int serverInstanceId);
	
	
	/**
	 * Method will get all rule list based on processing services id and action like Filter, duplicate, invalid.
	 * @param hqlQuery
	 * @return
	 */
	public Map<String,List<Object[]>> getAllRuleByServiceAndAction(Integer [] serviceId, String actionType,String reasonCategory,String reasonSeverity,String reasonErrorCode);
	
	public PolicyRule getPolicyRuleByAlias(String alias, int serverId);

	public void saveRuleHistory(PolicyRuleHistory policyRuleHistory);

	List<PolicyRuleConditionRel> getPolicyRuleConditionsPaginatedListByRuleID(
			Class<PolicyRuleConditionRel> classInstance,
			List<Criterion> conditions, Map<String, String> aliases,
			int offset, int limit, String sortColumn, String sortOrder);

	List<PolicyRuleActionRel> getPolicyRuleActionsPaginatedListByRuleID(
			Class<PolicyRuleActionRel> classInstance,
			List<Criterion> conditions, Map<String, String> aliases,
			int offset, int limit, String sortColumn, String sortOrder);
	
	
}
