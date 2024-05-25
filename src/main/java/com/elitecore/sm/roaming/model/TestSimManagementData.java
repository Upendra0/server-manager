package com.elitecore.sm.roaming.model;

import java.util.Date;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component
public class TestSimManagementData extends BaseModel {

	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2492145898015870294L;
	
	private int inBoundPmnCode;
	private int inBoundImsi;
	private int inBoundmsisdn;
	private Date inBoundFromDate;
	private Date inBoundToDate;
	private String inBoundServices;	
	private int outBoundPmnCode;
	private int outBoundImsi;
	private int outBoundmsisdn;
	private Date outBoundFromDate;
	private Date outBoundToDate;
	private String outBoundServices;	
	private String partnerName;
	private String lob;
	private String primaryTadig;
	private String secondaryTadig;
	
	public String getPrimaryTadig() {
		return primaryTadig;
	}
	public void setPrimaryTadig(String primaryTadig) {
		this.primaryTadig = primaryTadig;
	}
	public String getSecondaryTadig() {
		return secondaryTadig;
	}
	public void setSecondaryTadig(String secondaryTadig) {
		this.secondaryTadig = secondaryTadig;
	}
	public int getInBoundPmnCode() {
		return inBoundPmnCode;
	}
	public int getOutBoundPmnCode() {
		return outBoundPmnCode;
	}
	public void setOutBoundPmnCode(int outBoundPmnCode) {
		this.outBoundPmnCode = outBoundPmnCode;
	}
	public int getOutBoundImsi() {
		return outBoundImsi;
	}
	public void setOutBoundImsi(int outBoundImsi) {
		this.outBoundImsi = outBoundImsi;
	}
	public int getOutBoundmsisdn() {
		return outBoundmsisdn;
	}
	public void setOutBoundmsisdn(int outBoundmsisdn) {
		this.outBoundmsisdn = outBoundmsisdn;
	}
	public Date getOutBoundFromDate() {
		return outBoundFromDate;
	}
	public void setOutBoundFromDate(Date outBoundFromDate) {
		this.outBoundFromDate = outBoundFromDate;
	}
	public Date getOutBoundToDate() {
		return outBoundToDate;
	}
	public void setOutBoundToDate(Date outBoundToDate) {
		this.outBoundToDate = outBoundToDate;
	}
	public String getOutBoundServices() {
		return outBoundServices;
	}
	public void setOutBoundServices(String outBoundServices) {
		this.outBoundServices = outBoundServices;
	}
	public void setInBoundPmnCode(int inBoundPmnCode) {
		this.inBoundPmnCode = inBoundPmnCode;
	}
	public int getInBoundImsi() {
		return inBoundImsi;
	}
	public void setInBoundImsi(int inBoundImsi) {
		this.inBoundImsi = inBoundImsi;
	}
	public int getInBoundmsisdn() {
		return inBoundmsisdn;
	}
	public void setInBoundmsisdn(int inBoundmsisdn) {
		this.inBoundmsisdn = inBoundmsisdn;
	}
	public Date getInBoundFromDate() {
		return inBoundFromDate;
	}
	public void setInBoundFromDate(Date inBoundFromDate) {
		this.inBoundFromDate = inBoundFromDate;
	}
	public Date getInBoundToDate() {
		return inBoundToDate;
	}
	public void setInBoundToDate(Date inBoundToDate) {
		this.inBoundToDate = inBoundToDate;
	}
	public String getInBoundServices() {
		return inBoundServices;
	}
	public void setInBoundServices(String inBoundServices) {
		this.inBoundServices = inBoundServices;
	}
	
}
