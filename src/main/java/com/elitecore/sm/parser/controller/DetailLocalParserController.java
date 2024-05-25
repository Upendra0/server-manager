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
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.FileHeaderFooterTypeEnum;
import com.elitecore.sm.parser.model.FileTypeEnum;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatASCIIEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.AsciiParserService;
import com.elitecore.sm.parser.service.DetailLocalParserService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author saumil.vachheta
 * @date 10-FEB-2022
 *
 */
@Controller
public class DetailLocalParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	DetailLocalParserService detailLocalParserService;
	
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
	 * Redirect to detail local parser configuration page 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_DETAIL_LOCAL_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initDetailLocalParserConfig(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DETAIL_LOCAL_PARSER_CONFIGURATION);
		
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		
		addCommonParamToModel(requestParamMap,model);
		
		model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN, (DetailLocalParserMapping) SpringApplicationContext.getBean(DetailLocalParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DETAIL_LOCAL_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject("fileTypeEnum", java.util.Arrays.asList(FileTypeEnum.values()));
		model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
		model.addObject("headerFooterTypeEnum", java.util.Arrays.asList(FileHeaderFooterTypeEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Redirect to detail local parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_DETAIL_LOCAL_PARSER_ATTRIBUTE, method = RequestMethod.POST)
	public ModelAndView initDetailLocalParserAttribute(@RequestParam Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DETAIL_LOCAL_PARSER_CONFIGURATION);
		int detailLocalParserMappingId=0;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			
			detailLocalParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=detailLocalParserService.getDetailLocalParserMappingById(detailLocalParserMappingId); 
			if(responseObject.isSuccess()){
				
				DetailLocalParserMapping detailLocalParser=(DetailLocalParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN,detailLocalParser);
				
			}else{
				model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN,(DetailLocalParserMapping) SpringApplicationContext.getBean(DetailLocalParserMapping.class));
			}
		
		}
		
		addCommonParamToModel(requestParamMap,model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DETAIL_LOCAL_PARSER_ATTRIBUTE);
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
	 * Update and Associate Detail Local Parser Mapping with parser
	 * @param regExParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_DETAIL_LOCAL_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateassociateDetailLocalParserMapping(
			@ModelAttribute (value=FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN)  DetailLocalParserMapping  detailLocalParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.DETAIL_LOCAL_PARSER_CONFIGURATION); 
		
		String parserMappingId=requestParamMap.get("id");
		//Below code commented as fix of Jira MEDSUP-934
		// detailLocalParserMapping.setFind(requestParamMap.get("find")/*.replace(" ", "[s]")*/);
		// detailLocalParserMapping.setReplace(requestParamMap.get("replace")/*.replace(" ","[s]")*/);
		
		int detailLocalParserMappingId=0;
		if(parserMappingId !=null){
			detailLocalParserMappingId=Integer.parseInt(parserMappingId);
		}
		int staffId=eliteUtils.getLoggedInStaffId(request);
		
		if("true".equals(requestParamMap.get("readOnlyFlag"))){
				logger.debug("Detail Local Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
				updateAndAssociateParserMapping(detailLocalParserMapping,requestParamMap,detailLocalParserMappingId,model,staffId);
			
		}else{
				logger.debug("Detail Local Parser Configuration is updated , first update and then associate configuration with parser");
				
				parserMappingValidator.validateParserMappingParameter(detailLocalParserMapping, result, null, false,null);
				
				if(result.hasErrors()){
					
					model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN, detailLocalParserMapping);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DETAIL_LOCAL_PARSER_CONFIGURATION);
					model.addObject("fileTypeEnum", java.util.Arrays.asList(FileTypeEnum.values()));
					model.addObject("SeparatorEnum", java.util.Arrays.asList(SeparatorEnum.values()));
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
					updateAndAssociateParserMapping(detailLocalParserMapping,requestParamMap,detailLocalParserMappingId,model,staffId);
				
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
	 * Update detailLocal parser basic detail from attribute tab
	 * @param detailLocalParser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_DETAIL_LOCAL_PARSER_MAPPING,method=RequestMethod.POST)
	@ResponseBody public  String updateDetailLocalParserBasicDetail(
			@ModelAttribute(value=FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN) DetailLocalParserMapping detailLocalParser,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) {
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject  responseObject ;
	
		parserMappingValidator.validateSrcDateFormat(detailLocalParser,result,null,false, null);
		
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{		
					detailLocalParser.setLastUpdatedDate(new Date());
					detailLocalParser.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					responseObject=detailLocalParserService.updateDetailLocalParserMapping(detailLocalParser);
					ajaxResponse=	eliteUtils.convertToAjaxResponse(responseObject);
			}
		
		return ajaxResponse.toString();
		
	}
	
	/**
	 * Update and associate parser mapping
	 * @param detailLocalParserMapping
	 * @param requestParamMap
	 * @param detailLocalParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(DetailLocalParserMapping detailLocalParserMapping,Map<String,String> requestParamMap,int detailLocalParserMappingId,ModelAndView model,int staffId){
		
		ResponseObject responseObject =  parserMappingService.updateAndAssociateParserMapping(detailLocalParserMapping, requestParamMap.get("plugInType"), Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		
		if(responseObject!=null && responseObject.isSuccess()){
			model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DETAIL_LOCAL_PARSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			
			responseObject=detailLocalParserService.getDetailLocalParserMappingById(detailLocalParserMappingId);
		 
			if(responseObject.isSuccess()){
				
				DetailLocalParserMapping detailLocalParser=(DetailLocalParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN,detailLocalParser);
		
			}else{
				model.addObject(FormBeanConstants.DETAIL_LOCAL_PARSER_MAPPING_FORM_BEAN,(DetailLocalParserMapping) SpringApplicationContext.getBean(DetailLocalParserMapping.class));
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
			model = new ModelAndView(ViewNameConstants.DETAIL_LOCAL_PARSER_CONFIGURATION); 
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DETAIL_LOCAL_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
		}
	}

}
