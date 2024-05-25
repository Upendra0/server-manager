package com.elitecore.sm.netflowclient.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;


public interface NetflowClientService {

	public long getTotalClientCount(int serviceId);

	public List<NetflowClient> getPaginatedList(int startIndex, int limit, String sidx, String sord,int serviceId);
	
	public ResponseObject getClientListForService(int serviceId);
	
	public ResponseObject updateNetflowClient(NetflowClient netfloeClient,String serviceType);
	
	public ResponseObject addCollectionClient(NetflowClient client,String serviceType);
	
	public ResponseObject deleteClientDetails(int clientId);
	
	public ResponseObject updtClientStatus(int clientId,String status);
	
	public List<ImportValidationErrors> validateClientForImport(NetflowClient netFlowClient,List<ImportValidationErrors> clientImportErrorList);
	
	public void  iterateServiceClientDetails(NetflowBinaryCollectionService service,boolean isImport);
	
	public void importNetflowClientForAddAndKeepBothMode(NetflowClient client, NetflowBinaryCollectionService service, int importMode);
	
	public void importNetflowClientForUpdateMode(NetflowClient dbClient, NetflowClient exportedClient);
	
	public void importServiceClientAddAndKeepBothMode(NetflowBinaryCollectionService exportedService, int importMode);
	
	public void importServiceClientUpdateMode(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService, int importMode);
	
	public NetflowClient getClientFromList(List<NetflowClient> clientList, String clientName);
	
	public NetflowClient getClientByIpAndPort(String ipAddress, int port, int serviceId);
	
}
