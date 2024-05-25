package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum ParseConnFunctionEnum {

	@XmlEnumValue("Equals")Equals("Equals"),
	@XmlEnumValue("Startswith")Startswith("Startswith"),
	@XmlEnumValue("Contains")Contains("Contains"),
	@XmlEnumValue("Notequals")Notequals("Notequals");
	
	private String name;

	private ParseConnFunctionEnum(String name) {
		this.name = name;
	}

	public String getParseConnFunctionEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
