/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author jay.shah
 * 
 */
@Entity()
@Table(name = "TBLTSERVICESCHEDULINGPARAM")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="schedulingParameterCache")
@XmlType(propOrder = { "id","schedulingEnabled","schType", "date","day","time"})
public class ServiceSchedulingParams extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6554298855452202187L;
	private int id;
	private boolean schedulingEnabled = false;
	private String schType= SchedulingTypeEnum.Daily.getValue();
	private String date = SchedulingDateEnum.First.getNumVal();
	private String day = SchedulingDayEnum.Monday.getDayVal();
	private String time="23:55:00";

	/**
	 * @param schedulingEnabled
	 * @param schType
	 * @param date
	 * @param day
	 * @param time
	 * @param service
	 */
	public ServiceSchedulingParams(String schType, String date, String day, String time) {
		super();
		this.schedulingEnabled = false;
		this.schType = schType;
		this.date = date;
		this.day = day;
		this.time = time;

	}

	public ServiceSchedulingParams() {
		// default constructor for hibernate
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ServiceSchedulingParams",allocationSize=1)
	@XmlElement
	@Column(name="ID")
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int scheduleParamId) {
		this.id = scheduleParamId;
	}

	/**
	 * @return the schedulingEnabled
	 */
	@Column(name = "SCHEDULINGENABLED", nullable = false, length = 10)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isSchedulingEnabled() {
		return schedulingEnabled;
	}

	/**
	 * @return the schType
	 */
	@Column(name = "SCHEDULINGTYPE", nullable = false, length = 10)
	@XmlElement
	public String getSchType() {
		return schType;
	}

	/**
	 * @return the date
	 */
	@Column(name = "SCHEDULINGDATE", nullable = true, length = 15)
	@XmlElement
	public String getDate() {
		return date;
	}

	/**
	 * @return the day
	 */
	@Column(name = "SCHEDULINGDAY", nullable = true, length = 15)
	@XmlElement
	public String getDay() {
		return day;
	}

	/**
	 * @return the time
	 */
	@Column(name = "SCHEDULINGTIME", nullable = true, length = 15)
	@XmlElement(nillable=true)
	public String getTime() {
		return time;
	}

	/**
	 * @param schedulingEnabled
	 *            the schedulingEnabled to set
	 */
	public void setSchedulingEnabled(boolean schedulingEnabled) {
		this.schedulingEnabled = schedulingEnabled;
	}

	/**
	 * @param schType
	 *            the schType to set
	 */
	public void setSchType(String type) {
		this.schType = type;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @param day
	 *            the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

}
