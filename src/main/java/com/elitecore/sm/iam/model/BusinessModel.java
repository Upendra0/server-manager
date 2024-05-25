/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
@Component(value="businessModelObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMBUSINESSMODEL")
public class BusinessModel  extends BaseModel  implements Serializable{
	private static final long serialVersionUID = -7629147949049146742L;

	private int id;
	
	private String name;
	
	private String description;
	
	private String alias;
	
	private Set<BusinessModule> businessModuleList;
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		map.put("description", description);
		map.put("alias", alias);
		map.put("visible", status);
		return JSONValue.toJSONString(map);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="BusinessModel",allocationSize=1)
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

	@Column(name="alias", nullable = false, length = 255)
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentBusinessModel")
	public Set<BusinessModule> getBusinessModuleList() {
		return businessModuleList;
	}

	public void setBusinessModuleList(Set<BusinessModule> businessModuleList) {
		this.businessModuleList = businessModuleList;
	}

	@Column(name="description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}