package com.elitecore.sm.aggregationservice.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.common.dao.GenericDAOImpl;

@Repository(value = "aggregationAttributeDao")
public class AggregationAttributeDaoImpl extends GenericDAOImpl<AggregationAttribute> implements IAggregationAttributeDao {
	
	
	@Override
	public void deleteAggregationAttribute(AggregationAttribute aggregationAttribute) {
		getCurrentSession().delete(aggregationAttribute);
	}

}