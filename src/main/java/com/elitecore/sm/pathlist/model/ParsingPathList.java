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
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.parser.model.Parser;

/**
 * @author vishal.lakhyani
 *
 */
@Entity
@DiscriminatorValue("PPL")
@Component(value="parsePathList")
@DynamicUpdate
@XmlType(propOrder = { "parserWrappers","fileGrepDateEnabled","dateFormat","position","startIndex","endIndex","parentDevice","circle","mandatoryFields"})
public class ParsingPathList  extends CommonPathList{

	private static final long serialVersionUID = -6295651591992807091L;
	private Boolean fileGrepDateEnabled = false;
	private String dateFormat = "";
	private Integer startIndex = 0;
	private Integer endIndex = 0;
	private String position = PositionEnum.LEFT.getValue();
	private List<Parser> parserWrappers = new ArrayList<>(0);
	private String referenceDevice;
	private Device parentDevice;
	private int maxFileCountAlert;
	private boolean writeFileSplit;
	private Circle circle;
	private String mandatoryFields;
	private boolean writeCdrHeaderFooterEnabled;
	private boolean writeCdrDefaultAttributes;

	
	public ParsingPathList() {
		// default constructor.
	}
	

	/**
	 * @return the parserWrappers
	 */
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "parsingPathList",cascade = CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<Parser> getParserWrappers() {
		// use Lazy mapping so that parser wrappers done get fetched.
		return parserWrappers;
	}

	/**
	 * @param parserWrappers the parserWrappers to set
	 */
	public void setParserWrappers(List<Parser> parserWrappers) {
		this.parserWrappers = parserWrappers;
	}
	
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
	@XmlElement
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
	
	@Column(name = "REFERENCEDEVICE", nullable = true, length = 250)
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

	@Column(name = "MAXFILECOUNTALERT", nullable = true, length = 5)
	@XmlTransient
	public int getMaxFileCountAlert() {
		return maxFileCountAlert;
	}

	public void setMaxFileCountAlert(int maxFileCountAlert) {
		this.maxFileCountAlert = maxFileCountAlert;
	}

	@Column(name = "WRITEFILESPLIT", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlTransient
	public boolean isWriteFileSplit() {
		return writeFileSplit;
	}

	public void setWriteFileSplit(boolean writeFileSplit) {
		this.writeFileSplit = writeFileSplit;
	}

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "CIRCLEID", nullable = true, foreignKey = @ForeignKey(name = "FK_TBLTPATHLIST_CIRCLE_ID"))
	@XmlElement
	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}
	@Column(name = "MANDATORYFIELDS", nullable = true, length = 3000)
	@XmlElement
	public String getMandatoryFields() {
		return mandatoryFields;
	}


	public void setMandatoryFields(String mandatoryFields) {
		this.mandatoryFields = mandatoryFields;
	}
	
	@Column(name = "WRITECDRHEADERFOOTERENABLED", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlTransient
	public boolean isWriteCdrHeaderFooterEnabled() {
		return writeCdrHeaderFooterEnabled;
	}

	public void setWriteCdrHeaderFooterEnabled(boolean writeCdrHeaderFooterEnabled) {
		this.writeCdrHeaderFooterEnabled = writeCdrHeaderFooterEnabled;
	}

	@Column(name = "WRITECDRDEFAULTATTRIBUTES", nullable = true, length = 5)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlTransient
	public boolean isWriteCdrDefaultAttributes() {
		return writeCdrDefaultAttributes;
	}

	public void setWriteCdrDefaultAttributes(boolean writeCdrDefaultAttributes) {
		this.writeCdrDefaultAttributes = writeCdrDefaultAttributes;
	}
}
