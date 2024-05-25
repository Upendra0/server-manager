/**
 * 
 */
package com.elitecore.sm.common.aspect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditUserDetails;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;
import com.elitecore.sm.systemaudit.service.AuditDetailsService;
import com.elitecore.sm.systemaudit.service.SystemAuditService;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterGroupData;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.JaversUtil;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 *
 */
@Aspect
public class AuditingAspect extends BaseAspect implements Ordered {

	@Autowired
	@Qualifier(value = "systemAuditService")
	SystemAuditService systemAuditService;

	@Autowired
	@Qualifier(value = "eliteUtilsQualifier")
	protected EliteUtils eliteUtils;

	@Autowired
	@Qualifier(value = "auditDetailsService")
	AuditDetailsService auditDetailsService;

	@Autowired
	ServicesService servicesService;

	@Autowired
	ServerInstanceService serverInstanceService;

	@Autowired
	DriversService driversService;

	String remarkMessage = null;
	
	HashMap<String, AuditActivity> auditActivityMap = null;
	
	String auditActivity = null;
	
	Object oldEntity = null;
	
	/**
	 * Method will audit current action details in.
	 * @param proceedingJoinPoint
	 * @param auditable
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(auditable)")
	@Transactional
	public Object aroundAudit(ProceedingJoinPoint proceedingJoinPoint, Auditable auditable) {
		Object value = null;
		String name = null;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		List<SystemAuditDetails> auditDetailsList = null;
		int currentEntityId = 0;

		ResponseObject responseObject;
		try {
			auditActivityMap = (HashMap<String, AuditActivity>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ACTIVITY_ALIAS_LIST);
			Object[] args = proceedingJoinPoint.getArgs(); // It will get all argument from the called method.
			auditActivity = auditable.auditActivity();

			switch (auditable.actionType()) {
				case BaseConstants.CREATE_ACTION:
					name = checkAndGetEntityName(args);
					break;
				case BaseConstants.UPDATE_ACTION:
					name = checkAndGetEntityName(args);
					auditDetailsList = getModifiedPropList(args,auditable.ignorePropList(),false);
					break;
				case BaseConstants.UPDATE_CUSTOM_ACTION:
					currentEntityId = (Integer) args[0];
					Map<String, Object> objectMap = getEntityNameFromObjectClone(currentEntityId, auditable.currentEntity());
					name = objectMap.get(BaseConstants.OBJECT_NAME).toString();
					oldEntity = objectMap.get(BaseConstants.OLD_OBJECT);
					break;
				case BaseConstants.DELETE_ACTION:
					currentEntityId = Integer.parseInt(args[0].toString());
					Map<String, Object> deleteObjectMap = getEntityNameFromObjectClone(currentEntityId, auditable.currentEntity());
					name = deleteObjectMap.get(BaseConstants.OBJECT_NAME).toString();
					oldEntity = deleteObjectMap.get(BaseConstants.OLD_OBJECT);
					break;
				case BaseConstants.DELETE_CUSTOM_ACTION:
					name = checkAndGetEntityName(args);
					break;
				case BaseConstants.SM_UPDATE_ACTION_BULK_LIST:
					name = checkAndGetEntityName(args);
					auditDetailsList = getModifiedPropList(args,auditable.ignorePropList(),false);
					break;
				case BaseConstants.UPDATE_LIST_ACTION:
					name = checkAndGetEntityName(args);
					auditDetailsList = getModifiedPropList(args,auditable.ignorePropList(),true);
					break;	
				case BaseConstants.DELETE_MULTIPLE_ACTION:
					name = getEntityNameForStringInput(args, auditable.currentEntity());
					break;
				default:
					logger.info("No Audit activity match for current audit activity. "
							+ auditable.actionType());
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		try {
			value = proceedingJoinPoint.proceed(); // Method will return ResponseObject from Service Layer
			if (BaseConstants.SM_ACTION_BULK_MAP.equalsIgnoreCase(auditable.actionType())) {
				responseObject = new ResponseObject();
				responseObject.setSuccess(true);
			} else {
				responseObject = (ResponseObject) value;
			}
			
		} catch (ClassCastException classCastException) {
			logger.error(classCastException.getMessage(), classCastException);
			responseObject = new ResponseObject();
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AJAX_GENERIC_EXCEPTION);

			logger.info("Failed to audit activity  " + auditActivity + " due to  "
					+ classCastException.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseObject = new ResponseObject();
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AJAX_GENERIC_EXCEPTION);

			logger.info("Failed to audit activity  " + auditActivity + " due to  "
					+ e.getMessage());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);

			responseObject = new ResponseObject();
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AJAX_GENERIC_EXCEPTION);
			value = responseObject;
		}

		//Code for audit process after method execution.
		try {
			if (responseObject.isSuccess()) {

				switch (auditable.actionType()) {
					case BaseConstants.SM_ACTION:
						if(responseObject.getObject() != null){
							name = getNameFromEntity(responseObject.getObject());
						}
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.DELETE_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.DELETE_CUSTOM_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.EXPORT_ACTION:
						getExportObjectNameAndAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, responseObject);
						break;
					case BaseConstants.UPDATE_CUSTOM_ACTION:
						getPropListForCustomUpdate(oldEntity, currentEntityId, auditable.auditActivity(), BaseConstants.UPDATE_ACTION, request, responseObject, name, auditable.ignorePropList());
						break;
					case BaseConstants.CREATE_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), BaseConstants.CREATE_ACTION, request, name, responseObject, false);
						break;
					case BaseConstants.UPDATE_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), BaseConstants.UPDATE_ACTION, request, name, responseObject, false);
						break;
					case BaseConstants.SM_UPDATE_ACTION_BULK_LIST:
						auditSystemParametersDetails(auditDetailsList, auditable.auditActivity(), BaseConstants.UPDATE_ACTION, request, name, responseObject, false);
						break;
					case BaseConstants.SM_ACTION_BULK_MAP: // Its for synchronization action operation.
						getAuditDetailsForSMBulk(value, auditable.auditActivity(), BaseConstants.SM_ACTION, request, responseObject);
						break;
					case BaseConstants.UPDATE_LIST_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), BaseConstants.UPDATE_ACTION, request, name, responseObject, false);
						break;
					case BaseConstants.DELETE_MULTIPLE_ACTION:
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.IMPORT_ACTION:
						if(responseObject.getObject() != null){
							name = getNameFromEntity(responseObject.getObject());
						}
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.FILE_REPROCESS:
						if(responseObject != null){
							Map<String, String> entityNameMap = new HashMap<>();
							entityNameMap.put(BaseConstants.ENTITY_NAME, responseObject.getModuleName());
							name = getNameFromEntity(entityNameMap);
						}
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					case BaseConstants.LICENSE_ACTION:
						initializeRemarkMessage();
						setAuditDetails(auditDetailsList, auditable.auditActivity(), auditable.actionType(), request, name, responseObject, false);
						break;
					default:
						logger.info("Default switch case condition match"); // It will never match this condition. Added this code due to sonar warning.
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return value;
	}
	
	/**
	 * Method will set and add audit details to data base.
	 * @param auditDetailsList
	 * @param auditActivity
	 * @param auditAction
	 * @param request
	 * @param entityName
	 */
	private void setAuditDetails(List<SystemAuditDetails> auditDetailsList, String auditActivity, String auditAction, HttpServletRequest request, String entityName, ResponseObject responseObject, boolean isGetName){
		String name ;
		boolean isProcess = isGetName;
		if(AuditConstants.CREATE_PATHLIST.equalsIgnoreCase(auditActivity)){
			isProcess = true;
		}
		
		if(responseObject.getObject() != null && isProcess){
			name = getNameFromEntity(responseObject.getObject());
		}else{
			name = entityName;
		}
		AuditUserDetails auditUserDetails = new AuditUserDetails(request.getRemoteAddr(), eliteUtils.getLoggedInStaffId(request), eliteUtils.getUserNameOfUser(request), name);
		logAuditActivity(auditUserDetails, auditDetailsList, auditActivity, auditAction);
	}
	
	
	/**
	 * Method will get entity name from the return response object and add audit details to database.
	 * @param auditDetailsList
	 * @param auditActivity
	 * @param auditAction
	 * @param request
	 * @param responseObject
	 */
	@SuppressWarnings("unchecked")
	private void getExportObjectNameAndAuditDetails(List<SystemAuditDetails> auditDetailsList, String auditActivity, String auditAction, HttpServletRequest request, ResponseObject responseObject){
		if (responseObject.getObject() != null ) {
			Map<String, Object> mapObject = (Map<String, Object>) responseObject.getObject();
			String name = getNameFromEntity(mapObject.get(BaseConstants.SERVICE_JAXB_OBJECT));
			setAuditDetails(auditDetailsList, auditActivity, auditAction, request, name, responseObject, false);
		}
	}
	
	
	/**
	 * Method get all properties list.
	 * @param oldEntity
	 * @param currentEntityId
	 * @param auditActivity
	 * @param auditAction
	 * @param request
	 * @param responseObject
	 * @param entityName
	 * @param ignorePropList
	 */
	private void getPropListForCustomUpdate(Object oldEntity, int currentEntityId, String auditActivity, String auditAction, HttpServletRequest request, ResponseObject responseObject, String entityName, String ignorePropList){
		Object newEntity = null;
		
		String name = oldEntity.getClass().getName();
		if((name.equals(IPLogParsingService.class.getName()) || name.equals(ParsingService.class.getName())) && auditActivity.equals(AuditConstants.UPDATE_SERVICE_STATUS)){//NOSONAR
			ignorePropList = "fileGroupingParameter";
		}
		if (oldEntity != null) {
			newEntity = systemAuditService.getOldEntity(oldEntity.getClass(), currentEntityId);
		}
		List<SystemAuditDetails> auditDetailsList = getModifiedPropList(newEntity, oldEntity,ignorePropList); // Getting modified properties list.
		setAuditDetails(auditDetailsList, auditActivity, auditAction, request, entityName, responseObject, false);
	}
	
