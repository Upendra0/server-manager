package com.elitecore.sm.aggregationservice.dao;

import org.springframework.stereotype.Repository;

import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.common.dao.GenericDAOImpl;

@Repository(value = "aggregationConditionDao")
public class AggregationConditionDaoImpl extends GenericDAOImpl<AggregationCondition>
		implements IAggregationConditionDao {
	
	@Override
	public void deleteAggregationCondition(AggregationCondition aggregationCondition) {
		getCurrentSession().delete(aggregationCondition);
	}

}
