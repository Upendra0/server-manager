/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * @author vandana.awatramani
 * enum for define scheduling type 
 *
 */
@XmlEnum(String.class)
public enum SchedulingTypeEnum {
@XmlEnumValue("daily")Daily("daily"),@XmlEnumValue("weekly")Weekly("weekly"),@XmlEnumValue("monthly")Monthly("monthly");

private String value;

SchedulingTypeEnum(String value) {
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
