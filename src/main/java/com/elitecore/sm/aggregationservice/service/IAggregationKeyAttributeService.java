package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;

public interface IAggregationKeyAttributeService {

	public void importDataAggregationKeyAttributeAddAndKeepBothMode(AggregationKeyAttribute exportedKeyAttribute, AggregationDefinition aggregationDefinition, int importMode);

	public AggregationKeyAttribute getDataAggregationKeyAttributeFromList(List<AggregationKeyAttribute> aggKeyAttributeList, String fieldName);
}
