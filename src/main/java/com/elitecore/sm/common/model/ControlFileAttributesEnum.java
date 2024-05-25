package com.elitecore.sm.common.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum ControlFileAttributesEnum {

	@XmlEnumValue("FILE_CREATION_DATETIME")FileCreationDate("FILE_CREATION_DATETIME"),
	@XmlEnumValue("DISTRIBUTED_FILE_NAME")FileName("DISTRIBUTED_FILE_NAME"),
	@XmlEnumValue("TOTAL_SUCCESS_RECORDS")NoOfRecords("TOTAL_SUCCESS_RECORDS");
	private String value;

	ControlFileAttributesEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	public static String getCommaSeparatedToString()
	{
		StringBuilder attributes = new StringBuilder();
		for(ControlFileAttributesEnum attr : ControlFileAttributesEnum.values())
		{
			attributes.append(attr.getValue() + ",");
		}
		attributes.deleteCharAt(attributes.length() - 1);
		return attributes.toString();
	}
}