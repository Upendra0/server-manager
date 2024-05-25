package com.elitecore.sm.consolidationservice.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.dao.DataConsolidationMappingDao;
import com.elitecore.sm.consolidationservice.dao.IConsolidationAttributeDao;
import com.elitecore.sm.consolidationservice.dao.IConsolidationDefinitionDao;
import com.elitecore.sm.consolidationservice.dao.IConsolidationGroupAttributeDao;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.DataConsolidationService;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "consolidationDefinitionService")
public class ConsolidationDefinitionServiceImpl implements IConsolidationDefinitionService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ServicesDao servicesDao;
	
	@Autowired
	IConsolidationDefinitionDao consolidationDefinitionDao;
	
	@Autowired
	private IConsolidationAttributeService consolidationAttributeService;

	@Autowired
	private IConsolidationGroupAttributeService consolidationGroupAttributeService;
	
	@Autowired
	PathListDao pathListDao;

	@Autowired
	IDataConsolidationService iDataConsolidation;
	
	@Autowired
	IConsolidationAttributeDao consolidationAttributeDao;
	
	@Autowired
	IConsolidationGroupAttributeDao consolidationGroupAttributeDao;
	
	@Autowired
	DataConsolidationMappingDao dataConsolidationMappingDao;
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_CONSOLIDATION_DEFINITION, actionType = BaseConstants.CREATE_ACTION, currentEntity = DataConsolidation.class, ignorePropList = "")
	public ResponseObject addConsolidationDefintionList(DataConsolidation dataConsList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Adding Data Consolidation Definition List details.");
		List<DataConsolidation> dataConsolidationList = this.iDataConsolidation.getDataConsolidationServicewiseCount(dataConsList.getConsName(), dataConsList.getConsService().getId());
		
		if(dataConsolidationList.isEmpty()){
			DataConsolidationService consService = (DataConsolidationService) servicesDao.getAllServiceDepedantsByServiceId(dataConsList.getConsService().getId());
			if (consService != null) {
				dataConsList.setConsService(consService);
				consolidationDefinitionDao.save(dataConsList);
				if (dataConsList.getId() != 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_CREATE_SUCCESS);
					responseObject.setObject(dataConsList);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_CREATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_CREATE_FAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_CONSOLIDATION_NAME_EXIST);
		}
		return responseObject;
	}
	
	@Transactional(readOnly=true)
	public boolean  isDataConsolidationUniqueForUpdate(int serviceId,String name, int consolidationId){
		
		List<DataConsolidation> dataConsolidationList = this.iDataConsolidation.getDataConsolidationServicewiseCount(name, serviceId);
		
		boolean isUnique=false;
		if(dataConsolidationList!=null && !dataConsolidationList.isEmpty()){
			
			for(DataConsolidation consolidation:dataConsolidationList){
				//If ID is same , then it is same pathList object
				if(consolidationId == consolidation.getId()){
					isUnique=true;
				}else{ // It is another pathList object , but name is same
					isUnique=false;
				}
			}
		}else if(dataConsolidationList != null && dataConsolidationList.isEmpty()){ // No pathList found with same name 
			isUnique=true;
		}
		return isUnique;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_CONSOLIDATION_DEFINITION, actionType = BaseConstants.UPDATE_ACTION, currentEntity = DataConsolidation.class, ignorePropList = "consAttList,consGrpAttList,consService")
	public ResponseObject updateConsolidationDefintionList(DataConsolidation dataConsList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Update Data Consolidation Definition List details.");
		if(isDataConsolidationUniqueForUpdate(dataConsList.getConsService().getId(), dataConsList.getConsName(), dataConsList.getId())){
			DataConsolidationService consService = (DataConsolidationService) servicesDao.getAllServiceDepedantsByServiceId(dataConsList.getConsService().getId());
			if (consService != null) {
				dataConsList.setConsService(consService);
				servicesDao.update(consService);
				consolidationDefinitionDao.merge(dataConsList);
				if (dataConsList.getId() != 0) {
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_UPDATE_SUCCESS);
					responseObject.setObject(dataConsList);
				} else {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_UPDATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_UPDATE_FAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DATA_CONSOLIDATION_NAME_EXIST);
		}
		
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getConsolidationDefintionListByServiceId(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray jConsDefArr = new JSONArray();
		JSONObject jConsDef ;
		List<DataConsolidation> dataConsolidations = (List<DataConsolidation>)consolidationDefinitionDao.getConsolidationDefintionListByServiceId(serviceId);
		if(dataConsolidations != null && !dataConsolidations.isEmpty()) {
			for(DataConsolidation dataConsolidation : dataConsolidations) {
				jConsDef = new JSONObject();
				createConsolidationDefJsonObject(jConsDef,dataConsolidation);
				createConsolidationDefGroupJsonObject(jConsDef,dataConsolidation.getConsGrpAttList());
				createConsolidationDefAttJsonObject(jConsDef,dataConsolidation.getConsAttList());
				jConsDefArr.put(jConsDef);
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jConsDefArr);
		return responseObject;
	}
	
	@Transactional
	@Override
	public List<DataConsolidation> getConsolidationListByServiceId(int serviceId){
		return (List<DataConsolidation>)consolidationDefinitionDao.getConsolidationDefintionListByServiceId(serviceId);
	}

	/**
	 * Method will create consolidation list details to jsonObject
	 * @param jConsDef
	 * @param list
	 */
	private void createConsolidationDefGroupJsonObject(JSONObject jConsDef, List<DataConsolidationGroupAttribute> list){
		JSONArray jsonList = new JSONArray();
		if(list != null){
			for(DataConsolidationGroupAttribute groupAttribute : list){
				if(!groupAttribute.getStatus().equals(StateEnum.DELETED)){
					JSONObject groupAttributeObj = new JSONObject();
					groupAttributeObj.put("id",groupAttribute.getId()); 							
					groupAttributeObj.put("groupingField",groupAttribute.getGroupingField());
					groupAttributeObj.put("regExEnable",groupAttribute.isRegExEnable());
					groupAttributeObj.put("regExExpression",groupAttribute.getRegExExpression());
					groupAttributeObj.put("destinationField",groupAttribute.getDestinationField());
					groupAttributeObj.put("enableLookup",groupAttribute.isLookUpEnable());
					groupAttributeObj.put("lookUpTableName",groupAttribute.getLookUpTableName());
					groupAttributeObj.put("lookUpTableColumnName",groupAttribute.getLookUpTableColumnName()); 	
					jsonList.put(groupAttributeObj);
				}
			}
		}
		jConsDef.put("consGrpAttList", jsonList);
	}
	
	/**
	 * Method will create a list to jsonObject
	 * @param jConsDef
	 * @param list
	 */
	private void createConsolidationDefAttJsonObject(JSONObject jConsDef, List<DataConsolidationAttribute> list){
		JSONArray jsonList = new JSONArray();
		if(list != null){
			for(DataConsolidationAttribute attribute : list){
				if(!attribute.getStatus().equals(StateEnum.DELETED)){
					JSONObject attributeObj = new JSONObject();
					attributeObj.put("id",attribute.getId()); 							
					attributeObj.put("fieldName",attribute.getFieldName());
					attributeObj.put("dataType",attribute.getDataType());
					attributeObj.put("operation",attribute.getOperation());
					attributeObj.put("description",attribute.getDescription());
					jsonList.put(attributeObj);
				}
			}
		}
		jConsDef.put("consAttList", jsonList);
	}
	
	/**
	 * Method will create a list to jsonObject
	 * @param jConsDef
	 * @param dataConsolidation
	 */
	private void createConsolidationDefJsonObject(JSONObject jConsDef, DataConsolidation dataConsolidation){
		jConsDef.put("id", dataConsolidation.getId());
		jConsDef.put("consName", dataConsolidation.getConsName());
		jConsDef.put("dateFieldName", dataConsolidation.getDateFieldName());
		jConsDef.put("segregationType", dataConsolidation.getSegregationType());
		jConsDef.put("acrossFilePartition", dataConsolidation.getAcrossFilePartition());
	}
	
	@Transactional
	@Override
	public ResponseObject getConsolidationDefintionById(DataConsolidation dataConsList) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Get Data Consolidation Definition List details.");
		
		DataConsolidation dataConsolidation = (DataConsolidation) consolidationDefinitionDao.findByPrimaryKey(DataConsolidation.class, dataConsList.getId());
		if (dataConsolidation != null) {
			Hibernate.initialize(dataConsolidation.getConsService());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_VIEW_SUCCESS);
			responseObject.setObject(dataConsolidation);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_VIEW_FAIL);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getConsolidationDefintionList(int serviceId) {
		ResponseObject responseObject = new ResponseObject();
		JSONArray jConsDefArr = new JSONArray();
		JSONObject jConsDef ;
		List<DataConsolidation> dataConsolidations = (List<DataConsolidation>)consolidationDefinitionDao.getConsolidationDefintionListByServiceId(serviceId);
		if(dataConsolidations != null && !dataConsolidations.isEmpty()) {
			for(DataConsolidation dataConsolidation : dataConsolidations) {
				jConsDef = new JSONObject();
				createConsolidationDefJsonObject(jConsDef,dataConsolidation);
				jConsDefArr.put(jConsDef);
			}
		}
		responseObject.setSuccess(true);
		responseObject.setObject(jConsDefArr);
		return responseObject;
	}
	
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_CONSOLIDATION_DEFINITION, actionType = BaseConstants.DELETE_CUSTOM_ACTION, currentEntity = DataConsolidation.class, ignorePropList = "consAttList,consGrpAttList")
	public ResponseObject deleteConsolidationDefintionList(DataConsolidation dataConsList, HttpServletRequest request, EliteUtils eliteUtils) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Delete Data Consolidation Definition List details.");
		
		responseObject = getConsolidationDefintionById(dataConsList);
		DataConsolidation dataConsolidation = (DataConsolidation)responseObject.getObject();
		if(dataConsolidation != null) {
			 
			boolean isExist = false;
			List<PathList> pathlistdetails = pathListDao.getPathListByServiceId(dataConsolidation.getConsService().getId());
			if(pathlistdetails != null && !pathlistdetails.isEmpty()){
				for(PathList pathList : pathlistdetails){
					if(StateEnum.ACTIVE.equals(pathList.getStatus())){
						DataConsolidationPathList consolidationPathList = (DataConsolidationPathList) pathList;
						List<DataConsolidationMapping> mappingList = consolidationPathList.getConMappingList();
						if(mappingList != null && !mappingList.isEmpty()){
							for(DataConsolidationMapping consMapping : mappingList){
								if(StateEnum.ACTIVE.equals(consMapping.getStatus()) && consMapping.getMappingName().equalsIgnoreCase(dataConsList.getConsName())){
									isExist = true;
									break;
								}
							}
						}
					}
				}
			}
			
			//Check IF Consolidation Definition Beaing Used by Path Mapping. If yes don't allow to delete it.
			if(isExist){
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_DELETE_USED_FAIL);
				return responseObject;
			}
			Date date = new Date();
			List<DataConsolidationAttribute> consAttList = dataConsolidation.getConsAttList();
			for(DataConsolidationAttribute dataConsolidationAttribute : consAttList) {
				dataConsolidationAttribute.setStatus(StateEnum.DELETED);
				dataConsolidationAttribute.setLastUpdatedDate(date);
				dataConsolidationAttribute.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				consolidationAttributeService.updateConsolidationAttributeList(dataConsolidationAttribute, dataConsolidation.getId());
			}
			List<DataConsolidationGroupAttribute> groupAttribute = dataConsolidation.getConsGrpAttList();
			for(DataConsolidationGroupAttribute dataConsolidationGroupAttribute : groupAttribute) {
				dataConsolidationGroupAttribute.setStatus(StateEnum.DELETED);
				dataConsolidationGroupAttribute.setLastUpdatedDate(date);
				dataConsolidationGroupAttribute.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				consolidationGroupAttributeService.updateConsolidationGroupAttributeList(dataConsolidationGroupAttribute, dataConsolidation.getId());
			}
			dataConsolidation.setStatus(StateEnum.DELETED);
			dataConsolidation.setLastUpdatedDate(date);
			dataConsolidation.setConsName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,dataConsolidation.getConsName()));
			dataConsolidation.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = updateConsolidationDefintionList(dataConsolidation);
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_DELETE_SUCCESS);
			responseObject.setObject(ajaxResponse);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_DELETE_FAIL);
		}
		return responseObject;
	}
	
	@Override
	public void importDataConsolidationUpdateMode(DataConsolidation dbConsolidation, DataConsolidation exportedConsolidation, int importMode) {
		
		if(importMode != BaseConstants.IMPORT_MODE_ADD){
			dbConsolidation.setDateFieldName(exportedConsolidation.getDateFieldName());
			dbConsolidation.setSegregationType(exportedConsolidation.getSegregationType());
			dbConsolidation.setAcrossFilePartition(exportedConsolidation.getAcrossFilePartition());
			dbConsolidation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		}
		
		List<DataConsolidationAttribute> dbAttributeList = dbConsolidation.getConsAttList();
		List<DataConsolidationAttribute> exportedAttributeList = exportedConsolidation.getConsAttList();
		
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null) {
					DataConsolidationAttribute dbAttribute = consolidationAttributeService.getDataConsolidationAttributeFromList(dbAttributeList, exportedAttribute.getFieldName());
					if(dbAttribute != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to update data consolidation attribute : "+dbAttribute.getFieldName());
						consolidationAttributeService.importDataConsolidationAttributeUpdateMode(dbAttribute, exportedAttribute);
						dbAttributeList.add(dbAttribute);
					} else if(dbAttribute == null || importMode != BaseConstants.IMPORT_MODE_ADD){
						logger.debug("going to add data consolidation attribute : "+exportedAttribute.getFieldName());
						consolidationAttributeService.importDataConsolidationAttributeAddAndKeepBothMode(exportedAttribute, dbConsolidation, BaseConstants.IMPORT_MODE_UPDATE);
						dbAttributeList.add(exportedAttribute);
					}
				}
			}
		}
		
		List<DataConsolidationGroupAttribute> dbGroupAttributeList = dbConsolidation.getConsGrpAttList();
		List<DataConsolidationGroupAttribute> exportedGroupAttributeList = exportedConsolidation.getConsGrpAttList();
		
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			int length = exportedGroupAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null) {
					DataConsolidationGroupAttribute dbGroupAttribute = consolidationGroupAttributeService.getDataConsolidationGroupAttributeFromList(dbGroupAttributeList, exportedGroupAttribute.getGroupingField());
					if(dbGroupAttribute != null && importMode != BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to update data consolidation group attribute : "+dbGroupAttribute.getGroupingField());
						consolidationGroupAttributeService.importDataConsolidationGroupAttributeUpdateMode(dbGroupAttribute, exportedGroupAttribute);
						dbGroupAttributeList.add(dbGroupAttribute);
					} else if(dbGroupAttribute == null || importMode != BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to add data consolidation group attribute : "+exportedGroupAttribute.getGroupingField());
						consolidationGroupAttributeService.importDataConsolidationGroupAttributeAddAndKeepBothMode(exportedGroupAttribute, dbConsolidation);
						dbGroupAttributeList.add(exportedGroupAttribute);
					}
				}
			}
		}
	}
	
	@Override
	public void importDataConsolidationAddMode(DataConsolidation dbConsolidation, DataConsolidation exportedConsolidation) {
		dbConsolidation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		
		List<DataConsolidationAttribute> dbAttributeList = dbConsolidation.getConsAttList();
		List<DataConsolidationAttribute> exportedAttributeList = exportedConsolidation.getConsAttList();
		
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null) {
					DataConsolidationAttribute dbAttribute = consolidationAttributeService.getDataConsolidationAttributeFromList(dbAttributeList, exportedAttribute.getFieldName());
					if(dbAttribute == null) {
						logger.debug("going to add data consolidation attribute : "+exportedAttribute.getFieldName());
						consolidationAttributeService.importDataConsolidationAttributeAddAndKeepBothMode(exportedAttribute, dbConsolidation, BaseConstants.IMPORT_MODE_UPDATE);
						dbAttributeList.add(exportedAttribute);
					}
				}
			}
		}
		
		List<DataConsolidationGroupAttribute> dbGroupAttributeList = dbConsolidation.getConsGrpAttList();
		List<DataConsolidationGroupAttribute> exportedGroupAttributeList = exportedConsolidation.getConsGrpAttList();
		
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			int length = exportedGroupAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null) {
					DataConsolidationGroupAttribute dbGroupAttribute = consolidationGroupAttributeService.getDataConsolidationGroupAttributeFromList(dbGroupAttributeList, exportedGroupAttribute.getGroupingField());
					if(dbGroupAttribute == null) {
						logger.debug("going to add data consolidation group attribute : "+exportedGroupAttribute.getGroupingField());
						consolidationGroupAttributeService.importDataConsolidationGroupAttributeAddAndKeepBothMode(exportedGroupAttribute, dbConsolidation);
						dbGroupAttributeList.add(exportedGroupAttribute);
					}
				}
			}
		}
	}
	
	@Override
	public void importDataConsolidationAddAndKeepBothMode(DataConsolidation exportedDataConsolidation, DataConsolidationService service, int importMode) {
		exportedDataConsolidation.setId(0);
		exportedDataConsolidation.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedDataConsolidation.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedDataConsolidation.setCreatedByStaffId(service.getCreatedByStaffId());
		exportedDataConsolidation.setLastUpdatedByStaffId(service.getLastUpdatedByStaffId());
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedDataConsolidation.setConsName(EliteUtils.checkForNames(BaseConstants.IMPORT, exportedDataConsolidation.getConsName()));
		}
		
		List<DataConsolidationAttribute> exportedAttributeList = exportedDataConsolidation.getConsAttList();
		if(exportedAttributeList != null && !exportedAttributeList.isEmpty()) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null) {
					consolidationAttributeService.importDataConsolidationAttributeAddAndKeepBothMode(exportedAttribute, exportedDataConsolidation, importMode);
				}
			}
		}
		
		List<DataConsolidationGroupAttribute> exportedGroupAttributeList = exportedDataConsolidation.getConsGrpAttList();
		if(exportedGroupAttributeList != null && !exportedGroupAttributeList.isEmpty()) {
			int length = exportedGroupAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null) {
					consolidationGroupAttributeService.importDataConsolidationGroupAttributeAddAndKeepBothMode(exportedGroupAttribute, exportedDataConsolidation);
				}
			}
		}
		exportedDataConsolidation.setConsService(service);
	}
	
	@Override
	public DataConsolidation getDataConsolidationFromList(List<DataConsolidation> dataConsolidationList, String dataConsolidationName) {
		if(!CollectionUtils.isEmpty(dataConsolidationList)) {
			int length = dataConsolidationList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidation consolidation = dataConsolidationList.get(i);
				if(consolidation != null && !consolidation.getStatus().equals(StateEnum.DELETED) && consolidation.getConsName().equalsIgnoreCase(dataConsolidationName)) {
					return dataConsolidationList.remove(i);
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public long getDataConsolidationMappingCountByMappingNameAndDestPath(int mappingId,String mappingName, String destPath) {
		return dataConsolidationMappingDao.getDataConsolidationMappingCountByMappingNameAndDestPath(mappingId,mappingName, destPath);
	}
}