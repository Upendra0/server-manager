/**
 * 
 */
package com.elitecore.sm.common.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;

import com.elitecore.sm.util.JsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Sunil Gulabani
 * Jul 7, 2015
 */
@MappedSuperclass
public abstract class BaseModel extends ToStringProcessor implements Serializable,Cloneable {

	private static final long serialVersionUID = 1L;
	
	protected Date createdDate = new Date();
	protected int createdByStaffId = 0;
	
	protected Date lastUpdatedDate = new Date();
	protected int lastUpdatedByStaffId = 0;
	
	protected StateEnum status = StateEnum.ACTIVE;

	public BaseModel(){
		
	}
	
	/**
	 * 
	 * @param createdDate
	 * @param createdByStaffId
	 * @param lastUpdatedDate
	 * @param lastUpdatedByStaffId
	 */
	public BaseModel(Date createdDate,int createdByStaffId,Date lastUpdatedDate,int lastUpdatedByStaffId,StateEnum status){
		this.createdDate=createdDate;
		this.createdByStaffId=createdByStaffId;
		this.lastUpdatedDate=lastUpdatedDate;
		this.lastUpdatedByStaffId=lastUpdatedByStaffId;
		this.status=status;
	}
	
	/**
	 * @return the creationTime
	 */
	@Column(name = "CREATEDDATE", nullable = false)
	@Type(type = "timestamp")
	@XmlTransient
	@DiffIgnore
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdByStaff
	 */
	@Column(name = "CREATEDBYSTAFFID", nullable = false, length = 15)
	@DiffIgnore
	@XmlTransient
	public int getCreatedByStaffId() {
		return createdByStaffId;
	}

	/**
	 * @param createdByStaffId
	 *            the createdByStaffId to set
	 */
	public void setCreatedByStaffId(int createdByStaffId) {
		this.createdByStaffId = createdByStaffId;
	}

	/**
	 * @return the lastUpdatedTime
	 */
	@Column(name = "LASTUPDATEDDATE", nullable = false)
	@Type(type = "timestamp")
	@XmlTransient
	@DiffIgnore
	@JsonDeserialize(using=JsonDateDeserializer.class)
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate
	 *            the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	/**
	 * @return the lastUpdatedByStaff
	 */
	@XmlTransient
	@Column(name = "LASTUPDATEDBYSTAFFID", nullable = false, length = 15)
	@DiffIgnore
	public int getLastUpdatedByStaffId() {
		return lastUpdatedByStaffId;
	}

	/**
	 * @param lastUpdatedByStaff
	 *            the lastUpdatedByStaff to set
	 */
	public void setLastUpdatedByStaffId(int lastUpdatedByStaffId) {
		this.lastUpdatedByStaffId = lastUpdatedByStaffId;
	}
	
	/**
	 * 
	 * @return the status
	 */
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS" ,nullable=false)
	public StateEnum getStatus() {
		return status;
	}

	/**
	 * 
	 * @param state
	 */
	public void setStatus(StateEnum state) {
		this.status = state;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
	
}
