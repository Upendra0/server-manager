package com.elitecore.sm.device.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.services.model.Service;



/**
 * @author Ranjitsinh Reval
 *
 */
public interface DeviceDao extends GenericDAO<Device> {

	public List<Device> getAllDeviceList();
	
	public List<Device> getAllDeviceByVendorAndDeviceType(int deviceTypeId, int vendorId, String decodeType);
	
	public List<Device> getAllDeviceByDecodeType(String decodeType);
	
	public Device getDevicebyId(int deviceId);
	
	public String getHQLBySearchParameters(SearchDeviceMapping device,String actionType, String sidx, String sord);
	
	public long getDeviceCountByName(String name,String decodeType);
	
	public List<Device> getDeviceCountByNameForUpdate(String name,String decodeType);
	
	public List<Object[]> getDeviceDetailsById(int deviceId, String decodeType);
	
	public List<Object[]> getObjectListByQuery(String hqlQuery);
	
	public void iterateOverDevice(Device device);
	
	public List<DeviceType> getAllDeviceTypeIdsByDecodeType(String decodeType);
	
	public Device getDeviceByName(String name);
	
	public List<Integer> getAllDeviceIdsByDeviceTypeId(int deviceTypeId);
	
	public long getDeviceCountByDeviceId(Device device);
	
	public long getDeviceCountByVendorId(Device device);
	
	public Map<String, Object> getDeviceByDecodeType(SearchDeviceMapping searchDevice);
	
	public List<Device> getDevicesPaginatedList(Class<Device> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);
	
}