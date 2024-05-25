/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "certKey"})
public class SFTPCollectionDriver extends FTPCollectionDriver {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2980046287000699982L;
	// dummy field to represent certificates
	private String certKey;

	/**
	 * @return the certKey
	 */
	@XmlElement
	@Column(name = "CERTKEY", nullable = true, length = 1000)
	public String getCertKey() {
		return certKey;
	}

	/**
	 * @param certKey
	 *            the certKey to set
	 */
	public void setCertKey(String certKey) {
		this.certKey = certKey;
	}

}
