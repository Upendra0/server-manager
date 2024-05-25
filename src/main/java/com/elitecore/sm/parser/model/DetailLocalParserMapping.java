/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN)
@Component(value="DetailLocalParserMapping") // saumil.vachheta // experiment // based on @Component annotation available in ASCII and ASN1 Parser  
public class DetailLocalParserMapping extends ParserMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8856421355707366470L;
	// TODO check if this cna be enum values ate UTF-8 and UTF-16

//	private String attributeSeperator;
//	
//	private boolean vendorNameSeparatorEnable;
//	private String vendorSeparatorValue;
	private String attributeSeperator="=";
	
	private boolean vendorNameSeparatorEnable;
	private String vendorSeparatorValue=":";

	//private List<ParserAttribute> parserAttributes;


	/**
	 * @return the attributeSeperator
	 */
	@Column(name = "ATTRSEPARATOR", nullable = true, length = 50)
	@XmlElement
	public String getAttributeSeperator() {
		return attributeSeperator;
	}

	

	/**
	 * @return the vendorNameSeperatorEnable
	 */
	@Column(name = "VENDORNAMESEPARATOR", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isVendorNameSeparatorEnable() {
		return vendorNameSeparatorEnable;
	}

	/**
	 * @return the vendorSeperatorValue
	 */
	@Column(name = "VENDORSEPARATORVALUE", nullable = true, length = 100)
	@XmlElement
	public String getVendorSeparatorValue() {
		return vendorSeparatorValue;
	}

	/**
	 * @return the parserAttributes
	 */
	/*@XmlElement
	@OneToMany(mappedBy = "myParserPlugin")
	public List<ParserAttribute> getParserAttributes() {
		return parserAttributes;
	}*/


	

	/**
	 * @param attributeSeperator
	 *            the attributeSeperator to set
	 */
	public void setAttributeSeperator(String attributeSeperator) {
		this.attributeSeperator = attributeSeperator;
	}



	/**
	 * @param vendorNameSeperatorEnable
	 *            the vendorNameSeperatorEnable to set
	 */
	public void setVendorNameSeparatorEnable(boolean vendorNameSeperatorEnable) {
		this.vendorNameSeparatorEnable = vendorNameSeperatorEnable;
	}

	/**
	 * @param vendorSeperatorValue
	 *            the vendorSeperatorValue to set
	 */
	public void setVendorSeparatorValue(String vendorSeperatorValue) {
		this.vendorSeparatorValue = vendorSeperatorValue;
	}

	/**
	 * @param parserAttributes
	 *            the parserAttributes to set
	 */
	/*public void setParserAttributes(List<ParserAttribute> parserAttributes) {
		this.parserAttributes = parserAttributes;
	}*/
	

}
