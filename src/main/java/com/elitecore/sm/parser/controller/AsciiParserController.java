package com.elitecore.sm.parser.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.FileTypeEnum;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatASCIIEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.AsciiParserService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class AsciiParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	AsciiParserService asciiParserService;
	
	@Autowired
	ParserMappingService parserMappingService;
	
	@Autowired
	ParserMappingValidator parserMappingValidator;
	
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
	 * Redirect to ascii parser configuration page 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASCII_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initAsciiParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN, (AsciiParserMapping) SpringApplicationContext.getBean(AsciiParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject("fileTypeEnum", java.util.Arrays.asList(FileTypeEnum.values()));
		List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
		List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
		logger.debug("Separator enum" +separatorEnum);
		for(SeparatorEnum separator: separatorEnum) {
			if(separator.getValue() != "ssss") {
				delimeter.add(separator);
			}
		}
		model.addObject("SeparatorEnum",delimeter ); 
		model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to ascii parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_ASCII_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initAsciiParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_PARSER_CONFIGURATION);
		int asciiParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			asciiParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=asciiParserService.getAsciiParserMappingById(asciiParserMappingId); 
			if(responseObject.isSuccess()){
				
				AsciiParserMapping asciiParser=(AsciiParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN,asciiParser);
				
			}else{
				model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN,(AsciiParserMapping) SpringApplicationContext.getBean(AsciiParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_PARSER_ATTRIBUTE);
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("readOnlyFlag","true");
		model.addObject("mappingId",requestParamMap.get("mappingId") );
		model.addObject("deviceName",requestParamMap.get("deviceName") );
		model.addObject("deviceId",requestParamMap.get("deviceId") );
		model.addObject("mappingName",requestParamMap.get("mappingName"));
		model.addObject("sourceFieldFormat",java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
		model.addObject("ipPortFieldSeperator",java.util.Arrays.asList(SeparatorEnum.values()));
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
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_ASCII_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateAsciiParserMapping(
			@ModelAttribute (value=FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN)  AsciiParserMapping  asciiParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.ASCII_PARSER_CONFIGURATION); 
		
		String parserMappingId=requestParamMap.get("id");
		//Below code commented as fix of Jira MEDSUP-934
		asciiParserMapping.setFind(requestParamMap.get("find")/*.replace(" ", "[s]")*/);
		asciiParserMapping.setReplace(requestParamMap.get("replace")/*.replace(" ","[s]")*/);
		
		int asciiParserMappingId=0;
		if(parserMappingId !=null){
			asciiParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get("readOnlyFlag"))){
				logger.debug("Ascii Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(asciiParserMapping,requestParamMap,asciiParserMappingId,model,staffId);
			
		}else{
				logger.debug("Ascii Parser Configuration is updated , first update and then associate configuration with parser");
				
				parserMappingValidator.validateParserMappingParameter(asciiParserMapping, result, null, false,null);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN, asciiParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_PARSER_CONFIGURATION);
					model.addObject("fileTypeEnum", java.util.Arrays.asList(FileTypeEnum.values()));
					List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
					List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
					logger.debug("Separator enum" +separatorEnum);
					for(SeparatorEnum separator: separatorEnum) {
						if(separator.getValue() != "ssss") {
							delimeter.add(separator);
						}
					}
					model.addObject("SeparatorEnum",delimeter ); 
					model.addObject("trimPosition", java.util.Arrays.asList(TrimPositionEnum.values()));
					model.addObject("readOnlyFlag", false);
					model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
					model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
					model.addObject("isValidationFail",true);
					model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
					
					model.addObject("mappingName",requestParamMap.get("selecteMappingName") );
				
					getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
					
					model.addObject("selDeviceId", requestParamMap.get("selDeviceId"));
					model.addObject("selMappingId",    requestParamMap.get("selMappingId"));
					model.addObject("selVendorTypeId", requestParamMap.get("selVendorTypeId"));
					model.addObject("selDeviceTypeId", requestParamMap.get("selDeviceTypeId") );
					addCommonParamToModel(requestParamMap,model);
					
				}else{
					logger.debug("Validation done successfully , going to update and associate parser mapping ");
					updateAndAssociateParserMapping(asciiParserMapping,requestParamMap,asciiParserMappingId,model,staffId);
				
				}
				
		}
		return model;

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
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE) );
		
		
	}
	
	/**
	 * Get Device and vendor type detail
	 * @param model
	 * @param pluginId
	 * @return
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
		
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
		model.addObject("deviceId",deviceId );
		
		return mappingId;
}
	
	/**
	 * Update ascii parser basic detail from attribute tab
	 * @param asciiParser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_ASCII_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateAsciiParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN) AsciiParserMapping asciiParser,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		parserMappingValidator.validateSrcDateFormat(asciiParser,result,null,false, null);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
					asciiParser.setLastUpdatedDate(new Date());
					asciiParser.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					responseObject=asciiParserService.updateAsciiParserMapping(asciiParser);
					ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
			}
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Update and associate parser mapping
	 * @param asciiParserMapping
	 * @param requestParamMap
	 * @param asciiParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(AsciiParserMapping asciiParserMapping,Map<String,String> requestParamMap,int asciiParserMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(asciiParserMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_PARSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			
			responseObject=asciiParserService.getAsciiParserMappingById(asciiParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				AsciiParserMapping asciiParser=(AsciiParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN,asciiParser);
		
			}else{
				model.addObject(FormBeanConstants.ASCII_PARSER_MAPPING_FORM_BEAN,(AsciiParserMapping) SpringApplicationContext.getBean(AsciiParserMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
	
			model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject("mappingName",requestParamMap.get("selecteMappingName") );
			model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject("sourceFieldFormat",java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
			model.addObject("ipPortFieldSeperator",java.util.Arrays.asList(SeparatorEnum.values()));
		}else{
			model = new ModelAndView(ViewNameConstants.ASCII_PARSER_CONFIGURATION); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ASCII_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}

}
