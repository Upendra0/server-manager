package com.elitecore.sm.migration.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author brijesh.soni
 *
 * This class holds mapping data for xml to sm class, jaxb class and xsd file
 * 
 */
@Component(value = "migrationEntityMapping")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLMMIGRATIONENTITYMAPPING")
@DynamicUpdate
public class MigrationEntityMapping extends BaseModel implements Serializable {

	private static final long serialVersionUID = 2878214705929535882L;
	private int id;
	private String xmlName;
	private String jaxbClassName;
	private String smClassName;
	private String xsdName;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "MigrationEntityMapping", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "XMLNAME", nullable = false, unique = true, length = 250)
	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	@Column(name = "JAXBCLASSNAME", nullable = false, unique = true, length = 250)
	public String getJaxbClassName() {
		return jaxbClassName;
	}

	public void setJaxbClassName(String jaxbClassName) {
		this.jaxbClassName = jaxbClassName;
	}

	@Column(name = "SMCLASSNAME", nullable = false, unique = true, length = 250)
	public String getSmClassName() {
		return smClassName;
	}

	public void setSmClassName(String smClassName) {
		this.smClassName = smClassName;
	}

	@Column(name = "XSDNAME", nullable = false, unique = true, length = 250)
	public String getXsdName() {
		return xsdName;
	}

	public void setXsdName(String xsdName) {
		this.xsdName = xsdName;
	}

}
