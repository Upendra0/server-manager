package com.elitecore.sm.license.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * 
 * @author avani.panchal
 *
 */
@Component(value = "license")
@Entity()
@Table(name = "TBLTLICENSE",uniqueConstraints=@UniqueConstraint(columnNames={"SMSERVERID", "APPLICATIONPATH"}))
@DynamicUpdate	
public class License extends BaseModel{
	
	private static final long serialVersionUID = 4398110581558834930L;
	private int id;
	private String hostName;
	private String smServerId;
	private String location;
	private String customer;
	private Date startDate = new Date();
	private Date endDate;
	private LicenseTypeEnum licenceType;
	private String productType;
	private byte[] tps;
	private String dailyRecords;
	private String monthlyRecords;
	
	private ServerInstance serverInstance;
	private String componentType;
	private String applicationPath;
	private Circle circle;
	
	public License(){
		// Default constructor
	}
	
	public License(String hostName, Date startDate, Date endDate, ServerInstance serverInstance, String componentType) {
		super();
		this.hostName = hostName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.serverInstance = serverInstance;
		this.componentType = componentType;
	}
	
	public License(int id, String hostName, String smServerId, String location, String customer, Date startDate, Date endDate, LicenseTypeEnum licenceType, String productType, byte[] tps, String dailyRecords, String monthlyRecords) {
		super();
		this.id = id;
		this.hostName = hostName;
		this.smServerId = smServerId;
		this.location = location;
		this.customer = customer;
		this.startDate = startDate;
		this.endDate = endDate;
		this.licenceType = licenceType;
		this.productType = productType;
		this.tps = tps;
		this.dailyRecords = dailyRecords;
		this.monthlyRecords = monthlyRecords;
	}
	
	/**
	 * 
	 * @return id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="Licence",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return hostName
	 */
	@Column(name = "HOSTNAME", nullable = false, length = 150)
	@XmlElement
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * 
	 * @param hostName
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	/**
	 * 
	 * @return serverId
	 */
	@Column(name = "SMSERVERID", nullable = true, unique=true)
	@XmlElement
	public String getSmServerId() {
		return smServerId;
	}
	
	/**
	 * 
	 * @param smServerId
	 */
	public void setSmServerId(String smServerId) {
		this.smServerId = smServerId;
	}
	
	/**
	 * 
	 * @return location
	 */
	@Column(name = "LOCATION", nullable = true)
	@XmlElement
	public String getLocation() {
		return location;
	}
	
	/**
	 * 
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * 
	 * @return customer
	 */
	@Column(name = "CUSTOMER", nullable = true)
	@XmlElement
	public String getCustomer() {
		return customer;
	}
	
	/**
	 * 
	 * @param customer
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	
	/**
	 * 
	 * @return startDate
	 */
	@Column(name = "STARTDATE", nullable = false)
	@XmlElement
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/**
	 * 
	 * @return endDate
	 */
	@Column(name = "ENDDATE", nullable = false)
	@XmlElement
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * 
	 * @return licenceType
	 */
	@Column(name = "LICENCETYPE", nullable = false)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public LicenseTypeEnum getLicenceType() {
		return licenceType;
	}
	
	/**
	 * 
	 * @param licenceType
	 */
	public void setLicenceType(LicenseTypeEnum licenceType) {
		this.licenceType = licenceType;
	}
	
	/**
	 * 
	 * @return productType
	 */
	@Column(name = "PRODUCTTYPE", nullable = true)
	@XmlElement
	public String getProductType() {
		return productType;
	}
	
	/**
	 * 
	 * @param productType
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}

	
	/**
	 * @return the tps
	 */
	@Lob
	@Column(name = "TPS", nullable = true)
	public byte[] getTps() {
		return tps;
	}

	
	/**
	 * @return the dailyRecords
	 */
	@Column(name = "DAILYRECORDS", nullable = true, length=150)
	public String getDailyRecords() {
		return dailyRecords;
	}

	
	/**
	 * @return the monthlyRecords
	 */
	@Column(name = "MONTHLYRECORDS", nullable = true, length=150)
	public String getMonthlyRecords() {
		return monthlyRecords;
	}

	/**
	 * @return the serverInstance
	 */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
	@JoinColumn(name = "SERVERINSTANCEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVER_INSTANCE_ID"))
	@XmlElement
	public ServerInstance getServerInstance() {
		return serverInstance;
	}

	
	/**
	 * @return the componentType
	 */
	@Column(name = "COMPONENTTYPE", nullable = false, length=15)
	public String getComponentType() {
		return componentType;
	}
	
	/**
	 * @param bs the tps to set
	 */
	public void setTps(byte[] bs) {
		this.tps = bs;
	}

	
	/**
	 * @param dailyRecords the dailyRecords to set
	 */
	public void setDailyRecords(String dailyRecords) {
		this.dailyRecords = dailyRecords;
	}

	
	/**
	 * @param monthlyRecords the monthlyRecords to set
	 */
	public void setMonthlyRecords(String monthlyRecords) {
		this.monthlyRecords = monthlyRecords;
	}

	
	/**
	 * @param serverInstance the serverInstance to set
	 */
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}

	
	/**
	 * @param componentType the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	/**
	 * @return the applicationPath
	 */
	@Column(name = "APPLICATIONPATH", nullable = true, length=1000)
	public String getApplicationPath() {
		return applicationPath;
	}

	/**
	 * @param applicationPath the applicationPath to set
	 */
	public void setApplicationPath(String applicationPath) {
		this.applicationPath = applicationPath;
	}
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "CIRCLEID", nullable = true, foreignKey = @ForeignKey(name = "FK_TBLTLICENSE_CIRCLE_ID"))
	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

}
