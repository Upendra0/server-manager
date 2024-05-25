/**
 * 
 */
package com.elitecore.sm.device.model;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component(value = "deviceType")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLMDEVICETYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="deviceTypeCache")
@XmlType(propOrder = { "id", "name","description"})
public class DeviceType extends BaseModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	
	
	public DeviceType(){
		//Default Constructor
	}
	public DeviceType(Date createdDate, int createdByStaffId,
			Date lastUpdatedDate, int lastUpdatedByStaffId, StateEnum status,
			int id, String name, String description) {
		super(createdDate, createdByStaffId, lastUpdatedDate,
				lastUpdatedByStaffId, status);
		this.id = id;
		this.name = name;
		this.description = description;
	}

	
	
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="DeviceType",allocationSize=1)
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
	@Column(name = "NAME", nullable = false, length = 250, unique = true)
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
	
}
