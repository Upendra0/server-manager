/**
 * 
 */
package com.elitecore.sm.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;

/**
 * Exception Utilities
 * 
 * @author Sunil Gulabani Mar 17, 2015
 */
public class EliteExceptionUtils extends ExceptionUtils {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public static String convertToString(String[] args0) {
		StringBuffer sb = new StringBuffer();
		if (args0 != null) {
			for (int i = 0; i < args0.length; i++) {
				sb.append("\n");
				sb.append(args0[i]);
			}
		}
		return sb.toString();
	}

	public static String getRootCauseStackTraceAsString(Throwable args) {
		String[] trace = ExceptionUtils.getRootCauseStackTrace(args);
		return convertToString(trace);
	}

	// customize the error message
	public String getErrorMessage(HttpServletRequest request, String key,MessageSource messageSource) {
		Exception exception = (Exception) request.getSession().getAttribute(key);
		
		logger.info("exception: " + exception);
		String error = "";
		if(exception!=null){
			logger.error(exception.getMessage(), exception);
			if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().equals(BaseConstants.LOGIN_IP_UNAUTHORIZED) ) {
				logger.info("instanceof LoginIPUnauthorizedException");
				error = messageSource.getMessage("login.ip.unauthorized", null, LocaleContextHolder.getLocale());
			} else if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().equals(BaseConstants.ACCOUNT_LOCKED_BY_ADMIN) ) {
				logger.info("instanceof StaffAccountLockedException");
				error = messageSource.getMessage("account.locked.by.admin", null, LocaleContextHolder.getLocale());
			} else if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().equals(BaseConstants.USERNAME_PASSWORD_BLANK) ) {
				logger.info("instanceof BadCredentialsException Username & Password blank");
				error = messageSource.getMessage("index.page.username.password.mandatory", null, LocaleContextHolder.getLocale());
			} else if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().equals(BaseConstants.USERNAME_BLANK) ) {
				logger.info("instanceof BadCredentialsException Username blank");
				error = messageSource.getMessage("index.page.username.mandatory", null, LocaleContextHolder.getLocale());
			} else if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().equals(BaseConstants.PASSWORD_BLANK) ) {
				logger.info("instanceof BadCredentialsException Password blank");
				error = messageSource.getMessage("index.page.password.mandatory", null, LocaleContextHolder.getLocale());
			}else if (!StringUtils.isEmpty(exception.getMessage()) && exception.getMessage().contains("LDAP server is not reachable")) {
				logger.info("instanceof CommunicationException");
				error = messageSource.getMessage("ldap.timeout", null, LocaleContextHolder.getLocale());
			} else if (exception instanceof BadCredentialsException) {
				logger.info("instanceof BadCredentialsException");
				error = messageSource.getMessage("bad.credentials", null, LocaleContextHolder.getLocale());
			} else if (exception instanceof LockedException) {
				error = messageSource.getMessage("account.locked", null, LocaleContextHolder.getLocale());
			} else if (exception instanceof AccountExpiredException) {
				error = messageSource.getMessage("account.expired", null, LocaleContextHolder.getLocale());
			} else if (exception instanceof SessionAuthenticationException) {
				if(exception.getMessage().contains("Maximum")){
					String exceptionMsg = exception.getMessage(); //Maximum sessions of 1 for this principal exceeded;
					exceptionMsg = exceptionMsg.replace("Maximum sessions of ", "");
					exceptionMsg = exceptionMsg.replace(" for this principal exceeded", "");
					logger.info("Total sessions allowed: " + exceptionMsg);
					error = messageSource.getMessage("maximum.session.allowed", null, LocaleContextHolder.getLocale());
					error = error.replace("[MAX_CONCURRENT_SESSION_COUNT]", exceptionMsg);
					logger.info("error: " + error);
				}else{
					error = messageSource.getMessage("invalid.session", null, LocaleContextHolder.getLocale());
				}
			} else if (exception instanceof DisabledException) {
				error = messageSource.getMessage("account.disabled", null, LocaleContextHolder.getLocale());
			} else if (exception instanceof CredentialsExpiredException) {
				error = messageSource.getMessage("credentials.expired", null, LocaleContextHolder.getLocale());
			} else {
				error = messageSource.getMessage("bad.credentials", null, LocaleContextHolder.getLocale());
			}
		}
		logger.info("error: " + error);
		return error;
	}
}