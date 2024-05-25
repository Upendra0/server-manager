package com.elitecore.sm.policy.model;

/**
 * The Class SearchPolicyAction.
 */
public class SearchPolicyAction {

	/** The policy action name. */
	private String policyActionName;
	
	/** The policy action type. */
	private String policyActionType;
	
	/** The policy action description. */
	private String policyActionDesc;
	
	/** The policy action association status. */
	private String policyActionAssoStatus;
	
	private Integer serverInstanceId;
	
	private Integer[] existingActionIds;	
	
	/** The action IDS added to policy rule grid. */
	public Integer[] getExistingActionIds() {
		return existingActionIds;
	}

	public void setExistingActionIds(Integer[] existingActionIds) {
		this.existingActionIds = existingActionIds;
	}

	public Integer getServerInstanceId() {
		return serverInstanceId;
	}

	public void setServerInstanceId(Integer serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

	/**
	 * Gets the policy action name.
	 *
	 * @return the policy action name
	 */
	public String getPolicyActionName() {
		return policyActionName;
	}
	
	/**
	 * Sets the policy action name.
	 *
	 * @param policyActionName the new policy action name
	 */
	public void setPolicyActionName(String policyActionName) {
		this.policyActionName = policyActionName;
	}
	
	/**
	 * Gets the policy action type.
	 *
	 * @return the policy action type
	 */
	public String getPolicyActionType() {
		return policyActionType;
	}
	
	/**
	 * Sets the policy action type.
	 *
	 * @param policyActionType the new policy action type
	 */
	public void setPolicyActionType(String policyActionType) {
		this.policyActionType = policyActionType;
	}
	
	/**
	 * Gets the policy action description.
	 *
	 * @return the policy action description
	 */
	public String getPolicyActionDesc() {
		return policyActionDesc;
	}
	
	/**
	 * Sets the policy action description.
	 *
	 * @param policyActionDesc the new policy action description
	 */
	public void setPolicyActionDesc(String policyActionDesc) {
		this.policyActionDesc = policyActionDesc;
	}
	
	/**
	 * Gets the policy action association status.
	 *
	 * @return the policy action association status
	 */
	public String getPolicyActionAssoStatus() {
		return policyActionAssoStatus;
	}
	
	/**
	 * Sets the policy action association status.
	 *
	 * @param policyActionAssoStatus the new policy action association status
	 */
	public void setPolicyActionAssoStatus(String policyActionAssoStatus) {
		this.policyActionAssoStatus = policyActionAssoStatus;
	}
	
}
