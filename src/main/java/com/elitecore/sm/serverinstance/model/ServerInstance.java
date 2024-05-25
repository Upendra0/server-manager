/**
 * 
 */
package com.elitecore.sm.serverinstance.model;

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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani
 * 
 */
@Component(value = "serverInstanceObject")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTSERVERINSTANCE")
@DynamicUpdate	
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serverInstanceCache")
@XmlType(propOrder = { "id", "name", "description", "port",
		"fileStatInDBEnable", "fileCdrSummaryDBEnable", "minDiskSpace",
		"snmpAlertEnable", "syncSIStatus", "syncChildStatus",
		"webservicesEnable","restWebservicesEnable", "serverHome", "javaHome","mediationRoot","reprocessingBackupPath", "minMemoryAllocation",
		"maxMemoryAllocation", "fileStorageLocation",
		"maxConnectionRetry", "retryInterval", "connectionTimeout",
		"scriptName", "selfSNMPServerConfig", "snmpListeners",
		"thresholdSysAlertEnable", "thresholdTimeInterval", "thresholdMemory",
		"loadAverage","server","databaseInit", "serverManagerDatasourceConfig", "iploggerDatasourceConfig", "logsDetail","services","agentList","policyList",
		"policyGroupList", "policyRuleList", "policyConditionList", "policyActionList", "databaseQueryList"})

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ServerInstance.class)
public class ServerInstance extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	private int port;
	private Server server;

	private boolean fileStatInDBEnable = false;
	private boolean fileCdrSummaryDBEnable = false;
	private boolean webservicesEnable = false;
	private boolean restWebservicesEnable = false;

	private String minDiskSpace = "-1";
	private boolean snmpAlertEnable = false;
	private boolean syncSIStatus = false;
	private boolean syncChildStatus = true;
	private String serverHome;
	private String javaHome;
	private String mediationRoot="C:\\MEDIATION_ROOT";
	private String reprocessingBackupPath;

	private int minMemoryAllocation = 256;
	private int maxMemoryAllocation = 512;

	private List<Service> services = new ArrayList<>(0);
	private List<Agent> agentList = new ArrayList<>(0);
	private List<Policy> policyList = new ArrayList<>(0);
	private List<PolicyGroup> policyGroupList = new ArrayList<>(0);
	private List<PolicyRule> policyRuleList = new ArrayList<>(0);
	private List<PolicyCondition> policyConditionList = new ArrayList<>(0);
	private List<PolicyAction> policyActionList = new ArrayList<>(0);
	private List<DatabaseQuery> databaseQueryList = new ArrayList<>(0);
	private DataSourceConfig serverManagerDatasourceConfig;
	private DataSourceConfig iploggerDatasourceConfig;
	private boolean databaseInit = false;

	// for bi-directional one to one relationships
	private LogsDetail logsDetail;

	private String fileStorageLocation;

	private int maxConnectionRetry = BaseConstants.SERVER_MGMT_TRY_TO_CONNECT;
	private int retryInterval = BaseConstants.SERVER_CONNECTION_INTERVAL;
	private int connectionTimeout = BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT;
	private String scriptName;

	// adding snmp alert mappings
	private List<SNMPServerConfig> selfSNMPServerConfig= new ArrayList<>(0);
	private List<SNMPServerConfig> snmpListeners = new ArrayList<>(0);

	private boolean thresholdSysAlertEnable;
	private int thresholdTimeInterval=10;
	private int thresholdMemory=70;
	private int loadAverage=10;
	


	public ServerInstance() {
		// default public constructor needed for hibernate. explicit is used for testing
	}

	/**
	 * @param name
	 * @param description
	 * @param port
	 * @param server
	 * @param alias
	 * @param fileStatInDBEnable
	 * @param fileCdrSummaryDBEnable
	 * @param webservicesEnable
	 * @param restWebservicesEnable
	 * @param minDiskSpace
	 * @param snmpAlertEnable
	 * @param syncSIStatus
	 * @param syncChildStatus
	 * @param serverHome
	 * @param javaHome
	 * @param myDSconfig
	 * @param instanceLogsDetail
	 * @param status
	 * @param connDetails
	 */
	public ServerInstance(String name, int port,
			Server server,   String serverHome, String javaHome,
			DataSourceConfig serverManagerDatasourceConfig,DataSourceConfig iploggerDatasourceConfig, LogsDetail instanceLogsDetail	) {
		super();
		this.name = name;
		this.description = name;
		this.port = port;
		this.server = server;

		this.fileStatInDBEnable = false;
		this.fileCdrSummaryDBEnable = false;
		this.webservicesEnable = false;
		this.restWebservicesEnable = false;
		
		this.snmpAlertEnable = false;
		this.syncSIStatus = false;
		this.syncChildStatus = false;
		this.serverHome = serverHome;
		this.javaHome = javaHome;
		this.serverManagerDatasourceConfig=serverManagerDatasourceConfig;
		this.iploggerDatasourceConfig=iploggerDatasourceConfig;
		this.logsDetail = instanceLogsDetail;
		this.status = StateEnum.ACTIVE;
		
		
	}
		
	/**
	 * @return the serverInstanceId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ServerInstance",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the instancename
	 */
	@Column(name = "NAME", nullable = false, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @return the server
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinColumn(name = "SERVERID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER_SRV_INSTANCE"))
	@XmlElement
	@DiffIgnore
	public Server getServer() {
		return server;
	}
	
	/**
	 * 
	 * @return mediationRoot
	 */
	@Column(name = "MEDIATIONROOT", nullable = true, length = 200)
	@XmlElement
	public String getMediationRoot() {
		return mediationRoot;
	}

	/**
	 * 
	 * @return reprocessingBackupPath
	 */
	@Column(name = "REPROCESSIGNBACKUPPATH", nullable = true, length = 600)
	@XmlElement
	public String getReprocessingBackupPath() {
		return reprocessingBackupPath;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = true, length = 2000)
	@XmlElement(nillable=true)
	public String getDescription() {
		return description;
	}

	/**
	 * @return the port
	 */
	@Column(name = "PORT", nullable = false, length = 6)
	@XmlElement
	public int getPort() {
		return port;
	}

	/**
	 * @return the fileStatInDBEnable
	 */
	@Column(name = "FLIESTATENABLE", nullable = false, length = 10)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement(defaultValue="null")
	public boolean isFileStatInDBEnable() {
		return fileStatInDBEnable;
	}

	/**
	 * @return the minDiskSpace
	 */
	@Column(name = "MINDISKSPACE", nullable = true, length = 10)
	@XmlElement
	public String getMinDiskSpace() {
		return minDiskSpace;
	}

	/**
	 * @return the snmpAlertEnable
	 */
	@Column(name = "SNMPALERTENABLE", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isSnmpAlertEnable() {
		return snmpAlertEnable;
	}

	/**
	 * @return the syncStatus
	 */
	@Column(name = "SYNCSISTATUS", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSyncSIStatus() {
		return syncSIStatus;
	}

	/**
	 * 
	 * @return the syncChildStatus
	 */
	@Column(name = "SYNCCHILDSTATUS", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isSyncChildStatus() {
		return syncChildStatus;
	}

	/**
	 * @return the services
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "serverInstance",fetch=FetchType.LAZY)
	@XmlElement
	@DiffIgnore
	public List<Service> getServices() {
		return services;
	}

	/**
	 * @return the agentList
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "serverInstance",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<Agent> getAgentList() {
		return agentList;
	}
	
	/**
	 * Gets Policy List
	 * @return the policy list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "server",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<Policy> getPolicyList() {
		return policyList;
	}

	/**
	 * Sets Policy List
	 * @param policyList the Policy List to be set
	 */
	public void setPolicyList(List<Policy> policyList) {
		this.policyList = policyList;
	}

	/**
	 * Gets Policy Group List
	 * @return the policy group list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "server",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<PolicyGroup> getPolicyGroupList() {
		return policyGroupList;
	}

	/**
	 * Sets Policy Group List
	 * @param policyGroupList the policy group list to be set
	 */
	public void setPolicyGroupList(List<PolicyGroup> policyGroupList) {
		this.policyGroupList = policyGroupList;
	}

	/**
	 * Gets Policy rule list
	 * @return the policy rule list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "server",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<PolicyRule> getPolicyRuleList() {
		return policyRuleList;
	}

	/**
	 * Sets Policy rule list
	 * @param policyRuleList the policy rule list to set
	 */
	public void setPolicyRuleList(List<PolicyRule> policyRuleList) {
		this.policyRuleList = policyRuleList;
	}

	/**
	 * Gets Policy Condition List
	 * @return the policy condition list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "server",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<PolicyCondition> getPolicyConditionList() {
		return policyConditionList;
	}

	/**
	 * Sets Policy Condition List
	 * @param policyConditionList the policy condition list to be set
	 */
	public void setPolicyConditionList(List<PolicyCondition> policyConditionList) {
		this.policyConditionList = policyConditionList;
	}
	/**
	 * Gets Policy Action List
	 * @return the policy action list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "server",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<PolicyAction> getPolicyActionList() {
		return policyActionList;
	}

	/**
	 * Sets Policy Action List
	 * @param policyActionList the policy action list to be set
	 */
	public void setPolicyActionList(List<PolicyAction> policyActionList) {
		this.policyActionList = policyActionList;
	}
	
	/**
	 * Gets Database Query List
	 * @return the database query list
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "serverInstance",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<DatabaseQuery> getDatabaseQueryList(){
		return databaseQueryList;
	}
	
	/**
	 * Sets Database Query List
	 * @param the database query list
	 */
	public void setDatabaseQueryList(List<DatabaseQuery> databaseQueryList){
		this.databaseQueryList = databaseQueryList;
	}
	/**
	 * @return the instanceLogsDetail
	 */
	@XmlElement
	@Embedded
	public LogsDetail getLogsDetail() {
		return logsDetail;
	}


	@Column(name = "FILESTORAGELOCATION", nullable = true, length = 200)
	@XmlElement(nillable=true)
	public String getFileStorageLocation() {
		return fileStorageLocation;
	}

	/**
	 * @return the myDSconfig
	 *//*
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "DSID", nullable = true, foreignKey = @ForeignKey(name = "FK_SRV_INSTANCE_DS"))
	public DataSourceConfig getDatasourceConfig() {
		return datasourceConfig;
	}*/

	/**
	 * @return the fileCdrSummaryDBEnable
	 */
	@Column(name = "CDRSUMMDBENABLE", nullable = false, length = 10)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isFileCdrSummaryDBEnable() {
		return fileCdrSummaryDBEnable;
	}

	/**
	 * @return the webservicesEnable
	 */
	@Column(name = "WEBSERVICEENABLE", nullable = false, length = 10)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isWebservicesEnable() {
		return webservicesEnable;
	}
	
	/**
	 * @return the restWebservicesEnable
	 */
	@Column(name = "RESTWEBSERVICEENABLE", nullable = false, length = 10)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isRestWebservicesEnable() {
		return restWebservicesEnable;
	}

	/**
	 * @return the serverHome
	 */
	@Column(name = "SERVERHOME", nullable = true, length = 200)
	@XmlElement
	public String getServerHome() {
		return serverHome;
	}

	/**
	 * @return the javaHome
	 */
	@Column(name = "JAVAHOME", nullable = true, length = 200)
	@XmlElement
	public String getJavaHome() {
		return javaHome;
	}

	@Column(name = "MINMEMORY", nullable = true, length = 20)
	@XmlElement
	public int getMinMemoryAllocation() {
		return minMemoryAllocation;
	}

	@Column(name = "MAXMEMORY", nullable = true, length = 20)
	@XmlElement
	public int getMaxMemoryAllocation() {
		return maxMemoryAllocation;
	}

	@Column(name = "CONNECTIONRETRY", nullable = false, length = 5)
	@XmlElement
	public int getMaxConnectionRetry() {
		return maxConnectionRetry;
	}

	@Column(name = "RETRYINTERVAL", nullable = false, length = 5)
	@XmlElement
	public int getRetryInterval() {
		return retryInterval;
	}

	@Column(name = "CONNECTIONTIMEOUT", nullable = false, length = 20)
	@XmlElement
	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	@Column(name = "SCRIPTNAME", nullable = true, length = 50)
	@XmlElement
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * @return the selfSNMPServerConfig
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@XmlElement
	@OneToMany(mappedBy = "serverInstance",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@DiffIgnore
	public List<SNMPServerConfig> getSelfSNMPServerConfig() {
		return selfSNMPServerConfig;
	}

	/**
	 * @return the snmpListeners
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@OneToMany(mappedBy = "serverInstance",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@XmlElement
	@DiffIgnore
	public List<SNMPServerConfig> getSnmpListeners() {
		return snmpListeners;
	}

	/**
	 * @return the thresholdSysAlertEnable
	 */
	@Column(name = "THRESHOLDSYSALERTENABLE", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isThresholdSysAlertEnable() {
		return thresholdSysAlertEnable;
	}

	/**
	 * @return the thresholdTimeInterval
	 */
	@Column(name = "THRESHOLDTIMEINTERVAL", nullable = true, length = 4)
	public int getThresholdTimeInterval() {
		return thresholdTimeInterval;
	}

	/**
	 * @return the thresholdMemory
	 */
	@Column(name = "THRESHOLDMEMORY", nullable = true, length = 4)
	public int getThresholdMemory() {
		return thresholdMemory;
	}

	/**
	 * @return the loadAverage
	 */
	@Column(name = "THRESHOLDLOADAVERAGE", nullable = true, length = 4)
	public int getLoadAverage() {
		return loadAverage;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @param server
	 *            the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @param fileStatInDBEnable
	 *            the fileStatInDBEnable to set
	 */
	public void setFileStatInDBEnable(boolean fileStatInDBEnable) {
		this.fileStatInDBEnable = fileStatInDBEnable;
	}

	/**
	 * @param fileCdrSummaryDBEnable
	 *            the fileCdrSummaryDBEnable to set
	 */
	public void setFileCdrSummaryDBEnable(boolean fileCdrSummaryDBEnable) {
		this.fileCdrSummaryDBEnable = fileCdrSummaryDBEnable;
	}

	/**
	 * @param webservicesEnable
	 *            the webservicesEnable to set
	 */
	public void setWebservicesEnable(boolean webservicesEnable) {
		this.webservicesEnable = webservicesEnable;
	}
	
	/**
	 * @param restWebservicesEnable
	 *            the restWebservicesEnable to set
	 */
	public void setRestWebservicesEnable(boolean webservicesEnable) {
		this.restWebservicesEnable = webservicesEnable;
	}

	/**
	 * @param minDiskSpace
	 *            the minDiskSpace to set
	 */
	public void setMinDiskSpace(String minDiskSpace) {
		this.minDiskSpace = minDiskSpace;
	}

	/**
	 * @param snmpAlertEnable
	 *            the snmpAlertEnable to set
	 */
	public void setSnmpAlertEnable(boolean snmpAlertEnable) {
		this.snmpAlertEnable = snmpAlertEnable;
	}

	/**
	 * @param syncStatus
	 *            the syncStatus to set
	 */
	public void setSyncSIStatus(boolean syncStatus) {
		this.syncSIStatus = syncStatus;
	}

	/**
	 * 
	 * @param syncChildStatus
	 */
	public void setSyncChildStatus(boolean syncChildStatus) {
		this.syncChildStatus = syncChildStatus;
	}

	/**
	 * @param services
	 *            the services to set
	 */
	public void setServices(List<Service> services) {
		this.services = services;
	}

	/**
	 * @param agentList
	 *            the agentList to set
	 */
	public void setAgentList(List<Agent> agentList) {
		this.agentList = agentList;
	}

	/**
	 * @param myDSconfig
	 *            the myDSconfig to set
	 *//*
	public void setDatasourceConfig(DataSourceConfig datasourceConfig) {
		this.datasourceConfig = datasourceConfig;
	}*/

	/**
	 * @param instanceLogsDetail
	 *            the instanceLogsDetail to set
	 */
	public void setLogsDetail(LogsDetail logsDetail) {
		this.logsDetail = logsDetail;
	}

	/**
	 * @param serverHome
	 *            the serverHome to set
	 */
	public void setServerHome(String serverHome) {
		this.serverHome = serverHome;
	}

	/**
	 * @param javaHome
	 *            the javaHome to set
	 */
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}

	public void setMinMemoryAllocation(int minMemoryAllocation) {
		this.minMemoryAllocation = minMemoryAllocation;
	}

	public void setMaxMemoryAllocation(int maxMemoryAllocation) {
		this.maxMemoryAllocation = maxMemoryAllocation;
	}

	

	public void setFileStorageLocation(String fileStorageLocation) {
		this.fileStorageLocation = fileStorageLocation;
	}

	public void setMaxConnectionRetry(int maxConnectionRetry) {
		this.maxConnectionRetry = maxConnectionRetry;
	}

	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	/**
	 * @param selfSNMPServerConfig
	 *            the selfSNMPServerConfig to set
	 */
	public void setSelfSNMPServerConfig(List<SNMPServerConfig> selfSNMPServerConfig) {
		this.selfSNMPServerConfig = selfSNMPServerConfig;
	}

	/**
	 * @param snmpListeners
	 *            the snmpListeners to set
	 */
	public void setSnmpListeners(List<SNMPServerConfig> snmpListeners) {
		this.snmpListeners = snmpListeners;
	}

	/**
	 * @param thresholdSysAlertEnable
	 *            the thresholdSysAlertEnable to set
	 */
	public void setThresholdSysAlertEnable(boolean thresholdSysAlertEnable) {
		this.thresholdSysAlertEnable = thresholdSysAlertEnable;
	}

	/**
	 * @param thresholdTimeInterval
	 *            the thresholdTimeInterval to set
	 */
	public void setThresholdTimeInterval(int thresholdTimeInterval) {
		this.thresholdTimeInterval = thresholdTimeInterval;
	}

	/**
	 * @param thresholdMemory
	 *            the thresholdMemory to set
	 */
	public void setThresholdMemory(int thresholdMemory) {
		this.thresholdMemory = thresholdMemory;
	}

	/**
	 * @param loadAverage
	 *            the loadAverage to set
	 */
	public void setLoadAverage(int loadAverage) {
		this.loadAverage = loadAverage;
	}
	
	/**
	 * 
	 * @param mediationRoot
	 */
	public void setMediationRoot(String mediationRoot) {
		this.mediationRoot = mediationRoot;
	}

	/**
	 * 
	 * @param reprocessingBackupPath
	 */
	public void setReprocessingBackupPath(String reprocessingBackupPath) {
		this.reprocessingBackupPath = reprocessingBackupPath;
	}
	
	/**
	 * @return the databaseInit
	 */
	@Column(name = "DATABASEINIT", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isDatabaseInit() {
		return databaseInit;
	}

	/**
	 * @param databaseInit the databaseInit to set
	 */
	public void setDatabaseInit(boolean databaseInit) {
		this.databaseInit = databaseInit;
	}
	

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "SMDSID", nullable = true, foreignKey = @ForeignKey(name = "FK_SRV_INSTANCE_DS_SM"))
	public DataSourceConfig getServerManagerDatasourceConfig() {
		return serverManagerDatasourceConfig;
	}

	public void setServerManagerDatasourceConfig(DataSourceConfig serverManagerDatasourceConfig) {
		this.serverManagerDatasourceConfig = serverManagerDatasourceConfig;
	}
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "IPLOGEERDSID", nullable = true, foreignKey = @ForeignKey(name = "FK_SRV_INSTANCE_DS_IPLOGGER"))
	public DataSourceConfig getIploggerDatasourceConfig() {
		return iploggerDatasourceConfig;
	}

	public void setIploggerDatasourceConfig(DataSourceConfig iploggerDatasourceConfig) {
		this.iploggerDatasourceConfig = iploggerDatasourceConfig;
	}
}