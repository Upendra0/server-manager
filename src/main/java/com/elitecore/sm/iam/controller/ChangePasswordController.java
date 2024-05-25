/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.iam.validator.StaffValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.PasswordEncoderConfiguration;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani
 * Apr 17, 2015
 */
@Controller
public class ChangePasswordController extends BaseController{
	
	@Autowired(required=true)
	@Qualifier(value="staffService")
	private StaffService staffService;
	
	@Autowired
	private StaffValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	    binder.setValidator(validator);
	}

	/**
	 * Initializes the change password jsp page ViewNameConstants.CHANGE_PASSWORD
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CHANGE_PASSWORD_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_CHANGE_PASSWORD, method = RequestMethod.GET)
	public ModelAndView initChangePassword(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.CHANGE_PASSWORD);
		return model;
	}
	
	/**
	 * Change Password process will be carried out.
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmNewPassword
	 * @param question1
	 * @param answer1
	 * @param question2
	 * @param answer2
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CHANGE_PASSWORD')")
	@RequestMapping(value = ControllerConstants.CHANGE_PASSWORD, method = RequestMethod.POST)
	public ModelAndView changePassword(
			@RequestParam(value = "oldPassword",required=true) String oldPassword,
			@RequestParam(value = "newPassword",required=true) String newPassword,
			@RequestParam(value = "confirmNewPassword",required=true) String confirmNewPassword,
			@RequestParam(value = "question1",required=false,defaultValue="") String question1,
			@RequestParam(value = "answer1",required=false,defaultValue="") String answer1,
			@RequestParam(value = "question2",required=false,defaultValue="") String question2,
			@RequestParam(value = "answer2",required=false,defaultValue="") String answer2,
			HttpServletRequest request, 
			HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.CHANGE_PASSWORD);
		
		Map<String,String> validationMap = validator.validateForChangePassword(
														oldPassword, newPassword, confirmNewPassword,
														request.getSession().getAttribute(BaseConstants.IS_LOGIN_FIRST_TIME),
														question1, answer1,
														question2, answer2
													);
		Staff loggedInStaff = null;
		if(request.getSession().getAttribute(BaseConstants.STAFF_DETAILS)!=null){
			loggedInStaff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);
			
		}
		
		if(validationMap.size() == 0){
			if(loggedInStaff!=null){
				
				boolean isChangePasswordFirstTime = false;
				
				if(loggedInStaff.isFirstTimeLogin()  || (loggedInStaff.getPasswordExpiryDate()!=null && (new Date()).compareTo(loggedInStaff.getPasswordExpiryDate()) <=0))
 
					isChangePasswordFirstTime = true;
				ResponseObject responseObject = this.staffService.changePassword(loggedInStaff.getUsername(), oldPassword, newPassword,question1, answer1, question2, answer2);
				
				if(responseObject.isSuccess() && "change.password.success".equals(responseObject.getResponseCode().toString())){
					if(isChangePasswordFirstTime){
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						if (auth != null){
							new SecurityContextLogoutHandler().logout(request, response, auth);
						}
						model = new ModelAndView(new RedirectView(ControllerConstants.WELCOME));
						redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG, getMessage("change.password.and.logout.success"));
					}else{
						model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
					}
					
				}else{
					model.addObject(BaseConstants.ERROR_MSG, getMessage(responseObject.getResponseCode().toString()));
					if(responseObject.getResponseCode().toString().equalsIgnoreCase("old.password.invalid")){
						model.addObject(BaseConstants.OLD_PASS_ERROR, getMessage("changePassword.old.password.incorrect"));
					}
						
				}
			
			}
		}else{
			Staff staff =null;
			if(loggedInStaff!=null && loggedInStaff.getUsername()!=null) {
				staff =  this.staffService.getStaffDetails(loggedInStaff.getUsername());
			}			
			
			if(staff==null)
				validationMap.put(BaseConstants.OLD_PASS_ERROR, getMessage("changePassword.old.password.incorrect"));
			else if (!new PasswordEncoderConfiguration().matches(oldPassword, staff.getPassword()))
				validationMap.put(BaseConstants.OLD_PASS_ERROR, getMessage("changePassword.old.password.incorrect"));
			
			for(String key : validationMap.keySet()){
				model.addObject(key, validationMap.get(key));
			}
			model.addObject("OLD_PASSWORD", oldPassword);
			model.addObject("NEW_PASSWORD", newPassword);
			model.addObject("CONFIRM_NEW_PASSWORD", confirmNewPassword);
			
			model.addObject("QUESTION1", question1);
			model.addObject("ANSWER1", answer1);
			model.addObject("QUESTION2", question2);
			model.addObject("ANSWER2", answer2);
		}

		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF')")
	@RequestMapping(value = ControllerConstants.UPDATE_STAFF_PASSWORD, method = RequestMethod.POST)
	public ModelAndView changePasswordForStaff(@RequestParam(value = "staffUsername", required = true, defaultValue = "") String staffUsername, @RequestParam(value = "newPassword", required = true) String newPassword, @RequestParam(value = "confirmNewPassword", required = true) String confirmNewPassword, @RequestParam(value = "staff-change-password-reason-for-change", required = true) String reasonForChange, @RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false, defaultValue = BaseConstants.UPDATE_STAFF_BASIC_DETAIL) String requestActionType, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);

		Map<String, String> validationMap = validator.validateForResetPassword(newPassword, confirmNewPassword);

		if (validator.validateReasonForChange(reasonForChange)) {
			validationMap.put("STAFF_CHANGE_PASSWORD_REASON_FOR_CHANGE_ERROR", getMessage("staff.password.changed.reason.regex.invalid").replace("[REGEX]", Regex.get(SystemParametersConstant.STAFF_REASON_FOR_CHANGE_REGEX)));
		}

		if (validationMap.size() == 0) {
			if (request.getSession().getAttribute(BaseConstants.STAFF_DETAILS) != null) {
				Staff loggedInStaff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);



				ResponseObject resposneObject = staffService.resetPassword(staffUsername, confirmNewPassword, loggedInStaff.getId());

				
				if(resposneObject.isSuccess() &&  "reset.password.success".equalsIgnoreCase(resposneObject.getResponseCode().toString()) ){
					model.addObject(BaseConstants.RESPONSE_MSG, getMessage("change.password.success"));
				}else{
					model.addObject(BaseConstants.ERROR_MSG, getMessage(resposneObject.getResponseCode().toString()));
				}
				
			
			} else {
				model.addObject(BaseConstants.ERROR_MSG, getMessage("no.staff.found"));
			}
		} else {
			for (String key : validationMap.keySet()) {
				model.addObject(key, validationMap.get(key));
			}
		}

		Staff staff = staffService.getFullStaffDetails(staffUsername);
		model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);

		String profilePicAsString = "images/staff_default_profile_pic.png";
	
		model.addObject("profile_pic", profilePicAsString);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}		
}