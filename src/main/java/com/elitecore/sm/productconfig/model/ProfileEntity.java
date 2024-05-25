package com.elitecore.sm.productconfig.model;

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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.server.model.ServerType;

/**
 * 
 * @author  avani.panchal
 *
 */
@Component(value = "profileEntity")
@Entity()
@Table(name = "TBLTPROFILENTITY" ,uniqueConstraints=@UniqueConstraint(columnNames={"SERVERTYPEID", "ISDEFAULT","ENTITYALIAS"}))
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="profileEntityCache")
public class ProfileEntity extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private ServerType serverType;
	private boolean isDefault;
	private String entityType;
	private String entityAlias;
	private License license;
	
	/**
	 * 
	 * @return id
	 */ 
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ProfileEntity",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}
	
	/**LICENSEID
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return serverType
	 */
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="SERVERTYPEID")
	public ServerType getServerType() {
		return serverType;
	}
	
	/**
	 * 
	 * @param serverType
	 */
	public void setServerType(ServerType serverType) {
		this.serverType = serverType;
	}
	
	
	/**
	 * 
	 * @return isDefault
	 */
	@Column(name = "ISDEFAULT", nullable = false)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 
	 * @param isDefault
	 */
	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 
	 * @return entityType
	 */
	@Column(name = "ENTITYTYPE", nullable = false)
	public String getEntityType() {
		return entityType;
	}
	
	/**
	 * 
	 * @param entityType
	 */
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
	/**
	 * 
	 * @return entityAlias
	 */
	@Column(name = "ENTITYALIAS", nullable = false)
	public String getEntityAlias() {
		return entityAlias;
	}
	
	/**
	 * 
	 * @param entityAlias
	 */
	public void setEntityAlias(String entityAlias) {
		this.entityAlias = entityAlias;
	}

	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name = "LICENSEID", nullable = true, foreignKey = @ForeignKey(name = "FK_LICENSE_ID"))
	public License getLicense() {
		return license;
	}

	public void setLicense(License license) {
		this.license = license;
	}

	
}
