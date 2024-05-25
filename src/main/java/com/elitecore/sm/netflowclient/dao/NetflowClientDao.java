package com.elitecore.sm.netflowclient.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.netflowclient.model.NetflowClient;


public interface NetflowClientDao extends GenericDAO<NetflowClient>  {

	public List<NetflowClient> getClientListForService(int serviceId);
	
	public List<NetflowClient> getClientListByName(String clientName, int serviceId);
	
	public NetflowClient getNetflowClientById(int clientId);
	
	public int getClientCount(String clientName, int serviceId);
	
	public NetflowClient getClientByIpAndPort(String ipAddress, int port, int serviceId);
	
	public List<NetflowClient> getClientListByKafkaDSId(int kafkaDSId, int startIndex,int limit);
	
	public int getClientCountByKafkaDSId(int kafkaDSId);
	
}
