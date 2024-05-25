package com.elitecore.sm.policy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Policy Group Model/Entity Class.
 *
 * @author chintan.patel
 */

@Entity
@Table(name = "TBLTPOLICYGROUP")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder = {"id", "alias", "name", "description", "policyRules", "policyRuleStr"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PolicyGroup extends BaseModel implements Serializable {

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
    
    /**  The Policy Group Relation. */
    private List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
    
    /**  The Policy Group Rule Relation. */
    private List<PolicyGroupRuleRel> policyGroupRuleRelSet = new ArrayList<>();
    
    /**  The Association Status. */
    private String associationStatus;
    
    /** The Policy Rules Aliases */
    private List<String> policyRules = new ArrayList<>();
    
    /** The Policy Rule List String */
    private String policyRuleStr;
    
    /**
     * Default Constructor for Hibernate.
     */
    public PolicyGroup() {
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
								 pkColumnValue="PolicyGroup",allocationSize=1)
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
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_POLICY_INSTANCE"))
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
	 * Gets the policy group relation set.
	 *
	 * @return the policy group relation set
	 */
	@XmlTransient
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
	@DiffIgnore
	public List<PolicyGroupRel> getPolicyGroupRelSet() {
		return policyGroupRelSet;
	}

	/**
	 * Sets the policy group relation set.
	 *
	 * @param policyGroupRelSet the new policy group relation set
	 */
	public void setPolicyGroupRelSet(List<PolicyGroupRel> policyGroupRelSet) {
		this.policyGroupRelSet = policyGroupRelSet;
	}

	/**
	 * Gets the policy group rule relation set.
	 *
	 * @return the policy group rule relation set
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "group", fetch = FetchType.LAZY)
	@Fetch (org.hibernate.annotations.FetchMode.SELECT)
	@DiffIgnore
	@XmlTransient
	public List<PolicyGroupRuleRel> getPolicyGroupRuleRelSet() {
		if(policyGroupRuleRelSet != null) {
			Collections.sort(policyGroupRuleRelSet);
		}		
		return policyGroupRuleRelSet;
	}

	/**
	 * Sets the policy group rule relation set.
	 *
	 * @param policyGroupRuleRelSet the new policy group rule relation set
	 */
	public void setPolicyGroupRuleRelSet(List<PolicyGroupRuleRel> policyGroupRuleRelSet) {
		this.policyGroupRuleRelSet = policyGroupRuleRelSet;
		if( this.policyGroupRuleRelSet != null) {
			Collections.sort( this.policyGroupRuleRelSet );
		}
		List <String> policyRules = new ArrayList<>();
		if(policyGroupRuleRelSet != null && !policyGroupRuleRelSet.isEmpty()){
			for(PolicyGroupRuleRel groupRuleRel : policyGroupRuleRelSet){
				if(groupRuleRel.getPolicyRule() != null){
					policyRules.add(groupRuleRel.getPolicyRule().getAlias());
				}
			}
		}
		this.setPolicyRules(policyRules);
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
	 * Gets Policy Rule Aliases
	 * @return Policy Rule Aliases
	 */
	@XmlElementWrapper(name="policyRules")
	@XmlElement(name="policyRule")
	@Transient
	public List<String> getPolicyRules() {
		return policyRules;
	}

	/**
	 * Sets Policy Rule Aliases
	 * @param policyRules Policy Rule Aliases
	 */
	public void setPolicyRules(List<String> policyRules) {
		this.policyRules = policyRules;
	}

	/**
	 * Gets Policy Rule List String
	 * @return Policy Rule List String
	 */
	@XmlElement(name="policyRuleStr")
	@Transient
	public String getPolicyRuleStr() {
		this.policyRuleStr = EliteUtils.getCSVString(getPolicyRules());
		return this.policyRuleStr;		
	}
	
	//commented below code because it won't work in sync
	/*@XmlElement(name="policyRuleStr")
    @Transient
    public String getPolicyRuleStr() {
        this.policyRuleStr = EliteUtils.getCSVString(getPolicyGroupRuleRelSet().stream()
                   .map(object -> Objects.toString(object.getPolicyRule().getAlias(), null))
                   .collect(Collectors.toList()));
        return this.policyRuleStr;
    }*/

	/**
	 * Sets Policy Rule List String
	 * @param policyRuleStr Policy Rule List String
	 */
	public void setPolicyRuleStr(String policyRuleStr) {
		this.policyRuleStr = policyRuleStr;
	}
	
	
}