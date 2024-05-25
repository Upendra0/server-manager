package com.elitecore.sm.aggregationservice.service;

import com.elitecore.sm.services.model.AggregationService;

public interface IAggregationService {

	public void importServiceAggregationAddAndKeepBothMode(AggregationService exportedService, int importMode); 
	
	public void importServiceAggregationDefinitionUpdateMode(AggregationService dbService, AggregationService exportedService);
	
	public void importServiceAggregationDefinitionAddMode(AggregationService dbService, AggregationService exportedService);

}
