package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.util.EliteUtils;

public interface IConsolidationDefinitionService {
	
	public ResponseObject addConsolidationDefintionList(DataConsolidation dataConsList);
	
	public ResponseObject updateConsolidationDefintionList(DataConsolidation dataConsList);
	
	public ResponseObject getConsolidationDefintionListByServiceId(int serviceId);
	
	public List<DataConsolidation> getConsolidationListByServiceId(int serviceId);
	
	public ResponseObject getConsolidationDefintionById(DataConsolidation dataConsList);
	
	public ResponseObject getConsolidationDefintionList(int serviceId);
	
	public ResponseObject deleteConsolidationDefintionList(DataConsolidation dataConsList, HttpServletRequest request, EliteUtils eliteUtils);
	
	public void importDataConsolidationUpdateMode(DataConsolidation dbConsolidation, DataConsolidation exportedConsolidation, int importMode);
	
	public void importDataConsolidationAddAndKeepBothMode(DataConsolidation exportedDataConsolidation, DataConsolidationService service, int importMode);
	
	public DataConsolidation getDataConsolidationFromList(List<DataConsolidation> dataConsolidationList, String dataConsolidationName);

	public long getDataConsolidationMappingCountByMappingNameAndDestPath(int mappingId, String mappingName, String destPath);
	
	public void importDataConsolidationAddMode(DataConsolidation dbConsolidation, DataConsolidation exportedConsolidation);
}
