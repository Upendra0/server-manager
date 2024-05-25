package com.elitecore.sm.errorreprocess.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.JQGridParams;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;
import com.elitecore.sm.errorreprocess.service.RuleActionConditionService;
import com.elitecore.sm.errorreprocess.validator.RuleActionConditionValidator;

/**
 * @author Ranjitsinh Reval
 *
 */
@RestController
public class RuleActionConditionController extends BaseController {

	
	@Autowired
	private RuleActionConditionService actionConditionService;
	
	@Autowired
	private RuleActionConditionValidator  actionConditionValidator;	
	/**
	 * Method will create action condition details for error re-processing.
	 * @param ruleActionConditionObj
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.CRAETE_RULE_ACTION_CONDITION, method = RequestMethod.POST)
	public String createRuleActionCondition(@RequestBody RuleConditionDetails ruleActionConditionObj,BindingResult result) {//NOSONAR
		
		AjaxResponse ajaxResponse  = new AjaxResponse();
		this.actionConditionValidator.validateActionConditionExpression(ruleActionConditionObj, result, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			/*int staffId = eliteUtils.getLoggedInStaffId(request);
			ruleActionConditionObj.setCreatedByStaffId(staffId);
			ruleActionConditionObj.setCreatedDate(new Date());*/
			//ResponseObject responseObject = this.actionConditionService.createActionCondition(ruleActionConditionObj);
			ResponseObject responseObject = new ResponseObject();
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will get all static action and condition list. 
	 * @param serverInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.GET_ALL_ACTION_CONDITION_LIST, method = RequestMethod.POST)
	@ResponseBody public String getAllActionCondtionList(JQGridParams jqGridParams){
		long count = this.actionConditionService.getRuleActionConditionTotalCount();
		List<Map<String, Object>> rowList = null;
		if (count > 0) {
			rowList = this.actionConditionService.getRuleActionConditionPaginatedList(eliteUtils.getStartIndex(jqGridParams.getRows(),jqGridParams.getPage(),eliteUtils.getTotalPagesCount(count, jqGridParams.getRows())),
					jqGridParams.getRows(), jqGridParams.getSidx(), jqGridParams.getSord());
		}
		logger.debug("<< getAllActionCondtionList in RuleActionCondition controller. ");
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, jqGridParams.getRows()), jqGridParams.getPage(),(int) count, rowList).getJsonString();
	}
	
	@RequestMapping(value = ControllerConstants.UPDATE_RULE_CONDITION_APPLICATION_ORDER, method = RequestMethod.POST)
	@ResponseBody public String updateRuleConditionApplicationOrder(@RequestParam(value="ruleConditionActionJson",required=true) String ruleConditionActionJson, HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = this.actionConditionService.changeApplicationOrder(ruleConditionActionJson, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will update action condition details for error re-processing.
	 * @param ruleActionConditionObj
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.UPDATE_RULE_ACTION_CONDITION, method = RequestMethod.POST)
	public String updateRuleActionCondition(@RequestBody RuleConditionDetails ruleActionConditionObj,BindingResult result, HttpServletRequest request) {//NOSONAR
		
		AjaxResponse ajaxResponse  = new AjaxResponse();
		this.actionConditionValidator.validateActionConditionExpression(ruleActionConditionObj, result, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			ruleActionConditionObj.setLastUpdatedByStaffId(staffId);
			ruleActionConditionObj.setLastUpdatedDate(new Date());
			
			ResponseObject responseObject = this.actionConditionService.updateActionCondtionDetails(ruleActionConditionObj);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will update action condition details for error re-processing.
	 * @param ruleActionConditionObj
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.DELETE_RULE_ACTION_CONDITION, method = RequestMethod.POST)
	public String deleteRuleActionCondition(@RequestBody RuleConditionDetails ruleActionConditionObj, HttpServletRequest request) {//NOSONAR
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = this.actionConditionService.deleteActionConditionDetails(ruleActionConditionObj.getId(), staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
}
