/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.elitecore.sm.common.constants.EngineConstants;



/**
 * @author vandana.awatramani
 *
 */
@Entity()
@DiscriminatorValue(EngineConstants.HADOOP_DISTRIBUTION_DRIVER)
public class HadoopDistributionDriver extends DistributionDriver {
	
	// add Hadoop Specific parameters
	
	private static final long serialVersionUID = -7719129717303224339L;

}
