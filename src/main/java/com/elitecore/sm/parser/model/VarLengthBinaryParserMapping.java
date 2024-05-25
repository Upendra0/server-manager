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
 * @author hardik.loriya
 *
 */
@Component(value="varLengthBinaryParser")
@Entity
@DiscriminatorValue(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN)
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
public class VarLengthBinaryParserMapping extends ParserMapping {

	
	private static final long serialVersionUID = -9123917145966083773L;

	private String dataDefinitionPath;
	private boolean skipFileHeader;
	private int fileHeaderSize;
	private boolean skipSubFileHeader;
	private int subFileHeaderSize;
	private int subFileLength;
	private String extractionRuleKey;
	private String extractionRuleValue;
	private String recordLengthAttribute;
	

	@Column(name = "DATADEFINITIONPATH", nullable = true, length = 500)
	@XmlElement
	public String getDataDefinitionPath() {
		return dataDefinitionPath;
	}

	public void setDataDefinitionPath(String dataDefinitionPath) {
		this.dataDefinitionPath = dataDefinitionPath;
	}

	@XmlElement
	@Column(name = "SKIPFILEHEADER", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSkipFileHeader() {
		return skipFileHeader;
	}

	public void setSkipFileHeader(boolean skipFileHeader) {
		this.skipFileHeader = skipFileHeader;
	}
	
	@XmlElement
	@Column(name = "FILEHEADERSIZE", length = 5)
	public int getFileHeaderSize() {
		return fileHeaderSize;
	}

	public void setFileHeaderSize(int fileHeaderSize) {
		this.fileHeaderSize = fileHeaderSize;
	}

	@XmlElement
	@Column(name = "SKIPSUBFILEHEADER", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSkipSubFileHeader() {
		return skipSubFileHeader;
	}

	public void setSkipSubFileHeader(boolean skipSubFileHeader) {
		this.skipSubFileHeader = skipSubFileHeader;
	}

	@XmlElement
	@Column(name = "SUBFILEHEADERSIZE", length = 5)
	public int getSubFileHeaderSize() {
		return subFileHeaderSize;
	}

	public void setSubFileHeaderSize(int subFileHeaderSize) {
		this.subFileHeaderSize = subFileHeaderSize;
	}

	@XmlElement
	@Column(name = "SUBFILELENGTH", length = 5)
	public int getSubFileLength() {
		return subFileLength;
	}

	public void setSubFileLength(int subFileLength) {
		this.subFileLength = subFileLength;
	}

	@XmlElement
	@Column(name = "EXTRACTIONRULEKEY", nullable=true, length = 100)
	public String getExtractionRuleKey() {
		return extractionRuleKey;
	}

	public void setExtractionRuleKey(String extractionRuleKey) {
		this.extractionRuleKey = extractionRuleKey;
	}

	@XmlElement
	@Column(name = "EXTRACTIONRULEVALUE", nullable=true, length = 100)
	public String getExtractionRuleValue() {
		return extractionRuleValue;
	}

	public void setExtractionRuleValue(String extractionRuleValue) {
		this.extractionRuleValue = extractionRuleValue;
	}

	@XmlElement
	@Column(name = "RECORDLENGTHATTRIBUTE", nullable=true, length = 100)
	public String getRecordLengthAttribute() {
		return recordLengthAttribute;
	}

	public void setRecordLengthAttribute(String recordLengthAttribute) {
		this.recordLengthAttribute = recordLengthAttribute;
	}
	
}
