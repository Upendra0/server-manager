/**
 * 
 */
package com.elitecore.sm.common.model;

/**
 * @author Ranjitsinh Reval
 *
 */
public enum PluginTypeCategoryEnum {
	PARSER("Parser"), COMPOSER("Composer");
	
	private String value;
	

	PluginTypeCategoryEnum(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getValue() {
		return value;
	}
}
