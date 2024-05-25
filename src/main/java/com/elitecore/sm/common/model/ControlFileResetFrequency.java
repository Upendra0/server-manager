package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum ControlFileResetFrequency {
	@XmlEnumValue("default")DEFAULT("default"),
	@XmlEnumValue("daily")DAILY("daily");

	private String value;
	
	ControlFileResetFrequency(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
}