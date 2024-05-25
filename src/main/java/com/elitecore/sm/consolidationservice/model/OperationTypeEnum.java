package com.elitecore.sm.consolidationservice.model;

public enum OperationTypeEnum {
	SUM("1"), MAX("2"), MIN("3"), AVERAGE("4"), MERGE("5");

	private String value;

	OperationTypeEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
