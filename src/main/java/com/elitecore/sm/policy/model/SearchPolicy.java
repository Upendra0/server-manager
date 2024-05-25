package com.elitecore.sm.policy.model;

/**
 * Policy Search Parameters class.
 *
 * @author chintan.patel
 */
public class SearchPolicy {
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The Policy Rule Category String */
    private String category = "";
    
    /** The Policy Rule Severity String */
    private String severity = "";
    
    /** The Policy Rule Error Code String */
    private String errorcode = "";
    
	/** The association status. */
	private String associationStatus;
	
	/** The status. */
	private String status;
	
	/** The server instance id. */
	private int serverInstanceId;
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Gets the association status.
	 *
	 * @return the association status
	 */
	public String getAssociationStatus() {
		return associationStatus;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Gets the server instance id.
	 *
	 * @return the server instance id
	 */
	public int getServerInstanceId() {
		return serverInstanceId;
	}
	
	/**
	 * Sets the server instance id.
	 *
	 * @param serverInstanceId the new server instance id
	 */
	public void setServerInstanceId(int serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getErrorCode() {
		return errorcode;
	}

	public void setErrorCode(String errorCode) {
		this.errorcode = errorCode;
	}
	
	/**
	 * Instantiates a new search policy.
	 *
	 * @param name the name
	 * @param description the description
	 * @param associationStatus the association status
	 * @param status the status
	 * @param serverInstanceId the server instance id
	 */
	public SearchPolicy(String name, String description, String associationStatus, String status,
			int serverInstanceId,String reasonCategory,String reasonSeverity,String reasonErrorCode) {
		super();
		this.name = name;
		this.description = description;
		this.associationStatus = associationStatus;
		this.status = status;
		this.serverInstanceId = serverInstanceId;
		this.category = reasonCategory;
		this.severity = reasonSeverity;
		this.errorcode = reasonErrorCode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SearchPolicy [policyName=" + name + ", description=" + description + ", associationStatus="
				+ associationStatus + ", status=" + status + ", serverInstanceId=" + serverInstanceId +
				", category=" + category + ", severity=" + severity + ", errorCode=" + errorcode + "]";
		
		
	}
}
