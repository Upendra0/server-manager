package com.elitecore.sm.composer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.springframework.stereotype.Component;

import com.elitecore.sm.parser.model.ASN1ATTRTYPE;

@Component
@Entity
@XmlSeeAlso({TAPComposerAttribute.class,RAPComposerAttribute.class,NRTRDEComposerAttribute.class})
public class RoamingComposerAttribute extends ComposerAttribute{
	
    private static final long serialVersionUID = -7515797564897948069L;
	private String asn1DataType;
	private String argumentDataType;
	private String destFieldDataFormat;
	private String choiceId;
	private String childAttributes;
	private boolean composeFromJsonEnable = false;
	private boolean cloneRecordEnable = false;
	private ASN1ATTRTYPE attrType = ASN1ATTRTYPE.ATTRIBUTE;

	@Column(name = "ASN1DATATYPE")
	public String getasn1DataType() {
		return asn1DataType;
	}

	@Column(name = "ARGUMENTDATATYPE")
	public String getArgumentDataType() {
		return argumentDataType;
	}

	@Column(name = "DESTFIELDDATAFORMAT")
	public String getDestFieldDataFormat() {
		return destFieldDataFormat;
	}

	@Column(name = "CHOICEID")
	public String getChoiceId() {
		return choiceId;
	}

	@Column(name = "CHILDATTRIBUTES")
	public String getChildAttributes() {
		return childAttributes;
	}

	@XmlElement
	@Column(name = "COMPOSEFROMJSONENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isComposeFromJsonEnable() {
		return composeFromJsonEnable;
	}

	@XmlElement
	@Column(name = "CLONERECORDENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isCloneRecordEnable() {
		return cloneRecordEnable;
	}

	@XmlElement
	@Enumerated  (EnumType.STRING)
	@Column(name = "ATTRTYPE", nullable = true, length = 50)
	public ASN1ATTRTYPE getAttrType() {
		return attrType;
	}

	public void setasn1DataType(String asn1DataType) {
		this.asn1DataType = asn1DataType;
	}

	public void setArgumentDataType(String argumentDataType) {
		this.argumentDataType = argumentDataType;
	}

	public void setDestFieldDataFormat(String destFieldDataFormat) {
		this.destFieldDataFormat = destFieldDataFormat;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	public void setChildAttributes(String childAttributes) {
		this.childAttributes = childAttributes;
	}

	public void setComposeFromJsonEnable(boolean composeFromJsonEnable) {
		this.composeFromJsonEnable = composeFromJsonEnable;
	}

	public void setCloneRecordEnable(boolean cloneRecordEnable) {
		this.cloneRecordEnable = cloneRecordEnable;
	}

	public void setAttrType(ASN1ATTRTYPE attrtype) {
		this.attrType = attrtype;
	}
	
}
