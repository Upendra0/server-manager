package com.elitecore.sm.policy.model;

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
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Sagar Ghetiya
 *
 */

@Component(value = "databaseQueryConditions")
@Entity
@Table(name = "TBLTDATABASEQUERYCONDITIONS")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder =  {"id","databaseFieldName", "policyConditionOperatorEnum", "unifiedField", "databaseKey"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DatabaseQueryCondition extends BaseModel implements Serializable{
	
	/**
	 * The constant serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The id in database
	 */
	private int id;
	
	/**
	 * The field name in database
	 */
	private String databaseFieldName;
	
	/**
	 * The field that contains a drop down of policyConditionOperatorEnum
	 */
	private String policyConditionOperatorEnum;
	
	/**
	 * The unified field in CDR
	 */
	private String unifiedField;

	/**
	 * The database query for relation with condition
	 */
	private DatabaseQuery databaseQuery;
	
	private boolean databaseKey;
	
	/**
	 * For hibernate since it does not require arguments
	 */
	public DatabaseQueryCondition(){
		super();
	}
	
	public DatabaseQueryCondition(String databaseFieldName, String policyConditionOperatorEnum,
			String unifiedField) {
		super();
		this.databaseFieldName = databaseFieldName;
		this.policyConditionOperatorEnum = policyConditionOperatorEnum;
		this.unifiedField = unifiedField;
	}

	/**
	 * Represents the primary key in the database
	 * @return id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DatabaseQueryCondition",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Represents the field name in database
	 * @return databaseFieldName
	 */
	@XmlElement
    @Column(name = "DATABASEFIELDNAME", nullable = false, length = 512, unique = false)
	public String getDatabaseFieldName() {
		return databaseFieldName;
	}

	/**
	 * @param databaseFieldName
	 */
	public void setDatabaseFieldName(String databaseFieldName) {
		this.databaseFieldName = databaseFieldName;
	}

	/**
	 * Represents the condition operator in the database
	 * @return policyConditionOperatorEnum
	 */
	@XmlElement
	@Column(name = "POLICYCONDITIONOPERATOR", nullable = false, length = 512, unique = false)
	public String getPolicyConditionOperatorEnum() {
		return policyConditionOperatorEnum;
	}

	/**
	 * @param policyConditionOperatorEnum
	 */
	public void setPolicyConditionOperatorEnum(String policyConditionOperatorEnum) {
		this.policyConditionOperatorEnum = policyConditionOperatorEnum;
	}

	/**
	 * Represents the unified field in 
	 * @return unifiedField
	 */
	@XmlElement
    @Column(name = "UNIFIEDFIELD", nullable = true, length = 512, unique = false)
	public String getUnifiedField() {
		return unifiedField;
	}

	/**
	 * @param unifiedField
	 */
	public void setUnifiedField(String unifiedField) {
		this.unifiedField = unifiedField;
	}

	/**
	 * Represents the query assigned to condition
	 * @return database Query
	 */
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "DATABASEQUERYID", nullable = true, foreignKey = @ForeignKey(name = "FK_QUERY_ID_QUERYACTION"))
	@DiffIgnore
	public DatabaseQuery getDatabaseQuery() {
		return databaseQuery;
	}

	public void setDatabaseQuery(DatabaseQuery databaseQuery) {
		this.databaseQuery = databaseQuery;
	}

	@XmlElement
	@Column(name = "DBKEYENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isDatabaseKey() {
		return databaseKey;
	}

	public void setDatabaseKey(boolean databaseKey) {
		this.databaseKey = databaseKey;
	}
	
}
