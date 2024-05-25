/**
 * 
 */
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.NatflowASN1ParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatASCIIEnum;



/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class NATFlowParserController extends BaseController{

	
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private ParserService parserService;
	
	@Autowired
	private DeviceTypeService deviceTypeService;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired 
	private ParserMappingValidator parserValidator;
	
	@Autowired
	private ParserMappingService parserMappingService;
	
	
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
	 * Method will redirect user to netflow parser configuration page.
	 * @param serviceId
	 * @param request
	 * @return ModelView Object
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_NATFLOW_PARSER_MANAGER,method= RequestMethod.GET)
	public ModelAndView initNatflowParser(@RequestParam(required=true) Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NATFLOW_PARSER_CONFIGURATION);
		
		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)));
		int deviceId = -1 ;
		int mappingId = -1; 
		int vendorTypeId = -1;
		int deviceTypeId = -1;
		
		
		if(parser != null){
			mappingId = parser.getParserMapping().getId();
			deviceId =  parser.getParserMapping().getDevice().getId();
			vendorTypeId =  parser.getParserMapping().getDevice().getVendorType().getId();
			deviceTypeId =  parser.getParserMapping().getDevice().getDeviceType().getId();
			model.addObject("deviceName", parser.getParserMapping().getDevice().getName());
		}
		
		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM); 
		//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
		
		if(responseObject.isSuccess()){
			model.addObject(BaseConstants.DEVICE_TYPE_LIST,(List<DeviceType>) responseObject.getObject());
		}else{
			model.addObject(BaseConstants.DEVICE_TYPE_LIST,getMessage("device.type.not.found"));
		}
		
		model.addObject(FormBeanConstants.NATFLOW_PARSER_MAPPING_FORM_BEAN, (NATFlowParserMapping) SpringApplicationContext.getBean(NATFlowParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_CONFIGURATION);
		
		
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.INSTANCE_ID, requestParamMap.get(BaseConstants.INSTANCE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE) );
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
			
		return model;		
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_NETFLOW_PARSER_ATTRIBUTE,method= RequestMethod.POST)
	public ModelAndView initNetflowParserAttribute(@RequestParam(required=true) Map<String,String> requestParamMap) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NATFLOW_PARSER_CONFIGURATION);
		Service serviceObj = servicesService.getServiceandServerinstance(Integer.parseInt(requestParamMap.get(BaseConstants.SERVICE_ID)));
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_ATTRIBUTE);
		
		model.addObject("mappingId", requestParamMap.get("selAttributeMappingId"));
		model.addObject("mappingName", requestParamMap.get("selAttributeMappingName"));
		model.addObject("deviceName", requestParamMap.get("selDeviceName"));
		model.addObject(BaseConstants.READ_ONLY_FLAG , "true");		
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, serviceObj.getSvctype().getAlias());
		model.addObject("serverInstanceId", serviceObj.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE) );
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("sourceDateFormatEnum", java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("sourceFieldFormat", java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
		
		return model;		
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_NATFLOW_ASN_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateNatflowASNParserConfiguration(
			@ModelAttribute (value="natflow_parser_mapping_form_bean")  NatflowASN1ParserMapping  natFlowASNParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		
		return updateNatflowConfiguration(natFlowASNParserMapping, requestParamMap, result, request);
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_NATFLOW_PARSER_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateNatflowParserConfiguration(
			@ModelAttribute (value="natflow_parser_mapping_form_bean")  NATFlowParserMapping  natFlowParserMapping,//NOSONAR
			@RequestParam(required=true) Map<String,String> requestParamMap,
			BindingResult result,
			HttpServletRequest request) {
		return updateNatflowConfiguration(natFlowParserMapping, requestParamMap, result, request);
	}
	
	
	
	
	
	/**
	 * Method will update natflow and natflow asn configuration details.
	 * @param parserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	private ModelAndView updateNatflowConfiguration(ParserMapping parserMapping,Map<String,String> requestParamMap, BindingResult result, HttpServletRequest request){
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NATFLOW_PARSER_CONFIGURATION); 
		model.addObject(FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN, (ParserAttribute) SpringApplicationContext.getBean("parserAttribute"));
		if("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))){
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_ATTRIBUTE);
			
		}else{
			parserValidator.validateParserMappingParameter(parserMapping, result, null, false,null);      
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.NATFLOW_PARSER_MAPPING_FORM_BEAN, parserMapping);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_CONFIGURATION);
				model.addObject("isError", true);
			}else{
				int staffId = eliteUtils.getLoggedInStaffId(request);
				ResponseObject responseObject ;
				if(requestParamMap.get(BaseConstants.PLUGIN_TYPE).equals(EngineConstants.NATFLOW_ASN_PARSING_PLUGIN)){
					 NatflowASN1ParserMapping asn1ParserMapping = (NatflowASN1ParserMapping) parserMapping;
					 responseObject =  parserMappingService.updateAndAssociateParserMapping(asn1ParserMapping, requestParamMap.get(BaseConstants.PLUGIN_TYPE), Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)), requestParamMap.get("actionType"), staffId);
				}else{
					 NATFlowParserMapping  natFlowParserMapping = (NATFlowParserMapping) parserMapping;
					 responseObject =  parserMappingService.updateAndAssociateParserMapping(natFlowParserMapping, requestParamMap.get(BaseConstants.PLUGIN_TYPE), Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)), requestParamMap.get("actionType"), staffId);
				}
				
				
				if(responseObject.isSuccess()){
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage("natflow.parser.config.update.success"));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_ATTRIBUTE);
				}else{
					model = new ModelAndView(ViewNameConstants.NATFLOW_PARSER_CONFIGURATION); 
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_PARSER_CONFIGURATION);
					model.addObject(BaseConstants.ERROR_MSG,getMessage("natflow.parser.config.update.fail"));
				}
		   }
			ResponseObject tempResponseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM); 
			//ResponseObject responseObject = deviceTypeService.getAllDeviceType();
				
				if(tempResponseObject.isSuccess()){
					model.addObject(BaseConstants.DEVICE_TYPE_LIST,(List<DeviceType>) tempResponseObject.getObject());
				}else{
					model.addObject(BaseConstants.DEVICE_TYPE_LIST,getMessage("device.type.not.found"));
				}
			model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));
		}	
		
		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(requestParamMap.get(BaseConstants.PLUGIN_ID)));
		int deviceId = -1; 
		int mappingId = -1 ; 
		int vendorTypeId = -1;
		int deviceTypeId = -1;	
		
		if(parser != null){
			mappingId = parser.getParserMapping().getId();
			deviceId =  parser.getParserMapping().getDevice().getId();
			vendorTypeId =  parser.getParserMapping().getDevice().getVendorType().getId();
			deviceTypeId =  parser.getParserMapping().getDevice().getDeviceType().getId(); 
		}
		
		model.addObject("unifiedField", java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.INSTANCE_ID, requestParamMap.get(BaseConstants.INSTANCE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE) );	
		
		model.addObject("mappingId", requestParamMap.get("id"));
		model.addObject("mappingName", requestParamMap.get("selecteMappingName"));
		model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
		
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId",   mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId );
		model.addObject("selectedMappingType", requestParamMap.get("selectedMappingType") );
		model.addObject("sourceDateFormatEnum", java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("sourceFieldFormat", java.util.Arrays.asList(SourceFieldFormatASCIIEnum.values()));
		
		model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
	
	return model;
		
		
		
		
	}
	
	
}
