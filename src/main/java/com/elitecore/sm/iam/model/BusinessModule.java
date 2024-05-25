package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author Sunil Gulabani
 * Mar 31, 2015
 */
@Component(value="businessModuleObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMBUSINESSMODULE")
@DynamicUpdate
public class BusinessModule extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 3533930913829999167L;

	private int id;
	
	private String name;
	
	private String alias;
	
	private String description;
	
	private Set<BusinessSubModule> subModuleList;
	
	private BusinessModel parentBusinessModel;
	
	@Column(name="description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="name", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="BusinessModule",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="alias", nullable = false, length = 255)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentBusinessModule")
	public Set<BusinessSubModule> getSubModuleList() {
		return subModuleList;
	}

	public void setSubModuleList(Set<BusinessSubModule> subModuleList) {
		this.subModuleList = subModuleList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentbusinessmodelid")
	public BusinessModel getParentBusinessModel() {
		return parentBusinessModel;
	}

	public void setParentBusinessModel(BusinessModel parentBusinessModel) {
		this.parentBusinessModel = parentBusinessModel;
	}
}