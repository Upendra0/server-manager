package com.elitecore.sm.common.model;
/**
 * @author neha.kochhar
 *
 */
public enum MonthEnum {
	JANUARY("0"), FEBRUARY("1"), MARCH("2"),APRIL("3"),MAY("4"),JUNE("5"),JULY("6"),AUGUST("7"),SEPTEMBER("8"),OCTOBER("9"),NOVEMBER("10"),DECEMBER("11");
	
	private String value;
	

	MonthEnum(String value) {
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
