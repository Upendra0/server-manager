package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;

public interface IConsolidationGroupAttributeService {
	
	public ResponseObject addConsolidationGroupAttributeList(DataConsolidationGroupAttribute dataConsolidationGroupAttribute, int iConsDefId);
	
	public ResponseObject updateConsolidationGroupAttributeList(DataConsolidationGroupAttribute dataConsolidationGroupAttribute, int iConsDefId);
	
	public ResponseObject getConsolidationGroupById(int id);
	
	public void importDataConsolidationGroupAttributeAddAndKeepBothMode(DataConsolidationGroupAttribute exportedGroupAttribute, DataConsolidation dataConsolidation);
	
	public void importDataConsolidationGroupAttributeUpdateMode(DataConsolidationGroupAttribute dbGroupAttribute, DataConsolidationGroupAttribute exportedGroupAttribute);
	
	public DataConsolidationGroupAttribute getDataConsolidationGroupAttributeFromList(List<DataConsolidationGroupAttribute> groupAttributeList, String groupingField);

	public DataConsolidation getDataConsolidationById(int id);

}