/**
 * 
 */
package com.elitecore.sm.snmp.model;

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

import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani This class represents the data model for SNMP
 *         alerts.
 */
@Component(value = "snmpAlert")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTSNMPALERT")
@DynamicUpdate
@XmlRootElement
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SNMPAlert extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4472419188458339474L;
	private int id;
	private String alertId;
	private String name;
	private String desc;
	private int threshold;

	
	SNMPAlertType alertType;
	

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SNMPAlert",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 *  
	 * @return alertId;
	 */
	@XmlElement
	@Column(name = "ALERTID", unique = true, nullable = false, length= 100)
	public String getAlertId() {
		return alertId;
	}

	/**
	 * @return the name
	 */
	@XmlElement
	@Column(name = "NAME", unique = true, length=100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @return the desc
	 */
	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true)
	public String getDesc() {
		return desc;
	}

	/**
	 * @return the threshold
	 */
	@XmlElement
	@Column(name = "THRESHOLD", nullable = false, length = 5)
	public int getThreshold() {
		return threshold;
	}
	
	
	/**
	 * 
	 * @return alertType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ALERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_ALERT_ALERTYPE"))
	@DiffIgnore
	public SNMPAlertType getAlertType() {
		return alertType;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param alertId
	 */
	public void setAlertId(String alertId) {
		this.alertId = alertId;
	}
	
	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @param threshold
	 *            the threshold to set
	 */
	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}
	
	/**
	 * 
	 * @param alertType
	 */
	public void setAlertType(SNMPAlertType alertType) {
		this.alertType = alertType;
	}
	



}
