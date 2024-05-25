/**
 * 
 */
package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import com.elitecore.sm.common.model.DiameterActionOnOverloadEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;


@Controller
public class DiameterCollectionServiceController extends BaseController{
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired
	ServiceValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_DIAMETER_COLLECTION_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initDiameterCollectionService(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId
    		) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DIAMETER_COLLECTION_SERVICE_MANAGER);
		int iserviceId=Integer.parseInt(serviceId);
		DiameterCollectionService diameterService = (DiameterCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject("lastUpdateTime",DateFormatter.formatDate(diameterService.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DIAMETER_COLLECTION_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, diameterService.getServInstanceId());
		model.addObject("enableFileStats",diameterService.isEnableFileStats());
		model.addObject("enableDBStats",diameterService.isEnableDBStats());
		model.addObject("syncStatus",diameterService.isSyncStatus());
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_DIAMETER_COLLECTION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtDiameterCollectionConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required = true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required = false) String serviceInstanceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required = true) String serviceType) {

		ModelAndView model = new ModelAndView(ViewNameConstants.DIAMETER_COLLECTION_SERVICE_MANAGER);
		int iserviceId=Integer.parseInt(serviceId);
		DiameterCollectionService diameterService = (DiameterCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DIAMETER_COLLECTION_SERVICE_CONFIGURATION);
		model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN, diameterService);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,diameterService.getName());
		model.addObject(BaseConstants.INSTANCE_ID,diameterService.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_TYPE,diameterService.getSvctype().getAlias());
		model.addObject(BaseConstants.SERVICE_INST_ID, diameterService.getServInstanceId());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(diameterService.getLastUpdatedDate()));
		model.addObject("enableFileStats",diameterService.isEnableFileStats());
		model.addObject("enableDBStats",diameterService.isEnableDBStats());
		model.addObject("syncStatus",diameterService.isSyncStatus());
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("startupMode", java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("actionOnOverload", java.util.Arrays.asList(DiameterActionOnOverloadEnum.values()));
		List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
		List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
		logger.debug("Separator enum" +separatorEnum);
		for(SeparatorEnum separator: separatorEnum) {
			if(separator.getValue() != "ssss") {
				delimeter.add(separator);
			}
		}
		model.addObject("SeparatorEnum",delimeter ); 
		return model;	
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_DIAMETER_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateDiameterServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") DiameterCollectionService collectionService,//NOSONAR
			BindingResult result, HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.DIAMETER_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(collectionService,result,null,false);
		DiameterCollectionService locDiameterCollection=collectionService;
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.DIAMETER_COLLECTION_SERVICE_FORM_BEAN,locDiameterCollection);
		}else{
			locDiameterCollection.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=servicesService.updateDiameterCollectionServiceConfiguration(locDiameterCollection);
			if(responseObject.isSuccess()){
				locDiameterCollection=(DiameterCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locDiameterCollection.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		addCommonParamToModel(model, locDiameterCollection);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DIAMETER_COLLECTION_SERVICE_CONFIGURATION);
		return model;		
	}
	
	/** Add common service param to model object
	 * @param model
	 * @param collectionService
	 */
	private void addCommonParamToModel(ModelAndView model,DiameterCollectionService collectionService){
		
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("startupMode", java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("lastUpdateTime",DateFormatter.formatDate(collectionService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, collectionService.getId());
		model.addObject(BaseConstants.SERVICE_TYPE, collectionService.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, collectionService.getServerInstance().getId());
		model.addObject("enableFileStats",collectionService.isEnableFileStats());
		model.addObject("enableDBStats",collectionService.isEnableDBStats());
		model.addObject("actionOnOverload", java.util.Arrays.asList(DiameterActionOnOverloadEnum.values()));
		List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
		List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
		logger.debug("Separator enum" +separatorEnum);
		for(SeparatorEnum separator: separatorEnum) {
			if(separator.getValue() != "ssss") {
				delimeter.add(separator);
			}
		}
		model.addObject("SeparatorEnum",delimeter ); 
		model.addObject(BaseConstants.SERVICE_INST_ID, collectionService.getServInstanceId());
	}
}
