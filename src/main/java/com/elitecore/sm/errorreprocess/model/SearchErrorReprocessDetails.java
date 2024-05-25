/**
 * 
 */
package com.elitecore.sm.errorreprocess.model;




/**
 * @author Ranjitsinh Reval
 *
 */
public class SearchErrorReprocessDetails {

	private String serviceType;
	private Integer batchId = 0;
	private int serviceId;
	private String reprocessStatus;
	private String fileNameContains;
	private String fromDate;
	private String toDate;
	private String fileAction;
	private String category;
	private String serviceInstanceIds;
	private Integer ruleId = 0;
	private String ruleAlias;
	
	
	public String getServiceInstanceIds() {
		return serviceInstanceIds;
	}
	
	public void setServiceInstanceIds(String serviceInstanceIds) {
		this.serviceInstanceIds = serviceInstanceIds;
	}


	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the batchId
	 */
	public int getBatchId() {
		return batchId;
	}
	
	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	
	/**
	 * @return the serviceId
	 */
	public int getServiceId() {
		return serviceId;
	}
	
	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	
	/**
	 * @return the reprocessStatus
	 */
	public String getReprocessStatus() {
		return reprocessStatus;
	}
	
	/**
	 * @param reprocessStatus the reprocessStatus to set
	 */
	public void setReprocessStatus(String reprocessStatus) {
		this.reprocessStatus = reprocessStatus;
	}
	
	/**
	 * @return the fileNameContains
	 */
	public String getFileNameContains() {
		return fileNameContains;
	}
	
	/**
	 * @param fileNameContains the fileNameContains to set
	 */
	public void setFileNameContains(String fileNameContains) {
		this.fileNameContains = fileNameContains;
	}
	
	/**
	 * @return the fileAction
	 */
	public String getFileAction() {
		return fileAction;
	}

	
	/**
	 * @param fileAction the fileAction to set
	 */
	public void setFileAction(String fileAction) {
		this.fileAction = fileAction;
	}

	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	
	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	
	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	
	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	
	/**
	 * @return the ruleId
	 */
	public int getRuleId() {
		return ruleId;
	}

	
	/**
	 * @param ruleId the ruleId to set
	 */
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	
	/**
	 * @return the ruleAlias
	 */
	public String getRuleAlias() {
		return ruleAlias;
	}

	
	/**
	 * @param ruleAlias the ruleAlias to set
	 */
	public void setRuleAlias(String ruleAlias) {
		this.ruleAlias = ruleAlias;
	}
	
	
}
