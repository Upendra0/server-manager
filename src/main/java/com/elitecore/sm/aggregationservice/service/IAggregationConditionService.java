package com.elitecore.sm.aggregationservice.service;

import java.util.List;

import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;

public interface IAggregationConditionService {

	public void importDataAggregationConditionAddAndKeepBothMode(AggregationCondition exportedCondition, AggregationDefinition aggregationDefinition, int importMode);
	
	public void importDataConditionUpdateMode(AggregationCondition dbCondition, AggregationCondition exportedCondition);
	
	public AggregationCondition getDataConditionFromList(List<AggregationCondition> aggConditionList, String condExpression);
}
