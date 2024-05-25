/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="commonPathList")
@Entity()
@DynamicUpdate
@XmlType(propOrder = { "compressInFileEnabled", "compressOutFileEnabled","readFilenamePrefix","readFilenameSuffix",
		"readFilenameContains","readFilenameExcludeTypes","writeFilePath","writeFilenamePrefix", "fileNamePattern"})
public class CommonPathList extends PathList {

	private static final long serialVersionUID = 441385137299072202L;
	private String readFilenamePrefix;
	private String readFilenameSuffix;
	private String readFilenameContains;
	private String readFilenameExcludeTypes;
	private String writeFilePath;
	private String writeFilenamePrefix;
	private boolean compressInFileEnabled;
	private boolean compressOutFileEnabled;
	private String fileNamePattern;
	

	public CommonPathList() {
		// Default Constructor.
	}


	/**
	 * @return the compressInFileEnabled
	 */
	@Column(name = "COMPRESSINFILEENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isCompressInFileEnabled() {
		return compressInFileEnabled;
	}

	/**
	 * @return the compressOutFileEnabled
	 */
	@Column(name = "COMPRESSOUTFILEENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isCompressOutFileEnabled() {
		return compressOutFileEnabled;
	}

	/**
	 * @param compressInFileEnabled
	 *            the compressInFileEnabled to set
	 */
	public void setCompressInFileEnabled(boolean compressInFileEnabled) {
		this.compressInFileEnabled = compressInFileEnabled;
	}

	/**
	 * @param compressOutFileEnabled
	 *            the compressOutFileEnabled to set
	 */
	public void setCompressOutFileEnabled(boolean compressOutFileEnabled) {
		this.compressOutFileEnabled = compressOutFileEnabled;
	}




	/**
	 * @return the readFilenamePrefix
	 */
	@Column(name = "READFILENAMEPREFIX", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getReadFilenamePrefix() {
		return readFilenamePrefix;
	}

	/**
	 * @return the readFilenameSuffix
	 */
	@Column(name = "READFILENAMESUFFIX", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getReadFilenameSuffix() {
		return readFilenameSuffix;
	}

	/**
	 * @return the readFilenameContains
	 */
	@Column(name = "READFILENAMECONTAINS", nullable = true, length = 1000)
	@XmlElement(nillable=true)
	public String getReadFilenameContains() {
		return readFilenameContains;
	}

	/**
	 * @return the readFilenameExcludeTypes
	 */
	@Column(name = "READFILENAMEEXCLUDETYPES", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getReadFilenameExcludeTypes() {
		return readFilenameExcludeTypes;
	}

	/**
	 * @return the writeFilePath
	 */
	@Column(name = "WRITEFILEPATH", nullable = true, length = 500)
	@XmlElement
	public String getWriteFilePath() {
		return writeFilePath;
	}

	/**
	 * @return the writeFilenamePrefix
	 */
	@Column(name = "WRITEFILENAMEPREFIX", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getWriteFilenamePrefix() {
		return writeFilenamePrefix;
	}

	/**
	 * @param readFilenamePrefix
	 *            the readFilenamePrefix to set
	 */
	public void setReadFilenamePrefix(String readFilenamePrefix) {
		this.readFilenamePrefix = readFilenamePrefix;
	}

	/**
	 * @param readFilenameSuffix
	 *            the readFilenameSuffix to set
	 */
	public void setReadFilenameSuffix(String readFilenameSuffix) {
		this.readFilenameSuffix = readFilenameSuffix;
	}

	/**
	 * @param readFilenameContains
	 *            the readFilenameContains to set
	 */
	public void setReadFilenameContains(String readFilenameContains) {
		this.readFilenameContains = readFilenameContains;
	}

	/**
	 * @param readFilenameExcludeTypes
	 *            the readFilenameExcludeTypes to set
	 */
	public void setReadFilenameExcludeTypes(String readFilenameExcludeTypes) {
		this.readFilenameExcludeTypes = readFilenameExcludeTypes;
	}

	/**
	 * @param writeFilePath
	 *            the writeFilePath to set
	 */
	public void setWriteFilePath(String writeFilePath) {
		this.writeFilePath = writeFilePath;
	}

	/**
	 * @param writeFilenamePrefix
	 *            the writeFilenamePrefix to set
	 */
	public void setWriteFilenamePrefix(String writeFilenamePrefix) {
		this.writeFilenamePrefix = writeFilenamePrefix;
	}
	
	/**
	 * @return the readFileNamePattern
	 */
	@Column(name = "READFILENAMEPATTERN", nullable = true, length = 200)
	@XmlElement(nillable=true)
	public String getFileNamePattern() {
		return fileNamePattern;
	}
	
	/**
	 * @param fileNamePattern
	 *            the fileNamePattern to set
	 */
	public void setFileNamePattern(String fileNamePattern) {
		this.fileNamePattern = fileNamePattern;
	}
}
