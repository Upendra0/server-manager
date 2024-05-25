package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum DateTypeEnum {

	@XmlEnumValue("LOCAL_DATE")LOCALDATE("LOCAL_DATE"),
	@XmlEnumValue("FILE_DATE")FILEDATE("FILE_DATE");
		
	private String name;

	private DateTypeEnum(String name) {
		this.name = name;
	}

	public String getDateValueEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
