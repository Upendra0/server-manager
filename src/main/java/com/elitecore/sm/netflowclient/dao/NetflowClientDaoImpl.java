package com.elitecore.sm.netflowclient.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.services.dao.ServicesDao;


/**
 * @author vishal.lakhyani
 *
 */
@Repository(value="netflowClientDao")
public class NetflowClientDaoImpl extends GenericDAOImpl<NetflowClient> implements NetflowClientDao {

	
	@Autowired
	private ServicesDao servicesDao;
	
	/**
	 * Mark service and server instance dirty , then update client
	 */
	@Override
	public void update (NetflowClient client){
		
		servicesDao.merge(client.getService());
		
		getCurrentSession().merge(client);
		
	}
	
	/**
	 * Mark service and server instance dirty , then save client
	 */
	@Override
	public void save(NetflowClient client){
		
		servicesDao.merge(client.getService());
		
		getCurrentSession().save(client);
	}
	
	/**
	 * Mark service and server instance dirty , then update client
	 */
	@Override
	public void merge(NetflowClient client){
		
		servicesDao.merge(client.getService());
		
		getCurrentSession().merge(client);
	}

	/**
	 * Fetch list if Netflow client by service id
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<NetflowClient> getClientListForService(int serviceId) {

		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.addOrder(Order.desc("createdDate"));

		return criteria.list();
	}
	
	
	/**
	 * Fetch Netflow client by client name
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<NetflowClient> getClientListByName(String clientName, int serviceId) {

		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("name", clientName));
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		return (List<NetflowClient>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NetflowClient getClientByIpAndPort(String ipAddress, int port, int serviceId) {
		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.eq("clientIpAddress", ipAddress));
		if(port <= 0) {
			criteria.add(Restrictions.eq("clientPort", 0));
		} else {
			criteria.add(Restrictions.eq("clientPort", port));
		}
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		List<NetflowClient> clientList = criteria.list();
		return (!CollectionUtils.isEmpty(clientList)) ? clientList.get(0) : null;
	}
	
	/**
	 * Fetch Netflow client by id
	 * @param clientId
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public NetflowClient getNetflowClientById(int clientId){
		
		NetflowClient netflowClient=null;
		Criteria criteria=getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("id", clientId));
		
		List<NetflowClient> netflowClientList=(List<NetflowClient>)criteria.list();
		
		if(netflowClientList!=null && !netflowClientList.isEmpty()){
			netflowClient=netflowClientList.get(0);
		}
		return netflowClient;
	}
	
	/**
	 * Fetch Client count based on name , for check unique client name
	 */
	@Override
	public int getClientCount(String clientName, int serviceId){
				
		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		if (clientName != null) {
			criteria.add(Restrictions.eq("name", clientName));
		} 
		if (serviceId > 0) {
			criteria.add(Restrictions.eq("service.id", serviceId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 * This method is used to get the Demo Model list based on the offset and sort order
	 * 
	 * @param offset defines the starting row index 
	 * @param limit defines how many number of rows to be fetched.
	 * @param sortColumn defines which column have sort criteria
	 * @param sortOrder defines asc or desc order.
	 * @return List<Demo>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NetflowClient> getPaginatedList(Class<NetflowClient> klass, List<Criterion> conditions, Map<String,String> aliases, int offset,int limit,String sortColumn,String sortOrder){
		List<NetflowClient> resultList = new ArrayList<>();
		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		if(sortOrder.equalsIgnoreCase("desc"))
			criteria.addOrder(Order.desc(sortColumn));
		else if(sortOrder.equalsIgnoreCase("asc"))
			criteria.addOrder(Order.asc(sortColumn));

		if(conditions!=null){
			for(Criterion condition : conditions){
				criteria.add(condition);
			}
		}
		
		if(aliases!=null){
			for (Entry<String, String> entry : aliases.entrySet()){
				criteria.createAlias(entry.getKey(), entry.getValue());
			}
		}

		criteria.setFirstResult(offset);//first record is 2
		criteria.setMaxResults(limit);//records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NetflowClient> getClientListByKafkaDSId(int kafkaDSId,  int startIndex, int limit) {
		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("kafkaDataSourceConfig.id", kafkaDSId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.addOrder(Order.desc("kafkaDataSourceConfig.id"));
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		return (List<NetflowClient>)criteria.list();
	}
	
	@Override
	public int getClientCountByKafkaDSId(int kafkaDSId) {
		Criteria criteria = getCurrentSession().createCriteria(NetflowClient.class);
		criteria.add(Restrictions.eq("kafkaDataSourceConfig.id", kafkaDSId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return criteria.list().size();
	}

}
