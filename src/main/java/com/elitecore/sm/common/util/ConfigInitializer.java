package com.elitecore.sm.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.PluginTypeCategoryEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.config.model.EntitiesRegex;
import com.elitecore.sm.config.model.EntityRegexCache;
import com.elitecore.sm.config.model.EntityValidationRange;
import com.elitecore.sm.config.service.ConfigService;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.dictionarymanager.service.DictionaryConfigService;
import com.elitecore.sm.drivers.model.DriverCategory;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.iam.service.AccessGroupService;
import com.elitecore.sm.iam.service.MenuService;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.migration.model.MigrationEntityMapping;
import com.elitecore.sm.migration.service.MigrationEntityMappingService;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.productconfig.model.ProfileEntity;
import com.elitecore.sm.productconfig.service.ProductConfigurationService;
import com.elitecore.sm.samples.AddSamples;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditEntity;
import com.elitecore.sm.systemaudit.model.AuditSubEntity;
import com.elitecore.sm.systemaudit.service.AuditActivityService;
import com.elitecore.sm.systemaudit.service.AuditEntityService;
import com.elitecore.sm.systemaudit.service.AuditSubEntityService;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.LanguageUtilProperties;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
@Configuration
@PropertySource("classpath:jdbc.properties")
public class ConfigInitializer {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
    private ServletContext servletContext;
	
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddl_auto;
	
	@Autowired(required=true)
	@Qualifier(value="configService")
	private ConfigService configService;
	
	@Autowired(required=true)
	@Qualifier(value="menuService")
	private MenuService menuService;
	
	@Autowired(required=true)
	@Qualifier(value="accessGroupService")
	private AccessGroupService accessGroupService;

	@Autowired(required=true)
	@Qualifier(value="systemParameterService")
	private SystemParameterService systemParameterService;
	
	
	@Autowired(required=true)
	@Qualifier(value="servicesService")
	private ServicesService servicesService;
	
	
	@Autowired(required=true)
	@Qualifier(value="serverService")
	private ServerService serverService;
	
	
	@Autowired(required=true)
	@Qualifier(value="parserService")
	private ParserService parserService;
	

	@Autowired(required=true)
	@Qualifier(value="driversService")
	private DriversService driversService;
	
	@Autowired(required=true)
	@Qualifier(value="languageUtilProperties")
	private LanguageUtilProperties languageUtilProperties;
	
	
	@Autowired(required=true)
	@Qualifier(value="auditEntityService")
	private AuditEntityService auditEntityService;
	
	@Autowired(required=true)
	@Qualifier(value="auditSubEntityService")
	private AuditSubEntityService auditSubEntityService;
	
	
	@Autowired(required=true)
	@Qualifier(value="auditActivityService")
	private AuditActivityService  auditActivityService;
	
	@Autowired(required=true)
	@Qualifier(value="productConfigurationService")
	private ProductConfigurationService  productConfigurationService;
	
	
	@Autowired(required=true)
	@Qualifier(value="agentService")
	private AgentService agentService;
	
	@Autowired
	@Qualifier(value="migrationEntityMappingService")
	private MigrationEntityMappingService migrationEntityMappingService;
	
	@Resource(name = "enumMapping")
	private Properties properties ; // = (Properties) SpringApplicationContext.getBean("enumMapping"); // getting spring bean for aop context issue.
	
	@Autowired(required = true)
	@Qualifier(value = "licenseService")
	private LicenseService licenseService;
	
	@Autowired
	@Qualifier(value = "licenseUtilityQualifier")
	LicenseUtility licenseUtility;
	
	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	private DictionaryConfigService dictionaryService;
	
	@Autowired
	ProductConfigurationService productConfiguration;
	
	/**
	 * This method is invoked on Spring Application startup. The bean is initialized in view-controller-context.xml 
	 */
	public void loadOnStartUp(){
		
		
		// add Samples
		new AddSamples(
					hbm2ddl_auto, 
					configService, 
					menuService, 
					accessGroupService,
					systemParameterService,
					serverService,
					servicesService,
					serverInstanceService,
					driversService,
					parserService,
					this,
					servletContext);

		loadConfig();

	}
	
