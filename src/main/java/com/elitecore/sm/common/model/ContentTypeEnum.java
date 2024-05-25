package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum ContentTypeEnum {
	
	@XmlEnumValue("application/json")JSON("application/json"), 
	@XmlEnumValue("application/xml")XML("application/xml"),
	@XmlEnumValue("text/plain")TEXT("text/plain");
	
	private String name;

	private ContentTypeEnum(String name) {
		this.name = name;
	}

	public String getContentTypeEnum() {
		return this.name;
	}

	public String getName() {
		return name;
	}
}
