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
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Policy Model/Entity Class.
 *
 * @author chintan.patel
 */

@Entity
@Table(name = "TBLTPOLICY")
@DynamicUpdate
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="policy")
@XmlType(propOrder = {"id", "alias", "name", "description", "policyGroups", "policyGroupStr"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Policy extends BaseModel implements Serializable {

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
    
    /**  The Policy Group Map. */
    private List<PolicyGroupRel> policyGroupRelSet = new ArrayList<>();
    
    /**  The Association Status. */
    private String associationStatus;
    
    private List<ProcessingPathList> processingPathList;
    
    /** The Policy Group Aliases */
    private List<String> policyGroups = new ArrayList<>();
    
    /** The Policy Group List String */
    private String policyGroupStr;
    
    /**
     * Default Constructor for Hibernate.
     */
    public Policy() {
    	// Default Constructor
    }
    
    
    /**
     * Instantiates a new policy.
     *
     * @param id the id
     * @param server the server
     * @param name the name
     * @param description the description
     */
    public Policy(int id, ServerInstance server, String name, String description) {
		super();
		this.id = id;
		this.server = server;
		this.name = name;
		this.alias = name;
		this.description = description;
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
								 pkColumnValue="Policy",allocationSize=1)
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
	@DiffIgnore
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
    @Column(name = "ALIAS", nullable = false, length = 255, unique = true)
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
	@Column(name = "NAME", nullable = false, length = 255, unique = true)
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
	 * Gets the policy group rel set.
	 *
	 * @return the policy group rel set
	 */
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "policy", fetch = FetchType.EAGER)
	@Fetch (org.hibernate.annotations.FetchMode.SELECT)
	@XmlTransient
	@DiffIgnore
	public List<PolicyGroupRel> getPolicyGroupRelSet() {
		if(policyGroupRelSet != null) {
			Collections.sort(policyGroupRelSet);
		}
		return policyGroupRelSet;
	}

	/**
	 * Sets the policy group rel set.
	 *
	 * @param policyGroupRelSet the new policy group rel set
	 */
	public void setPolicyGroupRelSet(List<PolicyGroupRel> policyGroupRelSet) {
		this.policyGroupRelSet = policyGroupRelSet;
		if(policyGroupRelSet!=null){
			Collections.sort(policyGroupRelSet);
		}
		List <String> policyGroup = new ArrayList<>();
		if(policyGroupRelSet != null && !policyGroupRelSet.isEmpty()){
			for(PolicyGroupRel groupRel : policyGroupRelSet){
				if(groupRel.getGroup() != null){
					policyGroup.add(groupRel.getGroup().getAlias());
				}
			}
		}
		this.setPolicyGroups(policyGroup);
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
	 * Gets Policy Group Aliases
	 * @return Policy Group Aliases
	 */
	@XmlElement(name="policyGroup")
	@Transient
	@XmlElementWrapper(name="policyGroups")
	public List<String> getPolicyGroups() {
		return policyGroups;
	}

	/**
	 * Sets Policy Group Aliases
	 * @param policyGroups Policy Group Aliases
	 */
	public void setPolicyGroups(List<String> policyGroups) {
		this.policyGroups = policyGroups;
	}

	/**
	 * Gets Processing Service Path List
	 * @return Processing Service Path List
	 */
	@OneToMany(mappedBy = "policy", fetch = FetchType.EAGER)
	@Fetch (org.hibernate.annotations.FetchMode.SELECT)
	@XmlTransient
	@DiffIgnore
	public List<ProcessingPathList> getProcessingPathList() {
		return processingPathList;
	}

	/**
	 * Sets Processing Service Path List
	 * @param processingPathList Processing Service Path List
	 */
	public void setProcessingPathList(List<ProcessingPathList> processingPathList) {
		this.processingPathList = processingPathList;
	}

	/**
	 * Gets Policy Group List String
	 * @return Policy Group List String
	 */
	@XmlElement(name="policyGroupStr")
	@Transient
	public String getPolicyGroupStr() {
		this.policyGroupStr = EliteUtils.getCSVString(getPolicyGroups());
		return policyGroupStr;
	}

	/**
	 * Sets Policy Group List String
	 * @param policyGroupStr Policy Group List String
	 */
	public void setPolicyGroupStr(String policyGroupStr) {
		this.policyGroupStr = policyGroupStr;
	}
	
	
}