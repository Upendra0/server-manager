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
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.consolidationservice.service.IConsolidationGroupAttributeService;
import com.elitecore.sm.consolidationservice.validator.ConsolidationGroupAttributeValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ConsolidationGroupAttributeController extends BaseController {

	@Autowired
	private IConsolidationGroupAttributeService consolidationGroupAttributeService;

	@Autowired
	private ConsolidationGroupAttributeValidator consolidationGroupAttributeValidator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(consolidationGroupAttributeValidator);
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String createConsolidationDefinitionGroupList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_GROUP_ATTRIBUTE_FORM_BEAN) DataConsolidationGroupAttribute dataConsolidationGroupAttribute,//NOSONAR
			BindingResult result, @RequestParam(value = "consDefId", required = true) String consDefId, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		
		consolidationGroupAttributeValidator.validateConsolidationDefinitionGroupAttributes(dataConsolidationGroupAttribute, result, null, false);
		
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
			dataConsolidationGroupAttribute.setCreatedDate(new Date());
			dataConsolidationGroupAttribute.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			DataConsolidation dataConsolidation = consolidationGroupAttributeService.getDataConsolidationById(iConsDefId);
			if(dataConsolidation != null) {
				dataConsolidationGroupAttribute.setDataConsolidation(dataConsolidation);
				responseObject = consolidationGroupAttributeService.addConsolidationGroupAttributeList(dataConsolidationGroupAttribute,iConsDefId);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_ADD_FAIL);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String updateConsolidationDefinitionGroupList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_GROUP_ATTRIBUTE_FORM_BEAN) DataConsolidationGroupAttribute dataConsolidationGroupAttribute,//NOSONAR
			BindingResult result, @RequestParam(value = "consDefId", required = true) String consDefId, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject  = new ResponseObject();
		
		consolidationGroupAttributeValidator.validateConsolidationDefinitionGroupAttributes(dataConsolidationGroupAttribute, result, null, false);
		
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
			dataConsolidationGroupAttribute.setLastUpdatedDate(new Date());
			dataConsolidationGroupAttribute.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			DataConsolidation dataConsolidation = consolidationGroupAttributeService.getDataConsolidationById(iConsDefId);
			
			if(dataConsolidation != null){
				dataConsolidationGroupAttribute.setDataConsolidation(dataConsolidation);
				responseObject = consolidationGroupAttributeService.updateConsolidationGroupAttributeList(dataConsolidationGroupAttribute,iConsDefId);	
			}
			else {
					logger.info("Consolidation Defination Group Attribute List update failed");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_UPDATE_FAIL);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String deleteConsolidationDefinitionGroupList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_GROUP_ATTRIBUTE_FORM_BEAN) DataConsolidationGroupAttribute dataConsolidationGroupAttribute,//NOSONAR
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
					responseObject = consolidationGroupAttributeService.getConsolidationGroupById(id);
					DataConsolidationGroupAttribute dataConsolidationGroupAttribute2 = (DataConsolidationGroupAttribute)responseObject.getObject();
					dataConsolidationGroupAttribute2.setStatus(StateEnum.DELETED);
					dataConsolidationGroupAttribute2.setLastUpdatedDate(new Date());
					dataConsolidationGroupAttribute2.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					DataConsolidation dataConsolidation = consolidationGroupAttributeService.getDataConsolidationById(dataConsolidationGroupAttribute2.getDataConsolidation().getId());
					if(dataConsolidation != null ){
						dataConsolidationGroupAttribute2.setDataConsolidation(dataConsolidation);
						responseObject = consolidationGroupAttributeService.updateConsolidationGroupAttributeList(dataConsolidationGroupAttribute2, dataConsolidationGroupAttribute2.getDataConsolidation().getId());
					}
					else {
						logger.info("Consolidation Defination Group Attribute List deletion failed");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_DELETE_FAIL);
				}
					
				}
				if(responseObject != null && !responseObject.isSuccess()){
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_DELETE_FAIL);
				}else if(responseObject != null && responseObject.isSuccess()){
					responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_DELETE_SUCCESS);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
		}	
		return ajaxResponse.toString();
	}
}
