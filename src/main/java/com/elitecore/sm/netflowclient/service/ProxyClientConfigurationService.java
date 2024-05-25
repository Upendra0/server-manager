package com.elitecore.sm.netflowclient.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;

public interface ProxyClientConfigurationService {
	
	public ResponseObject getAllProxyClientByServiceId(int serviceId);

	public ResponseObject addProxyClientParams(NatFlowProxyClient natFlowProxyClient);

	public ResponseObject deleteProxyClient(int clientId);

	public ResponseObject updateProxyClient(NatFlowProxyClient natFlowProxyClient);
	
	long getProxyClientCount(NatFlowProxyClient client);

	public void iterateServiceClientDetails(NetflowBinaryCollectionService exportedService, boolean isImport);

	public void importProxyClientAddAndKeepBothMode(NetflowBinaryCollectionService exportedService, int importMode);
	
}
