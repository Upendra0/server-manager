package com.elitecore.sm.netflowclient.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.netflowclient.dao.ProxyClientConfigurationDAO;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.util.EliteUtils;

@Service
public class ProxyClientConfigurationServiceImpl implements ProxyClientConfigurationService {

	@Autowired
	private ProxyClientConfigurationDAO proxyClientConfigurationDAO;
	
	@Autowired
	private ServicesDao servicesDao;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	@Transactional(readOnly = true)
	public ResponseObject getAllProxyClientByServiceId(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		 JSONObject proxyJsonObject ;
		JSONArray proxyArray = new JSONArray();
		
		List<NatFlowProxyClient> natFlowProxyClients = proxyClientConfigurationDAO.getAllProxyClientByServiceId(serviceId);
		
		if(natFlowProxyClients != null && !natFlowProxyClients.isEmpty()){
			for (NatFlowProxyClient natFlowProxyClient : natFlowProxyClients) {
				proxyJsonObject = new JSONObject();
				proxyJsonObject.put("id", natFlowProxyClient.getId());
				proxyJsonObject.put("proxyIp", natFlowProxyClient.getProxyIp());
				proxyJsonObject.put("proxyPort", natFlowProxyClient.getProxyPort());
				proxyArray.put(proxyJsonObject);
			}
			responseObject.setObject(proxyArray);
			responseObject.setSuccess(true);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject addProxyClientParams(NatFlowProxyClient natFlowProxyClient) {
		logger.debug("Inside:addCharRenameOperationParams  adding new charcater rename operation params.");
	
		ResponseObject responseObject = new ResponseObject() ;
		
		if(natFlowProxyClient.getService() != null && natFlowProxyClient.getService().getId() > 0){
			
			com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(com.elitecore.sm.services.model.Service.class, natFlowProxyClient.getService().getId());
			if(service != null){
				if(service instanceof NetflowCollectionService)
					natFlowProxyClient.setService((NetflowCollectionService)service);
				else if(service instanceof NetflowBinaryCollectionService)
					natFlowProxyClient.setService((NetflowBinaryCollectionService)service);
				service.setSyncStatus(true);
				servicesDao.merge(service);
				proxyClientConfigurationDAO.save(natFlowProxyClient);
				
				if(natFlowProxyClient.getId() > 0){
					logger.info("Char rename operation parameters created successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(natFlowProxyClient);
					responseObject.setResponseCode(ResponseCode.CLIENT_ADD_SUCCESS);
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CLIENT_ADD_FAIL);
					responseObject.setResponseCodeNFV(NFVResponseCode.ADD_CLIENT_FAIL);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CLIENT_ADD_FAIL_SERV_UNAVAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CLIENT_ADD_FAIL_SERV_UNAVAIL);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public long getProxyClientCount(NatFlowProxyClient client) {
		
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		conditions.add(Restrictions.eq("proxyIp",client.getProxyIp()));
		conditions.add(Restrictions.eq("proxyPort",client.getProxyPort()));
		aliases.put("service", "s");
		conditions.add(Restrictions.eq("s.id",client.getService().getId()));
		return proxyClientConfigurationDAO.getQueryCount(NatFlowProxyClient.class,conditions,aliases);
	}

	@Override
	@Transactional
	public ResponseObject deleteProxyClient(int clientId) {
		ResponseObject responseObject = new ResponseObject();
		NatFlowProxyClient client = proxyClientConfigurationDAO.getProxyClientById(clientId);
		if(client != null) {
			client.setStatus(StateEnum.DELETED);
			com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(com.elitecore.sm.services.model.Service.class, client.getService().getId());
			service.setSyncStatus(true);
			servicesDao.merge(service);
			proxyClientConfigurationDAO.merge(client);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CLIENT_DELETE_SUCCESS);
			responseObject.setObject(client);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject updateProxyClient(NatFlowProxyClient natFlowProxyClient) {
		ResponseObject responseObject = new ResponseObject();
		boolean isUniqueProxyClientForUpdate = proxyClientConfigurationDAO.isUniqueProxyClientForUpdate(natFlowProxyClient);
		if(isUniqueProxyClientForUpdate ) {
			proxyClientConfigurationDAO.merge(natFlowProxyClient);
			com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(com.elitecore.sm.services.model.Service.class, natFlowProxyClient.getService().getId());
			service.setSyncStatus(true);
			servicesDao.merge(service);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.NETFLOW_CLIENT_UPDATE_SUCCESS);
			responseObject.setObject(natFlowProxyClient);
		}else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_PROXY_CLIENT);
		}
		return responseObject;
	}

	@Override
	@Transactional(readOnly=true)
	public void iterateServiceClientDetails(NetflowBinaryCollectionService service, boolean isImport) {
		ResponseObject responseObject = new ResponseObject();
		
		if(service != null ){
			List<NatFlowProxyClient> clients = service.getNatFlowProxyClients();
			
			if (clients != null && !clients.isEmpty()) {
				NatFlowProxyClient client ;
				for (int i = 0, size = clients.size(); i < size; i++) {
					client = clients.get(i);
					if(isImport){
						client.setId(0);
						//client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,client.getName()));
						client.setCreatedByStaffId(service.getLastUpdatedByStaffId());
						client.setCreatedDate(new Date());
						client.setService(service);
					}else{
						//client.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,client.getName()));
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
	public void importProxyClientAddAndKeepBothMode(NetflowBinaryCollectionService exportedService, int importMode) {
		if(exportedService != null && exportedService.getNatFlowProxyClients() != null) {
			List<NatFlowProxyClient> natFlowProxyClients = exportedService.getNatFlowProxyClients();
			for(NatFlowProxyClient natFlowProxyClient : natFlowProxyClients) {
				if(natFlowProxyClient.getStatus().equals(StateEnum.ACTIVE)) {
					natFlowProxyClient.setId(0);
					natFlowProxyClient.setCreatedByStaffId(exportedService.getLastUpdatedByStaffId());
					natFlowProxyClient.setCreatedDate(EliteUtils.getDateForImport(false));					
					natFlowProxyClient.setService(exportedService);
				}
			}
		}
	}
}
