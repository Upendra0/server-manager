package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum FilterActionEnum {
	@XmlEnumValue("move")MOVE("move"),
	@XmlEnumValue("rename")RENAME("rename"),
	@XmlEnumValue("na")NA("na"),
	@XmlEnumValue("moveandrename")MOVEANDRENAME("moveandrename"),
	@XmlEnumValue("delete")DELETE("delete");
	
	private String value;

	FilterActionEnum(String value) {
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
