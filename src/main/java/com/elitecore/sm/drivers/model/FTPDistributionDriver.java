/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.stereotype.Component;



/**
 * @author jay.shah
 *
 */
@Component(value = "ftpDistributionDriver")
@Entity()
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@XmlSeeAlso({SFTPDistributionDriver.class})
public class FTPDistributionDriver extends DistributionDriver {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3589429376702596267L;
	private ConnectionParameter ftpConnectionParams;

	/**
	 * @return the ftpConnectionParams
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "CONNPARAMID", nullable = true, foreignKey = @ForeignKey(name = "FK_DistDRV_CONNPARAM"))
	public ConnectionParameter getFtpConnectionParams() {
		return ftpConnectionParams;
	}

	/**
	 * @param ftpConnectionParams
	 *            the ftpConnectionParams to set
	 */
	public void setFtpConnectionParams(ConnectionParameter ftpConnectionParams) {
		this.ftpConnectionParams = ftpConnectionParams;
	}

}
