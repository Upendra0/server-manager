package com.elitecore.sm.aggregationservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.aggregationservice.dao.IAggregationAttributeDao;
import com.elitecore.sm.aggregationservice.dao.IAggregationConditionDao;
import com.elitecore.sm.aggregationservice.dao.IAggregationDefinitionDao;
import com.elitecore.sm.aggregationservice.dao.IAggregationKeyAttributeDao;
import com.elitecore.sm.aggregationservice.model.AggregationAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationCondition;
import com.elitecore.sm.aggregationservice.model.AggregationConditionEnum;
import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationKeyAttribute;
import com.elitecore.sm.aggregationservice.model.AggregationOperationExpressionEnum;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "aggregationDefinitionService")
public class AggregationDefinitionServiceImpl implements IAggregationDefinitionService {
	
	@Autowired
	ServicesService servicesService;

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IAggregationDefinitionDao aggDefinitionDAO;
		
	@Autowired
	IAggregationConditionDao aggConditionDao;
	
	@Autowired
	IAggregationKeyAttributeDao aggKeyAttributeDao;
	
	@Autowired
	IAggregationAttributeDao aggAttributeDao;
	
	@Autowired
	IAggregationAttributeService aggAttributeService;

	@Autowired
	IAggregationKeyAttributeService aggKeyAttributeService;
	
	@Autowired
	IAggregationConditionService aggConditionService;
	
