package com.elitecore.sm.composer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.model.RoamingComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerAttribute;
import com.elitecore.sm.composer.service.ComposerAttributeService;
import com.elitecore.sm.composer.validator.ComposerAttributeValidator;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ComposerAttributeController extends BaseController{
	
	@Autowired
	ComposerAttributeService composerAttributeService;
	
	@Autowired
	ComposerAttributeValidator composerAttributeValidator;
	
	@Autowired
	ServletContext servletContext;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	/**
	 * Method will add edit composer attribute details.
	 * @param attributeId
	 * @param mappingId
	 * @return ResponseBody
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_ASCII_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditAsciiAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN) ASCIIComposerAttr composerAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();

		ResponseObject responseObject ;
		composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = composerAttributeService.createComposerAttributes(composerAttribute, mappingId, plugInType, staffId,-1);
			}else{
				responseObject = composerAttributeService.updateComposerAttributes(composerAttribute, mappingId, plugInType,staffId);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will add edit composer attribute details.
	 * @param attributeId
	 * @param mappingId
	 * @return ResponseBody
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditFixedLengthAsciiComposerAttribute(
						@ModelAttribute (value=FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN) FixedLengthASCIIComposerAttribute composerAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();

		ResponseObject responseObject ;
		composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, result, null, false,null);
		//Because null pointer is generated
		composerAttribute.setDestinationField("");
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = composerAttributeService.createComposerAttributes(composerAttribute, mappingId, plugInType, staffId,-1);
			}else{
				responseObject = composerAttributeService.updateComposerAttributes(composerAttribute, mappingId, plugInType,staffId);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Adds the edit asn1 attribute by mapping id.
	 *
	 * @param composerAttribute the composer attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 * @throws SMException the SM exception
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_ASN1_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditAsn1AttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN) ASN1ComposerAttribute composerAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "attributeType",required=false) String atrributeType,
						BindingResult result,
						HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();

		ResponseObject responseObject ;
		if(atrributeType != null){
			if(atrributeType.equals(BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE)){
				composerAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
			}else if(atrributeType.equals(BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE)){
				composerAttribute.setAttrType(ASN1ATTRTYPE.TRAILER);
			}else{
				composerAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
			}
		}
		composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, result, null, false,null);       
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = composerAttributeService.createComposerAttributes(composerAttribute, mappingId, plugInType, staffId,-1);
			}else{
				responseObject = composerAttributeService.updateComposerAttributes(composerAttribute, mappingId, plugInType,staffId);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 *  Method will fetch all COMPOSER attribute by mapping id
	 * @param service
	 * @param result
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Json Ajax response
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_COMPOSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID, method = RequestMethod.POST)
	@ResponseBody public  String getAttributeGridListByMappingId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId, 
				@RequestParam(value = "skipPaging", defaultValue = "false") boolean skipPaging,
				@RequestParam(value = "attributeType", required = false) String attributeType){
		logger.debug("getAttributeListByMappingId in ComposerAttributeController " + mappingId); 
		if(attributeType != null){
			logger.debug("Attribute Type ::"+attributeType);
		}
		long count =  this.composerAttributeService.getAttributeListCountByMappingId(mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> resultList = new ArrayList<>();
		if(count > 0){
			if(skipPaging){
				ASN1ATTRTYPE asn1attrtype = ASN1ATTRTYPE.ATTRIBUTE;
				if(attributeType != null){
					if(attributeType.equals(BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE)){
						asn1attrtype = ASN1ATTRTYPE.HEADER;
					}else if(attributeType.equals(BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE)){
						asn1attrtype = ASN1ATTRTYPE.TRAILER;
					}
				}
				
				if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(composerType)){
					responseObject = this.composerAttributeService.getASN1AttributeListByMappingId(mappingId,asn1attrtype);
				}else if(EngineConstants.TAP_COMPOSER_PLUGIN.equals(composerType)){
					responseObject = this.composerAttributeService.getTapAttributeListByMappingId(mappingId,asn1attrtype);
				}else if(EngineConstants.RAP_COMPOSER_PLUGIN.equals(composerType)){
					responseObject = this.composerAttributeService.getRapAttributeListByMappingId(mappingId,asn1attrtype);
				}else if(EngineConstants.NRTRDE_COMPOSER_PLUGIN.equals(composerType)){
					responseObject = this.composerAttributeService.getNrtrdeAttributeListByMappingId(mappingId,asn1attrtype);
				}
				
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ComposerAttribute>) responseObject.getObject();
				}
				
			}else{
				responseObject = this.composerAttributeService.getAttributeListByMappingId(mappingId, BaseConstants.DOWNSTREAM);
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ComposerAttribute>) responseObject.getObject();
				}
			}
		}
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerAttribute attribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", attribute.getId());
				jAttrObj.put("sequenceNumber", String.valueOf(attribute.getSequenceNumber()));
				jAttrObj.put("destinationField", attribute.getDestinationField());
				jAttrObj.put("unifiedField", attribute.getUnifiedField());
				jAttrObj.put("defualtValue",attribute.getDefualtValue());
				jAttrObj.put("trimChar", attribute.getTrimchars());
				jAttrObj.put("trimPosition", attribute.getTrimPosition());
				jAttrObj.put("description", attribute.getDescription());
				jAttrObj.put("dataType", attribute.getDataType());
				jAttrObj.put("dateFormat", attribute.getDateFormat());
				jAttrObj.put("attributeOrder",attribute.getAttributeOrder());
				
				if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(composerType)){
					ASCIIComposerAttr asciiComposerAttr = (ASCIIComposerAttr)attribute;
					jAttrObj.put("replaceConditionList", asciiComposerAttr.getReplaceConditionList());
					jAttrObj.put("paddingEnable", String.valueOf(asciiComposerAttr.isPaddingEnable()));
					jAttrObj.put("length", String.valueOf(asciiComposerAttr.getLength()));
					jAttrObj.put("paddingType", asciiComposerAttr.getPaddingType());
					jAttrObj.put("paddingChar", asciiComposerAttr.getPaddingChar());
					jAttrObj.put("prefix", asciiComposerAttr.getPrefix());
					jAttrObj.put("suffix", asciiComposerAttr.getSuffix());
				}else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(composerType)){
					ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute)attribute;
					jAttrObj.remove("trimPosition");
					jAttrObj.remove("dataType");
					jAttrObj.put("asn1DataType", asn1ComposerAttribute.getasn1DataType());
					jAttrObj.put("argumentDataType", asn1ComposerAttribute.getArgumentDataType());
					jAttrObj.put("destFieldDataFormat", asn1ComposerAttribute.getDestFieldDataFormat());
					jAttrObj.put("choiceId", asn1ComposerAttribute.getChoiceId());
					jAttrObj.put("childAttributes", asn1ComposerAttribute.getChildAttributes());
				}else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(composerType)){
					FixedLengthASCIIComposerAttribute fixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute)attribute;
					jAttrObj.remove("destinationField");
					jAttrObj.remove("trimPosition");
					jAttrObj.remove("dateFormat");
					jAttrObj.remove("attributeOrder");
					jAttrObj.put("paddingType", fixedLengthASCIIComposerAttribute.getPaddingType());
					jAttrObj.put("prefix", fixedLengthASCIIComposerAttribute.getPrefix());
					jAttrObj.put("suffix", fixedLengthASCIIComposerAttribute.getSuffix());
					jAttrObj.put("paddingChar", fixedLengthASCIIComposerAttribute.getPaddingChar());
					jAttrObj.put("fixedLengthDateFormat", fixedLengthASCIIComposerAttribute.getFixedLengthDateFormat());
					jAttrObj.put("fixedLength", String.valueOf(fixedLengthASCIIComposerAttribute.getFixedLength()));
				}else if(attribute instanceof RoamingComposerAttribute){
					RoamingComposerAttribute roamingComposerAttribute = (RoamingComposerAttribute)attribute;
					jAttrObj.remove("trimPosition");
					jAttrObj.remove("dataType");
					jAttrObj.put("asn1DataType", roamingComposerAttribute.getasn1DataType());
					jAttrObj.put("argumentDataType", roamingComposerAttribute.getArgumentDataType());
					jAttrObj.put("destFieldDataFormat", roamingComposerAttribute.getDestFieldDataFormat());
					jAttrObj.put("choiceId", roamingComposerAttribute.getChoiceId());
					jAttrObj.put("childAttributes", roamingComposerAttribute.getChildAttributes());
					jAttrObj.put("composeFromJson", roamingComposerAttribute.isComposeFromJsonEnable());
					jAttrObj.put("cloneRecord", roamingComposerAttribute.isCloneRecordEnable());
				}				
				jAllAttrArr.put(jAttrObj);
			}
		}
		
		jAttributeList.put("attributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	
	/**
	 * Method will delete attribute by attribute id.
	 * @param attributeid
	 * @return ResponseBody
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.DELETE_COMPOSER_ATTRIBTE, method = RequestMethod.POST)
	@ResponseBody public  String deleteComposerAttribute(
						@RequestParam(value = "attributeId", required=true) String attributeIds,
						HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = composerAttributeService.deleteComposerAttributes(attributeIds, staffId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_XML_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditXMLAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN) XMLComposerAttribute composerAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();

		ResponseObject responseObject ;
		composerAttributeValidator.validateComposerAttributeParameter(composerAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = composerAttributeService.createComposerAttributes(composerAttribute, mappingId, plugInType, staffId,-1);
			}else{
				responseObject = composerAttributeService.updateComposerAttributes(composerAttribute, mappingId, plugInType,staffId);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPLOAD_COMPOSER_ATTR_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String uploadComposerAttrData(@RequestParam(value = "parserType", required = true) String parserType,
										@RequestParam(value = "parserMappingId",required=true) String parserMappingId,
										@RequestParam(value = "file", required = true) MultipartFile multipartFile,
										@RequestParam(value = "actionType",defaultValue="NO",required=false) String asnActionType,
										HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		String responseStr = "";
		multipartFile.getName();
		multipartFile.getSize();
		int staffId = eliteUtils.getLoggedInStaffId(request);

		if (!multipartFile.isEmpty()) {
			logger.debug("File object found.Going to insert data");
			// check if file is a valid csv file
			if (BaseConstants.CSV_FILE_CONTENT.equalsIgnoreCase(multipartFile.getContentType())
					|| "application/octet-stream".equalsIgnoreCase(multipartFile.getContentType())
					|| multipartFile.getOriginalFilename().endsWith(".csv")) {
				logger.debug("Valid CSV file found");
				// Check if the file headers match
				String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT) + File.separator;
				File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
				try {
					multipartFile.transferTo(lookupDataFile);
					int mappingId = Integer.parseInt(parserMappingId);
					logger.debug("Fetching all attribute for device configuration : " + parserMappingId);
					ResponseObject oldAttributeResponseObject = composerAttributeService.getAttributeListByMappingId(mappingId, BaseConstants.DOWNSTREAM);
					List<ComposerAttribute> oldAttributeList =  (List<ComposerAttribute>) oldAttributeResponseObject.getObject();
					String delAttrIds = getAttributeIdsFromList(oldAttributeList);
					if(!StringUtils.isEmpty(delAttrIds) && !EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType)) {
						composerAttributeService.deleteComposerAttributes(delAttrIds, staffId);
					}else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType)){
						String delAttrs =getAttributeIdsActionTypeFromList(oldAttributeList,asnActionType);
						composerAttributeService.deleteComposerAttributes(delAttrs, staffId);
					}		
					responseObject = composerAttributeService.uploadComposerAttributesFromCSV(lookupDataFile, mappingId, parserType, staffId, asnActionType);
					if(!responseObject.isSuccess()) {						
						for(ComposerAttribute oldParserAttrs : oldAttributeList) {
							oldParserAttrs.setStatus(StateEnum.ACTIVE);
							composerAttributeService.updateComposerAttributes(oldParserAttrs, mappingId, parserType, staffId);	
						}
					}
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
					JSONArray jsonArray = new JSONArray("[" + ajaxResponse.toString() + "]");
					responseStr = jsonArray.toString();
				} catch (Exception e) {
					logger.trace("Problem occured while uploading lookup data file", e);
				}
			} else {
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg("Please upload a valid CSV file");
			}
		}
		return responseStr;
	}	
	
	private String getAttributeIdsFromList(List<ComposerAttribute> list) {
		String atttributeIds = null;
		if(list != null && !list.isEmpty()) {			
			for(ComposerAttribute attribute : list) {
				if(atttributeIds == null)
					atttributeIds = String.valueOf(attribute.getId());
				else
					atttributeIds = atttributeIds +BaseConstants.COMMA_SEPARATOR+ attribute.getId();
			}
		}
		return atttributeIds;
	}	
	
	private String getAttributeIdsActionTypeFromList(List<ComposerAttribute> list,String actionType) {
		String atttributeIds = null;
		String aType;
		if(actionType.equals(BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE)){
			aType = "HEADER";
		}else if(actionType.equals(BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE)){
			aType = "TRAILER";
		}else{
			aType = "ATTRIBUTE";
		}
		if(list!=null && !list.isEmpty()) {			
			for(ComposerAttribute attribute : list) {
				ASN1ComposerAttribute asn1ParserAttribute = (ASN1ComposerAttribute) attribute;				
				if(asn1ParserAttribute.getAttrType().name().equals(aType)){
					if(atttributeIds==null){
						atttributeIds = String.valueOf(attribute.getId());
					}else{											
						atttributeIds = atttributeIds +BaseConstants.COMMA_SEPARATOR+ attribute.getId();
					}					
				}	
			}
		}
	
		return atttributeIds;
	}	
}
