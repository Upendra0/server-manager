/**
 * 
 */
package com.elitecore.sm.drivers.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.FileTransferModeEnum;
//import com.mysql.fabric.xmlrpc.base.Array;

/**
 * @author jay.shah
 *
 */
@Entity
@Table(name = "TBLTCONNPARAMETER")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "id","port","username","password","timeout","fileSeparator","fileTransferMode","keyFileLocation","iPAddressHostList","validateInProcessFile","activeDistribution"})
public class ConnectionParameter extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2594810334436924202L;
	private int id;
	private FileTransferModeEnum fileTransferMode = FileTransferModeEnum.BINARY;
	private String iPAddressHost;
	private int port= 21;
	private int timeout=6000;
	private String username;
	private String password;
	private String keyFileLocation;
	private String fileSeparator="/";
	private List<HostParameters> iPAddressHostList; //NOSONAR
	private boolean validateInProcessFile=true; 
	private boolean activeDistribution=false; 

	/**
	 * @return the id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ConnectionParameter",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @return the fileTransferMode
	 */
	@XmlElement
	@Column(name = "FILETRANSFERMODE",length=255)
	@Enumerated
	public FileTransferModeEnum getFileTransferMode() {
		return fileTransferMode;
	}

	

	/**
	 * @return the iPAddressHost
	 */
	@Column(name = "HOSTIPADDRESS", nullable = true,length=255)
	@XmlTransient
	public String getiPAddressHost() {
		return iPAddressHost;
	}

	/**
	 * @return the port
	 */
	@Column(name = "PORT", nullable = true,length=5)
	@XmlElement(nillable=true)
	public int getPort() {
		return port;
	}

	/**
	 * @return the timeout
	 */
	@Column(name = "FTPTIMEOUT", nullable = true,length=7)
	@XmlElement
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the username
	 */
	@Column(name = "USERNAME", nullable = true,length=200)
	@XmlElement(nillable=true)
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	@Column(name = "FTP_PASSWORD", nullable = true,length=200)
	@XmlElement(nillable=true)
	@DiffIgnore
	public String getPassword() {
		return password;
	}

	/**
	 * @return the fileSeparator
	 */
	@Column(name = "FILESEPARATOR", nullable = true,length=1)
	@XmlElement(nillable=true)
	public String getFileSeparator() {
		return fileSeparator;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param fileTransferMode
	 *            the fileTransferMode to set
	 */
	public void setFileTransferMode(FileTransferModeEnum fileTransferMode) {
		this.fileTransferMode = fileTransferMode;
	}


	/**
	 * @param iPAddressHost
	 *            the iPAddressHost to set
	 */
	public void setiPAddressHost(String iPAddressHost) {
		this.iPAddressHost = iPAddressHost;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param fileSeparator
	 *            the fileSeparator to set
	 */
	public void setFileSeparator(String fileSeparator) {
		this.fileSeparator = fileSeparator;
	}

	@Column(name = "KEYFILESEPERATOR", nullable = true,length=300)
	@XmlElement(nillable=true)
	public String getKeyFileLocation() {
		return keyFileLocation;
	}

	public void setKeyFileLocation(String keyFileLocation) {
		this.keyFileLocation = keyFileLocation;
	}
	
	@XmlElement(name="iPAddressHostList")
	@Transient
	public List<HostParameters> getiPAddressHostList() {
		String ipAddress=getiPAddressHost();
		String[] iPAddressHostListArray =null;
		HostParameters hostParameter;
		
		if(ipAddress!=null && !StringUtils.isEmpty(ipAddress)){
			iPAddressHostListArray= ipAddress.split(",");
		}
		if(iPAddressHostListArray!=null && iPAddressHostListArray.length>0){
			iPAddressHostList=new ArrayList<>();
			for(int i=0;i<iPAddressHostListArray.length;i++){
			hostParameter=new HostParameters();
			hostParameter.setiPAddressHost(iPAddressHostListArray[i]);
			iPAddressHostList.add(hostParameter);
			}
		}
		return iPAddressHostList;
	}

	public void setiPAddressHostList(List<HostParameters> iPAddressHostList) {
		this.iPAddressHostList = iPAddressHostList;
	}
	
	@Column(name = "VALIDATEINPROCESSFILE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isValidateInProcessFile() {
		return validateInProcessFile;
	}

	public void setValidateInProcessFile(boolean validateInProcessFile) {
		this.validateInProcessFile = validateInProcessFile;
	}
	
	@Column(name = "ACTIVEDISTRIBUTION", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isActiveDistribution() {
		return activeDistribution;
	}

	public void setActiveDistribution(boolean activeDistribution) {
		this.activeDistribution = activeDistribution;
	}

}
