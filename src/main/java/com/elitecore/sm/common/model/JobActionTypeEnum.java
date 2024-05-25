package com.elitecore.sm.common.model;

public enum JobActionTypeEnum {
	Append("Append"), Overwrite("Overwrite"),Update("Update");

	private String value;

	JobActionTypeEnum(String value) {
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
