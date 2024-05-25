package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVClient.
 *
 * @author brijesh soni
 * August 9, 2017
 */
@Component(value = "nfvclient")
public class NFVClient {
	
	@NotNull(message = "clientIpAddress required")
	private String clientIpAddress;
	
	@NotNull(message = "clientPort required")
	private int clientPort = -1;
	
	@NotNull(message = "serverIpAddress required")
	private String serverIpAddress;
	
	@NotNull(message = "serviceType required")
	private String serviceType;

	public String getClientIpAddress() {
		return clientIpAddress;
	}
	
	public void setClientIpAddress(String clientIpAddress) {
		this.clientIpAddress = clientIpAddress;
	}
	
	public int getClientPort() {
		return clientPort;
	}
	
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	
	public String getServerIpAddress() {
		return serverIpAddress;
	}

	public void setServerIpAddress(String serverIpAddress) {
		this.serverIpAddress = serverIpAddress;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
}