/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.DuplicateCheckParamEnum;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.device.model.Device;

/**
 * @author jay.shah
 *
 */

@Component(value="collectionDriverPathList")
@Entity()
@DynamicUpdate
@DiscriminatorValue("CDPL")
@XmlType(propOrder = { "maxFilesCountAlert", "remoteFileAction","remoteFileActionParamName","remoteFileActionValue","remoteFileActionParamNameTwo","remoteFileActionValueTwo",
		"fileGrepDateEnabled","dateFormat","position","startIndex","endIndex","fileSeqAlertEnabled","seqStartIndex","seqEndIndex",
		"referenceDevice","parentDevice","duplicateCheckParamName","duplicateFileSuffix","timeInterval","missingFileSequenceId","fileSizeCheckEnabled","fileSizeCheckMinValue","fileSizeCheckMaxValue",
		"validFileTimeInterval"
})
public class CollectionDriverPathList extends CommonPathList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 204370631253286769L;
	private int maxFilesCountAlert=1000;
	private String remoteFileAction = FilterActionEnum.NA.getValue();
	private String remoteFileActionParamName= FileActionParamEnum.NA.getValue();
	private String remoteFileActionValue;
	private String remoteFileActionParamNameTwo= FileActionParamEnum.EXTENSION.getValue();
	private String remoteFileActionValueTwo;
	private boolean fileGrepDateEnabled;
	private String dateFormat;
	private int startIndex=0;
	private int endIndex=0;
	private String position=PositionEnum.LEFT.getValue();
	private boolean fileSeqAlertEnabled;
	private int seqStartIndex=0;
	private int seqEndIndex=0;
	//private int maxCounterLimit=10;
	private FileSequenceMgmt missingFileSequenceId;
	private String duplicateCheckParamName = DuplicateCheckParamEnum.NAME.getValue();
	private boolean duplicateFileSuffix;
	private int timeInterval=0;
	private String referenceDevice;
	private String deviceName;
	private Device parentDevice;
	private boolean fileSizeCheckEnabled;
	private int fileSizeCheckMinValue=0;
	private int fileSizeCheckMaxValue=0;
	private int validFileTimeInterval=1;

	/**
	 * @return the maxFilesCountAlert
	 */
	@XmlElement
	@Column(name = "MAXFILECOUNTALERT", length = 5)
	public int getMaxFilesCountAlert() {
		return maxFilesCountAlert;
	}

	/**
	 * @return the remoteFileAction
	 */
	@XmlElement
	@Column(name = "REMOTEFILEACTION", length = 20)
	public String getRemoteFileAction() {
		return remoteFileAction;
	}

	/**
	 * @return the remoteFileActionParamName
	 */
	@XmlElement
	@Column(name = "REMOTEFILEACTIONNAME", length = 20)
	public String getRemoteFileActionParamName() {
		return remoteFileActionParamName;
	}

	/**
	 * @return the remoteFileActionValue
	 */
	 @XmlElement(nillable=true)
	@Column(name = "REMOTEFILEACTIONVALUE", length = 255)
	public String getRemoteFileActionValue() {
		return remoteFileActionValue;
	}

	@XmlElement(nillable=true)
	@Column(name = "REMOTEFILEACTIONNAMETWO", length = 20)
	public String getRemoteFileActionParamNameTwo() {
		return remoteFileActionParamNameTwo;
	}

	@XmlElement(nillable=true)
	@Column(name = "REMOTEFILEACTIONVALUETWO", length = 255)
	public String getRemoteFileActionValueTwo() {
		return remoteFileActionValueTwo;
	}

	/**
	 * @return the fileGrepDateEnabled
	 */
	@XmlElement
	@Column(name = "FILEGREPDATEENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getFileGrepDateEnabled() {
		return fileGrepDateEnabled;
	}

	/**
	 * @return the dateFormat
	 */
	 @XmlElement(nillable=true)
	@Column(name = "DATEFORMAT", length = 30)
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @return the startIndex
	 */
	 @XmlElement
	@Column(name = "STARTINDEX", length = 7)
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the endIndex
	 */
	 @XmlElement
	@Column(name = "ENDINDEX", length = 7)
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the position
	 */
	 @XmlElement
	@Column(name = "POSITION", length = 10)
	public String getPosition() {
		return position;
	}

	/**
	 * @return the fileSeqAlertEnabled
	 */
	 @XmlElement
	 @Column(name = "FILESEQALERTENABLED")
	 @org.hibernate.annotations.Type(type = "yes_no")
	public boolean getFileSeqAlertEnabled() {
		return fileSeqAlertEnabled;
	}

	/**
	 * @return the seqStartIndex
	 */
	@XmlElement
	@Column(name = "SEQSTARTINDEX", length = 7)
	public int getSeqStartIndex() {
		return seqStartIndex;
	}

	/**
	 * @return the seqEndIndex
	 */
	@XmlElement
	@Column(name = "SEQENDINDEX", length = 7)
	public int getSeqEndIndex() {
		return seqEndIndex;
	}

	/**
	 * @return the maxCounterLimit
	 */
	/* @XmlElement
	@Column(name = "MAXCOUNTERLIMIT", length = 7)
	public int getMaxCounterLimit() {
		return maxCounterLimit;
	}*/
	 
	/**
	 * @return the getDuplicateCheckParamName
	 */
	@XmlElement
	@Column(name = "DUPLICATECHECKPARAMNAME", length = 250)
	public String getDuplicateCheckParamName() {
		return duplicateCheckParamName;
	}
	
	/**
	* @return the timeInterval
	*/
	@XmlElement
	@Column(name = "TIMEINTERVAL", length = 2)
	public int getTimeInterval() {
		return timeInterval;
	}
	
	@XmlElement
	@Column(name = "FILESIZECHECKENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getFileSizeCheckEnabled() {
		return fileSizeCheckEnabled;
	}
	
	@XmlElement
	@Column(name = "FILESIZECHECKMINVALUE", length = 20)
	public int getFileSizeCheckMinValue() {
		return fileSizeCheckMinValue;
	}
	 
	@XmlElement
	@Column(name = "FILESIZECHECKMAXVALUE", length = 20)
	public int getFileSizeCheckMaxValue() {
		return fileSizeCheckMaxValue;
	}
	 
	/**
	 * @param maxFilesCountAlert
	 *            the maxFilesCountAlert to set
	 */

	public void setMaxFilesCountAlert(int maxFilesCountAlert) {
		this.maxFilesCountAlert = maxFilesCountAlert;
	}

	/**
	 * @param remoteFileAction
	 *            the remoteFileAction to set
	 */
	public void setRemoteFileAction(String remoteFileAction) {
		this.remoteFileAction = remoteFileAction;
	}

	/**
	 * @param remoteFileActionParamName
	 *            the remoteFileActionParamName to set
	 */
	public void setRemoteFileActionParamName(String remoteFileActionParamName) {
		this.remoteFileActionParamName = remoteFileActionParamName;
	}
	
	/**
	 * @param remoteFileActionValue
	 *            the remoteFileActionValue to set
	 */
	public void setRemoteFileActionValue(String remoteFileActionValue) {
		this.remoteFileActionValue = remoteFileActionValue;
	}

	public void setRemoteFileActionParamNameTwo(String remoteFileActionParamNameTwo) {
		this.remoteFileActionParamNameTwo = remoteFileActionParamNameTwo;
	}
	
	public void setRemoteFileActionValueTwo(String remoteFileActionValueTwo) {
		this.remoteFileActionValueTwo = remoteFileActionValueTwo;
	}
	
	/**
	 * @param fileGrepDateEnabled
	 *            the fileGrepDateEnabled to set
	 */
	public void setFileGrepDateEnabled(boolean fileGrepDateEnabled) {
		this.fileGrepDateEnabled = fileGrepDateEnabled;
	}

	/**
	 * @param dateFormat
	 *            the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param startIndex
	 *            the startIndex to set
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @param endIndex
	 *            the endIndex to set
	 */
	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @param fileSeqAlertEnabled
	 *            the fileSeqAlertEnabled to set
	 */
	public void setFileSeqAlertEnabled(boolean fileSeqAlertEnabled) {
		this.fileSeqAlertEnabled = fileSeqAlertEnabled;
	}

	/**
	 * @param seqStartIndex
	 *            the seqStartIndex to set
	 */
	public void setSeqStartIndex(int seqStartIndex) {
		this.seqStartIndex = seqStartIndex;
	}

	/**
	 * @param seqEndIndex
	 *            the seqEndIndex to set
	 */
	public void setSeqEndIndex(int seqEndIndex) {
		this.seqEndIndex = seqEndIndex;
	}

	/**
	 * @param maxCounterLimit
	 *            the maxCounterLimit to set
	 */
	/*public void setMaxCounterLimit(int maxCounterLimit) {
		this.maxCounterLimit = maxCounterLimit;
	}*/
	
	/**
	 * @param duplicateCheckParamName
	 *            the duplicateCheckParamName to set
	 */
	public void setDuplicateCheckParamName(String duplicateCheckParamName) {
		this.duplicateCheckParamName = duplicateCheckParamName;
	}

	/**
	 * @param timeInterval
	 *            the timeInterval to set
	 */
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public void setFileSizeCheckEnabled(boolean fileSizeCheckEnabled) {
		this.fileSizeCheckEnabled=fileSizeCheckEnabled;
	}
	
	public void setFileSizeCheckMinValue(int fileSizeCheckMinValue) {
		this.fileSizeCheckMinValue=fileSizeCheckMinValue;
	}
	 
	public void setFileSizeCheckMaxValue(int fileSizeCheckMaxValue) {
		this.fileSizeCheckMaxValue=fileSizeCheckMaxValue;
	}
	
	@XmlElement
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name = "FILESEQMGMTID", nullable = true, foreignKey = @ForeignKey(name = "FK_PATHLISTFILESEQMGMTID_ID"))
	public FileSequenceMgmt getMissingFileSequenceId() {
		return missingFileSequenceId;
	}

	public void setMissingFileSequenceId(FileSequenceMgmt missingFileSequenceId) {
		this.missingFileSequenceId = missingFileSequenceId;
	}
	
	@Column(name = "REFERENCEDEVICE", nullable = true, length = 400)
	@XmlElement
	public String getReferenceDevice() {
		return referenceDevice;
	}
	public void setReferenceDevice(String referenceDevice) {
		this.referenceDevice = referenceDevice;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PARENTDEVICE", nullable = true, foreignKey = @ForeignKey(name = "FK_PARENTDEVICE_ID"))
	@XmlElement
	@DiffIgnore
	public Device getParentDevice() {
		return parentDevice;
	}

	public void setParentDevice(Device parentDevice) {
		this.parentDevice = parentDevice;
	}
	
	@XmlElement
	@Column(name = "DUPFILESUFFIX")
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getDuplicateFileSuffix() {
		return duplicateFileSuffix;
	}

	public void setDuplicateFileSuffix(boolean duplicateFileSuffix) {
		this.duplicateFileSuffix = duplicateFileSuffix;
	}
	
    /**
	* @return the validFileTimeInterval
	*/
	@XmlElement
	@Column(name = "VALIDFILETIMEINTERVAL", length = 2)
	public int getValidFileTimeInterval() {
		return validFileTimeInterval;
	}

	public void setValidFileTimeInterval(int validFileTimeInterval) {
		this.validFileTimeInterval = validFileTimeInterval;
	}

}
