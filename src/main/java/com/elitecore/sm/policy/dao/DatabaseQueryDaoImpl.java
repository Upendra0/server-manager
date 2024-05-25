package com.elitecore.sm.policy.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.SearchDatabaseQuery;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

/**
 * @author Sagar Ghetiya
 *
 */
@Repository(value = "databaseQueryDao")
public class DatabaseQueryDaoImpl extends GenericDAOImpl<DatabaseQuery> implements IDatabaseQueryDao{
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseQuery> getAllQueriesByServerId(int serverInstanceId){
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQuery.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseQuery> getAllQueriesByServerIdAndtableName(int serverInstanceId,String tableName){
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQuery.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		if(!StringUtils.isEmpty(tableName) && tableName!=null) 
		  criteria.add(Restrictions.ilike("queryValue", "%"+tableName+"%"));
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		return criteria.list();
	}
	
	@Override
	public Map<String,Object> getQueryConditionList(SearchDatabaseQuery searchDatabaseQuery){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		if(StringUtils.isNotEmpty(searchDatabaseQuery.getQueryName())) {
			conditionList.add(Restrictions.like(BaseConstants.QUERY_NAME, StringUtils.trim(searchDatabaseQuery.getQueryName()) + "%").ignoreCase());
		}
		if(StringUtils.isNotEmpty(searchDatabaseQuery.getDescription())) {
			conditionList.add(Restrictions.like(BaseConstants.DESCRIPTION, StringUtils.trim(searchDatabaseQuery.getDescription()) + "%").ignoreCase());
		}
		conditionList.add(Restrictions.eq(BaseConstants.SERVER_INSTANC_ID, searchDatabaseQuery.getServerInstanceId()));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		if(!"ALL".equalsIgnoreCase(searchDatabaseQuery.getAssociationStatus())){
			conditionList.add(Restrictions.eq("associationStatus",searchDatabaseQuery.getAssociationStatus()));
		}
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@Override
	public int getDatabaseQueryCountByName(String queryName,int serverId){
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQuery.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("queryName",queryName));
		criteria.add(Restrictions.eq(BaseConstants.SERVER_INSTANC_ID, serverId));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public Map<String, Object> getQueryConditionListByServerId(int serverId){
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.SERVER_INSTANC_ID, serverId));
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DatabaseQuery getDatabaseQueryByAlias(String alias, int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQuery.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("alias",alias));
		criteria.add(Restrictions.eq(BaseConstants.SERVER_INSTANC_ID, serverId));
		List<DatabaseQuery> databaseQueries = criteria.list();
		return (databaseQueries != null && !databaseQueries.isEmpty()) ? databaseQueries.get(0) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DatabaseQuery> getAssociatedQueriesByServerId(int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(DatabaseQuery.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS,StateEnum.DELETED));
		criteria.add(Restrictions.eq("associationStatus",BaseConstants.ASSOCIATED));
		criteria.add(Restrictions.eq("cacheEnable",Boolean.TRUE));
		return criteria.list();
	}
}
