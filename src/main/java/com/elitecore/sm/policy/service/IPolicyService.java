package com.elitecore.sm.policy.service;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.PolicyRuleHistory;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.model.SearchPolicyAction;
import com.elitecore.sm.policy.model.SearchPolicyCondition;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * The Interface IPolicyService.
 */
public interface IPolicyService {
	
	// India part - Chintan
	
	/**
	 * Gets the total policy count.
	 *
	 * @param policy the policy
	 * @return the total policy count
	 */
	public long getTotalPolicyCount(SearchPolicy policy);
	
	/**
	 * Gets the paginated list.
	 *
	 * @param searchPolicy the search policy
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the paginated list
	 */
	public List<Policy> getPaginatedList(SearchPolicy searchPolicy, int startIndex, int limit, String sidx, String sord);
	
	/**
	 * Gets the total policy group count.
	 *
	 * @param policy the policy
	 * @return the total policy group count
	 */
	/*public long getTotalPolicyGroupCount(SearchPolicy policy);*/
	
	public long getTotalPolicyGroupCount(SearchPolicy policy , Integer[] existingIDS );
	
	/**
	 * Gets the policy group paginated list.
	 *
	 * @param policy the policy
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the policy group paginated list
	 */
	/** public List<PolicyGroup> getPolicyGroupPaginatedList(SearchPolicy policy, int startIndex, int limit, String sidx, String sord);*/
	
	public List<PolicyGroup> getPolicyGroupPaginatedList(SearchPolicy policy,Integer[] existingIDS , int startIndex, int limit, String sidx, String sord);
	
	/**
	 * Save policy.
	 *
	 * @param policy the policy
	 * @param policyGroups the policy groups
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject savePolicy(Policy policy, String policyGroups, int staffId, int serverId);
	
	/**
	 * Gets the policy group paginated list by policy.
	 *
	 * @param policyId the policy id
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the policy group paginated list by policy
	 */
	public List<PolicyGroupRel> getPolicyGroupPaginatedListByPolicy(int policyId, int startIndex, int limit, String sidx, String sord);
	
	/**
	 * Gets the total policy group count.
	 *
	 * @param policyId the policy id
	 * @return the total policy group count
	 */
	public long getTotalPolicyGroupCount(int policyId);
	
	/**
	 * Update policy.
	 *
	 * @param policy the policy
	 * @param policyGroups the policy groups
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject updatePolicy(Policy policy, String policyGroups, int staffId, int serverId);
	
	/**
	 * Removes the policy.
	 *
	 * @param policyId the policy id
	 * @return the response object
	 */
	public ResponseObject removePolicy(int policyId);
	
	/**
	 * Get Policy Object by policy id
	 * 
	 * @param policyId the policy id
	 * @return the policy object
	 */
	public Policy getPolicyById(int policyId);
	
	/**
	 * Get Policy Rules count By Rule Group
	 * 
	 * @param ruleGroupId the rule group id
	 * @return Policy Rules count 
	 */
	public long getPolicyRuleCountByRuleGroup(int ruleGroupId);
	
	/**
	 * Gets the total policy rule count.
	 *
	 * @param policy the search policy
	 * @return the total policy rule count
	 */
	public long getTotalPolicyRuleCount(SearchPolicy policy);
	
	/**
	 * Gets the paginated list.
	 *
	 * @param searchPolicy the search policy
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the paginated list
	 */
	public List<PolicyRule> getPolicyRulePaginatedList(SearchPolicy searchPolicy, int startIndex, int limit, String sidx, String sord);
	
	/**
	 * Save policy Group.
	 *
	 * @param policyGroup the policy group
	 * @param groupRules the policy group rules
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject savePolicyGroup(PolicyGroup policyGroup, String groupRules, int staffId, int serverId);
	
	/**
	 * Update policy.
	 *
	 * @param policyGroup the policy group
	 * @param groupRules the policy group rules
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject updatePolicyGroup(PolicyGroup policyGroup, String groupRules, int staffId, int serverId);
	
	/**
	 * Removes the policy.
	 *
	 * @param policyGroupId the policy group id
	 * @return the response object
	 */
	public ResponseObject removePolicyGroup(int policyGroupId);
	
	/**
	 * Get Policy Group by id
	 * 
	 * @param policyGroupId the policy group id
	 * @return the Policy Group
	 */
	public PolicyGroup getPolicyGroupById(int policyGroupId);
	
	/**
	 * Gets the policy rule paginated list by policy group.
	 *
	 * @param ruleGroupId the policy rule group id
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the policy rule paginated list by policy
	 */
	public List<PolicyGroupRuleRel> getPolicyRulePaginatedListByRuleGroup(int ruleGroupId, int startIndex, int limit, String sidx,
			String sord);
	/**
	 * Indo - Pak border.
	 *
	 * @param searchPolicyAction the search policy action
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the paginated list
	 */
	List<PolicyAction> getPaginatedList(SearchPolicyAction searchPolicyAction, int startIndex, int limit, String sidx, String sord);

	/**
	 * Gets the total policy action count.
	 *
	 * @param searchPolicyAction the search policy action
	 * @return the total policy action count
	 */
	long getTotalPolicyActionCount(SearchPolicyAction searchPolicyAction);

	/**
	 * Save action.
	 *
	 * @param policyAction the policy action
	 * @return the response object
	 */
	ResponseObject saveAction(PolicyAction policyAction);

	/**
	 * Delete policy action.
	 *
	 * @param actionId the action id
	 * @param staffId the staff id
	 * @return the response object
	 */
	ResponseObject deletePolicyAction(int actionId, int staffId);

	/**
	 * Gets the policy action by id.
	 *
	 * @param id the id
	 * @return the policy action by id
	 */
	PolicyAction getPolicyActionById(int id);

