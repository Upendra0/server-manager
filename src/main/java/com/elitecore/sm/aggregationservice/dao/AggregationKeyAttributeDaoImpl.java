package com.elitecore.sm.aggregationservice.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.common.dao.GenericDAOImpl;

@Repository(value = "aggregationKeyAttributeDao")
public class AggregationKeyAttributeDaoImpl extends GenericDAOImpl<AggregationKeyAttribute>
		implements IAggregationKeyAttributeDao {

	@Override
	public void deleteAggregationKeyAttribute(AggregationKeyAttribute aggregationKeyAttribute) {
		getCurrentSession().delete(aggregationKeyAttribute);
	}

}
