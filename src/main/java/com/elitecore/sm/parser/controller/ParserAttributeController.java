/**
 * 
 */
package com.elitecore.sm.parser.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.elitecore.sm.device.validator.ParserAttributeValidator;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.NatFlowParserAttribute;
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
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.TAPATTRTYPE;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.VarLengthAsciiParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.XMLParserAttribute;
import com.elitecore.sm.parser.model.XlsParserAttribute;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.util.ASN1DefinitionToAttributeList;
import com.elitecore.sm.util.AutoSuggestUtilAfterSpace;
import com.elitecore.sm.util.AutoSuggestUtilForUnifiedField;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.Utilities;

/**
 * The Class ParserAttributeController.
 *
 * @author Ranjitsinh Reval
 */
@Controller
public class ParserAttributeController extends BaseController{

	@Autowired
	@Qualifier(value = "parserMappingService")
	private ParserMappingService parserMappingService;

	/** The parser attribute service. */
	@Autowired
	private ParserAttributeService parserAttributeService;
	
	/** The parser attribute validator. */
	@Autowired
	private ParserAttributeValidator parserAttributeValidator;
	
	@Autowired
	ServletContext servletContext;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	
	/**
	 * Method will get all attribute List by Device Mapping.
	 *
	 * @param mappingId the mapping id
	 * @return the attribute list by mapping id
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_ATTRIBUTE_LIST_BY_MAPPING_ID, method = RequestMethod.POST)
	@ResponseBody public String getAttributeListByMappingId(
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "deviceType",required=true) String deviceType){
		ResponseObject responseObject = parserAttributeService.getAttributeListByMappingId(mappingId,deviceType);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will get all attribute by mapping id.
	 *
	 * @param attributeId the attribute id
	 * @return the attribute by id
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_ATTRIBUTE_BY_ID, method = RequestMethod.POST)
	@ResponseBody public String getAttributeById(
						@RequestParam(value = "attributeId",required=true) int attributeId){
		ResponseObject responseObject = parserAttributeService.getAttributeById(attributeId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Delete attribute.
	 *
	 * @param attributeIds the attribute ids
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.DELETE_ATTRIBTE, method = RequestMethod.POST)
	@ResponseBody public  String deleteAttribute(
						@RequestParam(value = "attributeId", required=true) String attributeIds,
						HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = parserAttributeService.deleteParserAttributes(attributeIds, staffId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
		
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditFixedLengthASCIIAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) FixedLengthASCIIParserAttribute parserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	/**
	 * Adds the edit attribute by mapping id.
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_EDIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String addEditFixedLengthBinaryAttributeByMappingId(
			@ModelAttribute(value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) FixedLengthBinaryParserAttribute parserAttribute,//NOSONAR
			@RequestParam(value = "actionType",required=true) String actionType,
			@RequestParam(value = "plugInType",required=true) String plugInType,
			@RequestParam(value = "mappingId",required=true) int mappingId,
			BindingResult result, HttpServletRequest request){
	    	AjaxResponse ajaxResponse = new AjaxResponse();
			ResponseObject responseObject;
			parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);
			if(result.hasErrors()){
				 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			}else{
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
				}else{
					responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		
			return ajaxResponse.toString();

	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_EDIT_PDF_PARSER_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String addEditPDFAttributeByMappingId(
			@ModelAttribute(value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) PDFParserAttribute parserAttribute,//NOSONAR
			@RequestParam(value = "actionType",required=true) String actionType,
			@RequestParam(value = "plugInType",required=true) String plugInType,
			@RequestParam(value = "mappingId",required=true) int mappingId,
			@RequestParam(value = "groupId",required=true) int groupId,
			BindingResult result, HttpServletRequest request){
		
    	AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
//		parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				if(groupId > 0){
					ParserGroupAttribute pga = new ParserGroupAttribute();
					pga.setId(groupId);
					parserAttribute.setParserGroupAttribute(pga);
					parserAttribute.setAssociatedByGroup(true);
				}else{
					parserAttribute.setAssociatedByGroup(false);
				}
				responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) ParserAttribute parserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *  
	 * @param varLengthAsciiParserAttribute
	 * @param actionType
	 * @param plugInType
	 * @param mappingId
	 * @param result
	 * @param request
	 * @return string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditVarLengthAsciiAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) VarLengthAsciiParserAttribute varLengthAsciiParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(varLengthAsciiParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(varLengthAsciiParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id(natflow parser)
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_NATFLOW_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditNatFlowAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) NatFlowParserAttribute natFlowParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(natFlowParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(natFlowParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(natFlowParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_ASCII_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditAsciiAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) AsciiParserAttribute asciiParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(asciiParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(asciiParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(asciiParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 *  Adds the edit attribute by mapping id.
	 * 
	 * @param varLengthBinaryParserAttribute
	 * @param actionType
	 * @param plugInType
	 * @param mappingId
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditVarLengthBinaryAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) VarLengthBinaryParserAttribute varLengthBinaryParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "attributeType",required=false) String atrributeType,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		if(atrributeType != null){
			if(atrributeType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE)){
				varLengthBinaryParserAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
			}else{
				varLengthBinaryParserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
			}
		}
		ResponseObject responseObject;
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(varLengthBinaryParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(varLengthBinaryParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param xmlParserAttribute the xml parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_XML_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditXMLAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) XMLParserAttribute xmlParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(xmlParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(xmlParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(xmlParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param xmlParserAttribute the xml parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_HTML_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditHtmlAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) HtmlParserAttribute htmlParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "groupId",required=true) int groupId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		//parserAttributeValidator.validateParserAttributeParameter(htmlParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				if(groupId >0 ){
					ParserGroupAttribute pa= new ParserGroupAttribute();
					pa.setId(groupId);
					htmlParserAttribute.setAssociatedByGroup(true);
					htmlParserAttribute.setParserGroupAttribute(pa);
				}
				else{
					htmlParserAttribute.setAssociatedByGroup(false);
					htmlParserAttribute.setParserGroupAttribute(null);
				}
				responseObject = parserAttributeService.createParserAttributes(htmlParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(htmlParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_XLS_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditXlsAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) XlsParserAttribute xlsParserAttribute, //NOSONAR
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "groupId",required=true) int groupId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				if(groupId >0 ){
					ParserGroupAttribute parserGroupAttribute= new ParserGroupAttribute();
					parserGroupAttribute.setId(groupId);
					xlsParserAttribute.setAssociatedByGroup(true);
					xlsParserAttribute.setParserGroupAttribute(parserGroupAttribute);
				}
				else{
					xlsParserAttribute.setAssociatedByGroup(false);
					xlsParserAttribute.setParserGroupAttribute(null);
				}
				responseObject = parserAttributeService.createParserAttributes(xlsParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(xlsParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	/**
	 * Update order attribute by mapping id.
	 *
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param jsonFormatData the json format data
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.REORDER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID, method = RequestMethod.POST)
	@ResponseBody public  String updateOrderAttributeByMappingId(
			@RequestParam(value = "plugInType",required=true) String plugInType,
			@RequestParam(value = "mappingId",required=true) String mappingId,
			@RequestParam(value = "attributeIds" , required = true) String attributeIds,
			HttpServletRequest request){
		AjaxResponse ajaxResponse = new AjaxResponse();
		try{
			Integer[] attributeIdsArray = Utilities.convertStringArrayToInt(attributeIds);
			List<Integer> attributeIdList =new ArrayList<>();
			List<ParserAttribute> parserAttrList =new ArrayList<>();
			if(attributeIdsArray.length > 0){
				for(int i = 0 ; i <  attributeIdsArray.length ; i++){
						attributeIdList.add(attributeIdsArray[i]);
				}
				if(!attributeIdList.isEmpty()){
					@SuppressWarnings("unchecked")
					List<ParserAttribute> resultList = (List<ParserAttribute>)this.parserAttributeService.getAttributeListByMappingId(Integer.parseInt(mappingId),"UPSTREAM").getObject();
					Map<Integer,ParserAttribute> attributeListMap = new HashMap<>();
					for(ParserAttribute parserAttribute : resultList){
						attributeListMap.put(parserAttribute.getId(), parserAttribute);
					}
					int order = 1;
					int staffId = eliteUtils.getLoggedInStaffId(request);
					ParserMapping parserMappingObj=resultList.get(0).getParserMapping();
					for(Integer attributeId : attributeIdList){
						ParserAttribute attribute = attributeListMap.get(attributeId);
						if(attribute != null){
							attribute.setAttributeOrder(order++);
							parserAttrList.add(attribute);
						//	this.parserAttributeService.updateParserAttributesOrder(attribute,staffId);
							attributeListMap.remove(attributeId);
						}
					}
					parserMappingObj.setParserAttributes(parserAttrList);
					this.parserAttributeService.updateParserAttributesOrder(parserMappingObj,staffId);
					for(Map.Entry<Integer, ParserAttribute> entry : attributeListMap.entrySet()) {
						ParserAttribute parserAttribute = entry.getValue();
						parserAttribute.setAttributeOrder(order++);
						parserAttrList.add(parserAttribute);
						//this.parserAttributeService.updateParserAttributesOrder(parserAttribute,staffId);
					}
					ajaxResponse.setResponseMsg("Attributes order sequence updated successfully.");	
				}else{
					ajaxResponse.setResponseMsg("No Attribute available for reorder operation");
				}
				ajaxResponse.setResponseCode("200");

			}
		}catch(Exception e){
			logger.error("Attribute Ordering failed , reason :"+e.getMessage(),e);
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg("Attribute Ordering failed");
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit asn1 parser attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param atrributeType the atrribute type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_ASN1_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditAsn1ParserAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) ASN1ParserAttribute parserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "attributeType",required=false) String atrributeType,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		if(atrributeType != null){
			if(atrributeType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE)){
				parserAttribute.setAttrType(ASN1ATTRTYPE.HEADER);
			}else if(atrributeType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)){
				parserAttribute.setAttrType(ASN1ATTRTYPE.TRAILER);
			}else{
				parserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
			}
		}
		parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	/**
	 * Adds the edit rap parser attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param atrributeType the atrribute type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_EDIT_RAP_PARSER_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody
	public String addEditRapParserAttributeByMappingId(
			@ModelAttribute(value = FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) RAPParserAttribute parserAttribute,//NOSONAR
			@RequestParam(value = "actionType", required = true) String actionType,
			@RequestParam(value = "plugInType", required = true) String plugInType,
			@RequestParam(value = "mappingId", required = true) int mappingId,
			@RequestParam(value = "attributeType", required = false) String atrributeType, BindingResult result,
			HttpServletRequest request) {
		   AjaxResponse ajaxResponse = new AjaxResponse();
		   ResponseObject responseObject;
		   if(atrributeType != null){
			   parserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);  
		   }
			parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);       
			if(result.hasErrors()){
				 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			}else{
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
				}else{
					responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
				}	
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		return ajaxResponse.toString();
	}
	/**
	 * Adds the edit tapparser attribute by mapping id.
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param atrributeType the atrribute type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_TAP_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody
	public String addEditTapParserAttributeByMappingId(
			@ModelAttribute(value = FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) TAPParserAttribute parserAttribute,//NOSONAR
			@RequestParam(value = "actionType", required = true) String actionType,
			@RequestParam(value = "plugInType", required = true) String plugInType,
			@RequestParam(value = "mappingId", required = true) int mappingId,
			@RequestParam(value = "attributeType", required = false) String atrributeType, BindingResult result,
			HttpServletRequest request) {
		   AjaxResponse ajaxResponse = new AjaxResponse();
		   ResponseObject responseObject;
		   if(atrributeType != null){
			   parserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);  
		   }
			parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);       
		   if(result.hasErrors()){
				 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			}else{
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType, staffId);
				}else{
					responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,staffId,false);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
			return ajaxResponse.toString();
	}
	/**
	 * Adds the edit nrtrde parser attribute by mapping id.
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param atrributeType the atrribute type
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_NRTRDE_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody
	public String addEditNrtrdeParserAttributeByMappingId(
			@ModelAttribute(value = FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) NRTRDEParserAttribute parserAttribute,//NOSONAR
			@RequestParam(value = "actionType", required = true) String actionType,
			@RequestParam(value = "plugInType", required = true) String plugInType,
			@RequestParam(value = "mappingId", required = true) int mappingId,
			@RequestParam(value = "attributeType", required = false) String atrributeType, BindingResult result,
			HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		if (atrributeType != null) {
			parserAttribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
		}
		parserAttributeValidator.validateParserAttributeParameter(parserAttribute, result, null, false,null);       

		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
		} else {
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if (BaseConstants.ACTION_TYPE_ADD.equals(actionType)) {
				responseObject = parserAttributeService.createParserAttributes(parserAttribute, mappingId, plugInType,
						staffId);

			} else {
				responseObject = parserAttributeService.updateParserAttributes(parserAttribute, mappingId, plugInType,
						staffId,false);

			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		}

		return ajaxResponse.toString();
	}
	
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_JSON_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditJsonAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) JsonParserAttribute jsonParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(jsonParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(jsonParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(jsonParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	/**
	 * Adds the edit attribute by mapping id.
	 *
	 * @param parserAttribute the parser attribute
	 * @param actionType the action type
	 * @param plugInType the plug in type
	 * @param mappingId the mapping id
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_MTSIEMENS_BINARY_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditMTSiemensBinaryParserAttributeDetails(
						@ModelAttribute (value=FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN) MTSiemensParserAttribute mtsiemensParserAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						BindingResult result,
						HttpServletRequest request){
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserAttributeValidator.validateParserAttributeParameter(mtsiemensParserAttribute, result, null, false,null);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
				responseObject = parserAttributeService.createParserAttributes(mtsiemensParserAttribute, mappingId, plugInType, staffId);
			}else{
				responseObject = parserAttributeService.updateParserAttributes(mtsiemensParserAttribute, mappingId, plugInType,staffId,false);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}

	/**
	 * Gets the attribute grid list by mapping id.
	 *
	 * @param parserType the parser type
	 * @param mappingId the mapping id
	 * @param limit the limit
	 * @param currentPage the current page
	 * @param sidx the sidx
	 * @param sord the sord
	 * @param skipPaging the skip paging
	 * @param attributeType the attribute type
	 * @return the attribute grid list by mapping id
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.GET_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID, method = RequestMethod.POST)
	@ResponseBody public String getAttributeGridListByMappingId(
				@RequestParam(value="plugInType",required=true) String parserType,
				@RequestParam(value = "mappingId" , required = true) int mappingId, 				
				@RequestParam(value = "skipPaging", defaultValue = "false") boolean skipPaging,
				@RequestParam(value = "attributeType", required = false) String attributeType) {
		logger.debug(">> getAttributeListByMappingId in ParserAttributeController " + mappingId); 
		if(attributeType != null){
			logger.debug("Attribute Type ::"+attributeType);
		}
		if(EngineConstants.ASCII_PARSING_PLUGIN.equals(parserType))
			parserAttributeService.updateAsciiParserTypeByMappingId(mappingId);
		
		long count =  this.parserAttributeService.getAttributeListCountByMappingId(mappingId);
		ResponseObject responseObject = new ResponseObject();
		List<ParserAttribute> resultList = new ArrayList<>();
		if(count > 0){
			logger.debug("<< getAttributeListByMappingId in ParserAttributeController ");
			if(skipPaging){
				ASN1ATTRTYPE asn1attrtype = ASN1ATTRTYPE.ATTRIBUTE;
				RAPATTRTYPE rapattrtype = RAPATTRTYPE.ATTRIBUTE;
				TAPATTRTYPE tapattrtype = TAPATTRTYPE.ATTRIBUTE;
				if(attributeType != null){
					if(attributeType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE)){
						asn1attrtype = ASN1ATTRTYPE.HEADER;
					}else if(attributeType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)){
						asn1attrtype = ASN1ATTRTYPE.TRAILER;
					}
				}
				/*resultList = this.parserAttributeService.getASN1PaginatedList(mappingId, eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
						limit, sidx,sord,asn1attrtype);*/
				
