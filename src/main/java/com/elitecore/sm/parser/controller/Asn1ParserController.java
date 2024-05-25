package com.elitecore.sm.parser.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.ASN1DecodeTypeEnum;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.SourceFieldDataFormatEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.Asn1ParserService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * The Class Asn1ParserController.
 * 
 * @author Sagar R Patel
 */
@Controller
public class Asn1ParserController extends BaseController {

	/** The parser service. */
	@Autowired
	ParserService parserService;
	
	/** The device type service. */
	@Autowired
	DeviceTypeService deviceTypeService;
	
	/** The device service. */
	@Autowired
	DeviceService deviceService;
	
	/** The asn1 parser service. */
	@Autowired
	Asn1ParserService asn1ParserService;
	
	/** The parser mapping service. */
	@Autowired
	ParserMappingService parserMappingService;
	
	/** The parser mapping validator. */
	@Autowired
	ParserMappingValidator parserMappingValidator;
	
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
	 * Inits the asn1 parser config.
	 * @param requestParamMap the request param map
	 * @return the model and view
	 */
	@RequestMapping(value = ControllerConstants.INIT_ASN1_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initAsn1ParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		addCommonParamToModel(requestParamMap,model);
		
		ASN1ParserMapping asn1ParserMapping = getASN1ParserMapping(mappingId);
		
		model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN, asn1ParserMapping);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("recMainAttribute",   asn1ParserMapping.getRecMainAttribute());
		model.addObject("removeAddByte",   asn1ParserMapping.isRemoveAddByte());
		model.addObject("headerOffset",   asn1ParserMapping.getHeaderOffset());
		model.addObject("recOffset",   asn1ParserMapping.getRecOffset());
		model.addObject("removeFillers",   asn1ParserMapping.getRemoveFillers());
		model.addObject("removeAddHeaderFooter",   asn1ParserMapping.isRemoveAddHeaderFooter());
		model.addObject("recordStartIds",   asn1ParserMapping.getRecordStartIds());
		model.addObject("skipAttributeMapping",   asn1ParserMapping.isSkipAttributeMapping());
		model.addObject("rootNodeName",   asn1ParserMapping.getRootNodeName());
		model.addObject("decodeFormat",   asn1ParserMapping.getDecodeFormat());
		model.addObject("bufferSize",   asn1ParserMapping.getBufferSize());
		model.addObject("asn1DecodeTypeEnum", java.util.Arrays.asList(ASN1DecodeTypeEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to asn1 parser attribute page.
	 *
	 * @param requestParamMap the request param map
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASN1_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initAsn1ParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_PARSER_CONFIGURATION);
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			int asn1ParserMappingId = Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=asn1ParserService.getAsn1ParserMappingById(asn1ParserMappingId);
			if(responseObject.isSuccess()){
				
				ASN1ParserMapping asn1ParserMapping =(ASN1ParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN,asn1ParserMapping);
				
			}else{
				model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN,(ASN1ParserMapping) SpringApplicationContext.getBean(ASN1ParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		if(StringUtils.isNotEmpty(requestParamMap.get(BaseConstants.ASN1_PARSER_ATTRIBUTE_TYPE))){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestParamMap.get(BaseConstants.ASN1_PARSER_ATTRIBUTE_TYPE));
		}else{
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_PARSER_ATTRIBUTE);
		}
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("srcDataFormat",java.util.Arrays.asList(SourceFieldDataFormatEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,"true");
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get(BaseConstants.DEVICE_ID) );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.PARSER_MAPPING_NAME));
		model.addObject(FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ASN1ParserAttribute.class));
		return model;
	}
	
	
	/**
	 * Updateassociate as n1 parser mapping.
	 *
	 * @param asn1ParserMapping the asn1 parser mapping
	 * @param requestParamMap the request param map
	 * @param result the result
	 * @param request the request
	 * @return the model and view
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_ASN1_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateASN1ParserMapping(
			@ModelAttribute (value=FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN)  ASN1ParserMapping  asn1ParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_PARSER_CONFIGURATION); 
		String requestAction = BaseConstants.ASN1_PARSER_ATTRIBUTE;
		String parserMappingId=requestParamMap.get("id");
		int asn1ParserMappingId=0;
		if(parserMappingId !=null){
			asn1ParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("Asn1 Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(asn1ParserMapping,requestParamMap,asn1ParserMappingId,model,staffId, requestAction);
		}else{
				logger.debug("Asn1 Parser Configuration is updated , first update and then associate configuration with parser");
				boolean validationRequired = true;
				String nextAttributeType = requestParamMap.get("nextAttributeType");
				if(nextAttributeType != null && (nextAttributeType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE) || 
						nextAttributeType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE)) ){
					validationRequired = false;
					requestAction = requestParamMap.get("nextAttributeType");
				}
				if(validationRequired)
					parserMappingValidator.validateParserMappingParameter(asn1ParserMapping, result, null, false,null);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN, asn1ParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.READ_ONLY_FLAG, false);
					model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
					model.addObject("asn1DecodeTypeEnum", java.util.Arrays.asList(ASN1DecodeTypeEnum.values()));
					model.addObject("isValidationFail",true);
					model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
					model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME) );
					getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
					model.addObject(BaseConstants.SELECTED_DEVICE_ID, requestParamMap.get(BaseConstants.SELECTED_DEVICE_ID));
					model.addObject(BaseConstants.SELECTED_MAPPING_ID,    requestParamMap.get(BaseConstants.SELECTED_MAPPING_ID));
					model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID, requestParamMap.get(BaseConstants.SELECTED_VENDOR_TYPE_ID));
					model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID, requestParamMap.get(BaseConstants.SELECTED_DEVICE_TYPE_ID));
					addCommonParamToModel(requestParamMap,model);
				}else{
					logger.debug("Validation done successfully , going to update and associate parser mapping ");
					updateAndAssociateParserMapping(asn1ParserMapping,requestParamMap,asn1ParserMappingId,model,staffId,requestAction);
				}
		}
		return model;

	}

	/**
	 * Add Commom paramter to model .
	 *
	 * @param requestParamMap the request param map
	 * @param model the model
	 */
	public void addCommonParamToModel(Map<String,String> requestParamMap,ModelAndView model){
		
		
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.SERVER_INSTANCE_ID,requestParamMap.get(BaseConstants.SERVER_INSTANCE_ID) );
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE) );
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		
	}
	
	/**
	 * Get Device and vendor type detail.
	 *
	 * @param model the model
	 * @param pluginId the plugin id
	 * @return the device and vendor type detail
	 */
	@SuppressWarnings("unchecked")
	public int getDeviceAndVendorTypeDetail(ModelAndView model,String pluginId){
		
		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM);
		//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
		
		if(responseObject.isSuccess()){
			model.addObject("deviceTypeList",(List<DeviceType>) responseObject.getObject());
		}else{
			model.addObject("deviceTypeList",getMessage("device.type.not.found"));
		}

		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(pluginId));
		int deviceId = -1 ;
		int mappingId = -1 ;
		int vendorTypeId = -1;
		int deviceTypeId = -1;	
		
		if(parser != null){
			mappingId = parser.getParserMapping().getId();
			deviceId =  parser.getParserMapping().getDevice().getId();
			vendorTypeId =  parser.getParserMapping().getDevice().getVendorType().getId();
			deviceTypeId =  parser.getParserMapping().getDevice().getDeviceType().getId(); 
		}
		
		model.addObject(BaseConstants.SELECTED_DEVICE_ID, deviceId);
		model.addObject(BaseConstants.SELECTED_MAPPING_ID,   mappingId);
		model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID, vendorTypeId);
		model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID, deviceTypeId );
		model.addObject("deviceId",deviceId );
		
		return mappingId;
}
	
	/**
	 * Update asn1 parser basic detail from attribute tab.
	 *
	 * @param asn1Parser the asn1 parser
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_ASN1_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateAsn1ParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN) ASN1ParserMapping asn1Parser,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		parserMappingValidator.validateSrcDateFormat(asn1Parser,result,null,false, null);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
			asn1Parser.setLastUpdatedDate(new Date());
			asn1Parser.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject= asn1ParserService.updateAsn1ParserMapping(asn1Parser);
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Update and associate parser mapping.
	 *
	 * @param asn1ParserMapping the asn1 parser mapping
	 * @param requestParamMap the request param map
	 * @param asn1ParserMappingId the asn1 parser mapping id
	 * @param model the model
	 * @param staffId the staff id
	 * @param requestAction the request action
	 */
	private void updateAndAssociateParserMapping(ASN1ParserMapping asn1ParserMapping,Map<String,String> requestParamMap,
									int asn1ParserMappingId,ModelAndView model,int staffId,String requestAction){
		
		boolean bUpdate = Boolean.TRUE; 
		if(requestAction.equals(BaseConstants.ASN1_PARSER_ATTRIBUTE)){
			ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(asn1ParserMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
			if(responseObject == null || !responseObject.isSuccess()){
				bUpdate = Boolean.FALSE;
			}
		}
		
		if(bUpdate){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestAction);
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			ResponseObject responseObject= asn1ParserService.getAsn1ParserMappingById(asn1ParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				ASN1ParserMapping asn1Parser=(ASN1ParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN,asn1Parser);
		
			}else{
				model.addObject(FormBeanConstants.ASN1_PARSER_MAPPING_FORM_BEAN,(ASN1ParserMapping) SpringApplicationContext.getBean(ASN1ParserMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
	
			model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME) );
			
			model.addObject(BaseConstants.SELECTED_DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
			model.addObject("id", requestParamMap.get("id"));
			model.addObject(BaseConstants.SELECTED_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME) );
			
			model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject("srcDataFormat",java.util.Arrays.asList(SourceFieldDataFormatEnum.values()));
			model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		}else{
			model.setViewName(ViewNameConstants.ASN1_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}

	/**
	 * Gets the ASN1 parser mapping.
	 *
	 * @param asn1ParserMappingId the asn1 parser mapping id
	 * @return the ASN1 parser mapping
	 */
	private ASN1ParserMapping getASN1ParserMapping(int asn1ParserMappingId){
		
		if(asn1ParserMappingId > 0){
			
			ResponseObject responseObject=asn1ParserService.getAsn1ParserMappingById(asn1ParserMappingId);
			if(responseObject.isSuccess()){
				
				return (ASN1ParserMapping)responseObject.getObject();
				
			}else{
				return (ASN1ParserMapping) SpringApplicationContext.getBean(ASN1ParserMapping.class);
			}
		}else{
			return (ASN1ParserMapping) SpringApplicationContext.getBean(ASN1ParserMapping.class);
		}
	}
	
}
