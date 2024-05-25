/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Entity()
@DiscriminatorValue(EngineConstants.LOCAL_DISTRIBUTION_DRIVER)
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="driverCache")
public class LocalDistributionDriver extends DistributionDriver {
	//add Local Distribution driver specific parameters
	
	private static final long serialVersionUID = 3107575746414331141L;

}
