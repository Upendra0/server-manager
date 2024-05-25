package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum SourceDateFormatEnum {

	@XmlEnumValue("MM-dd-yyyy HH:mm:ss")MM_dd_yyyy_HH_mm_ss("MM-dd-yyyy HH:mm:ss"),
	@XmlEnumValue("dd-MM-yyyy HH:mm:ss")dd_MM_yyyy_HH_mm_ss("dd-MM-yyyy HH:mm:ss"),
	@XmlEnumValue("MM/dd/yyyy HH:mm:ss")MM_dd_yyyy_HH_mmss("MM/dd/yyyy HH:mm:ss"),
	@XmlEnumValue("dd/MM/yyyy HH:mm:ss")dd_MM_yyyy_HH_mmss("dd/MM/yyyy HH:mm:ss"),
	@XmlEnumValue("yyyy/dd/mm HH:mm:ss")yyyy_dd_mm_HH_mm_ss("yyyy/dd/mm HH:mm:ss"),
	@XmlEnumValue("yyyy/mm/dd HH:mm:ss")yyyy_mm_dd_HH_mm_ss("yyyy/mm/dd HH:mm:ss"),
	@XmlEnumValue("yyMMDDHHmmss")yyMMDDhhmmss("yyMMDDHHmmss"),
	@XmlEnumValue("Other")OTHER("Other");
		
	private String name;

	private SourceDateFormatEnum(String name) {
		this.name = name;
	}

	public String getSourceDateFormatEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
