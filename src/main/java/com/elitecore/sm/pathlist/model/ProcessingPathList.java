
/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TimeUnitEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.services.model.DuplicateRecordPolicyTypeEnum;

/**
 * @author jay.shah
 *
 */
@Component(value="processingPathList")
@Entity()
@DynamicUpdate
@DiscriminatorValue("PROPL")
@XmlType(propOrder = { "maxFilesCountAlert", "policyAlias", "fileGrepDateEnabled","dateFormat","position","startIndex","endIndex","parentDevice","acrossFileDuplicateCDRCacheLimit", "writeCdrHeaderFooterEnabled","acrossFileDuplicateDateField","acrossFileDuplicateDateFieldFormat","acrossFileDuplicateDateInterval","acrossFileDuplicateDateIntervalType","alertDescription","alertId","duplicateRecordPolicyEnabled","duplicateRecordPolicyType","unifiedFields"})
public class ProcessingPathList extends CommonPathList {

	private static final long serialVersionUID = -7133905717359742941L;
	private Policy policy;
	private String policyAlias;
	private int maxFilesCountAlert=1000;
	private Boolean fileGrepDateEnabled = false;
	private Boolean writeCdrHeaderFooterEnabled= true;
	private String dateFormat = "";
	private Integer startIndex = 0;
	private Integer endIndex = 0;
	private String position = PositionEnum.LEFT.getValue();
	private String referenceDevice;
	private Device parentDevice;
	
	private boolean duplicateRecordPolicyEnabled;
	private String alertId;
	private String alertDescription;
	private DuplicateRecordPolicyTypeEnum duplicateRecordPolicyType=DuplicateRecordPolicyTypeEnum.IN_FILE;
	private String unifiedFields;
	private int acrossFileDuplicateCDRCacheLimit;
	private TimeUnitEnum acrossFileDuplicateDateIntervalType;
	private String acrossFileDuplicateDateField;
	private int acrossFileDuplicateDateInterval;
	private String acrossFileDuplicateDateFieldFormat;
	
 
	/**
	 * @return the maxFilesCountAlert
	 */
	@Column(name = "MAXFILECOUNTALERT", length = 5)
	public int getMaxFilesCountAlert() {
		return maxFilesCountAlert;
	}
	
	/**
	 * @param maxFilesCountAlert
	 *            the maxFilesCountAlert to set
	 */

	public void setMaxFilesCountAlert(int maxFilesCountAlert) {
		this.maxFilesCountAlert = maxFilesCountAlert;
	}

