package com.elitecore.sm.consolidationservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.parser.model.UnifiedDateFieldEnum;
import com.elitecore.sm.services.model.DataConsolidationService;

@Component("dataConsolidation")
@Entity()
@Table(name = "TBLTCONSOLIDATIONLIST",uniqueConstraints=@UniqueConstraint(columnNames={"NAME", "CONSRVID"}))
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "dataconservice")
public class DataConsolidation extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1359726233372759767L;
	private int id;

	// Consolidation Parameters
	private String consName;
	private String dateFieldName ;
	private SegregationTypeEnum segregationType = SegregationTypeEnum.MINUTE;

	// OneToMany Relation with Consolidation Parameters & AttributeList
	private List<DataConsolidationAttribute> consAttList = new ArrayList<>();

	// OneToMany Relation with Consolidation Parameters & Group Attribute List
	private List<DataConsolidationGroupAttribute> consGrpAttList = new ArrayList<>();

	// ManyToOne Relation with Consolidation Parameters & Service Parameters
	private DataConsolidationService consService;
	
	private Integer acrossFilePartition = 100;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "DataConsolidation", allocationSize = 1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	@Column(name = "NAME", nullable = false, length = 200)
	public String getConsName() {
		return consName;
	}

	public void setConsName(String consName) {
		this.consName = consName;
	}

	@XmlElement
	@Column(name = "DATEFIELDNAME", nullable = true, length = 200)
	public String getDateFieldName() {
		return dateFieldName;
	}

	public void setDateFieldName(String dateFieldName) {
		this.dateFieldName = dateFieldName;
	}

	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "SEGREGATIONTYPE", nullable = true, length = 10)
	public SegregationTypeEnum getSegregationType() {
		return segregationType;
	}

	public void setSegregationType(SegregationTypeEnum segregationType) {
		this.segregationType = segregationType;
	}

	@XmlElement
	@OneToMany(mappedBy = "dataConsolidation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SELECT)
	public List<DataConsolidationAttribute> getConsAttList() {
		return consAttList;
	}

	public void setConsAttList(List<DataConsolidationAttribute> conAttList) {
		this.consAttList = conAttList;
	}

	@XmlElement
	@OneToMany(mappedBy = "dataConsolidation", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SELECT)
	public List<DataConsolidationGroupAttribute> getConsGrpAttList() {
		return consGrpAttList;
	}

	public void setConsGrpAttList(List<DataConsolidationGroupAttribute> consGrpAttList) {
		this.consGrpAttList = consGrpAttList;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CONSRVID", nullable = false, foreignKey = @ForeignKey(name = "FK_CON_LIST"))
	@Fetch(value = FetchMode.SELECT)
	@XmlTransient
	public DataConsolidationService getConsService() {
		return consService;
	}

	public void setConsService(DataConsolidationService consService) {
		this.consService = consService;
	}

	@XmlElement
	@Column(name = "ACROSSFILEPARTITION", nullable = true, length = 5)
	public Integer getAcrossFilePartition() {
		return acrossFilePartition;
	}

	public void setAcrossFilePartition(Integer acrossFilePartition) {
		this.acrossFilePartition = acrossFilePartition;
	}

}
