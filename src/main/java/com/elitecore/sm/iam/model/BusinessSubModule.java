/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.List;

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
@Component(value="businessSubModuleObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMBUSINESSSUBMODULE")
@DynamicUpdate
public class BusinessSubModule extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 813232054686665315L;

	private int id;

	private String name;
	
	private String alias;
	
	private String description;
	
	private BusinessModule parentBusinessModule;
	
	private List<Action> actionList;

	@Column(name="alias", nullable = false, length = 255)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="BusinessSubModule",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="name", nullable = false, length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentbusinessmoduleid")
	public BusinessModule getParentBusinessModule() {
		return parentBusinessModule;
	}

	public void setParentBusinessModule(BusinessModule parentBusinessModule) {
		this.parentBusinessModule = parentBusinessModule;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentBusinessSubModule")
	public List<Action> getActionList() {
		return actionList;
	}

	public void setActionList(List<Action> actionList) {
		this.actionList = actionList;
	}	
}