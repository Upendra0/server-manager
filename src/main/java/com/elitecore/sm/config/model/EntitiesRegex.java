/**
 * 
 */
package com.elitecore.sm.config.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
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
@Component(value="entitiesRegexObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMREGEX")
public class EntitiesRegex extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7634039995462453429L;

	private int id;
	private String entity;
	private String key;
	private String value;
	private String description;
	private List<EntityValidationRange> validationRangeList = new ArrayList<>(0);
	
	public EntitiesRegex() {
		//Default Constructor
	}

	
	public EntitiesRegex(String entity, String key, String value, String description) {
		this.entity = entity;
		this.key = key;
		this.description = description;
		this.value = value;
	}
	
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("entity", entity);
		map.put("key", key);
		map.put("value", value);
		map.put("description", description);
		return JSONValue.toJSONString(map);
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="EntitiesRegex",allocationSize=1)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="REGEXKEY", nullable = false, length = 255, unique=true)
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name="value", nullable = true, length = 2000)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name="description", nullable = true, length = 500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="entity", nullable = false, length = 255, unique=false)
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * @return the validationRangeList
	 */
	@OneToMany(mappedBy = "entitiesRegex" ,cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	public List<EntityValidationRange> getValidationRangeList() {
		return validationRangeList;
	}

	/**
	 * @param validationRangeList the validationRangeList to set
	 */
	public void setValidationRangeList(
			List<EntityValidationRange> validationRangeList) {
		this.validationRangeList = validationRangeList;
	}
}
