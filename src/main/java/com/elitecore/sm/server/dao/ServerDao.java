/**
 * 
 */
package com.elitecore.sm.server.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;

/**
 * @author Sunil Gulabani Jul 2, 2015
 */

public interface ServerDao extends GenericDAO<Server> {
	
	public List<Server> getServerListByIpAndTypeAndUtility(String ipAddress, int typeId,int utilityPort);
	
	public ServerType addServerType(ServerType serverType);

	public List<ServerType> getAllServerTypeList();

	public ServerType getServerType(int id);

	public int getServerCount(String name, String ipAddress, int id);

	public List<Server> getServerList();
	
	public List<ServerType> getActiveServerTypeList();

	public int getServerCountByIpAndType(String ipAddress, int typeId, int id);
	
	public int getServerCountByIpAndTypeAndUtilityPort(String ipAddress, int typeId, int id, int port);
	
	public List<Server>  getAllServerByServerType(int serverTypeId);
	
	public Server getServerByServerIpAddress(String ipAddress);

	public List<Server> getServerListByIpAndType(String ipAddress, int typeId);

	public List<Server> getServerListByServerType(int serverType);
	
	public Server getServer(int id);
	
	public Long getContainerServerList(String ipaddr);
	
	public List<String> getGroupServerIDList();

}