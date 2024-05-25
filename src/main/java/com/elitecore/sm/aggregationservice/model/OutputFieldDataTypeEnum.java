package com.elitecore.sm.aggregationservice.model;

public enum OutputFieldDataTypeEnum {
	STRING("String"),
	DATE("Date"),
	INTEGER("Integer"),
	DOUBLE("Double"),
	FLOAT("Float")
	;
	
	private String displayValue;

	private OutputFieldDataTypeEnum(String displayValue) {
		this.setDisplayValue(displayValue);
	}
	
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

}