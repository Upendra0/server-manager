package com.elitecore.sm.snmp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.elitecore.sm.common.model.BaseModel;

@XmlRootElement
public class SnmpAlertCustomObjectWrapper extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7381771720066608686L;
	private List<SnmpAlertCustomObject> alertCustomObjectList;
	
	@XmlElement
	public List<SnmpAlertCustomObject> getAlertCustomObjectList() {
		return alertCustomObjectList;
	}

	public void setAlertCustomObjectList(List<SnmpAlertCustomObject> alertCustomObjectList) {
		this.alertCustomObjectList = alertCustomObjectList;
	}
	
	
}
