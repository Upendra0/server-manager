/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.stereotype.Component;

import com.elitecore.sm.parser.model.ASN1ATTRTYPE;

/**
 * @author jay.shah
 *
 */
@Entity
@Component(value="asn1ComposerAttribute")
@DiscriminatorValue("ASN1ComposerAttribute")
@XmlType(propOrder = { "argumentDataType", "attrType", "childAttributes", "choiceId", "destFieldDataFormat", "asn1DataType"})
public class ASN1ComposerAttribute extends ComposerAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7515797564897948068L;
	
	private String asn1DataType;
	private String argumentDataType;
	private String destFieldDataFormat;
	private String choiceId;
	private String childAttributes;
	private ASN1ATTRTYPE attrType = ASN1ATTRTYPE.ATTRIBUTE;

	/**
	 * @return the asn1DataType
	 */
	@Column(name = "ASN1DATATYPE")
	public String getasn1DataType() {
		return asn1DataType;
	}

	/**
	 * @return the argumentDataType
	 */
	@Column(name = "ARGUMENTDATATYPE")
	public String getArgumentDataType() {
		return argumentDataType;
	}

	/**
	 * @return the destFieldDataFormat
	 */
	@Column(name = "DESTFIELDDATAFORMAT")
	public String getDestFieldDataFormat() {
		return destFieldDataFormat;
	}

	/**
	 * @return the choiceId
	 */
	@Column(name = "CHOICEID")
	public String getChoiceId() {
		return choiceId;
	}

	/**
	 * @return the childAttributes
	 */
	@Column(name = "CHILDATTRIBUTES")
	public String getChildAttributes() {
		return childAttributes;
	}

	
	/**
	 * @return the attrtype
	 */
	@XmlElement
	@Enumerated  (EnumType.STRING)
	@Column(name = "ATTRTYPE", nullable = true, length = 50)
	public ASN1ATTRTYPE getAttrType() {
		return attrType;
	}


	/**
	 * @param aSN1DataType
	 *            the aSN1DataType to set
	 */
	public void setasn1DataType(String asn1DataType) {
		this.asn1DataType = asn1DataType;
	}

	/**
	 * @param argumentDataType
	 *            the argumentDataType to set
	 */
	public void setArgumentDataType(String argumentDataType) {
		this.argumentDataType = argumentDataType;
	}

	/**
	 * @param destFieldDataFormat
	 *            the destFieldDataFormat to set
	 */
	public void setDestFieldDataFormat(String destFieldDataFormat) {
		this.destFieldDataFormat = destFieldDataFormat;
	}

	/**
	 * @param choiceId
	 *            the choiceId to set
	 */
	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	/**
	 * @param childAttributes
	 *            the childAttributes to set
	 */
	public void setChildAttributes(String childAttributes) {
		this.childAttributes = childAttributes;
	}
	/**
	 * @param attrtype the attrtype to set
	 */
	public void setAttrType(ASN1ATTRTYPE attrtype) {
		this.attrType = attrtype;
	}
	
}
