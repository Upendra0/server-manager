package com.elitecore.sm.job.model;


import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;


@Component(value = "job")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLTJOB")
@Cacheable
public class CrestelSMJob extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	private int ID;
	private String jobName;
	private String description;
	private Date lastRunTime;
	private Date nextRunTime;
	private String jobGroup;
	private CrestelSMTrigger triggerId;
	private String parentTrigger;
	private String originalTrigger;
	private String jobType;
	
	public CrestelSMJob(){
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="Job",allocationSize=1)
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
	}

	@Column(name = "JOBNAME", nullable = false, length = 200,  unique = true)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Column(name = "DESCRIPTION", nullable = true, length = 400)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "LASTRUNTIME", nullable = true)
	@Type(type = "timestamp")
	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}
	
	@Column(name = "NEXTRUNTIME", nullable = true)
	@Type(type = "timestamp")
	public Date getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TRIGGERID", nullable = true, foreignKey = @ForeignKey(name = "FK_TBLTTRIGGER_ID"))
	@DiffIgnore
	public CrestelSMTrigger getTrigger() {
		return triggerId;
	}
	
	public void setTrigger(CrestelSMTrigger triggerId) {
		this.triggerId = triggerId;
	}
	
	@Column(name = "JOBGROUP", nullable = true, length = 200)
	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	@Column(name = "PARENTTRIGGER", nullable = true, length = 200)
	public String getParentTrigger() {
		return parentTrigger;
	}

	public void setParentTrigger(String parentTrigger) {
		this.parentTrigger = parentTrigger;
	}
	
	@Column(name = "ORIGINALTRIGGER", nullable = true, length = 200)
	public String getOriginalTrigger() {
		return originalTrigger;
	}

	public void setOriginalTrigger(String originalTrigger) {
		this.originalTrigger = originalTrigger;
	}

	@Column(name = "JOBTYPE", nullable = true, length = 200)
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	@Override
	public String toString() {
		return "CrestelSMJob [ID=" + ID + ", jobName=" + jobName + ", description=" + description + ", lastRunTime="
				+ lastRunTime + ", nextRunTime=" + nextRunTime + ", jobGroup=" + jobGroup + ", triggerId=" + triggerId
				+ ", parentTrigger=" + parentTrigger + ", originalTrigger=" + originalTrigger + ", jobType=" + jobType
				+ "]";
	}
	
}
