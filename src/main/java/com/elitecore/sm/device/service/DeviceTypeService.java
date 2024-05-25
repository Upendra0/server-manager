/**
 * 
 */
package com.elitecore.sm.device.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.model.DeviceType;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface DeviceTypeService {

	public ResponseObject getAllDeviceType();
	public ResponseObject getDeviceTypeById(int deviceTypeId);
	
	public ResponseObject createDeviceType(DeviceType deviceType);
	
	public void validateImportDeviceTypeDetails(DeviceType device, List<ImportValidationErrors> importErrorList);
	
	public ResponseObject getDeviceTypeByName(String name);
	
	public ResponseObject validateDeviceTypeName(String name);
	
}
