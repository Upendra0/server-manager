package com.elitecore.sm.common.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("sequenceMgmt")
@Entity
@Table(name = "TBLTSEQUENCEMGMT")
@DynamicUpdate
@Scope(value = "prototype")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cacheable
@XmlType(propOrder = {"id","startRange","endRange","nextValue","resetFrequency","paddingEnable"})

public class SequenceManagement extends BaseModel {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int startRange = 0;
	private int endRange = 0;
	private int nextValue = 0;
	private String resetFrequency = ControlFileResetFrequency.DEFAULT.getValue();
	private Date lastsequpdatedDate = new Date();
	private boolean paddingEnable = true;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SequenceMgmt",allocationSize=1)
	@XmlElement 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "STARTRANGE", nullable = false)
	@XmlElement
	public int getStartRange() {
		return startRange;
	}
	public void setStartRange(int startRange) {
		this.startRange = startRange;
	}
	
	@Column(name = "ENDRANGE", nullable = false)
	@XmlElement
	public int getEndRange() {
		return endRange;
	}
	public void setEndRange(int endRange) {
		this.endRange = endRange;
	}
	
	@Column(name = "NEXTVALUE", nullable = false)
	@XmlElement
	public int getNextValue() {
		return nextValue;
	}
	public void setNextValue(int nextValue) {
		this.nextValue = nextValue;
	}
	
	@Column(name = "RESETFREQUENCY", nullable = false)
	@XmlElement
	public String getResetFrequency() {
		return resetFrequency;
	}
	public void setResetFrequency(String resetFrequency) {
		this.resetFrequency = resetFrequency;
	}
	
	@Column(name = "LASTSEQUPDATEDDATE", nullable = false)
	@Type(type = "timestamp")
	@XmlTransient
	public Date getLastsequpdatedDate() {
		return lastsequpdatedDate;
	}
	public void setLastsequpdatedDate(Date lastsequpdatedDate) {
		this.lastsequpdatedDate = lastsequpdatedDate;
	}
	
	@Column(name = "PADDINGENABLED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isPaddingEnable() {
		return paddingEnable;
	}
	public void setPaddingEnable(boolean paddingEnable) {
		this.paddingEnable = paddingEnable;
	}
}
