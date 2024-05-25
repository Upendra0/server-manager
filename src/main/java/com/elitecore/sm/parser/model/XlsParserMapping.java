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
 * @author avani.panchal
 *
 */
@Component(value="xslParser")
@Entity
@DiscriminatorValue(EngineConstants.XLS_PARSING_PLUGIN)
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
public class XlsParserMapping extends ParserMapping {

	/**
	 * 
	 */

	private boolean recordWiseExcelFormat;
	private boolean isFileParsed;
	
	
	@Column(name = "RECORDWISEEXCELFORMAT", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getRecordWiseExcelFormat() {
		return recordWiseExcelFormat;
	}
	
	public void setRecordWiseExcelFormat(boolean recordWiseExcelFormat) {
		this.recordWiseExcelFormat = recordWiseExcelFormat;
	}
	
	@Column(name = "FILEPARSED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getIsFileParsed() {
		return isFileParsed;
	}
	
	public void setIsFileParsed(boolean isFileParser) {
		this.isFileParsed = isFileParser;
	}
	
	
}	
