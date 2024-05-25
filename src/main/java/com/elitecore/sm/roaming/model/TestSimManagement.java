package com.elitecore.sm.roaming.model;

import java.beans.Transient;
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
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component(value="testSimManagement")
@Entity(name = "TBLMTESTSIMMANAGEMENT")
public class TestSimManagement extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7937676215788208608L;
	
	
	private int id;
	private String type;
	private int pmnCode;
	private int imsi;
	private int msisdn;
	private Date fromDate;
	private Date toDate;
	private String services;	
	private Partner partner;
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="testsimmanagemnt",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(name = "TYPE", nullable = false, length = 100)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "PMNCODE",nullable = false)
	public int getPmnCode() {
		return pmnCode;
	}
	public void setPmnCode(int pmnCode) {
		this.pmnCode = pmnCode;
	}
	@Column(name = "IMSI",nullable= false)
	public int getImsi() {
		return imsi;
	}
	public void setImsi(int imsi) {
		this.imsi = imsi;
	}
	@Column(name= "MSISDN",nullable = false)
	public int getMsisdn() {
		return msisdn;
	}
	public void setMsisdn(int msisdn) {
		this.msisdn = msisdn;
	}
	
	@Column(name = "FROMDATE",nullable =false)
	@Type(type = "timestamp")
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	@Column(name = "TODATE",nullable = false)
	@Type(type = "timestamp")
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	@Column(name = "SERVICES",nullable = false)
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@Transient
	@JoinColumn(name = "PARTNERID",nullable = true,foreignKey = @ForeignKey(name = "FK_TESTSIMPARTNERID"))
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	
}
