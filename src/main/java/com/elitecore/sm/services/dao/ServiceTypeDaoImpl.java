/**
 * 
 */
package com.elitecore.sm.services.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.services.model.ServiceType;

/**
 * @author vandana.awatramani
 *
 */
@Repository(value = "ServiceTypeDao")
public class ServiceTypeDaoImpl extends GenericDAOImpl<ServiceType> implements ServiceTypeDao {

	/**
	 * Fetch Service based on type
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ServiceType getServiceTypeByAlias(String serviceAlias) {
		Criteria criteria = getCurrentSession().createCriteria(ServiceType.class);
		criteria.add(Restrictions.eq("alias", serviceAlias));

		List<ServiceType> listServiceType = criteria.list();

		return (!listServiceType.isEmpty()) ? listServiceType.get(0) : null;

	}

	/**
	 * Fetch only enabal service type from db
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ServiceType> getEnableServiceTypeList() {
		Criteria criteria = getCurrentSession().createCriteria(ServiceType.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));

		return (List<ServiceType>) criteria.list();
	}

	@Override
	public ServiceType getServiceIDbyAlias(String serviceAlias) {
		Criteria criteria = getCurrentSession().createCriteria(ServiceType.class);
		criteria.add(Restrictions.eq("alias", serviceAlias));
		return (ServiceType) criteria.list().get(0);
	}

	@Override
	public List<ServiceType> getServiceTypeList() {
		List<ServiceType> serviceTypeList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select a.alias, a.name, a.netServerTypeId, a.netServiceTypeId ");
		sb.append(" from NetServiceTypeData a where exists ( ");
		sb.append(" select 1 from NetServerInstanceData b, NetServiceInstanceData c ");
		sb.append(" where b.netServerTypeId = a.netServerTypeId ");
		sb.append(" and b.netServerId = c.netServerId ");
		sb.append(" and c.netServiceTypeId = a.netServiceTypeId ) ");
		Query query = getCurrentSession().createQuery(sb.toString());
		ServiceType serviceTypeData;
		Iterator iterator = query.list().iterator();
		while (iterator.hasNext()) {
			Object[] tuple = (Object[]) iterator.next();
			serviceTypeData = new ServiceType();

			serviceTypeData.setAlias((String) tuple[0]);
			serviceTypeData.setType((String) tuple[1]);
			serviceTypeData.setId((int) tuple[3]);

			serviceTypeList.add(serviceTypeData);
		}
		return serviceTypeList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceType> getAllServiceTypeList() {

		Criteria criteria = getCurrentSession().createCriteria(ServiceType.class);

		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));

		return (List<ServiceType>) criteria.list();
	}

}
