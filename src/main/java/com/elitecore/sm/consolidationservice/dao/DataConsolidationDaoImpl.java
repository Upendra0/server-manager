/**
 * 
 */
package com.elitecore.sm.consolidationservice.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;


/**
 * @author Ranjitsinh Reval
 *
 */
@Repository
public class DataConsolidationDaoImpl extends GenericDAOImpl<DataConsolidation> implements DataConsolidationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<DataConsolidation> getDataConsolidationServicewiseCount(String name, int serviceId) {
		Criteria criteria = getCurrentSession().createCriteria(DataConsolidation.class);
		criteria.add(Restrictions.eq("consName", name));
		criteria.add(Restrictions.eq("consService.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}

}
