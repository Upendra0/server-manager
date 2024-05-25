/**
 * 
 */
package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum FileMergeGroupingByEnum {
	@XmlEnumValue("all")ALL("all"), @XmlEnumValue("hourly")Hourly("hourly");
	
	private String value;

	FileMergeGroupingByEnum(String value) {
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
