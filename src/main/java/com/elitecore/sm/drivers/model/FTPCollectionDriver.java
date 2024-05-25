/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.pathlist.model.FileFetchParams;

/**
 * @author jay.shah
 *
 */
@Component(value = "ftpCollectionDriver")
@Entity()
@XmlSeeAlso({SFTPCollectionDriver.class})
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlType(propOrder = { "ftpConnectionParams", "myFileFetchParams"})
public class FTPCollectionDriver extends CollectionDriver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7557631435423354963L;
	private ConnectionParameter ftpConnectionParams;
	private FileFetchParams myFileFetchParams;

	/**
	 * @return the ftpConnectionParams
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "CONNPARAMID", nullable = true, foreignKey = @ForeignKey(name = "FK_DRV_CONNPARAM"))
	@XmlElement
	public ConnectionParameter getFtpConnectionParams() {
		return ftpConnectionParams;
	}

	/**
	 * @return the myFileFetchParams
	 */
	@Embedded
	@XmlElement
	public FileFetchParams getMyFileFetchParams() {
		return myFileFetchParams;
	}

	/**
	 * @param ftpConnectionParams
	 *            the ftpConnectionParams to set
	 */
	public void setFtpConnectionParams(ConnectionParameter ftpConnectionParams) {
		this.ftpConnectionParams = ftpConnectionParams;
	}

	/**
	 * @param myFileFetchParams
	 *            the myFileFetchParams to set
	 */
	public void setMyFileFetchParams(FileFetchParams myFileFetchParams) {
		this.myFileFetchParams = myFileFetchParams;
	}

}
