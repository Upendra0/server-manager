package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.FileGroupDateTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FilterGroupTypeEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.services.model.CDRDateSummaryTypeEnum;
import com.elitecore.sm.services.model.CDRFileDateTypeEnum;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.SchedulingDateEnum;
import com.elitecore.sm.services.model.SchedulingDayEnum;
import com.elitecore.sm.services.model.SchedulingTypeEnum;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ProcessingServiceController extends BaseController{
	
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	SnmpService snmpService;
	
	@Autowired
	ServiceValidator validator;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	PathListService pathlistService;
	
	
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
	@RequestMapping(value = ControllerConstants.INIT_PROCESSING_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initProcessingService(@RequestParam(value = BaseConstants.SERVICE_ID, required=false) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=false) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=false) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=false) String serverInstanceId,
    		HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.PROCESSING_SERVICE_MANAGER);
		int iserviceId=Integer.parseInt(serviceId);
		ProcessingService service = (ProcessingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		
		model.setViewName(ViewNameConstants.PROCESSING_SERVICE_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PROCESSING_SERVICE_SUMMARY);
		model.addObject(FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN, service);
		model.addObject(BaseConstants.INSTANCE_ID,serverInstanceId);
		addCommonParametersToModal(model, service);
		
		return model;
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_PROCESSING_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtProcessingConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType
			) {

		ModelAndView model = new ModelAndView();
		int iserviceId=Integer.parseInt(serviceId);
		ProcessingService service = (ProcessingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.setViewName(ViewNameConstants.PROCESSING_SERVICE_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PROCESSING_SERVICE_CONFIGURATION);
		model.addObject(FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN, service);
		addCommonConfigurationParameters(model);
		addCommonParametersToModal(model, service);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_PROCESSING_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateProcessingServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value=FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN) ProcessingService service,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.PROCESSING_SERVICE_MANAGER);
		
		validator.validateServiceConfigurationParameter(service, result,null,false);
		
		if(result.hasErrors()){
			logger.debug(" Validation error occurs");
			model.addObject(FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN, service);
		}else{
			service.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			model.addObject(BaseConstants.SERVICE_TYPE, BaseConstants.PROCESSING_SERVICE);
			model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
			ResponseObject responseObject = servicesService.updateProcessingServiceConfiguration(service);
			if(responseObject != null && responseObject.isSuccess()){
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
			model.addObject(FormBeanConstants.PROCESSING_SERVICE_CONFIGURATION_FORM_BEAN, service);
		}
		
		addCommonConfigurationParameters(model);
		addCommonParametersToModal(model, service);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PROCESSING_SERVICE_CONFIGURATION);
		
		
		return model;
	}
	
	private void addGenericAlertList(ModelAndView model){
		List<SNMPAlert> genericAlertList = snmpService.getAlertsByCategory(SNMPAlertTypeEnum.GENERIC);
		if(genericAlertList != null && !genericAlertList.isEmpty()){
			 SNMPAlert dummyAlert = new SNMPAlert();
			 dummyAlert.setName("N/A");
			 genericAlertList.add(0, dummyAlert);
		}
		model.addObject("alertList", genericAlertList);
	}
	
	private void addCommonParametersToModal(ModelAndView model, ProcessingService service){
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
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
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject("filterGroupType",java.util.Arrays.asList(FilterGroupTypeEnum.values()));
		model.addObject("overrideFileDateTypeEnum",java.util.Arrays.asList(CDRFileDateTypeEnum.values()));
		model.addObject("cdrDataSummaryTypeEnum",java.util.Arrays.asList(CDRDateSummaryTypeEnum.values()));
	}
	
	/**
	 * 
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_PROCESSING_POLICY_PATH_LIST, method = RequestMethod.GET)
	@ResponseBody public String getProcessingPolicyPathList(@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord, @RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId, HttpServletRequest request) {

		int iserviceId = Integer.parseInt(serviceId);
		ProcessingService service = (ProcessingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		
		final String policyId = "policyId";
		final String policyName = "policyName";
		
		ResponseObject responseObject = pathlistService.getProcessingPathListByServiceId(iserviceId);
		if(responseObject.isSuccess() && responseObject.getObject() != null){
			JSONArray jPathList = (JSONArray)responseObject.getObject();
			if(jPathList != null && jPathList.length() > 0){
				for(int i = 0; i < jPathList.length(); i++){
					row = new HashMap<>();
					JSONObject pathObject = (JSONObject)jPathList.get(i);
					if(pathObject.has(policyId)){
							row.put(policyId, pathObject.getInt(policyId));
							row.put(policyName, pathObject.getString(policyName));
					}
					row.put("readFilePath", pathObject.has("readPath") ? pathObject.getString("readPath") : "");
					row.put("writeFilePath", pathObject.has("writePath") ? pathObject.getString("writePath") : "");
					rowList.add(row);
				}
			}
		}
		long count = rowList.size();
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
	}

	/**
	 * 
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_PROCESSING_POLICY_LIST, method = RequestMethod.GET)
	@ResponseBody public String getProcessingPolicyList(@RequestParam(value = "rows", defaultValue = "15") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord, @RequestParam(value = BaseConstants.SERVER_INSTANCE_ID, required=true) String serverInstanceid, @RequestParam(value = "actionType", required=false) String actionType, HttpServletRequest request) {

		int serverInstanceId = Integer.parseInt(serverInstanceid);
		SearchPolicy searchPolicy = new SearchPolicy(null, null, null, null, serverInstanceId,null,null,null);
		searchPolicy.setServerInstanceId(serverInstanceId);
		
		List<Policy> policyList = policyService.getPaginatedList(searchPolicy, currentPage > 1 ? currentPage * limit + 1 : 0, limit, sidx, sord);
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		final String policyId = "policyId";
		final String policyName = "policyName";
		final String description = "description";
		
		if(policyList != null && !policyList.isEmpty()){
			for(Policy policy:policyList){
				row = new HashMap<>();
				row.put(policyId, policy.getId());
				row.put(policyName, policy.getName());
				row.put(description, policy.getDescription());
				rowList.add(row);
			}
		}
		// Below code to add a policy that is none
		row = new HashMap<>();
		row.put("policyId", 0);
		row.put(policyName, BaseConstants.UNDEFINED_POLICY);
		row.put(description, "Select this if you want path list without policy ");
		rowList.add(row);
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(1, limit+1), currentPage,
				rowList.size(), rowList).getJsonString();
	}

}
