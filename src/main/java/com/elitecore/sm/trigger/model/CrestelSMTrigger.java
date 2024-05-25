package com.elitecore.sm.trigger.model;


import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;


@Component(value = "trigger")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLTTRIGGER")
@Cacheable
public class CrestelSMTrigger extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	private int ID;
	private String triggerName;
	private String description;
	private String recurrenceType;
	private Integer alterationCount;
	private String dayOfWeek;
	private Integer dayOfMonth;
	private String firstOrLastDayOfMonth;
	private Integer executionStartingHour;
	private Integer executionStartingMinute;
	private Integer executionEndingHour;
	private Integer executionEndingMinute;
	private String croneExpression;
	private Date startAtDate;
	private Integer startAtHour;
	private Integer startAtMinute;
	private Date endAtDate;
	private Integer endAtHour;
	private Integer endAtMinute;
	private String triggerGroup;
	
	public CrestelSMTrigger(){
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="Trigger",allocationSize=1)
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}

	@Column(name = "TRIGGERNAME", nullable = false, length = 200,  unique = true)
	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	@Column(name = "DESCRIPTION", nullable = true, length = 400)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "RECURRENCETYPE", nullable = false, length = 200)
	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}

	@Column(name = "ALTERATIONCOUNT", nullable = true, length = 10)
	public Integer getAlterationCount() {
		return alterationCount;
	}

	public void setAlterationCount(Integer alterationCount) {
		this.alterationCount = alterationCount;
	}

	@Column(name = "DAYOFWEEK", nullable = true, length = 30)
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Column(name = "DAYOFMONTH", nullable = true, length = 10)
	public Integer getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(Integer dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	@Column(name = "FIRSTORLASTDAYOFMONTH", nullable = true, length = 50)
	public String getFirstOrLastDayOfMonth() {
		return firstOrLastDayOfMonth;
	}

	public void setFirstOrLastDayOfMonth(String firstOrLastDayOfMonth) {
		this.firstOrLastDayOfMonth = firstOrLastDayOfMonth;
	}

	@Column(name = "EXECUTIONSTARTINGHOUR", nullable = true, length = 4)
	public Integer getExecutionStartingHour() {
		return executionStartingHour;
	}

	public void setExecutionStartingHour(Integer executionStartingHour) {
		this.executionStartingHour = executionStartingHour;
	}

	@Column(name = "EXECUTIONSTARTINGMINUTE", nullable = true, length = 4)
	public Integer getExecutionStartingMinute() {
		return executionStartingMinute;
	}

	public void setExecutionStartingMinute(Integer executionStartingMinute) {
		this.executionStartingMinute = executionStartingMinute;
	}

	@Column(name = "EXECUTIONENDINGHOUR", nullable = true, length = 4)
	public Integer getExecutionEndingHour() {
		return executionEndingHour;
	}

	public void setExecutionEndingHour(Integer executionEndingHour) {
		this.executionEndingHour = executionEndingHour;
	}

	@Column(name = "EXECUTIONENDINGMINUTE", nullable = true, length = 4)
	public Integer getExecutionEndingMinute() {
		return executionEndingMinute;
	}

	public void setExecutionEndingMinute(Integer executionEndingMinute) {
		this.executionEndingMinute = executionEndingMinute;
	}

	@Column(name = "CRONEXPRESSION", nullable = false, length = 400)
	public String getCroneExpression() {
		return croneExpression;
	}

	public void setCroneExpression(String croneExpression) {
		this.croneExpression = croneExpression;
	}

	@Column(name = "STARTATDATE", nullable = true)
	@Type(type = "timestamp")
	public Date getStartAtDate() {
		return startAtDate;
	}

	public void setStartAtDate(Date startAtDate) {
		this.startAtDate = startAtDate;
	}

	@Column(name = "STARTATHOUR", nullable = true, length = 4)
	public Integer getStartAtHour() {
		return startAtHour;
	}

	public void setStartAtHour(Integer startAtHour) {
		this.startAtHour = startAtHour;
	}

	@Column(name = "STARTATMINUTE", nullable = true, length = 4)
	public Integer getStartAtMinute() {
		return startAtMinute;
	}

	public void setStartAtMinute(Integer startAtMinute) {
		this.startAtMinute = startAtMinute;
	}

	@Column(name = "ENDATDATE", nullable = true)
	@Type(type = "timestamp")
	public Date getEndAtDate() {
		return endAtDate;
	}

	public void setEndAtDate(Date endAtDate) {
		this.endAtDate = endAtDate;
	}

	@Column(name = "ENDATHOUR", nullable = true, length = 4)
	public Integer getEndAtHour() {
		return endAtHour;
	}

	public void setEndAtHour(Integer endAtHour) {
		this.endAtHour = endAtHour;
	}

	@Column(name = "ENDATMINUTE", nullable = true, length = 4)
	public Integer getEndAtMinute() {
		return endAtMinute;
	}

	public void setEndAtMinute(Integer endAtMinute) {
		this.endAtMinute = endAtMinute;
	}

	@Column(name = "TRIGGERGROUP", nullable = true, length = 200)
	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	
}
