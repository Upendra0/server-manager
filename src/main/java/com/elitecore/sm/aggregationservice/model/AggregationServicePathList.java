package com.elitecore.sm.aggregationservice.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.pathlist.model.CommonPathList;

@Entity()
@DynamicUpdate
@DiscriminatorValue("APL")
@Component(value="aggregationPathList")
@XmlType(propOrder = { "maxFilesCountAlert", "fileGrepDateEnabled", "dateFormat", "startIndex","endIndex","position","wPathNonAggregate","wPathAggregateError","oFilePathName","oFileMinRange","oFileMaxRange","oFileSeqEnables"
,"oFilePathNameForNonAgg","oFileMinRangeForNonAgg","oFileMaxRangeForNonAgg","oFileSeqEnablesForNonAgg",
"oFilePathNameForError","oFileMinRangeForError","oFileMaxRangeForError","oFileSeqEnablesForError","parentDevice"})

public class AggregationServicePathList extends CommonPathList {

	private static final long serialVersionUID = -6126790950297804767L;

	private int maxFilesCountAlert=1000;
	private boolean fileGrepDateEnabled;
	private String dateFormat = "";
	private Integer startIndex = 0;
	private Integer endIndex = 0;
	private String position = PositionEnum.LEFT.getValue();
	private String wPathNonAggregate = "";
	private String wPathAggregateError = "";
	private String oFilePathName = "";
	private boolean oFileSeqEnables;
	private Integer oFileMinRange = 1;
	private Integer oFileMaxRange = 100;
	private String oFilePathNameForNonAgg = "";
	private boolean oFileSeqEnablesForNonAgg;
	private Integer oFileMinRangeForNonAgg = 1;
	private Integer oFileMaxRangeForNonAgg = 100;
	private String oFilePathNameForError = "";
	private boolean oFileSeqEnablesForError;
	private Integer oFileMinRangeForError = 1;
	private Integer oFileMaxRangeForError = 100;
	private String referenceDevice;
	private Device parentDevice;
	
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
	 * @param fileGrepDateEnabled the fileGrepDateEnabled to set
	 */
	public void setFileGrepDateEnabled(boolean fileGrepDateEnabled) {
		this.fileGrepDateEnabled = fileGrepDateEnabled;
	}

	@XmlElement
	@Column(name = "MAXFILECOUNTALERT", length = 5)
	public int getMaxFilesCountAlert() {
		return maxFilesCountAlert;
	}

	public void setMaxFilesCountAlert(int maxFilesCountAlert) {
		this.maxFilesCountAlert = maxFilesCountAlert;
	}

	@XmlElement(nillable=true)
	@Column(name = "DATEFORMAT", length = 30)
	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@XmlElement
	@Column(name = "STARTINDEX", length = 7)
	public Integer getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}
	
	@XmlElement
	@Column(name = "ENDINDEX", length = 7)
	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	@XmlElement
	@Column(name = "POSITION", length = 10)
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@XmlElement
	@Column(name = "WPATHNONAGGREGATE", length = 200)
	public String getwPathNonAggregate() {
		return wPathNonAggregate;
	}

	public void setwPathNonAggregate(String wPathNonAggregate) {
		this.wPathNonAggregate = wPathNonAggregate;
	}

	@XmlElement
	@Column(name = "WPATHAGGREGATEERROR", length = 200)
	public String getwPathAggregateError() {
		return wPathAggregateError;
	}

	public void setwPathAggregateError(String wPathAggregateError) {
		this.wPathAggregateError = wPathAggregateError;
	}

	@XmlElement
	@Column(name = "OUTPUTPLNAME", length = 250)
	public String getoFilePathName() {
		return oFilePathName;
	}

	public void setoFilePathName(String oFilePathName) {
		this.oFilePathName = oFilePathName;
	}

	@XmlElement
	@Column(name = "MINRANGE")
	public Integer getoFileMinRange() {
		return oFileMinRange;
	}

	public void setoFileMinRange(Integer oFileMinRange) {
		this.oFileMinRange = oFileMinRange;
	}

	@XmlElement
	@Column(name = "MAXRANGE")
	public Integer getoFileMaxRange() {
		return oFileMaxRange;
	}

	public void setoFileMaxRange(Integer oFileMaxRange) {
		this.oFileMaxRange = oFileMaxRange;
	}

	@Column(name = "OUTPUTFILESEQENABLES", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getoFileSeqEnables() {
		return oFileSeqEnables;
	}

	public void setoFileSeqEnables(boolean oFileSeqEnables) {
		this.oFileSeqEnables = oFileSeqEnables;
	}

	@XmlElement
	@Column(name = "OUTPUTPLNAMEFORNONAGG", length = 250)
	public String getoFilePathNameForNonAgg() {
		return oFilePathNameForNonAgg;
	}

	public void setoFilePathNameForNonAgg(String oFilePathNameForNonAgg) {
		this.oFilePathNameForNonAgg = oFilePathNameForNonAgg;
	}

	@Column(name = "OUTPUTFILESEQENABLESFORNONAGG", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isoFileSeqEnablesForNonAgg() {
		return oFileSeqEnablesForNonAgg;
	}

	public void setoFileSeqEnablesForNonAgg(boolean oFileSeqEnablesForNonAgg) {
		this.oFileSeqEnablesForNonAgg = oFileSeqEnablesForNonAgg;
	}

	@Column(name = "MINRANGEFORNONAGG")
	@XmlElement
	public Integer getoFileMinRangeForNonAgg() {
		return oFileMinRangeForNonAgg;
	}

	public void setoFileMinRangeForNonAgg(Integer oFileMinRangeForNonAgg) {
		this.oFileMinRangeForNonAgg = oFileMinRangeForNonAgg;
	}
	
	@Column(name = "MAXRANGEFORNONAGG")
	@XmlElement
	public Integer getoFileMaxRangeForNonAgg() {
		return oFileMaxRangeForNonAgg;
	}

	public void setoFileMaxRangeForNonAgg(Integer oFileMaxRangeForNonAgg) {
		this.oFileMaxRangeForNonAgg = oFileMaxRangeForNonAgg;
	}

	@XmlElement
	@Column(name = "OUTPUTPLNAMEFORERR", length = 250)
	public String getoFilePathNameForError() {
		return oFilePathNameForError;
	}

	public void setoFilePathNameForError(String oFilePathNameForError) {
		this.oFilePathNameForError = oFilePathNameForError;
	}

	@Column(name = "OUTPUTFILESEQENABLESFORERR", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isoFileSeqEnablesForError() {
		return oFileSeqEnablesForError;
	}

	public void setoFileSeqEnablesForError(boolean oFileSeqEnablesForError) {
		this.oFileSeqEnablesForError = oFileSeqEnablesForError;
	}
	
	@Column(name = "MINRANGEFORERR")
	@XmlElement
	public Integer getoFileMinRangeForError() {
		return oFileMinRangeForError;
	}

	public void setoFileMinRangeForError(Integer oFileMinRangeForError) {
		this.oFileMinRangeForError = oFileMinRangeForError;
	}

	@Column(name = "MAXRANGEFORERR")
	@XmlElement
	public Integer getoFileMaxRangeForError() {
		return oFileMaxRangeForError;
	}

	public void setoFileMaxRangeForError(Integer oFileMaxRangeForError) {
		this.oFileMaxRangeForError = oFileMaxRangeForError;
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
	
}
