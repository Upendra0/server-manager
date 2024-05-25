/**
 * 
 */
package com.elitecore.sm.config.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 * @version 1
 */
@Controller
public class I18NReloadController extends BaseController{
	
	@Autowired
	ReloadableResourceBundleMessageSource messageSource;   
	
	
	/**
	 * Method will change current language based on selected dropdown value and reload the message resource.
	 * @param currentLang
	 * @param locale
	 * @param request
	 * @param response
	 * @return ModelView
	 */
	@RequestMapping(value = ControllerConstants.CHANGE_LANGUAGE, method = RequestMethod.POST)
	public ModelAndView changeLanguage(
						@RequestParam(value = "currentLang", required = true) String currentLang,
						Locale locale ,
						HttpServletRequest request,
						HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		//SessionLocaleResolver localeResolver = (SessionLocaleResolver) RequestContextUtils.getLocaleResolver(request); // Will be use if we will use session local resolver.
		CookieLocaleResolver localeResolver = (CookieLocaleResolver) RequestContextUtils.getLocaleResolver(request); // As in I18 context file we have set cookie local resolver
		locale = new Locale(currentLang);
		localeResolver.setLocale(request, response, locale);
		messageSource.clearCache(); // It will reload the resource bundle with selected locale
		
		model.addObject(BaseConstants.CURRENT_LANGUAGE_LOCALE, locale); // Setting current locale to set the value in dropdown.
		model.addObject(BaseConstants.LANGUAGE_PROP_LIST,MapCache.getConfigValueAsObject(SystemParametersConstant.LANGUAGE_PROP_LIST));
		model.setViewName(ViewNameConstants.LOGIN_PAGE);
		model.addObject(response);
		return model;
	}
}
