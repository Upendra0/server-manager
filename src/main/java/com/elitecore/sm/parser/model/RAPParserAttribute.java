package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.SourceFieldDataFormatEnum;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Component(value="RAPParserAttribute")
@Entity
@DynamicUpdate
public class RAPParserAttribute extends ParserAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1284453441438035741L;
	
	private ASN1ATTRTYPE attrType = ASN1ATTRTYPE.ATTRIBUTE;
	private String ASN1DataType;
	private SourceFieldDataFormatEnum srcDataFormat;
	private String childAttributes;
	private String recordInitilializer;
	private String unifiedFieldHoldsChoiceId;
	private String parseAsJson;
	/**
	 * @return the attrType
	 */
	@XmlElement
	@Enumerated  (EnumType.STRING)
	@Column(name = "ATTRTYPE", nullable = true, length = 50)	
	public ASN1ATTRTYPE getAttrType() {
		return attrType;
	}

	/**
	 * @return the aSN1DataType
	 */
	@Column(name = "ASN1DATATYPE", nullable = true, length = 500)
	@XmlElement
	public String getASN1DataType() {
		return ASN1DataType;
	}

	/**
	 * @return the srcDataFormat
	 */
	@Column(name = "SRCDATAFORMAT", nullable = true, length = 500)
	@XmlElement
	@Enumerated(EnumType.STRING)
	public SourceFieldDataFormatEnum getSrcDataFormat() {
		return srcDataFormat;
	}

	/**
	 * @return the childAttributes
	 */
	@Column(name = "CHILDATTR", nullable = true, length = 500)
	@XmlElement
	public String getChildAttributes() {
		return childAttributes;
	}

	/**
	 * @return the recordInitilializer
	 */
	@Column(name = "RECINITIALIZER", nullable = true, length = 500)
	@XmlElement
	public String isRecordInitilializer() {
		return recordInitilializer;
	}

	/**
	 * @param attrType
	 *            the attrType to set
	 */
	public void setAttrType(ASN1ATTRTYPE attrType) {
		this.attrType = attrType;
	}

	/**
	 * @param aSN1DataType
	 *            the aSN1DataType to set
	 */
	public void setASN1DataType(String aSN1DataType) {
		ASN1DataType = aSN1DataType;
	}

	/**
	 * @param srcDataFormat
	 *            the srcDataFormat to set
	 */
	public void setSrcDataFormat(SourceFieldDataFormatEnum srcDataFormat) {
		this.srcDataFormat = srcDataFormat;
	}

	/**
	 * @param childAttributes
	 *            the childAttributes to set
	 */
	public void setChildAttributes(String childAttributes) {
		this.childAttributes = childAttributes;
	}

	/**
	 * @param recordInitilializer
	 *            the recordInitilializer to set
	 */
	public void setRecordInitilializer(String recordInitilializer) {
		this.recordInitilializer = recordInitilializer;
	}
	
	
	@XmlElement
	@Column(name = "UNIFIEDFIELD_CHOICEID_HOLDER", length = 100, unique = false)
	public String getUnifiedFieldHoldsChoiceId() {
		return unifiedFieldHoldsChoiceId;
	}

	public void setUnifiedFieldHoldsChoiceId(String unifiedFieldHoldsChoiceId) {
		this.unifiedFieldHoldsChoiceId = unifiedFieldHoldsChoiceId;
	}
	/**
	 * @return the parseAsJson
	 */
	@Column(name = "PARSEASJSON", nullable = true, length = 500)
	@XmlElement
	public String isParseAsJson() {
		return parseAsJson;
	}
	/**
	 * @param parseAsJson
	 *            the parseAsJson to set
	 */
	public void setParseAsJson(String parseAsJson) {
		this.parseAsJson = parseAsJson;
	}

}
