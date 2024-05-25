package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("HtmlParserAttribute")
public class HtmlParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5782423943364749204L;
	
	private String fieldIdentifier;
	private String fieldExtractionMethod;
	private String fieldSectionId;
	private String containsFieldAttribute;
	private String tdNo;
	private String valueSeparator;
	private String valueIndex;
	
	@XmlElement
	@Column(name = "CONTAINSFIELDATTRIBUTE", nullable = true)
	public String getContainsFieldAttribute() {
		return containsFieldAttribute;
	}

	public void setContainsFieldAttribute(String containsFieldAttribute) {
		this.containsFieldAttribute = containsFieldAttribute;
	}

	@XmlElement
	@Column(name = "FIELDIDENTIFIER", nullable = true)
	public String getFieldIdentifier() {
		return fieldIdentifier;
	}

	public void setFieldIdentifier(String fieldIdentifier) {
		this.fieldIdentifier = fieldIdentifier;
	}
	
	@XmlElement
	@Column(name = "FIELDEXTRACTIONMETHOD", nullable = true)	
	public String getFieldExtractionMethod() {
		return fieldExtractionMethod;
	}

	public void setFieldExtractionMethod(String fieldExtractionMethod) {
		this.fieldExtractionMethod = fieldExtractionMethod;
	}
	
	@XmlElement
	@Column(name = "FIELDSECTIONID", nullable = true)
	public String getFieldSectionId() {
		return fieldSectionId;
	}

	public void setFieldSectionId(String fieldSectionId) {
		this.fieldSectionId = fieldSectionId;
	}

	@XmlElement
	@Column(name = "TDNO", nullable = true)
	public String getTdNo() {
		return tdNo;
	}


	public void setTdNo(String tdNo) {
		this.tdNo = tdNo;
	}

	@XmlElement
	@Column(name = "VALUESEPARATOR", nullable = true)
	public String getValueSeparator() {
		return valueSeparator;
	}

	public void setValueSeparator(String valueSeparator) {
		this.valueSeparator = valueSeparator;
	}

	@XmlElement
	@Column(name = "VALUEINDEX", nullable = true)
	public String getValueIndex() {
		return valueIndex;
	}

	public void setValueIndex(String valueIndex) {
		this.valueIndex = valueIndex;
	}

	
}
