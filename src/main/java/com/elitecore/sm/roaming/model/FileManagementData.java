package com.elitecore.sm.roaming.model;

import java.beans.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component
@Entity(name = "TBLMFILEMANAGEMENT")
public class FileManagementData extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5339522694405822089L;
	private int id;
	private String serviceType;
	private String tapInVersion;
	private String tapOutVersion;
	private String    maxRecordsInTapOut;
	private String    maxFileSizeOfTapOut;
	private String regeneratedTapOutFileSequence;
	private String fileValidation;
	private String sendTapNotification;
	private String rapInVersion;
	private String rapOutVersion;
	private String sendNrtrde;
	private String nrtrdeInVersion;
	private String nrtrdeOutVersion;
	private String    maxRecordsInNrtrdeOut;
	private String    maxfileSizeOfnrtrdeOut;
	private Partner partner;
	
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="fileManagement",allocationSize=1)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	/*@Column(name = "SERVICETYPE", nullable = false,length = 100)
	public String getServicetype() {
		return serviceType;
	}
	public void setServicetype(String serviceType) {
		this.serviceType = serviceType;
	}*/
	
	@Column(name = "TAPINVERSION", nullable = false)
	public String getTapInVersion() {
		return tapInVersion;
	}
	public void setTapInVersion(String tapInVersion) {
		this.tapInVersion = tapInVersion;
	}
	@Column(name = "TAPOUTVERSION",nullable = false)
	public String getTapOutVersion() {
		return tapOutVersion;
	}
	public void setTapOutVersion(String tapOutVersion) {
		this.tapOutVersion = tapOutVersion;
	}
	@Column(name = "MAXRECORDSINTAPOUT",nullable = false)
	public String getMaxRecordsInTapOut() {
		return maxRecordsInTapOut;
	}
	public void setMaxRecordsInTapOut(String maxRecordsInTapOut) {
		this.maxRecordsInTapOut = maxRecordsInTapOut;
	}
	@Column(name = "MAXFILESIZEOFTAPOUT",nullable = false)
	public String getMaxFileSizeOfTapOut() {
		return maxFileSizeOfTapOut;
	}
	public void setMaxFileSizeOfTapOut(String maxFileSizeOfTapOut) {
		this.maxFileSizeOfTapOut = maxFileSizeOfTapOut;
	}
	@Column(name = "REGENERATEDTAPOUTFILESEQUENCE",nullable = false)
	public String getRegeneratedTapOutFileSequence() {
		return regeneratedTapOutFileSequence;
	}
	public void setRegeneratedTapOutFileSequence(String regeneratedTapOutFileSequence) {
		this.regeneratedTapOutFileSequence = regeneratedTapOutFileSequence;
	}
	@Column(name = "TESTFILEVALIDATION",nullable = false)
	public String getFileValidation() {
		return fileValidation;
	}
	public void setFileValidation(String fileValidation) {
		this.fileValidation = fileValidation;
	}
	@Column(name = "TAPNOTIFICATION",nullable = false)
	public String getSendTapNotification() {
		return sendTapNotification;
	}
	public void setSendTapNotification(String sendTapNotification) {
		this.sendTapNotification = sendTapNotification;
	}
	@Column(name = "RAPINVERSION",nullable = false)
	public String getRapInVersion() {
		return rapInVersion;
	}
	public void setRapInVersion(String rapInVersion) {
		this.rapInVersion = rapInVersion;
	}
	@Column(name = "RAPOUTVERSION",nullable = false)
	public String getRapOutVersion() {
		return rapOutVersion;
	}
	public void setRapOutVersion(String rapOutVersion) {
		this.rapOutVersion = rapOutVersion;
	}
	@Column(name = "SENDNRTRDE",nullable = false)
	public String getSendNrtrde() {
		return sendNrtrde;
	}
	public void setSendNrtrde(String sendNrtrde) {
		this.sendNrtrde = sendNrtrde;
	}
	@Column(name = "NRTRDEINVERSION",nullable = false)
	public String getNrtrdeInVersion() {
		return nrtrdeInVersion;
	}
	public void setNrtrdeInVersion(String nrtrdeInVersion) {
		this.nrtrdeInVersion = nrtrdeInVersion;
	}
	@Column(name = "NRTRDEOUTVERSION",nullable = false)
	public String getNrtrdeOutVersion() {
		return nrtrdeOutVersion;
	}
	public void setNrtrdeOutVersion(String nrtrdeOutVersion) {
		this.nrtrdeOutVersion = nrtrdeOutVersion;
	}
	@Column(name = "MAXRECORDSINNRTRDEOUT",nullable = false)
	public String getMaxRecordsInNrtrdeOut() {
		return maxRecordsInNrtrdeOut;
	}
	public void setMaxRecordsInNrtrdeOut(String maxRecordsInNrtrdeOut) {
		this.maxRecordsInNrtrdeOut = maxRecordsInNrtrdeOut;
	}
	@Column(name = "MAXFILESIZEOFNRTRDEOUT",nullable = false)
	public String getMaxfileSizeOfnrtrdeOut() {
		return maxfileSizeOfnrtrdeOut;
	}
	public void setMaxfileSizeOfnrtrdeOut(String maxfileSizeOfnrtrdeOut) {
		this.maxfileSizeOfnrtrdeOut = maxfileSizeOfnrtrdeOut;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@Transient
	@JoinColumn(name = "PARTNERID",nullable = true,foreignKey = @ForeignKey(name = "FK_FILEMANAGEMENTPARTNERID"))
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	@Column(name = "SERVICETYPE", nullable = false,length = 100)
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	
	
}
