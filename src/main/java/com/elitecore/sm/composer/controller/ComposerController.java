/**
 * 
 */
package com.elitecore.sm.composer.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.composer.validator.ComposerValidator;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class ComposerController extends BaseController {

	
	@Autowired
	ComposerService  composerService;
	
	@Autowired
	ComposerValidator  composerValidator;
	
	@Inject
	private PathListHistoryService pathListHistoryService;
	

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
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.CREATE_COMPOSER_PLUGIN, method = RequestMethod.POST)
	@ResponseBody public  String createComposerPlugin(
			@ModelAttribute (value = FormBeanConstants.COMPOSER_PLUGIN_FORM_BEAN) Composer composer,//NOSONAR
			BindingResult result,
			HttpServletRequest request)  {
		logger.debug(">> createComposerPlugin in ComposerController " ); 
		
		 AjaxResponse ajaxResponse = new AjaxResponse();
		 composerValidator.validationComposerPluginParameters(composer, result,null,false,null); // It will validate the composer plug-in fields.
		 
		 if(result.hasErrors()){
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			  logger.info("Failed to validate composer plugin details.");
		 }else{
			 composer.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			 composer.setCreatedDate(new Date());
			 
			 ResponseObject responseObject  = this.composerService.addComposer(composer);  // it will create new selected composer plug-in
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			 if(responseObject.isSuccess()) {
				 PathList pathList = composer.getMyDistDrvPathlist();
				 int composerId = composer.getId();
				 Composer comp = composerService.getComposerById(composerId);
				 pathListHistoryService.save(pathList, comp);
			 }
		 }
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_COMPOSER_PLUGIN, method = RequestMethod.POST)
	@ResponseBody public  String updateComposerPlugin(
			@ModelAttribute (value = FormBeanConstants.COMPOSER_PLUGIN_FORM_BEAN) Composer composer,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		
		 logger.debug(">> updateComposerPlugin in ComposerController " ); 
		
		 AjaxResponse ajaxResponse = new AjaxResponse();
		 composerValidator.validationComposerPluginParameters(composer, result,null,false,null); // It will validate the composer plug-in fields.
		 
		 if(result.hasErrors()){
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			  logger.info("Failed to validate composer plugin details.");
		 }else{
			 composer.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			 composer.setLastUpdatedDate(new Date());
			 ResponseObject responseObject  = this.composerService.updateComposer(composer);  // it will update composer plugin details.
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			 if(responseObject.isSuccess()) {
				 pathListHistoryService.save(composer.getMyDistDrvPathlist(), composer);
			 }
		 }
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * When click on composer name link , redirect to configuration page 
	 * @param requestParamMap
	 * @param request
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_COMPOSER_CONFIGURATION, method = RequestMethod.GET)
	public ModelAndView initComposerConfig(@RequestParam Map<String,String> requestParamMap){
		
		ModelAndView model = new ModelAndView();
		RedirectView view=null;
		String  composerType=requestParamMap.get("plugInType");
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.SERVER_INSTANCE_ID, requestParamMap.get(BaseConstants.INSTANCE_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get("plugInId"));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get("plugInName"));
		model.addObject(BaseConstants.PLUGIN_TYPE,composerType);
		model.addObject(BaseConstants.DIST_DRIVER_ID,requestParamMap.get(BaseConstants.DIST_DRIVER_ID));
		model.addObject(BaseConstants.DIST_DRIVER_NAME,requestParamMap.get(BaseConstants.DIST_DRIVER_NAME));
		model.addObject(BaseConstants.DIST_DRIVERTYPE_ALIAS,requestParamMap.get(BaseConstants.DIST_DRIVERTYPE_ALIAS));
		
		if(EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view=new RedirectView(ControllerConstants.INIT_ASCII_COMPOSER_MANGER);
		}else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view=new RedirectView(ControllerConstants.INIT_ASN1_COMPOSER_MANGER);
		}
		else if(EngineConstants.XML_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view=new RedirectView(ControllerConstants.INIT_XML_COMPOSER_MANAGER);
		}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view = new RedirectView(ControllerConstants.INIT_FIXED_LENGTH_ASCII_COMPOSER_MANAGER);
		}else if(EngineConstants.TAP_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view = new RedirectView(ControllerConstants.INIT_TAP_COMPOSER_MANGER);
		}else if(EngineConstants.RAP_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view = new RedirectView(ControllerConstants.INIT_RAP_COMPOSER_MANGER);
		}else if(EngineConstants.NRTRDE_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
			view = new RedirectView(ControllerConstants.INIT_NRTRDE_COMPOSER_MANGER);
		}
		
		model.setView(view);
		return model;
	}

	/**
	 * Delete parser from database
	 * @param parserIdList
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.DELETE_COMPOSER_PLUGIN, method = RequestMethod.POST)
	@ResponseBody public  String deleteComposerPlugin(
			@RequestParam(value="composerIdList") String parserIdList,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse;
		ResponseObject responseObject ;

		responseObject = composerService.deleteComposer(parserIdList, eliteUtils.getLoggedInStaffId(request));
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}

}
