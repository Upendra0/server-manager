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
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyGroupRuleRel;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.PolicyRuleGroupValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class PolicyRuleGroupController extends BaseController{

	@Autowired
	IPolicyService policyService;
	
	@Autowired
	PolicyRuleGroupValidator policyRuleGroupValidator;
	
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

	/**
	 * Get Rule Group List
	 * 
	 * @param limit the row limit
	 * @param currentPage current grid page
	 * @param sidx sorting column id
	 * @param sord sorting column order
	 * @param ruleGroupName rule group name
	 * @param groupDescription group description
	 * @param searchAssociationStatus search association status
	 * @param serverInstanceId server instance id
	 * @return the rule group list
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_RULE_GROUP_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getRuleGroupList(@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = BaseConstants.RULE_GROUP_NAME, required = false) String ruleGroupName,
			@RequestParam(value = BaseConstants.DESCRIPTION, required = false) String groupDescription,
			@RequestParam(value = BaseConstants.ASSOCIATION_STATUS, required = false) String searchAssociationStatus,
			@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID) int serverInstanceId,
			@RequestParam(value = "existingConditionIds" , required=false) Integer[] existingIDS  ) {

		
		
		SearchPolicy searchResult = new SearchPolicy(ruleGroupName, groupDescription, searchAssociationStatus, null, serverInstanceId,null,null,null);
		
		long count = policyService.getTotalPolicyGroupCount(searchResult , existingIDS );

		List<PolicyGroup> resultList = new ArrayList<>();
		if (count > 0)
			resultList = policyService.getPolicyGroupPaginatedList(searchResult, existingIDS ,
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);

		Map<String, Object> row;

		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (PolicyGroup group : resultList) {
				row = new HashMap<>();
				row.put("id", group.getId());
				row.put("name", group.getName());
				row.put("description", group.getDescription());
				row.put(BaseConstants.ASSOCIATION_STATUS, group.getAssociationStatus());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}

	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_GROUP_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.INIT_CREATE_RULE_GROUP,method=RequestMethod.GET)
	public ModelAndView initCreateRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) String instanceId,
			@RequestParam(value=BaseConstants.RULE_GROUP_ID , required=false) String ruleGroupId,
			@RequestParam(value=BaseConstants.RULE_GROUP_NAME , required=false) String ruleGroupName,
			@RequestParam(value=BaseConstants.RULE_GROUP_DESC , required=false) String ruleGroupDesc,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName){

		ModelAndView model=new ModelAndView(ViewNameConstants.CREATE_RULE_GROUP);
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.RULE_GROUP_ID,ruleGroupId);
		model.addObject(BaseConstants.RULE_GROUP_NAME,ruleGroupName);
		model.addObject(BaseConstants.RULE_GROUP_DESC,ruleGroupDesc);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		int id = 0;
		try {
			id = Integer.parseInt(ruleGroupId);
		} catch (NumberFormatException e) {
			// Invalid / Null policy id
		}
		PolicyGroup policyGroup = new PolicyGroup();
		if (id != 0) {
			policyGroup = policyService.getPolicyGroupById(id);
		}
		model.addObject(FormBeanConstants.RULEGROUP_FORM_BEAN, policyGroup);

		return model;
	}

	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_GROUP_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.CREATE_RULE_GROUP, method = RequestMethod.POST)
	public ModelAndView createRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value="policy-grouprules") String policyGroupRules,
			@ModelAttribute(value = FormBeanConstants.RULEGROUP_FORM_BEAN) PolicyGroup policyGroup,//NOSONAR
			HttpServletRequest request,
			BindingResult result) {

		ModelAndView model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);; 
		policyRuleGroupValidator.validatePolicyRuleGroupParameters(policyGroup, result, null, null, false);
		
		if(result.hasErrors()) {
			model = new ModelAndView(ViewNameConstants.CREATE_RULE_GROUP);
			model.addObject(FormBeanConstants.RULEGROUP_FORM_BEAN, policyGroup);
		} else {
			policyGroup.setAlias(policyGroup.getName());
			if(policyGroup.getServer() == null) {
				policyGroup.setServer(new ServerInstance());
			}
			policyGroup.getServer().setId(instanceId);
			if(policyGroup.getDescription() == null) {
				policyGroup.setDescription(StringUtils.EMPTY);
			}
			ResponseObject responseObject = policyService.savePolicyGroup(policyGroup, policyGroupRules, eliteUtils.getLoggedInStaffId(request), instanceId);
			if(!responseObject.isSuccess()) {
				model.setViewName(ViewNameConstants.CREATE_RULE_GROUP);
			}
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
		}
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.RULE_GROUP);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		return model;
		
	}

	//			@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_RULE_GROUP, method = RequestMethod.POST)
	public ModelAndView updateRuleGroup(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value="policy-grouprules") String policyGroupRules,
			@ModelAttribute(value = FormBeanConstants.RULEGROUP_FORM_BEAN) PolicyGroup policyGroup,//NOSONAR
			HttpServletRequest request,
			BindingResult result) {

		ModelAndView model;
		policyRuleGroupValidator.validatePolicyRuleGroupParameters(policyGroup, result, null, null, false);
		
		if(result.hasErrors()) {
			model = new ModelAndView(ViewNameConstants.CREATE_RULE_GROUP);
			model.addObject(FormBeanConstants.RULEGROUP_FORM_BEAN, policyGroup);
		} else {
			model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
			policyGroup.setAlias(policyGroup.getName());
			if(policyGroup.getServer() == null) {
				policyGroup.setServer(new ServerInstance());
			}
			policyGroup.getServer().setId(instanceId);
			ResponseObject responseObject = policyService.updatePolicyGroup(policyGroup, policyGroupRules, eliteUtils.getLoggedInStaffId(request), instanceId);
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
		}
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.RULE_GROUP);

		return model;
	}

	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_RULE_LIST_BY_RULE_GROUP_ID, method = RequestMethod.GET)
	@ResponseBody
	public String getPolicyRuleList(@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "ruleGroupId") int ruleGroupId) {
		
		long count =  policyService.getPolicyRuleCountByRuleGroup(ruleGroupId);
		List<PolicyGroupRuleRel> resultList = new ArrayList<>();
		if(count > 0){
			resultList = policyService.getPolicyRulePaginatedListByRuleGroup(ruleGroupId, eliteUtils.getStartIndex(limit, currentPage, 
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (PolicyGroupRuleRel policyGroupRuleRel : resultList) {
				
				row = new HashMap<>();
				
				row.put(BaseConstants.ID, policyGroupRuleRel.getPolicyRule().getId());
				row.put(BaseConstants.NAME, policyGroupRuleRel.getPolicyRule().getName());
				row.put(BaseConstants.DESCRIPTION, policyGroupRuleRel.getPolicyRule().getDescription());
				row.put(BaseConstants.APPLICATION_ORDER, policyGroupRuleRel.getApplicationOrder());
				row.put("relId", policyGroupRuleRel.getId());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
													(int) count, rowList).getJsonString();
	}
	
	@RequestMapping(value = ControllerConstants.REMOVE_RULE_GROUP, method = RequestMethod.POST)
	@ResponseBody
	public String removePolicyGroup(@RequestParam("ruleGroupId") int ruleGroupId) {
		AjaxResponse ajaxResponse;
		
		ResponseObject responseObject = policyService.removePolicyGroup(ruleGroupId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}


}
