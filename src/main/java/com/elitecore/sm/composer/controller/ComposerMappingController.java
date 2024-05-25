package com.elitecore.sm.composer.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller	
public class ComposerMappingController extends BaseController{
	
	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	/**
	 * Method will fetch all mapping list based on selected device and composer type.
	 * @param deviceId
	 * @param composerType
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_COMPOSER_MAPPING_LIST_BY_DEVICE, method = RequestMethod.POST)
	@ResponseBody public String getComposerMappingListByDevice(
				@RequestParam(value = "deviceId",required = true) int deviceId,
				@RequestParam(value = "composerType",required = true) String composerType) {
		logger.debug(">> getComposerMappingListByDevice in ComposerMappingController " + deviceId  + " and " + composerType); 
		ResponseObject responseObject  = this.composerMappingService.getMappingByDeviceAndComposerType(deviceId, composerType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will create or update mapping 
	 * @param mappingId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.CREATE_UPDATE_COMPOSER_MAPPING_DETAIL, method = RequestMethod.POST)
	@ResponseBody public  String createOrUpdateComposerMappingDetails(
			   @ModelAttribute (value=FormBeanConstants.COMPOSER_MAPPING_FORM_BEAN) ComposerMapping composerMapping,//NOSONAR 
			   @RequestParam(value="actionType",required=true) String actionType,
			   @RequestParam(value="mappingId",required=true) int mappingId,
			   @RequestParam(value = "pluginType",required = true) String pluginType,
			   BindingResult result,
			   HttpServletRequest request) throws CloneNotSupportedException{
		
		logger.debug(">> createOrUpdateComposerMappingDetails in ComposerMappingController " + mappingId  + " " + actionType + " " + pluginType); 
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		composerMappingValidator.validateComposerMappingName(composerMapping, result, null, false);
		
		 if(result.hasErrors()){
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		 }else{
			 int staffId = eliteUtils.getLoggedInStaffId(request);
			 ResponseObject responseObject = this.composerMappingService.createOrUpdateComposerMappingDetails(composerMapping, mappingId, staffId, actionType, pluginType);
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		 }
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will get mapping details for selected mapping.
	 * @param mappingId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_COMPOSER_MAPPING_DETAILS_ID, method = RequestMethod.POST)
	@ResponseBody public  String getComposerMappingDetailsById(
				@RequestParam(value = "mappingId",required = true) int mappingId,
				@RequestParam(value = "pluginType",required = true) String pluginType) {
		logger.debug(">> getComposerMappingDetailsById in ComposerMappingController " + mappingId ); 
		ResponseObject responseObject  = this.composerMappingService.getComposerMappingDetailsById(mappingId, pluginType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceTypeId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_COMPOSER_MAPPING_ASSOCIATION_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getMappingAssociationDetails(
				@RequestParam(value = "mappingId",required = true) int mappingId) {
		logger.debug(">> getMappingAssociationDetails in ComposerMappingController " + mappingId ); 
		ResponseObject responseObject  = this.composerMappingService.getMappingAssociationDetails(mappingId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}

}
