package com.elitecore.sm.snmp.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

@Component(value = "snmpServiceThreshold")
@Entity()
@Table(name = "TBLTSNMPSERVICETHRESHOLD")
@DynamicUpdate
public class SNMPServiceThreshold extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2655116510568287599L;
	private int id;
	private int threshold;
	private SNMPAlertWrapper wrapper;
	private Service service;
	private ServerInstance serverInstance;
	
	

	/**
	 * 
	 * @return id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SNMPServiceThreshold",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * 
	 * @return threshold
	 */
	@Column(name = "THRESHOLD",  nullable = true)
	@XmlElement
	public int getThreshold() {
		return threshold;
	}
	/**
	 * 
	 * @param threshold
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * 
	 * @return wrapper
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SNMPWRAPPERID", nullable = false, foreignKey = @ForeignKey(name = "FK_SNMPWRAPPER_SVCTHRESHOLD"))
	@XmlTransient
	public SNMPAlertWrapper getWrapper() {
		return wrapper;
	}
	
	/**
	 * 
	 * @param wrapper
	 */
	public void setWrapper(SNMPAlertWrapper wrapper) {
		this.wrapper = wrapper;
	}
	
	/**
	 * 
	 * @return service
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY, optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SVCTHRESHOLD_SVCID"))
	public Service getService() {
		return service;
	}
	
	/**
	 * 
	 * @param service
	 */
	public void setService(Service service) {
		this.service = service;
	}
	
	/**
	 * 
	 * @return
	 */
	@XmlTransient
	@OneToOne(fetch = FetchType.EAGER, optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SVCTHRESHOLD_SIID"))
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	/**
	 * 
	 * @param serverInstance
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	@Override
	public Object clone() {
		SNMPServiceThreshold serviceThreshold = null;
		try {
			serviceThreshold = (SNMPServiceThreshold) super.clone();
			serviceThreshold.setId(0);
		} catch (CloneNotSupportedException e) {
			logger.error("Clone not supported", e);
		}
		return serviceThreshold;
	}

}
