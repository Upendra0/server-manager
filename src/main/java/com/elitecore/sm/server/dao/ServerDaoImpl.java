/**
 * 
 */
package com.elitecore.sm.server.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;

/**
 * @author Sunil Gulabani Jul 2, 2015
 */

@Repository(value = "serverDao")
public class ServerDaoImpl extends GenericDAOImpl<Server> implements ServerDao {

	/**
	 * Get list of server from database
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Server> getServerList() {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.addOrder(Order.asc("id"));
		return criteria.list();
	}

	/**
	 * Add Server Type in database.
	 */
	@Override
	public ServerType addServerType(ServerType serverType) {
		getCurrentSession().save(serverType);
		return serverType;
	}

	/**
	 * Get All Server Type From database.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ServerType> getAllServerTypeList() {
		Criteria criteria = getCurrentSession().createCriteria(ServerType.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return criteria.list();
	}

	/**
	 * Get Only Active Server Type From database.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ServerType> getActiveServerTypeList() {
		Criteria criteria = getCurrentSession().createCriteria(ServerType.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		return criteria.list();
	}

	/**
	 * Get the specific server type based on id.
	 */
	@Override
	public ServerType getServerType(int id) {
		return (ServerType) getCurrentSession().load(ServerType.class, id);
	}

	/**
	 * Provides the Server Count based on input.
	 */
	@Override
	public int getServerCount(String name, String ipAddress, int id) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		if (id != 0) {
			criteria.add(Restrictions.ilike("name", name.toLowerCase()));
			criteria.add(Restrictions.ne("id", id));
		} else {
			criteria.add(Restrictions.ilike("name", name.toLowerCase()));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	/**
	 * Provides the Server Count based on ip and serverType.
	 */
	@Override
	public int getServerCountByIpAndType(String ipAddress, int typeId, int id) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		if (id != 0) {
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
			criteria.add(Restrictions.ne("id", id));
		} else {
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 * Provides the Server Count based on ip and serverType.
	 */
	@Override
	public int getServerCountByIpAndTypeAndUtilityPort(String ipAddress, int typeId, int id, int port) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		if (id != 0) {
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
			criteria.add(Restrictions.eq("utilityPort", port));
			criteria.add(Restrictions.ne("id", id));
		} else {
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
			criteria.add(Restrictions.eq("utilityPort", port));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getAllServerByServerType(int serverTypeId) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		if(serverTypeId != 0){
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, serverTypeId));	
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id").as("id"));
		criteria.setProjection(projections);
		criteria.setResultTransformer(Transformers.aliasToBean(Server.class));
		return  criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Server getServerByServerIpAddress(String ipAddress) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		criteria.add(Restrictions.eq("ipAddress", ipAddress));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		List<Server> serverList = criteria.list();
		
		if(serverList != null && !serverList.isEmpty()){
			return serverList.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Provides the Server List based on ip and serverType.
	 * @param ipAddress
	 * @param typeId
	 * @return List<Server>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getServerListByIpAndType(String ipAddress, int typeId) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			
		return criteria.list();
	}
	
	@Override
	public List<Server> getServerListByIpAndTypeAndUtility(String ipAddress, int typeId,int utilityPort) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		    criteria.add(Restrictions.eq("utilityPort", utilityPort));
			criteria.add(Restrictions.ilike(BaseConstants.IPADDRESS, ipAddress.toLowerCase()));
			criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, typeId));
			criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
			
		return criteria.list();
	}
	
	
	/**
	 * Provides the Server List based on serverType.
	 * @param serverType
	 * @return List<Server>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Server> getServerListByServerType(int serverType) {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
		criteria.add(Restrictions.eq(BaseConstants.SERVERTYPE_ID, serverType));
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		return criteria.list();
	}

	@Override
	public Server getServer(int id) {
		return (Server) getCurrentSession().load(Server.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Long getContainerServerList(String ipaddr) {
		
		String hql = "SELECT COUNT(*) from Server "
				+ " where ipAddress=:ipAddress and containerenvironment=:containerenvironment " 
				+ " and status<>'DELETED' group by utilityPort ";
	 		
		Query query = getCurrentSession().createQuery(hql);          
		query.setParameter("ipAddress",ipaddr);
		query.setParameter("containerenvironment","Y");
		
		return  (long)query.list().size();
	 
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> getGroupServerIDList() {
		Criteria criteria = getCurrentSession().createCriteria(Server.class);
        Projection projection = Projections.property("groupServerId"); 
        criteria.setProjection(Projections.distinct(projection)); 
        List<String> groupServerList=criteria.list();
		return groupServerList;
	}
}