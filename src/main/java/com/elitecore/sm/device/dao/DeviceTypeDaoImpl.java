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
import com.elitecore.sm.device.model.DeviceType;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="deviceTypeDao")
public class DeviceTypeDaoImpl extends GenericDAOImpl<DeviceType> implements DeviceTypeDao{

	/**
	 *Method will fetch all device type master list. 
	 * @return List<DeviceType>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DeviceType> getAllDeviceType() {
		logger.debug(">> getAllDeviceType ");
		Criteria criteria=getCurrentSession().createCriteria(DeviceType.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.debug("<< getAllDeviceType ");
		return criteria.list();
	}

	/**
	 * Method will get deviceType by id.
	 * @param deviceTypeId
	 * @return DeviceType
	 */
	@Override
	public DeviceType getDeviceTypeById(int deviceTypeId) {
		logger.debug(">> getDeviceTypeById :: deviceType id is  "  +  deviceTypeId);
		DeviceType deviceType = findByPrimaryKey(DeviceType.class, deviceTypeId);
		logger.debug("<< getDeviceTypeById ");
		return deviceType;
	}

	/**
	 * Method will get device type count by name to check unique name of device type.
	 * @param name
	 * @return long
	 */
	@Override
	public long getDeviceTypeCountByName(String name) {
		logger.debug("Going to fetch device type count for device type " + name);
		Criteria criteria = getCurrentSession().createCriteria(DeviceType.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		if (name != null) {
			criteria.add(Restrictions.eq("name",name).ignoreCase());
		} 
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	/**
	 * Method will fetch Device details by device name.
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DeviceType getDeviceTypeByName(String name) {
		logger.debug("Fetching DeviceType by name  " + name);
		Criteria criteria = getCurrentSession().createCriteria(DeviceType.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		List<DeviceType> deviceTypeList = criteria.list();
		if(deviceTypeList != null && !deviceTypeList.isEmpty()){
			return deviceTypeList.get(0);
		}
		return null;
	}

}
