package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;

public interface IConsolidationAttributeService {

	public ResponseObject addConsolidationAttributeList(DataConsolidationAttribute dataConsolidationAttribute, int iConsDefId);
	
	public ResponseObject updateConsolidationAttributeList(DataConsolidationAttribute dataConsolidationAttribute, int iConsDefId);
	
	public ResponseObject getConsolidationAttributeById(int id);
	
	public void importDataConsolidationAttributeAddAndKeepBothMode(DataConsolidationAttribute exportedAttribute, DataConsolidation dataConsolidation, int importMode);
	
	public void importDataConsolidationAttributeUpdateMode(DataConsolidationAttribute dbAttribute, DataConsolidationAttribute exportedAttribute);
	
	public DataConsolidationAttribute getDataConsolidationAttributeFromList(List<DataConsolidationAttribute> consolidationAttributeList, String fieldName);
	
	public DataConsolidation getDataConsolidationById(int id);
}
