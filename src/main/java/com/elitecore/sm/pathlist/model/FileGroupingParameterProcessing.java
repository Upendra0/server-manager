/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import com.elitecore.sm.common.model.FilterGroupTypeEnum;

/**
 * @author jay.shah This class is associated with Services or Drivers, So it has
 *         One to One mapping with particular Service or Driver. Mapping table
 *         for Service and FileGroup - TBLTSERVICEFILEGROUPMAPPING Mapping table
 *         for Service and FileGroup - TBLTDRIVERFILEGROUPMAPPING
 *
 */
@Entity()
@XmlType
public class FileGroupingParameterProcessing extends FileGroupingParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40664784630686048L;
	private String duplicateDirPath;

	private boolean enableForFilter=false;
	private String filterDirPath;

	private boolean enableForInvalid=false;
	private String invalidDirPath;
	
	private boolean enableForArchive=false;
	private String archivePath;

	private FilterGroupTypeEnum filterGroupType;

	
	/**
	 * @return the duplicateDirPath
	 */
	@XmlElement
	@Column(name = "DUPLICATEDIRPATH", nullable = true, length = 500)
	public String getDuplicateDirPath() {
		return duplicateDirPath;
	}

	/**
	 * @return the enableForFilter
	 */
	@XmlElement
	@Column(name = "ENABLEFORFILTER", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEnableForFilter() {
		return enableForFilter;
	}

	/**
	 * @return the filterDirPath
	 */
	@XmlElement
	@Column(name = "FILTERDIRPATH", nullable = true, length = 500)
	public String getFilterDirPath() {
		return filterDirPath;
	}

	/**
	 * @return the enableForInvalid
	 */
	@XmlElement
	@Column(name = "ENABLEFORINVALID", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEnableForInvalid() {
		return enableForInvalid;
	}

	/**
	 * @return the invalidDirPath
	 */
	@XmlElement
	@Column(name = "INVALIDDIRPATH", nullable = true, length = 500)
	public String getInvalidDirPath() {
		return invalidDirPath;
	}

	/**
	 * @return the filterGroupType
	 */
	@XmlElement
	@Column(name = "FILTERGROUPTYPE", nullable = true, length = 15)
	@Enumerated  (EnumType.STRING)
	public FilterGroupTypeEnum getFilterGroupType() {
		return filterGroupType;
	}
	
	/**
	 * @return the enableForArchive
	 */
	@Column(name = "ENABLEFORARCHIVE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isEnableForArchive() {
		return enableForArchive;
	}
	
	/**
	 * @return the archivePath
	 */
	@Column(name = "ARCHIVEDIRPATH", nullable = true, length = 500)
	@XmlElement
	public String getArchivePath() {
		return archivePath;
	}

	/**
	 * @param duplicateDirPath
	 *            the duplicateDirPath to set
	 */
	public void setDuplicateDirPath(String duplicateDirPath) {
		this.duplicateDirPath = duplicateDirPath;
	}

	/**
	 * @param enableForFilter
	 *            the enableForFilter to set
	 */
	public void setEnableForFilter(boolean enableForFilter) {
		this.enableForFilter = enableForFilter;
	}

	/**
	 * @param filterDirPath
	 *            the filterDirPath to set
	 */
	public void setFilterDirPath(String filterDirPath) {
		this.filterDirPath = filterDirPath;
	}

	/**
	 * @param enableForInvalid
	 *            the enableForInvalid to set
	 */
	public void setEnableForInvalid(boolean enableForInvalid) {
		this.enableForInvalid = enableForInvalid;
	}

	/**
	 * @param invalidDirPath
	 *            the invalidDirPath to set
	 */
	public void setInvalidDirPath(String invalidDirPath) {
		this.invalidDirPath = invalidDirPath;
	}

	/**
	 * @param filterGroupType
	 *            the filterGroupType to set
	 */
	public void setFilterGroupType(FilterGroupTypeEnum filterGroupType) {
		this.filterGroupType = filterGroupType;
	}
	
	/**
	 * @param enableForArchive
	 *            the enableForArchive to set
	 */
	public void setEnableForArchive(boolean enableForArchive) {
		this.enableForArchive = enableForArchive;
	}

	/**
	 * @param archivePath
	 *            the archivePath to set
	 */
	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}

}
