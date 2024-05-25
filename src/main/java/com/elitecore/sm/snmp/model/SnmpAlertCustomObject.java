package com.elitecore.sm.snmp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.elitecore.sm.common.model.BaseModel;



@XmlType(propOrder = { "alertId", "alertName", "description", 
		"servicecThresholdList", "serverInstanceId", })
public class SnmpAlertCustomObject extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3725758793125803024L;
	private String alertId;
	private String alertName;
	private String description;
	private List<SnmpServiceThresholdCustom> servicecThresholdList;
	
	@XmlElement
	public List<SnmpServiceThresholdCustom> getServicecThresholdList() {
		return servicecThresholdList;
	}
	public void setServicecThresholdList(List<SnmpServiceThresholdCustom> servicecThresholdList) {
		this.servicecThresholdList = servicecThresholdList;
	}
	private int serverInstanceId;
	
	@XmlElement
	public String getAlertId() {
		return alertId;
	}
	public void setAlertId(String alertObject) {
		this.alertId = alertObject;
	}
	@XmlElement
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElement
	public int getServerInstanceId() {
		return serverInstanceId;
	}
	public void setServerInstanceId(int serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}
	
	
}
