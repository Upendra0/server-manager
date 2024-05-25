/**
 * 
 */
package com.elitecore.sm.systemaudit.model;

/**
 * @author Ranjitsinh Reval
 *
 */
public class AuditUserDetails {

	
	private String userIpAddress;
	private int loggedInStaffId;
	private String loggedInUserName;
	private String remarkName;
	
	
	
	public AuditUserDetails(String userIpAddress, int loggedInStaffId, String loggedInUserName, String remarkName) {
		super();
		this.userIpAddress = userIpAddress;
		this.loggedInStaffId = loggedInStaffId;
		this.loggedInUserName = loggedInUserName;
		this.remarkName = remarkName;
	}
	/**
	 * @return the userIpAddress
	 */
	public String getUserIpAddress() {
		return userIpAddress;
	}
	/**
	 * @param userIpAddress the userIpAddress to set
	 */
	public void setUserIpAddress(String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}
	/**
	 * @return the loggedInStaffId
	 */
	public int getLoggedInStaffId() {
		return loggedInStaffId;
	}
	/**
	 * @param loggedInStaffId the loggedInStaffId to set
	 */
	public void setLoggedInStaffId(int loggedInStaffId) {
		this.loggedInStaffId = loggedInStaffId;
	}
	/**
	 * @return the loggedInUserName
	 */
	public String getLoggedInUserName() {
		return loggedInUserName;
	}
	/**
	 * @param loggedInUserName the loggedInUserName to set
	 */
	public void setLoggedInUserName(String loggedInUserName) {
		this.loggedInUserName = loggedInUserName;
	}
	/**
	 * @return the remarkName
	 */
	public String getRemarkName() {
		return remarkName;
	}
	/**
	 * @param remarkName the remarkName to set
	 */
	public void setRemarkName(String remarkName) {
		this.remarkName = remarkName;
	}
	
}
