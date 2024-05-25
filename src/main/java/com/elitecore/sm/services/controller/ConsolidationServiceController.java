package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.AcrossFileProcessingTypeEnum;
import com.elitecore.sm.consolidationservice.model.ConsolidationDataTypeEnum;
import com.elitecore.sm.consolidationservice.model.ConsolidationTypeEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.LogicalConditionOperatorEnum;
import com.elitecore.sm.consolidationservice.model.OperationTypeEnum;
import com.elitecore.sm.consolidationservice.model.ProcessingTypeEnum;
import com.elitecore.sm.consolidationservice.model.SegregationTypeEnum;
import com.elitecore.sm.consolidationservice.service.IConsolidationDefinitionService;
import com.elitecore.sm.parser.model.UnifiedDateFieldEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ConsolidationServiceController extends BaseController {

	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;

	@Autowired
	private ServiceValidator validator;

	
	@Autowired
	PathListService pathlistService;

	@Autowired
	IConsolidationDefinitionService consolidationDefinitionService;

	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setRequiredFields(new String[] { "svcExecParams.minThread", "svcExecParams.maxThread",
				"svcExecParams.fileBatchSize", "svcExecParams.queueSize" });
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	/**
	 * Redirect to collection service summary page
	 * 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_CONSOLIDATION_MANAGER, method = RequestMethod.GET)
	public ModelAndView initConsolidationManager(
			@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required = true) String serviceType,
			@RequestParam(value = BaseConstants.SERVICE_NAME, required = true) String serviceName,
			@RequestParam(value = BaseConstants.INSTANCE_ID, required = true) String instanceId) {

		ModelAndView model = new ModelAndView(ViewNameConstants.CONSOLIDATION_CONFIG_MANAGER);

		int iserviceId = Integer.parseInt(serviceId);
		Service service = servicesService.getServiceandServerinstance(iserviceId);

		model.addObject("lastUpdateTime", DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CONSOLIDATION_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("syncStatus", service.isSyncStatus());
		ResponseObject responseObject = consolidationDefinitionService.getConsolidationDefintionList(iserviceId);
		if(responseObject.isSuccess()){
			model.addObject("conslist",responseObject.getObject());
		}
		return model;
	}

	/**
	 * when click on service configuration tab
	 * 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_CONSOLIDATION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initConsolidationServiceConfiguration(
			@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required = true) String serviceType,
			@RequestParam(value = BaseConstants.SERVICE_NAME, required = true) String serviceName,
			@RequestParam(value = BaseConstants.INSTANCE_ID, required = true) String instanceId,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.CONSOLIDATION_CONFIG_MANAGER);

		int iserviceId = Integer.parseInt(serviceId);
		DataConsolidationService consservice = (DataConsolidationService) servicesService
				.getAllServiceDepedantsByServiceId(iserviceId);

		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CONSOLIDATION_SERVICE_CONFIGURATION);
		model.addObject(FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN, consservice);

		model.addObject("startupMode", Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType", Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria", Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject("consolidationType", Arrays.asList(ConsolidationTypeEnum.values()));
		model.addObject("processingType", Arrays.asList(ProcessingTypeEnum.values()));
		model.addObject("acrossFileProcessingType", Arrays.asList(AcrossFileProcessingTypeEnum.values()));
		model.addObject("enableFileStats",consservice.isEnableFileStats());
		model.addObject("enableDBStats",consservice.isEnableDBStats());
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_INST_ID, consservice.getServInstanceId());
		model.addObject(BaseConstants.GROUP_TYPE_ENUM, FileGroupEnum.getAllValues());
		model.addObject("trueFalseEnum", Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.STATE_ENUM, Arrays.asList(StateEnum.values()));
		model.addObject("lastUpdateTime", DateFormatter.formatDate(consservice.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_TYPE, consservice.getSvctype().getAlias());

		return model;
	}

	/**
	 * when click on service configuration tab
	 * 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_CONSOLIDATION_DEFINITION, method = RequestMethod.POST)
	public ModelAndView initConsolidationDefination(
			@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required = true) String serviceType,
			@RequestParam(value = BaseConstants.SERVICE_NAME, required = true) String serviceName,
			@RequestParam(value = BaseConstants.INSTANCE_ID, required = true) String instanceId,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.CONSOLIDATION_CONFIG_MANAGER);
		int iServiceId = Integer.parseInt(serviceId);
		DataConsolidationService service = (DataConsolidationService) servicesService.getServiceandServerinstance(iServiceId);
		ResponseObject responseObject = consolidationDefinitionService.getConsolidationDefintionListByServiceId(iServiceId);
		if(responseObject.isSuccess()){
			model.addObject("consolidationDefanitionList",responseObject.getObject());
		}
				
		model.addObject("unifiedFieldEnum", Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("unifiedDateFieldEnum", Arrays.asList(UnifiedDateFieldEnum.values()));
		model.addObject("segragationType", Arrays.asList(SegregationTypeEnum.values()));
		model.addObject("dataType", Arrays.asList(ConsolidationDataTypeEnum.values()));
		model.addObject("operationType", Arrays.asList(OperationTypeEnum.values()));

		model.addObject("lastUpdateTime", DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CONSOLIDATION_DEFINITION);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("syncStatus", service.isSyncStatus());
		model.addObject("trueFalseEnum", TrueFalseEnum.values());

		return model;
	}

	/**
	 * when click on service configuration tab
	 * 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_CONSOLIDATION_PATHLIST, method = RequestMethod.POST)
	public ModelAndView initConsolidationPathlist(
			@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required = true) String serviceType,
			@RequestParam(value = BaseConstants.SERVICE_NAME, required = true) String serviceName,
			@RequestParam(value = BaseConstants.INSTANCE_ID, required = true) String instanceId,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.CONSOLIDATION_CONFIG_MANAGER);

		int iserviceId = Integer.parseInt(serviceId);
		Service service = servicesService.getServiceandServerinstance(iserviceId);
		ResponseObject responseObject = pathlistService.getConsolidationPathListByServiceId(iserviceId);
		if(responseObject.isSuccess()){
			model.addObject("pathList",responseObject.getObject());
		}
		model.addObject("lastUpdateTime", DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CONSOLIDATION_SOURCE_PATH_MAPPING);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("syncStatus", service.isSyncStatus());
		model.addObject("trueFalseEnum", TrueFalseEnum.values());
		model.addObject("unifiedFieldEnum", UnifiedFieldEnum.values());
		model.addObject("sortingTypeEnum", SortingTypeEnum.values());
		model.addObject("sortingRecordFieldType", ConsolidationDataTypeEnum.values());
		model.addObject("logicalOperator", LogicalConditionOperatorEnum.values());
		List<DataConsolidation> definitionList = consolidationDefinitionService.getConsolidationListByServiceId(iserviceId);
		List<String> consolidationNameList = new ArrayList<>();
		if(definitionList != null && !definitionList.isEmpty()){
			for(DataConsolidation cons : definitionList){
				consolidationNameList.add(cons.getConsName());
			}
		}
		model.addObject("definitionList", consolidationNameList);
		return model;
	}

	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView updateConsolidationServiceConfiguration(
			@RequestParam(value = BaseConstants.SERVICE_NAME, required = true) String serviceName,
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN) DataConsolidationService service,//NOSONAR
			BindingResult result, HttpServletRequest request) {

		ModelAndView model = new ModelAndView(ViewNameConstants.CONSOLIDATION_CONFIG_MANAGER);

		validator.validateServiceConfigurationParameter(service, result, null, false);

		if (result.hasErrors()) {
			logger.debug(" Validation error occurs");
			model.addObject(FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		} else {
			service.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			model.addObject(BaseConstants.SERVICE_TYPE, BaseConstants.DATA_CONSOLIDATION_SERVICE);
			model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
			ResponseObject responseObject = servicesService.updateConsolidationServiceConfiguration(service);
			if (responseObject != null && responseObject.isSuccess()) {
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("Service.configuration.update.success"));
			} else {
				model.addObject(BaseConstants.ERROR_MSG, getMessage("Service.configuration.update.fail"));
			}
			model.addObject(FormBeanConstants.DATA_CONSOLIDATION_SERVICE_CONFIGURATION_FORM_BEAN, service);
		}

		addCommonConfigurationParameters(model);
		addCommonParametersToModal(model, service);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.CONSOLIDATION_SERVICE_CONFIGURATION);

		return model;
	}

	private void addCommonParametersToModal(ModelAndView model, DataConsolidationService service) {
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("lastUpdateTime", DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject("unifiedFieldEnum", java.util.Arrays.asList(UnifiedFieldEnum.values()));
	}

	private void addCommonConfigurationParameters(ModelAndView model) {
		model.addObject("startupMode", java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType", java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria", java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject("trueFalseEnum", Arrays.asList(TrueFalseEnum.values()));
		model.addObject("consolidationType", Arrays.asList(ConsolidationTypeEnum.values()));
		model.addObject("processingType", Arrays.asList(ProcessingTypeEnum.values()));
		model.addObject("acrossFileProcessingType", Arrays.asList(AcrossFileProcessingTypeEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM, FileGroupEnum.getAllValues());

	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.GET_CONSOLIDATION_DEFINITION, method = RequestMethod.GET)
	@ResponseBody
	public String getConsolidationDefinitionList(@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		int iServiceId = Integer.parseInt(serviceId);
		ResponseObject responseObject = consolidationDefinitionService.getConsolidationDefintionList(iServiceId);
		if(responseObject.isSuccess()){
			return responseObject.getObject().toString();
		}
		return ajaxResponse.toString();	
	}
}
