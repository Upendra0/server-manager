package com.elitecore.sm.common.model;

public enum AutoOperationStatusEnum {
	ALL("ALL"), INPROGRESS("INPROGRESS"), COMPLETED("COMPLETED"), FAILED("FAILED");

	private String value;

	AutoOperationStatusEnum(String value) {
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
