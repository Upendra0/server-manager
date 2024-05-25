/**
 * 
 */
package com.elitecore.sm.migration.converter;

import org.apache.log4j.Logger;
import org.dozer.DozerConverter;

import com.elitecore.sm.common.exception.EnumException;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 *	
 */
@SuppressWarnings("rawtypes")
public class DozerCustomConvertor extends DozerConverter {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@SuppressWarnings("unchecked")
	public DozerCustomConvertor() {
		super(String.class, String.class);
	}

	@SuppressWarnings("unchecked")
	public DozerCustomConvertor(Class<String> prototypeA, Class<String> prototypeB) {
		super(prototypeA, prototypeB);
	}

	/*
	 * (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object convertTo(Object source, Object destination) {

		if(source == null || "".equals(source) ){
			return null;
		}else{
			String enumClassKey  = getParameter(); 
			String fullyClassifyEnumClass = (String) MapCache.getConfigValueAsObject(enumClassKey);
			if(fullyClassifyEnumClass != null ){
				Class<?> clazz = getFullClassName(fullyClassifyEnumClass);
				if(clazz != null ){
					return getInstance(source.toString(), (Class<Enum>) clazz);
				}else{
					throw new EnumException("Failed to get fully classified class name " +  enumClassKey);
				}
			}else{
				throw new EnumException("Failed to get full enum class name for " + enumClassKey);
			}
		}
	}

	/**
	 * Method will get instance for from the provide value and destination
	 * class.
	 * 
	 * @param value
	 * @param enumClass
	 * @return
	 */
	public <T> Object getInstance(final String value, final Class<T> enumClass) {
		String myValue = value;
		T[] results = enumClass.getEnumConstants();

		for (T result : results) {
			if (result.toString().equalsIgnoreCase(myValue)) {
				return result.toString();
			}
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (T e : results) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(e.toString());
		}

		throw new EnumException("Required value are : " + stringBuilder.toString()
				+ " And value found is : " + value);

	}

	@Override
	public Object convertFrom(Object source, Object destination) {
		return convertTo(source, destination);
	}

	/**
	 * @param className
	 * @return
	 */
	public Class<?> getFullClassName(String className) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
		return clazz;
	}
}
