/**
 * 
 */
package com.elitecore.sm.errorreprocess.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author Ranjitsinh Reval
 *
 */

@Component(value="errorReprocessDetails")
@Entity()
@Table(name = "TBLTREPROCESSDETAIL")
@DynamicUpdate
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ErrorReprocessDetails.class)
public class ErrorReprocessDetails extends BaseModel {

	private static final long serialVersionUID = 3946223438608509490L;

	private int id;
	private String fileName;
	private BigDecimal fileSize;
	private ErrorReprocessingBatch reprocessingBatch;
	private Date reprocessStartTime;
	private Date reprocessEndTime;
	private String failureReason;
	private FileReprocessStatusEnum errorReprocessStatus = FileReprocessStatusEnum.IN_QUEUE;
	private Service service;
	private ServiceType svctype;
	private ServerInstance serverInstance;
	private String filePath;
	private Parser parser;
	private Composer composer;
	private String readFilePath;
	private String absoluteFilePath;
	private String fileBackUpPath;
	private String fileReprocessType = "MANUAL";
	
	@JsonProperty
	private boolean isCompress;
	
	@JsonProperty
	private boolean isInputSourceCompress;
	
	
	private boolean isModifyFileReprocessed;
	
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="ErrorReprocessDetails",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	
	/**
	 * @return the fileName
	 */
	@Column(name = "FILENAME", unique = false, nullable = false, length = 100)
	public String getFileName() {
		return fileName;
	}

	
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	/**
	 * @return the fileSize
	 */
	@Column(name = "FILESIZE", unique = false, nullable = false, precision=10, scale=2)
	public BigDecimal getFileSize() {
		return fileSize;
	}

	
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(BigDecimal fileSize) {
		this.fileSize = fileSize;
	}

	
	/**
	 * @return the reprocessingBatch
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REPROCESSBATCHID", nullable = false, foreignKey = @ForeignKey(name = "FK_BATCH_REPROCESS_DETAILS"))
	public ErrorReprocessingBatch getReprocessingBatch() {
		return reprocessingBatch;
	}

	
	/**
	 * @param reprocessingBatch the reprocessingBatch to set
	 */
	public void setReprocessingBatch(ErrorReprocessingBatch reprocessingBatch) {
		this.reprocessingBatch = reprocessingBatch;
	}

	
	/**
	 * @return the reprocessStartTime
	 */
	@Column(name = "REPROCESSSTARTTIME", nullable = true)
	@Type(type = "timestamp")
	public Date getReprocessStartTime() {
		return reprocessStartTime;
	}

	
	/**
	 * @param reprocessStartTime the reprocessStartTime to set
	 */
	public void setReprocessStartTime(Date reprocessStartTime) {
		this.reprocessStartTime = reprocessStartTime;
	}

	
	/**
	 * @return the reprocessEndTime
	 */
	@Column(name = "REPROCESSENDTIME", nullable = true)
	@Type(type = "timestamp")
	public Date getReprocessEndTime() {
		return reprocessEndTime;
	}

	
	/**
	 * @param reprocessEndTime the reprocessEndTime to set
	 */
	public void setReprocessEndTime(Date reprocessEndTime) {
		this.reprocessEndTime = reprocessEndTime;
	}

	
	/**
	 * @return the failureReason
	 */
	@Column(name = "FAILUREREASON", nullable = true, length = 600)
	public String getFailureReason() {
		return failureReason;
	}

	
	/**
	 * @param failureReason the failureReason to set
	 */
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	
	/**
	 * @return the errorReprocessStatus
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "ERRORREPROCESSSTATUS", nullable = false)
	public FileReprocessStatusEnum getErrorReprocessStatus() {
		return errorReprocessStatus;
	}

	
	/**
	 * @param errorReprocessStatus the errorReprocessStatus to set
	 */
	public void setErrorReprocessStatus(FileReprocessStatusEnum errorReprocessStatus) {
		this.errorReprocessStatus = errorReprocessStatus;
	}
	
