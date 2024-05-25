/**
 * 
 */
package com.elitecore.sm.snmp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author vandana.awatramani
 *
 *         This class holds the config data for SNMP server, as host and as
 *         listener, distinguished by type. As a listener it has list of
 *         configured alerts, an alert configuration holds threshold value along
 *         with alert.
 * 
 * 
 */
@Component(value = "snmpServerconfig")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTSNMPSERVERCONFIG")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = { "id","name","version","type","hostIP","port","portOffset","community","advance","configuredAlerts",
		"snmpV3AuthAlgorithm","snmpV3AuthPassword","snmpV3PrivAlgorithm","snmpV3PrivPassword"})
public class SNMPServerConfig extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2878214705929535882L;
	private int id;
	private String hostIP;
	private String port;
	private int portOffset=100;
	//private SNMPCommunityType community=SNMPCommunityType.Public;
	private String community;
	private SNMPServerType type;
	private String name;
	private SNMPVersionType version=SNMPVersionType.V1;
	private boolean advance=true;
	// as a listener it will have list of alerts and the SI to which it belongs.
	private List<SNMPAlertWrapper> configuredAlerts = new ArrayList<>(
			0);
	private ServerInstance serverInstance;
	
	private String snmpV3AuthAlgorithm;
	
	private String snmpV3AuthPassword;
	
	private String snmpV3PrivAlgorithm;
	
	private String snmpV3PrivPassword;
	
	private String snmpV3EngineId;
	
	private String snmpV3DesEngineId;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SNMPServerConfig",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the hostIP
	 */
	@Column(name = "HOSTIP", nullable = false, length = 50)
	@XmlElement
	public String getHostIP() {
		return hostIP;
	}

	/**
	 * @return the port
	 */
	@Column(name = "PORT", nullable = true, length = 10)
	@XmlElement
	public String getPort() {
		return port;
	}

	/**
	 * @return the offset
	 */
	@Column(name = "PORTOFFSET", nullable = true, length = 5)
	@XmlElement
	public int getPortOffset() {
		return portOffset;
	}

	/**
	 * @return the community
	 */
	@Column(name = "COMMUNITY", nullable = true, length = 100)
	@XmlElement
	public String getCommunity() {
		return community;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false, length = 100)
	@XmlElement
	@Enumerated(EnumType.STRING)
	public SNMPServerType getType() {
		return type;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false,unique = true, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @return the version
	 */
	@Column(name = "VERSION", nullable = true, length = 10)
	@XmlElement
	public SNMPVersionType getVersion() {
		return version;
	}

	/**
	 * @return the advance
	 */
	@Column(name = "ADVANCE", nullable = true, length = 100)
	@XmlElement
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getAdvance() {
		return advance;
	}

	/**
	 * @return the configuredAlerts
	 */

	@OneToMany(mappedBy = "listener", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@XmlElement
	public List<SNMPAlertWrapper> getConfiguredAlerts() {
		return configuredAlerts;
	}

	/**
	 * @return the serverInstance
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_LISTENER_SRV_INSTANCE"))
	@XmlTransient
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param hostIP
	 *            the hostIP to set
	 */
	public void setHostIP(String hostIP) {
		this.hostIP = hostIP;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @param offset
	 *            the offset to set
	 */
	public void setPortOffset(int portOffset) {
		this.portOffset = portOffset;
	}

	/**
	 * @param community
	 *            the community to set
	 */
	public void setCommunity(String community) {
		this.community = community;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(SNMPServerType type) {
		this.type = type;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(SNMPVersionType version) {
		this.version = version;
	}

	/**
	 * @param advance
	 *            the advance to set
	 */
	public void setAdvance(boolean advance) {
		this.advance = advance;
	}

	/**
	 * @param configuredAlerts
	 *            the configuredAlerts to set
	 */
	public void setConfiguredAlerts(List<SNMPAlertWrapper> configuredAlerts) {
		this.configuredAlerts = configuredAlerts;
	}

	/**
	 * @param serverInstance
	 *            the serverInstance to set
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	/**
	 *  @return the snmpV3AuthAlgorithm
	 */
	@Column(name = "SNMPV3AUTHALGORITHM", nullable = true, length = 10)
	@XmlElement
	public String getSnmpV3AuthAlgorithm() {
		return snmpV3AuthAlgorithm;
	}

	/**
	 * @param snmpV3AuthAlgorithm
	 *            the snmpV3AuthAlgorithm to set
	 */
	public void setSnmpV3AuthAlgorithm(String snmpV3AuthAlgorithm) {
		this.snmpV3AuthAlgorithm = snmpV3AuthAlgorithm;
	}

	/**
	 *  @return the snmpV3AuthPassword
	 */
	@Column(name = "SNMPV3AUTHPASSWORD", nullable = true, length = 4000)
	@XmlElement
	public String getSnmpV3AuthPassword() {
		return snmpV3AuthPassword;
	}

	/**
	 * @param snmpV3AuthAlgorithm
	 *            the snmpV3AuthAlgorithm to set
	 */
	public void setSnmpV3AuthPassword(String snmpV3AuthPassword) {
		this.snmpV3AuthPassword = snmpV3AuthPassword;
	}

	/**
	 *  @return the snmpV3PrivAlgorithm
	 */
	@Column(name = "SNMPV3PRIVALGORITHM", nullable = true, length = 10)
	@XmlElement
	public String getSnmpV3PrivAlgorithm() {
		return snmpV3PrivAlgorithm;
	}

	/**
	 * @param snmpV3PrivAlgorithm
	 *            the snmpV3PrivAlgorithm to set
	 */
	public void setSnmpV3PrivAlgorithm(String snmpV3PrivAlgorithm) {
		this.snmpV3PrivAlgorithm = snmpV3PrivAlgorithm;
	}

	/**
	 *  @return the snmpV3PrivPassword
	 */
	@Column(name = "SNMPV3PRIVPASSWORD", nullable = true, length = 4000)
	@XmlElement
	public String getSnmpV3PrivPassword() {
		return snmpV3PrivPassword;
	}

	/**
	 * @param snmpV3PrivPassword
	 *            the snmpV3PrivPassword to set
	 */
	public void setSnmpV3PrivPassword(String snmpV3PrivPassword) {
		this.snmpV3PrivPassword = snmpV3PrivPassword;
	}
	
	/**
	 *  @return the snmpV3EngineId
	 */
	@Column(name = "SNMPV3ENGINEID", nullable = true, length = 100)
	@XmlTransient
	public String getSnmpV3EngineId() {
		return snmpV3EngineId;
	}

	/**
	 * @param snmpV3EngineId
	 *            the snmpV3EngineId to set
	 */
	public void setSnmpV3EngineId(String snmpV3EngineId) {
		this.snmpV3EngineId = snmpV3EngineId;
	}
	
	/**
	 *  @return the snmpV3DesEngineId Aes
	 */
	@Column(name = "SNMPV3DESENGINEID", nullable = true, length = 100)
	@XmlTransient
	public String getSnmpV3DesEngineId() {
		return snmpV3DesEngineId;
	}

	/**
	 * @param snmpV3DesEngineId 
	 *            the snmpV3DesEngineId to set
	 */
	public void setSnmpV3DesEngineId(String snmpV3DesEngineId) {
		this.snmpV3DesEngineId = snmpV3DesEngineId;
	}

}
