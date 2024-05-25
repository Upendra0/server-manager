package com.elitecore.sm.consolidationservice.model;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Type;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.services.model.SortingTypeEnum;

@Entity()
@Table(name = "TBLTCONMAPPINGLIST")
public class DataConsolidationMapping extends BaseModel implements Serializable {

	private static final long serialVersionUID = 4961419592901298729L;
	private int id;

	// Consolidation Mapping
	private String mappingName;
	private String destPath;
	private String logicalOperator;
	private int processRecordLimit;
	private String conditionList;
	private boolean compressedOutput;
	private boolean writeOnlyConfiguredAttribute;
	private String fieldNameForCount = UnifiedFieldEnum.General80.toString();
	private String recordSortingType = SortingTypeEnum.Ascending.getValue();
	private String recordSortingField;
	private String recordSortingFieldType;

	// File Parameters
	private String fileName = "data_cons{yyyyMMddHHmmssSSS}.gz";
	private boolean fileSequence;
	private int minSeqRange = 1;
	private int maxSeqRange = 100;

	// ManyToOne Relation PathList & MappingList.
	private DataConsolidationPathList dataConsPathList;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DataConsolidationMapping", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@Column(name = "NAME", nullable = false, length = 200)
	public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	@XmlElement
	@Column(name = "DESTINATIONPATH", nullable = false, length = 500)
	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	@XmlElement
	@Column(name = "LOGICALOPERATOR", nullable = true, length = 5)
	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	@XmlElement
	@Column(name = "PROCESSRECORDLIMIT", nullable = true, length = 5)
	public int getProcessRecordLimit() {
		return processRecordLimit;
	}

	public void setProcessRecordLimit(int processRecordLimit) {
		this.processRecordLimit = processRecordLimit;
	}

	@XmlElement
	@Column(name = "CONDITIONLIST", nullable = true, length = 300)
	public String getConditionList() {
		return conditionList;
	}

	public void setConditionList(String conditionList) {
		this.conditionList = conditionList;
	}


	@XmlElement
	@Column(name = "COMPRESSEDOUTPUT", nullable = true, length = 1)
	@Type(type = "yes_no")
	public boolean isCompressedOutput() {
		return compressedOutput;
	}

	public void setCompressedOutput(boolean compressedOutput) {
		this.compressedOutput = compressedOutput;
	}

	@XmlElement
	@Column(name = "WRITEONLY_CONFIGURED_ATTRIBUTE", nullable = true, length = 1)
	@Type(type = "yes_no")
	public boolean isWriteOnlyConfiguredAttribute() {
		return writeOnlyConfiguredAttribute;
	}

	public void setWriteOnlyConfiguredAttribute(boolean writeOnlyConfiguredAttribute) {
		this.writeOnlyConfiguredAttribute = writeOnlyConfiguredAttribute;
	}

	@XmlElement
	//@Enumerated(EnumType.STRING)
	@Column(name = "FIELDNAMEFORCOUNT", nullable = true, length = 100)
	public String getFieldNameForCount() {
		return fieldNameForCount;
	}

	public void setFieldNameForCount(String fieldNameForCount) {
		this.fieldNameForCount = fieldNameForCount;
	}

	@XmlElement
	@Column(name = "RECORDSORTINGTYPE", nullable = true, length = 25)
	public String getRecordSortingType() {
		return recordSortingType;
	}

	public void setRecordSortingType(String recordSortingType) {
		this.recordSortingType = recordSortingType;
	}

	@XmlElement
	@Column(name = "RECORDSORTINGFIELD", nullable = true, length = 100)
	public String getRecordSortingField() {
		return recordSortingField;
	}

	public void setRecordSortingField(String recordSortingField) {
		this.recordSortingField = recordSortingField;
	}

	@XmlElement
	//@Enumerated(EnumType.STRING)
	@Column(name = "RECORDSORTINGFIELDTYPE", nullable = true, length = 20)
	public String getRecordSortingFieldType() {
		return recordSortingFieldType;
	}

	public void setRecordSortingFieldType(String recordSortingFieldType) {
		this.recordSortingFieldType = recordSortingFieldType;
	}

	@XmlElement
	@Column(name = "FILENAME", nullable = true, length = 250)
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlElement
	@Column(name = "FILESEQUENCE", nullable = true, length = 1)
	@Type(type = "yes_no")
	public boolean isFileSequence() {
		return fileSequence;
	}

	public void setFileSequence(boolean fileSequence) {
		this.fileSequence = fileSequence;
	}

	@XmlElement
	@Column(name = "MINSEQRANGE", nullable = true, length = 10)
	public int getMinSeqRange() {
		return minSeqRange;
	}

	public void setMinSeqRange(int minSeqRange) {
		this.minSeqRange = minSeqRange;
	}

	@XmlElement
	@Column(name = "MAXSEQRANGE", nullable = true, length = 10)
	public int getMaxSeqRange() {
		return maxSeqRange;
	}

	public void setMaxSeqRange(int maxSeqRange) {
		this.maxSeqRange = maxSeqRange;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONPATHLIST_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CON_PATH_MAPPING"))
	@XmlTransient
	public DataConsolidationPathList getDataConsPathList() {
		return dataConsPathList;
	}

	public void setDataConsPathList(DataConsolidationPathList dataConsPathList) {
		this.dataConsPathList = dataConsPathList;
	}

}
