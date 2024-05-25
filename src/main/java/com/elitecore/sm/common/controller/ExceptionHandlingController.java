/**
 * 
 */
package com.elitecore.sm.common.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.exception.EnumException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.MapCache;

/**
 * @author Sunil Gulabani
 * Apr 15, 2015
 */
@ControllerAdvice
public class ExceptionHandlingController extends BaseController{
	
	
	/**
	 * Handles Access Denied exception
	 * @param exception
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler({
			AccessDeniedException.class
		})
	public ModelAndView handleAccessDeniedException(Exception exception, HttpServletRequest request, HttpServletResponse response,Locale locale) throws IOException {
		logger.error(exception.getMessage(), exception);
		logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
		ModelAndView model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
		model.addObject(BaseConstants.ERROR_MSG, exception.getMessage());
		
		boolean isAjaxRequest = isRequestViaAjax();
		logger.info("isAjaxRequest: " + isAjaxRequest);
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute(BaseConstants.STAFF_DETAILS) == null ){
			if(isAjaxRequest){
				model.addObject(BaseConstants.ERROR_MSG, getMessage("session.timeout"));
				model.setViewName(ViewNameConstants.AJAX_SESSION_TIMEOUT);
			}else{
				logger.error( "session timeout in exception handler.");
				model.addObject(BaseConstants.ERROR_MSG, getMessage("session.timeout"));
				if(Boolean.parseBoolean(MapCache.getConfigValueAsObject("SSO_ENABLE").toString())){
					return new ModelAndView("redirect:"+"/sso");
				}
				else{
					model.setViewName(ViewNameConstants.LOGIN_PAGE);
				}
			}
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){
				new SecurityContextLogoutHandler().logout(request, response, auth);
				logger.info("auth.getPrincipal(): " + auth.getPrincipal());
			}
		
		}else if(exception instanceof AccessDeniedException){
			if(isAjaxRequest){
				model.addObject(BaseConstants.ERROR_MSG, getMessage("access.denied"));
				model.setViewName(ViewNameConstants.AJAX_ACCESS_DENIED);
			}else{
				model.setViewName(ViewNameConstants.ACCESS_DENIED);
			}
		}
		model.addObject(BaseConstants.CURRENT_LANGUAGE_LOCALE, locale);
		model.addObject(BaseConstants.LANGUAGE_PROP_LIST,MapCache.getConfigValueAsObject(SystemParametersConstant.LANGUAGE_PROP_LIST));
		return model;
	}
	
	/**
	 * It handles the missing parameter exception in request.
	 * @param exception
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@ExceptionHandler({
		MissingServletRequestParameterException.class
	})
	public ModelAndView handleMissingParametersException(Exception exception) throws IOException {
		logger.error(exception.getMessage(), exception);
		logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
		
		ModelAndView model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
		model.addObject(BaseConstants.ERROR_MSG, exception.getMessage());

		if(isRequestViaAjax()){
			AjaxResponse ajaxResponse = new AjaxResponse();
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg(exception.getMessage());
			model.addObject(BaseConstants.ERROR_MSG, ajaxResponse.toString());
			model.setViewName(ViewNameConstants.AJAX_ERROR_PAGE);
		}
		return model;
	}
	
	/**
	 * it will handle max upload size exceed exceptions.
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({MaxUploadSizeExceededException.class})
    public ModelAndView fileSizeException(Exception exception) {
		logger.error(exception.getMessage(), exception);
		logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
		
		ModelAndView model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
		model.addObject(BaseConstants.ERROR_MSG, exception.getMessage());
		return model;
    }

	/**
	 * it will handle generic exception for AJAX and HTTP Request.
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({Exception.class,SMException.class , EnumException.class})
    public ModelAndView exception(Exception exception) {
		logger.debug("Global exception handling for Exception or SMException.");
		logger.error(exception.getMessage(), exception);
		logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
		
		ModelAndView model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
		model.addObject(BaseConstants.ERROR_MSG, exception.getMessage());
		
		if(isRequestViaAjax()){
			logger.debug("Found ajax http ajax request.");
			AjaxResponse ajaxResponse = new AjaxResponse();
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg(getMessage(ResponseCode.AJAX_GENERIC_EXCEPTION.toString()));
			model.addObject(BaseConstants.ERROR_MSG, ajaxResponse.toString());
			model.setViewName(ViewNameConstants.AJAX_ERROR_PAGE);
		}else{
			logger.debug("Found general http request.");
		}
		
		return model;
    }
	
	
	
	/**
	 * it will check current request is HTTP or AJAX request. 
	 * @return
	 */
	private boolean isRequestViaAjax(){
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			return request!=null && request.getHeader("X-Requested-With")!=null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))?true:false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
}