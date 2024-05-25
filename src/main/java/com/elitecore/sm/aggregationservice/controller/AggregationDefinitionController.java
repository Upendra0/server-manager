package com.elitecore.sm.aggregationservice.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.aggregationservice.model.AggregationConditionActionEnum;
import com.elitecore.sm.aggregationservice.model.AggregationConditionEnum;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationOperationExpressionEnum;
import com.elitecore.sm.aggregationservice.model.OutputFieldDataTypeEnum;
import com.elitecore.sm.aggregationservice.service.IAggregationDefinitionService;
import com.elitecore.sm.aggregationservice.validator.AggregationDefinitionValidator;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.ConsolidationTypeEnum;
import com.elitecore.sm.parser.model.UnifiedDateFieldEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.FieldForOutputFileEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class AggregationDefinitionController extends BaseController {
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	private AggregationDefinitionValidator aggDefValidator;
	
	@Autowired
	IAggregationDefinitionService aggregatioDefinitionService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				DateFormatter.getShortDataFormat(), true));
	}
		
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_AGGREGATION_DEFINITION_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initAggregationDefinitionConfiguration(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId
			) {
		ModelAndView model = new ModelAndView();
		int iserviceId=Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		loadAllDefinitionName(model,service);
		ResponseObject responseObject = aggregatioDefinitionService.getAggregationDefinitionByServiceId(iserviceId);
		if(responseObject.isSuccess()){
			AggregationDefinition aggDefinition = (AggregationDefinition) responseObject.getObject();
			loadDefinitionData(model, aggDefinition);
		}
		
		model.setViewName(ViewNameConstants.AGGREGATION_SERVICE_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AGGREGATION_DEFINITION_CONFIGURATION);
		addCommonDefinitionParametersToModal(model,service);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.CREATE_AGGREGATION_DEFINITION_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String createAggregationDefinitionList(
			@ModelAttribute(value = FormBeanConstants.AGGREGATION_SERVICE_DEFINITION_FORM_BEAN) AggregationDefinition aggDefinition,//NOSONAR
			BindingResult result,
			@RequestParam(value="aggregation-def-conditions", required=false) String defConditions,
			@RequestParam(value="aggregation-def-aggattribute", required=false) String defAggAttributes,
			@RequestParam(value="aggregation-def-keyattribute", required=false) String defKeyAttributes,
			HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		aggDefValidator.validateAggregationDefinitionParams(aggDefinition, result,null,false);
		
		if(result.hasErrors()){
			logger.debug("Aggregation Service Definition Validation error occurs");
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			logger.info("Validation done successfully for aggregation definition parameters.");
			ResponseObject responseObject = aggregatioDefinitionService.saveAggregationDefintion(aggDefinition, defConditions, defAggAttributes,defKeyAttributes , eliteUtils.getLoggedInStaffId(request));
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_AGGREGATION_DEFINITION_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String updateAggregationDefinitionList(
			@ModelAttribute(value = FormBeanConstants.AGGREGATION_SERVICE_DEFINITION_FORM_BEAN) AggregationDefinition aggDefinition,//NOSONAR
			BindingResult result,
			@RequestParam(value="aggregation-def-conditions", required=false) String defConditions,
			@RequestParam(value="aggregation-def-aggattribute", required=false) String defAggAttributes,
			@RequestParam(value="aggregation-def-keyattribute", required=false) String defKeyAttributes,
			HttpServletRequest request) {

		AjaxResponse ajaxResponse=new AjaxResponse();
		aggDefValidator.validateAggregationDefinitionParams(aggDefinition, result,null,false);
		if(result.hasErrors()){
			logger.debug("Aggregation Service Definition Validation error occurs");
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			logger.info("Validation done successfully for aggregation definition parameters.");
			ResponseObject responseObject = aggregatioDefinitionService.updateAggregationDefintion(aggDefinition, defConditions, defAggAttributes,defKeyAttributes , eliteUtils.getLoggedInStaffId(request));
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.LOAD_AGGREGATION_DEFINITION_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String loadAggregationDefinitionData(@RequestParam(value = "aggDefName", required=true) String aggDefName) {
		ResponseObject responseObject = aggregatioDefinitionService.getAggregationDefintionData(aggDefName);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void loadAllDefinitionName(ModelAndView model, AggregationService service){
		//Logic to get all the definition name start
		ResponseObject responseObject = aggregatioDefinitionService.getAllAggregationDefintionName();
		List<AggregationDefinition> aggDefinitionNameList = null;
		if(responseObject.isSuccess()) {
			aggDefinitionNameList =  (List<AggregationDefinition>) responseObject.getObject();	
		}
		model.addObject("aggDefinitionNameList",aggDefinitionNameList);
		//Logic to get all the definition list end
	}
	
	private void loadDefinitionData(ModelAndView model,AggregationDefinition aggDefinition){
		JSONArray jsonConditionArray =  new JSONArray();
		JSONArray jsonAggKeyArray =  new JSONArray();
		JSONArray jsonAggAttributeArray =  new JSONArray();
		if(aggDefinition == null) {
			aggDefinition = new AggregationDefinition();
		}else{	
			jsonConditionArray = aggregatioDefinitionService.getJSONFromAggConditionList(aggDefinition.getAggConditionList());
			jsonAggKeyArray = aggregatioDefinitionService.getJSONFromKeyAttrList(aggDefinition.getAggKeyAttrList());		
			jsonAggAttributeArray = aggregatioDefinitionService.getJSONFromAggAttrList(aggDefinition.getAggAttrList());	
		}		
		model.addObject(FormBeanConstants.AGGREGATION_SERVICE_DEFINITION_FORM_BEAN, aggDefinition);
		model.addObject("defConditionList", jsonConditionArray);
		model.addObject("defKeyAttributeList", jsonAggKeyArray);
		model.addObject("defAggAttributeList", jsonAggAttributeArray);
	}
	
	private void addCommonDefinitionParametersToModal(ModelAndView model, AggregationService service){
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject("UnifiedDateFieldEnum",java.util.Arrays.asList(UnifiedDateFieldEnum.values()).subList(1, UnifiedDateFieldEnum.values().length));
		model.addObject("FieldForOutputFileEnum",java.util.Arrays.asList(FieldForOutputFileEnum.values()));
		model.addObject("aggType", Arrays.asList(ConsolidationTypeEnum.values()));
		model.addObject("aggConditionExpression", Arrays.asList(AggregationConditionEnum.values()));
		model.addObject("aggConditionAction", Arrays.asList(AggregationConditionActionEnum.values()));		
		model.addObject("aggOutputFieldDataTypeEnum", Arrays.asList(OutputFieldDataTypeEnum.values()));	
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));		
		model.addObject("aggOprExEnum",java.util.Arrays.asList(AggregationOperationExpressionEnum.values()));		
	}
}