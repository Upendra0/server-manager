package com.elitecore.sm.productconfig.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JSTreeProductConfigurationData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.productconfig.model.ProfileEntity;
import com.elitecore.sm.productconfig.service.ProductConfigurationService;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ProductConfigurationController extends BaseController{
	@Autowired
	ProductConfigurationService productConfigService;

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
	 * Call when click on product configuration left menu
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_PRODUCT_CONFIGURATION,method= RequestMethod.GET)
	public ModelAndView initProductConfiguration() {
		ModelAndView model = new ModelAndView(ViewNameConstants.PRODUCT_CONFIGURATION_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PRODUCT_CONFIGURATION);
		
		List<ServerType> activeServerTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject("activeServerTypeList",activeServerTypeList);
		
		return model;
	}
	
	/**
	 * Fetch default configuration based on server type
	 * @param selectedServerTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.GET_DEFAULT_PRODUCT_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public  String getDefaultProductConfiguration(
			 @RequestParam(value="selectedServerTypeId") String selectedServerTypeId) {
	
		List<ProfileEntity> defaultCacheprofileEntityList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.DEFAULT_PRODUCT_CONFIGURATION_LIST,selectedServerTypeId,null);
		
		return new JSTreeProductConfigurationData(defaultCacheprofileEntityList).toString();
		
	}
	
	/**
	 * Fetch Custom configuration based on server type
	 * @param selectedServerTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PRODUCT_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.GET_CUSTOM_PRODUCT_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public  String getCustomProductConfiguration(
			 @RequestParam(value="selectedServerTypeId") String selectedServerTypeId) {
			
		List<ProfileEntity> customprofileEntityList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST,selectedServerTypeId,null);
		
		return new JSTreeProductConfigurationData(customprofileEntityList).toString();
		
	}
	
	/**
	 * Update custom profile 
	 * @param serverTypeId
	 * @param profileEntityList
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PRODUCT_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.UPDATE_PRODUCT_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public String updateProductconfiguration(
			@RequestParam(value="serverTypeId") String serverTypeId,
			@RequestParam(value="profileEntityList") String profileEntityList,
			HttpServletRequest request) throws CloneNotSupportedException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		if(!StringUtils.isEmpty(serverTypeId)){
			int iserverTypeId=Integer.parseInt(serverTypeId);
			ResponseObject responseObject=productConfigService.updateProductConfiguration(iserverTypeId,profileEntityList,eliteUtils.getLoggedInStaffId(request));
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Reset Product Configuration to default
	 * @param selectedProductTypeforReset
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PRODUCT_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.RESET_TO_DEFAULT_PRODUCT_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public String resetProductConfiguration(
			@RequestParam (value="selectedProductTypeforReset" , required=true) String selectedProductTypeforReset,
			HttpServletRequest request){
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		if(!StringUtils.isEmpty(selectedProductTypeforReset)){
			
			int iserverTypeId=Integer.parseInt(selectedProductTypeforReset);
			ResponseObject responseObject=productConfigService.resetProductConfiguration(iserverTypeId, eliteUtils.getLoggedInStaffId(request));
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
			
		}
		
		return ajaxResponse.toString();
	}
}
