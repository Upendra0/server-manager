package com.elitecore.sm.policy.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * The Policy / Group Relation Class.
 *
 * @author chintan.patel
 */
@Entity
@Table(name = "TBLTPOLICYGROUPRULEREL")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlRootElement
@XmlType(propOrder = {"id", "applicationOrder", "policyRule"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyGroupRuleRel extends BaseModel implements Serializable, Comparable<PolicyGroupRuleRel> {

/** The Constant serialVersionUID. */
private static final long serialVersionUID = 1L;
	
	/**  The Policy Id. */
	private int id;
	
	/**  The Policy Rule. */
    private PolicyRule policyRule;
    
    /**  The Policy group. */
    private PolicyGroup group;
    
    /**  The Application Order. */
    private int applicationOrder;

    /**
     * Default Constructor for Hibernate.
     */
    public PolicyGroupRuleRel() {
    	// Default Constructor
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
								 pkColumnValue="PolicyGroupRuleRel",allocationSize=1)
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
    @JoinColumn(name = "RULEID")
	public PolicyRule getPolicyRule() {
		return policyRule;
	}

	/**
	 * Sets the policy rule.
	 *
	 * @param policyRule the new policy rule
	 */
	public void setPolicyRule(PolicyRule policyRule) {
		this.policyRule = policyRule;
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	@ManyToOne
	@JoinColumn(name = "GROUPID")
	@XmlTransient
	public PolicyGroup getGroup() {
		return group;
	}

	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(PolicyGroup group) {
		this.group = group;
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
	public int compareTo(PolicyGroupRuleRel o) {
		
		return this.applicationOrder < o.applicationOrder ? -1 : this.applicationOrder > o.applicationOrder ? 1 : 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + applicationOrder;
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + id;
		result = prime * result + ((policyRule == null) ? 0 : policyRule.hashCode());
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
		PolicyGroupRuleRel other = (PolicyGroupRuleRel) obj;
		if (applicationOrder != other.applicationOrder)
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group)) {
			return false;
		}
		if (id != other.id)
			return false;
		if (policyRule == null) {
			if (other.policyRule != null)
				return false;
		} else if (!policyRule.equals(other.policyRule)) {
			return false;
		}
		return true;
	}
}
