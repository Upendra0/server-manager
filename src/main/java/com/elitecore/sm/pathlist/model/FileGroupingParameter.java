/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.FileGroupEnum;

/**
 * @author jay.shah This class is associated with Services or Drivers, So it has
 *         One to One mapping with particular Service or Driver. Mapping table
 *         for Service and FileGroup - TBLTSERVICEFILEGROUPMAPPING Mapping table
 *         for Service and FileGroup - TBLTDRIVERFILEGROUPMAPPING
 *
 */
@Entity()
@Table(name = "TBLTFILEGRPPARAM")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "id", "fileGroupEnable","groupingType","enableForDuplicate", "sourcewiseArchive", "groupingDateType"})
public class FileGroupingParameter extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40664784630686048L;
	private int id;
	private boolean fileGroupEnable=false;
	private boolean sourcewiseArchive=false;
	private String groupingType = FileGroupEnum.DAY.getFileGroupEnum();
	private boolean enableForDuplicate=false;
	private String groupingDateType = "PROCESSDATE";
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
							     pkColumnName="TABLE_NAME",valueColumnName="VALUE",
							     pkColumnValue="FileGroupingParameter",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @return the fileGroupEnable
	 */
	@Column(name = "FILEGROUPENABLE", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileGroupEnable() {
		return fileGroupEnable;
	}

	/**
	 * @return the sourcewiseArchive
	 */
	@Column(name = "SOURCEWISEARCHIVE", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isSourcewiseArchive() {
		return sourcewiseArchive;
	}
	
	/**
	 * @return the groupingType
	 */
	@Column(name = "GROUPINGTYPE", nullable = true, length = 20)
	//@Enumerated  (EnumType.STRING)
	@XmlElement
	public String getGroupingType() {
		return groupingType;
	}

	/**
	 * @return the enableForDuplicate
	 */
	@Column(name = "ENABLEFORDUPLICATE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isEnableForDuplicate() {
		return enableForDuplicate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int fileGroupId) {
		this.id = fileGroupId;
	}

	/**
	 * @param fileGroupEnable
	 *            the fileGroupEnable to set
	 */
	public void setFileGroupEnable(boolean fileGroupEnable) {
		this.fileGroupEnable = fileGroupEnable;
	}
	
	/**
	 * @param sourcewiseArchive
	 *            the sourcewiseArchive to set
	 */
	public void setSourcewiseArchive(boolean sourcewiseArchive) {
		this.sourcewiseArchive = sourcewiseArchive;
	}

	/**
	 * @param groupingType
	 *            the groupingType to set
	 */
	public void setGroupingType(String groupingType) {
		this.groupingType = groupingType;
	}
	
	/**
	 * @param enableForDuplicate
	 *            the enableForDuplicate to set
	 */
	public void setEnableForDuplicate(boolean enableForDuplicate) {
		this.enableForDuplicate = enableForDuplicate;
	}
	
	/**
	 * @return the groupingDateType
	 */
	@Column(name = "GROUPINGDATETYPE", nullable = true, length = 100)
	@XmlElement
	public String getGroupingDateType() {
		return groupingDateType;
	}
	
	/**
	 * @param groupingDateType
	 *            the groupingDateType to set
	 */
	public void setGroupingDateType(String groupingDateType) {
		this.groupingDateType = groupingDateType;
	}

}
