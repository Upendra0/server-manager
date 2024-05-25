/**
 * 
 */
package com.elitecore.sm.device.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.model.VendorType;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface VendorTypeService {

	public List<VendorType> getAllVendorList();
	
	public ResponseObject getVendorListByDeviceTypeId(int deviceTypeId);
	
	public ResponseObject getVendorTypeById(int vendorTypeId);
	
	public ResponseObject createVendorType(VendorType vendorType);
	
	public void validateImportVendorTypeDetails(VendorType vendorType, List<ImportValidationErrors> importErrorList);
	
	public ResponseObject getVendorTypeByName(String name);
	
	public ResponseObject validateVendorTypeName(String name);
	
}
