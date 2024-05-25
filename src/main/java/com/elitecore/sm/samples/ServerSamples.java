/**
 * 
 */
package com.elitecore.sm.samples;

import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;

/**
 * @author Sunil Gulabani
 * Jul 3, 2015
 */
public class ServerSamples {
	
	private ServerService serverService;
	
	public void addMediationServerType() {
		ServerType serverType = new ServerType();
		serverType.setName("Mediation");
		serverType.setDescription("Mediation Server Type");
		serverType.setAlias("MEDIATION");
		serverService.addServerType(serverType);
	}
	
	public void addIPLMSServerType() {
		ServerType serverType = new ServerType();
		serverType.setName("IPLMS");
		serverType.setDescription("IPLMS Server Type");
		serverType.setAlias("IPLMS");
		serverService.addServerType(serverType);
	}
	
	public void addCGFServerType() {
		ServerType serverType = new ServerType();
		serverType.setName("CGF");
		serverType.setDescription("CGF Server Type");
		serverType.setAlias("CGF");
		serverService.addServerType(serverType);
	}

	public ServerService getServerService() {
		return serverService;
	}

	public void setServerService(ServerService serverService) {
		this.serverService = serverService;
	}
}