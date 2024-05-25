package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.SchedulingDateEnum;
import com.elitecore.sm.services.model.SchedulingDayEnum;
import com.elitecore.sm.services.model.SchedulingTypeEnum;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;
/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class CollectionServiceController extends BaseController{
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired
	private ServiceValidator validator;
	
	@Autowired
	private DriversService driversService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setRequiredFields(new String[] {"svcExecParams.minThread","svcExecParams.maxThread","svcExecParams.fileBatchSize","svcExecParams.queueSize"});
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		binder.setValidator(validator);
	}
	
	/**
	 * Redirect to collection service summary page 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_COLLECTION_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initCollectionService(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.COLLECTION_SERVICE_MANAGER);
		
		int iserviceId=Integer.parseInt(serviceId);
		Service service =  servicesService.getServiceandServerinstance(iserviceId);
			 
		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.COLLECTION_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats",service.isEnableFileStats());
		model.addObject("enableDBStats",service.isEnableDBStats());
		model.addObject("syncStatus",service.isSyncStatus());
		
		return model;
	}
	    
	
	/**
	 * when click on service configuration tab 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	    @PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
		@RequestMapping(value = ControllerConstants.INIT_COLLECTION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
		public ModelAndView initCollectionServiceConfiguration(@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
	    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
	    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
	    		@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
	    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId,
	    		HttpServletRequest request) {
			ModelAndView model = new ModelAndView(ViewNameConstants.COLLECTION_SERVICE_MANAGER);
			
			int iserviceId=Integer.parseInt(serviceId);
			CollectionService colService=(CollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.COLLECTION_SERVICE_CONFIGURATION);
			model.addObject(FormBeanConstants.COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,colService);
			model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
			model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
			model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
			model.addObject("schType",java.util.Arrays.asList(SchedulingTypeEnum.values()));
			model.addObject("day",java.util.Arrays.asList(SchedulingDayEnum.values()));
			model.addObject("date",java.util.Arrays.asList(SchedulingDateEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,serviceId);
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.STATE_ENUM,java.util.Arrays.asList(StateEnum.values()));
			model.addObject("lastUpdateTime",DateFormatter.formatDate(colService.getLastUpdatedDate()));
			model.addObject(BaseConstants.SERVICE_TYPE, colService.getSvctype().getAlias());
			
			return model;
		}
	    
	    /**
	     * Fetch Agent list for collection service
	     * @param limit
	     * @param currentPage
	     * @param sidx
	     * @param sord
	     * @param request
	     * @return
	     */
	// @PreAuthorize("hasAnyAuthority('CREATE_SERVICE_INSTANCE')")
		@RequestMapping(value = ControllerConstants.GET_COLLECTION_AGENT_LIST, method = RequestMethod.GET)
		public @ResponseBody String getSystemAgentList(
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx") String sidx,
				@RequestParam(value = "sord") String sord,
				HttpServletRequest request) {
			
					Map<String, Object> row = null;
					List<Map<String, Object>> rowList = new ArrayList<>();
					long count = rowList.size();
					row = new HashMap<>();
					row.put("agentId", "101");
					row.put("typeOfAgent", "Packet Statistic Agent");
					row.put("lastExecutionDate", "10-12-2015 05:06:07");
					row.put("nextExecutionDate", "10-12-2015 05:06:07");
					row.put("syncAgentStatus", "ACTIVE");
					rowList.add(row);
			
					row = new HashMap<>();
					row.put("agentId", "102");
					row.put("typeOfAgent", "File Rename Agent");
					row.put("lastExecutionDate", "10-12-2015 05:06:07");
					row.put("nextExecutionDate", "10-12-2015 05:06:07");
					row.put("syncAgentStatus", "ACTIVE");
					rowList.add(row);
			
					row = new HashMap<>();
					row.put("agentId", "103");
					row.put("typeOfAgent", "File Rename Agent");
					row.put("lastExecutionDate", "10-12-2015 05:06:07");
					row.put("nextExecutionDate", "10-12-2015 05:06:07");
					row.put("syncAgentStatus", "ACTIVE");
					rowList.add(row);

			return new JqGridData<Map<String, Object>>(
					eliteUtils.getTotalPagesCount(count, limit), currentPage,
					(int) count, rowList).getJsonString();
		}
	
		/**
		 * Update Collection service configuration
		 * @param collectionService
		 * @param result
		 * @param status
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
		@RequestMapping(value = ControllerConstants.UPDATE_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateColServiceConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME) String serviceName,
				@ModelAttribute (value=FormBeanConstants.COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN) CollectionService collectionService,//NOSONAR
				BindingResult result,HttpServletRequest request) {
			ModelAndView model = new ModelAndView(ViewNameConstants.COLLECTION_SERVICE_MANAGER);
			validator.validateServiceConfigurationParameter(collectionService, result,null,false);
			
			if(result.hasErrors()){
				model.addObject(BaseConstants.SERVICE_NAME,serviceName);
				model.addObject(FormBeanConstants.COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,collectionService);
			}else{
				collectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				servicesService.iterateServiceConfigDetails(collectionService);
				ResponseObject responseObject=servicesService.updateCollectionServiceConfiguration(collectionService);
				
				if(responseObject.isSuccess()){
					collectionService=(CollectionService)responseObject.getObject();
					model.addObject(BaseConstants.SERVICE_NAME,collectionService.getName());
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
				}
			}
			
			model.addObject(FormBeanConstants.COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,collectionService);
			model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
			model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
			model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
			model.addObject("schType",java.util.Arrays.asList(SchedulingTypeEnum.values()));
			model.addObject("day",java.util.Arrays.asList(SchedulingDayEnum.values()));
			model.addObject("date",java.util.Arrays.asList(SchedulingDateEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,collectionService.getId());
			model.addObject(BaseConstants.INSTANCE_ID,collectionService.getServerInstance().getId());
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.SERVICE_TYPE, collectionService.getSvctype().getAlias());
			model.addObject(BaseConstants.SERVICE_INST_ID, collectionService.getServInstanceId());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.COLLECTION_SERVICE_CONFIGURATION);
				
			return model;		
		}
				
		/**
		 * Method will update Collection Driver status using JMX call to P-ENGINE.
		 * @param DriverId
		 * @return AJAX responseObj
		 */
		/*@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")*/
		@RequestMapping(value = ControllerConstants.GET_COLLECTION_SERVICE_COUNTER_STATUS, method = RequestMethod.POST)
		@ResponseBody public  String getCollectionServiceCounterStatus(
							@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
			
			int iserviceId=Integer.parseInt(serviceId);
			 ResponseObject responseObject = servicesService.getServiceCounterDetails(iserviceId);
			 AjaxResponse	 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			   
			return ajaxResponse.toString();
		}
		
		/**
		 * Method will reset the service counter details using JMX call to P-ENGINE.
		 * @param serviceId
		 * @return Response body
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
		@RequestMapping(value = ControllerConstants.RESET_COLLECTION_SERVICE_COUNTER_STATUS, method = RequestMethod.POST)
		public @ResponseBody String resetCollectionServiceCounterStatus(
							@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
			
			  int iserviceId=Integer.parseInt(serviceId);
			  ResponseObject  responseObject = servicesService.resetServiceCounterDetails(iserviceId);
			  AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			return ajaxResponse.toString();
		}
}
