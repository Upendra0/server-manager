package com.elitecore.sm.mis.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
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
import javax.persistence.Transient;

@Entity()
@Table(name = "TBLTMISREPORTDATA")
public class MISReportData implements Comparable<MISReportData>,Serializable{

	private static final long serialVersionUID = 1594126980923662088L;
	private int id;
	private String serviceType;
	private Integer serviceId;
	private String serviceName;
	private Timestamp reportStartTime;
	private Timestamp reportEndTime;
	private long receivedPackets;
	private long successPackets;
	private long receivedFiles;
	private long successFiles;
	private long failFiles;
	private long totalRecords;
	private long successRecords;
	private long droppedPackets;
	private long failRecords;
	private double droppedPacketsPercentage;
	private double failedFilesPercentage;
	private double failedRecordsPercentage;
	private Date reportStartDate;

	private MISDetail reportCallDetail;
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	pkColumnValue="MISReportData",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "SERVICETYPE", length = 60, nullable= false)
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	@Column(name = "SERVICEID", nullable= false)
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	@Column(name = "SERVICENAME", length=250, nullable= false)
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	@Column(name = "REPORTSTARTTIME", nullable= false)
	public Timestamp getReportStartTime() {
		return reportStartTime;
	}
	public void setReportStartTime(Timestamp reportStartTime) {
		this.reportStartTime = reportStartTime;
	}
	@Column(name = "REPORTENDTIME", nullable= false)
	public Timestamp getReportEndTime() {
		return reportEndTime;
	}
	public void setReportEndTime(Timestamp reportEndTime) {
		this.reportEndTime = reportEndTime;
	}

	@Column(name = "RECEIVED_PACKETS")
	public long getReceivedPackets() {
		return receivedPackets;
	}
	public void setReceivedPackets(long receivedPackets) {
		this.receivedPackets = receivedPackets;
	}

	@Column(name = "SUCCESS_PACKET")
	public long getSuccessPackets() {
		return successPackets;
	}
	public void setSuccessPackets(long successPackets) {
		this.successPackets = successPackets;
	}

	@Column(name = "DROPPED_PACKET")
	public long getDroppedPackets() {
		return droppedPackets;
	}
	public void setDroppedPackets(long droppedPackets) {
		this.droppedPackets = droppedPackets;
	}


	@Column(name = "RECEIVED_FILES")
	public long getReceivedFiles() {
		return receivedFiles;
	}
	public void setReceivedFiles(long receivedFiles) {
		this.receivedFiles = receivedFiles;
	}

	@Column(name = "SUCCESS_FILES")
	public long getSuccessFiles() {
		return successFiles;
	}
	public void setSuccessFiles(long successFiles) {
		this.successFiles = successFiles;
	}

	@Column(name = "FAIL_FILES")
	public long getFailFiles() {
		return failFiles;
	}
	public void setFailFiles(long failFiles) {
		this.failFiles = failFiles;
	}

	@Column(name = "TOTAL_RECORDS")
	public long getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Column(name = "SUCCESS_RECORDS")
	public long getSuccessRecords() {
		return successRecords;
	}
	public void setSuccessRecords(long successRecords) {
		this.successRecords = successRecords;
	}

	@Column(name = "FAIL_RECORDS")
	public long getFailRecords() {
		return failRecords;
	}
	public void setFailRecords(long failRecords) {
		this.failRecords = failRecords;
	}

	@Transient
	public Double getDroppedPacketsPercentage() {
		return droppedPacketsPercentage;
	}

	public void setDroppedPacketsPercentage(Double droppedPacketsPercentage) {
		this.droppedPacketsPercentage = droppedPacketsPercentage;
	}

	@Transient
	public Double getFailedFilesPercentage() {
		return failedFilesPercentage;
	}

	public void setFailedFilesPercentage(Double failedFilesPercentage) {
		this.failedFilesPercentage = failedFilesPercentage;
	}

	@Transient
	public Double getFailedRecordsPercentage() {
		return failedRecordsPercentage;
	}

	public void setFailedRecordsPercentage(Double failedRecordsPercentage) {
		this.failedRecordsPercentage = failedRecordsPercentage;
	}

	//@XmlElement
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "CALLID", referencedColumnName = "CALLID" ,nullable= false, insertable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_MIS_DETAIL"))
	public MISDetail getReportCallDetail() {
		return reportCallDetail;
	}

	public void setReportCallDetail(MISDetail reportCallDetail) {
		this.reportCallDetail = reportCallDetail;
	}


	@Override                                                        
	public String toString() {                                        

		StringWriter out = new StringWriter();                        
		PrintWriter writer = new PrintWriter(out);
		writer.println();                    
		writer.println("------------MISReportData-----------------");
		writer.println("id=" +id);
		writer.println("serviceType=" +serviceType);                                     
		writer.println("serviceId=" +serviceId);                                     
		writer.println("serviceName=" +serviceName);                                     
		writer.println("receivedPackets=" +receivedPackets);                                     
		writer.println("successPackets=" +successPackets);                                     
		writer.println("droppedPackets=" +droppedPackets);
		writer.println("receivedFiles=" +receivedFiles);                                     
		writer.println("successFiles=" +successFiles);                                     
		writer.println("failFiles=" +failFiles);   
		writer.println("totalRecords=" +totalRecords);                                     
		writer.println("successRecords=" +successRecords);                                     
		writer.println("failRecords=" +failRecords);
		writer.println("reportStartTime=" +reportStartTime);                                     
		writer.println("reportEndTime=" +reportEndTime);   
		writer.println("-------------------------------------------");
		writer.close();                                               
		return out.toString();
	}
	
	@Column(name = "REPORTSTARTDATE")
	public Date getReportStartDate() {
		return reportStartDate;
	}
	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	@Override
	public int compareTo(MISReportData o) { //NOSONAR

		if(this == o) {
			return 0;
		}

		int i = this.serviceType.compareTo(o.serviceType);
		if(i != 0) {
			return i;
		}

		i = this.serviceId.compareTo(o.serviceId);
		if(i != 0) {
			return i;
		}

		i = this.reportStartTime.compareTo(o.reportStartTime);
		if(i != 0) {
			return i;
		}

		i = this.reportEndTime.compareTo(o.reportEndTime);
		return i;
	}
}
