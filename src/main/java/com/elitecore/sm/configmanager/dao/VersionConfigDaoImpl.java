package com.elitecore.sm.configmanager.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.configmanager.model.VersionConfig;

@Repository(value = "versionConfigDao")
public class VersionConfigDaoImpl extends GenericDAOImpl<VersionConfig> implements VersionConfigDao{

	@Override
	public VersionConfig getVersionConfigIndex(int serverInstanceId) {
		VersionConfig versionConfig = null;
		logger.info(">> getVersionConfigIndex ");
		Criteria criteria=getCurrentSession().createCriteria(VersionConfig.class);
		criteria.add(Restrictions.eq("serverInstance.id",serverInstanceId));
		criteria.addOrder(Order.desc("id"));
		List<VersionConfig> list = criteria.list();
		if(list!=null && !list.isEmpty()){
			versionConfig = list.get(0);
		}
		logger.info("<< getVersionConfigIndex ");
		return versionConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<VersionConfig> getVersionConfigList(String serverInstanceId) {
		
		List<VersionConfig> resultList;
		Criteria criteria = getCurrentSession().createCriteria(VersionConfig.class);
		criteria.add(Restrictions.eq("serverInstance.id", Integer.parseInt(serverInstanceId)));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.addOrder(Order.desc("createdDate"));
		//criteria.setFirstResult(offset);
		//criteria.setMaxResults(limit);
		resultList = criteria.list();
		return resultList;
	}

	@Override
	public VersionConfig getVersionConfigObj(int id){
		Criteria criteria = getCurrentSession().createCriteria(VersionConfig.class);
		criteria.add(Restrictions.eq("id",id));
		return (VersionConfig) criteria.uniqueResult();
	}
	
	@Override
	public int getVersionConfigCount(int serverInstanceId) {
		Criteria criteria = getCurrentSession().createCriteria(VersionConfig.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		@SuppressWarnings("unchecked")
		List<VersionConfig> resultList=criteria.list();
		return resultList.size();
	}
}
