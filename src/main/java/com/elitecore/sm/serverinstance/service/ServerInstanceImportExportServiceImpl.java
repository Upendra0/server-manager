package com.elitecore.sm.serverinstance.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.aggregationservice.service.IAggregationService;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.service.IDataConsolidationService;
import com.elitecore.sm.diameterpeer.service.DiameterPeerService;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.netflowclient.service.NetflowClientService;
import com.elitecore.sm.netflowclient.service.ProxyClientConfigurationService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.dao.ServiceTypeDao;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CoAPCollectionService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.MqttCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceExecutionParams;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServiceEntityImportService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author brijesh.soni
 *
 */
@org.springframework.stereotype.Service(value = "serverInstanceImportExportService")
public class ServerInstanceImportExportServiceImpl implements ServerInstanceImportExportService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ServiceEntityImportService serviceEntityImportService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private ServicesDao servicesDao;
	
	@Autowired
	private DriversService driversService;
	
	@Autowired
	private PathListService pathListService;
	
	@Autowired
	private ServiceTypeDao servicetypeDao;
	
	@Autowired
	private NetflowClientService netflowClientService;
	
	@Autowired
	private DiameterPeerService diameterPeerService;
	
	@Autowired
	private IDataConsolidationService dataConsolidationService;
	
	@Autowired
	private ProxyClientConfigurationService proxyClientConfigurationService; 
	
	@Autowired
	private IAggregationService aggregationService;
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public ResponseObject importServiceAndDepedants(ServerInstance serverInstanceDB, Service exportedService, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("Import service and its depedendants");
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(true);
		
		if (exportedService.getSvcExecParams() == null) {
			exportedService.setSvcExecParams(new ServiceExecutionParams());
		}
		
		Service serviceDb = servicesService.getServiceListByIDAndTypeAlias(exportedService.getSvctype().getAlias(), exportedService.getServInstanceId(), serverInstanceDB.getId());
		
		if (exportedService instanceof NetflowCollectionService
				|| exportedService instanceof SysLogCollectionService
				|| exportedService instanceof MqttCollectionService
				|| exportedService instanceof CoAPCollectionService
				|| exportedService instanceof GTPPrimeCollectionService
				|| exportedService instanceof NetflowBinaryCollectionService) {

			if(isSave(importMode, serviceDb)) {
				return saveOnlineCollectionServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, exportedService.getSvctype().getAlias(), importMode);
			} else if(isUpdate(importMode)) {
				return updateOnlineCollectionServiceForImport(serviceDb, exportedService, importMode);
			}
			
		} else if (exportedService instanceof DiameterCollectionService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveDiameterCollectionServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, exportedService.getSvctype().getAlias(), importMode);
			} else if(isUpdate(importMode)) {
				return updateDiameterCollectionServiceForImport((DiameterCollectionService) serviceDb, (DiameterCollectionService) exportedService, importMode);
			}
			
		} else if (exportedService instanceof CollectionService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveCollectionServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(isUpdate(importMode)) {
				return updateCollectionServiceForImport((CollectionService) serviceDb, (CollectionService) exportedService, importMode);
			}
			
		} else if (exportedService instanceof IPLogParsingService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveIpLogParsingServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			}else if(importMode == BaseConstants.IMPORT_MODE_ADD && serviceDb!=null){
				serviceEntityImportService.importIpLogParsingService((IPLogParsingService) serviceDb, (IPLogParsingService) exportedService, importMode);
			} else if(isUpdate(importMode)) {
				return updateIpLogParsingServiceForImport((IPLogParsingService) serviceDb, (IPLogParsingService) exportedService, importMode);
			}

		} else if (exportedService instanceof ParsingService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveParsingServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(importMode == BaseConstants.IMPORT_MODE_ADD && serviceDb!=null){
				serviceEntityImportService.importParsingService((ParsingService) serviceDb, (ParsingService) exportedService, importMode);
			}else if(isUpdate(importMode)) {
				return updateParsingServiceForImport((ParsingService) serviceDb, (ParsingService) exportedService, importMode);
			}
			
		} else if (exportedService instanceof DistributionService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveDistributionServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(isUpdate(importMode)) {
				return updateDistributionServiceForImport((DistributionService) serviceDb, (DistributionService) exportedService, importMode);
			}
			
		} else if (exportedService instanceof ProcessingService) {
		
			if(isSave(importMode, serviceDb)) {
				return saveProcessingServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(isUpdate(importMode)) {
				return updateProcessingServiceForImport((ProcessingService) serviceDb, (ProcessingService) exportedService, importMode);
			}
			
		} else if(exportedService instanceof DataConsolidationService){
		
			if(isSave(importMode, serviceDb)) {
				return saveDataConsolidationServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(isUpdate(importMode)) {
				return updateDataConsolidationServiceForImport((DataConsolidationService) serviceDb, (DataConsolidationService) exportedService, importMode);
			}
			
		} else if(exportedService instanceof AggregationService){
		
			if(isSave(importMode, serviceDb)) {
				return saveAggregationServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, importMode);
			} else if(isUpdate(importMode)) {
				return updateAggregationServiceForImport((AggregationService) serviceDb, (AggregationService) exportedService, importMode);
			}			
			
		}
		return responseObject;
	}
	
	@Override
	public ResponseObject saveCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save collection service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.COLLECTION_SERVICE, importMode);
		servicesService.importServiceScheduleParamsAddAndKeepBothMode(((CollectionService) exportedService).getServiceSchedulingParams());
		driversService.importServiceDriverAddAndKeepBothMode(exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateCollectionServiceForImport(CollectionService collectionServiceDb, CollectionService exportedCollectionService, int importMode) {
		logger.debug("update collection service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD)
			servicesService.importServiceScheduleParamsUpdateMode(collectionServiceDb.getServiceSchedulingParams(), exportedCollectionService.getServiceSchedulingParams());
		driversService.importServiceDriverUpdateMode(collectionServiceDb, exportedCollectionService, importMode);
		return updateServiceForImport(collectionServiceDb);
	}
	
	@Override
	public ResponseObject saveDistributionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save distribution service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.DISTRIBUTION_SERVICE, importMode);
		servicesService.importServiceScheduleParamsAddAndKeepBothMode(((DistributionService) exportedService).getServiceSchedulingParams());
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((DistributionService)exportedService).getFileGroupingParameter());
		driversService.importServiceDriverAddAndKeepBothMode(exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateDistributionServiceForImport(DistributionService distributionServiceDb, DistributionService exportedDistributionService, int importMode) {
		logger.debug("update distribution service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importDistributionServiceBasicParameterUpdateMode(distributionServiceDb, exportedDistributionService);
			servicesService.importServiceScheduleParamsUpdateMode(distributionServiceDb.getServiceSchedulingParams(), exportedDistributionService.getServiceSchedulingParams());
			servicesService.importFileGroupingParameterUpdateMode(distributionServiceDb.getFileGroupingParameter(), exportedDistributionService.getFileGroupingParameter());
		}
		driversService.importServiceDriverUpdateMode(distributionServiceDb, exportedDistributionService, importMode);
		return updateServiceForImport(distributionServiceDb);
	}
	
	@Override
	public ResponseObject saveParsingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save parsing service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.PARSING_SERVICE, importMode);
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((ParsingService) exportedService).getFileGroupingParameter());
		pathListService.importServicePathListAddAndKeepBothMode(exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateParsingServiceForImport(ParsingService parsingServiceDb, ParsingService exportedParsingService, int importMode) {
		logger.debug("update parsing service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importParsingServiceBasicParameterUpdateMode(parsingServiceDb, exportedParsingService);
			servicesService.importFileGroupingParameterUpdateMode(parsingServiceDb.getFileGroupingParameter(), exportedParsingService.getFileGroupingParameter());
		}
		pathListService.importServicePathListUpdateMode(parsingServiceDb, exportedParsingService, importMode);
		return updateServiceForImport(parsingServiceDb);
	}
	
	@Override
	public ResponseObject saveProcessingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save processing service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.PROCESSING_SERVICE, importMode);
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((ProcessingService)exportedService).getFileGroupingParameter());
		pathListService.importServicePathListAddAndKeepBothMode(exportedService, importMode);
		return saveServiceForImport(exportedService);
	}

	@Override
	public ResponseObject updateProcessingServiceForImport(ProcessingService dbProcessingService, ProcessingService exportedProcessingService, int importMode) {
		logger.debug("update processing service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importProcessingServiceBasicParameterUpdateMode(dbProcessingService, exportedProcessingService);
			servicesService.importFileGroupingParameterUpdateMode(dbProcessingService.getFileGroupingParameter(), exportedProcessingService.getFileGroupingParameter());
		}
		pathListService.importServicePathListUpdateMode(dbProcessingService, exportedProcessingService, importMode);
		return updateServiceForImport(dbProcessingService);
	}
	
	@Override
	public ResponseObject saveOnlineCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, String serviceAlias, int importMode) {
		logger.debug("save online collection service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, serviceAlias, importMode);
		if(exportedService instanceof NetflowBinaryCollectionService) {
			netflowClientService.importServiceClientAddAndKeepBothMode((NetflowBinaryCollectionService) exportedService, importMode);
			proxyClientConfigurationService.importProxyClientAddAndKeepBothMode((NetflowBinaryCollectionService) exportedService, importMode);
			return saveServiceForImport(exportedService);
		} 
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(false);
		return responseObject;
	}
	
	@Override
	public ResponseObject updateOnlineCollectionServiceForImport(Service dbService, Service exportedService, int importMode) {
		logger.debug("update online collection service for import");
		dbService.setLastUpdatedByStaffId(dbService.getServerInstance().getCreatedByStaffId());
		servicesService.importOnlineCollectionServiceBasicParameterForUpdateMode(dbService, exportedService, importMode);
		if(exportedService instanceof NetflowBinaryCollectionService) {
			netflowClientService.importServiceClientUpdateMode((NetflowBinaryCollectionService) dbService, (NetflowBinaryCollectionService) exportedService, importMode);
			return updateServiceForImport(dbService);
		}
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(false);
		return responseObject;
	}
	

	@Override
	public ResponseObject saveDiameterCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, String serviceAlias, int importMode) {
		logger.debug("save diameter collection service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, serviceAlias, importMode);
		diameterPeerService.importServicePeerAddAndKeepBothMode((DiameterCollectionService) exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateDiameterCollectionServiceForImport(Service dbService, Service exportedService, int importMode) {
		logger.debug("update diameter collection service for import");
		dbService.setLastUpdatedByStaffId(dbService.getServerInstance().getCreatedByStaffId());
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importDiameterCollectionServiceBasicParameterForUpdateMode((DiameterCollectionService)dbService, (DiameterCollectionService)exportedService);
		}
		diameterPeerService.importServicePeerUpdateMode((DiameterCollectionService) dbService, (DiameterCollectionService) exportedService, importMode);
		return updateServiceForImport(dbService);
	}
	
	@Override
	public ResponseObject saveIpLogParsingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save ip log parsing service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.IPLOG_PARSING_SERVICE, importMode);
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((IPLogParsingService) exportedService).getFileGroupingParameter());
		servicesService.importServicePartitionParamAddAndKeepBothMode((IPLogParsingService) exportedService);
		pathListService.importServicePathListAddAndKeepBothMode(exportedService, importMode);		
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateIpLogParsingServiceForImport(IPLogParsingService dbService, IPLogParsingService exportedService, int importMode) {
		logger.debug("update ip log parsing service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importIpLogParsingServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
			servicesService.importServicePartitionParamUpdateMode(dbService, exportedService);
		}else {
			servicesService.importServicePartitionParamAddMode(dbService, exportedService);
		}
		pathListService.importServicePathListUpdateMode(dbService, exportedService, importMode);
		return updateServiceForImport(dbService);
	}
	
	@Override
	public ResponseObject saveDataConsolidationServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save data consolidation service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.DATA_CONSOLIDATION_SERVICE, importMode);
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((DataConsolidationService) exportedService).getFileGroupParam());
		pathListService.importServicePathListAddAndKeepBothMode(exportedService, importMode);
		dataConsolidationService.importServiceDataConsolidationAddAndKeepBothMode((DataConsolidationService) exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateDataConsolidationServiceForImport(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode) {
		logger.debug("update data consolidation service for import");
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importDataConsolidationServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupParam(), exportedService.getFileGroupParam());
		}
		dataConsolidationService.importServiceDataConsolidationUpdateMode(dbService, exportedService, importMode);
		pathListService.importServicePathListUpdateMode(dbService, exportedService, importMode);
		return updateServiceForImport(dbService);
	}
	
	@Override
	public ResponseObject saveAggregationServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("save aggregation service for import");
		setBasicParameterInServiceForImport(exportedService, serverInstanceDB, exportedServerInstance, EngineConstants.AGGREGATION_SERVICE, importMode);
		servicesService.importFileGroupingParameterAddAndKeepBothMode(((AggregationService) exportedService).getFileGroupingParameter());
		servicesService.importServiceScheduleParamsAddAndKeepBothMode(((AggregationService) exportedService).getServiceSchedulingParams());
		pathListService.importServicePathListAddAndKeepBothMode(exportedService, importMode);
		aggregationService.importServiceAggregationAddAndKeepBothMode((AggregationService)exportedService, importMode);
		return saveServiceForImport(exportedService);
	}
	
	@Override
	public ResponseObject updateAggregationServiceForImport(AggregationService dbService, AggregationService exportedService, int importMode) {
		logger.debug("update aggregation service for import");		
		if(importMode!=BaseConstants.IMPORT_MODE_ADD) {
			servicesService.importAggreagationServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(),exportedService.getFileGroupingParameter());
			servicesService.importServiceScheduleParamsUpdateMode(dbService.getServiceSchedulingParams(), exportedService.getServiceSchedulingParams());
			pathListService.importServicePathListUpdateMode(dbService, exportedService, importMode);
			aggregationService.importServiceAggregationDefinitionUpdateMode(dbService, exportedService);			
		} else {
			pathListService.importServicePathListUpdateMode(dbService, exportedService, importMode);
			aggregationService.importServiceAggregationDefinitionAddMode(dbService, exportedService);			
		}			
		return updateServiceForImport(dbService);
	}
	
	public void setBasicParameterInServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, String serviceAlias, int importMode) {
		exportedService.setId(0);
		
		String exportedServInstanceId = exportedService.getServInstanceId();
		Service service = servicesDao.getServiceByServiceInstanceId(serverInstanceDB.getId(), exportedService.getSvctype().getId(), exportedServInstanceId);
		if(service != null){
			exportedServInstanceId = servicesService.getMaxServiceInstanceIdForServer(serverInstanceDB.getId(), exportedService.getSvctype().getId());
		}
		exportedService.setServInstanceId(exportedServInstanceId);
		List<Agent> exportedAgents = exportedServerInstance.getAgentList();
		if(!CollectionUtils.isEmpty(exportedAgents)){
			for(Agent exportedAgent : exportedAgents){
				if(exportedAgent instanceof PacketStatisticsAgent){
					List<ServicePacketStatsConfig> servicePacketStatsConfigs = ((PacketStatisticsAgent) exportedAgent).getServiceList();
					if(servicePacketStatsConfigs != null && !servicePacketStatsConfigs.isEmpty()){
						for(ServicePacketStatsConfig servicePacketStatsConfig : servicePacketStatsConfigs){
							if(servicePacketStatsConfig.getService().getName().equalsIgnoreCase(exportedService.getName())){
								servicePacketStatsConfig.getService().setServInstanceId(exportedServInstanceId); // it will be use when we add new PacketStatisticsAgent for this serverInstanceDB
							}
						}
					}
				}
			}
		}
		
		boolean isServiceNameAvailable = false;
		List<Service> dbServiceList = serverInstanceDB.getServices();
		if(!CollectionUtils.isEmpty(dbServiceList)){
			for(Service dbService : dbServiceList){
				if(dbService.getName().equalsIgnoreCase(exportedService.getName())){
					isServiceNameAvailable = true;
				}
			}
		}
		
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH || isServiceNameAvailable) {
			exportedService.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedService.getName()));
		}
		exportedService.setServerInstance(serverInstanceDB);
		exportedService.setCreatedByStaffId(serverInstanceDB.getCreatedByStaffId());
		exportedService.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedService.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedService.setLastUpdatedByStaffId(serverInstanceDB.getCreatedByStaffId());
		exportedService.setSvctype(servicetypeDao.getServiceTypeByAlias(serviceAlias));
		
		//set maximum service instance id
		//MED-4605
		//exportedService.setServInstanceId(servicesService.getMaxServiceInstanceIdForServer(exportedService.getServerInstance().getId(), exportedService.getSvctype().getId()));
	}
	
	public boolean isSave(int importMode, Service service) {
		return (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH
				|| importMode == BaseConstants.IMPORT_MODE_OVERWRITE 
				|| service == null) ? true : false;
	}
	
	public boolean isUpdate(int importMode) {
		return (importMode == BaseConstants.IMPORT_MODE_UPDATE || importMode == BaseConstants.IMPORT_MODE_ADD) ? true : false;
	}
	
	public ResponseObject updateServiceForImport(Service service) {
		ResponseObject responseObject = new ResponseObject();
		service.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		service.setLastUpdatedByStaffId(service.getServerInstance().getCreatedByStaffId());
		servicesDao.merge(service);
		responseObject.setSuccess(true);
		responseObject.setObject(service);
		return responseObject;
	}
	
	public ResponseObject saveServiceForImport(Service service) {
		ResponseObject responseObject = new ResponseObject();
		servicesDao.save(service);
		responseObject.setSuccess(true);
		responseObject.setObject(service);
		return responseObject;
	}

}