/**
 * 
 */
package com.elitecore.sm.parser.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="ASN1ParserMapping")
@Entity
@DiscriminatorValue(EngineConstants.ASN1_PARSING_PLUGIN)
@XmlRootElement
public class ASN1ParserMapping extends ParserMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4591616708918313073L;
	private String recMainAttribute;
	private boolean removeAddByte;
	private int headerOffset;
	private int recOffset;
	private boolean removeFillers;
	private boolean removeAddHeaderFooter;
	private String recordStartIds;
	boolean skipAttributeMapping;
	private String rootNodeName;
	private String decodeFormat = "XML";
	private int bufferSize = 1024;
	// hibernate cannot distinguish between different association with same
	// class so added attt type to class.
	
	/**
	 * @return the recMainAttribute
	 */
	@XmlElement
	@Column(name = "RECMAINATTR", nullable = true, length = 500)
	public String getRecMainAttribute() {
		return recMainAttribute;
	}

	/**
	 * @return the removeAddByte
	 */
	@XmlElement
	@Column(name = "REMOVEADDBYTE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isRemoveAddByte() {
		return removeAddByte;
	}

	/**
	 * @return the headerOffset
	 */
	@XmlElement
	@Column(name = "HEADEROFFSET", nullable = true, length = 50)
	public int getHeaderOffset() {
		return headerOffset;
	}

	/**
	 * @return the recOffset
	 */
	@XmlElement
	@Column(name = "RECOFFSET", nullable = true, length = 50)
	public int getRecOffset() {
		return recOffset;
	}

	/**
	 * @return the removeFillers
	 */
	@XmlElement
	@Column(name = "REMOVEFILLERS", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getRemoveFillers() {
		return removeFillers;
	}
	
	/**
	 * @return the recordStartIds
	 */
	@XmlElement
	@Column(name = "RECORDSTARTIDS", nullable = true, length = 100)
	public String getRecordStartIds() {
		return recordStartIds;
	}
	
	/**
	 * @param recMainAttribute
	 *            the recMainAttribute to set
	 */
	public void setRecMainAttribute(String recMainAttribute) {
		this.recMainAttribute = recMainAttribute;
	}

	/**
	 * @param removeAddByte
	 *            the removeAddByte to set
	 */
	public void setRemoveAddByte(boolean removeAddByte) {
		this.removeAddByte = removeAddByte;
	}

	/**
	 * @param headerOffset
	 *            the headerOffset to set
	 */
	public void setHeaderOffset(int headerOffset) {
		this.headerOffset = headerOffset;
	}

	/**
	 * @param recOffset
	 *            the recOffset to set
	 */
	public void setRecOffset(int recOffset) {
		this.recOffset = recOffset;
	}
	
	/**
	 * @param removeFillers
	 *            the removeFillers to set
	 */
	public void setRemoveFillers(boolean removeFillers) {
		this.removeFillers =  removeFillers;
	}
	
	/**
	 * @param recordStartIds
	 *            the recordStartIds to set
	 */
	public void setRecordStartIds(String recordStartIds) {
		this.recordStartIds =  recordStartIds;
	}
	
	/** 
	 * 
	 * @return removeAddHeaderFooter
	 */
	@XmlElement
	@Column(name = "REMOVEADDHEADERFOOTER", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isRemoveAddHeaderFooter() {
		return removeAddHeaderFooter;
	}

	/** 
	 * 
	 * @param removeAddHeaderFooter
	 */
	public void setRemoveAddHeaderFooter(boolean removeAddHeaderFooter) {
		this.removeAddHeaderFooter = removeAddHeaderFooter;
	}
	
	
	/** 
	 * 
	 * @return skipAttributeMapping
	 */
	@XmlElement
	@Column(name = "SKIPATTRIBUTEMAPPING", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSkipAttributeMapping() {
		return skipAttributeMapping;
	}

	/** 
	 * 
	 * @param skipAttributeMapping
	 */
	public void setSkipAttributeMapping(boolean skipAttributeMapping) {
		this.skipAttributeMapping = skipAttributeMapping;
	}
	
	/**
	 * @return the rootNodeName
	 */
	@XmlElement
	@Column(name = "ROOTNODENAME", nullable = true, length = 100)
	public String getRootNodeName() {
		return rootNodeName;
	}
	
	/**
	 * @param rootNodeName
	 *            the rootNodeName to set
	 */
	public void setRootNodeName(String rootNodeName) {
		this.rootNodeName =  rootNodeName;
	}
	
	/**
	 * @return the decodeFormat
	 */
	@XmlElement
	@Column(name = "DECODETYPE", nullable = true, length = 100)
	public String getDecodeFormat() {
		return decodeFormat;
	}
	
	/**
	 * @param decodeFormat
	 *            the decodeFormat to set
	 */
	public void setDecodeFormat(String decodeFormat) {
		this.decodeFormat =  decodeFormat;
	}
	
	
	/**
	 * @return the bufferSize
	 */
	@XmlElement
	@Column(name = "BUFFERSIZE", nullable = true, length = 50)
	public int getBufferSize() {
		return bufferSize;
	}
	
	/**
	 * @param bufferSize
	 *            the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all header attributes.
	 * 
	 * @return list of ASN1ParserAttributes
	 */
	public List<ASN1ParserAttribute> loadHeaderAttributeList() {
		List<ASN1ParserAttribute> headerList = new ArrayList<>();
		
		/*for (ParserAttribute parserattr : getParserAttributes()) {
			ASN1ParserAttribute attr=(ASN1ParserAttribute)parserattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.HEADER)) {
				headerList.add(attr);
			}
		}*/
		return headerList;
	}

	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all  attributes.
	 * 
	 * @return list of ASN1ParserAttributes
	 */
	public List<ASN1ParserAttribute> loadAttributeList() {
		List<ASN1ParserAttribute> attrList = new ArrayList<>();
	/*	for (ParserAttribute parserattr : getParserAttributes()) {
			ASN1ParserAttribute attr=(ASN1ParserAttribute)parserattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.ATTRIBUTE)) {
				attrList.add(attr);
			}
		}*/
		return attrList;
	}
	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all trailer attributes.
	 * 
	 * @return list of ASN1ParserAttributes
	 */

	public List<ASN1ParserAttribute> loadTrailerAttributeList() {
		List<ASN1ParserAttribute> trailerAttrList = new ArrayList<>();
		/*for (ParserAttribute parserattr : getParserAttributes()) {
			ASN1ParserAttribute attr=(ASN1ParserAttribute)parserattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.TRAILER)) {
				trailerAttrList.add(attr);
			}
		}*/
		return trailerAttrList;
	}
}
