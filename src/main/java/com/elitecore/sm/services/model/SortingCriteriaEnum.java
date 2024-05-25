/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;


/**
 * @author vandana.awatramani
 * enum for define sorting criteria
 *
 */
@XmlEnum(String.class)
public enum SortingCriteriaEnum {
@XmlEnumValue("FILE_NAME")File_Name("FILE_NAME"),
@XmlEnumValue("LAST_MODIFIED_DATE")Last_Modified_Date("LAST_MODIFIED_DATE");


private String value;

private SortingCriteriaEnum(String value) {
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
