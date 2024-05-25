package com.elitecore.sm.policy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.javers.core.metamodel.annotation.DiffIgnore;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Policy Group Model/Entity Class.
 *
 * @author chintan.patel
 */

@Entity
@Table(name = "TBLTPOLICYRULE")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder = {"id", "alias", "name", "operator", "globalSequenceRuleId", "description", "alert", "alertDescription", 
		"policyConditions", "policyActions", "policyConditionStr", "policyActionStr","category","severity","errorCode"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyRule extends BaseModel implements Serializable {

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
    
    /**  The SNMP Alert. */
    private SNMPAlert alert;
    
    /**  The SNMP Alert Description. */
    private String alertDescription;
    
    /**  The Global Sequence Rule Id. */
    private String globalSequenceRuleId;
    
    /**  The Policy Group Rule Relation. */
    private List<PolicyGroupRuleRel> policyGroupRuleRelSet = new ArrayList<>();
    
    /**  The Policy Condition Set. */
    private List<PolicyRuleConditionRel> policyRuleConditionRel = new ArrayList<>();
    
    /**  The Policy Action Set. */    
    private List<PolicyRuleActionRel> policyRuleActionRel = new ArrayList<>();
    
    
    /**  The Logical Operator between conditions. */
    private String operator;
    
    /**  The Association Status. */
    private String associationStatus;
    
    /** The Policy Condition alias names */
    private List<String> policyConditions = new ArrayList<>();
    
    /** The Policy Action alias names */
    private List<String> policyActions = new ArrayList<>();
    
    /** The Policy Condition String */
    private String policyConditionStr;
    
    /** The Policy Action String */
    private String policyActionStr;
    
    /** The Policy Rule Category String */
    private String category = "";
    
    /** The Policy Rule Severity String */
    private String severity = "";
    
    /** The Policy Rule Error Code String */
    private String errorCode = "";
    
    /**
     * Default Constructor for Hibernate.
     */
    public PolicyRule() {
    	// Default Constructor for Hibernate
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
								 pkColumnValue="PolicyRule",allocationSize=1)
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
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_POLICYRULE_INSTANCE"))
	@DiffIgnore
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the alert.
	 *
	 * @return the alert
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ALERT", nullable = true, foreignKey = @ForeignKey(name = "FK_POLICYRULE_ALERT"))
	public SNMPAlert getAlert() {
		return alert;
	}

	/**
	 * Sets the alert.
	 *
	 * @param alert the new alert
	 */
	public void setAlert(SNMPAlert alert) {
		this.alert = alert;
	}

	/**
	 * Gets the alert description.
	 *
	 * @return the alert description
	 */
	@XmlElement
	@Column(name = "ALERTDESCRIPTION", nullable = true, length = 4000)
	public String getAlertDescription() {
		return alertDescription;
	}

	/**
	 * Sets the alert description.
	 *
	 * @param alertDescription the new alert description
	 */
	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	/**
	 * Gets the global sequence rule id.
	 *
	 * @return the global sequence rule id
	 */
	@XmlElement
	@Column(name = "GLOBALSEQUENCERULE", nullable = true, length = 100)
	public String getGlobalSequenceRuleId() {
		return globalSequenceRuleId;
	}

	/**
	 * Sets the global sequence rule id.
	 *
	 * @param globalSequenceRuleId the new global sequence rule id
	 */
	public void setGlobalSequenceRuleId(String globalSequenceRuleId) {
		this.globalSequenceRuleId = globalSequenceRuleId;
	}

	/**
	 * Gets the policy group rule rel set.
	 *
	 * @return the policy group rule rel set
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "policyRule")
	@DiffIgnore
	@XmlTransient
	public List<PolicyGroupRuleRel> getPolicyGroupRuleRelSet() {
		return policyGroupRuleRelSet;
	}

	/**
	 * Sets the policy group rule rel set.
	 *
	 * @param policyGroupRuleRelSet the new policy group rule rel set
	 */
	public void setPolicyGroupRuleRelSet(List<PolicyGroupRuleRel> policyGroupRuleRelSet) {
		this.policyGroupRuleRelSet = policyGroupRuleRelSet;
	}

	/**
	 * Gets the policy condition set.
	 *
	 * @return the policy condition set
	 */
	
 	
	
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "policyRuleCon" ,  orphanRemoval = true , fetch = FetchType.LAZY)	
	@Fetch (org.hibernate.annotations.FetchMode.SELECT)
	@DiffIgnore
	@XmlTransient
	public List<PolicyRuleConditionRel> getPolicyRuleConditionRel() {
		if(policyRuleConditionRel != null) {
			Collections.sort(policyRuleConditionRel);
		}
		return policyRuleConditionRel;
	}

	public void setPolicyRuleConditionRel( List<PolicyRuleConditionRel> policyRuleConditionRel) {
		this.policyRuleConditionRel = policyRuleConditionRel;
		if( this.policyRuleConditionRel != null) {
			Collections.sort( this.policyRuleConditionRel );
		}
		List <String> policyCondition = new ArrayList<>();
		if(policyRuleConditionRel != null && !policyRuleConditionRel.isEmpty()){
			for(PolicyRuleConditionRel conditionRule : policyRuleConditionRel){
				
				if(conditionRule != null && conditionRule.getCondition()!=null){
					policyCondition.add(conditionRule.getCondition().getName());
				}
			}
		}		
		this.setPolicyConditions(policyCondition);
	}

	/**
	 * Sets the policy condition set.
	 *
	 * @param policyConditionSet the new policy condition set
	 */
	

	/**
	 * Gets the policy action set.
	 *
	 * @return the policy action set
	 */
	
	

	/**
	 * Sets the policy action set.
	 *
	 * @param policyActionSet the new policy action set
	 */
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "policyRuleAction" ,  orphanRemoval = true , fetch = FetchType.LAZY)	
	@Fetch (org.hibernate.annotations.FetchMode.SELECT)
	@DiffIgnore
	@XmlTransient
	public List<PolicyRuleActionRel> getPolicyRuleActionRel() {
		return policyRuleActionRel;
	}

	public void setPolicyRuleActionRel(List<PolicyRuleActionRel> policyRuleActionRel) {
		this.policyRuleActionRel = policyRuleActionRel;
		
		if( this.policyRuleActionRel != null) {
			Collections.sort( this.policyRuleActionRel );
		}
		List <String> policyAction = new ArrayList<>();
		if(policyRuleActionRel != null && !policyRuleActionRel.isEmpty()){
			for(PolicyRuleActionRel actionRule : policyRuleActionRel){
				
				if( actionRule != null && actionRule.getAction()!=null){
					policyAction.add(actionRule.getAction().getName());
				}
			}
		}
		this.setPolicyActions(policyAction);
	}
	
	
	
	

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	@XmlElement
	@Column(name = "OPERATOR", nullable = true, length = 10)
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
	 * Gets the association status.
	 *
	 * @return the association status
	 */
	@Transient
	@XmlTransient
	@DiffIgnore
	public String getAssociationStatus() {
		return associationStatus;
	}

	/**
	 * Sets the association status.
	 *
	 * @param associationStatus the new association status
	 */
	public void setAssociationStatus(String associationStatus) {
		this.associationStatus = associationStatus;
	}

	/**
	 * Gets Policy Actions Aliases
	 * @return List of Policy Actions Aliases
	 */
	@XmlElementWrapper(name="policyActions")
	@XmlElement(name="policyAction")
	@Transient
	public List<String> getPolicyActions() {
		return policyActions;
	}

	/**
	 * Sets Policy Actions Aliases
	 * @param policyActions Policy Actions Aliases
	 */
	public void setPolicyActions(List<String> policyActions) {
		this.policyActions = policyActions;
	}

	/**
	 * Gets Policy Conditions Aliases
	 * @return List of Policy Conditions Aliases
	 */
	@XmlElementWrapper(name="policyConditions")
	@XmlElement(name="policyCondition")
	@Transient
	public List<String> getPolicyConditions() {
		return policyConditions;
	}

	/**
	 * Sets Policy Conditions Aliases
	 * @param policyActions Policy Conditions Aliases
	 */
	public void setPolicyConditions(List<String> policyConditions) {
		this.policyConditions = policyConditions;
	}

	/**
	 * Gets Policy conditions list string
	 * @return Policy conditions list string
	 */
	@XmlElement(name="policyConditionStr")
	@Transient
	public String getPolicyConditionStr() {
		policyConditionStr = EliteUtils.getCSVString(getPolicyConditions());
		return policyConditionStr;
	}

	/**
	 * Sets Policy conditions list string
	 * @param policyConditionStr Policy conditions list string
	 */
	public void setPolicyConditionStr(String policyConditionStr) {
		this.policyConditionStr = policyConditionStr;
	}

	/**
	 * Gets Policy actions list string
	 * @return Policy actions list string
	 */
	@XmlElement(name="policyActionStr")
	@Transient
	public String getPolicyActionStr() {
		policyActionStr = EliteUtils.getCSVString(getPolicyActions());
		return policyActionStr;
	}

	/**
	 * Sets Policy actions list string
	 * @param policyActionStr Policy actions list string
	 */
	public void setPolicyActionStr(String policyActionStr) {
		this.policyActionStr = policyActionStr;
	}

	@XmlElement
	@Column(name = "category", nullable = true, length = 250)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@XmlElement
	@Column(name = "severity", nullable = true, length = 100)
	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@XmlElement
	@Column(name = "errorcode", nullable = true, length = 250)
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
}
