package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.DatabaseQueryAction;

public interface IDatabaseQueryActionDao extends GenericDAO<DatabaseQueryAction>{

	public Map<String, Object> getActionConditionsList(int queryId);
	
	public List<DatabaseQueryAction> getAllDatabaseQueryActionsByQueryId(int queryId);
	
}
