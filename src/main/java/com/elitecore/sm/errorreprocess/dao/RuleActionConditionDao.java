package com.elitecore.sm.errorreprocess.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface RuleActionConditionDao extends GenericDAO<RuleConditionDetails> {

	
	
	/**
	 * Method will create condition list and alias, order by condition.
	 * @return
	 */
	public Map<String, Object> getRuleConditionList();
	
	
	/**
	 * Method will get rule condition details record with paginated and order by based on parameter
	 * @param ruleConditionDetails
	 * @param conditions
	 * @param aliases
	 * @param offset
	 * @param limit
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	public List<RuleConditionDetails> getRuleConditionPaginatedList(Class<RuleConditionDetails> ruleConditionDetails, List<Criterion> conditions,	Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder); 
	
	
	
	/**
	 * Method will update application order for  rule.
	 * @return
	 */
	public List<RuleConditionDetails> getLastApplicationOrder();
	
	
	/**
	 * Method will get all active rule condition list.
	 * @return
	 */
	public List<RuleConditionDetails> getAllActiveRuleList();
}
