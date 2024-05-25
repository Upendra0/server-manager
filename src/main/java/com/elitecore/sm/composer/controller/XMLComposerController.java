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
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.XMLComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerMapping;
import com.elitecore.sm.composer.service.ComposerMappingService;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.composer.service.XMLComposerService;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author Mitul.Vora
 *
 */
	@Controller
	public class XMLComposerController extends BaseController{
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	/** The device service. */
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	ComposerService composerService;
	
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	
	@Autowired
	ComposerMappingService composerMappingService;
	
	@Autowired
	XMLComposerService xmlComposerService;
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
	 * Redirect to xml composer configuration page 
	 * @param requestParamMap
	 * @return
	 */
	
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_XML_COMPOSER_MANAGER, method = RequestMethod.GET)
	public ModelAndView initXMLComposerConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XML_COMPOSER_MANAGER);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN, (XMLComposerMapping) SpringApplicationContext.getBean(XMLComposerMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XML_COMPOSER_CONFIGURATION);
		model.addObject(BaseConstants.COMPOSER_MAPPING_ID,   mappingId);
		model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
		model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
		model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to xml composer attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER','UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.INIT_XML_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initXMLComposerAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XML_COMPOSER_MANAGER);
		int xmlComposerMappingId;
	if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			xmlComposerMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=xmlComposerService.getXMLComposerMappingById(xmlComposerMappingId);
			 
			if(responseObject.isSuccess()){				
				XMLComposerMapping xmlComposer=(XMLComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN,xmlComposer);
				logger.debug("dateFormatEnum::"+xmlComposer.getDateFormatEnum());
				logger.debug("destdateFormat::"+xmlComposer.getDestDateFormat());
				
			}else {
				model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN,(XMLComposerMapping) SpringApplicationContext.getBean(XMLComposerMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XML_COMPOSER_ATTRIBUTE);
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(DataTypeEnum.values())); 
		model.addObject(BaseConstants.TRUE_FALSE_ENUM,java.util.Arrays.asList(TrueFalseEnum.values())); 
		model.addObject(BaseConstants.READ_ONLY_FLAG,"true");
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get("mappingId") );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get("deviceId") );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get("mappingName"));
	
		model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(XMLComposerAttribute.class));
		
		return model;
	}
	
	/**
	 * Update and Associate xml Composer Mapping with parser
	 * @param xmlComposeserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_XML_COMPOSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateXMLComposerMapping(
			@ModelAttribute (value=FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN)  XMLComposerMapping  xmlComposerMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XML_COMPOSER_MANAGER); 
		
		String composerMappingId=requestParamMap.get("id");
		int xmlComposerMappingId=0;
		if(composerMappingId !=null){
			xmlComposerMappingId=Integer.parseInt(composerMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("XML Composer Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateComposerMapping(xmlComposerMapping,requestParamMap,xmlComposerMappingId,model,staffId);
			
		}else {
			logger.debug("XML Composer Configuration is updated , first update and then associate configuration with parser");
				
			composerMappingValidator.validateComposerMappingParameter(xmlComposerMapping, result, null, false);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN, xmlComposerMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XML_COMPOSER_CONFIGURATION);
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
					updateAndAssociateComposerMapping(xmlComposerMapping,requestParamMap,xmlComposerMappingId,model,staffId);
				
				}
				
		}
		return model;

	}
	
	/**
	 * Update and associate parser mapping
	 * @param xmlcomposerserMapping
	 * @param requestParamMap
	 * @param xmlcomposerserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateComposerMapping(XMLComposerMapping xmlComposerMapping,Map<String,String> requestParamMap,int xmlComposerMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  composerMappingService.updateAndAssociateComposerMapping(xmlComposerMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XML_COMPOSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			
			responseObject=xmlComposerService.getXMLComposerMappingById(xmlComposerMappingId);
		 
			if(responseObject.isSuccess()){
				
				XMLComposerMapping xmlComposer=(XMLComposerMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN,xmlComposer);
		
			}else{
				model.addObject(FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN,(XMLComposerMapping) SpringApplicationContext.getBean(XMLComposerMapping.class));
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
			model.addObject("trueFalseEnum",java.util.Arrays.asList(TrueFalseEnum.values())); 
			model.addObject(FormBeanConstants.COMPOSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(XMLComposerAttribute.class));
			
		}else{
			model = new ModelAndView(ViewNameConstants.XML_COMPOSER_MANAGER); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XML_COMPOSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	/**
	 * Update xml composer basic detail from attribute tab
	 * @param xmlComposer
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_XML_COMPOSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody
	public String updateXMLComposeBasicDetail(
			@ModelAttribute(value=FormBeanConstants.XML_COMPOSER_MAPPING_FORM_BEAN) XMLComposerMapping xmlComposer,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		composerMappingValidator.validateXMLBasicDetail(xmlComposer,result,null,false);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
			xmlComposer.setLastUpdatedDate(new Date());
			xmlComposer.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject=xmlComposerService.updateXMLComposerMapping(xmlComposer);
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
