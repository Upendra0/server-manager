package com.elitecore.sm.device.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.device.model.DeviceType;


/**
 * @author Ranjitsinh Reval
 *
 */

public interface DeviceTypeDao extends GenericDAO<DeviceType>{
	
	public List<DeviceType> getAllDeviceType();
	
	public DeviceType getDeviceTypeById(int deviceTypeId);
	
	public long getDeviceTypeCountByName(String name);
	
	public DeviceType getDeviceTypeByName(String name);
	
}
