package com.elitecore.sm.consolidationservice.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;

@Repository(value = "consolidationDefinitionDao")
public class ConsolidationDefinitionDaoImpl extends GenericDAOImpl<DataConsolidation>
		implements IConsolidationDefinitionDao {
	
	/**
	 * Get defination by service id
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataConsolidation> getConsolidationDefintionListByServiceId(int serviceId){
		Criteria criteria = getCurrentSession().createCriteria(DataConsolidation.class);
		criteria.add(Restrictions.eq("consService.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
}