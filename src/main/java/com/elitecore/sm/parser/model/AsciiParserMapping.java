/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author avani.panchal
 *
 */
@Component(value="asciiParser")
@Entity
@DiscriminatorValue(EngineConstants.ASCII_PARSING_PLUGIN)
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
public class AsciiParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8390318303085416992L;
	
	private FileTypeEnum fileTypeEnum=FileTypeEnum.KEY_VALUE_RECORD;

	private String fieldSeparatorEnum;
	private String fieldSeparator;
	private String find;
	private String replace;

	private boolean fileHeaderEnable;
	private boolean fileHeaderContainsFields;
	private FileHeaderFooterTypeEnum fileHeaderParser=FileHeaderFooterTypeEnum.STANDARD;
	private int fileHeaderRow;
	private int fileHeaderStartIndex;
	
	private boolean fileFooterEnable;
	private FileHeaderFooterTypeEnum fileFooterParser=FileHeaderFooterTypeEnum.BAF;
	private String fileFooterContains;
	private int fileFooterRows;
	
	private boolean keyValueRecordEnable;
	private String keyValueSeparatorEnum;
	private String keyValueSeparator;
	
	private boolean recordHeaderEnable;
	private String recordHeaderSepEnum;
	private String recordHeaderSeparator;
	private String recordHeaderLength;
	
	private String recordHeaderIdentifier;
	private int excludeCharactersMin ;
	private int excludeCharactersMax ;
	private String excludeLinesStart;
	private boolean linearKeyValueRecordEnable;
	/**
	 * 
	 * @return FileTypeEnum
	 */
	@XmlElement
	@Column(name = "FILETYPE", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	public FileTypeEnum getFileTypeEnum() {
		return fileTypeEnum;
	}
	
	/**
	 * 
	 * @param fileTypeEnum
	 */
	public void setFileTypeEnum(FileTypeEnum fileTypeEnum) {
		this.fileTypeEnum = fileTypeEnum;
	}
	
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

	/**
	 * @return the fileFooterEnable
	 */
	@Column(name = "FILEFOOTERENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getFileFooterEnable() {
		return fileFooterEnable;
	}
/**
	 * @return the fileHeaderParser
	 */
	@Column(name = "FILEHEADERPARSER", nullable = true, length = 100)
	@XmlElement
	@Enumerated  (EnumType.STRING)
	public FileHeaderFooterTypeEnum getFileHeaderParser() {
		return fileHeaderParser;
	}
	
	/**
	 * @return the fileFooterParser
	 */
	@Column(name = "FILEFOOTERPARSER", nullable = true, length = 100)
	@XmlElement
	@Enumerated  (EnumType.STRING)
	public FileHeaderFooterTypeEnum getFileFooterParser() {
		return fileFooterParser;
	}

	/**
	 * @return the fileFooterContains
	 */
	@Column(name = "FILEFOOTERCONTAINS", nullable = true, length = 500)
	@XmlElement
	public String getFileFooterContains() {
		return fileFooterContains;
	}


	/**
	 * @return the keyValueRecordEnable
	 */
	@Column(name = "KEYVALUERECENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isKeyValueRecordEnable() {
		return keyValueRecordEnable;
	}

	/**
	 * @return the keyValueSeparator
	 */

	@Column(name = "KEYVALUESEPARATOR", nullable = true, length = 100)
	@XmlElement
	public String getKeyValueSeparator() {
		return keyValueSeparator;
	}

	/**
	 * @return the fieldSeparator
	 */
	@Column(name = "FILEDSEPARATOR", nullable = true, length = 100)
	@XmlElement
	public String getFieldSeparator() {
		return fieldSeparator;
	}

	/**
	 * @return the recordHeaderEnable
	 */
	@Column(name = "RECHEADERENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isRecordHeaderEnable() {
		return recordHeaderEnable;
	}

	/**
	 * @return the recordHeaderSeparator
	 */
	@Column(name = "RECHEADERSEPARATOR", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getRecordHeaderSeparator() {
		return recordHeaderSeparator;
	}

	/**
	 * @return the recordHeaderLength
	 */
	@Column(name = "RECHEADERLENGTH", nullable = true, length = 5)
	@XmlElement
	public String getRecordHeaderLength() {
		return recordHeaderLength;
	}
	
	/**
	 * 
	 * @return the find string
	 */
	@Column(name = "FINDSTR", nullable = true, length = 100)
	@XmlElement
	public String getFind() {
		return find;
	}	
	
	/**
	 * 
	 * @return replace string
	 */
	@Column(name = "REPLACESTR", nullable = true, length = 100)
	@XmlElement
	public String getReplace () {
		return replace;
	}
	
	/**
	 * 
	 * @return FileHeaderRow
	 */
	@Column(name = "FILEHEADERROW", nullable = true, length = 3)
	@XmlElement
	public int getFileHeaderRow() {
		return fileHeaderRow;
	}

	/**
	 * 
	 * @return FileHeaderStartIndex
	 */
	@Column(name = "FILEHEADERSTARTIND", nullable = true, length = 3)
	@XmlElement
	public int getFileHeaderStartIndex() {
		return fileHeaderStartIndex;
	}
	
	/**
	 * 
	 * @return FileFooterRows
	 */
	@Column(name = "FILEFOOTERROW", nullable = true, length = 3)
	@XmlElement
	public int getFileFooterRows() {
		return fileFooterRows;
	}
	/**
	 * 
	 * @return SeparatorEnum
	 */
	@Transient
	public String getFieldSeparatorEnum() {
		return fieldSeparatorEnum;
	}
	
	/**
	 * 
	 * @param fieldSeparatorEnum
	 */
	public void setFieldSeparatorEnum(String fieldSeparatorEnum) {
		this.fieldSeparatorEnum = fieldSeparatorEnum;
	}
	
	/**
	 * 
	 * @param fileHeaderStartIndex
	 */
	public void setFileHeaderStartIndex(int fileHeaderStartIndex) {
		this.fileHeaderStartIndex = fileHeaderStartIndex;
	}
	
	/**
	 * 
	 * @param fileFooterRows
	 */
		public void setFileFooterRows(int fileFooterRows) {
		this.fileFooterRows = fileFooterRows;
	}

	/**
	 * @param fileHeaderEnable
	 *            the fileHeaderEnable to set
	 */
	public void setFileHeaderEnable(boolean fileHeaderEnable) {
		this.fileHeaderEnable = fileHeaderEnable;
	}
	/**
	 * @param fileHeaderParser
	 *            the fileHeaderParser to set
	 */
	public void setFileHeaderParser(FileHeaderFooterTypeEnum fileHeaderParser) {
		this.fileHeaderParser = fileHeaderParser;
	}

	/**
	 * @param fileheaderContainsFields
	 *            the fileHeaderContainsFields to set
	 */
	public void setFileHeaderContainsFields(boolean fileheaderContainsFields) {
		this.fileHeaderContainsFields = fileheaderContainsFields;
	}

	/**
	 * @param fileFooterEnable
	 *            the fileFooterEnable to set
	 */
	public void setFileFooterEnable(boolean fileFooterEnable) {
		this.fileFooterEnable = fileFooterEnable;
	}
	/**
	 * @param fileFooterParser
	 *            the fileFooterParser to set
	 */
	public void setFileFooterParser(FileHeaderFooterTypeEnum fileFooterParser) {
		this.fileFooterParser = fileFooterParser;
	}
	
	/**
	 * @param fileFooterContains
	 *            the fileFooterContains to set
	 */
	public void setFileFooterContains(String fileFooterContains) {
		this.fileFooterContains = fileFooterContains;
	}


	/**
	 * @param keyValueRecordEnable
	 *            the keyValueRecordEnable to set
	 */
	public void setKeyValueRecordEnable(boolean keyValueRecordEnable) {
		this.keyValueRecordEnable = keyValueRecordEnable;
	}

	/**
	 * @param keyValueSeparator
	 *            the keyValueSeparator to set
	 */
	public void setKeyValueSeparator(String keyValueSeparator) {
		this.keyValueSeparator = keyValueSeparator;
	}

	/**
	 * @param fieldSeparator
	 *            the fieldSeparator to set
	 */
	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	/**
	 * @param recordHeaderEnable
	 *            the recordHeaderEnable to set
	 */
	public void setRecordHeaderEnable(boolean recordHeaderEnable) {
		this.recordHeaderEnable = recordHeaderEnable;
	}

	/**
	 * @param recordHeaderSeparator
	 *            the recordHeaderSeparator to set
	 */
	public void setRecordHeaderSeparator(String recordHeaderSeparator) {
		this.recordHeaderSeparator = recordHeaderSeparator;
	}

	/**
	 * @param recordHeaderLength
	 *            the recordHeaderLength to set
	 */
	public void setRecordHeaderLength(String recordHeaderLength) {
		this.recordHeaderLength = recordHeaderLength;
	}
	
	/**
	 * 
	 * @param find
	 */
	public void setFind(String find) {
		this.find = find;
	}
	
	/**
	 * 
	 * @param replace
	 */
	public void setReplace(String replace) {
		this.replace = replace;
	}
	
	/**
	 * 
	 * @param fileHeaderRow
	 */
	public void setFileHeaderRow(int fileHeaderRow) {
		this.fileHeaderRow = fileHeaderRow;
	}
	
	/**
	 * 
	 * @return KeyValueSeparatorEnum
	 */
	@Transient
	public String getKeyValueSeparatorEnum() {
		return keyValueSeparatorEnum;
	}

	/**
	 * 
	 * @param keyValueSeparatorEnum
	 */
	public void setKeyValueSeparatorEnum(String keyValueSeparatorEnum) {
		this.keyValueSeparatorEnum = keyValueSeparatorEnum;
	}

	/**
	 * 
	 * @return RecordHeaderSepEnum
	 */
	@Transient
	public String getRecordHeaderSepEnum() {
		return recordHeaderSepEnum;
	}

	/**
	 * 
	 * @param recordHeaderSepEnum
	 */
	public void setRecordHeaderSepEnum(String recordHeaderSepEnum) {
		this.recordHeaderSepEnum = recordHeaderSepEnum;
	}
	@Column(name = "RECORDHEADERIDENTIFIER", nullable = true, length = 100)
	@XmlElement
	public String getRecordHeaderIdentifier() {
		return recordHeaderIdentifier;
	}

	public void setRecordHeaderIdentifier(String recordHeaderIdentifier) {
		this.recordHeaderIdentifier = recordHeaderIdentifier;
	}
	@Column(name = "EXCLUDECHARACTERSMIN", nullable = true, length = 3)
	@XmlElement
	public int getExcludeCharactersMin() {
		return excludeCharactersMin;
	}

	public void setExcludeCharactersMin(int excludeCharactersMin) {
		this.excludeCharactersMin = excludeCharactersMin;
	}
	@Column(name = "EXCLUDECHARACTERSMAX", nullable = true, length = 3)
	@XmlElement
	public int getExcludeCharactersMax() {
		return excludeCharactersMax;
	}

	public void setExcludeCharactersMax(int excludeCharactersMax) {
		this.excludeCharactersMax = excludeCharactersMax;
	}
	@Column(name = "EXCLUDELINESSTART", nullable = true, length = 100)
	@XmlElement
	public String getExcludeLinesStart() {
		return excludeLinesStart;
	}

	public void setExcludeLinesStart(String excludeLinesStart) {
		this.excludeLinesStart = excludeLinesStart;
	}
	@Column(name = "LINEARKEYVALUERECENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isLinearKeyValueRecordEnable() {
		return linearKeyValueRecordEnable;
	}

	public void setLinearKeyValueRecordEnable(boolean linearKeyValueRecordEnable) {
		this.linearKeyValueRecordEnable = linearKeyValueRecordEnable;
	}
}
