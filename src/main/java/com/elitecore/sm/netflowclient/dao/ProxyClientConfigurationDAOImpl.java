package com.elitecore.sm.netflowclient.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.netflowclient.dao.ProxyClientConfigurationDAO;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;

@Repository
public class ProxyClientConfigurationDAOImpl extends GenericDAOImpl<NatFlowProxyClient>
		implements ProxyClientConfigurationDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<NatFlowProxyClient> getAllProxyClientByServiceId(int serviceId) {
		logger.info(">> getAllProxyClientByServiceId: " + serviceId);
		Criteria criteria = getCurrentSession().createCriteria(NatFlowProxyClient.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public NatFlowProxyClient getProxyClientById(int clientId) {

		NatFlowProxyClient natFlowProxyClient = null;
		Criteria criteria = getCurrentSession().createCriteria(NatFlowProxyClient.class);
		criteria.add(Restrictions.eq("id", clientId));

		List<NatFlowProxyClient> flowProxyClients = (List<NatFlowProxyClient>) criteria.list();

		if (flowProxyClients != null && !flowProxyClients.isEmpty()) {
			natFlowProxyClient = flowProxyClients.get(0);
		}
		return natFlowProxyClient;
	}

	@Override
	public boolean isUniqueProxyClientForUpdate(NatFlowProxyClient natFlowProxyClient) {
		NatFlowProxyClient natFlowProxyClient2 = getProxyClientById(natFlowProxyClient.getId());
		Criteria criteria = getCurrentSession().createCriteria(NatFlowProxyClient.class);
		criteria.add(Restrictions.eq("proxyIp", natFlowProxyClient.getProxyIp()));
		criteria.add(Restrictions.eq("proxyPort", natFlowProxyClient.getProxyPort()));
		criteria.add(Restrictions.eq("service.id", natFlowProxyClient.getService().getId()));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		return natFlowProxyClient2.getId() == natFlowProxyClient.getId() && (criteria.list().size() == 0 || ((NatFlowProxyClient)criteria.list().get(0)).getId() == natFlowProxyClient.getId());
	}

	@Override
	public NatFlowProxyClient getProxyClient(NatFlowProxyClient natFlowProxyClient) {
		Criteria criteria = getCurrentSession().createCriteria(NatFlowProxyClient.class);
		criteria.add(Restrictions.eq("proxyIp", natFlowProxyClient.getProxyIp()));
		criteria.add(Restrictions.eq("proxyPort", natFlowProxyClient.getProxyPort()));
		criteria.add(Restrictions.eq("service.id", natFlowProxyClient.getService().getId()));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		List<NatFlowProxyClient> natFlowProxyClients = criteria.list();
		if(natFlowProxyClients != null && !natFlowProxyClients.isEmpty())
			return natFlowProxyClients.get(0);
		return null;
	}

}
