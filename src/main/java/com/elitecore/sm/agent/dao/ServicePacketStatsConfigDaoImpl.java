package com.elitecore.sm.agent.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="servicePacketStatsConfigDao")
@Transactional
public class ServicePacketStatsConfigDaoImpl extends GenericDAOImpl<ServicePacketStatsConfig> implements ServicePacketStatsConfigDao{
	
	@Autowired
	ServerInstanceDao serverInstanceDao;
	
	@Override
	public ServicePacketStatsConfig getServiceDetailByServiceIdAndAgentId(int serviceId,int agentId){
		
		Criteria criteria=getCurrentSession().createCriteria(ServicePacketStatsConfig.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.eq("agent.id", agentId));
		
		return (criteria.list() !=null && !criteria.list().isEmpty()) ? (ServicePacketStatsConfig)(criteria.list()).get(0) : null;
		
	}
	
	@Override
	public Map<String, Object> getPacketStatServicePaginatedList(int agentId) {

		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		if (agentId != 0) {
			aliases.put("agent", "a");
			conditions.add(Restrictions.eq("a.id", agentId));
		}
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServicePacketStatsConfig> getPacketStatServicePaginatedList(Class<ServicePacketStatsConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		
		List<ServicePacketStatsConfig> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
			 if (("id").equals(sortColumn)) {
				criteria.addOrder(Order.desc("id"));
			} else {
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			 if ("id".equals(sortColumn)) {
				criteria.addOrder(Order.asc("id"));
			} else {
				criteria.addOrder(Order.asc(sortColumn));
			}
		}

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

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public ServicePacketStatsConfig getPacketStatService(int packetStatasticServiceId) {

		ServicePacketStatsConfig packetStatasticService = null;
		
		Criteria criteria = getCurrentSession().createCriteria(ServicePacketStatsConfig.class);
		criteria.add(Restrictions.eq("id", packetStatasticServiceId));

		List<ServicePacketStatsConfig> packetStatasticServiceList = (List<ServicePacketStatsConfig>) criteria.list();
		

		if (packetStatasticServiceList != null && !packetStatasticServiceList.isEmpty()) {
			packetStatasticService = packetStatasticServiceList.get(0);
			packetStatasticService.isEnable();
		}
		
		return packetStatasticService;
	}
	
	/**
	 * Mark  server instance child flag dirty , then update agent
	 */
	@Override
	public void update(ServicePacketStatsConfig svcStat){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(svcStat.getAgent().getServerInstance());
		
		getCurrentSession().merge(svcStat);
	}
	
	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(ServicePacketStatsConfig svcStat){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(svcStat.getAgent().getServerInstance());
		
		getCurrentSession().save(svcStat);
	}
	
	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(ServicePacketStatsConfig svcStat){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(svcStat.getAgent().getServerInstance());
		
		getCurrentSession().merge(svcStat);
		
		getCurrentSession().flush();
		
	}

}
