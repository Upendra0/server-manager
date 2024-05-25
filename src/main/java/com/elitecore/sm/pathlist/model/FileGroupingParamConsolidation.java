package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author vishal.lakhyani
 *
 */
@Entity()
@XmlType(propOrder = { "enableForArchive", "archivePath" })
public class FileGroupingParamConsolidation extends FileGroupingParameter
		implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -40664784630686049L;

	private boolean enableForArchive = true;

	private String archivePath;

	/**
	 * @return enableForArchive
	 */
	@XmlElement
	@Column(name = "ENABLEFORARCHIVE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEnableForArchive() {
		return enableForArchive;
	}

	/**
	 * @return the archivePath
	 */
	@XmlElement
	@Column(name = "ARCHIVEPATH", nullable = true, length = 500)
	public String getArchivePath() {
		return archivePath;
	}

	/**
	 * @param enableForArchive
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
