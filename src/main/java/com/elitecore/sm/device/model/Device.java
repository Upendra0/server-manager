package com.elitecore.sm.device.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.parser.model.ParserMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value = "device")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLMDEVICE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="deviceCache")
@XmlType(propOrder = { "id", "name","description","decodeType","isPreConfigured","deviceType","vendorType"})
public class Device extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String description;
	private String decodeType;
	private int isPreConfigured;
	private DeviceType deviceType;
	private VendorType vendorType;
	private List<ParserMapping> parserMapping = new ArrayList<>(0);
	private List<ComposerMapping> composerMapping = new ArrayList<>(0);
	
	public Device(){
		//Default constructor
	}
	
	
	public Device(int id, String name, String description, String decodeType, int isPreConfigured, DeviceType deviceType, VendorType vendorType) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.decodeType = decodeType;
		this.isPreConfigured = isPreConfigured;
		this.deviceType = deviceType;
		this.vendorType = vendorType;
	}


	public Device(int id, String name, String description, VendorType vendorType) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.vendorType = vendorType;
	}
	
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="Device",allocationSize=1)
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 250,  unique = true)
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
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 255)
    @XmlElement
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the deviceType
	 */
	@XmlElement
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEVICETYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_DEVICE_TYPE"))
	@DiffIgnore
	public DeviceType getDeviceType() {
		return deviceType;
	}
	/**
	 * @param deviceType the deviceType to set
	 */
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	/**
	 * @return the vendorType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDORTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_VENDOR_TYPE"))
	@DiffIgnore
	public VendorType getVendorType() {
		return vendorType;
	}
	/**
	 * @param vendorType the vendorType to set
	 */
	public void setVendorType(VendorType vendorType) {
		this.vendorType = vendorType;
	}
	
	/**
	 * @return the decodeType
	 */
	@Column(name = "DECODETYPE", nullable = false, length = 10)
    @XmlElement
	public String getDecodeType() {
		return decodeType;
	}
	/**
	 * @param decodeType the decodeType to set
	 */
	public void setDecodeType(String decodeType) {
		this.decodeType = decodeType;
	}
	
	
	/**
	 * @return the parserMapping
	 */
	@XmlTransient
	@OneToMany(mappedBy = "device",fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	public List<ParserMapping> getParserMapping() {
		return parserMapping;
	}
	/**
	 * @param parserMapping the parserMapping to set
	 */
	public void setParserMapping(List<ParserMapping> parserMapping) {
		this.parserMapping = parserMapping;
	}

	/**
	 * @return the composerMapping
	 */
	@XmlTransient
	@OneToMany(mappedBy = "device",fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	public List<ComposerMapping> getComposerMapping() {
		return composerMapping;
	}

	
	/**
	 * @param composerMapping the composerMapping to set
	 */
	public void setComposerMapping(List<ComposerMapping> composerMapping) {
		this.composerMapping = composerMapping;
	}

	/**
	 * @return the isPreConfigured
	 */
	@Column(name = "ISPRECONFIGURED", nullable = false)
    @XmlElement
	public int getIsPreConfigured() {
		return isPreConfigured;
	}

	/**
	 * @param isPreConfigured the isPreConfigured to set
	 */
	public void setIsPreConfigured(int isPreConfigured) {
		this.isPreConfigured = isPreConfigured;
	}
	
}
