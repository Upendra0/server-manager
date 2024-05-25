package com.elitecore.sm.rulelookup.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.rulelookup.model.RuleLookupTableConfiguration;

@Repository
public class RuleLookupTableConfigurationDaoImpl extends GenericDAOImpl<RuleLookupTableConfiguration> implements IRuleLookupTableConfigurationDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleLookupTableConfiguration> getListOfImmediateExecutionByViewName(String viewName) {
		
		Criteria criteria=getCurrentSession().createCriteria(RuleLookupTableConfiguration.class);
		
		criteria.add(Restrictions.eq("viewName", viewName));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("immediateExecution",true));
		
		return (List<RuleLookupTableConfiguration>)criteria.list();
	}

}
