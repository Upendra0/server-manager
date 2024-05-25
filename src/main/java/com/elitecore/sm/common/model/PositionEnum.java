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
public enum PositionEnum {
	@XmlEnumValue("left")LEFT("left"), @XmlEnumValue("right")RIGHT("right");
	
	private String value;

	PositionEnum(String value) {
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
