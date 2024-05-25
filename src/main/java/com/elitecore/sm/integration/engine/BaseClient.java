/**
 * 
 */
package com.elitecore.sm.integration.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.elitecore.core.commons.util.restservice.RestServiceConstants;
import com.elitecore.core.server.hazelcast.HazelcastUtility;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.rest.client.RemoteRestHelper;
import com.elitecore.sm.rest.util.RestClientConstants;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.MapCache;


/**
 * @author Sunil Gulabani Jul 13, 2015
 */

@Component
public abstract class BaseClient {

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	private MBeanServerConnection mBeanServerConnection;
	protected JMXConnector jmxConnector;

	protected String ipAddress;
	protected int port;
	private String errorMessage;

	protected int maxConnectionRetry = BaseConstants.SERVER_MGMT_TRY_TO_CONNECT;
	protected int retryInterval = BaseConstants.SERVER_CONNECTION_INTERVAL;
	protected int connectionTimeout = BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT;	

	public static final List<String> errorReprocessingMethods = new ArrayList<String>(Arrays.//NOSONAR
			asList(RestServiceConstants.GET_BYTES_OF_FILE,RestServiceConstants.GET_LIST_OF_ERROR_FILES,
					RestServiceConstants.REPROCESS_LIST_ERROR_FILES,RestServiceConstants.RESTORE_LIST_ARCHIVE_FILES,RestServiceConstants.DELETE_LIST_ERROR_FILES,
					RestServiceConstants.UPLOAD_AND_REPROCESS_ERROR_FILES,RestServiceConstants.REVERT_TO_ORIGINAL_LIST_OF_ERROR_FILES,
					RestServiceConstants.MODIFY_LIST_OF_ERROR_FILES,RestServiceConstants.DOWNLOAD_FILE));
	
	private boolean kubernetesEnvironmentFlag = false;
	
	
	public BaseClient(String ipAddress, int port, int maxConnRetry, int retryInterval, int conTimeout) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.errorMessage = null;
		this.maxConnectionRetry = maxConnRetry;
		this.retryInterval = retryInterval;
		this.connectionTimeout = conTimeout;
		