	private void  auditSystemParametersDetails(List<SystemAuditDetails> auditDetailsList, String auditActivity, String auditAction, HttpServletRequest request, String entityName, ResponseObject responseObject, boolean isGetName){
		String name ;
		
		if(responseObject.getObject() != null && isGetName){
			name = getNameFromEntity(responseObject.getObject());
		}else{
			name = entityName;
		}
		if(auditDetailsList != null){
			AuditUserDetails auditUserDetails = new AuditUserDetails(request.getRemoteAddr(), eliteUtils.getLoggedInStaffId(request), eliteUtils.getUserNameOfUser(request), name);
			
			if (responseObject != null) {
				Object object = responseObject.getObject();
				SystemParameterData sys = (SystemParameterData) object;
				SystemParameterGroupData parameterGroup = sys.getParameterGroup();
				String nametest = parameterGroup.getName();

				if (nametest != null) {
					if (SystemParametersConstant.CUSTOMER_PARAMETERS.equalsIgnoreCase(nametest)) {
						auditActivity = AuditConstants.UPDATE_CUSTOMER_DETAILS;
					} else if (SystemParametersConstant.PASSWORD_PARAMETERS.equalsIgnoreCase(nametest)) {
						auditActivity = AuditConstants.UPDATE_PASSWORD_POLICY;
					} else if (SystemParametersConstant.FILE_REPROCESSING_PARAMETERS.equalsIgnoreCase(nametest)) {
						auditActivity = AuditConstants.UPDATE_FILE_REPROCESSING;
					}
					
				}
			}
			logAuditActivity(auditUserDetails, auditDetailsList, auditActivity, auditAction);
		}
		
	}
	
