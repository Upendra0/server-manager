package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * 
 * @author avani.panchal
 * enum for define scheduling day 
 *
 */
@XmlEnum(String.class)
public enum SchedulingDayEnum {
	@XmlEnumValue("1")Sunday("1"), @XmlEnumValue("2")Monday("2"), @XmlEnumValue("3")Tuesday("3"), 
	@XmlEnumValue("4")Wednesday("4"),@XmlEnumValue("5") Thursday("5"), @XmlEnumValue("6")Friday("6"),
	@XmlEnumValue("7")Saturday("7");
	
	private String dayVal;
	
	SchedulingDayEnum(String dayVal) {
		this.dayVal = dayVal;
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getDayVal() {
		return dayVal;
	}
}
