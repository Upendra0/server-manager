package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.SearchPolicy;

public interface IPolicyGroupDao extends GenericDAO<PolicyGroup> {

	/**
	 * Get Policy Group List by search parameters
	 * 
	 * @param policy the search policy object
	 * @return Search result
	 */
	/*	public Map<String, Object> getPolicyGroupCriteriaBySearchParams(SearchPolicy policy);*/
	
	public Map<String, Object> getPolicyGroupCriteriaBySearchParams(SearchPolicy policy , Integer[] existingIDS );
	
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
	public List<PolicyGroup> getPolicyGroupPaginatedList(Class<PolicyGroup> classInstance, List<Criterion> conditions, Map<String, String> aliases,
											   		int offset, int limit, String sortColumn, String sortOrder);
	
	/**
	 * Get Policy Groups associated with policy
	 * 
	 * @param policyId the policy id
	 * @return the associated policy group list
	 */
	public Map<String, Object> getPolicyGroupsCriteriaByPolicyId(int policyId);
	
	/**
	 * Get Policy group count by alias
	 * 
	 * @param alias the policy group alias
	 * @param serverId 
	 * @return the count of policy groups with same alias
	 */
	public long getPolicyGroupCountByAlias(String alias, int serverId);

	public List<PolicyGroup> getPolicyGroupforServerInstance(int serverInstanceId);
	
	public PolicyGroup getPolicyGroupByAlias(String alias, int serverId);
	
}
