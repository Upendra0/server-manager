package com.elitecore.sm.agent.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="AgentDao")
public class AgentDaoImpl extends GenericDAOImpl<Agent> implements AgentDao{
	
	@Autowired
	ServicesDao servicesDao;
	
	@Autowired
	ServerInstanceDao serverInstanceDao;

	/**
	 * Creates the criteria conditions based on the input.
	 */
	@Override
	public Map<String, Object> createCriteriaConditions(int searchInstanceId,List<AgentType> agentTypeList) {
		Map<String, Object> returnMap = new HashMap<>(); 

		HashMap<String, String> aliases = new HashMap<>(); 

		List<Criterion> conditions = new ArrayList<>(); //sonar issue L-27
		 Disjunction objDisjunction = Restrictions.disjunction();
		for(int i=0;i<agentTypeList.size();i++){
			 objDisjunction.add(Restrictions.eq("agentType.id", agentTypeList.get(i).getId()));
		}
		conditions.add(objDisjunction);
		if (searchInstanceId!=0) {
			aliases.put("serverInstance", "s");
			conditions.add(Restrictions.eq("s.id", searchInstanceId));
		}
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	/**
	 * Get Agent List by ServerInstance Id
	 * 
	 * @param serverInstanceId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Agent> getAgentByServerInstanceIdAndAgentTypeID(int serverInstanceId,int agentTypeId) {
		List<Agent> systemAgentList;
		Criteria criteria = getCurrentSession().createCriteria(Agent.class);
		if (serverInstanceId != 0) {

			criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		}
		if(agentTypeId !=0) {
			
			criteria.add(Restrictions.eq("agentType.id", agentTypeId));
			
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		systemAgentList = (List<Agent>) criteria.list();

		return systemAgentList;
	}
	
	/**
	 * Fetch agent list for summary page
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Agent> getAgentPaginatedList(Class<Agent> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

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
		
		return  (List<Agent>)criteria.list();
	}
	
	/**
	 * Fetch Agent Full hierarchy
	 * @param agent
	 * @return
	 */
	@Override
	@Transactional
	public Agent getAgentFullHierarchy(Agent agent){
		
		if(agent instanceof PacketStatisticsAgent){
			logger.debug("inside getAgentFullHierarchy method for PacketStatisticsAgent");
			PacketStatisticsAgent pkgAgent=(PacketStatisticsAgent)agent;
			
			List<ServicePacketStatsConfig> svcPktStatList = pkgAgent.getServiceList();
			List<ServicePacketStatsConfig> newSvcPkyList=new ArrayList<>();
			if (svcPktStatList != null && !svcPktStatList.isEmpty()) {
				for (ServicePacketStatsConfig svcPktConfig : svcPktStatList) {
					if (svcPktConfig != null &&  !StateEnum.DELETED.equals(svcPktConfig.getStatus())) {
					Service service = svcPktConfig.getService();
					if (service != null &&  !StateEnum.DELETED.equals(service.getStatus())) {
						 servicesDao.getServiceForSNMPServiceThreshold(service.getId());
					}
					newSvcPkyList.add(svcPktConfig);
					}
				}
			}
			
			pkgAgent.setServiceList(newSvcPkyList);
			
		} else if(agent instanceof FileRenameAgent) {
			logger.debug("inside getAgentFullHierarchy method for FileRenameAgent");
			FileRenameAgent fileRenameAgent = (FileRenameAgent) agent;
			List<ServiceFileRenameConfig> serviceFileRenameConfigs = fileRenameAgent.getServiceList();
			if(serviceFileRenameConfigs != null && !serviceFileRenameConfigs.isEmpty()) {
				int length = serviceFileRenameConfigs.size();
				for(int i = length-1; i >=0; i--) {
					ServiceFileRenameConfig serviceFileRenameConfig = serviceFileRenameConfigs.get(i);
					if(serviceFileRenameConfig != null) {
						if(StateEnum.DELETED.equals(serviceFileRenameConfig.getStatus())) {
							serviceFileRenameConfigs.remove(i);
						} else {
							Service service = serviceFileRenameConfig.getService();
							if(service != null && service.getStatus().equals(StateEnum.DELETED)) {
								serviceFileRenameConfigs.remove(i);
								} else  {
								List<CharRenameOperation> charRenameOpList = serviceFileRenameConfig.getCharRenameOpList();
								if(charRenameOpList != null && !charRenameOpList.isEmpty()) {
									int charRenameOperationsLength = charRenameOpList.size();
									for(int j = charRenameOperationsLength-1; j >= 0; j--) {
										CharRenameOperation charRenameOperation = charRenameOpList.get(j);
										if(charRenameOperation != null && StateEnum.DELETED.equals(charRenameOperation.getStatus())) {
											charRenameOpList.remove(j);
										}
									}
								}
								serviceFileRenameConfig.setCharRenameOpList(charRenameOpList);
							}	
						}
					} 
				}
			}
			fileRenameAgent.setServiceList(serviceFileRenameConfigs);
		}
		
		return agent;
	}
	
	/**
	 * Mark  server instance child flag dirty , then update agent
	 */
	@Override
	public void update(Agent agent){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(agent.getServerInstance());
		
		getCurrentSession().merge(agent);
	}
	
	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(Agent agent){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(agent.getServerInstance());
		
		getCurrentSession().save(agent);
	}
	
	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(Agent agent){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(agent.getServerInstance());
		
		getCurrentSession().merge(agent);
		
		getCurrentSession().flush();
		
	}

}
