package com.elitecore.sm.drivers.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.model.ControlFileAttributesEnum;

@Embeddable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = {"controlFileEnabled","controlFileLocation","attributes","attributeSeparator","fileRollingDuration","fileRollingStartTime","controlFileName","fileSeqEnable"})
public class ControlFileParams implements Serializable {

	private static final long serialVersionUID = 1L;
	private boolean controlFileEnabled = false;
	private String controlFileLocation;
	private String attributes = ControlFileAttributesEnum.getCommaSeparatedToString();
	private String attributeSeparator;
	private int fileRollingDuration = 14400;
	private String fileRollingStartTime;
	private String controlFileName = "CF{yyyyMMddHHmmss}.txt";
	private boolean fileSeqEnable= true;
	
	@Column(name = "CONTROLFILEENABLED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isControlFileEnabled() {
		return controlFileEnabled;
	}

	public void setControlFileEnabled(boolean controlFileEnabled) {
		this.controlFileEnabled = controlFileEnabled;
	}

	@Column(name = "CONTROLFILELOCATION", nullable = true, length = 500)
	@XmlElement
	public String getControlFileLocation() {
		return controlFileLocation;
	}

	public void setControlFileLocation(String controlFileLocation) {
		this.controlFileLocation = controlFileLocation;
	}

	@Column(name = "ATTRIBUTES", nullable = true, length = 100)
	@XmlElement
	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Column(name = "ATTRIBUTESEPARATOR", nullable = true, length = 3)
	@XmlElement
	public String getAttributeSeparator() {
		return attributeSeparator;
	}

	public void setAttributeSeparator(String attributeSeparator) {
		this.attributeSeparator = attributeSeparator;
	}

	@Column(name = "FILEROLLINGDURATION", nullable = true)
	@XmlElement
	public int getFileRollingDuration() {
		return fileRollingDuration;
	}

	public void setFileRollingDuration(int fileRollingDuration) {
		this.fileRollingDuration = fileRollingDuration;
	}

	@Column(name = "FILEROLLINGSTARTTIME", nullable = true, length = 10 )
	@XmlElement
	public String getFileRollingStartTime() {
		return fileRollingStartTime;
	}

	public void setFileRollingStartTime(String fileRollingStartTime) {
		this.fileRollingStartTime = fileRollingStartTime;
	}

	@Column(name = "CONTROLFILENAME", nullable = true, length = 50)
	@XmlElement
	public String getControlFileName() {
		return controlFileName;
	}

	public void setControlFileName(String controlFileName) {
		this.controlFileName = controlFileName;
	}

	@Column(name = "FILESEQENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileSeqEnable() {
		return fileSeqEnable;
	}

	public void setFileSeqEnable(boolean fileSeqEnable) {
		this.fileSeqEnable = fileSeqEnable;
	}
}
