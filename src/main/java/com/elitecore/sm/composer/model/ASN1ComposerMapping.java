/**
 * 
 */
package com.elitecore.sm.composer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.parser.model.ASN1ATTRTYPE;

/**
 * @author vandana.awatramani
 *
 */
@Component
@Entity
@DiscriminatorValue("ASN1ComposerMapping")
public class ASN1ComposerMapping extends ComposerMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5679585744726606089L;
	private String recMainAttribute;
	private String startFormat;
	private String multiContainerDelimiter;

	/**
	 * @return the recMainAttribute
	 */
	@XmlElement
	@Column(name = "RECMAINATTR", nullable = true, length = 500)
	public String getRecMainAttribute() {
		return recMainAttribute;
	}

	/**
	 * @return the startFormat
	 */
	@XmlElement
	@Column(name = "STARTFORMAT", nullable = true, length = 500)
	public String getStartFormat() {
		return startFormat;
	}

	@XmlElement
	@Column(name = "MULTICONTAINERDELIMITER", nullable = true, length = 500)
	public String getMultiContainerDelimiter() {
		return multiContainerDelimiter;
	}

	/**
	 * @param recMainAttribute
	 *            the recMainAttribute to set
	 */
	public void setRecMainAttribute(String recMainAttribute) {
		this.recMainAttribute = recMainAttribute;
	}

	/**
	 * @param startFormat
	 *            the startFormat to set
	 */
	public void setStartFormat(String startFormat) {
		this.startFormat = startFormat;
	}

	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all header attributes.
	 * 
	 * @return list of ASN1ComposerAttributes
	 */
	public List<ASN1ComposerAttribute> loadHeaderAttributeList() {
		List<ASN1ComposerAttribute> headerList = new ArrayList<>();
		for (ComposerAttribute composerattr : getAttributeList()) {
			ASN1ComposerAttribute attr=(ASN1ComposerAttribute) composerattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.HEADER)) {
				headerList.add(attr);
			}
		}
		return headerList;
	}

	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all  attributes.
	 * 
	 * @return list of ASN1ComposerAttributes
	 */
	public List<ASN1ComposerAttribute> loadAttributeList() {
		List<ASN1ComposerAttribute> attrList = new ArrayList<>();
		for (ComposerAttribute composerattr : getAttributeList()) {
			ASN1ComposerAttribute attr=(ASN1ComposerAttribute) composerattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.ATTRIBUTE)) {
				attrList.add(attr);
			}
		}
		return attrList;
	}
	/**
	 * This is utility method to classify, attributes to different lists as per
	 * their type. This method returns list of all trailer attributes.
	 * 
	 * @return list of ASN1ComposerAttributes
	 */

	public List<ASN1ComposerAttribute> loadTrailerAttributeList() {
		List<ASN1ComposerAttribute> trailerAttrList = new ArrayList<>();
		for (ComposerAttribute composerattr : getAttributeList()) {
			ASN1ComposerAttribute attr=(ASN1ComposerAttribute) composerattr;
			if (attr.getAttrType().equals(ASN1ATTRTYPE.TRAILER)) {
				trailerAttrList.add(attr);
			}
		}
		return trailerAttrList;
	}

	public void setMultiContainerDelimiter(String multiContainerDelimiter) {
		this.multiContainerDelimiter = multiContainerDelimiter;
	}
	
	
	
	

}
