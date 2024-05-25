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

@Component(value="aggregationcondition")
@Entity()
@Table(name = "TBLTCONDITION")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "aggregationcondition")
@XmlType(propOrder = { "id","condExpression", "condAction"})
public class AggregationCondition extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private String condExpression;
	private String condAction;
	private int id;
	private AggregationDefinition aggregationDefinition;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "AggregationCondition", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "CONDACTION")
	public String getCondAction() {
		return condAction;
	}
	
	public void setCondAction(String condAction) {
		this.condAction = condAction;
	}
	
	@Column(name = "CONDEXPRESSION")
	public String getCondExpression() {
		return condExpression;
	}
	public void setCondExpression(String condExpression) {
		this.condExpression = condExpression;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AGGDEFID", nullable = false, foreignKey = @ForeignKey(name = "FK_TBLTCONDITION"))
	@XmlTransient
	public AggregationDefinition getAggregationDefinition() {
		return aggregationDefinition;
	}
	
	public void setAggregationDefinition(AggregationDefinition aggregationDefinition) {
		this.aggregationDefinition = aggregationDefinition;
	}
	
}
