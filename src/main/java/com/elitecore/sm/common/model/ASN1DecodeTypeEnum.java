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
public enum ASN1DecodeTypeEnum {
	@XmlEnumValue("XML")XML("XML"), @XmlEnumValue("JSON")JSON("JSON");
	
	private String value;

	ASN1DecodeTypeEnum(String value) {
		this.value = value; 
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getValue() {
		return value;
	}
	
	public String getASN1DecodeTypeEnum() {
		return this.value;
	}
}
