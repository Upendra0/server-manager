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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.SearchPolicyAction;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.PolicyActionValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.AutoSuggestUtil;
import com.elitecore.sm.util.DateFormatter;

/**
 * The Class PolicyRuleActionController.
 */
@Controller
public class PolicyRuleActionController extends BaseController{
	
	@Autowired
	IDatabaseQueryService databaseQueryService;
	
	
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
	
	/** The policy action validator. */
	@Autowired
	PolicyActionValidator policyActionValidator;
	
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
	 * Gets the policy rule action list.
	 *
	 * @param searchPolicyAction the search policy action
	 * @param limit the limit
	 * @param currentPage the current page
	 * @param sidx the sidx
	 * @param sord the sord
	 * @param request the request
	 * @return the policy rule action list
	 */
	@RequestMapping(value = ControllerConstants.GET_POLICY_RULE_ACTION_LIST, method = RequestMethod.GET)
	public @ResponseBody String getPolicyRuleActionList(
			SearchPolicyAction searchPolicyAction,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			HttpServletRequest request) {
		
		List<PolicyAction>  resultList = null;
		long count = policyService.getTotalPolicyActionCount(searchPolicyAction);
		if(count > 0){
			resultList = this.policyService.getPaginatedList(searchPolicyAction, eliteUtils.getStartIndex(limit, currentPage,
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx,sord);
		}
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if(resultList != null){
			
			for(PolicyAction result : resultList){
				row = new HashMap<>();
				row.put("id", result.getId());
				row.put("name", result.getName());
				row.put("description", result.getDescription());
				row.put("type", result.getType());
				row.put("action", result.getAction());
				row.put("expression", result.getActionExpression());
				/** row.put("delete", result.getPolicyRuleSet() != null && 
						result.getPolicyRuleSet().size() > 0 ? "Associated" : "");*/
				row.put("delete", result.getPolicyRuleActionRel() != null && 
						result.getPolicyRuleActionRel().size() > 0 ? "Associated" : "");
				rowList.add(row);
			}
		}
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
	}

	/**
	 * Creates the policy action.
	 *
	 * @param policyAction the policy action
	 * @param pageType the page type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_ACTION_CONFIGURATION','UPDATE_POLICY_ACTION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.ADD_POLICY_RULE_ACTION, method = RequestMethod.POST)
	@ResponseBody public  String createPolicyAction(@ModelAttribute (value=FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN)
	PolicyAction policyAction, @RequestParam(value = "pageType", required=false) String pageType,//NOSONAR
	@RequestParam(value = "policyActionId" , required=false) String policyActionId,
	BindingResult result, HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		policyAction.setActionExpression(policyAction.getActionExpression().replaceAll("\n", " ").trim());
		policyActionValidator.validatePolicyParameters(policyAction, result, null, false);
		if(result.hasErrors()){
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			
		}else{
			ResponseObject responseObject;
			if(policyAction != null){
				policyAction.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				policyAction.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ServerInstance serverInstance = serverInstanceService.getServerInstance(policyAction.getServer().getId());
				policyAction.setServer(serverInstance);
				policyAction.setAlias(policyAction.getName());
				if(pageType != null && "update".equalsIgnoreCase(pageType)){
					int id = 0;
					if (!StringUtils.isEmpty(policyActionId)) {
						id = Integer.parseInt(policyActionId);
					}
					policyAction.setId(id);
					responseObject = policyService.updateAction(policyAction);
				}else{
					responseObject = policyService.saveAction(policyAction);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Delete policy rule action.
	 *
	 * @param actionId the action id
	 * @param request the request
	 * @return the string
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_POLICY_ACTION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.DELETE_POLICY_RULE_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String deletePolicyRuleAction(
			@RequestParam(value = "actionId", required = true) String actionId,
			HttpServletRequest request) throws CloneNotSupportedException {
		int actionIdVal = 0;
		if (!StringUtils.isEmpty(actionId)) {
			actionIdVal = Integer.parseInt(actionId);
		}
		ResponseObject responseObject = policyService.deletePolicyAction(
				actionIdVal, eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}

	@RequestMapping(value = "getTags", method = RequestMethod.GET)
	@ResponseBody 
	public List<String> getTags(@RequestParam String tagName) {
		List<String> unifiedFields =parserAttributeService.getListOfUnifiedFields();
		return AutoSuggestUtil.simulateSearchResult(tagName, unifiedFields);
	}
	
	@RequestMapping(value = ControllerConstants.VALIDATE_ACTION_EXPRESSION, method = RequestMethod.POST)
	@ResponseBody 
	public String isExpressionValid(@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,@ModelAttribute (value=FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN)
	PolicyAction policyAction, BindingResult result) {//NOSONAR
		ResponseObject responseObject = policyService.validateActionExpression(serverInstanceId, policyAction.getActionExpression());
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}