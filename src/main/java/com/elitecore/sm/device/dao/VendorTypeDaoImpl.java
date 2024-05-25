/**
 * 
 */
package com.elitecore.sm.device.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.device.model.VendorType;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="vendorTypeDao")
public class VendorTypeDaoImpl extends GenericDAOImpl<VendorType> implements VendorTypeDao{

	/**
	 *Method will fetch all device type master list. 
	 * @return List<DeviceType>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<VendorType> getAllVendorType() {
		logger.debug(">> getAllVendorType ");
		Criteria criteria=getCurrentSession().createCriteria(VendorType.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.debug("<< getAllVendorType ");
		return criteria.list();
	}

	/**
	 * Method will fetch all Vendor list by Device Type id.
	 * @param deviceTypeId
	 * @see com.elitecore.sm.device.dao.VendorTypeDao#getAllVendorListByDeviceTypeId(int)
	 * @return List<VendorType>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllVendorListByDeviceTypeId(int deviceTypeId) {
		logger.debug(">> getAllVendorListByDeviceTypeId " + deviceTypeId);
		List<Object[]> objectList ;
		StringBuilder hqlQuery = new StringBuilder(); 
		hqlQuery.append("SELECT DISTINCT v.id , v.name FROM Device d INNER JOIN  d.vendorType v WHERE d.status='"+StateEnum.ACTIVE+"' ");
		if(deviceTypeId > 0 || deviceTypeId == -1){
			hqlQuery.append(" AND d.deviceType.id='"+deviceTypeId+"' "); 
		}
		objectList = getCurrentSession().createQuery(hqlQuery.toString()).list();
		logger.debug("<< getAllVendorListByDeviceTypeId ");
		return objectList;
	}

	@Override
	public VendorType getVendorTypeById(int vendorTypeId) {
		logger.debug(">> getVendorTypeById :: deviceType id is  "  +  vendorTypeId);
		VendorType vendorType = findByPrimaryKey(VendorType.class, vendorTypeId);
		logger.debug("<< getVendorTypeById ");
		return vendorType;
	}

	/**
	 * Method will get vendor type count by name to check unique name of vendor type.
	 * @param name
	 * @return long
	 */
	@Override
	public long getVendorTypeCountByName(String name) {
		Criteria criteria = getCurrentSession().createCriteria(VendorType.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	
	/**
	 * Method will fetch Device details by device name.
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public VendorType getVendorTypeByName(String name) {
		logger.debug("Fetching VendorType by name  " + name);
		Criteria criteria = getCurrentSession().createCriteria(VendorType.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		List<VendorType> vendorTypeList = criteria.list();
		if(vendorTypeList != null && !vendorTypeList.isEmpty()){
			return vendorTypeList.get(0);
		}
		return null;
	}


}
