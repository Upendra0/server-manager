package com.elitecore.sm.aggregationservice.dao;

import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.common.dao.GenericDAO;

public interface IAggregationKeyAttributeDao extends GenericDAO<AggregationKeyAttribute>{
	void deleteAggregationKeyAttribute(AggregationKeyAttribute aggregationKeyAttribute);

}
