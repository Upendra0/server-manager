package com.elitecore.sm.drivers.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;

/**
 * 
 * @author avani.panchal
 *
 */
public interface DriverTypeDao extends GenericDAO<DriverType>{

	public List<DriverType> getAllDriverTypeList(DriverCategory driverCategory);
	
	public DriverType getDriverTypeByAlias(String driverAlias);
	
	public List<DriverType> getEnableDriverTypeList();
	
}
