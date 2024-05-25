package com.elitecore.sm.iam.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.MapCache;

/**
 * LoginController is the entry point for any user.
 * 
 * @author Sunil Gulabani
 * Mar 31, 2015
 */
@Controller
public class LoginController extends BaseController{
	
	/**
	 * If credentials are wrong or user account is inactive or expired or disabled, this method be invoked.
	 * It redirects to login page with specific error.
	 * @param error
	 * @param errorTimeout
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.LOGIN_FAIL, method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "error-timeout", required = false) String errorTimeout,
			HttpServletRequest request,
			Locale locale) {	
		ModelAndView model = new ModelAndView();
		
		if (error != null) {
			logger.info("if(error != null)");
			model.addObject("error", new EliteExceptionUtils().getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION",getMessageSource()));
		}else if(errorTimeout !=null){
			logger.info("else if(errorTimeout !=null)");
			model.addObject("error", getMessage("session.timeout"));
		}
		
		logger.info("error: " + error);
		logger.info("errorTimeout: " + errorTimeout);
		model.addObject(BaseConstants.CURRENT_LANGUAGE_LOCALE, locale);
		model.addObject(BaseConstants.LANGUAGE_PROP_LIST,MapCache.getConfigValueAsObject(SystemParametersConstant.LANGUAGE_PROP_LIST));
		if(Boolean.parseBoolean(MapCache.getConfigValueAsObject("SSO_ENABLE").toString())){
			return new ModelAndView("redirect:"+"/sso");
		}
		else{
			model.setViewName(ViewNameConstants.LOGIN_PAGE);
		}
		return model;
	}
}