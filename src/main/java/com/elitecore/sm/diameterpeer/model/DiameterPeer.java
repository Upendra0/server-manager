package com.elitecore.sm.diameterpeer.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.services.model.DiameterCollectionService;

@Component("diameterPeer")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TBLTDIAMETERPEER")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = { "id","name","identity","realmName","watchDogInterval","requestTimeOut","fileNameFormat","outFileLocation",
		"fileSeqEnable","appendFilePaddingInFileName","minFileSeq","maxFileSeq","logRollingUnitVol","logRollingUnitTime","inputCompressed","diameterAVPs"})
public class DiameterPeer extends BaseModel{

	private static final long serialVersionUID = 2252086629078034849L;
	
	private int id;
	private String name;
	private String identity;
	private String realmName;
	private int watchDogInterval = 6;
	private int requestTimeOut = 3000;
	private String fileNameFormat=" DP{yyyyMMddHHmmssSSS}.log";
	private String outFileLocation;
	private boolean fileSeqEnable = false;
	private boolean appendFilePaddingInFileName = false;
	private int minFileSeq=-1;
	private int maxFileSeq=-1;
	private int logRollingUnitVol=1024;
	private int logRollingUnitTime=2;
	private boolean inputCompressed=true;
	private DiameterCollectionService service;
	private List<DiameterAVP> diameterAVPs = new ArrayList<>();
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DiameterPeer",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@XmlElement
	@Column(name = "NAME", nullable = true,length=250)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement
	@Column(name = "IDENTITY", nullable = true,length=255)
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	
	@XmlElement
	@Column(name = "REALMNAME", nullable = true,length=255)
	public String getRealmName() {
		return realmName;
	}
	public void setRealmName(String realmName) {
		this.realmName = realmName;
	}
	
	@XmlElement
	@Column(name = "WATCHDOGINTERVAL", nullable = false)
	public int getWatchDogInterval() {
		return watchDogInterval;
	}
	public void setWatchDogInterval(int watchDogInterval) {
		this.watchDogInterval = watchDogInterval;
	}
	
	@XmlElement
	@Column(name = "REQUESTTIMEOUT", nullable = false)
	public int getRequestTimeOut() {
		return requestTimeOut;
	}
	public void setRequestTimeOut(int requestTimeOut) {
		this.requestTimeOut = requestTimeOut;
	}

	@XmlElement
	@Column(name = "FILENAMEFORMAT", nullable = true,length=50)
	public String getFileNameFormat() {
		return fileNameFormat;
	}
	public void setFileNameFormat(String fileNameFormat) {
		this.fileNameFormat = fileNameFormat;
	}
	
	@XmlElement
	@Column(name = "OUTFILELOCATION", nullable = true,length=500)
	public String getOutFileLocation() {
		return outFileLocation;
	}
	public void setOutFileLocation(String outFileLocation) {
		this.outFileLocation = outFileLocation;
	}
	
	@XmlElement
	@Column(name = "FILESEQENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isFileSeqEnable() {
		return fileSeqEnable;
	}
	public void setFileSeqEnable(boolean fileSeqEnable) {
		this.fileSeqEnable = fileSeqEnable;
	}
	
	@XmlElement
	@Column(name = "APPENDPADDINGINFILESEQ", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isAppendFilePaddingInFileName() {
		return appendFilePaddingInFileName;
	}

	public void setAppendFilePaddingInFileName(boolean appendFilePaddingInFileName) {
		this.appendFilePaddingInFileName = appendFilePaddingInFileName;
	}
	
	@XmlElement
	@Column(name = "MINFILESEQ", nullable = false)
	public int getMinFileSeq() {
		return minFileSeq;
	}
	public void setMinFileSeq(int minFileSeq) {
		this.minFileSeq = minFileSeq;
	}
	
	@XmlElement
	@Column(name = "MAXFILESEQ", nullable = false)
	public int getMaxFileSeq() {
		return maxFileSeq;
	}
	public void setMaxFileSeq(int maxFileSeq) {
		this.maxFileSeq = maxFileSeq;
	}
	
	@XmlElement
	@Column(name = "LOGROLLINGUNITVOL", nullable = false,length=5)
	public int getLogRollingUnitVol() {
		return logRollingUnitVol;
	}
	public void setLogRollingUnitVol(int logRollingUnitVol) {
		this.logRollingUnitVol = logRollingUnitVol;
	}
	
	@XmlElement
	@Column(name = "LOGROLLINGUNITTIME", nullable = false,length=5)
	public int getLogRollingUnitTime() {
		return logRollingUnitTime;
	}
	public void setLogRollingUnitTime(int logRollingUnitTime) {
		this.logRollingUnitTime = logRollingUnitTime;
	}

	@XmlElement
	@Column(name = "INPUTCOMPRESSED", nullable = false,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isInputCompressed() {
		return inputCompressed;
	}
	public void setInputCompressed(boolean inputCompressed) {
		this.inputCompressed = inputCompressed;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVICE_DIAMETER_PEER"))
	@XmlTransient
	@DiffIgnore
	public DiameterCollectionService getService() {
		return service;
	}
	public void setService(DiameterCollectionService service) {
		this.service = service;
	}
	
	@XmlElement
	@OneToMany(mappedBy = "peer",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@DiffIgnore
	public List<DiameterAVP> getDiameterAVPs() {
		return diameterAVPs;
	}
	public void setDiameterAVPs(List<DiameterAVP> diameterAVPs) {
		this.diameterAVPs = diameterAVPs;
	}
	
}
