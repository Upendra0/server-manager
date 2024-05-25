package com.elitecore.sm.common.model;

public enum DecodeTypeEnum {
	UPSTREAM("UPSTREAM"), DOWNSTREAM("DOWNSTREAM");
	
	private String value;
	

	DecodeTypeEnum(String value) {
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
