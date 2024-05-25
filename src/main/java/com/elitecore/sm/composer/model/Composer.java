/**
 * 
 */
package com.elitecore.sm.composer.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author jay.shah This class is associated with Path list of Services or
 *         Drivers, So it has One to Many mapping with particular Path list of
 *         Service or Driver. Here we have not created separate mapping table
 *         between Path list and Plugin list, But we have provide pathListId of
 *         Path list and pluginId of Plugins as foreign key in this table.
 * 
 */
@Component(value="composer")
@Entity()
@Table(name = "TBLTCOMPOSER")
@DynamicUpdate
@XmlType(propOrder = { "id", "name", "composerType","writeFilenamePrefix","writeFilenameSuffix","fileExtension","defaultFileExtensionRemoveEnabled","fileSplitEnabled"
		,"destPath","fileBackupPath","composerMapping","charRenameOperationList"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Composer.class)
public class Composer extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8287693345758501032L;
	private int id;
	private String name;
	private PluginTypeMaster composerType;
	
	private String writeFilenamePrefix;
	private String writeFilenameSuffix;
	private String fileExtension;
	private String destPath;
	private String fileBackupPath;
	private ComposerMapping composerMapping;
	private List<CharRenameOperation> charRenameOperationList = new ArrayList<>(0);
	private DistributionDriverPathList myDistDrvPathlist;
	private boolean fileSplitEnabled=false;
	private boolean defaultFileExtensionRemoveEnabled=false;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="ComposerWrapper",allocationSize=1)
	@Column(name = "ID")
	@XmlElement
	public int getId() {
		return id;
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
	 * @return the composerType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "COMPOSERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_COMPWRAPPER_CTYPE"))
	@DiffIgnore
	public PluginTypeMaster getComposerType() {
		return composerType;
	}

	/**
	 * @return the destPath
	 */
	@Column(name = "DESTPATH", nullable = false, length = 500)
	@XmlElement
	public String getDestPath() {
		return destPath;
	}

	/**
	 * @return the fileBackupPath
	 */
	@Column(name = "FILEBKPPATH", nullable = true, length = 500)
	@XmlElement
	public String getFileBackupPath() {
		return fileBackupPath;
	}

	/**
	 * @return the myDistDrvPathlist
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISTDRVPLID", nullable = false, foreignKey = @ForeignKey(name = "FK_DDPL_CW"))
	@XmlTransient
	@DiffIgnore
	public DistributionDriverPathList getMyDistDrvPathlist() {
		return myDistDrvPathlist;
	}
	
	/**
	 * 
	 * @return composerMapping
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.EAGER, optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "COMPOSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_ComposerWrap_CMapping"))
	public ComposerMapping getComposerMapping() {
		return composerMapping;
	}
	
	/**
	 * @param composerMapping
	 *            the composerMapping to set
	 */
	public void setComposerMapping(ComposerMapping composerMapping) {
		this.composerMapping = composerMapping;
	}
	
	/**
	 * @return the charRenameOperationList
	 */
	@OneToMany(mappedBy = "composer", cascade=CascadeType.ALL)
	@XmlElement
	public List<CharRenameOperation> getCharRenameOperationList() {
		return charRenameOperationList;
	}


	/**
	 * @return the writeFilenamePrefix
	 */
	@Column(name = "WRITEFILENAMEPREFIX", nullable = true, length = 50)
	@XmlElement
	public String getWriteFilenamePrefix() {
		return writeFilenamePrefix;
	}

	/**
	 * @return the writeFilenameSuffix
	 */
	@Column(name = "WRITEFILENAMESUFFIX", nullable = true, length = 50)
	@XmlElement
	public String getWriteFilenameSuffix() {
		return writeFilenameSuffix;
	}

	/**
	 * @return the fileExtension
	 */
	@Column(name = "FILEEXTENSION", nullable = true, length = 20)
	@XmlElement
	public String getFileExtension() {
		return fileExtension;
	}
	
	/**
	 * @param myDistDrvPathlist
	 *            the myDistDrvPathlist to set
	 */
	public void setMyDistDrvPathlist(DistributionDriverPathList myDistDrvPathlist) {
		this.myDistDrvPathlist = myDistDrvPathlist;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @param destPath
	 *            the destPath to set
	 */
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	/**
	 * @param fileBackupPath
	 *            the fileBackupPath to set
	 */
	public void setFileBackupPath(String fileBackupPath) {
		this.fileBackupPath = fileBackupPath;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param composerType the composerType to set
	 */
	public void setComposerType(PluginTypeMaster composerType) {
		this.composerType = composerType;
	}


	/**
	 * @param charRenameOperationList the charRenameOperationList to set
	 */
	public void setCharRenameOperationList(
			List<CharRenameOperation> charRenameOperationList) {
		this.charRenameOperationList = charRenameOperationList;
	}

	

	/**
	 * @param writeFilenamePrefix the writeFilenamePrefix to set
	 */
	public void setWriteFilenamePrefix(String writeFilenamePrefix) {
		this.writeFilenamePrefix = writeFilenamePrefix;
	}

	/**
	 * @param writeFilenameSuffix the writeFilenameSuffix to set
	 */
	public void setWriteFilenameSuffix(String writeFilenameSuffix) {
		this.writeFilenameSuffix = writeFilenameSuffix;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
		Composer composer = (Composer)super.clone();
		Date date = new Date();
		composer.setId(0);
		composer.setCreatedDate(date);
		composer.setLastUpdatedDate(date);
		composer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, composer.getName()));
		
		List<CharRenameOperation> newList = new ArrayList<>();
		List<CharRenameOperation> oldList = composer.getCharRenameOperationList();
		
		if(oldList != null && oldList.isEmpty()) {
			int length = oldList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation newCharRenameOperation = (CharRenameOperation) oldList.get(i).clone();
				newCharRenameOperation.setComposer(composer);
				newList.add(newCharRenameOperation);
			}
		}
		
		composer.setCharRenameOperationList(newList);
		composer.setComposerMapping((ComposerMapping) composer.getComposerMapping().clone());
        return composer;
    }
	@Column(name = "fileSplitEnabled", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileSplitEnabled() {
		return fileSplitEnabled;
	}

	public void setFileSplitEnabled(boolean fileSplitEnabled) {
		this.fileSplitEnabled = fileSplitEnabled;
	}
	
	@Column(name = "DEFAULTFILEEXTREMOVE", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isDefaultFileExtensionRemoveEnabled() {
		return defaultFileExtensionRemoveEnabled;
	}

	public void setDefaultFileExtensionRemoveEnabled(boolean defaultFileExtensionRemoveEnabled) {
		this.defaultFileExtensionRemoveEnabled = defaultFileExtensionRemoveEnabled;
	}

}
