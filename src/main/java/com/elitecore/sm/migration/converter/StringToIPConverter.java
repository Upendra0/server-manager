package com.elitecore.sm.migration.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

/**
 * @author brijesh.soni
 * Nov 4, 2016
 */
public class StringToIPConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("StringToIPConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return getIpAddress((String) sourceFieldValue);

		}
		return null;
	}

	private String getIpAddress(String ipAndPortString) {
		try {
			if (StringUtils.isNotEmpty(ipAndPortString)) {
				String[] parts = ipAndPortString.split(":");
				String ipAddress = parts[0];
				if (StringUtils.isNotEmpty(ipAddress)) {
					return ipAddress.trim();
				} else {
					return "";
				}
			} else {
				return "";
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}
}
