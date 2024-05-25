package com.elitecore.sm.serverinstance.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.elitecore.sm.agent.dao.AgentDao;
import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.dao.IPolicyGroupDao;
import com.elitecore.sm.policy.dao.IPolicyGroupRelDao;
import com.elitecore.sm.policy.dao.IPolicyRuleDao;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;
import com.elitecore.sm.policy.model.PolicyCondition;
import com.elitecore.sm.policy.model.PolicyGroup;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.snmp.dao.SnmpDao;
import com.elitecore.sm.snmp.model.SNMPServerConfig;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value = "serverInstanceDao")
public class ServerInstanceDaoImpl extends GenericDAOImpl<ServerInstance> implements ServerInstanceDao {
	
	@Autowired
	ServicesDao servicesDao;
	
	@Autowired
	SnmpDao snmpDao;
	
	@Autowired
	AgentDao agentDao;

	/** The policy dao. */
	@Autowired
	IPolicyDao policyDao;
	
	/** The policy action dao. */
	@Autowired
	private IPolicyActionDao policyActionDao;
	
	/** The policy condition dao. */
	@Autowired
	private IPolicyConditionDao policyConditionDao;
	
	/** The policy group dao. */
	@Autowired
	IPolicyGroupDao policyGroupDao;
	
	/** The policy group rel dao. */
	@Autowired
	IPolicyGroupRelDao policyGroupRelDao;

	@Autowired
	IPolicyRuleDao policyRuleDao;
	

