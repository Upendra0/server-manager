/**
 * 
 */
package com.elitecore.sm.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Sunil Gulabani Jul 14, 2015
 */
public class SpringApplicationContext implements ApplicationContextAware {

	private static ApplicationContext appContext;

	// Private constructor prevents instantiation from other classes
	private SpringApplicationContext() {

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		appContext = applicationContext;//NOSONAR

	}

	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}
	
	@SuppressWarnings("unchecked")
	public static Object getBean(@SuppressWarnings("rawtypes") Class clazz) {
		return appContext.getBean(clazz);
	}
}