package com.elitecore.sm.migration.converter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

/**
 * @author brijesh.soni Nov 4, 2016
 */
public class MinFileRangeConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public Object convert(Object destinationFieldValue,
			Object sourceFieldValue, Class<?> destinationClass,
			Class<?> sourceClass) {
		logger.info("MinFileRangeConverter.convert() ***");
		if (null == sourceFieldValue)
			return null;
		if (String.class.isAssignableFrom(sourceClass)
				&& int.class.isAssignableFrom(destinationClass)) {
			return getMinFileRange((String) sourceFieldValue);

		}
		if (Integer.class.isAssignableFrom(sourceClass)
				&& String.class.isAssignableFrom(destinationClass)) {
			return sourceFieldValue.toString();
		}
		return null;
	}

	private Integer getMinFileRange(String fileRange) {
		try {
			logger.info("MinFileRangeConverter.getMinFileRange()  " + fileRange);
			if (StringUtils.isEmpty(fileRange))
				return 0;
			else {
				String[] parts = fileRange.split("-");
				String minFileRange = parts[0];
				if (StringUtils.isNotEmpty(minFileRange)) {
					return Integer.parseInt(minFileRange);
				} else {
					return 0;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
	}
}
