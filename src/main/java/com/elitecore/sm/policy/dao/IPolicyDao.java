package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.SearchPolicy;

/**
 * 
 * @author chintan.patel
 *
 */
public interface IPolicyDao extends GenericDAO<Policy> {

	/**
	 * Get Policy List Criteria by search parameters
	 * 
	 * @param policy the search policy object
	 * @return Search result
	 */
	public Map<String, Object> getPolicyCriteriaBySearchParams(SearchPolicy policy);
	
	/**
	 * Get Paginated list of policies
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
	public List<Policy> getPolicyPaginatedList(Class<Policy> classInstance, List<Criterion> conditions, Map<String, String> aliases,
											   int offset, int limit, String sortColumn, String sortOrder);
	
	/**
	 * Iterate over Policy sub objects
	 * 
	 * @param policy The Policy
	 */
	public void iterateOverPolicy(Policy policy);
	
	/**
	 * Get Policy count by alias
	 * 
	 * @param alias the policy alias
	 * @param serverId 
	 * @return the count of policies with same alias
	 */
	public long getPolicyCountByAlias(String alias, int serverId);
	
	
	/**
	 * Get Policy by alias
	 * 
	 * @param alias the policy alias
	 * @return the Policy object
	 */
	public Policy getPolicyByAlias(String alias, int serverId);

	public List<Policy> getPolicyforServerInstance(int serverInstanceId);
	
	public Policy getPolicyFullHierarchy(int policyId) throws SMException;
}
