package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/**
 * 
 * @author avani.panchal
 * enum for define scheduling date
 *
 */
@XmlEnum(String.class)
public enum SchedulingDateEnum {
	@XmlEnumValue("1")First("1"), @XmlEnumValue("2")Second("2"), @XmlEnumValue("3")Third("3"),@XmlEnumValue("4") Fourth("4"),@XmlEnumValue("5") Fifth("5"),
	@XmlEnumValue("6")Sixth("6"),@XmlEnumValue("7") Seventh("7"),@XmlEnumValue("8") Eighth("8"),@XmlEnumValue("9") Ninth("9"), @XmlEnumValue("10")Tenth("10"),
	@XmlEnumValue("11")Eleventh("11"),@XmlEnumValue("12") Twelfth("12"), @XmlEnumValue("13")Thirteenth("13"),@XmlEnumValue("14") Forteenth("14"),
	@XmlEnumValue("15")Fifteenth("15"),@XmlEnumValue("16") Sixteenth("16"), @XmlEnumValue("17")Seventeenth("17"),@XmlEnumValue("18") Eighteenth("18"),
	@XmlEnumValue("19")Ninteenth("19"),@XmlEnumValue("20") Twentieth("20"),@XmlEnumValue("21") TwentyFirst("21"),@XmlEnumValue("22") TwentySecond("22"),
	@XmlEnumValue("23")TwentyThird("23"),@XmlEnumValue("24") TwentyFourth("24"),@XmlEnumValue("25") Twentyfifth("25"),@XmlEnumValue("26") TwentySixth("26"), 
	@XmlEnumValue("27")TwentySeventh("27"),@XmlEnumValue("28") TwentyEighth("28"),@XmlEnumValue("0") LastDay("0");

	private String numVal;

	SchedulingDateEnum(String numVal) {
		this.numVal = numVal;
	}
	
	/**
	 * 
	 * @return constant value
	 */
	public String getNumVal() {
		return numVal;
	}
}
