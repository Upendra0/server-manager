package com.elitecore.sm.rulelookup.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;


@Repository(value="ruleLookUpTableDao")
public class RuleLookupTableDataDaoImpl extends GenericDAOImpl<RuleLookupTableData> implements IRuleLookupTableDataDao{
	
	@Override
	public int getCountByName(String name){
		Criteria criteria = getCurrentSession().createCriteria(RuleLookupTableData.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("viewName",name));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public Map<String,Object> getRuleTableConditionList(){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}

	@Override
	public Map<String, Object> getRuleForSearchTableConditionList(String searchName, String searchDesc) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(StringUtils.isNotBlank(searchName)){
			conditionList.add(Restrictions.like("viewName", "%" + StringUtils.trim(searchName) + "%").ignoreCase());
		}
		if(StringUtils.isNotBlank(searchDesc)){
			conditionList.add(Restrictions.like("description", "%" + StringUtils.trim(searchDesc) + "%").ignoreCase());
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RuleLookupTableData> getRuleLookUpTableList() {
		Criteria criteria = getCurrentSession().createCriteria(RuleLookupTableData.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}

	@Override
	public int getRuleLookTableIdByViewName(String viewName) {
		Criteria criteria = getCurrentSession().createCriteria(RuleLookupTableData.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("viewName",viewName));
		List<RuleLookupTableData> list = criteria.list();
		int id = 0;
		if(list!=null && !list.isEmpty()){
			id = list.get(0).getId();
		}
		return id;
	}
	
}
