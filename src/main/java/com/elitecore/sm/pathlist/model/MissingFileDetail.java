/**
 * 
 */
package com.elitecore.sm.pathlist.model;

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

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 
 * 
 * 
 */
@Entity()
@DynamicUpdate
@Table(name = "TBLTMISSINGFILEDETAIL")
@Component(value = "missingFileDetail")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class MissingFileDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733404212991596531L;
	private int id;
	private FileSequenceMgmt missingFileSequenceId;
	private int missingFileSequence;
	private Date entryDate;
	private String statusMessage;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="missingFileDetail",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "FILESEQMGMTID", nullable = true, foreignKey = @ForeignKey(name = "FK_FILESEQMGMTID_ID"))
	public FileSequenceMgmt getMissingFileSequenceId() {
		return missingFileSequenceId;
	}
	public void setMissingFileSequenceId(FileSequenceMgmt missingFileSequenceId) {
		this.missingFileSequenceId = missingFileSequenceId;
	}
	
	@Column(name = "MISSINGSEQUENCE", nullable = false)
	@XmlElement
	public int getMissingFileSequence() {
		return missingFileSequence;
	}
	
	public void setMissingFileSequence(int missingFileSequence) {
		this.missingFileSequence = missingFileSequence;
	}
	
	@Column(name = "ENTRYDATE", nullable = false)
	@XmlElement
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	@Column(name = "STATUSMESSAGE", nullable = false)
	@XmlElement
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
}
