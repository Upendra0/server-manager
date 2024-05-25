package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;

/**
 * 
 * @author chintan.patel
 *
 */
public interface IPolicyGroupRelDao extends GenericDAO<PolicyGroupRel> {

	/**
	 * Delete Policy Group Relation
	 * 
	 * @param policyGroupRel the policy group relation to delete
	 */
	public void deletePolicyGroupRel(PolicyGroupRel policyGroupRel);
	
	public List<PolicyGroupRel> getPolicyGroupRelPaginatedList(Class<PolicyGroupRel> classInstance, List<Criterion> conditions, Map<String, String> aliases,
	   		int offset, int limit, String sortColumn, String sortOrder);
	
}
