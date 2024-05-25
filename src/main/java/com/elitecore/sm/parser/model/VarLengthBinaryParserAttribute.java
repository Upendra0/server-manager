package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;

/**
 * 
 * @author hardik.loriya
 *
 */
@Entity
@DiscriminatorValue("VarLengthBinaryParserAttribute")
@DynamicUpdate
public class VarLengthBinaryParserAttribute extends ParserAttribute{

	
	private static final long serialVersionUID = 8051604319470628314L;
	
	private String sourceFieldName;
	private String prefix;
	private String postfix;
	private String rightDelimiter;
	private String length;
	private int startLength;
	private int endLength;
	private ASN1ATTRTYPE attrType = ASN1ATTRTYPE.ATTRIBUTE;
	
	/**
	 * 
	 * @return sourceFieldName
	 */
	@XmlElement
	@Column(name = "SRCFIELDNAME", nullable = true, length = 200, unique = false)
	public String getSourceFieldName() {
		return sourceFieldName;
	}

	public void setSourceFieldName(String sourceFieldName) {
		this.sourceFieldName = sourceFieldName;
	}

	/**
	 * @return the prefix
	 */
	@XmlElement
	@Column(name = "PREFIX", nullable = true, length = 30)
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the postfix
	 */
	@XmlElement
	@Column(name = "POSTFIX", nullable = true, length = 30)
	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	/**
	 * @return the rightDelimiter
	 */
	@XmlElement
	@Column(name = "RIGHTDELIMITER", nullable = true, length = 20)
	public String getRightDelimiter() {
		return rightDelimiter;
	}

	public void setRightDelimiter(String rightDelimiter) {
		this.rightDelimiter = rightDelimiter;
	}

	/**
	 * @return the length
	 */
	@XmlElement
	@Column(name = "LENGTH", nullable = true, length = 50)
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
	/**
	 * @return the startLength
	 */
	@XmlElement
	@Column(name = "STARTLENGTH", nullable = true, length = 30)
	public int getStartLength() {
		return startLength;
	}

	public void setStartLength(int startLength) {
		this.startLength = startLength;
	}
	
	/**
	 * @return the endLength
	 */
	@XmlElement
	@Column(name = "ENDLENGTH", nullable = true, length = 30)
	public int getEndLength() {
		return endLength;
	}
	
	public void setEndLength(int endLength) {
		this.endLength = endLength;
	}
	
	/**
	 * @return the attrType
	 */
	@XmlElement
	@Enumerated  (EnumType.STRING)
	@Column(name = "ATTRTYPE", nullable = true, length = 50)	
	public ASN1ATTRTYPE getAttrType() {
		return attrType;
	}
	
	public void setAttrType(ASN1ATTRTYPE attrType) {
		this.attrType = attrType;
	}
	
}
