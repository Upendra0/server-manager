/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author jay.shah This class is associated with Services or Drivers, So it has
 *         One to One mapping with particular Service or Driver. Mapping table
 *         for Service and FileGroup - TBLTSERVICEFILEGROUPMAPPING Mapping table
 *         for Service and FileGroup - TBLTDRIVERFILEGROUPMAPPING
 *
 */
@Entity()
@XmlType()
public class FileGroupingParameterCollection extends FileGroupingParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40664784630686048L;
	private String duplicateDirPath;

	/**
	 * @return the duplicateDirPath
	 */
	@XmlElement
	@Column(name = "DUPLICATEDIRPATH", nullable = true, length = 500)
	public String getDuplicateDirPath() {
		return duplicateDirPath;
	}
	
	/**
	 * @param duplicateDirPath
	 *            the duplicateDirPath to set
	 */
	public void setDuplicateDirPath(String duplicateDirPath) {
		this.duplicateDirPath = duplicateDirPath;
	}
	
}
