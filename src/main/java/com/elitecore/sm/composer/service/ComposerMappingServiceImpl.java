package com.elitecore.sm.composer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.ComposerAttributeDao;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.dao.ComposerMappingDao;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.DefaultComposerMapping;
import com.elitecore.sm.composer.model.DetailLocalComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.composer.model.NRTRDEComposerMapping;
import com.elitecore.sm.composer.model.RAPComposerMapping;
import com.elitecore.sm.composer.model.RoamingComposerMapping;
import com.elitecore.sm.composer.model.TAPComposerMapping;
import com.elitecore.sm.composer.model.XMLComposerMapping;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.util.EliteUtils;

/**
 * 
 * @author avani.panchal
 *
 */
@Service(value="composerMappingService")
public class ComposerMappingServiceImpl implements ComposerMappingService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private ParserService parserService;
	
	@Autowired
	private ComposerMappingDao composerMappingDao;
	
	@Autowired
	private ComposerAttributeDao composerAttributeDao;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	ComposerDao composerDao;
	
	@Autowired
	ComposerService composerService;
	
	
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	
	
	@Autowired
	ComposerAttributeService composerAttributeService;
	
	@Autowired
	ComposerGroupAttributeService composerGroupAttributeService;
	
	private String serviceNameConstant = "composerMappingService";
	
	
	
	/**
	 * Method will get all mapping details for selected device and plug-in type.(It will display mapping list selected composer plug-in) 
	 * @param deviceId
	 * @param  composerType
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingByDeviceAndComposerType(int deviceId,	String composerType) {
		
		logger.debug("Going to fetch all mapping details for  device id " + deviceId + " and  composer  type " + composerType + "." );
		
		ResponseObject responseObject  = parserService.getPluginByType(composerType);
		if(responseObject.isSuccess()){
			PluginTypeMaster pluginMaster = (PluginTypeMaster) responseObject.getObject();
			logger.debug("Plugin list found not going to fetch device details for device id " + deviceId);
			
			if(deviceId > 0){
				List<ComposerMapping> composerMappingList = composerMappingDao.getAllMappingBydeviceAndComposerType(deviceId, pluginMaster.getId());
				
				if(composerMappingList != null && !composerMappingList.isEmpty()){
					responseObject.setSuccess(true);
					responseObject.setObject(composerMappingList);
					logger.info(composerMappingList.size() + " mapping list found successfully!");
					
				}else{
					logger.info("Failed to fetch parser mapping details for device id " + deviceId +  " and  plugin type " + composerType);
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
				}
			}else{
				logger.info("Failed  fetch mapping details due to device id found " + deviceId);
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
			}
			
		}else{
			logger.info("Failed to get mapping list due not able to get plugin master for parser type " + composerType );
			responseObject.setSuccess(false);	
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_DEVICE_ID);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will create or update the composer mapping using selected base mapping or none option.
	 * @param ComposerMapping
	 * @param mappingId
	 * @param staffId
	 * @param actionType
	 * @return ResponseObject
	 * @throws CloneNotSupportedException 
	 */
	@Transactional
	@Override
	public ResponseObject createOrUpdateComposerMappingDetails(ComposerMapping newComposerMapping, int mappingId, int staffId, String actionType, String composerType) throws CloneNotSupportedException {
	
		logger.debug("Going to create new mapping details for different actions.Like create from base mapping or create fresh copy of mapping.");
		
		ResponseObject responseObject = new ResponseObject();
		
		int mappingCount = composerMappingDao.getMappingCount(newComposerMapping.getName());
		
		  if(mappingCount > 0){
				logger.info("Duplicate mapping name found:" + newComposerMapping.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_MAPPING_FOUND);
		  }else{
			  
			  if(BaseConstants.CREATE.equals(actionType)  && mappingId > 0 ){
					
				  ComposerMapping baseComposerMapping = composerMappingDao.findByPrimaryKey(ComposerMapping.class, mappingId);
				  Date date = new Date();
				  if(baseComposerMapping != null){
					  logger.debug("Composer mapping found successfully not going create mapping.");
				
					  ComposerMapping mappingObj =  (ComposerMapping) baseComposerMapping.clone();  
					 
					  mappingObj.setCreatedByStaffId(staffId);

					  if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(composerType) || EngineConstants.ASN1_COMPOSER_PLUGIN.equals(composerType) || EngineConstants.XML_COMPOSER_PLUGIN.equals(composerType) || EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(composerType)){
						  createComposerAttributes(mappingObj);
					  }
					  composerMappingDao.evict(baseComposerMapping);
				  	mappingObj.setId(0);
				  	mappingObj.setMappingType(BaseConstants.USER_DEFINED_MAPPING); // Setting user defined mapping type. 
				  	mappingObj.setName(newComposerMapping.getName());
				  	mappingObj.setComposerWrapper(null);
				  	mappingObj.setCreatedDate(date);
				  	mappingObj.setLastUpdatedDate(date);
				  	mappingObj.setCreatedByStaffId(staffId);
				  	mappingObj.setLastUpdatedByStaffId(staffId);
				  	ComposerMappingService  mappingServiceImpl = (ComposerMappingService) SpringApplicationContext.getBean(serviceNameConstant);
				  	responseObject = mappingServiceImpl.createNewMapping(mappingObj);
				  	if(responseObject.isSuccess()){
				  		logger.info("Mapping has been created successfully from selected base mapping.");
				  	}else{
				  		logger.info("Failed to create mapping as selected base mapping found  " + baseComposerMapping);
				  		responseObject.setSuccess(false);
				  		responseObject.setObject(null);
				  		responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
				  	}
			}else{
		  		logger.info("Failed to create mapping as selected base mapping found  " + baseComposerMapping);
		  		responseObject.setSuccess(false);
		  		responseObject.setObject(null);
		  		responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		  	}	
				
		}else if(BaseConstants.CREATE.equals(actionType)  && mappingId == -2 && newComposerMapping.getDevice().getId() > 0){ // From front end selected custom option so need to create new mapping.
			
			Device device ;
			responseObject =  this.deviceService.getDeviceByDeviceId(newComposerMapping.getDevice().getId(), "DEVICE");
			if(responseObject.isSuccess()){
				
				device  = (Device) responseObject.getObject();
				responseObject  =	parserService.getPluginByType(composerType);
				
				if(responseObject.isSuccess()){
					PluginTypeMaster pluginTypeMaster = (PluginTypeMaster) responseObject.getObject();
					
					ComposerMapping composerMapping = null;
					logger.info("Found plugin type " + composerType);
					
					 if(EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping =new ASCIIComposerMapping();
					 }else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						composerMapping =new ASN1ComposerMapping();
					 }
					 else if(EngineConstants.XML_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping =new XMLComposerMapping();
					 }
					 else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping = new FixedLengthASCIIComposerMapping();
					 }
					 else if(EngineConstants.TAP_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping =new TAPComposerMapping();
					 }
					 else if(EngineConstants.RAP_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping =new RAPComposerMapping();
					 }
					 else if(EngineConstants.NRTRDE_COMPOSER_PLUGIN.equalsIgnoreCase(composerType)){
						 composerMapping =new NRTRDEComposerMapping();
					 }
					
					
					 if(composerMapping != null){
						 Date date = new Date();
						 composerMapping.setDevice(device);
						 composerMapping.setComposerType(pluginTypeMaster);
						 composerMapping.setComposerWrapper(null);
						 composerMapping.setCreatedDate(date);
						 composerMapping.setLastUpdatedDate(date);
						 composerMapping.setCreatedByStaffId(staffId);
						 composerMapping.setLastUpdatedByStaffId(staffId);
						 composerMapping.setAttributeList(null);
						 composerMapping.setMappingType(BaseConstants.USER_DEFINED_MAPPING);
						 composerMapping.setName(newComposerMapping.getName());
						 ComposerMappingService  mappingServiceImpl = (ComposerMappingService) SpringApplicationContext.getBean(serviceNameConstant);
						 responseObject = mappingServiceImpl.createNewMapping(composerMapping);
						
						if(responseObject.isSuccess()){
							logger.info("Composer mapping has been created successfully.");
						}else{
							logger.info("Failed to create mapping.");
							responseObject.setSuccess(false);
							responseObject.setObject(null);
							responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
						}
					 }else{
							logger.info("Failed to create mapping as mapping object found null.");
							responseObject.setSuccess(false);
							responseObject.setObject(null);
							responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
					}
		  }else{
				logger.info("Failed to create mapping as condition is matched.");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		}
	}else{
		logger.info("Failed to create mapping as condition is matched.");
		responseObject.setSuccess(false);
		responseObject.setObject(null);
		responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
	}
	}
    }
		return responseObject;
	}

	/**
	 * Create Parser Attribute
	 * @param mappingObj
	 */
	@Override
	public void createComposerAttributes(ComposerMapping mappingObj){
		
		List<ComposerAttribute> attributeList ;
		if (mappingObj.getAttributeList() != null && !mappingObj.getAttributeList().isEmpty()) {
			mappingObj.getAttributeList().get(0).getId();
			attributeList = mappingObj.getAttributeList();

			List<ComposerAttribute> tempAttributeList = new ArrayList<>();

			if (attributeList != null && !attributeList.isEmpty()) {
				Date date = new Date();
				for (ComposerAttribute composerAttribute : attributeList) {
					composerAttributeDao.evict(composerAttribute);
					composerAttribute.setMyComposer(mappingObj);
					composerAttribute.setId(0);
					composerAttribute.setCreatedDate(date);
					composerAttribute.setLastUpdatedDate(date);
					composerAttribute.setCreatedByStaffId(mappingObj.getCreatedByStaffId());
					composerAttribute.setLastUpdatedByStaffId(mappingObj.getCreatedByStaffId());

					tempAttributeList.add(composerAttribute);
				}
				mappingObj.setAttributeList(tempAttributeList);

			} else {
				mappingObj.setAttributeList(null);
			}
		} else {
			mappingObj.setAttributeList(null); 
		}

	}
	
	/**
	 * Method will create new mapping details and also it will check for the duplicate name for composer mapping.
	 * @param parserMapping
	 * @param staffId
	 * @return ResponseObject
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_MAPPING, actionType = BaseConstants.CREATE_ACTION, currentEntity = ComposerMapping.class, ignorePropList= "attributeList,composerWrapper,device,name")
	public ResponseObject createNewMapping(ComposerMapping composerMapping) {
		ResponseObject responseObject = new ResponseObject();
		
		logger.debug("Going to create new mapping details");
		
		composerMappingDao.save(composerMapping);
		
		if (composerMapping.getId() >  0) {
			logger.info("Composer Mapping details created successfully.");
			responseObject.setSuccess(true);
			responseObject.setObject(composerMapping);
			responseObject.setResponseCode(ResponseCode.CREATE_MAPPING_SUCCESS);
		}else{
			logger.info("Failed to create composer mapping details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will update composer mapping details based on the composer type and associate it with composer.
	 * @param composerMapping
	 * @param parserType
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject updateAndAssociateComposerMapping(ComposerMapping composerMapping, String pluginType, int plugInId, String actionType, int staffId) {
		logger.debug("Going to update plugin association with composer mapping.");
		ResponseObject responseObject = new ResponseObject();
		
		 if(actionType != null && "NO_ACTION".equals(actionType)){
			 if(composerMapping.getId() > 0 ){
				 logger.debug("Customization option is not selected so going to update simple association with selected plugin with selected mapping.");
				 responseObject = updateComposerMappingDetails(composerMapping.getId(), plugInId, staffId);
				 if(responseObject.isSuccess()){
					 logger.debug("Composer is associated with selected composer mapping successfully.");
				}
			 }else{
				 logger.debug("Failed to update mappign details as mapping id found " + composerMapping.getId());
				 responseObject.setSuccess(false);
				 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
			 }
		 }else if(actionType != null && "UPDATE".equals(actionType)){
				 
			 ComposerMapping composerMappingDetails = composerMappingDao.findByPrimaryKey(ComposerMapping.class, composerMapping.getId());
			 
					 if(composerMappingDetails != null){
						 updateMappingDetails(composerMapping, composerMappingDetails, staffId);
					 }else{
						 logger.debug("Failed to update comoposer mapping details due to composerMappingDetails found " + composerMappingDetails + " for mapping id " + composerMapping.getId());
						 responseObject.setSuccess(false);
						 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
					 }
				 
					 responseObject = updateComposerMappingDetails(composerMapping.getId(), plugInId, staffId);
					 logger.debug("Mapping and plugin association has been updated successfully.");
				 
			 }else{
				 logger.debug("Failed to update composer mapping details due to mapping id found " + composerMapping.getId());
				 responseObject.setSuccess(false);
				 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
			 }
		
		return responseObject;
	}
	
	
	/**
	 * Method will update mapping details for select mapping.
	 * @param mappingId
	 * @param plugInId
	 * @param staffId
	 * @return ResponseObject
	 */
	private ResponseObject updateComposerMappingDetails(int mappingId, int plugInId, int staffId){
		ResponseObject responseObject = new ResponseObject();
		
		logger.debug("Going to update composer with selected mapping.");
		ComposerMapping composerMappingDetails = composerMappingDao.findByPrimaryKey(ComposerMapping.class, mappingId);
		 
		 if(composerMappingDetails != null && plugInId > 0){
			 Composer composer = composerDao.findByPrimaryKey(Composer.class, plugInId);
			 
			 if(composer != null ){
				 composer.setComposerMapping(composerMappingDetails);
				 composer.setLastUpdatedDate(new Date());
				 composer.setLastUpdatedByStaffId(staffId);
				 
				 responseObject = composerService.updateComposerMapping(composer);
				 if(responseObject.isSuccess()){
					 responseObject.setSuccess(true);
					 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_SUCCESS);
					 logger.info("Composer mapping is associated successfully with selected plugin.");
					 
				 }else{
					 logger.info("Failed to update composer details for selected mapping.");
					 responseObject.setSuccess(false);
					 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
				 }
			 }else{
				 logger.info("Failed to update composer details for selected mapping.");
				 responseObject.setSuccess(false);
				 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
			 }
		 }else{
			 logger.info("Failed to associate composer as composer mapping object found " + composerMappingDetails + " for id " + mappingId);
			 responseObject.setSuccess(false);
			 responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
		 }
		return responseObject;
	}
	
	
	
	/**
	 * Method will update mapping details for selected plugin type.
	 * @param newParserMapping
	 * @param dbParserMapping
	 * @param staffId
	 */
	
	public void updateMappingDetails(ComposerMapping newComposerMapping, ComposerMapping dbComposerMapping, int staffId){
		logger.debug("Going to update mapping details.");
		
		
		ComposerMappingService  mappingServiceImpl = (ComposerMappingService) SpringApplicationContext.getBean(serviceNameConstant);
		dbComposerMapping.setLastUpdatedByStaffId(staffId);
		if(newComposerMapping instanceof ASCIIComposerMapping){
			logger.info("Plugin type found ASCIIComposerMapping. ");
			ASCIIComposerMapping  newasciiComposerMapping = (ASCIIComposerMapping) newComposerMapping;
			ASCIIComposerMapping  dbasciiComposerMapping  = (ASCIIComposerMapping) dbComposerMapping;
			mappingServiceImpl.setAndUpdateAsciiComposerDetail(newasciiComposerMapping,dbasciiComposerMapping);
		} else if(newComposerMapping instanceof ASN1ComposerMapping){
			logger.info("Plugin type found ASN1ComposerMapping. ");
			ASN1ComposerMapping  newasn1ComposerMapping = (ASN1ComposerMapping) newComposerMapping;
			ASN1ComposerMapping  dbasn1ComposerMapping  = (ASN1ComposerMapping) dbComposerMapping;
			mappingServiceImpl.setAndUpdateAsn1ComposerDetail(newasn1ComposerMapping,dbasn1ComposerMapping);
		} else if(newComposerMapping instanceof FixedLengthASCIIComposerMapping){
			logger.info("Plugin type found FixedLengthAsciiComposerMapping");
			FixedLengthASCIIComposerMapping newFixedLengthAsciiMapping = (FixedLengthASCIIComposerMapping) newComposerMapping;
			FixedLengthASCIIComposerMapping dbFixedLengthAsciiComposerMapping = (FixedLengthASCIIComposerMapping) dbComposerMapping;
			mappingServiceImpl.setAndUpdateFixedLengthAsciiComposerMapping(newFixedLengthAsciiMapping,dbFixedLengthAsciiComposerMapping);
		}else if(newComposerMapping instanceof RoamingComposerMapping){
			RoamingComposerMapping  newRoamingComposerMapping = (RoamingComposerMapping) newComposerMapping;
			RoamingComposerMapping  dbRoamingComposerMapping  = (RoamingComposerMapping) dbComposerMapping;
			mappingServiceImpl.setAndUpdateRoamingComposerDetail(newRoamingComposerMapping,dbRoamingComposerMapping);
		}
	}
	
	/**
	 * set and update ASCII composer detail
	 * @param newasciiComposerMapping
	 * @param dbasciiComposerMapping
	 */
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class, ignorePropList = "attributeList,composerWrapper,device,composerType,name,destFileExt,mappingType") 
	public ResponseObject setAndUpdateAsciiComposerDetail(ASCIIComposerMapping newasciiComposerMapping, ASCIIComposerMapping dbasciiComposerMapping){
		ResponseObject responseObject = new ResponseObject();
			dbasciiComposerMapping.setFileHeaderEnable(newasciiComposerMapping.getFileHeaderEnable());
			dbasciiComposerMapping.setFieldSeparator(newasciiComposerMapping.getFieldSeparator());
			dbasciiComposerMapping.setFileHeaderParser(newasciiComposerMapping.getFileHeaderParser());
			dbasciiComposerMapping.setFileHeaderContainsFields(newasciiComposerMapping.getFileHeaderContainsFields());
			dbasciiComposerMapping.setFileFooterEnable(newasciiComposerMapping.isFileFooterEnable());
			dbasciiComposerMapping.setFileHeaderSummaryEnable(newasciiComposerMapping.isFileHeaderSummaryEnable());
			dbasciiComposerMapping.setFileFooterSummaryEnable(newasciiComposerMapping.isFileFooterSummaryEnable());
			if(dbasciiComposerMapping.isFileHeaderSummaryEnable() && newasciiComposerMapping.getFileHeaderSummary() != null){
				dbasciiComposerMapping.setFileHeaderSummary(newasciiComposerMapping.getFileHeaderSummary().replaceAll("\r", ""));					
			}
			if(dbasciiComposerMapping.isFileFooterSummaryEnable() && newasciiComposerMapping.getFileFooterSummary() != null){
				dbasciiComposerMapping.setFileFooterSummary(newasciiComposerMapping.getFileFooterSummary().replaceAll("\r", ""));					
			}
			dbasciiComposerMapping.setLastUpdatedDate(new Date());
		
			composerMappingDao.merge(dbasciiComposerMapping); 
			responseObject.setSuccess(true);
			return responseObject;
	}
	
	
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING,actionType = BaseConstants.UPDATE_ACTION, currentEntity = FixedLengthASCIIComposerMapping.class, ignorePropList = "attributeList,composerWrapper,device,composerType,name,destFileExt,mappingType,destDateFormat,destCharset,dateFormatEnum")
	public ResponseObject setAndUpdateFixedLengthAsciiComposerMapping(FixedLengthASCIIComposerMapping newFixedLengthAsciiMapping, FixedLengthASCIIComposerMapping dbFixedLengthAsciiMapping){
		ResponseObject responseObject = new ResponseObject();
		dbFixedLengthAsciiMapping.setFieldSeparator(newFixedLengthAsciiMapping.getFieldSeparator());
		dbFixedLengthAsciiMapping.setLastUpdatedDate(new Date());
		composerMappingDao.merge(dbFixedLengthAsciiMapping);
		responseObject.setObject(true);
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class, ignorePropList = "attributeList,composerWrapper,device,composerType,name,destFileExt,mappingType,destCharset,destDateFormat,multiContainerDelimiter") 
	public ResponseObject setAndUpdateAsn1ComposerDetail(ASN1ComposerMapping newasn1ComposerMapping, ASN1ComposerMapping dbasn1ComposerMapping){
		ResponseObject responseObject = new ResponseObject();
		dbasn1ComposerMapping.setRecMainAttribute(newasn1ComposerMapping.getRecMainAttribute());
		dbasn1ComposerMapping.setStartFormat(newasn1ComposerMapping.getStartFormat());
		dbasn1ComposerMapping.setLastUpdatedDate(new Date());
		composerMappingDao.merge(dbasn1ComposerMapping); 
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_MAPPING, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerMapping.class, ignorePropList = "attributeList,composerWrapper,device,composerType,name,destFileExt,mappingType,destCharset,destDateFormat,multiContainerDelimiter,composeAsSingleRecordEnable,groupAttributeList") 
	public ResponseObject setAndUpdateRoamingComposerDetail(RoamingComposerMapping newRoamingComposerMapping,RoamingComposerMapping dbRoamingComposerMapping){
		ResponseObject responseObject = new ResponseObject();
		dbRoamingComposerMapping.setRecMainAttribute(newRoamingComposerMapping.getRecMainAttribute());
		dbRoamingComposerMapping.setStartFormat(newRoamingComposerMapping.getStartFormat());
		dbRoamingComposerMapping.setLastUpdatedDate(new Date());
		if(newRoamingComposerMapping instanceof TAPComposerMapping){
			logger.info("Plugin type found TAPComposerMapping. ");
		} else if(newRoamingComposerMapping instanceof RAPComposerMapping){
			logger.info("Plugin type found RAPComposerMapping. ");
		} else if(newRoamingComposerMapping instanceof NRTRDEComposerMapping){
			logger.info("Plugin type found NRTRDEComposerMapping. ");
		}
		composerMappingDao.merge(dbRoamingComposerMapping); 
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * Method will get mapping details by mapping id and plug-in type. 
	 * @param mappingId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getComposerMappingDetailsById(int mappingId, String pluginType) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to fetch mapping details for mapping id " + mappingId +  "and plugin type "  + pluginType);
		
		if(mappingId > 0){
			ComposerMapping composerMapping = composerMappingDao.findByPrimaryKey(ComposerMapping.class, mappingId);
			
			if(composerMapping != null ){
				logger.debug("Composer mapping details found successfully.");		
				if(EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.ASCII_COMPOSER_PLUGIN);
					ASCIIComposerMapping asciiComposer = (ASCIIComposerMapping) composerMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(asciiComposer);
				}else if(EngineConstants.XML_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found " + EngineConstants.XML_COMPOSER_PLUGIN);
					XMLComposerMapping xmlComposer = (XMLComposerMapping) composerMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(xmlComposer);
				}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
					logger.info("Plugin type found FixedLengthAscii");
					FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = (FixedLengthASCIIComposerMapping) composerMapping;
					responseObject.setSuccess(true);
					responseObject.setObject(fixedLengthASCIIComposerMapping);
				}

			responseObject.setSuccess(true);
			responseObject.setObject(composerMapping);

			}else{
				logger.info("Failed to fetch parser mapping details for mapping id " + mappingId +  " and plugin type "  + pluginType + " as compoer mapping object found "  + composerMapping);
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
			}
		
		}else if(mappingId == -2){ // Front end option will selected to CUSTOM so need provide default data for this as there is not CUSTOME mapping in database.
			
			if(EngineConstants.ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				ASCIIComposerMapping asciiComposerMapping=new ASCIIComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(asciiComposerMapping);
			}else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				ASN1ComposerMapping asn1ComposerMapping = new ASN1ComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(asn1ComposerMapping);
			}else if(EngineConstants.XML_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				XMLComposerMapping xmlComposerMapping=new XMLComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(xmlComposerMapping);
			}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = new FixedLengthASCIIComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(fixedLengthASCIIComposerMapping);
			}else if(EngineConstants.TAP_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				TAPComposerMapping tapComposerMapping = new TAPComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(tapComposerMapping);
			}else if(EngineConstants.RAP_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				RAPComposerMapping rapComposerMapping = new RAPComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(rapComposerMapping);
			}else if(EngineConstants.NRTRDE_COMPOSER_PLUGIN.equalsIgnoreCase(pluginType)){
				NRTRDEComposerMapping nrtrdeComposerMapping = new NRTRDEComposerMapping();
				responseObject.setSuccess(true);
				responseObject.setObject(nrtrdeComposerMapping);
			}
			
		}else{
			logger.info("Failed to fetch composer mapping details for mapping id " + mappingId +  " and plugin type "  + pluginType);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will fetch all association details for selected mapping
	 * @param mappingId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingAssociationDetails(int mappingId) {
		
		logger.debug("Going to fetch all association for mapping : " + mappingId);
		
		ResponseObject responseObject = new ResponseObject();
		
		if(mappingId > 0){
			List<Composer> composerList = composerMappingDao.getMappingAssociationDetails(mappingId);
			
			if (composerList != null && !composerList.isEmpty()) {
				logger.info(composerList.size() + " association found " +  " for mapping " + mappingId );
				responseObject.setObject(iterateMappingAssocitionDetails(composerList)); // It will iterate and fetch all associted entities details for selected mapping.
				responseObject.setSuccess(true);
			}else{
				logger.info("Failed to fetch mapping details for mapping id " + mappingId);
				responseObject.setObject(composerList);
				responseObject.setSuccess(false);
			}
		}else{
			logger.info("Failed to fetch mapping details for mapping id " + mappingId);
			responseObject.setObject(null);
			responseObject.setSuccess(false);
		}
		

		return responseObject;
	}
	
	/**
	 * Method will iterate composer details and get association name, service, server instances
	 *  for the selected mapping. 
	 * @param composerList
	 * 
	 */
	private JSONArray iterateMappingAssocitionDetails(List<Composer> composerList){
		
		JSONArray jsonArray = new JSONArray();
		
		for (Composer composer : composerList) {
			
			composer.getMyDistDrvPathlist().getDriver().getService().getName();
			composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getServer().getIpAddress();
			 
			JSONObject jsonObject = new JSONObject();
			
			jsonObject.put("serverInstanceName", composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getName());
			jsonObject.put("serverIpAddress",	composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getServer().getIpAddress());
			jsonObject.put("serverInstancePort", composer.getMyDistDrvPathlist().getDriver().getService().getServerInstance().getPort());
			jsonObject.put(BaseConstants.SERVICE_NAME, composer.getMyDistDrvPathlist().getDriver().getService().getName());
			jsonObject.put("pluginName", composer.getName());
			jsonArray.put(jsonObject);
		}
		
		return jsonArray;
	}
	
    @Transactional
	@Override
	public ResponseObject importComposerMappingAndDependents(Composer composer, boolean isImport, int importMode) {
		logger.debug("Importing Composer mapping details.");
		ResponseObject responseObject = null;
		ComposerMapping composerMapping  = composer.getComposerMapping();
		if(composerMapping != null && composerMapping.getMappingType() == BaseConstants.SYSTEM_DEFINED_MAPPING){
			logger.debug("Found System defined mapping.");
			responseObject = setComposerMappingDetails(composer, true, importMode);  // Get and set mapping object by name and plug-in type.
		}else if((composerMapping != null && composerMapping.getMappingType() == BaseConstants.USER_DEFINED_MAPPING) && (composerMapping.getDevice() != null &&  composerMapping.getDevice().getIsPreConfigured() == BaseConstants.SYSTEM_DEFINED_DEVICE) ){
			logger.debug("Found user defined mapping and System defined Device.");
			responseObject = setComposerMappingDetails(composer, false, importMode);  // Get and set mapping object by name and plug-in type.
		}else if((composerMapping != null && composerMapping.getMappingType() == BaseConstants.USER_DEFINED_MAPPING) && (composerMapping.getDevice() != null &&  composerMapping.getDevice().getIsPreConfigured() == BaseConstants.USER_DEFINED_DEVICE) ){
			responseObject = setComposerMappingDetails(composer, false, importMode);  // Get and set mapping object by name and plug-in type.
		}
		return responseObject;
	}
	
	@Override
	public void importComposerMappingForAddAndKeepBothMode(Composer exportedComposer, DistributionDriverPathList pathList, int importMode) {
		logger.debug("going to add composer for import : "+exportedComposer.getName());
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			importComposerMappingAndDependents(exportedComposer, true, importMode);//NOSONAR
		} else {
			ComposerMapping exportedComposerMapping = exportedComposer.getComposerMapping();
			if(exportedComposerMapping != null) {
				ComposerMapping dbComposerMapping = composerMappingDao.getComposerMappingDetailsByNameAndType(exportedComposerMapping.getName(), -1);
				if(dbComposerMapping != null) {
					exportedComposer.setComposerMapping(dbComposerMapping);
				} else {
					importComposerMappingAndDependents(exportedComposer, true, importMode);//NOSONAR
				}
			}
		}
	}
	
	@Transactional
	@Override
	public ResponseObject getComposerMappingDetailsByNameAndType(String name, int pluginTypeId) {
		ResponseObject responseObject = new ResponseObject();
		ComposerMapping composerMapping = composerMappingDao.getComposerMappingDetailsByNameAndType(name, pluginTypeId);
		if(composerMapping != null ){
			responseObject.setSuccess(true);
			responseObject.setObject(composerMapping);
			logger.debug("Composer mapping details found successfully for name  " + name + " plugin id " + pluginTypeId );
		}else{
			logger.info("Failed to get mapping details for given name " + name + " and plugin id " + pluginTypeId);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	/**
	 * Method will fetch mapping object and set it to parser object.
	 * @param name
	 * @param pluginTypeId
	 * @param parser
	 * @return
	 * @throws SMException 
	 */
	private ResponseObject setComposerMappingDetails(Composer composer, boolean flag, int importMode) {
		
		ComposerMapping composerMapping  =  composer.getComposerMapping();
		ResponseObject  responseObject;
		if(flag){
			responseObject  =  getComposerMappingDetailsByNameAndType(composerMapping.getName(), composerMapping.getComposerType().getId());
			   if(responseObject.isSuccess()){
				   composerMapping = (ComposerMapping) responseObject.getObject();
				   composer.setComposerMapping(composerMapping); 
				   responseObject.setSuccess(true);
			   }else{
				   responseObject.setSuccess(false);
				   responseObject.setResponseCode(ResponseCode.IMPORT_FAIL);
			   }
		}else{
			   responseObject = importeComposerMappingDetails(composerMapping, importMode);
		}
		return responseObject;
	}
	/**
	 * Method will import mapping details and verify the device, vendor type and device type entity.
	 * @param parserMapping
	 * @return
	 * @throws SMException 
	 */
	
	private ResponseObject importeComposerMappingDetails(ComposerMapping composerMapping, int importMode) {
		Date date = new Date();
		ResponseObject responseObject ;
		logger.debug("Iterating parser mapping details.");
		
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			composerMapping.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,composerMapping.getName()));
		}
		
		composerMapping.setId(0);
		composerMapping.setCreatedDate(date);
		composerMapping.setLastUpdatedDate(date);
		
		responseObject = verifyAndCreateDeviceDetails(composerMapping);
		if(responseObject.isSuccess()){
			List<ComposerAttribute> attributeList = composerMapping.getAttributeList();
			List<ComposerAttribute> attributeListAssociatedByGroup = new ArrayList<>(0);
			if(attributeList != null && !attributeList.isEmpty()){
				logger.debug("Found " + attributeList.size()  + "  attributes for composer mapping " + composerMapping.getName());
				for(ComposerAttribute attribute: attributeList){
					if(!attribute.isAssociatedByGroup()){
						attribute.setId(0);
						attribute.setCreatedByStaffId(composerMapping.getCreatedByStaffId());
						attribute.setCreatedDate(date);
						attribute.setLastUpdatedDate(date);
						attribute.setLastUpdatedByStaffId(composerMapping.getCreatedByStaffId());
						attribute.setMyComposer(composerMapping);						
					} else {
						attributeListAssociatedByGroup.add(attribute);
					}   
				}
				attributeList.removeAll(attributeListAssociatedByGroup);
				composerMapping.setAttributeList(attributeList); 

			}else{
				logger.debug("No attribute found for composer mapping " + composerMapping.getName());
			}
			List<ComposerGroupAttribute> groupAttributeList = composerMapping.getGroupAttributeList();
			List<ComposerGroupAttribute> groupAttributeListAssociatedByGroup = new ArrayList<>(0);
			if(groupAttributeList != null && !groupAttributeList.isEmpty()){
				logger.debug("Found " + groupAttributeList.size()  + " group attributes for composer mapping " + composerMapping.getName());
				for(ComposerGroupAttribute groupAttribute: groupAttributeList){
					if(!groupAttribute.isAssociatedByGroup()){
						groupAttribute.setId(0);
						groupAttribute.setCreatedByStaffId(composerMapping.getCreatedByStaffId());
						groupAttribute.setCreatedDate(date);
						groupAttribute.setLastUpdatedDate(date);
						groupAttribute.setLastUpdatedByStaffId(composerMapping.getCreatedByStaffId());
						groupAttribute.setMyComposer(composerMapping);
						composerGroupAttributeService.setGroupAttributeHierarchyForImport(groupAttribute,composerMapping);   					
					} else {
						groupAttributeListAssociatedByGroup.add(groupAttribute);
					} 
				}
				groupAttributeList.removeAll(groupAttributeListAssociatedByGroup);
				composerMapping.setGroupAttributeList(groupAttributeList); 

			}else{
				logger.debug("No group attribute found for composer mapping " + composerMapping.getName());
			}
			
			responseObject = this.createNewMapping(composerMapping);
		  	if(responseObject.isSuccess()){
		  		logger.info("Mapping has been created successfully from selected base mapping.");
		  	}else{
		  		logger.info("Failed to create mapping as selected base mapping found  " + composerMapping);
		  		responseObject.setSuccess(false);
		  		responseObject.setObject(null);
		  		responseObject.setResponseCode(ResponseCode.FAIL_CREATE_MAPPING);
		  	}

		}	
		
		return responseObject;
	}

	/**
	 * Method will verify and create device details.
	 * @param parserMapping
	 * @return
	 */
	private ResponseObject verifyAndCreateDeviceDetails(ComposerMapping composerMapping) {
		ResponseObject responseObject ;
		if(composerMapping.getDevice().getIsPreConfigured() == BaseConstants.SYSTEM_DEFINED_DEVICE){
			responseObject = deviceService.getDeviceByName(composerMapping.getDevice().getName());
			if(responseObject.isSuccess()){
				logger.debug("Found already created device  with name " + composerMapping.getDevice().getName());
				composerMapping.setDevice((Device) responseObject.getObject());
				responseObject.setSuccess(true);
			 }
		}else{
			logger.debug("Failed to get device  with name " + composerMapping.getDevice().getName() + " so creating new device.");
			responseObject = deviceService.verifyAndCreateDeviceDetails(composerMapping.getDevice(),composerMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_KEEP_BOTH);
			if(responseObject.isSuccess()){
				composerMapping.setDevice((Device)responseObject.getObject());
			}
		}
		return responseObject;
	}

	/**
	 * Method will validate composer mapping details and its dependents. 
	 * 
	 * 
	 */
	@Override
	public void validateImportedMappingDetails(ComposerMapping composerMapping, List<ImportValidationErrors> importErrorList) {

		if(composerMapping != null){
			composerMappingValidator. validateImportedComposerMappingParameters(composerMapping, composerMapping.getName(), true, importErrorList); 
			if(composerMapping.getMappingType() != BaseConstants.SYSTEM_DEFINED_MAPPING){
				if(composerMapping.getDevice() != null){
					deviceService.validateImportDeviceDetails(composerMapping.getDevice(), importErrorList); // Device and its associated entity(DeviceType and VendorType) validation.
				}else{
					ImportValidationErrors importErrors = new ImportValidationErrors(composerMapping.getName(),composerMapping.getName(), "Device", null, "device.details.get.fail" );
					importErrorList.add(importErrors);
				}
			}
			
			List<ComposerAttribute> attributeList = composerMapping.getAttributeList();	
			if(attributeList != null && !attributeList.isEmpty()){
				logger.debug("Validating parser attribute for mapping " + composerMapping.getName());
				for(ComposerAttribute attribute: attributeList){
					composerAttributeService.validateComoposerAttributes(attribute, importErrorList);
				}
			}
		}else{
			logger.debug("Mapping details found null.");
		}
	}
	
	@Override
	@Transactional
	public ComposerMapping getComposerMappingById(int composerMapppingId){
		return composerMappingDao.findByPrimaryKey(ComposerMapping.class, composerMapppingId);
	}

	@Override
	@Transactional(readOnly =  true)
	public ASN1ComposerMapping getAsn1ComposerMappingById(int mappingId) {
		if( mappingId >0){
			return composerMappingDao.getAsn1ComposerMappingById(mappingId);		
		}
		else{
			return null;
		}
	}
	
	@Override
	public void importComposerMappingForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		//set basic parameter to composer mapping
		logger.debug("going to update composer mapping for import : "+dbComposerMapping.getName());
		updateComposerMappingBasicParameters(dbComposerMapping, exportedComposerMapping);
		
		//set device to composer mapping
		Device dbDevice = deviceService.getDeviceForImportUpdateMode(exportedComposerMapping.getDevice(), dbComposerMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_UPDATE);
		if(dbDevice.getId() != 0){
			dbComposerMapping.setDevice(dbDevice);
		} else {
			ResponseObject responseObject = verifyAndCreateDeviceDetails(exportedComposerMapping);
			if(responseObject.isSuccess()){
				dbComposerMapping.setDevice(exportedComposerMapping.getDevice());
			}
		}
		
		//set composer attribute to composer mapping
		composerAttributeService.importComposerAttributesForUpdateMode(dbComposerMapping, exportedComposerMapping);
		
		//set composer group attribute to composer mapping
		composerGroupAttributeService.importComposerGroupAttributesForUpdateMode(dbComposerMapping, exportedComposerMapping);
	}
	
	@Override
	public void importComposerMappingForAddMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		//set basic parameter to composer mapping
		logger.debug("going to update composer mapping for import : "+dbComposerMapping.getName());
		//set device to composer mapping
		Device dbDevice = deviceService.getDeviceForImportUpdateMode(exportedComposerMapping.getDevice(), dbComposerMapping.getCreatedByStaffId(), BaseConstants.IMPORT_MODE_UPDATE);
		if(dbDevice.getId() != 0){
			dbComposerMapping.setDevice(dbDevice);
		} else {
			ResponseObject responseObject = verifyAndCreateDeviceDetails(exportedComposerMapping);
			if(responseObject.isSuccess()){
				dbComposerMapping.setDevice(exportedComposerMapping.getDevice());
			}
		}
		
		//set composer attribute to composer mapping
		composerAttributeService.importComposerAttributesForAddMode(dbComposerMapping, exportedComposerMapping);
		
		//set composer group attribute to composer mapping
		composerGroupAttributeService.importComposerGroupAttributesForUpdateMode(dbComposerMapping, exportedComposerMapping);
	}
	
	public void updateComposerMappingBasicParameters(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		if(dbComposerMapping instanceof ASCIIComposerMapping && exportedComposerMapping instanceof ASCIIComposerMapping) {
			iterateASCIIComposerMapping((ASCIIComposerMapping) dbComposerMapping, (ASCIIComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof FixedLengthASCIIComposerMapping && exportedComposerMapping instanceof FixedLengthASCIIComposerMapping) {
			iterateFixedLengthASCIIComposerMapping((FixedLengthASCIIComposerMapping) dbComposerMapping, (FixedLengthASCIIComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof ASN1ComposerMapping && exportedComposerMapping instanceof ASN1ComposerMapping) {
			iterateASN1ComposerMapping((ASN1ComposerMapping) dbComposerMapping, (ASN1ComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof DefaultComposerMapping && exportedComposerMapping instanceof DefaultComposerMapping) {
			iterateDefaultComposerMapping((DefaultComposerMapping) dbComposerMapping, (DefaultComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof DetailLocalComposerMapping && exportedComposerMapping instanceof DetailLocalComposerMapping) {
			iterateDetailLocalComposerMapping((DetailLocalComposerMapping) dbComposerMapping, (DetailLocalComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof XMLComposerMapping && exportedComposerMapping instanceof XMLComposerMapping) {
			iterateXMLComposerMapping((XMLComposerMapping) dbComposerMapping, (XMLComposerMapping) exportedComposerMapping);
		} else if(dbComposerMapping instanceof RoamingComposerMapping && exportedComposerMapping instanceof RoamingComposerMapping) {
			iterateRoamingComposerMapping((RoamingComposerMapping) dbComposerMapping, (RoamingComposerMapping) exportedComposerMapping);
		} else {
			iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
		}
	}
	
	public void iterateASCIIComposerMapping(ASCIIComposerMapping dbComposerMapping, ASCIIComposerMapping exportedComposerMapping) {
		dbComposerMapping.setFileHeaderEnable(exportedComposerMapping.getFileHeaderEnable());
		dbComposerMapping.setFileHeaderParser(exportedComposerMapping.getFileHeaderParser());
		dbComposerMapping.setFileHeaderContainsFields(exportedComposerMapping.getFileHeaderContainsFields());
		dbComposerMapping.setFieldSeparatorEnum(exportedComposerMapping.getFieldSeparatorEnum());
		dbComposerMapping.setFieldSeparator(exportedComposerMapping.getFieldSeparator());
		dbComposerMapping.setFileFooterEnable(exportedComposerMapping.isFileFooterEnable());
		dbComposerMapping.setFileFooterSummaryEnable(exportedComposerMapping.isFileFooterSummaryEnable());
		dbComposerMapping.setFileHeaderSummaryEnable(exportedComposerMapping.isFileHeaderSummaryEnable());
		dbComposerMapping.setFileFooterSummary(exportedComposerMapping.getFileFooterSummary());
		dbComposerMapping.setFileHeaderSummary(exportedComposerMapping.getFileHeaderSummary());
		
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateFixedLengthASCIIComposerMapping(FixedLengthASCIIComposerMapping dbComposerMapping, FixedLengthASCIIComposerMapping exportedComposerMapping) {
		iterateASCIIComposerMapping(dbComposerMapping, exportedComposerMapping);
		//iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateASN1ComposerMapping(ASN1ComposerMapping dbComposerMapping, ASN1ComposerMapping exportedComposerMapping) {
		dbComposerMapping.setRecMainAttribute(exportedComposerMapping.getRecMainAttribute());
		dbComposerMapping.setStartFormat(exportedComposerMapping.getStartFormat());
		dbComposerMapping.setMultiContainerDelimiter(exportedComposerMapping.getMultiContainerDelimiter());
		
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateRoamingComposerMapping(RoamingComposerMapping dbComposerMapping, RoamingComposerMapping exportedComposerMapping) {
		dbComposerMapping.setRecMainAttribute(exportedComposerMapping.getRecMainAttribute());
		dbComposerMapping.setStartFormat(exportedComposerMapping.getStartFormat());
		dbComposerMapping.setMultiContainerDelimiter(exportedComposerMapping.getMultiContainerDelimiter());
		dbComposerMapping.setComposeAsSingleRecordEnable(exportedComposerMapping.isComposeAsSingleRecordEnable());
		
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateDefaultComposerMapping(DefaultComposerMapping dbComposerMapping, DefaultComposerMapping exportedComposerMapping) {
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateDetailLocalComposerMapping(DetailLocalComposerMapping dbComposerMapping, DetailLocalComposerMapping exportedComposerMapping) {
		dbComposerMapping.setSrcCharset(exportedComposerMapping.getSrcCharset());
		dbComposerMapping.setKeyValueSeparator(exportedComposerMapping.getKeyValueSeparator());
		dbComposerMapping.setHeaderAttributeDateFormat(exportedComposerMapping.getHeaderAttributeDateFormat());
		
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateXMLComposerMapping(XMLComposerMapping dbComposerMapping, XMLComposerMapping exportedComposerMapping) {
		iterateComposerMapping(dbComposerMapping, exportedComposerMapping);
	}
	
	public void iterateComposerMapping(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		dbComposerMapping.setDestCharset(exportedComposerMapping.getDestCharset());
		dbComposerMapping.setDateFormatEnum(exportedComposerMapping.getDateFormatEnum());
		dbComposerMapping.setDestDateFormat(exportedComposerMapping.getDestDateFormat());
		dbComposerMapping.setDestFileExt(exportedComposerMapping.getDestFileExt());
		dbComposerMapping.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllMappingById(Integer[] ids) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch parser mapping list");
		List<ComposerMapping> composerMappingList = composerMappingDao.getAllMappingById(ids);

		if (composerMappingList != null && !composerMappingList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(composerMappingList);
			logger.info(composerMappingList.size() + "  Composer mapping list found.");
		} else {
			logger.info("Failed to fetch composer mapping list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllMappingByDeviceId(Integer[] deviceIds) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch parser mapping list using given device Ids");
		List<ComposerMapping> composerMappingList = composerMappingDao.getAllMappingByDeviceId(deviceIds);

		if (composerMappingList != null && !composerMappingList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(composerMappingList);
			logger.info(composerMappingList.size() + "  Composer mapping list found.");
		} else {
			logger.info("Failed to fetch composer mapping list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_MAPPING_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will delete composer mapping details and its associated composer
	 * attributes.
	 * 
	 * @param composerMapping
	 * @param staffId
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject deleteMapping(ComposerMapping composerMapping, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Going to delete composer mapping details.");
		if (composerMapping != null) {
			List<ComposerAttribute> attributeList = composerMapping.getAttributeList();
			if (attributeList != null && !attributeList.isEmpty()) {
				logger.debug("Attribute found for composer mapping  " + attributeList.size() + " going to delete all.");
				int size = attributeList.size();
				for (int i = 0; i < size; i++) {
					composerAttributeService.deleteAttribute(attributeList.get(i).getId(), staffId);
				}
				logger.info("Composer mapping and its associated composer attribute have been deleted successfully.");
				responseObject = mergeComposerMapping(composerMapping);
			} else {
				logger.info("Composer mapping details has been deleted successfully.");
				responseObject = mergeComposerMapping(composerMapping);
			}
		} else {
			logger.info("Failed to delete composer mapping details.");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_DELETE_MAPPING);
		}
		return responseObject;
	}
	
	public ResponseObject mergeComposerMapping(ComposerMapping composerMapping){
		ResponseObject responseObject = new ResponseObject();
		composerMappingDao.merge(composerMapping);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.DELETE_MAPPING_SUCCESS);
		return responseObject;
	}
	
}