package com.elitecore.sm.profileconfig.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JSTreeBusinessModelData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.service.MenuService;
import com.elitecore.sm.profileconfig.service.ProfileConfigurationService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ProfileConfigurationController extends BaseController{
	
	@Autowired(required=true)
	@Qualifier(value="menuService")
	private MenuService menuService;
	
	@Autowired
	private ProfileConfigurationService profileConfigService;
	
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
	 * Call when click on profile configuration left menu
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PROFILE_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_PROFILE_CONFIGURATION,method= RequestMethod.GET)
	public ModelAndView initProfileConfiguration() {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.PROFILE_CONFIGURATION_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PROFILE_CONFIGURATION);
		return model;
	}
	
	/**
	 * Fetch Custom configuration based on server type
	 * @param selectedServerTypeId
	 * @return
	 */
	
	@PreAuthorize("hasAnyAuthority('VIEW_PROFILE_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.GET_PROFILE_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public  String getProfileConfiguration() {
			
		List<BusinessModel> modelList = this.menuService.getFullModelHierarchy();
		return new JSTreeBusinessModelData(modelList,null,true).toString();
		
	}
	
	/**
	 * Update Profile information 
	 * @param selectedActionList
	 * @param selectedSubModuleList
	 * @param selectedModulesList
	 * @param request
	 * @return String
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PROFILE_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.UPDATE_PROFILE_CONFIGURATION,method=RequestMethod.POST)
	@ResponseBody public String updateProfileconfiguration(
			@RequestParam(value="selectedActionList") String selectedActionList,
			@RequestParam(value="selectedSubModuleList") String selectedSubModuleList,
			@RequestParam(value="selectedModulesList") String selectedModulesList,
			HttpServletRequest request){
		
		AjaxResponse ajaxResponse;
		
		ResponseObject responseObject=profileConfigService.updateProfileConfiguration(selectedActionList,selectedSubModuleList,selectedModulesList,eliteUtils.getLoggedInStaffId(request));
		ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
		
	}

}
