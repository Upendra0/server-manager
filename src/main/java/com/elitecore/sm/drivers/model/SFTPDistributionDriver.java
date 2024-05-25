/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Entity()
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
@DiscriminatorValue(EngineConstants.SFTP_DISTRIBUTION_DRIVER)
public class SFTPDistributionDriver extends FTPDistributionDriver {

	// add SFTP distribution parameters
	/**
	 * 
	 */
	private static final long serialVersionUID = -7435929735476570464L;

}