	@Autowired
	ServicesDao serviceDao;
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_AGGREGATION_DEFINATION, actionType = BaseConstants.CREATE_ACTION, currentEntity = AggregationService.class ,ignorePropList= "")
	public ResponseObject saveAggregationDefintion(
			AggregationDefinition aggDefinition, String defConditions,
			String defAggAttributes, String defKeyAttributes, int staffId) {
		
		ResponseObject responseObject = new ResponseObject();
		
		AggregationService aggregationService = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(aggDefinition.getAggregationService().getId());
		AggregationDefinition aggDefinitionData = aggDefinitionDAO.getAggregationDefintionListByName(aggDefinition.getAggDefName());
		
		if(aggDefinitionData != null){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_NAME_EXIST);
		}else{	
			List<AggregationCondition> aggConditionList = new ArrayList<>();
			if(StringUtils.isNotEmpty(defConditions)) {
				JSONArray jsonArray = new JSONArray(defConditions);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String conditionExpressionValue = jsonObj.getString("conditionExpressionValue");
					String conditionAction = jsonObj.getString("conditionAction");

					AggregationCondition aggCondition = new AggregationCondition();
					aggCondition.setCreatedByStaffId(staffId);
					aggCondition.setCondExpression(conditionExpressionValue);
					aggCondition.setCondAction(conditionAction);
					aggCondition.setAggregationDefinition(aggDefinition);
					aggConditionList.add(aggCondition);
				}
				aggDefinition.setAggConditionList(aggConditionList);
			}
			
			List<AggregationKeyAttribute> aggKeyAttrList = new ArrayList<>();
			if(StringUtils.isNotEmpty(defKeyAttributes)) {
				JSONArray jsonArray = new JSONArray(defKeyAttributes);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String fieldName = jsonObj.getString("aggregationFieldName");
					AggregationKeyAttribute aggregationKeyAttribute = new AggregationKeyAttribute();
					aggregationKeyAttribute.setCreatedByStaffId(staffId);
					aggregationKeyAttribute.setFieldName(fieldName);
					aggregationKeyAttribute.setAggregationDefinition(aggDefinition);
					aggKeyAttrList.add(aggregationKeyAttribute);
				}
				aggDefinition.setAggKeyAttrList(aggKeyAttrList);
			}
			
			List<AggregationAttribute> aggAttrList = new ArrayList<>();
			if(StringUtils.isNotEmpty(defAggAttributes)) {
				JSONArray jsonArray = new JSONArray(defAggAttributes);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String outputDataType = jsonObj.getString("aggoutputfielddatatypefield");
					String outputExpression = jsonObj.getString("operationexpression");
					String outputFieldName = jsonObj.getString("outputfieldname");
					AggregationAttribute aggregationAttribute = new AggregationAttribute();
					aggregationAttribute.setCreatedByStaffId(staffId);
					aggregationAttribute.setOutputDataType(outputDataType);
					aggregationAttribute.setOutputExpression(outputExpression);
					aggregationAttribute.setOutputFieldName(outputFieldName);
					aggregationAttribute.setAggregationDefinition(aggDefinition);
					aggAttrList.add(aggregationAttribute);
				}
				aggDefinition.setAggAttrList(aggAttrList);
			}
			aggregationService.setAggregationDefinition(aggDefinition);
			aggDefinition.setAggregationService(aggregationService);
			aggDefinition.setCreatedByStaffId(staffId);
			serviceDao.update(aggregationService);
			responseObject.setObject(aggregationService.getAggregationDefinition());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_ADD_SUCCESS);
		}
		return responseObject;		
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_AGGREGATION_DEFINATION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = AggregationDefinition.class, ignorePropList = "aggConditionList,aggKeyAttrList,aggAttrList,aggregationService")
	public ResponseObject updateAggregationDefintion(AggregationDefinition newDefinition, String defConditions,
			String defAggAttributes, String defKeyAttributes, int staffId) {
		ResponseObject responseObject = new ResponseObject();	
		if(newDefinition != null){
			AggregationService aggregationService = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(newDefinition.getAggregationService().getId());
			AggregationDefinition oldAggDefinition = aggregationService.getAggregationDefinition();
			
			if(oldAggDefinition.getAggConditionList() != null){
				Iterator<AggregationCondition> aggCondIter = oldAggDefinition.getAggConditionList().iterator();
				while(aggCondIter.hasNext()){
					AggregationCondition aggregationCondition = aggCondIter.next();
					aggConditionDao.deleteAggregationCondition(aggregationCondition);
				}
			}
			oldAggDefinition.getAggConditionList().clear();
			if(StringUtils.isNotEmpty(defConditions)) {
				JSONArray jsonArray = new JSONArray(defConditions);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String conditionExpressionValue = jsonObj.getString("conditionExpressionValue");
					String conditionAction = jsonObj.getString("conditionAction");

					AggregationCondition aggCondition = new AggregationCondition();
					aggCondition.setCreatedByStaffId(staffId);
					aggCondition.setCondExpression(conditionExpressionValue);
					aggCondition.setCondAction(conditionAction);
					aggCondition.setAggregationDefinition(oldAggDefinition);
					oldAggDefinition.getAggConditionList().add(aggCondition);
				}
			}
			
			if(oldAggDefinition.getAggKeyAttrList() != null){
				Iterator<AggregationKeyAttribute> aggKeyIter = oldAggDefinition.getAggKeyAttrList().iterator();
				while(aggKeyIter.hasNext()){
					AggregationKeyAttribute aggregationKeyAttribute = aggKeyIter.next();
					aggKeyAttributeDao.deleteAggregationKeyAttribute(aggregationKeyAttribute);
				}
			}
			oldAggDefinition.getAggKeyAttrList().clear();
			if(StringUtils.isNotEmpty(defKeyAttributes)) {
				JSONArray jsonArray = new JSONArray(defKeyAttributes);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String fieldName = jsonObj.getString("aggregationFieldName");
					AggregationKeyAttribute aggregationKeyAttribute = new AggregationKeyAttribute();
					aggregationKeyAttribute.setCreatedByStaffId(staffId);
					aggregationKeyAttribute.setFieldName(fieldName);
					aggregationKeyAttribute.setAggregationDefinition(oldAggDefinition);
					oldAggDefinition.getAggKeyAttrList().add(aggregationKeyAttribute);
				}
			}
			
			if(oldAggDefinition.getAggAttrList() != null){
				Iterator<AggregationAttribute> aggAttrIter = oldAggDefinition.getAggAttrList().iterator();
				while(aggAttrIter.hasNext()){
					AggregationAttribute aggregationAttribute  = aggAttrIter.next();
					aggAttributeDao.deleteAggregationAttribute(aggregationAttribute);
				}
			}
			oldAggDefinition.getAggAttrList().clear();
			if(StringUtils.isNotEmpty(defAggAttributes)) {
				JSONArray jsonArray = new JSONArray(defAggAttributes);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObj = jsonArray.getJSONObject(i);
					String outputDataType = jsonObj.getString("aggoutputfielddatatypefield");
					String outputExpression = jsonObj.getString("operationexpression");
					String outputFieldName = jsonObj.getString("outputfieldname");
					AggregationAttribute aggregationAttribute = new AggregationAttribute();
					aggregationAttribute.setCreatedByStaffId(staffId);
					aggregationAttribute.setOutputDataType(outputDataType);
					aggregationAttribute.setOutputExpression(outputExpression);
					aggregationAttribute.setOutputFieldName(outputFieldName);
					aggregationAttribute.setAggregationDefinition(oldAggDefinition);
					oldAggDefinition.getAggAttrList().add(aggregationAttribute);
				}
			}
			oldAggDefinition.setAggDefName(newDefinition.getAggDefName());
			oldAggDefinition.setNoOfPartition(newDefinition.getNoOfPartition());
			oldAggDefinition.setPartCDRField(newDefinition.getPartCDRField());
			oldAggDefinition.setfLegVal(newDefinition.getfLegVal());
			oldAggDefinition.setlLegVal(newDefinition.getlLegVal());
			oldAggDefinition.setUnifiedDateFiled(newDefinition.getUnifiedDateFiled());
			oldAggDefinition.setAggInterval(newDefinition.getAggInterval());
			oldAggDefinition.setOutputFileField(newDefinition.getOutputFileField());

			serviceDao.merge(aggregationService);
			responseObject.setObject(aggregationService.getAggregationDefinition());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_UPDATE_SUCCESS);
					
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AGGREGATION_DEFINITION_UPDATE_FAIL);
		}		
		return responseObject;
	}
	
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getAggregationDefinitionByServiceId(int serviceId) {
		ResponseObject responseObject=new ResponseObject();
		AggregationDefinition aggDefinition = aggDefinitionDAO.getAggregationDefintionListByServiceId(serviceId);
		responseObject.setSuccess(true);
		responseObject.setObject(aggDefinition);
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAllAggregationDefintionName() {
		ResponseObject responseObject = new ResponseObject();
		List<AggregationDefinition> aggDefinitionList = aggDefinitionDAO.getAllAggregationDefintionName();
		if(aggDefinitionList !=null){
			responseObject.setSuccess(true);
			responseObject.setObject(aggDefinitionList);
		}else{
			logger.debug("There is no aggregation definition created");
		}
		return responseObject;	
	}
	
	@Transactional(readOnly = true)
	@Override
	public boolean isUniqueDefinitionName(int defId,String newName){
		String oldName = aggDefinitionDAO.findByPrimaryKey(AggregationDefinition.class, defId).getAggDefName();
		int count = 0;
		if (org.apache.commons.lang3.StringUtils.isNotBlank(newName)) {
			count = aggDefinitionDAO.getDefinitionNameCount(newName);
		}
		logger.debug("defId :: " + defId + ",oldName ::" + oldName + ",newName ::" + newName);
		if(!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(oldName, newName) && count>0){
			return false;
		}else{
			return true;
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAggregationDefintionData(String aggDefName) {
		ResponseObject responseObject = new ResponseObject();
		AggregationDefinition aggDefinition = aggDefinitionDAO.getAggregationDefintionListByName(aggDefName);
		Map<String,Object> obj = new HashMap<>();
		if(aggDefinition !=null){
			JSONArray jsonConditionArray =  getJSONFromAggConditionList(aggDefinition.getAggConditionList());
			JSONArray jsonAggKeyArray =  getJSONFromKeyAttrList(aggDefinition.getAggKeyAttrList());	
			JSONArray jsonAggAttributeArray =  getJSONFromAggAttrList(aggDefinition.getAggAttrList());	
			obj.put("jsonConditionArray", jsonConditionArray);
			obj.put("jsonAggKeyArray", jsonAggKeyArray);
			obj.put("jsonAggAttributeArray", jsonAggAttributeArray);
			obj.put("aggDefinition", aggDefinition);
			responseObject.setSuccess(true);
			responseObject.setObject(obj);			
		}else{
			responseObject.setSuccess(false);
			logger.debug("There is no aggregation definition find with " + aggDefName);
		}
		return responseObject;	
	}
	
	@Override
	public void importDefinitionAddAndKeepBothMode(AggregationDefinition exportedDefinition, AggregationService service, int importMode) {
		AggregationDefinition dbDefinition = service.getAggregationDefinition();
		if(dbDefinition == null){
			exportedDefinition.setId(0);
			exportedDefinition.setCreatedDate(EliteUtils.getDateForImport(false));
			exportedDefinition.setLastUpdatedDate(EliteUtils.getDateForImport(false));
			exportedDefinition.setCreatedByStaffId(service.getCreatedByStaffId());
			exportedDefinition.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
			
			int defNameCount = aggDefinitionDAO.getDefinitionNameCount(exportedDefinition.getAggDefName());
			if(defNameCount > 0){
				exportedDefinition.setAggDefName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedDefinition.getAggDefName()));
			}
			
			List<AggregationCondition> exportedConditionList = exportedDefinition.getAggConditionList();
			if(exportedConditionList != null && !exportedConditionList.isEmpty()) {
				int length = exportedConditionList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationCondition exportedCondition = exportedConditionList.get(i);
					if(exportedCondition != null) {
						aggConditionService.importDataAggregationConditionAddAndKeepBothMode(exportedCondition, exportedDefinition, importMode);
					}
				}
			}
			
			List<AggregationKeyAttribute> exportedKeyAttrList = exportedDefinition.getAggKeyAttrList();
			if(exportedKeyAttrList != null && !exportedKeyAttrList.isEmpty()) {
				int length = exportedKeyAttrList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationKeyAttribute exportedKeyAttribute = exportedKeyAttrList.get(i);
					if(exportedKeyAttribute != null) {
						aggKeyAttributeService.importDataAggregationKeyAttributeAddAndKeepBothMode(exportedKeyAttribute, exportedDefinition, importMode);
					}
				}
			}
			
			List<AggregationAttribute> exportedAggAttrList = exportedDefinition.getAggAttrList();
			if(exportedAggAttrList != null && !exportedAggAttrList.isEmpty()) {
				int length = exportedAggAttrList.size();
				for(int i = length-1; i >= 0; i--) {
					AggregationAttribute exportedAggAttribute = exportedAggAttrList.get(i);
					if(exportedAggAttribute != null) {
						aggAttributeService.importDataAggregationAttributeAddAndKeepBothMode(exportedAggAttribute, exportedDefinition, importMode);
					}
				}
			}
			service.setAggregationDefinition(exportedDefinition);
			exportedDefinition.setAggregationService(service);
		}
	}
	
	@Transactional
	@Override
	public void importDefinitionUpdateMode(AggregationDefinition dbDefinition, AggregationDefinition exportedDefinition, int importMode) {
		
		if(importMode == BaseConstants.IMPORT_MODE_UPDATE){
			dbDefinition.setNoOfPartition(exportedDefinition.getNoOfPartition());
			dbDefinition.setPartCDRField(exportedDefinition.getPartCDRField());
			dbDefinition.setfLegVal(exportedDefinition.getfLegVal());
			dbDefinition.setlLegVal(exportedDefinition.getlLegVal());
			dbDefinition.setUnifiedDateFiled(exportedDefinition.getUnifiedDateFiled());
			dbDefinition.setAggInterval(exportedDefinition.getAggInterval());
			dbDefinition.setOutputFileField(exportedDefinition.getOutputFileField());
		}
		
		List<AggregationAttribute> dbAttributeList = dbDefinition.getAggAttrList();
		List<AggregationAttribute> exportedAttributeList = exportedDefinition.getAggAttrList();
		
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null) {
					AggregationAttribute dbAttribute = aggAttributeService.getDataAggregationAttributeFromList(dbAttributeList, exportedAttribute.getOutputFieldName());
					if(dbAttribute != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to update attribute : "+dbAttribute.getOutputFieldName());
						aggAttributeService.importDataAggregationAttributeUpdateMode(dbAttribute, exportedAttribute);
						dbAttributeList.add(dbAttribute);
					} else if(dbAttribute == null || importMode != BaseConstants.IMPORT_MODE_ADD){
						logger.debug("going to add attribute : "+exportedAttribute.getOutputFieldName());
						aggAttributeService.importDataAggregationAttributeAddAndKeepBothMode(exportedAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
						dbAttributeList.add(exportedAttribute);
					}
				}
			}
		}
		
		List<AggregationKeyAttribute> dbKeyAttributeList = dbDefinition.getAggKeyAttrList();
		List<AggregationKeyAttribute> exportedKeyAttributeList = exportedDefinition.getAggKeyAttrList();
		
		if(!CollectionUtils.isEmpty(exportedKeyAttributeList)) {
			int length = exportedKeyAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationKeyAttribute exportedKeyAttribute = exportedKeyAttributeList.get(i);
				if(exportedKeyAttribute != null) {
					AggregationKeyAttribute dbKeyAttribute = aggKeyAttributeService.getDataAggregationKeyAttributeFromList(dbKeyAttributeList, exportedKeyAttribute.getFieldName());
					if(dbKeyAttribute == null) {
						logger.debug("going to add attribute : "+exportedKeyAttribute.getFieldName());
						aggKeyAttributeService.importDataAggregationKeyAttributeAddAndKeepBothMode(exportedKeyAttribute, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
						dbKeyAttributeList.add(exportedKeyAttribute);
					}					
				}
			}
		}
		
		List<AggregationCondition> dbConditionList = dbDefinition.getAggConditionList();
		List<AggregationCondition> exportedConditionList = exportedDefinition.getAggConditionList();
		
		if(!CollectionUtils.isEmpty(exportedConditionList)) {
			int length = exportedConditionList.size();
			for(int i = length-1; i >= 0; i--) {
				AggregationCondition exportedCondition = exportedConditionList.get(i);
				if(exportedCondition != null) {
					AggregationCondition dbCondition = aggConditionService.getDataConditionFromList(dbConditionList, exportedCondition.getCondExpression());
					if(dbCondition != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to update attribute : "+dbCondition.getCondExpression());
						aggConditionService.importDataConditionUpdateMode(dbCondition, exportedCondition);
						dbConditionList.add(dbCondition);
					} else if(dbCondition == null || importMode != BaseConstants.IMPORT_MODE_ADD){
						logger.debug("going to add attribute : "+exportedCondition.getCondExpression());
						aggConditionService.importDataAggregationConditionAddAndKeepBothMode(exportedCondition, dbDefinition, BaseConstants.IMPORT_MODE_UPDATE);
						dbConditionList.add(exportedCondition);
					}
				}
			}
		}
	}
	
	@Override
	public JSONArray getJSONFromAggConditionList(List<AggregationCondition> aggConditionList){
		JSONArray jsonConditionArray =  new JSONArray();
		if(!aggConditionList.isEmpty()) {
			for(AggregationCondition aggCondition : aggConditionList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", aggCondition.getId());
				jsonObj.put("conditionExpression", AggregationConditionEnum.fromValue(aggCondition.getCondExpression()));
				jsonObj.put("conditionExpressionValue", aggCondition.getCondExpression());
				jsonObj.put("conditionAction", aggCondition.getCondAction());
				jsonConditionArray.put(jsonObj);
			}
		}
		return jsonConditionArray;
	}
	
	@Override
	public JSONArray getJSONFromAggAttrList(List<AggregationAttribute> aggAttrList){
		JSONArray jsonAggAttributeArray =  new JSONArray();
		if(!aggAttrList.isEmpty()) {
			for(AggregationAttribute aggAttribute : aggAttrList){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", aggAttribute.getId());
				jsonObj.put("aggoutputfielddatatypefield", aggAttribute.getOutputDataType());
				jsonObj.put("operationexpression", aggAttribute.getOutputExpression());
				jsonObj.put("operationexpressionText", AggregationOperationExpressionEnum.fromValue(aggAttribute.getOutputExpression()));
				jsonObj.put("outputfieldname", aggAttribute.getOutputFieldName());
				jsonAggAttributeArray.put(jsonObj);
			}
		}
		return jsonAggAttributeArray;
	}
	
	@Override
	public JSONArray getJSONFromKeyAttrList(List<AggregationKeyAttribute> aggregationKeyAttribute){
		JSONArray jsonAggKeyArray =  new JSONArray();
		if(!aggregationKeyAttribute.isEmpty()) {
			for(AggregationKeyAttribute aggKeyAttribute : aggregationKeyAttribute){
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("id", aggKeyAttribute.getId());
				jsonObj.put("aggregationFieldName", aggKeyAttribute.getFieldName());
				jsonAggKeyArray.put(jsonObj);
			}
		}
		return jsonAggKeyArray;
	}
}