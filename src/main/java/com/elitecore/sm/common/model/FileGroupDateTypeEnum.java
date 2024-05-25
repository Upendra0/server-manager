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
public enum FileGroupDateTypeEnum {
	@XmlEnumValue("PROCESSDATE")PROCESS_DATE("PROCESSDATE"), @XmlEnumValue("FILEDATE")FILE_DATE("FILEDATE");
	
	private String value;

	FileGroupDateTypeEnum(String value) {
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
