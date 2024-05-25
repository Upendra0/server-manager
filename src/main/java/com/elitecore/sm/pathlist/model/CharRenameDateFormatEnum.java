package com.elitecore.sm.pathlist.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum CharRenameDateFormatEnum {

	@XmlEnumValue("")NA("NA"),
	@XmlEnumValue("yyMMddHHmmss")yyMMddHHmmss("yyMMddHHmmss"),
	@XmlEnumValue("Other")OTHER("Other");
		
	private String name;

	private CharRenameDateFormatEnum(String name) {
		this.name = name;
	}

	public String getCharRenameDateFormatEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
