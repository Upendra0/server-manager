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
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.SourceFieldFormatEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.FixedLengthBinaryParserService;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Controller
public class FixedLengthBinaryParserController extends BaseController {
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	DeviceTypeService deviceTypeService;
	
	/** The device service. */
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	FixedLengthBinaryParserService fixedLengthBinaryParserService;
	
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
	 * Redirect to Binary parser configuration page
	 * 
	 * @param requestParamMap
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_FIXED_LENGTH_BINARY_PARSER_CONFIG, method = RequestMethod.GET)
	public ModelAndView initFixedLengthBinaryParserConfig(@RequestParam Map<String, String> requestParamMap) {

		ModelAndView model = new ModelAndView(ViewNameConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
		int mappingId = getDeviceAndVendorTypeDetail(model, requestParamMap.get(BaseConstants.PLUGIN_ID));
		addCommonParamToModel(requestParamMap, model);
		model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,
				(FixedLengthBinaryParserMapping) SpringApplicationContext
						.getBean(FixedLengthBinaryParserMapping.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
        model.addObject(BaseConstants.PARSER_MAPPING_ID,mappingId);
        model.addObject("trueFalseEnum",java.util.Arrays.asList(TrueFalseEnum.values()));
		return model;
	}
	
	/**
	 * Get Device and vendor type detail
	 * 
	 * @param model
	 * @param pluginId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getDeviceAndVendorTypeDetail(ModelAndView model, String pluginId) {

		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM);
		//ResponseObject responseObject = deviceTypeService.getAllDeviceType();

		if (responseObject.isSuccess()) {
			model.addObject("deviceTypeList", (List<DeviceType>) responseObject.getObject());
		} else {
			model.addObject("deviceTypeList", getMessage("device.type.not.found"));
		}

		Parser parser = parserService.getParserMappingDetailsByParserId(Integer.parseInt(pluginId));
		int deviceId = -1;
		int mappingId = -1;
		int vendorTypeId = -1;
		int deviceTypeId = -1;

		if (parser != null) {
			mappingId = parser.getParserMapping().getId();
			deviceId = parser.getParserMapping().getDevice().getId();
			vendorTypeId = parser.getParserMapping().getDevice().getVendorType().getId();
			deviceTypeId = parser.getParserMapping().getDevice().getDeviceType().getId();
		}
		model.addObject("selDeviceId", deviceId);
		model.addObject("selMappingId", mappingId);
		model.addObject("selVendorTypeId", vendorTypeId);
		model.addObject("selDeviceTypeId", deviceTypeId);
		model.addObject("deviceId", deviceId);
		return mappingId;
	}
	/**
	 * Add Commom paramter to model
	 * 
	 * @param requestParamMap
	 * @param model
	 */
	public void addCommonParamToModel(Map<String, String> requestParamMap, ModelAndView model) {

		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.SERVER_INSTANCE_ID, requestParamMap.get(BaseConstants.SERVER_INSTANCE_ID));
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE, requestParamMap.get(BaseConstants.PLUGIN_TYPE));
	}
	
	/**
	 * Redirect to fixed Length Binary parser attribute page
	 * @param requestParamMap
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.INIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE,method=RequestMethod.POST)
	public ModelAndView initFixedLengthBinaryParserAttribute(@RequestParam Map<String,String> requestParamMap){
		ModelAndView model =new ModelAndView(ViewNameConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
		int fixedLengthBinaryParserMappingId;
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID)!=null){
			fixedLengthBinaryParserMappingId=Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			
			ResponseObject responseObject=fixedLengthBinaryParserService.getFixedLengthBinaryParserMappingById(fixedLengthBinaryParserMappingId);
			if(responseObject.isSuccess()){
				FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping=(FixedLengthBinaryParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,fixedLengthBinaryParserMapping);
			}else{
				model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN, (FixedLengthBinaryParserMapping)SpringApplicationContext.getBean(FixedLengthBinaryParserMapping.class));
			}
		}
		addCommonParamToModel(requestParamMap, model);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE);
		model.addObject("sourceCharsetName", java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField", java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("sourceFieldFormat", java.util.Arrays.asList(SourceFieldFormatEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject("sourceDateFormatEnum", java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject("readOnlyFlag", "true");
		model.addObject("mappingId", requestParamMap.get("mappingId"));
		model.addObject("deviceName", requestParamMap.get("deviceName"));
		model.addObject("recordLength", requestParamMap.get("recordLength"));
		model.addObject("deviceId", requestParamMap.get("deviceId"));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("mappingName", requestParamMap.get("mappingName"));

		return model;
	}

	/**
	 * Update and Associate Fixed Length Binary Parser Mapping with parser
	 * 
	 * @param regExParserMapping
	 * @param requestParamMap
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_FIXED_LENGTH_BINARY_PARSER_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView updateassociateFixedLengthBinaryParserMapping(
			@ModelAttribute(value = FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN) FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping,//NOSONAR
			@RequestParam(required = true) Map<String, String> requestParamMap, BindingResult result,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
		String parserMappingId = requestParamMap.get("id");
		int fixedLengthBinaryParserId = 0;
		if (parserMappingId != null) {
			fixedLengthBinaryParserId = Integer.parseInt(parserMappingId);
		}
		int staffId = eliteUtils.getLoggedInStaffId(request);
		if ("true".equals(requestParamMap.get("readOnlyFlag"))) {
			logger.debug(
					"Fixed Length Binary Parser Configuration is not updated ,  can not call validate just redirect to attribute page in disable mode");
			updateAndAssociateParserMapping(fixedLengthBinaryParserMapping, requestParamMap, fixedLengthBinaryParserId,
					model, staffId);
		} else {
			logger.debug(
					"Fixed Length Binary Parser Configuration is updated , first update and then associate configuration with parser");
			parserMappingValidator.validateParserMappingParameter(fixedLengthBinaryParserMapping, result, null, false,
					null);

			if (result.hasErrors()) {

				model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,
						fixedLengthBinaryParserMapping);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,
						BaseConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
				model.addObject("readOnlyFlag", false);
				model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
				model.addObject("isValidationFail", true);
				model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));

				model.addObject("mappingName", requestParamMap.get("selecteMappingName"));
				model.addObject("recordLength", fixedLengthBinaryParserMapping.getRecordLength());
				getDeviceAndVendorTypeDetail(model, requestParamMap.get(BaseConstants.PLUGIN_ID));

				model.addObject("selDeviceId", requestParamMap.get("selDeviceId"));
				model.addObject("selMappingId", requestParamMap.get("selMappingId"));
				model.addObject("selVendorTypeId", requestParamMap.get("selVendorTypeId"));
				model.addObject("selDeviceTypeId", requestParamMap.get("selDeviceTypeId"));
				addCommonParamToModel(requestParamMap, model);
			} else {
				logger.debug("Validation done successfully , going to update and associate parser mapping ");
				updateAndAssociateParserMapping(fixedLengthBinaryParserMapping, requestParamMap,
						fixedLengthBinaryParserId, model, staffId);
			}
		}
		return model;
	}
	/**
	 * Update and associate parser mapping
	 * @param fixedLengthBinaryParserMapping
	 * @param requestParamMap
	 * @param BinaryParserMappingId
	 * @param model
	 * @param staffId
	 */
	private void updateAndAssociateParserMapping(FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping,
			Map<String, String> requestParamMap, int fixedLengthBinaryParserMappingId, ModelAndView model,
			int staffId) {

		ResponseObject responseObject = parserMappingService.updateAndAssociateParserMapping(
				fixedLengthBinaryParserMapping, requestParamMap.get("plugInType"),
				Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
		if (responseObject.isSuccess() && responseObject != null) {
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE);
			model.addObject("readOnlyFlag", requestParamMap.get("readOnlyFlag"));
			responseObject = fixedLengthBinaryParserService
					.getFixedLengthBinaryParserMappingById(fixedLengthBinaryParserMappingId);
			if (responseObject.isSuccess()) {
				fixedLengthBinaryParserMapping = (FixedLengthBinaryParserMapping) responseObject
						.getObject();
				model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,
						fixedLengthBinaryParserMapping);
			} else {
				model.addObject(FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN,
						(FixedLengthBinaryParserMapping) SpringApplicationContext
								.getBean(FixedLengthBinaryParserMapping.class));
			}
			getDeviceAndVendorTypeDetail(model, requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap, model);
			model.addObject("deviceName", requestParamMap.get("selecteDeviceName"));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject("mappingName", requestParamMap.get("selecteMappingName"));
			model.addObject("recordLength", fixedLengthBinaryParserMapping.getRecordLength());
			model.addObject("sourceCharsetName", java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField", java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
			model.addObject("sourceDateFormatEnum", java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject("sourceFieldFormat", java.util.Arrays.asList(SourceFieldFormatEnum.values()));
			
		}else {
			model = new ModelAndView(ViewNameConstants.FIXED_LENGTH_BINARY_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.BINARY_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG, getMessage("natflow.parser.config.update.fail"));
		}

	}
	/**
	 * Update Binary parser basic detail from attribute tab
	 * 
	 * @param BinaryParser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_FIXED_LENGTH_BINARY_PARSER_MAPPING, method = RequestMethod.POST)
	@ResponseBody
	public String updateFixedBinaryParserBasicDetail(
			@ModelAttribute(value = FormBeanConstants.FIXED_LENGTH_BINARY_PARSER_MAPPING_FORM_BEAN) FixedLengthBinaryParserMapping fixedLengthBinaryParser,//NOSONAR
			BindingResult result, HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserMappingValidator.validateSrcDateFormat(fixedLengthBinaryParser, result, null, false, null);
		parserMappingValidator.validateFixedLengthBinaryMappingParamater(fixedLengthBinaryParser, result, null, false, null);
		if(result.hasErrors()){
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
		}else{
			fixedLengthBinaryParser.setLastUpdatedDate(new Date());
			fixedLengthBinaryParser.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject=fixedLengthBinaryParserService.updateFixedLengthBinaryParser(fixedLengthBinaryParser);
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
			
		}
		return ajaxResponse.toString();
	}
	
}
