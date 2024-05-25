package com.elitecore.sm.parser.model;

// TODO: Auto-generated Javadoc
/**
 * The Enum PolicyActionExpression. This enumeration is used to store function name and length for policy action
 * @author Elitecore
 */
public enum PolicyConditionExpressionEnum {

	/** The contains. */
	CONTAINS("contains(",2),
	
	/** The endwith. */
	ENDWITH("endWith(",2),
	
	/** The isblank. */
	ISBLANK("isBlank(",1),
	
	/** The startwith. */
	STARTWITH("startWith(",2),
	
	/** The is in. */
	isIn("isIn(",2),
	
	/** The is null. */
	isNull("isNull(",1),
	
	/** The is date greater than. */
	isDateGreaterThan("isDateGreaterThan(",2),
	
	/** The is date less than. */
	isDateLessThan("isDateLessThan(",2),
	
	/** The is date equels. */
	isDateEquels("isDateEquels(",2),
	
	/** The is date Valid. */
	isDateValid("isDateValid(",2),
	
	/** The best match. */
	bestMatch("bestMatch(",2),
	
	REGION_MATCHES("regionMatches(", 1),
	EQUALS("equals(", 1),
	CONTENTEQUALS("contentEquals(", 1),
	EQUALSIGNORECASE("equalsIgnoreCase(", 1),
	MATCHES("matches(", 1),
	;

	/** The function name. */
	private String functionName;
	
	/** The function lenght. */
	private int functionLenght;

	/**
	 * Instantiates a new policy action expression enum.
	 *
	 * @param functionName the function name
	 * @param functionLenght the function lenght
	 */
	private PolicyConditionExpressionEnum(String functionName, int functionLenght) {
		this.functionName = functionName;
		this.functionLenght = functionLenght;
	}

	/**
	 * Gets the function name.
	 *
	 * @return the function name
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * Gets the function lenght.
	 *
	 * @return the function lenght
	 */
	public int getFunctionLenght() {
		return functionLenght;
	}
}
