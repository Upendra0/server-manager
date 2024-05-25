/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

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
public class LocalCollectionDriver extends CollectionDriver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1885812139690360622L;

	// no differentiating property identified yet. this class can be
	// discarded.
}
