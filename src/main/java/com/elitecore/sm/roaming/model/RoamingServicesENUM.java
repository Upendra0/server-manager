package com.elitecore.sm.roaming.model;
import javax.xml.bind.annotation.XmlEnumValue;


public enum RoamingServicesENUM {
	

	@XmlEnumValue("SMS")SMS("SMS"),
	@XmlEnumValue("DATA")DATA("DATA"),
	@XmlEnumValue("VOICE")VOICE("VOICE");
	
	private String name;

	private RoamingServicesENUM(String name) {
		this.name = name;
	}

	public String getRoamingServicesENUM() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
		

}
