package com.elitecore.sm.policy.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author chetan.vanapariya
 *
 */
@Entity
@Table(name = "TBLTPOLICYRULECONDITIONREL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlRootElement
@XmlType(propOrder = {"id", "applicationOrder" ,"condition"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyRuleConditionRel implements Serializable, Comparable<PolicyRuleConditionRel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**  The Policy Id. */
	private int id;
	
	/**  The Policy Rule. */
    private PolicyRule policyRuleCon;
    
    /**  The Policy group. */
    private PolicyCondition condition;
    
    /**  The Application Order. */
    private int applicationOrder;

    /**
     * Default Constructor for Hibernate.
     */
    public PolicyRuleConditionRel() {
    	// Default Constructor
    }
    
    @Id
    @XmlElement
    @Column
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="PolicyRuleConditionRel",allocationSize=1)
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
     * Gets the policy rule.
     *
     * @return the policy rule
     */
    @ManyToOne
    @JoinColumn(name = "RULEID", referencedColumnName="id", foreignKey=@ForeignKey(name = "TBLTPOLICYRULECONDITION_FK") )
	public PolicyRule getPolicyRuleCon() {
		return policyRuleCon;
	}

	/**
	 * Sets the policy rule.
	 *
	 * @param policyRule the new policy rule
	 */
	public void setPolicyRuleCon(PolicyRule policyRuleCon) {
		this.policyRuleCon = policyRuleCon;
	}

	
	@ManyToOne
	@JoinColumn(name = "CONDITIONID" ,  referencedColumnName="id", foreignKey=@ForeignKey(name = "TBLTPOLICYCONDITION_FK"))
	@XmlTransient
	public PolicyCondition getCondition() {
		return condition;
	}

	public void setCondition(PolicyCondition condition) {
		this.condition = condition;
	}

	
	

	/**
	 * Gets the application order.
	 *
	 * @return the application order
	 */
	@XmlElement
	@Column(name = "APPLICATIONORDER", nullable = false)
	public int getApplicationOrder() {
		return applicationOrder;
	}
	

	/**
	 * Sets the application order.
	 *
	 * @param applicationOrder the new application order
	 */
	public void setApplicationOrder(int applicationOrder) {
		this.applicationOrder = applicationOrder;
	}

	@Override
	public int compareTo(PolicyRuleConditionRel o) {
		
		return this.applicationOrder < o.applicationOrder ? -1 : this.applicationOrder > o.applicationOrder ? 1 : 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		result = prime * result + applicationOrder;
		result = prime * result + (( policyRuleCon == null) ? 0 : policyRuleCon.hashCode());
		result = prime * result + id;
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolicyRuleConditionRel other = (PolicyRuleConditionRel) obj;
		if (applicationOrder != other.applicationOrder)
			return false;
		if (policyRuleCon == null) {
			if (other.policyRuleCon != null)
				return false;
		} else if (!policyRuleCon.equals(other.policyRuleCon)) {
			return false;
		}
		if (id != other.id)
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition)) {
			return false;
		}
		return true;
	}
}
