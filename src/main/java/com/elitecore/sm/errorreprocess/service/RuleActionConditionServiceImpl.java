/**
 * 
 */
package com.elitecore.sm.errorreprocess.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.dao.RuleActionConditionDao;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;

/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value="ruleActionConditionService")
public class RuleActionConditionServiceImpl implements RuleActionConditionService {

	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private RuleActionConditionDao actionConditionDao;
	
	
	/**
	 * Method will create new action condition details.
	 * @param actionConditionObj
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject createActionCondition(RuleConditionDetails actionConditionObj) {
		logger.debug("inside save rule condition action");
		ResponseObject responseObject = new ResponseObject();
		
		this.actionConditionDao.save(actionConditionObj);
		
		if(actionConditionObj.getId() > 0 ){
			logger.info("Action and condition details created successfully!");
			responseObject.setSuccess(true);
			responseObject.setObject(actionConditionObj);
			responseObject.setResponseCode(ResponseCode.RULE_ACTION_CONDITION_CREATE_SUCCESS);
		}else{
			logger.info("Failed to create action and condition.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_RULE_ACTION_CONDITION_CREATE);
		}
		return responseObject;
	}

	
	/**
	 * Method will update action condition details.
	 * @param actionConditionObj
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject updateActionCondtionDetails(RuleConditionDetails actionConditionObj) {
		logger.debug("inside update action condition details.");
		ResponseObject responseObject = new ResponseObject();
		
		if(actionConditionObj.getId() > 0 ){
			this.actionConditionDao.update(actionConditionObj);
			logger.info("Action and condition details updated successfully!");
			responseObject.setSuccess(true);
			responseObject.setObject(actionConditionObj);
			responseObject.setResponseCode(ResponseCode.RULE_ACTION_CONDITION_UPDATE_SUCCESS);
		}else{
			logger.info("Failed to update action and condition.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_RULE_ACTION_CONDITION_UPDATE);
		}
		return responseObject;
	}

	/**
	 * Method will delete action condition details.
	 * @param actionConditionObj
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject deleteActionConditionDetails(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Inside delete of action condition details.");
		if(id > 0 ){
			RuleConditionDetails ruleConditionDetail = this.actionConditionDao.findByPrimaryKey(RuleConditionDetails.class, id);
			if(ruleConditionDetail != null){
				ruleConditionDetail.setLastUpdatedDate(new Date());
				ruleConditionDetail.setLastUpdatedByStaffId(staffId);
				ruleConditionDetail.setStatus(StateEnum.DELETED);
				
				this.actionConditionDao.update(ruleConditionDetail);
				
				responseObject.setSuccess(true);
				responseObject.setObject(ruleConditionDetail);
				responseObject.setResponseCode(ResponseCode.RULE_ACTION_CONDITION_DELETE_SUCCESS);
				logger.info("Action condition details delete successfully!");;
			}else{
				logger.debug("Failed to get condition action details for id :: " + id);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_RULE_ACTION_CONDITION_DELETE);
			}
			
			
		}else{
			logger.info("Failed to delete action and condition.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_RULE_ACTION_CONDITION_DELETE);
		}
		
		return responseObject;
	}

	
	@Override
	@Transactional(readOnly = true)
	public long getRuleActionConditionTotalCount() {
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return this.actionConditionDao.getQueryCount(RuleConditionDetails.class,	conditions,	null);
	}

	/**
	 * Method will delete action condition details.
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getRuleActionConditionPaginatedList(int startIndex, int limit, String sidx, String sord) {
		logger.debug("Inside load action condition details page.");
		
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = this.actionConditionDao.getRuleConditionList();

		List<RuleConditionDetails> ruleConditionList = this.actionConditionDao.getRuleConditionPaginatedList(RuleConditionDetails.class,	(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);  

		if (ruleConditionList != null && !ruleConditionList.isEmpty()) {
			for (RuleConditionDetails ruleCondition : ruleConditionList) {
				row = new HashMap<>();
				row.put("id", ruleCondition.getId());
				row.put("actionExpression", ruleCondition.getActionExpression());
				row.put("conditionExpression", ruleCondition.getConditionExpression());
				row.put("applicationOrder", ruleCondition.getApplicationOrder());
				row.put("upAction", "");
				row.put("dwonAction", "");
				row.put(BaseConstants.EDIT, "Edit");
				row.put("Delete", "Delete");
				rowList.add(row);
			}
		}
		return rowList;
		
	}

	/**
	 * Method will update application order rule and condition.
	 * @param jsonData
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject changeApplicationOrder(String jsonData, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray jsonArray = new JSONArray(jsonData); 
		
		List<RuleConditionDetails> ruleConditionList = new ArrayList<>();
		for(int i=0; i< jsonArray.length();i++){
			RuleConditionDetails ruleCondition = new RuleConditionDetails();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			
			ruleCondition.setId(jsonObject.getInt("id"));
			ruleCondition.setActionExpression(jsonObject.getString("actionExpression"));
			ruleCondition.setConditionExpression(jsonObject.getString("conditionExpression"));
			ruleCondition.setApplicationOrder(jsonObject.getInt("applicationOrder"));
			ruleCondition.setLastUpdatedDate(new Date());
			ruleCondition.setLastUpdatedByStaffId(staffId);
			ruleConditionList.add(ruleCondition);
		}
		
		if(!ruleConditionList.isEmpty()){
			for (RuleConditionDetails ruleConditionDetails : ruleConditionList) {
				this.actionConditionDao.update(ruleConditionDetails);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SUCCESS_RULE_APPLICATION_ORDER);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_RULE_APPLICATION_ORDER);
		}
		
		return responseObject;
	}


	@Override
	@Transactional
	public ResponseObject getAllActiveRuleList() {
		ResponseObject responseObject = new ResponseObject();
		List<RuleConditionDetails> ruleDetailsList = this.actionConditionDao.getAllActiveRuleList();
		if(ruleDetailsList != null && !ruleDetailsList.isEmpty()){
			logger.info("rule details list fetch successfully!");
			responseObject.setSuccess(true);
			responseObject.setObject(ruleDetailsList);
		}else{
			logger.info("Failed to get rule details.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_RULE_DETAILS);
		}
		return responseObject;
	}


	@Override
	@Transactional
	public ResponseObject updateRuleStatus(List<RuleConditionDetails> ruleDetailsList) {
		ResponseObject responseObject = new ResponseObject();
		
		if(ruleDetailsList != null && !ruleDetailsList.isEmpty()){
			for (int i = 0; i < ruleDetailsList.size(); i++) {
				this.actionConditionDao.update(ruleDetailsList.get(i));
			}
			responseObject.setSuccess(true);
		}
		
		return responseObject;
	}
	
	
}
