/**
 * 
 */
package com.elitecore.sm.errorreprocess.service;


import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;
/**
 * @author Ranjitsinh
 *
 */
public interface RuleActionConditionService {


	/**
	 * Method will create new action condition details.
	 * @param actionConditionObj
	 * @return
	 */
	public ResponseObject  createActionCondition(RuleConditionDetails actionConditionObj);
	
	
	/**
	 * Method will update action condition details.
	 * @param actionConditionObj
	 * @return
	 */
	public ResponseObject updateActionCondtionDetails(RuleConditionDetails actionConditionObj);
	
	
	/**
	 * Method will delete action condition details.
	 * @param id
	 * @return
	 */
	public ResponseObject deleteActionConditionDetails(int id, int staffId);
	
	public long getRuleActionConditionTotalCount();
	
	public List<Map<String, Object>> getRuleActionConditionPaginatedList( int startIndex, int limit, String sidx, String sord);
	
	
	/**
	 * Method will update application order rule and condition.
	 * @param jsonData
	 * @return
	 */
	public ResponseObject changeApplicationOrder(String jsonData, int staffId);
	
	public ResponseObject getAllActiveRuleList();
	
	public ResponseObject updateRuleStatus(List<RuleConditionDetails> ruleList);
}
