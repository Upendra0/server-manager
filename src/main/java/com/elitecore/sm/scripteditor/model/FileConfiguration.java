package com.elitecore.sm.scripteditor.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.elitecore.sm.common.model.BaseModel;


/**
 * The persistent class for the TBLMFILECONFIGMST database table.
 * 
 */
@Entity
@Table(name="TBLMFILECONFIGMST")
@NamedQuery(name="FileConfiguration.findAll", query="SELECT f FROM FileConfiguration f")
public class FileConfiguration extends  BaseModel {
	
	private static final long serialVersionUID = 1L;

	private long fileId;

	private String description;

	private String exenCommand;

	private String fileAlias;

	private String fileName;

	private String filePath;

	private int fileType;
	
	private String staffName;
	
	private String fileContent;
	
	private String scriptOperation;

	private String logFileName;
	
	private String logPath;
	
	private String statusCommand;
	
	private String statusContains;
	
	
	@Column(name="STATUSCOMMAND", nullable=false)
	public String getStatusCommand() {
		return statusCommand;
	}

	public void setStatusCommand(String statusCommand) {
		this.statusCommand = statusCommand;
	}

	@Column(name="STATUSCONTAINS", nullable=false)
	public String getStatusContains() {
		return statusContains;
	}

	public void setStatusContains(String statusContains) {
		this.statusContains = statusContains;
	}

	@Column(name="LOGFILENAME", nullable=false)
	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	@Column(name="LOGPATH", nullable=false)
	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		this.logPath = logPath;
	}

	@Transient
	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	@Transient
	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	private ServerConfiguration tblmserverconfigmst;

	public FileConfiguration() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
							     pkColumnName="TABLE_NAME",valueColumnName="VALUE",
							     pkColumnValue="ScriptMgr-FileConfig",allocationSize=1)
	@Column(name="FILEID", nullable=false)
	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	@Column(name="DESCRIPTION", nullable=false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="EXENCOMMAND", nullable=false)
	public String getExenCommand() {
		return exenCommand;
	}

	public void setExenCommand(String exenCommand) {
		this.exenCommand = exenCommand;
	}

	@Column(name="FILEALIAS", nullable=false)
	public String getFileAlias() {
		return fileAlias;
	}

	public void setFileAlias(String fileAlias) {
		this.fileAlias = fileAlias;
	}

	@Column(name="FILENAME", nullable=false)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	@Column(name="FILEPATH", nullable=false)
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Column(name="FILETYPE", nullable=false)
	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	//bi-directional many-to-one association to ServerConfiguration
	@ManyToOne
	@JoinColumn(name="SERVERID")
	public ServerConfiguration getTblmserverconfigmst() {
		return this.tblmserverconfigmst;
	}

	public void setTblmserverconfigmst(ServerConfiguration tblmserverconfigmst) {
		this.tblmserverconfigmst = tblmserverconfigmst;
	}

	@Transient
	public String getScriptOperation() {
		return scriptOperation;
	}
	
	public void setScriptOperation(String scriptOperation) {
		this.scriptOperation = scriptOperation;
	}
}