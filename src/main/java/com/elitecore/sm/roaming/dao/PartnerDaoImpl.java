package com.elitecore.sm.roaming.dao;



import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.roaming.model.Partner;

@Repository(value = "partnerDao")
public class PartnerDaoImpl extends GenericDAOImpl<Partner> implements PartnerDao  {


	@Override
	@Transactional
	public List<Partner> getPartnerByNameandLob(String partner, String lob) {
		Criteria createCriteria = getCurrentSession().createCriteria(Partner.class);
		createCriteria.add(Restrictions.eq("name",partner).ignoreCase());
		createCriteria.add(Restrictions.eq("lob", lob).ignoreCase());
		return createCriteria.list();
		
		 
		 
	}
	@Override
	@Transactional
	public long getTotalPartnerCountByName(Partner partner,String partnerName){
		partnerName = partnerName.concat("%");
		Criteria criteria = getCurrentSession().createCriteria(Partner.class);
		if(partnerName.trim().length() > 0){
		criteria.add(Restrictions.like("name", partnerName).ignoreCase());
		}
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
		
	}

	@Override
	@Transactional
	public List<Partner> getDevicesPaginatedList(Class<Partner> classInstance,String partnerName, int offset, int limit, String sortColumn, String sortOrder) {
		List<Partner> resultList;
		Criteria criteria = getCurrentSession().createCriteria(classInstance);
partnerName = partnerName.concat("%");
		logger.debug("Sort column =" + sortColumn);

		if (("desc").equalsIgnoreCase(sortOrder)) {
				criteria.addOrder(Order.desc(sortColumn));
		} else if (("asc").equalsIgnoreCase(sortOrder)) {
				criteria.addOrder(Order.asc(sortColumn));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		if(partnerName.trim().length() > 0){
		criteria.add(Restrictions.like("name", partnerName).ignoreCase());
		//criteria.add(Restrictions.like(propertyName, value, matchMode))
		}
		criteria.setFirstResult(offset);// first record is 2
		criteria.setMaxResults(limit);// records from 2 to (2+3) 5
		resultList = criteria.list();
		return resultList;
	}

}
