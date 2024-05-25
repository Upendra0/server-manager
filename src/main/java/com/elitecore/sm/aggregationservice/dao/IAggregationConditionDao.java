package com.elitecore.sm.aggregationservice.dao;

import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.common.dao.GenericDAO;

public interface IAggregationConditionDao extends GenericDAO<AggregationCondition>{
	public void deleteAggregationCondition(AggregationCondition aggregationCondition);
}