	/**
	 * @return the policy
	 */
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "POLICYID", nullable = true, foreignKey = @ForeignKey(name = "FK_PROCESSING_PATHLIST_POLICY"))
	@XmlTransient
	public Policy getPolicy() {
		return policy;
	}

	/**
	 * @param policy the policy to set
	 */
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}

	/**
	 * @return the policyAlias
	 */
	@Transient
	public String getPolicyAlias() {
		return policyAlias;
	}

	/**
	 * @param policyAlias the policyAlias to set
	 */
	public void setPolicyAlias(String policyAlias) {
		this.policyAlias = policyAlias;
	}

	
	/**
	 * @return the fileGrepDateEnabled
	 */
	@XmlElement
	@Column(name = "FILEGREPDATEENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	public Boolean getFileGrepDateEnabled() {
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
	public Integer getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the endIndex
	 */
	@XmlElement
	@Column(name = "ENDINDEX", length = 7)
	public Integer getEndIndex() {
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
	 * @param fileGrepDateEnabled the fileGrepDateEnabled to set
	 */
	public void setFileGrepDateEnabled(Boolean fileGrepDateEnabled) {
		this.fileGrepDateEnabled = fileGrepDateEnabled;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name = "REFERENCEDEVICE", nullable = true, length = 400)
	@XmlTransient
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
	
	/**
	 * @return the duplicateRecordPolicyEnabled
	 */
	@Column(name = "DUPLICATERECORDPOLICYENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isDuplicateRecordPolicyEnabled() {
		return duplicateRecordPolicyEnabled;
	}
	
	/**
	 * @param duplicateRecordPolicyEnabled
	 *            the duplicateRecordPolicyEnabled to set
	 */
	public void setDuplicateRecordPolicyEnabled(
			boolean duplicateRecordPolicyEnabled) {
		this.duplicateRecordPolicyEnabled = duplicateRecordPolicyEnabled;
	}
	
	/**
	 * @return the alertId
	 */
	@Column(name = "ALERTID", nullable = true, length = 100)
	@XmlElement
	public String getAlertId() {
		return alertId;
	}
	
	/**
	 * @param alertId
	 *            the alertId to set
	 */
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}

	/**
	 * @return the alertDescription
	 */
	@Column(name = "ALERTDESCRIPTION", nullable = true, length = 100)
	@XmlElement
	public String getAlertDescription() {
		return alertDescription;
	}
	
	/**
	 * @param alertDescription
	 *            the alertDescription to set
	 */
	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}
	
	/**
	 * @return the acrossFileDuplicateDateIntervalType
	 */
	@Column(name = "ACROSSFILEDUPLDATEINTYPE", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	@XmlElement
	public TimeUnitEnum getAcrossFileDuplicateDateIntervalType() {
		return acrossFileDuplicateDateIntervalType;
	}
	
	/**
	 * @param acrossFileDuplicateDateIntervalType
	 *            the acrossFileDuplicateDateIntervalType to set
	 */
	public void setAcrossFileDuplicateDateIntervalType(
			TimeUnitEnum acrossFileDuplicateDateIntervalType) {
		this.acrossFileDuplicateDateIntervalType = acrossFileDuplicateDateIntervalType;
	}
	
	/**
	 * @return the unifiedFields
	 */
	@Column(name = "UNIFIEDFILEDS", nullable = true, length = 4000)
	@XmlElement
	public String getUnifiedFields() {
		return unifiedFields;
	}
	
	/**
	 * @param unifiedFields
	 *            the unifiedFields to set
	 */
	public void setUnifiedFields(String unifiedFields) {
		this.unifiedFields = unifiedFields;
	}
	
	/**
	 * @return the acrossFileDuplicateDateField
	 */
	@Column(name = "ACROSSFILEDUPLICATEDATEFIELD", nullable = true, length = 100)
	@XmlElement
	public String getAcrossFileDuplicateDateField() {
		return acrossFileDuplicateDateField;
	}
	
	/**
	 * @param acrossFileDuplicateDateField
	 *            the acrossFileDuplicateDateField to set
	 */
	public void setAcrossFileDuplicateDateField(
			String acrossFileDuplicateDateField) {
		this.acrossFileDuplicateDateField = acrossFileDuplicateDateField;
	}
	
	/**
	 * @return the duplicateRecordPolicyType
	 */
	@Column(name = "DUPLICATERECORDPOLICYTYPE", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	@XmlElement
	public DuplicateRecordPolicyTypeEnum getDuplicateRecordPolicyType() {
		return duplicateRecordPolicyType;
	}
	
	/**
	 * @param duplicateRecordPolicyType
	 *            the duplicateRecordPolicyType to set
	 */
	public void setDuplicateRecordPolicyType(
			DuplicateRecordPolicyTypeEnum duplicateRecordPolicyType) {
		this.duplicateRecordPolicyType = duplicateRecordPolicyType;
	}
	
	/**
	 * @return the acrossFileDuplicateDateInterval
	 */
	@Column(name = "ACROSSFILEDUPDATEINTERVAL", nullable = true, length = 100)
	@XmlElement
	public int getAcrossFileDuplicateDateInterval() {
		return acrossFileDuplicateDateInterval;
	}
	
	/**
	 * @param acrossFileDuplicateDateInterval
	 *            the acrossFileDuplicateDateInterval to set
	 */
	public void setAcrossFileDuplicateDateInterval(
			int acrossFileDuplicateDateInterval) {
		this.acrossFileDuplicateDateInterval = acrossFileDuplicateDateInterval;
	}

	/**
	 * @return the acrossFileDuplicateCDRCacheLimit
	 */
	@Column(name = "ACROSSFILEDUPCDRCACHELIMIT", nullable = true, length = 100)
	@XmlElement
	public int getAcrossFileDuplicateCDRCacheLimit() {
		return acrossFileDuplicateCDRCacheLimit;
	}
	
	/**
	 * @param acrossFileDuplicateCDRCacheLimit
	 *            the acrossFileDuplicateCDRCacheLimit to set
	 */
	public void setAcrossFileDuplicateCDRCacheLimit(
			int acrossFileDuplicateCDRCacheLimit) {
		this.acrossFileDuplicateCDRCacheLimit = acrossFileDuplicateCDRCacheLimit;
	}
	
	/**
	 * @return the fileHeaderFooterEnabled
	 */
	@Column(name = "WRITECDRHEADERFOOTERENABLED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public Boolean getwriteCdrHeaderFooterEnabled() {
		return writeCdrHeaderFooterEnabled;
	}
	/**
	 * @param fileHeaderFooterEnabled
	 *            the fileHeaderFooterEnabled to set
	 */
	public void setwriteCdrHeaderFooterEnabled(Boolean writeCdrHeaderFooterEnabled) {
		this.writeCdrHeaderFooterEnabled = writeCdrHeaderFooterEnabled;
	}
	/**
	 * @return the acrossFileDuplicateDateFieldFormat
	 */
	@Column(name = "ACROSSFILEDUPLICATEDATEFIELDFORMAT", nullable = true, length = 100)
	@XmlElement
	public String getAcrossFileDuplicateDateFieldFormat() {
		return acrossFileDuplicateDateFieldFormat;
	}
	
	/**
	 * @param acrossFileDuplicateDateFieldFormat
	 *            the acrossFileDuplicateDateFieldFormat to set
	 */
	public void setAcrossFileDuplicateDateFieldFormat(
			String acrossFileDuplicateDateFieldFormat) {
		this.acrossFileDuplicateDateFieldFormat = acrossFileDuplicateDateFieldFormat;
		
		
	}
}
