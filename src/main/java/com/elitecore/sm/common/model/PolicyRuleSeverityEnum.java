package com.elitecore.sm.common.model;

/**
 * Policy Rule Severity Enum
 * 
 * @author krupesh.shah
 *
 */
public enum PolicyRuleSeverityEnum {
	HIGH("High"), MEDIUM("Medium"), LOW("Low");

	private String value;


	PolicyRuleSeverityEnum(String value) {
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
