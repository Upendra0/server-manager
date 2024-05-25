package com.elitecore.sm.device.service;

import java.util.List;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.SearchDeviceMapping;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface DeviceService {

	public List<Device> getAllDeviceList();
	
	public ResponseObject getAllDeviceByVendorAndDeviceType(int deviceTypeId, int vendorId, String decodeType);
	
	public ResponseObject getAllDeviceByDecodeType(String decodeType);
	
	public ResponseObject getDeviceByDeviceId(int deviceId,String objectType);
	
	public ResponseObject createDevice(Device device, int staffId, String flag, String currentAction) throws SMException;
	
	public long getTotalDeviceMappingCount(SearchDeviceMapping searchDevice,String sidx, String sord);
	
	public List<Object[]> getPaginatedList(SearchDeviceMapping searchDevice, int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject getDeviceDetails(int deviceId, String decodeType);
	
	public ResponseObject getAllDeviceTypeIdsByDecodeType(String decodeType);
	
	public ResponseObject deleteDevicesAndMappings(int staffId, Integer [] deviceIds, String decodeType);
	
	public ResponseObject deleteMappings(int staffId, Integer[] mappingIDs, String decodeType);
	
	public void validateImportDeviceDetails(Device device, List<ImportValidationErrors> importErrorList);
	
	public ResponseObject getDeviceByName(String name);
	
	public ResponseObject verifyDeviceDetails(Device device);
	
	public ResponseObject getAllDeviceIdsByDeviceType(int deviceTypeId);
	
	public ResponseObject verifyAndCreateDeviceDetails(Device device, int staffId, int importMode);
	
	public ResponseObject updateDevice(Device device);
	
	public ResponseObject createDevice(Device device);
	
	public ResponseObject setDeviceDetails(String migrationPrefix, int staffId, DecodeTypeEnum streamType) throws MigrationSMException ;
	
	public Device addDeviceForImportAddAndKeepBothMode(Device exportedDevice, int staffId, int importMode);
	
	public Device updateDeviceforImport(Device dbDevice, Device exportedDevice);
	
	public Device getDeviceForImportUpdateMode(Device exportedDevice, int staffId, int importMode);
	
	public long getTotalDeviceCountByDecodeType(SearchDeviceMapping searchDevice,String sidx,String sord);
	
	public List<Device> getPaginatedListForDevice(SearchDeviceMapping searchDevice, int startIndex, int limit, String sidx, String sord);
	
}