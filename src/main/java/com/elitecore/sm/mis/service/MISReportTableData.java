package com.elitecore.sm.mis.service;

import java.util.Date;
/**
 * Contains data mapping to be downloaded/shown in grid for MIS reports
 * @author Neha Kochhar
 *
 */
public class MISReportTableData 
{
	private String serverId;
	private String serverName;
	private String serviceType;
	private String serviceId;
	private String serviceName;
	private Date reportDate;
	private Long receivedPackets = 0L;
	private Long successPackets = 0L;
	private Long droppedPackets = 0L;
	private Long receivedFiles = 0L;
	private Long successFiles = 0L;
	private Long failFiles = 0L;
	private Long totalRecords = 0L;
	private Long successRecords = 0L;
	private Long failRecords = 0L;
	private Double droppedPacketsPercentage = 0.0;
	private Double failedFilesPercentage = 0.0;
	private Double failedRecordsPercentage = 0.0;
	private Integer hour;
	private Integer date;
	private Integer month;
	private Integer year;
	/**
	 * Server Id
	 * @return
	 */
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	/**
	 * Server Name
	 * @return
	 */
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * Service Type
	 * @return
	 */
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	/**
	 * Service Id
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * Service Name
	 * @return
	 */
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * Report Date
	 * @return
	 */
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * Total recieved packets
	 * @return
	 */
	public Long getReceivedPackets() {
		return receivedPackets;
	}
	public void setReceivedPackets(Long receivedPackets) {
		this.receivedPackets = receivedPackets;
	}
	/**
	 * Total Success packets
	 * @return
	 */
	public Long getSuccessPackets() {
		return successPackets;
	}
	public void setSuccessPackets(Long successPackets) {
		this.successPackets = successPackets;
	}
	/**
	 * Total dropped packets
	 * @return
	 */
	public Long getDroppedPackets() {
		return droppedPackets;
	}
	public void setDroppedPackets(Long droppedPackets) {
		this.droppedPackets = droppedPackets;
	}
	/**
	 * Total received files
	 * @return
	 */
	public Long getReceivedFiles() {
		return receivedFiles;
	}
	public void setReceivedFiles(Long receivedFiles) {
		this.receivedFiles = receivedFiles;
	}
	/**
	 * Total success files
	 * @return
	 */
	public Long getSuccessFiles() {
		return successFiles;
	}
	public void setSuccessFiles(Long successFiles) {
		this.successFiles = successFiles;
	}
	/**
	 * Total fail files
	 * @return
	 */
	public Long getFailFiles() {
		return failFiles;
	}
	public void setFailFiles(Long failFiles) {
		this.failFiles = failFiles;
	}
	/**
	 * Total Records
	 * @return
	 */
	public Long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}
	/**
	 * Total success records
	 * @return
	 */
	public Long getSuccessRecords() {
		return successRecords;
	}
	public void setSuccessRecords(Long successRecords) {
		this.successRecords = successRecords;
	}
	/**
	 * Total fail records
	 * @return
	 */
	public Long getFailRecords() {
		return failRecords;
	}
	public void setFailRecords(Long failRecords) {
		this.failRecords = failRecords;
	}
	/**
	 * Dropped Packets Percentage
	 * @return
	 */
	public Double getDroppedPacketsPercentage() {
		return droppedPacketsPercentage;
	}
	public void setDroppedPacketsPercentage(Double droppedPacketsPercentage) {
		this.droppedPacketsPercentage = droppedPacketsPercentage;
	}
	/**
	 * Failed files percentage
	 * @return
	 */
	public Double getFailedFilesPercentage() {
		return failedFilesPercentage;
	}
	public void setFailedFilesPercentage(Double failedFilesPercentage) {
		this.failedFilesPercentage = failedFilesPercentage;
	}
	/**
	 * Failed Records percentage
	 * @return
	 */
	public Double getFailedRecordsPercentage() {
		return failedRecordsPercentage;
	}
	public void setFailedRecordsPercentage(Double failedRecordsPercentage) {
		this.failedRecordsPercentage = failedRecordsPercentage;
	}
	/**
	 * Hour
	 * @return
	 */
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	/**
	 * Date
	 * @return
	 */
	public Integer getDate() {
		return date;
	}
	public void setDate(Integer date) {
		this.date = date;
	}
	/**
	 * Month
	 * @return
	 */
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	/**
	 * Year
	 * @return
	 */
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
}

