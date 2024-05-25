/**
 * 
 */
package com.elitecore.sm.composer.service;


import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.CharacterRenameOperationDao;
import com.elitecore.sm.composer.dao.ComposerAttributeDao;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.dao.ComposerMappingDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.validator.ComposerValidator;
import com.elitecore.sm.device.dao.DeviceDao;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.parser.dao.PluginTypeDao;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value="composerService")
public class ComposerServiceImpl implements ComposerService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ComposerDao composerDao;
	
	@Autowired
	PluginTypeDao pluginTypeDao;

	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	ComposerMappingDao composerMappingDao;
	
	@Autowired
	CharacterRenameOperationDao charRenameOperationDao;

	@Autowired
	ComposerValidator composerValidator;

	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	ComposerAttributeDao composerAttributeDao;
	
	@Autowired
	DeviceDao deviceDao;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	CharRenameOperationService charRenameOperationService;
	
	@Autowired
	ComposerAttributeService composerAttributeService;
	
	private String failureLoggerMsg = "Failed to get composer plugin master for alias ";
	
	private String failureConstant = "Failed to get pathlist details for id ";
	/**
	 * @param composer
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_COMPOSER, actionType = BaseConstants.CREATE_ACTION, currentEntity = Composer.class,ignorePropList= "" )
	public ResponseObject addComposer(Composer composer) {
		logger.debug("Inside:addComposer  Adding new composer.");
		ResponseObject responseObject = new ResponseObject();
		
		if(isUniqueComposer(composer.getName())){  // It will check unique composer name.
			
			if(composer.getComposerType().getId() > 0 ){
				PluginTypeMaster pluginMaster =   (PluginTypeMaster) MapCache.loadMasterEntityById(composer.getComposerType().getId(), SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);  
				if(pluginMaster != null ){
					composer.setComposerType(pluginMaster);
					 if(composer.getMyDistDrvPathlist().getId() > 0 ){
						
						 PathList distributionDriverPathList = pathListDao.getDistributionPathListById(composer.getMyDistDrvPathlist().getId());
						 
						 if(distributionDriverPathList != null ){
							 	composer.setMyDistDrvPathlist((DistributionDriverPathList) distributionDriverPathList);
							 	composer.setComposerMapping(null);
							 	composer.setCharRenameOperationList(null);
							 
							 	composerDao.save(composer);
								
								if(composer.getId() >  0){
									logger.info("Composer has been created successfully.");
									responseObject.setSuccess(true);
									responseObject.setObject(setComposerJSONObject(composer));
									responseObject.setResponseCode(ResponseCode.CREATE_COMPOSER_SUCCESS);
								}else{
									logger.info("Failed to create composer.");
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.CREATE_COMPOSER_FAIL);
								}
							 
						 }else{
							 logger.info("Failed to get pathlist for id " + composer.getMyDistDrvPathlist().getId());
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND); 
						 }
					 }else{
						 	logger.info(failureConstant + composer.getMyDistDrvPathlist().getId());
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND); 
					 }
			 }else{
					logger.info(failureLoggerMsg + composer.getComposerType().getAlias());
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
				}
			}else{
				logger.info(failureLoggerMsg + composer.getComposerType().getAlias());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
			}
				
		}else{
			logger.info("Found duplicate composer name ");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_COMPOSER_NAME);
		}
		return responseObject;
	}
	
	/**
	 * @param composer
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER, actionType = BaseConstants.UPDATE_ACTION, currentEntity = Composer.class, ignorePropList= "")
	public ResponseObject updateComposer(Composer composer) {
		logger.debug("Inside:updateComposer  update composer details.");

		ResponseObject responseObject = new ResponseObject();

		if (isUniqueComposerForUpdate(composer.getName(), composer.getId())) {

			if (composer.getMyDistDrvPathlist().getId() > 0) {
				PathList distributionDriverPathList = pathListDao.getDistributionPathListById(composer.getMyDistDrvPathlist().getId());

				if (distributionDriverPathList != null) {
					composer.setMyDistDrvPathlist((DistributionDriverPathList) distributionDriverPathList);
					PluginTypeMaster pluginMaster =   (PluginTypeMaster) MapCache.loadMasterEntityById(composer.getComposerType().getId(), SystemParametersConstant.DISTRIBUTION_PLUGIN_TYPE_LIST);
					if(pluginMaster != null ){
						 composer.setComposerType(pluginMaster);
						 if (composer.getComposerMapping() != null) {
								if (composer.getComposerMapping().getId() > 0) {
									ComposerMapping composerMapping = composerMappingDao.findByPrimaryKey(ComposerMapping.class,composer.getComposerMapping().getId());
									composer.setComposerMapping(composerMapping);
								} else {
									composer.setComposerMapping(null);
								}
							}
						    composerDao.evict(composer);
							composerDao.merge(composer);

							if (composer.getId() > 0) {
								logger.info("Composer details has been updated successfully.");
								responseObject.setSuccess(true);
								responseObject.setObject(setComposerJSONObject(composer));
								responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_SUCCESS);
							} else {
								logger.info("Failed to update composerdetails.");
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
							}
					}
					else{
						logger.info(failureLoggerMsg + composer.getComposerType().getAlias());
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.PLUGIN_TYPE_NOT_FOUND);
					}
				} else {
					logger.info(failureConstant
							+ composer.getMyDistDrvPathlist().getId());
					responseObject.setSuccess(false);
					responseObject
							.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
				}

			} else {
				logger.info(failureConstant + composer.getMyDistDrvPathlist().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
			}

		} else {
			logger.info("Found duplicate compser name while update plugin details.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_COMPOSER_NAME);
		}
		return responseObject;
	}

	/**
	 * Method will check unique name while creating new Composer.
	 * @param name
	 * @return
	 */
	@Transactional
	@Override
	public boolean isUniqueComposer(String name) {
		logger.debug("Checking duplicate composer name : "  + name);
		List<Composer> composerList = composerDao.getComposerByName(name);
		boolean isUnique ;
		if(composerList != null && !composerList.isEmpty()){
			logger.info("Found duplicate composer name.");
			isUnique=false;
		}else { // No composer found with same name 
			logger.info("Found unique composer name while create new composer.");
			isUnique=true;
		}
		return isUnique;
	}
	
	/**
	 * Method will check unique name while update Composer details.
	 * @param name
	 * @return
	 */
	@Transactional
	@Override
	public boolean isUniqueComposerForUpdate(String name, int composerId) {
		logger.debug("Checking duplicate composer name : "  + name);
		List<Composer> composerList = composerDao.getComposerByName(name);
		
		boolean isUnique =false;
		if(composerList != null && !composerList.isEmpty()){
				for(Composer composer:composerList){
					//If ID is same , then it is same Composer object
					if(composerId == composer.getId()){
						isUnique=true;
						logger.info("Found duplicate composer name.");
					}else{ // It is another Composer object , but name is same
						isUnique=false;
						logger.info("Found unique composer name.");
					}
				}
		}else { // No composer found with same name 
			isUnique = true;
			logger.info("Found unique composer name while update composer details.");
		}
		return isUnique;
	}

	
	/**
	 * Method will create new JSON object for composer details hierarchy. (Char rename operation parameters)
	 * @param composer
	 * @return
	 */
	@Transactional(readOnly = true)
	public JSONObject setComposerJSONObject(Composer composer){
		
		logger.debug("Setting JSON data for composer plugin");
		 JSONObject composerJsonObject = new JSONObject();
		 JSONObject charJsonObject ;
		 JSONArray charArray = new JSONArray();
		 
		 	composerJsonObject.put("id", composer.getId());
			composerJsonObject.put("name", composer.getName());
			composerJsonObject.put("pluginTypeId", composer.getComposerType().getId());
			composerJsonObject.put("pluginType", composer.getComposerType().getType());
			composerJsonObject.put("pluginTypeAlias", composer.getComposerType().getAlias());
			composerJsonObject.put("writeFilenamePrefix", composer.getWriteFilenamePrefix());
			composerJsonObject.put("writeFilenameSuffix", composer.getWriteFilenameSuffix());
			composerJsonObject.put("destPath", composer.getDestPath());
			composerJsonObject.put("fileExtension", composer.getFileExtension());
			composerJsonObject.put("defaultFileExtensionRemoveEnabled", composer.isDefaultFileExtensionRemoveEnabled());
			composerJsonObject.put("fileBackupPath", composer.getFileBackupPath());
			composerJsonObject.put("fileSplitEnabled", composer.isFileSplitEnabled());
			if(composer.getComposerMapping() != null){
				composerJsonObject.put("composerMappingId", composer.getComposerMapping().getId());	
			}else{
				composerJsonObject.put("composerMappingId", "0");
			}
			if(composer.getCharRenameOperationList() != null && !composer.getCharRenameOperationList().isEmpty()){
				for (CharRenameOperation charRename : composer.getCharRenameOperationList()) {
					charJsonObject = new JSONObject();
					
					charJsonObject.put("id", charRename.getId());
					charJsonObject.put("sequenceNo", charRename.getSequenceNo());
					charJsonObject.put("query", charRename.getQuery());
					charJsonObject.put("position", charRename.getPosition());
					charJsonObject.put("startIndex", charRename.getStartIndex());
					charJsonObject.put("endIndex", charRename.getEndIndex());
					charJsonObject.put("paddingType", charRename.getPaddingType());
					charJsonObject.put("defaultValue", charRename.getDefaultValue());
					charJsonObject.put("length", charRename.getLength());
					charArray.put(charJsonObject);
					
				}
				composerJsonObject.put("characterRename", charArray);
			}else{
				composerJsonObject.put("characterRename", "");
			}
		 
		 return composerJsonObject;
	 }

	
	/**
	 * Method will remove the composer and its dependents (Char rename operations.)
	 * @param composer
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_COMPOSER, actionType = BaseConstants.DELETE_ACTION, currentEntity = Composer.class, ignorePropList= "")
	public ResponseObject deleteComposerPluginAndItsDependents(int composerId,  int staffId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Delete: Composer plug-in details and its dependents.");
		Composer composer = composerDao.findByPrimaryKey(Composer.class, composerId);
		if(composer != null){
		
			List<CharRenameOperation> charRenameOperationParameterList = composer.getCharRenameOperationList();
			
				if(charRenameOperationParameterList != null && !charRenameOperationParameterList.isEmpty()){
					logger.debug("Found char rename operation params ");
					int charListCount = charRenameOperationParameterList.size();
					for(int j=0; j < charListCount; j++ ){
						CharRenameOperation charRenameOperation =  charRenameOperationDao.findByPrimaryKey(CharRenameOperation.class, charRenameOperationParameterList.get(j).getId()); 
						charRenameOperation.setStatus(StateEnum.DELETED);
						charRenameOperation.setLastUpdatedByStaffId(staffId);
						charRenameOperation.setLastUpdatedDate(new Date());
					}
				}
				
			composer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,composer.getName()));
			composer.setStatus(StateEnum.DELETED);
			PathList distributionDriverPathList = pathListDao.getDistributionPathListById(composer.getMyDistDrvPathlist().getId());
			distributionDriverPathList.getDriver().getService().setSyncStatus(false);
			distributionDriverPathList.getDriver().getService().getServerInstance().setSyncSIStatus(false);
			distributionDriverPathList.getDriver().getService().getServerInstance().setSyncChildStatus(false);
			composer.setComposerMapping(null);
			composer.setLastUpdatedByStaffId(staffId);
			composer.setLastUpdatedDate(new Date());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DELETE_COMPOSER_SUCCESS);
			logger.info("Composer plugin details has been deleted successfully.");
		}else{
			logger.info("Failed to delete composer plugin details.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DELETE_COMPOSER_FAIL);
		}
		
		return responseObject;
	}

	/**
	 * Method will delete multiple composer plug-in details with its dependents.
	 * @param pluginIds
	 * @return
	 */
	@Transactional
	@Override
	public ResponseObject deleteComposer(String pluginIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		String[] idArray = pluginIds.split(",");
		ComposerService composerServiceImpl = (ComposerService) SpringApplicationContext.getBean("composerService"); 
		for(int i = 0; i < idArray.length;i++){
			responseObject = composerServiceImpl.deleteComposerPluginAndItsDependents(Integer.parseInt(idArray[i]), staffId);
		}
		
		
		return responseObject;
		
	}
	
	/**
	 * Method will get composer and its associated mapping details by composer id.
	 * @param composerId
	 * @return Composer
	 */
	@Transactional(readOnly = true)
	@Override
	public Composer getComposerMappingDetailsByComposerId(int composerId) {
		
		if(composerId > 0){
			Composer composer = composerDao.findByPrimaryKey(Composer.class, composerId);
			if(composer != null && composer.getComposerMapping() != null){
				composer.getComposerMapping().getId();
				composer.getComposerMapping().getDevice().getId();
				composer.getComposerMapping().getDevice().getDeviceType().getId();
				composer.getComposerMapping().getDevice().getVendorType().getId();
				
				return composer;
			}else{
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Method will associate composer with device mapping. 
	 * @param composer
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject updateComposerMapping(Composer composer) {

		ResponseObject responseObject = new ResponseObject();
		
		if(composer != null ){
			
			composerDao.merge(composer);
			
			if(composer.getId() != 0){
				responseObject.setObject(composer);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.UPDATE_COMPOSER_FAIL);
		}
		
		return responseObject;
	}
	
	@Override
	public void importComposerForUpdateMode(Composer dbComposer, Composer exportedComposer) {
		//update general properties in composer
		logger.debug("going to update composer for import : "+dbComposer.getName());
		dbComposer.setWriteFilenamePrefix(exportedComposer.getWriteFilenamePrefix());
		dbComposer.setWriteFilenameSuffix(exportedComposer.getWriteFilenameSuffix());
		dbComposer.setFileExtension(exportedComposer.getFileExtension());
		dbComposer.setDestPath(exportedComposer.getDestPath());
		dbComposer.setFileBackupPath(exportedComposer.getFileBackupPath());
		dbComposer.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		dbComposer.setFileSplitEnabled(exportedComposer.isFileSplitEnabled());
		
		//update character rename operation in composer
		charRenameOperationService.importCharacterRenameOperationUpdateMode(dbComposer, exportedComposer);

		ComposerMapping exportedComposerMapping = exportedComposer.getComposerMapping();
		ComposerMapping dbComposerMapping = dbComposer.getComposerMapping();
		
		if(exportedComposerMapping != null && !exportedComposerMapping.getStatus().equals(StateEnum.DELETED)) {
			if(dbComposerMapping == null) {
				try {
					dbComposerMapping = (ComposerMapping) exportedComposerMapping.clone();
				} catch (CloneNotSupportedException e) {
					logger.error("Clone not supported", e);
				}
				dbComposer.setComposerMapping(dbComposerMapping);
				dbComposerMapping = dbComposer.getComposerMapping();
			}
			composerMappingService.importComposerMappingForUpdateMode(dbComposerMapping, exportedComposerMapping);
		}		
	}
	
	@Override
	public void importComposerForAddMode(Composer dbComposer, Composer exportedComposer) {
		//update general properties in composer
		logger.debug("going to update composer for import : "+dbComposer.getName());		
		//update character rename operation in composer
		charRenameOperationService.importCharacterRenameOperationAddMode(dbComposer, exportedComposer);

		ComposerMapping exportedComposerMapping = exportedComposer.getComposerMapping();
		ComposerMapping dbComposerMapping = dbComposer.getComposerMapping();
		
		if(exportedComposerMapping != null && !exportedComposerMapping.getStatus().equals(StateEnum.DELETED)) {
			if(dbComposerMapping == null) {
				try {
					dbComposerMapping = (ComposerMapping) exportedComposerMapping.clone();
				} catch (CloneNotSupportedException e) {
					logger.error("Clone not supported", e);
				}
				dbComposer.setComposerMapping(dbComposerMapping);
				dbComposerMapping = dbComposer.getComposerMapping();
			}
			composerMappingService.importComposerMappingForAddMode(dbComposerMapping, exportedComposerMapping);
		}		
	}
	
	@Override
	public Composer getComposerFromList(List<Composer> composerList, String composerAlias, String composerName) {
		if(!CollectionUtils.isEmpty(composerList)) {
			int length = composerList.size();
			for(int i = length-1; i >= 0; i--) {
				Composer composer = composerList.get(i);
				if(composer != null && !composer.getStatus().equals(StateEnum.DELETED)
						&& composer.getComposerType().getAlias().equalsIgnoreCase(composerAlias)
						&& composer.getName().equalsIgnoreCase(composerName)) {
					return composerList.remove(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Method will validate composer details and its associated dependents. 
	 * @param composer
	 * @return ResponseObject
	 */
	@Override
	public void validateComposerDetails(Composer composer, List<ImportValidationErrors> importErrorList) {
		if(composer != null && StateEnum.ACTIVE.equals(composer.getStatus())){
			composerValidator.validationComposerPluginParameters(composer, null, composer.getName(), true,importErrorList); // Method will validate plug-in parameters.
			composerMappingService.validateImportedMappingDetails(composer.getComposerMapping(), importErrorList); // method will validate composer mapping params.
			
		}	
	}
	
	/**
	 * Fetch Composer object by composer id
	 * 
	 * @param serviceId
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public Composer getComposerById(int composerId) {
		return composerDao.findByPrimaryKey(Composer.class, composerId);
	}
	
}


