/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.services.model.DateTypeEnum;

/**
 * @author jay.shah This class is associated with Plugin list of distribution
 *         drivers, So it has One to Many mapping with particular Plugin list.
 *         Here we have not created separate mapping table between Plugin List
 *         and Char Rename Operations, But we have provide pathListId of Path
 *         list and pluginId of Plugins as foreign key in this table.
 * 
 */
@Component(value = "charRenameOperation")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTCHARRENAMEOPERATIONS")
@DynamicUpdate
@XmlType(propOrder = { "id", "sequenceNo", "query", "position",	"startIndex", "endIndex", "paddingType", 
										"defaultValue", "length","paddingValue","dateFormat","srcDateFormat","dateType","cacheEnable"})
public class CharRenameOperation extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int sequenceNo;
	private String query;

	private String position = PositionEnum.LEFT.getValue();
	private int startIndex;
	private int endIndex;
	private String paddingType  = PositionEnum.LEFT.getValue();
	private String defaultValue;
	private int length;
	private Composer composer;
	// char rename class can be associated to either composer or to file rename agent
	private ServiceFileRenameConfig svcFileRenConfig;
	
	private String paddingValue;
	private String dateFormat = SourceDateFormatEnum.dd_MM_yyyy_HH_mm_ss.getName();
	private String srcDateFormat ="";
	private String dateType = DateTypeEnum.LOCALDATE.getName();
	
	private PathList pathList;
	private boolean isCacheEnable = false;

	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="CharRenameOperation",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 * @return the sequenceNo
	 */
	@Column(name = "SEQNUMBER", nullable = false, length = 3)
	@XmlElement
	public int getSequenceNo() {
		return sequenceNo;
	}

	/**
	 * @return the query
	 */
	@Column(name = "QUERY", nullable = true, length = 500)
	@XmlElement
	public String getQuery() {
		return query;
	}

	/**
	 * @return the position
	 */
	@Column(name = "POSITION", nullable = true, length = 10)
	@XmlElement
	public String getPosition() {
		return position;
	}

	/**
	 * @return the startIndex
	 */
	@Column(name = "STARTINDEX", nullable = true, length = 3)
	@XmlElement
	public int getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the endIndex
	 */
	@Column(name = "ENDINDEX", nullable = true, length = 3)
	@XmlElement
	public int getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the paddingType
	 */
	@Column(name = "PADDINNGTYPE", nullable = true, length = 10)
	@XmlElement
	public String getPaddingType() {
		return paddingType;
	}



	/**
	 * @return the defaultValue
	 */
	@Column(name = "DEFAULTVALUE", nullable = true, length = 500)
	@XmlElement
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the length
	 */
	@Column(name = "LENGTH", nullable = true, length = 3)
	@XmlElement
	public int getLength() {
		return length;
	}

	
	/**
	 * @return the composer
	 */
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "COMPOSERPLUGINID", nullable = true, foreignKey = @ForeignKey(name = "FK_CHAR_RENAME_COMPOSER_ID"))
	public Composer getComposer() {
		return composer;
	}
	
	
	/**
	 * @return the svcFileRenConfig
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SVCFILERENCONFIGID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVICEFILERENID_CHAR"))
	@XmlTransient
	public ServiceFileRenameConfig getSvcFileRenConfig() {
		return svcFileRenConfig;
	}

	@Column(name = "PADDINGVALUE", nullable = true, length = 500)
	@XmlElement
	public String getPaddingValue() {
		return paddingValue;
	}

	@Column(name = "DATEFORMAT", nullable = true, length = 500)
	@XmlElement
	public String getDateFormat() {
		return dateFormat;
	}

	@Column(name = "DATETYPE", nullable = true, length = 500)
	@XmlElement
	public String getDateType() {
		return dateType;
	}

	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = {CascadeType.DETACH,CascadeType.PERSIST, CascadeType.REFRESH,CascadeType.REMOVE})
	@JoinColumn(name = "PATHLISTID", nullable = true, foreignKey = @ForeignKey(name = "FK_CHAR_RENAME_PATH_LIST_ID"))
	public PathList getPathList() {
		return pathList;
	}
	
	@XmlElement
	@Column(name = "SRCDATEFORMAT", nullable = true, length = 100)
	public String getSrcDateFormat() {
		return srcDateFormat;
	}
	
	@XmlElement
	@Column(name = "ISCACHEENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isCacheEnable() {
		return isCacheEnable;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @param sequenceNo the sequenceNo to set
	 */
	public void setSequenceNo(int sequenceNo) {
		this.sequenceNo = sequenceNo;
	}


	/**
	 * @param query
	 *            the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
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
	 * @param paddingType
	 *            the paddingType to set
	 */
	public void setPaddingType(String paddingType) {
		this.paddingType = paddingType;
	}


	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param composer the composer to set
	 */
	public void setComposer(Composer composer) {
		this.composer = composer;
	}


	/**
	 * @param svcFileRenConfig the svcFileRenConfig to set
	 */
	public void setSvcFileRenConfig(ServiceFileRenameConfig svcFileRenConfig) {
		this.svcFileRenConfig = svcFileRenConfig;
	}
	
	public void setPaddingValue(String paddingValue) {
		this.paddingValue = paddingValue;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public void setPathList(PathList pathList) {
		this.pathList = pathList;
	}
	
	public void setSrcDateFormat(String srcDateFormat) {
		this.srcDateFormat = srcDateFormat;
	}
	
	public void setCacheEnable(boolean isCacheEnable) {
		this.isCacheEnable = isCacheEnable;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Date date = new Date();
		CharRenameOperation charRenameOperation = (CharRenameOperation) super.clone();
		charRenameOperation.setId(0);
		charRenameOperation.setCreatedDate(date);
		charRenameOperation.setLastUpdatedDate(date);
		return charRenameOperation;
	}
}
