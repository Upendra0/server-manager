package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;

/**
 * 
 * @author chintan.patel
 *
 */
public interface IPolicyGroupRuleRelDao extends GenericDAO<PolicyGroupRuleRel> {
	
	/**
	 * Delete Policy Group Relation
	 * 
	 * @param policyGroupRuleRel the policy group rule relation to delete
	 */
	public void deletePolicyRuleGroupRel(PolicyGroupRuleRel policyGroupRuleRel);
	
	/**
	 * Get Policy Group Rule Paginated List
	 * 
	 * @param classInstance the PolicyGroupRuleRel class instance
	 * @param conditions criteria list
	 * @param aliases alias list
	 * @param offset the offset
	 * @param limit the page record limit
	 * @param sortColumn the sorting column
	 * @param sortOrder the sorting order
	 * @return List of rules
	 */
	public List<PolicyGroupRuleRel> getPolicyGroupRulePaginatedList(Class<PolicyGroupRuleRel> classInstance, List<Criterion> conditions, Map<String, String> aliases,
	   		int offset, int limit, String sortColumn, String sortOrder);
	
	public int getMaxApplicationOrder();
	
}
