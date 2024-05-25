package com.elitecore.sm.policy.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.DatabaseQueryAction;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;
import com.elitecore.sm.policy.model.SearchDatabaseQuery;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author Sagar Ghetiya
 *
 */

public interface IDatabaseQueryService {
	
	public List<DatabaseQuery> getAllQueriesByServerId(int serverInstanceId);
	
	public List<DatabaseQuery> getAllQueriesByServerIdAndtableName(int serverInstanceId,String tableName);
	
	public long getQueryListCountByServerId(SearchDatabaseQuery searchDatabaseQuery);
	
	public ResponseObject createDatabaseQuery(DatabaseQuery databaseQuery, String serverInstanceId, int staffId);
	
	public ResponseObject updateDatabaseQuery(DatabaseQuery databaseQuery, String serverInstanceId, int staffId);
	
	public boolean isUniqueForUpdate(String name, String dbName,int id);
	
	public ResponseObject deleteQuery(String queryId, int staffId);
	
	public List<DatabaseQuery> getPaginatedList(int serverId, int startIndex, int limit, String sidx, String sord,boolean isSearch,SearchDatabaseQuery databaseQuery);
	
	public long getConditionListCountByQueryId(int queryId);
	
	public long getActionListCountByQueryId(int queryId);

	public List<DatabaseQueryCondition> getConditionPaginatedList(int queryId, int startIndex, int limit, String sidx,
			String sord);
	
	public List<DatabaseQueryAction> getActionPaginatedList(int queryId, int startIndex, int limit, String sidx,
			String sord);
	
	public void iterateOverDatabaseQuery(ServerInstance serverInstance, DatabaseQuery databaseQuery, boolean isImport);
	
	public void iterateOverDatabaseQueryConditions(List<DatabaseQueryCondition> databaseQueryConditions, boolean isImport,int staffId);
	
	public void iterateOverDatabaseQueryActions(List<DatabaseQueryAction> databaseQueryActions, boolean isImport, int staffId);
	
	public void setAssociationStatus(int serverId);

	public List<ImportValidationErrors>  validateDatabaseQueryForImport(DatabaseQuery databaseQuery,
			List<ImportValidationErrors> importErrorList, int importserverInstanceId);
	
	public List<DatabaseQuery> getAssociatedQueriesByServerId(int serverInstanceId);
}
