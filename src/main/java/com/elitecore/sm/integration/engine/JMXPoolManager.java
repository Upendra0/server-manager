/**
 * 
 */
package com.elitecore.sm.integration.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.remote.JMXConnector;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.elitecore.sm.common.util.SpringApplicationContext;

/**
 * @author Ranjitsinh Reval
 *
 */
public class JMXPoolManager {

	
	private static GenericObjectPoolConfig config;
	private Map<String, JmxPool<JMXConnector>> map = new HashMap<>();
	private static  Properties properties = (Properties) SpringApplicationContext.getBean("jmxConnectionPool"); // getting spring bean for aop context issue.
	
	private static final Logger logger = Logger.getLogger(JMXPoolManager.class);

	static {
		config = new GenericObjectPoolConfig();
		/**
		 * Returns the target for the minimum number of idle objects to maintain in the pool.
		 *  This setting only has an effect if it is positive and BaseGenericObjectPool.getTimeBetweenEvictionRunsMillis() is greater than zero.
		 *  If this is the case, an attempt is made to ensure that the pool has the required minimum number of instances during idle object eviction runs.
		 *	If the configured value of minIdle is greater than the configured value for maxIdle then the value of maxIdle will be used instead. 
		 */
		config.setMaxIdle(Integer.parseInt(properties.getProperty(ApplicationProperty.JMX_POOL_MAX_IDLE)));
		/**
		 * Returns the cap on the number of "idle" instances in the pool. 
		 * If maxIdle is set too low on heavily loaded systems it is possible you will see objects being destroyed 
		 * and almost immediately new objects being created.
		 * This is a result of the active threads momentarily returning objects faster than they are requesting them them,
		 * causing the number of idle objects to rise above maxIdle. 
		 * The best value for maxIdle for heavily loaded system will vary but the default is a good starting point.
		 */
		config.setMinIdle(Integer.parseInt(properties.getProperty(ApplicationProperty.JMX_POOL_MIN_IDLE)));
		config.setMaxTotal(Integer.parseInt(properties.getProperty(ApplicationProperty.JMX_POOL_MAX_TOTAL)));
		config.setJmxEnabled(Boolean.TRUE);
		config.setTestOnBorrow(Boolean.TRUE);
		config.setTestOnReturn(Boolean.TRUE);
		config.setTestOnCreate(Boolean.TRUE);
	}

	private static JMXPoolManager jmxPoolManagerInstance = null;
	
	/** Default constructor */
	public JMXPoolManager() {
		// Default Constructor
	}
	
	/**
	 * Creating a singleton object 
	 * @return
	 */
	public static JMXPoolManager getInstance() {
		if (jmxPoolManagerInstance == null) {
			jmxPoolManagerInstance = new JMXPoolManager();
		}
		return jmxPoolManagerInstance;
	}
	
	
	/**
	 * Provides connection from the pool.
	 * @param host
	 * @param port
	 * @throws ConnectionNotFoundException
	 */
	public JMXConnector getConnectionFromPool(JmxHost host) throws ConnectionNotFoundException {
		JmxPool<JMXConnector> pool ;
		synchronized (map) {
			pool = map.get(host.getHost()+"-"+host.getPort());
			if (pool == null) {
				pool = new JmxPool<>(new JMXPoolFactory<JMXConnector>(host), config);
				map.put(host.getHost()+"-"+host.getPort(), pool);
				logger.info("New Pool connection created for  :: " + host.getHost()+"-"+host.getPort());
			}
		}
		logger.info("Pool Manager map size is :: " + map.size());
		JMXConnector jmxConnector = null;
		try {
			jmxConnector = pool.borrowObject();
			logger.info("Pool Stats:\n Created:[" + pool.getCreatedCount() + "], Borrowed:[" + pool.getBorrowedCount() + "]");
		} catch (Exception e) {
			// If we include e in following statement, long irrelevant trace is printed in log.
			logger.error(e);
			throw new ConnectionNotFoundException(host.getHost(), String.valueOf(host.getPort()));
		}
		return jmxConnector;
	}

	/**
	 * Returns the connection to the pool.
	 * 
	 * @param host
	 * @param connector
	 * @throws Exception
	 */
	public void returnConnectionToPool(JmxHost host, JMXConnector connector) {
		JmxPool<JMXConnector> pool;
		synchronized (map) {
			pool = map.get(host.getHost()+"-"+host.getPort());
			logger.info("Fetching connection object from pool for " + host.getHost()+"-"+host.getPort());
		}
		try {
			if(pool == null && logger.isDebugEnabled()){
            		logger.debug("Invalid pool state");
			}
			if(pool != null){
				pool.returnObject(connector);
			}
		} catch (Exception e) {
			logger.error(e);
			logger.error(e.getMessage());
		}
	}

	/**
	 * This method will invalidate the pool.
	 * @param host
	 */
	public void cleanup() {
		logger.info("Cleaning all jmx pool objects.");
		Set<String> hostSet = map.keySet();
		for (String host : hostSet) {
			JmxPool<JMXConnector> pool = map.get(host);
			logger.info("In cleanup removed pool : " + pool);
			pool.close();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("out invalidatePool()");
		}
	}
}
