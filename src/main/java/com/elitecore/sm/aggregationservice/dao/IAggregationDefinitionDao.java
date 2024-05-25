package com.elitecore.sm.aggregationservice.dao;

import java.util.List;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.common.dao.GenericDAO;

public interface IAggregationDefinitionDao extends GenericDAO<AggregationDefinition>{
	
	public AggregationDefinition getAggregationDefintionListByName(String defName);
	
	public List<AggregationDefinition> getAllAggregationDefintionName();
	
	public int getDefinitionNameCount(String defName);
	
	public AggregationDefinition getAggregationDefintionListByServiceId(int serviceId);
}
