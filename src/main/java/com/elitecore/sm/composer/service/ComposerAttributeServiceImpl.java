package com.elitecore.sm.composer.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.DataTypeEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.controller.ComposerAttributeFactory;
import com.elitecore.sm.composer.dao.ComposerAttributeDao;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerArgumentTypeEnum;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.DestinationFieldFormat;
import com.elitecore.sm.composer.model.EnumComposerAttributeHeader;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.model.NRTRDEComposerAttribute;
import com.elitecore.sm.composer.model.RAPComposerAttribute;
import com.elitecore.sm.composer.model.RoamingComposerAttribute;
import com.elitecore.sm.composer.model.TAPComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerAttribute;
import com.elitecore.sm.composer.validator.ComposerAttributeValidator;
import com.elitecore.sm.parser.dao.ParserAttributeDao;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.util.CSVUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.Regex;



@Service(value="composerAttributeService")
public class ComposerAttributeServiceImpl implements ComposerAttributeService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	ComposerAttributeDao composerAttributeDao;
	
	@Autowired
	ComposerAttributeValidator composerAttributeValidator;
	
	@Autowired
	@Qualifier(value = "parserAttributeDao")
	private ParserAttributeDao parserAttributeDao;
	
	/**
	 * Create Composer Attribute
	 * @param composerAttribute
	 * @param mappingId
	 * @param pluginType
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_COMPOSER_ATTRIBUTE, actionType = BaseConstants.CREATE_ACTION, currentEntity = ComposerAttribute.class ,ignorePropList= "")
	public ResponseObject createComposerAttributes(ComposerAttribute composerAttribute, int mappingId, String pluginType, int staffId,int importMode) {
		
		ResponseObject responseObject = new ResponseObject();
		composerAttribute.setSequenceNumber(composerAttribute.getSequenceNumber());
		composerAttribute.setCreatedByStaffId(staffId);
		composerAttribute.setLastUpdatedByStaffId(staffId);
		composerAttribute.setCreatedDate(new Date());
		composerAttribute.setLastUpdatedDate(new Date());
		composerAttribute.setAttributeOrder(0);
		if (isAttributeUniqueForUpdate(composerAttribute ,composerAttribute.getId(), composerAttribute.getDestinationField(), mappingId,importMode)){
		responseObject = composerMappingService.getComposerMappingDetailsById(mappingId, pluginType);
		
		if(responseObject.isSuccess()){

			composerAttribute.setMyComposer((ComposerMapping) responseObject.getObject());
			composerAttributeDao.save(composerAttribute);
					
			if(composerAttribute.getId() > 0){
				responseObject.setSuccess(true);
				responseObject.setObject(composerAttribute);
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
			logger.info("duplicate attribute source field found:" + composerAttribute.getDestinationField());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.COMPOSER_DUPLICATE_ATTRIBUTE_FOUND);
		}
		return responseObject;
	}
	
	
	/**
	 * Method will get attribute list count by mapping id
	 * @param mappingId
	 * @return long
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public long getAttributeListCountByMappingId(int mappingId) {
		Map<String, Object> attributeConditionsList = composerAttributeDao.getAttributeConditionList(mappingId);
		return composerAttributeDao.getQueryCount(ComposerAttribute.class, (List<Criterion>) attributeConditionsList.get("conditions"),(HashMap<String, String>) attributeConditionsList.get("aliases"));
	}

	/**
	 * @param mappingId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List<ParserAttribute>
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ComposerAttribute> getPaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord) {
		
		Map<String, Object> attributeConditionsList = composerAttributeDao.getAttributeConditionList(mappingId);
		return composerAttributeDao.getPaginatedList(ComposerAttribute.class,(List<Criterion>) attributeConditionsList.get("conditions"), (HashMap<String, String>) attributeConditionsList.get("aliases"),
																		  startIndex,limit, sidx, sord);
	}
	
	/**
	 * Prepare composer attribute map for jqgrid 
	 * @param resultList
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public List<Map<String, Object>> getAttributeMap(List<ComposerAttribute> resultList){
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (ComposerAttribute attributeList : resultList) {
				row = new HashMap<>();
				row.put("id", attributeList.getId());
				row.put("sequenceNumber", attributeList.getSequenceNumber());
				row.put("destinationField", attributeList.getDestinationField());
				row.put("unifiedField", attributeList.getUnifiedField());
				row.put("description", attributeList.getDescription());
				row.put("dataType",ObjectUtils.toString(attributeList.getDataType()));
				row.put("defualtValue", attributeList.getDefualtValue());
				row.put("dateFormat", attributeList.getDateFormat());
				row.put("trimchars", attributeList.getTrimchars());
				row.put("trimPosition",attributeList.getTrimPosition());
				row.put("attributeOrder", String.valueOf(attributeList.getAttributeOrder()));
				if(attributeList instanceof ASCIIComposerAttr){
					logger.debug("Ascii Composer attribute found");
					ASCIIComposerAttr asciiAttr=(ASCIIComposerAttr)attributeList;
					
					row.put("replaceConditionList", asciiAttr.getReplaceConditionList());
					row.put("paddingEnable", asciiAttr.isPaddingEnable());
					row.put("length", asciiAttr.getLength());
					row.put("paddingType", asciiAttr.getPaddingType().toString());
					row.put("paddingChar", asciiAttr.getPaddingChar());
					row.put("prefix", asciiAttr.getPrefix());
					row.put("suffix", asciiAttr.getSuffix());
				}
				if(attributeList instanceof ASN1ComposerAttribute){
					logger.debug("ASN1 composer found");
					ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute)attributeList;
					row.put("choiceId", asn1ComposerAttribute.getChoiceId());
					row.put("asn1DataType", asn1ComposerAttribute.getasn1DataType());
					row.put("argumentDataType", asn1ComposerAttribute.getArgumentDataType());
					row.put("destFieldDataFormat", asn1ComposerAttribute.getDestFieldDataFormat());
					row.put("childAttributes", asn1ComposerAttribute.getChildAttributes());
				}
				if(attributeList instanceof FixedLengthASCIIComposerAttribute){
					logger.debug("Fixed Length Composer found");
					FixedLengthASCIIComposerAttribute fixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute)attributeList;
					row.put("fixedLength", fixedLengthASCIIComposerAttribute.getFixedLength());
					row.put("fixedLengthDateFormat", fixedLengthASCIIComposerAttribute.getFixedLengthDateFormat());
					row.put("paddingType", fixedLengthASCIIComposerAttribute.getPaddingType().toString());
					row.put("paddingChar", fixedLengthASCIIComposerAttribute.getPaddingChar());
					row.put("prefix", fixedLengthASCIIComposerAttribute.getPrefix());
					row.put("suffix", fixedLengthASCIIComposerAttribute.getSuffix());
				}
				rowList.add(row);
			}
		}
		return rowList;
		
	}
	
	/**
	 * Update Composer Attribute
	 * @param parserAttribute
	 * @param mappingId
	 * @param pluginType
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_COMPOSER_ATTRIBUTE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = ComposerAttribute.class ,ignorePropList= "myComposer")
	public ResponseObject updateComposerAttributes(ComposerAttribute composerAttribute, int mappingId, String pluginType, int staffId){
		ResponseObject responseObject = new ResponseObject();
		 
		composerAttribute.setCreatedByStaffId(staffId);
		composerAttribute.setLastUpdatedByStaffId(staffId);
		composerAttribute.setCreatedDate(new Date());
		composerAttribute.setLastUpdatedDate(new Date());
		String destinationField= composerAttribute.getDestinationField();
		if(destinationField==null) {
			destinationField="";
		}
		if (isAttributeUniqueForUpdate(composerAttribute ,composerAttribute.getId(), destinationField, mappingId,-1)){
			if(composerAttribute.getId() > 0 ){
				ComposerAttribute dbComposerAttribute = composerAttributeDao.findByPrimaryKey(ComposerAttribute.class, composerAttribute.getId());
				
				if(dbComposerAttribute != null ){
					
					dbComposerAttribute.setSequenceNumber(composerAttribute.getSequenceNumber());
					dbComposerAttribute.setDestinationField(composerAttribute.getDestinationField());
					dbComposerAttribute.setDescription(composerAttribute.getDescription());
					dbComposerAttribute.setUnifiedField(composerAttribute.getUnifiedField());
					dbComposerAttribute.setDataType(composerAttribute.getDataType());
					dbComposerAttribute.setDefualtValue(composerAttribute.getDefualtValue());
					dbComposerAttribute.setTrimchars(composerAttribute.getTrimchars());
					dbComposerAttribute.setDateFormat(composerAttribute.getDateFormat());
					dbComposerAttribute.setAttributeOrder(composerAttribute.getAttributeOrder());
					
					if(composerAttribute instanceof ASCIIComposerAttr){
						logger.debug("Ascii Composer Attribute found");
						ASCIIComposerAttr asciiComposerAttr=(ASCIIComposerAttr)composerAttribute;
						ASCIIComposerAttr dbasciiComposerAttr=(ASCIIComposerAttr)dbComposerAttribute;
						dbasciiComposerAttr.setReplaceConditionList(asciiComposerAttr.getReplaceConditionList());
						dbasciiComposerAttr.setPaddingEnable(asciiComposerAttr.isPaddingEnable());
						dbasciiComposerAttr.setLength(asciiComposerAttr.getLength());
						dbasciiComposerAttr.setPaddingType(asciiComposerAttr.getPaddingType());
						dbasciiComposerAttr.setPaddingChar(asciiComposerAttr.getPaddingChar());
						dbasciiComposerAttr.setPrefix(asciiComposerAttr.getPrefix());
						dbasciiComposerAttr.setSuffix(asciiComposerAttr.getSuffix());
						dbComposerAttribute.setTrimPosition(asciiComposerAttr.getTrimPosition());
					}
					else if(composerAttribute instanceof ASN1ComposerAttribute){
						logger.debug("ASN1 composer Attribute found");
						ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute)composerAttribute;
						ASN1ComposerAttribute dbAsn1ComposerAttribute = (ASN1ComposerAttribute)dbComposerAttribute;
						dbAsn1ComposerAttribute.setArgumentDataType(asn1ComposerAttribute.getArgumentDataType());
						dbAsn1ComposerAttribute.setasn1DataType(asn1ComposerAttribute.getasn1DataType());
						dbAsn1ComposerAttribute.setChildAttributes(asn1ComposerAttribute.getChildAttributes());
						dbAsn1ComposerAttribute.setChoiceId(asn1ComposerAttribute.getChoiceId());
						dbAsn1ComposerAttribute.setDestFieldDataFormat(asn1ComposerAttribute.getDestFieldDataFormat());
					} else if(composerAttribute instanceof FixedLengthASCIIComposerAttribute){
						logger.info("Fixed length ASCII attribute found");
						FixedLengthASCIIComposerAttribute fixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute)composerAttribute;
						FixedLengthASCIIComposerAttribute dbFixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute)dbComposerAttribute;
						dbFixedLengthASCIIComposerAttribute.setPrefix(fixedLengthASCIIComposerAttribute.getPrefix());
						dbFixedLengthASCIIComposerAttribute.setSuffix(fixedLengthASCIIComposerAttribute.getSuffix());
						dbFixedLengthASCIIComposerAttribute.setPaddingType(fixedLengthASCIIComposerAttribute.getPaddingType());
						dbFixedLengthASCIIComposerAttribute.setPaddingChar(fixedLengthASCIIComposerAttribute.getPaddingChar());
						dbFixedLengthASCIIComposerAttribute.setFixedLength(fixedLengthASCIIComposerAttribute.getFixedLength());
						dbFixedLengthASCIIComposerAttribute.setFixedLengthDateFormat(fixedLengthASCIIComposerAttribute.getFixedLengthDateFormat());
					} else if(composerAttribute instanceof RoamingComposerAttribute){
						if(composerAttribute instanceof TAPComposerAttribute){
						    logger.debug("TAP composer Attribute found");
						} else if(composerAttribute instanceof RAPComposerAttribute){
							logger.debug("RAP composer Attribute found");
						} else if(composerAttribute instanceof NRTRDEComposerAttribute){
							logger.debug("NRTRDE composer Attribute found");
						}
						RoamingComposerAttribute roamingComposerAttribute = (RoamingComposerAttribute)composerAttribute;
						RoamingComposerAttribute dbroamingComposerAttribute = (RoamingComposerAttribute)dbComposerAttribute;
						dbroamingComposerAttribute.setArgumentDataType(roamingComposerAttribute.getArgumentDataType());
						dbroamingComposerAttribute.setasn1DataType(roamingComposerAttribute.getasn1DataType());
						dbroamingComposerAttribute.setChildAttributes(roamingComposerAttribute.getChildAttributes());
						dbroamingComposerAttribute.setChoiceId(roamingComposerAttribute.getChoiceId());
						dbroamingComposerAttribute.setDestFieldDataFormat(roamingComposerAttribute.getDestFieldDataFormat());
						dbroamingComposerAttribute.setComposeFromJsonEnable(roamingComposerAttribute.isComposeFromJsonEnable());
						dbroamingComposerAttribute.setCloneRecordEnable(roamingComposerAttribute.isCloneRecordEnable());
					}
					dbComposerAttribute.setLastUpdatedDate(new Date());
					dbComposerAttribute.setLastUpdatedByStaffId(staffId);
					dbComposerAttribute.setStatus(StateEnum.ACTIVE);
					
					composerAttributeDao.merge(dbComposerAttribute);
					responseObject.setSuccess(true);
					responseObject.setObject(composerAttribute);
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
			logger.info("duplicate attribute source field found:" + composerAttribute.getDestinationField());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.COMPOSER_DUPLICATE_ATTRIBUTE_FOUND);
		}
		return responseObject;
		

	}
	
	/**
	 * Delete Multiple composer Attributes
	 * @param attributeId
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_COMPOSER_ATTRIBUTE, actionType = BaseConstants.DELETE_ACTION, currentEntity = ComposerAttribute.class ,ignorePropList= "")
	public ResponseObject deleteComposerAttributes(String attributeIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(attributeIds)){
			String [] attributeIdList = attributeIds.split(",");
			
			for(int i = 0; i < attributeIdList.length; i ++ ){
				responseObject = deleteAttribute(Integer.parseInt(attributeIdList[i]), staffId);
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
	
	/**
	 * Method will delete selected attribute for any parser configuration.
	 * @param attributeId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_COMPOSER_ATTRIBUTE, actionType = BaseConstants.DELETE_ACTION, currentEntity = ComposerAttribute.class ,ignorePropList= "")
	public ResponseObject deleteAttribute(int attributeId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
	
		
		if(attributeId > 0){
			
			ComposerAttribute composerAttribute  = composerAttributeDao.findByPrimaryKey(ComposerAttribute.class, attributeId);
			if(composerAttribute != null){
				int composerMappingId=composerAttribute.getMyComposer().getId();
				logger.debug("Composer Mapping Id found in delete attribute "+composerMappingId);
			
				List<ComposerAttribute> composerAttributeList=composerAttributeDao.getAllAttributeByMappingId(composerMappingId);
				if(composerAttributeList!=null && !composerAttributeList.isEmpty()){
					for(int i = 0 ; i < composerAttributeList.size(); i++ ){
						ComposerAttribute dbComposerAttribute=composerAttributeList.get(i);
						if(attributeId == (dbComposerAttribute.getId()) ){
							dbComposerAttribute.setSequenceNumber(-1);
							
							dbComposerAttribute.setLastUpdatedDate(new Date());
							dbComposerAttribute.setLastUpdatedByStaffId(staffId);
							dbComposerAttribute.setStatus(StateEnum.DELETED);
							
							composerAttributeDao.merge(dbComposerAttribute);
							responseObject.setSuccess(true);
							responseObject.setObject(composerAttribute);
							responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
						}
					}
				}else{
					logger.debug("Fail to fetch all attribute using composer mapping");
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
				}
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
	
	@Transactional(readOnly = true)
	public boolean isAttributeUniqueForUpdate(ComposerAttribute composerAttribute, int attributeId, String attributeName, int mappingId,int importMode) {
		logger.debug("Checking unique attribute name for mapping " + mappingId);
		//composerAttribute instanceof FixedLengthASCIIComposerAttribute added because destinationField is not there in FixedLengthASCIIComposerAttribute
		if(composerAttribute instanceof ASN1ComposerAttribute || composerAttribute instanceof FixedLengthASCIIComposerAttribute || composerAttribute instanceof TAPComposerAttribute || composerAttribute instanceof RAPComposerAttribute || composerAttribute instanceof NRTRDEComposerAttribute){
			return true;
		}
		ComposerAttribute composerAttribute1 = composerAttributeDao.checkUniqueAttributeNameForUpdate(mappingId, attributeName);
		boolean isUnique;
		if (importMode == BaseConstants.IMPORT_MODE_ADD) {
			if (composerAttribute1 != null) {
				logger.info("Duplicate mapping name found.");
				isUnique = false;
			} else {
				logger.info("Mapping name is found unique. ");
				isUnique = true;
			}
		} else if (composerAttribute1 != null) {
			// If ID is same , then it is same attribute object
			if (attributeId == composerAttribute1.getId()) {
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


	@Override
	public void validateComoposerAttributes(ComposerAttribute composerAttribute,  List<ImportValidationErrors> importErrorList) {
		if(composerAttribute != null){
			logger.debug("Validating composer attributes.");
			composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, null, composerAttribute.getDestinationField(), true,importErrorList);
		}else{
			logger.debug("Composer attribute found null.");
		}
	}
	
	/**
	 * Method will fetch all attributes by device configuration.
	 * 
	 * @param deviceConfigId
	 * @return ResponseObject
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAttributeListByMappingId(int mappingId, String deviceType) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();

		if ("UPSTREAM".equalsIgnoreCase(deviceType)) {
			List<ParserAttribute> attributeList = parserAttributeDao.getAttributeListByMappingId(mappingId);

			if (attributeList != null && !attributeList.isEmpty()) {
				responseObject.setObject(attributeList);
				responseObject.setSuccess(true);
			} else {
				responseObject.setObject(attributeList);
				responseObject.setSuccess(false);
			}
		} else if ("DOWNSTREAM".equalsIgnoreCase(deviceType)) {

			List<ComposerAttribute> attributeList = composerAttributeDao.getAllAttributeByMappingId(mappingId);
			if (attributeList != null && !attributeList.isEmpty()) {
				responseObject.setObject(attributeList);
				responseObject.setSuccess(true);
			} else {
				responseObject.setObject(attributeList);
				responseObject.setSuccess(false);
			}
		}
		return responseObject;
	}
	
	@Override
	public List<ComposerAttribute> importComposerAttributesForUpdateMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		logger.debug("import : going to add/update composer attributes of composer : "+dbComposerMapping.getName());
		List<ComposerAttribute> dbAttributeList = dbComposerMapping.getAttributeList();
		List<ComposerAttribute> exportedAttributeList = exportedComposerMapping.getAttributeList();
		Iterator<ComposerAttribute> itrExp = exportedAttributeList.iterator();
		while(itrExp.hasNext()){
			ComposerAttribute composerAttr = itrExp.next();
			if(composerAttr.isAssociatedByGroup()){
				itrExp.remove();
			}
		}
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null && !exportedAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(exportedAttribute instanceof ASCIIComposerAttr) {
						ASCIIComposerAttr dbASCIIComposerAttr = getASCIIComposerAttrFromList(dbAttributeList, exportedAttribute.getSequenceNumber(), exportedAttribute.getDestinationField());
						if(dbASCIIComposerAttr != null) {
							logger.debug("going to update ascii composer attribute for import : "+dbASCIIComposerAttr);
							updateComposerAttribute(dbASCIIComposerAttr, exportedAttribute);
							dbAttributeList.add(dbASCIIComposerAttr);
						} else {
							logger.debug("going to add ascii composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof ASN1ComposerAttribute) {
						ASN1ComposerAttribute dbASN1ComposerAttribute = getASN1ComposerAttributeFromList(dbAttributeList, ((ASN1ComposerAttribute) exportedAttribute).getChildAttributes(), exportedAttribute.getDestinationField(), exportedAttribute.getUnifiedField());
						if(dbASN1ComposerAttribute != null){
							logger.debug("going to update asn1 composer attribute for import : "+dbASN1ComposerAttribute);
							updateComposerAttribute(dbASN1ComposerAttribute, exportedAttribute);
							dbAttributeList.add(dbASN1ComposerAttribute);
						} else {
							logger.debug("going to add asn1 composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof XMLComposerAttribute) {
						XMLComposerAttribute dbXMLComposerAttribute = getXMLComposerAttributeFromList(dbAttributeList, exportedAttribute.getSequenceNumber());
						if(dbXMLComposerAttribute != null) {
							logger.debug("going to update xml composer attribute for import : "+dbXMLComposerAttribute);
							updateComposerAttribute(dbXMLComposerAttribute, exportedAttribute);
							dbAttributeList.add(dbXMLComposerAttribute);
						} else {
							logger.debug("going to add xml composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof FixedLengthASCIIComposerAttribute) {
						FixedLengthASCIIComposerAttribute dbFixedLengthASCIIComposerAttribute = getFixedLengthASCIIComposerAttributeFromList(dbAttributeList, exportedAttribute.getSequenceNumber());
						if(dbFixedLengthASCIIComposerAttribute != null) {
							logger.debug("going to update fixed length ascii composer atttribute for import : "+dbFixedLengthASCIIComposerAttribute);
							updateComposerAttribute(dbFixedLengthASCIIComposerAttribute, exportedAttribute);
							dbAttributeList.add(dbFixedLengthASCIIComposerAttribute);
						} else {
							logger.debug("going to add fixed length ascii composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof RoamingComposerAttribute && !exportedAttribute.isAssociatedByGroup()) {
						RoamingComposerAttribute dbRoamingComposerAttribute = getRoamingComposerAttributeFromList(dbAttributeList, ((RoamingComposerAttribute) exportedAttribute).getChildAttributes(), exportedAttribute.getDestinationField(), exportedAttribute.getUnifiedField(),false);
						if(dbRoamingComposerAttribute != null){
							logger.debug("going to update roaming composer attribute for import : "+dbRoamingComposerAttribute);
							updateComposerAttribute(dbRoamingComposerAttribute, exportedAttribute);
							dbAttributeList.add(dbRoamingComposerAttribute);
						} else {
							logger.debug("going to add roaming composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					}
				}
			}
		}
		return dbAttributeList;
	}
	
	@Override
	public List<ComposerAttribute> importComposerAttributesForAddMode(ComposerMapping dbComposerMapping, ComposerMapping exportedComposerMapping) {
		logger.debug("import : going to add/update composer attributes of composer : "+dbComposerMapping.getName());
		List<ComposerAttribute> dbAttributeList = dbComposerMapping.getAttributeList();
		List<ComposerAttribute> exportedAttributeList = exportedComposerMapping.getAttributeList();
		Iterator<ComposerAttribute> itrExp = exportedAttributeList.iterator();
		while(itrExp.hasNext()){
			ComposerAttribute composerAttr = itrExp.next();
			if(composerAttr.isAssociatedByGroup()){
				itrExp.remove();
			}
		}
		if(!CollectionUtils.isEmpty(exportedAttributeList)) {
			int length = exportedAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute exportedAttribute = exportedAttributeList.get(i);
				if(exportedAttribute != null && !exportedAttribute.getStatus().equals(StateEnum.DELETED)) {
					if(exportedAttribute instanceof ASCIIComposerAttr) {
						ASCIIComposerAttr dbASCIIComposerAttr = getASCIIComposerAttrFromList(dbAttributeList, exportedAttribute.getSequenceNumber(), exportedAttribute.getDestinationField());
						if(dbASCIIComposerAttr == null) {							
							logger.debug("going to add ascii composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof ASN1ComposerAttribute) {
						ASN1ComposerAttribute dbASN1ComposerAttribute = getASN1ComposerAttributeFromList(dbAttributeList, ((ASN1ComposerAttribute) exportedAttribute).getChildAttributes(), exportedAttribute.getDestinationField(), exportedAttribute.getUnifiedField());
						if(dbASN1ComposerAttribute == null){							
							logger.debug("going to add asn1 composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof XMLComposerAttribute) {
						XMLComposerAttribute dbXMLComposerAttribute = getXMLComposerAttributeFromList(dbAttributeList, exportedAttribute.getSequenceNumber());
						if(dbXMLComposerAttribute == null) {							
							logger.debug("going to add xml composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof FixedLengthASCIIComposerAttribute) {
						FixedLengthASCIIComposerAttribute dbFixedLengthASCIIComposerAttribute = getFixedLengthASCIIComposerAttributeFromList(dbAttributeList, exportedAttribute.getSequenceNumber());
						if(dbFixedLengthASCIIComposerAttribute == null) {							
							logger.debug("going to add fixed length ascii composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					} else if(exportedAttribute instanceof RoamingComposerAttribute && !exportedAttribute.isAssociatedByGroup()) {
						RoamingComposerAttribute dbRoamingComposerAttribute = getRoamingComposerAttributeFromList(dbAttributeList, ((RoamingComposerAttribute) exportedAttribute).getChildAttributes(), exportedAttribute.getDestinationField(), exportedAttribute.getUnifiedField(),false);
						if(dbRoamingComposerAttribute == null){							
							logger.debug("going to add roaming composer attribute for import : "+exportedAttribute);
							saveComposerAttribute(exportedAttribute, exportedComposerMapping, dbComposerMapping.getLastUpdatedByStaffId());
							dbAttributeList.add(exportedAttribute);
						}
					}
				}
			}
		}
		return dbAttributeList;
	}
	
	public ASCIIComposerAttr getASCIIComposerAttrFromList(List<ComposerAttribute> composerAttributeList, int sequenceNo, String destinationField) {
		if(!CollectionUtils.isEmpty(composerAttributeList)) {
			int length = composerAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute composerAttribute = composerAttributeList.get(i);
				if(composerAttribute != null && !composerAttribute.getStatus().equals(StateEnum.DELETED)
						&& composerAttribute instanceof ASCIIComposerAttr) {
					ASCIIComposerAttr asciiComposerAttr = (ASCIIComposerAttr) composerAttribute;
					if(asciiComposerAttr.getSequenceNumber() == sequenceNo && (asciiComposerAttr.getDestinationField() != null && asciiComposerAttr.getDestinationField().equalsIgnoreCase(destinationField))) {
						return (ASCIIComposerAttr) composerAttributeList.remove(i);
					}
				}
			}
		}
		return null;
	}
	
	public ASN1ComposerAttribute getASN1ComposerAttributeFromList(List<ComposerAttribute> composerAttributeList, String childAttributes, String destinationField, String unifiedField) {
		if(!CollectionUtils.isEmpty(composerAttributeList)) {
			int length = composerAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute composerAttribute = composerAttributeList.get(i);
				if(composerAttribute != null && !composerAttribute.getStatus().equals(StateEnum.DELETED)
						&& composerAttribute instanceof ASN1ComposerAttribute) {
					if(isAsn1ComposerAttributeUnique((ASN1ComposerAttribute) composerAttribute, childAttributes, destinationField, unifiedField)) {
						return (ASN1ComposerAttribute) composerAttributeList.remove(i);
					}
				}
			}
		}
		return null;
	}
	
	public boolean isAsn1ComposerAttributeUnique(ASN1ComposerAttribute asnComposerAttribute, String childAttributes, String destinationField, String unifiedField) {
		boolean isChildAttributeMatched = false;
		boolean isDestinationFieldMatched = false;
		boolean isUnifiedFieldMatched = false;
		
		if(asnComposerAttribute.getUnifiedField() == null && unifiedField == null) {
			isUnifiedFieldMatched = true;
		} else if(asnComposerAttribute.getUnifiedField() != null && unifiedField != null
				&& asnComposerAttribute.getUnifiedField().equalsIgnoreCase(unifiedField)) {
			isUnifiedFieldMatched = true;
		}
		
		if(asnComposerAttribute.getDestinationField() == null && destinationField == null) {
			isDestinationFieldMatched = true;
		} else if(asnComposerAttribute.getDestinationField() != null && destinationField != null
				&& asnComposerAttribute.getDestinationField().equalsIgnoreCase(destinationField)) {
			isDestinationFieldMatched = true;
		}
		
		if(asnComposerAttribute.getChildAttributes() == null && childAttributes == null) {
			isChildAttributeMatched = true;
		} else if(asnComposerAttribute.getChildAttributes() != null && childAttributes != null
				&& asnComposerAttribute.getChildAttributes().equalsIgnoreCase(childAttributes)) {
			isChildAttributeMatched = true;
		}
		
		return isChildAttributeMatched && isDestinationFieldMatched && isUnifiedFieldMatched ? true : false;
	}
	
	@Override
	public RoamingComposerAttribute getRoamingComposerAttributeFromList(List<ComposerAttribute> composerAttributeList, String childAttributes, String destinationField, String unifiedField,boolean associatedFlag) {
		if(!CollectionUtils.isEmpty(composerAttributeList)) {
			int length = composerAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute composerAttribute = composerAttributeList.get(i);
					if(composerAttribute != null && !composerAttribute.getStatus().equals(StateEnum.DELETED)
							&& composerAttribute instanceof RoamingComposerAttribute && composerAttribute.isAssociatedByGroup()==associatedFlag) {
						if(isRoamingComposerAttributeUnique((RoamingComposerAttribute) composerAttribute, childAttributes, destinationField, unifiedField)) {
							return (RoamingComposerAttribute) composerAttributeList.remove(i);
						}
					}
			}
		}
		return null;
	}
	
	public boolean isRoamingComposerAttributeUnique(RoamingComposerAttribute roamingComposerAttribute, String childAttributes, String destinationField, String unifiedField) {
		boolean isChildAttributeMatched = false;
		boolean isDestinationFieldMatched = false;
		boolean isUnifiedFieldMatched = false;
		
		if(roamingComposerAttribute.getUnifiedField() == null && unifiedField == null) {
			isUnifiedFieldMatched = true;
		} else if(roamingComposerAttribute.getUnifiedField() != null && unifiedField != null
				&& roamingComposerAttribute.getUnifiedField().equalsIgnoreCase(unifiedField)) {
			isUnifiedFieldMatched = true;
		}
		
		if(roamingComposerAttribute.getDestinationField() == null && destinationField == null) {
			isDestinationFieldMatched = true;
		} else if(roamingComposerAttribute.getDestinationField() != null && destinationField != null
				&& roamingComposerAttribute.getDestinationField().equalsIgnoreCase(destinationField)) {
			isDestinationFieldMatched = true;
		}
		
		if(roamingComposerAttribute.getChildAttributes() == null && childAttributes == null) {
			isChildAttributeMatched = true;
		} else if(roamingComposerAttribute.getChildAttributes() != null && childAttributes != null
				&& roamingComposerAttribute.getChildAttributes().equalsIgnoreCase(childAttributes)) {
			isChildAttributeMatched = true;
		}
		
		return isChildAttributeMatched && isDestinationFieldMatched && isUnifiedFieldMatched ? true : false;
	}
	
	public XMLComposerAttribute getXMLComposerAttributeFromList(List<ComposerAttribute> composerAttributeList, int sequenceNo) {
		if(!CollectionUtils.isEmpty(composerAttributeList)) {
			int length = composerAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute composerAttribute = composerAttributeList.get(i);
				if(composerAttribute != null && !composerAttribute.getStatus().equals(StateEnum.DELETED)
						&& composerAttribute instanceof XMLComposerAttribute) {
					XMLComposerAttribute xmlComposerAttribute = (XMLComposerAttribute) composerAttribute;
					if(xmlComposerAttribute.getSequenceNumber() == sequenceNo) {
						return (XMLComposerAttribute) composerAttributeList.remove(i);
					}
				}
			}
		}
		return null;
	}
	
	public FixedLengthASCIIComposerAttribute getFixedLengthASCIIComposerAttributeFromList(List<ComposerAttribute> composerAttributeList, int sequenceNo) {
		if(!CollectionUtils.isEmpty(composerAttributeList)) {
			int length = composerAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute composerAttribute = composerAttributeList.get(i);
				if(composerAttribute != null && !composerAttribute.getStatus().equals(StateEnum.DELETED)
						&& composerAttribute instanceof FixedLengthASCIIComposerAttribute) {
					FixedLengthASCIIComposerAttribute fixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
					if(fixedLengthASCIIComposerAttribute.getSequenceNumber() == sequenceNo) {
						return (FixedLengthASCIIComposerAttribute) composerAttributeList.remove(i);
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public void saveComposerAttribute(ComposerAttribute composerAttribute, ComposerMapping composerMapping, int staffId) {
		composerAttribute.setId(0);
		composerAttribute.setCreatedByStaffId(staffId);
		composerAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		composerAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		composerAttribute.setLastUpdatedByStaffId(staffId);
		composerAttribute.setMyComposer(composerMapping);
	}
	
	@Override
	public void updateComposerAttribute(ComposerAttribute dbAttribute, ComposerAttribute exportedAttribute) {
		if(dbAttribute instanceof ASCIIComposerAttr
				&& exportedAttribute instanceof ASCIIComposerAttr) {
			iterateAsciiComposerAttribute((ASCIIComposerAttr) dbAttribute, (ASCIIComposerAttr) exportedAttribute);
		} else if(dbAttribute instanceof ASN1ComposerAttribute
				&& exportedAttribute instanceof ASN1ComposerAttribute) {
			iterateASN1ComposerAttribute((ASN1ComposerAttribute) dbAttribute, (ASN1ComposerAttribute) exportedAttribute);
		} else if(dbAttribute instanceof FixedLengthASCIIComposerAttribute
				&& exportedAttribute instanceof FixedLengthASCIIComposerAttribute) {
			iterateFixedLengthASCIIComposerAttribute((FixedLengthASCIIComposerAttribute) dbAttribute, (FixedLengthASCIIComposerAttribute) exportedAttribute);
		} else if(dbAttribute instanceof XMLComposerAttribute
				&& exportedAttribute instanceof XMLComposerAttribute) {
			iterateXMLComposerAttribute((XMLComposerAttribute) dbAttribute, (XMLComposerAttribute) exportedAttribute);
		} else if(dbAttribute instanceof RoamingComposerAttribute
				&& exportedAttribute instanceof RoamingComposerAttribute) {
			iterateRoamingComposerAttribute((RoamingComposerAttribute) dbAttribute, (RoamingComposerAttribute) exportedAttribute);
		} else {
			iterateComposerAttribute(dbAttribute, exportedAttribute);
		}
	}
	
	public void iterateAsciiComposerAttribute(ASCIIComposerAttr dbAttribute, ASCIIComposerAttr exportedAttribute) {
		dbAttribute.setReplaceConditionList(exportedAttribute.getReplaceConditionList());
		dbAttribute.setPaddingEnable(exportedAttribute.isPaddingEnable());
		dbAttribute.setLength(exportedAttribute.getLength());
		dbAttribute.setPaddingType(exportedAttribute.getPaddingType());
		dbAttribute.setPaddingChar(exportedAttribute.getPaddingChar());
		dbAttribute.setPrefix(exportedAttribute.getPrefix());
		dbAttribute.setSuffix(exportedAttribute.getSuffix());
		
		iterateComposerAttribute(dbAttribute, exportedAttribute);
	}
	
	public void iterateASN1ComposerAttribute(ASN1ComposerAttribute dbAttribute, ASN1ComposerAttribute exportedAttribute) {
		dbAttribute.setasn1DataType(exportedAttribute.getasn1DataType());
		dbAttribute.setArgumentDataType(exportedAttribute.getArgumentDataType());
		dbAttribute.setDestFieldDataFormat(exportedAttribute.getDestFieldDataFormat());
		dbAttribute.setChoiceId(exportedAttribute.getChoiceId());
		dbAttribute.setChildAttributes(exportedAttribute.getChildAttributes());
		dbAttribute.setAttrType(exportedAttribute.getAttrType());
		
		iterateComposerAttribute(dbAttribute, exportedAttribute);
	}
	
	public void iterateRoamingComposerAttribute(RoamingComposerAttribute dbAttribute, RoamingComposerAttribute exportedAttribute) {
		dbAttribute.setasn1DataType(exportedAttribute.getasn1DataType());
		dbAttribute.setArgumentDataType(exportedAttribute.getArgumentDataType());
		dbAttribute.setDestFieldDataFormat(exportedAttribute.getDestFieldDataFormat());
		dbAttribute.setChoiceId(exportedAttribute.getChoiceId());
		dbAttribute.setChildAttributes(exportedAttribute.getChildAttributes());
		dbAttribute.setAttrType(exportedAttribute.getAttrType());
		dbAttribute.setComposeFromJsonEnable(exportedAttribute.isComposeFromJsonEnable());
		dbAttribute.setCloneRecordEnable(exportedAttribute.isCloneRecordEnable());
		
		iterateComposerAttribute(dbAttribute, exportedAttribute);
	}
	
	public void iterateFixedLengthASCIIComposerAttribute(FixedLengthASCIIComposerAttribute dbAttribute, FixedLengthASCIIComposerAttribute exportedAttribute) {
		dbAttribute.setPaddingType(exportedAttribute.getPaddingType());
		dbAttribute.setPrefix(exportedAttribute.getPrefix());
		dbAttribute.setSuffix(exportedAttribute.getSuffix());
		dbAttribute.setPaddingChar(exportedAttribute.getPaddingChar());
		dbAttribute.setFixedLengthDateFormat(exportedAttribute.getFixedLengthDateFormat());
		dbAttribute.setFixedLength(exportedAttribute.getFixedLength());
		
		iterateComposerAttribute(dbAttribute, exportedAttribute);
	}
	
	public void iterateXMLComposerAttribute(XMLComposerAttribute dbAttribute, XMLComposerAttribute exportedAttribute) {
		iterateComposerAttribute(dbAttribute, exportedAttribute);
	}
	
	public void iterateComposerAttribute(ComposerAttribute dbAttribute, ComposerAttribute exportedAttribute) {
		dbAttribute.setSequenceNumber(exportedAttribute.getSequenceNumber());
		dbAttribute.setDestinationField(exportedAttribute.getDestinationField());
		dbAttribute.setUnifiedField(exportedAttribute.getUnifiedField());
		dbAttribute.setDefualtValue(exportedAttribute.getDefualtValue());
		dbAttribute.setTrimchars(exportedAttribute.getTrimchars());
		dbAttribute.setTrimPosition(exportedAttribute.getTrimPosition());
		dbAttribute.setDescription(exportedAttribute.getDescription());
		dbAttribute.setDataType(exportedAttribute.getDataType());
		dbAttribute.setDateFormat(exportedAttribute.getDateFormat());
		dbAttribute.setAttributeOrder(exportedAttribute.getAttributeOrder());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Integer> getAllSequenceNumbers(int mappingId,int composerId) {
		return composerAttributeDao.getAllAttributeSeqNumberByMappingId(mappingId,composerId);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ComposerAttribute> getASN1PaginatedList(int mappingId, int startIndex, int limit, String sidx,
			String sord, ASN1ATTRTYPE asn1attrtype) {
		Map<String, Object> attributeConditionsList = composerAttributeDao.getASN1AttributeConditionList(mappingId,asn1attrtype);
		return composerAttributeDao.getPaginatedList(ComposerAttribute.class,(List<Criterion>) attributeConditionsList.get("conditions"), (HashMap<String, String>) attributeConditionsList.get("aliases"),
				  startIndex,limit, sidx, sord);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Integer> getAllSequenceNumbersForASN1(ComposerAttribute composerAttr) {
		return composerAttributeDao.getAllASN1AttributeSeqNumberByMappingId(composerAttr);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getASN1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for ASN1 Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerAttributeDao.getAsn1AttributeListByMappingId(mappingId, asn1attrtype);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getTapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for RAP Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerAttributeDao.getTapAttributeListByMappingId(mappingId, asn1attrtype);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getRapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for RAP Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerAttributeDao.getRapAttributeListByMappingId(mappingId, asn1attrtype);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getNrtrdeAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for RAP Composer : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> attributeList = composerAttributeDao.getNrtrdeAttributeListByMappingId(mappingId, asn1attrtype);

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
	public ResponseObject uploadComposerAttributesFromCSV(File csvFile, int mappingId, String pluginType, int staffId, String asnActionType) {
		logger.debug("inside uploadComposerAttributesFromCSV");
		logger.debug("pluginType : " + pluginType);
		logger.debug("mappingId : " + mappingId);		
		ResponseObject responseObject = new ResponseObject();		
		String header =  null;
		String[] headers = null;
		String row = null;
		int hlength=0;
		int rlength=0;		
		boolean isUploadSuccess = true;		
		List<String> newAttributeList = new ArrayList<>();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();		
		String recordFailureMessage = "CSV record attribute upload failed on Line number # ";
		String failureMessage = "CSV Record failed on Line number # ";
		try {			
			responseObject.setSuccess(true);
			//reading csv file with Files utility which returns line by line in List<String> type
			List<String> data = Files.readAllLines(Paths.get(csvFile.getAbsolutePath())); 
			if(data.size() <= 1){
	    		responseObject.setSuccess(false);
	    		responseObject.setResponseCode(ResponseCode.UPLOADED_ATRRIBUTE_FILE_EMPTY);
	    		return  responseObject;
	    	}
			for (int i=0;i<data.size();i++){
				if(i==0){
					header = data.get(i);			    				
					headers = header.split(BaseConstants.COMMA_SEPARATOR);
					hlength = headers.length;					
				}else{
					row = data.get(i);
					
					List<String> rows = CSVUtils.parseLine(row, ',');
					rlength = rows.size();
					//if header length is not matching with csv record length then return error
					if(hlength!=rlength) {
						responseObject.setSuccess(false);
						responseObject.setObject(failureMessage+i);
			    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_HEADER_MISTMATCH);
			    		deleteComposerAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
			    		return  responseObject;
					}else {				
						header = headers[0];
						//get appropriate composer object by plugin type and set csv row value by comparing header values
						ComposerAttribute composerAttribute = ComposerAttributeFactory.getParserAttributeByType(pluginType);
						
						if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(pluginType)){
							setAsnAttributeActionType(composerAttribute,asnActionType);
						}
						
						for(int attrCnt = 0; attrCnt< headers.length; attrCnt++) {
							if(headers[attrCnt].equals(EnumComposerAttributeHeader.SEQ_NO.getName())) {
								if(rows.get(attrCnt) != null){
									if(Integer.parseInt(rows.get(attrCnt)) > 0){
										composerAttribute.setSequenceNumber(Integer.parseInt(rows.get(attrCnt)));
									}else{
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Provide a proper sequence number greater than 0");
										break;										
									}
									List<Integer> composerAttributeSeq = getAllSequenceNumbers(mappingId,composerAttribute.getId());
									if(composerAttributeSeq!=null && !composerAttributeSeq.isEmpty() && composerAttributeSeq.contains((Integer)composerAttribute.getSequenceNumber())){
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Sequence number is duplicate please enter a unique sequence number.");
										break;	
									}
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DESTINATION_FIELD_NAME.getName())) {
								composerAttribute.setDestinationField(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.UNIFIED_FIELD_NAME.getName())) {
								composerAttribute.setUnifiedField(rows.get(attrCnt));
								if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(pluginType) || EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)  || EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType) || EngineConstants.XML_COMPOSER_PLUGIN.equals(pluginType)){
									String regexVal=Regex.get(SystemParametersConstant.ATTR_UNIFIED_FIELD,BaseConstants.REGEX_MAP_CACHE);
									if (!StringUtils.isEmpty(rows.get(attrCnt)) && (rows.get(attrCnt).length()>100 ||match(regexVal, rows.get(attrCnt)))){
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"'  Unified field must begin with a letter of the alphabet and allows only Maximum 100 alphanumeric characters.");									
										break;
									}
								}
								else {
									if(!StringUtils.isEmpty(rows.get(attrCnt))) {
										try {
											UnifiedFieldEnum.valueOf(rows.get(attrCnt));
										}catch(IllegalArgumentException e) {
											logger.trace("IllegalArgumentException: ",e);
											isUploadSuccess = false;	
											setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Unified field not available in the system");
											break;										
										}
									}
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DESCRIPTION.getName())) {
								composerAttribute.setDescription(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DATA_TYPE.getName())) {
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										composerAttribute.setDataType(DataTypeEnum.valueOf(rows.get(attrCnt)));
										DataTypeEnum.valueOf(rows.get(attrCnt));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException : ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Data Type not available in the system");
										break;										
									}
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DEFAULT_VALUE.getName())) {
								composerAttribute.setDefualtValue(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DATE_FORMAT.getName())) {
								 if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									 FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
									 flaComposerAttribute.setFixedLengthDateFormat(rows.get(attrCnt));
								 }else{
									 composerAttribute.setDateFormat(rows.get(attrCnt));	
								 }									
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.TRIM_CHARACTER.getName())) {
								composerAttribute.setTrimchars(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.TRIM_POSITION.getName())) {
								composerAttribute.setTrimPosition(rows.get(attrCnt));
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {									
									boolean flag = false;
									for (TrimPositionEnum trimPositionEnum : TrimPositionEnum.values()) {
								        if (trimPositionEnum.getValue().equalsIgnoreCase(rows.get(attrCnt))) {
								        	composerAttribute.setTrimPosition(trimPositionEnum.getValue());
								        	flag =true;
								        	break;
								        }
								    }					
									if(!flag) {
										isUploadSuccess = false;
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Trim Postion not available in the system");
										break;	
									}									
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName())) {
								ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
								asciiComposerAttribute.setReplaceConditionList(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.ENABLE_PADDING.getName())) {
								ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
								String isPaddingEnable = rows.get(attrCnt); 
								if((isPaddingEnable !=null) && ("true".equalsIgnoreCase(isPaddingEnable) || "false".equalsIgnoreCase(isPaddingEnable))) {
									asciiComposerAttribute.setPaddingEnable(Boolean.parseBoolean(rows.get(attrCnt)));
								}else{
									isUploadSuccess = false;
									setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Enable Padding! Value must be either true or false");
									break;
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.PADDING_LENGTH.getName())) {
								try {
									ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
									asciiComposerAttribute.setLength(Integer.parseInt(rows.get(attrCnt)));	
								}catch(Exception e) {
									logger.trace("Exception  : ",e);
									isUploadSuccess = false;	
									setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) + "' Invalid value for Padding Length.");
									break;										
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.LENGTH.getName())) {	
								if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									try {
										FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
										flaComposerAttribute.setFixedLength(Integer.parseInt(rows.get(attrCnt)));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException  : ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) + "' Provide a proper length greater than 0.");
										break;										
									}
								}		
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.TYPE.getName())) {
								if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
									if(!StringUtils.isEmpty(rows.get(attrCnt))) {
										try {
											asciiComposerAttribute.setPaddingType(PositionEnum.valueOf(rows.get(attrCnt).toUpperCase()));
											PositionEnum.valueOf(rows.get(attrCnt));
										}catch(IllegalArgumentException e) {
											logger.trace("IllegalArgumentException  : ",e);
											isUploadSuccess = false;	
											setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Type not available in the system");
											break;										
										}
									}		
								}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
									if(!StringUtils.isEmpty(rows.get(attrCnt))) {
										try {
											flaComposerAttribute.setPaddingType(PositionEnum.valueOf(rows.get(attrCnt).toUpperCase()));
											PositionEnum.valueOf(rows.get(attrCnt));
										}catch(IllegalArgumentException e) {
											logger.trace("IllegalArgumentException  : ",e);
											isUploadSuccess = false;	
											setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Type not available in the system");
											break;										
										}
									}		
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.PADDING_CHARACTER.getName())) {
								if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
									asciiComposerAttribute.setPaddingChar(rows.get(attrCnt));
								}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
									flaComposerAttribute.setPaddingChar(rows.get(attrCnt));
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.PREFIX.getName())) {
								if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
									asciiComposerAttribute.setPrefix(rows.get(attrCnt));
								}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
									flaComposerAttribute.setPrefix(rows.get(attrCnt));
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.SUFFIX.getName())) {
								if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) composerAttribute;
									asciiComposerAttribute.setSuffix(rows.get(attrCnt));
								}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(pluginType)){
									FixedLengthASCIIComposerAttribute flaComposerAttribute = (FixedLengthASCIIComposerAttribute) composerAttribute;
									flaComposerAttribute.setSuffix(rows.get(attrCnt));
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.ASN1_DATATYPE.getName())) {
								ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
								asn1ComposerAttribute.setasn1DataType(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.DESTINATION_FIELD_FORMAT.getName())) {
								ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
								asn1ComposerAttribute.setDestFieldDataFormat(rows.get(attrCnt));	
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										DestinationFieldFormat.valueOf(rows.get(attrCnt));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException: ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Destination Field Format not available in the system");
										break;										
									}
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.ARGUMENT_DATATYPE.getName())) {
								ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
								asn1ComposerAttribute.setArgumentDataType(rows.get(attrCnt));	
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										ComposerArgumentTypeEnum.valueOf(rows.get(attrCnt));
									}catch(IllegalArgumentException e) {
										logger.trace("IllegalArgumentException: ",e);
										isUploadSuccess = false;	
										setResponseObject(responseObject,recordFailureMessage+(i+1)+". '"+rows.get(attrCnt) +"' Argument Data Type not available in the system");
										break;										
									}
								}
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.CHOICE_ID.getName())) {
								ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
								asn1ComposerAttribute.setChoiceId(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumComposerAttributeHeader.CHILD_ATTRIBUTES.getName())) {
								ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
								asn1ComposerAttribute.setChildAttributes(rows.get(attrCnt).replace("\"", ""));						
							}else {
								responseObject.setSuccess(false);
					    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
					    		responseObject.setObject(failureMessage+(i+1));					   
					    		isUploadSuccess=false;
					    		break;
							}
							composerAttribute.setAttributeOrder(i+1);
						}	
						
						//validate composer attribute
						if(!isUploadSuccess) {									
							deleteComposerAttributes(EliteUtils.getCSVString(newAttributeList), staffId);		
				    		break;
						}	
							composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, null, null, true, importErrorList);
							checkCSVRecordErrors(responseObject, importErrorList, i+1);
						
						//validation failed then return with errors
						if(!responseObject.isSuccess()) {
							deleteComposerAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
							isUploadSuccess=false;
				    		break;
						}						
						//if validation passes then create composer attribute
						responseObject = createComposerAttributes(composerAttribute, mappingId, pluginType, staffId,-1);						
						//if any error while creating attribute do roll back - removing all added attribute prior to this attribute
						if(!responseObject.isSuccess()) {
							deleteComposerAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							responseObject.setObject(failureMessage+(i+1));									    		
				    		isUploadSuccess=false;
				    		break;
						}
						//maintaining newly added attributes list for anytime rollback in future
						
						ComposerAttribute newAttribute = (ComposerAttribute) responseObject.getObject();
						if (newAttribute.getId() > 0) {
							newAttributeList.add(String.valueOf(newAttribute.getId()));	
						}		
						
					}
				}
			}
			//if all attributes added successfully then return sucess message to controller
			if(isUploadSuccess) {
				responseObject = new ResponseObject();				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.COMPOSER_ATTR_UPLOAD_SUCCESS);
			}
	    }catch(FileNotFoundException e){			    	
	    	logger.trace("Lookup data file not found : ",e);
	    	responseObject.setSuccess(false);
	    	responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);	    		    	
	    }catch (IOException e) {			    
			logger.trace("Problem occurred while reading file : ",e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}catch (Exception e) {			    	
			logger.trace("Exception occurred while reading file : ",e);			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}
		return responseObject;
	}		
	
	private void setResponseObject(ResponseObject responseObject,String s){
		responseObject.setSuccess(false);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
		responseObject.setObject(s);
	}
	
	private void setAsnAttributeActionType(ComposerAttribute composerAttribute,String asnActionType){
		if(asnActionType.equals(BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE)){
			ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
			asn1ComposerAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
		}else if(asnActionType.equals(BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE)){
			ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
			asn1ComposerAttribute.setAttrType(ASN1ATTRTYPE.TRAILER);
		}else {
			ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) composerAttribute;
			asn1ComposerAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
		}
	}	
	
	private void checkCSVRecordErrors(ResponseObject responseObject, List<ImportValidationErrors> importErrorList, int lineNo) {
		
		if (!importErrorList.isEmpty()) {
			logger.debug("Validation Fail for imported file");
			JSONArray finaljArray = new JSONArray();
			for (ImportValidationErrors errors : importErrorList) {
				JSONArray jArray = new JSONArray();
				jArray.put("Error on csv file line # "+lineNo +". " +errors.getErrorMessage());
				finaljArray.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);
		}
	}	
	
	private boolean match(String regex, String value){
		String newValue = "";
		if(value != null){
			newValue = value;
		}
		//logger.info("value: " + newValue);
		//logger.info(regexLoggerConstant + regex);
    	Pattern pattern = Pattern.compile(regex,Pattern.UNICODE_CHARACTER_CLASS);
    	//logger.info("pattern: " + pattern);
    	Matcher matcher = pattern.matcher(newValue);
    	return !matcher.matches();
	}

}