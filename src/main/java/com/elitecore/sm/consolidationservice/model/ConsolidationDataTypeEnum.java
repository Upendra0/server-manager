package com.elitecore.sm.consolidationservice.model;

import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
public enum ConsolidationDataTypeEnum {
	@XmlEnumValue("DATE")DATE("Date"),
	@XmlEnumValue("Double")DOUBLE("Double"), 
	@XmlEnumValue("Float")FLOAT("Float"), 
	@XmlEnumValue("Integer")INTEGER("Integer"), 
	@XmlEnumValue("String")STRING("String"),
	@XmlEnumValue("Long")LONG("Long");

	private String value;

	ConsolidationDataTypeEnum(String value) {
		this.value = value;
	}

	public String getConsolidationDataTypeEnum() {
		return this.value;
	}
	
	public String getValue() {
		return value;
	}
}
