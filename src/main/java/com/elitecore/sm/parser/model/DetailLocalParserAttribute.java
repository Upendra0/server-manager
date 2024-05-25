package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("DetailLocalParserAttribute")
@DynamicUpdate
public class DetailLocalParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782423943364749204L;
	
		
}
