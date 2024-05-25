/**
 * 
 */
package com.elitecore.sm.serverinstance.model;

/**
 * @author vandana.awatramani
 *
 */
public enum LogRollingTypeEnum {
	
	TIME_BASED("TIME-BASED"), VOLUME_BASED("SIZE-BASED");
	
	private String value;
	

	LogRollingTypeEnum(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}