package com.elitecore.sm.migration.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

public class StringToReplaceConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("StringToReplaceConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return getReplaceProperty((String) sourceFieldValue);

		}
		return null;
	}

	private String getReplaceProperty(String findAndReplaceString) {
		try {
			if (StringUtils.isNotEmpty(findAndReplaceString)) {
				String[] parts = findAndReplaceString.split(",");
				String replace = parts[1];
				if (StringUtils.isNotEmpty(replace)) {
					return replace.replaceAll(" ", "[s]");//NOSONAR
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
