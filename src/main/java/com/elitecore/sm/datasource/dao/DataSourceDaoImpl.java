/**
 * 
 */
package com.elitecore.sm.datasource.dao;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author Sunil Gulabani
 * Jul 30, 2015
 * @author Keyur Raval
 * Jul 22, 2016
 * 
 */
@Repository(value = "dataSourceDao")
public class DataSourceDaoImpl extends GenericDAOImpl<DataSourceConfig> implements DataSourceDao{	
	@Autowired
	private ServerInstanceDao serverInstanceDao;
	
	/**
	 * Fetch DataSourceConfiguration by the object
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataSourceConfig> getAllObject()
	{
		Criteria criteria =getCurrentSession().createCriteria(DataSourceConfig.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<DataSourceConfig>)criteria.list();
	}
	
	/**
	 * Fetch DataSourceConfiguration by name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataSourceConfig> getDataBaseConfigurationName(String dbconfigName){
		Criteria criteria=getCurrentSession().createCriteria(DataSourceConfig.class);
		criteria.add(Restrictions.eq("name", dbconfigName).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<DataSourceConfig>)criteria.list();
	}
	
	/**
	 * Fetch DataSourceConfiguration by url,type and userName
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataSourceConfig> getDataBaseConfigurationByUrlAndUsernameAndType(String dbUserName,String dbUrl,String dbType){
		Criteria criteria=getCurrentSession().createCriteria(DataSourceConfig.class);
		criteria.add(Restrictions.eq("username", dbUserName).ignoreCase());
		criteria.add(Restrictions.eq("connURL", dbUrl).ignoreCase());
		criteria.add(Restrictions.eq("type", dbType).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if(criteria.list() != null) {
			return (List<DataSourceConfig>)criteria.list();	
		}
		return new ArrayList<>();
	}
	
	
	/**
	 * Mark server instance dirty , then update dataSource
	 */
	@Override
	public void merge(DataSourceConfig dataSourceConfig){
		
		List<ServerInstance> serverInstanceList = serverInstanceDao.getServerInstanceListByDSId(dataSourceConfig.getId());
		for(int i =0; i < serverInstanceList.size(); i++)
			serverInstanceDao.markServerInstanceChildFlagDirty(serverInstanceList.get(i));
		getCurrentSession().merge(dataSourceConfig);
		getCurrentSession().flush();
		
	}
	
	/**
	 * Fetch DataSourceConfiguration by the object
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DataSourceConfig> getDataBaseConfigurationByDSType(String dsType)
	{
		Criteria criteria =getCurrentSession().createCriteria(DataSourceConfig.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", dsType));
		return (List<DataSourceConfig>)criteria.list();
	}
}