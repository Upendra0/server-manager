/**
 * 
 */
package com.elitecore.sm.device.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.dao.DeviceTypeDao;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.validator.DeviceTypeValidator;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value="deviceTypeService")
public class DeviceTypeServiceImpl implements DeviceTypeService{

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value="deviceTypeDao")
	private DeviceTypeDao deviceTypeDao;
	
	@Autowired
	private DeviceTypeValidator deviceTypeValidator;
	
	/**
	 * Method will get all device type.
	 * @return responseObject
	 */
	@Transactional
	@Override
	public ResponseObject getAllDeviceType() {
		
		logger.debug("Going to fetch all device type list.");
		ResponseObject responseObject = new ResponseObject();
		List<DeviceType> deviceTypeList = deviceTypeDao.getAllDeviceType(); 
		
		if (deviceTypeList != null && !deviceTypeList.isEmpty() )  {
			logger.info("Found " + deviceTypeList.size() + " device type.");
			responseObject.setSuccess(true);
			responseObject.setObject(deviceTypeList);
			
		} else {
			logger.info("Failed to fetch device type details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}


	/**
	 * Method will fetch device type details by id.
	 * @param deviceTypeId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject getDeviceTypeById(int deviceTypeId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to fetch device type by id " + deviceTypeId);
		DeviceType  deviceType = deviceTypeDao.getDeviceTypeById(deviceTypeId);
		
		if(deviceType != null ){
			logger.info("Device type details found successfully.");
			responseObject.setSuccess(true);
			responseObject.setObject(deviceType);
		}else{
			logger.info("Failed to get device type details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setObject(ResponseCode.DEVICE_TYPE_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will create new device type. 
	 * @param deviceType
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject createDeviceType(DeviceType deviceType) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to create new device type with details of " + deviceType);
		
		long count = deviceTypeDao.getDeviceTypeCountByName(deviceType.getName());
		
		if(count > 0 ){
			logger.info("duplicate device type found with name :" + deviceType.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DEVICE_TYPE_NAME);
			responseObject.setObject(null);
		}else{
			
			deviceTypeDao.save(deviceType);
			
			if(deviceType.getId() > 0){
				logger.info("Device type has been created successfully.");
				responseObject.setSuccess(true);
				responseObject.setObject(deviceType);
			}else{
				logger.info("Failed to create device type.");
				responseObject.setSuccess(false);
				responseObject.setObject(deviceType);
				responseObject.setResponseCode(ResponseCode.FAIL_CREATE_DEVICE_TYPE);
			}
		}
		return responseObject;
	}

	/**
	 * Method will validate device type details.
	 * @param device
	 * @param importErrorList
	 */
	@Override
	public void validateImportDeviceTypeDetails(DeviceType deviceType, List<ImportValidationErrors> importErrorList) {
		if(deviceType != null){
			logger.debug("Validating imported DeviceType details.");
			deviceTypeValidator.validateDeviceType(deviceType, null, deviceType.getName(), true, importErrorList);
		}else{
			logger.debug("Device Type object found null.");
		}
	}

	/**
	 * Method will fetch device type details by device name.
	 * @param device
	 * @param importErrorList
	 */
	@Transactional
	@Override
	public ResponseObject getDeviceTypeByName(String name) {
		logger.debug("Fetching device type bye name: "  + name);
	
		ResponseObject responseObject = new ResponseObject();
		DeviceType deviceType = deviceTypeDao.getDeviceTypeByName(name);
		
		if(deviceType != null){
			responseObject.setObject(deviceType);
			responseObject.setSuccess(true);
		}else{
			logger.debug("Faile to get device type by name : " + name);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DEVICE_TYPE_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will check unique name for device name.
	 * @param name
	 * @return
	 */
	@Override
	public ResponseObject validateDeviceTypeName(String name) {
		ResponseObject responseObject = new ResponseObject();	
		long count = deviceTypeDao.getDeviceTypeCountByName(name);
		if(count > 0 ){
			logger.info("duplicate device type found with name :" + name);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DEVICE_TYPE_NAME);
			responseObject.setObject(null);
		}else{
			responseObject.setSuccess(true);
			logger.debug("Unique name found for device type.");
		}
		return responseObject;
	}
	
}
