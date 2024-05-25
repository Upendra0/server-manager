package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVSyncServer.
 *
 * @author brijesh soni
 * August 3, 2017
 */
@Component(value = "nfvsyncserver")
public class NFVSyncServer{
	
	/** The ip address. */
	@NotNull(message = "ipAddress required")
	private String ipAddress;
	
	private int port;
	
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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}