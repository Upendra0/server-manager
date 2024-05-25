/**
 * 
 */
package com.elitecore.sm.parser.controller;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.multipart.MultipartFile;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.RegExParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */

@Controller	
public class ParserMappingController extends BaseController {

	
	@Autowired
	@Qualifier(value = "parserMappingService")
	private ParserMappingService parserMappingService;
	
	@Autowired
	private ParserMappingValidator parserMappingValidator;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private RegExParserService regExParserService;
	
	@Autowired
	SystemParameterService systemParamService;
	
	
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
	 * Method will fetch all vendor list by device type id.
	 * @param deviceTypeId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_MAPPING_ASSOCIATION_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getMappingAssociationDetails(
				@RequestParam(value = "mappingId",required = true) int mappingId,
				@RequestParam(value = "decodeType",required = true) String decodeType ) {
		logger.debug(">> getMappingAssociationDetails in ParserMappingController " + mappingId +":::"+decodeType); 
		ResponseObject responseObject  = this.parserMappingService.getMappingAssociationDetails(mappingId,decodeType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all mapping list based on selected device and parser type.
	 * @param deviceId
	 * @param parserType
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_MAPPING_LIST_BY_DEVICE, method = RequestMethod.POST)
	@ResponseBody public String getMappingListByDevice(
				@RequestParam(value = "deviceId",required = true) int deviceId,
				@RequestParam(value = "parserType",required = true) String parserType) {
		logger.debug(">> getMappingListByDevice in ParserMappingController " + deviceId  + " and " + parserType); 
		ResponseObject responseObject  = this.parserMappingService.getMappingByDeviceAndParserType(deviceId, parserType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all mapping list based on selected device and parser type.
	 * @param deviceId
	 * @param parserType
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_MAPPING_LIST_BY_DEVICE_TYPE, method = RequestMethod.POST)
	@ResponseBody public String getMappingListByDeviceType(
				 @RequestParam(value = "deviceTypeId",required = true) int deviceTypeId,
				@RequestParam(value = "parserType",required = true) String parserType) {
		logger.debug(">> getMappingListByDevice Type in ParserMappingController " + deviceTypeId  + " and " + parserType);
		
		ResponseObject responseObject  = this.parserMappingService.getMappingListByDeviceTypeAndParserType(deviceTypeId, parserType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will get mapping details for selected mapping.
	 * @param mappingId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.GET_MAPPING_DETAILS_ID, method = RequestMethod.POST)
	@ResponseBody public  String getMappingDetailsById(
				@RequestParam(value = "mappingId",required = true) int mappingId,
				@RequestParam(value = "pluginType",required = true) String pluginType) {
		logger.debug(">> getMappingDetailsById in ParserMappingController " + mappingId ); 
		ResponseObject responseObject  = this.parserMappingService.getMappingDetailsById(mappingId, pluginType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 *  Method will create or update mapping 
	 * @param mappingId
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.CREATE_UPDATE_MAPPING_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String createOrUpdateMappingDetails(
			   @ModelAttribute (value=FormBeanConstants.NATFLOW_PARSER_MAPPING_FORM_BEAN) ParserMapping parserMapping,//NOSONAR 
			   @RequestParam(value="actionType",required=true) String actionType,
			   @RequestParam(value="mappingId",required=true) int mappingId,
			   @RequestParam(value = "pluginType",required = true) String pluginType,
			   @RequestParam(value = "pluginId",required = true) int pluginId,
			   BindingResult result,
			   HttpServletRequest request) {
		
		logger.debug(">> createOrUpdateMappingDetails in ParserMappingController " + mappingId  + " " + actionType + " " + pluginType + " " + pluginId); 
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		parserMappingValidator.validateParserMappingName(parserMapping, result, null, false, null);
		
		 if(result.hasErrors()){
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		 }else{
			 int staffId = eliteUtils.getLoggedInStaffId(request);
			 ResponseObject responseObject = this.parserMappingService.createOrUpdateParserMappingDetails(parserMapping, mappingId, staffId, actionType, pluginType, pluginId);
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		 }
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Update Regex Parser Mapping Basic detail 
	 * @param sampleFile
	 * @param regExParser
	 * @param result
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_REGEX_ATTR_BASIC_DETAIL,method=RequestMethod.POST)
	@ResponseBody public  String addRegExAttrBasicDetail(@RequestParam(value = "sampleFile", required = false) MultipartFile sampleFile,
			 @ModelAttribute(value=FormBeanConstants.REGEX_PARSER_MAPPING_FORM_BEAN) RegexParserMapping regExParser,//NOSONAR
			 BindingResult result) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject =new ResponseObject();
		String samplePath=null;
	
		parserMappingValidator.validateParserMappingParameter(regExParser,result,null,false,null);
		try{
			
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
				if (sampleFile !=null) {
					responseObject=systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.REGEXPARSER);
				
					if(responseObject.isSuccess()){
						samplePath=(String)responseObject.getObject();
						logger.debug("Going to store regex Parser Sample file at location: "+samplePath);
						responseObject =regExParserService.getSampleDataFileForRegExParser(regExParser.getId());	
						if(responseObject.isSuccess()){
							logger.debug("If file exists then remove it");
							File uploadedFile=(File)responseObject.getObject();
							uploadedFile.delete();
						}
						File uploadedFile = new File(samplePath+File.separator+regExParser.getId()+"_"+sampleFile.getOriginalFilename());
						sampleFile.transferTo(uploadedFile);
					
						if(uploadedFile.exists()){
							logger.debug("Sample File saved successfully , now save basic detail of regex parser mapping");
							responseObject=regExParserService.addRegExAttrBasicDetail(regExParser,uploadedFile);
						}else{
							logger.debug("Unable to save sample file");
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.REGEX_PARSER_SAMPLE_FILE_SAVE_FAIL);
							responseObject.setArgs(new Object[]{samplePath});
						}
					}else{
						logger.debug("System Backup path is not valid");
					}
				}else{
					logger.debug("File is not uploded found");
					responseObject =regExParserService.getSampleDataFileForRegExParser(regExParser.getId());	
					if(responseObject.isSuccess()){
						File uploadedFile=(File)responseObject.getObject();
						responseObject=regExParserService.addRegExAttrBasicDetail( regExParser,uploadedFile);		
					}
				
				}
				ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
			}
	}catch(Exception e){
		logger.error("Exception Occured:"+e);
		throw new  SMException(e.getMessage());
	}
		
		return ajaxResponse.toString();
		
	}
	
	
}
