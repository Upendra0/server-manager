package com.elitecore.sm.mis.model;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@Entity()
@Table(name = "TBLTMISREPORTAGENTCALLDETAIL")
public class MISDetail implements Serializable{

	private static final long serialVersionUID = 7492134298307574845L;
	private int callId;
	private int serverId;
	private String serverName;
	private Timestamp reportStartTime;
	private Timestamp reportEndTime;
	private Timestamp callTime;
	private int reportStatus;
	private Set<MISReportData> reportDataSet;

	@Id
	@Column(name = "CALLID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	pkColumnValue="MISDetail",allocationSize=1)
	public int getCallId() {
		return callId;
	}
	public void setCallId(int callId) {
		this.callId = callId;
	}

	@Column(name = "CALLTIME", nullable= false)
	public Timestamp getCallTime() {
		return callTime;
	}
	public void setCallTime(Timestamp callTime) {
		this.callTime = callTime;
	}

	@Column(name = "SERVERID", nullable= false)
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	@Column(name = "SERVERNAME", length=250, nullable= false)
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name = "REPORTSTARTTIME",nullable= false)
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

	@Column(name = "STATUS", nullable= false)
	public int getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(int reportStatus) {
		this.reportStatus = reportStatus;
	}

	/*@OneToMany(fetch = FetchType.LAZY)
	@Cascade({CascadeType.ALL_DELETE_ORPHAN})*/
	@OneToMany(cascade=javax.persistence.CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CALLID", nullable= false/*, foreignKey = @ForeignKey(name = "FK_SRV_INSTANCE_MIS_DETAIL")*/)
	public Set<MISReportData> getReportDataSet() {
		return reportDataSet;
	}
	public void setReportDataSet(Set<MISReportData> reportDataSet) {
		this.reportDataSet = reportDataSet;
	}



	@Override                                                        
	public String toString() {                                        

		StringWriter out = new StringWriter();                        
		PrintWriter writer = new PrintWriter(out);
		writer.println();                    
		writer.println("------------MISReportAgentCallDetail-----------------");
		writer.println("callId=" +callId);                                     
		writer.println("serverId=" +serverId);                                     
		writer.println("serverName=" +serverName);                                     
		writer.println("reportStartTime=" +reportStartTime);                                     
		writer.println("reportEndTime=" +reportEndTime);                                     
		writer.println("callTime=" +callTime);                                     
		writer.println("status=" +reportStatus);
		writer.println("data set count=" + (reportDataSet != null ? reportDataSet.size() : 0)); 
		writer.println("----------------------------------------------------");
		writer.close();                                               
		return out.toString();
	}
}

