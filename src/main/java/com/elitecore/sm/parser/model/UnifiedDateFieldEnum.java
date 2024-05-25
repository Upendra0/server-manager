package com.elitecore.sm.parser.model;

import javax.xml.bind.annotation.XmlEnumValue;


public enum UnifiedDateFieldEnum {
	
	@XmlEnumValue("")NA(""),
	@XmlEnumValue("EgressEndDate")EgressEndDate("EgressEndDate"),
	@XmlEnumValue("EgressStartDate")EgressStartDate("EgressStartDate"),
	@XmlEnumValue("EndDate")EndDate("EndDate"),
	@XmlEnumValue("IngressEndDate")IngressEndDate("IngressEndDate"),
	@XmlEnumValue("IngressStartDate")IngressStartDate("IngressStartDate"),
	@XmlEnumValue("StartDate")StartDate("StartDate");
	
	
	private String name;

	private UnifiedDateFieldEnum(String name) {
		this.name = name;
	}

	public String getUnifiedFiedEnum() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
		

}
