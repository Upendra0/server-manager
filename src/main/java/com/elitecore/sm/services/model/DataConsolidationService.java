package com.elitecore.sm.services.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.consolidationservice.model.AcrossFileProcessingTypeEnum;
import com.elitecore.sm.consolidationservice.model.ConsolidationTypeEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.ProcessingTypeEnum;
import com.elitecore.sm.pathlist.model.FileGroupingParamConsolidation;

/**
 * @author vandana.awatramani
 *
 */
@Component(value = "dataConsolidationService")
@XmlRootElement
@Entity()
@Table(name = "TBLTDATACONSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
public class DataConsolidationService extends Service {

	private static final long serialVersionUID = 1L;

	// Data Consolidation Service Parameter.
	private int noFileAlertInterval = 10;
	private int minFileRange = 10;
	private int maxFileRange = 300;
	private String processingType = ProcessingTypeEnum.FILE_BASED.getValue();
	private String mergeDelimiter = "|";
	private int minFileBatchSize;
	private int maxFileBatchSize;

	// Data Consolidation Parameters
	private ConsolidationTypeEnum consolidationType = ConsolidationTypeEnum.IN_FILE;
	private AcrossFileProcessingTypeEnum acrossFileProcessingType = AcrossFileProcessingTypeEnum.SINGLE;
	private int acrossFilePartition = 100;
	private int acrossFileMinBatchSize = 5;
	private int acrossFileMaxBatchSize = 15;

	// File Grouping Parameters
	private FileGroupingParamConsolidation fileGroupParam;

	// OneToMany Relation with Service Parameter & Consolidation Service List
	private List<DataConsolidation> consolidation = new ArrayList<>();

	@XmlElement
	@Column(name = "NOFILEALERT", nullable = true, length = 5)
	public int getNoFileAlertInterval() {
		return noFileAlertInterval;
	}

	public void setNoFileAlertInterval(int noFileAlertInterval) {
		this.noFileAlertInterval = noFileAlertInterval;
	}

	@XmlElement
	@Column(name = "MINFILERANGE", nullable = true, length = 10)
	public int getMinFileRange() {
		return minFileRange;
	}

	public void setMinFileRange(int minFileRange) {
		this.minFileRange = minFileRange;
	}

	@XmlElement
	@Column(name = "MAXFILERANGE", nullable = true, length = 10)
	public int getMaxFileRange() {
		return maxFileRange;
	}

	public void setMaxFileRange(int maxFileRange) {
		this.maxFileRange = maxFileRange;
	}

	@XmlElement
	@Column(name = "PROCESSINGTYPE", nullable = false, length = 50)
	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(String processingType) {
		this.processingType = processingType;
	}

	@XmlElement
	@Column(name = "MERGEDELIMITER", nullable = true, length = 50)
	public String getMergeDelimiter() {
		return mergeDelimiter;
	}

	public void setMergeDelimiter(String mergeDelimiter) {
		this.mergeDelimiter = mergeDelimiter;
	}

	@XmlElement
	@Column(name = "MINFILEBATCHSIZE", nullable = true, length = 5)
	public int getMinFileBatchSize() {
		return minFileBatchSize;
	}

	public void setMinFileBatchSize(int minFileBatchSize) {
		this.minFileBatchSize = minFileBatchSize;
	}

	@XmlElement
	@Column(name = "MAXFILEBATCHSIZE", nullable = true, length = 5)
	public int getMaxFileBatchSize() {
		return maxFileBatchSize;
	}

	public void setMaxFileBatchSize(int maxFileBatchSize) {
		this.maxFileBatchSize = maxFileBatchSize;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "CONSOLIDATIONTYPE", nullable = false, length = 20)
	public ConsolidationTypeEnum getConsolidationType() {
		return consolidationType;
	}

	public void setConsolidationType(ConsolidationTypeEnum consolidationType) {
		this.consolidationType = consolidationType;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "FILEPROCESSINGTYPE", nullable = true, length = 15)
	public AcrossFileProcessingTypeEnum getAcrossFileProcessingType() {
		return acrossFileProcessingType;
	}

	public void setAcrossFileProcessingType(AcrossFileProcessingTypeEnum acrossFileProcessingType) {
		this.acrossFileProcessingType = acrossFileProcessingType;
	}

	@XmlElement
	@Column(name = "FILEPARTITION", nullable = true, length = 5)
	public int getAcrossFilePartition() {
		return acrossFilePartition;
	}

	public void setAcrossFilePartition(int acrossFilePartition) {
		this.acrossFilePartition = acrossFilePartition;
	}

	@XmlElement
	@Column(name = "FILEMINBATCHSIZE", nullable = true, length = 5)
	public int getAcrossFileMinBatchSize() {
		return acrossFileMinBatchSize;
	}

	public void setAcrossFileMinBatchSize(int acrossFileMinBatchSize) {
		this.acrossFileMinBatchSize = acrossFileMinBatchSize;
	}

	@XmlElement
	@Column(name = "FILEMAXBATCHSIZE", nullable = true, length = 5)
	public int getAcrossFileMaxBatchSize() {
		return acrossFileMaxBatchSize;
	}

	public void setAcrossFileMaxBatchSize(int acrossFileMaxBatchSize) {
		this.acrossFileMaxBatchSize = acrossFileMaxBatchSize;
	}

	@XmlElement
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_CON_SVC_FILEGRP"))
	public FileGroupingParamConsolidation getFileGroupParam() {
		return fileGroupParam;
	}

	public void setFileGroupParam(FileGroupingParamConsolidation fileGroupParam) {
		this.fileGroupParam = fileGroupParam;
	}

	@XmlElement
	@OneToMany(mappedBy = "consService", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<DataConsolidation> getConsolidation() {
		return consolidation;
	}

	public void setConsolidation(List<DataConsolidation> consolidation) {
		this.consolidation = consolidation;
	}

}
