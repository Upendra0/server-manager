package com.elitecore.sm.snmp.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table( name="TBLMSNMPALERTTYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="snmpAlertTypeCache")
@XmlType(propOrder = { "id", "name","alias","category","description"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SNMPAlertType extends BaseModel{

	private static final long serialVersionUID = 8140479469087082912L;
	private int id;
	private String name;
	private String alias;
	private SNMPAlertTypeEnum category;
	private String description;
	
	/**
	 * 
	 * @return id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SNMPAlertType",allocationSize=1)
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
	 * @return name
	 */
	@Column(name = "NAME", nullable = false, unique=true,length = 255)
	@XmlElement
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return alias
	 */
	@Column(name="ALIAS",nullable=false,length=255)
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
	 * 
	 * @return category
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "CATEGORY", nullable = false, length = 100)
	@XmlElement
	public SNMPAlertTypeEnum getCategory() {
		return category;
	}
	
	/**
	 * 
	 * @param category
	 */
	public void setCategory(SNMPAlertTypeEnum category) {
		this.category = category;
	}
	
	/**
	 * 
	 * @return description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 100)
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
	
}
