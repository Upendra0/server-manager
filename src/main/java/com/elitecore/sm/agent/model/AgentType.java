package com.elitecore.sm.agent.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * 
 * @author avani.panchal
 *
 */
@Entity
@Table(name = "TBLMAGENTTYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="agentTypeCache")
@XmlType(propOrder = { "id", "type","alias","description","agentFullClassName"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class AgentType extends BaseModel{
	
	private static final long serialVersionUID = -5680350636647741498L;
	private int id;
	private String type;
	private String description;
	private String alias;
	private String agentFullClassName;
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="AgentType",allocationSize=1)
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
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false, length = 255)
	@XmlElement
	public String getType() {
		return type;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 255)
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return the alias
	 */
	@Column(name = "ALIAS", nullable = false, length = 255)
	@XmlElement
	public String getAlias() {
		return alias;
	}
	
	/**
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * @return the agentFullClassName
	 */
	@Column(name = "AGENTFULLCLASSNAME", nullable = false, length = 255)
	@XmlElement
	public String getAgentFullClassName() {
		return agentFullClassName;
	}
	/**
	 * 
	 * @param agentFullClassName
	 */
	public void setAgentFullClassName(String agentFullClassName) {
		this.agentFullClassName = agentFullClassName;
	}

}
