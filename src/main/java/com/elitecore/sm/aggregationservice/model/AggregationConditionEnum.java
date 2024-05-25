package com.elitecore.sm.aggregationservice.model;

public enum AggregationConditionEnum {
	ONLYFILFOUND("Only First, Intermediate, Last Found",1),
	ONLYFLFOUND("Only First,Last Found",2),
	ONLYFIFOUND("Only First, Intermediate Found",3),
	ONLYILFOUND("Only Intermediate, Last Found",4),
	ONLYF("Only First",5),
	ONLYI("Only Intermediate",6),
	ONLYL("Only Last Found",7)
	;
	
	private String displayValue;
	private int value;

	private AggregationConditionEnum(String displayValue,int value) {
		this.setDisplayValue(displayValue);
		this.setValue(value);
	}
	
	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	public static String fromValue(String exprValue) {
	    for (AggregationConditionEnum aggCondition : AggregationConditionEnum.values()) {
		  if (String.valueOf(aggCondition.value).equals(exprValue)) {
	        return aggCondition.displayValue;
	      }
	    }
	    return null;
	}

}