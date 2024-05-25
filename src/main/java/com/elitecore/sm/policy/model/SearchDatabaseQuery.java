package com.elitecore.sm.policy.model;

/**
 * @author Sagar Ghetiya
 *
 */

public class SearchDatabaseQuery {
	
	/**
	 * The name
	 */
	private String queryName;
	
	/**
	 * The description
	 */
	private String description;
	
	/**
	 * The association status
	 */
	private String associationStatus;

	/**
	 * The serverInstanceId
	 */
	private int serverInstanceId;
	
	/** Instantiates the Search Database Query
	 * @param queryName
	 * @param description
	 * @param associationStatus
	 * @param serverInstanceId
	 */
	public SearchDatabaseQuery(String queryName, String description, String associationStatus, int serverInstanceId) {
		super();
		this.queryName = queryName;
		this.description = description;
		this.associationStatus = associationStatus;
		this.serverInstanceId = serverInstanceId;
	}

	/**
	 * Gets the name of the query
	 * @return queryName
	 */
	public String getQueryName() {
		return queryName;
	}

	/**
	 * Sets the name of the query
	 * @param queryName
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	/**
	 * Gets the description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the association status
	 * @return associationStatus
	 */
	public String getAssociationStatus() {
		return associationStatus;
	}

	/**
	 * Sets the association status
	 * @param associationStatus
	 */
	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}

	/**
	 * Gets the instance id of the server
	 * @return instance id of the server
	 */
	public int getServerInstanceId() {
		return serverInstanceId;
	}

	/**
	 * Sets the instance id of the server
	 * @param serverInstanceId
	 */
	public void setServerInstanceId(int serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}
	
	
}
