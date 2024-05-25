/**
 * 
 */
package com.elitecore.sm.config.model;

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

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value="entityValidationRange")
@Scope(value="prototype")
@Entity
@Table(name="TBLMVALIDATIONRANGE")
public class EntityValidationRange extends BaseModel{

	
	private static final long serialVersionUID = -7634039995462453429L;

	private int id;
	private String entityType;
	private int minRange = 0;
	private int maxRange = 0;
	private String additionalCheckVal = "";
	private EntitiesRegex entitiesRegex;
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="EntitiesValidationRange",allocationSize=1)
	public int getId() {
		return id;
	}
	/**
	 * @return the entityType
	 */
	@Column(name="ENTITYTYPE", nullable = false, length = 100, unique=false)
	public String getEntityType() {
		return entityType;
	}
	/**
	 * @return the minRange
	 */
	@Column(name="MINRANGE", nullable = true,  unique=false)
	public int getMinRange() {
		return minRange;
	}
	/**
	 * @return the maxRange
	 */
	@Column(name="MAXRANGE", nullable = true, unique=false)
	public int getMaxRange() {
		return maxRange;
	}
	/**
	 * @return the entitiesRegex
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ENTITYREGEXID", nullable = true, foreignKey = @ForeignKey(name = "FK_ENTITY_REGEX_VALID_RANGE"))
	public EntitiesRegex getEntitiesRegex() {
		return entitiesRegex;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param entityType the entityType to set
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	/**
	 * @param minRange the minRange to set
	 */
	public void setMinRange(int minRange) {
		this.minRange = minRange;
	}
	/**
	 * @param maxRange the maxRange to set
	 */
	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}
	/**
	 * @param entitiesRegex the entitiesRegex to set
	 */
	public void setEntitiesRegex(EntitiesRegex entitiesRegex) {
		this.entitiesRegex = entitiesRegex;
	}
	
	@Column(name="ADDITIONALCHECKVAL", nullable = true, unique=false)
	public String getAdditionalCheckVal() {
		return additionalCheckVal;
	}
	
	public void setAdditionalCheckVal(String additionalCheckVal) {
		this.additionalCheckVal = additionalCheckVal;
	}
}
