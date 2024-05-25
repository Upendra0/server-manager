package com.elitecore.sm.license.model;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * 
 * @author sterlite
 *
 */
@Component(value = "mappedDevicesInfoDTO")
public class MappedDevicesInfoDTO extends BaseModel {

	private static final long serialVersionUID = 1L;
	private String deviceName;
	private String circleName;
	private String deviceType;
	private String serviceName;
	private String serverInstanceName;
	
	public MappedDevicesInfoDTO() {
		// Default constructor
	}
	
	public MappedDevicesInfoDTO(String deviceName, String circleName, String deviceType, String serviceName,
			String serverInstanceName) {
		super();
		this.deviceName = deviceName;
		this.circleName = circleName;
		this.deviceType = deviceType;
		this.serviceName = serviceName;
		this.serverInstanceName = serverInstanceName;
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

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServerInstanceName() {
		return serverInstanceName;
	}

	public void setServerInstanceName(String serverInstanceName) {
		this.serverInstanceName = serverInstanceName;
	}

}
