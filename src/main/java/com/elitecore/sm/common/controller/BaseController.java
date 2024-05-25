/**
 * 
 */
package com.elitecore.sm.common.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.elitecore.sm.util.EliteUtils;

/**
 * @author Sunil Gulabani
 * Apr 29, 2015
 */
public abstract class BaseController {
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
    public MessageSource messageSource;
	
	@Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
	
	/**
	 * This method will provide the value for the specified key based on the user's locale.
	 * @param key
	 * @return
	 */
	protected String getMessage(String key){
		try {
			return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	protected String getMessage(String key,Object[]arguments){
		try {
			return messageSource.getMessage(key, arguments, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.error(" Error :"+ e.getMessage(), e);
		}
		return "";
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
