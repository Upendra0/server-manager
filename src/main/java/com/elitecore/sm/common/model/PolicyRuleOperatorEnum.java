package com.elitecore.sm.common.model;

/**
 * Policy Operator Enum
 * 
 * @author chintan.patel
 *
 */
public enum PolicyRuleOperatorEnum {
	AND("AND"), OR("OR");

	private String value;


	PolicyRuleOperatorEnum(String value) {
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
