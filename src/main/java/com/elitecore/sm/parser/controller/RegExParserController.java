package com.elitecore.sm.parser.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.RegExParserService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class RegExParserController extends BaseController{
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	RegExParserService regExParserService;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	ParserMappingService parserMappingService;


	
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
 * Redirect to regex parser config page
 * @param requestParamMap
 * @param request
 * @return
 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_REGEX_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initRegexParserConfig(
			@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.REGEX_PARSER_CONFIGURATION);
	
		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)));
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
		
		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM); 
		//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
		
		if(responseObject.isSuccess()){
			model.addObject("deviceTypeList",(List<DeviceType>) responseObject.getObject());
		}else{
			model.addObject("deviceTypeList",getMessage("device.type.not.found"));
		}
		model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN, (RegexParserMapping) SpringApplicationContext.getBean(RegexParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.INSTANCE_ID,requestParamMap.get(BaseConstants.INSTANCE_ID) );
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject("plugInName", requestParamMap.get("plugInName"));
		model.addObject("plugInType",requestParamMap.get("plugInType") );
		model.addObject("mappingId",   mappingId);
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
				
		return model;
	}
	
	/**
	 * Redirect to regex parser attribute page
	 * @param requestParamMap
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_REGEX_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initRegexParserAttribute(@RequestParam Map<String,String> requestParamMap,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.REGEX_PARSER_CONFIGURATION);
		int regExParserMappingId=0;
		if(requestParamMap.get("mappingId") !=null){
			
			regExParserMappingId=Integer.parseInt(requestParamMap.get("mappingId"));
			ResponseObject responseObject=regExParserService.getRegExParserMappingById(regExParserMappingId); 
			if(responseObject.isSuccess()){
				
				RegexParserMapping regExParser=(RegexParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,regExParser);
				
				responseObject =regExParserService.getSampleDataFileForRegExParser(regExParserMappingId);	
				if(responseObject.isSuccess()){
					File sampleFile=(File)responseObject.getObject();
					model.addObject("uploadedFileName", sampleFile.getName());
				}
				responseObject=regExParserService.getRegExPatternAndAttrByMappingId(regExParserMappingId);
				if(responseObject.isSuccess()){
					model.addObject("patternList",responseObject.getObject());
				}
				
			}else{
				model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,(RegexParserMapping) SpringApplicationContext.getBean(RegexParserMapping.class));
			}
		
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_ATTRIBUTE);
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.INSTANCE_ID,requestParamMap.get(BaseConstants.INSTANCE_ID) );
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject("plugInName", requestParamMap.get("plugInName"));
		model.addObject("plugInType",requestParamMap.get("plugInType") );
		model.addObject("mappingId",requestParamMap.get("mappingId") );
		model.addObject("deviceName",requestParamMap.get("deviceName") );
		model.addObject("deviceId",requestParamMap.get("deviceId") );
		model.addObject("mappingName",requestParamMap.get("mappingName") );
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("readOnlyFlag","true");
		
		return model;
	}
	
	/**
	 * Generate regex pattern token
	 * @param regExPattern
	 * @param patternListCounter
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.GENERATE_REGEX_PATTERN_ATTRIBUTE,method=RequestMethod.POST)
	public @ResponseBody String generateRegExPatternToken(
			 @ModelAttribute(value=FormBeanConstants.REGEX_PATTERN_CONFIGURATION_FORM_BEAN) RegExPattern regExPattern,//NOSONAR
			 @RequestParam(value="patternListCounter") String patternListCounter,
			 HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
			
		ResponseObject responseObject=regExParserService.generateRegExPatternToken(regExPattern,patternListCounter);
		
		ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
		
	}
		
	/**
	 * Associate RegEx Parser Mapping with parser
	 * @param regExParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_REGEX_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView associateRegExParserMapping(
			@ModelAttribute (value=FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN)  RegexParserMapping  regExParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.REGEX_PARSER_CONFIGURATION); 
		
		String parserMappingId=requestParamMap.get("id");
		int regExParserMappingId=0;
		if(parserMappingId !=null){
			regExParserMappingId=Integer.parseInt(parserMappingId);
		}
		
		if("true".equals(requestParamMap.get("readOnlyFlag"))){
			logger.debug("RegEx Parser Configuration is not updated , just redirect to attribute page in disable mode");
			
				ResponseObject responseObject=regExParserService.getRegExParserMappingById(regExParserMappingId); 
				if(responseObject.isSuccess()){
					
					RegexParserMapping regExParser=(RegexParserMapping)responseObject.getObject();
					model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,regExParser);
					
					responseObject=regExParserService.getRegExPatternAndAttrByMappingId(regExParserMappingId);
					if(responseObject.isSuccess()){
						model.addObject("patternList",responseObject.getObject());
					}
					
				}else{
					model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,(RegexParserMapping) SpringApplicationContext.getBean(RegexParserMapping.class));
				}
			
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_ATTRIBUTE);
			
		}else{
		      
				int staffId = eliteUtils.getLoggedInStaffId(request);
				ResponseObject responseObject  = null;
				
				if(requestParamMap.get("plugInType").equals(EngineConstants.REGEX_PARSING_PLUGIN)){
					 responseObject =  parserMappingService.updateAndAssociateParserMapping(regExParserMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)), requestParamMap.get("actionType"), staffId);
				}
				
				if(responseObject!=null && responseObject.isSuccess()){
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_ATTRIBUTE);
					
					responseObject=regExParserService.getRegExParserMappingById(regExParserMappingId); 
					if(responseObject.isSuccess()){
						
						RegexParserMapping regExParser=(RegexParserMapping)responseObject.getObject();
						model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,regExParser);
						
						responseObject =regExParserService.getSampleDataFileForRegExParser(regExParserMappingId);	
						if(responseObject.isSuccess()){
							File sampleFile=(File)responseObject.getObject();
							model.addObject("uploadedFileName", sampleFile.getName());
						}
						
						responseObject=regExParserService.getRegExPatternAndAttrByMappingId(regExParserMappingId);
						if(responseObject.isSuccess()){
							model.addObject("patternList",responseObject.getObject());
						}
					}else{
						model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,(RegexParserMapping) SpringApplicationContext.getBean(RegexParserMapping.class));
					}
				}else{
					model = new ModelAndView(ViewNameConstants.REGEX_PARSER_CONFIGURATION); 
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
				}
		   
				ResponseObject tempResponseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM); 
				//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
				
				if(tempResponseObject.isSuccess()){
					model.addObject("deviceTypeList",(List<DeviceType>) tempResponseObject.getObject());
				}else{
					model.addObject("deviceTypeList",getMessage("device.type.not.found"));
				}
			model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		}	
		
		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)));
		int deviceId = -1 ,mappingId = -1 ,vendorTypeId = -1,deviceTypeId = -1;			
		if(parser != null){
			mappingId = parser.getParserMapping().getId();
			deviceId =  parser.getParserMapping().getDevice().getId();
			vendorTypeId =  parser.getParserMapping().getDevice().getVendorType().getId();
			deviceTypeId =  parser.getParserMapping().getDevice().getDeviceType().getId(); 
		}
		
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.INSTANCE_ID, requestParamMap.get(BaseConstants.INSTANCE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject("plugInName", requestParamMap.get("plugInName"));
		model.addObject("plugInType",requestParamMap.get("plugInType") );
		
		model.addObject("deviceId",deviceId );
		model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
		model.addObject("mappingId", requestParamMap.get("id"));
		model.addObject("mappingName",requestParamMap.get("selecteMappingName") );
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
		
		model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
	
	return model;

	}
	
	/**
	 * Download regex sample file
	 * @param requestParamMap
	 * @param request
	 * @param response
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_REGEX_SAMPLE_DATA_FILE, method = RequestMethod.POST)
	public ModelAndView downloadSampleFile(@RequestParam Map<String,String> requestParamMap,
																			HttpServletRequest request, HttpServletResponse response) throws SMException{
		
		ModelAndView model = new ModelAndView(ViewNameConstants.REGEX_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REGEX_PARSER_ATTRIBUTE);
		File sampleFile=null;
		int iMappingId=0;
		try{
			
		if(!(StringUtils.isEmpty(requestParamMap.get("mappingId")))){
			iMappingId=Integer.parseInt(requestParamMap.get("mappingId"));
			
			ResponseObject responseObject =regExParserService.getSampleDataFileForRegExParser(iMappingId);	
			if(responseObject.isSuccess()){
				sampleFile=(File)responseObject.getObject();
				
				FileInputStream inputStream = new FileInputStream(sampleFile);	//NOSONAR
				ServletOutputStream outStream = response.getOutputStream();
				
				response.reset();
				response.setContentType(BaseConstants.CONTENT_TYPE_FOR_EXPORT_CONFIG);
				
				response.setHeader("Content-Disposition", "attachment; filename=\"" +sampleFile.getName());
		        byte[] buffer = new byte[(int) sampleFile.length()];
		        int bytesRead = -1;
		        
		        // write bytes read from the input stream into the output stream
		        while ((bytesRead = inputStream.read(buffer)) != -1) {
		            outStream.write(buffer, 0, bytesRead);
		        }

		        outStream.flush();
		        inputStream.close();
		        outStream.close();
			}else{
				logger.debug("Sample data file not found for parser mapping id: "+iMappingId);
				responseObject=regExParserService.getRegExParserMappingById(iMappingId); 
				if(responseObject.isSuccess()){
					
					RegexParserMapping regExParser=(RegexParserMapping)responseObject.getObject();
					model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,regExParser);
					
					responseObject=regExParserService.getRegExPatternAndAttrByMappingId(iMappingId);
					if(responseObject.isSuccess()){
						model.addObject("patternList",responseObject.getObject());
					}
					
				}else{
					model.addObject(FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN,(RegexParserMapping) SpringApplicationContext.getBean(RegexParserMapping.class));
				}
				model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
				model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
				model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
				model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
				model.addObject("plugInName", requestParamMap.get("plugInName"));
				model.addObject("plugInType",requestParamMap.get("plugInType") );
				model.addObject("mappingId",requestParamMap.get("mappingId") );
				model.addObject("deviceName",requestParamMap.get("deviceName") );
				model.addObject("deviceId",requestParamMap.get("deviceId") );
				model.addObject("mappingName",requestParamMap.get("mappingName") );
				model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
				model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
				model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
				model.addObject("readOnlyFlag","false");
				model.addObject(BaseConstants.ERROR_MSG, getMessage("regex.parser.sample.file.not.found"));	
			}
		}
		}catch(Exception e){
			logger.error("Exception Occured:"+e);
			throw new  SMException(e.getMessage());
		}
			return model;
		}

		
}
