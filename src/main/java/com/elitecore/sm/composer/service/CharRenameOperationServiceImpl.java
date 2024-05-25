/**
 * 
 */
package com.elitecore.sm.composer.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.agent.dao.ServiceFileRenameConfigDao;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.CharacterRenameOperationDao;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.validator.ComposerValidator;
import com.elitecore.sm.drivers.dao.DriversDao;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value="charRenameOperationService")
public class CharRenameOperationServiceImpl implements CharRenameOperationService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ComposerDao composerDao;
	
	@Autowired
	ServiceFileRenameConfigDao serviceFileRenameConfigDao;
	
	@Autowired
	CharacterRenameOperationDao charRenameOperationDao;

	@Autowired
	ComposerValidator composerValidator;
	
	@Autowired
	PathListDao pathListDao;
	
	@Autowired
	DriversDao driverDao;
	
	private String failureMsgConstant = "Failed to get composer plugin for id ";
	
	/**
	 * Method will create new plug-in character rename operation parameters
	 * @param charRenameOperation
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_CHAR_RENAME, actionType = BaseConstants.CREATE_ACTION, currentEntity = CharRenameOperation.class ,ignorePropList= "")	
	public ResponseObject addCharRenameOperationParams(CharRenameOperation charRenameOperation) {
		logger.debug("Inside:addCharRenameOperationParams  adding new charcater rename operation params.");
	
		ResponseObject responseObject = new ResponseObject() ;
		
		if(charRenameOperation.getComposer() != null && charRenameOperation.getComposer().getId() > 0){
			Composer composer  = composerDao.findByPrimaryKey(Composer.class, charRenameOperation.getComposer().getId());
			
			if(composer != null){
				charRenameOperation.setComposer(composer);
				composerDao.merge(composer);
				charRenameOperationDao.save(charRenameOperation);
				
				if(charRenameOperation.getId() > 0){
					logger.info("Char rename operation parameters created successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_SUCCESS);
				}else{
					logger.info("Failed to crate char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_FAIL);
				}
			}else{
				logger.info("Failed to get composer plugin due to object found for plugin  id " + charRenameOperation.getComposer().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
			}
		}else if(charRenameOperation.getPathList() != null && charRenameOperation.getPathList().getId() > 0){
			PathList pathList  = pathListDao.findByPrimaryKey(PathList.class, charRenameOperation.getPathList().getId());
			
			if(pathList != null){
				charRenameOperation.setPathList(pathList);
				pathListDao.merge(pathList);
				charRenameOperationDao.save(charRenameOperation);
				
				if(charRenameOperation.getId() > 0){
					logger.info("Char rename operation parameters created successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_SUCCESS);
				}else{
					logger.info("Failed to crate char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_FAIL);
				}
			}else{
				logger.info("Failed to get pathlist details for id " + charRenameOperation.getPathList().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
			}
		}else{
			logger.info("Failed to crate char rename operation parameters.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will get all char rename operation parameters by id.
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getCharRenameParamsById(int pluginId) {
		ResponseObject responseObject = new ResponseObject();
		 JSONObject charJsonObject ;
		 JSONArray charArray = new JSONArray();
		 
		List<CharRenameOperation> charRenameParamList =  charRenameOperationDao.getAllRenameOperationsById(pluginId);
		
		if(charRenameParamList != null && !charRenameParamList.isEmpty()){
			logger.info(charRenameParamList.size() + " char rename param operation params found for plug in " + pluginId);
			
			for (CharRenameOperation charRename : charRenameParamList) {
				charJsonObject = new JSONObject();
				
				charJsonObject.put("id", charRename.getId());
				charJsonObject.put("sequenceNo", charRename.getSequenceNo());
				charJsonObject.put("query", charRename.getQuery());
				charJsonObject.put("position", charRename.getPosition());
				charJsonObject.put("startIndex", charRename.getStartIndex());
				charJsonObject.put("endIndex", charRename.getEndIndex());
				charJsonObject.put("paddingType", charRename.getPaddingType());
				charJsonObject.put("paddingValue", charRename.getPaddingValue());
				charJsonObject.put("defaultValue", charRename.getDefaultValue());
				charJsonObject.put("length", charRename.getLength());
				charJsonObject.put("isCacheEnable", charRename.isCacheEnable());
				charArray.put(charJsonObject);
				
			}
			
			responseObject.setObject(charArray);
			responseObject.setSuccess(true);
		}else{
			logger.info("Chare rename paramers are not found for plugin id :: " + pluginId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
		}
		return responseObject;
	}


	/**
	 * Method will get all char rename operation parameters by id.
	 * @param id
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllRenameOperationsBySvcFileRenConfigId(int serviceFileRenameConfigId){
		ResponseObject responseObject = new ResponseObject();
		 JSONObject charJsonObject ;
		 JSONArray charArray = new JSONArray();
		 
		List<CharRenameOperation> charRenameParamList =  charRenameOperationDao.getAllRenameOperationsBySvcFileRenConfigId(serviceFileRenameConfigId);
		
		if(charRenameParamList != null && !charRenameParamList.isEmpty()){
			logger.info(charRenameParamList.size() + " char rename param operation params found for serviceFileRenameConfigId" + serviceFileRenameConfigId);
			
			for (CharRenameOperation charRename : charRenameParamList) {
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
			
			responseObject.setObject(charArray);
			responseObject.setSuccess(true);
		}else{
			logger.info("Chare rename paramers are not found for serviceFileRenameConfig id :: " + serviceFileRenameConfigId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Method will update character rename operation parameters for selected plug-in.
	 * @param charRenameOperation
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CHAR_RENAME, actionType = BaseConstants.UPDATE_ACTION, currentEntity = CharRenameOperation.class ,ignorePropList= "composer")
	public ResponseObject updateCharRenameOperationParams(CharRenameOperation charRenameOperation) {
		ResponseObject responseObject = new ResponseObject();
		
		if(charRenameOperation.getComposer() != null && charRenameOperation.getComposer().getId() > 0){
			Composer composer  = composerDao.findByPrimaryKey(Composer.class, charRenameOperation.getComposer().getId());
			
			if(composer != null){
				charRenameOperation.setComposer(composer);
				if (charRenameOperation.getId() > 0) {
					composerDao.merge(composer);
					charRenameOperationDao.merge(charRenameOperation);
					logger.info("Char rename operation parameters updated successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject
							.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_UPDATE_SUCCESS);

				} else {
					logger.info("Failed to update char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject
							.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_UPDATE_FAIL);
				}
			}else{
				logger.info(failureMsgConstant + charRenameOperation.getComposer().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
			}
			
		}else if(charRenameOperation.getPathList() != null && charRenameOperation.getPathList().getId() > 0){
			PathList pathList  = pathListDao.findByPrimaryKey(PathList.class, charRenameOperation.getPathList().getId());
			
			if(pathList != null){
				charRenameOperation.setPathList(pathList);
				pathListDao.merge(pathList);
				charRenameOperationDao.merge(charRenameOperation);
				
				if(charRenameOperation.getId() > 0){
					logger.info("Char rename operation parameters created successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_SUCCESS);
				}else{
					logger.info("Failed to crate char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_FAIL);
				}
			}else{
				logger.info("Failed to get composer plugin due to object found for plugin  id " + charRenameOperation.getComposer().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
			}
		}else{
			logger.info("Failed to crate char rename operation parameters.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PATHLIST_NOT_FOUND);
		}
		
		return responseObject;
	}

	/**
	 * Method will delete char rename operation parameters 
	 * @param id
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CHAR_RENAME_TO_FILE_RENAME_AGENT, actionType = BaseConstants.DELETE_ACTION, currentEntity = CharRenameOperation.class ,ignorePropList= "")
	public ResponseObject deleteCharRenameOperationParams(int id, boolean isAgent) {
		ResponseObject responseObject = new ResponseObject();
		CharRenameOperation charRenameOperation  = charRenameOperationDao.findByPrimaryKey(CharRenameOperation.class, id);
		if(charRenameOperation != null ){
			charRenameOperation.setStatus(StateEnum.DELETED);
			charRenameOperation.setLastUpdatedDate(new Date());
			
			if(isAgent){
				charRenameOperationDao.mergeCharOperationAgent(charRenameOperation);
			}else{
				if(charRenameOperation.getPathList()!=null){
					Hibernate.initialize(charRenameOperation.getPathList());
					PathList pathlist = charRenameOperation.getPathList();
					Hibernate.initialize(pathlist.getDriver());
					Drivers drivers = pathlist.getDriver();
					Service service = drivers.getService();
					service.setSyncStatus(false);
					driverDao.merge(drivers);
				}
				charRenameOperationDao.merge(charRenameOperation);
				
			}
			
			logger.info("Char rename operation parameters deleted successfully.");
			responseObject.setSuccess(true);
			responseObject.setObject(charRenameOperation);
			responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_DELETE_SUCCESS);
		}else{
			logger.info("Failed to delete character rename operation details ");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_DELETE_FAIL);
		}
		
		return responseObject;
	}

	
	@Override
	public void validateCharRenameOperationParameters(CharRenameOperation charRenameOperation,	List<ImportValidationErrors> importErrorList) {
		if(charRenameOperation != null){
			logger.debug("Validating char rename operation parameters.");
			composerValidator.validateChareRenameParameters(charRenameOperation, null, charRenameOperation.getDefaultValue(), true, importErrorList, false);
		}else{
			logger.debug("charRenameOperation object found null.");
		}
		
	}
	
	/**
	 * Method will create new character rename operation parameters in file rename agent 
	 * @param charRenameOperation
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_CHAR_RENAME_TO_FILE_RENAME_AGENT, actionType = BaseConstants.CREATE_ACTION, currentEntity = CharRenameOperation.class ,ignorePropList= "")	
	public ResponseObject addCharRenameOperationToFileRenameAgent(CharRenameOperation charRenameOperation) {
		logger.debug("Inside:addCharRenameOperationParams  adding new charcater rename operation params.");
	
		ResponseObject responseObject = new ResponseObject() ;
		
		if(charRenameOperation.getSvcFileRenConfig().getId() > 0){
			
			ServiceFileRenameConfig serviceFileRenameConfig = serviceFileRenameConfigDao.findByPrimaryKey(ServiceFileRenameConfig.class, charRenameOperation.getSvcFileRenConfig().getId()); 
					
			if(serviceFileRenameConfig != null){
				charRenameOperation.setSvcFileRenConfig(serviceFileRenameConfig);
				
				charRenameOperationDao.saveCharRenameOperation(charRenameOperation);
				
				if(charRenameOperation.getId() > 0){
					logger.info("Char rename operation parameters created successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_SUCCESS);
				}else{
					logger.info("Failed to crate char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_ADD_FAIL);
				}
			}else{
				logger.info("Failed to get file rename agent service configuration id" + charRenameOperation.getSvcFileRenConfig().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_SERVICE_NOT_FOUND);
			}
		}else{
			logger.info("Failed to get file rename agent service configuration id" + charRenameOperation.getSvcFileRenConfig().getId());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_SERVICE_NOT_FOUND);
		}
		
		return responseObject;
	}
	
	/**
	 * Method will update character rename operation parameters for selected file renaming agent service.
	 * @param charRenameOperation
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CHAR_RENAME_TO_FILE_RENAME_AGENT, actionType = BaseConstants.UPDATE_ACTION, currentEntity = CharRenameOperation.class ,ignorePropList= "composer,svcFileRenConfig,query")	
	public ResponseObject updateCharRenameOperationParamsForFileRenameAgent(CharRenameOperation charRenameOperation) {
		ResponseObject responseObject = new ResponseObject();
		
		if(charRenameOperation.getSvcFileRenConfig().getId() > 0){
			
			ServiceFileRenameConfig serviceFileRenameConfig = serviceFileRenameConfigDao.findByPrimaryKey(ServiceFileRenameConfig.class, charRenameOperation.getSvcFileRenConfig().getId());
			
			if(serviceFileRenameConfig != null){
				charRenameOperation.setSvcFileRenConfig(serviceFileRenameConfig);
				if (charRenameOperation.getId() > 0) {

					charRenameOperationDao.mergeCharOperationAgent(charRenameOperation);
					logger.info("Char rename operation parameters updated successfully.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_UPDATE_SUCCESS);

				} else {
					logger.info("Failed to update char rename operation parameters.");
					responseObject.setSuccess(true);
					responseObject.setObject(charRenameOperation);
					responseObject.setResponseCode(ResponseCode.CHAR_RENAME_PARAM_UPDATE_FAIL);
				}
			}else{
				logger.info(failureMsgConstant + charRenameOperation.getSvcFileRenConfig().getId());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_SERVICE_NOT_FOUND);
			}
			
		}else{
			logger.info(failureMsgConstant + charRenameOperation.getSvcFileRenConfig().getId());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FILE_RENAME_AGENT_SERVICE_NOT_FOUND);
		}
		
		return responseObject;
	}
	
	@Override
	public List<CharRenameOperation> importCharacterRenameOperationUpdateMode(Composer dbComposer, Composer exportedComposer) {
		List<CharRenameOperation> dbCharRenameOperationList = dbComposer.getCharRenameOperationList();
		List<CharRenameOperation> exportedCharRenameOperationList = exportedComposer.getCharRenameOperationList();
		if(!CollectionUtils.isEmpty(exportedCharRenameOperationList)) {
			int length = exportedCharRenameOperationList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation exportedCharRenameOperation = exportedCharRenameOperationList.get(i);
				if(exportedCharRenameOperation != null && !exportedCharRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					CharRenameOperation dbCharRenameOperation = getCharRenameOperationFromList(dbCharRenameOperationList, exportedCharRenameOperation.getSequenceNo());
					if(dbCharRenameOperation != null) {
						logger.debug("going to update char rename operation for import : "+dbCharRenameOperation.getSequenceNo());
						updateCharRenameOperation(dbCharRenameOperation, exportedCharRenameOperation);
						dbCharRenameOperationList.add(dbCharRenameOperation);
					} else {
						logger.debug("going to add char rename operation for import : "+exportedCharRenameOperation.getSequenceNo());
						importCharRenameOperationAddAndKeepBothMode(exportedCharRenameOperation, dbComposer);
						dbCharRenameOperationList.add(exportedCharRenameOperation);
					}
				}
			}
		}
		return dbCharRenameOperationList;
	}
	
	@Override
	public List<CharRenameOperation> importCharacterRenameOperationAddMode(Composer dbComposer, Composer exportedComposer) {
		List<CharRenameOperation> dbCharRenameOperationList = dbComposer.getCharRenameOperationList();
		List<CharRenameOperation> exportedCharRenameOperationList = exportedComposer.getCharRenameOperationList();
		if(!CollectionUtils.isEmpty(exportedCharRenameOperationList)) {
			int length = exportedCharRenameOperationList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation exportedCharRenameOperation = exportedCharRenameOperationList.get(i);
				if(exportedCharRenameOperation != null && !exportedCharRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					CharRenameOperation dbCharRenameOperation = getCharRenameOperationFromList(dbCharRenameOperationList, exportedCharRenameOperation.getSequenceNo());
					if(dbCharRenameOperation == null) {						
						logger.debug("going to add char rename operation for import : "+exportedCharRenameOperation.getSequenceNo());
						importCharRenameOperationAddAndKeepBothMode(exportedCharRenameOperation, dbComposer);
						dbCharRenameOperationList.add(exportedCharRenameOperation);
					}
				}
			}
		}
		return dbCharRenameOperationList;
	}
	
	public CharRenameOperation getCharRenameOperationFromList(List<CharRenameOperation> charRenameOperationList, int sequenceNo) {
		if(!CollectionUtils.isEmpty(charRenameOperationList)) {
			int length = charRenameOperationList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation charRenameOperation = charRenameOperationList.get(i);
				if(charRenameOperation != null && !charRenameOperation.getStatus().equals(StateEnum.DELETED)
						&& charRenameOperation.getSequenceNo() == sequenceNo) {
					return charRenameOperationList.remove(i);
				}
			}
		}
		return null;
	}
	
	public void importCharRenameOperationAddAndKeepBothMode(CharRenameOperation exportedCharRenameOperation, Object dbData) {
		
		Composer dbComposer = null;
		PathList pathList = null;
		if(dbData instanceof Composer){
			dbComposer = (Composer)dbData;
		}else{
			pathList = (PathList) dbData;
		}
		int maxSequenceNo = 0;
			exportedCharRenameOperation.setId(0);
			exportedCharRenameOperation.setCreatedDate(EliteUtils.getDateForImport(false));
			exportedCharRenameOperation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		if(dbComposer  != null){
			exportedCharRenameOperation.setCreatedByStaffId(dbComposer.getCreatedByStaffId());
			exportedCharRenameOperation.setLastUpdatedByStaffId(dbComposer.getCreatedByStaffId());
			exportedCharRenameOperation.setComposer(dbComposer);
			maxSequenceNo = charRenameOperationDao.getMaxSequenceNoForPlugin(dbComposer.getId());
			exportedCharRenameOperation.setSequenceNo(maxSequenceNo+1);
		} else {
			exportedCharRenameOperation.setCreatedByStaffId(pathList.getCreatedByStaffId());
			exportedCharRenameOperation.setLastUpdatedByStaffId(pathList.getCreatedByStaffId());
			exportedCharRenameOperation.setPathList(pathList);
			maxSequenceNo = charRenameOperationDao.getMaxSequenceNoForCollectionPathListId(pathList.getId());
			exportedCharRenameOperation.setSequenceNo(maxSequenceNo+1);
		}
	}
	
	public void updateCharRenameOperation(CharRenameOperation dbCharRenameOperation, CharRenameOperation exportedCharRenameOperation) {
		dbCharRenameOperation.setQuery(exportedCharRenameOperation.getQuery());
		dbCharRenameOperation.setCacheEnable(exportedCharRenameOperation.isCacheEnable());
		dbCharRenameOperation.setPosition(exportedCharRenameOperation.getPosition());
		dbCharRenameOperation.setStartIndex(exportedCharRenameOperation.getStartIndex());
		dbCharRenameOperation.setEndIndex(exportedCharRenameOperation.getEndIndex());
		dbCharRenameOperation.setPaddingType(exportedCharRenameOperation.getPaddingType());
		dbCharRenameOperation.setDefaultValue(exportedCharRenameOperation.getDefaultValue());
		dbCharRenameOperation.setLength(exportedCharRenameOperation.getLength());
		dbCharRenameOperation.setPaddingValue(exportedCharRenameOperation.getPaddingValue());
		dbCharRenameOperation.setDateFormat(exportedCharRenameOperation.getDateFormat());
		dbCharRenameOperation.setSrcDateFormat(exportedCharRenameOperation.getSrcDateFormat());
		dbCharRenameOperation.setDateType(exportedCharRenameOperation.getDateType());
		dbCharRenameOperation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	@Override
	public ResponseObject importOrDeleteComposerCharRenameOperation(Composer composer, boolean isImport){
		ResponseObject responseObject = new ResponseObject();
		Date date = new Date();
		List<CharRenameOperation> charRenameOperationList = composer.getCharRenameOperationList();
		if(charRenameOperationList != null && !charRenameOperationList.isEmpty()){
			CharRenameOperation charRenameOperation ;
			for (int i = 0, size = charRenameOperationList.size(); i < size; i++) {
				charRenameOperation = charRenameOperationList.get(i);
				
				if (charRenameOperation != null && !charRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					if (isImport) {
						logger.debug("Going to import or create new char rename parameters for the plugin."+charRenameOperation.getSequenceNo());
						charRenameOperation.setId(0);
						charRenameOperation.setComposer(composer);
						responseObject.setSuccess(true);
					}else{
						logger.debug("Going to delete char rename operation parameters. ");
						charRenameOperation.setStatus(StateEnum.DELETED);
						//charRenameOperation.setComposer(composer);
						charRenameOperation.setLastUpdatedDate(date);
					}
				}else{
					logger.debug("charRenameOperation status found inactive or deleted.");
					responseObject.setSuccess(true);
				}
			}
			composer.setCharRenameOperationList(charRenameOperationList); // Setting all char rename parameters in composer object.
		}else{
			logger.debug("charRenameOperation list not configured for distribution driver pathlist plugins.");
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public long getSeqNumberCountByPluginId(int sequenceNumber,int pluginId, int id) {
		return charRenameOperationDao.getSeqNumberCountByPluginId(sequenceNumber, pluginId, id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public long getSeqNumberCountByIdForFileRenameAgent(int sequenceNumber,int svcFileRenConfigId, int id) {
		return charRenameOperationDao.getSeqNumberCountByIdForFileRenameAgent(sequenceNumber, svcFileRenConfigId, id);
	}

	@Transactional
	@Override
	public long getSeqNumberCountByCollectionServicePathListId(int sequenceNumber, int pathListId, int id) {
		return charRenameOperationDao.getSeqNumberCountByCollectionServicePathListId(sequenceNumber, pathListId, id);
	}

	@Transactional
	@Override
	public ResponseObject getCollectionCharRenameParamsById(int pathListId) {
		ResponseObject responseObject = new ResponseObject();
		 JSONObject charJsonObject ;
		 JSONArray charArray = new JSONArray();
		 
		List<CharRenameOperation> charRenameParamList =  charRenameOperationDao.getAllRenameOperationsByPathListId(pathListId);
		
		if(charRenameParamList != null && !charRenameParamList.isEmpty()){
			logger.info(charRenameParamList.size() + " char rename param operation params found for plug in " + pathListId);
			
			for (CharRenameOperation charRename : charRenameParamList) {
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
				charJsonObject.put("paddingValue", charRename.getPaddingValue());
				charJsonObject.put("dateType", charRename.getDateType());
				charJsonObject.put("dateFormat", charRename.getDateFormat());
				charJsonObject.put("srcDateFormat", charRename.getSrcDateFormat());
				charArray.put(charJsonObject);
				
			}
			
			responseObject.setObject(charArray);
			responseObject.setSuccess(true);
		}else{
			logger.info("Chare rename paramers are not found for plugin id :: " + pathListId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PLUGIN_NOT_FOUND);
		}
		return responseObject;
	}

	@Override
	public ResponseObject importOrDeleteCharRenameOperation(PathList pathList, boolean isImport) {

		ResponseObject responseObject = new ResponseObject();
		Date date = new Date();
		List<CharRenameOperation> charRenameOperationList = pathList.getCharRenameOperationList();
		if(charRenameOperationList != null && !charRenameOperationList.isEmpty()){
			CharRenameOperation charRenameOperation ;
			for (int i = 0, size = charRenameOperationList.size(); i < size; i++) {
				charRenameOperation = charRenameOperationList.get(i);
				
				if (charRenameOperation != null && !charRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					if (isImport) {
						logger.debug("Going to import or create new char rename parameters for the plugin."+charRenameOperation.getSequenceNo());
						charRenameOperation.setId(0);
						charRenameOperation.setPathList(pathList);
						responseObject.setSuccess(true);
					}else{
						logger.debug("Going to delete char rename operation parameters. ");
						charRenameOperation.setStatus(StateEnum.DELETED);
						//charRenameOperation.setComposer(composer);
						charRenameOperation.setLastUpdatedDate(date);
					}
				}else{
					logger.debug("charRenameOperation status found inactive or deleted.");
					responseObject.setSuccess(true);
				}
			}
			pathList.setCharRenameOperationList(charRenameOperationList); // Setting all char rename parameters in composer object.
		}else{
			logger.debug("charRenameOperation list not configured for distribution driver pathlist plugins.");
			responseObject.setSuccess(true);
		}
		return responseObject;
	
	}

	@Override
	public List<CharRenameOperation> importCharacterRenameOperationUpdateMode(PathList dbPath, PathList exportedPath) {

		List<CharRenameOperation> dbCharRenameOperationList = dbPath.getCharRenameOperationList();
		List<CharRenameOperation> exportedCharRenameOperationList = exportedPath.getCharRenameOperationList();
		if(!CollectionUtils.isEmpty(exportedCharRenameOperationList)) {
			int length = exportedCharRenameOperationList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation exportedCharRenameOperation = exportedCharRenameOperationList.get(i);
				if(exportedCharRenameOperation != null && !exportedCharRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					CharRenameOperation dbCharRenameOperation = getCharRenameOperationFromList(dbCharRenameOperationList, exportedCharRenameOperation.getSequenceNo());
					if(dbCharRenameOperation != null) {
						logger.debug("going to update char rename operation for import : "+dbCharRenameOperation.getSequenceNo());
						updateCharRenameOperation(dbCharRenameOperation, exportedCharRenameOperation);
						dbCharRenameOperationList.add(dbCharRenameOperation);
					} else {
						logger.debug("going to add char rename operation for import : "+exportedCharRenameOperation.getSequenceNo());
						importCharRenameOperationAddAndKeepBothMode(exportedCharRenameOperation, dbPath);
						dbCharRenameOperationList.add(exportedCharRenameOperation);
					}
				}
			}
		}
		return dbCharRenameOperationList;
	}
	
	@Override
	public List<CharRenameOperation> importCharacterRenameOperationAddMode(PathList dbPath, PathList exportedPath) {

		List<CharRenameOperation> dbCharRenameOperationList = dbPath.getCharRenameOperationList();
		List<CharRenameOperation> exportedCharRenameOperationList = exportedPath.getCharRenameOperationList();
		if(!CollectionUtils.isEmpty(exportedCharRenameOperationList)) {
			int length = exportedCharRenameOperationList.size();
			for(int i = length-1; i >= 0; i--) {
				CharRenameOperation exportedCharRenameOperation = exportedCharRenameOperationList.get(i);
				if(exportedCharRenameOperation != null && !exportedCharRenameOperation.getStatus().equals(StateEnum.DELETED)) {
					CharRenameOperation dbCharRenameOperation = getCharRenameOperationFromList(dbCharRenameOperationList, exportedCharRenameOperation.getSequenceNo());
					if(dbCharRenameOperation == null) {						
						logger.debug("going to add char rename operation for import : "+exportedCharRenameOperation.getSequenceNo());
						importCharRenameOperationAddAndKeepBothMode(exportedCharRenameOperation, dbPath);
						dbCharRenameOperationList.add(exportedCharRenameOperation);
					}
				}
			}
		}
		return dbCharRenameOperationList;
	
	}

}
