/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.iam.validator.StaffValidator;

/**
 * @author Sunil Gulabani Apr 17, 2015
 */
@Controller
public class ResetPasswordController extends BaseController {

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	private StaffService staffService;

	@Autowired
	private StaffValidator validator;

	/**
	 * Reset Staff's Password
	 * 
	 * @param newPassword
	 * @param confirmNewPassword
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('RESET_PASSWORD_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.RESET_PASSWORD, method = RequestMethod.POST)
	public ModelAndView resetPassword(@RequestParam(value = "newPassword", required = true) String newPassword, @RequestParam(
			value = "confirmNewPassword", required = true) String confirmNewPassword, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.RESET_PASSWORD);

		Map<String, String> validationMap = validator.validateForResetPassword(newPassword, confirmNewPassword);

		if (validationMap.size() == 0) {
			if (request.getSession().getAttribute(BaseConstants.STAFF_DETAILS) != null) {
				Staff loggedInStaff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);

				ResponseObject responseObject = this.staffService.resetPassword(loggedInStaff.getUsername(), newPassword, 0);
				if (responseObject.isSuccess() && "reset.password.success".equals(responseObject.getResponseCode().toString())) {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					if (auth != null) {
						new SecurityContextLogoutHandler().logout(request, response, auth);
						logger.info("auth.getPrincipal(): " + auth.getPrincipal());
					}

					model = new ModelAndView(new RedirectView(ControllerConstants.WELCOME));
					redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG, getMessage("reset.password.and.logout.success"));
				} else {
					model.addObject(BaseConstants.ERROR_MSG, getMessage(responseObject.getResponseCode().toString()));
				}
			}
		} else {
			for (String key : validationMap.keySet()) {
				model.addObject(key, validationMap.get(key));
			}
			model.addObject("NEW_PASSWORD", newPassword);
			model.addObject("CONFIRM_NEW_PASSWORD", confirmNewPassword);
		}
		return model;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
}