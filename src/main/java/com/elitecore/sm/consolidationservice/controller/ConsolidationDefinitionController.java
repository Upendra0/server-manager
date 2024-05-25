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
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.service.IConsolidationDefinitionService;
import com.elitecore.sm.consolidationservice.validator.ConsolidationDefinitionValidator;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ConsolidationDefinitionController extends BaseController {

	@Autowired
	private IConsolidationDefinitionService consolidationDefinitionService;

	@Autowired
	private ConsolidationDefinitionValidator consolidationDefinitionValidator;
	
	@Autowired
	private ServiceValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(consolidationDefinitionValidator);
	}

	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_CONSOLIDATION_DEFINITION_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String createConsolidationDefinitionList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_DEFINITION_FORM_BEAN) DataConsolidation dataConsList,//NOSONAR
			BindingResult result, @RequestParam(value = "conListCount", required = true) String counter, HttpServletRequest request) {

		logger.debug("Create new consolidation list. " + dataConsList.getConsName());

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		
		validator.validateConsolidationDefinitionParams(dataConsList, result, null, false);

		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(counter + "_" + error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(counter + "_" + error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			logger.info("Validation done successfully for distribution driver pathlist parameters.");
			dataConsList.setCreatedDate(new Date());
			dataConsList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = consolidationDefinitionService.addConsolidationDefintionList(dataConsList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_DEFINITION_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String updateConsolidationDefinitionList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_DEFINITION_FORM_BEAN) DataConsolidation dataConsList,//NOSONAR
			BindingResult result, @RequestParam(value = "conListCount", required = true) String counter,HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;

		validator.validateConsolidationDefinitionParams(dataConsList, result, null, false);
		
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(counter + "_" + error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(counter + "_" + error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			dataConsList.setLastUpdatedDate(new Date());
			dataConsList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = consolidationDefinitionService.updateConsolidationDefintionList(dataConsList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CONSOLIDATION_DEFINITION_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String deleteConsolidationDefinitionList(
			@ModelAttribute(value = FormBeanConstants.DATA_CONSOLIDATION_SERVICE_DEFINITION_FORM_BEAN) DataConsolidation dataConsList,//NOSONAR
			BindingResult result, @RequestParam(value = "conListCount", required = true) String counter,HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		Map<String, String> errorMsgs = new HashMap<>();
		
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(counter + "_" + error.getField(),
							getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(counter + "_" + error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			dataConsList = (DataConsolidation) consolidationDefinitionService.getConsolidationDefintionById(dataConsList).getObject();
			responseObject = consolidationDefinitionService.deleteConsolidationDefintionList(dataConsList, request, eliteUtils);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
}
