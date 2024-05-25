package com.elitecore.sm.parser.controller;

import java.io.File;
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

import com.elitecore.core.util.Constant;
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
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.VarLengthBinaryParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;

@Controller
public class VarLengthBinaryParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	VarLengthBinaryParserService varLengthBinaryParserService;
	
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
	 * Redirect to var length binary parser configuration page 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_VAR_LENGTH_BINARY_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initVarLengthBinaryParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		int serverInstanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));

		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN, (VarLengthBinaryParserMapping) SpringApplicationContext.getBean(VarLengthBinaryParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject(BaseConstants.TRUEFALSEENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.DATADEFINITIONFILEENUM, parserService.getDataDefinitionFileList(serverInstanceId));
		return model;
	}
	
	/**
	 * Redirect to var binary parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initVarLengthBinaryParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION);
		int varLBParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			varLBParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=varLengthBinaryParserService.getVarLengthBinaryParserMappingById(varLBParserMappingId); 
			if(responseObject.isSuccess()){
				
				VarLengthBinaryParserMapping vLengthBinaryParserMapping=(VarLengthBinaryParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,vLengthBinaryParserMapping);
				
			}else{
				model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,(VarLengthBinaryParserMapping) SpringApplicationContext.getBean(VarLengthBinaryParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		if(StringUtils.isNotEmpty(requestParamMap.get(BaseConstants.ASN1_PARSER_ATTRIBUTE_TYPE))){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestParamMap.get(BaseConstants.ASN1_PARSER_ATTRIBUTE_TYPE));
		}else{
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE);
		}
		//model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE);
		model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject(BaseConstants.TRIM_POSITION,java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,BaseConstants.TRUE);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get(BaseConstants.DEVICE_ID) );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.PARSER_MAPPING_NAME));
		model.addObject(BaseConstants.SOURCE_FIELD_FORMAT,java.util.Arrays.asList(SourceFieldFormatEnum.values()));
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
	 * Update and Associate Var Length Binary Parser Mapping with parser
	 * @param varLengthBinaryParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_VAR_LENGTH_BINARY_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateVarLengthBinaryParserConfiguration(
			@ModelAttribute (value=FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN)  VarLengthBinaryParserMapping  varLengthBinaryParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION); 
		String requestAction = BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE;
		String parserMappingId=requestParamMap.get(BaseConstants.ID);
		if(varLengthBinaryParserMapping.getDataDefinitionPath()!=null && !varLengthBinaryParserMapping.getDataDefinitionPath().isEmpty()){
			int instanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));
			ServerInstance serverInstance = serverInstanceService.getServerInstance(instanceId);
			String crestelPEnginePath = BaseConstants.CRESTEL_P_ENGINE_HOME_PATH;
			if(serverInstance.getServerHome()!=null && !Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
				crestelPEnginePath = serverInstance.getServerHome();
			}
			varLengthBinaryParserMapping.setDataDefinitionPath(crestelPEnginePath + File.separator + Constant.MODULES + File.separator + Constant.MEDIATION + varLengthBinaryParserMapping.getDataDefinitionPath());
		}
		int varLengthBinaryParserMappingId=0;
		if(parserMappingId !=null){
			varLengthBinaryParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if(BaseConstants.TRUE.equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("Var Length Binary Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(varLengthBinaryParserMapping,requestParamMap,varLengthBinaryParserMappingId,model,staffId,requestAction);
			
		}else{
				logger.debug("Var Length Binary Parser Configuration is updated , first update and then associate configuration with parser");
				
				String nextAttributeType = requestParamMap.get("nextAttributeType");
				if(nextAttributeType != null && nextAttributeType.equals(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE) ){
					requestAction = requestParamMap.get("nextAttributeType");
				}
				
				if(result.hasErrors()){
					int serverInstanceId = Integer.parseInt(requestParamMap.get(BaseConstants.INSTANCE_ID));
					model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN, varLengthBinaryParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_ASCII_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.TRIM_POSITION, java.util.Arrays.asList(TrimPositionEnum.values()));
					model.addObject(BaseConstants.READ_ONLY_FLAG, false);
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
					updateAndAssociateParserMapping(varLengthBinaryParserMapping,requestParamMap,varLengthBinaryParserMappingId,model,staffId,requestAction);
				
				}
				
		}
		return model;

	}
	
	/**
	 * Update and associate parser mapping
	 * @param varLengthBinaryParserMapping
	 * @param requestParamMap
	 * @param varLengthBinaryParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(VarLengthBinaryParserMapping varLengthBinaryParserMapping,Map<String,String> requestParamMap,int varLengthBinaryParserMappingId,ModelAndView model,int staffId, String requestAction){
		
		boolean bUpdate = Boolean.TRUE; 
		if(requestAction.equals(BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE)){
			ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(varLengthBinaryParserMapping, requestParamMap.get(BaseConstants.PLUGIN_TYPE), Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)), requestParamMap.get(BaseConstants.ACTION_TYPE), staffId);
			if(responseObject == null || !responseObject.isSuccess()){
				bUpdate = Boolean.FALSE;
			}
		}
		
		if(bUpdate){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestAction);
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			
			ResponseObject responseObject=varLengthBinaryParserService.getVarLengthBinaryParserMappingById(varLengthBinaryParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				VarLengthBinaryParserMapping varLengthBinaryParser=(VarLengthBinaryParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,varLengthBinaryParser);
		
			}else{
				model.addObject(FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,(VarLengthBinaryParserMapping) SpringApplicationContext.getBean(VarLengthBinaryParserMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
			model.addObject(BaseConstants.SELECTED_DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
			model.addObject(BaseConstants.ID, requestParamMap.get(BaseConstants.ID));
			model.addObject(BaseConstants.SELECTED_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME) );
			model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECT_DEVICE_NAME));
			model.addObject(BaseConstants.PARSER_MAPPING_ID, requestParamMap.get(BaseConstants.ID));
			model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECT_MAPPING_NAME) );
			model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject(BaseConstants.TRIM_POSITION,java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject(BaseConstants.SOURCE_FIELD_FORMAT,java.util.Arrays.asList(SourceFieldFormatEnum.values()));
		}else{
			model = new ModelAndView(ViewNameConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VAR_LENGTH_BINARY_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	/**
	 * Update Var Length Binary Parser basic detail from attribute tab
	 * @param varLengthBinaryParserMapping
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_VAR_LENGTH_BINARY_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateVarLengthBinaryParserMapping(
			@ModelAttribute(value=FormBeanConstants.VAR_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN) VarLengthBinaryParserMapping varLengthBinaryParserMapping,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
			varLengthBinaryParserMapping.setLastUpdatedDate(new Date());
			varLengthBinaryParserMapping.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject=varLengthBinaryParserService.updateVarLengthBinaryParserMapping(varLengthBinaryParserMapping);
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
		
	}
	
	/*@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
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
			}catch(Exception e) {
				responseObject.setSuccess(false);
			}
			ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
		
	}*/

}
