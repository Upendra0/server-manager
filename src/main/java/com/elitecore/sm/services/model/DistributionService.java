/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.FileMergeGroupingByEnum;
import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;



/**
 * @author vandana.awatramani
 * 
 */
@Component(value="distributionService")
@Entity()
@Table(name = "TBLTDISTSVC")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
@XmlRootElement
public class DistributionService extends Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3523140649417019113L;
	private boolean thirdPartyTransferEnabled = false;
	private String timestenDatasourceName;
	private int processRecordLimit = 3000;
	private int writeRecordLimit = -1;
	private boolean fileMergeEnabled = false;
	private String fileMergeGroupingBy = FileMergeGroupingByEnum.ALL.getValue();
	private boolean remainingFileMergeEnabled = false;
	private FileGroupingParameterParsing fileGroupingParameter;
	
	private ServiceSchedulingParams serviceSchedulingParams;
	private String errorPath;

	
	/**
	 * @return the thirdPartyTransferEnabled
	 */
	@Column(name = "THIRDPARTYTRANSFERENABLED", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isThirdPartyTransferEnabled() {
		return thirdPartyTransferEnabled;
	}

	/**
	 * @return the timestenDatasourceName
	 */
	@Column(name = "TIMESTENDATASOURCENAME", nullable = true, length = 500)
	@XmlElement
	public String getTimestenDatasourceName() {
		return timestenDatasourceName;
	}

	/**
	 * @return the processRecordLimit
	 */
	@Column(name = "PROCESSRECLIMIT", nullable = false, length = 5)
	@XmlElement
	public int getProcessRecordLimit() {
		return processRecordLimit;
	}

	/**
	 * @return the writeRecordLimit
	 */
	@Column(name = "WRITERECORDLIMIT", nullable = false, length = 5)
	@XmlElement
	public int getWriteRecordLimit() {
		return writeRecordLimit;
	}

	/**
	 * 
	 * @return the fileMergeGroupingBy
	 */
	@Column(name = "FILEMERGEGROUPINGBY", nullable = true, length = 20)
	@XmlElement
	public String getFileMergeGroupingBy() {
		return fileMergeGroupingBy;
	}
	
	/**
	 * @return the fileMergeEnabled
	 */
	@Column(name = "FILEMERGEENABLED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileMergeEnabled() {
		return fileMergeEnabled;
	}

	/**
	 * @return the remainingFileMergeEnabled
	 */
	@Column(name = "REMAININGFILEMERGEENABLED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isRemainingFileMergeEnabled() {
		return remainingFileMergeEnabled;
	}
	
	/**
	 * @return the fileGroupingParameter
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_DISTSVC_FILEGRP"))
	@XmlElement
	public FileGroupingParameterParsing getFileGroupingParameter() {
		return fileGroupingParameter;
	}

	/**
	 * @return the serviceSchedulingParams
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.EAGER, optional = false,cascade=CascadeType.ALL)
	@JoinColumn(name = "SCHEDULEID", nullable = false, foreignKey = @ForeignKey(name = "FK_DSVC_Schedule"))
	public ServiceSchedulingParams getServiceSchedulingParams() {
		return serviceSchedulingParams;
	}

	/**
	 * @param thirdPartyTransferEnabled
	 *            the thirdPartyTransferEnabled to set
	 */
	public void setThirdPartyTransferEnabled(boolean thirdPartyTransferEnabled) {
		this.thirdPartyTransferEnabled = thirdPartyTransferEnabled;
	}

	/**
	 * @param timestenDatasourceName
	 *            the timestenDatasourceName to set
	 */
	public void setTimestenDatasourceName(String timestenDatasourceName) {
		this.timestenDatasourceName = timestenDatasourceName;
	}

	/**
	 * @param processRecordLimit
	 *            the processRecordLimit to set
	 */
	public void setProcessRecordLimit(int processRecordLimit) {
		this.processRecordLimit = processRecordLimit;
	}

	/**
	 * @param writeRecordLimit
	 *            the writeRecordLimit to set
	 */
	public void setWriteRecordLimit(int writeRecordLimit) {
		this.writeRecordLimit = writeRecordLimit;
	}

	/**
	 * 
	 * @param fileMergeGroupingBy
	 */
	public void setFileMergeGroupingBy(String fileMergeGroupingBy) {
		this.fileMergeGroupingBy = fileMergeGroupingBy;
	}

	/**
	 * @param fileMergeEnabled
	 *            the fileMergeEnabled to set
	 */
	public void setFileMergeEnabled(boolean fileMergeEnabled) {
		this.fileMergeEnabled = fileMergeEnabled;
	}
	
	/**
	 * @param remainingFileMergeEnabled
	 *            the remainingFileMergeEnabled to set
	 */
	public void setRemainingFileMergeEnabled(boolean remainingFileMergeEnabled) {
		this.remainingFileMergeEnabled = remainingFileMergeEnabled;
	}
	
	/**
	 * @param serviceSchedulingParams
	 *            the serviceSchedulingParams to set
	 */
	public void setServiceSchedulingParams(ServiceSchedulingParams serviceSchedulingParams) {
		this.serviceSchedulingParams = serviceSchedulingParams;
	}


	/**
	 * @param fileGroupingParameter the fileGroupingParameter to set
	 */
	public void setFileGroupingParameter(FileGroupingParameterParsing fileGroupingParameter) {
		this.fileGroupingParameter = fileGroupingParameter;
	}
	
	/**
	 * Get error path
	 * @return the error path
	 */
	@XmlElement
	@Column(name = "ERRORPATH", nullable = true, length = 600)
	public String getErrorPath() {
		return errorPath;
	}

	/**
	 * Set error path
	 * @param errorPath the error path
	 */
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}

}
