package com.elitecore.sm.drivers.model;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"iPAddressHost"})
public class HostParameters {
	private String iPAddressHost;

	@XmlElement(name="iPAddressHost")
	@Transient
	public String getiPAddressHost() {
		return iPAddressHost;
	}

	public void setiPAddressHost(String iPAddressHost) {
		this.iPAddressHost = iPAddressHost;
	}

}
