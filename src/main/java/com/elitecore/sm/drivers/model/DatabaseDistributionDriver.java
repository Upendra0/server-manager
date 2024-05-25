/**
 * 
 */
package com.elitecore.sm.drivers.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue("DBDist")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "driverCache")
public class DatabaseDistributionDriver extends DistributionDriver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1530556053973808798L;
	private String tableName;
	private List<DatabaseDistributionDriverAttribute> attributeList = new ArrayList<>(0);
	

	/**
	 * @return the attributeList
	 */
	@OneToMany(mappedBy = "dbDisDriver",fetch=FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@XmlElement
	public List<DatabaseDistributionDriverAttribute> getAttributeList() {
		return attributeList;
	}

	/**
	 * @param attributeList
	 *            the attributeList to set
	 */
	
	public void setAttributeList(List<DatabaseDistributionDriverAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	/**
	 * @return the tableName
	 */
	@XmlElement
	@Column(name = "TBLNAME", nullable = true, length = 100)
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param tableName
	 *            the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

}
