package com.elitecore.sm.services.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import com.elitecore.sm.pathlist.model.FileGroupingParameterProcessing;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="processingService")
@Entity()
@Table(name = "TBLTPROCESSINGSERVICE")
@DynamicUpdate
@DiscriminatorValue("PROSRVC")
@PrimaryKeyJoinColumn(name = "SERVICEID")
@XmlRootElement
public class ProcessingService extends Service {

	private static final long serialVersionUID = 6652408889829078613L;
	private boolean fileSeqOrderEnable;
	private int minFileRange = 1;
	private int maxFileRange = 300;
	private int recordBatchSize;
	private boolean globalSeqEnabled;
	private String globalSeqDeviceName;
	private int globalSeqMaxLimit=-1;
	private String errorPath;
	private int acrossFileDuplicatePurgeCacheInterval;
	private int noFileAlert;
	
	private FileGroupingParameterProcessing fileGroupingParameter;
	private boolean storeCDRFileSummaryDB = false;
	private String dateFieldForSummary;
	private CDRDateSummaryTypeEnum typeForSummary;
	
	private boolean overrideFileDateEnabled = false;
	private String overrideFileDateType = CDRFileDateTypeEnum.MAXIMUM.getValue();


	/**
	 * @return the fileSeqOrderEnable
	 */
	@Column(name = "SEQORDERENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileSeqOrderEnable() {
		return fileSeqOrderEnable;
	}

	/**
	 * @return the recordBatchSize
	 */
	@Column(name = "RECBATCHSIZE", nullable = true, length = 3)
	@XmlElement
	public int getRecordBatchSize() {
		return recordBatchSize;
	}


	/**
	 * @return the globalSeqEnabled
	 */
	@Column(name = "GLOBALSEQENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isGlobalSeqEnabled() {
		return globalSeqEnabled;
	}

	/**
	 * @return the globalSeqDeviceName
	 */
	@Column(name = "GLOBALSEQDEVICENAME", nullable = true, length = 1)
	@XmlElement
	public String getGlobalSeqDeviceName() {
		return globalSeqDeviceName;
	}

	/**
	 * @return the globalSeqMaxLimit
	 */
	@Column(name = "GLOBALSEQMAXLIMIT", nullable = true, length = 100)
	@XmlElement
	public int getGlobalSeqMaxLimit() {
		return globalSeqMaxLimit;
	}

	/**
	 * @return the acrossFileDuplicatePurgeCacheInterval
	 */
	@Column(name = "ACROSSFILEDUPPURGECACHEINT", nullable = true, length = 100)
	@XmlElement
	public int getAcrossFileDuplicatePurgeCacheInterval() {
		return acrossFileDuplicatePurgeCacheInterval;
	}

	
	/**
	 * @return the processingFileGroupParams
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_Pro_FILEGRP"))
	public FileGroupingParameterProcessing getFileGroupingParameter() {
		return fileGroupingParameter;
	}


	/**
	 * @param fileSeqOrderEnable
	 *            the fileSeqOrderEnable to set
	 */
	public void setFileSeqOrderEnable(boolean fileSeqOrderEnable) {
		this.fileSeqOrderEnable = fileSeqOrderEnable;
	}

	/**
	 * @param recordBatchSize
	 *            the recordBatchSize to set
	 */
	public void setRecordBatchSize(int recordBatchSize) {
		this.recordBatchSize = recordBatchSize;
	}


	/**
	 * @param globalSeqEnabled
	 *            the globalSeqEnabled to set
	 */
	public void setGlobalSeqEnabled(boolean globalSeqEnabled) {
		this.globalSeqEnabled = globalSeqEnabled;
	}

	/**
	 * @param globalSeqDeviceName
	 *            the globalSeqDeviceName to set
	 */
	public void setGlobalSeqDeviceName(String globalSeqDeviceName) {
		this.globalSeqDeviceName = globalSeqDeviceName;
	}

	/**
	 * @param globalSeqMaxLimit
	 *            the globalSeqMaxLimit to set
	 */
	public void setGlobalSeqMaxLimit(int globalSeqMaxLimit) {
		this.globalSeqMaxLimit = globalSeqMaxLimit;
	}

