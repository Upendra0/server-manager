package com.elitecore.sm.netflowclient.model;

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
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;



@Component("netflowProxyClient")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "TBLTNETFLOWPROXYCLIENT")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@XmlType(propOrder = {"id","proxyIp", "proxyPort"})
public class NatFlowProxyClient extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2243671253907414351L;

	private int id;
	private String proxyIp;
	private int proxyPort;
	private NetflowBinaryCollectionService service;
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="NetflowProxyClient",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="PROXYIP")
	@XmlElement
	public String getProxyIp() {
		return proxyIp;
	}
	public void setProxyIp(String proxyIp) {
		this.proxyIp = proxyIp;
	}
	
	@Column(name="PROXYPORT")
	@XmlElement
	public int getProxyPort() {
		return proxyPort;
	}
	
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVID", nullable = true, foreignKey = @ForeignKey(name = "FK_PXY_SVC_NETFLOW_CLIENT"))
	@XmlTransient
	@DiffIgnore
	public NetflowBinaryCollectionService getService() {
		return service;
	}

	public void setService(NetflowBinaryCollectionService service) {
		this.service = service;
	}
	
}
