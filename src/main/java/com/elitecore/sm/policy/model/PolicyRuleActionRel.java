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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author chetan.vanapariya
 *
 */
@Entity
@Table(name = "TBLTPOLICYRULEACTIONREL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlRootElement
@XmlType(propOrder = {"id", "applicationOrder" })
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyRuleActionRel implements Serializable, Comparable<PolicyRuleActionRel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**  The Policy Id. */
	private int id;
	
	/**  The Policy Rule. */
    private PolicyRule policyRuleAction;
    
    /**  The Policy group. */
    private PolicyAction action;
    
    /**  The Application Order. */
    private int applicationOrder;

    /**
     * Default Constructor.
     */
    public PolicyRuleActionRel() {
    	// Default Constructor
    }
    
    @Id
    @XmlElement
    @Column
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="PolicyRuleActionRel",allocationSize=1)
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
    @JoinColumn(name = "RULEID", referencedColumnName="id", foreignKey=@ForeignKey(name = "TBLTPOLICYRULEACTION_FK") )
	public PolicyRule getPolicyRuleAction() {
		return policyRuleAction;
	}

	/**
	 * Sets the policy rule.
	 *
	 * @param policyRule the new policy rule
	 */
	public void setPolicyRuleAction(PolicyRule policyRuleAction) {
		this.policyRuleAction = policyRuleAction;
	}

	
	@ManyToOne
	@JoinColumn(name = "ACTIONID" ,  referencedColumnName="id", foreignKey=@ForeignKey(name = "TBLTPOLICYACTION_FK"))
	@XmlTransient
	public PolicyAction getAction() {
		return action;
	}

	public void setAction(PolicyAction action) {
		this.action = action;
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
	public int compareTo(PolicyRuleActionRel  o) {
		
		return this.applicationOrder < o.applicationOrder ? -1 : this.applicationOrder > o.applicationOrder ? 1 : 0;
	}
	
	@Override
	public int hashCode() {
		final int prime = 13;
		int result = 1;
		result += prime * result + applicationOrder;
		result += prime * result + (( policyRuleAction == null) ? 0 : policyRuleAction.hashCode());
		result += prime * result + id;
		result += prime * result + (( action == null) ? 0 : action.hashCode() );
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
		PolicyRuleActionRel other = (PolicyRuleActionRel) obj;
		if (applicationOrder != other.applicationOrder)
			return false;
		if (policyRuleAction == null) {
			if (other.policyRuleAction != null)
				return false;
		} else if (!policyRuleAction.equals(other.policyRuleAction)) {
			return false;
		}
		if (id != other.id)
			return false;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals( other.action )) {
			return false;
		}
		return true;
	}
}