package com.elitecore.sm.policy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.PolicyConditionOperatorEnum;
import com.elitecore.sm.common.model.PolicyRuleCategoryEnum;
import com.elitecore.sm.common.model.PolicyRuleSeverityEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.parser.model.UnifiedFieldEngineEnum;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.model.PolicyRuleActionRel;
import com.elitecore.sm.policy.model.PolicyRuleConditionRel;
import com.elitecore.sm.policy.model.PolicyRuleHistory;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.PolicyRuleValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author chintan.patel
 *
 */
@Controller
public class PolicyRuleController extends BaseController{

	@Autowired
	IPolicyService policyService;
	
	@Autowired
	SnmpService snmpService;
	
	@Autowired
	PolicyRuleValidator policyRuleValidator;
	
	@Autowired
	IDatabaseQueryService databaseQueryService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));

	}

	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_RULE_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.INIT_CREATE_RULE,method=RequestMethod.GET)
	public ModelAndView initCreateRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) String instanceId,
			@RequestParam(value=BaseConstants.RULE_ID , required=false) String ruleId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value=BaseConstants.CURRENT_PAGE , required=false) Integer currentPage){

		ModelAndView model = new ModelAndView(ViewNameConstants.POLICY_RULE_MANAGER);
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.RULE_ID, ruleId);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.CURRENT_PAGE,currentPage);
		
		List<DatabaseQuery> databaseQueries = databaseQueryService.getAllQueriesByServerId(Integer.parseInt(instanceId));
		List<String> databaseQueryName = new ArrayList<>(); 
		for(DatabaseQuery databaseQuery : databaseQueries){
			databaseQueryName.add(databaseQuery.getQueryName());
		}
		model.addObject("databaseQueryList",databaseQueryName);
		
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEngineEnum.values()));
		model.addObject("logicalOperatorEnum", PolicyConditionOperatorEnum.getPolicyConditionList());
		model.addObject(FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN, (PolicyCondition) SpringApplicationContext
				.getBean(PolicyCondition.class));
		
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEngineEnum.values()));
		model.addObject(FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN, (PolicyAction) SpringApplicationContext
				.getBean(PolicyAction.class));
		
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_CATEGORY,java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_SEVERITY,java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CREATE_NEW_RULE);
		int id = 0;
		try {
			id = Integer.parseInt(ruleId);
		} catch (NumberFormatException e) {
			// Invalid / Null policy id
		}
		PolicyRule policyRule = new PolicyRule();
		if (id != 0) {
			policyRule = policyService.getPolicyRuleById(id);
		}
		List<SNMPAlert> snmpAlertList = snmpService.getAllSnmpAlerts();
		model.addObject("snmpAlerts", snmpAlertList);
		model.addObject(FormBeanConstants.RULE_FORM_BEAN, policyRule);

		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_RULE_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.CREATE_RULE, method = RequestMethod.POST)	
	public ModelAndView createRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@ModelAttribute(value = FormBeanConstants.RULE_FORM_BEAN) PolicyRule policyRule,//NOSONAR
			@RequestParam(value="rule-alert", required=false) String alertId,
			@RequestParam(value="policy-rule-conditions", required=false) String ruleConditions,
			@RequestParam(value="policy-rule-actions", required=false) String ruleActions,
			@RequestParam(value=BaseConstants.CURRENT_PAGE, required=false) Integer currentPage,
			HttpServletRequest request,
			BindingResult result) {

		ModelAndView model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
		policyRuleValidator.validatePolicyRuleParameters(policyRule, result, null, null, false , instanceId , ControllerConstants.CREATE_RULE );
		
		if(result.hasErrors()) {
			model = new ModelAndView(ViewNameConstants.POLICY_RULE_MANAGER);
			List<SNMPAlert> snmpAlertList = snmpService.getAllSnmpAlerts();
			model.addObject("snmpAlerts", snmpAlertList);
			model.addObject(FormBeanConstants.RULE_FORM_BEAN, policyRule);
			model.addObject(FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN, (PolicyCondition) SpringApplicationContext
					.getBean(PolicyCondition.class));
			model.addObject(FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN, (PolicyAction) SpringApplicationContext
					.getBean(PolicyAction.class));
			model.addObject(BaseConstants.PROCESSING_POLICY_RULE_CATEGORY,java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
			model.addObject(BaseConstants.PROCESSING_POLICY_RULE_SEVERITY,java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		} else {
			policyRule.setAlias(policyRule.getName());
			if(policyRule.getServer() == null) {
				policyRule.setServer(new ServerInstance());
			}
			policyRule.getServer().setId(instanceId);
			if(policyRule.getDescription() == null) {
				policyRule.setDescription(StringUtils.EMPTY);
			}
			if(StringUtils.isNotEmpty(alertId)) {
				int id = 0;
				try {
					id = Integer.parseInt(alertId);
				}catch (NumberFormatException e) {
					// Do nothing
				}
				if(id != 0) {
					SNMPAlert snmpAlert = snmpService.getSnmpAlertById(id);
					policyRule.setAlert(snmpAlert);
				}
			}
			ResponseObject responseObject = policyService.savePolicyRule(policyRule, ruleConditions, ruleActions, eliteUtils.getLoggedInStaffId(request), instanceId);
			if(!responseObject.isSuccess()) {
				model.setViewName(ViewNameConstants.POLICY_RULE_MANAGER);
				model.addObject(FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN, (PolicyCondition) SpringApplicationContext
						.getBean(PolicyCondition.class));
				model.addObject(FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN, (PolicyAction) SpringApplicationContext
						.getBean(PolicyAction.class));
				
				
				
			}else{
				//Storing Rule Data Into RuleHistory Table
			PolicyRuleHistory policyRuleHistory = new PolicyRuleHistory();
			policyRuleHistory.setRuleName(policyRule.getName());
			policyRuleHistory.setRuleCatagory("catagory");
			policyRuleHistory.setRuleSubCatagory("subCatagory");
			policyRuleHistory.setErrorCode("errorCode");
			policyService.saveRuleHistory(policyRuleHistory);
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
			}
		}
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_CATEGORY,java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_SEVERITY,java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.POLICY_RULE);
		return model;
		
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_POLICY_RULE_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.UPDATE_RULE, method = RequestMethod.POST)	
	public ModelAndView updateRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@ModelAttribute(value = FormBeanConstants.RULE_FORM_BEAN) PolicyRule policyRule,//NOSONAR
			@RequestParam(value="rule-alert", required=false) String alertId,
			@RequestParam(value="policy-rule-conditions", required=false) String ruleConditions,
			@RequestParam(value="policy-rule-actions", required=false) String ruleActions,
			@RequestParam(value=BaseConstants.CURRENT_PAGE, required=false) Integer currentPage,
			HttpServletRequest request,
			BindingResult result) {

		ModelAndView model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
		policyRuleValidator.validatePolicyRuleParameters(policyRule, result, null, null, false , instanceId , ControllerConstants.UPDATE_RULE );
		
		if(result.hasErrors()) {
			/** model = new ModelAndView(ViewNameConstants.POLICY_RULE_MANAGER);
			List<SNMPAlert> snmpAlertList = snmpService.getAllSnmpAlerts();
			model.addObject("snmpAlerts", snmpAlertList);
			model.addObject(FormBeanConstants.RULE_FORM_BEAN, policyRule); **/
			
			model = new ModelAndView(ViewNameConstants.POLICY_RULE_MANAGER);
			List<SNMPAlert> snmpAlertList = snmpService.getAllSnmpAlerts();
			model.addObject("snmpAlerts", snmpAlertList);
			model.addObject(FormBeanConstants.RULE_FORM_BEAN, policyRule);
			model.addObject(FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN, (PolicyCondition) SpringApplicationContext
					.getBean(PolicyCondition.class));
			model.addObject(FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN, (PolicyAction) SpringApplicationContext
					.getBean(PolicyAction.class));
			
		} else {
			policyRule.setAlias(policyRule.getName());
			if(policyRule.getServer() == null) {
				policyRule.setServer(new ServerInstance());
			}
			policyRule.getServer().setId(instanceId);
			if(policyRule.getDescription() == null) {
				policyRule.setDescription(StringUtils.EMPTY);
			}
			if(StringUtils.isNotEmpty(alertId)) {
				int id = 0;
				try {
					id = Integer.parseInt(alertId);
				}catch (NumberFormatException e) {
					// Do nothing
				}
				if(id != 0) {
					SNMPAlert snmpAlert = snmpService.getSnmpAlertById(id);
					policyRule.setAlert(snmpAlert);
				}
			}
			ResponseObject responseObject = policyService.updatePolicyRule(policyRule, ruleConditions, ruleActions, eliteUtils.getLoggedInStaffId(request), instanceId);
			
			if(!responseObject.isSuccess()) {
				model.setViewName(ViewNameConstants.POLICY_RULE_MANAGER);
			} else{
				//Storing Rule Data into RuleHistory Table
				PolicyRuleHistory policyRuleHistory = new PolicyRuleHistory();
				policyRuleHistory.setRuleName(policyRule.getName());
				policyRuleHistory.setRuleCatagory("catagory");
				policyRuleHistory.setRuleSubCatagory("subCatagory");
				policyRuleHistory.setErrorCode("errorCode");
				policyService.saveRuleHistory(policyRuleHistory);
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
			}
		}
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_CATEGORY,java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_SEVERITY,java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.POLICY_RULE);
		model.addObject(BaseConstants.CURRENT_PAGE, currentPage);
		return model;
		
	}

	@ResponseBody 
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_RULE_LIST, method = RequestMethod.GET)
	public String getPolicyRuleList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = BaseConstants.RULE_NAME, required = false) String ruleName,
			@RequestParam(value = BaseConstants.DESCRIPTION, required = false) String searchDescription,
			@RequestParam(value = BaseConstants.ASSOCIATION_STATUS, required = false) String searchAssociationStatus,
			@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID) int serverInstanceId,
			@RequestParam(value = "reasonCategory") String reasonCategory,
			@RequestParam(value = "reasonSeverity") String reasonSeverity,
			@RequestParam(value = "reasonErrorCode") String reasonErrorCode) {

		SearchPolicy searchPolicy = new SearchPolicy(ruleName, searchDescription, searchAssociationStatus, null, serverInstanceId,reasonCategory,reasonSeverity,reasonErrorCode);
		
		long count = policyService.getTotalPolicyRuleCount(searchPolicy);

		List<PolicyRule> resultList = new ArrayList<>();
		if (count > 0) {
			resultList = policyService.getPolicyRulePaginatedList(searchPolicy, 
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}

		Map<String, Object> row;

		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			for (PolicyRule rule : resultList) {
				row = new HashMap<>();
				row.put(BaseConstants.ID, rule.getId());
				row.put(BaseConstants.NAME, rule.getName());
				row.put(BaseConstants.DESCRIPTION, StringUtils.isNotEmpty(rule.getDescription()) ? rule.getDescription() : StringUtils.EMPTY);
				row.put(BaseConstants.ASSOCIATION_STATUS, rule.getAssociationStatus());
				row.put("category",rule.getCategory());
				row.put("severity",rule.getSeverity());
				row.put("error_code",rule.getErrorCode());
				row.put("alertId", rule.getAlert() != null ? rule.getAlert().getName() : StringUtils.EMPTY);
				row.put("alertDescription", StringUtils.isNotEmpty(rule.getAlertDescription()) ? rule.getAlertDescription() : StringUtils.EMPTY);
				row.put("globalSequenceId", StringUtils.isNotEmpty(rule.getGlobalSequenceRuleId()) ? rule.getGlobalSequenceRuleId() : StringUtils.EMPTY);
				row.put("operator", rule.getOperator());
				row.put("additionalParam", "");

				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}

	/**
	 * Get Policy Condition List By Rule 
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param ruleId
	 * @return list of Condition Action
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_CONDITION_LIST_BY_RULE_ID, method = RequestMethod.GET)
	@ResponseBody
	public String getPolicyConditionListByRule(@RequestParam(value = "rows", defaultValue = "10") int limit,
												@RequestParam(value = "page", defaultValue = "1") int currentPage,
												@RequestParam(value = "sidx" , required=false) String sidx,
												@RequestParam(value = "sord" , required=false) String sord,
												@RequestParam(value = "ruleId") int ruleId) {
		
		long count =  policyService.getPolicyConditionCountByRule(ruleId);

		sidx = "applicationOrder";
		List<PolicyRuleConditionRel> resultList = new ArrayList<>();
		if(count > 0){
			resultList = policyService.getPolicyConditionPaginatedListByRule(ruleId, eliteUtils.getStartIndex(limit, currentPage, 
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (PolicyRuleConditionRel policyRuleCondition : resultList) {
				
				PolicyCondition policyCondition = policyRuleCondition.getCondition();
				
				row = new HashMap<>();
				
				row.put(BaseConstants.ID, policyCondition.getId());
				row.put(BaseConstants.NAME, policyCondition.getName());
				row.put(BaseConstants.DESCRIPTION, policyCondition.getDescription());
				row.put("expression", policyCondition.getConditionExpression());
				row.put("type", policyCondition.getType());
				row.put("operator", policyCondition.getOperator());
				row.put("unifiedField", policyCondition.getUnifiedField());
				row.put("condition", policyCondition.getOperator());
				row.put("value", policyCondition.getValue());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
													(int) count, rowList).getJsonString();
	}
	
	/**
	 * Get Policy Action List By Rule 
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param ruleId
	 * @return list of Policy Action
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_ACTION_LIST_BY_RULE_ID, method = RequestMethod.GET)
	@ResponseBody
	public String getPolicyActionListByRule(@RequestParam(value = "rows", defaultValue = "10") int limit,
											@RequestParam(value = "page", defaultValue = "1") int currentPage,
											@RequestParam(value = "sidx" , required=false) String sidx,
											@RequestParam(value = "sord" , required=false) String sord,
											@RequestParam(value = "ruleId") int ruleId) {
		
		long count =  policyService.getPolicyActionCountByRule(ruleId);
		
		
		List<PolicyRuleActionRel> resultList = new ArrayList<>();
		if(count > 0){
			resultList = policyService.getPolicyActionPaginatedListByRule(ruleId, eliteUtils.getStartIndex(limit, currentPage, 
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for ( PolicyRuleActionRel policyRuleAction : resultList) {
				
				PolicyAction policyAction = policyRuleAction.getAction();
				row = new HashMap<>();
				
				row.put(BaseConstants.ID, policyAction.getId());
				row.put(BaseConstants.NAME, policyAction.getName());
				row.put(BaseConstants.DESCRIPTION, policyAction.getDescription());
				row.put("expression", policyAction.getActionExpression());
				row.put("action", policyAction.getAction());
				row.put("type", policyAction.getType());
				row.put("operator", policyAction.getOperator());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
													(int) count, rowList).getJsonString();
	}
	
	/**
	 * Remove Policy Rule
	 * 
	 * @param ruleId the policy Id
	 * @return the remove response
	 */
	@RequestMapping(value = ControllerConstants.REMOVE_RULE, method = RequestMethod.POST)
	@ResponseBody
	public String removePolicyRule(@RequestParam("ruleId") int ruleId) {
		AjaxResponse ajaxResponse;
		
		ResponseObject responseObject = policyService.removePolicyRule(ruleId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
}