	/**
	 * Method will audit all details for sync actions.
	 * @param value
	 * @param auditActivity
	 * @param auditAction
	 * @param request
	 * @param responseObject
	 */
	@SuppressWarnings("unchecked")
	private void getAuditDetailsForSMBulk(Object value, String auditActivity, String auditAction, HttpServletRequest request, ResponseObject responseObject){
		Map<String, ResponseObject> responseMap = (Map<String, ResponseObject>) value;
		String name = null;
		for (Map.Entry<String, ResponseObject> responseObj : responseMap.entrySet()) {
			if (responseObj.getValue().isSuccess()) {
				if (responseObj.getValue().getObject() != null) {
					name = getNameFromEntity(responseObj.getValue().getObject());
				}
				setAuditDetails(null, auditActivity, auditAction, request, name, responseObject, false);
			}
		}
	}

	/**
	 * Method will get Entity name from the entity.
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNameFromEntity(Object entity) {
		ResponseObject responseObject = systemAuditService.checkEntityAndGetName(entity);
		Map<String, String> entityNameMap = (Map<String, String>) responseObject.getObject();
		if(entityNameMap != null){
			return getNameFromEntity(entityNameMap);
		}else{
			logger.info("Failed to get Entity name for object type " + entity.getClass());
			return null;
		}
	}
	
	public String getNameFromEntity(Map<String, String> entityNameMap) {
		AuditActivity auditActivityObj = auditActivityMap.get(this.auditActivity);
		if(auditActivityObj != null){
			remarkMessage = systemAuditService.setRemarks(entityNameMap, auditActivityObj.getMessage());
		}else{
			remarkMessage = "";
		}
		return entityNameMap.get(BaseConstants.ENTITY_NAME);
	}
	
	public void initializeRemarkMessage() {
		AuditActivity auditActivityObj = auditActivityMap.get(this.auditActivity);
		if(auditActivityObj != null){
			remarkMessage = auditActivityObj.getMessage();
		} else {
			remarkMessage = "";
		}
	}

	/**
	 * Method will get all property change by its value using JAVERS utility.
	 * @param newEntity
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public List<SystemAuditDetails> getModifiedPropList(Object newEntity, String ignorePropList,boolean isMapAction) throws IllegalAccessException {
		return getAllAuditPropsDiff(newEntity, ignorePropList, isMapAction);
	}
	
	/**
	 * Method will compare and get audit list for modified properties list.
	 * @param newEntity
	 * @param ignorePropList
	 * @param isMapAction
	 * @return
	 * @throws IllegalAccessException
	 */
	public List<SystemAuditDetails>  getAllAuditPropsDiff(Object newEntity, String ignorePropList, boolean isMapAction) throws IllegalAccessException{
		List<SystemAuditDetails> auditDetailsList = null;
		Field field = ReflectionUtils.findField(newEntity.getClass(), "id");
		List<ValueChange> modifiedPropList;
		if (field != null) {
			field.setAccessible(true);
			Object value = field.get(newEntity);
			if (value != null) {
				Object oldObject = systemAuditService.getOldEntity(newEntity.getClass(), Integer.parseInt(value.toString()));
				
				if(isMapAction){
					Map<String, List<?>>  propertiesList  = JaversUtil.getAllModifiedProp(oldObject, newEntity,ignorePropList);
					if(propertiesList != null && !propertiesList.isEmpty()){
						auditDetailsList = auditDetailsService.getAllPropties(propertiesList);
						return auditDetailsList;
					}
				}else{
					modifiedPropList = JaversUtil.compareObj(oldObject, newEntity,ignorePropList);
					if (modifiedPropList != null && !modifiedPropList.isEmpty()) {
						auditDetailsList = auditDetailsService.getAllModifiedProps(modifiedPropList);
						return auditDetailsList;
					}
				}
			}
		}
		return auditDetailsList;
	}
	
