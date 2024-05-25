/**
 * 
 */
package com.elitecore.sm.services.model;

/**
 * @author Ranjitsinh Reval
 *
 */
public class SearchServices {

	
	private String serviceId;
	private String serviceInstanceName;
	private String serviceType;
	private String serverInstanceName;
	private String syncStatus;
	
	
	
	/**
	 * @return the serviceId
	 */
	public String getServiceId() {
		return serviceId;
	}
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * @return the serviceInstanceName
	 */
	public String getServiceInstanceName() {
		return serviceInstanceName;
	}
	/**
	 * @param serviceInstanceName the serviceInstanceName to set
	 */
	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}
	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}
	/**
	 * @param serviceType the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * @return the serverInstanceName
	 */
	public String getServerInstanceName() {
		return serverInstanceName;
	}
	/**
	 * @param serverInstanceName the serverInstanceName to set
	 */
	public void setServerInstanceName(String serverInstanceName) {
		this.serverInstanceName = serverInstanceName;
	}
	/**
	 * @return the syncStatus
	 */
	public String getSyncStatus() {
		return syncStatus;
	}
	/**
	 * @param syncStatus the syncStatus to set
	 */
	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}
	
}
