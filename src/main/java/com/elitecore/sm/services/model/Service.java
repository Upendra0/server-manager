/**
 * 
 */
package com.elitecore.sm.services.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * The Class Service.
 *
 * @author vandana.awatramani
 */
@Entity()
@Table(name = "TBLTSERVICE")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.JOINED)
@XmlSeeAlso({CollectionService.class,ParsingService.class,DistributionService.class,ProcessingService.class,AggregationService.class,CorrelationService.class,
							DataConsolidationService.class,IPLogParsingService.class,GTPPrimeCollectionService.class,AggregationService.class})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serviceCache")
@XmlType(propOrder = { "id","servInstanceId","name", "description","syncStatus","enableFileStats","enableDBStats","fileCDRSummaryFlag","svctype",
					"svcExecParams","myDrivers","svcPathList"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=Service.class)
public  class Service extends BaseModel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2006070353240547084L;

	/** The svctype. */
	private ServiceType svctype;

	/** The id. */
	// Added by Jay Shah
	private int id;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The server instance. */
	private ServerInstance serverInstance; 

	/** The sync status. */
	private boolean syncStatus;
	
	/** The svc exec params. */
	private ServiceExecutionParams svcExecParams;
	
	/** The enable file stats. */
	private boolean enableFileStats;
	
	/** The enable DB stats. */
	private boolean enableDBStats;
	
	/** The service instance id. */
	private String servInstanceId; 

	/** The my drivers. */
	// this till be optional for parsing, processing etc.
	private List<Drivers> myDrivers = new ArrayList<>(0);
	
	/** The svc path list. */
	private List<PathList> svcPathList = new ArrayList<>(0);

	
	@SuppressWarnings("unused")
	private boolean fileCDRSummaryFlag;
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="Service",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false,unique=false, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}


	/**
	 * Gets the server instance.
	 *
	 * @return the serverInstance
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVERINSTANCEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICE_SRV_INSTANCE"))
	@XmlTransient
	@DiffIgnore
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 2000)
	@XmlElement(nillable=true)
	public String getDescription() {
		return description;
	}

	/**
	 * Checks if is sync status.
	 *
	 * @return the syncStatus
	 */
	@Column(name = "SYNCSTATUS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isSyncStatus() {
		return syncStatus;
	}

	/**
	 * Gets the service instance id.
	 *
	 * @return the syncStatus
	 */
	@Column(name = "SERVICEINSTANCEID", nullable = false,unique=false, length = 3)
	@XmlElement
	public String getServInstanceId() {
		return servInstanceId;
	}

	/**
	 * Gets the svc exec params.
	 *
	 * @return the svcExecParams
	 */
	@Embedded
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@XmlElement
	public ServiceExecutionParams getSvcExecParams() {
		return svcExecParams;
	}

	/**
	 * Gets the my drivers.
	 *
	 * @return the myDrivers
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "service" ,cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@XmlElement
	
	@DiffIgnore
	public List<Drivers> getMyDrivers() {
		return myDrivers;
	}

	/**
	 * Gets the svctype.
	 *
	 * @return the svctype
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SVCYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SVC_SVCTYPE"))
	@DiffIgnore
	public ServiceType getSvctype() {
		return svctype;
	}

	/**
	 * Gets the svc path list.
	 *
	 * @return the svcPathList
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy="service" , cascade = CascadeType.ALL,fetch=FetchType.LAZY)
	@XmlElement
	@DiffIgnore
	public List<PathList> getSvcPathList() {
		return svcPathList;
	}
	
	
	

	/**
	 * Checks if is enable file stats.
	 *
	 * @return the enableFileStats
	 */
	@Column(name = "ENABLEFILESTATS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isEnableFileStats() {
		return enableFileStats;
	}

	/**
	 * Checks if is enable DB stats.
	 *
	 * @return the enableDBStats
	 */
	@Column(name = "ENABLEDBSTATS", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isEnableDBStats() {
		return enableDBStats;
	}


	/**
	 * Sets the id.
	 *
	 * @param id            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name.
	 *
	 * @param name            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the description.
	 *
	 * @param description            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the server instance.
	 *
	 * @param serverInstance            the serverInstance to set
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}


	/**
	 * Sets the sync status.
	 *
	 * @param syncStatus            the syncStatus to set
	 */
	public void setSyncStatus(boolean syncStatus) {
		this.syncStatus = syncStatus;
	}

	/**
	 * Sets the service instance id.
	 *
	 * @param serviceInstanceId the new service instance id
	 */
	public void setServInstanceId(String servInstanceId) {
		this.servInstanceId = servInstanceId;
	}

	/**
	 * Sets the svc exec params.
	 *
	 * @param svcExecParams            the svcExecParams to set
	 */
	public void setSvcExecParams(ServiceExecutionParams svcExecParams) {
		this.svcExecParams = svcExecParams;
	}

	/**
	 * Sets the my drivers.
	 *
	 * @param myDrivers            the myDrivers to set
	 */
	public void setMyDrivers(List<Drivers> myDrivers) {
		this.myDrivers = myDrivers;
	}

	/**
	 * Sets the svctype.
	 *
	 * @param svctype            the svctype to set
	 */
	public void setSvctype(ServiceType svctype) {
		this.svctype = svctype;
	}

	/**
	 * Sets the svc path list.
	 *
	 * @param svcPathList            the svcPathList to set
	 */
	public void setSvcPathList(List<PathList> svcPathList) {
		this.svcPathList = svcPathList;
	}

	/**
	 * Sets the enable file stats.
	 *
	 * @param enableFileStats the enableFileStats to set
	 */
	public void setEnableFileStats(boolean enableFileStats) {
		this.enableFileStats = enableFileStats;
	}

	/**
	 * Sets the enable DB stats.
	 *
	 * @param enableDBStats the enableDBStats to set
	 */
	public void setEnableDBStats(boolean enableDBStats) {
		this.enableDBStats = enableDBStats;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.common.model.BaseModel#toString()
	 */
	@Override
	public String toString(){
		return super.toString();
	}

	
	/**
	 * @return the fileCDRSummaryFlag
	 */
	@XmlElement
	@Transient
	public boolean isFileCDRSummaryFlag() {
		return this.serverInstance.isFileCdrSummaryDBEnable();
	}

	
	/**
	 * @param fileCDRSummaryFlag the fileCDRSummaryFlag to set
	 */
	public void setFileCDRSummaryFlag(boolean fileCDRSummaryFlag) {
		this.fileCDRSummaryFlag = fileCDRSummaryFlag;
	}
}