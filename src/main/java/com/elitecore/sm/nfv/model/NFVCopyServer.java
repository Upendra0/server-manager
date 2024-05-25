package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVCopyServer.
 *
 * @author brijesh soni
 * August 3, 2017
 */
@Component(value = "nfvcopyserver")
public class NFVCopyServer{
	
	@NotNull(message = "copyFromIp required")
	private String copyFromIp;
	
	@NotNull(message = "copyToIp required")
	private String copyToIp;
	
	@NotNull(message = "copyFromPort required")
	private String copyFromPort;
	
	@NotNull(message = "copyToPort required")
	private String copyToPort;
	
	public String getCopyFromIp() {
		return copyFromIp;
	}
	
	public void setCopyFromIp(String copyFromIp) {
		this.copyFromIp = copyFromIp;
	}
	
	public String getCopyToIp() {
		return copyToIp;
	}
	
	public void setCopyToIp(String copyToIp) {
		this.copyToIp = copyToIp;
	}

	public String getCopyFromPort() {
		return copyFromPort;
	}

	public void setCopyFromPort(String copyFromPort) {
		this.copyFromPort = copyFromPort;
	}

	public String getCopyToPort() {
		return copyToPort;
	}

	public void setCopyToPort(String copyToPort) {
		this.copyToPort = copyToPort;
	}
	
}