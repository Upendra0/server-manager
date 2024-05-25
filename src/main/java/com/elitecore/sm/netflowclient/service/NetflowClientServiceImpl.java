package com.elitecore.sm.netflowclient.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.kafka.datasource.service.KafkaDataSourceService;
import com.elitecore.sm.netflowclient.dao.NetflowClientDao;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.validator.NetflowClientValidator;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

@org.springframework.stereotype.Service(value = "netfloewClientService")
public class NetflowClientServiceImpl implements NetflowClientService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	@Autowired
	NetflowClientDao clientDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ServicesDao serviceDao;
	
	@Autowired
	NetflowClientValidator netFlowClientValidator;
	
	@Autowired
	NetflowClientDao netflowClientDao;
	
	@Autowired
	KafkaDataSourceService kafkaDataSourceService;
	 
	/**
	 * @param serviceId
	 * @return Total netflow client count for service
	 */
	@Override
	@Transactional
	public long getTotalClientCount(int serviceId) {
		
		HashMap<String, String> aliases = new HashMap<>();
		
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		
		aliases.put("service", "s");
		conditions.add(Restrictions.eq("s.id",serviceId));
		
		return clientDao.getQueryCount(NetflowClient.class,conditions,aliases);
		
	}

	/**
	 * Get Paginated Netflow client list
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @param serviceId
	 * @return List of paginated netflow client
	 */
	@Override
	@Transactional
	public List<NetflowClient> getPaginatedList(int startIndex, int limit, String sidx,String sord,int serviceId) {
		
		HashMap<String, String> aliases = new HashMap<>();
		
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		
		aliases.put("service", "s");
		conditions.add(Restrictions.eq("s.id",serviceId));

		List<NetflowClient> clientList = clientDao.getPaginatedList(NetflowClient.class,
				conditions, aliases, startIndex,
				limit, sidx, sord);

		return clientList;
	}

	/**
	 * Get Netflow client list by service id
	 * @param serviceId
	 * @return Response object of netflow client list
	 */
	@Override
	@Transactional
	public ResponseObject getClientListForService(int serviceId){
		
		ResponseObject responseobject = new ResponseObject();
		
		List<NetflowClient> clientList = clientDao.getClientListForService(serviceId);
		responseobject.setSuccess(true);
		responseobject.setObject(clientList);
		
		return responseobject;
	}
	
	/**
	 * Update Netflow client in database
	 * @param netfloeClient
	 * @return Update netflow client response as response object
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_CLIENT,actionType = BaseConstants.UPDATE_ACTION, currentEntity = NetflowClient.class, ignorePropList= "")
	public ResponseObject updateNetflowClient(NetflowClient netfloeClient,String serviceType) {
		ResponseObject responseObject = new ResponseObject();

		if (isClientUniqueForUpdate(netfloeClient.getId(), netfloeClient.getName(), netfloeClient.getService().getId()) || serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE)) {
			
			NetflowClient netflowClient = clientDao.getNetflowClientById(netfloeClient.getId());
			
			logger.debug("Service Id ="+netflowClient.getService().getId());
			
			netflowClient.setName(netfloeClient.getName());
			netflowClient.setClientIpAddress(netfloeClient.getClientIpAddress());
			netflowClient.setClientPort(netfloeClient.getClientPort());
			netflowClient.setOutFileLocation(netfloeClient.getOutFileLocation());
			netflowClient.setBkpBinaryfileLocation(netfloeClient.getBkpBinaryfileLocation());
			netflowClient.setFileNameFormat(netfloeClient.getFileNameFormat());
			netflowClient.setAppendFileSequenceInFileName(netfloeClient.isAppendFileSequenceInFileName());
			netflowClient.setAppendFilePaddingInFileName(netfloeClient.isAppendFilePaddingInFileName());
			netflowClient.setMinFileSeqValue(netfloeClient.getMinFileSeqValue());
			netflowClient.setMaxFileSeqValue(netfloeClient.getMaxFileSeqValue());
			netflowClient.setTimeLogRollingUnit(netfloeClient.getTimeLogRollingUnit());
			netflowClient.setVolLogRollingUnit(netfloeClient.getVolLogRollingUnit());
			netflowClient.setInputCompressed(netfloeClient.isInputCompressed());
			netflowClient.setStatus(netfloeClient.getStatus());
			netflowClient.setSnmpAlertEnable(netfloeClient.isSnmpAlertEnable());
			netflowClient.setAlertInterval(netfloeClient.getAlertInterval());
			netflowClient.setRollingType(netfloeClient.getRollingType());
			netflowClient.setNodeAliveRequest(netfloeClient.isNodeAliveRequest());
			netflowClient.setEchoRequest(netfloeClient.getEchoRequest());
			netflowClient.setRequestExpiryTime(netfloeClient.getRequestExpiryTime());
			netflowClient.setRequestRetry(netfloeClient.getRequestRetry());
			netflowClient.setRequestBufferCount(netfloeClient.getRequestBufferCount());
			netflowClient.setRedirectionIp(netfloeClient.getRedirectionIp());
			netflowClient.setSharedSecretKey(netfloeClient.getSharedSecretKey());
			netflowClient.setTopicName(netfloeClient.getTopicName());
			netflowClient.setResourcesName(netfloeClient.getResourcesName());
			netflowClient.setRegisterObserver(netfloeClient.isRegisterObserver());
			netflowClient.setObserverTimeout(netfloeClient.getObserverTimeout());
			netflowClient.setRequestType(netfloeClient.getRequestType());
			netflowClient.setMessageType(netfloeClient.getMessageType());
			netflowClient.setRequestTimeout(netfloeClient.getRequestTimeout());
			netflowClient.setRequestRetryCount(netfloeClient.getRequestRetryCount());
			netflowClient.setReqExecutionInterval(netfloeClient.getReqExecutionInterval());
			netflowClient.setReqExecutionFreq(netfloeClient.getReqExecutionFreq());
			netflowClient.setExchangeLifeTime(netfloeClient.getExchangeLifeTime());
			netflowClient.setContentFormat(netfloeClient.getContentFormat());
			netflowClient.setPayload(netfloeClient.getPayload());
			netflowClient.setEnableSecurity(netfloeClient.isEnableSecurity());
			netflowClient.setSecurityType(netfloeClient.getSecurityType());
			netflowClient.setSecurityIdentity(netfloeClient.getSecurityIdentity());
			netflowClient.setSecurityKey(netfloeClient.getSecurityKey());
			netflowClient.setSecCerLocation(netfloeClient.getSecCerLocation());
			netflowClient.setSecCerPasswd(netfloeClient.getSecCerPasswd());
			netflowClient.setEnableProxy(netfloeClient.isEnableProxy());
			netflowClient.setProxyResources(netfloeClient.getProxyResources());
			netflowClient.setProxySchema(netfloeClient.getProxySchema());
			netflowClient.setProxyServerIp(netfloeClient.getProxyServerIp());
			netflowClient.setProxyServerPort(netfloeClient.getProxyServerPort());
			netflowClient.setJsonValidate(netfloeClient.isJsonValidate());
			netflowClient.setContentType(netfloeClient.getContentType());
			netflowClient.setUri(netfloeClient.getUri());
			netflowClient.setEnableKafka(netfloeClient.isEnableKafka());
			netflowClient.setKafkaDataSourceConfig(netfloeClient.getKafkaDataSourceConfig());
			
			clientDao.merge(netflowClient);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.NETFLOW_CLIENT_UPDATE_SUCCESS);
			responseObject.setObject(netflowClient);
			
		} else {
			//logger.debug("inside updateCollectionDriver : duplicate driver name found in update:" + netfloeClient.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_NETFLOW_CLIENT_NAME);
		}
		return responseObject;
	}
	
	/**
	 * Check client name is unique in case of update 
	 * @param clientId
	 * @param clientName
	 * @return
	 */
	private boolean  isClientUniqueForUpdate(int clientId,String clientName, int serviceId){
		List<NetflowClient> clientList= clientDao.getClientListByName(clientName,serviceId);
		boolean isUnique=false;
		if(clientList!=null && !clientList.isEmpty()){
			
			for(NetflowClient client:clientList){
				//If ID is same , then it is same client object
				if(clientId == (client.getId())){
					isUnique=true;
				}else{ // It is another client object , but name is same
					isUnique=false;
				}
			}
		}else if(clientList!=null && clientList.isEmpty()){ // No client found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	/**
	 * Add Netflow Client in database
	 * @param NetflowClient
	 * @return ResponseObject
	 */
	@Override
	@Transactional()
	@Auditable(auditActivity = AuditConstants.CREATE_CLIENT,actionType = BaseConstants.CREATE_ACTION, currentEntity = NetflowClient.class, ignorePropList= "")
	public ResponseObject addCollectionClient(NetflowClient client,String serviceType){
	
		ResponseObject responseObject = new ResponseObject();

		int clientCount = clientDao.getClientCount(client.getName(),client.getService().getId());
		if (clientCount > 0 && (serviceType!=null &&!serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE))) {
			//logger.debug("inside addCollectionDriver : duplicate driver name found:" + client.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_CLIENT_NAME);
		} else {
			Service service = serviceDao.findByPrimaryKey(Service.class,client.getService().getId());

			if (service != null) {
				if(service instanceof NetflowCollectionService)
					client.setService((NetflowCollectionService)service);
				else if(service instanceof NetflowBinaryCollectionService)
					client.setService((NetflowBinaryCollectionService)service);

				clientDao.save(client);
				
				if(client.getId() !=0){
					responseObject.setSuccess(true);
					responseObject.setObject(client);
					responseObject.setResponseCode(ResponseCode.CLIENT_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CLIENT_ADD_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.ADD_CLIENT_FAIL);
				}
			} else {
				// add client fail
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CLIENT_ADD_FAIL_SERV_UNAVAIL);
			}
		}
		return responseObject;
	}	
	
	/**
	 * Method will delete netflow client detail from database
	 * @param clientId
	 * @return Delete netflow client response as response object
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CLIENT,actionType = BaseConstants.DELETE_ACTION, currentEntity = NetflowClient.class, ignorePropList= "")
	public ResponseObject deleteClientDetails(int clientId) {
		ResponseObject responseObject = new ResponseObject();
		
		NetflowClient client = clientDao.getNetflowClientById(clientId);
		
		if(client != null){
			if(!client.getStatus().equals(StateEnum.ACTIVE) || client.getService().getSvctype().getAlias().equals(EngineConstants.RADIUS_COLLECTION_SERVICE)){
				client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,client.getName()));
				client.setStatus(StateEnum.DELETED);
				clientDao.merge(client);
				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.CLIENT_DELETE_SUCCESS);
				responseObject.setObject(client);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CLIENT_ACTIVE_DELETE_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CLIENT_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	/**
	 * update netflow client status in database
	 * @param clientId
	 * @param status
	 * @return Update netflow client status response as response object
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_CLIENT_STATUS,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = NetflowClient.class, ignorePropList= "")
	public ResponseObject updtClientStatus(int clientId,String status){
		ResponseObject responseObject = new ResponseObject();
		
		NetflowClient client = clientDao.getNetflowClientById(clientId);
		
		if(client != null){
			if((StateEnum.ACTIVE.name().equals(status)))
				client.setStatus(StateEnum.ACTIVE);
			else
				client.setStatus(StateEnum.INACTIVE);
			
			clientDao.merge(client);
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.NETFLOW_CLIENT_UPDATE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CLIENT_ID_UNAVALIABLE);
		}
		return responseObject;
	}
	
	/**
	 * Validate client parameter for import operation
	 * @param netFlowClient
	 * @param clientImportErrorList
	 * @return
	 */
	@Override
	public List<ImportValidationErrors> validateClientForImport(NetflowClient netFlowClient,List<ImportValidationErrors> clientImportErrorList){
		
		logger.debug("Validate Netflow client");
		netFlowClientValidator.validateNetflowClient(netFlowClient, null, null, clientImportErrorList, BaseConstants.NETFLOW_CLIENT, true);
		return clientImportErrorList;
	}
	
	/**
	 * Iterate over netflow client , change id and name for import operation
	 * @param service
	 * @return ResponseObject
	 */
	@Transactional(readOnly=true)
	public void  iterateServiceClientDetails(NetflowBinaryCollectionService service,boolean isImport){
		ResponseObject responseObject = new ResponseObject();
		
		if(service != null ){
			List<NetflowClient> clients = service.getNetFLowClientList();
			
			if (clients != null && !clients.isEmpty()) {
				NetflowClient client ;
				for (int i = 0, size = clients.size(); i < size; i++) {
					client = clients.get(i);
					if(isImport){
						client.setId(0);
						client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,client.getName()));
						client.setCreatedByStaffId(service.getLastUpdatedByStaffId());
						client.setCreatedDate(new Date());
						client.setService(service);
					}else{
						client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,client.getName()));
						client.setStatus(StateEnum.DELETED);
					}
						
						client.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
						client.setLastUpdatedDate(new Date());
						
						responseObject.setSuccess(true);
						responseObject.setObject(client);
				}
			}else{
				logger.debug("Client not configured for service " + service.getId());
				responseObject.setSuccess(true);
				responseObject.setObject(service);
			}
		}
	}
	
	@Override
	public void importServiceClientAddAndKeepBothMode(NetflowBinaryCollectionService exportedService, int importMode) {
		if(exportedService != null ){
			List<NetflowClient> exportedClientList = exportedService.getNetFLowClientList();
			if (!CollectionUtils.isEmpty(exportedClientList)) {
				int clientLength = exportedClientList.size();
				for(int i = clientLength-1; i >= 0; i--) {
					NetflowClient exportedClient = exportedClientList.get(i);
					if(exportedClient != null && !StateEnum.DELETED.equals(exportedClient.getStatus())) {
						if(exportedClient.isEnableKafka() && exportedClient.getKafkaDataSourceConfig()!=null) {
							kafkaDataSourceService.createKafkaDataSourceForImport(exportedClient.getKafkaDataSourceConfig(), exportedService);
						}
						importNetflowClientForAddAndKeepBothMode(exportedClient, exportedService, importMode);
					}
				}
			}
		}
		
	}
	
	@Override
	public void importServiceClientUpdateMode(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService, int importMode) {
		if(exportedService != null ){
			List<NetflowClient> dbClientList = dbService.getNetFLowClientList();
			List<NetflowClient> exportedClientList = exportedService.getNetFLowClientList();
			if (exportedClientList != null && !exportedClientList.isEmpty()) {
				int clientLength = exportedClientList.size();
				for(int i = clientLength-1; i >= 0; i--) {
					NetflowClient exportedClient = exportedClientList.get(i);
					if(exportedClient != null) {
						NetflowClient dbClient = getClientFromList(dbClientList, exportedClient.getName());
						if(dbClient != null && importMode!=BaseConstants.IMPORT_MODE_ADD) {
							//update client
							importNetflowClientForUpdateMode(dbClient, exportedClient);
							dbClientList.add(dbClient);
						} else if((dbClient == null && importMode==BaseConstants.IMPORT_MODE_ADD) || importMode == BaseConstants.IMPORT_MODE_OVERWRITE){
							//add client
							if(exportedClient.isEnableKafka() && exportedClient.getKafkaDataSourceConfig()!=null) {
								kafkaDataSourceService.createKafkaDataSourceForImport(exportedClient.getKafkaDataSourceConfig(), exportedService);
							}
							importNetflowClientForAddAndKeepBothMode(exportedClient, dbService, importMode);
							dbClientList.add(exportedClient);
						} 
						
					}
				}
			}
		}
	}
	
	@Override
	public void importNetflowClientForUpdateMode(NetflowClient dbClient, NetflowClient exportedClient) {
		dbClient.setStatus(exportedClient.getStatus()); 
		dbClient.setClientIpAddress(exportedClient.getClientIpAddress());
		dbClient.setClientPort(exportedClient.getClientPort());
		dbClient.setFileNameFormat(exportedClient.getFileNameFormat());
		dbClient.setAppendFileSequenceInFileName(exportedClient.isAppendFileSequenceInFileName());
		dbClient.setAppendFilePaddingInFileName(exportedClient.isAppendFilePaddingInFileName());
		dbClient.setMinFileSeqValue(exportedClient.getMinFileSeqValue());
		dbClient.setMaxFileSeqValue(exportedClient.getMaxFileSeqValue());
		dbClient.setOutFileLocation(exportedClient.getOutFileLocation());
		dbClient.setVolLogRollingUnit(exportedClient.getVolLogRollingUnit());
		dbClient.setTimeLogRollingUnit(exportedClient.getTimeLogRollingUnit());
		dbClient.setInputCompressed(exportedClient.isInputCompressed());
		dbClient.setBkpBinaryfileLocation(exportedClient.getBkpBinaryfileLocation());
		dbClient.setSnmpAlertEnable(exportedClient.isSnmpAlertEnable());
		dbClient.setAlertInterval(exportedClient.getAlertInterval());
		dbClient.setRollingType(exportedClient.getRollingType());
		dbClient.setNodeAliveRequest(exportedClient.isNodeAliveRequest());
		dbClient.setEchoRequest(exportedClient.getEchoRequest());
		dbClient.setRequestExpiryTime(exportedClient.getRequestExpiryTime());
		dbClient.setRequestRetry(exportedClient.getRequestRetry());
		dbClient.setRedirectionIp(exportedClient.getRedirectionIp());
		dbClient.setSharedSecretKey(exportedClient.getSharedSecretKey());
		dbClient.setRequestBufferCount(exportedClient.getRequestBufferCount());
		dbClient.setTopicName(exportedClient.getTopicName());
		dbClient.setResourcesName(exportedClient.getResourcesName());
		dbClient.setRegisterObserver(exportedClient.isRegisterObserver());
		dbClient.setObserverTimeout(exportedClient.getObserverTimeout());
		dbClient.setRequestType(exportedClient.getRequestType());
		dbClient.setMessageType(exportedClient.getMessageType());
		dbClient.setRequestTimeout(exportedClient.getRequestTimeout());
		dbClient.setRequestRetryCount(exportedClient.getRequestRetryCount());
		dbClient.setReqExecutionInterval(exportedClient.getReqExecutionInterval());
		dbClient.setReqExecutionFreq(exportedClient.getReqExecutionFreq());
		dbClient.setExchangeLifeTime(exportedClient.getExchangeLifeTime());
		dbClient.setContentFormat(exportedClient.getContentFormat());
		dbClient.setPayload(exportedClient.getPayload());
		dbClient.setEnableSecurity(exportedClient.isEnableSecurity());
		dbClient.setSecurityType(exportedClient.getSecurityType());
		dbClient.setSecurityIdentity(exportedClient.getSecurityIdentity());
		dbClient.setSecurityKey(exportedClient.getSecurityKey());
		dbClient.setSecCerLocation(exportedClient.getSecCerLocation());
		dbClient.setSecCerPasswd(exportedClient.getSecCerPasswd());
		dbClient.setEnableProxy(exportedClient.isEnableProxy());
		dbClient.setProxyResources(exportedClient.getProxyResources());
		dbClient.setProxySchema(exportedClient.getProxySchema());
		dbClient.setProxyServerIp(exportedClient.getProxyServerIp());
		dbClient.setProxyServerPort(exportedClient.getProxyServerPort());
		dbClient.setJsonValidate(exportedClient.isJsonValidate());
		dbClient.setContentType(exportedClient.getContentType());
		dbClient.setUri(exportedClient.getUri());
		dbClient.setEnableKafka(exportedClient.isEnableKafka());
		dbClient.setKafkaDataSourceConfig(exportedClient.getKafkaDataSourceConfig());
		dbClient.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	@Override
	public void importNetflowClientForAddAndKeepBothMode(NetflowClient client, NetflowBinaryCollectionService service, int importMode) {
		client.setId(0);
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,client.getName()));
		}
		client.setCreatedByStaffId(service.getLastUpdatedByStaffId());
		client.setCreatedDate(EliteUtils.getDateForImport(false));
		client.setService(service);
	}
	
	@Override
	public NetflowClient getClientFromList(List<NetflowClient> clientList, String clientName) {
		if(!CollectionUtils.isEmpty(clientList)) {
			int length = clientList.size();
			for(int i = length-1; i >= 0; i--) {
				NetflowClient client = clientList.get(i);
				if(client != null && !client.getStatus().equals(StateEnum.DELETED) && client.getName().equalsIgnoreCase(clientName)) {
					return clientList.remove(i);
				}
			}
		}
		return null;
	}
	
	@Override
	@Transactional
	public NetflowClient getClientByIpAndPort(String ipAddress, int port, int serviceId) {
		return clientDao.getClientByIpAndPort(ipAddress, port, serviceId);
	}
	
}