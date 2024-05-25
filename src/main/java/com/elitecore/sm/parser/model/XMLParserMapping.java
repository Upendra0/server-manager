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
@Entity
@DiscriminatorValue(EngineConstants.XML_PARSING_PLUGIN)
@Component(value="xmlParser")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
public class XMLParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2725177781110304315L;
	private boolean recordWiseXmlFormat;
	private String commonFields;
	
	/**
	 * @return the recordWiseXmlFormat
	 */	
	@Column(name = "RECORDXMLFORMAT", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getRecordWiseXmlFormat() {
		return recordWiseXmlFormat;
	}
	
	/**
	 * @return the commonFields
	 */
	@XmlElement
	@Column(name = "COMMONFIELDS", nullable = true, length = 100)	
	public String getCommonFields() {
		return commonFields;
	}
	
	/**
	 * @param name
	 *            the recordWiseXmlFormat to set
	 */
	public void setRecordWiseXmlFormat(boolean recordWiseXmlFormat) {
		this.recordWiseXmlFormat = recordWiseXmlFormat;
	}
	
	/**
	 * @param name
	 *            the commonFields to set
	 */
	public void setCommonFields(String commonFields) {
		this.commonFields = commonFields;
	}
}