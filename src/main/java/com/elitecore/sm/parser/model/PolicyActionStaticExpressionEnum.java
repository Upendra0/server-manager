package com.elitecore.sm.parser.model;

// TODO: Auto-generated Javadoc
/**
 * The Enum PolicyActionExpression. This enumeration is used to store function name and length for policy action
 * @author Elitecore
 */
public enum PolicyActionStaticExpressionEnum {


	/** The add. */
	GETHOUR("getHour()"),
	
	GETMONTH("getMonth()"),
	
	GETWEEKDAY("getWeekDay()"),
	
	GETYEAR("getYear()"),
	
	SYSDATE("sysDate"),
	
	NOW("now");
	

	/** The function name. */
	private String name;
	
	/**
	 * Instantiates a new policy action expression enum.
	 *
	 * @param functionName the function name
	 * @param functionLenght the function lenght
	 */
	private PolicyActionStaticExpressionEnum(String name) {
		this.name = name;
	}

	/**
	 * Gets the function name.
	 *
	 * @return the function name
	 */
	public String getName() {
		return name;
	}
}
