package com.elitecore.sm.drivers.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;

/**
 * 
 * @author Chirag.Rathod
 *
 */
public interface DriverAttributeDao extends GenericDAO<DatabaseDistributionDriverAttribute>{

	public Map<String, Object> getAttributeConditionList(int driverId);

	public List<DatabaseDistributionDriverAttribute> getAllAttributeByDriverId(int driverId);

	public DatabaseDistributionDriverAttribute checkUniqueAttributeNameForUpdate(int driverId, String name);

}
