package com.elitecore.sm.serverinstance.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.services.model.DiameterCollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.model.Service;


public interface ServerInstanceImportExportService {
	
	public ResponseObject importServiceAndDepedants(ServerInstance serverInstanceDB, Service exportedService, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject saveCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject saveDistributionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject saveParsingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject updateCollectionServiceForImport(CollectionService collectionServiceDb, CollectionService exportedCollectionService, int importMode);
	
	public ResponseObject updateDistributionServiceForImport(DistributionService distributionServiceDb, DistributionService exportedDistributionService, int importMode);
	
	public ResponseObject updateParsingServiceForImport(ParsingService parsingServiceDb, ParsingService exportedParsingService, int importMode);
	
	public ResponseObject saveOnlineCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, String serviceAlias, int importMode);
	
	public ResponseObject updateOnlineCollectionServiceForImport(Service dbService, Service exportedService, int importMode);
	
	public ResponseObject updateDiameterCollectionServiceForImport(Service dbService, Service exportedService, int importMode);
	
	public ResponseObject saveProcessingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject updateProcessingServiceForImport(ProcessingService dbProcessingService, ProcessingService exportedProcessingService, int importMode);
	
	public ResponseObject updateDataConsolidationServiceForImport(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode);
	
	public ResponseObject saveDataConsolidationServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject updateIpLogParsingServiceForImport(IPLogParsingService dbService, IPLogParsingService exportedService, int importMode);
	
	public ResponseObject saveIpLogParsingServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);
	
	public ResponseObject updateAggregationServiceForImport(AggregationService dbService, AggregationService exportedService, int importMode);
	
	public ResponseObject saveAggregationServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, int importMode);

	public ResponseObject saveDiameterCollectionServiceForImport(Service exportedService, ServerInstance serverInstanceDB, ServerInstance exportedServerInstance, String serviceAlias, int importMode);
}
