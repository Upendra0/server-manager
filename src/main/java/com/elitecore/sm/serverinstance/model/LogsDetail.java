/**
 * 
 */
package com.elitecore.sm.serverinstance.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.ToStringProcessor;

/**
 * @author jay.shah
 * 
 */
@Component(value="logsDetailObject")
@Scope(value="prototype")
@Embeddable
@XmlType(propOrder = { "level","rollingType","logPathLocation","rollingValue","maxRollingUnit"})
public class LogsDetail extends ToStringProcessor implements Serializable {

	private static final long serialVersionUID = -3069559334511108631L;

	private LogLevelEnum level = LogLevelEnum.ALL;

	private String rollingType = LogRollingTypeEnum.TIME_BASED.getValue();

	private int maxRollingUnit = 10;

	private int rollingValue = 5;

	private String logPathLocation;

	/**
	 * @return the level
	 */
	@Column(name = "LOGLEVEL", nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public LogLevelEnum getLevel() {
		return level;
	}

	/**
	 * @return the rollingType
	 */
	/*@Enumerated(EnumType.STRING)*/
	@Column(name = "ROLLINGTYPE", nullable = false, length = 20)
	@XmlElement
	public String getRollingType() {
		return rollingType;
	}

	/**
	 * @return the rollingValue
	 */
	@Column(name = "ROLLINGVALUE", nullable = false, length = 5)
	@XmlElement
	public int getRollingValue() {
		return rollingValue;
	}

	/**
	 * @return the maxRollingUnit
	 */
	@Column(name = "MAXROLLINGUNIT", nullable = false, length = 5)
	@XmlElement
	public int getMaxRollingUnit() {
		return maxRollingUnit;
	}

	/**
	 * @return the logPathLocation
	 */
	@Column(name = "LOGPATHLOCATION", nullable = true, length = 200)
	@XmlElement
	public String getLogPathLocation() {
		return logPathLocation;
	}

	/**
	 * @param logLevel
	 *            the logLevel to set
	 */
	public void setLevel(LogLevelEnum logLevel) {
		this.level = logLevel;
	}

	/**
	 * @param rollingType
	 *            the rollingType to set
	 */
	public void setRollingType(String rollingType) {
		this.rollingType = rollingType;
	}

	/**
	 * @param rollingValue
	 *            the rollingValue to set
	 */
	public void setRollingValue(int rollingValue) {
		this.rollingValue = rollingValue;
	}

	/**
	 * @param maxRollingUnit
	 *            the maxRollingUnit to set
	 */
	public void setMaxRollingUnit(int maxRollingUnit) {
		this.maxRollingUnit = maxRollingUnit;
	}

	/**
	 * @param logPathLocation
	 *            the logPathLocation to set
	 */
	public void setLogPathLocation(String logPathLocation) {
		this.logPathLocation = logPathLocation;
	}
	
}