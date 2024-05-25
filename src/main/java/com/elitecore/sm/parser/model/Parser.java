/**
 * 
 */
package com.elitecore.sm.parser.model;

import java.util.Date;

import javax.persistence.CascadeType;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vishal.lakhyani
 *
 */
@Component(value="parser")
@Entity()
@Table(name = "TBLTPARSER")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = { "id", "name", "parserType","readFilenamePrefix","readFilenameSuffix","readFilenameContains","readFilenameExcludeTypes","writeFilePath"
		,"compressInFileEnabled","compressOutFileEnabled","maxFileCountAlert","writeFileSplit","writeFilenamePrefix","parserMapping", "fileNamePattern", "writeCdrHeaderFooterEnabled", "writeCdrDefaultAttributes"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Parser.class)
public class Parser extends BaseModel {

	
	private static final long serialVersionUID = -47108349811657916L;

	private int id;
	
	private String name;
	private PluginTypeMaster parserType;
	
	private String readFilenamePrefix;
	private String readFilenameSuffix;  
	private String readFilenameContains;
	private String readFilenameExcludeTypes;

	private String writeFilePath;

	private boolean compressInFileEnabled = true;
	private boolean compressOutFileEnabled = true;

	private ParserMapping parserMapping;
	private ParsingPathList parsingPathList;
	private String fileNamePattern;
	
	// used for parsing service only
	private int maxFileCountAlert;
	// user for parsing service only
	private boolean writeFileSplit;
	
	private String writeFilenamePrefix;
	
	private boolean writeCdrHeaderFooterEnabled = true;
	private boolean writeCdrDefaultAttributes;
	
	public Parser() {
		//Default Constructor
	}
	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ParserWrapper",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @return the readFilenamePrefix
	 */
	@Column(name = "READFILENAMEPREFIX", nullable = true, length = 50)
	@XmlElement
	public String getReadFilenamePrefix() {
		return readFilenamePrefix;
	}

	/**
	 * @return the readFilenameSuffix
	 */
	@Column(name = "READFILENAMESUFFIX", nullable = true, length = 50)
	@XmlElement
	public String getReadFilenameSuffix() {
		return readFilenameSuffix;
	}

	/**
	 * @return the readFilenameContains
	 */
	@Column(name = "READFILENAMECONTAINS", nullable = true, length = 1000)
	@XmlElement
	public String getReadFilenameContains() {
		return readFilenameContains;
	}

	/**
	 * @return the readFilenameExcludeTypes
	 */
	@Column(name = "READFILENAMEEXCLUDETYPES", nullable = true, length = 100)
	@XmlElement
	public String getReadFilenameExcludeTypes() {
		return readFilenameExcludeTypes;
	}

	/**
	 * @return the writeFilePath
	 */
	@Column(name = "WRITEFILEPATH", nullable = false, length = 600)
	@XmlElement
	public String getWriteFilePath() {
		return writeFilePath;
	}
	
	/**
	 * @return the max file count alert
	 */
	@Column(name = "MAXFILECOUNTALERT", nullable = true, length = 5)
	@XmlElement
	public int getMaxFileCountAlert() {
		return maxFileCountAlert;
	}

	/**
	 * @return the write file split
	 */
	@Column(name = "WRITEFILESPLIT", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isWriteFileSplit() {
		return writeFileSplit;
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
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the parsingPathList
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARSINGPLID", nullable = false, foreignKey = @ForeignKey(name = "FK_PARSINGPL_PW"))
	@XmlTransient
	public ParsingPathList getParsingPathList() {
		return parsingPathList;
	}

	/**
	 * @param parsingPathList the parsingPathList to set
	 */
	public void setParsingPathList(ParsingPathList parsingPathList) {
		this.parsingPathList = parsingPathList;
	}

	/**
	 * @param maxFileCountAlert : set max file count alert
	 */
	public void setMaxFileCountAlert(int maxFileCountAlert) {
		this.maxFileCountAlert = maxFileCountAlert;
	}

	
	/**
	 * @param writeFileSplit : set write file split enable or not
	 */
	public void setWriteFileSplit(boolean writeFileSplit) {
		this.writeFileSplit = writeFileSplit;
	}

	/**
	 * @return the readFilenameExcludeTypes
	 */
	@Column(name = "WRITEFILENAMEPREFIX", nullable = true, length = 50)
	@XmlElement
	public String getWriteFilenamePrefix() {
		return writeFilenamePrefix;
	}

	/**
	 * @param writeFilenamePrefix
	 */
	public void setWriteFilenamePrefix(String writeFilenamePrefix) {
		this.writeFilenamePrefix = writeFilenamePrefix;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, unique=true, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parserType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARSERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_PLUGIN_WRAPPER_PLUGINTYPE"))
	@DiffIgnore
	public PluginTypeMaster getParserType() {
		return parserType;
	}

	/**
	 * @param parserType the parserType to set
	 */
	public void setParserType(PluginTypeMaster parserType) {
		this.parserType = parserType;
	}

	/**
	 * @return the parserMapping
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER,optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "PARSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_PWRAPER_PARSER_MAP_ID"))
	public ParserMapping getParserMapping() {
		return parserMapping;
	}

	/**
	 * @param parserMapping the parserMapping to set
	 */
	public void setParserMapping(ParserMapping parserMapping) {
		this.parserMapping = parserMapping;
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
	
	/**
	 * @return the writeCdrHeaderFooterEnabled
	 */
	@Column(name = "WRITECDRHEADERFOOTERENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isWriteCdrHeaderFooterEnabled() {
		return writeCdrHeaderFooterEnabled;
	}

	/**
	 * @param writeCdrHeaderFooterEnabled
	 *            the writeCdrHeaderFooterEnabled to set
	 */
	public void setWriteCdrHeaderFooterEnabled(boolean writeCdrHeaderFooterEnabled) {
		this.writeCdrHeaderFooterEnabled = writeCdrHeaderFooterEnabled;
	}
	
	/**
	 * @return the cdr file default attributes enable or not
	 */
	@Column(name = "WRITECDRDEFAULTATTRIBUTES", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isWriteCdrDefaultAttributes() {
		return writeCdrDefaultAttributes;
	}
	
	/**
	 * @param writeCdrDefaultAttributes : set write cdr default attributes enable or not
	 */
	public void setWriteCdrDefaultAttributes(boolean writeCdrDefaultAttributes) {
		this.writeCdrDefaultAttributes = writeCdrDefaultAttributes;
	}
	
	public Parser(String name,String readFilenamePrefix, String readFilenameSuffix,
			String readFilenameExcludeTypes,
			String writeFilePath, boolean compressInFileEnabled,
			boolean compressOutFileEnabled) {
		super();
		this.name = name;
		this.readFilenamePrefix = readFilenamePrefix;
		this.readFilenameSuffix = readFilenameSuffix;
		this.readFilenameExcludeTypes = readFilenameExcludeTypes;
		this.writeFilePath = writeFilePath;
		this.compressInFileEnabled = compressInFileEnabled;
		this.compressOutFileEnabled = compressOutFileEnabled;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Parser parser = (Parser) super.clone();
		Date date = new Date();
		parser.setId(0);
		parser.setCreatedDate(date);
		parser.setLastUpdatedDate(date);
		parser.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, parser.getName()));
		parser.setParserMapping((ParserMapping) parser.getParserMapping().clone());
		return parser;
	}

}
