/**
 * 
 */
package com.elitecore.sm.agent.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.json.simple.JSONValue;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani
 *
 */

@Entity()
@Table(name = "TBLTAGENT")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING,length=100)
@XmlSeeAlso({FileRenameAgent.class,PacketStatisticsAgent.class})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="agentCache")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Agent extends BaseModel{
	
	private static final long serialVersionUID = -7196193478301617157L;
	private int id;
	
	private ServerInstance serverInstance;
	private AgentType agentType;
	// measured in mSec
	private int initialDelay;
	private long executionInterval=60000;
	
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>(); // sonar issue L68
		map.put("id", id);
		if(serverInstance!=null){
			map.put("serverInstance", serverInstance.toString());
		}
	
		return JSONValue.toJSONString(map);
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="Agent",allocationSize=1)
	@Column(name = "ID")
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the serverInstance
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_AGENT_SRV_INSTANCE"))
	@XmlTransient
	public ServerInstance getServerInstance() {
		return serverInstance;
	}
	
	/**
	 * @return the initialDelay
	 */
	@Column(name = "INITIALDELAY", nullable = true)
	@XmlElement
	public int getInitialDelay() {
		return initialDelay;
	}

	/**
	 * @return the executionInterval
	 */
	@Column(name = "EXECUTIONINTERVAL", nullable = true)
	@XmlElement
	public long getExecutionInterval() {
		return executionInterval;
	}

	/**
	 * @param initialDelay the initialDelay to set
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * @param executionInterval the executionInterval to set
	 */
	public void setExecutionInterval(long executionInterval) {
		this.executionInterval = executionInterval;
	}


	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int agentId) {
		this.id = agentId;
	}

	/**
	 * @param serverInstance
	 *            the serverInstance to set
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	/**
	 * 
	 * @return agentType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENTTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_AGENT_AGENTTYPE"))
	@DiffIgnore
	public AgentType getAgentType() {
		return agentType;
	}

	/**
	 * 
	 * @param agentType
	 */
	public void setAgentType(AgentType agentType) {
		this.agentType = agentType;
	}

}
