package com.elitecore.sm.policy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * The model for database query
 * @author Sagar Ghetiya
 *
 */
@Component(value = "databaseQuery")
@Entity
@Table(name = "TBLTDATABASEQUERY")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder = {"id", "alias", "queryName", "queryValue", "returnMultipleRowsEnable" ,"cacheEnable", "logicalOperator", "conditionExpressionEnable", "conditionExpression", "databaseQueryConditions", "databaseQueryActions", "outputDbField", "description", "associationStatus"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DatabaseQuery extends BaseModel implements Serializable {

	/**
	 * The Constant serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The id for database storage
	 */
	private int id;
	
	/**
	 * The alias for a query
	 */
	private String alias;
	
	/**
	 * The name of the query
	 */
	private String queryName;
	
	/**
	 * The value of query
	 */
	private String queryValue;
	
	/**
	 * The description of the query
	 */
	private String description;
	
	/**
	 * The output DB field for query
	 */
	private String outputDbField;
	
	/**
	 * The returnMultipleRowsEnable type
	 */
	private boolean returnMultipleRowsEnable = true;
	
	/**
	 * The cacheEnable type
	 */
	private boolean cacheEnable;
	
	/**
	 * The conditionExpressionEnable for query
	 */
	private boolean conditionExpressionEnable;
	
	/**
	 * The condition expression for query
	 */
	private String conditionExpression;

	/**
	 * The serverInstanceId
	 */
	private ServerInstance serverInstance;
	
	/**
	 *  The Logical Operator Enum for query
	 */
	private String logicalOperator;
	
	/**
	 * The list of associated conditions
	 */
	private List<DatabaseQueryCondition> databaseQueryConditions = new ArrayList<>(0);
	
	/**
	 * The list of associated actions
	 */
	private List<DatabaseQueryAction> databaseQueryActions = new ArrayList<>(0);
	
	private List<PolicyAction> policyActions = new ArrayList<>(0);
	
	private List<PolicyCondition> policyConditions = new ArrayList<>(0);

	private String associationStatus = BaseConstants.NONASSOCIATED;
	/**
	 * represent the auto generated primary key
	 * @return Id 
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DatabaseQuery",allocationSize=1)
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
	 * Represents the alias of database query
	 * @return alias
	 */
	@XmlElement
    @Column(name = "ALIAS", nullable = false, length = 255, unique = false)
	public String getAlias() {
		return alias;
	}
	
	/**
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	
	/**
	 * Represents the name of the query
	 * @return query Name
	 */
	@XmlElement
    @Column(name = "QUERYNAME", nullable = false, length = 255, unique = false)
	public String getQueryName() {
		return queryName;
	}
	
	/**
	 * @param queryName
	 */
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	/**
	 * Represents the query
	 * @return queryValue
	 */
	@XmlElement
    @Column(name = "QUERYVALUE", nullable = false, length = 2000, unique = false)
	public String getQueryValue() {
		return queryValue;
	}
	
	/**
	 * @param queryValue
	 */
	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}
	
	/**
	 * Represents the description of the query
	 * @return description
	 */
	@XmlElement
    @Column(name = "DESCRIPTION", nullable = true, length = 2000, unique = false)
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Represents the output field of the DB
	 * @return Output DB field 
	 */
	@XmlElement
    @Column(name = "OUTPUTDBFIELD", nullable = true, length = 200, unique = false)
	public String getOutputDbField() {
		return outputDbField;
	}
	
	/**
	 * @param outputDbField
	 */
	public void setOutputDbField(String outputDbField) {
		this.outputDbField = outputDbField;
	}
	
	
	/**
	 * Represents whether the returnMultipleRows is enabled or not 
	 * @return returnMultipleRowsEnable
	 */
	@Column(name = "RETURNMULTIPLEROWS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isReturnMultipleRowsEnable() {
		return returnMultipleRowsEnable;
	}

	/**
	 * @param returnMultipleRowsEnable
	 */
	public void setReturnMultipleRowsEnable(boolean returnMultipleRowsEnable) {
		this.returnMultipleRowsEnable = returnMultipleRowsEnable;
	}


	/**
	 * Represents whether the cache is enabled or not 
	 * @return cacheEnable
	 */
	@Column(name = "ISCACHEENABLED", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getCacheEnable() {
		return cacheEnable;
	}
	
	/**
	 * @param cacheEnable
	 */
	public void setCacheEnable(boolean cacheEnable) {
		this.cacheEnable = cacheEnable;
	}
	
	/**
	 * Represents whether the condition Expression is enabled
	 * @return conditionExpressionEnable
	 */
	@Column(name = "ISCONDITIONEXPRESSIONENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean getConditionExpressionEnable() {
		return conditionExpressionEnable;
	}
	
	/**
	 * @param conditionExpressionEnable
	 */
	public void setConditionExpressionEnable(boolean conditionExpressionEnable) {
		this.conditionExpressionEnable = conditionExpressionEnable;
	}
	
	/**
	 * Represents the condition expression
	 * @return conditionExpression
	 */
	@XmlElement
    @Column(name = "CONDITIONEXPRESSION", nullable = true, length = 2000, unique = false)
	public String getConditionExpression() {
		return conditionExpression;
	}
	
	/**
	 * @param conditionExpression
	 */
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	/**
	 * Represents the logical operator
	 * @return logical operator
	 */
	@XmlElement
    @Column(name = "LOGICALOPERATOR", nullable = true, length = 200, unique = false)
	public String getLogicalOperator() {
		return logicalOperator;
	}
	
	/**
	 * @param logicalOperator
	 */
	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	
	/**
	 * Represents the server Id to which this query is associated
	 * @return server Instance Id
	 */	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_QUERY_SERVERINSTANCE"))
	@XmlTransient
	@DiffIgnore
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	/**
	 * @param serverInstance
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	/**
	 * Represents the databaseQueryConditions
	 * @return databaseQueryConditions
	 */
	@XmlElement
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "DATABASEQUERYID", nullable = true,foreignKey = @ForeignKey(name = "FK_QUERY_ID_QUERYCONDITION"))
	public List<DatabaseQueryCondition> getDatabaseQueryConditions() {
		return databaseQueryConditions;
	}
	
	/**
	 * @param databaseQueryConditions
	 */
	public void setDatabaseQueryConditions(List<DatabaseQueryCondition> databaseQueryConditions) {
		this.databaseQueryConditions = databaseQueryConditions;
	}
	
	/**
	 * Represents the actions to be performed on database queries
	 * @return databaseQueryActions
	 */
	@XmlElement
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "DATABASEQUERYID", nullable = true,foreignKey = @ForeignKey(name = "FK_QUERY_ID_QUERYACTION"))
	public List<DatabaseQueryAction> getDatabaseQueryActions() {
		return databaseQueryActions;
	}
	
	/**
	 * @param databaseQueryActions
	 */
	public void setDatabaseQueryActions(List<DatabaseQueryAction> databaseQueryActions) {
		this.databaseQueryActions = databaseQueryActions;
	}
	
	@DiffIgnore
	@Column(name = "ASSOCIATIONSTATUS", nullable = true, unique = false, length=200)
	public String getAssociationStatus() {
		return associationStatus;
	}

	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}
	
	@XmlTransient
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "DATABASEQUERYID", nullable = true,foreignKey = @ForeignKey(name = "FK_ID_PACTION"))
	public List<PolicyAction> getPolicyActions() {
		return policyActions;
	}

	public void setPolicyActions(List<PolicyAction> policyActions) {
		this.policyActions = policyActions;
	}
	
	@XmlTransient
	@OneToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "DATABASEQUERYID", nullable = true,foreignKey = @ForeignKey(name = "FK_ID_PCONDITION"))
	public List<PolicyCondition> getPolicyConditions() {
		return policyConditions;
	}

	public void setPolicyConditions(List<PolicyCondition> policyConditions) {
		this.policyConditions = policyConditions;
	}
	
}
