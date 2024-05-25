package com.elitecore.sm.rulelookup.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;

@Entity
@Component
@Table(name = "TBLTAUTOJOBSTATISTIC")
public class AutoJobStatistic {

	private static final long serialVersionUID = 4042152392614201815L;
	
	private int id;
	private String processType;
	private String tableName;
	private String dbQuery;
	private String serverInstance;
	private String sourceDirectory;
	private String action;
	private String schedulerName;
	private String schedulerType;
	private String jobStatus;
	private Timestamp executionStart;
	private Timestamp executionEnd;
	private String reloadRecordCount;
	private String reloadDbQueryStatus;
	private int successFileCount; 
	private int failedFileCount; 
	private int successRecordCount;
	private int failedRecordCount;
	private int duplicateRecordCount;
	private String reason;
	private Integer jobId;
	protected StateEnum status = StateEnum.ACTIVE;
	private String filePrefix;
	private String fileContains;

	@Id
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AutoJobStatistic",allocationSize=1)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "PROCESSTYPE", nullable = false, length = 30)
	public String getProcessType() {
		return processType;
	}
	
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
	@Column(name = "TABLENAME", nullable = true, length = 255)
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Column(name = "DBQUERY", nullable = true, length = 255)
	public String getDbQuery() {
		return dbQuery;
	}
	
	public void setDbQuery(String dbQuery) {
		this.dbQuery = dbQuery;
	}
	
	@Column(name = "SERVERINSTANCE", nullable = true, length = 255)
	public String getServerInstance() {
		return serverInstance;
	}
	
	public void setServerInstance(String serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	@Column(name = "SOURCEDIRECTORY", nullable = true, length = 255)
	public String getSourceDirectory() {
		return sourceDirectory;
	}
	
	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}
	
	@Column(name = "ACTION", nullable = true, length = 255)
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "SCHEDULER", nullable = false, length = 255)
	public String getSchedulerName() {
		return schedulerName;
	}
	
	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}
	
	@Column(name = "SCHEDULERTYPE", nullable = true, length = 30)
	public String getSchedulerType() {
		return schedulerType;
	}
	
	public void setSchedulerType(String schedulerType) {
		this.schedulerType = schedulerType;
	}
	
	@Column(name = "JOBSTATUS", nullable = false, length = 30)
	public String getJobStatus() {
		return jobStatus;
	}
	
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	
	@Column(name = "EXECUTIONSTART", nullable = false)
	public Timestamp getExecutionStart() {
		return executionStart;
	}
	
	public void setExecutionStart(Timestamp executionStart) {
		this.executionStart = executionStart;
	}
	
	@Column(name = "EXECUTIONEND", nullable = true)
	public Timestamp getExecutionEnd() {
		return executionEnd;
	}
	
	public void setExecutionEnd(Timestamp executionEnd) {
		this.executionEnd = executionEnd;
	}

	@Column(name = "RELOADRECORDCOUNT", nullable = true, length = 500)
	public String getReloadRecordCount() {
		return reloadRecordCount;
	}

	public void setReloadRecordCount(String reloadRecordCount) {
		this.reloadRecordCount = reloadRecordCount;
	}

	@Column(name = "RELOADDBQUERYSTATUS", nullable = true, length = 500)
	public String getReloadDbQueryStatus() {
		return reloadDbQueryStatus;
	}

	public void setReloadDbQueryStatus(String reloadDbQueryStatus) {
		this.reloadDbQueryStatus = reloadDbQueryStatus;
	}

	@Column(name = "SUCCESSFILECOUNT", nullable = true)
	public int getSuccessFileCount() {
		return successFileCount;
	}

	public void setSuccessFileCount(int successFileCount) {
		this.successFileCount = successFileCount;
	}

	@Column(name = "FAILEDFILECOUNT", nullable = true)
	public int getFailedFileCount() {
		return failedFileCount;
	}

	public void setFailedFileCount(int failedFileCount) {
		this.failedFileCount = failedFileCount;
	}

	@Column(name = "SUCCESSRECORDCOUNT", nullable = true)
	public int getSuccessRecordCount() {
		return successRecordCount;
	}

	public void setSuccessRecordCount(int successRecordCount) {
		this.successRecordCount = successRecordCount;
	}

	@Column(name = "FAILEDRECORDCOUNT", nullable = true)
	public int getFailedRecordCount() {
		return failedRecordCount;
	}

	public void setFailedRecordCount(int failedRecordCount) {
		this.failedRecordCount = failedRecordCount;
	}

	@Column(name = "DUPLICATERECORDCOUNT", nullable = true)
	public int getDuplicateRecordCount() {
		return duplicateRecordCount;
	}

	public void setDuplicateRecordCount(int duplicateRecordCount) {
		this.duplicateRecordCount = duplicateRecordCount;
	}
	
	@Column(name = "REASON", nullable = true, length = 500)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "JOBID", nullable = false)
	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS" ,nullable=false)
	public StateEnum getStatus() {
		return status;
	}

	public void setStatus(StateEnum state) {
		this.status = state;
	}

	@Column(name = "FILEPREFIX", nullable = true, length = 500)
	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	@Column(name = "FILECONTAINS", nullable = true, length = 500)
	public String getFileContains() {
		return fileContains;
	}

	public void setFileContains(String fileContains) {
		this.fileContains = fileContains;
	}
		
}
