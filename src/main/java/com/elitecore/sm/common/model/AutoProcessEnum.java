package com.elitecore.sm.common.model;

public enum AutoProcessEnum {
	AutoReload("AutoReloadCache"), AutoUpload("AutoUpload");

	private String value;

	AutoProcessEnum(String value) {
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
