package com.elitecore.sm.aggregationservice.dao;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;

public interface IAggregationAttributeDao extends GenericDAO<AggregationAttribute>{
	void deleteAggregationAttribute(AggregationAttribute aggregationAttribute);

}
