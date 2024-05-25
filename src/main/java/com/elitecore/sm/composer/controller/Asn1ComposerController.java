package com.elitecore.sm.composer.controller;

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
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.DataTypeEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerArgumentTypeEnum;
import com.elitecore.sm.composer.model.DestinationFieldFormat;
import com.elitecore.sm.composer.service.Asn1ComposerService;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.util.DateFormatter;

// TODO: Auto-generated Javadoc
/**
 * The Class Asn1ComposerController.
 *
 * @author avani.panchal
 */
@Controller
public class Asn1ComposerController extends BaseController{
	
	/** The device type service. */
	@Autowired
	DeviceTypeService deviceTypeService;
	
	/** The device service. */
	@Autowired
	DeviceService deviceService;
	
	/** The composer service. */
	@Autowired
	ComposerService composerService;
	
	/** The composer mapping validator. */
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	
	/** The composer mapping service. */
	@Autowired
	ComposerMappingService composerMappingService;
	
	/** The asn1 composer service. */
	@Autowired
	Asn1ComposerService asn1ComposerService;
	
	
	/**
	 * Inits the binder.
	 *
	 * @param binder the binder
	 */
	@InitBinder()
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	/**
	 * Inits the asn composer config.
	 *
	 * @param requestParamMap the request param map
	 * @return the model and view
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASN1_COMPOSER_MANGER, method = RequestMethod.GET)
	public ModelAndView initAsnComposerConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_COMPOSER_MANAGER);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN, (ASN1ComposerMapping) SpringApplicationContext.getBean(ASN1ComposerMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_COMPOSER_CONFIGURATION);
		model.addObject(BaseConstants.COMPOSER_MAPPING_ID,   mappingId);
		
		return model;
	}
	
	
	/**
	 * Inits the asn composer attribute.
	 *
	 * @param requestParamMap the request param map
	 * @return the model and view
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASN1_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initAsnComposerAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_COMPOSER_MANAGER);
		int asn1ComposerMappingId;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			asn1ComposerMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=asn1ComposerService.getAsn1ComposerMappingById(asn1ComposerMappingId);
			 
			if(responseObject.isSuccess()){
				
				ASN1ComposerMapping asn1Composer=(ASN1ComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN,asn1Composer);
				logger.debug("dateFormatEnum::"+asn1Composer.getDateFormatEnum());
				logger.debug("destdateFormat::"+asn1Composer.getDestDateFormat());
					
			}else{
				model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN,(ASN1ComposerMapping) SpringApplicationContext.getBean(ASN1ComposerMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		if(StringUtils.isNotEmpty(requestParamMap.get(BaseConstants.ASN1_COMPOSER_ATTRIBUTE_TYPE))){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestParamMap.get(BaseConstants.ASN1_COMPOSER_ATTRIBUTE_TYPE));
		}else{
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_COMPOSER_ATTRIBUTE);
		}
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(DataTypeEnum.values())); 
		model.addObject(BaseConstants.TRUE_FALSE_ENUM,java.util.Arrays.asList(TrueFalseEnum.values())); 
		model.addObject("destFieldDataFormat",java.util.Arrays.asList(DestinationFieldFormat.values()));
		model.addObject("paddingTypeEnum",java.util.Arrays.asList(PositionEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,"true");
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get("mappingId") );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get("deviceId") );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get("mappingName"));
	
		model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ASN1ComposerAttribute.class));
		
		return model;
	}
	
	
	/**
	 * Updateassociate as n1 composer mapping.
	 *
	 * @param asn1ComposerMapping the asn1 composer mapping
	 * @param requestParamMap the request param map
	 * @param result the result
	 * @param request the request
	 * @return the model and view
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_ASN1_COMPOSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateASN1ComposerMapping(
			@ModelAttribute (value=FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN)  ASN1ComposerMapping  asn1ComposerMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASN1_COMPOSER_MANAGER); 
		String composerMappingId=requestParamMap.get("id");
		String requestAction = BaseConstants.ASN1_COMPOSER_ATTRIBUTE;
		int asn1ComposerMappingId=0;
		if(composerMappingId !=null){
			asn1ComposerMappingId=Integer.parseInt(composerMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
			logger.debug("Asn1 Composer Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
			updateAndAssociateComposerMapping(asn1ComposerMapping,requestParamMap,asn1ComposerMappingId,model,staffId,requestAction);
		
		}else{
			logger.debug("Asn1 Parser Configuration is updated , first update and then associate configuration with parser");
			boolean validationRequired = true;
			String actionType = "actionType";
			String nextAttributeType = requestParamMap.get("nextAttributeType");
			if(nextAttributeType != null && (nextAttributeType.equals(BaseConstants.ASN1_HEADER_COMPOSER_ATTRIBUTE) || 
					nextAttributeType.equals(BaseConstants.ASN1_TRAILER_COMPOSER_ATTRIBUTE)) ){
				validationRequired = false;
				requestAction = requestParamMap.get("nextAttributeType");
				requestParamMap.remove(actionType);
				requestParamMap.putIfAbsent(actionType, "NO_ACTION");
			}
			if(validationRequired)
				composerMappingValidator.validateComposerMappingParameter(asn1ComposerMapping, result, null, false);
			
			logger.debug("Asn1 Composer Configuration is updated , first update and then associate configuration with parser");
			if(result.hasErrors()){
				
				model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN, asn1ComposerMapping);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_COMPOSER_CONFIGURATION);
				model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
				model.addObject(BaseConstants.READ_ONLY_FLAG, false);
				model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
				model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
				model.addObject("isValidationFail",true);
				model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
				
				model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get("selecteMappingName") );
			
				getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
				
				model.addObject(BaseConstants.SELECTED_DEVICE_ID, requestParamMap.get("selDeviceId"));
				model.addObject(BaseConstants.SELECTED_MAPPING_ID,    requestParamMap.get("selMappingId"));
				model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID, requestParamMap.get("selVendorTypeId"));
				model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID, requestParamMap.get("selDeviceTypeId") );
				addCommonParamToModel(requestParamMap,model);
				
			}else{
				logger.debug("Validation done successfully , going to update and associate parser mapping ");
				updateAndAssociateComposerMapping(asn1ComposerMapping,requestParamMap,asn1ComposerMappingId,model,staffId,requestAction);
			}
				
		}
		return model;

	}
	
	/**
	 * Update and associate parser mapping.
	 *
	 * @param ASN1ComposerMapping the AS n1 composer mapping
	 * @param requestParamMap the request param map
	 * @param asn1ComposerMappingId the asn1 composer mapping id
	 * @param model the model
	 * @param staffId the staff id
	 * @param requestAction 
	 */
	private void updateAndAssociateComposerMapping(ASN1ComposerMapping ASN1ComposerMapping,Map<String,String> requestParamMap,int asn1ComposerMappingId,ModelAndView model,int staffId, String requestAction){
		
		ResponseObject responseObject =  composerMappingService.updateAndAssociateComposerMapping(ASN1ComposerMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_COMPOSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			
			responseObject=asn1ComposerService.getAsn1ComposerMappingById(asn1ComposerMappingId);
		 
			if(responseObject.isSuccess()){
				
				ASN1ComposerMapping asciiComposer=(ASN1ComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN,asciiComposer);
		
			}else{
				model.addObject(FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN,(ASN1ComposerMapping) SpringApplicationContext.getBean(ASN1ComposerMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestAction);
			model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject("mappingName",requestParamMap.get("selecteMappingName") );
			model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject("dataTypeEnum",java.util.Arrays.asList(DataTypeEnum.values())); 
			model.addObject("trueFalseEnum",java.util.Arrays.asList(TrueFalseEnum.values())); 
			model.addObject("argumentDataType",java.util.Arrays.asList(ComposerArgumentTypeEnum.values())); 
			model.addObject("destFieldDataFormat",java.util.Arrays.asList(DestinationFieldFormat.values()));
			model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ASN1ComposerAttribute.class));
			
		}else{
			model = new ModelAndView(ViewNameConstants.ASN1_COMPOSER_MANAGER); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASN1_COMPOSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	/**
	 * Update ASN1 composer basic detail from attribute tab.
	 *
	 * @param asciiComposer the ascii composer
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_ASN1_COMPOSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateAsn1ParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.ASN1_COMPOSER_MAPPING_FORM_BEAN) ASN1ComposerMapping asciiComposer,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		composerMappingValidator.validateBasicDetail(asciiComposer,result,null,false);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
			asciiComposer.setLastUpdatedDate(new Date());
			asciiComposer.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject=asn1ComposerService.updateASN1ComposerMapping(asciiComposer);
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}


	/**
	 * Get Device and vendor type detail.
	 *
	 * @param model the model
	 * @param composerId the composer id
	 * @return the device and vendor type detail
	 */
	@SuppressWarnings("unchecked")
	public int getDeviceAndVendorTypeDetail(ModelAndView model,String composerId){
		
		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.DOWNSTREAM);
		//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
		
		if(responseObject.isSuccess()){
			model.addObject("deviceTypeList",(List<DeviceType>) responseObject.getObject());
		}else{
			model.addObject("deviceTypeList",getMessage("device.type.not.found"));
		}

		Composer composer = composerService.getComposerMappingDetailsByComposerId(Integer.parseInt(composerId));
		int deviceId = -1 ;
		int mappingId = -1 ;
		int vendorTypeId = -1;
		int deviceTypeId = -1;	
		
		if(composer != null){
			mappingId = composer.getComposerMapping().getId();
			deviceId =  composer.getComposerMapping().getDevice().getId();
			vendorTypeId =  composer.getComposerMapping().getDevice().getVendorType().getId();
			deviceTypeId =  composer.getComposerMapping().getDevice().getDeviceType().getId(); 
		}
		
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
		model.addObject("deviceId",deviceId );
		
		return mappingId;
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
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE));
		model.addObject(BaseConstants.DIST_DRIVER_ID,requestParamMap.get(BaseConstants.DIST_DRIVER_ID));
		model.addObject(BaseConstants.DIST_DRIVER_NAME,requestParamMap.get(BaseConstants.DIST_DRIVER_NAME));
		model.addObject(BaseConstants.DIST_DRIVERTYPE_ALIAS,requestParamMap.get(BaseConstants.DIST_DRIVERTYPE_ALIAS));
	}
	
	
}
