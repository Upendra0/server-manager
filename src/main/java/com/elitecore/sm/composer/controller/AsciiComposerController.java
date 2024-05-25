package com.elitecore.sm.composer.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.service.AsciiComposerService;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.AsciiFileHeaderFooterSummaryFunctionEnum;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.util.DateFormatter;
/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class AsciiComposerController extends BaseController{
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	ComposerService composerService;
	
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	
	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	AsciiComposerService asciiComposerService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder()
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	/**
	 * Redirect to ascii composer configuration page 
	 * @param requestParamMap
	 * @return
	 */
	
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASCII_COMPOSER_MANGER, method = RequestMethod.GET)
	public ModelAndView initAsciiComposerConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_COMPOSER_MANAGER);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN, (ASCIIComposerMapping) SpringApplicationContext.getBean(ASCIIComposerMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_COMPOSER_CONFIGURATION);
		model.addObject(BaseConstants.COMPOSER_MAPPING_ID,   mappingId);

		model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
		model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
		model.addObject("fileUtilityFunctionEnum", java.util.Arrays.asList(AsciiFileHeaderFooterSummaryFunctionEnum.values()));
		model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to ascii composer attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASCII_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initAsciiComposerAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_COMPOSER_MANAGER);
		int asciiComposerMappingId;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			asciiComposerMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=asciiComposerService.getAsciiComposerMappingById(asciiComposerMappingId);
			 
			if(responseObject.isSuccess()){
				
				ASCIIComposerMapping asciiComposer=(ASCIIComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN,asciiComposer);
			logger.debug("dateFormatEnum::"+asciiComposer.getDateFormatEnum());
			logger.debug("destdateFormat::"+asciiComposer.getDestDateFormat());
				
			}else{
				model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN,(ASCIIComposerMapping) SpringApplicationContext.getBean(ASCIIComposerMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_COMPOSER_ATTRIBUTE);
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(DataTypeEnum.values())); 
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject(BaseConstants.TRUE_FALSE_ENUM,java.util.Arrays.asList(TrueFalseEnum.values())); 
		model.addObject("paddingTypeEnum",java.util.Arrays.asList(PositionEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,"true");
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get("mappingId") );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get("deviceId") );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get("mappingName"));
	
		model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ASCIIComposerAttr.class));
		
		return model;
	}
	
	/**
	 * Update and Associate Ascii Parser Mapping with parser
	 * @param regExParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_ASCII_COMPOSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateAsciiComposerMapping(
			@ModelAttribute (value=FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN)  ASCIIComposerMapping  asciiComposerMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_COMPOSER_MANAGER); 
		
		String composerMappingId=requestParamMap.get("id");
		int asciiComposerMappingId=0;
		if(composerMappingId !=null){
			asciiComposerMappingId=Integer.parseInt(composerMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("Ascii Composer Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateComposerMapping(asciiComposerMapping,requestParamMap,asciiComposerMappingId,model,staffId);
			
		}else{
				logger.debug("Ascii Composer Configuration is updated , first update and then associate configuration with parser");
				
				composerMappingValidator.validateComposerMappingParameter(asciiComposerMapping, result, null, false);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN, asciiComposerMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_COMPOSER_CONFIGURATION);
					model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
					model.addObject(BaseConstants.READ_ONLY_FLAG, false);
					model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
					model.addObject("fileUtilityFunctionEnum", java.util.Arrays.asList(AsciiFileHeaderFooterSummaryFunctionEnum.values()));
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
					updateAndAssociateComposerMapping(asciiComposerMapping,requestParamMap,asciiComposerMappingId,model,staffId);
				
				}
				
		}
		return model;

	}
	
	/**
	 * Update and associate parser mapping
	 * @param asciiParserMapping
	 * @param requestParamMap
	 * @param asciiParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateComposerMapping(ASCIIComposerMapping asciiComposerMapping,Map<String,String> requestParamMap,int asciiComposerMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  composerMappingService.updateAndAssociateComposerMapping(asciiComposerMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_COMPOSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			
			responseObject=asciiComposerService.getAsciiComposerMappingById(asciiComposerMappingId);
		 
			if(responseObject.isSuccess()){
				
				ASCIIComposerMapping asciiComposer=(ASCIIComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN,asciiComposer);
		
			}else{
				model.addObject(FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN,(ASCIIComposerMapping) SpringApplicationContext.getBean(ASCIIComposerMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
	
			model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject("mappingName",requestParamMap.get("selecteMappingName") );
			model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject("dataTypeEnum",java.util.Arrays.asList(DataTypeEnum.values())); 
			model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject("trueFalseEnum",java.util.Arrays.asList(TrueFalseEnum.values())); 
			model.addObject("paddingTypeEnum",java.util.Arrays.asList(PositionEnum.values())); 
			model.addObject("fileUtilityFunctionEnum", java.util.Arrays.asList(AsciiFileHeaderFooterSummaryFunctionEnum.values()));
			model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ASCIIComposerAttr.class));
			
		}else{
			model = new ModelAndView(ViewNameConstants.ASCII_COMPOSER_MANAGER); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_COMPOSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	/**
	 * Update ascii composer basic detail from attribute tab
	 * @param asciiComposer
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_ASCII_COMPOSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateAsciiParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.ASCII_COMPOSER_MAPPING_FORM_BEAN) ASCIIComposerMapping asciiComposer,//NOSONAR
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
			responseObject=asciiComposerService.updateAsciiComposerMapping(asciiComposer);
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}


	/**
	 * Get Device and vendor type detail
	 * @param model
	 * @param pluginId
	 * @return
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
	 * Add Commom paramter to model 
	 * @param requestParamMap
	 * @param model
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
