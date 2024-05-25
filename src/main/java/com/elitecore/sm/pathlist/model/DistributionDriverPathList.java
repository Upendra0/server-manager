/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.device.model.Device;

/**
 * @author jay.shah
 *
 */
@Component(value="distributionDriverPathList")
@Entity()
@DynamicUpdate
@DiscriminatorValue("DDPL")
public class DistributionDriverPathList extends CommonPathList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9004763745322553706L;
	private int maxFilesCountAlert=1000;
	private List<Composer> composerWrappers =new ArrayList<>(0);
	private Boolean fileGrepDateEnabled = false;
	private String dateFormat = "";
	private Integer startIndex = 0;
	private Integer endIndex = 0;
	private String position = PositionEnum.LEFT.getValue();
	private String referenceDevice;
	private Device parentDevice;
	private String dbReadFileNameExtraSuffix;

	/**
	 * @return the composerWrappers
	 */
	
	@XmlElement
	@Column(name = "MAXFILECOUNTALERT", length = 5)
	public int getMaxFilesCountAlert() {
		return maxFilesCountAlert;
	}
	
	/**
	 * @param maxFilesCountAlert
	 *            the maxFilesCountAlert to set
	 */

	public void setMaxFilesCountAlert(int maxFilesCountAlert) {
		this.maxFilesCountAlert = maxFilesCountAlert;
	}

	@OneToMany(mappedBy = "myDistDrvPathlist",fetch=FetchType.LAZY,cascade = CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<Composer> getComposerWrappers() {
		return composerWrappers;
	}

	/**
	 * @param composerWrappers
	 *            the composerWrappers to set
	 */
	public void setComposerWrappers(List<Composer> composerWrappers) {
		this.composerWrappers = composerWrappers;
	}

	// optional mapping of composers that is relevant only for distributions
	// driver case.
	
	/**
	 * @return the fileGrepDateEnabled
	 */
	@XmlElement
	@Column(name = "FILEGREPDATEENABLED")
	@org.hibernate.annotations.Type(type = "yes_no")
	public Boolean getFileGrepDateEnabled() {
		return fileGrepDateEnabled;
	}

	/**
	 * @return the dateFormat
	 */
	@XmlElement(nillable=true)
	@Column(name = "DATEFORMAT", length = 30)
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @return the startIndex
	 */
	@XmlElement
	@Column(name = "STARTINDEX", length = 7)
	public Integer getStartIndex() {
		return startIndex;
	}

	/**
	 * @return the endIndex
	 */
	@XmlElement
	@Column(name = "ENDINDEX", length = 7)
	public Integer getEndIndex() {
		return endIndex;
	}

	/**
	 * @return the position
	 */
	@XmlElement
	@Column(name = "POSITION", length = 10)
	public String getPosition() {
		return position;
	}

	/**
	 * @param fileGrepDateEnabled the fileGrepDateEnabled to set
	 */
	public void setFileGrepDateEnabled(Boolean fileGrepDateEnabled) {
		this.fileGrepDateEnabled = fileGrepDateEnabled;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param startIndex the startIndex to set
	 */
	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	/**
	 * @param endIndex the endIndex to set
	 */
	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name = "REFERENCEDEVICE", nullable = true, length = 400)
	@XmlTransient
	public String getReferenceDevice() {
		return referenceDevice;
	}
	public void setReferenceDevice(String referenceDevice) {
		this.referenceDevice = referenceDevice;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PARENTDEVICE", nullable = true, foreignKey = @ForeignKey(name = "FK_PARENTDEVICE_ID"))
	@XmlElement
	@DiffIgnore
	public Device getParentDevice() {
		return parentDevice;
	}
	public void setParentDevice(Device parentDevice) {
		this.parentDevice = parentDevice;
	}
	
	@Column(name = "DBREADFILENAMEEXTRASUFFIX", nullable = true, length = 100)
	@XmlElement(nillable=true)
	public String getDbReadFileNameExtraSuffix() {
		return dbReadFileNameExtraSuffix;
	}

	public void setDbReadFileNameExtraSuffix(String dbReadFileNameExtraSuffix) {
		this.dbReadFileNameExtraSuffix = dbReadFileNameExtraSuffix;
	}

}
