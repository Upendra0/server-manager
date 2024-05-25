package com.elitecore.sm.snmp.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;

@Repository(value = "snmpServiceThresholdDao")
public class SNMPServiceThresholdDaoImpl extends GenericDAOImpl<SNMPServiceThreshold> implements SNMPServiceThresholdDao {

	@Autowired
	private ServerInstanceDao serverInstanceDao;

	/**
	 * Get Service threshold by Wrapper Id
	 * 
	 * @param snmpAlertWrapperId
	 * @param svcId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServiceThreshold> getSvcThresholdListByWrapperId(int snmpAlertWrapperId, int svcId) {
		List<SNMPServiceThreshold> snmpSvcThresholdList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServiceThreshold.class);
		if (snmpAlertWrapperId != 0) {

			criteria.add(Restrictions.eq(BaseConstants.WRAPPER_ID, snmpAlertWrapperId));
		}
		if (svcId != 0) {

			criteria.add(Restrictions.eq("service.id", svcId));
		}

		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		snmpSvcThresholdList = (List<SNMPServiceThreshold>) criteria.list();

		return snmpSvcThresholdList;
	}

	/**
	 * Get svc Threshold List by ServerInstance Id
	 * 
	 * @param snmpAlertWrapperId
	 * @param serverInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServiceThreshold> getSvcThresholdListByWrapperIdAndInstanceId(int snmpAlertWrapperId, int serverInstanceId) {
		List<SNMPServiceThreshold> snmpSvcThresholdList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServiceThreshold.class);
		if (snmpAlertWrapperId != 0) {

			criteria.add(Restrictions.eq(BaseConstants.WRAPPER_ID, snmpAlertWrapperId));
		}
		if (serverInstanceId != 0) {

			criteria.add(Restrictions.eq("serverInstance.id", serverInstanceId));
		}

		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		snmpSvcThresholdList = (List<SNMPServiceThreshold>) criteria.list();

		return snmpSvcThresholdList;
	}

	/**
	 * Get Svc Threshold List for Generic type Alerts
	 * 
	 * @param snmpAlertWrapperId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServiceThreshold> getThresholdListByWrapperId(int snmpAlertWrapperId) {
		List<SNMPServiceThreshold> snmpThresholdList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServiceThreshold.class);
		if (snmpAlertWrapperId != 0) {

			criteria.add(Restrictions.eq(BaseConstants.WRAPPER_ID, snmpAlertWrapperId));
		}

		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		snmpThresholdList = (List<SNMPServiceThreshold>) criteria.list();

		return snmpThresholdList;
	}

	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(SNMPServiceThreshold svcThreshold) {

		serverInstanceDao.markServerInstanceChildFlagDirty(svcThreshold.getWrapper().getListener().getServerInstance());

		getCurrentSession().save(svcThreshold);
	}

	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(SNMPServiceThreshold svcThreshold) {

		serverInstanceDao.markServerInstanceChildFlagDirty(svcThreshold.getWrapper().getListener().getServerInstance());

		getCurrentSession().merge(svcThreshold);

		getCurrentSession().flush();

	}

	/**
	 * Mark service own and server instance child flag dirty , then update
	 * service
	 */
	@Override
	public void update(SNMPServiceThreshold svcThreshold) {

		serverInstanceDao.markServerInstanceChildFlagDirty(svcThreshold.getWrapper().getListener().getServerInstance());

		getCurrentSession().merge(svcThreshold);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPServiceThreshold> getActiveServiceThresodeList(int snmpAlertWrapperId) {
		List<SNMPServiceThreshold> snmpThresholdList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPServiceThreshold.class);
		if (snmpAlertWrapperId != 0) {
			criteria.add(Restrictions.eq(BaseConstants.WRAPPER_ID, snmpAlertWrapperId));
		}
		criteria.createAlias("service", "s",JoinType.INNER_JOIN);
		criteria.add(Restrictions.ne("s."+BaseConstants.STATUS, StateEnum.DELETED));
		
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		snmpThresholdList = (List<SNMPServiceThreshold>) criteria.list();

		return snmpThresholdList;
	}

}
