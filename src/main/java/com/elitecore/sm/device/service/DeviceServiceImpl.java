/**
 * 
 */
package com.elitecore.sm.device.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.dao.DeviceTypeDao;
import com.elitecore.sm.device.dao.VendorTypeDao;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.device.model.VendorType;
import com.elitecore.sm.device.validator.DeviceValidator;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserWrapperService;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MigrationUtil;



/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value="deviceService")
public class DeviceServiceImpl implements DeviceService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value="parserWrapperService")
	private ParserWrapperService parserWrapperService;

	
	@Autowired
	@Qualifier(value="deviceTypeService")
	private DeviceTypeService deviceTypeService;

	
	@Autowired
	@Qualifier(value="vendorTypeService")
	private VendorTypeService vendorTypeService;

	@Autowired
	@Qualifier(value="parserMappingService")
	private ParserMappingService parserMappingService;
	
	@Autowired
	@Qualifier(value="composerMappingService")
	private ComposerMappingService composerMappingService;

	
	@Autowired
	@Qualifier(value="deviceDao")
	private DeviceDao deviceDao;
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	@Autowired
	private DeviceValidator deviceValidator;
	
	@Autowired
	DeviceTypeDao deviceTypeDao;
	
	@Autowired
	VendorTypeDao vendorTypeDao;

	/**
	 * Method will get all device list.
	 *@return List<Device>  
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Device> getAllDeviceList() {
		if (logger.isDebugEnabled()) {
			logger.debug("Going to fetch all devices list.");
		}
		return deviceDao.getAllDeviceList();
	}

	/**
	 * Method will get device details by device Id
	 * @param deviceId
	 * @param objectType
	 * @return deviceId  
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDeviceByDeviceId(int deviceId, String objectType) {
		ResponseObject responseObject = new ResponseObject();
		
		logger.debug("Going to fetch device details for device id " + deviceId );
		Device device = deviceDao.getDevicebyId(deviceId);
		
		if(device != null){
			logger.info("Device details found successfully for deviceId " + deviceId);
			
			device.getDeviceType().getId();
			device.getVendorType().getId();
			
			if(BaseConstants.JSON.equals(objectType)){  // It will set device ,device type,vendor type details as JSON object.
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("deviceId", device.getId());
				jsonObject.put("deviceName", device.getName());
				jsonObject.put("decodeType", device.getDecodeType());
				jsonObject.put("deviceDescription", device.getDescription());
				jsonObject.put("deviceTypeId", device.getDeviceType().getId());
				jsonObject.put("vendorTypeId", device.getVendorType().getId());
				
				responseObject.setObject(jsonObject);
			}else{ 
				responseObject.setObject(device);  // set device object in response object.
			}
			responseObject.setSuccess(true);
			
		}else{
			logger.info("Failed to fetch device details for deviceId " + deviceId);
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_DEVICE_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will create new device.
	 * @param device
	 * @param staffId
	 * @param flag
	 * @param currentAction
	 * @return ResponseObject
	 * @throws SMException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject createDevice(Device device, int staffId, String flag, String currentAction) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		///MEDSUP-1572///
		long count =  0;
		if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
			count = deviceDao.getDeviceCountByName(device.getName(),device.getDecodeType());
		}else{
			if(!checkUniqueDeviceNameForUpdate(device.getId(), device.getName(),device.getDecodeType())){
				count = 1;
			}
		}
		if(count > 0 ){
			logger.info("duplicate device name found:" + device.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_DEVICE_NAME);
			responseObject.setObject(null);
			return responseObject;					
		}
		///MEDSUP-1572///
		if(BaseConstants.CREATE_DEVICE_TYPE.equals(flag)){
			responseObject = createDeviceType(device, staffId);    // It will create device type.
			if(responseObject.isSuccess()){
				logger.info("Device Type created successfully for device " + device.getName());
			}
		}else if(BaseConstants.CREATE_VENDOR_TYPE.equals(flag)){
			responseObject = createVendorType(device, staffId);    // It will create new vendor type
			if (responseObject.isSuccess()) {
				logger.info("Vendor Type created successfully for device " + device.getName());
			}
		}else if(BaseConstants.BOTH.equals(flag)){
			responseObject = validateDeviceDependents(device, staffId);
		}else{
			responseObject.setSuccess(true);  // No need to create device or vendor as already created options are selected.
		}
		
		if(responseObject.isSuccess()){
			logger.debug("Going to create or update device details. ");
			responseObject = createNewDevice(responseObject, device, staffId, currentAction);
		}
		
		return responseObject;
	}

	/**
	 * Method will check device type and vendor type.
	 * @param device
	 * @param staffId
	 * @return
	 */
	private ResponseObject validateDeviceDependents(Device device, int staffId){
		ResponseObject	responseObject  = deviceTypeService.validateDeviceTypeName(device.getDeviceType().getName()); 
		if(responseObject.isSuccess()){
			responseObject = vendorTypeService.validateVendorTypeName(device.getVendorType().getName());
			if(responseObject.isSuccess()){
				responseObject = createDeviceType(device, staffId);     // It will create device and vendor both.
				if (responseObject.isSuccess()) {
					responseObject = createVendorType(device, staffId);
					if (responseObject.isSuccess()) {
						logger.info("Device type and Vendor Type created successfully for device " + device.getName());
					}
				}
			}
		}
		return responseObject;
	}
	
	
	
	/**
	 * Method will create new device type and set newly created object to device.
	 * @param device
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject createDeviceType(Device device, int staffId){
		ResponseObject responseObject ;
		DeviceType deviceType = new DeviceType(new Date(), staffId, new Date(), staffId, StateEnum.ACTIVE, device.getDeviceType().getId(), device.getDeviceType().getName(), device.getDeviceType().getName());
		responseObject = deviceTypeService.createDeviceType(deviceType);
		if(responseObject.isSuccess()){
			device.setDeviceType(deviceType);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will create new vendor type and set newly created object to device.
	 * @param device
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject createVendorType(Device device, int staffId){
		ResponseObject responseObject;
		VendorType vendorType = new VendorType(new Date(), staffId, new Date(), staffId, StateEnum.ACTIVE, device.getVendorType().getId(), device.getVendorType().getName(), device.getVendorType().getName());
		responseObject = vendorTypeService.createVendorType(vendorType);
		if(responseObject.isSuccess()){
			device.setVendorType(vendorType);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will create device in database.
	 * @param responObj
	 * @param device
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject createNewDevice(ResponseObject responObj, Device device, int staffId, String currentAction){
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to create new device with name " + device.getName());
		if(responObj.isSuccess()){
			long count =  0;
			if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
				count = deviceDao.getDeviceCountByName(device.getName(),device.getDecodeType());
			}else{
				if(!checkUniqueDeviceNameForUpdate(device.getId(), device.getName(),device.getDecodeType())){
					count = 1;
				}
			}
			if(count > 0 ){
				logger.info("duplicate device name found:" + device.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_DEVICE_NAME);
				responseObject.setObject(null);
			}else{
				if(device.getDeviceType().getId() > 0 && device.getVendorType().getId() > 0){
					logger.debug("Device Type found " + device.getDeviceType().getName() + " and  Vendor Type found " +  device.getVendorType().getName());
					
					if (!BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
						responseObject =  deviceTypeService.getDeviceTypeById(device.getDeviceType().getId());
					}else{
						responseObject.setSuccess(true);
					}
					
					if(responseObject.isSuccess()){
						if (!BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
							device.setDeviceType((DeviceType) responseObject.getObject());
							logger.debug("Setting device type details for update device details.");
						 }
						
						if (!BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
							responseObject =  vendorTypeService.getVendorTypeById(device.getVendorType().getId());
						}else{
							responseObject.setSuccess(true);
						}
						
						if(responseObject.isSuccess()){
							if (!BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
								device.setVendorType((VendorType) responseObject.getObject());
								logger.debug("Setting vendor details for update device details.");
							}
							
							device.setParserMapping(null);
							device.setDescription(device.getName());
							device.setIsPreConfigured(BaseConstants.USER_DEFINED_DEVICE);  // 1 for user created devices and 0 for system pre-configured defined devices.
							device.setCreatedByStaffId(staffId);
							device.setCreatedDate(new Date());
							device.setLastUpdatedDate(new Date());
							device.setLastUpdatedByStaffId(staffId);
							
							DeviceService deviceService = (DeviceService) SpringApplicationContext.getBean("deviceService"); // getting spring bean for aop context issue.
							if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
								deviceService.createDevice(device);
							}else{
								deviceService.updateDevice(device);
							}
							
							if(device.getId() > 0 ){
								responseObject.setSuccess(true);
								if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
									logger.info("Device has been created successfully with name " + device.getName() + ".");
									responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_SUCCESS);
								}else{
									logger.info("Device has been updated successfully with name " + device.getName() + ".");
									responseObject.setResponseCode(ResponseCode.UPDATE_DEVICE_SUCCESS);
								}
							}else{
								responseObject = setResponseMessageCode(currentAction);
							}
						}else{
							responseObject = setResponseMessageCode(currentAction);
						}
					}else{
						responseObject = setResponseMessageCode(currentAction);
					}
				}else{
					responseObject = setResponseMessageCode(currentAction);
				}
			}
		}
		return responseObject;
	}
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_DEVICE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Device.class, ignorePropList= "description")
	public ResponseObject updateDevice(Device device){
		ResponseObject responseObject = new ResponseObject();
		deviceDao.merge(device);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_DEVICE, actionType = BaseConstants.CREATE_ACTION, currentEntity = Device.class , ignorePropList= "")
	public ResponseObject createDevice(Device device){
		ResponseObject responseObject = new ResponseObject();
		deviceDao.save(device);
		responseObject.setObject(device);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * Method will Set response code messages for create or update device.
	 * @param currentAction
	 * @return
	 */
	private ResponseObject setResponseMessageCode(String currentAction){
		ResponseObject responseObject = new ResponseObject();
		if (BaseConstants.ACTION_TYPE_ADD.equals(currentAction)) {
			logger.info("Failed to create device details.");
			responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
		}else{
			logger.info("Failed to update device details.");
			responseObject.setResponseCode(ResponseCode.UPDATE_DEVICE_FAIL);
		}
		responseObject.setSuccess(false);
		responseObject.setObject(null);
		return responseObject;
	}
	
	/**
	 * Method will check device unique name while edit device details.
	 * @param deviceId
	 * @param deviceName
	 * @return boolean
	 */
	private boolean checkUniqueDeviceNameForUpdate(int deviceId, String deviceName, String decodeType){
		boolean isUnique = false;
		
		List<Device> deviceList = deviceDao.getDeviceCountByNameForUpdate(deviceName,decodeType);
		if(deviceList != null  && !deviceList.isEmpty()){
			for(Device device : deviceList){
				//If ID is same , then it is same pathList object
				if(deviceId == device.getId()){
					isUnique=true;
				}else{ // It is another device object , but name is same
					isUnique=false;
				}
			}
				
		}else{ // No device found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	/** 
	 * Method will fetch total device mapping list count using HQL query made based on list of search parameters.
	 * @param searchDevices
	 * @return long (total count)
	 */
	@Transactional(readOnly = true)
	@Override
	public long getTotalDeviceMappingCount(SearchDeviceMapping searchDevice,String sidx, String sord) {
		logger.debug("Going to fetch total cound for device, mapping, mapping association details for params " + searchDevice);
		String hqlQuery = deviceDao.getHQLBySearchParameters(searchDevice,BaseConstants.HQL_QUERY_COUNT ,getColumnFromString(sidx),sord);
		return deviceDao.getTotalCountUsingHQL(hqlQuery);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getTotalDeviceCountByDecodeType(SearchDeviceMapping searchDevice,String sidx,String sord) {
		Map<String, Object> deviceCondition = deviceDao.getDeviceByDecodeType(searchDevice);
		return deviceDao.getQueryCount(Device.class, (List<Criterion>) deviceCondition.get("conditions"),(HashMap<String, String>) deviceCondition.get("aliases"));
	}

	/**
	 * This method is used to get the device mapping list based on the offset and sort order
	 * @param offset defines the starting row index 
	 * @param limit defines how many number of rows to be fetched.
	 * @param sortColumn defines which column have sort criteria
	 * @param sortOrder defines asc or desc order.
	 * @return List<ParserMapping>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Object[]> getPaginatedList(SearchDeviceMapping searchDevice, int startIndex, int limit, String sidx, String sord) {
		logger.debug("Going to fetch device, mapping, mapping association details for params " + searchDevice);
		return deviceDao.getListUsingHQL(deviceDao.getHQLBySearchParameters(searchDevice,BaseConstants.HQL_QUERY_LIST,getColumnFromString(sidx), sord), startIndex, limit);
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<Device> getPaginatedListForDevice(SearchDeviceMapping searchDevice, int startIndex, int limit, String sidx, String sord) {
		Map<String, Object> deviceCondition = deviceDao.getDeviceByDecodeType(searchDevice);		
		return deviceDao.getDevicesPaginatedList(Device.class, (List<Criterion>) deviceCondition.get("conditions"),(HashMap<String, String>) deviceCondition.get("aliases"), startIndex, limit, sidx, sord);

	}

	/**
	 * Method  will fetch all device by Device type and vendor Id. 
	 * (non-Javadoc)
	 * @see com.elitecore.sm.device.service.DeviceService#getAllDeviceByVendorAndDeviceType(int, int)
	 * @param deviceTypeId
	 * @param vendorId
	 * @return List<Device>
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllDeviceByVendorAndDeviceType(int deviceTypeId, int vendorId, String decodeType) {
		
		logger.debug("Going to fetch  "+decodeType+"   device details for " + deviceTypeId + " and " + vendorId);
		ResponseObject responseObject = new ResponseObject();
		List<Device> deviceList  = deviceDao.getAllDeviceByVendorAndDeviceType(deviceTypeId, vendorId, decodeType);
		
		if (deviceList != null && !deviceList.isEmpty() ) {
			logger.info(deviceList.size() + " Device list found ");
			JSONArray jsonArray = new JSONArray();
			
			for (Device device : deviceList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", device.getId());
				jsonObject.put("name", device.getName());
				jsonArray.put(jsonObject);    
			}
			responseObject.setSuccess(true);
			responseObject.setObject(jsonArray);
		}else{
			logger.info("Failed to get  device list for "  + deviceTypeId + " and " + vendorId);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}

	/**
	 * Method will fetch all device details like Mapping list and all mapping association.
	 * @param deviceId
	 * @see com.elitecore.sm.device.service.DeviceService#getDeviceDetails(int)
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getDeviceDetails(int deviceId, String decodeType) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to fetch device details for device id " + deviceId);

		List<Object[]> deviceDetails = deviceDao.getDeviceDetailsById(deviceId, decodeType); 
		
		if(deviceDetails != null && !deviceDetails.isEmpty() ){
			logger.info(deviceDetails.size() + " Device list found with all its detailed information.");
			JSONArray deviceArray = new JSONArray(deviceDetails);
			
			responseObject.setSuccess(true);
			responseObject.setObject(deviceArray);
		}else{
			logger.info("Failed to get device details for device id " + deviceId);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_DEVICE_DETAILS); 
		}
		return responseObject;
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_DEVICE, actionType = BaseConstants.DELETE_ACTION, currentEntity = Device.class, ignorePropList= "")
	public ResponseObject deleteDevicesAndMappings(int staffId, Integer [] deviceIds, String decodeType){
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to delete devices and its associated mappings for device ids : " + deviceIds);
		Date currentDate = new Date();
		int deviceIdsListLength = deviceIds.length;
		if(deviceIds != null && deviceIdsListLength > 0){
			if(BaseConstants.UPSTREAM.equals(decodeType)) {
				responseObject = parserMappingService.getAllMappingByDeviceId(deviceIds);
				if(responseObject.isSuccess()){
					List<ParserMapping> parserMappingList = (List<ParserMapping>)responseObject.getObject();
					responseObject = deleteParserMappingList(parserMappingList, staffId, currentDate);
				}else{
					responseObject = setResponseObjectStatus(false,ResponseCode.DEVICE_MAPPING_DELETE_FAIL);
				}
			}else{
				responseObject = composerMappingService.getAllMappingByDeviceId(deviceIds);
				if(responseObject.isSuccess()){
					List<ComposerMapping> composerMappingList = (List<ComposerMapping>)responseObject.getObject();
					responseObject = deleteComposerMappingList(composerMappingList, staffId, currentDate);
				}else{
					responseObject = setResponseObjectStatus(false,ResponseCode.DEVICE_MAPPING_DELETE_FAIL);
				}
			}
			// delete Devices
			for(int i=0 ; i< deviceIdsListLength; i++ ){
				logger.debug("Going to delete device details now.");
				Device device = deviceDao.getDevicebyId(deviceIds[i]);
				responseObject = deleteDevice(device, staffId);
				if (responseObject.isSuccess()) {
					logger.info("Device details with deviceId "+deviceIds[i]+" has been deleted with associated mapping successfully.");
					responseObject = setResponseObjectStatus(true,ResponseCode.DEVICE_MAPPING_DELETE_SUCCESS);
				}else{
					logger.info("Failed to delete device details having deviceId " + deviceIds[i]);
					responseObject = setResponseObjectStatus(false,ResponseCode.DEVICE_MAPPING_DELETE_FAIL);
				}
			}
		}
		return responseObject;
	}
	
	private ResponseObject deleteParserMappingList(List<ParserMapping> parserMappingList,int staffId,Date currentDate){
		ResponseObject responseObject = new ResponseObject();
		if(!StringUtils.isEmpty(parserMappingList)){
			int parserMappListLength = parserMappingList.size();
			for(int i=0 ; i< parserMappListLength; i++ ){
				parserMappingList.get(i).setLastUpdatedByStaffId(staffId);;
				parserMappingList.get(i).setLastUpdatedDate(currentDate);
				parserMappingList.get(i).setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,parserMappingList.get(i).getName()));
				parserMappingList.get(i).setStatus(StateEnum.DELETED);
				responseObject = parserMappingService.deleteMapping(parserMappingList.get(i), staffId);
				if (responseObject.isSuccess()) {
					logger.info("Device mapping has been deleted successfully.");
				}
			}
		}
	return responseObject;	
	}
	
	private ResponseObject deleteComposerMappingList(List<ComposerMapping> composerMappingList,int staffId,Date currentDate){

		ResponseObject responseObject = new ResponseObject();
		if(!StringUtils.isEmpty(composerMappingList)){
			int composerMappListLength = composerMappingList.size();
			for(int i=0 ; i< composerMappListLength; i++ ){
				composerMappingList.get(i).setLastUpdatedByStaffId(staffId);;
				composerMappingList.get(i).setLastUpdatedDate(currentDate);
				composerMappingList.get(i).setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,composerMappingList.get(i).getName()));
				composerMappingList.get(i).setStatus(StateEnum.DELETED);
				responseObject = composerMappingService.deleteMapping(composerMappingList.get(i), staffId);
				if (responseObject.isSuccess()) {
					logger.info("Device mapping has been deleted successfully.");
				}
			}
		}
	return responseObject;	
	
		
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor=Exception.class)
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_MAPPING, actionType = BaseConstants.DELETE_ACTION, currentEntity = Device.class, ignorePropList= "")
	public ResponseObject deleteMappings(int staffId, Integer[] mappingIDs, String decodeType){
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to delete "+ decodeType +" mappings : " + mappingIDs);
		Date currentDate = new Date();
		if(mappingIDs != null && mappingIDs.length > 0){
			if(BaseConstants.UPSTREAM.equals(decodeType)) {
				responseObject = parserMappingService.getAllMappingById(mappingIDs);
				if(responseObject.isSuccess()){
					List<ParserMapping> parserMappingList = (List<ParserMapping>) responseObject.getObject();
					responseObject = deleteParserMappingList(parserMappingList, staffId, currentDate);   
				}else{
					logger.info("Failed to delete parser Mapping details.");
					setResponseObjectStatus(false,ResponseCode.DEVICE_MAPPING_DELETE_FAIL);
				}
			}else{
				responseObject = composerMappingService.getAllMappingById(mappingIDs);
				if(responseObject.isSuccess()){
					List<ComposerMapping> composerMappingList = (List<ComposerMapping>) responseObject.getObject();
					responseObject = deleteComposerMappingList(composerMappingList, staffId, currentDate);
				}else{
					logger.info("Failed to delete composer Mapping details.");
					setResponseObjectStatus(false,ResponseCode.DEVICE_MAPPING_DELETE_FAIL);
				}
			}
		}
		
		return responseObject;
	}

	/**
	 * Method will delete device.
	 * @param device
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject deleteDevice(Device device, int staffId){
		ResponseObject responseObject = new ResponseObject();
		
		device.setLastUpdatedDate(new Date());
		device.setLastUpdatedByStaffId(staffId);
		device.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,device.getName()));
		
		/*
		 * device type id (1 -> CGNAT, 2 -> P-Gateway, 3 -> S-Gateway, 4 -> Elitecore BSS)
		 * are master entries thats why device.getDeviceType().getId() > 4 condition is added to 
		 * prevent deletion of these master entries.
		 */
		if(device.getDeviceType() != null && device.getDeviceType().getId() > 4) {
			long deviceCount = deviceDao.getDeviceCountByDeviceId(device);
			if(deviceCount <= 0) {
				device.getDeviceType().setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,device.getDeviceType().getName()));
				device.getDeviceType().setStatus(StateEnum.DELETED);
			}
		}
		
		/*
		 * vendor type id (1 -> Cisco, 2 -> A10, 3 -> Elitecore) are master entries thats why
		 * device.getVendorType().getId() > 3 condition is added to prevent deletion of these 
		 * master entries.
		 */
		if(device.getVendorType() != null && device.getVendorType().getId() > 3) {
			long deviceCount = deviceDao.getDeviceCountByVendorId(device);
			if(deviceCount <= 0) {
				device.getVendorType().setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,device.getVendorType().getName()));
				device.getVendorType().setStatus(StateEnum.DELETED);
			}
		}
		
		device.setStatus(StateEnum.DELETED);
		
		deviceDao.merge(device);
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.DELETE_DEVICE_SUCCESS);
		
		return responseObject;
	}
	
	/**
	 * Method will add alias in sort column name for sorting the current column.
	 * @param sidx
	 * @return String
	 */
	private String getColumnFromString(String sidx){
		String sortOrder;
		if("deviceType".equals(sidx)){
			sortOrder = "device.deviceType.name";
		}else if("vendorType".equals(sidx)){
			sortOrder = "device.vendorType.name";
		}else if("deviceName".equals(sidx)){
			sortOrder = "device.name";
		}else{
			sortOrder = "device.id";
		}
		
		return sortOrder;
	}

	/**
	 * Method will validate imported device details.
	 * (non-Javadoc)
	 * @see com.elitecore.sm.device.service.DeviceService#validateImportDeviceDetails(com.elitecore.sm.device.model.Device, java.util.List)
	 */
	@Override
	public void validateImportDeviceDetails(Device device, List<ImportValidationErrors> importErrorList) {
		if(device != null){
			logger.debug("Validating imported device details.");
			deviceValidator.validateDevice(device, null, device.getName(), true,importErrorList);  // It will validate device details.	
			
			deviceTypeService.validateImportDeviceTypeDetails(device.getDeviceType(), importErrorList); // It will validate Device Type details
			vendorTypeService.validateImportVendorTypeDetails(device.getVendorType(), importErrorList); // It will validate Vendor type details.
			
		}else{
			logger.debug("Device object found null.");
		}
	}

	/**
	 * Method will get device details by device Name.
	 * @param name
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getDeviceByName(String name) {
		logger.debug("Fetching device by name: "  + name);
	
		ResponseObject responseObject = new ResponseObject();
		Device device = deviceDao.getDeviceByName(name);
		if(device != null){
			deviceDao.iterateOverDevice(device);
			responseObject.setObject(device);
			responseObject.setSuccess(true);
		}else{
			logger.debug("Faile to get device by name : " + name);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DEVICE_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Method will verify device dependents or create. 
	 * @param device
	 * @return
	 */
	@Override
	public ResponseObject verifyDeviceDetails(Device device) {
		ResponseObject responseObject = new ResponseObject();
		
			if(device.getDeviceType() != null && device.getVendorType() != null){
				responseObject = verifyAndCreateDeviceType(device.getDeviceType());
				if(responseObject.isSuccess()){
					device.setDeviceType((DeviceType) responseObject.getObject());
					responseObject = verifyAndCreateVendorType(device.getVendorType());
					if(responseObject.isSuccess()){
						responseObject.setSuccess(true);
						device.setVendorType((VendorType) responseObject.getObject());
						responseObject.setObject(device);
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FAIL_CREATE_VENDOR_TYPE);
					}	
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FAIL_CREATE_DEVICE_TYPE);
				}
			}else{ 
				logger.debug("Failed to create device dependents.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
			}
		return responseObject;
	}
	
	
	/**
	 *  Method will verify and create device type details.
	 * @param deviceType
	 * @return
	 */
	private ResponseObject verifyAndCreateDeviceType(DeviceType deviceType){
		ResponseObject responseObject  = deviceTypeService.getDeviceTypeByName(deviceType.getName());
		
		if(responseObject.isSuccess()){
			responseObject.setSuccess(true);
			responseObject.setObject((DeviceType)responseObject.getObject());
		}else{
			deviceType.setId(0);
			deviceType.setCreatedDate(new Date());
			deviceType.setName(deviceType.getName());
			deviceType.setCreatedByStaffId(deviceType.getCreatedByStaffId());
			
			responseObject = deviceTypeService.createDeviceType(deviceType);
			if(responseObject.isSuccess()){
				responseObject.setObject(deviceType);
				responseObject.setSuccess(true);
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_CREATE_DEVICE_TYPE);
			}
		}
		return responseObject;
	}
	/**
	 * Method will verify and create vendor type details.
	 * @param vendorType
	 * @return
	 */
	private ResponseObject verifyAndCreateVendorType(VendorType vendorType){
		 ResponseObject	responseObject = vendorTypeService.getVendorTypeByName(vendorType.getName());
		if(responseObject.isSuccess()){
			responseObject.setSuccess(true);
			responseObject.setObject((VendorType)responseObject.getObject());
		}else{
			vendorType.setId(0);
			vendorType.setCreatedDate(new Date());
			vendorType.setName(vendorType.getName());
			
			responseObject = vendorTypeService.createVendorType(vendorType);
			if(responseObject.isSuccess()){
				responseObject.setObject(vendorType);
				responseObject.setSuccess(true);
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_CREATE_VENDOR_TYPE);
			}
		}
		return responseObject;
	}

	/**
	 * Method will fetch all device id by Device type id.
	 * @param deviceId
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getAllDeviceIdsByDeviceType(int deviceTypeId) {
		logger.debug("Fetching all device by device type id " + deviceTypeId);
		ResponseObject responseObject = new ResponseObject();
		List<Integer> deviceIdsList = deviceDao.getAllDeviceIdsByDeviceTypeId(deviceTypeId);
		if(deviceIdsList != null && !deviceIdsList.isEmpty()){
			logger.debug("Found " + deviceIdsList.size() + " device for device type " + deviceTypeId);
			responseObject.setSuccess(true);
			responseObject.setObject(deviceIdsList);
		}else{
			logger.debug("Device list found null for device type " + deviceTypeId);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
	/**
	 * Method will fetch all device id by Decode Type.
	 * @param decodeType
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject getAllDeviceTypeIdsByDecodeType(String decodeType){
		logger.debug("Fetching all device by decodeType " + decodeType);
		ResponseObject responseObject = new ResponseObject();
		List<DeviceType> deviceTypeIdsList = deviceDao.getAllDeviceTypeIdsByDecodeType(decodeType);
		if(deviceTypeIdsList != null && !deviceTypeIdsList.isEmpty()){
			logger.debug("Found " + deviceTypeIdsList.size() + " deviceTypeIds for decodeType " + decodeType);
			responseObject.setSuccess(true);
			responseObject.setObject(deviceTypeIdsList);
		}else{
			logger.debug("deviceTypeIds list found null for decodeType " + decodeType);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}

		return responseObject;
		
	}

	/**
	 * Method will set device entity for import and create device.
	 * @param device
	 * @param staffId
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject verifyAndCreateDeviceDetails(Device device, int staffId, int importMode) {
		ResponseObject	responseObject = verifyDeviceDetails(device);
		
		if(responseObject.isSuccess()){
			Device newDevice = (Device) responseObject.getObject();
			newDevice.setId(0);
			
			long count = deviceDao.getDeviceCountByName(device.getName(),null);
			if(count>0) {
				newDevice.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,device.getName()));
			}
			
			newDevice.setCreatedDate(new Date());
			newDevice.setCreatedByStaffId(staffId);
			newDevice.setLastUpdatedByStaffId(staffId);
			newDevice.setLastUpdatedDate(new Date());
			try {
				responseObject = createDevice(newDevice, staffId, "IMPORT", BaseConstants.ACTION_TYPE_ADD);
				if(responseObject.isSuccess()){
					responseObject.setObject(newDevice);
					responseObject.setSuccess(true);
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
				}
			} catch (SMException sme) {
				logger.error("Failed to create device details due to error " + sme);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
			}
		}
		return responseObject;
	}

	/**
	 * Method will create new device, device type and vendor type for migration entity
	 * 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject setDeviceDetails(String migrationPrefix, int staffId, DecodeTypeEnum streamType) throws MigrationSMException {
		logger.debug("Creating device , device type and vendor type");
		ResponseObject responseObject = new ResponseObject();
		
		//Setting device type details.
		DeviceType deviceType = new DeviceType();
		deviceType.setName(migrationUtil.getRandomName(migrationPrefix + "_devicetype"));	
		deviceType.setId(0);
		deviceType.setDescription("This is created for migration use only.");
		migrationUtil.setCurrentDateAndStaffId(deviceType, staffId);
		
		VendorType vendorType = new VendorType();
		vendorType.setName(migrationUtil.getRandomName(migrationPrefix + "_vendortype"));	
		vendorType.setId(0);
		vendorType.setDescription("This is created for migration use only.");
		migrationUtil.setCurrentDateAndStaffId(vendorType, staffId);
		Device device = new Device(0, migrationUtil.getRandomName(migrationPrefix + "device"), "Migration device ", streamType.getValue(), BaseConstants.USER_DEFINED_DEVICE, deviceType, vendorType);
		try {
			logger.info("Adding device details");
			responseObject = createDevice(device, staffId, BaseConstants.BOTH, BaseConstants.ACTION_TYPE_ADD);
			responseObject.setObject(device);
		} catch (SMException e) {
			logger.error(e.getMessage(), e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DEVICE_CREATE_FAIL);
			throw new MigrationSMException(responseObject, e.getMessage());
		}
		
		return responseObject;
	}
	
	@Override
	public Device getDeviceForImportUpdateMode(Device exportedDevice, int staffId, int importMode) {
		Device dbDevice = null;
		if(exportedDevice != null && !exportedDevice.getStatus().equals(StateEnum.DELETED)) {
			dbDevice = deviceDao.getDeviceByName(exportedDevice.getName());
			if(dbDevice == null) {
				logger.debug("going to add device for import : "+exportedDevice.getName());
				dbDevice = addDeviceForImportAddAndKeepBothMode(exportedDevice, staffId, importMode);
			} else {
				logger.debug("going to update device for import : "+dbDevice.getName());
				dbDevice = updateDeviceforImport(dbDevice, exportedDevice);
			}
		}
		return dbDevice;
	}
	
	@Override
	public Device addDeviceForImportAddAndKeepBothMode(Device exportedDevice, int staffId, int importMode) {
		VendorType exportedVendorType = exportedDevice.getVendorType();
		DeviceType exportedDeviceType = exportedDevice.getDeviceType();
		VendorType dbVendorType = null;
		DeviceType dbDeviceType = null;
		if(exportedVendorType != null && !exportedVendorType.getStatus().equals(StateEnum.DELETED)) {
			if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
				dbVendorType = new VendorType(EliteUtils.getDateForImport(false), staffId, EliteUtils.getDateForImport(false), staffId, StateEnum.ACTIVE, 0, EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedVendorType.getName()), exportedVendorType.getDescription());
			} else {
				dbVendorType = vendorTypeDao.getVendorTypeByName(exportedVendorType.getName());
				if(dbVendorType == null) {
					dbVendorType = new VendorType(EliteUtils.getDateForImport(false), staffId, EliteUtils.getDateForImport(false), staffId, StateEnum.ACTIVE, 0, exportedVendorType.getName(), exportedVendorType.getDescription());
				}
			}
		}
		if(exportedDeviceType != null && !exportedDeviceType.getStatus().equals(StateEnum.DELETED)) {
			if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
				dbDeviceType = new DeviceType(EliteUtils.getDateForImport(false), staffId, EliteUtils.getDateForImport(false), staffId, StateEnum.ACTIVE, 0, EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDeviceType.getName()), exportedDeviceType.getDescription());
			} else {
				dbDeviceType = deviceTypeDao.getDeviceTypeByName(exportedDeviceType.getName());
				if(dbDeviceType == null) {
					dbDeviceType = new DeviceType(EliteUtils.getDateForImport(false), staffId, EliteUtils.getDateForImport(false), staffId, StateEnum.ACTIVE, 0, exportedDeviceType.getName(), exportedDeviceType.getDescription());
				}
			}
		}
		exportedDevice.setId(0);
		
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedDevice.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDevice.getName()));
		}
		
		exportedDevice.setIsPreConfigured(1);
		exportedDevice.setDeviceType(dbDeviceType);
		exportedDevice.setVendorType(dbVendorType);
		exportedDevice.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedDevice.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedDevice.setCreatedByStaffId(staffId);
		exportedDevice.setLastUpdatedByStaffId(staffId);
		return exportedDevice;
	}
	
	@Override
	public Device updateDeviceforImport(Device dbDevice, Device exportedDevice) {
		if(dbDevice.getIsPreConfigured() == 1) {
			dbDevice.setDecodeType(exportedDevice.getDecodeType());
			
			DeviceType deviceType = dbDevice.getDeviceType();
			deviceType.setName(exportedDevice.getDeviceType().getName());
			deviceType.setDescription(exportedDevice.getDeviceType().getDescription());
			
			VendorType vendorType = dbDevice.getVendorType();
			vendorType.setName(exportedDevice.getVendorType().getName());
			vendorType.setDescription(exportedDevice.getVendorType().getDescription());
			dbDevice.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		}
		return dbDevice;
	}

	private ResponseObject setResponseObjectStatus(boolean successFlag,ResponseCode responseCode){
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(successFlag);
		responseObject.setResponseCode(responseCode);
		responseObject.setObject(null);
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllDeviceByDecodeType(String decodeType) {
		
		logger.debug("Going to fetch  "+decodeType+"   device details ");
		ResponseObject responseObject = new ResponseObject();
		List<Device> deviceList  = deviceDao.getAllDeviceByDecodeType(decodeType);
		
		if (deviceList != null && !deviceList.isEmpty() ) {
			logger.info(deviceList.size() + " Device list found ");
			JSONArray jsonArray = new JSONArray();
			
			for (Device device : deviceList) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", device.getId());
				jsonObject.put("name", device.getName());
				jsonObject.put("deviceType", device.getDeviceType());
				jsonObject.put("vendorType",device.getVendorType());
				
				jsonArray.put(jsonObject);    
			}
			responseObject.setSuccess(true);
			responseObject.setObject(jsonArray);
		}else{
			logger.info("Failed to get  device list for "  + decodeType);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
}