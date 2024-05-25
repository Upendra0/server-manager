package com.elitecore.sm.aggregationservice.model;

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
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component(value="aggregationAttribute")
@Entity()
@Table(name = "TBLTAGGATTRIBUTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "aggregationAttribute")
@XmlType(propOrder = { "id","outputFieldName","outputExpression","outputDataType"})
//,uniqueConstraints=@UniqueConstraint(columnNames={"OUTPUTFIELDNAME,AGGDEFID"})
public class AggregationAttribute extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String outputFieldName;
	private String outputExpression;
	private String outputDataType;
	private AggregationDefinition aggregationDefinition;
	private int id;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "AggregationAttribute", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "OUTPUTFIELDNAME")
	public String getOutputFieldName() {
		return outputFieldName;
	}
	public void setOutputFieldName(String outputFieldName) {
		this.outputFieldName = outputFieldName;
	}
	
	@Column(name = "OUTPUTEXPRESSION")
	public String getOutputExpression() {
		return outputExpression;
	}
	public void setOutputExpression(String outputExpression) {
		this.outputExpression = outputExpression;
	}
		
	@Column(name = "DATATYPE")
	public String getOutputDataType() {
		return outputDataType;
	}
	public void setOutputDataType(String outputDataType) {
		this.outputDataType = outputDataType;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGGDEFID", nullable = false, foreignKey = @ForeignKey(name = "FK_TBLTAGGATTRIBUTE"))
	@XmlTransient
	public AggregationDefinition getAggregationDefinition() {
		return aggregationDefinition;
	}
	public void setAggregationDefinition(AggregationDefinition aggregationDefinition) {
		this.aggregationDefinition = aggregationDefinition;
	}
}
