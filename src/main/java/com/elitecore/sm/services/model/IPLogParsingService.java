/**
 * 
 */
package com.elitecore.sm.services.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;


/**
 * @author vishal.lakhyani
 *
 */
@Component(value="iplogParsingService")
@XmlRootElement
@Entity()
@Table(name = "TBLTIPLOGPARSINGSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="IPLogParsingServiceCache")
public class IPLogParsingService extends Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String equalCheckField;
	private String equalCheckFunction;
	private String equalCheckValue;
	private int recordBatchSize = 5000;
	private String hashSeparator = HashSeparatorEnum.UNDERSCORE.getValue();
	private String indexType = IndexTypeEnum.HASHBASE.getValue();
	private int purgeInterval = 2;
	private int purgeDelayInterval = 5;
	private String outFileHeaders;
	private boolean fileStatsEnabled = false;
	private String fileStatsLoc;
	private boolean correlEnabled = false;
	private String mappedSourceField;
	private String destPortField ;
	private String destPortFilter ;
	private String createRecDestPath;
	private String deleteRecDestPath;
	private String outputFileHeader;
	
	private HashDataTypeEnum dataType = HashDataTypeEnum.NAT_DATA_RECORD;
	
	private List<PartitionParam> partionParamList = new ArrayList<>(0);
	
	private FileGroupingParameterParsing fileGroupingParameter;
	
	public IPLogParsingService(){
		// default constructor
	}

	/**
	 * 
	 * @return mappedSourceField
	 */
	@XmlElement
	@Column(name = "EQUALCHECKFIELD" ,nullable=true,length=20)
	public String getEqualCheckField() {
		return equalCheckField;
	}

	/**
	 * @param equalCheckField
	 */
	public void setEqualCheckField(String equalCheckField) {
		this.equalCheckField = equalCheckField;
	}

	/**
	 * @return equalcheckvalue
	 */
	@XmlElement
	@Column(name = "EQUALCHECKVALUE", nullable = true, length = 100)
	public String getEqualCheckValue() {
		return equalCheckValue;
	}

	/**
	 * @param equalCheckValue
	 */
	public void setEqualCheckValue(String equalCheckValue) {
		this.equalCheckValue = equalCheckValue;
	}

	/**
	 * @return recordbatchsize
	 */
	@XmlElement
	@Column(name = "RECORDBATCHSIZE", nullable = false, length = 10)
	public Integer getRecordBatchSize() {
		return recordBatchSize;
	}

	/**
	 * @param recordBatchSize
	 */
	public void setRecordBatchSize(Integer recordBatchSize) {
		this.recordBatchSize = recordBatchSize;
	}

	/**
	 * @return hashSeparator
	 */
	@XmlElement
	@Column(name = "HASHSEPARATOR" ,nullable=false)
	public String getHashSeparator() {
		return hashSeparator;
	}

	/**
	 * @param hashSeparator
	 */
	
	public void setHashSeparator(String hashSeparator) {
		this.hashSeparator = hashSeparator;
	}

	
	/**
	 * @return indexType
	 */
	@XmlElement
	@Column(name = "INDEXTYPE" ,nullable=false)
	public String getIndexType() {
		return indexType;
	}


	/**
	 * @param indexType
	 */
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}

	/**
	 * @return purgeinterval
	 */
	@XmlElement
	@Column(name = "PURGEINTERVAL", nullable = false, length = 5)
	public int getPurgeInterval() {
		return purgeInterval;
	}

	/**
	 * @param purgeInterval
	 */
	public void setPurgeInterval(int purgeInterval) {
		this.purgeInterval = purgeInterval;
	}

	/**
	 * @return purgedelayinterval
	 */
	@XmlElement
	@Column(name = "PURGEDELAYINTERVAL", nullable = false, length = 5)
	public int getPurgeDelayInterval() {
		return purgeDelayInterval;
	}


	/**
	 * @param purgeDelayInterval
	 */
	public void setPurgeDelayInterval(int purgeDelayInterval) {
		this.purgeDelayInterval = purgeDelayInterval;
	}

	/**
	 * @return outFileHeaders
	 */
	@XmlElement
	@Column(name = "OUTFILEHEADERS", nullable = true, length = 500)
	public String getOutFileHeaders() {
		return outFileHeaders;
	}

	/**
	 * @param outFileHeaders
	 */
	public void setOutFileHeaders(String outFileHeaders) {
		this.outFileHeaders = outFileHeaders;
	}

	/**
	 * @return fileStatsEnabled
	 */
	@XmlElement
	@Column(name = "FILESTATEENABLED", nullable = false, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isFileStatsEnabled() {
		return fileStatsEnabled;
	}

	/**
	 * @param fileStatsEnabled
	 */
	public void setFileStatsEnabled(boolean fileStatsEnabled) {
		this.fileStatsEnabled = fileStatsEnabled;
	}

	/**
	 * @return fileStatsLoc
	 */
	@XmlElement
	@Column(name = "FILESTATLOC", nullable = true, length = 600)
	public String getFileStatsLoc() {
		return fileStatsLoc;
	}

	/**
	 * @param fileStatsLoc
	 */
	public void setFileStatsLoc(String fileStatsLoc) {
		this.fileStatsLoc = fileStatsLoc;
	}

	/**
	 * @return correlEnabled
	 */
	@XmlElement
	@Column(name = "CORRELENABLE", nullable = false, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isCorrelEnabled() {
		return correlEnabled;
	}

	/**
	 * @param correlEnabled
	 */
	public void setCorrelEnabled(boolean correlEnabled) {
		this.correlEnabled = correlEnabled;
	}

	/**
	 * 
	 * @return mappedSourceField
	 */
	@XmlElement
	@Column(name = "MAPPEDSOURCEFIELD" ,nullable=true)
	public String getMappedSourceField() {
		return mappedSourceField;
	}

	/**
	 * @param mappedSourceField
	 */
	public void setMappedSourceField(String mappedSourceField) {
		this.mappedSourceField = mappedSourceField;
	}
	
	
	/**
	 * @return destPortField
	 */
	@XmlElement
	@Column(name = "DESTPORTFIELD", nullable = true, length = 10)
	public String getDestPortField() {
		return destPortField;
	}

	/**
	 * @param destPortField
	 */
	public void setDestPortField(String destPortField) {
		this.destPortField = destPortField;
	}

	/**
	 * @return destPortFilter
	 */
	@XmlElement
	@Column(name = "DESTPORTFILTER", nullable = true, length = 10)
	public String getDestPortFilter() {
		return destPortFilter;
	}

	/**
	 * @param destPortFilter
	 */
	public void setDestPortFilter(String destPortFilter) {
		this.destPortFilter = destPortFilter;
	}

	/**
	 * @return createRecDestPath
	 */
	@XmlElement
	@Column(name = "CREATERECDESTPATH", nullable = true, length = 600)
	public String getCreateRecDestPath() {
		return createRecDestPath;
	}

	/**
	 * @param createRecDestPath
	 */
	public void setCreateRecDestPath(String createRecDestPath) {
		this.createRecDestPath = createRecDestPath;
	}

	/**
	 * @return deleteRecDestPath
	 */
	@XmlElement
	@Column(name = "DELETERECDESTPATH", nullable = true, length = 600)
	public String getDeleteRecDestPath() {
		return deleteRecDestPath;
	}

	/**
	 * @param deleteRecDestPath
	 */
	public void setDeleteRecDestPath(String deleteRecDestPath) {
		this.deleteRecDestPath = deleteRecDestPath;
	}

	/**
	 * @return all partionParamList
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy="parsingService" , cascade = CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<PartitionParam> getPartionParamList() {
		return partionParamList;
	}

	/**
	 * @param partionParamList
	 */
	public void setPartionParamList(List<PartitionParam> partionParamList) {
		this.partionParamList = partionParamList;
	}
	
	/**
	 * @return fileGroupingParameter
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_IpLogParsing_FILEGRP"))
	public FileGroupingParameterParsing getFileGroupingParameter() {
		return fileGroupingParameter;
	}

	/**
	 * @param fileGroupingParameter
	 */
	public void setFileGroupingParameter(
			FileGroupingParameterParsing fileGroupingParameter) {
		this.fileGroupingParameter = fileGroupingParameter;
	}


	/**
	 * 
	 * @return dataType
	 */
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "HASHDATATYPE" ,nullable=false)
	public HashDataTypeEnum getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 */
	public void setDataType(HashDataTypeEnum dataType) {
		this.dataType = dataType;
	}

	@XmlElement
	@Column(name = "OUTPUTFILEHEADER", nullable = true, length = 600)
	public String getOutputFileHeader() {
		return outputFileHeader;
	}

	public void setOutputFileHeader(String outputFileHeader) {
		this.outputFileHeader = outputFileHeader;
	}
	
	@Column(name = "EQUALCHECKFUNCTION", nullable = true, length = 50)
	@XmlElement
	public String getEqualCheckFunction() {
		return equalCheckFunction;
	}


	public void setEqualCheckFunction(String equalCheckFunction) {
		this.equalCheckFunction = equalCheckFunction;
	}
	
}
