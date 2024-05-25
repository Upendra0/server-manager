package com.elitecore.sm.productconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.PluginTypeCategoryEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.productconfig.dao.ProductConfigurationDao;
import com.elitecore.sm.productconfig.model.ProfileEntity;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.model.ServiceTypeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Service(value="productConfigurationService")
public class ProductConfigurationServiceImpl implements ProductConfigurationService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ProductConfigurationDao productConfigDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	DriversService driversService;
	
	@Autowired
	ParserService  parserService;
	
	@Autowired
	AgentService agentService;
	
	/**
	 * copy default profile into custom profile 
	 * @param serverType
	 * @param staffIdImplementation
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject createCustomProfileUsingServerType(License license){
		
		logger.debug("Inside createCustomProfileUsingServerType , Server Type is " + license.getProductType());
		ResponseObject responseObject=new ResponseObject();
		String[] serverTypeArr;
		String serverType;
		
		if(!StringUtils.isEmpty(license.getProductType())){
			if(license.getProductType().contains(",")){
				logger.debug("Multiple Server Type  Found");
				serverTypeArr=license.getProductType().split(",");
				
				for(int i=0;i<serverTypeArr.length;i++){
					serverType=serverTypeArr[i];
					responseObject=findDefaultProfileAndCreateCustomProfile(serverType,license);
				}
			}else{
				logger.debug("Single Server Type  Found");
				responseObject=findDefaultProfileAndCreateCustomProfile(license.getProductType(),license);
			}
		}
		
		return responseObject;
	}
	
	/**
	 * Fetch profile detail using server type id
	 * @param serverTypeAlias
	 * @param staffId
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject findDefaultProfileAndCreateCustomProfile(String serverTypeAlias,License license){
		ResponseObject responseObject=new ResponseObject();
		logger.debug("Process to create custom profile for server type :: " +serverTypeAlias);
		ServerType serverTypeObj=(ServerType)MapCache.loadMasterEntityByAlias(serverTypeAlias,SystemParametersConstant.ALL_SERVER_TYPE_LIST);
		if(serverTypeObj !=null ){
		
			List<ProfileEntity> defaultprofileEntityList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.DEFAULT_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverTypeObj.getId()),null);
			List<ProfileEntity> customProfileList;
			
			if(defaultprofileEntityList !=null && !defaultprofileEntityList.isEmpty()){
				customProfileList=new ArrayList<>();
				
				for(int i=0,size=defaultprofileEntityList.size();i<size;i++){
					ProfileEntity profileEntity=defaultprofileEntityList.get(i);
					
					ProfileEntity customeProfileEntity = productConfigDao.findCustomProfileEntity(profileEntity.getServerType().getId(),profileEntity.getEntityAlias(),false);
					if(customeProfileEntity!=null) {
						customProfileList.add(customeProfileEntity);
						responseObject.setSuccess(true);
						continue;
					}
					ProfileEntity profileEntity1 = new ProfileEntity();
					
					profileEntity1.setEntityType(profileEntity.getEntityType());
					profileEntity1.setEntityAlias(profileEntity.getEntityAlias());
					profileEntity1.setStatus(profileEntity.getStatus());
					profileEntity1.setServerType(profileEntity.getServerType());
					profileEntity1.setIsDefault(false);
					profileEntity1.setCreatedDate(new Date());
					//profileEntity1.setCreatedByStaffId(license.getCreatedByStaffId());
					profileEntity1.setCreatedByStaffId(1);
					profileEntity1.setLastUpdatedDate(new Date());
					//profileEntity1.setLastUpdatedByStaffId(license.getCreatedByStaffId());
					profileEntity1.setLastUpdatedByStaffId(1);
					profileEntity1.setLicense(license);
					logger.info("Entity Alias :::::: "+profileEntity.getEntityAlias() + " Server Type ::: "+profileEntity.getServerType() + " IsDefault ::::: "+profileEntity.getIsDefault());
					try {
						productConfigDao.merge(profileEntity1);
						logger.debug("Profile Entity created successfully for custom profile with id " +profileEntity1.getId() );
						customProfileList.add(profileEntity1);
						responseObject.setSuccess(true);
					} catch(Exception e) {  // NOSONAR
						logger.debug("Fail to create Profile Entity"  );
						responseObject.setSuccess(false);
						break;
					}
				}
				refreshCustomProfileCache(serverTypeObj.getId(), customProfileList);
				refreshProfileEntityStatusCache(serverTypeObj.getId(), customProfileList);
			}else{
				logger.debug("Default Profile not found "  );
				responseObject.setSuccess(false);
			}
		}
		return responseObject;
	}
	
	
	/**
	 * Fetch default and custom profile detail using server type id
	 * @param serverTypeId
	 * @param isDefault
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject findProfileDetailByServerTypeId(int serverTypeId,int licenseId,boolean isDefault){
		ResponseObject responseObject=new ResponseObject();
		
		List<ProfileEntity> defaultprofileEntityList=productConfigDao.findProfileDetailByServerTypeId(serverTypeId,licenseId,isDefault);
		if(defaultprofileEntityList !=null && !defaultprofileEntityList.isEmpty()){
			logger.debug("Default Profile Entity Detail find successfully"  );
			responseObject.setSuccess(true);
			responseObject.setObject(defaultprofileEntityList);
		}else{
			logger.debug("Fail to fetch default Profile Entity"  );
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	/**
	 * Update custom profile 
	 * @param serverTypeId
	 * @param selectedEntities
	 * @param staffId
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public ResponseObject updateProductConfiguration(int serverTypeId,String selectedEntities,int staffId) throws CloneNotSupportedException{
		logger.debug("inside updateProductConfiguration serverTypeId :: "+serverTypeId+ " selectedEntities:: " +selectedEntities );
		
		ResponseObject responseObject=new ResponseObject();
		boolean isActive=false;
		
		if(!StringUtils.isEmpty(selectedEntities)){
			
			List<ProfileEntity> customProfileEntityList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverTypeId),null);
			
			JSONArray selectedEntitiesArr=new JSONArray(selectedEntities);
			if(customProfileEntityList !=null && !customProfileEntityList.isEmpty()){
				for(int i=0,size=customProfileEntityList.size();i<size;i++){
					ProfileEntity profileEntity = customProfileEntityList.get(i);
					ProfileEntity entityClonObj  = (ProfileEntity) profileEntity.clone();
					for(int j =0 , size1 = selectedEntitiesArr.length(); j <size1 ; j++ ){
						if(profileEntity.getId() == selectedEntitiesArr.getInt(j)){
							isActive=true;
							break;
						}else{
							isActive=false;
							entityClonObj.setStatus(StateEnum.INACTIVE);
						}
				}
				
					if(isActive){
						logger.debug("Make Profile Entity Active for entity id " + profileEntity.getId());
						entityClonObj.setStatus(StateEnum.ACTIVE);
					}else{
						logger.debug("Make Profile Entity Inactive for entity id " + profileEntity.getId());
						entityClonObj.setStatus(StateEnum.INACTIVE);
						
					}
					entityClonObj.setLastUpdatedDate(new Date());
					entityClonObj.setLastUpdatedByStaffId(staffId);
					
					customProfileEntityList.set(i, entityClonObj);
					
					ProductConfigurationService productConfigurationService = (ProductConfigurationService) SpringApplicationContext.getBean("productConfigurationService");
					productConfigurationService.updateProductConfig(entityClonObj);
			}
				
				refreshCustomProfileCache(serverTypeId,customProfileEntityList);
				refreshProfileEntityStatusCache(serverTypeId, customProfileEntityList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PRODUCT_CONFIGURATION_UPDATE_SUCCESS);
		}
		}
		
		return responseObject;
		
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_PRODUCT_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "serverType,license")
	public ResponseObject updateProductConfig(ProfileEntity profileEntity){
		ResponseObject responseObject = new ResponseObject();
		productConfigDao.merge(profileEntity);		
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	/**
	 * Reset Default Product configuration
	 * @param serverTypeId
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	@SuppressWarnings("unchecked")
	public ResponseObject resetProductConfiguration(int serverTypeId,int staffId){
		logger.debug("inside resetProductConfiguration serverTypeId :: "+serverTypeId );
		
		ResponseObject responseObject = new ResponseObject();
		
		List<ProfileEntity> defaultprofileEntityList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.DEFAULT_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverTypeId),null);
		
		List<ProfileEntity> customProfileList = (List<ProfileEntity>) MapCache.getConfigCollectionAsObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverTypeId),null);
			
			if(defaultprofileEntityList !=null && !defaultprofileEntityList.isEmpty() && customProfileList !=null && !customProfileList.isEmpty()){
				
				ProductConfigurationService productConfigurationService = (ProductConfigurationService) SpringApplicationContext.getBean("productConfigurationService");
				
				for(ProfileEntity customprofileEntity:customProfileList){
					for(ProfileEntity defaultprofileEntity:defaultprofileEntityList){
						
						if(customprofileEntity.getEntityAlias().equals(defaultprofileEntity.getEntityAlias()) && customprofileEntity.getServerType().getId() == (defaultprofileEntity.getServerType().getId())){
							logger.debug("Default Profile status for entity  :: "+ defaultprofileEntity.getEntityAlias() +" is" +defaultprofileEntity.getStatus() );
							customprofileEntity.setStatus(defaultprofileEntity.getStatus());
							break;
						}
					}
					
					customprofileEntity.setLastUpdatedDate(new Date());
					customprofileEntity.setLastUpdatedByStaffId(staffId);
					productConfigurationService.resetProductConfiguration2(customprofileEntity);

					
				}
				refreshCustomProfileCache(serverTypeId, customProfileList);
				refreshProfileEntityStatusCache(serverTypeId, customProfileList);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.RESET_PRODUCT_CONFIGURATION_SUCCESS);
		}
		
		return responseObject;
		
	}
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PRODUCT_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "serverType,license")
	public ResponseObject resetProductConfiguration2(ProfileEntity customprofileEntity){
		ResponseObject responseObject = new ResponseObject();
		productConfigDao.merge(customprofileEntity);
		responseObject.setSuccess(true);
		return responseObject;
	}
	/**
	 * Refresh Custom Profile Configuration cache
	 * @param serverTypeId
	 * @param customProfileEntityList
	 */
	public void refreshCustomProfileCache(int serverTypeId,List<ProfileEntity> customProfileEntityList){
		
		logger.debug("inside refreshCustomProfileCache for server type id : " +serverTypeId);
		MapCache.addConfigObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverTypeId),customProfileEntityList);
		
	}
	
	/**
	 * Refresh Custom Profile Entity Status Cache
	 * @param serverTypeId
	 * @param customProfileEntityList
	 */
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void refreshProfileEntityStatusCache(int serverTypeId,List<ProfileEntity> customProfileEntityList) {
		logger.debug("inside refreshProfileEntityStatusCache for server type id : " +serverTypeId);

			if(customProfileEntityList !=null && !customProfileEntityList.isEmpty()){
					
					Set<String> entityTypeSet = new LinkedHashSet<>();
					for (ProfileEntity profileEntity : customProfileEntityList) {

						entityTypeSet.add(profileEntity.getEntityType());
					}
				
					Map<String,List> enityStatusMap=new HashMap<>();
					List<ServiceType> mainserviceTypeList=new ArrayList<>();
					List<ServiceType> addserviceTypeList=new ArrayList<>();
					List<DriverType> collectionDriverTypeList=new ArrayList<>();
					List<DriverType> distributionDriverTypeList=new ArrayList<>();
					List<PluginTypeMaster> parserList=new ArrayList<>();
					List<PluginTypeMaster> composerList=new ArrayList<>();
					List<AgentType> agentList=new ArrayList<>();
					List<String> generalEntityList=new ArrayList<>();
					
					for (String entityType : entityTypeSet) {
						
						
						for(ProfileEntity profileEntity:customProfileEntityList){
							if (entityType.equals(profileEntity.getEntityType()) && StateEnum.ACTIVE.equals(profileEntity.getStatus())) {
								
									addProfileEntityTypeIntoCache(profileEntity,mainserviceTypeList,addserviceTypeList,collectionDriverTypeList,distributionDriverTypeList,
																						parserList,composerList,agentList,generalEntityList);
								
							}
						}
						enityStatusMap.put(BaseConstants.MAIN_SERVICE_TYPE,mainserviceTypeList);
						enityStatusMap.put(BaseConstants.ADDITIONAL_SERVICE_TYPE,addserviceTypeList);
						enityStatusMap.put(BaseConstants.COLLECTION_DRIVER_TYPE,collectionDriverTypeList);
						enityStatusMap.put(BaseConstants.DISTRIBUTION_DRIVER_TYPE,distributionDriverTypeList);
						enityStatusMap.put(BaseConstants.COMPOSER_PLUGIN_TYPE,composerList);
						enityStatusMap.put(BaseConstants.PARSER_PLUGIN_TYPE,parserList);
						enityStatusMap.put(BaseConstants.AGENT_TYPE,agentList);
						enityStatusMap.put(BaseConstants.GENERAL_TYPE_PROFILING,generalEntityList);
					
				}
					MapCache.addConfigObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_ENTITY_STATUS,String.valueOf(serverTypeId),(Map<String,List>) enityStatusMap);
			}
	}

	/**
	 * Add Profile Entity Status into cache
	 * @param profileEntity
	 * @param mainserviceTypeList
	 * @param addserviceTypeList
	 * @param collectionDriverTypeList
	 * @param distributionDriverTypeList
	 * @param parserList
	 * @param composerList
	 * @param agentList
	 * @param generalEntityList
	 */
