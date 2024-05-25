package com.elitecore.sm.nfv.model;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotNull;

/**
 * The Class NFVCopyServer.
 *
 * @author brijesh soni
 * August 3, 2017
 */
@Component(value = "nfvimportserver")
public class NFVImportServer{
	
	@NotNull(message = "ip required")
	private String ip;
	
	@NotNull(message = "port required")
	private String port;
	
	private String database;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
}