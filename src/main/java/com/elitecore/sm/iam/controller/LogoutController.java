package com.elitecore.sm.iam.controller;

import java.util.HashMap;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditUserDetails;
import com.elitecore.sm.systemaudit.service.SystemAuditService;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.MapCache;

/**
 * LoginController is the entry point for any user.
 * 
 * @author Sunil Gulabani
 * Mar 31, 2015
 */
@Controller
public class LogoutController extends BaseController{
	
	
	@Autowired
	@Qualifier(value = "systemAuditService")
	SystemAuditService systemAuditService;
	
	/**
	 * If logout gets failed or success then it redirects to login page with specific error or message.
	 * @param error
	 * @param logout
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.LOGOUT, method = RequestMethod.GET)
	public ModelAndView logout(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request,Locale locale) {	
		ModelAndView model = new ModelAndView();
		
		if (error != null) {
			model.addObject("error", new EliteExceptionUtils().getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION",getMessageSource()));
		}

		if (logout != null) {
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("logout.success"));
			 
			HttpSession httpSession =request.getSession();
			Staff staff = (Staff) httpSession.getAttribute(BaseConstants.STAFF_DETAILS);
			 
			httpSession.invalidate();

			//Code will audit logout action.
			if(staff != null ){
				HashMap<String, AuditActivity> auditActivityMap  =  (HashMap<String, AuditActivity>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ACTIVITY_ALIAS_LIST);
				AuditUserDetails auditUserDetails = new AuditUserDetails(eliteUtils.getFramedIpAddress(request) ,staff.getId()  , staff.getUsername() ,null);
				AuditActivity auditActivityObj = auditActivityMap.get(AuditConstants.LOGOUT_ACTION);
				String remarkMsg = "";
				if(auditActivityObj != null){
					remarkMsg = auditActivityObj.getMessage();
					if (remarkMsg != null && remarkMsg.indexOf("$entityName") >= 0) {
						remarkMsg = remarkMsg.replace("$entityName", "<b>"  + staff.getUsername() + "</b>");
					}
				}
				systemAuditService.addAuditDetails(auditUserDetails, auditActivityMap.get(AuditConstants.LOGOUT_ACTION), null, BaseConstants.SM_ACTION,remarkMsg);
			}
			
		}
		
		model.addObject(BaseConstants.CURRENT_LANGUAGE_LOCALE, locale);
		model.addObject(BaseConstants.LANGUAGE_PROP_LIST,MapCache.getConfigValueAsObject(SystemParametersConstant.LANGUAGE_PROP_LIST));
		model.setViewName(ViewNameConstants.LOGIN_PAGE);
		return model;
	}
}