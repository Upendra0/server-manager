package com.elitecore.sm.policy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.SearchPolicyCondition;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.PolicyConditionValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.AutoSuggestUtilForCondition;
import com.elitecore.sm.util.DateFormatter;



/**
 * The Class PolicyRuleConditionController.
 */
@Controller
public class PolicyRuleConditionController extends BaseController{
	
	/** The policy service. */
	@Autowired
	IPolicyService policyService;
	
	/** The parser attribute service. */
	@Autowired
	ParserAttributeService parserAttributeService ;
	
	
	/** The server instance service. */
	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	/** The policy condition validator. */
	@Autowired
	PolicyConditionValidator policyConditionValidator;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		
	}

	/**
	 * Gets the policy rule condition list.
	 *
	 * @param searchPolicyCondition the search policy condition
	 * @param limit the limit
	 * @param currentPage the current page
	 * @param sidx the sidx
	 * @param sord the sord
	 * @param request the request
	 * @return the policy rule condition list
	 */
	@RequestMapping(value = ControllerConstants.GET_POLICY_RULE_CONDITION_LIST, method = RequestMethod.GET)
	public @ResponseBody String getPolicyRuleConditionList(
			SearchPolicyCondition searchPolicyCondition,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			HttpServletRequest request) {
		
		List<PolicyCondition>  resultList = null;
		long count = policyService.getTotalPolicyConditionCount(searchPolicyCondition);
		if(count > 0){
			resultList = this.policyService.getPaginatedList(searchPolicyCondition, eliteUtils.getStartIndex(limit, currentPage,
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx,sord);
		}
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if(resultList != null){
			
			for(PolicyCondition result : resultList){
				row = new HashMap<>();
				row.put("id", result.getId());
				row.put("name", result.getName());
				row.put("description", result.getDescription());
				row.put("type", result.getType());
				row.put("condition", result.getOperator());
				row.put("expression", result.getConditionExpression());
				row.put("value", result.getValue());
				row.put("unifiedField", result.getUnifiedField());
			/*	row.put("delete", result.getPolicyRuleSet() != null && 
				result.getPolicyRuleSet().size() > 0 ? "Associated" : "");*/
				row.put("delete", result.getPolicyRuleConditionRel() != null && 
						result.getPolicyRuleConditionRel().size() > 0 ? "Associated" : "");
				rowList.add(row);
			}
		}
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
	}

	/**
	 * Creates the policy condition.
	 *
	 * @param policyCondition the policy condition
	 * @param pageType the page type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_CONDITION_CONFIGURATION','UPDATE_POLICY_CONDITION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.ADD_POLICY_RULE_CONDITION, method = RequestMethod.POST)
	@ResponseBody public  String createPolicyCondition(@ModelAttribute (value=FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN)
	PolicyCondition policyCondition, @RequestParam(value = "pageType", required=false) String pageType,//NOSONAR
	@RequestParam(value = "policyConditionId" , required=false) String policyConditionId,
	BindingResult result, HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		policyConditionValidator.validatePolicyParameters(policyCondition, result, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			
		}else{
			ResponseObject responseObject;
			if(policyCondition != null){
				policyCondition.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				policyCondition.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ServerInstance serverInstance = serverInstanceService.getServerInstance(policyCondition.getServer().getId());
				policyCondition.setServer(serverInstance);
				policyCondition.setAlias(policyCondition.getName());
				if(pageType != null && "update".equalsIgnoreCase(pageType)){
					int id = 0;
					if (!StringUtils.isEmpty(policyConditionId)) {
						id = Integer.parseInt(policyConditionId);
					}
					policyCondition.setId(id);
					responseObject = policyService.updateCondition(policyCondition);
				}else{
					responseObject = policyService.saveCondition(policyCondition);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Delete policy rule condition.
	 *
	 * @param conditionId the condition id
	 * @param request the request
	 * @return the string
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_POLICY_CONDITION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.DELETE_POLICY_RULE_CONDITION, method = RequestMethod.POST)
	@ResponseBody
	public String deletePolicyRuleCondition(
			@RequestParam(value = "conditionId", required = true) String conditionId,
			HttpServletRequest request) throws CloneNotSupportedException {
		int conditionIdVal = 0;
		if (!StringUtils.isEmpty(conditionId)) {
			conditionIdVal = Integer.parseInt(conditionId);
		}
		ResponseObject responseObject = policyService.deletePolicyCondition(
				conditionIdVal, eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}

	@RequestMapping(value = "getConditionTags", method = RequestMethod.POST)
	@ResponseBody 
	public List<String> getTags(@RequestParam String tagName) {
		List<String> unifiedFields =parserAttributeService.getListOfUnifiedFields();
		return AutoSuggestUtilForCondition.simulateSearchResult(tagName, unifiedFields);
	}
	
	@RequestMapping(value = ControllerConstants.VALIDATE_CONDITION_EXPRESSION, method = RequestMethod.POST)
	@ResponseBody 
	public String isExpressionValid(@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,@ModelAttribute (value=FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN)
	PolicyCondition policyCondition, BindingResult result) {//NOSONAR
		ResponseObject responseObject = policyService.validateConditionExpression(serverInstanceId, policyCondition.getConditionExpression());
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}

