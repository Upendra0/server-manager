package com.elitecore.sm.common.model;

public enum FirstLastEnum {

	LAST("L");

	private String name;

	private FirstLastEnum(String name) {
		this.name = name;
	}

	public String getFirstLastEnum() {
		return this.name;
	}
	
	public String getName() {
		return name;
	}
	
}
