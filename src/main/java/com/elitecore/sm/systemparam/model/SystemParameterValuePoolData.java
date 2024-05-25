/**
 * 
 */
package com.elitecore.sm.systemparam.model;

import javax.persistence.Cacheable;
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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@Table(name = "TBLMSYSTEMPARAMETERVALUEPOOL")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="systemParameterValuePoolData")
public class SystemParameterValuePoolData extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3361262519080269947L;
	private int id;
	private String name;
	private String value;
	private SystemParameterData parentSystemParameter;

	public SystemParameterValuePoolData() {
		//Default Constructor
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param parentSystemParameter
	 */
	public SystemParameterValuePoolData(String name, String value,
			SystemParameterData parentSystemParameter) {
		super();
		this.name = name;
		this.value = value;
		this.parentSystemParameter = parentSystemParameter;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	@Column(name = "VALUE", nullable = false, length = 100)
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the parentSystemParameter
	 */
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PARAMETERID", nullable = false, foreignKey = @ForeignKey(name = "FK_SYSVALUEPOOL_SYSPARAM"))
	@DiffIgnore
	public SystemParameterData getParentSystemParameter() {
		return parentSystemParameter;
	}

	/**
	 * @param parentSystemParameter
	 *            the parentSystemParameter to set
	 */
	public void setParentSystemParameter(
			SystemParameterData parentSystemParameter) {
		this.parentSystemParameter = parentSystemParameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SystemParameterValuePoolData [id=" + id
				+ ", name=" + name + ", value=" + value + "]";
	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "PARAMETERPOOLID", length = 8)
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="SystemParameterValuePoolData",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

}
