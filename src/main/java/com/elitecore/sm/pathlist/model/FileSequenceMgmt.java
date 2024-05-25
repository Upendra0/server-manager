/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.roaming.model.Partner;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 
 * 
 * 
 */
@Entity()
@DynamicUpdate
@Table(name = "TBLTFILESEQMGMT")
@Component(value = "missingFileSequence")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@XmlType(propOrder = { "id","elementType","referenceDevice","minValue",
		"maxValue","resetFrequency","missingFileStartIndex","missingFileEndIndex"
		})
public class FileSequenceMgmt extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733404212991596531L;
	private int id;
	private String elementType;
	private String referenceDevice;
	private Device parentDevice;
	private int minValue=0;
	private int maxValue=0;
	private int nextValue=0;
	private String resetFrequency;
	private Date resetTime;
	private int missingFileStartIndex=0;
	private int missingFileEndIndex=0;
	private Partner partner;
	
	public FileSequenceMgmt() {
		
	}
	
	public FileSequenceMgmt(String elementType, int maxValue,String resetFrequency,
			Date lastUpdatedDate,int lastUpdatedByStaffId,String referenceDevice,Partner partner) {
		super.lastUpdatedDate = lastUpdatedDate;
		super.lastUpdatedByStaffId = lastUpdatedByStaffId;
		this.elementType  =elementType;
		this.maxValue = maxValue;
		this.resetFrequency = resetFrequency;
		this.referenceDevice = referenceDevice;
		this.partner = partner;
		
	}
	
	@Id
	@Column(name="ID")
	@XmlElement
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="missingFileSequence",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "TYPE", nullable = false, length = 100)
	@XmlElement
	public String getElementType() {
		return elementType;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	
	@Column(name = "REFERENCEDEVICE", nullable = false, length = 400)
	@XmlElement
	public String getReferenceDevice() {
		return referenceDevice;
	}
	public void setReferenceDevice(String referenceDevice) {
		this.referenceDevice = referenceDevice;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENTDEVICE", nullable = true, foreignKey = @ForeignKey(name = "FK_PARENTDEVICE_ID"))
	@XmlTransient
	@DiffIgnore
	public Device getParentDevice() {
		return parentDevice;
	}
	public void setParentDevice(Device parentDevice) {
		this.parentDevice = parentDevice;
	}
	
	@Column(name = "MINVALUE", nullable = false)
	@XmlElement
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	@Column(name = "MAXVAL", nullable = false)
	@XmlElement
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	
	@Column(name = "NEXTVALUE", nullable = false)
	@XmlTransient
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
	
	@Column(name = "RESETTIME", nullable = true)
	@XmlTransient
	@Type(type = "timestamp")
	public Date getResetTime() {
		return resetTime;
	}
	public void setResetTime(Date resetTime) {
		this.resetTime = resetTime;
	}
	
	@Column(name = "MISSINGFILESTARTINDEX", nullable = false)
	@XmlElement
	public int getMissingFileStartIndex() {
		return missingFileStartIndex;
	}
	public void setMissingFileStartIndex(int missingFileStartIndex) {
		this.missingFileStartIndex = missingFileStartIndex;
	}
	
	@Column(name = "MISSINGFILEENDINDEX", nullable = false)
	@XmlElement
	public int getMissingFileEndIndex() {
		return missingFileEndIndex;
	}
	public void setMissingFileEndIndex(int missingFileEndIndex) {
		this.missingFileEndIndex = missingFileEndIndex;
	}
	

	@ManyToOne(fetch=FetchType.LAZY)
	@Transient
	@JoinColumn(name = "PARTNERID",nullable = true,foreignKey = @ForeignKey(name = "FK_PARTNERID"))
	@XmlTransient
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	
}
