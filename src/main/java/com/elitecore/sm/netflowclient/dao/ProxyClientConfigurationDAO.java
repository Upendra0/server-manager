package com.elitecore.sm.netflowclient.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;

public interface ProxyClientConfigurationDAO extends GenericDAO<NatFlowProxyClient>{
	
	public List<NatFlowProxyClient> getAllProxyClientByServiceId(int serviceId);

	public NatFlowProxyClient getProxyClientById(int clientId);

	public boolean isUniqueProxyClientForUpdate(NatFlowProxyClient natFlowProxyClient);

	public NatFlowProxyClient getProxyClient(NatFlowProxyClient client);

}
