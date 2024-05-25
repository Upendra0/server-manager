package com.elitecore.sm.diameterpeer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.services.dao.ServicesDao;

@Repository(value="diameterPeerDao")
public class DiameterPeerDaoImpl extends GenericDAOImpl<DiameterPeer> implements DiameterPeerDao {
	
	@Autowired
	private ServicesDao servicesDao;
	
	/**
	 * Mark service and server instance dirty , then update peer
	 */
	@Override
	public void update(DiameterPeer peer){
		servicesDao.merge(peer.getService());
		getCurrentSession().merge(peer);
	}
	
	/**
	 * Mark service and server instance dirty , then save peer
	 */
	@Override
	public void save(DiameterPeer peer){
		servicesDao.merge(peer.getService());
		getCurrentSession().save(peer);
	}
	
	/**
	 * Mark service and server instance dirty , then update peer
	 */
	@Override
	public void merge(DiameterPeer peer){
		servicesDao.merge(peer.getService());
		getCurrentSession().merge(peer);
	}
	
	/**
	 * Fetch Peer count based on name , for check unique peer name
	 */
	@Override
	public int getPeerCount(String peerName , int serviceId){
		Criteria criteria = getCurrentSession().createCriteria(DiameterPeer.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if (peerName != null) {
			criteria.add(Restrictions.ilike("name", peerName));
		} 
		if (serviceId > 0) {
			criteria.add(Restrictions.eq("service.id",serviceId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiameterPeer> getPeerListForService(int serviceId) {
		Criteria criteria = getCurrentSession().createCriteria(DiameterPeer.class);
		criteria.add(Restrictions.eq("service.id",serviceId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.addOrder(Order.desc("createdDate"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiameterPeer> getPeerListByName(String peerName, int serviceId)
	{
		Criteria criteria = getCurrentSession().createCriteria(DiameterPeer.class);
		criteria.add(Restrictions.eq("name", peerName));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if (serviceId > 0) {
			criteria.add(Restrictions.eq("service.id",serviceId));
		}
		return (List<DiameterPeer>)criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DiameterPeer getDiameterPeerById(int peerId)
	{
		DiameterPeer peer = null;
		Criteria criteria = getCurrentSession().createCriteria(DiameterPeer.class);
		criteria.add(Restrictions.eq("id", peerId));
		
		List<DiameterPeer> peerList =(List<DiameterPeer>)criteria.list(); 
		if (peerList != null && !peerList.isEmpty())
		{
			peer = peerList.get(0);
		}
		return peer;
	}
}
