/**
 * 
 */
package com.elitecore.sm.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.stat.Statistics;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.util.DateFormatter;

/**
 * This class implements all the set of common methods defined in GenericDAO<T>
 * which will be used by all the *DAOImpl classes.
 * 
 * @author Sunil Gulabani
 *         Mar 17, 2015
 */

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired(required = true)
	@Qualifier(value = "hibernate5AnnotatedSessionFactory")
	private SessionFactory sessionFactory;

	protected Statistics statistics;

	public GenericDAOImpl() {

	}

	/**
	 * Initializes the statistics
	 */
	private void initializeStatistics() {
		if (statistics == null) {
			statistics = sessionFactory.getStatistics();
			statistics.setStatisticsEnabled(true);
			
		}
	}

	
	
	/**
	 * Prints the Statistics of the query.
	 */
	protected void printStatistics() {
		initializeStatistics();
		
		logger.debug("\n\n*************************************************************************************");
		logger.debug("Fetch Count=" + statistics.getEntityFetchCount());
		logger.debug("Second Level Hit Count=" + statistics.getSecondLevelCacheHitCount());
		logger.debug("Second Level Miss Count=" + statistics.getSecondLevelCacheMissCount());
		logger.debug("Second Level Put Count=" + statistics.getSecondLevelCachePutCount());
		
		logger.debug("*************************************************************************************");
	}

	public void setSessionFactory(SessionFactory sf) {
		this.sessionFactory = sf;
		initializeStatistics();
	}

	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public Session openCurrentSession() {
		printStatistics();
		return sessionFactory.openSession();
	}

	
	
	
	/**
	 * Used to get all the rows
	 * 
	 * @param klass
	 *            defines the Class whose rows are to be returned.
	 * @return List of Model
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#getAllObject(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllObject(Class<T> klass) {
		return getCurrentSession().createQuery("from " + klass.getName()).list();
	}

	/**
	 * Used to save model into database.
	 * 
	 * @param klass
	 *            model instance
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#save(java.lang.Object)
	 */
	@Override
	public void save(T klass) {
		getCurrentSession().save(klass);
	}

	/**
	 * Used to update model into database.
	 * 
	 * @param klass
	 *            model instance
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#update(java.lang.Object)
	 */
	@Override
	public void update(T klass) {
		getCurrentSession().update(klass);
	}

	/**
	 * Finds the model based on the primary key.
	 * 
	 * @param klass
	 *            model class
	 * @param id
	 *            primary key column
	 * @return model
	 * @see com.elitecore.sm.common.dao.GenericDAO#findByPrimaryKey(java.lang.Class,
	 *      java.io.Serializable)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findByPrimaryKey(Class<T> klass, Serializable id) {
		return (T) getCurrentSession().get(klass, id);
	}

	/**
	 * Finds the model based on the NamedQuery and criteria defined.
	 * 
	 * @param query
	 *            NamedQuery name
	 * @param params
	 *            values for the criteria mentioned in the NamedQuery
	 * @return model
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#getUniqueEntityByNamedQuery(java.lang.String,
	 *      java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getUniqueEntityByNamedQuery(String query, Object... params) {
		Query q = getCurrentSession().getNamedQuery(query);
		int i = 1;
		String arg = "arg";
		for (Object o : params) {
			q.setParameter(arg + i, o);
			i++;
		}
		List<T> results = q.list();
		T foundentity = null;
		if (!results.isEmpty()) {
			// ignores multiple results
			foundentity = results.get(0);
		}
		return foundentity;
	}

	/**
	 * Get model rows using NamedQuery and Criteria
	 * 
	 * @param query
	 *            NamedQuery name
	 * @param params
	 *            values for the criteria mentioned in the NamedQuery
	 * @return List of Model
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#getListByNamedQuery(java.lang.String,
	 *      java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getListByNamedQuery(String query, Object... params) {
		Query q = getCurrentSession().getNamedQuery(query);
		int i = 1;
		String arg = "arg";
		if (params != null) {
			for (Object o : params) {
				if (o != null) {
					q.setParameter(arg + i, o);
					i++;
				}
			}
		}
		return (List<T>) q.list();
	}

	/**
	 * Deletes the model from database
	 * 
	 * @param klass
	 *            model
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#deleteObject(java.lang.Object)
	 */
	@Override
	public void deleteObject(T klass) {
		getCurrentSession().delete(klass);
		// Hard delete is not allowed, so dummy implementation provided. 
		//set respective flags and rename your entities in entity specific dao and then call update method of this class.
	}

	/**
	 * Provides the total count of the NamedQuery and criteria
	 * 
	 * @param query
	 *            NamedQuery name
	 * @param params
	 *            values for the criteria mentioned in the NamedQuery
	 * @return total count
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#getQueryCount(java.lang.String,
	 *      java.lang.Object[])
	 */
	@Override
	public Long getQueryCount(String query, Object... params) {
		Query q = getCurrentSession().getNamedQuery(query);
		int i = 1;
		String arg = "arg";
		Long count = (long) 0;
		if (params != null) {
			for (Object o : params) {
				if (o != null) {
					q.setParameter(arg + i, o);
					i++;
				}
			}
		}
		count = (Long) q.uniqueResult();
		return count;
	}

	/**
	 * It returns the total count based on the Model and conditions provided.
	 * 
	 * @param klass
	 * @param conditions
	 * @return Long
	 */
	@Override
	public Long getQueryCount(Class<T> klass, List<Criterion> conditions, Map<String, String> aliases) {
		Criteria criteria = getCurrentSession().createCriteria(klass);
		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	/**
	 * It returns the total count based on the Model and conditions provided.
	 * 
	 * @param klass
	 * @param conditions
	 * @param joinType
	 * @return Long
	 */
	@Override
	public Long getQueryCountByJoinType(Class<T> klass, List<Criterion> conditions, Map<String, String> aliases, JoinType joinType) {
		Criteria criteria = getCurrentSession().createCriteria(klass);
		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue(), joinType);
			}
		}

		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	/**
	 * It returns the total count based on the namedQuery and conditions
	 * provided.
	 * 
	 * @param namedQueryName
	 *            name of the named Query
	 * @param conditions
	 *            map of conditions to be added
	 * @return Long count of rows of records returned by this function
	 */
	@Override
	public Long getTotalCountUsingNamedQuery(String namedQueryName, Map<String, Object> conditions) {
		return getTotalCountUsingSQL(getCurrentSession().getNamedQuery(namedQueryName).getQueryString(), conditions);
	}

	/**
	 * This method is used to get the list based on the offset and conditions
	 * 
	 * @return List &lt; Map &lt; String,Object &gt;&gt;
	 */
	@Override
	public List<Map<String, Object>> getListUsingNamedQuery(String namedQueryName, Map<String, Object> conditions, int offset, int limit, String sortColumn, String sortOrder) {
		return getListUsingSQL(getCurrentSession().getNamedQuery(namedQueryName).getQueryString()
				+ " order by " + sortColumn + " " + sortOrder, conditions, offset, limit);
	}

	/**
	 * It returns the total count based on the query provided.
	 * 
	 * @param sql
	 * @param conditions
	 * 
	 */
	@Override
	public Long getTotalCountUsingSQL(String sql, Map<String, Object> conditions) {
		Long count = (long) 0;
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("count", LongType.INSTANCE);

		if (conditions != null) {
			for (String key : conditions.keySet()) {
				query.setParameter(key, conditions.get(key));
			}
		}
		count = (Long) query.uniqueResult();
		return count;
	}

	/**
	 * It returns the total count based on the query provided.
	 * 
	 * @param sql
	 * @param conditions
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long getTotalCountUsingHQL(String hqlQuery) {
		long count = 0;

		List<Object[]> liResult = (List<Object[]>) getCurrentSession().createQuery(hqlQuery).list();

		if (liResult != null) {
			return Long.valueOf(String.valueOf(liResult.size()));
		} else {
			return count;
		}
	}

	/**
	 * This method is used to get the list based on the offset and conditions
	 * 
	 * @param offset
	 *            defines the starting row index
	 * @param limit
	 *            defines how many number of rows to be fetched.
	 * @return List<Map<String,Object>>
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> getListUsingSQL(String nativeQuery, Map<String, Object> conditions, int offset, int limit) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		SQLQuery query = getCurrentSession().createSQLQuery(nativeQuery);

		if (conditions != null) {
			for (String key : conditions.keySet()) {
				query.setParameter(key, conditions.get(key));
			}
		}

		query.setFirstResult(offset);//first record is 2
		query.setMaxResults(limit);//records from 2 to (2+3) 5

		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		Map<String, Object> resultRow;
		for (Object object : data) {
			Map row = (Map) object;
			if (row != null) {
				resultRow = new HashMap<>();//sonar issue line 368
				for (Object key : row.keySet()) {
					if (row.get(key) instanceof Date)
						resultRow.put(key.toString().toLowerCase(), DateFormatter.formatDate((Date) row.get(key)));
					else
						resultRow.put(key.toString().toLowerCase(), row.get(key));
				}
				resultList.add(resultRow);
			}
		}
		return resultList;
	}

	/**
	 * This method is used to get the Object list based on the offset and limit.
	 * 
	 * @param offset
	 *            defines the starting row index
	 * @param limit
	 *            defines how many number of rows to be fetched.
	 * @return List<Object[]>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getListUsingHQL(String hqlQuery, int offset, int limit) {
		Query query = getCurrentSession().createQuery(hqlQuery);

		query.setFirstResult(offset);//first record is 2
		query.setMaxResults(limit);//records from 2 to (2+3) 5

		List<Object[]> objList = query.list();
		return objList;
	}

	@Override
	public Object getSingleColumnValue(String nativeQuery) {
		return getCurrentSession().createSQLQuery(nativeQuery).uniqueResult();
	}

	/**
	 * This method is used to get the Demo Model list based on the offset and
	 * sort order
	 * 
	 * @param offset
	 *            defines the starting row index
	 * @param limit
	 *            defines how many number of rows to be fetched.
	 * @param sortColumn
	 *            defines which column have sort criteria
	 * @param sortOrder
	 *            defines asc or desc order.
	 * @return List<Demo>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> getPaginatedList(Class<T> klass, List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {
		List<T> resultList;// sonar L416
		Criteria criteria = getCurrentSession().createCriteria(klass);
		if (BaseConstants.DESC.equals(sortOrder))
			criteria.addOrder(Order.desc(sortColumn));
		else if (BaseConstants.ASC.equals(sortOrder))
			criteria.addOrder(Order.asc(sortColumn));

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	/**
	 * This method is used to get the Demo Model list based on the offset and
	 * sort order
	 * 
	 * @param offset
	 *            defines the starting row index
	 * @param limit
	 *            defines how many number of rows to be fetched.
	 * @param sortColumn
	 *            defines which column have sort criteria
	 * @param sortOrder
	 *            defines asc or desc order.
	 * @return List<Demo>
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<T> getPaginatedListByJoinType(Class<T> klass, List<Criterion> conditions, HashMap<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder, JoinType joinType) {
		List<T> resultList = new ArrayList<>(); //sonar issue L 453
		Criteria criteria = getCurrentSession().createCriteria(klass);
		if (sortOrder.equalsIgnoreCase("desc"))
			criteria.addOrder(Order.desc(sortColumn));
		else if (sortOrder.equalsIgnoreCase("asc"))
			criteria.addOrder(Order.asc(sortColumn));

		if (conditions != null) {
			for (Criterion condition : conditions) {
				criteria.add(condition);
			}
		}
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));

		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue(), joinType);
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	/**
	 * Used to merge model into database.
	 * 
	 * @param klass
	 *            model instance
	 * 
	 * @see com.elitecore.sm.common.dao.GenericDAO#update(java.lang.Object)
	 */
	@Override
	public void merge(T klass) {
		getCurrentSession().merge(klass);
	}

	/**
	 * This method is used to remove object from session cache
	 * 
	 * @param Klass
	 */
	@Override
	public void evict(T Klass) {
		getCurrentSession().evict(Klass);
	}

}