/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.model.FileFetchTypeEnum;

/**
 * @author vandana.awatramani
 *
 */
@Embeddable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "fileFetchRuleEnabled", "fileFetchType","fileFetchIntervalMin","timeZone"})
public class FileFetchParams implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -442324261658845036L;
	private boolean fileFetchRuleEnabled = false;
	private String fileFetchType = FileFetchTypeEnum.LOCAL.getValue();
	private int fileFetchIntervalMin =  10;
	
	private String timeZone = "GMT+05:30";

	/**
	 * @return the fileFetchRuleEnabled
	 */
	@Column(name = "FILEFETCHRULENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileFetchRuleEnabled() {
		return fileFetchRuleEnabled;
	}

	/**
	 * @return the fileFetchType
	 */
	@Column(name = "FILEFETCHTYPE", nullable = true, length = 50)
	@XmlElement
	public String getFileFetchType() {
		return fileFetchType;
	}

	/**
	 * @return the fileFetchIntervalMin
	 */
	@Column(name = "FILEFFETCHINTERVAL", nullable = true, length = 3)
	@XmlElement
	public int getFileFetchIntervalMin() {
		return fileFetchIntervalMin;
	}

	/**
	 * @return the timeZone
	 */
	@Column(name = "TIMEZONE", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param fileFetchRuleEnabled
	 *            the fileFetchRuleEnabled to set
	 */
	public void setFileFetchRuleEnabled(boolean fileFetchRuleEnabled) {
		this.fileFetchRuleEnabled = fileFetchRuleEnabled;
	}

	/**
	 * @param fileFetchType
	 *            the fileFetchType to set
	 */
	public void setFileFetchType(String fileFetchType) {
		this.fileFetchType = fileFetchType;
	}

	/**
	 * @param fileFetchIntervalMin
	 *            the fileFetchIntervalMin to set
	 */
	public void setFileFetchIntervalMin(int fileFetchIntervalMin) {
		this.fileFetchIntervalMin = fileFetchIntervalMin;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

}
