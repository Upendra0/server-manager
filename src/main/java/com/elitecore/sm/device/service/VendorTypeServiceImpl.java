/**
 * 
 */
package com.elitecore.sm.device.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.dao.VendorTypeDao;
import com.elitecore.sm.device.model.VendorType;
import com.elitecore.sm.device.validator.VendorTypeValidator;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service("vendorTypeService")
public class VendorTypeServiceImpl implements VendorTypeService{

	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value="vendorTypeDao")
	private VendorTypeDao vendorTypeDao;
	
	
	@Autowired
	VendorTypeValidator vendorTypeValidator;
	
	@Transactional(readOnly = true)
	@Override
	public List<VendorType> getAllVendorList() {
		return vendorTypeDao.getAllVendorType();
	}

	/**
	 * Methdd will get all vendor list by Device type id. 
	 * @param deviceTypeId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getVendorListByDeviceTypeId(int deviceTypeId) {

		logger.debug("Going to fetch vendor list by device type.");
		List<Object[]> objectList = vendorTypeDao.getAllVendorListByDeviceTypeId(deviceTypeId);
		
		
		ResponseObject responseObject = new ResponseObject();
		if (objectList != null && !objectList.isEmpty()) {
			
			JSONArray jsonObjectArray = new JSONArray(objectList); 
			responseObject.setSuccess(true);
			responseObject.setObject(jsonObjectArray);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_VENDOR_DETAILS);
		}
		return responseObject;
	}
	
	/**
	 * Method will get Vendor details by vendor id. 
	 * @see com.elitecore.sm.device.service.VendorTypeService#getVendorTypeById(int)
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getVendorTypeById(int vendorTypeId) {
		
		ResponseObject responseObject = new ResponseObject();
		
		VendorType  vendorType = vendorTypeDao.getVendorTypeById(vendorTypeId);
		
		if(vendorType != null ){
			responseObject.setSuccess(true);
			responseObject.setObject(vendorType);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setObject(ResponseCode.VENDOR_TYPE_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will create new vendor type in database 
	 * @param deviceType
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject createVendorType(VendorType vendorType) {
		logger.debug("Going to create new vendor type.");
		ResponseObject responseObject = new ResponseObject();
		long count = vendorTypeDao.getVendorTypeCountByName(vendorType.getName());
		
		if(count > 0 ){
			logger.info("duplicate vendor type found with name :" + vendorType.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_VENDOR_TYPE_NAME);
			responseObject.setObject(null);
		}else{
			vendorTypeDao.save(vendorType);
			if(vendorType.getId() > 0){
				logger.info("Vendor type has been created successfully!");
				responseObject.setSuccess(true);
				responseObject.setObject(vendorType);
			}else{
				logger.info("Vendor type has been created successfully!");
				responseObject.setSuccess(false);
				responseObject.setObject(vendorType);
				responseObject.setResponseCode(ResponseCode.FAIL_CREATE_VENDOR_TYPE);
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will validate the vendor type details.
	 * @param vendorType
	 * @param importErrorList
	 */
	@Override
	public void validateImportVendorTypeDetails(VendorType vendorType, List<ImportValidationErrors> importErrorList) {
		if(vendorType != null){
			logger.debug("Validating imported vendorType details.");
			vendorTypeValidator.validateVendorType(vendorType, null, vendorType.getName(), true, importErrorList);
		}else{
			logger.debug("Vendor Type object found null.");
		}
	}
	
	/**
	 * Method will fetch device type details by device name.
	 * @param device
	 * @param importErrorList
	 */
	@Transactional
	@Override
	public ResponseObject getVendorTypeByName(String name) {
		logger.debug("Fetching vendor type bye name: "  + name);
	
		ResponseObject responseObject = new ResponseObject();
		VendorType vendorType = vendorTypeDao.getVendorTypeByName(name);
		
		if(vendorType != null){
			responseObject.setObject(vendorType);
			responseObject.setSuccess(true);
		}else{
			logger.debug("Faile to get vendor type by name : " + name);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.VENDOR_TYPE_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Method will check unique name for vendor type name.
	 * @param name
	 * @return
	 */
	@Override
	public ResponseObject validateVendorTypeName(String name) {
		ResponseObject responseObject = new ResponseObject();	
		long count = vendorTypeDao.getVendorTypeCountByName(name);
		if(count > 0 ){
			logger.info("duplicate vendor type found with name :" + name);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_VENDOR_TYPE_NAME);
			responseObject.setObject(null);
		}else{
			responseObject.setSuccess(true);
			logger.debug("Unique name found for vendor type.");
		}
		return responseObject;
	}
}
