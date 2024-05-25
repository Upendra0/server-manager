package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;

public interface IAggregationAttributeService {
	
	public void importDataAggregationAttributeAddAndKeepBothMode(AggregationAttribute exportedAttribute, AggregationDefinition aggregationDefinition, int importMode);

	public void importDataAggregationAttributeUpdateMode(AggregationAttribute dbAttribute, AggregationAttribute exportedAttribute);
	
	public AggregationAttribute getDataAggregationAttributeFromList(List<AggregationAttribute> aggAttributeList, String fieldName);
}
