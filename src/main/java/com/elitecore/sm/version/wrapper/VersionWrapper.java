package com.elitecore.sm.version.wrapper;



public class VersionWrapper {

	private String versionNumber;
	private String hotfixNumber;
	private String hostName;
	private String installationDate;
	private	String moduleName;
	private String startDate;
	private String endDate;
	private String licenceType;
	private String productType;	

	public VersionWrapper(String versionNumber, String hotfixNumber,String installationDate,String moduleName,
			String hostName,String startDate, String endDate, String licenceType,
			String productType) {
		super();
		this.versionNumber = versionNumber;
		this.hotfixNumber = hotfixNumber;
		this.hostName = hostName;
		this.installationDate = installationDate;
		this.moduleName = moduleName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.licenceType = licenceType;
		this.productType = productType;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getLicenceType() {
		return licenceType;
	}

	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(String installationDate) {
		this.installationDate = installationDate;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getHotfixNumber() {
		return hotfixNumber;
	}

	public void setHotfixNumber(String hotfixNumber) {
		this.hotfixNumber = hotfixNumber;
	}
	
}
