/**
 * 
 */
package com.elitecore.sm.common.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.sql.JoinType;

/**
 * This defines a set of common methods that all will be used by all *DAOImpl classes.
 * 
 * @author Sunil Gulabani
 * Mar 18, 2015
 */
public interface GenericDAO<T> {
	public List<T> getAllObject(Class<T> klass);

	public void save(T klass);

	public void update(T klass);

	public T findByPrimaryKey(Class<T> klass, Serializable id);

	public T getUniqueEntityByNamedQuery(String query, Object... params);

	public List<T> getListByNamedQuery(String query, Object... params);

	public void deleteObject(T klass);

	public Long getQueryCount(String query, Object... params);
	
	public Long getQueryCount(Class<T> klass,List<Criterion> conditions, Map<String,String> aliases);
	
	public Long getQueryCountByJoinType(Class<T> klass,List<Criterion> conditions, Map<String,String> aliases, JoinType joinType);
	
	public Long getTotalCountUsingNamedQuery(String namedQueryName, Map<String,Object> conditions);
	
	public List<Map<String,Object>> getListUsingNamedQuery(String namedQueryName, Map<String,Object> conditions, int offset,int limit, String sortColumn, String sortOrder);
	
	public Long getTotalCountUsingSQL(String sql, Map<String,Object> conditions);
	
	public Long getTotalCountUsingHQL(String sql);
	
	public List<Map<String,Object>> getListUsingSQL(String sql, Map<String,Object> conditions, int offset,int limit);
	
	public List<Object[]> getListUsingHQL(String hql, int offset,int limit);
	
	List<T> getPaginatedList(Class<T> klass,List<Criterion> conditions, Map<String,String> aliases, int start, int limit,String sidx, String sord);
	
	List<T> getPaginatedListByJoinType(Class<T> klass,List<Criterion> conditions, HashMap<String,String> aliases, int start, int limit,String sidx, String sord, JoinType joinType);
	
	public Object getSingleColumnValue(String nativeQuery);
	
	public void merge(T klass) ;
	
	public void evict(T klass);
}