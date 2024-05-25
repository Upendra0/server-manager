package com.elitecore.sm.roaming.model;


import org.springframework.stereotype.Component;

@Component
public class FileSequenceManagement extends RoamingConfiguration {
	/**
	 * 
	 */
	private static final long serialVersionUID = -718753426773378343L;
	
	private int testTapIn;
	private int testTapOut;
	private int testRapIn;
	private int testRapOut;
	private int nrtrdeIn;
	private int commercialTapIn;
	private int commercialTapOut;
	private int commercialRapIn;
	private int commercialRapOut;
	private int nrtrdeOut;
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
	public int getTestTapIn() {
		return testTapIn;
	}
	public void setTestTapIn(int testTapIn) {
		this.testTapIn = testTapIn;
	}
	public int getTestTapOut() {
		return testTapOut;
	}
	public void setTestTapOut(int testTapOut) {
		this.testTapOut = testTapOut;
	}
	public int getTestRapIn() {
		return testRapIn;
	}
	public void setTestRapIn(int testRapIn) {
		this.testRapIn = testRapIn;
	}
	public int getTestRapOut() {
		return testRapOut;
	}
	public void setTestRapOut(int testRapOut) {
		this.testRapOut = testRapOut;
	}
	public int getNrtrdeIn() {
		return nrtrdeIn;
	}
	public void setNrtrdeIn(int nrtrdeIn) {
		this.nrtrdeIn = nrtrdeIn;
	}
	public int getCommercialTapIn() {
		return commercialTapIn;
	}
	public void setCommercialTapIn(int commercialTapIn) {
		this.commercialTapIn = commercialTapIn;
	}
	public int getCommercialTapOut() {
		return commercialTapOut;
	}
	public void setCommercialTapOut(int commercialTapOut) {
		this.commercialTapOut = commercialTapOut;
	}
	public int getCommercialRapIn() {
		return commercialRapIn;
	}
	public void setCommercialRapIn(int commercialRapIn) {
		this.commercialRapIn = commercialRapIn;
	}
	public int getCommercialRapOut() {
		return commercialRapOut;
	}
	public void setCommercialRapOut(int commercialRapOut) {
		this.commercialRapOut = commercialRapOut;
	}
	public int getNrtrdeOut() {
		return nrtrdeOut;
	}
	public void setNrtrdeOut(int nrtrdeOut) {
		this.nrtrdeOut = nrtrdeOut;
	}
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
	
}
