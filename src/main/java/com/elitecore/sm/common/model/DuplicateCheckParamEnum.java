package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum DuplicateCheckParamEnum {

	@XmlEnumValue("name")NAME("name"),@XmlEnumValue("filesize")FILE_SIZE("filesize"),@XmlEnumValue("md5")MD5("md5"),
	@XmlEnumValue("referencedevicename")REFERENCE_DEVICE_NAME("referencedevicename");
	
	private String value;

	DuplicateCheckParamEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