	/**
	 * It loads the config from the database. It is invoked when spring boots up.
	 */
	public void loadConfig() {
		
		loadEnvironmentVariables();
		loadSystemParameter();
		loadCustomerLogo();
		loadOtherConfig();
		loadAllServerType();
		loadActiveServerType();
		loadParserPluginType();
		loadDistributionPluginType();
		loadAllCollectionDriverType();    //load all collection driver masters.
		loadAllDistributionDriverType();  //load all distribution driver masters.
		loadLanguagePropertiesList();     //load all language properties.
		loadAuditEntityList();  		  // load all Audit master entity list.
		loadAuditSubEntityList(); 		  // Load all sub entity list by entity.
		loadAuditActivityList();	      // Load all activity list by sub entity.
		loadAllActivityList(); 			  // Load all activity list with Alias as key.
		//loadAllDictionaryAttribute();   // Method will load all default dictionary attribute list in map cache.
		loadAllServiceTypeMasterData();   // load all service type master data
		
		loadDefaultProfileConfiguration();
		loadMigrationEntityMappingMasterData(); // load all migration entity mapping master data
		loadAllEnumMappingConfiguration(); // Method will read all enum mapping configuration from properties file and add it to cache.
		loadAllAgentTypeMaster(); // Method will load all agent master list and stor it to map cache.
		loadMigrationStatus();	// Method to load migration in process status to check migration is in process or not
		loadMigrateAllPassword();
		loadDefaultDictionaryConfig();
		loadMigrationDictionaryConfig();
		//loadLicenseUtilizationDataInMap(); // These method will load max, current and total CDR count from table into map
		
		// :: Important note :: // 
		// This method call has to be at the end, after all other method load calls are completed
		createCustomProfilesForAllServerTypes();
		loadCustomProfileConfiguration();
		loadCustomProfileEntityStatus();
	}
	
	private void createCustomProfilesForAllServerTypes() {
		logger.debug("Inside createCustomProfilesForAllServerTypes method");
		List<ServerType> serverTypeList = serverService.getAllServerTypeList();
		logger.debug("Getting list of server types");
		if(serverTypeList !=null && !serverTypeList.isEmpty()){
			ResponseObject responseObject = null;
				String serverType;					
				logger.debug("Multiple Server Type  Found with no custom profile");
				for(ServerType type: serverTypeList){
					serverType=type.getAlias();
					logger.info("Custom profiles for Server Type : " + serverType + " going to create");
					responseObject=productConfiguration.findDefaultProfileAndCreateCustomProfile(serverType,null);
					if(responseObject!=null && responseObject.isSuccess())
						logger.info("Custom profiles creation for Server Type - " + serverType + " is successful");
					else
						logger.error("Custom profiles creation for Server Type - " + serverType + " is failed");
				}				
		}
	}
	
	public void loadMigrateAllPassword(){ 
		serverInstanceService.migrateAllPassword();
	}
	/**
	 * It Loads all System Parameter in put into cache																																																																			
	 */
	@SuppressWarnings("deprecation")
	public void loadSystemParameter(){
		List<SystemParameterData> systemParametersList = systemParameterService.loadSystemParameterList();
		logger.info("systemParametersList: " + systemParametersList);
		
		if(systemParametersList!=null){
			for(SystemParameterData systemParameter : systemParametersList){
				MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
			}
		}
	}
	
