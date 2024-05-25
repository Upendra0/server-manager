/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.springframework.stereotype.Component;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.services.model.Service;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani This class is associated with Services or Drivers,
 *         So it has One to Many mapping with particular Service or Driver.
 *         Either one of the mapping is always present while the other is null.
 * 
 * 
 */
@Component(value="pathList")
@Entity()
@Table(name = "TBLTPATHLIST")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlSeeAlso({CollectionDriverPathList.class, DistributionDriverPathList.class,
		CommonPathList.class, ProcessingPathList.class,ParsingPathList.class, DataConsolidationPathList.class, AggregationServicePathList.class })
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="pathListCache")
@XmlType(propOrder = { "id", "pathId", "name","readFilePath","charRenameOperationList"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class PathList extends BaseModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6733404212991596531L;

	// Added by Jay Shah
	private int id;

	private String readFilePath;

	private Drivers driver;
	private Service service;
	private String name;
	private String pathId;
	private List<PathListHistory> pathListHistoryList = new ArrayList<>(0);
	private List<CharRenameOperation> charRenameOperationList = new ArrayList<>(0);
	
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="PathList",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @return the readFilePath
	 */
	@Column(name = "READFILEPATH", nullable = false, length = 500)
	@XmlElement
	public String getReadFilePath() {
		return readFilePath;
	}

	/**
	 * @return the driver
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "DRIVERID", nullable = true, foreignKey = @ForeignKey(name = "FK_DRIVER_PATHLIST"))
	@XmlTransient
	@DiffIgnore
	public Drivers getDriver() {
		return driver;
	}

	/**
	 * @return the service
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVICE_PATHLIST"))
	@XmlTransient
	@DiffIgnore
	public Service getService() {
		return service;
	}

	/**
	 * 
	 * @param readFilePath
	 * @param readFilenamePrefix
	 * @param readFilenameSuffix
	 * @param readFilenameContains
	 * @param readFilenameExcludeTypes
	 * @param writeFilePath
	 * @param writeFilenamePrefix
	 *
	 */
	public PathList(String readFilePath) {
		super();

		this.readFilePath = readFilePath;

	}

	public PathList() {
		// default no arg constructor
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param readFilePath
	 *            the readFilePath to set
	 */
	public void setReadFilePath(String readFilePath) {
		this.readFilePath = readFilePath;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(Drivers driver) {
		this.driver = driver;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return the name
	 * Each pathlist should be names always 
	 */
	@Column(name = "PLNAME", nullable = false, unique=true, length = 250)
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get Path Id
	 * @return the path id
	 */
	@XmlElement
	@Column(name = "PATHID", nullable = false, length = 3)
	@DiffIgnore
	public String getPathId() {
		return pathId;
	}
	
	/**
	 * Set Path Id
	 * 
	 * @param pathId the path id
	 */
	public void setPathId(String pathId) {
		this.pathId = pathId;
	}
	
	@OneToMany(mappedBy = "pathList", fetch=FetchType.LAZY)
	@XmlTransient
	public List<PathListHistory> getPathListHistoryList() {
		return pathListHistoryList;
	}

	public void setPathListHistoryList(List<PathListHistory> pathListHistoryList) {
		this.pathListHistoryList = pathListHistoryList;
	}
	
	/**
	 * @return the charRenameOperationList
	 */
	@OneToMany(mappedBy = "pathList", cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@XmlElement
	public List<CharRenameOperation> getCharRenameOperationList() {
		return charRenameOperationList;
	}
	
	/**
	 * @param charRenameOperationList the charRenameOperationList to set
	 */
	public void setCharRenameOperationList(
			List<CharRenameOperation> charRenameOperationList) {
		this.charRenameOperationList = charRenameOperationList;
	}

}
