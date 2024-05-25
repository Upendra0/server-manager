/**
 * 
 */
package com.elitecore.sm.datasource.model;

import java.io.Serializable;

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
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators; 

/**
 * @author vandana.awatramani
 *
 */
@Component(value = "dsConfigObject")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTDSCONFIG")
@DynamicUpdate
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "datasourceConfigCache")
@XmlType(propOrder = { "id","name","type","connURL","username","password","minPoolSize","maxPoolsize","failTimeout"})
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class DataSourceConfig extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3430595285236163774L;
	private int id;
	private String name;
	private String connURL;
	private String type;
	private String username;
	private String password;
	private int minPoolSize = 10;
	private int maxPoolsize = 15;
	private String failTimeout;

	/**
	 * @return the dsId
	 */
	@Id
	@Column(name = "DSID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DataSourceConfig",allocationSize=1)

	@XmlElement
	public int getId() {
		return id;
	}

	/**
	 * @return the dsName
	 */
	@Column(name = "NAME", length=250, unique= true)
	public String getName() {
		return name;
	}

	/**
	 * @return the connURL
	 */
	@Column(name = "CONNURL", length=700)
	public String getConnURL() {
		return connURL;
	}

	/**
	 * @return the dsType
	 */
	@Column(name = "TYPE", length=200)
	public String getType() {
		return type;
	}

	/**
	 * @return the username
	 */
	@Column(name = "USERNAME", length=200)
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	
	@Column(name = "DS_PASSWORD", nullable = true,length=4000)
	public String getPassword() {
		return password;
	}

	/**
	 * @return the minPoolSize
	 */
	@Column(name = "MINPOOLSIZE")
	public int getMinPoolSize() {
		return minPoolSize;
	}

	/**
	 * @return the maxPoolsize
	 */
	@Column(name = "MAXPOOLSIZE")
	public int getMaxPoolsize() {
		return maxPoolsize;
	}
	
	/**
	 * @return the failtimeout
	 */
	@Column(name = "FAILTIMEOUT", length=10)
	public String getFailTimeout() {
		return failTimeout;
	}

	/**
	 * @param dsId
	 *            the dsId to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param dsName
	 *            the dsName to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param connURL
	 *            the connURL to set
	 */
	public void setConnURL(String connURL) {
		this.connURL = connURL;
	}

	/**
	 * @param dsType
	 *            the dsType to set
	 */
	public void setType(String dsType) {
		this.type = dsType;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param minPoolSize
	 *            the minPoolSize to set
	 */
	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	/**
	 * @param maxPoolsize
	 *            the maxPoolsize to set
	 */
	public void setMaxPoolsize(int maxPoolsize) {
		this.maxPoolsize = maxPoolsize;
	}
	
	/**
	 * @param failTimeout
	 *            the failTimeout to set
	 */
	public void setFailTimeout(String filaTimeout) {
		this.failTimeout = filaTimeout;
	}

	
}
