package com.elitecore.sm.policy.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.SearchDatabaseQuery;

/**
 * @author Sagar Ghetiya
 *
 */

public interface IDatabaseQueryDao extends GenericDAO<DatabaseQuery>{
	
	public List<DatabaseQuery> getAllQueriesByServerId(int serverId);

	public Map<String, Object> getQueryConditionList(SearchDatabaseQuery databaseQuery);
	
	public int getDatabaseQueryCountByName(String queryName,int serverId);
	
	public Map<String, Object> getQueryConditionListByServerId(int serverId);
	
	public DatabaseQuery getDatabaseQueryByAlias(String alias, int serverId);

	public List<DatabaseQuery> getAssociatedQueriesByServerId(int serverId);

	List<DatabaseQuery> getAllQueriesByServerIdAndtableName(int serverInstanceId, String tableName);
}
