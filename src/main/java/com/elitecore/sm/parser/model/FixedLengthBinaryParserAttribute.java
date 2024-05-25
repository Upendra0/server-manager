package com.elitecore.sm.parser.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Entity
@DiscriminatorValue("FixedLengthBinaryParserAttribute")
public class FixedLengthBinaryParserAttribute extends ParserAttribute {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6047418007752913505L;
	private int startLength;
	private int endLength;
	private String prefix;
	private String postfix;
	private String length;
	private String rightDelimiter;
	private int bitStartLength;
	private int bitEndLength;
	private boolean readAsBits;
	private boolean multiRecord;

	/**
	 * @return the startLength
	 */
	@XmlElement
	@Column(name = "STARTLENGTH", nullable = true, length = 30)
	public int getStartLength() {
		return startLength;
	}

	/**
	 * @return the endLength
	 */
	@XmlElement
	@Column(name = "ENDLENGTH", nullable = true, length = 30)
	public int getEndLength() {
		return endLength;
	}

	/**
	 * @return the prefix
	 */
	@XmlElement
	@Column(name = "PREFIX", nullable = true, length = 30)
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the postfix
	 */
	@XmlElement
	@Column(name = "POSTFIX", nullable = true, length = 30)
	public String getPostfix() {
		return postfix;
	}

	/**
	 * @return the length
	 */
	@XmlElement
	@Column(name = "LENGTH", nullable = true, length = 50)
	public String getLength() {
		return length;
	}
	
	/**
	 * @return the rightDelimiter
	 */
	@XmlTransient
	@Column(name = "RIGHTDELIMITER", nullable = true, length = 20)
	public String getRightDelimiter() {
		return rightDelimiter;
	}

	/**
	 * @param startLength
	 *            the startLength to set
	 */
	public void setStartLength(int startLength) {
		this.startLength = startLength;
	}
	
	/**
	 * @param endLength
	 *            the endLength to set
	 */
	public void setEndLength(int endLength) {
		this.endLength = endLength;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param postfix
	 *            the postfix to set
	 */
	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @param rightDelimiter
	 *            the rightDelimiter to set
	 */
	public void setRightDelimiter(String rightDelimiter) {
		this.rightDelimiter = rightDelimiter;
	}

	/**
	 * @return the bitStartLength
	 */
	@XmlElement
	@Column(name = "BITSTARTLENGTH", nullable = true, length = 30)
	public int getBitStartLength() {
		return bitStartLength;
	}

	/**
	 * @return the bitEndLength
	 */
	@XmlElement
	@Column(name = "BITENDLENGTH", nullable = true, length = 30)
	public int getBitEndLength() {
		return bitEndLength;
	}

	/**
	 * @param bitStartLength
	 *            the bitStartLength to set
	 */
	public void setBitStartLength(int bitStartLength) {
		this.bitStartLength = bitStartLength;
	}
	
	/**
	 * @param bitEndLength
	 *            the bitEndLength to set
	 */
	public void setBitEndLength(int bitEndLength) {
		this.bitEndLength = bitEndLength;
	}
	
	/**
	 * @return the readAsBits
	 */
	@XmlElement
	@Column(name = "READASBITS", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isReadAsBits() {
		return readAsBits;
	}
	
	/**
	 * @param readAsBits
	 *            the readAsBits to set
	 */
	public void setReadAsBits(boolean readAsBits) {
		this.readAsBits = readAsBits;
	}

	@XmlElement
	@Column(name = "MULTIRECORD", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isMultiRecord() {
		return multiRecord;
	}

	public void setMultiRecord(boolean multiRecord) {
		this.multiRecord = multiRecord;
	}

	
}
