package com.elitecore.sm.agent.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author Harsh.Patel
 * @updated jui.purohit
 *
 */
@Repository(value="ServiceFileRenameConfigDao")
public class ServiceFileRenameConfigDaoImpl extends GenericDAOImpl<ServiceFileRenameConfig> implements ServiceFileRenameConfigDao{

	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	/**
	 * Method will get file rename agent data.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceFileRenameConfig> getFileRenameAgentPaginatedList(Class<ServiceFileRenameConfig> classInstance, int offset, int limit, String sortColumn, String sortOrder,int serverInstanceId){
		
		logger.info("fetching ServiceFileRenameConfig details :");		
		String hql = "SELECT sfrc FROM ServiceFileRenameConfig as sfrc WHERE sfrc.status =:fileRenameStatus AND sfrc.service.id in (SELECT service.id FROM Service as service WHERE service.serverInstance.id =:serverInstanceId AND service.status =:serviceStatus)";		
		Query query = getCurrentSession().createQuery(hql);
	
		query.setParameter("fileRenameStatus", StateEnum.ACTIVE);
		query.setParameter("serverInstanceId", serverInstanceId);
		query.setParameter("serviceStatus", StateEnum.ACTIVE);
		
		return query.list();
	}
	
	/**
	 * Method will give count of number of rows 
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getFileRenameAgentDetailsCount() {
		
		logger.debug("Fetching number of row count of file rename agent : ");
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();

		conditionList.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);

		logger.debug("<< getFileRenameAgentDetailsCount in ServiceFileRenameConfigDaoImpl ");
		return returnMap;
	}
	
	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance){

		serverInstanceDao.markServerInstanceFlagDirty(serverInstance);
		getCurrentSession().save(serviceFileRenameConfig);
	}

	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void merge(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance){

		serverInstanceDao.markServerInstanceFlagDirty(serverInstance);
		getCurrentSession().save(serviceFileRenameConfig);
	}

	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void update(ServiceFileRenameConfig serviceFileRenameConfig, ServerInstance serverInstance){

		serverInstanceDao.markServerInstanceFlagDirty(serverInstance);
		getCurrentSession().save(serviceFileRenameConfig);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")	
	public List<Service> getServicesforFileRenameAgent(int serverInstanceId){		

		logger.debug("Fetching all service file rename configured services for server instance id " +  serverInstanceId);
		
		String hql = "SELECT service FROM Service as service WHERE serverInstance.id =:serverInstanceId AND service.status=:serviceStatus AND service.id not in(SELECT sf.service.id FROM ServiceFileRenameConfig as sf where sf.status =:serviceFileRenameStatus)";		
		Query query = getCurrentSession().createQuery(hql);
		
		query.setParameter("serverInstanceId", serverInstanceId);
		query.setParameter("serviceStatus",  StateEnum.ACTIVE);
		query.setParameter("serviceFileRenameStatus", StateEnum.ACTIVE);
		
		return query.list();
	}
	
	@Override
	public ServiceFileRenameConfig getServiceDetailByServiceIdAndAgentId(int serviceId,int agentId){
		
		Criteria criteria=getCurrentSession().createCriteria(ServiceFileRenameConfig.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.eq("agent.id", agentId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (criteria.list() !=null && !criteria.list().isEmpty()) ? (ServiceFileRenameConfig)(criteria.list()).get(0) : null;
		
	}
}
