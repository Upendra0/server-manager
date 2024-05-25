/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@Table( name="TBLMSERVICETYPE")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="serviceTypeCache")
@XmlType(propOrder = { "id", "alias","description","type","serviceFullClassName","typeOfService","serviceCategory"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=ServiceType.class)
public class ServiceType extends BaseModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8140479469087082912L;
	private int id;
	private String type;
	private ServiceTypeEnum typeOfService;
	private String description;
	private String alias;
	private String serviceFullClassName;
	private ServiceCategoryEnum serviceCategory;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ServiceType",allocationSize=1)
	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the type
	 */
	@Column(name = "TYPE", nullable = false, length = 100)
	@XmlElement
	public String getType() {
		return type;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 100)
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @return the alias
	 */
	@Column(name="ALIAS",nullable=false,length=255)
	@XmlElement
	public String getAlias() {
		return alias;
	}

	/**
	 * 
	 * @return Service full  qualified class name 
	 */
	@Column(name = "SERVICEFULLCLASSNAME", nullable = false, length = 255)
	@XmlElement
	public String getServiceFullClassName() {
		return serviceFullClassName;
	}

	/**
	 * 
	 * @param serviceFullClassName
	 */
	public void setServiceFullClassName(String serviceFullClassName) {
		this.serviceFullClassName = serviceFullClassName;
	}


	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @param alias
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * 
	 * @return typeOfService
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "TYPEOFSERVICE", nullable = false, length = 100)
	@XmlElement
	public ServiceTypeEnum getTypeOfService() {
		return typeOfService;
	}

	/**
	 * 
	 * @param typeOfService
	 */
	public void setTypeOfService(ServiceTypeEnum typeOfService) {
		this.typeOfService = typeOfService;
	}
	
	/**
	 * 
	 * @return 
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "SERVICECATEGORY", nullable = false, length = 100)
	@XmlElement
	public ServiceCategoryEnum getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * 
	 * @param serviceCategory
	 */
	public void setServiceCategory(ServiceCategoryEnum serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	
	/**
	 * Check if alias is same then both service type is same 
	 * @param o
	 * @return
	 */
	@Override
    public boolean equals(Object o) {
		boolean isSameServiceType=false;
		if(o !=null && this.getAlias().equals(((ServiceType)o).getAlias())){
			isSameServiceType=true;
		}
		
		return isSameServiceType;
	}
	
	@Override
	public int hashCode(){
		return super.hashCode();
	}
	
}