package com.elitecore.sm.nfv.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.service.NetflowClientService;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.nfv.commons.license.GenerateKeysUtility;
import com.elitecore.sm.nfv.commons.license.LicenceGenerator;
import com.elitecore.sm.nfv.model.NFVAddServer;
import com.elitecore.sm.nfv.model.NFVClient;
import com.elitecore.sm.nfv.model.NFVCopyServer;
import com.elitecore.sm.nfv.model.NFVImportServer;
import com.elitecore.sm.nfv.model.NFVLicense;
import com.elitecore.sm.nfv.model.NFVServiceType;
import com.elitecore.sm.nfv.model.NFVSyncServer;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author brijesh.soni
 * August 08, 2017
 *
 */
@org.springframework.stereotype.Service(value = "nfvService")
public class NFVServiceImpl implements NFVService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private LicenseService licenservice;
	
	@Autowired
	private LicenceGenerator licenceGenerator;  // to get class path location
	
	@Autowired
	ServletContext servletContext;  // to get class path location
	
	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	@Autowired(required=true)
	@Qualifier(value="serverService")
	private ServerService serverService;
	
	@Autowired(required = true)
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired
	private NetflowClientService netflowClientService;
	
	private int port = 4434;
	private String serviceInstanceId = "000";
	
	private int maxRetryCount = 5;
	private long delayTimeInSeconds = 5;
	
	@Override
	public ResponseObject activateEngineFullLicense(NFVLicense license) {
		ResponseObject responseObject = new ResponseObject();
		try{
			// JMX CALL TO GET NEW SERVER DETAIL - MACID and HOSTNAME
			Map<String, String> newServerlicenseDetail = licenservice.serverIdDetails(license.getServerInstanceIP(), port);
			if(newServerlicenseDetail != null && newServerlicenseDetail.get(GenerateKeysUtility.MACID) != null && 
					newServerlicenseDetail.get(GenerateKeysUtility.HOSTNAME) != null){
				
				//JMX CALL TO GET ALL LICENSE DETAIL FROM OLD SERVER 
				Map<String, String> licenseDetailMap = licenservice.licenseFullInfo(license.getCopyFromIP(), port);
				
				if(licenseDetailMap != null){
					licenseDetailMap.put(GenerateKeysUtility.MACID, newServerlicenseDetail.get(GenerateKeysUtility.MACID));
					licenseDetailMap.put(GenerateKeysUtility.HOSTNAME, newServerlicenseDetail.get(GenerateKeysUtility.HOSTNAME));
					//GENERATE LICENSE AND GET LICENSE BYTES
					byte[] licenseBytes = licenceGenerator.createLicense(licenseDetailMap);
					if(licenseBytes != null){
						ServerInstance serverInstance = null;
						responseObject = serverInstanceService.getServerInstanceByIPAndPort(license.getServerInstanceIP(), port);
						if (responseObject.isSuccess()) {
							logger.info("Server instance details found successfully!");
							serverInstance= (ServerInstance) responseObject.getObject();
						}
						if(serverInstance != null){
							//APPLY LICENSE TO NEW SERVER
							responseObject = licenservice.applyEngineFullLicense(licenseBytes, serverInstance.getId());
						}
					}else{
						logger.error("Error while applying creation license for IP " + license.getServerInstanceIP());
						responseObject.setSuccess(false);
						responseObject.setResponseCodeNFV(NFVResponseCode.LICENSE_CREATION_FAIL);
					}
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCodeNFV(NFVResponseCode.GET_LICENSE_DETAILS_FAIL);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(NFVResponseCode.GET_SERVER_DETAILS_FAIL);
			}
			
		} catch (IOException e) {
			logger.error("Error while applying engine license to IP " + license.getServerInstanceIP());
			logger.trace(e);
		}catch(Exception e){
			logger.info("Error while appling engine license for IP " + license.getServerInstanceIP());
			logger.trace(e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject addServer(NFVAddServer serverModel) {
		ResponseObject responseObject = null;
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		try{
			Server server = new Server();
			ServerType serverType = new ServerType();
			serverType.setId(serverModel.getServerType());
			server.setServerId("000");
			server.setName(serverModel.getIpAddress()+"_"+serverModel.getUtilityPort());
			server.setDescription(serverModel.getIpAddress());
			server.setIpAddress(serverModel.getIpAddress());
			server.setServerType(serverType);
			server.setUtilityPort(serverModel.getUtilityPort());
			//server.setContainerEnvironment(serverModel.isCotainerEnvironment());
			responseObject = serverService.addServer(server);
			if((responseObject.getResponseCode() != null && responseObject.isSuccess()) ||
					responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER) ||
					responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER_SAME_TYPE) || responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER_SAME_TYPE_CONTAINER)){
				
				responseObject = serverService.getServerByIpAndTypeAndUtility(serverModel.getIpAddress(), serverModel.getServerType(),serverModel.getUtilityPort());
				if (responseObject.isSuccess()) {
					logger.info("Server details found successfully!");
					server = (Server) responseObject.getObject();
					server.setUtilityPort(serverModel.getUtilityPort());
					logger.info("Server Added Successfully");
				}else{
					logger.info("Server details not found!");
				}
			}
		}catch(Exception e){
			logger.info("Error while adding server for IP " + serverModel.getIpAddress());
			logger.trace(e.getMessage(),e);
		}
		return responseObject;
	}
	
	
	public ResponseObject addServerInstance(NFVAddServer serverModel) {
		ResponseObject responseObject = null;
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		try{
			Server server = new Server();
			ServerType serverType = new ServerType();
			serverType.setId(serverModel.getServerType());
			server.setServerId("000");
			server.setName(serverModel.getIpAddress()+"_"+serverModel.getUtilityPort());
			server.setDescription(serverModel.getIpAddress());
			server.setIpAddress(serverModel.getIpAddress());
			server.setServerType(serverType);
			server.setUtilityPort(serverModel.getUtilityPort());
			//server.setContainerEnvironment(serverModel.isCotainerEnvironment());
			responseObject = serverService.addServer(server);
			if((responseObject.getResponseCode() != null && responseObject.isSuccess()) ||
					responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER) ||
					responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER_SAME_TYPE) || responseObject.getResponseCode().equals(ResponseCode.DUPLICATE_SERVER_SAME_TYPE_CONTAINER)){
				
				responseObject = serverService.getServerByIpAndTypeAndUtility(serverModel.getIpAddress(), serverModel.getServerType(),serverModel.getUtilityPort());
				if (responseObject.isSuccess()) {
					logger.info("Server details found successfully!");
					server = (Server) responseObject.getObject();
					server.setUtilityPort(serverModel.getUtilityPort());
					logger.info("Server Added Successfully");
					
					ServerInstance serverInstance = new ServerInstance();
					serverInstance.setName(serverModel.getIpAddress()+":"+port+":"+serverModel.getServerType());
					serverInstance.setServer(server);
					serverInstance.setPort(port);
					
					//allocate min max from copy from instance data here
					ResponseObject ServerInstnaceCopyFromObj = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getCopyFromIp(),Integer.parseInt(serverModel.getCopyFromPort()) );
					ServerInstance serverinstanceObj=(ServerInstance) ServerInstnaceCopyFromObj.getObject();
					
					if(serverinstanceObj!=null){
					  serverInstance.setMinMemoryAllocation(serverinstanceObj.getMinMemoryAllocation());
					  serverInstance.setMaxMemoryAllocation(serverinstanceObj.getMaxMemoryAllocation());
					}
					
					serverInstance.setScriptName("startServer_"+port+".sh");
					serverInstance.setMaxConnectionRetry(3);
					serverInstance.setRetryInterval(5000);
					serverInstance.setConnectionTimeout(20);
					
					Map<String,Object> mParam = new HashMap<>();
					mParam.put("copyFromId","");
					mParam.put("exportPath",servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT));
					mParam.put("xmlPath",servletContext.getRealPath(BaseConstants.JAXB_XML_PATH));
					mParam.put("importFileName","");
					mParam.put("activeAgentList", "");
					
					responseObject = serverInstanceService.addServerInstance(serverInstance,"",mParam);
					
					if(responseObject.isSuccess() && responseObject.getResponseCodeNFV().equals(NFVResponseCode.SERVER_INSTANCE_INACTIVE_INSERT_SUCCESS)) {
						RemoteJMXHelper serverInstanceRemoteJMXCall = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(),
								serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(),
								serverInstance.getConnectionTimeout());
						for(int i = 0; i < maxRetryCount; i++) {
							if (serverInstanceRemoteJMXCall.checkPortRunningOfServerInstance()) {
								responseObject = serverInstanceService.updateStatus(serverInstance.getId());
								return responseObject;
							} 
							Thread.sleep(delayTimeInSeconds * 1000);
						}
						responseObject.setSuccess(false);
					}
				}else{
					logger.info("Server details not found!");
				}
			}
		}catch(Exception e){
			logger.info("Error while adding server for IP " + serverModel.getIpAddress());
			logger.trace(e.getMessage(),e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject checkStatus(NFVAddServer serverModel) {
		ResponseObject responseObject = null;
		ResponseObject responseObjectStatus = null;
		ResponseObject ServerInstnaceObj = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIpAddress(),serverModel.getPort());
		if(ServerInstnaceObj.isSuccess() && ServerInstnaceObj.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) ServerInstnaceObj.getObject();
			responseObject= serverInstanceService.loadInstanceStatus(serverInstance.getId());
			if(responseObject.isSuccess()){
				try {
					responseObjectStatus = serverInstanceService.updateStatus(serverInstance.getId());
					logger.info("===== UpdateStatus of ServerInstance =======" + responseObjectStatus.isSuccess());
				} catch (SMException e) {
					logger.trace(e);
				}
			}
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject syncServer(NFVSyncServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIpAddress(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) responseObject.getObject();

			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
			String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
			
			Map<String,String> syncInputMap = new HashMap<>();
			
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, String.valueOf(serverInstance.getId()));
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, "");
			syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
			syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
			syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
			
			Map<String, ResponseObject> responseMap = serverInstanceService.syncServerInstance(syncInputMap);
			if(responseMap != null) {
				for(Map.Entry<String, ResponseObject> response : responseMap.entrySet()) {
					responseObject = response.getValue();
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(NFVResponseCode.UNABLE_TO_SYNC_SERVER_INSTANCE);
			}
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject copyServer(NFVCopyServer serverModel, int staffId) throws SMException {
		ServerInstance fromServerInstance = null;
		ServerInstance toServerInstance = null;
		if(serverModel.getCopyFromPort()==null){
			serverModel.setCopyFromPort(Integer.toString(port));
		}
		if(serverModel.getCopyToPort()==null){
			serverModel.setCopyToPort(Integer.toString(port));
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getCopyFromIp(),Integer.parseInt(serverModel.getCopyFromPort()) );
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			fromServerInstance = (ServerInstance) responseObject.getObject();
		}
		responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getCopyToIp(), Integer.parseInt(serverModel.getCopyToPort()));
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			toServerInstance = (ServerInstance) responseObject.getObject();
		}
		if(responseObject.isSuccess() && fromServerInstance != null && toServerInstance != null) {
			String tempPathForExport = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			
			Map<String, ResponseObject> responseMap = serverInstanceService.copyServerInstanceConfig(fromServerInstance.getId(), 
					String.valueOf(toServerInstance.getId()), staffId, tempPathForExport, jaxbXmlPath);
			
			if(responseMap != null) {
				for(Map.Entry<String, ResponseObject> response : responseMap.entrySet()) {
					responseObject = response.getValue();
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(NFVResponseCode.SERVER_INSTANCE_COPY_CONFIG_FAIL);
			}
		}
		if(fromServerInstance==null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCodeNFV(NFVResponseCode.FROM_IP_PORT_NOT_AVAILABLE);
		}else if(toServerInstance==null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCodeNFV(NFVResponseCode.TO_IP_PORT_NOT_AVAILABLE);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject restartServer(NFVSyncServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIpAddress(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) responseObject.getObject();
			responseObject = serverInstanceService.restartInstance(serverInstance.getId());
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject importServer(NFVImportServer serverModel, int staffId) throws SMException {
		ServerInstance serverInstance = null;
		if(serverModel.getPort()!=null){
			port=Integer.parseInt(serverModel.getPort());
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIp(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			serverInstance = (ServerInstance) responseObject.getObject();
		}	
		ResponseObject responseImportInstance = null;
		String tempPath;
		if(serverModel.getDatabase()==null)
			tempPath=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator+"serverExport.xml";
		else
			tempPath=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator+"serverExport_"+serverModel.getDatabase()+".xml";			
		File importFile = new File(tempPath);	
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		if(serverInstance!=null) {
			responseImportInstance = serverInstanceService.importServerInstanceConfig(serverInstance.getId(), importFile,staffId,2,jaxbXmlPath,false);
		}
		if (responseImportInstance!=null && responseImportInstance.isSuccess()) {
			logger.debug("Import operation performed successfully");
			responseImportInstance.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_IMPORT_SUCCESS);
		} else {
			logger.debug("Import operation Fail");
			if (responseImportInstance!=null && !(responseImportInstance.getResponseCode() == ResponseCode.SERVICE_RUNNING)) {
				responseImportInstance.setResponseCodeNFV(NFVResponseCode.SERVERINSTANCE_IMPORT_FAIL);
			}
		}	
		return responseImportInstance;
	}
	
	@Override
	public ResponseObject deleteServerInstance(NFVAddServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ServerInstance serverInstanceId=serverInstanceService.getIDByIpPortUtility(serverModel.getIpAddress(),serverModel.getPort(),serverModel.getUtilityPort());
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		ResponseObject responseObject = null;
		try {
			responseObject = serverInstanceService.deleteServerInstance(serverInstanceId.getId(),0,jaxbXmlPath);
		} catch (SMException e) {
			logger.error(e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject deleteServerInstanceOnlyInSM(NFVAddServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ServerInstance serverInstanceId=serverInstanceService.getIDByIpPortUtility(serverModel.getIpAddress(),serverModel.getPort(),serverModel.getUtilityPort());
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		ResponseObject responseObject = null;
		try {
			responseObject = serverInstanceService.deleteServerInstanceOnlyInSM(serverInstanceId.getId(),0,jaxbXmlPath);
		} catch (SMException e) {
			logger.error(e);
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject deleteServer(NFVAddServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ResponseObject responseObject = null;
		responseObject = serverService.getServerByIpAndTypeAndUtility(serverModel.getIpAddress(), serverModel.getServerType(),serverModel.getUtilityPort());
		Server si=(Server) responseObject.getObject();
		responseObject = serverService.deleteServer(si.getId(),0);
		return responseObject;
	}
	
	@Override
	public ResponseObject startServer(NFVSyncServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIpAddress(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) responseObject.getObject();
			responseObject = serverInstanceService.startInstance(serverInstance.getId());
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject stopServer(NFVSyncServer serverModel) {
		if(serverModel.getPort()!=0){
			port=serverModel.getPort();
		}
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(serverModel.getIpAddress(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) responseObject.getObject();
			responseObject = serverInstanceService.stopInstance(serverInstance.getId());
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject addClient(NFVClient client) {
		ResponseObject responseObject = serverInstanceService.getServerInstanceByIPAndPort(client.getServerIpAddress(), port);
		if(responseObject.isSuccess() && responseObject.getObject() instanceof ServerInstance) {
			ServerInstance serverInstance = (ServerInstance) responseObject.getObject();
			Service service = getServiceForNetflowClient(client, serverInstance.getId());
			if(service != null) {
				NetflowClient dbClient = netflowClientService.getClientByIpAndPort(client.getClientIpAddress(), client.getClientPort(), service.getId());
				if(dbClient == null) {
					NetflowClient cloneNetflowClient = cloneNetflowClient(client, service);
					if(cloneNetflowClient != null) {
						responseObject = netflowClientService.addCollectionClient(cloneNetflowClient,null);
					} else {
						responseObject.setSuccess(false);
						responseObject.setResponseCodeNFV(NFVResponseCode.NO_CLIENT_FOUND_IN_SERVICE);
					}
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCodeNFV(NFVResponseCode.CLIENT_ALREADY_EXISTS);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(NFVResponseCode.NO_SERVICE_FOUND_IN_SERVER);
			}
		}
		return responseObject;
	}
	
	public Service getServiceForNetflowClient(NFVClient client, int serverInstanceId) {
		String serviceAlias;
		if(client.getServiceType().equalsIgnoreCase(NFVServiceType.NATFLOW.toString())) {
			serviceAlias = EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE;
		} else if(client.getServiceType().equalsIgnoreCase(NFVServiceType.GTP.toString())) {
			serviceAlias = EngineConstants.GTPPRIME_COLLECTION_SERVICE;
		} else if(client.getServiceType().equalsIgnoreCase(NFVServiceType.SYSLOG.toString())) {
			serviceAlias = EngineConstants.SYSLOG_COLLECTION_SERVICE;
		} else {
			return null;
		}
		return servicesService.getServiceListByIDAndTypeAlias(serviceAlias, serviceInstanceId, serverInstanceId);
	}
	
	@Transactional
	public NetflowClient cloneNetflowClient(NFVClient client, Service service) {
		NetflowClient newClient = null;
		List<NetflowClient> clientList = null;
		NetflowBinaryCollectionService netflowBinaryCollectionService = null;
		if(service instanceof GTPPrimeCollectionService) {
			netflowBinaryCollectionService = (GTPPrimeCollectionService) service;
			Hibernate.initialize(netflowBinaryCollectionService.getNetFLowClientList());
			clientList = netflowBinaryCollectionService.getNetFLowClientList();
		} else if(service instanceof SysLogCollectionService) {
			netflowBinaryCollectionService = (SysLogCollectionService) service;
			Hibernate.initialize(netflowBinaryCollectionService.getNetFLowClientList());
			clientList = netflowBinaryCollectionService.getNetFLowClientList();
		} else if(service instanceof NetflowBinaryCollectionService) {
			netflowBinaryCollectionService = (NetflowBinaryCollectionService) service;
			Hibernate.initialize(netflowBinaryCollectionService.getNetFLowClientList());
			clientList = netflowBinaryCollectionService.getNetFLowClientList();
		}
		if(!CollectionUtils.isEmpty(clientList)) {
			List<BaseModel> baseModels = EliteUtils.getActiveListFromGivenList(clientList);
			if(!CollectionUtils.isEmpty(baseModels) && baseModels.get(0) instanceof NetflowClient) {
				newClient = (NetflowClient) ((NetflowClient) baseModels.get(0)).clone();
				newClient.setName(client.getClientIpAddress()+":"+client.getClientPort());
				newClient.setClientIpAddress(client.getClientIpAddress());
				newClient.setClientPort(client.getClientPort());
				newClient.setService(netflowBinaryCollectionService);
				return newClient;
			}
		}
		return newClient;
	}
}