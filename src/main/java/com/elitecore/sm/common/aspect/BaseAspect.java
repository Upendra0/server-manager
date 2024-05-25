/**
 * 
 */
package com.elitecore.sm.common.aspect;

import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author Sunil Gulabani
 * Jun 27, 2015
 */
public abstract class BaseAspect {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * It will print parameters (key-value) that are available in HttpServeltRequest
	 * @param request
	 */
	protected void printRequestParameters(HttpServletRequest request){
		Enumeration<String> enumeration = request.getParameterNames();
     	while(enumeration.hasMoreElements()){
     		String paramName = (String)enumeration.nextElement();
     		if(request.getParameter(paramName) instanceof String){
     			//logger.info(paramName + ": " + request.getParameter(paramName));
     		}
     	}
	}
	
	/**
	 * It will print parameters values that are available in Method Arguments
	 * @param request
	 */
	protected void printMethodArgs(ProceedingJoinPoint proceedingJoinPoint){
		try {
        	if(proceedingJoinPoint.getArgs()!=null && proceedingJoinPoint.getArgs().length>0){
        		logger.debug("Arguments passed: " + Arrays.toString(proceedingJoinPoint.getArgs()));
        	}
        } catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
