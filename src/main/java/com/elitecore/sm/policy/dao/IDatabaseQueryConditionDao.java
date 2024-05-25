package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;

public interface IDatabaseQueryConditionDao extends GenericDAO<DatabaseQueryCondition>{
	
	public Map<String, Object> getConditionConditionsList(int queryId);
	
	public List<DatabaseQueryCondition> getAllDatabaseQueryConditionsByQueryId(int queryId);
}
