package com.elitecore.sm.aggregationservice.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationConditionEnum;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationOperationExpressionEnum;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FilterGroupTypeEnum;
import com.elitecore.sm.common.model.TimeUnitEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.DuplicateRecordPolicyTypeEnum;
import com.elitecore.sm.services.model.SchedulingDateEnum;
import com.elitecore.sm.services.model.SchedulingDayEnum;
import com.elitecore.sm.services.model.SchedulingTypeEnum;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class AggregationServiceController extends BaseController {
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ServiceValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				DateFormatter.getShortDataFormat(), true));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_AGGREGATION_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initAggregationService(@RequestParam(value = BaseConstants.SERVICE_ID, required=false) String serviceId){
		ModelAndView model = new ModelAndView(ViewNameConstants.AGGREGATION_SERVICE_MANAGER);
		int iserviceId=Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		AggregationDefinition aggDefinition = service.getAggregationDefinition();
		addCommonDefinitionParameters(model,aggDefinition);		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AGGREGATION_SERVICE_SUMMARY);
		model.addObject(FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		addCommonParametersToModal(model, service);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_AGGREGATION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initAggregationServiceConfig(@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId){
		ModelAndView model = new ModelAndView(ViewNameConstants.AGGREGATION_SERVICE_MANAGER);
		int iserviceId=Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AGGREGATION_SERVICE_CONFIGURATION);
		model.addObject(FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		addCommonConfigurationParameters(model);
		addCommonParametersToModal(model, service);
		return model;
	}	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_AGGREGATION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateAggregationServiceConfiguration(
			@ModelAttribute (value=FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN) AggregationService service,//NOSONAR
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			BindingResult result,HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.AGGREGATION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(service, result,null,false);
		
		if(result.hasErrors()){
			logger.debug("Aggregation Service Configuration Validation error occurs");
			model.addObject(FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		}else{
			logger.debug("Aggregation Service Configuration Validation done successfully");
			model.addObject(BaseConstants.SERVICE_TYPE, BaseConstants.AGGREGATION_SERVICE);
			model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
			ResponseObject responseObject = servicesService.updateAggregationServiceConfiguration(service,serviceId,eliteUtils.getLoggedInStaffId(request));
			if(responseObject != null && responseObject.isSuccess()){
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
			model.addObject(FormBeanConstants.AGGREGATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		}
		addCommonConfigurationParameters(model);
		addCommonParametersToModal(model, service);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AGGREGATION_SERVICE_CONFIGURATION);
		return model;
	}	

	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_AGGREGATION_SERVICE_COUNTER_STATUS, method = RequestMethod.POST)
	@ResponseBody public  String getAggregationServiceCounterStatus(@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
		int iserviceId=Integer.parseInt(serviceId);
		ResponseObject responseObject = servicesService.getServiceCounterDetails(iserviceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
		
	private void addCommonParametersToModal(ModelAndView model, AggregationService service){
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("syncStatus",service.isSyncStatus());
	}
	
	private void addCommonConfigurationParameters(ModelAndView model){
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject("schType",java.util.Arrays.asList(SchedulingTypeEnum.values()));
		model.addObject("day",java.util.Arrays.asList(SchedulingDayEnum.values()));
		model.addObject("date",java.util.Arrays.asList(SchedulingDateEnum.values()));
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getAllValues());
		model.addObject("filterGroupType",java.util.Arrays.asList(FilterGroupTypeEnum.values()));
		model.addObject("duplicateRecordPolicyType",java.util.Arrays.asList(DuplicateRecordPolicyTypeEnum.values()));
		model.addObject("acrossFileDuplicateDateIntervalType",java.util.Arrays.asList(TimeUnitEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("affOpExEnum",java.util.Arrays.asList(AggregationOperationExpressionEnum.values()));
	}
	
	private void addCommonDefinitionParameters(ModelAndView model,AggregationDefinition aggDefinition){
		JSONArray jsonConditionArray =  new JSONArray();
		JSONArray jsonAggKeyArray =  new JSONArray();
		JSONArray jsonAggAttributeArray =  new JSONArray();
		if(aggDefinition != null) {		
			List<AggregationCondition> aggConditionList = aggDefinition.getAggConditionList();
			if(!aggConditionList.isEmpty()) {
				for(AggregationCondition aggCondition : aggConditionList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("conditionExpressionValue", AggregationConditionEnum.fromValue(aggCondition.getCondExpression()));
					jsonObj.put("conditionAction", aggCondition.getCondAction());
					jsonConditionArray.put(jsonObj);
				}
			}			
			
			List<AggregationKeyAttribute> aggKeyAttributeList = aggDefinition.getAggKeyAttrList();
			if(!aggKeyAttributeList.isEmpty()) {
				for(AggregationKeyAttribute aggKeyAttribute : aggKeyAttributeList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("aggregationFieldName", aggKeyAttribute.getFieldName());
					jsonAggKeyArray.put(jsonObj);
				}
			}
						
			List<AggregationAttribute> aggAttrList = aggDefinition.getAggAttrList();
			if(!aggAttrList.isEmpty()) {
				for(AggregationAttribute aggAttribute : aggAttrList){
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("aggoutputfielddatatypefield", aggAttribute.getOutputDataType());
					jsonObj.put("operationexpression", AggregationOperationExpressionEnum.fromValue(aggAttribute.getOutputExpression()));
					jsonObj.put("outputfieldname", aggAttribute.getOutputFieldName());
					jsonAggAttributeArray.put(jsonObj);
				}
			}
		}
		model.addObject("defConditionList", jsonConditionArray);
		model.addObject("defKeyAttributeList", jsonAggKeyArray);
		model.addObject("defAggAttributeList", jsonAggAttributeArray);
	}
	
	
}
