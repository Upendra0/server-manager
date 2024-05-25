package com.elitecore.sm.snmp.model;

import javax.xml.bind.annotation.XmlElement;

import com.elitecore.sm.common.model.BaseModel;

public class SnmpServiceThresholdCustom extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceId;
	private String svcAlias;
	private int servicecThreshold;
	
	@XmlElement
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	@XmlElement
	public String getSvcAlias() {
		return svcAlias;
	}
	public void setSvcAlias(String svcAlias) {
		this.svcAlias = svcAlias;
	}
	
	@XmlElement
	public int getServicecThreshold() {
		return servicecThreshold;
	}
	public void setServicecThreshold(int servicecThreshold) {
		this.servicecThreshold = servicecThreshold;
	}
}
