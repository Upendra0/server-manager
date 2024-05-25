/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.elitecore.sm.integration.engine;

import java.util.Properties;

import com.elitecore.sm.common.util.SpringApplicationContext;

/**
 * This Exception is thrown when system is not able to establish connection to a
 * server.
 *
 */
public class ConnectionNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private static final Properties properties = (Properties) SpringApplicationContext.getBean("jmxConnectionPool"); // getting spring bean for aop context issue.
	private final StringBuilder message = new StringBuilder(properties.getProperty(ApplicationProperty.CONN_NOT_ESTABLISHED));

	public ConnectionNotFoundException() {
		super();
	}

	/**
	 *
	 * @param host
	 * @param port
	 */
	public ConnectionNotFoundException(String host, String port) {
		super();
		this.message.append(host);
		this.message.append(ApplicationProperty.COLON);
		this.message.append(port);
	}

	/**
	 *
	 * @param cause
	 */
	public ConnectionNotFoundException(Throwable cause) {
		super(cause);
	}

	@Override
	public String toString() {
		return message.toString();
	}

	@Override
	public String getMessage() {
		return message.toString();
	}
}
