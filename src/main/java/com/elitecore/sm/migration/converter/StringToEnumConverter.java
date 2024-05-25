package com.elitecore.sm.migration.converter;

import org.apache.log4j.Logger;
import org.dozer.CustomConverter;

import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.EnumException;
import com.elitecore.sm.serverinstance.model.LogRollingTypeEnum;
import com.elitecore.sm.snmp.model.SNMPVersionType;

/**
 * @author brijesh.soni Nov 4, 2016
 */
public class StringToEnumConverter implements CustomConverter {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object convert(Object destinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
		logger.info("StringToEnumConverter.convert() ***");
		
		if (String.class.isAssignableFrom(sourceClass) && Enum.class.isAssignableFrom(destinationClass)) {
			return getInstance((String) sourceFieldValue, (Class<Enum>) destinationClass);
		}
		if (Enum.class.isAssignableFrom(sourceClass) && String.class.isAssignableFrom(destinationClass)) {
			return sourceFieldValue.toString();
		}
		
		return null;
	}

	public <T extends Enum<T>> T getInstance(final String value, final Class<T> enumClass) {
		String myValue = value;
		
		T[] results = enumClass.getEnumConstants();
		
		for (T result : results) {
			logger.info("StringToEnumConverter.getInstance() : ENUM : " + result.toString() + " : StringValue : " + value);
				
			if(enumClass == LogRollingTypeEnum.class){
				
				if(myValue.equals(MigrationConstants.TIME_D_BASED)){
					myValue = LogRollingTypeEnum.TIME_BASED.getValue();
				}else if (myValue.equals(MigrationConstants.SIZE_BASED)) {
					myValue = LogRollingTypeEnum.VOLUME_BASED.getValue();
				} 
			}
			if(enumClass == SNMPVersionType.class){
				if("0".equals(myValue)){
					myValue = "V0";
				}else if ("1".equals(myValue)) {
					myValue = "V1";
				} 
				else if ("2".equals(myValue)) {
					myValue = "V2";
				} 
				else if ("3".equals(myValue)) {
					myValue = "V3";
				} 
			}
			
			if(result.toString().equalsIgnoreCase(myValue)) {
				return Enum.valueOf(enumClass, result.toString());
			}
			
			
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		for(T e : results){
			if(stringBuilder.length() > 0){
				stringBuilder.append(", ");
			}
			stringBuilder.append(e.toString());
		}
		if(myValue == null || myValue.isEmpty()) {
			return null;
		}
		
		throw new EnumException("Required value are : " + stringBuilder.toString() + " And value found is : " + value);
	}
}