		String kubernetesFlag =  (String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV);
		if(kubernetesFlag != null && !kubernetesFlag.isEmpty() && Boolean.TRUE.toString().equals(kubernetesFlag)){
			this.kubernetesEnvironmentFlag = true; 
		}
		 
	}
	
	/**
	 * Establishes JMX Connection
	 * 
	 * @return
	 */
	protected boolean isConnected() {
		try {
			if (jmxConnector == null){
				jmxConnector = getJMXConnection();
				
			}
			if(jmxConnector != null){
				logger.info("jmxConnector object is found " + jmxConnector);
				mBeanServerConnection = jmxConnector.getMBeanServerConnection();
			}
			

			if (mBeanServerConnection != null)
				return true;
		} catch (Exception e) {
			errorMessage = BaseConstants.JMX_CONNECTION_FAILURE;
			logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
		}
		return false;
	}
	
	/**
	 * Method will get JMXConnection object from pool. if its not found then it will create new one.
	 * @return
	 */
	protected JMXConnector getJMXConnection() {
		JMXConnector jmxConnection = null;
		JMXPoolManager jmxPoolHelper = JMXPoolManager.getInstance();
		JmxHost host = new JmxHost(ipAddress,port,maxConnectionRetry,retryInterval,connectionTimeout );
		try {
			logger.debug("Getting JMX connection object from pool.");
			jmxConnection = jmxPoolHelper.getConnectionFromPool(host);
		} catch (Exception e) {
			errorMessage = BaseConstants.JMX_CONNECTION_FAILURE;
			logger.error("Error occured during while fetching JMXconnection object.");
			logger.error(e);
		} finally {
			if (jmxConnection != null) {
				jmxPoolHelper.returnConnectionToPool(host, jmxConnection);
			} else {
				errorMessage = BaseConstants.JMX_CONNECTION_FAILURE;
				logger.debug("Failed to get JMX connection object from the JMX pool.");
			}
		}
		return jmxConnection;
	}


	/**
	 * Invokes JMX Calls
	 * 
	 * @param objectName
	 * @param methodName
	 * @param methodParamValues
	 * @param methodParamDataTypes
	 * @return
	 */
	public Object invoke(String objectName, String methodName, Object[] methodParamValues, String[] methodParamDataTypes) {
		if(!kubernetesEnvironmentFlag){
			return invokeRestCall(objectName, null, methodName, methodParamValues);
		}else{
			if(HazelcastUtility.hazelcastMapUpdateCommands.contains(methodName)){
				return executeCommandThroughHazelcastCache(objectName, null, methodName, methodParamValues);
			}else{
				return errorReprocessingMethods.contains(methodName) ? invokeRestCall(objectName, null, methodName, methodParamValues) : null;//NOSONAR
			}
		}
	}

	/**
	 * Invokes Method/Attribute via JMX
	 * 
	 * @param objectName
	 * @param attributeName
	 * @param methodName
	 * @param methodParamValues
	 * @param methodParamDataTypes
	 * @return
	 */
	private Object invokeJMXCall(String objectName, String attributeName, String methodName, Object[] methodParamValues, String[] methodParamDataTypes) {
		logger.debug("objectName: " + objectName);
		logger.debug("attributeName: " + attributeName);
		logger.debug("methodName: " + methodName);
		logger.debug("Max connection retry limit : " + maxConnectionRetry);
		logger.debug("Connection time out : " + connectionTimeout);
		logger.debug("Connection interval time : " + retryInterval);

		if (methodParamValues != null) {
			logger.debug("methodParamValues.length " + methodParamValues.length);
			for (Object o : methodParamValues) {
				if(o!=null)
					logger.debug(" **  methodParamValues  " + o.toString());
			}
		} else {
			logger.debug("methodParamValues is null");
		}

		if (methodParamDataTypes != null) {
			logger.debug("methodParamDataTypes.length " + methodParamDataTypes.length);
			for (Object o : methodParamDataTypes) {
				logger.debug(" #### methodParamDataTypes  " + o.toString());
			}
		} else {
			logger.debug("methodParamDataTypes is null");
		}

		Object response = null;
		for (int i = 0; i < maxConnectionRetry; i++) {
			logger.debug("Trying to connect " + ipAddress + ":" + port + " - " + (i + 1));
			try {

				if (isConnected()) {
					if (attributeName == null) {
						response = mBeanServerConnection.invoke(new ObjectName(objectName), methodName, methodParamValues, methodParamDataTypes);
					} else {
						response = mBeanServerConnection.getAttribute(new ObjectName(objectName), attributeName);
					}
				}
			} catch (Exception e) {
				errorMessage = BaseConstants.JMX_API_FAILURE;
				logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
				if (e.getMessage() != null
						&& "ENVIRONMENT_VARIABLE_NOT_SET".equals(e.getMessage())) {
					errorMessage = BaseConstants.SERVER_HOME_NOT_SET;
				}
			} 
			if (response == null
					&& getErrorMessage() != null
					&& (getErrorMessage().equals(BaseConstants.JMX_API_FAILURE) || getErrorMessage().equals(BaseConstants.JMX_CONNECTION_FAILURE))) {
				try {
					if (i < (maxConnectionRetry - 1)) // check if connect loop will continue than remove error message to get latest error message
						errorMessage = null;
					Thread.sleep(retryInterval);
				} catch (InterruptedException e) { // NOSONAR
					logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
				}
			} else {
				logger.debug("Request was successfully executed - " + (i + 1));
				break;
			}
		}
		return response;
	}

	/**
	 * Invokes JMX Call to get attribute value
	 * 
	 * @param objectName
	 * @param attributeName
	 * @return
	 */
	public Object getAttribute(String objectName, String attributeName) {
		//MED-10359 : make a REST call only , so removing if condition
		//if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(SystemParametersConstant.REST_API_CALL)))
		return invokeRestCall(objectName, null, attributeName, new Object[] {attributeName});
		//return invokeJMXCall(objectName, attributeName, null, null, null);	
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	private Object invokeRestCall(String objectName, String attributeName, String methodName, Object[] methodParamValues) {
		logger.debug("serviceName: " + objectName);
		logger.debug("attributeName: " + attributeName);
		logger.debug("methodName: " + methodName);
		logger.debug("Max connection retry limit : " + maxConnectionRetry);
		logger.debug("Connection time out : " + connectionTimeout);
		logger.debug("Connection interval time : " + retryInterval);

		Object response = null;
		for (int i = 0; i < maxConnectionRetry; i++) {
			logger.debug("Trying to connect " + ipAddress + ":" + port + " - " + (i + 1));
			try {
				RemoteRestHelper restClientHelper = new RemoteRestHelper(ipAddress, port, methodName, methodParamValues);
				response = restClientHelper.invoke();
			} catch (Exception e) {
				errorMessage = RestClientConstants.REST_API_FAILURE;
				logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
				if (e.getMessage() != null
						&& "ENVIRONMENT_VARIABLE_NOT_SET".equals(e.getMessage())) {
					errorMessage = BaseConstants.SERVER_HOME_NOT_SET;
				}
			}
			if (response == null
					&& getErrorMessage() != null
					&& (getErrorMessage().equals(RestClientConstants.REST_API_FAILURE) || getErrorMessage().equals(RestClientConstants.REST_CONNECTION_FAILURE))) {
				try {
					if (i < (maxConnectionRetry - 1)) // check if connect loop will continue than remove error message to get latest error message
						errorMessage = null;
					Thread.sleep(retryInterval);
				} catch (InterruptedException e) { // NOSONAR
					logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
				}
			} else {
				logger.debug("Request was successfully executed - " + (i + 1));
				break;
			}
		}
		return response;
	}
	
	
	private Object executeCommandThroughHazelcastCache(String objectName, String attributeName, String methodName, Object[] methodParamValues) {
		logger.debug("serviceName: " + objectName);
		logger.debug("attributeName: " + attributeName);
		logger.debug("methodName: " + methodName);

			try {
				Map<String, Object[]> commands = new HashMap<>();
				commands.put(methodName, methodParamValues);
				HazelcastUtility.getHazelcastUtility().executeCommandInHazelcastCache(commands, HazelcastUtility.getHazelcastKey(ipAddress, port+""));
			} catch (Exception e) {
				errorMessage = "Hazelcast Map Update Failure";
				logger.error(EliteExceptionUtils.getRootCauseStackTraceAsString(e));
				if (e.getMessage() != null
						&& "ENVIRONMENT_VARIABLE_NOT_SET".equals(e.getMessage())) {
					errorMessage = BaseConstants.SERVER_HOME_NOT_SET;
				}
			}
		return new Boolean(true);	
	}
}