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
import com.elitecore.sm.migration.model.SyslogCollectionServiceEntity;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "syslogCollectionServiceMigration")
public class SyslogCollectionServiceMigrationImpl implements SyslogCollectionServiceMigration{

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
	public ResponseObject iterateAndSetSyslogCollectionServiceDetails(List<Service> serviceList, int position, 
			Service service, String directoryPath, ServerInstance serverInstance, String migrationPrefix) throws MigrationSMException {
		this.position = position;
		this.migrationPrefix = migrationPrefix;
		this.serverInstance = serverInstance;
		this.service = service;
		return executeSyslogCollectionService(serviceList);
	}
	
	@SuppressWarnings("unchecked")
	private ResponseObject executeSyslogCollectionService( List<Service> serviceList) throws MigrationSMException {
		SysLogCollectionService syslogCollectionService;
		
		String serviceKey = EngineConstants.SYSLOG_COLLECTION_SERVICE + "-"
				+ this.service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();

			responseObject = migrationUtil.getAndValidateMappingEntityObj(MigrationConstants.SYSLOG_COLLECTION_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					Map<String, Object> mapOfObjects = (Map<String, Object>) responseObject.getObject();
					Object smObject = mapOfObjects.get(MAP_SM_CLASS_KEY);
					Object jaxbObject = mapOfObjects.get(MAP_JAXB_CLASS_KEY);

					if (smObject instanceof SysLogCollectionService
							&& jaxbObject instanceof SyslogCollectionServiceEntity) {
						syslogCollectionService = (SysLogCollectionService) smObject;
						
						
							syslogCollectionService = loadClientsInSyslogCollectionService(syslogCollectionService, (SyslogCollectionServiceEntity) jaxbObject);
						
							if(syslogCollectionService != null){
								syslogCollectionService.setName(migrationUtil.getRandomName(this.service.getName()));
								syslogCollectionService.setServInstanceId(this.service.getServInstanceId());
								ResponseObject serviceTypeObject = servicesService.getServiceTypeByAlias(EngineConstants.SYSLOG_COLLECTION_SERVICE);
								if (serviceTypeObject.isSuccess()) {
									ServiceType serviceType = (ServiceType) serviceTypeObject.getObject();
									syslogCollectionService.setSvctype(serviceType);
								}
								syslogCollectionService.setServerInstance(this.serverInstance);
								serviceList.set(this.position, syslogCollectionService);
								responseObject.setObject(syslogCollectionService);
								responseObject.setSuccess(true);
								displaySyslogCollectionService(syslogCollectionService);
							}
					}
				}
			}

		}
		return responseObject;
	}
	
	private SysLogCollectionService loadClientsInSyslogCollectionService( SysLogCollectionService smSyslogCollectionService,
			SyslogCollectionServiceEntity jaxbSyslogCollectionService) throws MigrationSMException {
		
		if (jaxbSyslogCollectionService != null
				&& jaxbSyslogCollectionService.getClients() != null
				&& !jaxbSyslogCollectionService.getClients().getClient().isEmpty()) {
			
			List<SyslogCollectionServiceEntity.Clients.Client> clients = jaxbSyslogCollectionService.getClients().getClient();
			
			int length = clients.size();
			List<NetflowClient> netflowClientList = new ArrayList<>();
			Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.netflowclient.model.NetflowClient");
			
			for(int i = length - 1; i >= 0; i--) {
				ResponseObject responseObject = migrationUtil.convertJaxbToSMObject( clients.get(i), destinationClass);
				
				if (responseObject.isSuccess() && responseObject.getObject() instanceof NetflowClient) {
					NetflowClient natflowClient = (NetflowClient) responseObject.getObject();
					natflowClient.setName(migrationUtil.getRandomName(this.migrationPrefix + "_NATFLOW_CLIENT"));
					natflowClient.setService(smSyslogCollectionService);
					netflowClientList.add(natflowClient);
				}
			}
			smSyslogCollectionService.setNetFLowClientList(netflowClientList);
			return smSyslogCollectionService;

		}
		return null;
	}
	
	public void displaySyslogCollectionService(SysLogCollectionService service) {
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
			logger.info("appendFileSequenceInFileName : " + client.isAppendFileSequenceInFileName());
			logger.info("minFileSeqValue : " + client.getMinFileSeqValue());
			logger.info("maxFileSeqValue : " + client.getMaxFileSeqValue());
			logger.info("outFileLocation : " + client.getOutFileLocation());
			logger.info("bkpBinaryfileLocation : " + client.getBkpBinaryfileLocation());
			logger.info("timeLogRollingUnit : " + client.getTimeLogRollingUnit());
			logger.info("volLogRollingUnit : " + client.getVolLogRollingUnit());
			logger.info("inputCompressed : " + client.isInputCompressed());
			logger.info("alertInterval : " + client.getAlertInterval());
			logger.info("snmpAlertEnable : " + client.isSnmpAlertEnable());
		}
	}
}
