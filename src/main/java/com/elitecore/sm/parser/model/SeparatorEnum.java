package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum SeparatorEnum {

	@XmlEnumValue(",")COMMA("[,] COMMA", ","),
	@XmlEnumValue(".")DOT("[.] DOT","."),
	@XmlEnumValue(":")COLON ("[:] COLON",":"),
	@XmlEnumValue(";")SEMICOLON("[;] SEMICOLON",";"),
	@XmlEnumValue("s")SPACE("[ ] SPACE","s"),
	@XmlEnumValue("-")HYPHEN("[-] HYPHEN","-"),
	@XmlEnumValue("_")UNDERSCORE("[_] UNDERSCORE","_"),
	@XmlEnumValue("|")PIPE("[|] PIPE","|"),
	@XmlEnumValue("ssss")TAB("[ ] TAB", "ssss"),
	@XmlEnumValue("Other")OTHER("Other","Other");
		
	private String input;
	private String value;

	private SeparatorEnum(String input,String value) {
		this.input = input;
		this.value=value;
	}


	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	
	/**
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	
}