	/**
	 * It Load default customer logo and store in cache
	 */
	@SuppressWarnings("deprecation")
	public void loadCustomerLogo(){
		try{
			MapCache.addConfigObject(SystemParametersConstant.CUSTOMER_LOGO, systemParameterService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO));
			MapCache.addConfigObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE, systemParameterService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO_LARGE));	
		}catch(SMException e){
			logger.info("Exception occured" +e);
		}
	}
	
	
	/**
	 * It Loads other configuration in put into cache
	 */
	public void loadOtherConfig(){
		List<EntitiesRegex> configList = configService.loadSystemParameters();
		logger.info("ConfigList: " + configList);
		
		if(configList != null ){
			 
			for(EntitiesRegex entityRegexObj : configList){
				Map<String, EntityValidationRange> validationRangeMap = null ;
				List<EntityValidationRange> validationRangeList = entityRegexObj.getValidationRangeList();
				if(validationRangeList != null && !validationRangeList.isEmpty()){
					validationRangeMap = new HashMap<>() ;
					for(EntityValidationRange validationRange:validationRangeList ){
						validationRange.getId();
						validationRangeMap.put(validationRange.getEntityType(), validationRange);
					}
				}
				EntityRegexCache entityRegexCache  = new EntityRegexCache(entityRegexObj,validationRangeMap);
			   MapCache.addConfigObject(BaseConstants.REGEX_MAP_CACHE, entityRegexObj.getKey(), entityRegexCache);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadAllServerType(){
		List<ServerType> liServerType = serverService.getAllServerTypeList();
		logger.info("All ServerTypeList: " + liServerType);
		
		if(liServerType != null && !liServerType.isEmpty()){
			for (ServerType serverType : liServerType) {
				MapCache.addConfigObject(serverType.getAlias(), serverType);
			}
			
			MapCache.addConfigObject(SystemParametersConstant.ALL_SERVER_TYPE_LIST, liServerType);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadActiveServerType(){
		List<ServerType> liServerType = serverService.getActiveServerTypeList();
		logger.info("Active ServerTypeList: " + liServerType);
		
		if(liServerType!=null){
			MapCache.addConfigObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST, liServerType);
		}
	}
	
	/**
	 * Method will load all master parser plug-in list in map cache and store single plug-in type object with key alias.. 
	 */
	@SuppressWarnings("deprecation")
	public void loadParserPluginType(){
		logger.debug("Fetching all parser master composer plugin list.");
		
		List<PluginTypeMaster> pluginTypeList = parserService.getPluginTypeList(PluginTypeCategoryEnum.PARSER.getValue());
		
		if(pluginTypeList != null && !pluginTypeList.isEmpty()){
			logger.info(" Found " + pluginTypeList.size() + " parser plugins. ");
			for (PluginTypeMaster pluginTypeMaster : pluginTypeList) {
				MapCache.addConfigObject(pluginTypeMaster.getAlias(), pluginTypeMaster);
			}
			MapCache.addConfigObject(SystemParametersConstant.PARSER_PLUGIN_TYPE_LIST, pluginTypeList);
		}else{
			logger.info("Parser master plugin list found  null.");
		}
	}
	
	/**
	 * Method will load all master distribution plug-in list in map cache. 
	 */
	@SuppressWarnings("deprecation")
	public void loadDistributionPluginType(){
		
		logger.debug("Fetching all distribution master composer plugin list.");
		
		List<PluginTypeMaster> pluginTypeList = parserService.getPluginTypeList(PluginTypeCategoryEnum.COMPOSER.getValue());
		
		if(pluginTypeList != null && !pluginTypeList.isEmpty()){
			logger.info(" Found  " + pluginTypeList.size() + " distribution plugins. ");
			for (PluginTypeMaster pluginTypeMaster : pluginTypeList) {
				MapCache.addConfigObject(pluginTypeMaster.getAlias(), pluginTypeMaster);
			}
			
			MapCache.addConfigObject(SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST, pluginTypeList);
		}else{
			logger.info("Distribution master plugin list  ");
		}
	}
	
	/**
	 * Method will load collection driver type master.
	 *  
	 */
	@SuppressWarnings("deprecation")
	public void loadAllCollectionDriverType(){
		logger.debug("Fetching all collection driver master.");
		List<DriverType> liDriverType = driversService.getDriverTypeList(DriverCategory.COLLECTION);
		logger.info("DriverTypeList: " + liDriverType);
		if(liDriverType != null && !liDriverType.isEmpty()){
			MapCache.addConfigObject(SystemParametersConstant.COLLECTION_DRIVER_TYPE_LIST, liDriverType);
			for (DriverType driverType : liDriverType) {
				MapCache.addConfigObject(driverType.getAlias(), driverType); // Adding alias as key so in application we can get master type object using alias.
			}
		}
	}
	
	
	/**
	 *Method will load distribution driver type master. 
	 */
	@SuppressWarnings("deprecation")
	public void loadAllDistributionDriverType(){
		logger.debug("Fetching all distribution driver master.");
		List<DriverType> liDriverType = driversService.getDriverTypeList(DriverCategory.DISTRIBUTION);
		logger.info("DriverTypeList: " + liDriverType);
		
		if(liDriverType != null  && !liDriverType.isEmpty()){
			MapCache.addConfigObject(SystemParametersConstant.DISTRIBUTION_DRIVER_TYPE_LIST, liDriverType);
			for (DriverType driverType : liDriverType) {
				MapCache.addConfigObject(driverType.getAlias(), driverType); // Adding alias as key so in application we can get master type object using alias.
			}
		}
	}
	
	/**
	 *Method will load all the language properties and put it in Map 
	 */
	@SuppressWarnings("deprecation")
	public void loadLanguagePropertiesList(){
		logger.info("Adding Language properties in Mapcache");
		MapCache.addConfigObject(SystemParametersConstant.LANGUAGE_PROP_LIST, languageUtilProperties.getAllProperties());
	}
	

	/**
	 *Method will load all Audit Entity in Map cache. 
	 */
	@SuppressWarnings("deprecation")
	public void loadAuditEntityList(){
		logger.info("Adding Audit entity in Mapcache");
		MapCache.addConfigObject(SystemParametersConstant.AUDIT_ENTITY_LIST, auditEntityService.getAllAuditEntity()); //set entity list in cache.
	}
	
	
	/**
	 *Method will load all sub entity list  by entity id. 
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void loadAuditSubEntityList(){
		List<AuditEntity> auditEntityList = (List<AuditEntity>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ENTITY_LIST);
		HashMap<Integer, List<AuditSubEntity>> subEntityMap = new HashMap<>(); //sonar L364
		if(auditEntityList != null && !auditEntityList.isEmpty()){
			for(int i = 0 ; i < auditEntityList.size(); i++ ){
				
				  List<AuditSubEntity> tempSubEntityList = auditSubEntityService.getAllAuditSubEntityByEntityId(auditEntityList.get(i).getId()); 
				
				  if(logger.isDebugEnabled()){
					  logger.debug("Sub entity list is " + tempSubEntityList);
				   }
				
				  if(tempSubEntityList != null && !tempSubEntityList.isEmpty()){
					
					  subEntityMap.put(auditEntityList.get(i).getId(), tempSubEntityList);
				  }else{
					  subEntityMap.put(auditEntityList.get(i).getId(), null);
				  }
			}
			logger.info("Adding Audit subentity in Mapcache");
			MapCache.addConfigObject(SystemParametersConstant.AUDIT_SUB_ENTITY_LIST, subEntityMap); //set sub entity map in cache.
		}
	}
	
	
	/**
	 * Method will fetch all audit activity list by sub entity id
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void loadAuditActivityList(){
		List<AuditSubEntity> subEntityList = auditSubEntityService.getAllAuditSubEntity();
		HashMap<Integer, List<AuditActivity>> auditActivityMap = new HashMap<>(); // sonar  Line 391
		if(subEntityList != null && !subEntityList.isEmpty()){
			
			for(int i = 0 ; i < subEntityList.size(); i++ ){
				
				List<AuditActivity> tempActivityList = auditActivityService.getAllAuditActivityBySubEntityId(subEntityList.get(i).getId());
				
				if(logger.isDebugEnabled()){
					logger.debug("Sub entity list is " + tempActivityList);
				}
				  
				 if(tempActivityList != null && !tempActivityList.isEmpty()){
					 auditActivityMap.put(subEntityList.get(i).getId(), tempActivityList);
				  }else{
					  auditActivityMap.put(subEntityList.get(i).getId(), null);
				  }
			}
			logger.info("Adding Audit activity in Mapcache");
			MapCache.addConfigObject(SystemParametersConstant.AUDIT_ACTIVITY_LIST, auditActivityMap); //set audit activity map in cache.
		}
	}
	/**
	 * Method will fetch all Activity List and put it into the Map with activity Alias as key
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void loadAllActivityList(){
		HashMap<String, AuditActivity> auditActivityMap = new HashMap<>(); //sonar issue L-415
		List<AuditActivity> activityList = auditActivityService.getAllAuditActivity();
		for (int i = 0; i < activityList.size(); i++) {
			auditActivityMap.put(activityList.get(i).getAlias(), activityList.get(i));
		}
		logger.info("Adding Audit activity with its alias as a action in Mapcache");
		MapCache.addConfigObject(SystemParametersConstant.AUDIT_ACTIVITY_ALIAS_LIST, auditActivityMap); //set audit activity alias map in cache.
	} 	
	
	/**
	 * Add Default Profile Configuration for all server type
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void loadDefaultProfileConfiguration(){
		logger.debug("Fetching Default Profile configuration and add to cache.");
		int licenseId=-1;
		//MED-4633 - Need to uncheck some plugins from default config, if not supported - causing problem in multistack license apply
		/*ResponseObject licenseResponseObject = licenseService.getLicenseByServerId(licenseUtility.getServerId());
		int licenseId=-1;
		if(licenseResponseObject.isSuccess() && licenseResponseObject.getObject()!=null){
			licenseId=((License)licenseResponseObject.getObject()).getId();
		}*/
		List<ServerType> serverTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ALL_SERVER_TYPE_LIST, null);
		if(serverTypeList !=null && !serverTypeList.isEmpty()){
			for(ServerType serverType:serverTypeList){
				ResponseObject responseObject=productConfigurationService.findProfileDetailByServerTypeId(serverType.getId(),licenseId,true);
				if(responseObject.isSuccess()){
					MapCache.addConfigObject(SystemParametersConstant.DEFAULT_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverType.getId()),(List<ProfileEntity>)responseObject.getObject());		
				}
			}
		}
	}
	
	/**
	 * Add Custom Profile Configuration for all server type
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void loadCustomProfileConfiguration(){
		
		logger.debug("Fetching Custom Profile configuration and add to cache.");
		//ResponseObject licenseResponseObject = licenseService.getLicenseByServerId(licenseUtility.getServerId());
		int licenseId=-1;
		//if(licenseResponseObject.isSuccess() && licenseResponseObject.getObject()!=null){
		//	licenseId=((License)licenseResponseObject.getObject()).getId();
		//}
		List<ServerType> serverTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST, null);
		if(serverTypeList !=null && !serverTypeList.isEmpty()){
			for(ServerType serverType:serverTypeList){
				ResponseObject responseObject=productConfigurationService.findProfileDetailByServerTypeId(serverType.getId(),licenseId,false);
				if(responseObject.isSuccess()){
					MapCache.addConfigObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST,String.valueOf(serverType.getId()),(List<ProfileEntity>)responseObject.getObject());		
				}
			}
		}
	}
	
	/**
	 * Add Custom Profile Entity Status into cache
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void loadCustomProfileEntityStatus(){
		
		logger.debug("Fetching Custom Profile configuration and add Entity status  into cache.");
		
		List<ServerType> serverTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST, null);
		if(serverTypeList !=null && !serverTypeList.isEmpty()){
			
			for(ServerType serverType:serverTypeList){
				List<ProfileEntity> customProfileList=(List<ProfileEntity> )MapCache.getConfigCollectionAsObject(SystemParametersConstant.CUSTOM_PRODUCT_CONFIGURATION_LIST, String.valueOf(serverType.getId()), null);
				
				if(customProfileList !=null && !customProfileList.isEmpty()){
					productConfigurationService.refreshProfileEntityStatusCache(serverType.getId(), customProfileList);
				}else{
					logger.debug("Custom profile not found for server type." +serverType.getId());
				}
			}
		}
	}
		
	/**
	 * Find unique entity type from profile entity list
	 * @param entityTypeSet
	 * @param customProfileList
	 */
	public void findUniqueEntityTypeFromEntityProfile(Set<String> entityTypeSet,List<ProfileEntity> customProfileList){
		
		for (ProfileEntity profileEntity : customProfileList) {

			entityTypeSet.add(profileEntity.getEntityType());
		}
	}
	
	
	/**
	 * Method will load all service master type entity.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void loadAllServiceTypeMasterData(){
		 List<ServiceType> serviceTypeList = servicesService.getServiceTypeList();
		 
		 if(serviceTypeList != null && !serviceTypeList.isEmpty()){
			 logger.info("Service type list found successfully.");
			 MapCache.addConfigObject(SystemParametersConstant.SERVICE_TYPE_LIST, serviceTypeList); //set service type list in map cache.
		
			 for (ServiceType serviceType : serviceTypeList) {
				 MapCache.addConfigObject(serviceType.getAlias(), serviceType); //set service type objects with alias key in map cache.
			 }
		 }else{
			 logger.info("Service Type list found null!");
		 }
	}
	
	/**
	 * Method will load all migration entity mapping.
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void loadMigrationEntityMappingMasterData(){
		ResponseObject responseObject = migrationEntityMappingService.getMigrationEntityMappingList();
		if(responseObject.isSuccess()) {
			List<MigrationEntityMapping> entityMappings = (List<MigrationEntityMapping>) responseObject.getObject();
			for (MigrationEntityMapping mapping : entityMappings) {
				MapCache.addConfigObject(mapping.getXmlName(), mapping); //set mapping object with xml name key in map cache.
			}
		} else {
			logger.info("MigrationEntityMapping list found null!");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadMigrationStatus() {
		MapCache.addConfigObject(BaseConstants.IS_MIGRATION_IN_PROCESS, false); //set migration in process false
	}
	
	/**
	 * Method will read all enum mapping configuration from properties file and add it to cache.
	 */
	@SuppressWarnings("deprecation")
	public void loadAllEnumMappingConfiguration(){
		logger.info("Reading all enum mapping configuration from properties file.");
		
		if(properties != null && !properties.keySet().isEmpty()){
			logger.info("Enum mapping properties file configuration read successfull!");
			Set<Object> keys = properties.keySet();
			for(Object k:keys){
				   String key = (String)k;
				   MapCache.addConfigObject(key,properties.get(key));
			}
		}else{
			logger.error("Failed to read enum mapping configuration properties file for migration.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void loadAllAgentTypeMaster(){
		logger.info("Fetching all agent master type and going to store it to map cache.");
		List<AgentType> agentTypeList = agentService.getAllAgentType();
		if(agentTypeList != null && !agentTypeList.isEmpty()){
			logger.info("Agent type list found successfully!");
			for (AgentType agentType : agentTypeList) {
				MapCache.addConfigObject(agentType.getAlias(), agentType); //set service type objects with alias key in map cache.
			}
		}else{
			logger.error("Failed to get agent type master list.");
		}
		
		
	}
	/**
	 * @return the logger
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	/**
	 * @return the hbm2ddl_auto
	 */
	public String getHbm2ddl_auto() {
		return hbm2ddl_auto;
	}

	/**
	 * @param hbm2ddl_auto the hbm2ddl_auto to set
	 */
	public void setHbm2ddl_auto(String hbm2ddl_auto) {
		this.hbm2ddl_auto = hbm2ddl_auto;
	}

	/**
	 * @return the configService
	 */
	public ConfigService getConfigService() {
		return configService;
	}

	/**
	 * @param configService the configService to set
	 */
	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}

	/**
	 * @return the menuService
	 */
	public MenuService getMenuService() {
		return menuService;
	}

	/**
	 * @param menuService the menuService to set
	 */
	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}

	
	/**
	 * @return the accessGroupService
	 */
	public AccessGroupService getAccessGroupService() {
		return accessGroupService;
	}

	/**
	 * @param accessGroupService the accessGroupService to set
	 */
	public void setAccessGroupService(AccessGroupService accessGroupService) {
		this.accessGroupService = accessGroupService;
	}

	/**
	 * @return the systemParameterService
	 */
	public SystemParameterService getSystemParameterService() {
		return systemParameterService;
	}

	/**
	 * @param systemParameterService the systemParameterService to set
	 */
	public void setSystemParameterService(
			SystemParameterService systemParameterService) {
		this.systemParameterService = systemParameterService;
	}

	public DriversService getDriverService() {
		return driversService;
	}

	public void setDriverService(DriversService driversService) {
		this.driversService = driversService;
	}
	
private void loadDefaultDictionaryConfig(){
		
		long cnt = dictionaryService.getTotalDictionaryEntries();
		if(cnt <= 0){
			String repositoryPath = servletContext.getRealPath(BaseConstants.BASE_PATH_FOR_EXPORT)+File.separator;
			
			File baseFolderPath = new File(repositoryPath+"dictionary"+File.separator);
			servletContext.getContextPath();
			List<File> fileList = new ArrayList<>();
			try{
				listFilesForFolder(baseFolderPath, fileList);
				DictionaryConfig dicConfig;
				
				for(File lookupDataFile : fileList){
					dicConfig = new DictionaryConfig();
					dicConfig.setCreatedByStaffId(1);
					dicConfig.setCreatedDate(new Date());
					dicConfig.setLastUpdatedByStaffId(1);
					dicConfig.setLastUpdatedDate(new Date());
					dicConfig.setId(0);
					dicConfig.setIsDefault(true);
					dicConfig.setUpdated(false);
					dicConfig.setIpAddress("0");
					dicConfig.setStatus(StateEnum.ACTIVE);
					dicConfig.setUtilityPort(0);
					String filePath = lookupDataFile.getParent().replace(repositoryPath, "");
					dicConfig.setPath(filePath);
					dicConfig.setFilename(lookupDataFile.getName());
					dicConfig.setDicFile(EliteUtils.convertFileToByteArray(lookupDataFile));
					dictionaryService.uploadDictionaryDataFile(dicConfig);
				}
				
			}catch(Exception ex){
				logger.error("Failed to insert default dictionary config."+ ex.getMessage());
				logger.error(ex);
			}
		}
	}

	public void listFilesForFolder(final File folder, List<File> fileList) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry, fileList);
	        } else {
	            fileList.add(fileEntry);
	        }
	    }
	}
	

	@SuppressWarnings("unused")
	private void loadMigrationDictionaryConfig(){
		List<Server> servers=serverService.getServerList();
		for(Server server: servers) {
			List<DictionaryConfig> configs=dictionaryService.getDictionaryConfigList(server.getIpAddress(), server.getUtilityPort());
			if(configs.isEmpty()) {
				//Start:MED-10547 : do entry in TBLTFILEINFO from default dictionary data
				List<DictionaryConfig> dictionaryConfigList=(List<DictionaryConfig>)dictionaryService.getDefaultDictionaryConfigObj();
				for(DictionaryConfig dictionaryConfig : dictionaryConfigList) {
					ResponseObject dictResponseObject=dictionaryService.createDictionaryData(dictionaryConfig,server);
					if(dictResponseObject.isSuccess()){
						logger.debug("INSERT SUCCESS : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
					}else{
						logger.debug("INSERT FAIL : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
					}
				}//END
			}
		}
	}	

	private void loadEnvironmentVariables() {
		 MapCache.addConfigObject(BaseConstants.KUBERNETES_ENV,  System.getenv(BaseConstants.KUBERNETES_ENV));
	//	 MapCache.addConfigObject(BaseConstants.SKIP_LICENSE,  System.getenv(BaseConstants.SKIP_LICENSE));
	}
	
	private void loadLicenseUtilizationDataInMap() {
		Map<Date, Long> map1 = licenseService.getHourWiseTotalUtilizationMap();
		Map<Date, Long> map2 = licenseService.getHourWiseCurrentUtilizationMap();
		MapCache.addConfigObject(BaseConstants.HOUR_WISE_TOTAL_TPS_MAP,  map1);
		MapCache.addConfigObject(BaseConstants.HOUR_WISE_CURRENT_TPS_MAP,  map2);
		
		long maxCount = licenseService.getMaxLicneseUtilizationCount(map1);
		long currentCount = licenseService.getCurrentLicenseUtilizationCountFromMap(map2);
		MapCache.addConfigObject(BaseConstants.CURRENT_LICENSE_TPS,  Utilities.getTPSByHour(currentCount));
		MapCache.addConfigObject(BaseConstants.MAX_LICENSE_TPS,  Utilities.getTPSByHour(maxCount));
		servletContext.setAttribute(BaseConstants.LAST_UPDATED_DATE_OF_HOUR_WISE_MAP, Utilities.getLastHourDate());
	}
	
}
