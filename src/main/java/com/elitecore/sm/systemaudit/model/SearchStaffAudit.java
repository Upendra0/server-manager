/**
 * 
 */
package com.elitecore.sm.systemaudit.model;

import java.util.Date;

/**
 * @author Ranjitsinh Reval
 *
 */
public class SearchStaffAudit {

	private int entityId;
	private int subEntityId;
	private int auditActivityId;
	private String userName;
	private String actionType;
	private Date durationFrom;
	private Date durationTo;
	
	
	/**
	 * @return the entityId
	 */
	public int getEntityId() {
		return entityId;
	}
	/**
	 * @param entityId the entityId to set
	 */
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	/**
	 * @return the subEntityId
	 */
	public int getSubEntityId() {
		return subEntityId;
	}
	/**
	 * @param subEntityId the subEntityId to set
	 */
	public void setSubEntityId(int subEntityId) {
		this.subEntityId = subEntityId;
	}
	/**
	 * @return the auditActivityId
	 */
	public int getAuditActivityId() {
		return auditActivityId;
	}
	/**
	 * @param auditActivityId the auditActivityId to set
	 */
	public void setAuditActivityId(int auditActivityId) {
		this.auditActivityId = auditActivityId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the durationFrom
	 */
	public Date getDurationFrom() {
		return durationFrom;
	}
	/**
	 * @param durationFrom the durationFrom to set
	 */
	public void setDurationFrom(Date durationFrom) {
		this.durationFrom = durationFrom;
	}
	/**
	 * @return the durationTo
	 */
	public Date getDurationTo() {
		return durationTo;
	}
	/**
	 * @param durationTo the durationTo to set
	 */
	public void setDurationTo(Date durationTo) {
		this.durationTo = durationTo;
	}
	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

}
