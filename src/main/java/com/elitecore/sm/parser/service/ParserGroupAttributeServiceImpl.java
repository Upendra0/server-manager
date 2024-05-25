package com.elitecore.sm.parser.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.dao.ParserAttributeDao;
import com.elitecore.sm.parser.dao.ParserGroupAttributeDao;
import com.elitecore.sm.parser.dao.ParserPageConfigurationDao;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.ParserPageConfiguration;
import com.elitecore.sm.util.EliteUtils;
@Service
public class ParserGroupAttributeServiceImpl implements ParserGroupAttributeService {
	
	private  Logger logger= Logger.getLogger(ParserGroupAttributeServiceImpl.class);
	
	@Autowired
	ParserMappingService ParserMappingService;
	
	@Autowired
	private ParserAttributeService parserAttributeService;
	
	@Autowired
	ParserGroupAttributeDao parserGroupAttributeDao;
	@Autowired
	ParserPageConfigurationDao parserPageConfigurationDao;
	 
	@Autowired 
	ParserAttributeDao parserAttributeDao;
	
	@Autowired
	private ParserMappingService parserMappingService;

	@Override
	@Transactional
	public ResponseObject createGroupAttribute(ParserGroupAttribute parserGroupAttribute, String groupAttrLists,
			String attrLists, int mappingId, String plugInType, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		parserGroupAttribute.setName(parserGroupAttribute.getName());
		parserGroupAttribute.setCreatedByStaffId(staffId);
		parserGroupAttribute.setLastUpdatedByStaffId(staffId);
		parserGroupAttribute.setCreatedDate(new Date());
		parserGroupAttribute.setLastUpdatedDate(new Date());
		if(isGroupAttributeUniqueForUpdate(parserGroupAttribute.getId(), parserGroupAttribute.getName(), mappingId)){
			   responseObject=ParserMappingService.getMappingDetailsById(mappingId, plugInType);
			   if(responseObject.isSuccess()){
				   parserGroupAttribute.setParserMapping((ParserMapping)responseObject.getObject());
				   parserGroupAttributeDao.save(parserGroupAttribute);
				   
				   List<ParserGroupAttribute> groupAttributeList=getGroupAttributeByGroupId(groupAttrLists, mappingId);
				   if(groupAttributeList !=null && ! groupAttributeList.isEmpty()){
					   
					   for(ParserGroupAttribute attribute:groupAttributeList){
						   attribute.setBaseGroup(parserGroupAttribute);
						   parserGroupAttributeDao.merge(attribute);
						   
					   }
				   }
				   List <ParserAttribute> attributeList =getParserAttributeByAttributeId(attrLists,mappingId);
				   if( attributeList != null && ! attributeList.isEmpty()){
					   for(ParserAttribute attribute:attributeList){
						   attribute.setParserGroupAttribute(parserGroupAttribute); 
						   parserAttributeDao.merge(attribute);
					   }
				   }
				   if(parserGroupAttribute.getId()>0){
					   responseObject.setSuccess(true);
					   responseObject.setObject(parserGroupAttribute);
					   responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				   }else{
					   responseObject.setSuccess(false);
					   responseObject.setObject(null);
					   responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_FAIL);
				   }
			   }else{
				   responseObject.setSuccess(false);
				   responseObject.setObject(null);
				   responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_FAIL);
				   
			   }
		}else{
				logger.info("duplicate attribute source field found:" + parserGroupAttribute.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.PARSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject updateGroupAttribute(ParserGroupAttribute parserGroupAttribute, String groupAttrLists,
			String attrLists, int mappingId, String plugInType, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		parserGroupAttribute.setName(parserGroupAttribute.getName());
		parserGroupAttribute.setCreatedByStaffId(staffId);
		parserGroupAttribute.setLastUpdatedByStaffId(staffId);
		parserGroupAttribute.setCreatedDate(new Date());
		parserGroupAttribute.setLastUpdatedDate(new Date());
		if(isGroupAttributeUniqueForUpdate(parserGroupAttribute.getId(), parserGroupAttribute.getName(), mappingId)){
			if(parserGroupAttribute.getId()>0){
				ParserGroupAttribute dbGroupAttribute = parserGroupAttributeDao.findByPrimaryKey(ParserGroupAttribute.class, parserGroupAttribute.getId());
              if(dbGroupAttribute !=null){
            	  dbGroupAttribute.setName(parserGroupAttribute.getName());
            	  dbGroupAttribute.setLastUpdatedDate(new Date());
            	  dbGroupAttribute.setLastUpdatedByStaffId(staffId);
            	  
            	  parserGroupAttributeDao.merge(dbGroupAttribute);
            	  List<ParserGroupAttribute> groupAttributeListToDetach=parserGroupAttributeDao.getGroupAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
            	  if(groupAttributeListToDetach !=null && !groupAttributeListToDetach.isEmpty()){
            		  for(ParserGroupAttribute groupAtrr:groupAttributeListToDetach){
            			  groupAtrr.setBaseGroup(null);
            			  parserGroupAttributeDao.merge(groupAtrr);
            		  }
            	  }
            	  List<ParserGroupAttribute> groupAttributeListToAttach=getGroupAttributeByGroupId(groupAttrLists, mappingId);
            	  if(groupAttributeListToAttach!=null&&!groupAttributeListToAttach.isEmpty()){
            		  for(ParserGroupAttribute groupAttr:groupAttributeListToAttach ){
            			  groupAttr.setBaseGroup(dbGroupAttribute);
            			  parserGroupAttributeDao.merge(groupAttr);

            		  }
            		  
            	  }
            	  List<ParserAttribute> attributeListToDetach =parserAttributeDao.getParserAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
            	  if(attributeListToDetach!=null&& !attributeListToDetach.isEmpty()){
            		  for(ParserAttribute attribute:attributeListToDetach){
            			  attribute.setParserGroupAttribute(null);
            			  parserAttributeDao.merge(attribute);
            		  }
            	  }
            	  List<ParserAttribute> attributeListToAttach=getParserAttributeByAttributeId(attrLists,mappingId);
            	  if(attributeListToAttach!=null&&!attributeListToAttach.isEmpty()){
            		  for(ParserAttribute attribute:attributeListToAttach){
            			  attribute.setParserGroupAttribute(dbGroupAttribute);
            			  parserAttributeDao.merge(attribute);

            		  }
            	  }
            	    responseObject.setSuccess(true);
					responseObject.setObject(parserGroupAttribute);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_SUCCESS);
              }else{
            	    responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_FAIL);
              }
				
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.COMPOSER_ATTRIBUTE_UPDATE_FAIL);
			}
			
		}else{
			logger.info("duplicate group attribute found:" + parserGroupAttribute.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_DUPLICATE_GROUP_ATTRIBUTE_FOUND);
		}
		
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	public boolean isGroupAttributeUniqueForUpdate(int groupAttributeId, String groupAttributeName, int mappingId) {

		ParserGroupAttribute parserGroupAttribute = parserGroupAttributeDao.checkUniqueGroupAttributeNameForUpdate(mappingId, groupAttributeName);

		boolean isUnique;

		if (parserGroupAttribute != null) {
			// If ID is same , then it is same attribute object
			if (groupAttributeId == parserGroupAttribute.getId()) {
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
	
	private List<ParserGroupAttribute> getGroupAttributeByGroupId(String groupAttrLists, int mappingId){
		
		List<ParserGroupAttribute> groupAttributeList=new ArrayList<>();
		if(!StringUtils.isEmpty(groupAttrLists)){
			String[] groupAttributeIdList=groupAttrLists.split(",");
			
			for(int i=0;i<groupAttributeIdList.length;i++){
				ParserGroupAttribute parserGroupAttribute=parserGroupAttributeDao.getGroupAttributeByGroupId(Integer.parseInt(groupAttributeIdList[i]), mappingId);
				groupAttributeList.add(parserGroupAttribute);
			}
		}
		return groupAttributeList;
	}
	@Transactional
	public List<ParserAttribute> getParserAttributeByAttributeId(String attrList,int mappingId){
		
		List<ParserAttribute> parserAttributesList=new ArrayList<>();
		if(!StringUtils.isEmpty(attrList)){
			String[] parsersAttributeIdList=attrList.split(",");
			for(int i=0;i<parsersAttributeIdList.length;i++){
				ParserAttribute parserAttribute=parserAttributeDao.getParserAttributeByAttributeId(Integer.parseInt(parsersAttributeIdList[i]), mappingId);
				parserAttributesList.add(parserAttribute);
			}
		}
		return parserAttributesList;
	}

	@Override
	@Transactional
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByMappingId(int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> groupAttributeList=parserGroupAttributeDao.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);
        if(groupAttributeList !=null && ! groupAttributeList.isEmpty()){
        	responseObject.setObject(groupAttributeList);
        	responseObject.setSuccess(true);
        }else{
        	responseObject.setObject(groupAttributeList);
        	responseObject.setSuccess(false);
        }

		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getAttributeListEligibleToAttachWithGroup(int mappingId) {
		logger.debug("Fetching all attribute list eligible to Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList=parserGroupAttributeDao.getAttributeListEligibleToAttachWithGroup(mappingId);
		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getAttachedGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
        List<ParserGroupAttribute> groupAttributeList=parserGroupAttributeDao.getAttachedGroupAttributeListByGroupId(groupId,mappingId);
        if (groupAttributeList != null && !groupAttributeList.isEmpty()) {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getGroupAttributeListEligibleToAttachWithGroupByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list Attached with group by mapping id : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> eligibleGroupAttributeList=parserGroupAttributeDao.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);
		List<ParserGroupAttribute> unEligibleGroupAttributeList=getParentGroupAttributesByGroupId(groupId,mappingId);
		unEligibleGroupAttributeList.add(parserGroupAttributeDao.getGroupAttributeByGroupId(groupId,mappingId));
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
	
	private List<ParserGroupAttribute> getParentGroupAttributesByGroupId(int groupId , int mappingId){
		
		List<ParserGroupAttribute> parentGroupAttributeList = new ArrayList<>();
		
		ParserGroupAttribute parentGroupAttribute =parserGroupAttributeDao.getGroupAttributeByGroupId(groupId, mappingId).getBaseGroup();
		
		if(parentGroupAttribute !=null){
			parentGroupAttributeList.add(parentGroupAttribute);
			parentGroupAttributeList.addAll(getParentGroupAttributesByGroupId(parentGroupAttribute.getId(),mappingId));
		}
		return parentGroupAttributeList;
	}

	@Override
	@Transactional
	public ResponseObject getAttachedAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all attribute list Attached with group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList =parserGroupAttributeDao.getAttachedAttributeListByGroupId(groupId,mappingId);
		if(attributeList !=null && ! attributeList.isEmpty()){
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		}else{
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	@Override
	@Transactional
	public ResponseObject getPageConfigListGroupId(int groupId) {
		ResponseObject responseObject = new ResponseObject();
		List<ParserPageConfiguration> pageConfigList =parserGroupAttributeDao.getParserPageConfigurationByGroupId(groupId);
		if(pageConfigList !=null && ! pageConfigList.isEmpty()){
			responseObject.setObject(pageConfigList);
			responseObject.setSuccess(true);
		}else{
			responseObject.setObject(pageConfigList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getGroupAttributeListByGroupId(int groupId, int mappingId) {
		logger.debug("Fetching all group attribute list by group id : " + groupId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> groupAttributeList=parserGroupAttributeDao.getGroupAttributeListByGroupId(groupId, mappingId);
		if (groupAttributeList != null && !groupAttributeList.isEmpty()) {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(groupAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getGroupAttributeListByMappingId(int mappingId) {
		logger.debug("Fetching all group attribute for TAP Parser : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserGroupAttribute> gruopAttributeList =parserGroupAttributeDao.getGroupAttributeListByMappingId(mappingId);
		if (gruopAttributeList != null && !gruopAttributeList.isEmpty()) {
			responseObject.setObject(gruopAttributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(gruopAttributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject deleteGroupAttributes(String groupAttributeIds, int staffId, int mappingId) {
		ResponseObject responseObject = new ResponseObject();
		if(!StringUtils.isEmpty(groupAttributeIds)){
			String [] GroupAttributeIdList = groupAttributeIds.split(",");
			for (int i = 0; i < GroupAttributeIdList.length; i++) {
				responseObject = deleteGroupAttribute(Integer.parseInt(GroupAttributeIdList[i]),staffId,mappingId);
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
	
	public ResponseObject deleteGroupAttribute(int groupAttributeId, int staffId, int mappingId){
		
		ResponseObject responseObject = new ResponseObject();
		
		if(groupAttributeId > 0){
			ParserGroupAttribute parserGroupAttribute  = parserGroupAttributeDao.findByPrimaryKey(ParserGroupAttribute.class, groupAttributeId);
		
			if(parserGroupAttribute!=null){
				List<ParserAttribute> parserAttributeLists = parserAttributeDao.getParserAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
				if(parserAttributeLists != null && !parserAttributeLists.isEmpty()){
					for(ParserAttribute parserAttribute : parserAttributeLists){
						parserAttribute.setParserGroupAttribute(null);
						parserAttributeDao.merge(parserAttribute);
					}
				}
				List<ParserGroupAttribute> childParserGroupAttributeLists = parserGroupAttributeDao.getGroupAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
				if(childParserGroupAttributeLists != null && !childParserGroupAttributeLists.isEmpty()){
					for(ParserGroupAttribute childParserGroupAttribute : childParserGroupAttributeLists){
						childParserGroupAttribute.setBaseGroup(null);
					}
				}
				parserGroupAttribute.setBaseGroup(null);
				parserGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,parserGroupAttribute.getName()));
				parserGroupAttribute.setLastUpdatedDate(new Date());
				parserGroupAttribute.setLastUpdatedByStaffId(staffId);
				parserGroupAttribute.setStatus(StateEnum.DELETED);
				parserGroupAttribute.setParserMapping(null);
				
				parserGroupAttributeDao.merge(parserGroupAttribute);
				responseObject.setSuccess(true);
				responseObject.setObject(parserGroupAttribute);
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

	@Override
	@Transactional
	public ResponseObject deleteGroupAttributeWithHierarchy(int groupAttributeId,  int staffId, int mappingId){
		ResponseObject responseObject = new ResponseObject();
		if(groupAttributeId > 0){
			ParserGroupAttribute parserGroupAttribute  = parserGroupAttributeDao.findByPrimaryKey(ParserGroupAttribute.class, groupAttributeId);
			if(parserGroupAttribute!=null){
				List<ParserAttribute> parserAttributeLists = parserAttributeDao.getParserAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
				if(parserAttributeLists != null && !parserAttributeLists.isEmpty()){
					for(ParserAttribute parserAttribute : parserAttributeLists){
						parserAttribute.setParserGroupAttribute(null);
						parserAttribute.setStatus(StateEnum.DELETED);
						parserAttribute.setAssociatedByGroup(false);
						parserAttributeDao.merge(parserAttribute);
					}
				}
				List<ParserGroupAttribute> childParserGroupAttributeLists = parserGroupAttributeDao.getGroupAttributeListByGroupId(parserGroupAttribute.getId(), mappingId);
				if(childParserGroupAttributeLists != null && !childParserGroupAttributeLists.isEmpty()){
					for(ParserGroupAttribute childParserGroupAttribute : childParserGroupAttributeLists){
						childParserGroupAttribute.setBaseGroup(null);
					}
				}
				List<ParserPageConfiguration> pageConfigurationList = parserGroupAttributeDao.getParserPageConfigurationByGroupId(parserGroupAttribute.getId());
				if(pageConfigurationList != null && !pageConfigurationList.isEmpty()){
					for(ParserPageConfiguration pageConfiguration : pageConfigurationList){
						pageConfiguration.setParserGroupAttribute(null);
						pageConfiguration.setStatus(StateEnum.DELETED);
					}
				}
				parserGroupAttribute.setBaseGroup(null);
				parserGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,parserGroupAttribute.getName()));
				parserGroupAttribute.setLastUpdatedDate(new Date());
				parserGroupAttribute.setLastUpdatedByStaffId(staffId);
				parserGroupAttribute.setStatus(StateEnum.DELETED);
				parserGroupAttribute.setParserMapping(null);
				parserGroupAttributeDao.merge(parserGroupAttribute);
				responseObject.setSuccess(true);
				responseObject.setObject(parserGroupAttribute);
				responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DELETE_SUCCESS);
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DELETE_FAIL);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DELETE_FAIL);
		}
		return responseObject;
	}
	@Override
	@Transactional
	public ResponseObject deletePageConfiguration(String pageIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if(!StringUtils.isEmpty(pageIds)){
			String [] pageIdList = pageIds.split(",");
			for (int i = 0; i < pageIdList.length; i++) {
				responseObject = deletePage(Integer.parseInt(pageIdList[i]),staffId);
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
	public ResponseObject deletePage(int pageId, int staffId){
		ResponseObject responseObject = new ResponseObject();
		if(pageId > 0){
			ParserPageConfiguration pageConfig = parserPageConfigurationDao.findByPrimaryKey(ParserPageConfiguration.class, pageId);
			if(pageConfig!=null){
				pageConfig.setLastUpdatedDate(new Date());
				pageConfig.setLastUpdatedByStaffId(staffId);
				pageConfig.setStatus(StateEnum.DELETED);
				pageConfig.setParserGroupAttribute(null);
				parserPageConfigurationDao.merge(pageConfig);
				responseObject.setSuccess(true);
				responseObject.setObject(pageConfig);
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
	@Override
	public void setGroupAttributeHierarchyForImport(ParserGroupAttribute groupAttribute, ParserMapping parserMapping) {
		Date date = new Date();
		List<ParserAttribute> attributeList = groupAttribute.getAttributeList();
		if(attributeList != null && !attributeList.isEmpty()){
			logger.debug("Found " + attributeList.size()  + "  attributes for group attribute " + groupAttribute.getName());
			for(ParserAttribute attribute: attributeList){
				attribute.setId(0);
				attribute.setCreatedByStaffId(groupAttribute.getCreatedByStaffId());
				attribute.setCreatedDate(date);
				attribute.setLastUpdatedDate(date);
				attribute.setLastUpdatedByStaffId(groupAttribute.getCreatedByStaffId());
				attribute.setParserMapping(parserMapping); 
				attribute.setParserGroupAttribute(groupAttribute);
			}
			groupAttribute.setAttributeList(attributeList); 

		}else{
			logger.debug("No attribute found for group attribute" + groupAttribute.getName());
		}
		
		List<ParserPageConfiguration> pageList = groupAttribute.getParserPageConfigurationList();
		if(pageList != null && !pageList.isEmpty()){
			logger.debug("Found " + pageList.size()  + "  page config for group attribute " + groupAttribute.getName());
			for(ParserPageConfiguration page: pageList){
				page.setId(0);
				page.setCreatedByStaffId(groupAttribute.getCreatedByStaffId());
				page.setCreatedDate(date);
				page.setLastUpdatedDate(date);
				page.setLastUpdatedByStaffId(groupAttribute.getCreatedByStaffId());
				page.setParserGroupAttribute(groupAttribute);
			}
			groupAttribute.setParserPageConfigurationList(pageList); 

		}
		
		List<ParserGroupAttribute> groupAttributeList = groupAttribute.getGroupAttributeList();
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.debug("Found " + groupAttributeList.size()  + " group attributes for group attribute " + groupAttribute.getName());
			for(ParserGroupAttribute groupAttributeChild : groupAttributeList){
				groupAttributeChild.setId(0);
				groupAttributeChild.setCreatedByStaffId(groupAttribute.getCreatedByStaffId());
				groupAttributeChild.setCreatedDate(date);
				groupAttributeChild.setLastUpdatedDate(date);
				groupAttributeChild.setLastUpdatedByStaffId(groupAttribute.getCreatedByStaffId());
				groupAttributeChild.setParserMapping(parserMapping);
				groupAttributeChild.setBaseGroup(groupAttribute);
				setGroupAttributeHierarchyForImport(groupAttributeChild,parserMapping);   
			}
			groupAttribute.setGroupAttributeList(groupAttributeList); 

		}else{
			logger.debug("No group attribute found for group attribute " + groupAttribute.getName());
		}
	}
	
	@Override
	public List<ParserGroupAttribute> importParserGroupAttributesForUpdateMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
		Date date = new Date();
		logger.debug("import : going to add/update parser group attributes of composer : "+dbParserMapping.getName());
		List<ParserGroupAttribute> dbGroupAttributeList = dbParserMapping.getGroupAttributeList();
		List<ParserGroupAttribute> exportedGroupAttributeList = exportedParserMapping.getGroupAttributeList();
		Iterator<ParserGroupAttribute> itrGrpExp = exportedGroupAttributeList.iterator();
		while(itrGrpExp.hasNext()){
			ParserGroupAttribute parserGrpAttr = itrGrpExp.next();
			if(parserGrpAttr.isAssociatedByGroup() || parserGrpAttr.getStatus().equals(StateEnum.DELETED)){
				itrGrpExp.remove();
			}
		}
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			for(ParserGroupAttribute parserGroupAttribute : exportedGroupAttributeList){
				parserGroupAttribute.setId(0);
				parserGroupAttribute.setCreatedDate(date);
				parserGroupAttribute.setLastUpdatedDate(date);
				parserGroupAttribute.setParserMapping(dbParserMapping);
				setGroupAttributeHierarchyForImport(parserGroupAttribute, dbParserMapping);
			}
			int exportedlength = exportedGroupAttributeList.size();
			for(int i = exportedlength-1; i >= 0; i--) {
				ParserGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null && !exportedGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(!CollectionUtils.isEmpty(dbGroupAttributeList)) {
						int dblength = dbGroupAttributeList.size();
						for(int j = dblength-1; j >= 0; j--) {
							ParserGroupAttribute dbGroupAttribute = dbGroupAttributeList.get(j);
							if(dbGroupAttribute != null && !(dbGroupAttribute.isAssociatedByGroup() || dbGroupAttribute.getStatus().equals(StateEnum.DELETED))) {
								if(exportedGroupAttribute.getName().equalsIgnoreCase(dbGroupAttribute.getName())){
									updateGroupAttributeForImport(dbGroupAttribute, exportedGroupAttribute, dbParserMapping);
									exportedGroupAttributeList.remove(exportedGroupAttribute);
								}
							}
						}
					}
				}
			}
		}
		
		checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbParserMapping, null);
		dbGroupAttributeList.addAll(exportedGroupAttributeList);
		return dbGroupAttributeList;
	}
	
	@Override
	public List<ParserGroupAttribute> importParserGroupAttributesForAddMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping) {
		Date date = new Date();
		logger.debug("import : going to add/update parser group attributes of composer : "+dbParserMapping.getName());
		List<ParserGroupAttribute> dbGroupAttributeList = dbParserMapping.getGroupAttributeList();
		List<ParserGroupAttribute> exportedGroupAttributeList = exportedParserMapping.getGroupAttributeList();
		Iterator<ParserGroupAttribute> itrGrpExp = exportedGroupAttributeList.iterator();
		while(itrGrpExp.hasNext()){
			ParserGroupAttribute parserGrpAttr = itrGrpExp.next();
			if(parserGrpAttr.isAssociatedByGroup() || parserGrpAttr.getStatus().equals(StateEnum.DELETED)){
				itrGrpExp.remove();
			}
		}
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			for(ParserGroupAttribute parserGroupAttribute : exportedGroupAttributeList){
				parserGroupAttribute.setId(0);
				parserGroupAttribute.setCreatedDate(date);
				parserGroupAttribute.setLastUpdatedDate(date);
				parserGroupAttribute.setParserMapping(dbParserMapping);
				setGroupAttributeHierarchyForImport(parserGroupAttribute, dbParserMapping);
			}
			int exportedlength = exportedGroupAttributeList.size();
			for(int i = exportedlength-1; i >= 0; i--) {
				ParserGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null && !exportedGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(!CollectionUtils.isEmpty(dbGroupAttributeList)) {
						int dblength = dbGroupAttributeList.size();
						for(int j = dblength-1; j >= 0; j--) {
							ParserGroupAttribute dbGroupAttribute = dbGroupAttributeList.get(j);
							if(dbGroupAttribute != null && !(dbGroupAttribute.isAssociatedByGroup() || dbGroupAttribute.getStatus().equals(StateEnum.DELETED))) {
								if(exportedGroupAttribute.getName().equalsIgnoreCase(dbGroupAttribute.getName())){
									updateGroupAttributeForImport(dbGroupAttribute, exportedGroupAttribute, dbParserMapping);
									exportedGroupAttributeList.remove(exportedGroupAttribute);
								}
							}
						}
					}
				}
			}
		}
		
		checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbParserMapping, null);
		dbGroupAttributeList.addAll(exportedGroupAttributeList);
		return dbGroupAttributeList;
	}

	private void updateGroupAttributeForImport(ParserGroupAttribute dbGroupAttributeInput, ParserGroupAttribute exportedGroupAttributeInput, ParserMapping dbParserMapping) {
		
		List<ParserAttribute> dbAttributeList = dbGroupAttributeInput.getAttributeList();
		List<ParserAttribute> exportedAttributeList = exportedGroupAttributeInput.getAttributeList();
		List<ParserGroupAttribute> dbGroupAttributeList = dbGroupAttributeInput.getGroupAttributeList();
		List<ParserGroupAttribute> exportedGroupAttributeList = exportedGroupAttributeInput.getGroupAttributeList();
		
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute exportedParserAttribute = exportedAttributeList.get(i);
				//check with attribute list under this group attribute
				ParserAttribute dbParserAttribute = ParserMappingService.getParserAttributeFromList(dbAttributeList, exportedParserAttribute.getSourceField(), exportedParserAttribute.getUnifiedField(), true);
				if(dbParserAttribute != null){
					logger.debug("going to update roaming composer attribute for import : "+dbParserAttribute);
					parserAttributeService.updateParserAttributeForImport(dbParserAttribute, exportedParserAttribute);
					dbAttributeList.add(dbParserAttribute);
				} else {
					logger.debug("going to add roaming composer attribute for import : "+exportedParserAttribute);
					parserAttributeService.saveParserAttributeForImport(exportedParserAttribute, dbParserMapping, null);
					exportedParserAttribute.setParserGroupAttribute(dbGroupAttributeInput);
					dbAttributeList.add(exportedParserAttribute);
				}
			}
		}
		
		if(!CollectionUtils.isEmpty(exportedGroupAttributeList)) {
			int exportedlength = exportedGroupAttributeList.size();
			for(int i = exportedlength-1; i >= 0; i--) {
				ParserGroupAttribute exportedGroupAttribute = exportedGroupAttributeList.get(i);
				if(exportedGroupAttribute != null && !exportedGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(!CollectionUtils.isEmpty(dbGroupAttributeList)) {
						int dblength = dbGroupAttributeList.size();
						for(int j = dblength-1; j >= 0; j--) {
							ParserGroupAttribute dbGroupAttribute = dbGroupAttributeList.get(j);
							if(dbGroupAttribute != null && !dbGroupAttribute.getStatus().equals(StateEnum.DELETED)) {
								if(exportedGroupAttribute.getName().equalsIgnoreCase(dbGroupAttribute.getName())){
									updateGroupAttributeForImport(dbGroupAttribute, exportedGroupAttribute, dbParserMapping);
									exportedGroupAttributeList.remove(exportedGroupAttribute);
								}
							}
						}
					}
				}
			}
		}
		
		checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbParserMapping, dbGroupAttributeInput);
		dbGroupAttributeList.addAll(exportedGroupAttributeList);
	}

	public void checkGroupAttributeAndUpdateForImport(List<ParserGroupAttribute> exportedGroupAttributeListInput, ParserMapping dbParserMapping, ParserGroupAttribute baseGroupAttribute) {
		
		List<ParserGroupAttribute> dbGroupAttributeList = dbParserMapping.getGroupAttributeList();
		
		for(ParserGroupAttribute parserGroupAttribute : exportedGroupAttributeListInput){
			parserGroupAttribute.setBaseGroup(baseGroupAttribute);
			for(ParserGroupAttribute dbGroupAttribute : dbGroupAttributeList){
				if(!dbGroupAttribute.getStatus().equals(StateEnum.DELETED) && dbGroupAttribute.getName().equalsIgnoreCase(parserGroupAttribute.getName())){
					parserGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,parserGroupAttribute.getName()));
				}
			}
			List<ParserGroupAttribute> exportedGroupAttributeList = parserGroupAttribute.getGroupAttributeList();
			if(exportedGroupAttributeList != null){
				checkGroupAttributeAndUpdateForImport(exportedGroupAttributeList, dbParserMapping, parserGroupAttribute);
			}
		}
	}
	
	/**
	 * Add Parser Group Attribute detail
	 */
	@Transactional
	@Override
	public ResponseObject createParserGroupAttributes(ParserGroupAttribute parserGroupAttributeExp, int mappingId, String pluginType, int staffId) {
ResponseObject responseObject = new ResponseObject();		
		
		parserGroupAttributeExp.setId(0);
		parserGroupAttributeExp.setCreatedByStaffId(staffId);
		parserGroupAttributeExp.setLastUpdatedByStaffId(staffId);
		parserGroupAttributeExp.setCreatedDate(new Date());
		parserGroupAttributeExp.setLastUpdatedDate(new Date());
		
		if(EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)){
			parserGroupAttributeExp.setParserPageConfigurationList(null);	
		}
		
		parserGroupAttributeExp.setGroupAttributeList(null);
		parserGroupAttributeExp.setAttributeList(null);

		responseObject = parserMappingService.getMappingDetailsById(mappingId, pluginType);

		if (responseObject.isSuccess()) {
			ParserMapping parserMapping = (ParserMapping) responseObject.getObject();
			parserGroupAttributeExp.setParserMapping(parserMapping);
			
			parserGroupAttributeDao.save(parserGroupAttributeExp);

			if (parserGroupAttributeExp.getId() > 0) {
				responseObject.setSuccess(true);
				responseObject.setObject(parserGroupAttributeExp);
				responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DETAIL_ADD_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DETAIL_ADD_FAIL);
			}

		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_DETAIL_ADD_FAIL);
		}

		return responseObject;
	}

	/**
	 * Add Parser Group Attribute detail
	 */
	@Transactional
	@Override
	public ResponseObject createParserPageConfiguration(ParserPageConfiguration parserPage, int staffId, int groupId) {
		ResponseObject responseObject = new ResponseObject();		
		parserPage.setId(0);
		parserPage.setCreatedByStaffId(staffId);
		parserPage.setLastUpdatedByStaffId(staffId);
		parserPage.setCreatedDate(new Date());
		parserPage.setLastUpdatedDate(new Date());
		parserPage.setStatus(StateEnum.ACTIVE);
		ParserGroupAttribute parserGroupDb = parserGroupAttributeDao.findByPrimaryKey(ParserGroupAttribute.class, groupId);
		if(parserGroupDb != null){
			parserPage.setParserGroupAttribute(parserGroupDb);
			parserPageConfigurationDao.save(parserPage);
			if (parserPage.getId() > 0) {
				responseObject.setSuccess(true);
				responseObject.setObject(parserPage);
				responseObject.setResponseCode(ResponseCode.PARSER_PAGE_CONFIG_ADD_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_PAGE_CONFIG_ADD_FAIL);
			}
		}else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PARSER_PAGE_CONFIG_ADD_FAIL);
		}
		return responseObject;
	}
	/**
	 * Add Parser Group Attribute detail
	 */
	@Transactional
	@Override
	public ResponseObject updateParserPageConfiguration(ParserPageConfiguration parserPage, int staffId, int groupId) {
		ResponseObject responseObject = new ResponseObject();		
		ParserPageConfiguration parserPgConfigDb = parserPageConfigurationDao.findByPrimaryKey(ParserPageConfiguration.class, parserPage.getId());
		if(parserPgConfigDb != null){
			parserPgConfigDb.setLastUpdatedByStaffId(staffId);
			parserPgConfigDb.setLastUpdatedDate(new Date());
			parserPgConfigDb.setPageSize(parserPage.getPageSize());
			parserPgConfigDb.setTableLocation(parserPage.getTableLocation());
			parserPgConfigDb.setTableCols(parserPage.getTableCols());
			parserPgConfigDb.setPageNumber(parserPage.getPageNumber());
			parserPgConfigDb.setExtractionMethod(parserPage.getExtractionMethod());
		}
		parserPageConfigurationDao.merge(parserPgConfigDb);
		if (parserPage.getId() > 0) {
			responseObject.setSuccess(true);
			responseObject.setObject(parserPgConfigDb);
			responseObject.setResponseCode(ResponseCode.PARSER_PAGE_CONFIG_UPDATE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PARSER_PAGE_CONFIG_UPDATE_FAIL);
		}
		return responseObject;
	}
	/**
	 * Update Parser Attribute
	 * 
	 * @param parserGroupAttribute
	 * @param mappingId
	 * @param pluginType
	 * @param staffId
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_GROUP_ATTRIBUTE, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ParserGroupAttribute.class, ignorePropList = "parserMapping")
	public ResponseObject updateParserGroupAttributes(ParserGroupAttribute parserGroupAttribute, int mappingId, String pluginType, int staffId,boolean isUpdateOrdering) {

		ResponseObject responseObject = new ResponseObject();

		ParserGroupAttribute parserGroupArrtibuteDB = parserGroupAttributeDao.getGroupAttributeByGroupId(parserGroupAttribute.getId(), mappingId);

		if (parserGroupArrtibuteDB != null) {
			parserGroupArrtibuteDB.setTableEndIdentifier(parserGroupAttribute.getTableEndIdentifier());
			parserGroupArrtibuteDB.setTableStartIdentifier(parserGroupAttribute.getTableStartIdentifier());
			parserGroupArrtibuteDB.setName(parserGroupAttribute.getName());
			
			if(EngineConstants.XLS_PARSING_PLUGIN.equals(pluginType)){
				parserGroupArrtibuteDB.setTableEndIdentifierCol(parserGroupAttribute.getTableEndIdentifierCol());
				parserGroupArrtibuteDB.setTableStartIdentifierCol(parserGroupAttribute.getTableStartIdentifierCol());
			}
			if(EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)){
				parserGroupArrtibuteDB.setTableEndIdentifierCol(parserGroupAttribute.getTableEndIdentifierCol());
				parserGroupArrtibuteDB.setTableStartIdentifierCol(parserGroupAttribute.getTableStartIdentifierCol());
				
				parserGroupArrtibuteDB.setTableEndIdentifierOccurence(parserGroupAttribute.getTableEndIdentifierOccurence());
				parserGroupArrtibuteDB.setTableEndIdentifierRowLocation(parserGroupAttribute.getTableEndIdentifierRowLocation());
				parserGroupArrtibuteDB.setTableRowIdentifier(parserGroupAttribute.getTableRowIdentifier());
			}else if(EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType)){
				parserGroupArrtibuteDB.setTableEndIdentifierTdNo(parserGroupAttribute.getTableEndIdentifierTdNo());
				parserGroupArrtibuteDB.setTableStartIdentifierTdNo(parserGroupAttribute.getTableStartIdentifierTdNo());	
				parserGroupArrtibuteDB.setTableRowsToIgnore(parserGroupAttribute.getTableRowsToIgnore());	
			}
			parserGroupArrtibuteDB.setLastUpdatedDate(new Date());
			parserGroupArrtibuteDB.setLastUpdatedByStaffId(staffId);

			parserGroupAttributeDao.merge(parserGroupArrtibuteDB);
			responseObject.setSuccess(true);
			responseObject.setObject(parserGroupArrtibuteDB);
			responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_UPDATE_SUCCESS);

		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.PARSER_GROUP_ATTRIBUTE_UPDATE_FAIL);
		}
			
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject getGroupAttributeByMappingIdAndGroupName(int mappingId, String groupName) {
		logger.debug("Fetching all group attribute for TAP Parser : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		ParserGroupAttribute gruopAttribute =parserGroupAttributeDao.getGroupAttributeByMappingIdAndGroupName(mappingId, groupName);
		if (gruopAttribute != null) {
			responseObject.setObject(gruopAttribute);
			responseObject.setSuccess(true);
		} else {			
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public String getSubGroupNameListByGroupId(ParserGroupAttribute groupAttribute) {
		return parserGroupAttributeDao.getSubGroupNameListByGroupId(groupAttribute);
	}

}
