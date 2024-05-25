	/**
 * 
 */
package com.elitecore.sm.services.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
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

import com.elitecore.sm.pathlist.model.FileGroupingParameterParsing;

/**
 * @author vandana.awatramani
 * 
 */
@Component(value="parsingService")
@Entity()
@Table(name = "TBLTPARSINGSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
@XmlRootElement
public class ParsingService extends Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3704005844813867057L;
	
	private int noFileAlert = 10;
	private int minFileRange = 1;
	private int maxFileRange = 300;
	private boolean fileSeqOrderEnable = false;
	private boolean fileStatInsertEnable = false;

	private boolean storeCDRFileSummaryDB = false;
	
	private String equalCheckField;
	private String equalCheckFunction;	
	private String equalCheckValue;
	
	private String dateFieldForSummary;
	private CDRDateSummaryTypeEnum typeForSummary;
	
	private boolean overrideFileDateEnabled = false;
	private String overrideFileDateType = CDRFileDateTypeEnum.MAXIMUM.getValue();
	//private String fileCopyFolders; // TODO check if parameter deprecated in  Removed as part of MED-2999
									// 6.2.3
	private int recordBatchSize = 5000;
	
	private FileGroupingParameterParsing fileGroupingParameter;
	private String errorPath;

	/**
	 * 
	 */
	public ParsingService() {
		super();
	
	}


	/**
	 * @return the equalCheckField
	 */
	@Column(name = "EQUALCHECKFIELD", nullable = true, length = 20)
	@XmlElement
	public String getEqualCheckField() {
		return equalCheckField;
	}

	/**
	 * @return the equalCheckValue
	 */	
			
	@Column(name = "EQUALCHECKVALUE", nullable = true, length = 4000)	
	@XmlElement
	public String getEqualCheckValue() {
		
		return equalCheckValue;
	}

	/**
	 * @return the dateFieldForSummary
	 */
	@Column(name = "DATEFIELDFORSUMMARY", nullable = true, length = 50)
	@XmlElement
	public String getDateFieldForSummary() {
		return dateFieldForSummary;
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
	 * @return the fileSeqOrderEnable
	 */
	@Column(name = "FILESEQORDERENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileSeqOrderEnable() {
		return fileSeqOrderEnable;
	}

	/**
	 * @return the recordBatchSize
	 */
	@Column(name = "RECBATCHSIZE", nullable = true)
	@XmlElement
	public int getRecordBatchSize() {
		return recordBatchSize;
	}

	/**
	 * @return the fileCopyFolders
	 */

	/*@Column(name = "FILECOPYFOLDERS", nullable = true)
	@XmlElement
	public String getFileCopyFolders() {
		return fileCopyFolders;
	}*/
	
	/**
	 * @return the parsingFileGroupParams
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_Parsing_FILEGRP"))
	public FileGroupingParameterParsing getFileGroupingParameter() {
		return fileGroupingParameter;
	}

	/**
	 * @return the minFileRange
	 */
	@Column(name = "MINFILERANGE", nullable = true)
	@XmlElement
	public int getMinFileRange() {
		return minFileRange;
	}

	/**
	 * @return the maxFileRange
	 */
	@Column(name = "MAXFILERANGE", nullable = true)
	@XmlElement
	public int getMaxFileRange() {
		return maxFileRange;
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
	 * @return cdr summary in db enabled
	 */
	@Column(name = "CDRSUMMARYDB", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isStoreCDRFileSummaryDB() {
		return storeCDRFileSummaryDB;
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
	 * @param fileCopyFolders
	 *            the fileCopyFolders to set
	 *//*
	public void setFileCopyFolders(String fileCopyFolders) {
		this.fileCopyFolders = fileCopyFolders;
	}
*/

	/**
	 * @param equalCheckField
	 *            the equalCheckField to set
	 */
	public void setEqualCheckField(String equalCheckField) {
		this.equalCheckField = equalCheckField;
	}

	/**
	 * @param equalCheckValue
	 *            the equalCheckValue to set
	 */
	public void setEqualCheckValue(String equalCheckValue) {				
			this.equalCheckValue = equalCheckValue;
	}

	/**
	 * @param dateFieldForSummary
	 *            the dateFieldForSummary to set
	 */
	public void setDateFieldForSummary(String dateFieldForSummary) {
		this.dateFieldForSummary = dateFieldForSummary;
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
	
	/**
	 * @param noFileAlert
	 *            the noFileAlert to set
	 */
	public void setNoFileAlert(int noFileAlert) {
		this.noFileAlert = noFileAlert;
	}

	/**
	 * @param storeCDRFileSummaryDB 
	 */
	public void setStoreCDRFileSummaryDB(boolean storeCDRFileSummaryDB) {
		this.storeCDRFileSummaryDB = storeCDRFileSummaryDB;
	}

	/**
	 * @param minFileRange
	 */
	public void setMinFileRange(int minFileRange) {
		this.minFileRange = minFileRange;
	}

	/**
	 * @param maxFileRange 
	 */
	public void setMaxFileRange(int maxFileRange) {
		this.maxFileRange = maxFileRange;
	}

	/**
	 * @param fileGroupingParameter
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


	@Column(name = "EQUALCHECKFUNCTION", nullable = true, length = 50)
	@XmlElement
	public String getEqualCheckFunction() {
		return equalCheckFunction;
	}


	public void setEqualCheckFunction(String equalCheckFunction) {
		this.equalCheckFunction = equalCheckFunction;
	}

	@Column(name = "FILESTATINSERTENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileStatInsertEnable() {
		return fileStatInsertEnable;
	}


	public void setFileStatInsertEnable(boolean fileStatInsertEnable) {
		this.fileStatInsertEnable = fileStatInsertEnable;
	}
	
}


