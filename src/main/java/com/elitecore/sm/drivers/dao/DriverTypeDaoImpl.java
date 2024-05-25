package com.elitecore.sm.drivers.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;

/**
 * 
 * @author avani.panchal
 *
 */

@Repository(value="driverTypeDao")
public class DriverTypeDaoImpl extends GenericDAOImpl<DriverType> implements DriverTypeDao{
	
	/**
	 * Return list of enable collection driver
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DriverType> getAllDriverTypeList(DriverCategory driverCategory){
		Criteria criteria=getCurrentSession().createCriteria(DriverType.class);
		criteria.add(Restrictions.eq("category", driverCategory));
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		
		return criteria.list();
		
	}
	
	/**
	 *  Fetch Driver Type using Alias
	 */
	@Override
	@SuppressWarnings("unchecked")
	public DriverType getDriverTypeByAlias(String driverAlias){
		Criteria criteria=getCurrentSession().createCriteria(DriverType.class);
		criteria.add(Restrictions.eq("alias", driverAlias));
		
		List<DriverType> listDriverType=criteria.list();
		
		return (!listDriverType.isEmpty())?listDriverType.get(0):null;
	}

	/**
	 * Fetch only enabled driver type from db 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<DriverType> getEnableDriverTypeList(){
		Criteria criteria=getCurrentSession().createCriteria(DriverType.class);
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		
		return criteria.list();
		
	}
}
