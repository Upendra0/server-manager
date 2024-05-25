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

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;

/**
 * @author vandana.awatramani
 *
 */

@Entity
@Component(value="asciiComposerAttribute")
@DiscriminatorValue("ASCIIComposerAtrribute")
@DynamicUpdate
public class ASCIIComposerAttr extends ComposerAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4436773343341863660L;

	private String replaceConditionList="";

	private boolean paddingEnable=false;
	private int length;

	private PositionEnum paddingType = PositionEnum.LEFT;
	private String paddingChar;
	private String prefix;
	private String suffix;

	/**
	 * @return the replaceConditionList
	 */
	@Column(name = "REPLACECONDITIONLIST", nullable = true, length = 100)
	@XmlElement
	public String getReplaceConditionList() {
		return replaceConditionList;
	}

	/**
	 * @return the paddingEnable
	 */
	@Column(name = "PADDINGENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isPaddingEnable() {
		return paddingEnable;
	}

	/**
	 * @return the length
	 */

	@Column(name = "LENGTH", nullable = true, length = 3)
	@XmlElement
	public int getLength() {
		return length;
	}

	/**
	 * @return the paddingType
	 */
	@Column(name = "PADDINGTYPE", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	@XmlElement
	public PositionEnum getPaddingType() {
		return paddingType;
	}

	/**
	 * @return the paddingChar
	 */
	@Column(name = "PADDINGCHAR", nullable = true, length = 100)
	@XmlElement
	public String getPaddingChar() {
		return paddingChar;
	}

	/**
	 * @return the prefix
	 */
	@Column(name = "PREFIX", nullable = true, length = 100)
	@XmlElement
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the suffix
	 */
	@Column(name = "SUFFIX", nullable = true, length = 100)
	@XmlElement
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param replaceConditionList
	 *            the replaceConditionList to set
	 */
	public void setReplaceConditionList(String replaceConditionList) {
		this.replaceConditionList = replaceConditionList;
	}

	/**
	 * @param paddingEnable
	 *            the paddingEnable to set
	 */
	public void setPaddingEnable(boolean paddingEnable) {
		this.paddingEnable = paddingEnable;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param paddingType
	 *            the paddingType to set
	 */
	public void setPaddingType(PositionEnum paddingType) {
		this.paddingType = paddingType;
	}

	/**
	 * @param paddingChar
	 *            the paddingChar to set
	 */
	public void setPaddingChar(String paddingChar) {
		this.paddingChar = paddingChar;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
