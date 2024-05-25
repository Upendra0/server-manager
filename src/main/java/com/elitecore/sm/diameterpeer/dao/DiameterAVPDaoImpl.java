package com.elitecore.sm.diameterpeer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;

@Repository(value="diameterAVPDao")
public class DiameterAVPDaoImpl extends GenericDAOImpl<DiameterAVP> implements DiameterAVPDao{

	@Autowired
	DiameterPeerDao peerDao;

	@Override
	public void save(DiameterAVP avp) {
		peerDao.merge(avp.getPeer());
		getCurrentSession().save(avp);
		
	}
	
	@Override
	public void merge(DiameterAVP avp) {
		peerDao.merge(avp.getPeer());
		getCurrentSession().merge(avp);
		
	}

	@Override
	public int getAVPCount(int attributeId, int vendorId, int peerId) {
		Criteria criteria = getCurrentSession().createCriteria(DiameterAVP.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		if (peerId > 0) {
			criteria.add(Restrictions.eq("peer.id", peerId));
			criteria.add(Restrictions.eq("vendorId", vendorId));
			criteria.add(Restrictions.eq("attributeId", attributeId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DiameterAVP> getAVPListByAttrVendor(int attributeId, int vendorId, int peerId) {
		Criteria criteria = getCurrentSession().createCriteria(DiameterAVP.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if (peerId > 0) {
			criteria.add(Restrictions.eq("peer.id", peerId));
			criteria.add(Restrictions.eq("vendorId", vendorId));
			criteria.add(Restrictions.eq("attributeId", attributeId));
		}
		return (List<DiameterAVP>)criteria.list();
	}
}


