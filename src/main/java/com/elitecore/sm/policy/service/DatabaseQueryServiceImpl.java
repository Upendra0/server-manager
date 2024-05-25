package com.elitecore.sm.policy.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IDatabaseQueryActionDao;
import com.elitecore.sm.policy.dao.IDatabaseQueryConditionDao;
import com.elitecore.sm.policy.dao.IDatabaseQueryDao;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.DatabaseQueryAction;
import com.elitecore.sm.policy.model.DatabaseQueryCondition;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.SearchDatabaseQuery;
import com.elitecore.sm.policy.validator.DatabaseQueryValidator;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author Sagar Ghetiya
 *
 */
@Service (value = "databaseQueryService")
public class DatabaseQueryServiceImpl implements IDatabaseQueryService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IDatabaseQueryConditionDao databaseQueryConditionDao;
	
	@Autowired
	IDatabaseQueryActionDao databaseQueryActionDao;
	
	@Autowired
	IDatabaseQueryDao databaseQueryDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@Autowired
	IPolicyConditionDao policyConditionDao;
	
	@Autowired
	IPolicyActionDao policyActionDao;
	
	@Autowired
	DatabaseQueryValidator databaseQueryValidator;
	
	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQuery>getAllQueriesByServerId(int serverInstanceId){
		return databaseQueryDao.getAllQueriesByServerId(serverInstanceId);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getQueryListCountByServerId(SearchDatabaseQuery searchDatabaseQuery) {
		Map<String, Object> databaseQueryConditions = databaseQueryDao.getQueryConditionList(searchDatabaseQuery);
		return databaseQueryDao.getQueryCount(DatabaseQuery.class, (List<Criterion>) databaseQueryConditions.get("conditions"),
				(HashMap<String, String>) databaseQueryConditions.get("aliases"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getConditionListCountByQueryId(int queryId){
		Map<String, Object> databaseQueryConditionsCondition = databaseQueryConditionDao.getConditionConditionsList(queryId);
		return databaseQueryConditionDao.getQueryCount(DatabaseQueryCondition.class,(List<Criterion>) databaseQueryConditionsCondition.get("conditions"),
				(HashMap<String, String>) databaseQueryConditionsCondition.get("aliases"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getActionListCountByQueryId(int queryId){
		Map<String, Object> databaseQueryActionsCondition = databaseQueryActionDao.getActionConditionsList(queryId);
		return databaseQueryActionDao.getQueryCount(DatabaseQueryAction.class,(List<Criterion>) databaseQueryActionsCondition.get("conditions"),
				(HashMap<String, String>) databaseQueryActionsCondition.get("aliases"));
	}
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_DATABASE_QUERY, actionType = BaseConstants.CREATE_ACTION,
	currentEntity = DatabaseQuery.class, ignorePropList = "serverInstance,databaseQueryConditions,databaseQueryActions,alias")
	public ResponseObject createDatabaseQuery(DatabaseQuery databaseQuery, String serverInstanceId, int staffId){
		ResponseObject responseObject = new ResponseObject();
		databaseQuery.setCreatedByStaffId(staffId);
		databaseQuery.setLastUpdatedByStaffId(staffId);
		databaseQuery.setCreatedDate(new Date());
		databaseQuery.setLastUpdatedDate(new Date());
		
		ServerInstance serverInstance = serverInstanceDao.getServerInstance(Integer.parseInt(serverInstanceId));
		if(serverInstance != null){
			databaseQuery.setServerInstance(serverInstance);
			int databaseQueryCount = databaseQueryDao.getDatabaseQueryCountByName(databaseQuery.getQueryName(),Integer.parseInt(serverInstanceId));
			if(databaseQueryCount > 0){
				logger.info("Duplicate database Query Found");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_QUERY_FOUND);
			}else{
				databaseQueryDao.save(databaseQuery);
				if(databaseQuery.getId() > 0){
					serverInstance.setSyncChildStatus(false);
					responseObject.setSuccess(true);
					responseObject.setObject(databaseQuery);
					responseObject.setResponseCode(ResponseCode.DATABASE_QUERY_ADD_SUCESS);
				}else{
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.DATABASE_QUERY_ADD_FAILURE);
				}
			}
		}
		else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.DATABASE_QUERY_ADD_FAILURE);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_DATABASE_QUERY, actionType = BaseConstants.UPDATE_ACTION,
	currentEntity = DatabaseQuery.class, ignorePropList = "serverInstance,alias,associationStatus")
	public ResponseObject updateDatabaseQuery(DatabaseQuery databaseQuery, String serverInstanceId, int staffId){
		ResponseObject responseObject = new ResponseObject();
		databaseQuery.setCreatedByStaffId(staffId);
		databaseQuery.setLastUpdatedByStaffId(staffId);
		databaseQuery.setCreatedDate(new Date());
		databaseQuery.setLastUpdatedDate(new Date());
		DatabaseQuery dbDatbaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, databaseQuery.getId());
		ServerInstance serverInstance = serverInstanceDao.getServerInstance(Integer.parseInt(serverInstanceId));
		if(dbDatbaseQuery!=null && isUniqueForUpdate(databaseQuery.getQueryName(),dbDatbaseQuery.getQueryName(),dbDatbaseQuery.getServerInstance().getId())){
			logger.info("duplicate query name found:" + databaseQuery.getQueryName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_QUERY_FOUND);
		}else if(dbDatbaseQuery != null){
			String oldDatbaseQueryAlias = dbDatbaseQuery.getAlias();
			String newDatbaseQueryAlias = databaseQuery.getAlias();
			if(!oldDatbaseQueryAlias.equals(newDatbaseQueryAlias)){
				List <PolicyCondition> policyConditions = policyConditionDao.getAllDynamicPolicyConditions(Integer.parseInt(serverInstanceId));
				if(policyConditions != null){
					for(PolicyCondition policyCondition : policyConditions ){
						if(dbDatbaseQuery.getId() == policyCondition.getDatabaseQuery().getId()){
							policyCondition.setValue(newDatbaseQueryAlias);
							policyCondition.setDatabaseQueryAlias(newDatbaseQueryAlias);
							policyConditionDao.update(policyCondition);
						}
					}
				}
				List <PolicyAction> policyActions = policyActionDao.getAllDynamicPolicyActions(Integer.parseInt(serverInstanceId));
				if(policyActions != null){
					for(PolicyAction policyAction : policyActions){
						if(dbDatbaseQuery.getId() == policyAction.getDatabaseQuery().getId()){
							String action = policyAction.getAction();
							if(action != null) {
								String[] aliasArray = action.split("=");
								policyAction.setAction(aliasArray[0]+"="+newDatbaseQueryAlias);
							}
							policyAction.setDatabaseQueryAlias(newDatbaseQueryAlias);
							policyActionDao.update(policyAction);
						}
					}
				}
			}			
			dbDatbaseQuery.setAlias(databaseQuery.getAlias());
			dbDatbaseQuery.setQueryName(databaseQuery.getQueryName());
			dbDatbaseQuery.setReturnMultipleRowsEnable(databaseQuery.isReturnMultipleRowsEnable());
			dbDatbaseQuery.setCacheEnable(databaseQuery.getCacheEnable());
			dbDatbaseQuery.setConditionExpression(databaseQuery.getConditionExpression());
			dbDatbaseQuery.setConditionExpressionEnable(databaseQuery.getConditionExpressionEnable());
			dbDatbaseQuery.setQueryValue(databaseQuery.getQueryValue());
			dbDatbaseQuery.setDescription(databaseQuery.getDescription());
			dbDatbaseQuery.setLogicalOperator(databaseQuery.getLogicalOperator());
			dbDatbaseQuery.setOutputDbField(databaseQuery.getOutputDbField());
			dbDatbaseQuery.setDatabaseQueryConditions(databaseQuery.getDatabaseQueryConditions());
			dbDatbaseQuery.setDatabaseQueryActions(databaseQuery.getDatabaseQueryActions());
			databaseQueryDao.merge(dbDatbaseQuery);
			Hibernate.initialize(databaseQuery);
			Hibernate.initialize(databaseQuery.getDatabaseQueryActions());
			Hibernate.initialize(databaseQuery.getDatabaseQueryConditions());
			serverInstance.setSyncChildStatus(false);
			responseObject.setSuccess(true);
			responseObject.setObject(databaseQuery);
			responseObject.setResponseCode(ResponseCode.DATABASE_QUERY_UPDATE_SUCCESS);
		}	
		return responseObject;
	}
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_DATABASE_QUERY, actionType = BaseConstants.DELETE_ACTION,
	currentEntity = DatabaseQuery.class, ignorePropList = "serverInstance,databaseQueryConditions,databaseQueryActions,alias,associationStatus")
	public ResponseObject deleteQuery(String queryId, int staffId){
		ResponseObject responseObject = new ResponseObject();
		int dbQueryId = Integer.parseInt(queryId);
		if(dbQueryId > 0){
			DatabaseQuery databaseQuery = databaseQueryDao.findByPrimaryKey(DatabaseQuery.class, dbQueryId);
			if(databaseQuery != null){
				ServerInstance serverInstance = databaseQuery.getServerInstance();
				databaseQuery.setLastUpdatedDate(new Date());
				databaseQuery.setLastUpdatedByStaffId(staffId);
				databaseQuery.setStatus(StateEnum.DELETED);
				List<DatabaseQueryAction> databaseQueryActions = databaseQuery.getDatabaseQueryActions();
				for(DatabaseQueryAction databaseQueryAction : databaseQueryActions){
					databaseQueryAction.setStatus(StateEnum.DELETED);
				}
				List<DatabaseQueryCondition> databaseQueryConditions = databaseQuery.getDatabaseQueryConditions();
				for(DatabaseQueryCondition databaseQueryCondition : databaseQueryConditions){
					databaseQueryCondition.setStatus(StateEnum.DELETED);
				}
				databaseQuery.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, databaseQuery.getAlias()));
				databaseQuery.setQueryName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, databaseQuery.getQueryName()));
				databaseQueryDao.merge(databaseQuery);
				serverInstance.setSyncChildStatus(false);
				responseObject.setSuccess(true);
				responseObject.setObject(databaseQuery);
				responseObject.setResponseCode(ResponseCode.QUERY_DELETE_SUCCESS);
			}else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.QUERY_DELETE_FAILURE);
			}
			
		}else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.QUERY_DELETE_FAILURE);
		}
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQuery> getPaginatedList(int serverId, int startIndex, int limit, String sidx, String sord,boolean isSearch,SearchDatabaseQuery databaseQuery){
		Map<String,Object> queryConditionList ;
		if(isSearch){
			queryConditionList = databaseQueryDao.getQueryConditionList(databaseQuery);
		}else{
			queryConditionList = databaseQueryDao.getQueryConditionListByServerId(serverId);
		}
		return databaseQueryDao.getPaginatedList(DatabaseQuery.class, (List<Criterion>) queryConditionList.get("conditions"),
				(HashMap<String, String>) queryConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQueryCondition> getConditionPaginatedList(int queryId, int startIndex, int limit, String sidx,
			String sord){
		Map<String, Object> queryConditionsConditionList = databaseQueryConditionDao.getConditionConditionsList(queryId);
		return databaseQueryConditionDao.getPaginatedList(DatabaseQueryCondition.class, (List<Criterion>) queryConditionsConditionList.get("conditions"), 
				(HashMap<String, String>) queryConditionsConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQueryAction> getActionPaginatedList(int queryId, int startIndex, int limit, String sidx,
			String sord){
		Map<String, Object> queryActionsConditionList = databaseQueryActionDao.getActionConditionsList(queryId);
		return databaseQueryActionDao.getPaginatedList(DatabaseQueryAction.class, (List<Criterion>) queryActionsConditionList.get("conditions"), 
				(HashMap<String, String>) queryActionsConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public boolean isUniqueForUpdate(String queryName, String dbName,int serverId){
		if(!queryName.equalsIgnoreCase(dbName) && databaseQueryDao.getDatabaseQueryCountByName(queryName,serverId) > 0){
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(rollbackFor = SMException.class)
	public void iterateOverDatabaseQuery(ServerInstance serverInstance, DatabaseQuery databaseQuery, boolean isImport){
		if (isImport) { // import call
			logger.debug("Import database Query");
			databaseQuery.setId(0);
			databaseQuery.setQueryName(databaseQuery.getQueryName());
			databaseQuery.setAlias(databaseQuery.getAlias());
			iterateOverDatabaseQueryActions(databaseQuery.getDatabaseQueryActions(), isImport, serverInstance.getLastUpdatedByStaffId());
			iterateOverDatabaseQueryConditions(databaseQuery.getDatabaseQueryConditions(), isImport,serverInstance.getLastUpdatedByStaffId());
			databaseQuery.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			databaseQuery.setCreatedDate(new Date());
			databaseQuery.setServerInstance(serverInstance);
			databaseQuery.setLastUpdatedDate(new Date());
			databaseQuery.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			databaseQueryDao.save(databaseQuery);
			
		}else{//delete call
			logger.debug("Delete database query for import and overwrite");
			databaseQuery.setQueryName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, databaseQuery.getQueryName()));
			databaseQuery.setAlias(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, databaseQuery.getAlias()));
			databaseQuery.setStatus(StateEnum.DELETED);
			iterateOverDatabaseQueryConditions(databaseQuery.getDatabaseQueryConditions(),isImport,serverInstance.getLastUpdatedByStaffId());
			iterateOverDatabaseQueryActions(databaseQuery.getDatabaseQueryActions(),isImport,serverInstance.getLastUpdatedByStaffId());
		}
		databaseQuery.setLastUpdatedDate(new Date());
	}
	
	@Override
	public void iterateOverDatabaseQueryConditions(List<DatabaseQueryCondition> databaseQueryConditions, boolean isImport,int staffId){
		if(isImport){
			for(DatabaseQueryCondition databaseQueryCondition : databaseQueryConditions){
				databaseQueryCondition.setId(0);
				databaseQueryCondition.setCreatedDate(new Date());
				databaseQueryCondition.setLastUpdatedDate(new Date());
				databaseQueryCondition.setCreatedByStaffId(staffId);
				databaseQueryCondition.setLastUpdatedByStaffId(staffId);
				}
		}
		else{//delete call
			logger.debug("Delete database query conditions for import and overwrite");
			for(DatabaseQueryCondition databaseQueryCondition : databaseQueryConditions){
				databaseQueryCondition.setStatus(StateEnum.DELETED);
				databaseQueryCondition.setLastUpdatedDate(new Date());
				databaseQueryCondition.setLastUpdatedByStaffId(staffId);
			}
		}
	}
	
	@Override
	public void iterateOverDatabaseQueryActions(List<DatabaseQueryAction> databaseQueryActions, boolean isImport,int staffId){
		if(isImport){
			for(DatabaseQueryAction action : databaseQueryActions){
				action.setId(0);
				action.setCreatedDate(new Date());
				action.setLastUpdatedDate(new Date());
				action.setCreatedByStaffId(staffId);
				action.setLastUpdatedByStaffId(staffId);
				}
		}
		else{
			logger.debug("Delete database query actions for import and overwrite");
			for(DatabaseQueryAction action : databaseQueryActions){
				action.setStatus(StateEnum.DELETED);
				action.setLastUpdatedDate(new Date());
				action.setLastUpdatedByStaffId(staffId);
			}
		}
	}
	
	@Transactional
	@Override
	public void setAssociationStatus(int serverId){
		List<PolicyAction> policyActions = policyActionDao.getAllDynamicPolicyActions(serverId);
	    List<PolicyCondition> policyConditions = policyConditionDao.getAllDynamicPolicyConditions(serverId);
	    List<DatabaseQuery> databaseQueryList = databaseQueryDao.getAllQueriesByServerId(serverId);
	    boolean flag = true;
	    for(DatabaseQuery databaseQuery : databaseQueryList){
		    for(PolicyCondition policyCondition : policyConditions){
		    	if(StringUtils.isNotBlank(policyCondition.getValue())&&policyCondition.getValue().equalsIgnoreCase(databaseQuery.getQueryName())){
		    		databaseQuery.setAssociationStatus(BaseConstants.ASSOCIATED);
		    		flag = false;
		    		break;
		    	}
		    }
		    for(PolicyAction policyAction : policyActions){
		    	if(StringUtils.isNotBlank(policyAction.getAction())){
		    		String[] parts = policyAction.getAction().split("=");
		    		if(parts.length>1&&StringUtils.isNotBlank(parts[1])&&parts[1].equalsIgnoreCase(databaseQuery.getQueryName())){
		    			databaseQuery.setAssociationStatus(BaseConstants.ASSOCIATED);
		    			flag = false;
		    		}
		    	}
		    }
		    if(flag){
		    	databaseQuery.setAssociationStatus(BaseConstants.NONASSOCIATED);
		    }
		    databaseQueryDao.merge(databaseQuery);
	    }
	}

	@Override
	public List<ImportValidationErrors> validateDatabaseQueryForImport(DatabaseQuery databaseQuery,
			List<ImportValidationErrors> importErrorList, int serverId) {
		//databaseQueryValidator.validateNameForUniqueness(databaseQuery,null,importErrorList,null,true,serverId);
		return importErrorList;
	}

	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQuery> getAssociatedQueriesByServerId(int serverInstanceId) {
		return databaseQueryDao.getAssociatedQueriesByServerId(serverInstanceId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<DatabaseQuery>getAllQueriesByServerIdAndtableName(int serverInstanceId, String tableName){
		return databaseQueryDao.getAllQueriesByServerIdAndtableName(serverInstanceId,tableName);
	}
	
}
