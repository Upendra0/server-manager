package com.elitecore.sm.policy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Policy Condition class.
 *
 * @author chintan.patel
 */

@Component(value = "policyCondition")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLTPOLICYCONDITION")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder = {"id","alias", "name", "unifiedField", "description", "operator", "value", "conditionExpression", "type", "databaseQueryAlias", "conditionExpressionForSync"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyCondition extends BaseModel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**  The Policy Id. */
	private int id;

	/**  The Net Server Id. */
	private ServerInstance server;

	/**  The Policy Alias. */
	private String alias;

	/**  The Policy Name. */
	private String name;

	/**  The Policy Description. */
	private String description;

	/**  The Unified Field. */
	private String unifiedField;
	
	/**  The Condition Operator. */
	private String operator;
	
	/**  The Condition Value. */
	private String value;
	
	/**  The Condition Expression. for Engine Side sync*/
	private String conditionExpression;
	
	/** The Condition Expression for SM side view **/
	private String conditionExpressionForSync;
	
	/**  The Condition Type. */
	private String type;
	
	/**  The Policy Rule Set. */
	/** @Transient
	private Set<PolicyRule> policyRuleSet;*/
	private List<PolicyRuleConditionRel> policyRuleConditionRel = new ArrayList<>();
	
	private DatabaseQuery databaseQuery;
	
	private String databaseQueryAlias;

	/**
	 * Default Constructor for Hibernate.
	 */
	public PolicyCondition() {
		//Default constructor
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@XmlElement
	@Column
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	pkColumnValue="PolicyCondition",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the server.
	 *
	 * @return the server
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_POLICYCONDITION_INSTANCE"))
	@XmlTransient
	public ServerInstance getServer() {
		return server;
	}

	/**
	 * Sets the server.
	 *
	 * @param server the new server
	 */
	public void setServer(ServerInstance server) {
		this.server = server;
	}

	/**
	 * Gets the alias.
	 *
	 * @return the alias
	 */
	@XmlElement
	@Column(name = "ALIAS", nullable = false, length = 255, unique = false)
	public String getAlias() {
		return alias;
	}

	/**
	 * Sets the alias.
	 *
	 * @param alias the new alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@XmlElement
	@Column(name = "NAME", nullable = false, length = 255, unique = false)
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true, length = 4000)
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the unified field.
	 *
	 * @return the unified field
	 */
	@XmlElement
	@Column(name = "UNIFIEDFIELD", nullable = true, length = 50)
	public String getUnifiedField() {
		return unifiedField;
	}

	/**
	 * Sets the unified field.
	 *
	 * @param unifiedField the new unified field
	 */
	public void setUnifiedField(String unifiedField) {
		this.unifiedField = unifiedField;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	@XmlElement
	@Column(name = "OPERATOR", nullable = true, length = 50)
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@XmlElement
	@Column(name = "VALUE", nullable = true, length = 500)
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	@XmlElement
	@Column(name = "TYPE", nullable = false, length = 20)
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the policy rule set.
	 *
	 * @return the policy rule set
	 */
	

	/**
	 * Sets the policy rule set.
	 *
	 * @param policyRuleSet the new policy rule set
	 */
	
	
	/** policy rule-condition relation 
	 * 
	 *
	 * **/
	@OneToMany(cascade = CascadeType.ALL,  mappedBy = "condition")
	@DiffIgnore
	@XmlTransient
	public List<PolicyRuleConditionRel> getPolicyRuleConditionRel() {
		/** if(policyRuleConditionRel != null) {
			Collections.sort(policyRuleConditionRel);
		}*/
		return policyRuleConditionRel;
	}

	public void setPolicyRuleConditionRel( List<PolicyRuleConditionRel> policyRuleConditionRel) {
		this.policyRuleConditionRel = policyRuleConditionRel;
	}
	
	/**
	 * @return the conditionExpression
	 */
	@XmlElement
	@Column(name = "conditionexpression", nullable = true, length = 1000)
	public String getConditionExpression() {
		return conditionExpression;
	}

	
	/**
	 * @param conditionExpression the conditionExpression to set
	 */
	public void setConditionExpression(String conditionExpression) {
		this.conditionExpression = conditionExpression;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "DATABASEQUERYID", nullable = true, foreignKey = @ForeignKey(name = "FK_QUERY_PCONDITION"))
	@DiffIgnore
	public DatabaseQuery getDatabaseQuery() {
		return databaseQuery;
	}
	
	public void setDatabaseQuery(DatabaseQuery databaseQuery) {
		this.databaseQuery = databaseQuery;
	}
	
	@XmlElement
	@Column(name = "DATABASEQUERYALIAS", nullable = true, length = 1000)
	public String getDatabaseQueryAlias() {
		return databaseQueryAlias;
	}

	public void setDatabaseQueryAlias(String databaseQueryAlias) {
		this.databaseQueryAlias = databaseQueryAlias;
	}

	@XmlElement
	@Column(name = "CONDITIONEXPRESSIONFORSYNC", nullable = true, length = 1000)
	public String getConditionExpressionForSync() {
		return conditionExpressionForSync;
	}

	public void setConditionExpressionForSync(String conditionExpressionForSync) {
		this.conditionExpressionForSync = conditionExpressionForSync;
	}
	
}