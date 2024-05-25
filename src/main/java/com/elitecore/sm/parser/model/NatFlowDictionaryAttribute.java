/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value = "natFlowDictionaryAttribute")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLMNETFLOWATTRIBUTEDICTIONARY")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="dictionaryAttributeCache")
@XmlRootElement(name="field")
@XmlType(propOrder = { "id","elementId", "name","dataType","applicability","group","dataTypeSemantics"})
public class NatFlowDictionaryAttribute extends BaseModel{

	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String elementId;
    private String dataType;
    private String fieldStatus;
    private String name;
    private String applicability;
    private String group;
    private String dataTypeSemantics;
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="ATTRFIELD",allocationSize=1)
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
	 * @return the elementId
	 */
	@Column(name = "ELEMENTID", nullable = false, length = 5)
    @XmlAttribute
	public String getElementId() {
		return elementId;
	}
	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	/**
	 * @return the dataType
	 */
	@Column(name = "DATATYPE", nullable = true, length = 25)
	@XmlAttribute
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the status
	 */
	@Column(name = "FIELDSTATUS", nullable = true, length = 7)
	@XmlAttribute
	/**
	 * @return the fieldStatus
	 */
	public String getFieldStatus() {
		return fieldStatus;
	}
	/**
	 * @param fieldStatus the fieldStatus to set
	 */
	public void setFieldStatus(String fieldStatus) {
		this.fieldStatus = fieldStatus;
	}
	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 50)
	@XmlAttribute
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
	 * @return the applicability
	 */
	@Column(name = "APPLICABILITY", nullable = true, length = 30)
	@XmlAttribute
	public String getApplicability() {
		return applicability;
	}
	/**
	 * @param applicability the applicability to set
	 */
	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}
	/**
	 * @return the group
	 */
	@Column(name = "ATTRGROUP", nullable = true, length = 18)
	@XmlAttribute
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the dataTypeSemantics
	 */
	@Column(name = "DATATYPESEMANTICS", nullable = true, length = 18)
	@XmlAttribute
	public String getDataTypeSemantics() {
		return dataTypeSemantics;
	}
	/**
	 * @param dataTypeSemantics the dataTypeSemantics to set
	 */
	public void setDataTypeSemantics(String dataTypeSemantics) {
		this.dataTypeSemantics = dataTypeSemantics;
	}	
	
}
