package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVAddServer.
 *
 * @author sagar shah
 * July 13, 2017
 */
@Component(value = "nfvaddserver")
public class NFVAddServer{
	
	/** The Constant serialVersionUID. */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 4398110581558834930L;
	
	/** The server type. */
	@NotNull(message = "serverType required")
	private int serverType = -1;  //ALLOWED VALUES : 1 - MEDIATION, 2 - CGF, 3 - IPLMS
	
	/** The ip address. */
	@NotNull(message = "ipAddress required")
	private String ipAddress;
	
	/** The min memory allocation. */
	@NotNull(message = "minMemoryAllocation  required")
	private int minMemoryAllocation = -1;
	
	/** The max memory allocation. */
	@NotNull(message = "maxMemoryAllocation  required")
	private int maxMemoryAllocation = -1;

	private int port;
	
	private int utilityPort;
	
	private boolean isCotainerEnvironment=true; 
	
	private String copyFromIp;

	private String copyFromPort;
    
    
	public String getCopyFromIp() {
		return copyFromIp;
	}

	public void setCopyFromIp(String copyFromIp) {
		this.copyFromIp = copyFromIp;
	}


	public String getCopyFromPort() {
		return copyFromPort;
	}

	public void setCopyFromPort(String copyFromPort) {
		this.copyFromPort = copyFromPort;
	}

	public boolean isCotainerEnvironment() {
		return isCotainerEnvironment;
	}

	public void setCotainerEnvironment(boolean isCotainerEnvironment) {
		this.isCotainerEnvironment = isCotainerEnvironment;
	}

	public int getUtilityPort() {
		return utilityPort;
	}

	public void setUtilityPort(int utilityPort) {
		this.utilityPort = utilityPort;
	}

	/**
	 * Gets the server type.
	 *
	 * @return the server type
	 */
	public int getServerType() {
		return serverType;
	}

	/**
	 * Sets the server type.
	 *
	 * @param serverType the new server type
	 */
	public void setServerType(int serverType) {
		this.serverType = serverType;
	}

	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets the min memory allocation.
	 *
	 * @return the min memory allocation
	 */
	public int getMinMemoryAllocation() {
		return minMemoryAllocation;
	}

	/**
	 * Sets the min memory allocation.
	 *
	 * @param minMemoryAllocation the new min memory allocation
	 */
	public void setMinMemoryAllocation(int minMemoryAllocation) {
		this.minMemoryAllocation = minMemoryAllocation;
	}

	/**
	 * Gets the max memory allocation.
	 *
	 * @return the max memory allocation
	 */
	public int getMaxMemoryAllocation() {
		return maxMemoryAllocation;
	}

	/**
	 * Sets the max memory allocation.
	 *
	 * @param maxMemoryAllocation the new max memory allocation
	 */
	public void setMaxMemoryAllocation(int maxMemoryAllocation) {
		this.maxMemoryAllocation = maxMemoryAllocation;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
