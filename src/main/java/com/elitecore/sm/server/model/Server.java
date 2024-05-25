/**
 * 
 */
package com.elitecore.sm.server.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani
 * 
 */
/**
 * @author elitecore
 *
 */
@Component(value="serverObject")
@Scope(value="prototype")
@Entity()
//@Table(name = "TBLTSERVER",uniqueConstraints=@UniqueConstraint(columnNames={"IPADDR", "SERVERTYPEID"}))
@Table(name = "TBLTSERVER")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serverCache")
@XmlType(propOrder = { "id", "name","description", BaseConstants.IPADDRESS,"utilityPort","serverType","serverId","groupServerId","containerEnvironment"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Server extends BaseModel implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String serverId;
	private String groupServerId;
	
	private ServerType serverType;
	private String name;
	private String description;
	private String ipAddress;
	private int utilityPort=BaseConstants.SERVER_MGMT_JMX_PORT;
	private boolean containerEnvironment = true;
	
	public Server() {
		// default constructor needed for hibernate
	}
	

	public Server(ServerType serverType, String name, String ipAddress, int utilityPort) {
		super();
		this.serverType = serverType;
		this.name = name;
		this.ipAddress = ipAddress;
		this.utilityPort = utilityPort;
	}


	/**
	 * @return the Id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="Server",allocationSize=1)
	public int getId() {
		return id;
	}

	//
	/**
	 * @return the Name
	 */
	@XmlElement
	@Column(name = "NAME", nullable = false, length = 250,unique=true)
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true, length = 2000)
	public String getDescription() {
		return description;
	}

	/**
	 * @param Id
	 *            the Id to set
	 */
	public void setId(int Id) {
		this.id = Id;
	}

	/**
	 * @param Name
	 *            the Name to set
	 */
	public void setName(String Name) {
		this.name = Name;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the ipaddress
	 */
	@Column(name = "IPADDR", nullable = false, length = 255)
	@XmlElement
	// length parameter kept default 255, so that domain names can be
	// accommodated for future extension.
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipaddress
	 *            the ipaddress to set
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	

	/**
	 * @return the serverType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER_SERVERTYPE"))
	@DiffIgnore
	public ServerType getServerType() {
		return serverType;
	}

	/**
	 * @param serverType
	 *            the serverType to set
	 */
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}

	/**
	 * @return the port
	 */
	@Column(name = "UTILITYPORT", nullable = false, length = 6)
	@XmlElement
	public int getUtilityPort() {
		return utilityPort;
	}
	
	public void setUtilityPort(int utilityPort) {
		this.utilityPort = utilityPort;
	}
	
	@Column(name = "SERVERID", nullable = true)
	@XmlElement
	public String getServerId() {
		return serverId;
	}

	
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@XmlElement
	@Column(name = "CONTAINERENVIRONMENT", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isContainerEnvironment() {
		return containerEnvironment;
	}

	public void setContainerEnvironment(boolean containerEnvironment) {
		//this.containerEnvironment = containerEnvironment;
	}
	
	@Column(name = "GROUPSERVERID", nullable = true)
	@XmlElement
	public String getGroupServerId() {
		return groupServerId;
	}

	
	public void setGroupServerId(String groupServerId) {
		this.groupServerId = groupServerId;
	}
}