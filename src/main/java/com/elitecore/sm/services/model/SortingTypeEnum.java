/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * @author vandana.awatramani
 *
 */
@XmlEnum(String.class)
public enum SortingTypeEnum {
	
@XmlEnumValue("ASCENDING")Ascending("ASCENDING"),
@XmlEnumValue("DESCENDING")Descending("DESCENDING"),
@XmlEnumValue("NA")NA("NA");

private String value;

private SortingTypeEnum(String value) {
	this.value = value;
}

public String getSortingTypeEnum() {
	return this.value;
}

/**
 * 
 * @return
 */
public String getValue() {
	return value;
}

/**
 * @param value the value to set
 */
public void setValue(String value) {
	this.value = value;
}
}
