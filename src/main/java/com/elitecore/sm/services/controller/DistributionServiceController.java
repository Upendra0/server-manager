package com.elitecore.sm.services.controller;

import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.FileGroupDateTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FileMergeGroupingByEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.DistributionService;
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

@Controller
public class DistributionServiceController extends BaseController {

	
	@Autowired
	private ServiceValidator validator;
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
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
	 * Redirect to Distribution service summary page 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initDistributionServiceManager(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) int instanceId) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_SERVICE_MANAGER);
		
		Service service =  servicesService.getServiceandServerinstance(serviceId);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DISTRIBUTION_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject("enableFileStats",service.isEnableFileStats());
		model.addObject("enableDBStats",service.isEnableDBStats());
		model.addObject("syncStatus",service.isSyncStatus());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject(BaseConstants.LAST_UPDATE_TIME,DateFormatter.formatDate(service.getLastUpdatedDate()));
		return model;
	}
	
	/**
	 * Method will display distribution service configuration page.
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView initDistributionServiceConfiguration(@RequestParam(value = BaseConstants.SERVICE_ID, required=false) int serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=false) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=false) String serviceName,
    		@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId) {
		
		logger.debug(">> initDistributionServiceConfiguration in DistributionServiceController.");
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_SERVICE_MANAGER);
		
		
		DistributionService distributionService = (DistributionService) servicesService.getAllServiceDepedantsByServiceId(serviceId);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DISTRIBUTION_SERVICE_CONFIGURATION);
		model.addObject(FormBeanConstants.DISTRIBUTION_SERVICE_CONFIGURATION_FORM_BEAN,distributionService);
		
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject("schType",java.util.Arrays.asList(SchedulingTypeEnum.values()));
		model.addObject("day",java.util.Arrays.asList(SchedulingDayEnum.values()));
		model.addObject("date",java.util.Arrays.asList(SchedulingDateEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID,distributionService.getId());
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getAllValues());
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject(BaseConstants.STATE_ENUM,java.util.Arrays.asList(StateEnum.values()));
		model.addObject(BaseConstants.FILE_MERGE_GROUPINGBY_ENUM,java.util.Arrays.asList(FileMergeGroupingByEnum.values()));
		model.addObject(BaseConstants.LAST_UPDATE_TIME,DateFormatter.formatDate(distributionService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		
		
		return model;
	}
	
	
	
	/**
	 * Method will update distribution service configuration parameters.
	 * @param serviceName
	 * @param distributionService
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_DISTRIBUTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateDistributionServiceConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME) String serviceName,
			@ModelAttribute (value=FormBeanConstants.DISTRIBUTION_SERVICE_CONFIGURATION_FORM_BEAN) DistributionService distributionService,//NOSONAR
			BindingResult result,HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_SERVICE_MANAGER);
		
		
		validator.validateServiceConfigurationParameter(distributionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.DISTRIBUTION_SERVICE_CONFIGURATION_FORM_BEAN,distributionService);
		}else{
			distributionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateDistributionServiceConfiguration(distributionService);
			
			if(responseObject.isSuccess()){
				DistributionService distributionServiceObj = (DistributionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,distributionServiceObj.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		model.addObject(FormBeanConstants.DISTRIBUTION_SERVICE_CONFIGURATION_FORM_BEAN,distributionService);
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject("schType",java.util.Arrays.asList(SchedulingTypeEnum.values()));
		model.addObject("day",java.util.Arrays.asList(SchedulingDayEnum.values()));
		model.addObject("date",java.util.Arrays.asList(SchedulingDateEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID,distributionService.getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, distributionService.getServInstanceId());
		model.addObject(BaseConstants.INSTANCE_ID,distributionService.getServerInstance().getId());
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.SERVICE_TYPE, distributionService.getSvctype().getAlias());
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getAllValues());
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject(BaseConstants.FILE_MERGE_GROUPINGBY_ENUM,java.util.Arrays.asList(FileMergeGroupingByEnum.values()));
		model.addObject(BaseConstants.LAST_UPDATE_TIME,DateFormatter.formatDate(distributionService.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DISTRIBUTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	
	@PreAuthorize("hasAnyAuthority('SERVICE_MANAGEMENT')")
	@RequestMapping(value = ControllerConstants.INIT_DITRIBUTION_PLUGIN_MANAGER,method= RequestMethod.GET)
	public ModelAndView initDistributionPluginManager() {
		ModelAndView model = new ModelAndView(ViewNameConstants.INIT_DITRIBUTION_PLUGIN_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_PLUGIN_CONFIGURATION);
		
		return model;
	}
	
	
}
