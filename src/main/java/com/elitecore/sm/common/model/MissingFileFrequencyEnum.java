package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum MissingFileFrequencyEnum {
	@XmlEnumValue("default")DEFAULT("default"),@XmlEnumValue("daily")DAILY("daily");
	
	private String value;

	MissingFileFrequencyEnum(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getValue() {
		return value;
	}

}
