package com.elitecore.sm.aggregationservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.parser.model.UnifiedDateFieldEnum;
import com.elitecore.sm.services.model.AggregationService;

@Component(value="aggregationDefinition")
@Entity()
@Table(name = "TBLTAGGDEFINITION",uniqueConstraints=@UniqueConstraint(columnNames={"AGGDEFNAME"}))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "aggregationDefinition")
@XmlType(propOrder = { "id", "aggDefName", "noOfPartition","partCDRField","fLegVal","lLegVal","unifiedDateFiled","aggInterval","outputFileField","aggConditionList","aggKeyAttrList","aggAttrList"})

public class AggregationDefinition extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String aggDefName;
	private int noOfPartition = 1;
	private String partCDRField;
	private String fLegVal;
	private String lLegVal;
	private String unifiedDateFiled = UnifiedDateFieldEnum.StartDate.getName();
	private int aggInterval = 30;
	private int outputFileField;
	
	// OneToMany Relation with aggregation condition
	private List<AggregationCondition> aggConditionList = new ArrayList<>();
	private List<AggregationKeyAttribute> aggKeyAttrList = new ArrayList<>();
	private List<AggregationAttribute> aggAttrList = new ArrayList<>();
	
	private AggregationService  aggregationService;

	@Id
	@Column(name = "AGGDEFID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DataAggregaton", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@OneToMany(mappedBy = "aggregationDefinition", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SELECT)
	public List<AggregationCondition> getAggConditionList() {
		return aggConditionList;
	}

	public void setAggConditionList(List<AggregationCondition> aggConditionList) {
		this.aggConditionList = aggConditionList;
	}
	
	@XmlElement
	@OneToMany(mappedBy = "aggregationDefinition", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SELECT)
	public List<AggregationKeyAttribute> getAggKeyAttrList() {
		return aggKeyAttrList;
	}

	public void setAggKeyAttrList(List<AggregationKeyAttribute> aggKeyAttrList) {
		this.aggKeyAttrList = aggKeyAttrList;
	}

	@XmlElement
	@OneToMany(mappedBy = "aggregationDefinition", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SELECT)
	public List<AggregationAttribute> getAggAttrList() {
		return aggAttrList;
	}

	public void setAggAttrList(List<AggregationAttribute> aggAttrList) {
		this.aggAttrList = aggAttrList;
	}

	@Column(name = "NOOFPARTITION")
	public int getNoOfPartition() {
		return noOfPartition;
	}

	public void setNoOfPartition(int noOfPartition) {
		this.noOfPartition = noOfPartition;
	}

	@Column(name = "PARTCDRFIELD")
	public String getPartCDRField() {
		return partCDRField;
	}

	public void setPartCDRField(String partCDRField) {
		this.partCDRField = partCDRField;
	}

	@Column(name = "FLEGVAL")
	public String getfLegVal() {
		return fLegVal;
	}

	public void setfLegVal(String fLegVal) {
		this.fLegVal = fLegVal;
	}

	@Column(name = "LLEGVAL")
	public String getlLegVal() {
		return lLegVal;
	}

	public void setlLegVal(String lLegVal) {
		this.lLegVal = lLegVal;
	}
	
	@Column(name = "UNIFIEDDATEFILED")
	public String getUnifiedDateFiled() {
		return unifiedDateFiled;
	}

	public void setUnifiedDateFiled(String unifiedDateFiled) {
		this.unifiedDateFiled = unifiedDateFiled;
	}
	
	@Column(name = "AGGINTERVAL")
	public int getAggInterval() {
		return aggInterval;
	}

	public void setAggInterval(int aggInterval) {
		this.aggInterval = aggInterval;
	}
		
	@Column(name = "OUTPUTFILEFIELD")
	public int getOutputFileField() {
		return outputFileField;
	}

	public void setOutputFileField(int outputFileField) {
		this.outputFileField = outputFileField;
	}
	
	@Column(name = "AGGDEFNAME")
	public String getAggDefName() {
		return aggDefName;
	}

	public void setAggDefName(String aggDefName) {
		this.aggDefName = aggDefName;
	}

	@XmlTransient
	@OneToOne(fetch = FetchType.LAZY, mappedBy="aggregationDefinition")
	public AggregationService getAggregationService() {
		return aggregationService;
	}

	public void setAggregationService(AggregationService aggregationService) {
		this.aggregationService = aggregationService;
	}}