	/**
	 * Update action.
	 *
	 * @param policyAction the policy action
	 * @return the response object
	 */
	ResponseObject updateAction(PolicyAction policyAction);
	
	/**
	 * Validate Policy object during import procedure
	 * 
	 * @param policy the imported policy object
	 * @param importErrorList the validation error list 
	 * @param i 
	 * @return list of input errors
	 */
	public List<ImportValidationErrors> validatePolictForImport(Policy policy, List<ImportValidationErrors> importErrorList, int i);

	long getTotalPolicyConditionCount(SearchPolicyCondition searchPolicyCondition);

	List<PolicyCondition> getPaginatedList(SearchPolicyCondition searchPolicyCondition, int startIndex,
			int limit, String sidx, String sord);

	ResponseObject saveCondition(PolicyCondition policyCondition);

	ResponseObject updateCondition(PolicyCondition policyCondition);

	PolicyCondition getPolicyConditionById(int id);

	ResponseObject deletePolicyCondition(int conditionId, int staffId);
	
	/**
	 * Get Policy Rule by id
	 * 
	 * @param ruleId the policy rule id
	 * @return the Policy Rule
	 */
	public PolicyRule getPolicyRuleById(int ruleId);
	
	/**
	 * Get Policy Condition count By Rule
	 * 
	 * @param ruleId the rule id
	 * @return Policy condition count 
	 */
	public long getPolicyConditionCountByRule(int ruleId);
	
	/**
	 * Get Policy Action count By Rule
	 * 
	 * @param ruleId the rule id
	 * @return Policy action count 
	 */
	public long getPolicyActionCountByRule(int ruleId);
	
	/**
	 * Gets the policy conditions paginated list by policy rule.
	 *
	 * @param ruleGroupId the policy rule group id
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the policy condition paginated list by policy rule
	 */
	/*public List<PolicyCondition> getPolicyConditionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx, String sord);*/
	
	public List<PolicyRuleConditionRel> getPolicyConditionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx, String sord);
	
	/**
	 * Gets the policy actions paginated list by policy rule.
	 *
	 * @param ruleGroupId the policy rule group id
	 * @param startIndex the start index
	 * @param limit the limit
	 * @param sidx the sidx
	 * @param sord the sord
	 * @return the policy action paginated list by policy rule
	 */
	/*public List<PolicyAction> getPolicyActionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx, String sord);*/
	
	public List<PolicyRuleActionRel> getPolicyActionPaginatedListByRule(int ruleId, int startIndex, int limit, String sidx, String sord);
	/**
	 * Save policy Rule.
	 *
	 * @param policyRule the policy rule
	 * @param ruleCondition the policy rule conditions
	 * @param ruleAction the policy rule actions
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject savePolicyRule(PolicyRule policyRule, String ruleCondition, String ruleAction, int staffId, int serverId);
	
	/**
	 * Update policy rule.
	 *
	 * @param policyRule the policy rule
	 * @param ruleCondition the policy rule conditions
	 * @param ruleAction the policy rule actions
	 * @param staffId the staff id
	 * @param serverId the server id
	 * @return the response object
	 */
	public ResponseObject updatePolicyRule(PolicyRule policyRule, String ruleCondition, String ruleAction, int staffId, int serverId);
	
	/**
	 * Removes the policy rule.
	 *
	 * @param policyRuleId the policy rule id
	 * @return the response object
	 */
	public ResponseObject removePolicyRule(int policyRuleId);


	public void iteratePolicyCondition(ServerInstance serverInstance, PolicyCondition exportedPolicyCondition, Map<String, Integer> policyConditionMap, boolean isImport);

	public void iteratePolicyAction(ServerInstance serverInstance, PolicyAction exportedPolicyAction, Map<String, Integer> policyActionMap, boolean isImport);

	public void iteratePolicyRule(ServerInstance serverInstance, PolicyRule exportedPolicyRule,
			Map<String, Integer> policyRuleMap, Map<String, Integer> policyConditionMap,
			Map<String, Integer> policyActionMap, boolean isImport);

	public void iteratePolicyGroup(ServerInstance serverInstance, PolicyGroup exportedPolicyGroup,
			Map<String, Integer> policyGroupMap, Map<String, Integer> policyRuleMap, boolean isImport);

	public void iteratePolicy(ServerInstance serverInstance, Policy exportedPolicy, Map<String, Integer> policyMap,
			Map<String, Integer> policyGroupMap, boolean isImport);
	
	
	public ResponseObject getAllRuleByServiceAndAction(Integer[] serviceIds, String actionType,String reasonCategory,String reasonSeverity,String reasonErrorCode);

	public List<ImportValidationErrors> validatePolicyConditionForImport(PolicyCondition policyCondition,
			List<ImportValidationErrors> importErrorList, int importserverInstanceId);

	public List<ImportValidationErrors> validatePolicyActionForImport(PolicyAction policyAction, List<ImportValidationErrors> importErrorList,
			int importserverInstanceId);

	public  List<ImportValidationErrors>  validatePolicyRuleForImport(PolicyRule policyRule, List<ImportValidationErrors> importErrorList,
			int importserverInstanceId);

	public List<ImportValidationErrors> validatePolicyGroupForImport(PolicyGroup policyGroup, List<ImportValidationErrors> importErrorList,
			int importserverInstanceId);
	
	public void saveRuleHistory(PolicyRuleHistory policyRuleHistory);
	
	public long getPolicyRuleCountByAlias(PolicyRule policyRule, int serverId);
	
	public ResponseObject validateConditionExpression(String serverInstanceId, String expressionStr);
	
	public ResponseObject validateActionExpression(String serverInstanceId, String expressionStr);
}
