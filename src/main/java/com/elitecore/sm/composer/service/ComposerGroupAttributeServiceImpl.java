package com.elitecore.sm.composer.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.dao.ComposerAttributeDao;
import com.elitecore.sm.composer.dao.ComposerGroupAttributeDao;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.RoamingComposerAttribute;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;

@Service
public class ComposerGroupAttributeServiceImpl implements ComposerGroupAttributeService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	ComposerAttributeService composerAttributeService;
	
	@Autowired
	ComposerGroupAttributeDao composerGroupAttributeDao;
	
	@Autowired
	ComposerAttributeDao composerAttributeDao;
	
	@Transactional
	@Override
	public ResponseObject getGroupAttributeByMappingId(int mappingId, int groupId) {
		logger.debug("Fetching single group attribute for TAP Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		ComposerGroupAttribute gruopAttribute = composerGroupAttributeDao.getGroupAttributeByMappingId(mappingId, groupId);
		
		if (gruopAttribute != null) {
			responseObject.setObject(gruopAttribute);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(gruopAttribute);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getGroupAttributeListByMappingId(int mappingId) {
		logger.debug("Fetching all group attribute for TAP Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> gruopAttributeList = composerGroupAttributeDao.getGroupAttributeListByMappingId(mappingId);
		
		if (gruopAttributeList != null && !gruopAttributeList.isEmpty()) {
			responseObject.setObject(gruopAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(gruopAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject deleteGroupAttributes(String groupAttributeIds, int staffId, int mappingId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(groupAttributeIds)){
			String [] GroupAttributeIdList = groupAttributeIds.split(",");
			
			for(int i = 0; i < GroupAttributeIdList.length; i ++ ){
				responseObject = deleteGroupAttribute(Integer.parseInt(GroupAttributeIdList[i]), staffId, mappingId);
			}
			
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}
		
		return responseObject;
	}
	
	public ResponseObject deleteGroupAttribute(int groupAttributeId, int staffId, int mappingId) {
		ResponseObject responseObject = new ResponseObject();
	
		if(groupAttributeId > 0){
			
			ComposerGroupAttribute composerGroupAttribute  = composerGroupAttributeDao.findByPrimaryKey(ComposerGroupAttribute.class, groupAttributeId);
			if(composerGroupAttribute != null){
				List<ComposerAttribute> composerAttributeLists = composerAttributeDao.getComposerAttributeListByGroupId(composerGroupAttribute.getId(), mappingId);
				if(composerAttributeLists != null && !composerAttributeLists.isEmpty()){
					for(ComposerAttribute composerAttribute : composerAttributeLists){
						composerAttribute.setComposerGroupAttribute(null);
						composerAttributeDao.merge(composerAttribute);
					}
				}
				List<ComposerGroupAttribute> childComposerGroupAttributeLists = composerGroupAttributeDao.getGroupAttributeListByGroupId(composerGroupAttribute.getId(), mappingId);
				if(childComposerGroupAttributeLists != null && !childComposerGroupAttributeLists.isEmpty()){
					for(ComposerGroupAttribute childComposerGroupAttribute : childComposerGroupAttributeLists){
						childComposerGroupAttribute.setBaseGroup(null);
					}
				}
				composerGroupAttribute.setBaseGroup(null);
				composerGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,composerGroupAttribute.getName()));
				composerGroupAttribute.setLastUpdatedDate(new Date());
				composerGroupAttribute.setLastUpdatedByStaffId(staffId);
				composerGroupAttribute.setStatus(StateEnum.DELETED);
				composerGroupAttribute.setMyComposer(null);
				
				composerGroupAttributeDao.merge(composerGroupAttribute);
				responseObject.setSuccess(true);
				responseObject.setObject(composerGroupAttribute);
				responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject createGroupAttribute(ComposerGroupAttribute composerGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		composerGroupAttribute.setName(composerGroupAttribute.getName());
		composerGroupAttribute.setCreatedByStaffId(staffId);
		composerGroupAttribute.setLastUpdatedByStaffId(staffId);
		composerGroupAttribute.setCreatedDate(new Date());
		composerGroupAttribute.setLastUpdatedDate(new Date());
		
		if (isGroupAttributeUniqueForUpdate(composerGroupAttribute.getId(), composerGroupAttribute.getName(), mappingId)){
		responseObject = composerMappingService.getComposerMappingDetailsById(mappingId, plugInType);
		
		if(responseObject.isSuccess()){

			composerGroupAttribute.setMyComposer((ComposerMapping) responseObject.getObject());
			//groupAttribute.setAttributeList(attributeList);
			composerGroupAttributeDao.save(composerGroupAttribute);

			List <ComposerGroupAttribute> groupAttributeList =  getGroupAttributeByGroupId(groupAttrLists, mappingId);
			if(groupAttributeList != null && !groupAttributeList.isEmpty()){
				for(ComposerGroupAttribute groupAttr : groupAttributeList){
					groupAttr.setBaseGroup(composerGroupAttribute);
					composerGroupAttributeDao.merge(groupAttr);
				}
			}
			
			List <ComposerAttribute> attributeList =  getComposerAttributeByAttributeId(attrLists, mappingId);
			if(attributeList != null && !attributeList.isEmpty()){
				for(ComposerAttribute attribute : attributeList){
					attribute.setComposerGroupAttribute(composerGroupAttribute);
					composerAttributeDao.merge(attribute);
				}
			}
					
			if(composerGroupAttribute.getId() > 0){
				responseObject.setSuccess(true);
				responseObject.setObject(composerGroupAttribute);
				responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_ADD_SUCCESS);
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_ADD_FAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_ADD_FAIL);
		}
		
			
		}else {
			logger.info("duplicate attribute source field found:" + composerGroupAttribute.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.COMPOSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND);
		}
		return responseObject;
	}

	private List<ComposerGroupAttribute> getGroupAttributeByGroupId(String groupAttrLists, int mappingId) {
		
		List<ComposerGroupAttribute> groupAttributeList = new ArrayList<>();
		
		if(!StringUtils.isEmpty(groupAttrLists)){
			String [] groupAttributeIdList = groupAttrLists.split(",");
			
			for(int i = 0; i < groupAttributeIdList.length; i ++ ){
				ComposerGroupAttribute composerGroupAttribute = composerGroupAttributeDao.getGroupAttributeByGroupId(Integer.parseInt(groupAttributeIdList[i]), mappingId);
				groupAttributeList.add(composerGroupAttribute);
			}
		}
		
		return groupAttributeList;
	}

	@Transactional
	@Override
	public ResponseObject updateGroupAttribute(ComposerGroupAttribute composerGroupAttribute, String groupAttrLists, String attrLists, int mappingId, String plugInType, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		 
		composerGroupAttribute.setCreatedByStaffId(staffId);
		composerGroupAttribute.setLastUpdatedByStaffId(staffId);
		composerGroupAttribute.setCreatedDate(new Date());
		composerGroupAttribute.setLastUpdatedDate(new Date());
		if (isGroupAttributeUniqueForUpdate(composerGroupAttribute.getId(), composerGroupAttribute.getName(), mappingId)){
			if(composerGroupAttribute.getId() > 0 ){
				ComposerGroupAttribute dbGroupAttribute = composerGroupAttributeDao.findByPrimaryKey(ComposerGroupAttribute.class, composerGroupAttribute.getId());
				
				if(dbGroupAttribute != null ){
					
					dbGroupAttribute.setName(composerGroupAttribute.getName());
					dbGroupAttribute.setLastUpdatedDate(new Date());
					dbGroupAttribute.setLastUpdatedByStaffId(staffId);
					
					composerGroupAttributeDao.merge(dbGroupAttribute);
					
					List <ComposerGroupAttribute> groupAttributeListToDetach =  composerGroupAttributeDao.getGroupAttributeListByGroupId(composerGroupAttribute.getId(), mappingId);
					if(groupAttributeListToDetach != null && !groupAttributeListToDetach.isEmpty()){
						for(ComposerGroupAttribute groupAttr : groupAttributeListToDetach){
							groupAttr.setBaseGroup(null);
							composerGroupAttributeDao.merge(groupAttr);
						}
					}
					List <ComposerGroupAttribute> groupAttributeListToAttach =  getGroupAttributeByGroupId(groupAttrLists, mappingId);
					if(groupAttributeListToAttach  != null && !groupAttributeListToAttach.isEmpty()){
						for(ComposerGroupAttribute groupAttr : groupAttributeListToAttach){
							groupAttr.setBaseGroup(dbGroupAttribute);
							composerGroupAttributeDao.merge(groupAttr);
						}
					}
					
					List <ComposerAttribute> attributeListToDetach =  composerAttributeDao.getComposerAttributeListByGroupId(composerGroupAttribute.getId(), mappingId);
					if(attributeListToDetach != null && !attributeListToDetach.isEmpty()){
						for(ComposerAttribute attribute : attributeListToDetach){
							attribute.setComposerGroupAttribute(null);
							composerAttributeDao.merge(attribute);
						}
					}
					List <ComposerAttribute> attributeListToAttach =  getComposerAttributeByAttributeId(attrLists, mappingId);
					if(attributeListToAttach  != null && !attributeListToAttach .isEmpty()){
						for(ComposerAttribute attribute : attributeListToAttach){
							attribute.setComposerGroupAttribute(dbGroupAttribute);
							composerAttributeDao.merge(attribute);
						}
					}
					
					responseObject.setSuccess(true);
					responseObject.setObject(composerGroupAttribute);
					responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_UPDATE_SUCCESS);
					
				}else{
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_UPDATE_FAIL);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_UPDATE_FAIL);
			}
		}
		else {
			logger.info("duplicate group attribute found:" + composerGroupAttribute.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.COMPOSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND);
		}
		return responseObject;
	}

	@Transactional(readOnly = true)
	public boolean isGroupAttributeUniqueForUpdate(int groupAttributeId, String groupAttributeName, int mappingId) {

		ComposerGroupAttribute composerGroupAttribute = composerGroupAttributeDao.checkUniqueGroupAttributeNameForUpdate(mappingId, groupAttributeName);

		boolean isUnique;

		if (composerGroupAttribute != null) {
			// If ID is same , then it is same attribute object
			if (groupAttributeId == composerGroupAttribute.getId()) {
				logger.info("Mapping name is found unique.");
				isUnique = true;
			} else { // It is another attribute object , but name is same
				logger.info("Duplicate mapping name found.");
				isUnique = false;
			}
		} else { // No attribute found with same name
			logger.info("Mapping name is found unique.");
			isUnique = true;
		}
		return isUnique;
	}
	
	@Transactional
	public List<ComposerAttribute> getComposerAttributeByAttributeId(String attrLists, int mappingId) {
		
		List<ComposerAttribute> composerAttributeList = new ArrayList<>();
		
		if(!StringUtils.isEmpty(attrLists)){
			String [] ComposerAttributeIdList = attrLists.split(",");
			
			for(int i = 0; i < ComposerAttributeIdList.length; i ++ ){
				ComposerAttribute composerAttribute = composerAttributeDao.getComposerAttributeByAttributeId(Integer.parseInt(ComposerAttributeIdList[i]), mappingId);
				composerAttributeList.add(composerAttribute);
			}
		}
		
		return composerAttributeList;
	}

	@Transactional
	@Override
	public ResponseObject getGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list by group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> groupAttributeList = composerGroupAttributeDao.getGroupAttributeListByGroupId(groupId, mappingId);

		if (groupAttributeList != null && !groupAttributeList.isEmpty()) {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getAttachedAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all attribute list Attached with group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerGroupAttributeDao.getAttachedAttributeListByGroupId(groupId, mappingId);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getAttachedGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> groupAttributeList = composerGroupAttributeDao.getAttachedGroupAttributeListByGroupId(groupId, mappingId);

		if (groupAttributeList != null && !groupAttributeList.isEmpty()) {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getAttributeListEligibleToAttachWithGroup(int mappingId) {
		logger.debug("Fetching all attribute list eligible to Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerGroupAttributeDao.getAttributeListEligibleToAttachWithGroup(mappingId);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> groupAttributeList = composerGroupAttributeDao.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);

		if (groupAttributeList != null && !groupAttributeList.isEmpty()) {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		
		List<ComposerGroupAttribute> eligibleGroupAttributeList = composerGroupAttributeDao.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);
		
		List<ComposerGroupAttribute> unEligibleGroupAttributeList = getParentGroupAttributesByGroupId(groupId, mappingId);

		unEligibleGroupAttributeList.add(composerGroupAttributeDao.getGroupAttributeByGroupId(groupId, mappingId));
		
		eligibleGroupAttributeList.removeAll(unEligibleGroupAttributeList);
		
		if (eligibleGroupAttributeList != null && !eligibleGroupAttributeList.isEmpty()) {
			responseObject.setObject(eligibleGroupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(eligibleGroupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	private List<ComposerGroupAttribute> getParentGroupAttributesByGroupId(int groupId , int mappingId) {
		
		List<ComposerGroupAttribute> parentGroupAttributeList = new ArrayList<>();
		
		ComposerGroupAttribute parentGroupAttribute = composerGroupAttributeDao.getGroupAttributeByGroupId(groupId, mappingId).getBaseGroup();
		
		if(parentGroupAttribute != null){
			parentGroupAttributeList.add(parentGroupAttribute);
			parentGroupAttributeList.addAll(getParentGroupAttributesByGroupId(parentGroupAttribute.getId(),mappingId));
		}
		return parentGroupAttributeList;
	}

	@Override
	public void setGroupAttributeHierarchyForImport(ComposerGroupAttribute groupAttribute, ComposerMapping composerMapping) {
		Date date = new Date();
		List<ComposerAttribute> attributeList = groupAttribute.getAttributeList();
		if(attributeList != null && !attributeList.isEmpty()){
			logger.debug("Found " + attributeList.size()  + "  attributes for group attribute " + groupAttribute.getName());
			for(ComposerAttribute attribute: attributeList){
				attribute.setId(0);
				attribute.setCreatedByStaffId(groupAttribute.getCreatedByStaffId());
				attribute.setCreatedDate(date);
				attribute.setLastUpdatedDate(date);
				attribute.setLastUpdatedByStaffId(groupAttribute.getCreatedByStaffId());
				attribute.setMyComposer(composerMapping); 
				attribute.setComposerGroupAttribute(groupAttribute);
			}
			groupAttribute.setAttributeList(attributeList); 

		}else{
			logger.debug("No attribute found for group attribute" + groupAttribute.getName());
		}
		
		List<ComposerGroupAttribute> groupAttributeList = groupAttribute.getGroupAttributeList();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.debug("Found " + groupAttributeList.size()  + " group attributes for group attribute " + groupAttribute.getName());
			for(ComposerGroupAttribute groupAttributeChild : groupAttributeList){
				groupAttributeChild.setId(0);
				groupAttributeChild.setCreatedByStaffId(groupAttribute.getCreatedByStaffId());
				groupAttributeChild.setCreatedDate(date);
				groupAttributeChild.setLastUpdatedDate(date);
				groupAttributeChild.setLastUpdatedByStaffId(groupAttribute.getCreatedByStaffId());
				groupAttributeChild.setMyComposer(composerMapping);
				groupAttributeChild.setBaseGroup(groupAttribute);
				setGroupAttributeHierarchyForImport(groupAttributeChild,composerMapping);   
			}
			groupAttribute.setGroupAttributeList(groupAttributeList); 

		}else{
			logger.debug("No group attribute found for group attribute " + groupAttribute.getName());
		}
		
	}
	
	@Override
	public List<ComposerGroupAttribute> importComposerGroupAttributesForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		Date date = new Date();
		logger.debug("import : going to add/update composer group attributes of composer : "+dbComposerMapping.getName());
		List<ComposerGroupAttribute> dbGroupAttributeList = dbComposerMapping.getGroupAttributeList();
		List<ComposerGroupAttribute> exportedGroupAttributeList = exportedComposerMapping.getGroupAttributeList();
		Iterator<ComposerGroupAttribute> itrGrpExp = exportedGroupAttributeList.iterator();
		while(itrGrpExp.hasNext()){
			ComposerGroupAttribute composerGrpAttr = itrGrpExp.next();
			if(composerGrpAttr.isAssociatedByGroup() || composerGrpAttr.getStatus().equals(StateEnum.DELETED)){
				itrGrpExp.remove();
			}
		}
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			for(ComposerGroupAttribute composerGroupAttribute : exportedGroupAttributeList){
				composerGroupAttribute.setId(0);
				composerGroupAttribute.setCreatedDate(date);
				composerGroupAttribute.setLastUpdatedDate(date);
				composerGroupAttribute.setMyComposer(dbComposerMapping);
				setGroupAttributeHierarchyForImport(composerGroupAttribute, dbComposerMapping);
			}
			int exportedlength = exportedGroupAttributeList.size();
			for(int i = exportedlength-1; i >= 0; i--) {
				ComposerGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null && !exportedGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(!CollectionUtils.isEmpty(dbGroupAttributeList)) {
						int dblength = dbGroupAttributeList.size();
						for(int j = dblength-1; j >= 0; j--) {
							ComposerGroupAttribute dbGroupAttribute = dbGroupAttributeList.get(j);
							if(dbGroupAttribute != null && !(dbGroupAttribute.isAssociatedByGroup() || dbGroupAttribute.getStatus().equals(StateEnum.DELETED))) {
								if(exportedGroupAttribute.getName().equalsIgnoreCase(dbGroupAttribute.getName())){
									updateGroupAttributeForImport(dbGroupAttribute, exportedGroupAttribute, dbComposerMapping);
									exportedGroupAttributeList.remove(exportedGroupAttribute);
								}
							}
						}
					}
				}
			}
		}
		
		checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbComposerMapping, null);
		dbGroupAttributeList.addAll(exportedGroupAttributeList);
		return dbGroupAttributeList;
	}

	private void updateGroupAttributeForImport(ComposerGroupAttribute dbGroupAttributeInput, ComposerGroupAttribute exportedGroupAttributeInput, ComposerMapping dbComposerMapping) {
		
		List<ComposerAttribute> dbAttributeList = dbGroupAttributeInput.getAttributeList();
		List<ComposerAttribute> exportedAttributeList = exportedGroupAttributeInput.getAttributeList();
		List<ComposerGroupAttribute> dbGroupAttributeList = dbGroupAttributeInput.getGroupAttributeList();
		List<ComposerGroupAttribute> exportedGroupAttributeList = exportedGroupAttributeInput.getGroupAttributeList();
		
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute exportedAttribute = exportedAttributeList.get(i);
				 if(exportedAttribute instanceof RoamingComposerAttribute) {
					//check with attribute list under this group attribute
					RoamingComposerAttribute dbRoamingComposerAttribute = composerAttributeService.getRoamingComposerAttributeFromList(dbAttributeList, ((RoamingComposerAttribute) exportedAttribute).getChildAttributes(), exportedAttribute.getDestinationField(), exportedAttribute.getUnifiedField(),true);
					if(dbRoamingComposerAttribute != null){
						logger.debug("going to update roaming composer attribute for import : "+dbRoamingComposerAttribute);
						composerAttributeService.updateComposerAttribute(dbRoamingComposerAttribute, exportedAttribute);
						dbAttributeList.add(dbRoamingComposerAttribute);
					} else {
						logger.debug("going to add roaming composer attribute for import : "+exportedAttribute);
						composerAttributeService.saveComposerAttribute(exportedAttribute, dbComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
						exportedAttribute.setComposerGroupAttribute(dbGroupAttributeInput);
						dbAttributeList.add(exportedAttribute);
					}
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			int exportedlength = exportedGroupAttributeList.size();
			for(int i = exportedlength-1; i >= 0; i--) {
				ComposerGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null && !exportedGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(!CollectionUtils.isEmpty(dbGroupAttributeList)) {
						int dblength = dbGroupAttributeList.size();
						for(int j = dblength-1; j >= 0; j--) {
							ComposerGroupAttribute dbGroupAttribute = dbGroupAttributeList.get(j);
							if(dbGroupAttribute != null && !dbGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
								if(exportedGroupAttribute.getName().equalsIgnoreCase(dbGroupAttribute.getName())){
									updateGroupAttributeForImport(dbGroupAttribute, exportedGroupAttribute, dbComposerMapping);
									exportedGroupAttributeList.remove(exportedGroupAttribute);
								}
							}
						}
					}
				}
			}
		}
		
		checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbComposerMapping, dbGroupAttributeInput);
		dbGroupAttributeList.addAll(exportedGroupAttributeList);
	}

	public void checkGroupAttributeAndUpdateForImport(List<ComposerGroupAttribute> exportedGroupAttributeListInput, ComposerMapping dbComposerMapping, ComposerGroupAttribute baseGroupAttribute) {
		
		List<ComposerGroupAttribute> dbGroupAttributeList = dbComposerMapping.getGroupAttributeList();
		
		for(ComposerGroupAttribute composerGroupAttribute : exportedGroupAttributeListInput){
			composerGroupAttribute.setBaseGroup(baseGroupAttribute);
			for(ComposerGroupAttribute dbGroupAttribute : dbGroupAttributeList){
				if(!dbGroupAttribute.getStatus().equals(StateEnum.DELETED) && dbGroupAttribute.getName().equalsIgnoreCase(composerGroupAttribute.getName())){
					composerGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,composerGroupAttribute.getName()));
				}
			}
			List<ComposerGroupAttribute> exportedGroupAttributeList = composerGroupAttribute.getGroupAttributeList();
			if(exportedGroupAttributeList != null){
				checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbComposerMapping, composerGroupAttribute);
			}
		}
	}

}
