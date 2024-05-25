package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.services.model.DataConsolidationService;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface IDataConsolidationService {

	
	public List<DataConsolidation>  getDataConsolidationServicewiseCount(String name, int serviceId);
	
	public void importDataConsolidationMappingUpdateMode(DataConsolidationMapping dbMapping, DataConsolidationMapping exportedMapping);
	
	public void importDataConsolidationMappingAddAndKeepBothMode(DataConsolidationMapping exportedMapping, DataConsolidationPathList pathList, int importMode);
	
	public void importServiceDataConsolidationUpdateMode(DataConsolidationService dbService, DataConsolidationService exportedService, int importMode);
	
	public void importServiceDataConsolidationAddAndKeepBothMode(DataConsolidationService exportedService, int importMode);
	
	public DataConsolidationMapping getDataConsolidationMappingFromList(List<DataConsolidationMapping> mappingList, String mappingName, String destPath);
	
}
