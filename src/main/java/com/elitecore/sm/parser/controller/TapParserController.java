package com.elitecore.sm.parser.controller;

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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.ASN1DecodeTypeEnum;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.common.model.SourceFieldDataFormatEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.service.TapParserService;
import com.elitecore.sm.parser.validator.ParserMappingValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Controller
public class TapParserController extends BaseController {
	/** The parser service. */
	@Autowired
	ParserService parserService;
	
	/** The device type service. */
	@Autowired
	DeviceTypeService deviceTypeService;
	
	/** The device service. */
	@Autowired
	DeviceService deviceService;
	
	/** The asn1 parser service. */
	@Autowired
	TapParserService tapParserService;
	
	/** The parser mapping service. */
	@Autowired
	ParserMappingService parserMappingService;
	
	/** The parser mapping validator. */
	@Autowired
	ParserMappingValidator parserMappingValidator;
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 *
	 * @param binder the binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	@RequestMapping(value=ControllerConstants.INIT_TAP_PARSER_CONFIG, method=RequestMethod.GET)
	public ModelAndView iniTapParserConfig(@RequestParam Map<String,String> requestParamMap){
		
		ModelAndView model=new ModelAndView(ViewNameConstants.ROAMING_PARSER_CONFIGURATION);
		int mappingId=getDeviceAndVendorTypeDetail(model,requestParamMap.get(BaseConstants.PLUGIN_ID));
		addCommonParamToModel(requestParamMap,model);
		TapParserMapping tapParserMapping = getTapParserMapping(mappingId);
		model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN,tapParserMapping);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ROAMING_PARSER_CONFIGURATION);
		model.addObject(BaseConstants.PARSER_MAPPING_ID,   mappingId);
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("recMainAttribute",   tapParserMapping.getRecMainAttribute());
		model.addObject("removeAddByte",   tapParserMapping.isRemoveAddByte());
		model.addObject("headerOffset",   tapParserMapping.getHeaderOffset());
		model.addObject("recOffset",   tapParserMapping.getRecOffset());
		model.addObject("removeFillers",   tapParserMapping.getRemoveFillers());
		model.addObject("removeAddHeaderFooter",   tapParserMapping.isRemoveAddHeaderFooter());
		model.addObject("recordStartIds",   tapParserMapping.getRecordStartIds());
		model.addObject("skipAttributeMapping",   tapParserMapping.isSkipAttributeMapping());
		model.addObject("rootNodeName",   tapParserMapping.getRootNodeName());
		model.addObject("decodeFormat",   tapParserMapping.getDecodeFormat());
		model.addObject("bufferSize",   tapParserMapping.getBufferSize());
		model.addObject("asn1DecodeTypeEnum", java.util.Arrays.asList(ASN1DecodeTypeEnum.values()));
		return model;
	}
	/**
	 * Updateassociate Tap parser mapping.
	 * @param TapParserMapping the tap parser mapping
	 * @param requestParamMap the request param map
	 * @param result the result
	 * @param request the request
	 * @return the model and view
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_TAP_PARSER_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView updateassociateTAPParserMapping(
			@ModelAttribute(value = FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN) TapParserMapping tapParserMapping,//NOSONAR
			@RequestParam(required = true) Map<String, String> requestParamMap, BindingResult result,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.ROAMING_PARSER_CONFIGURATION);
		String requestAction = BaseConstants.ROAMING_PARSER_ATTRIBUTE;
		String parserMappingId = requestParamMap.get("id");
		int tapParserMappingId = 0;
		if (parserMappingId != null) {
			tapParserMappingId = Integer.parseInt(parserMappingId);
		}
		int staffId = eliteUtils.getLoggedInStaffId(request);
		if ("true".equals(requestParamMap.get(BaseConstants.READ_ONLY_FLAG))) {
			logger.debug(
					"Tap  Parser Configuration is not updated ,  not call validate just redirect to attribute page in disable mode");
			updateAndAssociateParserMapping(tapParserMapping, requestParamMap, tapParserMappingId, model, staffId,
					requestAction);
		} else {
			logger.debug(
					"Tap Parser Configuration is updated , first update and then associate configuration with parser");
			boolean validationRequired = true;
			String nextAttributeType = requestParamMap.get("nextAttributeType");
			if (nextAttributeType != null && (nextAttributeType.equals(BaseConstants.ASN1_HEADER_PARSER_ATTRIBUTE)
					|| nextAttributeType.equals(BaseConstants.ASN1_TRAILER_PARSER_ATTRIBUTE))) {
				validationRequired = false;
				requestAction = requestParamMap.get("nextAttributeType");
			}
			if (validationRequired) {
				parserMappingValidator.validateParserMappingParameter(tapParserMapping, result, null, false, null);
			}
			if (result.hasErrors()) {

				model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN, tapParserMapping);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ROAMING_PARSER_CONFIGURATION);
				model.addObject(BaseConstants.READ_ONLY_FLAG, false);
				model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
				model.addObject("isValidationFail", true);
				model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
				model.addObject(BaseConstants.PARSER_MAPPING_NAME,
						requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME));
				getDeviceAndVendorTypeDetail(model, requestParamMap.get(BaseConstants.PLUGIN_ID));
				model.addObject(BaseConstants.SELECTED_DEVICE_ID,
						requestParamMap.get(BaseConstants.SELECTED_DEVICE_ID));
				model.addObject(BaseConstants.SELECTED_MAPPING_ID,
						requestParamMap.get(BaseConstants.SELECTED_MAPPING_ID));
				model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID,
						requestParamMap.get(BaseConstants.SELECTED_VENDOR_TYPE_ID));
				model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID,
						requestParamMap.get(BaseConstants.SELECTED_DEVICE_TYPE_ID));
				addCommonParamToModel(requestParamMap, model);
			} else {
				logger.debug("Validation done successfully , going to update and associate parser mapping ");
				updateAndAssociateParserMapping(tapParserMapping, requestParamMap, tapParserMappingId, model,
						staffId, requestAction);
			}
		}
		System.out.println("This plugin is updated");
		return model;
	}
	/**
	 * Redirect to Tap parser attribute page.
	 *
	 * @param requestParamMap the request param map
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER','UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.INIT_TAP_PARSER_ATTRIBUTE,method=RequestMethod.POST)
	public ModelAndView initTapParserAttribute(@RequestParam Map<String,String> requestParamMap){
		ModelAndView model = new ModelAndView(ViewNameConstants.ROAMING_PARSER_CONFIGURATION);
		if(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) !=null){
			int tapParserMappingId = Integer.parseInt(requestParamMap.get(BaseConstants.PARSER_MAPPING_ID));
			ResponseObject responseObject=tapParserService.getTapParserMappingById(tapParserMappingId);
			if(responseObject.isSuccess()){
				TapParserMapping tapParserMapping	=(TapParserMapping)responseObject.getObject();
				model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN,tapParserMapping);
			}else{
				model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN,(TapParserMapping) SpringApplicationContext.getBean(TapParserMapping.class));
			}
		}
		addCommonParamToModel(requestParamMap,model);
		if(StringUtils.isNotEmpty(requestParamMap.get(BaseConstants.ROAMING_PARSER_ATTRIBUTE_TYPE))){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestParamMap.get(BaseConstants.ROAMING_PARSER_ATTRIBUTE_TYPE));
		}else{
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ROAMING_PARSER_ATTRIBUTE);
		}
		model.addObject("sourceCharsetName",java.util.Arrays.asList(CharSetEnum.values()));
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("srcDataFormat",java.util.Arrays.asList(SourceFieldDataFormatEnum.values()));
		model.addObject("trimPosition",java.util.Arrays.asList(TrimPositionEnum.values()));
		model.addObject("sourceDateFormatEnum",java.util.Arrays.asList(SourceDateFormatEnum.values()));
		model.addObject(BaseConstants.READ_ONLY_FLAG,"true");
		model.addObject(BaseConstants.PARSER_MAPPING_ID,requestParamMap.get(BaseConstants.PARSER_MAPPING_ID) );
		model.addObject(BaseConstants.DEVICE_NAME,requestParamMap.get(BaseConstants.DEVICE_NAME) );
		model.addObject(BaseConstants.DEVICE_ID,requestParamMap.get(BaseConstants.DEVICE_ID) );
		model.addObject(BaseConstants.PARSER_MAPPING_NAME,requestParamMap.get(BaseConstants.PARSER_MAPPING_NAME));
		model.addObject(FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(TapParserMapping.class));
		model.addObject(FormBeanConstants.GROUP_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ParserGroupAttribute.class));

		return model;
	}
	/**
	 * Update tap  parser basic detail from attribute tab.
	 *
	 * @param tapParser the tap parser
	 * @param result the result
	 * @param request the request
	 * @return the string
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_TAP_PARSER_MAPPING, method = RequestMethod.POST)
	@ResponseBody
	public String UpdateTapParserBasicDetails(
			@ModelAttribute(value = FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN) TapParserMapping tapParserMapping,//NOSONAR
			BindingResult result, HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		parserMappingValidator.validateSrcDateFormat(tapParserMapping, result, null, false, null);
		if (result.hasErrors()) {
			logger.debug("Validation Error Occur");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
		}else{
			tapParserMapping.setLastUpdatedDate(new Date());
			tapParserMapping.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = tapParserService.updateTapParserMappings(tapParserMapping);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Get Device and vendor type detail.
	 *
	 * @param model the model
	 * @param pluginId the plugin id
	 * @return the device and vendor type detail
	 */
	@SuppressWarnings("unchecked")
	public int getDeviceAndVendorTypeDetail(ModelAndView model, String pluginId) {

		ResponseObject responseObject = deviceService.getAllDeviceTypeIdsByDecodeType(BaseConstants.UPSTREAM);
		// ResponseObject responseObject = deviceTypeService.getAllDeviceType();

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

		model.addObject(BaseConstants.SELECTED_DEVICE_ID, deviceId);
		model.addObject(BaseConstants.SELECTED_MAPPING_ID, mappingId);
		model.addObject(BaseConstants.SELECTED_VENDOR_TYPE_ID, vendorTypeId);
		model.addObject(BaseConstants.SELECTED_DEVICE_TYPE_ID, deviceTypeId);
		model.addObject("deviceId", deviceId);

		return mappingId;
	}

	/**
	 * Add Commom paramter to model .
	 *
	 * @param requestParamMap
	 *            the request param map
	 * @param model
	 *            the model
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
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));

	}
	/**
	 * Gets the TAP  parser mapping.
	 *
	 * @param rapParserMappingId
	 *            the TAP  parser mapping id
	 * @return the TapParserMapping
	 */
	private TapParserMapping getTapParserMapping(int tapParserMappingId) {
		if (tapParserMappingId > 0) {
			ResponseObject responseObject = tapParserService.getTapParserMappingById(tapParserMappingId);
			if (responseObject.isSuccess()) {
				return (TapParserMapping) responseObject.getObject();
			} else {
				return (TapParserMapping) SpringApplicationContext.getBean(TapParserMapping.class);
			}
		} else {
			return (TapParserMapping) SpringApplicationContext.getBean(TapParserMapping.class);
		}
	}
	/**
	 * Update and associate parser mapping.
	 *
	 * @param TapParserMapping the tap parser mapping
	 * @param requestParamMap the request param map
	 * @param TapParserMappingId the tap parser mapping id
	 * @param model the model
	 * @param staffId the staff id
	 * @param requestAction the request action
	 */
	private void updateAndAssociateParserMapping(TapParserMapping tapParserMapping,
			Map<String, String> requestParamMap, int tapParserMappingId, ModelAndView model, int staffId,
			String requestAction) {
		boolean bUpdate = Boolean.TRUE;
		if (requestAction.equals(BaseConstants.ROAMING_PARSER_ATTRIBUTE)) {
			ResponseObject responseObject = parserMappingService.updateAndAssociateParserMapping(tapParserMapping, requestParamMap.get("plugInType"),
					Integer.parseInt(requestParamMap.get("plugInId")), requestParamMap.get("actionType"), staffId);
			if(responseObject == null && !responseObject.isSuccess()){//NOSONAR
				bUpdate=Boolean.FALSE;
			}
		}
		if(bUpdate){//NOSONAR
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("natflow.parser.config.update.success"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestAction);
			model.addObject(BaseConstants.READ_ONLY_FLAG, requestParamMap.get(BaseConstants.READ_ONLY_FLAG));
			ResponseObject responseObject = tapParserService.getTapParserMappingById(tapParserMappingId);
			if (responseObject.isSuccess()) {
				TapParserMapping tapParser = (TapParserMapping) responseObject.getObject();
				model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN, tapParser);
			} else {
				model.addObject(FormBeanConstants.ROAMING_PARSER_MAPPING_FORM_BEAN,
						(TapParserMapping) SpringApplicationContext.getBean(TapParserMapping.class));
			}
			getDeviceAndVendorTypeDetail(model, requestParamMap.get(BaseConstants.PLUGIN_ID));
			addCommonParamToModel(requestParamMap, model);
			model.addObject(BaseConstants.DEVICE_NAME, requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
			model.addObject("mappingId", requestParamMap.get("id"));
			model.addObject(BaseConstants.PARSER_MAPPING_NAME,
					requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME));

			model.addObject(BaseConstants.SELECTED_DEVICE_NAME,
					requestParamMap.get(BaseConstants.SELECTED_DEVICE_NAME));
			model.addObject("id", requestParamMap.get("id"));
			model.addObject(BaseConstants.SELECTED_MAPPING_NAME,
					requestParamMap.get(BaseConstants.SELECTED_MAPPING_NAME));
			model.addObject("sourceCharsetName", java.util.Arrays.asList(CharSetEnum.values()));
			model.addObject("unifiedField", java.util.Arrays.asList(UnifiedFieldEnum.values()));
			model.addObject("trimPosition", java.util.Arrays.asList(TrimPositionEnum.values()));
			model.addObject("srcDataFormat", java.util.Arrays.asList(SourceFieldDataFormatEnum.values()));
			model.addObject("sourceDateFormatEnum", java.util.Arrays.asList(SourceDateFormatEnum.values()));
			model.addObject(FormBeanConstants.PARSER_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(TAPParserAttribute.class));
			model.addObject(FormBeanConstants.GROUP_ATTRIBUTE_FORM_BEAN,SpringApplicationContext.getBean(ParserGroupAttribute.class));
		}
		else {
			model.setViewName(ViewNameConstants.REGEX_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ROAMING_PARSER_CONFIGURATION);
			model.addObject(BaseConstants.ERROR_MSG, getMessage("natflow.parser.config.update.fail"));
		}
	}
}