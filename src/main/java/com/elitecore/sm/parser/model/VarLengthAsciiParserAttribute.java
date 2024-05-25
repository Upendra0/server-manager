package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * @author hardik.loriya
 *
 */
@Entity
@DiscriminatorValue("VarLengthAsciiParserAttribute")
@DynamicUpdate
public class VarLengthAsciiParserAttribute extends ParserAttribute{

	private static final long serialVersionUID = 5782423943364749205L;
	
	private String sourceFieldName;
	
	/**
	 * 
	 * @return sourceFieldName
	 */
	@XmlElement
	@Column(name = "SRCFIELDNAME", nullable = true, length = 200, unique = false)
	public String getSourceFieldName() {
		return sourceFieldName;
	}

	/**
	 * 
	 * @param sourceFieldName
	 */
	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}
	
}
