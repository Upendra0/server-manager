package com.elitecore.sm.license.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * 
 * @author nandish.patel
 *
 */
@Component(value = "circle")
@Entity()
@Table(name = "TBLMCIRCLE")
@DynamicUpdate
@XmlType(propOrder = { "id", "name","description"})
public class Circle extends BaseModel {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	private boolean isAssociated;
	private boolean isLicenseApplied;
	private LicenseTypeEnum licenseType;
	private boolean isLicenseExhausted;

	public Circle() {
	}

	public Circle(int id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public Circle(int id, String name, String description, boolean isAssociated, boolean isLicenseApplied) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.isAssociated = isAssociated;
		this.isLicenseApplied = isLicenseApplied;
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "Circle", allocationSize = 1)
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 20, unique = true)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	@XmlTransient
	public boolean isAssociated() {
		return isAssociated;
	}

	public void setAssociated(boolean isAssociated) {
		this.isAssociated = isAssociated;
	}

	@Transient
	@XmlTransient
	public boolean isLicenseApplied() {
		return isLicenseApplied;
	}

	public void setLicenseApplied(boolean isLicenseApplied) {
		this.isLicenseApplied = isLicenseApplied;
	}

	@Transient
	@XmlTransient
	public LicenseTypeEnum getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(LicenseTypeEnum licenseType) {
		this.licenseType = licenseType;
	}

	@Transient
	@XmlTransient
	public boolean isLicenseExhausted() {
		return isLicenseExhausted;
	}

	public void setLicenseExhausted(boolean isLicenseExhausted) {
		this.isLicenseExhausted = isLicenseExhausted;
	}
	
}