				if(EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)){
					
					responseObject = this.parserAttributeService.getASN1AttributeListByMappingId(mappingId,asn1attrtype);
				}
				else if(EngineConstants.RAP_PARSING_PLUGIN.equals(parserType)){
					responseObject =this.parserAttributeService.getRAPAttributeListByMappingId(mappingId, rapattrtype);
				}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)){
					responseObject =this.parserAttributeService.getTAPAttributeListByMappingId(mappingId, tapattrtype);
				}else if(EngineConstants.NRTRDE_PARSING_PLUGIN.equals(parserType)){
					responseObject =this.parserAttributeService.getNRTRDEAttributeListByMappingId(mappingId, asn1attrtype);
				}
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ParserAttribute>) responseObject.getObject();
				}
			} else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType) && attributeType != null) {
				ASN1ATTRTYPE asn1attrtype = ASN1ATTRTYPE.ATTRIBUTE;
				if(attributeType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE)) {
					asn1attrtype = ASN1ATTRTYPE.HEADER;
				}
				responseObject = this.parserAttributeService.getVarLengthBinaryAttributeListByMappingId(mappingId,asn1attrtype);
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ParserAttribute>) responseObject.getObject();
				}
			}else{
				/*resultList = this.parserAttributeService.getPaginatedList(mappingId, eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
											limit, sidx,sord);*/
				
				responseObject = this.parserAttributeService.getAttributeListByMappingId(mappingId,BaseConstants.UPSTREAM);
				if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
					resultList = (List<ParserAttribute>) responseObject.getObject();
				}				
			}
		}
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ParserAttribute attribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", attribute.getId());
				jAttrObj.put("sourceField", attribute.getSourceField());
				jAttrObj.put("unifiedField", attribute.getUnifiedField());
				jAttrObj.put("trimPosition", attribute.getTrimPosition());
				jAttrObj.put("description", attribute.getDescription());
				jAttrObj.put("defaultText",attribute.getDefaultValue() );
				jAttrObj.put("trimChar", attribute.getTrimChars());
				
				jAttrObj.put("attributeOrder", attribute.getAttributeOrder());
				
				if(EngineConstants.ASCII_PARSING_PLUGIN.equals(parserType)) {
					try{
						AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) attribute;
						jAttrObj.put("sourceFieldFormat", asciiParserAttribute.getSourceFieldFormat());
						jAttrObj.put("dateFormatInput", asciiParserAttribute.getDateFormat());
						jAttrObj.put("portUnifiedField", asciiParserAttribute.getPortUnifiedField());
						jAttrObj.put("ipPortSeperator", asciiParserAttribute.getIpPortSeperator());
					} catch(ClassCastException castException) {//NOSONAR
						jAttrObj.put("sourceFieldFormat", attribute.getSourceFieldFormat());
						jAttrObj.put("dateFormatInput", attribute.getDateFormat());
					}
					
				}
				else if(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equals(parserType)) {
					jAttrObj.put("sourceFieldFormat", attribute.getSourceFieldFormat());
					jAttrObj.put("dateFormatInput", attribute.getDateFormat());
				}
				else if(EngineConstants.NATFLOW_PARSING_PLUGIN.equals(parserType)) {
					NatFlowParserAttribute natFlowParserAttribute = (NatFlowParserAttribute)attribute;  
					jAttrObj.put("sourceFieldFormat", natFlowParserAttribute.getSourceFieldFormat());
					jAttrObj.put("destDateFormat", natFlowParserAttribute.getDestDateFormat());
					jAttrObj.put("dateFormatInput", natFlowParserAttribute.getDestDateFormat());
				}
				else if(EngineConstants.XML_PARSING_PLUGIN.equals(parserType)){
					jAttrObj.put("sourceFieldFormat", attribute.getSourceFieldFormat());
				}
				else if(EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)){
					ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute)attribute;  
					jAttrObj.put("childAttributes", asn1ParserAttribute.getChildAttributes());
					jAttrObj.put("ASN1DataType", asn1ParserAttribute.getASN1DataType());
					jAttrObj.put("recordInitilializer", String.valueOf(asn1ParserAttribute.isRecordInitilializer()));
					jAttrObj.put("unifiedFieldHoldsChoiceId", asn1ParserAttribute.getUnifiedFieldHoldsChoiceId());
					if(asn1ParserAttribute.getSrcDataFormat() != null && asn1ParserAttribute.getSrcDataFormat().toString() !="" && !StringUtils.isEmpty(asn1ParserAttribute.getSrcDataFormat())){
						jAttrObj.put("srcDataFormat", asn1ParserAttribute.getSrcDataFormat().toString());						
					}else{
						jAttrObj.put("srcDataFormat", "");
					}
				}else if(EngineConstants.RAP_PARSING_PLUGIN.equals(parserType)){
					RAPParserAttribute rapParserAttribute = (RAPParserAttribute)attribute;  
						jAttrObj.put("childAttributes", rapParserAttribute.getChildAttributes());
						jAttrObj.put("ASN1DataType", rapParserAttribute.getASN1DataType());
						jAttrObj.put("recordInitilializer", String.valueOf(rapParserAttribute.isRecordInitilializer()));
						jAttrObj.put("parseAsJson", String.valueOf(rapParserAttribute.isParseAsJson()));
                        jAttrObj.put("unifiedFieldHoldsChoiceId", rapParserAttribute.getUnifiedFieldHoldsChoiceId());
						if(rapParserAttribute.getSrcDataFormat() != null && rapParserAttribute.getSrcDataFormat().toString() !="" && !StringUtils.isEmpty(rapParserAttribute.getSrcDataFormat())){
							jAttrObj.put("srcDataFormat", rapParserAttribute.getSrcDataFormat().toString());						
						}else{
							jAttrObj.put("srcDataFormat", "");
						}
				}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)){
					TAPParserAttribute tapParserAttribute = (TAPParserAttribute)attribute;  
					jAttrObj.put("childAttributes", tapParserAttribute.getChildAttributes());
					jAttrObj.put("ASN1DataType", tapParserAttribute.getASN1DataType());
					jAttrObj.put("recordInitilializer", String.valueOf(tapParserAttribute.isRecordInitilializer()));
					jAttrObj.put("parseAsJson", String.valueOf(tapParserAttribute.isParseAsJson()));
					jAttrObj.put("unifiedFieldHoldsChoiceId", tapParserAttribute.getUnifiedFieldHoldsChoiceId());
					if(tapParserAttribute.getSrcDataFormat() != null && tapParserAttribute.getSrcDataFormat().toString() !="" && !StringUtils.isEmpty(tapParserAttribute.getSrcDataFormat())){
						jAttrObj.put("srcDataFormat", tapParserAttribute.getSrcDataFormat().toString());						
					}else{
						jAttrObj.put("srcDataFormat", "");
					}
				}else if(EngineConstants.NRTRDE_PARSING_PLUGIN.equals(parserType)){
					NRTRDEParserAttribute nrtrdeParserAttribute = (NRTRDEParserAttribute)attribute;  
					jAttrObj.put("childAttributes", nrtrdeParserAttribute.getChildAttributes());
					jAttrObj.put("ASN1DataType", nrtrdeParserAttribute.getASN1DataType());
					jAttrObj.put("recordInitilializer", String.valueOf(nrtrdeParserAttribute.isRecordInitilializer()));
					jAttrObj.put("parseAsJson", String.valueOf(nrtrdeParserAttribute.isParseAsJson()));
                    jAttrObj.put("unifiedFieldHoldsChoiceId", nrtrdeParserAttribute.getUnifiedFieldHoldsChoiceId());
					if(nrtrdeParserAttribute.getSrcDataFormat() != null && nrtrdeParserAttribute.getSrcDataFormat().toString() !="" && !StringUtils.isEmpty(nrtrdeParserAttribute.getSrcDataFormat())){
						jAttrObj.put("srcDataFormat", nrtrdeParserAttribute.getSrcDataFormat().toString());						
					}else{
						jAttrObj.put("srcDataFormat", "");
					}
				
				}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)){
					FixedLengthASCIIParserAttribute fixedLengthASCIIParserAttribute = (FixedLengthASCIIParserAttribute)attribute;
					jAttrObj.remove("sourceField");
					jAttrObj.put("startLength", fixedLengthASCIIParserAttribute.getStartLength());
					jAttrObj.put("endLength", fixedLengthASCIIParserAttribute.getEndLength());
					jAttrObj.put("sourceFieldFormat", fixedLengthASCIIParserAttribute.getSourceFieldFormat());
					jAttrObj.put("prefix", fixedLengthASCIIParserAttribute.getPrefix());
					jAttrObj.put("postfix", fixedLengthASCIIParserAttribute.getPostfix());
					jAttrObj.put("length", fixedLengthASCIIParserAttribute.getLength());
					jAttrObj.put("rightDelimiter", fixedLengthASCIIParserAttribute.getRightDelimiter());
				}else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
					FixedLengthBinaryParserAttribute fixedLengthBinaryParserAttribute = (FixedLengthBinaryParserAttribute)attribute;
					jAttrObj.remove("sourceField");
					jAttrObj.put("startLength", fixedLengthBinaryParserAttribute.getStartLength());
					jAttrObj.put("endLength", fixedLengthBinaryParserAttribute.getEndLength());
					jAttrObj.put("readAsBits", fixedLengthBinaryParserAttribute.isReadAsBits());
					jAttrObj.put("bitStartLength", fixedLengthBinaryParserAttribute.getBitStartLength());
					jAttrObj.put("bitEndLength", fixedLengthBinaryParserAttribute.getBitEndLength());
					jAttrObj.put("sourceFieldFormat", fixedLengthBinaryParserAttribute.getSourceFieldFormat());
					jAttrObj.put("prefix", fixedLengthBinaryParserAttribute.getPrefix());
					jAttrObj.put("postfix", fixedLengthBinaryParserAttribute.getPostfix());
					jAttrObj.put("length", fixedLengthBinaryParserAttribute.getLength());
					jAttrObj.put("rightDelimiter", fixedLengthBinaryParserAttribute.getRightDelimiter());
					jAttrObj.put("multiRecord", fixedLengthBinaryParserAttribute.isMultiRecord());
				}else if(EngineConstants.HTML_PARSING_PLUGIN.equals(parserType)){
					HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute)attribute;
					jAttrObj.put("fieldIdentifier", htmlParserAttribute.getFieldIdentifier());
					jAttrObj.put("fieldExtractionMethod", htmlParserAttribute.getFieldExtractionMethod());
					jAttrObj.put("fieldSectionId", htmlParserAttribute.getFieldSectionId());
					jAttrObj.put("containsFieldAttribute", htmlParserAttribute.getContainsFieldAttribute());
					jAttrObj.put("valueSeparator", htmlParserAttribute.getValueSeparator());
					jAttrObj.put("valueIndex", htmlParserAttribute.getValueIndex());
				}else if(EngineConstants.PDF_PARSING_PLUGIN.equals(parserType)){
					PDFParserAttribute pdfParserAttribute = (PDFParserAttribute)attribute;
					jAttrObj.put("location", pdfParserAttribute.getLocation());
					jAttrObj.put("columnStartLocation", pdfParserAttribute.getColumnStartLocation());
					jAttrObj.put("columnIdentifier", pdfParserAttribute.getColumnIdentifier());
					jAttrObj.put("referenceRow", pdfParserAttribute.getReferenceRow());
					jAttrObj.put("columnStartsWith", pdfParserAttribute.getColumnStartsWith());
					jAttrObj.put("mandatory", pdfParserAttribute.getMandatory());
					jAttrObj.put("multipleValues", pdfParserAttribute.isMultipleValues());
					jAttrObj.put("pageNumber", pdfParserAttribute.getPageNumber());
					jAttrObj.put("columnEndsWith", pdfParserAttribute.getColumnEndsWith());
					jAttrObj.put("valueSeparator", pdfParserAttribute.getValueSeparator());
				}else if(EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)){
					XlsParserAttribute xlsParserAttribute = (XlsParserAttribute)attribute;
					jAttrObj.put("columnStartsWith", xlsParserAttribute.getColumnStartsWith());
					jAttrObj.put("tableFooter", xlsParserAttribute.isTableFooter());
					jAttrObj.put("excelRow", xlsParserAttribute.getExcelRow());
					jAttrObj.put("excelCol", xlsParserAttribute.getExcelCol());
					jAttrObj.put("relativeExcelRow", xlsParserAttribute.getRelativeExcelRow());
					jAttrObj.put(BaseConstants.STARTS_WITH, xlsParserAttribute.getStartsWith());
					jAttrObj.put(BaseConstants.COLUMN_CONTAINS, xlsParserAttribute.getColumnContains());
					jAttrObj.put(BaseConstants.TABLE_ROW_ATTRIBUTE, xlsParserAttribute.isTableRowAttribute());
				}else if(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)) {
					VarLengthAsciiParserAttribute varAttribute = (VarLengthAsciiParserAttribute) attribute;
					jAttrObj.put("sourceFieldFormat", varAttribute.getSourceFieldFormat());
					jAttrObj.put("dateFormatInput", varAttribute.getDateFormat());
					jAttrObj.put("sourceFieldName", varAttribute.getSourceFieldName());
				}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
					VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute)attribute;
					jAttrObj.remove("sourceField");
					jAttrObj.put("sourceFieldName", varLengthBinaryParserAttribute.getSourceFieldName());
					jAttrObj.put("sourceFieldFormat", varLengthBinaryParserAttribute.getSourceFieldFormat());
					jAttrObj.put("dateFormatInput", varLengthBinaryParserAttribute.getDateFormat());
					jAttrObj.put("prefix", varLengthBinaryParserAttribute.getPrefix());
					jAttrObj.put("postfix", varLengthBinaryParserAttribute.getPostfix());
					jAttrObj.put("length", varLengthBinaryParserAttribute.getLength());
					jAttrObj.put("rightDelimiter", varLengthBinaryParserAttribute.getRightDelimiter());
					jAttrObj.put("startLength", varLengthBinaryParserAttribute.getStartLength());
					jAttrObj.put("endLength", varLengthBinaryParserAttribute.getEndLength());
				} else if(EngineConstants.JSON_PARSING_PLUGIN.equals(parserType)) {
					try{
						JsonParserAttribute asciiParserAttribute = (JsonParserAttribute) attribute;
						jAttrObj.put("sourceFieldFormat", asciiParserAttribute.getSourceFieldFormat());
						jAttrObj.put("dateFormatInput", asciiParserAttribute.getDateFormat());
					} catch(ClassCastException castException) {//NOSONAR
						jAttrObj.put("sourceFieldFormat", attribute.getSourceFieldFormat());
						jAttrObj.put("dateFormatInput", attribute.getDateFormat());
					}
					
				}
				
				if(EngineConstants.PDF_PARSING_PLUGIN.equals(parserType) || 
                        EngineConstants.HTML_PARSING_PLUGIN.equals(parserType)|| 
                        EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)){
                    if(!attribute.isAssociatedByGroup()){
                        jAllAttrArr.put(jAttrObj);
                    }
                }else{
                    jAllAttrArr.put(jAttrObj);    
                }
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
	 * Delete regEx Parser Attribute.
	 *
	 * @param patternAttrIdList the pattern attr id list
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PARSER')")
	@RequestMapping(value=ControllerConstants.DELETE_REGEX_PATTERN_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String deleteRegExPatternAttribute(@RequestParam(value="patternAttrIdList") String patternAttrIdList,
			HttpServletRequest request){
		
		ResponseObject responseObject=parserAttributeService.deleteParserAttributes(patternAttrIdList,eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Update regex parser attribute.
	 *
	 * @param regExParserAttribute the reg ex parser attribute
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_REGEX_PATTERN_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String updatePatternAttribute(@ModelAttribute(value=FormBeanConstants.REGEX_PATTERN_ATTRIBUTE_CONFIGURATION_FORM_BEAN) RegexParserAttribute regExParserAttribute,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request){
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		parserAttributeValidator.validateParserAttributeParameter(regExParserAttribute, result, null, false, null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
					errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			regExParserAttribute.setLastUpdatedDate(new Date());
			regExParserAttribute.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=parserAttributeService.updateRegExParserAttribute(regExParserAttribute);
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();

	}
	
	/**
	 * Validate regex parser attribute for update .
	 *
	 * @param regExParserAttribute the reg ex parser attribute
	 * @param result the result
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.VALIDATE_REGEX_PATTERN_ATTRIBUTE,method=RequestMethod.POST)
	@ResponseBody public String validatePatternAttribute(@ModelAttribute(value=FormBeanConstants.REGEX_PATTERN_ATTRIBUTE_CONFIGURATION_FORM_BEAN) RegexParserAttribute regExParserAttribute,//NOSONAR
			 BindingResult result){
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		parserAttributeValidator.validateParserAttributeParameter(regExParserAttribute, result, null, false, null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
					errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
		}
		
		return ajaxResponse.toString();

	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPLOAD_PARSER_ATTR_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String uploadParserAttributeData(@RequestParam(value = "parserType", required = true) String parserType,
										@RequestParam(value = "parserMappingId",required=true) String parserMappingId,
										@RequestParam(value = "file", required = true) MultipartFile multipartFile,
										@RequestParam(value = "actionType",defaultValue="NO",required=false) String asnActionType,
										@RequestParam(value = "groupId",defaultValue="0",required=false) String groupId,
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
					int mappingId = Integer.valueOf(parserMappingId);
					logger.debug("Fetching all attribute for device configuration : " + parserMappingId);
					ResponseObject oldAttributeResponseObject = parserAttributeService.getAttributeListByMappingId(mappingId, "UPSTREAM");
					@SuppressWarnings("unchecked")
					List<ParserAttribute> oldAttributeList = (List<ParserAttribute>) oldAttributeResponseObject.getObject();
					List<ParserAttribute> groupOldAttributeList = new ArrayList();
					if(EngineConstants.HTML_PARSING_PLUGIN.equals(parserType) || EngineConstants.PDF_PARSING_PLUGIN.equals(parserType)|| EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)){
						for(ParserAttribute pa:oldAttributeList){
							if(Integer.parseInt(groupId)>0 && pa.isAssociatedByGroup()){
								groupOldAttributeList.add(pa);
							}else if(Integer.parseInt(groupId)==0 && !pa.isAssociatedByGroup()){
								groupOldAttributeList.add(pa);
							}
							
						}
						oldAttributeList= groupOldAttributeList;
					}
					
					String delAttrIds = getAttributeIdsFromList(oldAttributeList);
					if(!StringUtils.isEmpty(delAttrIds) && !EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType) && !EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)) {
						parserAttributeService.deleteParserAttributes(delAttrIds, staffId);
					}else if(EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)){
						String delAttrs =getAttributeIdsActionTypeFromList(oldAttributeList,asnActionType);
						parserAttributeService.deleteParserAttributes(delAttrs, staffId);
					}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
						String delAttrs =getAttributeIdsForVLBPByActionType(oldAttributeList,asnActionType);
						parserAttributeService.deleteParserAttributes(delAttrs, staffId);
					}
					int groupIdentifier=0;
					if(groupId!=null){
						groupIdentifier=Integer.parseInt(groupId);
					}
					responseObject = parserAttributeService.uploadParserAttributesFromCSV(lookupDataFile, mappingId, parserType, staffId, asnActionType,groupIdentifier);
					if(!responseObject.isSuccess()  ) {						
						for(ParserAttribute oldParserAttrs : oldAttributeList) {
							oldParserAttrs.setStatus(StateEnum.ACTIVE);
							parserAttributeService.updateParserAttributes(oldParserAttrs, mappingId, parserType, staffId, false);	
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
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.GENERATE_JSON_PARSER_ATTR_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String generateJsonParserAttributeData(@RequestParam(value = "parserType", required = true) String parserType,
										@RequestParam(value = "parserMappingId",required=true) String parserMappingId,
										@RequestParam(value = "jsonString", required = true) String jsonString,
										@RequestParam(value = "actionType",required=false) String asnActionType,
										HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		String responseStr = "";
		int staffId = eliteUtils.getLoggedInStaffId(request);
		try {
			if(EliteUtils.isJSONValid(jsonString)) {
				int mappingId = Integer.valueOf(parserMappingId);
				logger.debug("Fetching all attribute for device configuration : " + parserMappingId);
				ResponseObject oldAttributeResponseObject = parserAttributeService.getAttributeListByMappingId(mappingId, "UPSTREAM");
				@SuppressWarnings("unchecked")
				List<ParserAttribute> oldAttributeList = (List<ParserAttribute>) oldAttributeResponseObject.getObject();
				String delAttrIds = getAttributeIdsFromList(oldAttributeList);
				parserAttributeService.deleteParserAttributes(delAttrIds, staffId);
				responseObject = parserAttributeService.generateAttributeFromJsonString(jsonString,mappingId,parserType,staffId);
				responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_INVALID_JSON_STRING);
			}
		} catch (Exception e) {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.GENERATE_JSON_PARSER_ATTRIBUTES_FAIL);
			logger.trace("Problem occured while generating json parser attributes ", e);
		}
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		JSONArray jsonArray = new JSONArray("[" + ajaxResponse.toString() + "]");
		responseStr = jsonArray.toString();
		return responseStr;
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPLOAD_PARSER_DICTIONARY_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String uploadParserDictionaryData(@RequestParam(value = "parserType", required = true) String parserType,
										@RequestParam(value = "parserMappingId",required=true) String parserMappingId,
										@RequestParam(value = "file", required = true) MultipartFile multipartFile,
										@RequestParam(value = "actionType",defaultValue="NO",required=false) String asnActionType,
										@RequestParam(value = "groupId",defaultValue="0",required=false) String groupId,
										HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject=null;
		String responseStr = "";
		multipartFile.getName();
		multipartFile.getSize();
		int staffId = eliteUtils.getLoggedInStaffId(request);

		if (!multipartFile.isEmpty()) {
			logger.debug("File object found.Going to insert data");
				String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT) + File.separator;
				File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
				try {
					multipartFile.transferTo(lookupDataFile);
					int mappingId = Integer.valueOf(parserMappingId);
					ResponseObject responseObjectMapping  = this.parserMappingService.getMappingDetailsById(mappingId, parserType);
					ASN1ParserMapping asnMapping = (ASN1ParserMapping)responseObjectMapping.getObject();
					List<ASN1ParserAttribute> list = ASN1DefinitionToAttributeList.converDictionaryToPlugin(lookupDataFile, asnMapping);
					if(list != null && !list.isEmpty()){
						
						logger.debug("Fetching all attribute for device configuration : " + parserMappingId);
						ResponseObject oldAttributeResponseObject = parserAttributeService.getAttributeListByMappingId(mappingId, "UPSTREAM");
						@SuppressWarnings("unchecked")
						List<ParserAttribute> oldAttributeList = (List<ParserAttribute>) oldAttributeResponseObject.getObject();
						String delAttrs =getAttributeIdsActionTypeFromList(oldAttributeList,asnActionType);
						parserAttributeService.deleteParserAttributes(delAttrs, staffId);
						
						for(ASN1ParserAttribute attribute : list){
							attribute.setAttrType(ASN1ATTRTYPE.ATTRIBUTE);
							attribute.setParserMapping(asnMapping);
							responseObject = parserAttributeService.createParserAttributes(attribute, mappingId, parserType, staffId);	
						}
						if(responseObject!=null) {
							responseObject.setSuccess(true);
							responseObject.setObject(null);
							responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
						}
						ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						JSONArray jsonArray = new JSONArray("[" + ajaxResponse.toString() + "]");
						responseStr = jsonArray.toString();
					}else {
						throw new NullPointerException();
					}
					
				} catch (Exception e) {
					responseObject=new ResponseObject();
					responseObject.setSuccess(false);
					responseObject.setObject(null);
					responseObject.setResponseCode(ResponseCode.ASN1_DICTIONARY_FILE_UPLOAD_FAILURE);
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
					JSONArray jsonArray = new JSONArray("[" + ajaxResponse.toString() + "]");
					responseStr = jsonArray.toString();
					logger.trace("Problem occured while uploading definition", e);
				}

				
		}
		return responseStr;
	}

	
	private String getVarLengthBinaryAttributeIdsActionTypeFromList(List<ParserAttribute> list,String actionType) {
		String atttributeIds = null;
		String aType;
		if(actionType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE)){
			aType = "HEADER";
		}else{
			aType = "ATTRIBUTE";
		}
		if(list!=null && list.size()>0) {			
			for(ParserAttribute attribute : list) {
				VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) attribute;				
				if(varLengthBinaryParserAttribute.getAttrType().name().equals(aType)){
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
	
	private String getAttributeIdsFromList(List<ParserAttribute> list) {
		String atttributeIds = null;
		if(list!=null && list.size()>0) {			
			for(ParserAttribute attribute : list) {
				if(atttributeIds==null)
					atttributeIds = String.valueOf(attribute.getId());
				else
					atttributeIds = atttributeIds +BaseConstants.COMMA_SEPARATOR+ attribute.getId();
			}
		}
		return atttributeIds;
	}
	private String getAttributeIdsActionTypeFromList(List<ParserAttribute> list,String actionType) {
		String atttributeIds = null;
		String aType;
		if(actionType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE)){
			aType = "HEADER";
		}else if(actionType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)){
			aType = "TRAILER";
		}else{
			aType = "ATTRIBUTE";
		}
		if(list!=null && list.size()>0) {			
			for(ParserAttribute attribute : list) {
				ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) attribute;				
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
	
	private String getAttributeIdsForVLBPByActionType(List<ParserAttribute> list,String actionType) {
		String atttributeIds = null;
		String aType;
		if(actionType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE)){
			aType = "HEADER";
		}else {
			aType = "ATTRIBUTE";
		}
		if(list!=null && list.size()>0) {			
			for(ParserAttribute attribute : list) {
				VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) attribute;				
				if(varLengthBinaryParserAttribute.getAttrType().name().equals(aType)){
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
	
	/** Code for MED-9753 : As Development manager, I want ASCII parser plug in to allow unrestricted 
	 * set of unified fields, so that reporting can be done more than 100 fields as 
	 * they are received from Network element*/
	
	@RequestMapping(value = "getUnifiedFieldList", method = RequestMethod.GET)
	@ResponseBody 
	public List<String> getUnifiedFieldList(@RequestParam String tagName,@RequestParam String typeOfData) {
		if(typeOfData.equalsIgnoreCase(ControllerConstants.GET_FIXED_UNIFIED_FIELD)) {
		   return AutoSuggestUtilForUnifiedField.unifiedFieldList(tagName);
		}
		else if(typeOfData.equalsIgnoreCase(ControllerConstants.GET_All_UNIFIED_FIELD)) {
			List<String> list=parserAttributeService.getListOfUnifiedFields();
			return AutoSuggestUtilAfterSpace.simulateSearchResultAfterSpace(tagName,list);
		}
		return AutoSuggestUtilForUnifiedField.unifiedFieldList(tagName);
	}
	
}
