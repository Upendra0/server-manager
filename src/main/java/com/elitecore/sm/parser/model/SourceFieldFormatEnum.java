package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum SourceFieldFormatEnum {
	
	@XmlEnumValue("NUMBER")NUMBER("NUMBER"),
	@XmlEnumValue("BCD")BCD("BCD"),
	@XmlEnumValue("TBCD")TBCD("TBCD"),
	@XmlEnumValue("TEXT")CHARS("TEXT"),
	@XmlEnumValue("DATE")DATE("DATE"),
	@XmlEnumValue("LITTLE_ENDIAN_DATE")LITTLE_ENDIAN_DATE("LITTLE_ENDIAN_DATE"),
	@XmlEnumValue("IPV4ADDRESS")IPV4ADDRESS("IPV4ADDRESS"),
	@XmlEnumValue("LITTLE_ENDIAN_NUMBER")LITTLE_ENDIAN_NUMBER("LITTLE_ENDIAN_NUMBER"),
	@XmlEnumValue("HEX_STRING")HEX_STRING("HEX_STRING"),
	@XmlEnumValue("LITTLE_ENDIAN_HEX_STRING")LITTLE_ENDIAN_HEX_STRING("LITTLE_ENDIAN_HEX_STRING"),
	@XmlEnumValue("HEX")HEX("HEX"),
	@XmlEnumValue("RVMBCD")RVMBCD("RVMBCD"),
	@XmlEnumValue("HEXTODATE")HEXTODATE("HEXTODATE"),
	@XmlEnumValue("BYTE_STRING")BYTE_STRING("BYTE_STRING"),
	@XmlEnumValue("STRING")STRING("STRING"),
	@XmlEnumValue("DIALBYTE_STRING")DIALBYTE_STRING("DIALBYTE_STRING"),
	@XmlEnumValue("HEX_REV")HEX_REV("HEX_REV"),
	@XmlEnumValue("NUMBER_REV")NUMBER_REV("NUMBER_REV");
	
	private String name = null;

	private SourceFieldFormatEnum(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}