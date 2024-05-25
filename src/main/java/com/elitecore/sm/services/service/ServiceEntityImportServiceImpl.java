package com.elitecore.sm.services.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.service.IAggregationDefinitionService;
import com.elitecore.sm.aggregationservice.service.IAggregationService;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.service.IConsolidationDefinitionService;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.diameterpeer.service.DiameterPeerService;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.service.NetflowClientService;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.serverinstance.service.ServerInstanceImportExportService;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author brijesh.soni
 *
 */
@org.springframework.stereotype.Service(value = "serviceEntityImportService")
public class ServiceEntityImportServiceImpl implements ServiceEntityImportService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private ServicesDao servicesDao;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private PathListService pathListService;
	
	@Autowired
	private DriversService driversService;
	
	@Autowired
	private NetflowClientService netflowClientService;
	
	@Autowired
	private DiameterPeerService diameterPeerService;
	
	@Autowired
	private IConsolidationDefinitionService consolidationDefinitionService;
	
	@Autowired
	private IAggregationService aggregationService;
	
	@Autowired
	private IAggregationDefinitionService aggDefinitionService;
	
	@Autowired
	private ServerInstanceImportExportService serverInstanceImportExportService;
	
	@Transactional(rollbackFor = SMException.class)
	@Override
	public ResponseObject importService(int serviceId, Service exportedService, int staffId, 
			String jaxbXMLPath, int serverId, int importMode) throws SMException {
		
		ResponseObject responseObject = new ResponseObject();
		
		Service dbService = servicesDao.findByPrimaryKey(Service.class, serviceId);
		
		Map<String, Object> serviceMap = servicesService.getServiceJAXBByTypeAndId(dbService.getId(), 
				dbService.getSvctype().getServiceFullClassName(), jaxbXMLPath, null);
		
		dbService = (Service) serviceMap.get(BaseConstants.SERVICE_JAXB_OBJECT);
		
		EliteUtils.getDateForImport(true); //initialize date object for whole import operation.
		if (dbService != null) {
			//if overwrite then delete service and dependents
			if(importMode == BaseConstants.IMPORT_MODE_OVERWRITE) {
				dbService.setSvcExecParams(exportedService.getSvcExecParams()); // MED-8238
				responseObject = overwriteAndSaveServiceForImport(dbService, exportedService, importMode);
			} else {
				if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238 
					setServiceBasicParameterForImport(dbService, exportedService, staffId);
				}
				
				if (dbService instanceof NetflowBinaryCollectionService 
						&& exportedService instanceof NetflowBinaryCollectionService) {
					importOnlineCollectionService(dbService, exportedService, importMode);
				} else if (dbService instanceof DiameterCollectionService && exportedService instanceof DiameterCollectionService) {
					importDiameterCollectionService((DiameterCollectionService)dbService, (DiameterCollectionService)exportedService, importMode);
				} else if (dbService instanceof CollectionService && exportedService instanceof CollectionService) {
					importCollectionService((CollectionService) dbService, (CollectionService) exportedService, importMode);
				} else if (dbService instanceof IPLogParsingService && exportedService instanceof IPLogParsingService) {
					importIpLogParsingService((IPLogParsingService) dbService, (IPLogParsingService) exportedService, importMode);
				} else if (dbService instanceof ParsingService && exportedService instanceof ParsingService) {
					importParsingService((ParsingService) dbService, (ParsingService) exportedService, importMode);
				} else if (dbService instanceof DistributionService && exportedService instanceof DistributionService) {
					importDistributionService((DistributionService) dbService, (DistributionService) exportedService, importMode);
				} else if (dbService instanceof ProcessingService && exportedService instanceof ProcessingService) {
					importProcessingService((ProcessingService) dbService, (ProcessingService) exportedService, importMode);
				} else if(dbService instanceof DataConsolidationService && exportedService instanceof DataConsolidationService){
					importDataConsolidationService((DataConsolidationService) dbService, (DataConsolidationService) exportedService, importMode);
				} else if(dbService instanceof AggregationService && exportedService instanceof AggregationService){
					importAggregationService((AggregationService) dbService, (AggregationService) exportedService, importMode);
				}
				servicesDao.merge(dbService);
				responseObject.setSuccess(true);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SERVICE_INSTANCE_NOT_FOUND);
			logger.debug("Fail to fetch service instance from database");
		}
		return responseObject;
	}
	
	public void importCollectionService(CollectionService dbService, CollectionService exportedService, int importMode) {
		logger.debug("going to import collection service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE)  //MED-8238
			servicesService.importServiceScheduleParamsUpdateMode(dbService.getServiceSchedulingParams(), exportedService.getServiceSchedulingParams());
		importServiceDriverList(dbService, exportedService, importMode);
	}
	
	public void importParsingService(ParsingService dbService, ParsingService exportedService, int importMode) {
		logger.debug("going to import parsing service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importParsingServiceBasicParameterUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
		}
		importServicePathList(dbService, exportedService, importMode);
	}
	
	public void importDistributionService(DistributionService dbService, DistributionService exportedService, int importMode) {
		logger.debug("goint to import distribution service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importDistributionServiceBasicParameterUpdateMode(dbService, exportedService);
			servicesService.importServiceScheduleParamsUpdateMode(dbService.getServiceSchedulingParams(), exportedService.getServiceSchedulingParams());
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
		}
		importServiceDriverList(dbService, exportedService, importMode);
	}
	
	public void importOnlineCollectionService(Service dbService, Service exportedService, int importMode) {
		logger.debug("going to import online collection service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) //MED-8238
			servicesService.importOnlineCollectionServiceBasicParameterForUpdateMode(dbService, exportedService, importMode);
		if(dbService instanceof NetflowBinaryCollectionService && exportedService instanceof NetflowBinaryCollectionService) {
			importOnlineCollectionServiceClientList((NetflowBinaryCollectionService) dbService, (NetflowBinaryCollectionService) exportedService, importMode);
			if(importMode != BaseConstants.IMPORT_MODE_UPDATE) 		// MED-8256
				servicesService.iteratenatFlowProxyClientInUpdateMode((NetflowBinaryCollectionService)dbService, (NetflowBinaryCollectionService)exportedService);
		}
	}
	
	public void importDiameterCollectionService(DiameterCollectionService dbService, DiameterCollectionService exportedService, int importMode) {
		logger.debug("going to import diameter collection service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) //MED-8238
			servicesService.importDiameterCollectionServiceBasicParameterForUpdateMode(dbService, exportedService);
		importDiameterCollectionServicePeerList((DiameterCollectionService) dbService, (DiameterCollectionService) exportedService, importMode);
	}
	
	public void importProcessingService(ProcessingService dbService, ProcessingService exportedService, int importMode) {
		logger.debug("goint to import processing service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importProcessingServiceBasicParameterUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
		}
		importServicePathList(dbService, exportedService, importMode);
	}
	
	public void importIpLogParsingService(IPLogParsingService dbService, IPLogParsingService exportedService, int importMode) {
		logger.debug("going to import ip log parsing service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importIpLogParsingServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
			servicesService.importServicePartitionParamUpdateMode(dbService, exportedService);
		}
		importServicePathList(dbService, exportedService, importMode);
	}
	
	public void importDataConsolidationService(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode) {
		logger.debug("going to import data consolidation service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importDataConsolidationServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupParam(), exportedService.getFileGroupParam());
		}
		importServiceDataConsolidationList(dbService, exportedService, importMode);
		importServicePathList(dbService, exportedService, importMode);
	}
	
	public void importAggregationService(AggregationService dbService, AggregationService exportedService, int importMode) {
		logger.debug("going to import aggreagation service");
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE) { //MED-8238
			servicesService.importAggreagationServiceBasicParameterForUpdateMode(dbService, exportedService);
			servicesService.importServiceScheduleParamsUpdateMode(dbService.getServiceSchedulingParams(), exportedService.getServiceSchedulingParams());
			servicesService.importFileGroupingParameterUpdateMode(dbService.getFileGroupingParameter(), exportedService.getFileGroupingParameter());
		}
		importServiceAggregationList(dbService, exportedService, importMode);
		importServicePathList(dbService, exportedService, importMode);
	}
	
	public void importServiceDriverList(Service dbService, Service exportedService, int importMode) {
		logger.debug("going to add/update drivers for service : "+dbService.getName());
		List<Drivers> dbDrivers = dbService.getMyDrivers();
		List<Drivers> exportedDriversList = exportedService.getMyDrivers();
		if(!CollectionUtils.isEmpty(exportedDriversList)) {
			int length = exportedDriversList.size();
			for(int i = length-1; i >= 0; i--) {
				Drivers exportedDriver = exportedDriversList.get(i);
				if(exportedDriver != null) {
					Drivers dbDriver = driversService.getDriverFromList(dbDrivers, exportedDriver.getName());
					if(dbDriver != null && !dbDriver.getDriverType().getAlias().equalsIgnoreCase(exportedDriver.getDriverType().getAlias())){
						dbDriver = null;
						exportedDriver.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT, exportedDriver.getName()));
					}
					if(isSave(importMode, dbDriver)) {
						logger.debug("going to add driver for import : "+exportedDriver.getName());
						driversService.saveDriverForImport(exportedDriver, dbService, importMode);
						dbDrivers.add(exportedDriver);
					} else if(isUpdate(importMode)) {
						logger.debug("going to update driver");
						driversService.importDriverForUpdateMode(dbDriver, exportedDriver, importMode);
						dbDrivers.add(dbDriver);
					}
				}
			}
		}
	}
	
	public void importServicePathList(Service dbService, Service exportedService, int importMode) {
		logger.debug("going to add/update pathlist for service : "+dbService.getName());
		List<PathList> dbPathList = dbService.getSvcPathList();
		List<PathList> backupPathList = new ArrayList<>(dbPathList);
		List<PathList> exportedPathList = exportedService.getSvcPathList();
		if(!CollectionUtils.isEmpty(exportedPathList)) {
			int length = exportedPathList.size();
			for(int i = length-1; i >= 0; i--) {
				PathList exportedPath = exportedPathList.get(i);
				if(exportedPath != null) {
					PathList dbPath = pathListService.getPathFromList(dbPathList, exportedPath.getName());
					if(dbPath != null && dbService instanceof ParsingService){
						List<Parser> parserList = ((ParsingPathList)dbPath).getParserWrappers();
						for (int ij = 0; ij < parserList.size(); ij++) {
							Parser p=parserList.get(ij);
							if(StateEnum.DELETED.equals(p.getStatus())){
								List<Parser> temp = new ArrayList<>();
								((ParsingPathList)dbPath).setParserWrappers(temp);
							}else if(!StateEnum.DELETED.equals(p.getStatus())){
								if( (!((ParsingPathList)dbPath).getParserWrappers().isEmpty()) && !(((ParsingPathList)exportedPath).getParserWrappers().isEmpty()) &&  !((ParsingPathList)dbPath).getParserWrappers().get(0).getName().equalsIgnoreCase(((ParsingPathList)exportedPath).getParserWrappers().get(0).getName())){
									dbPath = null;
									exportedPath.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedPath.getName()));
								}
							}
						}
						//MED-5899 : if pathlist's path names are same and and plugin names are different then it should create another pathlist with import suffix
											}
					if(isSave(importMode, dbPath)) {
						if(dbService instanceof IPLogParsingService) {//in iplog parsing service, only one path is supported
							if(EliteUtils.getActiveListFromGivenList(backupPathList).isEmpty()) {
								logger.debug("going to add pathlist : "+exportedPath.getName());
								pathListService.importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
								dbPathList.add(exportedPath);
							} 
						} else if(dbService instanceof AggregationService) { //in aggregation service, only one path is supported
							if(EliteUtils.getActiveListFromGivenList(backupPathList).isEmpty() && dbPath == null) {
								pathListService.importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
								dbPathList.add(exportedPath);
							}
						} else {
							logger.debug("going to add pathlist : "+exportedPath.getName());
							pathListService.importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
							dbPathList.add(exportedPath);
						}
					} else {
						if(isUpdate(importMode)) {
							if((dbService instanceof ParsingService || dbService instanceof IPLogParsingService) && importMode==BaseConstants.IMPORT_MODE_ADD){
								//MED-9164
								if(dbPath==null || ((ParsingPathList)dbPath).getParserWrappers().isEmpty()){
									pathListService.importPathListUpdateMode(dbPath, exportedPath,importMode);
								}else {
									pathListService.importPathListAddAndKeepBothMode(exportedPath, null, dbService, importMode);
								}
							}else{
								if(dbPath!=null && dbService instanceof IPLogParsingService) {
									logger.debug("IPLog Parsing Service can't allow multiple plugin in same pathlist.");
								} else {
									pathListService.importPathListUpdateMode(dbPath, exportedPath,importMode);
								}
							}
						}
						dbPathList.add(dbPath);
					}
				}
			}
		}
	}
	
	public void importOnlineCollectionServiceClientList(NetflowBinaryCollectionService dbService, NetflowBinaryCollectionService exportedService, int importMode) {
		logger.debug("going to add/update netflow client in service : "+dbService.getName());
		List<NetflowClient> dbNetflowClientList = dbService.getNetFLowClientList();
		List<NetflowClient> exportedNetflowClientList = exportedService.getNetFLowClientList();
		if(!CollectionUtils.isEmpty(exportedNetflowClientList)) {
			int length = exportedNetflowClientList.size();
			for(int i = length-1; i >= 0; i--) {
				NetflowClient exportedNetflowClient = exportedNetflowClientList.get(i);
				if(exportedNetflowClient != null) {
					NetflowClient dbNetflowClient = netflowClientService.getClientFromList(dbNetflowClientList, exportedNetflowClient.getName());
					if(isSave(importMode, dbNetflowClient)) {
						logger.debug("going to add netflow client : "+exportedNetflowClient.getName());
						netflowClientService.importNetflowClientForAddAndKeepBothMode(exportedNetflowClient, dbService, importMode);
						dbNetflowClientList.add(exportedNetflowClient);
					} else if(isUpdate(importMode)) {
						if(importMode==BaseConstants.IMPORT_MODE_ADD){
							logger.debug("Client is already added"+dbNetflowClient.getName());
						}else{
							logger.debug("going to update netflow client : "+dbNetflowClient.getName());
							netflowClientService.importNetflowClientForUpdateMode(dbNetflowClient, exportedNetflowClient);
							dbNetflowClientList.add(dbNetflowClient);
						}
					}
				}
			}
		}
	}
	
	public void importDiameterCollectionServicePeerList(DiameterCollectionService dbService, DiameterCollectionService exportedService, int importMode){
		logger.debug("going to add/update diameter peer in service : "+dbService.getName());
		List<DiameterPeer> dbDiameterPeerList = dbService.getDiameterPeerList();
		List<DiameterPeer> exportedDiameterPeerList = exportedService.getDiameterPeerList();
		
		if(!CollectionUtils.isEmpty(exportedDiameterPeerList)) {
			int length = exportedDiameterPeerList.size();
			for(int i = length-1; i >= 0; i--) {
				DiameterPeer exportedDiameterPeer = exportedDiameterPeerList.get(i);
				if(exportedDiameterPeer != null) {
					DiameterPeer dbDiameterPeer = diameterPeerService.getPeerFromList(dbDiameterPeerList, exportedDiameterPeer.getName());
					if(isSave(importMode, dbDiameterPeer)) {
						logger.debug("going to add dameter peer : "+exportedDiameterPeer.getName());
						diameterPeerService.importDiameterPeerForAddAndKeepBothMode(exportedDiameterPeer, dbService, importMode);
						dbDiameterPeerList.add(exportedDiameterPeer);
					} else if(isUpdate(importMode)) {
						logger.debug("going to update dameter peer : "+dbDiameterPeer.getName());
						diameterPeerService.importDiameterPeerForUpdateMode(dbDiameterPeer, exportedDiameterPeer,importMode);
						dbDiameterPeerList.add(dbDiameterPeer);
					}
				}
			}
		}
	}
	
	public void importServiceDataConsolidationList(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode) {
		logger.debug("going to add/update data cosolication for service : "+dbService.getName());
		List<DataConsolidation> dbDataConsolidationList = dbService.getConsolidation();
		List<DataConsolidation> exportedDataConsolidationList = exportedService.getConsolidation();
		if(!CollectionUtils.isEmpty(exportedDataConsolidationList)) {
			int length = exportedDataConsolidationList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidation exportedDataConsolidation = exportedDataConsolidationList.get(i);
				if(exportedDataConsolidation != null) {
					DataConsolidation dbDataConsolidation = consolidationDefinitionService.getDataConsolidationFromList(dbDataConsolidationList, exportedDataConsolidation.getConsName());
					if(isSave(importMode, dbDataConsolidation)) {
						logger.debug("going to add data cosolication : "+exportedDataConsolidation.getConsName());
						consolidationDefinitionService.importDataConsolidationAddAndKeepBothMode(exportedDataConsolidation, dbService, importMode);
						dbDataConsolidationList.add(exportedDataConsolidation);
					} else if(isUpdate(importMode)) {
						logger.debug("going to update data cosolication : "+dbDataConsolidation.getConsName());
						consolidationDefinitionService.importDataConsolidationUpdateMode(dbDataConsolidation, exportedDataConsolidation, importMode);
						dbDataConsolidationList.add(dbDataConsolidation);
					}
				}
			}
		}
	}
	
	public void importServiceAggregationList(AggregationService dbService, AggregationService exportedService, int importMode) {
		logger.debug("going to add/update data for aggregation service : "+dbService.getName());
		AggregationDefinition dbDefinition = dbService.getAggregationDefinition();
		AggregationDefinition exportedDefinition = exportedService.getAggregationDefinition();
		if(exportedDefinition != null) {
			if(isSave(importMode, dbDefinition)) {
				logger.debug("going to add definition : "+exportedDefinition.getAggDefName());
				aggDefinitionService.importDefinitionAddAndKeepBothMode(exportedDefinition,dbService, importMode);
			} else if(isUpdate(importMode)) {
				logger.debug("going to update definition : "+exportedDefinition.getAggDefName());
				aggDefinitionService.importDefinitionUpdateMode(dbDefinition, exportedDefinition, importMode);
			}
		}
	}
	
	public void setServiceBasicParameterForImport(Service dbService, Service exportedService, int staffId) {
		dbService.setSvcExecParams(exportedService.getSvcExecParams());
		dbService.setEnableFileStats(exportedService.isEnableFileStats());
		dbService.setEnableDBStats(exportedService.isEnableDBStats());
		dbService.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		dbService.setLastUpdatedByStaffId(staffId);
	}
	
	public ResponseObject overwriteAndSaveServiceForImport(Service dbService, Service exportedService, int importMode) {
		//delete dependents of dbService
		ResponseObject responseObject = servicesService.iterateOverServiceAndDepedantsForImport(dbService.getServerInstance(), dbService, false, false, null, null, false, true);
		logger.debug("Delete operation successful for db service dependents");
		if (responseObject.isSuccess()) {
			//update dependents of dbService with exportedService
			if(dbService instanceof NetflowBinaryCollectionService && exportedService instanceof NetflowBinaryCollectionService) {
				responseObject = serverInstanceImportExportService.updateOnlineCollectionServiceForImport(dbService, exportedService, importMode);
			} else if(dbService instanceof DiameterCollectionService && exportedService instanceof DiameterCollectionService) {
				responseObject = serverInstanceImportExportService.updateDiameterCollectionServiceForImport(dbService, exportedService, importMode);
			}else if(dbService instanceof CollectionService && exportedService instanceof CollectionService) {
				responseObject = serverInstanceImportExportService.updateCollectionServiceForImport((CollectionService) dbService, (CollectionService) exportedService, importMode);
			} else if(dbService instanceof DistributionService && exportedService instanceof DistributionService) {
				responseObject = serverInstanceImportExportService.updateDistributionServiceForImport((DistributionService) dbService, (DistributionService) exportedService, importMode);
			} else if(dbService instanceof ParsingService && exportedService instanceof ParsingService) {
				responseObject = serverInstanceImportExportService.updateParsingServiceForImport((ParsingService) dbService, (ParsingService) exportedService, importMode);
			} else if(dbService instanceof ProcessingService && exportedService instanceof ProcessingService) {
				responseObject = serverInstanceImportExportService.updateProcessingServiceForImport((ProcessingService) dbService, (ProcessingService) exportedService, importMode);
			} else if(dbService instanceof DataConsolidationService && exportedService instanceof DataConsolidationService) {
				responseObject = serverInstanceImportExportService.updateDataConsolidationServiceForImport((DataConsolidationService) dbService, (DataConsolidationService) exportedService, importMode);
			} else if(dbService instanceof IPLogParsingService && exportedService instanceof IPLogParsingService) {
				responseObject = serverInstanceImportExportService.updateIpLogParsingServiceForImport((IPLogParsingService) dbService, (IPLogParsingService) exportedService, importMode);
			} else if(dbService instanceof AggregationService && exportedService instanceof AggregationService) {
				responseObject = serverInstanceImportExportService.updateAggregationServiceForImport((AggregationService) dbService, (AggregationService) exportedService, importMode);
			}
		} else {
			responseObject.setResponseCode(ResponseCode.IMPORT_FAIL);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	public boolean isSave(int importMode, Object object) {
		//if import mode is keepboth OR import mode is add and object is null OR import mode is update and object is null
		return (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH || object == null) ? true : false;
	}
	
	public boolean isUpdate(int importMode) {
		//if import mode is update and object is there			  //MED-8460	
		return (importMode == BaseConstants.IMPORT_MODE_UPDATE || importMode == BaseConstants.IMPORT_MODE_ADD) ? true : false;
	}
}