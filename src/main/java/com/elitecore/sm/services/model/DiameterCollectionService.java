package com.elitecore.sm.services.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.diameterpeer.model.DiameterPeer;

@Component(value = "DiameterCollectionService")
@XmlRootElement
@Entity
@Table(name = "TBLTDIAMETERCOLLSVC")
@Scope(value = "prototype")
@XmlType(propOrder = { "stackIp", "stackPort", "stackIdentity","stackRealm","sessionCleanupInterval","sessionTimeout","actionOnOverload",
		"resultCodeOnOverload","duplicateRequestCheck","duplicatePurgeInterval","fieldSeparator","keyValueSeparator","groupFieldSeparator",
		"diameterPeerList"})
public class DiameterCollectionService extends CollectionService {

	private static final long serialVersionUID = 1L;
	private String stackIp;
	private int stackPort=4000;
	private String stackIdentity;
	private String stackRealm;
	private int sessionCleanupInterval = 3600;
	private int sessionTimeout = 86400;
	private String actionOnOverload;
	private int resultCodeOnOverload = 3004;
	private boolean duplicateRequestCheck = false;
	private int duplicatePurgeInterval = 15;
	private String fieldSeparator;
	private String fieldSeparatorEnum;
	private String keyValueSeparator;
	private String keyValueSeparatorEnum;
	private String groupFieldSeparator;
	private String groupFieldSeparatorEnum;
	
	private List<DiameterPeer> diameterPeerList = new ArrayList<>();

	@XmlElement
	@Column(name = "STACKIP", nullable = true, length = 20)
	public String getStackIp() {
		return stackIp;
	}

	public void setStackIp(String stackIp) {
		this.stackIp = stackIp;
	}

	@XmlElement
	@Column(name = "STACKPORT", nullable = true,length=6) 
	public int getStackPort() {
		return stackPort;
	}

	public void setStackPort(int stackPort) {
		this.stackPort = stackPort;
	}

	@XmlElement
	@Column(name = "STACKIDENTITY", nullable = true,length=255) 
	public String getStackIdentity() {
		return stackIdentity;
	}

	public void setStackIdentity(String stackIdentity) {
		this.stackIdentity = stackIdentity;
	}

	@XmlElement
	@Column(name = "STACKREALM", nullable = true,length=255) 
	public String getStackRealm() {
		return stackRealm;
	}

	public void setStackRealm(String stackRealm) {
		this.stackRealm = stackRealm;
	}

	@XmlElement
	@Column(name = "SESSIONCLEANUPINTERVAL", nullable = true,length=6) 
	public int getSessionCleanupInterval() {
		return sessionCleanupInterval;
	}

	public void setSessionCleanupInterval(int sessionCleanupInterval) {
		this.sessionCleanupInterval = sessionCleanupInterval;
	}

	@XmlElement
	@Column(name = "SESSIONTIMEOUT", nullable = true,length=6) 
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	@XmlElement
	@Column(name = "ACTIONONOVERLOAD", nullable = true,length=10) 
	public String getActionOnOverload() {
		return actionOnOverload;
	}

	public void setActionOnOverload(String actionOnOverload) {
		this.actionOnOverload = actionOnOverload;
	}

	@XmlElement
	@Column(name = "RESULTCODEONOVERLOAD", nullable = true,length=6) 
	public int getResultCodeOnOverload() {
		return resultCodeOnOverload;
	}

	public void setResultCodeOnOverload(int resultCodeOnOverload) {
		this.resultCodeOnOverload = resultCodeOnOverload;
	}

	@XmlElement
	@Column(name = "DUPLICATEREQUESTCHECK", nullable = true,length=1) 
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isDuplicateRequestCheck() {
		return duplicateRequestCheck;
	}

	public void setDuplicateRequestCheck(boolean duplicateRequestCheck) {
		this.duplicateRequestCheck = duplicateRequestCheck;
	}

	@XmlElement
	@Column(name = "DUPLICATEPURGEINTERVAL", nullable = true,length=6) 
	public int getDuplicatePurgeInterval() {
		return duplicatePurgeInterval;
	}

	public void setDuplicatePurgeInterval(int duplicatePurgeInterval) {
		this.duplicatePurgeInterval = duplicatePurgeInterval;
	}

	@XmlElement
	@Column(name = "FIELDSEPARATOR", nullable = true,length=3) 
	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	@XmlElement
	@Column(name = "KEYVALUESEPARATOR", nullable = true,length=3) 
	public String getKeyValueSeparator() {
		return keyValueSeparator;
	}

	public void setKeyValueSeparator(String keyValueSeparator) {
		this.keyValueSeparator = keyValueSeparator;
	}

	@XmlElement
	@Column(name = "GROUPFIELDSEPARATOR", nullable = true,length=3) 
	public String getGroupFieldSeparator() {
		return groupFieldSeparator;
	}

	public void setGroupFieldSeparator(String groupFieldSeparator) {
		this.groupFieldSeparator = groupFieldSeparator;
	}

	/**
	 * @return the fieldSeparatorEnum
	 */
	@Transient
	@XmlTransient
	public String getFieldSeparatorEnum() {
		return fieldSeparatorEnum;
	}

	/**
	 * @param fieldSeparatorEnum the fieldSeparatorEnum to set
	 */
	public void setFieldSeparatorEnum(String fieldSeparatorEnum) {
		this.fieldSeparatorEnum = fieldSeparatorEnum;
	}

	/**
	 * @return the keyValueSeparatorEnum
	 */
	@Transient
	@XmlTransient
	public String getKeyValueSeparatorEnum() {
		return keyValueSeparatorEnum;
	}

	/**
	 * @param keyValueSeparatorEnum the keyValueSeparatorEnum to set
	 */
	public void setKeyValueSeparatorEnum(String keyValueSeparatorEnum) {
		this.keyValueSeparatorEnum = keyValueSeparatorEnum;
	}

	/**
	 * @return the groupFieldSeparatorEnum
	 */
	@Transient
	@XmlTransient
	public String getGroupFieldSeparatorEnum() {
		return groupFieldSeparatorEnum;
	}

	/**
	 * @param groupFieldSeparatorEnum the groupFieldSeparatorEnum to set
	 */
	public void setGroupFieldSeparatorEnum(String groupFieldSeparatorEnum) {
		this.groupFieldSeparatorEnum = groupFieldSeparatorEnum;
	}

	@XmlElement
	@OneToMany(mappedBy = "service",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@DiffIgnore
	public List<DiameterPeer> getDiameterPeerList() {
		return diameterPeerList;
	}
	public void setDiameterPeerList(List<DiameterPeer> diameterPeerList) {
		this.diameterPeerList = diameterPeerList;
	}
}
