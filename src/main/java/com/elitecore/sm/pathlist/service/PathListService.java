package com.elitecore.sm.pathlist.service;

import java.util.List;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author avani.panchal
 *
 */
public interface PathListService {
	
	public ResponseObject addPathList(PathList pathList);
	
	public ResponseObject updatePathList(PathList driver);
	
	public ResponseObject createPathList(PathList pathList);
	
	public ResponseObject getPathListByDriverId(int driverId);
	
	public ResponseObject updatePathListDetail(PathList pathList);
	
	public ResponseObject deletePathListDetails(int pathListId, int staffId, boolean isProcessing) throws CloneNotSupportedException;
	
	public List<ImportValidationErrors> validatePathListForImport(PathList pathList,List<ImportValidationErrors> pathImportErrorList);
	
	public ResponseObject addIpLogParsingPathList(Parser parserWrapper,int serviceId);
	
	public ResponseObject createIpLogPathListAndParser(ParsingPathList pathlist);
	
	public ResponseObject updateIpLogParsingPathList(Parser parserWrapper,int serviceId);
	
	public boolean  isPathListUnique(String pathListName);
	
	public void validateWrapperForImport(Parser wrapper,List<ImportValidationErrors> importErrorList);
	
	public ResponseObject iterateOverParsingPathListConfig(ParsingPathList pathlist,boolean isImport, int importMode) ;
	
	public ResponseObject getParsingPathListByServiceId(int serviceId);
	
	public ResponseObject addParsingPathList(ParsingPathList pathList);
	
	public ResponseObject updateParsingPathList(ParsingPathList pathList);
	
	public ResponseObject deleteParsingPathListDetails(int id, int staffId) throws SMException;
	
	public ResponseObject getDistributionPathListAndPluginDetails(int driverId, String driverTypeAlias);
	
	public ResponseObject addDistributionDriverPathList(DistributionDriverPathList pathList);
	
	public ResponseObject getParsingPathListUsingServiceId(int serviceId);
	
	public ResponseObject iterateDriverPathListDetails(Drivers driver, boolean isImport);
	
	public ResponseObject getProcessingPathListByServiceId(int serviceId);
	
	public ResponseObject addProcessingServicePathList(ProcessingPathList pathList);
	
	public ResponseObject updateProcessingServicePathList(ProcessingPathList pathList);
	
	public PathList getPathListById(int pathListId);
	
	public ResponseObject addAggregationServicePathList(AggregationServicePathList pathList);
	
	public ResponseObject updateAggregationServicePathList(AggregationServicePathList pathList);
	
	public ResponseObject getAggregationPathListByServiceId(int serviceId);
	
	public ResponseObject addConsolidationServicePathList(DataConsolidationPathList pathList);
	
	public ResponseObject addConsolidationPathListMapping(DataConsolidationMapping mapping);
	
	public ResponseObject updateConsolidationPathListMapping(DataConsolidationMapping mapping);
	
	public ResponseObject deleteConsolidationPathListMapping(String mappingIds, int staffId);
	
	public ResponseObject deleteConsolidationPathListMapping(int mappingId, int staffId);
	
	public ResponseObject updateConsolidationServicePathList(DataConsolidationPathList pathList);
	
	public ResponseObject getConsolidationPathListByServiceId(int serviceId);
	
	public void importDriverPathListAddAndKeepBothMode(Drivers exportedDriver, int importMode);
	
	public void importDriverPathListUpdateMode(Drivers dbDriver, Drivers exportedDriver, int importMode);
	
	public String getMaxPathListId(Service service);
	
	public String getMaxPathListId(Drivers driver);
	
	public void importServicePathListUpdateMode(Service dbService, Service exportedService, int importMode);
	
	public void importServicePathListAddAndKeepBothMode(Service exportedService, int importMode);
	
	public void importPathListUpdateMode(PathList dbPath, PathList exportedPath,int importMode);
	
	public void importPathListAddAndKeepBothMode(PathList pathList, Drivers driver, Service service, int importMode);
	
	public PathList getPathFromList(List<PathList> pathList, String pathName);
	
	public ResponseObject deleteIpLogParsingPathListDetails(int id, int staffId) throws SMException;
	
	public PathList getPathListByServiceAndPathId(int serviceId, String pathId);
	
	public void importPathListAddMode(PathList dbPath, PathList exportedPath);
	
}
