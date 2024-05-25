package com.elitecore.sm.agent.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;


import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.common.dao.GenericDAO;

/**
 * 
 * @author avani.panchal
 *
 */
public interface ServicePacketStatsConfigDao extends GenericDAO<ServicePacketStatsConfig>{
	
	public ServicePacketStatsConfig getServiceDetailByServiceIdAndAgentId(int serviceId,int agentId);

	public Map<String, Object> getPacketStatServicePaginatedList(int agentId);

	public List<ServicePacketStatsConfig> getPacketStatServicePaginatedList(Class<ServicePacketStatsConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);

	public ServicePacketStatsConfig getPacketStatService(int packetStatasticServiceId);

}
