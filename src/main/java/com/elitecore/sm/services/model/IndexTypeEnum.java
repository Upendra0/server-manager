package com.elitecore.sm.services.model;

public enum IndexTypeEnum {
	
	HASHBASE("hash-base");
	
	private String value;

	IndexTypeEnum(String value) {
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
