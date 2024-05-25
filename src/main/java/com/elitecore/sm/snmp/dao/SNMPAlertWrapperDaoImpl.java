package com.elitecore.sm.snmp.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;

@Repository(value = "snmpAlertWrapperDao")
public class SNMPAlertWrapperDaoImpl extends GenericDAOImpl<SNMPAlertWrapper> implements SNMPAlertWrapperDao {

	@Autowired
	ServicesDao servicesDao;
	
	@Autowired
	SNMPServiceThresholdDao snmpServiceThresholdDao;
	
	@Autowired
	private ServerInstanceDao serverInstanceDao;

	/**
	 * Fetch Wrapper list by alert Id
	 * 
	 * @param alertId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getWrapperListByAlertId(int alertId) {
		List<SNMPAlertWrapper> snmpClientList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (alertId != 0) {

			criteria.add(Restrictions.eq("alert.id", alertId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		snmpClientList = (List<SNMPAlertWrapper>) criteria.list();

		return snmpClientList;
	}

	/**
	 * Fetch Wrapper list by client Id
	 * 
	 * @param clientId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getWrapperListByClientId(int clientId) {

		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (clientId != 0) {

			criteria.add(Restrictions.eq("listener.id", clientId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		return (List<SNMPAlertWrapper>) criteria.list();
	}
	
	/**
	 * Fetch Wrapper list by client Id
	 * 
	 * @param clientId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getAllWrapperListByClientId(int clientId) {

		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (clientId != 0) {

			criteria.add(Restrictions.eq("listener.id", clientId));
		}

		return (List<SNMPAlertWrapper>) criteria.list();
	}

	/**
	 * Fetch Wrapper list by alert Id and client Id
	 * 
	 * @param alertId
	 * @param clientId
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getWrapperListByAlertIdAndClientId(int alertId, int clientId) {
		List<SNMPAlertWrapper> snmpalertWrapperList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (alertId != 0) {

			criteria.add(Restrictions.eq("alert.id", alertId));
		}
		if (clientId != 0) {

			criteria.add(Restrictions.eq("listener.id", clientId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		snmpalertWrapperList = (List<SNMPAlertWrapper>) criteria.list();

		return snmpalertWrapperList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getAllWrapperListByAlertIdAndClientId(int alertId, int clientId) {
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (alertId != 0)
			criteria.add(Restrictions.eq("alert.id", alertId));
		if (clientId != 0) 
			criteria.add(Restrictions.eq("listener.id", clientId));
	//	criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return (List<SNMPAlertWrapper>) criteria.list();

	}

	@Override
	public void irerateOverSnmpWrapper(SNMPAlertWrapper alertWrapper) {

		if (alertWrapper != null) {
			Hibernate.initialize(alertWrapper.getAlert());
			if (alertWrapper.getServiceThreshold() != null) {
				List<SNMPServiceThreshold> serviceThresholdList =   snmpServiceThresholdDao.getActiveServiceThresodeList(alertWrapper.getId());
				if (serviceThresholdList != null && !serviceThresholdList.isEmpty()) {
					for (SNMPServiceThreshold threshold : serviceThresholdList) {
							Service service = threshold.getService();
							if(service != null){
								service.setSvcPathList(null);
								service.setMyDrivers(null);
							}
					}
					alertWrapper.setServiceThreshold(serviceThresholdList); 
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<SNMPAlertWrapper> getAllActiveSNMPAlertWrapperByServerConfig(int serverConfigId) {
		List<SNMPAlertWrapper> snmpalertWrapperList;
		Criteria criteria = getCurrentSession().createCriteria(SNMPAlertWrapper.class);
		if (serverConfigId != 0) {
			criteria.add(Restrictions.eq("listener.id", serverConfigId));
		}
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		snmpalertWrapperList = (List<SNMPAlertWrapper>) criteria.list();
		return snmpalertWrapperList;
	}
	/**
	 * Mark server instance dirty , then save service
	 */
	@Override
	public void save(SNMPAlertWrapper snmpAlertWrapper){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(snmpAlertWrapper.getListener().getServerInstance());
		
		getCurrentSession().save(snmpAlertWrapper);
	}
	
	/**
	 * Mark server instance dirty , then update service
	 */
	@Override
	public void merge(SNMPAlertWrapper snmpAlertWrapper){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(snmpAlertWrapper.getListener().getServerInstance());
		
		getCurrentSession().merge(snmpAlertWrapper);
		
		getCurrentSession().flush();
		
	}
	
	/**
	 * Mark service own and server instance child flag dirty , then update service
	 */
	@Override
	public void update(SNMPAlertWrapper snmpAlertWrapper){
		
		serverInstanceDao.markServerInstanceChildFlagDirty(snmpAlertWrapper.getListener().getServerInstance());
		
		getCurrentSession().merge(snmpAlertWrapper);
	}
	
	
}
