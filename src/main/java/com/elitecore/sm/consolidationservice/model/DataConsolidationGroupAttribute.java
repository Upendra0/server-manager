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
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component("dataConsolidationGroupAttribute")
@Entity()
@Table(name = "TBLTCONGRPATTLIST")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="dataconservice")
public class DataConsolidationGroupAttribute extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7379329286720016349L;
	private int id;
	private String groupingField;
	private boolean regExEnable;
	private String regExExpression;
	private String destinationField;
	private boolean lookUpEnable;
	private String lookUpTableName;
	private String lookUpTableColumnName;
	private DataConsolidation dataConsolidation;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DataConsolidationGroupAttribute", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@Column(name = "GROUPINGFIELD", nullable = true, length = 200)
	public String getGroupingField() {
		return groupingField;
	}

	public void setGroupingField(String groupingField) {
		this.groupingField = groupingField;
	}

	@XmlElement
	@Type(type = "yes_no")
	@Column(name = "REGEXENABLE", nullable = true, length = 1)
	public boolean isRegExEnable() {
		return regExEnable;
	}

	public void setRegExEnable(boolean regExEnable) {
		this.regExEnable = regExEnable;
	}

	@XmlElement
	@Column(name = "REGEXEXPRESSION", nullable = true, length = 300)
	public String getRegExExpression() {
		return regExExpression;
	}

	public void setRegExExpression(String regExExpression) {
		this.regExExpression = regExExpression;
	}

	@XmlElement
	@Column(name = "DESTINATIONFIELD", nullable = true, length = 100)
	public String getDestinationField() {
		return destinationField;
	}

	public void setDestinationField(String destinationField) {
		this.destinationField = destinationField;
	}

	@XmlElement
	@Type(type = "yes_no")
	@Column(name = "LOOKUPENABLE", nullable = true, length = 1)
	public boolean isLookUpEnable() {
		return lookUpEnable;
	}

	public void setLookUpEnable(boolean lookUpEnable) {
		this.lookUpEnable = lookUpEnable;
	}

	@XmlElement
	@Column(name = "LOOKUPTABLENAME", nullable = true, length = 100)
	public String getLookUpTableName() {
		return lookUpTableName;
	}

	public void setLookUpTableName(String lookUpTableName) {
		this.lookUpTableName = lookUpTableName;
	}

	@XmlElement
	@Column(name = "LOOKUPTABLECOLUMNNAME", nullable = true, length = 100)
	public String getLookUpTableColumnName() {
		return lookUpTableColumnName;
	}

	public void setLookUpTableColumnName(String lookUpTableColumnName) {
		this.lookUpTableColumnName = lookUpTableColumnName;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONLIST_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_CON_GROUP_ATTRIBUTE"))
	@XmlTransient
	public DataConsolidation getDataConsolidation() {
		return dataConsolidation;
	}

	public void setDataConsolidation(DataConsolidation dataConsolidation) {
		this.dataConsolidation = dataConsolidation;
	}

}
