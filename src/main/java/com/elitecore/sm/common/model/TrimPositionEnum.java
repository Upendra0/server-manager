package com.elitecore.sm.common.model;

public enum TrimPositionEnum {

	LEFT("Left") , RIGHT("Right") , BOTH("Both");

	private String value;
	

	TrimPositionEnum(String value) {
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
