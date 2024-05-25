/**
 * 
 */
package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * @author vandana.awatramani
 *
 */
@XmlEnum(String.class)
public enum FileFetchTypeEnum {
	@XmlEnumValue("local")LOCAL("local"), @XmlEnumValue("telnet")TELNET("telnet");
	
	private String value;

	FileFetchTypeEnum(String value) {
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
