package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum FileActionParamEnum {
	@XmlEnumValue("destpath")DESTINATIONPATH("destpath"),
	@XmlEnumValue("ext")EXTENSION("ext"),
	@XmlEnumValue("na")NA("na"),
	@XmlEnumValue("delete")DELETE("delete");
	
	private String value;

	FileActionParamEnum(String value) {
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
