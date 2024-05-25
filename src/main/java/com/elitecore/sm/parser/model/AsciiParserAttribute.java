package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("AsciiParserAttribute")
@DynamicUpdate
public class AsciiParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782423943364749204L;
	
	private String portUnifiedField;
	private String ipPortSeperator;
	
	@XmlElement
	@Column(name = "PORTUNIFIEDFIELD", nullable = true)
	public String getPortUnifiedField() {
		return portUnifiedField;
	}
	
	public void setPortUnifiedField(String portUnifiedField) {
		this.portUnifiedField = portUnifiedField;
	}
	
	@XmlElement
	@Column(name = "IPPORTSEPERATOR", nullable = true)
	public String getIpPortSeperator() {
		return ipPortSeperator;
	}
	
	public void setIpPortSeperator(String ipPortSeperator) {
		this.ipPortSeperator = ipPortSeperator;
	}

	
}
