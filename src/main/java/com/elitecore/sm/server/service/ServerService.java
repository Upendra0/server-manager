/**
 * 
 */
package com.elitecore.sm.server.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;

/**
 * @author Sunil Gulabani Jul 2, 2015
 */
public interface ServerService {
	
	public ResponseObject getServerByIpAndTypeAndUtility(String ipAddress, int serverType,int utilityPort);
	
	public ServerType addServerType(ServerType serverType);

	public List<ServerType> getAllServerTypeList();

	public ServerType getServerType(int id);

	public ResponseObject addServer(Server server);

	public Server getServer(int id);

	public List<Server> getServerList();

	public ResponseObject updateServer(Server server);

	public ResponseObject deleteServer(int serverId,int staffId);
	
	public List<ServerType> getActiveServerTypeList() ;

	public ResponseObject deleteServerCheck(int serverId);
	
	public ResponseObject getServerByIpAddress(String ipAddress);
	
	public ResponseObject getServerByIpAndType(String ipAddress, int serverType);

	public ResponseObject getServerListByServerType(int serverType);
	
	public ResponseObject getServerByHostAddress(String ipAddress);
	
	public ResponseObject getListOfGroupIds();

}