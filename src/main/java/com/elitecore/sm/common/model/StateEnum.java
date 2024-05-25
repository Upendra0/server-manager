/**
 * 
 */
package com.elitecore.sm.common.model;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
public enum StateEnum {
	ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), DELETED("DELETED");
	
	private String value;


	StateEnum(String value) {
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
