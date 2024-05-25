package com.elitecore.sm.policy.model;

/**
 * The Class PolicyConditionOperator.
 */
public class PolicyConditionOperator {
	
	/** The name. */
	private String name;
	
	/** The value. */
	private String value;
	
	/**
	 * Instantiates a new policy condition operator.
	 *
	 * @param name the name
	 * @param value the value
	 */
	public PolicyConditionOperator(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
