package com.elitecore.sm.policy.model;

/**
 * The Class SearchPolicyCondition.
 */
public class SearchPolicyCondition {

	/** The policy condition name. */
	private String policyConditionName;
	
	/** The policy condition type. */
	private String policyConditionType;
	
	/** The policy condition description. */
	private String policyConditionDesc;
	
	/** The policy condition association status. */
	private String policyConditionAssoStatus;
	
	private Integer serverInstanceId;
	
	
	private Integer[] existingConditionIds;
	/** The condition IDS added to policy rule grid. */
	public Integer[] getExistingConditionIds() {
		return existingConditionIds;
	}

	public void setExistingConditionIds(Integer[] existingConditionIds) {
		this.existingConditionIds = existingConditionIds;
	}

	public Integer getServerInstanceId() {
		return serverInstanceId;
	}

	public void setServerInstanceId(Integer serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

	/**
	 * Gets the policy condition name.
	 *
	 * @return the policy condition name
	 */
	public String getPolicyConditionName() {
		return policyConditionName;
	}
	
	/**
	 * Sets the policy condition name.
	 *
	 * @param policyConditionName the new policy condition name
	 */
	public void setPolicyConditionName(String policyConditionName) {
		this.policyConditionName = policyConditionName;
	}
	
	/**
	 * Gets the policy condition type.
	 *
	 * @return the policy condition type
	 */
	public String getPolicyConditionType() {
		return policyConditionType;
	}
	
	/**
	 * Sets the policy condition type.
	 *
	 * @param policyConditionType the new policy condition type
	 */
	public void setPolicyConditionType(String policyConditionType) {
		this.policyConditionType = policyConditionType;
	}
	
	/**
	 * Gets the policy condition description.
	 *
	 * @return the policy condition description
	 */
	public String getPolicyConditionDesc() {
		return policyConditionDesc;
	}
	
	/**
	 * Sets the policy condition description.
	 *
	 * @param policyConditionDesc the new policy condition description
	 */
	public void setPolicyConditionDesc(String policyConditionDesc) {
		this.policyConditionDesc = policyConditionDesc;
	}
	
	/**
	 * Gets the policy condition association status.
	 *
	 * @return the policy condition association status
	 */
	public String getPolicyConditionAssoStatus() {
		return policyConditionAssoStatus;
	}
	
	/**
	 * Sets the policy condition association status.
	 *
	 * @param policyConditionAssoStatus the new policy condition association status
	 */
	public void setPolicyConditionAssoStatus(String policyConditionAssoStatus) {
		this.policyConditionAssoStatus = policyConditionAssoStatus;
	}
	
}
