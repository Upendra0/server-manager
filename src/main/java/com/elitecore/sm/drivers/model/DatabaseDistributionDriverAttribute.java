package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;	
import javax.persistence.Enumerated;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.PositionEnum;

/**
 * 
 * @author Chirag.Rathod
 *
 */

@Entity()
@Table(name = "TBLTDRIVERATTR")
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "driverCache")
@XmlType(propOrder = { "id", "databaseFieldName", "unifiedFieldName","dataType", "defualtValue" ,"paddingEnable","length","paddingType","paddingChar","prefix","suffix" })
public class DatabaseDistributionDriverAttribute extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3433396788651376610L;
	private int id;
	private String databaseFieldName;
	private String unifiedFieldName;
	private String defualtValue;
	private String dataType;
	private boolean paddingEnable=false;	
	private int length;	
	private PositionEnum paddingType = PositionEnum.LEFT;	
	private String paddingChar;	
	private String prefix;	
	private String suffix;
	
	private DatabaseDistributionDriver dbDisDriver;

	/**
	 * represent the auto generated primary key
	 * 
	 * @return Id
	 */

	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DatabaseDistributionDriverAttribute", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the databaseFieldName
	 */
	@XmlElement
	@Column(name = "DBFIELDNAME", nullable = false, length = 100)
	public String getDatabaseFieldName() {
		return databaseFieldName;
	}

	public void setDatabaseFieldName(String databaseFieldName) {
		this.databaseFieldName = databaseFieldName;
	}

	/**
	 * @return the unifiedFieldName
	 */
	@XmlElement
	@Column(name = "UNIFIEDFIELDNAME", nullable = true, length = 100)
	public String getUnifiedFieldName() {
		return unifiedFieldName;
	}

	public void setUnifiedFieldName(String unifiedFieldName) {
		if(unifiedFieldName == null || unifiedFieldName.length() == 0){
			unifiedFieldName = " ";
		}
		this.unifiedFieldName = unifiedFieldName;
	}

	/**
	 * @return the defualtValue
	 */
	@XmlElement
	@Column(name = "DEFAULTVAL", nullable = true, length = 100)
	public String getDefualtValue() {
		return defualtValue;
	}

	public void setDefualtValue(String defualtValue) {
		this.defualtValue = defualtValue;
	}

	/**
	 * @return the dataType
	 */
	@XmlElement
	@Column(name = "DATATYPE", length = 100)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DRIVERID", nullable = false, foreignKey = @ForeignKey(name = "FK_DRVR_ATTR_DRVRID"))
	@XmlTransient
	public DatabaseDistributionDriver getDbDisDriver() {
		return dbDisDriver;
	}

	public void setDbDisDriver(DatabaseDistributionDriver dbDisDriver) {
		this.dbDisDriver = dbDisDriver;
	}
	

	/**
	 * @return the paddingEnable
	 */
	@Column(name = "PADDINGENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isPaddingEnable() {
		return paddingEnable;
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
	 * @return the paddingType
	 */
	@Column(name = "PADDINGTYPE", nullable = true, length = 100)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public PositionEnum getPaddingType() {
		return paddingType;
	}

	/**
	 * @return the paddingChar
	 */
	@Column(name = "PADDINGCHAR", nullable = true, length = 100)
	@XmlElement
	public String getPaddingChar() {
		return paddingChar;
	}

	/**
	 * @return the prefix
	 */
	@Column(name = "PREFIX", nullable = true, length = 100)
	@XmlElement
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return the suffix
	 */
	@Column(name = "SUFFIX", nullable = true, length = 100)
	@XmlElement
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param paddingEnable the paddingEnable to set
	 */
	public void setPaddingEnable(boolean paddingEnable) {
		this.paddingEnable = paddingEnable;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param paddingType the paddingType to set
	 */
	public void setPaddingType(PositionEnum paddingType) {
		this.paddingType = paddingType;
	}

	/**
	 * @param paddingChar the paddingChar to set
	 */
	public void setPaddingChar(String paddingChar) {
		this.paddingChar = paddingChar;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