	/**
	 * @param acrossFileDuplicatePurgeCacheInterval
	 *            the acrossFileDuplicatePurgeCacheInterval to set
	 */
	public void setAcrossFileDuplicatePurgeCacheInterval(
			int acrossFileDuplicatePurgeCacheInterval) {
		this.acrossFileDuplicatePurgeCacheInterval = acrossFileDuplicatePurgeCacheInterval;
	}

	/**
	 * @param processingFileGroupParams
	 *            the processingFileGroupParams to set
	 */
	public void setFileGroupingParameter(
			FileGroupingParameterProcessing fileGroupingParameter) {
		this.fileGroupingParameter = fileGroupingParameter;
	}
	
	/**
	 * @return the noFileAlert
	 */
	@XmlElement
	@Column(name = "NOFILEALERT", nullable = true, length = 3)
	public int getNoFileAlert() {
		return noFileAlert;
	}
	
	/**
	 * @param noFileAlert
	 *            the noFileAlert to set
	 */
	public void setNoFileAlert(int noFileAlert) {
		this.noFileAlert = noFileAlert;
	}
	
	

	/**
	 * @return the minFileRange
	 */
	@XmlElement
	@Column(name = "MINFILERANGE", nullable = true)
	public int getMinFileRange() {
		return minFileRange;
	}

	/**
	 * @param minFileRange the minFileRange to set
	 */
	public void setMinFileRange(int minFileRange) {
		this.minFileRange = minFileRange;
	}
	

	/**
	 * @return the maxFileRange
	 */
	@XmlElement
	@Column(name = "MAXFILERANGE", nullable = true)
	public int getMaxFileRange() {
		return maxFileRange;
	}

	/**
	 * @param maxFileRange the maxFileRange to set
	 */
	public void setMaxFileRange(int maxFileRange) {
		this.maxFileRange = maxFileRange;
	}
	
	/**
	 * @return the maxFileRange
	 */
	@XmlElement
	@Column(name = "ERRORPATH", nullable = true)
	public String getErrorPath() {
		return errorPath;
	}

	/**
	 * @param maxFileRange the maxFileRange to set
	 */
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	
	}
	/**
	 * @return cdr summary in db enabled
	 */
	@Column(name = "CDRSUMMARYDB", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isStoreCDRFileSummaryDB() {
		return storeCDRFileSummaryDB;
	}
	
	/**
	 * @param storeCDRFileSummaryDB 
	 */
	public void setStoreCDRFileSummaryDB(boolean storeCDRFileSummaryDB) {
		this.storeCDRFileSummaryDB = storeCDRFileSummaryDB;
	}

	/**
	 * @return the dateFieldForSummary
	 */
	@Column(name = "DATEFIELDFORSUMMARY", nullable = true, length = 50)
	@XmlElement
	public String getDateFieldForSummary() {
		return dateFieldForSummary;
	}
	public void setDateFieldForSummary(String dateFieldForSummary) {
		this.dateFieldForSummary = dateFieldForSummary;
	}
	/**
	 * @return the typeForSummary
	 */
	@XmlElement
	@Column(name = "DATETYPEFORSUMMARY", nullable = true, length = 15)
	@Enumerated(EnumType.STRING)
	public CDRDateSummaryTypeEnum getTypeForSummary() {
		return typeForSummary;
	}

	/**
	 * @return the overrideFileDateEnabled
	 */
	@XmlElement
	@Column(name = "OVERRIDEFILEDATEENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getOverrideFileDateEnabled() {
		return overrideFileDateEnabled;
	}

	/**
	 * @return the overrideFileDateType
	 */
	@XmlElement
	@Column(name = "OVERRIDEFILEDATETYPE", nullable = true, length = 15)
	public String getOverrideFileDateType() {
		return overrideFileDateType;
	}
	/**
	 * @param typeForSummary
	 *            the typeForSummary to set
	 */
	public void setTypeForSummary(CDRDateSummaryTypeEnum typeForSummary) {
		this.typeForSummary = typeForSummary;
	}

	/**
	 * @param overrideFileDateEnabled
	 *            the overrideFileDateEnabled to set
	 */
	public void setOverrideFileDateEnabled(boolean overrideFileDateEnabled) {
		this.overrideFileDateEnabled = overrideFileDateEnabled;
	}

	/**
	 * @param overrideFileDateType
	 *            the overrideFileDateType to set
	 */
	public void setOverrideFileDateType(String overrideFileDateType) {
		this.overrideFileDateType = overrideFileDateType;
	}
	

	
}
