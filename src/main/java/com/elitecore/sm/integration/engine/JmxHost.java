/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.elitecore.sm.integration.engine;

import com.elitecore.sm.common.constants.BaseConstants;

/**
 * This class will keep the server connection related information, 
 * @author Ranjitsinh Reval
 */
public class JmxHost {
	private String host;
	private int port;
	protected int maxConnectionRetry = BaseConstants.SERVER_MGMT_TRY_TO_CONNECT;
	protected int retryInterval = BaseConstants.SERVER_CONNECTION_INTERVAL;
	protected int connectionTimeout = BaseConstants.SERVER_MGMT_CONNECTION_TIMEOUT;
	
	public JmxHost(String host,int port, int maxConnectionRetry, int retryInterval, int connectionTimeout){
		this.host = host;
		this.port = port;
		this.maxConnectionRetry = maxConnectionRetry;
		this.retryInterval = retryInterval ;
		this.connectionTimeout = connectionTimeout ;
	}
	/**
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}
	/**
	 * 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}
	/**
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * @return the maxConnectionRetry
	 */
	public int getMaxConnectionRetry() {
		return maxConnectionRetry;
	}
	
	/**
	 * @return the retryInterval
	 */
	public int getRetryInterval() {
		return retryInterval;
	}
	
	/**
	 * @return the connectionTimeout
	 */
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	
	/**
	 * @param maxConnectionRetry the maxConnectionRetry to set
	 */
	public void setMaxConnectionRetry(int maxConnectionRetry) {
		this.maxConnectionRetry = maxConnectionRetry;
	}
	
	/**
	 * @param retryInterval the retryInterval to set
	 */
	public void setRetryInterval(int retryInterval) {
		this.retryInterval = retryInterval;
	}
	
	/**
	 * @param connectionTimeout the connectionTimeout to set
	 */
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
}
