package com.elitecore.sm.migration.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

public class StringToFindConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("StringToFindConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return getFindProperty((String) sourceFieldValue);

		}
		return null;
	}

	private String getFindProperty(String findAndReplaceString) {
		try {
			if (StringUtils.isNotEmpty(findAndReplaceString)) {
				String[] parts = findAndReplaceString.split(",");
				String find = parts[0];
				if (StringUtils.isNotEmpty(find)) {
					return find.replaceAll(" ", "[s]");//NOSONAR
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
