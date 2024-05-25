package com.elitecore.sm.aggregationservice.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;

@Repository(value = "aggregationDefinitionDao")
public class AggregationDefinitionDaoImpl extends GenericDAOImpl<AggregationDefinition>
		implements IAggregationDefinitionDao {
	
	@Override
	public AggregationDefinition getAggregationDefintionListByServiceId(int serviceId){
		AggregationDefinition aggDefinition = null;
		Criteria criteria = getCurrentSession().createCriteria(AggregationDefinition.class);
		criteria.createAlias("aggregationService", "service");
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		Object result = criteria.uniqueResult();
        if (result != null) {
        	aggDefinition = (AggregationDefinition) result;
        }
		return aggDefinition;
	}
	
	@Override
	public AggregationDefinition getAggregationDefintionListByName(String defName){
		AggregationDefinition aggDefinition = null;
		Criteria criteria = getCurrentSession().createCriteria(AggregationDefinition.class);
		criteria.add(Restrictions.eq("aggDefName", defName));
		Object result = criteria.uniqueResult();
        if (result != null) {
        	aggDefinition = (AggregationDefinition) result;
        }
		return aggDefinition;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AggregationDefinition> getAllAggregationDefintionName(){
		List<AggregationDefinition> resultList;
		
		Criteria criteria = getCurrentSession().createCriteria(AggregationDefinition.class)
				.setProjection(Projections.property("aggDefName"));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		resultList =  criteria.list();		
		return resultList;
	}
	
	@Override
	public int getDefinitionNameCount(String defName){
				
		Criteria criteria = getCurrentSession().createCriteria(AggregationDefinition.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (org.apache.commons.lang3.StringUtils.isNotBlank(defName)) {
			criteria.add(Restrictions.eq("aggDefName", defName));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
}
