package com.elitecore.sm.migration.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

/**
 * @author brijesh.soni
 * Nov 4, 2016
 */
public class StringToPortConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("StringToPortConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& int.class.isAssignableFrom(destinationClass)) {
			return getPort((String) sourceFieldValue);

		}
		if (int.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return sourceFieldValue.toString();
		}
		return null;
	}

	private Integer getPort(String ipAndPortString) {
		try {
			if(StringUtils.isNotEmpty(ipAndPortString)) {
				String[] parts = ipAndPortString.split(":");
				String port = parts[1];
				if (StringUtils.isNotEmpty(port)) {
					return Integer.parseInt(port);
				} else {
					return 0;
				}
			} else {
				return 0;
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
}
