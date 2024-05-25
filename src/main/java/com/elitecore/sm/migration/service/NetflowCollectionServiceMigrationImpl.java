package com.elitecore.sm.migration.service;

import static com.elitecore.sm.common.constants.MigrationConstants.MAP_JAXB_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.MAP_SM_CLASS_KEY;
import static com.elitecore.sm.common.constants.MigrationConstants.NATFLOW_COLLECTION_SERVICE_XML;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.model.NetflowCollectionServiceEntity;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MigrationUtil;

@org.springframework.stereotype.Service(value = "netflowCollectionServiceMigration")
public class NetflowCollectionServiceMigrationImpl implements NetflowCollectionServiceMigration {

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
	public ResponseObject iterateAndSetNetflowCollectionServiceDetails(List<Service> serviceList, int position, Service service, String directoryPath, ServerInstance serverInstance, String migrationPrefix) throws MigrationSMException {
		this.position = position;
		this.migrationPrefix = migrationPrefix;
		this.serverInstance = serverInstance;
		this.service = service;
		
		return executeNatflowCollectionService(serviceList);
	}

	@SuppressWarnings("unchecked")
	private ResponseObject executeNatflowCollectionService(List<Service> serviceList) throws MigrationSMException {
		NetflowCollectionService natflowCollectionService;
		String serviceKey = EngineConstants.NATFLOW_COLLECTION_SERVICE + "-"
				+ this.service.getServInstanceId();
		ResponseObject responseObject = migrationUtil.getFileContentFromMap(serviceKey);
		if (responseObject.isSuccess()) {
			byte[] fileContent = (byte[]) responseObject.getObject();
			responseObject = migrationUtil.getAndValidateMappingEntityObj(NATFLOW_COLLECTION_SERVICE_XML);
			if (responseObject.isSuccess()) {
				MigrationEntityMapping entityMapping = (MigrationEntityMapping) responseObject.getObject();
				responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent, entityMapping.getXsdName(), entityMapping.getXmlName());
				if (responseObject.isSuccess()) {
					Map<String, Object> mapOfObjects = (Map<String, Object>) responseObject.getObject();
					Object smObject = mapOfObjects.get(MAP_SM_CLASS_KEY);
					Object jaxbObject = mapOfObjects.get(MAP_JAXB_CLASS_KEY);
					if (smObject instanceof NetflowCollectionService
							&& jaxbObject instanceof NetflowCollectionServiceEntity) {
						natflowCollectionService = (NetflowCollectionService) smObject;

						natflowCollectionService = loadClientsInNetflowCollectionService(natflowCollectionService, (NetflowCollectionServiceEntity) jaxbObject);

						if(natflowCollectionService != null) {
							natflowCollectionService.setName(migrationUtil.getRandomName(this.service.getName()));
							natflowCollectionService.setServInstanceId(this.service.getServInstanceId());
							ResponseObject serviceTypeObject = servicesService.getServiceTypeByAlias(EngineConstants.NATFLOW_COLLECTION_SERVICE);
							if (serviceTypeObject.isSuccess()) {
								ServiceType serviceType = (ServiceType) serviceTypeObject.getObject();
								natflowCollectionService.setSvctype(serviceType);
							}
							natflowCollectionService.setServerInstance(this.serverInstance);
							migrationUtil.setCurrentDateAndStaffId(natflowCollectionService, 1);
							serviceList.set(this.position, natflowCollectionService);
							responseObject.setObject(natflowCollectionService);
							responseObject.setSuccess(true);
							displayNatflowCollectionService(natflowCollectionService);
						}
					}
				}
			}

		}
		return responseObject;
	}

	/**
	 * Method will get convert client object from jaxb to sm object.
	 * @param smNatflowCollectionService
	 * @param jaxbNatflowCollectionService
	 * @return
	 * @throws MigrationSMException
	 */
	private NetflowCollectionService loadClientsInNetflowCollectionService(NetflowCollectionService smNatflowCollectionService, NetflowCollectionServiceEntity jaxbNatflowCollectionService) throws MigrationSMException {
		if (jaxbNatflowCollectionService != null
				&& jaxbNatflowCollectionService.getClients() != null
				&& !jaxbNatflowCollectionService.getClients().getClient().isEmpty()) {
			List<NetflowCollectionServiceEntity.Clients.Client> clients = jaxbNatflowCollectionService.getClients().getClient();
			int length = clients.size();
			List<NetflowClient> netflowClientList = new ArrayList<>();
			Class<?> destinationClass = migrationUtil.getFullClassName("com.elitecore.sm.netflowclient.model.NetflowClient");
			for (int i = length - 1; i >= 0; i--) {
				ResponseObject responseObject = migrationUtil.convertJaxbToSMObject(clients.get(i), destinationClass);
				if (responseObject.isSuccess()
						&& responseObject.getObject() instanceof NetflowClient) {
					NetflowClient natflowClient = (NetflowClient) responseObject.getObject();
					natflowClient.setName(migrationUtil.getRandomName(this.migrationPrefix+ "_NATFLOW_CLIENT"));
					natflowClient.setService(smNatflowCollectionService);
					netflowClientList.add(natflowClient);
				}
			}
			smNatflowCollectionService.setNetFLowClientList(netflowClientList);
			return smNatflowCollectionService;

		}
		return null;
	}

	public void displayNatflowCollectionService(NetflowCollectionService service) {
		logger.info("serverIp : " + service.getServerIp());
		logger.info("netflowPort : " + service.getNetFlowPort());
		logger.info("startup-mode : " + service.getSvcExecParams().getStartupMode());
		logger.info("svcExecParams.queueSize : "
				+ service.getSvcExecParams().getQueueSize());
		logger.info("svcExecParams.minThread : "
				+ service.getSvcExecParams().getMinThread());
		logger.info("svcExecParams.maxThread : "
				+ service.getSvcExecParams().getMaxThread());
		logger.info("parallelFileWriteCount : " + service.getParallelFileWriteCount());
		logger.info("maxPktSize : " + service.getMaxPktSize());
		logger.info("maxWriteBufferSize : " + service.getMaxWriteBufferSize());
		logger.info("readTemplateOnInit : " + service.isReadTemplateOnInit());
		logger.info("optionTemplateId : " + service.getOptionTemplateId());
		logger.info("optionTemplateKey : " + service.getOptionTemplateKey());
		logger.info("optionTemplateValue : " + service.getOptionTemplateValue());
		logger.info("optionCopytoTemplateId : " + service.getOptionCopytoTemplateId());
		logger.info("optionCopyTofield : " + service.getOptionCopyTofield());
		logger.info("optionTemplateEnable : " + service.isOptionTemplateEnable());
		logger.info("enableParallelBinaryWrite : "
				+ service.isEnableParallelBinaryWrite());
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
			logger.info("bkpBinaryfileLocation : " + client.getBkpBinaryfileLocation());
			logger.info("timeLogRollingUnit : " + client.getTimeLogRollingUnit());
			logger.info("volLogRollingUnit : " + client.getVolLogRollingUnit());
			logger.info("inputCompressed : " + client.isInputCompressed());
			logger.info("alertInterval : " + client.getAlertInterval());
			logger.info("snmpAlertEnable : " + client.isSnmpAlertEnable());
		}
	}

}
