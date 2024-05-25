/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 * 
 */
@Component(value = "fixedLengthASCIIParser")
@Entity
@DiscriminatorValue(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN)
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "parserMappingCache")
public class FixedLengthASCIIParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4398862742046259838L;

	private boolean fileHeaderEnable;

	private boolean fileHeaderContainsFields;

	/**
	 * @return the fileHeaderEnable
	 */
	@Column(name = "FILEHEADERENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getFileHeaderEnable() {
		return fileHeaderEnable;
	}

	/**
	 * @return the fileHeaderContainsFields
	 */
	@Column(name = "FILEHEADERCONTAINSFILEDS", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getFileHeaderContainsFields() {
		return fileHeaderContainsFields;
	}

	public void setFileHeaderEnable(boolean fileHeaderEnable) {
		this.fileHeaderEnable = fileHeaderEnable;
	}

	public void setFileHeaderContainsFields(boolean fileHeaderContainsFields) {
		this.fileHeaderContainsFields = fileHeaderContainsFields;
	}
}
