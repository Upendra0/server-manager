package com.elitecore.sm.consolidationservice.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.consolidationservice.service.IConsolidationAttributeService;
import com.elitecore.sm.consolidationservice.validator.ConsolidationAttributeValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ConsolidationAttributeController extends BaseController {

	@Autowired
	private IConsolidationAttributeService consolidationAttributeService;
	
	@Autowired
	private ConsolidationAttributeValidator consolidationAttributeValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(consolidationAttributeValidator);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_CONSOLIDATION_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String createConsolidationAttributeList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_ATTRIBUTE_FORM_BEAN) DataConsolidationAttribute dataConsolidationAttribute,//NOSONAR
			BindingResult result, @RequestParam(value = "consDefId", required = true) String consDefId, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		
		consolidationAttributeValidator.validateConsolidationDefinitionAttributes(dataConsolidationAttribute, result, null, false);

		int iConsDefId = Integer.parseInt(consDefId);
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			dataConsolidationAttribute.setCreatedDate(new Date());
			dataConsolidationAttribute.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			DataConsolidation dataConsolidationById = consolidationAttributeService.getDataConsolidationById(iConsDefId);
			if(dataConsolidationById != null){
			dataConsolidationAttribute.setDataConsolidation(dataConsolidationById);
			responseObject = consolidationAttributeService.addConsolidationAttributeList(dataConsolidationAttribute,iConsDefId);
			}
				else {
					logger.info("Consolidation Defination Attribute added failed.");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_ADD_FAIL);
				}
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String updateConsolidationAttributeList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_ATTRIBUTE_FORM_BEAN) DataConsolidationAttribute dataConsolidationAttribute,//NOSONAR
			BindingResult result, @RequestParam(value = "consDefId", required = true) String consDefId, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		consolidationAttributeValidator.validateConsolidationDefinitionAttributes(dataConsolidationAttribute, result, null, false);

		int iConsDefId = Integer.parseInt(consDefId);
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			dataConsolidationAttribute.setLastUpdatedDate(new Date());
			dataConsolidationAttribute.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			DataConsolidation dataConsolidationById = consolidationAttributeService.getDataConsolidationById(iConsDefId);
			if(dataConsolidationById != null){
				dataConsolidationAttribute.setDataConsolidation(dataConsolidationById);
				responseObject = consolidationAttributeService.updateConsolidationAttributeList(dataConsolidationAttribute,iConsDefId);
			}
			else {
				logger.info("Consolidation Defination Attribute Updated Failed.");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_UPDATE_FAIL);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CONSOLIDATION_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String deleteConsolidationAttributeList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_ATTRIBUTE_FORM_BEAN) DataConsolidationAttribute dataConsolidationAttribute,//NOSONAR
			BindingResult result, @RequestParam(value = "composerIdList", required = true) String composerIdList, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = null;
		
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			if(composerIdList != null && composerIdList.length() > 0) {
				String[] ids = composerIdList.split(",", -1);
				for(String strId : ids) {
					int id = Integer.parseInt(strId);
					responseObject = consolidationAttributeService.getConsolidationAttributeById(id);
					DataConsolidationAttribute dataConsolidationAttribute2 = (DataConsolidationAttribute)responseObject.getObject();
					dataConsolidationAttribute2.setStatus(StateEnum.DELETED);
					dataConsolidationAttribute2.setLastUpdatedDate(new Date());
					dataConsolidationAttribute2.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					DataConsolidation dataConsolidationById = consolidationAttributeService.getDataConsolidationById(dataConsolidationAttribute2.getDataConsolidation().getId());
					if(dataConsolidationById != null){
						dataConsolidationAttribute2.setDataConsolidation(dataConsolidationById);
						responseObject = consolidationAttributeService.updateConsolidationAttributeList(dataConsolidationAttribute2, dataConsolidationAttribute2.getDataConsolidation().getId());
					}else{
						logger.info("Consolidation Defination Attribute Delete Failed.");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_DELETE_FAIL);
					}
				}
				if(responseObject != null && !responseObject.isSuccess()){
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_DELETE_FAIL);
				}else if(responseObject != null && responseObject.isSuccess()){
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_DELETE_SUCCESS);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		}	
		return ajaxResponse.toString();
	}
}
