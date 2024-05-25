package com.elitecore.sm.policy.controller;

public class TempFileStatistic {

	private String intanceName;
	private String rawCDR;
	private String processedCDR;
	private String rawFileSize ;
	private double deviation;
	private String processedFileSize;
	
	
	public String getIntanceName() {
		return intanceName;
	}
	public void setIntanceName(String intanceName) {
		this.intanceName = intanceName;
	}
	public String getRawCDR() {
		return rawCDR;
	}
	public void setRawCDR(String rawCDR) {
		this.rawCDR = rawCDR;
	}
	public String getProcessedCDR() {
		return processedCDR;
	}
	public void setProcessedCDR(String processedCDR) {
		this.processedCDR = processedCDR;
	}
	public String getRawFileSize() {
		return rawFileSize;
	}
	public void setRawFileSize(String rawFileSize) {
		this.rawFileSize = rawFileSize;
	}
	public String getProcessedFileSize() {
		return processedFileSize;
	}
	public void setProcessedFileSize(String processedFileSize) {
		this.processedFileSize = processedFileSize;
	}
	public double getDeviation() {
		return deviation;
	}
	public void setDeviation(double deviation) {
		this.deviation = deviation;
	}

	
	
	
}
