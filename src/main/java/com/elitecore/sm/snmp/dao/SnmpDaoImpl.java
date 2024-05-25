package com.elitecore.sm.snmp.dao;

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
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertType;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPCommunityType;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServerType;
import com.elitecore.sm.snmp.model.SNMPVersionType;

/**
 * @author Jui Purohit Apr 26, 2016
 */

@Repository(value = "snmpDao")
public class SnmpDaoImpl extends GenericDAOImpl<SNMPServerConfig> implements SnmpDao {

	@Autowired
	SNMPAlertWrapperDao snmpAlertWrapperDao;

	@Autowired
	private ServerInstanceDao serverInstanceDao;

	/**
	 * Method will fetch all snmpServers by server instance id.
	 * 
	 * @param serverInstanceId
	 * @return Map
	 */
	@Override
	public Map<String, Object> getSnmpServerPaginatedList(int serverInstanceId) {

		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		if (serverInstanceId != 0) {
			aliases.put("serverInstance", "s");
			conditions.add(Restrictions.eq("s.id", serverInstanceId));
		}
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		conditions.add(Restrictions.eq("type", SNMPServerType.Self));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	/**
	 * Get the list of all the Snmp Server
	 * 
	 * @param classInstance
	 * @param conditions
	 * @param aliases
	 * @param offset
	 * @param limit
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServerConfig> getSnmpServerPaginatedList(Class<SNMPServerConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<SNMPServerConfig> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.createAlias("name", "name").addOrder(Order.desc("name"));
			} else if (("id").equals(sortColumn)) {
				criteria.addOrder(Order.desc("id"));
			} else {
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.createAlias("name", "name").addOrder(Order.asc("name"));
			} else if ("id".equals(sortColumn)) {
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
		criteria.add(Restrictions.eq("type", SNMPServerType.Self));

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
	 * Fetch Snmp Server Dependents
	 * 
	 * @param snmpServerId
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public SNMPServerConfig getSnmpServerById(int snmpServerId) {

		SNMPServerConfig snmpServer = null;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("id", snmpServerId));

		List<SNMPServerConfig> snmpServerList = (List<SNMPServerConfig>) criteria.list();

		if (snmpServerList != null && !snmpServerList.isEmpty()) {
			snmpServer = snmpServerList.get(0);
			snmpServer.getHostIP();
			snmpServer.getStatus().name();

		}

		return snmpServer;
	}

	/**
	 * Fetch Snmp Server count based on name , for check unique Snmp Server name
	 * 
	 * @param snmpServerName
	 * @return
	 */
	@Override
	public int getSnmpServerCount(String snmpServerName) {

		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		if (snmpServerName != null) {
			criteria.add(Restrictions.eq("name", snmpServerName));
		}

		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

	}

	/**
	 * Fetch Active SNMP Server Count for Specific ServerInstance
	 * 
	 * @param serverInstanceId
	 * @return
	 */
	@Override
	public int getActiveSnmpServerCount(int serverInstanceId) {

		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("type", SNMPServerType.Self));
		criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));

		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();

	}

	/**
	 * Fetch snmpServer count and object by name
	 * 
	 * @param snmpServerName
	 * @return List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SNMPServerConfig> getSnmpServerListByName(String snmpServerName) {

		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("name", snmpServerName));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		return (List<SNMPServerConfig>) criteria.list();
	}

	/**
	 * Get snmp alert Type object by service name
	 * 
	 * @param serviceAliasList
	 * @return List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SNMPAlertType> getSnmpAlertType(List<String> serviceAliasList) {
		List<SNMPAlertType> snmpAlertTypeList = null;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertType.class);

		if (serviceAliasList != null && !serviceAliasList.isEmpty()) {

			Object[] serviceList = serviceAliasList.toArray();
			criteria.add(Restrictions.in("alias", serviceList));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));

			snmpAlertTypeList = (List<SNMPAlertType>) criteria.list();

		}
		return snmpAlertTypeList;
	}

	/**
	 * Get Common AlertType objects
	 * 
	 * @return List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SNMPAlertType> getCommonSnmpAlertType() {
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertType.class);

		criteria.add(Restrictions.eq("name", "Server Instance"));
		criteria.add(Restrictions.eq("name", "Generic"));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));

		return (List<SNMPAlertType>) criteria.list();
	}

	/**
	 * Get AlertList by AlertType
	 * 
	 * @param snmpAlertType
	 * @return List
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<SNMPAlert> getSnmpAlertsByAlertType(List<SNMPAlertType> snmpAlertType) {
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlert.class);
		List<SNMPAlert> snmpAlert = null;
		if (snmpAlertType != null && !snmpAlertType.isEmpty()) {
			List<Integer> snmpAlertTypeid = new ArrayList<>();
			for (SNMPAlertType alertType : snmpAlertType) {
				snmpAlertTypeid.add(alertType.getId());

			}

			Object[] snmpAlertIdArr = snmpAlertTypeid.toArray();
			criteria.add(Restrictions.in("alertType.id", snmpAlertIdArr));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			snmpAlert = (List<SNMPAlert>) criteria.list();

		}
		return snmpAlert;
	}

	/**
	 * Method will fetch all snmp Client by server instance id.
	 * 
	 * @param serverInstanceId
	 * @return Map
	 */
	@Override
	public Map<String, Object> getSnmpClientPaginatedList(int serverInstanceId) {

		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditions = new ArrayList<>();

		if (serverInstanceId != 0) {
			logger.debug("inside getSnmpClientPaginatedList::serverInstanceId:-" + serverInstanceId);
			aliases.put("serverInstance", "s");
			conditions.add(Restrictions.eq("s.id", serverInstanceId));
		}
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		conditions.add(Restrictions.eq("type", SNMPServerType.Listener));

		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditions);

		return returnMap;
	}

	/**
	 * Get the list of all the Snmp Client
	 * 
	 * @param classInstance
	 * @param conditions
	 * @param aliases
	 * @param offset
	 * @param limit
	 * @param sortColumn
	 * @param sortOrder
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<SNMPServerConfig> getSnmpClientPaginatedList(Class<SNMPServerConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder) {

		List<SNMPServerConfig> resultList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(classInstance);

		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.createAlias("name", "name").addOrder(Order.desc("name"));
			} else if (("id").equals(sortColumn)) {
				criteria.addOrder(Order.desc("id"));
			} else {
				criteria.addOrder(Order.desc(sortColumn));
			}
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
			if (("name").equals(sortColumn)) {
				criteria.createAlias("name", "name").addOrder(Order.asc("name"));
			} else if ("id".equals(sortColumn)) {
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
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("id"));
		projectionList.add(Projections.property("name"));
		projectionList.add(Projections.property("hostIP"));
		projectionList.add(Projections.property("port")); 
		projectionList.add(Projections.property("community")); 
		projectionList.add(Projections.property("version")); 
		projectionList.add(Projections.property("advance"));
		projectionList.add(Projections.property("status"));
		projectionList.add(Projections.property("snmpV3AuthAlgorithm"));
		projectionList.add(Projections.property("snmpV3AuthPassword"));
		projectionList.add(Projections.property("snmpV3PrivAlgorithm"));
		projectionList.add(Projections.property("snmpV3PrivPassword"));
       
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Listener));
		criteria.setProjection(projectionList);
		criteria.setProjection(Projections.distinct(projectionList));
		
		if (aliases != null) {
			for (Entry<String, String> entry : aliases.entrySet()) {
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		
		List list = criteria.list();
		Iterator itr = list.iterator();
		
		while(itr.hasNext()) {
			int counter = 0;
			Object[] obj = (Object[])itr.next();
			SNMPServerConfig snmpConfig = new SNMPServerConfig();
			snmpConfig.setId((int)obj[counter++]);
			snmpConfig.setName((String)obj[counter++]);
			snmpConfig.setHostIP((String)obj[counter++]);
			snmpConfig.setPort((String)obj[counter++]);
			snmpConfig.setCommunity((String)obj[counter++]);
			snmpConfig.setVersion((SNMPVersionType)obj[counter++]);
			snmpConfig.setAdvance((Boolean)obj[counter++]);
			snmpConfig.setStatus((StateEnum)obj[counter++]);
			
			snmpConfig.setSnmpV3AuthAlgorithm((String)obj[counter++]);
			snmpConfig.setSnmpV3AuthPassword((String)obj[counter++]);
			snmpConfig.setSnmpV3PrivAlgorithm((String)obj[counter++]);
			snmpConfig.setSnmpV3PrivPassword((String)obj[counter++]);
			
			resultList.add(snmpConfig);
		}
		return resultList;
	}

	/**
	 * Get Common AlertType objects
	 * 
	 * @return SNMPServerConfig
	 */
	@Override
	public SNMPServerConfig getConfiguredAlertListByClientId(int clientId) {
		SNMPServerConfig snmpClient = findByPrimaryKey(SNMPServerConfig.class, clientId);
		List<SNMPAlertWrapper> wrapperList = snmpAlertWrapperDao.getWrapperListByClientId(clientId);
		snmpClient.setConfiguredAlerts(wrapperList);
		return snmpClient;
	}

	/**
	 * Get cLIENT List by ServerInstance Id
	 * 
	 * @param serverInstanceId
	 * @return List
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<SNMPServerConfig> getClientListByServerInstanceId(int serverInstanceId) {
		List<SNMPServerConfig> snmpClientList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		if (serverInstanceId != 0) {

			criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Listener));
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("id"));
       
		criteria.setProjection(projectionList);
		criteria.setProjection(Projections.distinct(projectionList));

		List list = criteria.list();
		Iterator itr = list.iterator();
		
		while(itr.hasNext()) {
			SNMPServerConfig snmpConfig = findByPrimaryKey(SNMPServerConfig.class, (int)itr.next());
			snmpClientList.add(snmpConfig);
		}

		return snmpClientList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public SNMPServerConfig getSnmpClientfullHierarchyWithoutMarshlling(int clientId) {
		logger.debug("Inside getSnmpClientfullHierarchyWithoutMarshlling for client Id " + clientId);

		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("id", clientId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Listener));

		List<SNMPServerConfig> clientList = (List<SNMPServerConfig>) criteria.list();
		SNMPServerConfig clientDetail = clientList.get(0);

		if (clientDetail != null) {
			
			Hibernate.initialize(clientDetail.getServerInstance());

			List<SNMPAlertWrapper> snmpWrapperList = snmpAlertWrapperDao.getAllActiveSNMPAlertWrapperByServerConfig(clientDetail.getId());
			if(snmpWrapperList != null && !snmpWrapperList.isEmpty()){
				for (SNMPAlertWrapper alertWrapper : snmpWrapperList) {
					if (!StateEnum.DELETED.equals(alertWrapper.getStatus())) {
						Hibernate.initialize(alertWrapper.getAlert());
						logger.debug("Going to fetch Service Threshold detail ");
						snmpAlertWrapperDao.irerateOverSnmpWrapper(alertWrapper);
					}
				}
				clientDetail.setConfiguredAlerts(snmpWrapperList);
			}else {
				clientDetail.setConfiguredAlerts(null);
			}
		}
		return clientDetail;
	}

	/**
	 * 
	 * @param serverInstanceId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServerConfig> getServerListByServerInstanceId(int serverInstanceId) {
		List<SNMPServerConfig> snmpClientList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		if (serverInstanceId != 0) {

			criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Self));

		snmpClientList = (List<SNMPServerConfig>) criteria.list();

		return snmpClientList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getAlertListWithThreshold(ServerInstance serverInstance, String jaxbXmlPath, String xsltFilePath, String engineSampleXmlPath) {
		String hql = "select distinct a.alertId,a.name,a.desc,c.service.id, c.threshold,c.serverInstance.id, d.servInstanceId"
				+ " from SNMPAlertWrapper b right join b.alert a left join b.serviceThreshold c left join c.service d";
		Query query = getCurrentSession().createQuery(hql);
		List<Object> results = query.list();
		logger.debug("Inside getAlertListWithThreshold " + results);
		return results;
	}

	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(SNMPServerConfig snmpConfig) {

		serverInstanceDao.markServerInstanceChildFlagDirty(snmpConfig.getServerInstance());

		getCurrentSession().save(snmpConfig);
	}

	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(SNMPServerConfig snmpConfig) {

		serverInstanceDao.markServerInstanceChildFlagDirty(snmpConfig.getServerInstance());

		getCurrentSession().merge(snmpConfig);

		getCurrentSession().flush();

	}

	/**
	 * Mark service own and server instance child flag dirty , then update
	 * service
	 */
	@Override
	public void update(SNMPServerConfig snmpConfig) {

		serverInstanceDao.markServerInstanceChildFlagDirty(snmpConfig.getServerInstance());

		getCurrentSession().merge(snmpConfig);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SNMPServerConfig getSnmpServerConfigByName(String snmpName) {
		List<SNMPServerConfig> snmpServerList;
		SNMPServerConfig snmpServerConfig = null;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("name", snmpName));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Self));

		snmpServerList = (List<SNMPServerConfig>) criteria.list();
		if (snmpServerList != null && !snmpServerList.isEmpty()) {
			snmpServerConfig = snmpServerList.get(0);
		}
		return snmpServerConfig;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SNMPServerConfig getSnmpClientConfigByName(String snmpName) {
		List<SNMPServerConfig> snmpServerList;
		SNMPServerConfig snmpServerConfig = null;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("name", snmpName));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("type", SNMPServerType.Listener));

		snmpServerList = (List<SNMPServerConfig>) criteria.list();
		if (snmpServerList != null && !snmpServerList.isEmpty()) {
			snmpServerConfig = snmpServerList.get(0);
		}
		return snmpServerConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServerConfig> getServerConfigList() {
		List<SNMPServerConfig> snmpServerList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServerConfig.class);
		criteria.add(Restrictions.eq("type", SNMPServerType.Listener));
		snmpServerList = (List<SNMPServerConfig>) criteria.list();
		return snmpServerList;
	}
}
