package com.elitecore.sm.migration.converter;

import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

import com.elitecore.sm.common.exception.BooleanException;
import com.elitecore.sm.common.exception.EnumException;

/**
 * @author brijesh.soni Nov 4, 2016
 */
public class StringToBooleanConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class destinationClass, Class sourceClass) {
		logger.info("StringToBooleanConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass) && (Boolean.class.isAssignableFrom(destinationClass)||boolean.class.isAssignableFrom(destinationClass))) {
			
			return getBoolean((String) sourceFieldValue);

																			}
		if (Boolean.class.isAssignableFrom(sourceClass) && String.class.isAssignableFrom(destinationClass)) {
			return sourceFieldValue.toString();
		} 

		return null;
	}

	private Boolean getBoolean(String sourceFieldValue) {
		if ("on".equalsIgnoreCase(sourceFieldValue)
				|| "enabled".equalsIgnoreCase(sourceFieldValue)
				|| "true".equalsIgnoreCase(sourceFieldValue)) {
			return Boolean.TRUE;
		} else if ("off".equalsIgnoreCase(sourceFieldValue)
				|| "disabled".equalsIgnoreCase(sourceFieldValue)
				|| "false".equalsIgnoreCase(sourceFieldValue)) {
			return Boolean.FALSE;
		} else {
			throw new BooleanException("Required value are : 0n,Enabled,true,off,disabled and false. But value found is : " + sourceFieldValue);
		}
	}
}
