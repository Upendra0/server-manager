/**
 * 
 */
package com.elitecore.sm.integration.engine;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * This is the pool to keep the JMX connector instances.
 * @param <Q>
 * @author Ranjitsinh Reval
 *
 */
public class JmxPool<Q> extends GenericObjectPool<Q> {

	/**
	 * Constructor.
	 * 
	 * It uses the default configuration for pool provided by
	 * apache-commons-pool2.
	 * 
	 * @param factory
	 */
	public JmxPool(PooledObjectFactory<Q> factory) {
		super(factory);
	}

	/**
	 * Constructor.
	 * 
	 * This can be used to have full control over the pool using configuration
	 * object.
	 * 
	 * @param factory
	 * @param config
	 */
	public JmxPool(PooledObjectFactory<Q> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

}
