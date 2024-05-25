package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("NatFlowParserAttribute")
@DynamicUpdate
public class NatFlowParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782423943364749204L;
	
	private String destDateFormat;
	
	@XmlElement
	@Column(name = "DESTDATEFORMAT", nullable = true)
	public String getDestDateFormat() {
		return destDateFormat;
	}
	
	public void setDestDateFormat(String destDateFormat) {
		this.destDateFormat = destDateFormat;
	}
}
