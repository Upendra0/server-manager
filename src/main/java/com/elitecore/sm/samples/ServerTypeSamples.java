package com.elitecore.sm.samples;

import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;

/**
 * @author vishal.lakhyani
 *
 */
public class ServerTypeSamples {
	
	/**
	 * @param serverservice : this method will add all servertype to database 
	 */
	
	@Autowired
	ServerService serverService;
	
	public void addServerType(ServerService serverservice){
		
		ServerType servertype1 = new ServerType();
		servertype1.setAlias("MEDIATION");
		servertype1.setName("Mediation");
		servertype1.setDescription("Mediation Server Type");
		serverService.addServerType(servertype1);
		
		ServerType servertype2 = new ServerType();
		servertype2.setAlias("IPLMS");
		servertype2.setName("IPLMS");
		servertype2.setDescription("IPLMS Server Type");
		serverService.addServerType(servertype2);
		
		ServerType servertype3 = new ServerType();
		servertype3.setAlias("CGF");
		servertype3.setName("CGF");
		servertype3.setDescription("CGF Server Type");
		serverService.addServerType(servertype3);
	}
}
