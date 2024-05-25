package com.elitecore.sm.policy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.PolicyConditionOperatorEnum;
import com.elitecore.sm.common.model.PolicyRuleCategoryEnum;
import com.elitecore.sm.common.model.PolicyRuleSeverityEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.consolidationservice.model.LogicalConditionOperatorEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEngineEnum;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class BusinessPolicyController extends BaseController{

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
	
//	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.INIT_BUSINESS_POLICY_MGMT, method = RequestMethod.POST)
	public ModelAndView initBusinessPolicyMgmt(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) String instanceId,
    		@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
    		@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE,defaultValue="POLICY_RULE_CONDITION", required=false) String requestActionType
    		) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_CATEGORY,java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject(BaseConstants.PROCESSING_POLICY_RULE_SEVERITY,java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		
		List<DatabaseQuery> databaseQueries = databaseQueryService.getAllQueriesByServerId(Integer.parseInt(instanceId));
		List<String> databaseQueryName = new ArrayList<>(); 
		for(DatabaseQuery databaseQuery : databaseQueries){
			databaseQueryName.add(databaseQuery.getQueryName());
		}
		model.addObject("databaseQueryList",databaseQueryName);
		if(requestActionType.equalsIgnoreCase(BaseConstants.POLICY_RULE_CONDITION)){
			model.addObject(FormBeanConstants.POLICY_RULE_CONDITION_FORM_BEAN, (PolicyCondition) SpringApplicationContext
					.getBean(PolicyCondition.class));
			model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEngineEnum.values()));
			model.addObject("logicalOperatorEnum", PolicyConditionOperatorEnum.getPolicyConditionList());
		}
		else if(requestActionType.equalsIgnoreCase(BaseConstants.POLICY_RULE_ACTION)){
			model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEngineEnum.values()));
			model.addObject(FormBeanConstants.POLICY_RULE_ACTION_FORM_BEAN, (PolicyAction) SpringApplicationContext
					.getBean(PolicyAction.class));
		}else if(requestActionType.equalsIgnoreCase(BaseConstants.DATABASE_QUERIES)){
			model.addObject(FormBeanConstants.DATABASE_QUERY_FORM_BEAN, (DatabaseQuery) SpringApplicationContext
					.getBean(DatabaseQuery.class));
			model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
			model.addObject("logicalOperatorEnum", java.util.Arrays.asList(LogicalConditionOperatorEnum.values()));
			model.addObject("policyOperatorEnum", java.util.Arrays.asList(PolicyConditionOperatorEnum.values()));
			model.addObject("unifiedFieldEnum", java.util.Arrays.asList(UnifiedFieldEngineEnum.values()));
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
		
		return model;
	}
	
}
