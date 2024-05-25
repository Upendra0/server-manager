package com.elitecore.sm.parser.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.parser.model.RegExPattern;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue("RegexParserAtrribute")
@DynamicUpdate
public class RegexParserAttribute extends ParserAttribute {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8339150965324740717L;
	// TODO remove source field from display for this parser config
	private int seqNumber;
	private String regex;
	private RegExPattern pattern;
	private String sampleData;

	/**
	 * @return the seqNumber
	 */
	@XmlElement
	@Column(name = "SEQNUMBER", nullable = true, length = 5)
	public int getSeqNumber() {
		return seqNumber;
	}

	/**
	 * @param seqNumber
	 *            the seqNumber to set
	 */
	public void setSeqNumber(int seqNumber) {
		this.seqNumber = seqNumber;
	}

	/**
	 * 
	 * @return regex
	 */
	@XmlElement
	@Column(name = "REGEX", nullable = true, length = 500)
	public String getRegex() {
		return regex;
	}
	
	/**
	 * @param regex
	 *            the regex to set
	 */
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	/**
	 * 
	 * @return pattern
	 */
	@XmlTransient
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY,optional=true)
	@JoinColumn(name="PATTERNID",foreignKey=@ForeignKey(name = "FK_PARSERATTR_PATTERN_ID"))
	public RegExPattern getPattern() {
		return pattern;
	}

	/**
	 * 
	 * @param pattern
	 */
	public void setPattern(RegExPattern pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * 
	 * @return SampleData
	 */
	@Column(name = "SAMPLEDATA", nullable = true, length = 500)
	public String getSampleData() {
		return sampleData;
	}
	
	/**
	 * 
	 * @param sampleData
	 */
	public void setSampleData(String sampleData) {
		this.sampleData = sampleData;
	}

}
