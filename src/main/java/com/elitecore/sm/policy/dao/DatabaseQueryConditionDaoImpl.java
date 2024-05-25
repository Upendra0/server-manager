package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;


@Repository(value = "databaseQueryConditionDao")
public class DatabaseQueryConditionDaoImpl extends GenericDAOImpl<DatabaseQueryCondition> implements IDatabaseQueryConditionDao{
	
	@Override
	public Map<String, Object> getConditionConditionsList(int queryId){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.DATABASE_QUERY_ID, queryId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseQueryCondition> getAllDatabaseQueryConditionsByQueryId(int queryId){
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQueryCondition.class);
		criteria.add(Restrictions.eq("databaseQuery.id", queryId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}
}
