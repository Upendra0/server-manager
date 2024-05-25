package com.elitecore.sm.services.model;

import javax.xml.bind.annotation.XmlEnumValue;

public enum FieldForOutputFileEnum {
	@XmlEnumValue("0")WCONFONLY("Write configured fields only",0), 
	@XmlEnumValue("1")WCONFANDFPARTONLY("Write Configured fields and First partial record",1),
	@XmlEnumValue("2")WCONFANDLPARTONLY("Write Configured fields and Last partial record",2);
	
	private String name;
	private int value;

	private FieldForOutputFileEnum(String name,int value) {
		this.name = name;
		this.setValue(value);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
