/**
 * 
 */
package com.elitecore.sm.agent.model;

import java.io.Serializable;

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
import org.springframework.context.annotation.Scope;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.services.model.Service;

/**
 * @author vandana.awatramani
 *
 */

@Scope(value="prototype")
@Entity()
@Table(name = "TBLTSERVICEPKTCONFIG")
@DynamicUpdate
public class ServicePacketStatsConfig extends BaseModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2851808920364649652L;
	private int id;
	private Service service;
	private boolean enable;
	private PacketStatisticsAgent agent;

	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SERVICEPKT",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 * @return the service
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_ServicePacketStats"))
	public Service getService() {
		return service;
	}

	/**
	 * @return the enable
	 */
	@XmlElement
	@Column(name = "ENABLE", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @return the agent
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENTID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICE_AGENT"))
	@XmlTransient
	public PacketStatisticsAgent getAgent() {
		return agent;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(PacketStatisticsAgent agent) {
		this.agent = agent;
	}

}
