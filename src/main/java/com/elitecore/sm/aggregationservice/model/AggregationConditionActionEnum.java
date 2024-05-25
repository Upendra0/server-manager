package com.elitecore.sm.aggregationservice.model;


public enum AggregationConditionActionEnum {
	AGGREGATE("Aggregate"),	
	AGGTIMEOUT("Aggregate on timeout");
	
	private String displayValue;
	
	private AggregationConditionActionEnum(String displayValue) {
		this.setDisplayValue(displayValue);
	}
	
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

}