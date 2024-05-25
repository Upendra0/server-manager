package com.elitecore.sm.parser.controller;

import java.io.File;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.util.Constant;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.FileTypeEnum;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatASCIIEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.VarLengthAsciiParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class VarLengthAsciiParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	VarLengthAsciiParserService varLengthAsciiParserService;
	
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
	 * Redirect to var length ascii parser configuration page 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_VAR_LENGTH_ASCII_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initVarLengthAsciiParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		int serverInstanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));

		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN, (VarLengthAsciiParserMapping) SpringApplicationContext.getBean(VarLengthAsciiParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject(BaseConstants.FILETYPEENUM, java.util.Arrays.asList(FileTypeEnum.values()));
		List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
		List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
		logger.debug("Separator enum" +separatorEnum);
		for(SeparatorEnum separator: separatorEnum) {
			if(separator.getValue() != "ssss") {
				delimeter.add(separator);
			}
		}
		model.addObject("SeparatorEnum",delimeter ); 
		model.addObject(BaseConstants.HEADERFOOTERTYPEENUM, java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
		model.addObject(BaseConstants.TRUEFALSEENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.DATADEFINITIONFILEENUM, parserService.getDataDefinitionFileList(serverInstanceId));
		return model;
	}
	
	/**
	 * Redirect to ascii parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initVarLengthAsciiParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
		int varLAParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			varLAParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=varLengthAsciiParserService.getVarLengthAsciiParserMappingById(varLAParserMappingId); 
			if(responseObject.isSuccess()){
				
				VarLengthAsciiParserMapping vLengthAsciiParserMapping=(VarLengthAsciiParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN,vLengthAsciiParserMapping);
				
			}else{
				model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN,(VarLengthAsciiParserMapping) SpringApplicationContext.getBean(VarLengthAsciiParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_ATTRIBUTE);
		model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject(BaseConstants.TRIM_POSITION,java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,BaseConstants.TRUE);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get(BaseConstants.DEVICE_ID) );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.PARSER_MAPPING_NAME));
		model.addObject(BaseConstants.SOURCE_FIELD_FORMAT,java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
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
		
		if(responseObject.isSuccess()){
			model.addObject(BaseConstants.DEVICE_TYPE_LIST,(List<DeviceType>) responseObject.getObject());
		}else{
			model.addObject(BaseConstants.DEVICE_TYPE_LIST,getMessage("device.type.not.found"));
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
		model.addObject(BaseConstants.DEVICE_ID,deviceId );
		
		return mappingId;
	}
	
	/**
	 * Update and Associate Var Length Ascii Parser Mapping with parser
	 * @param varLengthAsciiParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_VAR_LENGTH_ASCII_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateVarLengthAsciiParserConfiguration(
			@ModelAttribute (value=FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN)  VarLengthAsciiParserMapping  varLengthAsciiParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION); 
		if(varLengthAsciiParserMapping.getDataDefinitionPath()!=null && !varLengthAsciiParserMapping.getDataDefinitionPath().isEmpty()){
			int instanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));
			ServerInstance serverInstance = serverInstanceService.getServerInstance(instanceId);
			varLengthAsciiParserMapping.setDataDefinitionPath(serverInstance.getServerHome() + File.separator + Constant.MODULES + File.separator + Constant.MEDIATION + varLengthAsciiParserMapping.getDataDefinitionPath());
		}
		String parserMappingId=requestParamMap.get(BaseConstants.ID);

		varLengthAsciiParserMapping.setFind(requestParamMap.get(BaseConstants.FIND)/*.replace(" ", "[s]")*/);
		varLengthAsciiParserMapping.setReplace(requestParamMap.get(BaseConstants.REPLACE)/*.replace(" ","[s]")*/);
		
		int varLengthAsciiParserMappingId=0;
		if(parserMappingId !=null){
			varLengthAsciiParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if(BaseConstants.TRUE.equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("Var Length Ascii Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(varLengthAsciiParserMapping,requestParamMap,varLengthAsciiParserMappingId,model,staffId);
			
		}else{
				logger.debug("Var Length  Ascii Parser Configuration is updated , first update and then associate configuration with parser");
				
				parserMappingValidator.validateParserMappingParameter(varLengthAsciiParserMapping, result, null, false,null);
				
				if(result.hasErrors()){
					int serverInstanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));
					model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN, varLengthAsciiParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.FILETYPEENUM, java.util.Arrays.asList(FileTypeEnum.values()));
					List<SeparatorEnum> separatorEnum = Arrays.asList(SeparatorEnum.values());	//MEDSUP-2196
					List<SeparatorEnum> delimeter =new ArrayList<SeparatorEnum>();
					logger.debug("Separator enum" +separatorEnum);
					for(SeparatorEnum separator: separatorEnum) {
						if(separator.getValue() != "ssss") {
							delimeter.add(separator);
						}
					}
					model.addObject("SeparatorEnum",delimeter ); 
					model.addObject(BaseConstants.TRIM_POSITION, java.util.Arrays.asList(TrimPositionEnum.values()));
					model.addObject(BaseConstants.READ_ONLY_FLAG, false);
					model.addObject(BaseConstants.HEADERFOOTERTYPEENUM, java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
					model.addObject(BaseConstants.TRUEFALSEENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
					model.addObject(BaseConstants.VALIDATION_FAIL,true);
					model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECT_DEVICE_NAME));
					
					model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECT_MAPPING_NAME) );
					model.addObject(BaseConstants.DATADEFINITIONFILEENUM, parserService.getDataDefinitionFileList(serverInstanceId));
					getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
					
					model.addObject(BaseConstants.SELECTED_DEVICE_ID, requestParamMap.get(BaseConstants.SELECTED_DEVICE_ID));
					model.addObject(BaseConstants.SELECTED_MAPPING_ID,    requestParamMap.get(BaseConstants.SELECTED_MAPPING_ID));
					model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID, requestParamMap.get(BaseConstants.SELECTED_VENDOR_TYPE_ID));
					model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID, requestParamMap.get(BaseConstants.SELECTED_DEVICE_TYPE_ID) );
					addCommonParamToModel(requestParamMap,model);
					
				}else{
					logger.debug("Validation done successfully , going to update and associate parser mapping ");
					updateAndAssociateParserMapping(varLengthAsciiParserMapping,requestParamMap,varLengthAsciiParserMappingId,model,staffId);
				
				}
				
		}
		return model;

	}
	
	/**
	 * Update and associate parser mapping
	 * @param varLengthAsciiParserMapping
	 * @param requestParamMap
	 * @param varLengthAsciiParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(VarLengthAsciiParserMapping varLengthAsciiParserMapping,Map<String,String> requestParamMap,int varLengthAsciiParserMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(varLengthAsciiParserMapping, requestParamMap.get(BaseConstants.PLUGIN_TYPE), Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)), requestParamMap.get(BaseConstants.ACTION_TYPE), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_ATTRIBUTE);
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			
			responseObject=varLengthAsciiParserService.getVarLengthAsciiParserMappingById(varLengthAsciiParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				VarLengthAsciiParserMapping varLengthAsciiParser=(VarLengthAsciiParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN,varLengthAsciiParser);
		
			}else{
				model.addObject(FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN,(VarLengthAsciiParserMapping) SpringApplicationContext.getBean(VarLengthAsciiParserMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
	
			model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECT_DEVICE_NAME));
			model.addObject(BaseConstants.PARSER_MAPPING_ID, requestParamMap.get(BaseConstants.ID));
			model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECT_MAPPING_NAME) );
			model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject(BaseConstants.TRIM_POSITION,java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject(BaseConstants.SOURCE_FIELD_FORMAT,java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
		}else{
			model = new ModelAndView(ViewNameConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	/**
	 * Update Var Length Ascii Parser basic detail from attribute tab
	 * @param varLengthAsciiParserMapping
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_VAR_LENGTH_ASCII_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateVarLengthAsciiParserMapping(
			@ModelAttribute(value=FormBeanConstants.VAR_LENGTH_ASCII_PARSER_MAPPING_FORM_BEAN) VarLengthAsciiParserMapping varLengthAsciiParserMapping,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		parserMappingValidator.validateSrcDateFormat(varLengthAsciiParserMapping,result,null,false, null);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
			varLengthAsciiParserMapping.setLastUpdatedDate(new Date());
			varLengthAsciiParserMapping.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject=varLengthAsciiParserService.updateVarLengthAsciiParserMapping(varLengthAsciiParserMapping);
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPLOAD_DATA_DEFINITION_FILE, method = RequestMethod.POST)
	@ResponseBody public String uploadDataDefinitionFile(@RequestParam(value = "parserMappingId", required = true) String mappingId,
			@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) throws SMException {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if (!file.isEmpty() ){
			if(BaseConstants.IMPORT_FILE_CONTENT_TYPE.equals(file.getContentType()) ) { // will check file type content type is XML and empty
				int staffId =  eliteUtils.getLoggedInStaffId(request);
				ResponseObject responseObject = varLengthAsciiParserService.uploadDataDefinitionFile(file, Integer.parseInt(mappingId), staffId, Integer.parseInt(serverInstanceId));
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}else{
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage("var.length.ascii.parser.mapping.wrong.file.select"));
			}
		}else{
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("var.length.ascii.plugin.data.definition.no.file.select"));
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.GET_DATA_DEFINITION_FILE_NAME,method=RequestMethod.POST)
	@ResponseBody public  String getDataDefinitionFileName(@RequestParam(value = "mappingId", required = true) String mappingId) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		
		if(mappingId!=null){
			try{
				String dataFileName =varLengthAsciiParserService.getDataDefinitionFileNameById(Integer.parseInt(mappingId));
				responseObject.setSuccess(true);
				responseObject.setObject(dataFileName);
			}catch(Exception e) {//NOSONAR
				responseObject.setSuccess(false);
			}
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
		
	}

}
