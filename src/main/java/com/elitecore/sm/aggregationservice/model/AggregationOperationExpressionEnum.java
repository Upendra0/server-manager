package com.elitecore.sm.aggregationservice.model;

public enum AggregationOperationExpressionEnum {
	SUM("SUM",1),
	MAX("MAX",2),
	MIN("MIN",3),
	AVERAGE("AVERAGE",4),
	MERGE("MERGE",5),
	FIRST("FIRST",6),
	LAST("LAST",7)
	;
		
	private String displayValue;
	private int value;

	private AggregationOperationExpressionEnum(String displayValue, int value) {
		this.displayValue = displayValue;
		this.value = value;
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
	    for (AggregationOperationExpressionEnum aggOprExp : AggregationOperationExpressionEnum.values()) {
		  if (String.valueOf(aggOprExp.value).equals(exprValue)) {
	        return aggOprExp.displayValue;
	      }
	    }
	    return null;
	}
}