	/**
	 * Creates the criteria conditions based on the input.
	 */
	@Override
	public Map<String, Object> createCriteriaConditions(String serverTypeId,String searchInstanceName, String searchHost, String searchServerName, String searchPort,
			String searchSyncStatus,String dsid) {
		Map<String, Object> returnMap = new HashMap<>();

		HashMap<String, String> aliases = new HashMap<>();

		List<Criterion> conditions = new ArrayList<>();
		
		if (!StringUtils.isEmpty(serverTypeId)) {
			logger.info("serverTypeId: " + serverTypeId);
			serverTypeId = serverTypeId.trim();
			if(Integer.parseInt(serverTypeId)!=-1){
			aliases.put("server", "s");
			conditions.add(Restrictions.eq("s.serverType.id", Integer.parseInt(serverTypeId)));
			}
		}
		
		if (!StringUtils.isEmpty(searchInstanceName)) {
			logger.info("searchInstancename: " + searchInstanceName);
			searchInstanceName = searchInstanceName.trim();
			conditions.add(Restrictions.like("name", "%" + searchInstanceName + "%").ignoreCase());
		}

		if (!StringUtils.isEmpty(searchHost)) {
			logger.info("searchHost: " + searchHost);
			searchHost = searchHost.trim();
			aliases.put("server", "s");
			conditions.add(Restrictions.like("s.ipAddress", "%" + searchHost + "%"));
		}


		if (!StringUtils.isEmpty(searchServerName)) {
			logger.info("searchServerName: " + searchServerName);
			searchServerName = searchServerName.trim();
			aliases.put("server", "s");
			conditions.add(Restrictions.like("s.name", "%" + searchServerName + "%").ignoreCase());
		}

		if (!StringUtils.isEmpty(searchPort)) {
			searchPort = searchPort.trim();
			logger.info("searchPort: " + searchPort);
			conditions.add(Restrictions.eq("port", Integer.parseInt(searchPort)));
		}

		if (!StringUtils.isEmpty(searchSyncStatus) && !"undefined".equals(searchSyncStatus)) {
			searchSyncStatus = searchSyncStatus.trim();
			logger.info("searchSyncStatus: " + searchSyncStatus);
			if ("yes".equals(searchSyncStatus)) {
				logger.info("yes: ");
				Criterion c1 = Restrictions.eq("syncSIStatus", true);
				Criterion c2 = Restrictions.eq("syncChildStatus", true);
				conditions.add(Restrictions.and(c1, c2));
			} else if ("no".equals(searchSyncStatus)) {
				logger.info("no");
				Criterion c3 = Restrictions.eq("syncSIStatus", false);
				Criterion c4 = Restrictions.eq("syncChildStatus", false);
				conditions.add(Restrictions.or(c3, c4));
			}
		}
		if(!StringUtils.isEmpty(dsid)){
			
			Criterion rest2= 	Restrictions.eq("serverManagerDatasourceConfig.id",Integer.parseInt(dsid));
			Criterion rest3= 	Restrictions.eq("iploggerDatasourceConfig.id",Integer.parseInt(dsid));
			
			conditions.add(Restrictions.or(rest2,rest3));
			
		}
		conditions.add(Restrictions.ne("status",StateEnum.DELETED));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	
	/**
	 *  Fetch server instance full hierarchy with marshlling
	 * @param serverInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	
	public ServerInstance getServerInstanceFullHierarchyWithOutMarshlling(int serverInstanceId) throws SMException {
		logger.debug("Fetch Server Instance full hierarchy without using marshlling:: "+serverInstanceId);

		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.eq("id", serverInstanceId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		List<ServerInstance> instanceList = (List<ServerInstance>) criteria.list();
		ServerInstance serverInstance = instanceList.get(0);
		
		if(serverInstance!=null){
			
			List<DatabaseQuery> databaseQueryList = serverInstance.getDatabaseQueryList();
			if(databaseQueryList != null && !databaseQueryList.isEmpty()) {
				int databaseQueryListLength = databaseQueryList.size();
				for(int i = databaseQueryListLength-1; i >= 0; i--) {
					DatabaseQuery databaseQuery = databaseQueryList.get(i);
					if(databaseQuery != null && StateEnum.DELETED.equals(databaseQuery.getStatus())) {
						databaseQueryList.remove(i);
					}
				}
			}
			serverInstance.setDatabaseQueryList(databaseQueryList);
			
			Hibernate.initialize(serverInstance.getServer());
			Hibernate.initialize(serverInstance.getServer().getServerType());
			Hibernate.initialize(serverInstance.getServerManagerDatasourceConfig());
			Hibernate.initialize(serverInstance.getIploggerDatasourceConfig());
			Hibernate.initialize(serverInstance.getLogsDetail());
			
			if(serverInstance.getServices() !=null){
				List<Service> tempServiceList=new ArrayList<>();
				List<Service> serviceList=servicesDao.getServicesforServerInstance(serverInstanceId);
				
				for(Service service:serviceList){
					logger.debug("Inside for loop:");
					if(!StateEnum.DELETED.equals(service.getStatus())){
						logger.debug("Service object is :: " + service);
						int serviceId=service.getId();
						logger.debug("Going to fetch service full hierarchy for :: "+serviceId);
						tempServiceList.add(servicesDao.getServicefullHierarchyWithoutMarshlling(serviceId,service.getSvctype().getServiceFullClassName()));	
					}
				}
				serverInstance.setServices(tempServiceList);
			}
			
			if (serverInstance.getPolicyList() != null) {
				List<Policy> tempPolicyList = new ArrayList<>();
				List<Policy> policyList = policyDao.getPolicyforServerInstance(serverInstanceId);

				for (Policy policy : policyList) {
					if (!StateEnum.DELETED.equals(policy.getStatus())) {
						int policyId = policy.getId();
						logger.debug("Going to fetch policy :: " + policyId);
						tempPolicyList.add(policy);
					}
				}
				serverInstance.setPolicyList(tempPolicyList);
			}

			if (serverInstance.getPolicyGroupList() != null) {
				List<PolicyGroup> tempPolicyGroupList = new ArrayList<>();
				List<PolicyGroup> policyGroupList = policyGroupDao.getPolicyGroupforServerInstance(serverInstanceId);

				for (PolicyGroup policyGroup : policyGroupList) {
					if (!StateEnum.DELETED.equals(policyGroup.getStatus())) {
						int policyGroupId = policyGroup.getId();
						logger.debug("Going to fetch policy rule group :: " + policyGroupId);
						tempPolicyGroupList.add(policyGroup);
					}
				}
				serverInstance.setPolicyGroupList(tempPolicyGroupList);
			}

			if (serverInstance.getPolicyRuleList() != null) {
				List<PolicyRule> tempPolicyRuleList = new ArrayList<>();
				List<PolicyRule> policyRuleList = policyRuleDao.getPolicyRuleforServerInstance(serverInstanceId);

				for (PolicyRule policyRule : policyRuleList) {
					if (!StateEnum.DELETED.equals(policyRule.getStatus())) {
						int policyRuleId = policyRule.getId();
						logger.debug("Going to fetch rule :: " + policyRuleId);
						tempPolicyRuleList.add(policyRule);
					}
				}
				serverInstance.setPolicyRuleList(tempPolicyRuleList);
			}

			if (serverInstance.getPolicyConditionList() != null) {
				List<PolicyCondition> tempPolicyConditionList = new ArrayList<>();
				List<PolicyCondition> policyConditionList = policyConditionDao.getPolicyConditionforServerInstance(serverInstanceId);

				for (PolicyCondition policyCondition : policyConditionList) {
					if (!StateEnum.DELETED.equals(policyCondition.getStatus())) {
						int policyConditionId = policyCondition.getId();
						logger.debug("Going to fetch policy condition :: " + policyConditionId);
						tempPolicyConditionList.add(policyCondition);
					}
				}
				serverInstance.setPolicyConditionList(tempPolicyConditionList);
			}

			if (serverInstance.getPolicyActionList() != null) {
				List<PolicyAction> tempPolicyActionList = new ArrayList<>();
				List<PolicyAction> policyActionList = policyActionDao.getPolicyActionforServerInstance(serverInstanceId);

				for (PolicyAction policyAction : policyActionList) {
					if (!StateEnum.DELETED.equals(policyAction.getStatus())) {
						int policyActionId = policyAction.getId();
						logger.debug("Going to fetch policy action :: " + policyActionId);
						tempPolicyActionList.add(policyAction);
					}
				}
				serverInstance.setPolicyActionList(tempPolicyActionList);
			}

			if(serverInstance.getSelfSNMPServerConfig() !=null){
				logger.debug("Fetching snmp servers:");
				List<SNMPServerConfig> snmpServerList=snmpDao.getServerListByServerInstanceId(serverInstanceId);
				serverInstance.setSelfSNMPServerConfig(snmpServerList);
				
			}
			
			if(serverInstance.getSnmpListeners() !=null){
				List<SNMPServerConfig> tempSnmpListnerList=new ArrayList<>();
				List<SNMPServerConfig> snmpListnerList=snmpDao.getClientListByServerInstanceId(serverInstanceId);
				
				for(SNMPServerConfig snmpClient:snmpListnerList){
					logger.debug("Inside for loop:");
					
						logger.debug("snmpClient object is :: " + snmpClient);
						int clientId=snmpClient.getId();
						logger.debug("Going to fetch snmpClient full hierarchy for :: "+clientId);
						tempSnmpListnerList.add(snmpDao.getSnmpClientfullHierarchyWithoutMarshlling(clientId));	
					
				}
				serverInstance.setSnmpListeners(tempSnmpListnerList);
			}
			
			if(serverInstance.getAgentList() !=null){
				List<Agent> tempAgentList=new ArrayList<>();
				logger.debug("Fetching Agents detail:");
				List<Agent> agentList=agentDao.getAgentByServerInstanceIdAndAgentTypeID(serverInstanceId,0);
				for(Agent agent:agentList){
					logger.debug("Inside for loop for fetch agent detail");
					if(!StateEnum.DELETED.equals(agent.getStatus())){
						logger.debug("Going to fetch agent full hierarchy ");
						tempAgentList.add(agentDao.getAgentFullHierarchy(agent));	
					}
					
				}
				serverInstance.setAgentList(tempAgentList);
			}
		}

		return serverInstance;

	}

	/**
	 * Provides the Server Instance Count based on input.
	 */
	@Override
	public int getServerInstanceCount(String name, int port, int id, String ipAddress) {
		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		criteria.createAlias("server", "serv");
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if (id != 0) {
			criteria.add(Restrictions.or(Restrictions.or(Restrictions.ilike("name", name.toLowerCase()), Restrictions.ne("id", id)),
					Restrictions.and(Restrictions.ilike("serv.ipAddress", ipAddress.toLowerCase()), Restrictions.eq("port", port))));
		} else {
			 criteria.add(Restrictions.or(Restrictions.ilike("name",name.toLowerCase()),
					 Restrictions.and(Restrictions.ilike("serv.ipAddress", ipAddress.toLowerCase()), Restrictions.eq("port", port))));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}


	/**
	 * Get Detail of Server Instance for Synchronization
	 */
	@Override
	public ServerInstance getServerInstanceforSync(int id) {

		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.eq("id", id));
		return (ServerInstance) criteria.list().get(0);

	}

	/**
	 * Fetch only specific field from agent for sync
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Agent> getAgentListforServerInstance(int serverInsId) {

		Criteria criteria = getCurrentSession().createCriteria(Agent.class);
		criteria.add(Restrictions.eq("serverInstance.id", serverInsId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		return criteria.list();
	}

	/**
	 * Get Server Instance based on id.
	 */
	@Override
	public ServerInstance getServerInstance(int id) {
		return  (ServerInstance) getCurrentSession().load(ServerInstance.class, id);
	}

	/**
	 * Get's the server instance list based on server id. If server id is null,
	 * it will provide all server instances.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceByServerId(int serverId) {
		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		if (serverId != 0) {
			criteria.createAlias("server", "srv");
			criteria.add(Restrictions.eq("srv.id", serverId));
		}
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		List<ServerInstance> serverInstanceList = criteria.list();
		if (serverInstanceList != null && !serverInstanceList.isEmpty()) {
			for (ServerInstance serverInstance : serverInstanceList) {
				iterateOverServerInstance(serverInstance);
			}
		}
		return serverInstanceList;
	}
	
	/**
	 * Get's the active server instance list
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceList() {
		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		List<ServerInstance> serverInstanceList = criteria.list();
		if (serverInstanceList != null && !serverInstanceList.isEmpty()) {
			for (ServerInstance serverInstance : serverInstanceList) {
				iterateOverServerInstance(serverInstance);
			}
		}
		return serverInstanceList;
	}

	/**
	 * Iterates over server instance
	 * 
	 * @param serverInstance
	 */
	private void iterateOverServerInstance(ServerInstance serverInstance) {
		if (serverInstance != null) {
			serverInstance.getId();
			if (serverInstance.getServer() != null) {
				serverInstance.getServer().getId();
				serverInstance.getServer().getServerType();
			}
		}
	}

	/**
	 * Get paginated server instance list based on search criteria
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getPaginatedList(Class<ServerInstance> instance, List<Criterion> conditions, Map<String, String> aliases,
			int offset, int limit, String sortColumn, String sortOrder) {
		List<ServerInstance> resultList;
		Criteria criteria = getCurrentSession().createCriteria(instance);

		logger.debug("Sort column =" + sortColumn);
		if ("desc".equalsIgnoreCase(sortOrder)) {
			if ("server.name".equals(sortColumn))
				criteria.createAlias("server", "server").addOrder(Order.desc("server.name"));
			else if ("server.ipAddress".equals(sortColumn))
				criteria.createAlias("server", "server").addOrder(Order.desc("server.ipAddress"));
			else
				criteria.addOrder(Order.desc(sortColumn));
		} else if ("asc".equalsIgnoreCase(sortOrder)) {
			if ("server.name".equals(sortColumn))
				criteria.createAlias("server", "server").addOrder(Order.asc("server.name"));
			else if ("server.ipAddress".equals(sortColumn))
				criteria.createAlias("server", "server").addOrder(Order.asc("server.ipAddress"));
			else
				criteria.addOrder(Order.asc(sortColumn));
		}

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

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	/**
	 * Mark server instance own flag dirty , then update server instance
	 */
	@Override
	public void update(ServerInstance serverInstance) {

		serverInstance.setSyncSIStatus(false);

		getCurrentSession().merge(serverInstance);
	}

	/**
	 * Mark server instance own flag dirty , then save server instance
	 */
	@Override
	public void save(ServerInstance serverInstance) {

		serverInstance.setSyncSIStatus(false);

		getCurrentSession().save(serverInstance);
	}

	/**
	 * Mark server instance own flag dirty , then update server instance
	 */
	@Override
	public void merge(ServerInstance serverInstance) {

		serverInstance.setSyncSIStatus(false);

		getCurrentSession().merge(serverInstance);
	}

	/**
	 * Reset Sync flag for Server Instance
	 */
	@Override
	public void updateForResetSyncFlagofServerInstance(ServerInstance serverInstance) {

		super.merge(serverInstance);
	}
	
	/**
	 * Get server instance list using list of id's
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstancesUsingInCondition(List<String> serverInstancesId){
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.in("id", serverInstancesId));
		criteria.addOrder(Order.asc("createdDate"));
		return (List<ServerInstance>)criteria.list();
	}
	
	/**
	 * Mark server instance child flag dirty , when any operation perform  on service and its dependents
	 */
	@Override
	public void markServerInstanceChildFlagDirty(ServerInstance serverInstance){
		
		serverInstance.setSyncChildStatus(false);
		super.merge(serverInstance);
		
	}
	
	/**
	 * Method will mark server instance flag dirty.
	 *  (non-Javadoc)
	 * @see com.elitecore.sm.serverinstance.dao.ServerInstanceDao#markServerInstanceFlagDirty(com.elitecore.sm.serverinstance.model.ServerInstance)
	 */
	@Override
	public void markServerInstanceFlagDirty(ServerInstance serverInstance) {
		if(serverInstance != null){
			serverInstance.setSyncChildStatus(false);
			serverInstance.setSyncSIStatus(false);
			super.merge(serverInstance);
		}
		
	}
	
	/*get the server instance name list */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceByName(String serverInstanceName) {
		
		
		logger.debug("getServerInstanceByName"+serverInstanceName);
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.eq("name",serverInstanceName).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<ServerInstance>)criteria.list();
		
	}



/**
 * get the server instance port with ip address  list
 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceByIpaddressAndPort(int port, String ipAddress) {
	
		logger.debug("getServerInstanceByIpaddressAndPort"+port);
		logger.debug("getServerInstanceByIpaddressAndPort"+ipAddress);
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		criteria.createAlias("server", "serv");
		criteria.add(Restrictions.eq("port",port));
		criteria.add(Restrictions.eq("serv.ipAddress",ipAddress));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<ServerInstance>)criteria.list();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceListByDSId(int serverInstanceDSid) {
		
		logger.debug("getServerInstanceListByDSId "+serverInstanceDSid);
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);				
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<ServerInstance>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceListByAssociatedDSId(int serverInstanceDSid) {
		
		logger.debug("getServerInstanceListByAssociatedDSId "+serverInstanceDSid);
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		
		Criterion rest1 = Restrictions.eq("serverManagerDatasourceConfig.id",serverInstanceDSid);
		Criterion rest2 = Restrictions.eq("iploggerDatasourceConfig.id",serverInstanceDSid);
		Criterion rest3 = Restrictions.ne("status", StateEnum.DELETED);		
	
		criteria.add(Restrictions.and(Restrictions.or(rest1,rest2),rest3));		
		return (List<ServerInstance>)criteria.list();
	}
	

	/**
	 * Method will fetch all server instance by Server Type id.
	 *  (non-Javadoc)
	 * @see com.elitecore.sm.serverinstance.dao.ServerInstanceDao#getAllInstanceByServerTypeId(int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getAllInstanceByServerTypeId(Integer[] serverIds) {
		logger.debug("Fetching all server instances for server ids " +  serverIds);
		
		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.in("server.id", serverIds));
		return criteria.list(); 
		
	}
	
	
	
	@Override
	public List<ServerInstance> getServiceTypeServerList(String serviceTypeId) //throws DataManagerException 
	{
		List<ServerInstance> serverList = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			sb.append(" select a.netServerId, a.name ");
			sb.append(" from NetServerInstanceData a where exists ( ");
			sb.append(" select 1 from NetServiceTypeData c, NetServiceInstanceData b ");
			sb.append(" where b.netServiceTypeId = c.netServiceTypeId ");
			sb.append(" and b.netServerId = a.netServerId ");
			sb.append(" and c.netServiceTypeId = :servicetypeid ) ");
			
			Query query = getCurrentSession().createQuery(sb.toString());
			query.setString("servicetypeid", serviceTypeId);
			
			ServerInstance serverData;
			Iterator iterator= query.list().iterator();
		    while(iterator.hasNext()) {
		        Object[] tuple= (Object[]) iterator.next();
		        serverData = new ServerInstance();
		        
		        serverData.setId((int)tuple[0]);
		        serverData.setName((String)tuple[1]);
		        
		        serverList.add(serverData);
		    }
		return serverList;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerList(){
		
		List<ServerInstance> serverList = null;
			Criteria criteria =getCurrentSession().createCriteria(ServerInstance.class);
			criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
			serverList = criteria.list();
		return serverList;
	}


	@Override
	public void clearSession() {
		getCurrentSession().flush();
		getCurrentSession().clear();
	}
	
	/**
	 * Get's the server instance list based on server id. If server id is null,
	 * it will provide all server instances.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceByServerIdAndPort(int serverId,int port) {
		Criteria criteria = getCurrentSession().createCriteria(ServerInstance.class);
		if (serverId != 0) {
			criteria.createAlias("server", "srv");
			criteria.add(Restrictions.eq("srv.id", serverId));
		}
		criteria.add(Restrictions.eq("port", port));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
	
		return  criteria.list();
	}
	
	/*get the server instance name list */
	@SuppressWarnings("unchecked")
	@Override
	public List<ServerInstance> getServerInstanceBySerInsId(int serverInstanceId) {
		logger.debug("getServerInstanceById "+ serverInstanceId );
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		criteria.add(Restrictions.eq("id",serverInstanceId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<ServerInstance>)criteria.list();
		
	}
	
	@Override
	public List<ServerInstance> getIDByIpPortUtility(String ip,int port,int utilityPort) {
		Criteria criteria=getCurrentSession().createCriteria(ServerInstance.class);
		criteria.createAlias("server", "srv");
		criteria.add(Restrictions.eq("srv.ipAddress", ip));
		criteria.add(Restrictions.eq("srv.utilityPort", utilityPort));
		criteria.add(Restrictions.eq("port",port));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<ServerInstance>)criteria.list();
		
	}

}
