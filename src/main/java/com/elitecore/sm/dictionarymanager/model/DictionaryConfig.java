package com.elitecore.sm.dictionarymanager.model;

import java.io.Serializable;
import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component(value="dictionaryConfigObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLTFILEINFO")
public class DictionaryConfig extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String filename;
	private String path;
	private String ipAddress;
	private int utilityPort=0;
	private boolean isDefault = false;
	private boolean isUpdated = false;
	private byte[]  dicFile; //NOSONAR
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="DictionaryConfig",allocationSize=1)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "FILENAME", nullable = false, length = 250,  unique = true)
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "PATH", nullable = false, length = 255)
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "IPADDRESS", nullable = true, length = 255)
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "UTILITYPORT", nullable = true, length = 6)
	public int getUtilityPort() {
		return utilityPort;
	}

	public void setUtilityPort(int utilityPort) {
		this.utilityPort = utilityPort;
	}

	@Column(name = "ISDEFAULT", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "ISUPDATED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isUpdated() {
		return isUpdated;
	}

	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	@Lob
	@Column(name = "DICFILE", nullable = true)
	public byte[]  getDicFile() {
		return dicFile;
	}

	public void setDicFile(byte[]  dicFile) {
		this.dicFile = dicFile;
	}
}
