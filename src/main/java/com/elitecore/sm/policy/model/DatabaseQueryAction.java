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
 * The model for database query action
 * @author Sagar Ghetiya
 *
 */

@Component(value = "databaseQueryActions")
@Entity
@Table(name = "TBLTDATABASEQUERYACTIONS")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder =  {"id","databaseFieldName", "unifiedField"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DatabaseQueryAction extends BaseModel implements Serializable{
	
	/**
	 * The constant serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The id of database action
	 */
	private int id;
	
	/**
	 * The name of the field in database
	 */
	private String databaseFieldName;
	
	/**
	 * The unified field in CDR's
	 */
	private String unifiedField;

	/**
	 * The databaseQuery to which it is mapped
	 */
	private DatabaseQuery databaseQuery;
	
	
	/**
	 * For Hibernate since it does not require arguments
	 */
	public DatabaseQueryAction(){
		super();
	}
	
	public DatabaseQueryAction(String databaseFieldName, String unifiedField,
			DatabaseQuery databaseQuery) {
		super();
		this.databaseFieldName = databaseFieldName;
		this.unifiedField = unifiedField;
		this.databaseQuery = databaseQuery;
	}

	/**
	 * Represents the id of the field to be stored in  database
	 * @return id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DatabaseQueryAction",allocationSize=1)
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
	 * Represents the field name in the database
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
	 * Represents the unified field in the CDR
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
	 * Represents the databaseQuery to which it is mapped
	 * @return databaseQuery
	 */
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "DATABASEQUERYID", nullable = true, foreignKey = @ForeignKey(name = "FK_QUERY_ID_QUERYACTION"))
	@DiffIgnore
	public DatabaseQuery getDatabaseQuery() {
		return databaseQuery;
	}

	/**
	 * @param databaseQuery
	 */
	public void setDatabaseQuery(DatabaseQuery databaseQuery) {
		this.databaseQuery = databaseQuery;
	}
	
}
