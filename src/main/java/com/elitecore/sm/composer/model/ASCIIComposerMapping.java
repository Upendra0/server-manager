/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;

/**
 * @author jay.shah
 *
 */
@Primary
@Component(value="asciiComposer")
@Entity
@DiscriminatorValue(EngineConstants.ASCII_COMPOSER_PLUGIN)
@DynamicUpdate
public class ASCIIComposerMapping extends ComposerMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3248390756653416486L;
	
	private boolean fileHeaderEnable;
	private FileHeaderFooterTypeEnum fileHeaderParser=FileHeaderFooterTypeEnum.STANDARD;
	private boolean fileHeaderContainsFields;
	private String fieldSeparatorEnum;
	private String fieldSeparator;
	private boolean fileFooterEnable;
	private boolean fileHeaderSummaryEnable;
	private boolean fileFooterSummaryEnable;
	private String fileHeaderSummary;
	private String fileFooterSummary;

	

	/**
	 * @return the fileHeaderEnable
	 */
	@Column(name = "FILEHEADERENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getFileHeaderEnable() {
		return fileHeaderEnable;
	}

	/**
	 * @return the fileHeaderParser
	 */
	@Column(name = "FILEHEADERPARSER", nullable = true)
	@XmlElement
	@Enumerated(EnumType.STRING)
	public FileHeaderFooterTypeEnum getFileHeaderParser() {
		return fileHeaderParser;
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
	 * @return the fieldSeparator
	 */
	@Column(name = "FILEDSEPARATOR", nullable = true, length = 10)
	@XmlElement
	public String getFieldSeparator() {
		return fieldSeparator;
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
	 * @return fileFooterEnable
	 */
	@Column(name = "ISFILEFOOTERENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileFooterEnable() {
		return fileFooterEnable;
	}
	
	
	/**
	 * 
	 * @param fieldSeparatorEnum
	 */
	public void setFieldSeparatorEnum(String fieldSeparatorEnum) {
		this.fieldSeparatorEnum = fieldSeparatorEnum;
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
	 * @param fileHeaderContainsFields
	 *            the fileHeaderContainsFields to set
	 */
	public void setFileHeaderContainsFields(boolean fileHeaderContainsFields) {
		this.fileHeaderContainsFields = fileHeaderContainsFields;
	}

	/**
	 * @param fieldSeparator
	 *            the fieldSeparator to set
	 */
	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}


	
	/**
	 * 
	 * @param fileFooterEnable
	 */
	public void setFileFooterEnable(boolean fileFooterEnable) {
		this.fileFooterEnable = fileFooterEnable;
	}

	
	@Column(name = "ISFILEHEADERSUMMARYENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileHeaderSummaryEnable() {
		return fileHeaderSummaryEnable;
	}

	public void setFileHeaderSummaryEnable(boolean fileHeaderSummaryEnable) {
		this.fileHeaderSummaryEnable = fileHeaderSummaryEnable;
	}

	
	@Column(name = "ISFILEFOOTERSUMMARYENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileFooterSummaryEnable() {
		return fileFooterSummaryEnable;
	}

	public void setFileFooterSummaryEnable(boolean fileFooterSummaryEnable) {
		this.fileFooterSummaryEnable = fileFooterSummaryEnable;
	}

	@XmlElement
	@Column(name = "FILEHEADERSUMMARY", nullable = true, length = 4000)
	public String getFileHeaderSummary() {
		return fileHeaderSummary;
	}

	public void setFileHeaderSummary(String fileHeaderSummary) {
		this.fileHeaderSummary = fileHeaderSummary;
	}

	@XmlElement
	@Column(name = "FILEFOOTERSUMMARY", nullable = true, length = 4000)
	public String getFileFooterSummary() {
		return fileFooterSummary;
	}

	public void setFileFooterSummary(String fileFooterSummary) {
		this.fileFooterSummary = fileFooterSummary;
	}
}
