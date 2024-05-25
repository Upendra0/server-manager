package com.elitecore.sm.consolidationservice.model;

public enum LogicalConditionOperatorEnum {
	AND("and"), OR("or");
	private String value;

	LogicalConditionOperatorEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
