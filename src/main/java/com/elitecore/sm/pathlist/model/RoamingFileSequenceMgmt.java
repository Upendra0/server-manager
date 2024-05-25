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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.roaming.model.Partner;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 
 * 
 * 
 */
@Entity()
@DynamicUpdate
@Table(name = "TBLTROAMINGFILESEQMGMT")
@Component(value = "roamingFileSequenceMgmt")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoamingFileSequenceMgmt extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733404212991596531L;
	private int id;
	private String elementType;
	private String fileType;
	private int minValue;
	private int maxValue;
	private Date resetTime;
	private int missingFileStartIndex;
	private int missingFileEndIndex;
	private Partner partner;
	
	public RoamingFileSequenceMgmt() {
		
	}
	
	public RoamingFileSequenceMgmt(String elementType,String fileType, int maxValue,
			Date lastUpdatedDate,int lastUpdatedByStaffId,Partner partner) {
		super.lastUpdatedDate = lastUpdatedDate;
		super.lastUpdatedByStaffId = lastUpdatedByStaffId;
		this.elementType  =elementType;
		this.fileType  =fileType;
		this.maxValue = maxValue;
		this.partner = partner;
		this.minValue=1;
	}
	
	@Id
	@Column(name="ID")
	@XmlElement
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="roamingFileSequenceMgmt",allocationSize=1)
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
	
	@Column(name = "FILETYPE", nullable = false, length = 100)
	@XmlElement
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	
	@Column(name = "MINVALUE", nullable = false)
	@XmlElement
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	
	@Column(name = "MAXVALUE", nullable = false)
	@XmlElement
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
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
	@JoinColumn(name = "PARTNERID",nullable = true,foreignKey = @ForeignKey(name = "FK_ROAMINGPARTNERID"))
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	
}