	/**
	 * Method will get all property change by its value using JAVERS utility.
	 * @param newEntity
	 * @param oldEntity
	 * @return List<SystemAuditDetails>
	 */
	public List<SystemAuditDetails> getModifiedPropList(Object newEntity, Object oldEntity, String ignorePropList) {
		List<ValueChange> modifiedPropList;
		List<SystemAuditDetails> auditDetailsList = null;
		modifiedPropList = JaversUtil.compareObj(oldEntity, newEntity,ignorePropList);
		if (modifiedPropList != null && !modifiedPropList.isEmpty()) {
			auditDetailsList = auditDetailsService.getAllModifiedProps(modifiedPropList);
			return auditDetailsList;
		}
		return auditDetailsList;
	}
	
	/**
	 * Method will get entity name from the method parameters.
	 * @param args
	 * @return
	 */
	private String checkAndGetEntityName(Object[] args) {
		String name = null;
		for (Object obj : args) {
			if (!obj.getClass().isPrimitive() && !(obj instanceof String) && !(obj instanceof Integer) ) {
				name = getNameFromEntity(obj);
				if (name != null) {
					break;
				}
			}
		}
		return name;
	}
	
	/**
	 * Method will be used to handle single/multiple entity delete operation.
	 * @param args
	 * @return
	 */
	private String getEntityNameForStringInput(Object[] args, Class<?> currentEntity) throws CloneNotSupportedException{
		StringBuilder builder = new StringBuilder("");
		for (Object obj : args) {
			if(obj instanceof String){
				String idString = (String)obj;
				String[] strIDArray = idString.split(",");
				for(String id : strIDArray){
					int numericId = Integer.parseInt(id);
					Map<String, Object> objectMap = getEntityNameFromObjectClone(numericId, currentEntity);
					if(builder.length() > 0){
						builder.append(",");
					}
					builder.append(objectMap.get(BaseConstants.OBJECT_NAME).toString());
				}
			}
		}
	
		//Logic to handle Remark message. It's work around for now. Need Proper handling which will require many methods and API change.
		Map<String, String> entityNameMap = new HashMap<>();
		entityNameMap.put(BaseConstants.ENTITY_NAME, builder.toString());  //For Multiple Entity Currently Added Custom Entity Name. We can add other Parameters like Service/Instance name based on requirements.
		AuditActivity auditActivityObj = auditActivityMap.get(this.auditActivity);
		if(auditActivityObj != null){
			remarkMessage = systemAuditService.setRemarks(entityNameMap, auditActivityObj.getMessage());
		}else{
			remarkMessage = "";
		}
		
		return builder.toString();
	}
	
