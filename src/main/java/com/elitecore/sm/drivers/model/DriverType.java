/**
 * 
 */
package com.elitecore.sm.drivers.model;

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
 * @author vandana.awatramani This class defines template for all specifying
 *         types of drivers.
 */
@Entity
@Table(name = "TBLMDRIVERTYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverTypeCache")
@XmlType(propOrder = { "id", "alias","description","type","driverFullClassName","category"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DriverType extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5680350636647741498L;
	private int id;
	private String type;
	private DriverCategory category;
	private String description;
	private String alias;
	private String driverFullClassName;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="DriverType",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false, length = 100)
	@XmlElement
	public String getType() {
		return type;
	}

	/**
	 * @return the category
	 */
	@Column(name = "CATEGORY", nullable = false, length = 100)
	@XmlElement
	@Enumerated (EnumType.STRING)
	public DriverCategory getCategory() {
		return category;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 100)
	@XmlElement
	public String getDescription() {
		return description;
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
	 * @return the driverFullClassName
	 */
	@Column(name = "DRIVERFULLCLASSNAME", nullable = false, length = 1000)
	@XmlElement
	public String getDriverFullClassName() {
		return driverFullClassName;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(DriverCategory category) {
		this.category = category;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @param driverFullClassName
	 *            the driverFullClassName to set
	 */
	public void setDriverFullClassName(String driverFullClassName) {
		this.driverFullClassName = driverFullClassName;
	}
}
