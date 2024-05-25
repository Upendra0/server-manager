
package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum SourceFieldFormatASCIIEnum {
	
	@XmlEnumValue("INTEGER")INTEGER("INTEGER"),
	@XmlEnumValue("FLOAT")FLOAT("FLOAT"),
	@XmlEnumValue("STRING")STRING("STRING"),
	@XmlEnumValue("DATE")DATE("DATE"),
	@XmlEnumValue("IPv6")IPv6("IPv6"),
	@XmlEnumValue("IPv6ToIPv4")IPv6ToIPv4("IPv6ToIPv4"),
	@XmlEnumValue("IP")IP("IP");
	
	private String name = null;

	private SourceFieldFormatASCIIEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
