package com.elitecore.sm.common.model;

/**
 * Policy Rule Category Enum
 * 
 * @author krupesh.shah
 *
 */
public enum PolicyRuleCategoryEnum {
	MISSING("Missing"), FILTER("Filter"),OTHER("Other"),DEFAULT("Default"),NA("NA"), BUSINESSVALIDATIONS("Business Validations"), LOOKUPFAILURE("Look up failure") ,MULTIPLEVALUESRETURNFAILURE("Multiple values return failure");

	private String value;


	PolicyRuleCategoryEnum(String value) {
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