	/**
	 * @return the service
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICE_REPROCESS_DETAIL"))
	public Service getService() {
		return service;
	}

	
	/**
	 * @param service the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	
	/**
	 * @return the svctype
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SVCTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICE_ERRORREPROCESSINGDETAIL"))
	public ServiceType getSvctype() {
		return svctype;
	}

	
	/**
	 * @param svctype the svctype to set
	 */
	public void setSvctype(ServiceType svctype) {
		this.svctype = svctype;
	}

	
	/**
	 * @return the serverInstance
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_INSTACNE_REPROCESS_DETAIL"))
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	
	/**
	 * @param serverInstance the serverInstance to set
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}

	
	/**
	 * @return the parser
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PLUGINID", nullable = true, foreignKey = @ForeignKey(name = "FK_PARSER_REPROCESS_DETAIL"))
	public Parser getParser() {
		return parser;
	}

	
	/**
	 * @param parser the parser to set
	 */
	public void setParser(Parser parser) {
		this.parser = parser;
	}

	
	/**
	 * @return the composer
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPOSERID", nullable = true, foreignKey = @ForeignKey(name = "FK_COMPOSER_REPROCESS_DETAIL"))
	public Composer getComposer() {
		return composer;
	}

	
	/**
	 * @param composer the composer to set
	 */
	public void setComposer(Composer composer) {
		this.composer = composer;
	}


	
	/**
	 * @return the filePath
	 */
	@Column(name = "FILEPATH", unique = false, nullable = false, length = 600)
	public String getFilePath() {
		return filePath;
	}


	
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	
	/**
	 * @return the readFilePath
	 */
	@Column(name = "READFILEPATH", unique = false, nullable = false, length = 600)
	public String getReadFilePath() {
		return readFilePath;
	}


	
	/**
	 * @param readFilePath the readFilePath to set
	 */
	public void setReadFilePath(String readFilePath) {
		this.readFilePath = readFilePath;
	}


	
	/**
	 * @return the isCompress
	 */
	@Column(name = "ISCOMPRESS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isCompress() {
		return isCompress;
	}

	
	/**
	 * @param isCompress the isCompress to set
	 */
	public void setCompress(boolean isCompress) {
		this.isCompress = isCompress;
	}


	
	/**
	 * @return the absoluteFilePath
	 */
	@Column(name = "ABSOLUTEFILEPATH", nullable = false, length = 600)
	public String getAbsoluteFilePath() {
		return absoluteFilePath;
	}


	
	/**
	 * @param absoluteFilePath the absoluteFilePath to set
	 */
	public void setAbsoluteFilePath(String absoluteFilePath) {
		this.absoluteFilePath = absoluteFilePath;
	}


	
	/**
	 * @return the fileBackUpPath
	 */
	@Column(name = "FILEBACKUPPATH", nullable = true, length = 600)
	public String getFileBackUpPath() {
		return fileBackUpPath;
	}

	
	/**
	 * @param fileBackUpPath the fileBackUpPath to set
	 */
	public void setFileBackUpPath(String fileBackUpPath) {
		this.fileBackUpPath = fileBackUpPath;
	}


	
	/**
	 * @return the isInputSourceCompress
	 */
	@Column(name = "ISINPUTSOURCECOMPRESS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isInputSourceCompress() {
		return isInputSourceCompress;
	}


	
	/**
	 * @param isInputSourceCompress the isInputSourceCompress to set
	 */
	public void setInputSourceCompress(boolean isInputSourceCompress) {
		this.isInputSourceCompress = isInputSourceCompress;
	}


	
	/**
	 * @return the isModifyFileReprocessed
	 */
	@Column(name = "ISMODIFYFILEREPROCESSED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isModifyFileReprocessed() {
		return isModifyFileReprocessed;
	}


	
	/**
	 * @param isModifyFileReprocessed the isModifyFileReprocessed to set
	 */
	public void setModifyFileReprocessed(boolean isModifyFileReprocessed) {
		this.isModifyFileReprocessed = isModifyFileReprocessed;
	}


	@Column(name = "FILEREPROCESSTYPE", nullable = false, length = 10)
	public String getFileReprocessType() {
		return fileReprocessType;
	}


	public void setFileReprocessType(String fileReprocessType) {
		this.fileReprocessType = fileReprocessType;
	}


}
