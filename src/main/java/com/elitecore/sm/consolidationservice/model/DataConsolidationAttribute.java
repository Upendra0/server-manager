package com.elitecore.sm.consolidationservice.model;

import java.io.Serializable;

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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component("dataConsolidationAttribute")
@Entity()
@Table(name = "TBLTCONATTLIST")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="dataconservice")
public class DataConsolidationAttribute extends BaseModel implements Serializable {

	private static final long serialVersionUID = -3864427928997141703L;
	private int id;
	private String fieldName;
	private String dataType;
	private String operation;
	private String description;
	private DataConsolidation dataConsolidation;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DataConsolidationAttribute", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@Column(name = "NAME", nullable = true, length = 200)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@XmlElement
	@Column(name = "DATATYPE", nullable = true, length = 15)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@XmlElement
	@Column(name = "OPERATION", nullable = true, length = 10)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true, length = 400)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONLIST_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CON_ATTRIBUTE"))
	@XmlTransient
	public DataConsolidation getDataConsolidation() {
		return dataConsolidation;
	}

	public void setDataConsolidation(DataConsolidation dataConsolidation) {
		this.dataConsolidation = dataConsolidation;
	}
}
