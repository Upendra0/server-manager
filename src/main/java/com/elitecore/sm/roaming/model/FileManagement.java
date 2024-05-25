package com.elitecore.sm.roaming.model;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;


@Component
public class FileManagement extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5683506841291988020L;
	
	private String testTapInVersion;
	private String testTapOutVersion;
	private String testMaxRecordsInTapOut;
	private String testMaxFileSizeOfTapOut;
	private String testRegeneratedTapOutFileSequence;
	private String testFileValidation;
	private String testSendTapNotification = "Yes";
	private String testRapInVersion;
	private String testRapOutVersion;
	private String testSendNrtrde = "Yes";
	private String testNrtrdeInVersion;
	private String testNrtrdeOutVersion;
	private String testMaxRecordsInNrtrdeOut;
	private String testMaxfileSizeOfnrtrdeOut;
	private String commercialTapInVersion;
	private String commercialTapOutVersion;
	private String commercialMaxRecordsInTapOut;
	private String commercialMaxFileSizeOfTapOut;
	private String commercialRegeneratedTapOutFileSequence;
	private String commercialFileValidation;
	private String commercialSendTapNotification = "Yes";
	private String commercialRapInVersion;
	private String commercialRapOutVersion;
	private String commercialSendNrtrde = "Yes";
	private String commercialNrtrdeInVersion;
	private String commercialNrtrdeOutVersion;
	private String commercialMaxRecordsInNrtrdeOut;
	private String commercialMaxfileSizeOfnrtrdeOut;
	private String partnerName;
	private String lob;
	private String primaryTadig;
	private String secondaryTadig;
	
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partner) {
		this.partnerName = partner;
	}
	public String getLob() {
		return lob;
	}
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
	public void setLob(String lob) {
		this.lob = lob;
	}
	public String getTestTapInVersion() {
		return testTapInVersion;
	}
	public void setTestTapInVersion(String testTapInVersion) {
		this.testTapInVersion = testTapInVersion;
	}
	public String getTestTapOutVersion() {
		return testTapOutVersion;
	}
	public void setTestTapOutVersion(String testTapOutVersion) {
		this.testTapOutVersion = testTapOutVersion;
	}
	public String getTestMaxRecordsInTapOut() {
		return testMaxRecordsInTapOut;
	}
	public void setTestMaxRecordsInTapOut(String testMaxRecordsInTapOut) {
		this.testMaxRecordsInTapOut = testMaxRecordsInTapOut;
	}
	public String getTestMaxFileSizeOfTapOut() {
		return testMaxFileSizeOfTapOut;
	}
	public void setTestMaxFileSizeOfTapOut(String testMaxFileSizeOfTapOut) {
		this.testMaxFileSizeOfTapOut = testMaxFileSizeOfTapOut;
	}
	public String getTestRegeneratedTapOutFileSequence() {
		return testRegeneratedTapOutFileSequence;
	}
	public void setTestRegeneratedTapOutFileSequence(String testRegeneratedTapOutFileSequence) {
		this.testRegeneratedTapOutFileSequence = testRegeneratedTapOutFileSequence;
	}
	public String getTestFileValidation() {
		return testFileValidation;
	}
	public void setTestFileValidation(String testFileValidation) {
		this.testFileValidation = testFileValidation;
	}
	public String getTestSendTapNotification() {
		return testSendTapNotification;
	}
	public void setTestSendTapNotification(String testSendTapNotification) {
		this.testSendTapNotification = testSendTapNotification;
	}
	public String getTestRapInVersion() {
		return testRapInVersion;
	}
	public void setTestRapInVersion(String testRapInVersion) {
		this.testRapInVersion = testRapInVersion;
	}
	public String getTestRapOutVersion() {
		return testRapOutVersion;
	}
	public void setTestRapOutVersion(String testRapOutVersion) {
		this.testRapOutVersion = testRapOutVersion;
	}
	public String getTestSendNrtrde() {
		return testSendNrtrde;
	}
	public void setTestSendNrtrde(String testSendNrtrde) {
		this.testSendNrtrde = testSendNrtrde;
	}
	public String getTestNrtrdeInVersion() {
		return testNrtrdeInVersion;
	}
	public void setTestNrtrdeInVersion(String testNrtrdeInVersion) {
		this.testNrtrdeInVersion = testNrtrdeInVersion;
	}
	public String getTestNrtrdeOutVersion() {
		return testNrtrdeOutVersion;
	}
	public void setTestNrtrdeOutVersion(String testNrtrdeOutVersion) {
		this.testNrtrdeOutVersion = testNrtrdeOutVersion;
	}
	public String getTestMaxRecordsInNrtrdeOut() {
		return testMaxRecordsInNrtrdeOut;
	}
	public void setTestMaxRecordsInNrtrdeOut(String testMaxRecordsInNrtrdeOut) {
		this.testMaxRecordsInNrtrdeOut = testMaxRecordsInNrtrdeOut;
	}
	public String getTestMaxfileSizeOfnrtrdeOut() {
		return testMaxfileSizeOfnrtrdeOut;
	}
	public void setTestMaxfileSizeOfnrtrdeOut(String testMaxfileSizeOfnrtrdeOut) {
		this.testMaxfileSizeOfnrtrdeOut = testMaxfileSizeOfnrtrdeOut;
	}
	public String getCommercialTapInVersion() {
		return commercialTapInVersion;
	}
	public void setCommercialTapInVersion(String commercialTapInVersion) {
		this.commercialTapInVersion = commercialTapInVersion;
	}
	public String getCommercialTapOutVersion() {
		return commercialTapOutVersion;
	}
	public void setCommercialTapOutVersion(String commercialTapOutVersion) {
		this.commercialTapOutVersion = commercialTapOutVersion;
	}
	public String getCommercialMaxRecordsInTapOut() {
		return commercialMaxRecordsInTapOut;
	}
	public void setCommercialMaxRecordsInTapOut(String commercialMaxRecordsInTapOut) {
		this.commercialMaxRecordsInTapOut = commercialMaxRecordsInTapOut;
	}
	public String getCommercialMaxFileSizeOfTapOut() {
		return commercialMaxFileSizeOfTapOut;
	}
	public void setCommercialMaxFileSizeOfTapOut(String commercialMaxFileSizeOfTapOut) {
		this.commercialMaxFileSizeOfTapOut = commercialMaxFileSizeOfTapOut;
	}
	public String getCommercialRegeneratedTapOutFileSequence() {
		return commercialRegeneratedTapOutFileSequence;
	}
	public void setCommercialRegeneratedTapOutFileSequence(String commercialRegeneratedTapOutFileSequence) {
		this.commercialRegeneratedTapOutFileSequence = commercialRegeneratedTapOutFileSequence;
	}
	public String getCommercialFileValidation() {
		return commercialFileValidation;
	}
	public void setCommercialFileValidation(String commercialFileValidation) {
		this.commercialFileValidation = commercialFileValidation;
	}
	public String getCommercialSendTapNotification() {
		return commercialSendTapNotification;
	}
	public void setCommercialSendTapNotification(String commercialSendTapNotification) {
		this.commercialSendTapNotification = commercialSendTapNotification;
	}
	public String getCommercialRapInVersion() {
		return commercialRapInVersion;
	}
	public void setCommercialRapInVersion(String commercialRapInVersion) {
		this.commercialRapInVersion = commercialRapInVersion;
	}
	public String getCommercialRapOutVersion() {
		return commercialRapOutVersion;
	}
	public void setCommercialRapOutVersion(String commercialRapOutVersion) {
		this.commercialRapOutVersion = commercialRapOutVersion;
	}
	public String getCommercialSendNrtrde() {
		return commercialSendNrtrde;
	}
	public void setCommercialSendNrtrde(String commercialSendNrtrde) {
		this.commercialSendNrtrde = commercialSendNrtrde;
	}
	public String getCommercialNrtrdeInVersion() {
		return commercialNrtrdeInVersion;
	}
	public void setCommercialNrtrdeInVersion(String commercialNrtrdeInVersion) {
		this.commercialNrtrdeInVersion = commercialNrtrdeInVersion;
	}
	public String getCommercialNrtrdeOutVersion() {
		return commercialNrtrdeOutVersion;
	}
	public void setCommercialNrtrdeOutVersion(String commercialNrtrdeOutVersion) {
		this.commercialNrtrdeOutVersion = commercialNrtrdeOutVersion;
	}
	public String getCommercialMaxRecordsInNrtrdeOut() {
		return commercialMaxRecordsInNrtrdeOut;
	}
	public void setCommercialMaxRecordsInNrtrdeOut(String commercialMaxRecordsInNrtrdeOut) {
		this.commercialMaxRecordsInNrtrdeOut = commercialMaxRecordsInNrtrdeOut;
	}
	public String getCommercialMaxfileSizeOfnrtrdeOut() {
		return commercialMaxfileSizeOfnrtrdeOut;
	}
	public void setCommercialMaxfileSizeOfnrtrdeOut(String commercialMaxfileSizeOfnrtrdeOut) {
		this.commercialMaxfileSizeOfnrtrdeOut = commercialMaxfileSizeOfnrtrdeOut;
	}
	
	
	
	

}
