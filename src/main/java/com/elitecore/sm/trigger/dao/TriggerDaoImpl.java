/**
 * 
 */
package com.elitecore.sm.trigger.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;

@Repository(value="triggerDao")
public class TriggerDaoImpl extends GenericDAOImpl<CrestelSMTrigger> implements TriggerDao {


	@Override
	public Map<String, Object> getTriggerCount(String searchName, String searchType) {
		Map<String, Object> returnMap = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(StringUtils.isNotBlank(searchName)){
			conditionList.add(Restrictions.like(BaseConstants.TRIGGERNAME, "%" + StringUtils.trim(searchName) + "%").ignoreCase());
		}
		if(searchType!=null && !(BaseConstants.DEFAULTRECURRENCETYPE.equals(searchType))){
			conditionList.add(Restrictions.eq(BaseConstants.RECURRENCETYPE,searchType));
		}
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CrestelSMTrigger> getAllTriggerList() {
		logger.info(">> getAllTriggers ");
		Criteria criteria=getCurrentSession().createCriteria(CrestelSMTrigger.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.addOrder(Order.asc(BaseConstants.TRIGGERID));
		logger.info("<< getAllTriggers ");
		return criteria.list();
	}

	@Override
	public int getCountByName(String name) {
		Criteria criteria = getCurrentSession().createCriteria(CrestelSMTrigger.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.TRIGGERNAME,name));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CrestelSMTrigger getTriggerByName(String name) {
		CrestelSMTrigger trigger = null;
		Criteria criteria = getCurrentSession().createCriteria(CrestelSMTrigger.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.TRIGGERNAME,name));
		List<CrestelSMTrigger> triggerList = criteria.list();
		if(triggerList!=null && triggerList.size()!=0){
			trigger =triggerList.get(0);
		}
		return trigger;
	}
	
	@Override
	public int getCountByID(int id) {
		Criteria criteria = getCurrentSession().createCriteria(CrestelSMTrigger.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq(BaseConstants.TRIGGERID,id));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

}