public void addProfileEntityTypeIntoCache(ProfileEntity profileEntity,List<ServiceType> mainserviceTypeList,List<ServiceType> addserviceTypeList,List<DriverType> collectionDriverTypeList,List<DriverType> distributionDriverTypeList,
		List<PluginTypeMaster> parserList,List<PluginTypeMaster> composerList,List<AgentType> agentList,List<String> generalEntityList){

		if (BaseConstants.SERVICE_TYPE_PROFILING.equals(profileEntity.getEntityType())) {
			logger.debug("Cache Service Type");
			ResponseObject responseObject = servicesService.getServiceTypeByAlias(profileEntity.getEntityAlias());
			if (responseObject.isSuccess()) {
				ServiceType serviceType = (ServiceType) responseObject.getObject();
				if (ServiceTypeEnum.MAIN.equals(serviceType.getTypeOfService())) {
					mainserviceTypeList.add(serviceType);
				} else if (ServiceTypeEnum.ADDITIONAL.equals(serviceType.getTypeOfService())) {
					addserviceTypeList.add(serviceType);
				}

			}
		} else if (BaseConstants.DRIVER_TYPE_PROFILING.equals(profileEntity.getEntityType())) {

			logger.debug("Cache Driver Type");
			ResponseObject responseObject = driversService.getDriverTypeByAlias(profileEntity.getEntityAlias());
			if (responseObject.isSuccess()) {
				DriverType driverType = (DriverType) responseObject.getObject();
				if (DriverCategory.COLLECTION.equals(driverType.getCategory())) {
					collectionDriverTypeList.add((DriverType) responseObject.getObject());
				} else if (DriverCategory.DISTRIBUTION.equals(driverType.getCategory())) {
					distributionDriverTypeList.add((DriverType) responseObject.getObject());
				}
			}

		} else if (BaseConstants.PLUGIN_TYPE_PROFILING.equals(profileEntity.getEntityType())) {
			logger.debug("Cache Plugin Type");
			ResponseObject responseObject = parserService.getPluginByType(profileEntity.getEntityAlias());
			if (responseObject.isSuccess()) {
				PluginTypeMaster pluginType = (PluginTypeMaster) responseObject.getObject();
				if (PluginTypeCategoryEnum.PARSER.getValue().equals(pluginType.getCategory())) {
					parserList.add(pluginType);
				} else if (PluginTypeCategoryEnum.COMPOSER.getValue().equals(pluginType.getCategory())) {
					composerList.add(pluginType);
				}
			}
		}else if(BaseConstants.AGENT_TYPE_PROFILING.equals(profileEntity.getEntityType())){
			logger.debug("Cache Agent Type");
			AgentType agentType=agentService.getAgentTypeByAlias(profileEntity.getEntityAlias());
			agentList.add(agentType);
		}else if(BaseConstants.GENERAL_TYPE_PROFILING.equals(profileEntity.getEntityType())){
			generalEntityList.add(profileEntity.getEntityAlias());
		}
}
}
