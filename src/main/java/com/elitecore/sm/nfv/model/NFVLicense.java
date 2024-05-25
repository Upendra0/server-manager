package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVLicense.
 *
 * @author sagar shah
 * July 13, 2017
 */
@Component(value = "nfvlicense")
public class NFVLicense{
	
	/** The Constant serialVersionUID. */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 4398110581558834930L;
	
	/** The server instance IP. */
	@NotNull(message = "serverInstanceIP required")
	private String serverInstanceIP;
	
	/** The host name. */
	@NotNull(message = "hostName required")
	private String hostName;

	/** The copy from IP. */
	@NotNull(message = "copyFromIP required")
	private String copyFromIP;
	
	/**
	 * Gets the server instance IP.
	 *
	 * @return the server instance IP
	 */
	public String getServerInstanceIP() {
		return serverInstanceIP;
	}
	
	/**
	 * Sets the server instance IP.
	 *
	 * @param serverInstanceIP the new server instance IP
	 */
	public void setServerInstanceIP(String serverInstanceIP) {
		this.serverInstanceIP = serverInstanceIP;
	}
	
	/**
	 * Gets the host name.
	 *
	 * @return the host name
	 */
	public String getHostName() {
		return hostName;
	}
	
	/**
	 * Sets the host name.
	 *
	 * @param hostName the new host name
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	/**
	 * Gets the copy from IP.
	 *
	 * @return the copy from IP
	 */
	public String getCopyFromIP() {
		return copyFromIP;
	}
	
	/**
	 * Sets the copy from IP.
	 *
	 * @param copyFromIP the new copy from IP
	 */
	public void setCopyFromIP(String copyFromIP) {
		this.copyFromIP = copyFromIP;
	}
}
