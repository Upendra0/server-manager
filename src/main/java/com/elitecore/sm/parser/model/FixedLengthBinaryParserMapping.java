package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author Elitecore
 *
 */
@Component(value = "FixedLengthBinaryParserMapping")
@Entity
@DiscriminatorValue(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN)
@XmlRootElement
public class FixedLengthBinaryParserMapping extends ParserMapping {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4398862742046259839L;

	private int recordLength = 0;
	
	/**
	 * @return the recordLength
	 */
	@XmlElement
	@Column(name = "RECORDLENGTH", nullable = true, length = 10)
	public int getRecordLength() {
		return recordLength;
	}
	
	/**
	 * @param recordLength
	 *            the recordLength to set
	 */
	public void setRecordLength(int recordLength) {
		this.recordLength = recordLength;
	}

}
