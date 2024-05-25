/**
 * 
 */
package com.elitecore.sm.drivers.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.ControlFileAttributesEnum;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.model.Service;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * @author vandana.awatramani
 *
 */

@Entity()
@Table(name = "TBLTDRIVER")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING,length=100)
@XmlSeeAlso({CollectionDriver.class,DistributionDriver.class})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "id", "name","timeout","applicationOrder","maxRetrycount","fileSeqOrder","minFileRange","maxFileRange","driverType","driverPathList","noFileAlert"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public  class Drivers extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5868780095181446209L;
	private int id;
	private String name;
	private int timeout=1000000;
	private int applicationOrder=0;
	private int maxRetrycount=10;
	private boolean fileSeqOrder= false;
	private int minFileRange=1;
	private int maxFileRange=300;
	private Service service;
	private List<PathList> driverPathList= new ArrayList<>();
	private DriverType driverType;
	private int noFileAlert=60;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="Drivers",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@Column(name = "NAME", unique = true,nullable = false, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @return the timeout
	 */
	@Column(name = "TIMEOUT", nullable = false, length = 9)
	@XmlElement
	@DiffIgnore
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the applicationOrder
	 */
	@Column(name = "APPLICATIONORDER", nullable = false, length = 3)
	@XmlElement
	public int getApplicationOrder() {
		return applicationOrder;
	}

	/**
	 * @return the maxRetrycount
	 */
	@Column(name = "MAXRETRYCOUNT", nullable = true, length = 10)
	@XmlElement
	public int getMaxRetrycount() {
		return maxRetrycount;
	}

	/**
	 * @return the fileSeqOrder
	 */
	@Column(name = "ISFILESEQ", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isFileSeqOrder() {
		return fileSeqOrder;
	}
	
	/**
	 * 
	 * @return driver Type
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DRIVERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_DRIVER_DRIVERTYPE"))
	@DiffIgnore
	public DriverType getDriverType() {
		return driverType;
	}

	/**
	 * @return the fileRange
	 */
	@Column(name = "MINFILERANGE", nullable = true, length = 10)
	@XmlElement
	public int getMinFileRange() {
		return minFileRange;
	}
	
	/**
	 * @return the fileRange
	 */
	@Column(name = "MAXFILERANGE", nullable = true, length = 10)
	@XmlElement
	public int getMaxFileRange() {
		return maxFileRange;
	}

	/**
	 * @return the service
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICEID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICE_DRIVER"))
	@XmlTransient
	@DiffIgnore
	public Service getService() {
		return service;
	}
	
	

	/**
	 * @return the driverPathList
	 */
	@OneToMany(mappedBy="driver",cascade=CascadeType.ALL )
	@XmlElement
	@DiffIgnore
	public List<PathList> getDriverPathList() {
		return driverPathList;
	}

	/**
	 * @return the noFileAlert
	 */
	@XmlElement
	@Column(name = "NOFILEALERT", nullable = true, length = 5)
	public int getNoFileAlert() {
		return noFileAlert;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int driverId) {
		this.id = driverId;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String driverName) {
		this.name = driverName;
	}

	/**
	 * @param timeout
	 *            the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param applicationOrder
	 *            the applicationOrder to set
	 */
	public void setApplicationOrder(int applicationOrder) {
		this.applicationOrder = applicationOrder;
	}

	/**
	 * @param maxRetrycount
	 *            the maxRetrycount to set
	 */
	public void setMaxRetrycount(int maxRetrycount) {
		this.maxRetrycount = maxRetrycount;
	}

	/**
	 * @param fileSeqOrder
	 *            the fileSeqOrder to set
	 */
	public void setFileSeqOrder(boolean fileSeqOrder) {
		this.fileSeqOrder = fileSeqOrder;
	}

	public void setMinFileRange(int minFileRange) {
		this.minFileRange = minFileRange;
	}

	public void setMaxFileRange(int maxFileRange) {
		this.maxFileRange = maxFileRange;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}
	
	/**
	 * @param driverPathList the driverPathList to set
	 */
	public void setDriverPathList(List<PathList> driverPathList) {
		this.driverPathList = driverPathList;
	}
	
	/**
	 * @param driverType 
	 */
	public void setDriverType(DriverType driverType) {
		this.driverType = driverType;
	}
	
	/**
	 * @param noFileAlert
	 *            the noFileAlert to set
	 */
	public void setNoFileAlert(int noFileAlert) {
		this.noFileAlert = noFileAlert;
	}
}