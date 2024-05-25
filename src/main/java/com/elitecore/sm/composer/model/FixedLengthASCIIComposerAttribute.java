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

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;

/**
 * @author vandana.awatramani
 *
 */
@Component
@Entity
@DiscriminatorValue("FixedLengthAsciiComposer")
public class FixedLengthASCIIComposerAttribute extends ComposerAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7888822275406205980L;
	
	private PositionEnum paddingType = PositionEnum.LEFT;
	
	private String prefix;
	
	private String suffix;
	
	private String paddingChar;

	private String fixedLengthDateFormat;
	
	private int fixedLength;
	
	@Column(name = "PADDINGTYPE", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	@XmlElement
	public PositionEnum getPaddingType() {
		return paddingType;
	}

	public void setPaddingType(PositionEnum paddingType) {
		this.paddingType = paddingType;
	}

	@Column(name = "PREFIX", nullable = true, length = 100)
	@XmlElement
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name = "SUFFIX", nullable = true, length = 100)
	@XmlElement
	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Column(name = "PADDINGCHAR", nullable = true, length = 100)
	@XmlElement
	public String getPaddingChar() {
		return paddingChar;
	}

	public void setPaddingChar(String paddingChar) {
		this.paddingChar = paddingChar;
	}

	@XmlElement
	@Column(name = "FIXEDDATEFORMAT", nullable = true, length = 100)
	public String getFixedLengthDateFormat() {
		return fixedLengthDateFormat;
	}

	public void setFixedLengthDateFormat(String fixedLengthDateFormat) {
		this.fixedLengthDateFormat = fixedLengthDateFormat;
	}

	@Column(name = "LENGTH", nullable = true, length = 3)
	@XmlElement
	public int getFixedLength() {
		return fixedLength;
	}

	public void setFixedLength(int fixedLength) {
		this.fixedLength = fixedLength;
	}

	
}
