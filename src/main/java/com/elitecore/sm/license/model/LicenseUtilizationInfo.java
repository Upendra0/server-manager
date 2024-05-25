package com.elitecore.sm.license.model;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * 
 * @author sterlite
 *
 */
@Component(value = "licenseUtilizationInfo")
public class LicenseUtilizationInfo extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String deviceName;
	private String circleName;
	private String licenseType;
	private long appliedTps;
	private long currentTps;
	private long maxTps;
	private boolean isLicenseExhausted;

	public LicenseUtilizationInfo() {
		// Default constructor
	}

	public LicenseUtilizationInfo(String deviceName, String circleName, String licenseType, long appliedTps,
			long currentTps) {
		super();
		this.deviceName = deviceName;
		this.circleName = circleName;
		this.licenseType = licenseType;
		this.appliedTps = appliedTps;
		this.currentTps = currentTps;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getCircleName() {
		return circleName;
	}

	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public long getAppliedTps() {
		return appliedTps;
	}

	public void setAppliedTps(long appliedTps) {
		this.appliedTps = appliedTps;
	}

	public long getCurrentTps() {
		return currentTps;
	}

	public void setCurrentTps(long currentTps) {
		this.currentTps = currentTps;
	}

	public long getMaxTps() {
		return maxTps;
	}

	public void setMaxTps(long maxTps) {
		this.maxTps = maxTps;
	}

	public boolean isLicenseExhausted() {
		return isLicenseExhausted;
	}

	public void setLicenseExhausted(boolean isLicenseExhausted) {
		this.isLicenseExhausted = isLicenseExhausted;
	}

}
