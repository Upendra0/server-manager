package com.elitecore.sm.migration.converter;

import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

public class PasswordConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("PasswordConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return getEncryptedPassword((String) sourceFieldValue);

		}
		return null;
	}

	public String getEncryptedPassword(String sourceFieldValue) {
		char[] chars = sourceFieldValue.toCharArray();
		StringBuilder hex = new StringBuilder();
		int length = chars.length;
		for (int i = length-1; i >= 0; i--) {
			hex.append(Integer.toHexString((int) chars[i]));
		}
		logger.info(hex.toString());
		return hex.toString();
	}
}

