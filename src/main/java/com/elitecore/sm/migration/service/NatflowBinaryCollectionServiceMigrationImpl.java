package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.MAP_JAXB_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_SM_CLASS_KEY;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.NatflowBinaryCollectionServiceEntity;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "natflowBinaryCollectionServiceMigration")
public class NatflowBinaryCollectionServiceMigrationImpl implements NatflowBinaryCollectionServiceMigration{

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private MigrationUtil migrationUtil;

	@Autowired
	private ServicesService servicesService;

	private int position;

	private String migrationPrefix;

	private ServerInstance serverInstance;

	private Service service;

	@Override
	public ResponseObject iterateAndSetNetflowBinaryCollectionServiceDetails(List<Service> serviceList, int position, 
			Service service, String directoryPath, ServerInstance serverInstance, String migrationPrefix) throws MigrationSMException {
		this.position = position;
		this.migrationPrefix = migrationPrefix;
		this.serverInstance = serverInstance;
		this.service = service;
		return executeNatflowBinaryCollectionService(serviceList);
	}
	
	@SuppressWarnings("unchecked")
	private ResponseObject executeNatflowBinaryCollectionService( List<Service> serviceList) throws MigrationSMException {
		NetflowBinaryCollectionService natflowBinaryCollectionService;
		
		String serviceKey = EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE + "-"+this.service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if(responseObject.isSuccess()){
			byte[] fileContent = (byte[]) responseObject.getObject();
			
			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.NATFLOW_BINARY_COLLECTION_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping  entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					
					Map<String, Object> mapOfObjects = (Map<String, Object>) responseObject.getObject();
					Object smObject = mapOfObjects.get(MAP_SM_CLASS_KEY);
					Object jaxbObject = mapOfObjects.get(MAP_JAXB_CLASS_KEY);
					
					if (smObject instanceof NetflowBinaryCollectionService && jaxbObject instanceof NatflowBinaryCollectionServiceEntity) {
						natflowBinaryCollectionService = (NetflowBinaryCollectionService) smObject;
						
						
							natflowBinaryCollectionService = loadClientsInNetflowBinaryCollectionService( natflowBinaryCollectionService,(NatflowBinaryCollectionServiceEntity) jaxbObject);
							
							if(natflowBinaryCollectionService != null){
								natflowBinaryCollectionService.setName(migrationUtil.getRandomName(this.service.getName()));
								natflowBinaryCollectionService.setServInstanceId(this.service .getServInstanceId());

								ResponseObject serviceTypeObject = servicesService.getServiceTypeByAlias(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE);
								if (serviceTypeObject.isSuccess()) {
									ServiceType serviceType = (ServiceType) serviceTypeObject.getObject();
									natflowBinaryCollectionService.setSvctype(serviceType);
								}
								
								natflowBinaryCollectionService.setServerInstance(this.serverInstance);
								serviceList.set(this.position, natflowBinaryCollectionService);
								responseObject.setObject(natflowBinaryCollectionService);
								responseObject.setSuccess(true);
								
								displayNatflowCollectionService(natflowBinaryCollectionService);
							}
							
							
					}
				}
			}
			
		}
		return responseObject;
	}
	
	private NetflowBinaryCollectionService loadClientsInNetflowBinaryCollectionService( NetflowBinaryCollectionService smNatflowBinaryCollectionService,
			NatflowBinaryCollectionServiceEntity jaxbNatflowBinaryCollectionService) throws MigrationSMException {
		
		if (jaxbNatflowBinaryCollectionService != null
				&& jaxbNatflowBinaryCollectionService.getClients() != null
				&& !jaxbNatflowBinaryCollectionService.getClients().getClient().isEmpty()) {

			List<NatflowBinaryCollectionServiceEntity.Clients.Client> clients = jaxbNatflowBinaryCollectionService.getClients().getClient();

			int length = clients.size();
			List<NetflowClient> netflowClientList = new ArrayList<>();
			Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.netflowclient.model.NetflowClient");
			for (int i = length - 1; i >= 0; i--) {
				ResponseObject responseObject = migrationUtil.convertJaxbToSMObject(clients.get(i), destinationClass);

				if (responseObject.isSuccess() && responseObject.getObject() instanceof NetflowClient) {
					NetflowClient natflowClient = (NetflowClient) responseObject.getObject();
					natflowClient.setName(migrationUtil.getRandomName(this.migrationPrefix + "_NATFLOW_CLIENT"));
					natflowClient.setService(smNatflowBinaryCollectionService);
					netflowClientList.add(natflowClient);
				}
			}
			smNatflowBinaryCollectionService.setNetFLowClientList(netflowClientList);
			return smNatflowBinaryCollectionService;

		}
		return null;
	}
	
	public void displayNatflowCollectionService(NetflowBinaryCollectionService service) {
		logger.info("serverIp : " + service.getServerIp());
		logger.info("netflowPort : " + service.getNetFlowPort());
		logger.info("startup-mode : "
				+ service.getSvcExecParams().getStartupMode());

		logger.info("svcExecParams.queueSize : "
				+ service.getSvcExecParams().getQueueSize());
		logger.info("svcExecParams.minThread : "
				+ service.getSvcExecParams().getMinThread());
		logger.info("svcExecParams.maxThread : "
				+ service.getSvcExecParams().getMaxThread());
		logger.info("parallelFileWriteCount : "
				+ service.getParallelFileWriteCount());
		logger.info("maxPktSize : " + service.getMaxPktSize());
		logger.info("maxWriteBufferSize : " + service.getMaxWriteBufferSize());

		List<NetflowClient> clients = service.getNetFLowClientList();
		int i = 0;
		for (NetflowClient client : clients) {
			++i;
			logger.info("Client : " + i);
			logger.info("clientIpAddress : " + client.getClientIpAddress());
			logger.info("clientPort : " + client.getClientPort());
			logger.info("fileNameFormat : " + client.getFileNameFormat());
			logger.info("appendFileSequenceInFileName : "
					+ client.isAppendFileSequenceInFileName());
			logger.info("minFileSeqValue : " + client.getMinFileSeqValue());
			logger.info("maxFileSeqValue : " + client.getMaxFileSeqValue());
			logger.info("outFileLocation : " + client.getOutFileLocation());
			logger.info("bkpBinaryfileLocation : "
					+ client.getBkpBinaryfileLocation());
			logger.info("timeLogRollingUnit : "
					+ client.getTimeLogRollingUnit());
			logger.info("volLogRollingUnit : " + client.getVolLogRollingUnit());
			logger.info("inputCompressed : " + client.isInputCompressed());
			logger.info("alertInterval : " + client.getAlertInterval());
			logger.info("snmpAlertEnable : " + client.isSnmpAlertEnable());
		}
	}
}