	/**
	 * Method will get all modified properties list from the object.
	 * @return
	 */
	private List<SystemAuditDetails> getModifiedPropList(Object[] args, String ignorePropList,boolean isMapAction) {
		List<SystemAuditDetails> detailPropList = null;
		for (Object obj : args) {
			if (!obj.getClass().isPrimitive() && !(obj instanceof String)) {
				try {
					detailPropList = getModifiedPropList(obj, ignorePropList,isMapAction);
					if (detailPropList != null) {
						break;
					}
				} catch (IllegalAccessException e) {
					logger.error("Failed to get modified properties list due to  " + e);
				}
			}
		}
		return detailPropList;
	}
	
	/**
	 * Method will getEntity name for action where we need to update only single field or delete action.
	 * @param args
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	private Map<String, Object> getEntityNameFromObjectClone(int currentEntityId, Class<?> auditableCurrentEntity) throws CloneNotSupportedException{
		Map<String, Object> objectMap = new HashMap<>();
		if (currentEntityId > 0) {
			BaseModel tempOldEntity = systemAuditService.getOldEntity(auditableCurrentEntity, currentEntityId);
			Object tempObject  = tempOldEntity.clone();
			objectMap.put("oldObject", tempObject);
			String name = getNameFromEntity(tempObject);
			objectMap.put("objectName", name);
		}
		return objectMap;
	}
	
	/**
	 * Method will add Audit Entry in audit tables (system audit and system
	 * audit details)
	 * @param auditUserDetails
	 * @param detailsPropsList
	 * @param auditActivity
	 */
	public void logAuditActivity(AuditUserDetails auditUserDetails, List<SystemAuditDetails> detailsPropsList, String auditActivity, String actionType) {
		systemAuditService.addAuditDetails(auditUserDetails, auditActivityMap.get(auditActivity), detailsPropsList, actionType, remarkMessage);
	}
	
	@Override
	public int getOrder() {
		return 1; // assign order as 1 for this aspect.
	}
}