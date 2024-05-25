/**
 * 
 */
package com.elitecore.sm.server.model;

import java.io.Serializable;

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
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
/**
 * @author jay.shah
 *
 */

@Entity()
@Table(name = "TBLMSERVERTYPE")
@DynamicUpdate
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="serverTypeCache")
@XmlType(propOrder = { "id", "alias","name","description"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ServerType extends BaseModel implements Serializable {

	/**
	 * generated uid
	 */
	private static final long serialVersionUID = -8170955491617673928L;

	// class fields
	private int id;
	private String alias;
	private String name;
	private String description;
	

	public ServerType() {
		//Default constructor.
	}

	/**
	 * @return the serverTypeId which is PK for this entity
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ServerType",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the serverTypeAlias
	 */
	@Column(name = "ALIAS", nullable = false, length = 100)
	@XmlElement
	public String getAlias() {
		return alias;
	}

	/**
	 * @return the Name
	 */
	@Column(name = "NAME", nullable = false, length = 100)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @return the Description
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * @param Id
	 *            the Id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param Alias
	 *            the Alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @param Name
	 *            the Name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param Description
	 *            the Description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}