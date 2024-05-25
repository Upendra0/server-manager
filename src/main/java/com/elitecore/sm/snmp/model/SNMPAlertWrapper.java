/**
 * 
 */
package com.elitecore.sm.snmp.model;

import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vandana.awatramani
 *
 */
@Component(value = "snmpAlertWrapper")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTSNMPALERTWRAPPER" ,uniqueConstraints=@UniqueConstraint(columnNames={"LISTENERID", "ALERTID"}))
@DynamicUpdate
@XmlRootElement
public class SNMPAlertWrapper extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1128618007624153271L;
	private int id;
	private SNMPServerConfig listener;
	private SNMPAlert alert;
	private List<SNMPServiceThreshold> serviceThreshold;
	private boolean isServiceThresholdConfigured=false;
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SNMPAlertWrapper",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 * @return the listener
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LISTENERID", nullable = false, foreignKey = @ForeignKey(name = "FK_ALERT_LISTENER"))
	@XmlTransient
	public SNMPServerConfig getListener() {
		return listener;
	}

	/**
	 * @return the alert
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "ALERTID", nullable = true, foreignKey = @ForeignKey(name = "FK_ALERT_ALERTWRAPPER"))
	public SNMPAlert getAlert() {
		return alert;
	}
	
	/**
	 * 
	 * @return serviceThreshold
	 */
	@OneToMany(mappedBy = "wrapper",cascade=CascadeType.ALL)
	@XmlElement
	public List<SNMPServiceThreshold> getServiceThreshold() {
		return serviceThreshold;
	}
	
	/**
	 * 
	 * @return isServiceThresholdConfigured
	 */
	@XmlElement
	@Column(name = "SVCTHRESHOLDCONFIGURED", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isServiceThresholdConfigured() {
		return isServiceThresholdConfigured;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param listener the listener to set
	 */
	public void setListener(SNMPServerConfig listener) {
		this.listener = listener;
	}

	/**
	 * @param alert the alert to set
	 */
	public void setAlert(SNMPAlert alert) {
		this.alert = alert;
	}
	/**
	 * 
	 * @param serviceThreshold
	 */
	public void setServiceThreshold(List<SNMPServiceThreshold> serviceThreshold) {
		this.serviceThreshold = serviceThreshold;
	}
	
	/**
	 * 
	 * @param isServiceThresholdConfigured
	 */
	public void setServiceThresholdConfigured(boolean isServiceThresholdConfigured) {
		this.isServiceThresholdConfigured = isServiceThresholdConfigured;
	}

}
