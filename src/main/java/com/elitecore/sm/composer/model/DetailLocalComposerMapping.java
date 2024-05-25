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

import com.elitecore.sm.common.model.CharSetEnum;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue("DetailLocalComposerMapping")
public class DetailLocalComposerMapping extends ComposerMapping {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2552173202593356033L;
	private CharSetEnum srcCharset;
	private String keyValueSeparator;
	private String headerAttributeDateFormat; 

	/**
	 * @return the srcCharset
	 */
	@XmlElement
	@Column(name = "SRCCHARSET", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	public CharSetEnum getSrcCharset() {
		return srcCharset;
	}

	/**
	 * @return the keyValueSeparator
	 */
	@XmlElement
	@Column(name = "KVSEPARATOR", nullable = true, length = 100)
	public String getKeyValueSeparator() {
		return keyValueSeparator;
	}

	/**
	 * @return the headerAttributeDateFormat
	 */
	@XmlElement
	@Column(name = "HEADERATTRDATEFORMAT", nullable = true, length = 100)
	public String getHeaderAttributeDateFormat() {
		return headerAttributeDateFormat;
	}

	/**
	 * @param srcCharset
	 *            the srcCharset to set
	 */
	public void setSrcCharset(CharSetEnum srcCharset) {
		this.srcCharset = srcCharset;
	}

	/**
	 * @param keyValueSeparator
	 *            the keyValueSeparator to set
	 */
	public void setKeyValueSeparator(String keyValueSeparator) {
		this.keyValueSeparator = keyValueSeparator;
	}

	/**
	 * @param headerAttributeDateFormat
	 *            the headerAttributeDateFormat to set
	 */
	public void setHeaderAttributeDateFormat(String headerAttributeDateFormat) {
		this.headerAttributeDateFormat = headerAttributeDateFormat;
	}


}
