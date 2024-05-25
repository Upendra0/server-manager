/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.SourceFieldDataFormatEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.dao.ComposerAttributeDao;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.device.validator.ParserAttributeValidator;
import com.elitecore.sm.parser.controller.ParserAttributeFactory;
import com.elitecore.sm.parser.dao.ParserAttributeDao;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.dao.RegExPatternDao;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.EnumParserAttributeHeader;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.HtmlParserAttribute;
import com.elitecore.sm.parser.model.JsonParserAttribute;
import com.elitecore.sm.parser.model.MTSiemensParserAttribute;
import com.elitecore.sm.parser.model.NRTRDEParserAttribute;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RAPATTRTYPE;
import com.elitecore.sm.parser.model.RAPParserAttribute;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.SourceFieldFormatASCIIEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatEnum;
import com.elitecore.sm.parser.model.TAPATTRTYPE;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.VarLengthAsciiParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.XMLParserAttribute;
import com.elitecore.sm.parser.model.XlsParserAttribute;
import com.elitecore.sm.util.CSVUtils;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.JsonAttributeGenerator;
import com.elitecore.sm.util.Regex;
import com.elitecore.sm.parser.model.NatFlowParserAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
@Service(value = "parserAttributeService")
public class ParserAttributeServiceImpl implements ParserAttributeService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	@Qualifier(value = "parserMappingDao")
	private ParserMappingDao parserMappingDao;

	@Autowired
	@Qualifier(value = "parserAttributeDao")
	private ParserAttributeDao parserAttributeDao;

	@Autowired
	private ParserMappingService parserMappingService;

	@Autowired
	private RegExPatternDao regExPatternDao;

	@Autowired
	private ParserAttributeValidator parserAttributeValidator;

	@Autowired
	private ComposerAttributeDao composerAttributeDao;
	
	@Autowired
	ParserGroupAttributeService parserGroupAttributeService;

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

	/**
	 * Method will iterate and add current error list to JSON for front end
	 * view.
	 * 
	 * @param attributeErrorList
	 * @return ResponseObject
	 */
	@Override
	public ResponseObject convertErrorToJSONObj(List<ImportValidationErrors> attributeErrorList) {
		ResponseObject responseObject = new ResponseObject();

		if (attributeErrorList != null && !attributeErrorList.isEmpty()) {
			JSONArray jsonErrorObject = new JSONArray();

			for (ImportValidationErrors errors : attributeErrorList) {
				JSONArray jArray = new JSONArray();

				jArray.put(errors.getSequendNumber());
				jArray.put(errors.getEntityName());
				jArray.put(errors.getPropertyName());
				jArray.put(errors.getPropertyValue());
				jArray.put(errors.getErrorMessage());

				jsonErrorObject.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setObject(jsonErrorObject);
		} else {
			responseObject.setSuccess(true);
			responseObject.setObject(null);
		}

		return responseObject;
	}

	/**
	 * Method will get attribute list count by mapping id
	 * 
	 * @param mappingId
	 * @return long
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public long getAttributeListCountByMappingId(int mappingId) {
		Map<String, Object> attributeConditionsList = parserAttributeDao.getAttributeConditionList(mappingId);
		return parserAttributeDao.getQueryCount(ParserAttribute.class, (List<Criterion>) attributeConditionsList.get("conditions"),
				(HashMap<String, String>) attributeConditionsList.get("aliases"));
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
	public List<ParserAttribute> getPaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord) {

		Map<String, Object> attributeConditionsList = parserAttributeDao.getAttributeConditionList(mappingId);
		return parserAttributeDao.getPaginatedList(ParserAttribute.class, (List<Criterion>) attributeConditionsList.get("conditions"),
				(HashMap<String, String>) attributeConditionsList.get("aliases"), startIndex, limit, sidx, sord);
	}

	/**
	 * Add Parser Attribute detail
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.CREATE_PARSER_ATTRIBUTE, actionType = BaseConstants.CREATE_ACTION,
	currentEntity = ParserAttribute.class, ignorePropList = "parserMapping")
	public ResponseObject createParserAttributes(ParserAttribute parserAttributeExp, int mappingId, String pluginType, int staffId) {
	
		ResponseObject responseObject = new ResponseObject();		
		
		/** MED-9175 : Ascii Attributes are not viewing on GUI after importing service. */
		
		/** Start of MED-9753 */
		/*if(!org.apache.commons.lang.StringUtils.isEmpty(parserAttributeExp.getUnifiedField())){
			parserAttributeExp.setUnifiedField(parserAttributeExp.getUnifiedField());
		}*/
		/** End of MED-9753 */
		ParserAttribute parserAttribute = ParserAttributeFactory.getParserAttributeByType(pluginType);
		BeanUtils.copyProperties(parserAttributeExp, parserAttribute);
		
		if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
			ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttributeExp;
			((ASN1ParserAttribute) parserAttribute).setRecordInitilializer(asn1ParserAttribute.isRecordInitilializer());
		}
		if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType) ){
			TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttributeExp;
			((TAPParserAttribute) parserAttribute).setRecordInitilializer(tapParserAttribute.isRecordInitilializer());
			((TAPParserAttribute) parserAttribute).setParseAsJson(tapParserAttribute.isParseAsJson());
		}
		parserAttribute.setCreatedByStaffId(staffId);
		parserAttribute.setLastUpdatedByStaffId(staffId);
		parserAttribute.setCreatedDate(new Date());
		parserAttribute.setLastUpdatedDate(new Date());
		
		int attributeCount = 0;
		if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)){
			AsciiParserAttribute asciiParserAttribute = new AsciiParserAttribute();
			BeanUtils.copyProperties(parserAttribute, asciiParserAttribute);
			
			/** Start of MED-9753 */
			/*if(!org.apache.commons.lang.StringUtils.isEmpty(asciiParserAttribute.getPortUnifiedField())){
				asciiParserAttribute.setPortUnifiedField(asciiParserAttribute.getPortUnifiedField().toLowerCase());
			}*/
			/** End of MED-9753 */
			attributeCount = parserAttributeDao.getAttributeCountByIPPortName(mappingId, asciiParserAttribute.getUnifiedField(),0);
			int attributeCount2 = parserAttributeDao.getAttributeCountByIPPortName(mappingId, asciiParserAttribute.getPortUnifiedField(),0);
			attributeCount = attributeCount+attributeCount2;
		}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
			attributeCount = parserAttributeDao.getAttributeCountByName(mappingId, null,parserAttribute.getUnifiedField(), 0);
		}else if(!EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) && !EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){ 
			attributeCount = parserAttributeDao.getAttributeCountByName(mappingId, parserAttribute.getSourceField(),parserAttribute.getUnifiedField(), 0);
		}
		
		if (attributeCount > 0 &&  !(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType) || EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType)||EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType) ||EngineConstants.XLS_PARSING_PLUGIN.equals(pluginType)  ||EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType) || pluginType.equalsIgnoreCase(EngineConstants.TAP_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.RAP_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.NRTRDE_PARSING_PLUGIN))) {
			logger.info("duplicate attribute source field found:" + parserAttribute.getSourceField());
			responseObject.setSuccess(false);
			
			if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)){
				responseObject.setResponseCode(ResponseCode.DUPLICATE_UNIFIED_OR_PORT_UNIFIED_ATTRIBUTE_FOUND);
			}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) || EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType) ){
				responseObject.setResponseCode(ResponseCode.DUPLICATE_UNIFIED_ATTRIBUTE_FOUND);
			}else{
				responseObject.setResponseCode(ResponseCode.DUPLICATE_ATTRIBUTE_FOUND);
			}
		} else {

			responseObject = parserMappingService.getMappingDetailsById(mappingId, pluginType);

			if (responseObject.isSuccess()) {
				ParserMapping parserMapping = (ParserMapping) responseObject.getObject();
				parserAttribute.setParserMapping(parserMapping);
				if(parserAttribute.getAttributeOrder()<=0){
					if(parserMapping.getParserAttributes()!=null){
						int count = parserMapping.getParserAttributes().size();
						parserAttribute.setAttributeOrder(count+1);
					}
				}
				parserAttributeDao.save(parserAttribute);

				if (parserAttribute.getId() > 0) {
					responseObject.setSuccess(true);
					responseObject.setObject(parserAttribute);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				} else {
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_FAIL);
				}

			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_FAIL);
			}
		}

		return responseObject;
	}

	/**
	 * Method will check parser attribute name for unique source field name.
	 * 
	 * @param attributeId
	 * @param attributeName
	 * @param mappingId
	 * @return
	 */
	@Transactional(readOnly = true)
	public boolean isAttributeUniqueForUpdate(int attributeId, String unifiedField, String attributeName, int mappingId, String pluginType) {
		if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType))
			attributeName = "";
		ParserAttribute parserAttribute = parserAttributeDao.checkUniqueAttributeNameForUpdate(mappingId, unifiedField, attributeName,pluginType);
		boolean isUnique;
		if (parserAttribute != null) {
			// If ID is same , then it is same attribute object
			
			if (attributeId == parserAttribute.getId()) {
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

	/**
	 * @param attributeId
	 * @return
	 */
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getAttributeById(int attributeId) {
		ResponseObject responseObject = new ResponseObject();

		if (attributeId > 0) {

			ParserAttribute parserAttribute = parserAttributeDao.findByPrimaryKey(ParserAttribute.class, attributeId);

			if (parserAttribute != null) {
				responseObject.setSuccess(true);
				responseObject.setObject(parserAttribute);
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.FAIL_GET_ATTRIBUTE_BY_ID);
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_ATTRIBUTE_BY_ID);
		}
		return responseObject;
	}

	/**
	 * Method will delete selected attribute for any parser configuration.
	 * 
	 * @param attributeId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_PARSER_ATTRIBUTE, actionType = BaseConstants.DELETE_ACTION,
			currentEntity = ParserAttribute.class, ignorePropList = "")
	public ResponseObject deleteAttribute(int attributeId, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if (attributeId > 0) {

			ParserAttribute parserAttribute = parserAttributeDao.findByPrimaryKey(ParserAttribute.class, attributeId);

			if (parserAttribute != null) {

				parserAttribute.setLastUpdatedDate(new Date());
				parserAttribute.setLastUpdatedByStaffId(staffId);
				parserAttribute.setStatus(StateEnum.DELETED);
				if(org.apache.commons.lang3.StringUtils.isNotBlank(parserAttribute.getSourceField())){
					parserAttribute.setSourceField(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, parserAttribute.getSourceField()));
				}
				if(org.apache.commons.lang3.StringUtils.isBlank(parserAttribute.getUnifiedField())){
					parserAttribute.setUnifiedField("NA");
				}
				parserAttribute.setUnifiedField(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, parserAttribute.getUnifiedField()));

				parserAttributeDao.merge(parserAttribute);
				responseObject.setSuccess(true);
				responseObject.setObject(parserAttribute);
				responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);

			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
			}

		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Delete Multiple Parser Attributes
	 * 
	 * @param attributeId
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_PARSER_ATTRIBUTE, actionType = BaseConstants.DELETE_MULTIPLE_ACTION,
			currentEntity = ParserAttribute.class, ignorePropList = "")
	public ResponseObject deleteParserAttributes(String attributeIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if (!StringUtils.isEmpty(attributeIds)) {
			String[] attributeIdList = attributeIds.split(",");

			for (int i = 0; i < attributeIdList.length; i++) {
				responseObject = deleteAttribute(Integer.parseInt(attributeIdList[i]), staffId);
			}

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_FAIL);
		}

		return responseObject;
	}

	/**
	 * Update Regex Parser attribute
	 */
	@Override
	@Transactional
	public ResponseObject updateRegExParserAttribute(RegexParserAttribute regExParserAttr) {

		ResponseObject responseObject = new ResponseObject();
		boolean isUniqueAttribute=false;
        RegExPattern regExPattern = regExPatternDao.findByPrimaryKey(RegExPattern.class, regExParserAttr.getPattern().getId());
		
		int attrCount=parserAttributeDao.getAttributeCountByNameForRegex(regExPattern.getId(), null,regExParserAttr.getUnifiedField(),regExParserAttr.getId());
		if(attrCount == 1){
			List<RegexParserAttribute> regexParserAttributeObj=parserAttributeDao.getRegExAttributeByPatternIdAndUnifiedField(regExPattern.getId(),regExParserAttr.getUnifiedField());
			RegexParserAttribute dbRegexParserAttribute=regexParserAttributeObj.get(0);
			if(dbRegexParserAttribute.getId()==regExParserAttr.getId()){
				isUniqueAttribute=true;
			}else {
				isUniqueAttribute=false;
			}
		}else if(attrCount==0) {
			isUniqueAttribute=true;
		}else {
			isUniqueAttribute=false;
		}	
		 /*//MED-8336
		 if(parserAttributeDao.getAttributeCountByNameForRegex(regExPattern.getId(), null,regExParserAttr.getUnifiedField(),regExParserAttr.getId()) > 0){
			isUniqueAttribute=false;
		}*/
		if(isUniqueAttribute){
			regExParserAttr.setPattern(regExPattern);
			parserAttributeDao.merge(regExParserAttr);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_SUCCESS);
			responseObject.setObject(regExParserAttr);
		}else{
			responseObject.setResponseCode(ResponseCode.DUPLICATE_UNIFIED_ATTRIBUTE_FOUND);
			responseObject.setSuccess(false);
		}
		return responseObject;

	}

	/**
	 * Delete Regex Parse Atribute by pattern id
	 */
	@Override
	@Transactional
	public ResponseObject deleteRegExParserAttributeByPatternId(int regExPatternId, int staffId) {

		ResponseObject responseObject = new ResponseObject();
		List<RegexParserAttribute> regExParserAttributeList = parserAttributeDao.getRegExAttributeByPatternId(regExPatternId);
		if (regExParserAttributeList != null) {
			for (int i = 0, size = regExParserAttributeList.size(); i < size; i++) {
				RegexParserAttribute regExParserAttribute = regExParserAttributeList.get(i);

				regExParserAttribute.setLastUpdatedByStaffId(staffId);
				regExParserAttribute.setLastUpdatedDate(new Date());
				regExParserAttribute.setStatus(StateEnum.DELETED);

				parserAttributeDao.merge(regExParserAttribute);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ATTRIBUTE_DELETE_SUCCESS);
		} else {
			logger.debug("No Regex Parser Attribute found with patttern ");
			responseObject.setSuccess(true);
		}
		return responseObject;
	}

	/**
	 * Update Parser Attribute
	 * 
	 * @param parserAttribute
	 * @param mappingId
	 * @param pluginType
	 * @param staffId
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PARSER_ATTRIBUTE, actionType = BaseConstants.UPDATE_ACTION,
			currentEntity = ParserAttribute.class, ignorePropList = "parserMapping")
	public ResponseObject updateParserAttributes(ParserAttribute parserAttribute, int mappingId, String pluginType, int staffId,boolean isUpdateOrdering) {
		ResponseObject responseObject = new ResponseObject();

		parserAttribute.setCreatedByStaffId(staffId);
		parserAttribute.setLastUpdatedByStaffId(staffId);
		parserAttribute.setCreatedDate(new Date());
		parserAttribute.setLastUpdatedDate(new Date());

		boolean isUniqueAttribute=true;
		if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)){
			AsciiParserAttribute asciiParserAttribute = new AsciiParserAttribute();
			BeanUtils.copyProperties(parserAttribute, asciiParserAttribute);
			int count = parserAttributeDao.getAttributeCountByIPPortName(mappingId, asciiParserAttribute.getUnifiedField(),asciiParserAttribute.getId());
			int count2 = parserAttributeDao.getAttributeCountByIPPortName(mappingId, asciiParserAttribute.getPortUnifiedField(),asciiParserAttribute.getId());
			count = count+count2;
			if(count > 0){			
				isUniqueAttribute=false;
			}
		}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
			if(parserAttributeDao.getAttributeCountByName(mappingId, null,parserAttribute.getUnifiedField(),parserAttribute.getId()) > 0){			
				isUniqueAttribute=false;
			}
		}else if(!EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) && !EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
			if(parserAttribute.getSourceField()==null && EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType))
			{
				parserAttribute.setSourceField("");
			}
			isUniqueAttribute = isAttributeUniqueForUpdate(parserAttribute.getId(), parserAttribute.getUnifiedField(), parserAttribute.getSourceField(), mappingId,pluginType);
		}	
		if (isUpdateOrdering || isUniqueAttribute  || EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType) || (pluginType.equalsIgnoreCase(EngineConstants.HTML_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.PDF_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.XLS_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.TAP_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.RAP_PARSING_PLUGIN) || pluginType.equalsIgnoreCase(EngineConstants.NRTRDE_PARSING_PLUGIN)|| pluginType.equalsIgnoreCase(EngineConstants.JSON_PARSING_PLUGIN)|| pluginType.equalsIgnoreCase(EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN))) {
			if (parserAttribute.getId() > 0) {

				ParserAttribute dbParserAttribute;
				
				if ((parserAttribute instanceof ASN1ParserAttribute)){
					
					dbParserAttribute = parserAttributeDao.getASN1ParserAttributeById(parserAttribute.getId());
				}
				else if(parserAttribute instanceof RAPParserAttribute){
					
					dbParserAttribute = parserAttributeDao.getRAPParserAttributeById(parserAttribute.getId());
				}else if(parserAttribute instanceof TAPParserAttribute){
					
					dbParserAttribute = parserAttributeDao.getTAPParserAttributeById(parserAttribute.getId());
				}else if(parserAttribute instanceof NRTRDEParserAttribute){
					
					dbParserAttribute = parserAttributeDao.getNrtrdeParserAttributeById(parserAttribute.getId());
				}
				else{
					dbParserAttribute = parserAttributeDao.findByPrimaryKey(ParserAttribute.class, parserAttribute.getId());
				}

				if (dbParserAttribute != null) {
					dbParserAttribute.setSourceField(parserAttribute.getSourceField());
					dbParserAttribute.setUnifiedField(parserAttribute.getUnifiedField());
					dbParserAttribute.setDefaultValue(parserAttribute.getDefaultValue());
					dbParserAttribute.setDescription(parserAttribute.getDescription());
					dbParserAttribute.setTrimChars(parserAttribute.getTrimChars());
					if(parserAttribute.getAttributeOrder()>0) {
						dbParserAttribute.setAttributeOrder(parserAttribute.getAttributeOrder());
					}
					dbParserAttribute.setTrimPosition(parserAttribute.getTrimPosition());
					dbParserAttribute.setStatus(StateEnum.ACTIVE);
					
					if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)) {
						if(dbParserAttribute instanceof AsciiParserAttribute && parserAttribute instanceof AsciiParserAttribute){
							AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) parserAttribute;
							AsciiParserAttribute dbAsciiParserAttribute = (AsciiParserAttribute) dbParserAttribute;
							dbAsciiParserAttribute.setDateFormat(asciiParserAttribute.getDateFormat());
							dbAsciiParserAttribute.setSourceFieldFormat(asciiParserAttribute.getSourceFieldFormat());
							dbAsciiParserAttribute.setIpPortSeperator(asciiParserAttribute.getIpPortSeperator());
							dbAsciiParserAttribute.setPortUnifiedField(asciiParserAttribute.getPortUnifiedField());
						} else if(dbParserAttribute instanceof ParserAttribute){
							dbParserAttribute.setDateFormat(parserAttribute.getDateFormat());
							dbParserAttribute.setSourceFieldFormat(parserAttribute.getSourceFieldFormat());
						}
					}
					else if (EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equals(pluginType)) {
						dbParserAttribute.setSourceFieldFormat(parserAttribute.getSourceFieldFormat());
						dbParserAttribute.setDateFormat(parserAttribute.getDateFormat());
					} 
					else if (EngineConstants.XML_PARSING_PLUGIN.equals(pluginType)) {
						dbParserAttribute.setSourceFieldFormat(parserAttribute.getSourceFieldFormat());
					} 
					else if (EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)) {
						ASN1ParserAttribute dbAsn1ParserAttribute = (ASN1ParserAttribute) dbParserAttribute;
						ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
						dbAsn1ParserAttribute.setASN1DataType(asn1ParserAttribute.getASN1DataType());
						dbAsn1ParserAttribute.setChildAttributes(asn1ParserAttribute.getChildAttributes());
						dbAsn1ParserAttribute.setRecordInitilializer(asn1ParserAttribute.isRecordInitilializer());
						dbAsn1ParserAttribute.setSrcDataFormat(asn1ParserAttribute.getSrcDataFormat());
						dbAsn1ParserAttribute.setUnifiedFieldHoldsChoiceId(asn1ParserAttribute.getUnifiedFieldHoldsChoiceId());
					}else if (EngineConstants.RAP_PARSING_PLUGIN.equals(pluginType)) {
						RAPParserAttribute dbRapParserAttribute = (RAPParserAttribute) dbParserAttribute;
						RAPParserAttribute rapParserAttribute = (RAPParserAttribute) parserAttribute;
						dbRapParserAttribute.setASN1DataType(rapParserAttribute.getASN1DataType());
						dbRapParserAttribute.setChildAttributes(rapParserAttribute.getChildAttributes());
						dbRapParserAttribute.setRecordInitilializer(rapParserAttribute.isRecordInitilializer());
						dbRapParserAttribute.setParseAsJson(rapParserAttribute.isParseAsJson());
						dbRapParserAttribute.setSrcDataFormat(rapParserAttribute.getSrcDataFormat());
						dbRapParserAttribute.setUnifiedFieldHoldsChoiceId(rapParserAttribute.getUnifiedFieldHoldsChoiceId());
					}else if (EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)) {
						TAPParserAttribute dbTapParserAttribute = (TAPParserAttribute) dbParserAttribute;
						TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
						dbTapParserAttribute.setASN1DataType(tapParserAttribute.getASN1DataType());
						dbTapParserAttribute.setChildAttributes(tapParserAttribute.getChildAttributes());
						dbTapParserAttribute.setRecordInitilializer(tapParserAttribute.isRecordInitilializer());
						dbTapParserAttribute.setParseAsJson(tapParserAttribute.isParseAsJson());
						dbTapParserAttribute.setSrcDataFormat(tapParserAttribute.getSrcDataFormat());
						dbTapParserAttribute.setUnifiedFieldHoldsChoiceId(tapParserAttribute.getUnifiedFieldHoldsChoiceId());
					}else if (EngineConstants.NRTRDE_PARSING_PLUGIN.equals(pluginType)) {
						NRTRDEParserAttribute dbNrtrdeParserAttribute = (NRTRDEParserAttribute) dbParserAttribute;
						NRTRDEParserAttribute nrtrdeParserAttribute = (NRTRDEParserAttribute) parserAttribute;
						dbNrtrdeParserAttribute.setASN1DataType(nrtrdeParserAttribute.getASN1DataType());
						dbNrtrdeParserAttribute.setChildAttributes(nrtrdeParserAttribute.getChildAttributes());
						dbNrtrdeParserAttribute.setRecordInitilializer(nrtrdeParserAttribute.isRecordInitilializer());
						dbNrtrdeParserAttribute.setParseAsJson(nrtrdeParserAttribute.isParseAsJson());
						dbNrtrdeParserAttribute.setSrcDataFormat(nrtrdeParserAttribute.getSrcDataFormat());
						dbNrtrdeParserAttribute.setUnifiedFieldHoldsChoiceId(nrtrdeParserAttribute.getUnifiedFieldHoldsChoiceId());
					}
					else if (EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
						FixedLengthASCIIParserAttribute dbFixedLengthASCIIParserAttribute = (FixedLengthASCIIParserAttribute)dbParserAttribute;
						FixedLengthASCIIParserAttribute fixedLengthASCIIParserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
						dbFixedLengthASCIIParserAttribute.setStartLength(fixedLengthASCIIParserAttribute.getStartLength());
						dbFixedLengthASCIIParserAttribute.setEndLength(fixedLengthASCIIParserAttribute.getEndLength());
						dbFixedLengthASCIIParserAttribute.setSourceFieldFormat(fixedLengthASCIIParserAttribute.getSourceFieldFormat());
						dbFixedLengthASCIIParserAttribute.setPrefix(fixedLengthASCIIParserAttribute.getPrefix());
						dbFixedLengthASCIIParserAttribute.setPostfix(fixedLengthASCIIParserAttribute.getPostfix());
						dbFixedLengthASCIIParserAttribute.setLength(fixedLengthASCIIParserAttribute.getLength());
						dbFixedLengthASCIIParserAttribute.setRightDelimiter(fixedLengthASCIIParserAttribute.getRightDelimiter());
					} else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
						FixedLengthBinaryParserAttribute dbFixedLengthBinaryParserAttribute=(FixedLengthBinaryParserAttribute)dbParserAttribute;
						FixedLengthBinaryParserAttribute fixedLengthBinaryParserAttribute=(FixedLengthBinaryParserAttribute)parserAttribute;
						dbFixedLengthBinaryParserAttribute.setStartLength(fixedLengthBinaryParserAttribute.getStartLength());
						dbFixedLengthBinaryParserAttribute.setEndLength(fixedLengthBinaryParserAttribute.getEndLength());
						dbFixedLengthBinaryParserAttribute.setReadAsBits(fixedLengthBinaryParserAttribute.isReadAsBits());
						dbFixedLengthBinaryParserAttribute.setBitStartLength(fixedLengthBinaryParserAttribute.getBitStartLength());
						dbFixedLengthBinaryParserAttribute.setBitEndLength(fixedLengthBinaryParserAttribute.getBitEndLength());
						dbFixedLengthBinaryParserAttribute.setSourceFieldFormat(fixedLengthBinaryParserAttribute.getSourceFieldFormat());
                        dbFixedLengthBinaryParserAttribute.setPrefix(fixedLengthBinaryParserAttribute.getPrefix());
                        dbFixedLengthBinaryParserAttribute.setPostfix(fixedLengthBinaryParserAttribute.getPostfix());
                        dbFixedLengthBinaryParserAttribute.setLength(fixedLengthBinaryParserAttribute.getLength());
                        dbFixedLengthBinaryParserAttribute.setRightDelimiter(fixedLengthBinaryParserAttribute.getRightDelimiter());
                        dbFixedLengthBinaryParserAttribute.setMultiRecord(fixedLengthBinaryParserAttribute.isMultiRecord());
					}else if(EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)){
						PDFParserAttribute dbPdfParserAttribute=(PDFParserAttribute)dbParserAttribute;
						PDFParserAttribute pdfParserAttribute=(PDFParserAttribute)parserAttribute;
						dbPdfParserAttribute.setLocation(pdfParserAttribute.getLocation());
						dbPdfParserAttribute.setColumnStartLocation(pdfParserAttribute.getColumnStartLocation());
						dbPdfParserAttribute.setColumnIdentifier(pdfParserAttribute.getColumnIdentifier());
						dbPdfParserAttribute.setReferenceRow(pdfParserAttribute.getReferenceRow());
						dbPdfParserAttribute.setReferenceCol(pdfParserAttribute.getReferenceCol());
						dbPdfParserAttribute.setTableFooter(pdfParserAttribute.isTableFooter());
						dbPdfParserAttribute.setColumnStartsWith(pdfParserAttribute.getColumnStartsWith());
						dbPdfParserAttribute.setColumnEndsWith(pdfParserAttribute.getColumnEndsWith());
						dbPdfParserAttribute.setPageNumber(pdfParserAttribute.getPageNumber());
						dbPdfParserAttribute.setMandatory(pdfParserAttribute.getMandatory());
						dbPdfParserAttribute.setMultiLineAttribute(pdfParserAttribute.isMultiLineAttribute());
						dbPdfParserAttribute.setMultipleValues(pdfParserAttribute.isMultipleValues());
						dbPdfParserAttribute.setRowTextAlignment(pdfParserAttribute.getRowTextAlignment());
						dbPdfParserAttribute.setValueSeparator(pdfParserAttribute.getValueSeparator());
					}else if (EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType)) {
						NatFlowParserAttribute dbNatFlowParserAttribute = (NatFlowParserAttribute)dbParserAttribute;
						NatFlowParserAttribute natFlowParserAttribute = (NatFlowParserAttribute)parserAttribute;
						dbNatFlowParserAttribute.setSourceFieldFormat(parserAttribute.getSourceFieldFormat());
						dbNatFlowParserAttribute.setDestDateFormat(natFlowParserAttribute.getDestDateFormat());
					}else if (EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType)) {
						HtmlParserAttribute dbHtmlParserAttribute=(HtmlParserAttribute)dbParserAttribute;
						HtmlParserAttribute htmlParserAttribute=(HtmlParserAttribute)parserAttribute;
						dbHtmlParserAttribute.setFieldIdentifier(htmlParserAttribute.getFieldIdentifier());
						dbHtmlParserAttribute.setFieldSectionId(htmlParserAttribute.getFieldSectionId());
						dbHtmlParserAttribute.setFieldExtractionMethod(htmlParserAttribute.getFieldExtractionMethod());
						dbHtmlParserAttribute.setContainsFieldAttribute(htmlParserAttribute.getContainsFieldAttribute());
						dbHtmlParserAttribute.setValueSeparator(htmlParserAttribute.getValueSeparator());
						dbHtmlParserAttribute.setValueIndex(htmlParserAttribute.getValueIndex());
						if(htmlParserAttribute.getTdNo()!=null){
							dbHtmlParserAttribute.setTdNo(htmlParserAttribute.getTdNo());
						}
					}else if (EngineConstants.XLS_PARSING_PLUGIN.equals(pluginType)) {
						XlsParserAttribute dbXlsParserAttribute=(XlsParserAttribute)dbParserAttribute;
						XlsParserAttribute xlsParserAttribute=(XlsParserAttribute)parserAttribute;
						dbXlsParserAttribute.setColumnStartsWith(xlsParserAttribute.getColumnStartsWith());
						dbXlsParserAttribute.setExcelCol(xlsParserAttribute.getExcelCol());
						dbXlsParserAttribute.setExcelRow(xlsParserAttribute.getExcelRow());
						dbXlsParserAttribute.setRelativeExcelRow(xlsParserAttribute.getRelativeExcelRow());
						dbXlsParserAttribute.setTableFooter(xlsParserAttribute.isTableFooter());
						dbXlsParserAttribute.setStartsWith(xlsParserAttribute.getStartsWith());
						dbXlsParserAttribute.setColumnContains(xlsParserAttribute.getColumnContains());
						dbXlsParserAttribute.setTableRowAttribute(xlsParserAttribute.isTableRowAttribute());

					}else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)) {
						VarLengthAsciiParserAttribute dbVAsciiParserAttribute=(VarLengthAsciiParserAttribute)dbParserAttribute;
						VarLengthAsciiParserAttribute vAsciiParserAttribute=(VarLengthAsciiParserAttribute)parserAttribute;
						dbVAsciiParserAttribute.setSourceFieldFormat(vAsciiParserAttribute.getSourceFieldFormat());
						dbVAsciiParserAttribute.setDateFormat(vAsciiParserAttribute.getDateFormat());
						dbVAsciiParserAttribute.setSourceFieldName(vAsciiParserAttribute.getSourceFieldName());
					} else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
						VarLengthBinaryParserAttribute dbVarLengthBinaryParserAttribute=(VarLengthBinaryParserAttribute)dbParserAttribute;
						VarLengthBinaryParserAttribute varLengthBinaryParserAttribute=(VarLengthBinaryParserAttribute)parserAttribute;
						dbVarLengthBinaryParserAttribute.setStartLength(varLengthBinaryParserAttribute.getStartLength());
						dbVarLengthBinaryParserAttribute.setEndLength(varLengthBinaryParserAttribute.getEndLength());
						dbVarLengthBinaryParserAttribute.setSourceFieldFormat(varLengthBinaryParserAttribute.getSourceFieldFormat());
						dbVarLengthBinaryParserAttribute.setDateFormat(varLengthBinaryParserAttribute.getDateFormat());
						dbVarLengthBinaryParserAttribute.setPrefix(varLengthBinaryParserAttribute.getPrefix());
						dbVarLengthBinaryParserAttribute.setPostfix(varLengthBinaryParserAttribute.getPostfix());
						dbVarLengthBinaryParserAttribute.setLength(varLengthBinaryParserAttribute.getLength());
						dbVarLengthBinaryParserAttribute.setRightDelimiter(varLengthBinaryParserAttribute.getRightDelimiter());
						dbVarLengthBinaryParserAttribute.setSourceFieldName(varLengthBinaryParserAttribute.getSourceFieldName());
					}
					else if(EngineConstants.JSON_PARSING_PLUGIN.equals(pluginType)) {
							JsonParserAttribute jsonParserAttribute = (JsonParserAttribute) parserAttribute;
							JsonParserAttribute dbJsonParserAttribute = (JsonParserAttribute) dbParserAttribute;
							dbJsonParserAttribute.setDateFormat(jsonParserAttribute.getDateFormat());
							dbJsonParserAttribute.setSourceFieldFormat(jsonParserAttribute.getSourceFieldFormat());
					}
					
					dbParserAttribute.setLastUpdatedDate(new Date());
					dbParserAttribute.setLastUpdatedByStaffId(staffId);

					parserAttributeDao.merge(dbParserAttribute);
					responseObject.setSuccess(true);
					responseObject.setObject(parserAttribute);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_SUCCESS);

				} else {
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_FAIL);
				}
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_FAIL);
			}
			
		}else{
			logger.info("duplicate attribute source field found:" + parserAttribute.getSourceField());
			responseObject.setSuccess(false);
			if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)){
				responseObject.setResponseCode(ResponseCode.DUPLICATE_UNIFIED_OR_PORT_UNIFIED_ATTRIBUTE_FOUND);
			}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) || EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
				responseObject.setResponseCode(ResponseCode.DUPLICATE_UNIFIED_ATTRIBUTE_FOUND);
			}else{
				responseObject.setResponseCode(ResponseCode.DUPLICATE_ATTRIBUTE_FOUND);
			}
		}
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getVarLengthBinaryAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList = parserAttributeDao.getVarLengthBinaryAttributeListByMappingId(mappingId, asn1attrtype);

		if (attributeList != null && !attributeList.isEmpty()) {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(true);
		} else {
			responseObject.setObject(attributeList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Method will validate the imported parser attributes.
	 * 
	 * @param parserAttribute
	 * @param importErrorList
	 */
	@Override
	public void validateImportedAttributesDetails(ParserAttribute parserAttribute, List<ImportValidationErrors> importErrorList) {
		if (parserAttribute.getSourceField() != null) {
			parserAttributeValidator.validateParserAttributeParameter(parserAttribute, null, parserAttribute.getSourceField(), true, importErrorList);
		}

	}

	/**
	 * Method will iterate and add attribute list to mapping object.
	 * 
	 * @param attributeList
	 */
	/*@Override
	public void importParserAttribute(ParserMapping parserMapping) {
		Date date = new Date();
		List<ParserAttribute> attributeList = parserMapping.getParserAttributes();
		List<ParserAttribute> attributeListAssociatedByGroup = new ArrayList<>(0);
		if (attributeList != null && !attributeList.isEmpty()) {
			logger.debug("Iterating parser attributes");
			for (ParserAttribute attribute : attributeList) {
				if(!attribute.isAssociatedByGroup()){
					attribute.setId(0);
					attribute.setCreatedByStaffId(parserMapping.getCreatedByStaffId());
					attribute.setCreatedDate(date);
					attribute.setLastUpdatedDate(date);
					attribute.setLastUpdatedByStaffId(parserMapping.getCreatedByStaffId());
					attribute.setParserMapping(parserMapping);
				} else {
					attributeListAssociatedByGroup.add(attribute);
				}  
			}
			attributeList.removeAll(attributeListAssociatedByGroup);
			parserMapping.setParserAttributes(attributeList);
		} else {
			logger.debug("No attribute found for parser mapping " + parserMapping.getName());
		}
		List<ParserGroupAttribute> groupAttributeList = parserMapping.getGroupAttributeList();
		List<ParserGroupAttribute> groupAttributeListAssociatedByGroup = new ArrayList<>(0);
		if(groupAttributeList != null && !groupAttributeList.isEmpty()){
			logger.debug("Found " + groupAttributeList.size()  + " group attributes for parser mapping " + parserMapping.getName());
			for(ParserGroupAttribute groupAttribute: groupAttributeList){
				if(!groupAttribute.isAssociatedByGroup()){
					groupAttribute.setId(0);
					groupAttribute.setCreatedByStaffId(parserMapping.getCreatedByStaffId());
					groupAttribute.setCreatedDate(date);
					groupAttribute.setLastUpdatedDate(date);
					groupAttribute.setLastUpdatedByStaffId(parserMapping.getCreatedByStaffId());
					groupAttribute.setParserMapping(parserMapping);
					parserGroupAttributeService.setGroupAttributeHierarchyForImport(groupAttribute,parserMapping);   					
				} else {
					groupAttributeListAssociatedByGroup.add(groupAttribute);
				} 
			}
			groupAttributeList.removeAll(groupAttributeListAssociatedByGroup);
			parserMapping.setGroupAttributeList(groupAttributeList); 

		}else{
			logger.debug("No group attribute found for composer mapping " + parserMapping.getName());
		}
	}*/
	
	@Override
	public void importParserAttribute(ParserMapping parserMapping) {
		Date date = new Date();
		List<ParserAttribute> attributeList = parserMapping.getParserAttributes();
		List<ParserAttribute> newAttributeListForAscii = new ArrayList<>(0);
		List<ParserAttribute> attributeListAssociatedByGroup = new ArrayList<>(0);
		String pluginType = parserMapping.getParserType().getAlias();
		
		if (attributeList != null && !attributeList.isEmpty()) {
			logger.debug("Iterating parser attributes");
			for (ParserAttribute attribute : attributeList) {
				if(!attribute.isAssociatedByGroup()){
					
					attribute.setId(0);
					attribute.setCreatedByStaffId(parserMapping.getCreatedByStaffId());
					attribute.setCreatedDate(date);
					attribute.setLastUpdatedDate(date);
					attribute.setLastUpdatedByStaffId(parserMapping.getCreatedByStaffId());
					attribute.setParserMapping(parserMapping);
					
					if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)) {
						AsciiParserAttribute asciiParserAttribute = new AsciiParserAttribute();
						BeanUtils.copyProperties(attribute, asciiParserAttribute);	
						newAttributeListForAscii.add(asciiParserAttribute);
					}
				} else {
					attributeListAssociatedByGroup.add(attribute);
				}  
			}
			attributeList.removeAll(attributeListAssociatedByGroup);
			if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType)) {
				parserMapping.setParserAttributes(newAttributeListForAscii);
			}else {
				parserMapping.setParserAttributes(attributeList);	
			}
			
		} else {
			logger.debug("No attribute found for parser mapping " + parserMapping.getName());
		}
		if(!EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
			List<ParserGroupAttribute> groupAttributeList = parserMapping.getGroupAttributeList();
			List<ParserGroupAttribute> groupAttributeListAssociatedByGroup = new ArrayList<>(0);
			if(groupAttributeList != null && !groupAttributeList.isEmpty()){
				logger.debug("Found " + groupAttributeList.size()  + " group attributes for parser mapping " + parserMapping.getName());
				for(ParserGroupAttribute groupAttribute: groupAttributeList){
					if(!groupAttribute.isAssociatedByGroup()){
						groupAttribute.setId(0);
						groupAttribute.setCreatedByStaffId(parserMapping.getCreatedByStaffId());
						groupAttribute.setCreatedDate(date);
						groupAttribute.setLastUpdatedDate(date);
						groupAttribute.setLastUpdatedByStaffId(parserMapping.getCreatedByStaffId());
						groupAttribute.setParserMapping(parserMapping);
						parserGroupAttributeService.setGroupAttributeHierarchyForImport(groupAttribute,parserMapping);   					
					} else {
						groupAttributeListAssociatedByGroup.add(groupAttribute);
					} 
				}
				groupAttributeList.removeAll(groupAttributeListAssociatedByGroup);
				parserMapping.setGroupAttributeList(groupAttributeList); 

			}else{
				logger.debug("No group attribute found for composer mapping " + parserMapping.getName());
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public List<ParserAttribute> getASN1PaginatedList(int mappingId, int startIndex, int limit, String sidx, String sord,ASN1ATTRTYPE asn1attrtype) {

		Map<String, Object> attributeConditionsList = parserAttributeDao.getASN1AttributeConditionList(mappingId,asn1attrtype);
		return parserAttributeDao.getPaginatedList(ParserAttribute.class, (List<Criterion>) attributeConditionsList.get("conditions"),
				(HashMap<String, String>) attributeConditionsList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@Override
	public void saveParserAttributeForImport(ParserAttribute exportedParserAttribute, ParserMapping dbParserMapping, RegExPattern regexPattern) {
		exportedParserAttribute.setId(0);
		exportedParserAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedParserAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		
		if(exportedParserAttribute instanceof RegexParserAttribute && regexPattern != null) {
			((RegexParserAttribute) exportedParserAttribute).setPattern(regexPattern);
		} else {
			exportedParserAttribute.setParserMapping(dbParserMapping);
		}
		if(dbParserMapping!=null){
		exportedParserAttribute.setCreatedByStaffId(dbParserMapping.getCreatedByStaffId());
		exportedParserAttribute.setLastUpdatedByStaffId(dbParserMapping.getLastUpdatedByStaffId());
		}
	}
	
	@Override
	public void updateParserAttributeForImport(ParserAttribute dbParserAttribute, ParserAttribute exportedParserAttribute) {
		if(dbParserAttribute instanceof AsciiParserAttribute
				&& exportedParserAttribute instanceof AsciiParserAttribute) {
			iterateAsciiParserAttribute((AsciiParserAttribute) dbParserAttribute, (AsciiParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof ASN1ParserAttribute
				&& exportedParserAttribute instanceof ASN1ParserAttribute) {
			iterateAsnParserAttribute((ASN1ParserAttribute) dbParserAttribute, (ASN1ParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof FixedLengthASCIIParserAttribute
				&& exportedParserAttribute instanceof FixedLengthASCIIParserAttribute) {
			iterateFixedLengthAsciiParserAttribute((FixedLengthASCIIParserAttribute) dbParserAttribute, (FixedLengthASCIIParserAttribute) exportedParserAttribute);
		}else if(dbParserAttribute instanceof FixedLengthBinaryParserAttribute
				&& exportedParserAttribute instanceof FixedLengthBinaryParserAttribute) {
			iterateFixedLengthBinaryParserAttribute((FixedLengthBinaryParserAttribute) dbParserAttribute, (FixedLengthBinaryParserAttribute) exportedParserAttribute);
		}else if(dbParserAttribute instanceof PDFParserAttribute 
				&& exportedParserAttribute instanceof PDFParserAttribute) {
			iteratePDFParserAttribute((PDFParserAttribute) dbParserAttribute, (PDFParserAttribute) exportedParserAttribute);
		}else if(dbParserAttribute instanceof RegexParserAttribute
				&& exportedParserAttribute instanceof RegexParserAttribute) {
			iterateRegexParserAttribute((RegexParserAttribute) dbParserAttribute, (RegexParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof XMLParserAttribute
				&& exportedParserAttribute instanceof XMLParserAttribute) {
			iterateXmlParserAttribute((XMLParserAttribute) dbParserAttribute, (XMLParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof TAPParserAttribute
				&& exportedParserAttribute instanceof TAPParserAttribute) {
			iterateTapParserAttribute((TAPParserAttribute) dbParserAttribute, (TAPParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof RAPParserAttribute
				&& exportedParserAttribute instanceof RAPParserAttribute) {
			iterateRapParserAttribute((RAPParserAttribute) dbParserAttribute, (RAPParserAttribute) exportedParserAttribute);
		} else if(dbParserAttribute instanceof NRTRDEParserAttribute
				&& exportedParserAttribute instanceof NRTRDEParserAttribute) {
			iterateNrtrdeParserAttribute((NRTRDEParserAttribute) dbParserAttribute, (NRTRDEParserAttribute) exportedParserAttribute);
		} else {
			iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
		}
	}
	
	public void iterateAsciiParserAttribute(AsciiParserAttribute dbParserAttribute, AsciiParserAttribute exportedParserAttribute) {
		dbParserAttribute.setPortUnifiedField(exportedParserAttribute.getPortUnifiedField());
		dbParserAttribute.setIpPortSeperator(exportedParserAttribute.getIpPortSeperator());
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateAsnParserAttribute(ASN1ParserAttribute dbParserAttribute, ASN1ParserAttribute exportedParserAttribute) {
		dbParserAttribute.setAttrType(exportedParserAttribute.getAttrType());
		dbParserAttribute.setASN1DataType(exportedParserAttribute.getASN1DataType());
		dbParserAttribute.setSrcDataFormat(exportedParserAttribute.getSrcDataFormat());
		dbParserAttribute.setChildAttributes(exportedParserAttribute.getChildAttributes());
		dbParserAttribute.setRecordInitilializer(exportedParserAttribute.isRecordInitilializer());
		dbParserAttribute.setUnifiedFieldHoldsChoiceId(exportedParserAttribute.getUnifiedFieldHoldsChoiceId());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateTapParserAttribute(TAPParserAttribute dbParserAttribute, TAPParserAttribute exportedParserAttribute) {
		dbParserAttribute.setAttrType(exportedParserAttribute.getAttrType());
		dbParserAttribute.setASN1DataType(exportedParserAttribute.getASN1DataType());
		dbParserAttribute.setSrcDataFormat(exportedParserAttribute.getSrcDataFormat());
		dbParserAttribute.setChildAttributes(exportedParserAttribute.getChildAttributes());
		dbParserAttribute.setRecordInitilializer(exportedParserAttribute.isRecordInitilializer());
		dbParserAttribute.setUnifiedFieldHoldsChoiceId(exportedParserAttribute.getUnifiedFieldHoldsChoiceId());
		dbParserAttribute.setParseAsJson(exportedParserAttribute.isParseAsJson());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateRapParserAttribute(RAPParserAttribute dbParserAttribute, RAPParserAttribute exportedParserAttribute) {
		dbParserAttribute.setAttrType(exportedParserAttribute.getAttrType());
		dbParserAttribute.setASN1DataType(exportedParserAttribute.getASN1DataType());
		dbParserAttribute.setSrcDataFormat(exportedParserAttribute.getSrcDataFormat());
		dbParserAttribute.setChildAttributes(exportedParserAttribute.getChildAttributes());
		dbParserAttribute.setRecordInitilializer(exportedParserAttribute.isRecordInitilializer());
		dbParserAttribute.setUnifiedFieldHoldsChoiceId(exportedParserAttribute.getUnifiedFieldHoldsChoiceId());
		dbParserAttribute.setParseAsJson(exportedParserAttribute.isParseAsJson());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateNrtrdeParserAttribute(NRTRDEParserAttribute dbParserAttribute, NRTRDEParserAttribute exportedParserAttribute) {
		dbParserAttribute.setAttrType(exportedParserAttribute.getAttrType());
		dbParserAttribute.setASN1DataType(exportedParserAttribute.getASN1DataType());
		dbParserAttribute.setSrcDataFormat(exportedParserAttribute.getSrcDataFormat());
		dbParserAttribute.setChildAttributes(exportedParserAttribute.getChildAttributes());
		dbParserAttribute.setRecordInitilializer(exportedParserAttribute.isRecordInitilializer());
		dbParserAttribute.setUnifiedFieldHoldsChoiceId(exportedParserAttribute.getUnifiedFieldHoldsChoiceId());
		dbParserAttribute.setParseAsJson(exportedParserAttribute.isParseAsJson());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateFixedLengthAsciiParserAttribute(FixedLengthASCIIParserAttribute dbParserAttribute, FixedLengthASCIIParserAttribute exportedParserAttribute) {
		dbParserAttribute.setStartLength(exportedParserAttribute.getStartLength());
		dbParserAttribute.setEndLength(exportedParserAttribute.getEndLength());
		dbParserAttribute.setPrefix(exportedParserAttribute.getPrefix());
		dbParserAttribute.setPostfix(exportedParserAttribute.getPostfix());
		dbParserAttribute.setLength(exportedParserAttribute.getLength());
		dbParserAttribute.setRightDelimiter(exportedParserAttribute.getRightDelimiter());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateFixedLengthBinaryParserAttribute(FixedLengthBinaryParserAttribute dbParserAttribute, FixedLengthBinaryParserAttribute exportedParserAttribute) {
		dbParserAttribute.setStartLength(exportedParserAttribute.getStartLength());
		dbParserAttribute.setEndLength(exportedParserAttribute.getEndLength());
		dbParserAttribute.setReadAsBits(exportedParserAttribute.isReadAsBits());
		dbParserAttribute.setBitStartLength(exportedParserAttribute.getBitStartLength());
		dbParserAttribute.setBitEndLength(exportedParserAttribute.getBitEndLength());
		dbParserAttribute.setPrefix(exportedParserAttribute.getPrefix());
		dbParserAttribute.setPostfix(exportedParserAttribute.getPostfix());
		dbParserAttribute.setLength(exportedParserAttribute.getLength());
		dbParserAttribute.setRightDelimiter(exportedParserAttribute.getRightDelimiter());
		dbParserAttribute.setMultiRecord(exportedParserAttribute.isMultiRecord());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iteratePDFParserAttribute(PDFParserAttribute dbParserAttribute, PDFParserAttribute exportedParserAttribute) {
		dbParserAttribute.setLocation(exportedParserAttribute.getLocation());
		dbParserAttribute.setColumnStartLocation(exportedParserAttribute.getColumnStartLocation());
		dbParserAttribute.setColumnIdentifier(exportedParserAttribute.getColumnIdentifier());
		dbParserAttribute.setReferenceRow(exportedParserAttribute.getReferenceRow());
		dbParserAttribute.setColumnStartsWith(exportedParserAttribute.getColumnStartsWith());
		dbParserAttribute.setPageNumber(exportedParserAttribute.getPageNumber());
		dbParserAttribute.setColumnEndsWith(exportedParserAttribute.getColumnEndsWith());
		dbParserAttribute.setValueSeparator(exportedParserAttribute.getValueSeparator());
		dbParserAttribute.setMandatory(exportedParserAttribute.getMandatory());
		dbParserAttribute.setMultipleValues(exportedParserAttribute.isMultipleValues());
				
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateRegexParserAttribute(RegexParserAttribute dbParserAttribute, RegexParserAttribute exportedParserAttribute) {
		dbParserAttribute.setSeqNumber(exportedParserAttribute.getSeqNumber());
		dbParserAttribute.setRegex(exportedParserAttribute.getRegex());
		dbParserAttribute.setSampleData(exportedParserAttribute.getSampleData());
		
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateXmlParserAttribute(XMLParserAttribute dbParserAttribute, XMLParserAttribute exportedParserAttribute) {
		iterateParserAttribute(dbParserAttribute, exportedParserAttribute);
	}
	
	public void iterateParserAttribute(ParserAttribute dbParserAttribute, ParserAttribute exportedParserAttribute) {
		dbParserAttribute.setDefaultValue(exportedParserAttribute.getDefaultValue());
		dbParserAttribute.setTrimChars(exportedParserAttribute.getTrimChars());
		dbParserAttribute.setTrimPosition(exportedParserAttribute.getTrimPosition());
		dbParserAttribute.setDescription(exportedParserAttribute.getDescription());
		dbParserAttribute.setAttributeOrder(exportedParserAttribute.getAttributeOrder()); //TODO
		dbParserAttribute.setSourceFieldFormat(exportedParserAttribute.getSourceFieldFormat());
		dbParserAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getASN1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList = parserAttributeDao.getAsn1AttributeListByMappingId(mappingId, asn1attrtype);

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
	public ResponseObject getRAPAttributeListByMappingId(int mappingId, RAPATTRTYPE rapattrtype) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList = parserAttributeDao.getRapAttributeListByMappingId(mappingId, rapattrtype);
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
	public ResponseObject getTAPAttributeListByMappingId(int mappingId, TAPATTRTYPE tapattrtype) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList = parserAttributeDao.getTapAttributeListByMappingId(mappingId, tapattrtype);
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
	@Transactional(readOnly = true)
	public ResponseObject getNRTRDEAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype) {
		logger.debug("Fetching all attribute for device configuration : " + mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> attributeList =parserAttributeDao.getNrtrdeAttributeListByMappingId(mappingId, asn1attrtype);
		if(attributeList!=null && !attributeList.isEmpty()){
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
	public ResponseObject uploadParserAttributesFromCSV(File csvFile, int mappingId, String pluginType, int staffId, String asnActionType,int groupId) {
		logger.debug("inside uploadParserAttributesFromCSV");
		logger.debug("pluginType : " + pluginType);
		logger.debug("mappingId : " + mappingId);		
		ResponseObject responseObject = new ResponseObject();		
		String header =  null;
		String headers[] = null;
		String row = null;
		int hlength=0;
		int rlength=0;		
		boolean isUploadSuccess = true;
		String groupName="";
		String subGroupName="";
		Map<String,List<String>> groupOfGroupMap = new HashMap<String, List<String>>();
		Set<String> tapGroupSet=new HashSet<>();
		List<String> newAttributeList = new ArrayList<>();
		List<ParserGroupAttribute> tapGroupAttributeList=null;
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		String RECORD_FAILURE_MESSAGE = "CSV record attribute upload failed on Line number # ";
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
						responseObject.setObject("CSV Record failed on Line number #"+(i+1));
			    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_HEADER_MISTMATCH);
			    		deleteParserAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
			    		return  responseObject;
					}else {				
						header = headers[0];
						//get appropriate parser object by plugin type and set csv row value by comparing header values
						ParserAttribute parserAttribute = ParserAttributeFactory.getParserAttributeByType(pluginType);
						if(EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType) || EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)|| EngineConstants.XLS_PARSING_PLUGIN.equals(pluginType)){
							if(groupId>0){
								ParserGroupAttribute pga = new ParserGroupAttribute();
								pga.setId(groupId);
								parserAttribute.setParserGroupAttribute(pga);
								parserAttribute.setAssociatedByGroup(true);
							}
						}
						
						if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
							setAsnAttributeActionType(parserAttribute,asnActionType);
						}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
							setVarLengthBinaryAttributeActionType(parserAttribute,asnActionType);
						}
					
						for(int attrCnt = 0; attrCnt< headers.length; attrCnt++) {
							if(headers[attrCnt].equals(EnumParserAttributeHeader.SOURCE_FIELD.getName())) {
								parserAttribute.setSourceField(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.SOURCE_FIELD_NAME.getName())) {
								if(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)) {
									VarLengthAsciiParserAttribute varLengthAsciiParserAttribute = (VarLengthAsciiParserAttribute) parserAttribute;
									varLengthAsciiParserAttribute.setSourceFieldName(rows.get(attrCnt));
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)) {
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									varLengthBinaryParserAttribute.setSourceFieldName(rows.get(attrCnt));
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.UNIFIED_FIELD.getName())) {
								parserAttribute.setUnifiedField(rows.get(attrCnt));
								if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType) || 
										EngineConstants.XML_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.RAP_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.NRTRDE_PARSING_PLUGIN.equals(pluginType) ||
										EngineConstants.REGEX_PARSING_PLUGIN.equals(pluginType)){
									
									String regexVal=Regex.get(SystemParametersConstant.ATTR_UNIFIED_FIELD,BaseConstants.REGEX_MAP_CACHE);
									if (StringUtils.isEmpty(rows.get(attrCnt)) || rows.get(attrCnt).length()>100 ||match(regexVal, rows.get(attrCnt))){
										isUploadSuccess = false;	
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Unified field must begin with a letter of the alphabet and allows only Maximum 100 alphanumeric characters.");
										break;
									}
								}else if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType) || EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
									String regexVal=Regex.get(SystemParametersConstant.ATTR_UNIFIED_FIELD,BaseConstants.REGEX_MAP_CACHE);
									if (!StringUtils.isEmpty(rows.get(attrCnt)) && (rows.get(attrCnt).length()>100 ||match(regexVal, rows.get(attrCnt)))){
										isUploadSuccess = false;	
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Unified field must begin with a letter of the alphabet and allows only Maximum 100 alphanumeric characters.");
										break;
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.DESCRIPTION.getName())) {
								parserAttribute.setDescription(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.DEFAULT_VALUE.getName())) {
								parserAttribute.setDefaultValue(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.TRIM_CHAR.getName())) {
								String trimCharData=rows.get(attrCnt);
								if(trimCharData.contains(BaseConstants.QUOTES)){
									trimCharData=trimCharData.replaceAll(BaseConstants.QUOTES, "\"");//NOSONAR
								}
								parserAttribute.setTrimChars(removeDoubleQuote(trimCharData));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.TRIM_POSITION.getName())) {
								parserAttribute.setTrimPosition(rows.get(attrCnt));
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {									
									boolean flag = false;
									for (TrimPositionEnum trimPositionEnum : TrimPositionEnum.values()) {
								        if (trimPositionEnum.getValue().equalsIgnoreCase(rows.get(attrCnt))) {
								        	parserAttribute.setTrimPosition(trimPositionEnum.getValue());
								        	flag =true;
								        	break;
								        }	
								    }					
									if(!flag) {
										isUploadSuccess = false;
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Trim Postion not available in the system");
										break;	
									}									
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName())) {
								parserAttribute.setSourceFieldFormat(rows.get(attrCnt));
								if(!StringUtils.isEmpty(rows.get(attrCnt))) {
									try {
										if(EngineConstants.ASCII_PARSING_PLUGIN.equals(pluginType) || EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)) {
											SourceFieldFormatASCIIEnum.valueOf(rows.get(attrCnt));
										}
										else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType) || EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
											SourceFieldFormatEnum.valueOf(rows.get(attrCnt)); 
											
										}
									}catch(IllegalArgumentException e) {//NOSONAR
										isUploadSuccess = false;	
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Source field format not available in the system");
										break;	
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.DATE_FORMAT.getName())) {
								parserAttribute.setDateFormat(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.DESTINATION_DATE_FORMAT.getName())) {
								NatFlowParserAttribute natFlowParserAttribute = (NatFlowParserAttribute)parserAttribute;
								natFlowParserAttribute.setDestDateFormat(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.LOCATION.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setLocation(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.COLUMN_IDENTIFIER.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setColumnIdentifier(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.COLUMN_START_LOCATION.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setColumnStartLocation(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.COLUMN_STARTS_WITH.getName())) {
								if(parserAttribute instanceof PDFParserAttribute){
									PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
									pdfParserAttribute.setColumnStartsWith(rows.get(attrCnt));
								}else{
									XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
									xlsParserAttribute.setColumnStartsWith(rows.get(attrCnt));
								}								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.REFERENCE_ROW.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setReferenceRow(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.PAGE_NUMBER.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setPageNumber(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.COLUMN_ENDS_WITH.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setColumnEndsWith(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.MANDATORY.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setMandatory(rows.get(attrCnt).replace("\"", ""));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.REFERENCE_COL.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setReferenceCol(rows.get(attrCnt).replace("\"", ""));
							}
							else if(headers[attrCnt].equals(EnumParserAttributeHeader.ROW_TEXT_ALIGHMENT.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setRowTextAlignment(rows.get(attrCnt).replace("\"", ""));
							}
							else if(headers[attrCnt].equals(EnumParserAttributeHeader.MULTILINE_ATTRIBUTE.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								String multiLineAttr = rows.get(attrCnt); 
								if(((multiLineAttr !=null) && (multiLineAttr.equalsIgnoreCase("true") || multiLineAttr.equalsIgnoreCase("false")))) {
									pdfParserAttribute.setMultiLineAttribute(Boolean.parseBoolean(multiLineAttr.toLowerCase()));
								}
								else{
									isUploadSuccess = false;
									setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Multiple Attribute! Value must either true ot false");
									break;
								}
							}
							else if(headers[attrCnt].equals(EnumParserAttributeHeader.MULTIPLE_VALUES.getName())) {
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								String multipleValuesAttr = rows.get(attrCnt); 
								if(((multipleValuesAttr !=null) && (multipleValuesAttr.equalsIgnoreCase("true") || multipleValuesAttr.equalsIgnoreCase("false")))) {
									pdfParserAttribute.setMultipleValues(Boolean.parseBoolean(multipleValuesAttr.toLowerCase()));
								}
								else{
									isUploadSuccess = false;
									setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Multiple Values! Value must either true ot false");
									break;
								}
							}							
							else if(headers[attrCnt].equals(EnumParserAttributeHeader.IP_PORT_UNIFIED_FIELD.getName())) {
								AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) parserAttribute;
								asciiParserAttribute.setPortUnifiedField(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.IP_PORT_SEPERATOR.getName())) {
								AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) parserAttribute;
								asciiParserAttribute.setIpPortSeperator(rows.get(attrCnt));
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.START_LENGTH.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).equals("-1")) {
										binaryParserAttribute.setStartLength(Integer.parseInt(rows.get(attrCnt)));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid start length! Start Length must be between 0 and 99999");
										break;
									}
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										fixedlengthasciiparserAttribute.setStartLength(-1);
									}else {
										if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).matches("-1")) {
											fixedlengthasciiparserAttribute.setStartLength(Integer.parseInt(rows.get(attrCnt)));
										}
										else{
											isUploadSuccess = false;
											setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid start length! Start Length must be between 0 and 99999");
											break;
										}
									}	
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										varLengthBinaryParserAttribute.setStartLength(-1);
									}else {
										if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).matches("-1")) {
											varLengthBinaryParserAttribute.setStartLength(Integer.parseInt(rows.get(attrCnt)));
										}
										else{
											isUploadSuccess = false;
											setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid start length! Start Length must be between 0 and 99999");
											break;
										}
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.END_LENGTH.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).equals("-1")) {
										binaryParserAttribute.setEndLength(Integer.parseInt(rows.get(attrCnt)));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid end Length! End Length must be between 0 and 99999");
										break;
									}
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										fixedlengthasciiparserAttribute.setEndLength(-1);
									}else {
										if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).matches("-1")) {
											fixedlengthasciiparserAttribute.setEndLength(Integer.parseInt(rows.get(attrCnt)));
										}
										else{
											isUploadSuccess = false;
											setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid end Length! End Length must be between 0 and 99999");
											break;
										}
									}	
								} else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										varLengthBinaryParserAttribute.setEndLength(-1);
									}else {
										if(rows.get(attrCnt).matches("\\d+")|| rows.get(attrCnt).matches("-1")) {
											varLengthBinaryParserAttribute.setEndLength(Integer.parseInt(rows.get(attrCnt)));
										}
										else{
											isUploadSuccess = false;
											setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid end Length! End Length must be between 0 and 99999");
											break;
										}
									}
								}	
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.READ_AS_BITS.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									String bitsFlag = rows.get(attrCnt); 
									if(((bitsFlag !=null) && (bitsFlag.equalsIgnoreCase("true") || bitsFlag.equalsIgnoreCase("false")))) {
										binaryParserAttribute.setReadAsBits(Boolean.parseBoolean(bitsFlag.toLowerCase()));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Read As Bits! Value must either true ot false");
										break;
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.BIT_START_LENGTH.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										binaryParserAttribute.setBitStartLength(-1);
									}else if(rows.get(attrCnt).matches("\\d+") || 
											rows.get(attrCnt).equalsIgnoreCase("-1")) {
										binaryParserAttribute.setBitStartLength(Integer.parseInt(rows.get(attrCnt)));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid bit start length! Bit Start Length must be between 0 and 99999");
										break;
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.BIT_END_LENGTH.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										binaryParserAttribute.setBitEndLength(-1);
									}else if(rows.get(attrCnt).matches("\\d+") || 
											rows.get(attrCnt).equalsIgnoreCase("-1")) {
										binaryParserAttribute.setBitEndLength(Integer.parseInt(rows.get(attrCnt)));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid bit end length! Bit End Length must be between 0 and 99999");
										break;
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.LENGTH.getName())) {								
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryparserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										binaryparserAttribute.setLength("-1");
									}else {
										if(rows.get(attrCnt).matches("\\d+")||rows.get(attrCnt).matches("-1")) {
											binaryparserAttribute.setLength(rows.get(attrCnt));
										}
										else{	
												isUploadSuccess = false;
												setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid Length! Length must be between 0 and 99999");
												break;
										}
									}
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										fixedlengthasciiparserAttribute.setLength("-1");
									}else {
										String lengthData=removeDoubleQuote(rows.get(attrCnt));
										if(lengthData.matches("^[\\d]+[,\\d]*")||lengthData.matches("-1")) {
											fixedlengthasciiparserAttribute.setLength(lengthData);
										}
										else{	
												isUploadSuccess = false;
												setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid Length! Length must be between 0 and 99999");
												break;
										}
									}								
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									if(StringUtils.isEmpty(rows.get(attrCnt))) {
										varLengthBinaryParserAttribute.setLength("-1");
									}else {
										if(rows.get(attrCnt).matches("\\d+")||rows.get(attrCnt).matches("-1")) {
											varLengthBinaryParserAttribute.setLength(rows.get(attrCnt));
										}
										else{	
												isUploadSuccess = false;
												setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+".  '"+ rows.get(attrCnt)+" "+"' Invalid Length! Length must be between 0 and 99999");
												break;
										}
									}
								}								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.PREFIX.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									binaryParserAttribute.setPrefix(rows.get(attrCnt));
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									fixedlengthasciiparserAttribute.setPrefix(rows.get(attrCnt));
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									varLengthBinaryParserAttribute.setPrefix(rows.get(attrCnt));
								}	
								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.POSTFIX.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									binaryParserAttribute.setPostfix(rows.get(attrCnt));
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									fixedlengthasciiparserAttribute.setPostfix(rows.get(attrCnt));
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									varLengthBinaryParserAttribute.setPostfix(rows.get(attrCnt));
								}	
								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.RIGHT_DELIMETER.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									binaryParserAttribute.setRightDelimiter(rows.get(attrCnt));	
								}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthASCIIParserAttribute fixedlengthasciiparserAttribute = (FixedLengthASCIIParserAttribute) parserAttribute;
									fixedlengthasciiparserAttribute.setRightDelimiter(rows.get(attrCnt));
								}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
									varLengthBinaryParserAttribute.setRightDelimiter(rows.get(attrCnt));
								}	
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.MULTI_RECORD.getName())) {
								if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(pluginType)){
									FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) parserAttribute;
									String multiRecordFlag = rows.get(attrCnt); 
									if(((multiRecordFlag !=null) && (multiRecordFlag.equalsIgnoreCase("true") || multiRecordFlag.equalsIgnoreCase("false")))) {
										binaryParserAttribute.setMultiRecord(Boolean.parseBoolean(multiRecordFlag.toLowerCase()));
									}
									else{
										isUploadSuccess = false;
										setResponseObject(responseObject,RECORD_FAILURE_MESSAGE+(i+1)+". '"+ rows.get(attrCnt)+" "+"' Invalid value for Multi Record! Value must either true ot false");
										break;
									}
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.ASN1_DATA_TYPE.getName())) {
								if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
									ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
									asn1ParserAttribute.setASN1DataType(rows.get(attrCnt));	
								}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
									TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
									tapParserAttribute.setASN1DataType(rows.get(attrCnt));	
								}	
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName())) {
								if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
									ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
									asn1ParserAttribute.setChildAttributes(rows.get(attrCnt).replace("\"", ""));
								}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
									TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
									tapParserAttribute.setChildAttributes(rows.get(attrCnt).replace("\"", ""));	
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.IS_RECORD_INITIALIZER.getName())) {
								String recordInitilizer = "true";
								if(!StringUtils.isEmpty(rows.get(attrCnt)))
									recordInitilizer = rows.get(attrCnt);
								if(!StringUtils.isEmpty(recordInitilizer) &&(recordInitilizer.toLowerCase().equals("true")|| recordInitilizer.toLowerCase().equals("false"))){
									if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
										ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
										asn1ParserAttribute.setRecordInitilializer(recordInitilizer.toLowerCase());
									}									
									if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
										TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
										tapParserAttribute.setRecordInitilializer(recordInitilizer.toLowerCase());
									}
								}else if(!StringUtils.isEmpty(recordInitilizer)){
										isUploadSuccess = false;	
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Record Initializer not available in the system");
										break;	
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.CHOICEID_HOLDER_UNIFIED_fIELD.getName())) {
								if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
									ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
									asn1ParserAttribute.setUnifiedFieldHoldsChoiceId(rows.get(attrCnt));
								}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
									TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
									tapParserAttribute.setUnifiedFieldHoldsChoiceId(rows.get(attrCnt));
								}						
								String regexVal=Regex.get(SystemParametersConstant.ATTR_UNIFIED_FIELD,BaseConstants.REGEX_MAP_CACHE);
								if (!StringUtils.isEmpty(rows.get(attrCnt)) && (rows.get(attrCnt).length()>100 ||match(regexVal, rows.get(attrCnt)))){
									isUploadSuccess = false;	
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
									responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' ChoiceId Holder Unified field must begin with a letter of the alphabet and allows only Maximum 100 alphanumeric characters");
									break;
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.SOURCE_FIELD_DATA_FORMAT.getName())) {
								try {
									if(!StringUtils.isEmpty(rows.get(attrCnt))){
										SourceFieldDataFormatEnum.valueOf(rows.get(attrCnt));
										if(EngineConstants.ASN1_PARSING_PLUGIN.equals(pluginType)){
											ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
											asn1ParserAttribute.setSrcDataFormat(SourceFieldDataFormatEnum.valueOf(rows.get(attrCnt)));
										}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
											TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
											tapParserAttribute.setSrcDataFormat(SourceFieldDataFormatEnum.valueOf(rows.get(attrCnt)));
										}										
									}
								}catch(IllegalArgumentException e) {//NOSONAR
									isUploadSuccess = false;	
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
									responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Source field data format not available in the system");
									break;										
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.PARSE_AS_JSON.getName())) {				
								String parseAsJson = "true";
								if(!StringUtils.isEmpty(rows.get(attrCnt)))
									parseAsJson = rows.get(attrCnt);
								if(!StringUtils.isEmpty(parseAsJson) &&(parseAsJson.toLowerCase().equals("true")|| parseAsJson.toLowerCase().equals("false"))){								
									if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)){
										TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
										tapParserAttribute.setParseAsJson(parseAsJson.toLowerCase());
									}
								}else if(!StringUtils.isEmpty(parseAsJson)){
										isUploadSuccess = false;	
										responseObject.setSuccess(false);
										responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
										responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Parse As Json not available in the system");
										break;	
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.GROUP_NAME.getName()) && EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)) {				
								if(!StringUtils.isEmpty(rows.get(attrCnt)) && !rows.get(attrCnt).matches("[A-Za-z]+")){
									isUploadSuccess = false;	
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
									responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Invalid Group Attribute name. Only alphabetical letters are allowed.");
									break;	
								}
								else if(!StringUtils.isEmpty(rows.get(attrCnt)) && rows.get(attrCnt).matches("[A-Za-z]+")) {
									TAPParserAttribute tapParserAttribute = (TAPParserAttribute) parserAttribute;
									tapParserAttribute.setAssociatedByGroup(true);
									groupName = rows.get(attrCnt);
								}		
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.SUB_GROUP_NAME.getName()) && EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)) {				
								/**if(!StringUtils.isEmpty(rows.get(attrCnt)) && !rows.get(attrCnt).matches("[A-Za-z]+")){
									isUploadSuccess = false;	
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
									responseObject.setObject(RECORD_FAILURE_MESSAGE+(i+1)+". '"+rows.get(attrCnt) +"' Invalid Group Attribute name. Only alphabetical letters are allowed.");
									break;	
								}
								else**/
								if (!StringUtils
										.isEmpty(rows.get(attrCnt)) /* && rows.get(attrCnt).matches("[A-Za-z]+") */) {									
									subGroupName = rows.get(attrCnt);
									groupOfGroupMap.put(groupName, Arrays.asList(subGroupName.split(BaseConstants.HASH_SEPARATOR)));
								}	
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.FIELD_IDENTIFIER.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setFieldIdentifier(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.FIELD_EXTRACTION_METHOD.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setFieldExtractionMethod(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.FILD_SECTION_ID.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setFieldSectionId(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.CONTAINS_FIELD_ATTRIBUTE.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setContainsFieldAttribute(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.TD_NO.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setTdNo(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.VALUE_SEPARATOR.getName())) {
								if(EngineConstants.HTML_PARSING_PLUGIN.equals(pluginType)){
									HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
									htmlParserAttribute.setValueSeparator(removeDoubleQuote(rows.get(attrCnt)));	
								}else if(EngineConstants.PDF_PARSING_PLUGIN.equals(pluginType)){
									PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
									pdfParserAttribute.setValueSeparator(removeDoubleQuote(rows.get(attrCnt)));
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.VALUE_INDEX.getName())) {
								HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) parserAttribute;
								htmlParserAttribute.setValueIndex(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.REFERENCE_COL.getName())) {//NOSONAR
								PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
								pdfParserAttribute.setReferenceCol(rows.get(attrCnt));									
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.TABLE_FOOTER.getName())) {
								if(parserAttribute instanceof PDFParserAttribute){
									PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) parserAttribute;
									pdfParserAttribute.setTableFooter(Boolean.parseBoolean(rows.get(attrCnt)));
								}else{
									XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
									xlsParserAttribute.setTableFooter(Boolean.parseBoolean(rows.get(attrCnt)));
								}
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.STARTS_WITH.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setStartsWith(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.EXCEL_ROW.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setExcelRow(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.EXCEL_COL.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setExcelCol(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.RELATIVE_EXCEL_ROW.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setRelativeExcelRow(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.COLUMN_CONTAINS.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setColumnContains(rows.get(attrCnt));								
							}else if(headers[attrCnt].equals(EnumParserAttributeHeader.TABLE_ROW_ATTRIBUTE.getName())) {
								XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) parserAttribute;
								xlsParserAttribute.setTableRowAttribute(Boolean.parseBoolean(rows.get(attrCnt)));
							}else {
								responseObject.setSuccess(false);
					    		responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
					    		responseObject.setObject("CSV Record failed on Line number #"+(i+1));					   
					    		isUploadSuccess=false;
					    		break;
							}
							parserAttribute.setAttributeOrder(i+1);
						}	
						
						//validate parser attribute
						if(!isUploadSuccess) {									
							deleteParserAttributes(EliteUtils.getCSVString(newAttributeList), staffId);		
				    		break;
						}		
						if(!EngineConstants.NATFLOW_PARSING_PLUGIN.equals(pluginType)){
							parserAttributeValidator.validateParserAttributeParameter(parserAttribute, null, null, true, importErrorList);
							checkCSVRecordErrors(responseObject, importErrorList, i+1);
						}	
						//validation failed then return with errors
						if(!responseObject.isSuccess()) {
							deleteParserAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							if(EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType) && !tapGroupSet.isEmpty()) {
					    		for(String att:tapGroupSet) {							
									parserGroupAttributeService.deleteGroupAttributeWithHierarchy(Integer.parseInt(att),staffId,mappingId);
								}
				    		}
							responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);
							//responseObject.setObject("CSV Record failed on Line number #"+(i+1));		
							isUploadSuccess=false;
				    		break;
						}						
						//if validation passes then create parser attribute
						responseObject = createParserAttributes(parserAttribute, mappingId, pluginType, staffId);	
						ResponseObject ro;
						//if tap parser attribute is part of group than add/update TapParserGroup 
						if(responseObject.isSuccess() && EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType) && parserAttribute.isAssociatedByGroup()) {
							if(tapGroupAttributeList==null) {
								tapGroupAttributeList = (List<ParserGroupAttribute>) parserGroupAttributeService.getGroupAttributeListByMappingId(mappingId).getObject();
							}
							ParserGroupAttribute parserGroupAttribute = new ParserGroupAttribute();
							parserGroupAttribute.setName(groupName);
							ParserAttribute attribute = (ParserAttribute) responseObject.getObject();
							List<ParserGroupAttribute> gruopAttributeList =(List<ParserGroupAttribute>) parserGroupAttributeService.getGroupAttributeListByMappingId(mappingId).getObject();
							boolean newGroup=true;
							for(ParserGroupAttribute att:gruopAttributeList) {							
								if(groupName.equals(att.getName())) {
									parserGroupAttribute=att;
									newGroup=false;
								}
							}
							if(newGroup) {
								ro=parserGroupAttributeService.createGroupAttribute(parserGroupAttribute,"",Integer.toString(attribute.getId()), mappingId, pluginType, staffId);
							}else {
								List<ParserAttribute> attList=(List<ParserAttribute>) parserGroupAttributeService.getAttachedAttributeListByGroupId(parserGroupAttribute.getId(),mappingId).getObject();
								String attributes=Integer.toString(attribute.getId());
								for(ParserAttribute att:attList) {							
									attributes=attributes+','+Integer.toString(att.getId());
								}
								ro=parserGroupAttributeService.updateGroupAttribute(parserGroupAttribute,"",attributes, mappingId, pluginType, staffId);
							}
							if(!ro.isSuccess()) {
								responseObject.setSuccess(false);
							}else {
								tapGroupSet.add(Integer.toString(parserGroupAttribute.getId()));
							}
						}
						//if any error while creating attribute do role back - removing all added attribute prior to this attribute
						if(!responseObject.isSuccess()) {
							deleteParserAttributes(EliteUtils.getCSVString(newAttributeList), staffId);	
							//responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);						
							responseObject.setObject("CSV Record failed on Line number #"+(i+1));									    		
							isUploadSuccess=false;
							break;						
						}
						//maintaining newly added attributes list for anytime rollback in future
						
						ParserAttribute newAttribute = (ParserAttribute) responseObject.getObject();
						if (newAttribute.getId() > 0) {
							newAttributeList.add(String.valueOf(newAttribute.getId()));	
						}		
						
					}
				}
			}
			if(isUploadSuccess && EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType) && associateParserAttributeGroupWithSubGroups(groupOfGroupMap, mappingId)) {
				//Delete extra tap group and hierarchy
				List<ParserGroupAttribute> gruopAttributeList =(List<ParserGroupAttribute>) parserGroupAttributeService.getGroupAttributeListByMappingId(mappingId).getObject();
				for(ParserGroupAttribute att:gruopAttributeList) {							
					if(!tapGroupSet.contains(Integer.toString(att.getId()))) {
						parserGroupAttributeService.deleteGroupAttributeWithHierarchy(att.getId(),staffId,mappingId);
					}
				}
			}else if(!isUploadSuccess && EngineConstants.TAP_PARSING_PLUGIN.equals(pluginType)) {
				if(!tapGroupAttributeList.isEmpty()) {//NOSONAR
					for(ParserGroupAttribute groupeAttribute:tapGroupAttributeList) {
						if(!tapGroupSet.isEmpty() && tapGroupSet.contains(Integer.toString(groupeAttribute.getId()))) {
							tapGroupSet.remove(Integer.toString(groupeAttribute.getId()));
						}	
					}
				}	
				if(!tapGroupSet.isEmpty()) {
					parserGroupAttributeService.deleteGroupAttributes(String.join(", ", tapGroupSet), staffId, mappingId);
	    		}
			}
			//if all attributes added successfully then return sucess message to controller
			if(isUploadSuccess) {
				responseObject = new ResponseObject();				
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.PARSER_ATTR_UPLOAD_SUCCESS);
			}
	    }catch(FileNotFoundException e){	
	    	logger.error(e);
	    	logger.trace("Lookup data file not found : ",e);
	    	responseObject.setSuccess(false);
	    	responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);	    		    	
	    }catch (IOException e) {
	    	logger.error(e);
			logger.trace("Problem occurred while reading file : ",e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}catch (Exception e) {	
			logger.error(e);
			logger.trace("Problem occurred while reading file : ",e);			
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.ATRRIBUTE_FILE_UPLOAD_FAILURE);			
		}
		return responseObject;
	}	
	
	private boolean match(String regex, String value){
		String newValue = "";
		if(value != null){
			newValue = value;
		}
    	Pattern pattern = Pattern.compile(regex,Pattern.UNICODE_CHARACTER_CLASS);
    	Matcher matcher = pattern.matcher(newValue);
    	return !matcher.matches();
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
	
	private void setResponseObject(ResponseObject responseObject,String s){
		responseObject.setSuccess(false);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTR_VALIDATION_FAIL);
		responseObject.setObject(s);
	}
	
	private void setAsnAttributeActionType(ParserAttribute parserAttribute,String asnActionType){
		if(asnActionType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE)){
			ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
			asn1ParserAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
		}else if(asnActionType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)){
			ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
			asn1ParserAttribute.setAttrType(ASN1ATTRTYPE.TRAILER);
		}else if(asnActionType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)){//NOSONAR
			ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) parserAttribute;
			asn1ParserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
		}
	}
	
	private void setVarLengthBinaryAttributeActionType(ParserAttribute parserAttribute,String asnActionType){
		if(asnActionType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE)){
			VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
			varLengthBinaryParserAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
		}else {
			VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) parserAttribute;
			varLengthBinaryParserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
		}
	}
	
	@Override
	@Transactional
	public void updateAsciiParserTypeByMappingId(int mappingId) {
		parserAttributeDao.updateAsciiParserTypeByMappingId(mappingId);		
	}
	
	@Override
	@Transactional
	public ResponseObject updateParserAttributesOrder(ParserMapping parserMapping,int staffId) {
		ResponseObject responseObject = new ResponseObject();
		parserMappingDao.update(parserMapping);
		responseObject.setSuccess(true);
		responseObject.setObject(parserMapping);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_UPDATE_SUCCESS);
		return responseObject;
	}
	
	private String removeDoubleQuote(String trimCharVal){
		if(trimCharVal!=null && !trimCharVal.isEmpty() && trimCharVal.indexOf("\"")==0 && trimCharVal.lastIndexOf("\"")== trimCharVal.length()-1)
			 trimCharVal=trimCharVal.substring(trimCharVal.indexOf("\"")+1, trimCharVal.lastIndexOf("\""));
		return trimCharVal;
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<String> getListOfUnifiedFields() {
	Set<String> listOfUnifiedFieldFromParsingService = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	List<ParserMapping> userDefinedMappinglist=parserMappingDao.getUserDefinedMappingIds();
	List<String> enumValueList = Stream.of(UnifiedFieldEnum.values())
            .map(UnifiedFieldEnum::name)
            .collect(Collectors.toList());
	Set<String> enumValueListSet=new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	enumValueListSet.addAll(enumValueList);
	listOfUnifiedFieldFromParsingService.addAll(enumValueListSet);
	for (ParserMapping parserMapping : userDefinedMappinglist) {
		List<ParserAttribute> pa=parserAttributeDao.getAttributeListByMappingId(parserMapping.getId());
		for (ParserAttribute parserAttribute : pa) {
			if(parserAttribute.getUnifiedField()!=null)
				//db
				listOfUnifiedFieldFromParsingService.add(parserAttribute.getUnifiedField());
		}
	}
	List<String> UnifiedFieldFromParsingServiceList = listOfUnifiedFieldFromParsingService.stream().collect(Collectors.toList());
	return UnifiedFieldFromParsingServiceList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public boolean associateParserAttributeGroupWithSubGroups(Map<String,List<String>> groupOfGroupMap, int mappingId) {
		boolean isAssociated = true;
		try {
			List<ParserGroupAttribute> groupAttributeList =(List<ParserGroupAttribute>) parserGroupAttributeService.getGroupAttributeListByMappingId(mappingId).getObject();
	        for (Map.Entry<String,List<String>> entry : groupOfGroupMap.entrySet())  {
	        	logger.debug("Key = " + entry.getKey() + ", Value = " + entry.getValue());
	            ParserGroupAttribute dbGrpAttr = getParserGroupAttributeByGroupName(groupAttributeList, entry.getKey());            
	            List<String> subGroupList = entry.getValue();
	            for(String subGroup : subGroupList) {
	            	 ParserGroupAttribute dbSubGrpAttr = getParserGroupAttributeByGroupName(groupAttributeList, subGroup);
	            	 if(dbSubGrpAttr!=null)
	            		 dbSubGrpAttr.setBaseGroup(dbGrpAttr);
	            }
	    	}
		}catch (Exception e) {
			logger.error(e);
			logger.error("Error occured while associateParserAttributeGroupWithSubGroups");
			isAssociated = false;
		}
		return isAssociated;
	}
	
	private ParserGroupAttribute getParserGroupAttributeByGroupName(List<ParserGroupAttribute> groupAttributeList, String groupName) {
		for(ParserGroupAttribute attr : groupAttributeList) {
			if(attr!=null && attr.getName().equalsIgnoreCase(groupName)) {
				return attr;
			}
		}
		return null;
	}

	@Override
	@Transactional
	public ResponseObject generateAttributeFromJsonString(String jsonString, int mappingId, String pluginType, int staffId) {
		Map<String, String> map = JsonAttributeGenerator.jsonToMap(jsonString);
		ResponseObject responseObject = parserMappingService.getMappingDetailsById(mappingId, pluginType);
		if (responseObject.isSuccess()) {
			ParserMapping parserMapping = (ParserMapping) responseObject.getObject();
			for (java.util.Map.Entry<String, String> entry : map.entrySet()) {
				logger.info("SourceField = "+entry.getValue()+"::: Unified Field = "+entry.getKey());
	        	ParserAttribute parserAttribute = new JsonParserAttribute();
	        	parserAttribute.setSourceField(entry.getValue());
	        	parserAttribute.setUnifiedField(entry.getKey());
	        	parserAttribute.setParserMapping(parserMapping);
	        	responseObject = createParserAttributes(parserAttribute, mappingId, pluginType, staffId);
	        	responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_SUCCESS);
	        	if (!responseObject.isSuccess()) {
	        		responseObject.setSuccess(false);
	    			responseObject.setObject(null);
	    			responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_FAIL);
	    			break;
	        	}
			} 
		}else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_FAIL);
		}
		return responseObject;
	}

}
