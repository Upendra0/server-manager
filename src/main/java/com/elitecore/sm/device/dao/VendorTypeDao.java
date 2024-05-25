/**
 * 
 */
package com.elitecore.sm.device.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.device.model.VendorType;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface VendorTypeDao extends GenericDAO<VendorType>{

	public List<VendorType> getAllVendorType();
	public List<Object[]> getAllVendorListByDeviceTypeId(int deviceTypeId);
	
	public VendorType getVendorTypeById(int vendorTypeId);
	
	public long getVendorTypeCountByName(String name);
	
	public VendorType getVendorTypeByName(String name);
}
