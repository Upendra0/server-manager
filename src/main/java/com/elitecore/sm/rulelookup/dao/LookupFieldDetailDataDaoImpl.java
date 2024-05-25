package com.elitecore.sm.rulelookup.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;


@Repository(value="lookupFieldDetailDataDao")
public class LookupFieldDetailDataDaoImpl extends GenericDAOImpl<LookupFieldDetailData> implements ILookupFieldDetailDataDao{
	
	@Override
	public Map<String,Object> getFieldListConditionList(int tableId){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.LOOKUP_TABLE_ID,tableId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<LookupFieldDetailData> getSortedLookUpFieldDetailData(int tableId){
		Criteria criteria = getCurrentSession().createCriteria(LookupFieldDetailData.class);
		criteria.add(Restrictions.eq(BaseConstants.LOOKUP_TABLE_ID,tableId));
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		return criteria.list();
	}
}
