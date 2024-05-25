/**
 * 
 */
package com.elitecore.sm.integration.engine;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.BaseConstants;


/**
 * @author Ranjitsinh Reval
 *
 */
public class JMXPoolFactory<Q> extends BasePooledObjectFactory<Q> {
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@SuppressWarnings("unused")
	private String errorMessage;

	private JmxHost jmxHost;
	/**
	 * constructor for jmx objects.
	 * @param jmxhost
	 */
	public JMXPoolFactory(JmxHost jmxHost){
		this.jmxHost = jmxHost;
	}
	/**
	 * default constructor
	 */
	public JMXPoolFactory(){
		//Default contructor
	}
	
	/**
	 * The create method will create the actual JMX connection.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Q create() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("In create method");
		}

		StringBuilder vUrlString = new StringBuilder(ApplicationProperty.JMX_URL_FRONT);
		vUrlString.append(jmxHost.getHost());
		vUrlString.append(ApplicationProperty.COLON);
		vUrlString.append(jmxHost.getPort());
		vUrlString.append(ApplicationProperty.JMX_URL_BACK);
		try {

			ExecutorService executor = Executors.newSingleThreadExecutor();
			final JMXServiceURL url = new JMXServiceURL(vUrlString.toString());
			
			Future<JMXConnector> future = executor.submit(new Callable<JMXConnector>() {
				@Override
				public JMXConnector call() throws Exception  {
					try {
						return JMXConnectorFactory.connect(url, null);
					} catch (IOException e) {
						errorMessage = BaseConstants.JMX_CONNECTION_FAILURE;
						logger.error(" Could not create JMX connection inside create method "+ e.getMessage());
						// need to rethrow exception
						throw e;
					}
				}
			});
			return (Q) future.get(jmxHost.getConnectionTimeout(), TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error(e);
			errorMessage = BaseConstants.JMX_CONNECTION_FAILURE;
			throw new ConnectionNotFoundException(jmxHost.getHost(), String.valueOf(jmxHost.getPort()));
		}
	}
	/**
	 *  This method will wrap the pool object.
	 */
    @Override
    public PooledObject<Q> wrap(Q jmxConnector) {
        return new DefaultPooledObject<>(jmxConnector);
    }
    /**
     * This method will make the pooled object.
     */
	@Override
	public PooledObject<Q> makeObject() throws Exception {
		return wrap(create());
	}

	/**
	 * This method will destroy the JMX connection.
	 */
    @Override
    public void destroyObject(PooledObject<Q> jmxPooledObject)throws Exception{
    	JMXConnector jMXConnector = (JMXConnector)(jmxPooledObject.getObject());
    	if(logger.isDebugEnabled()){
    		logger.debug("In destroyObject : jMXConnector "+ jMXConnector.getConnectionId());
    	}
    	jMXConnector.close();
    }
    /**
     * This method will validate the pooled object before it's served.
     */
    @Override    
    public boolean validateObject(PooledObject<Q> jmxPooledObject){
    	logger.info("Validating pool object");
    	boolean isValidConnection = Boolean.TRUE;
		try {
			((JMXConnector)(jmxPooledObject.getObject())).getMBeanServerConnection();
		} catch (IOException e) {
			logger.error(e);
			isValidConnection = Boolean.FALSE;
		} 
        return isValidConnection;
    }
}