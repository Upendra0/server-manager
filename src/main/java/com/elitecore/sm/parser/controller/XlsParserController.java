package com.elitecore.sm.parser.controller;

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
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.XlsParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class XlsParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	XlsParserService xlsParserService;
	
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
	 * Redirect to xls parser configuration page 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_XLS_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initXlsParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XLS_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN, (XlsParserMapping) SpringApplicationContext.getBean(XlsParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XLS_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to xls parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_XLS_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initXlsParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XLS_PARSER_CONFIGURATION);
		int xlsParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			xlsParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=xlsParserService.getXlsParserMappingById(xlsParserMappingId); 
			if(responseObject.isSuccess()){				
				XlsParserMapping xlsParser=(XlsParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,xlsParser);
				
			}else{
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,(XlsParserMapping) SpringApplicationContext.getBean(XlsParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XLS_PARSER_ATTRIBUTE);
		model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,BaseConstants.TRUE);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get(BaseConstants.DEVICE_ID) );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.PARSER_MAPPING_NAME));
		return model;
	}
	
	/**
	 * Update and Associate Xls Parser Mapping with parser
	 * @param regExParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_XLS_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateXlsParserMapping(
			@ModelAttribute (value=FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN)  XlsParserMapping  xlsParserMapping, //NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.XLS_PARSER_CONFIGURATION); 
		
		String parserMappingId=requestParamMap.get(BaseConstants.ID);
		//Below code commented as fix of Jira MEDSUP-934
		
		int xlsParserMappingId=0;
		if(parserMappingId !=null){
			xlsParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if(BaseConstants.TRUE.equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
				logger.debug("Xls Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(xlsParserMapping,requestParamMap,xlsParserMappingId,model,staffId);
			
		}else{
				logger.debug("Xls Parser Configuration is updated , first update and then associate configuration with parser");
				
				parserMappingValidator.validateParserMappingParameter(xlsParserMapping, result, null, false,null);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN, xlsParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XLS_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.READ_ONLY_FLAG, false);
					model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
					model.addObject(BaseConstants.TRIM_POSITION, java.util.Arrays.asList(TrimPositionEnum.values()));
					model.addObject(BaseConstants.VALIDATION_FAIL,true);
					model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECT_DEVICE_NAME));
					
					model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECT_MAPPING_NAME) );
				
					getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
					
					model.addObject(BaseConstants.SEL_DEVICE_ID, requestParamMap.get(BaseConstants.SEL_DEVICE_ID));
					model.addObject(BaseConstants.SEL_MAPPING_ID,    requestParamMap.get(BaseConstants.SEL_MAPPING_ID));
					model.addObject(BaseConstants.SEL_VENDOR_TYPE_ID, requestParamMap.get(BaseConstants.SEL_VENDOR_TYPE_ID));
					model.addObject(BaseConstants.SEL_DEVICE_TYPE_ID, requestParamMap.get(BaseConstants.SEL_DEVICE_TYPE_ID) );
					addCommonParamToModel(requestParamMap,model);
					
				}else{
					logger.debug("Validation done successfully , going to update and associate parser mapping ");
					updateAndAssociateParserMapping(xlsParserMapping,requestParamMap,xlsParserMappingId,model,staffId);
				
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
		
		model.addObject(BaseConstants.SEL_DEVICE_ID, deviceId);
		model.addObject(BaseConstants.SEL_MAPPING_ID,   mappingId);
		model.addObject(BaseConstants.SEL_VENDOR_TYPE_ID, vendorTypeId);
		model.addObject(BaseConstants.SEL_DEVICE_TYPE_ID, deviceTypeId );
		model.addObject(BaseConstants.DEVICE_ID,deviceId );
		
		return mappingId;
}
	
	/**
	 * Update xls parser basic detail from attribute tab
	 * @param xlsParser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_XLS_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateXlsParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN) XlsParserMapping xlsParser, //NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		parserMappingValidator.validateSrcDateFormat(xlsParser,result,null,false, null);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
					xlsParser.setLastUpdatedDate(new Date());
					xlsParser.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					responseObject=xlsParserService.updateXlsParserMapping(xlsParser);
					ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
			}
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Update and associate parser mapping
	 * @param xlsParserMapping
	 * @param requestParamMap
	 * @param xlsParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(XlsParserMapping xlsParserMapping,Map<String,String> requestParamMap,int xlsParserMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(xlsParserMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XLS_PARSER_ATTRIBUTE);
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			
			responseObject=xlsParserService.getXlsParserMappingById(xlsParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				XlsParserMapping xlsParser=(XlsParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,xlsParser);
		
			}else{
				model.addObject(FormBeanConstants.XLS_PARSER_MAPPING_FORM_BEAN,(XlsParserMapping) SpringApplicationContext.getBean(XlsParserMapping.class));
			}
			
			getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap,model);
			
			model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECT_DEVICE_NAME));
			model.addObject(BaseConstants.PARSER_MAPPING_ID, requestParamMap.get(BaseConstants.ID));
			model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.SELECT_MAPPING_NAME) );
			model.addObject(BaseConstants.SOURCE_CHARSET_NAME,java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject(BaseConstants.UNIFIED_FIELD,java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject(BaseConstants.SOURCE_DATE_FORMAT_ENUM,java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.TRIM_POSITION, java.util.Arrays.asList(TrimPositionEnum.values()));
			
		}else{
			model = new ModelAndView(ViewNameConstants.XLS_PARSER_CONFIGURATION); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.XLS_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}
	
	
	
}
