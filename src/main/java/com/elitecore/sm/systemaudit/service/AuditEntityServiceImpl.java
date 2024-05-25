/**
 * 
 */
package com.elitecore.sm.systemaudit.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.systemaudit.dao.AuditEntityDao;
import com.elitecore.sm.systemaudit.model.AuditActivity;
import com.elitecore.sm.systemaudit.model.AuditEntity;
import com.elitecore.sm.systemaudit.model.AuditSubEntity;
import com.elitecore.sm.util.MapCache;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "auditEntityService")
public class AuditEntityServiceImpl implements	AuditEntityService{

	@Autowired
	private AuditEntityDao auditEntityDao;
	

	/**
	 * Method will get data from Map cache based on selected master entity.
	 * @param entityType
	 * @param entityId
	 * @return ResponseObject
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseObject getAuditEntity(String entityType, int entityId) {
		ResponseObject responseObject = new ResponseObject();
		
		if("ENTITY".equals(entityType)){
			responseObject.setObject(MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ENTITY_LIST));
			responseObject.setSuccess(true); 
		}else if("SUBENTITY".equals(entityType)){
			HashMap<Integer,List<AuditSubEntity>> subEntityList = (HashMap<Integer, List<AuditSubEntity>>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_SUB_ENTITY_LIST);
			responseObject.setObject(subEntityList.get(entityId));
			responseObject.setSuccess(true);
		}else{
			HashMap<Integer,List<AuditActivity>> activityList = (HashMap<Integer, List<AuditActivity>>) MapCache.getConfigValueAsObject(SystemParametersConstant.AUDIT_ACTIVITY_LIST);
			responseObject.setObject(activityList.get(entityId));
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will fetch all Audit master entity list.
	 * 
	 */
	@Transactional
	@Override
	public List<AuditEntity> getAllAuditEntity() {
		return auditEntityDao.getAllAuditEntity();
	}
}
