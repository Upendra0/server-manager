package com.elitecore.sm.pathlist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
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
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ConsolidationPathListController extends BaseController{

	@Autowired
	ServicesService servicesService;
	
	@Autowired
	PathListService pathlistService;
	
	@Autowired
	PathListValidator pathlistValidator;
	
	@Inject
	private PathListHistoryService pathListHistoryService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_CONSOLIDATION_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String createConsolidationServicePathlist(	@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) DataConsolidationPathList pathList,//NOSONAR
																		BindingResult result,
																		@RequestParam(value="pathListCount",required=true) String counter,
																		HttpServletRequest request){
		
		logger.debug("Create Processing Path list. " + pathList.getName());
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		pathlistValidator.validateConsolidationPathlistParams(pathList,result,null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			pathList.setCreatedDate(new Date());
			pathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.addConsolidationServicePathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String updateConsolidationServicePathlist(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) DataConsolidationPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,
			HttpServletRequest request){
		
		logger.debug("Update processing path list. " + pathList.getName());
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject ;
		pathlistValidator.validateConsolidationPathlistParams(pathList, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			logger.info("Validation done successfully for processing service pathlist parameters.");
			DataConsolidationPathList consolidation_pathlist = (DataConsolidationPathList) pathlistService.getPathListById(pathList.getId());
			pathList.setCreatedDate(consolidation_pathlist.getCreatedDate());
			pathList.setCreatedByStaffId(consolidation_pathlist.getCreatedByStaffId());
			pathList.setLastUpdatedDate(new Date());
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.updateConsolidationServicePathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.ADD_CONSOLIDATION_MAPPING, method = RequestMethod.POST)
	@ResponseBody public String addConsolidationMapping(@ModelAttribute(value=FormBeanConstants.CONSOLIDATION_MAPPING_FORM_BEAN) DataConsolidationMapping mapping,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
		logger.debug("Add Consolidation Mapping. " + mapping);
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		mapping.setCreatedDate(new Date());
		mapping.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		pathlistValidator.validateConsolidationDefinationMapping(mapping, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = pathlistService.addConsolidationPathListMapping(mapping);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);	
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_CONSOLIDATION_MAPPING, method = RequestMethod.POST)
	@ResponseBody public String updateConsolidationMapping(@ModelAttribute(value=FormBeanConstants.CONSOLIDATION_MAPPING_FORM_BEAN) DataConsolidationMapping mapping,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
		logger.debug("Update Consolidation Mapping. " + mapping);
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		mapping.setCreatedDate(new Date());
		mapping.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		pathlistValidator.validateConsolidationDefinationMapping(mapping, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = pathlistService.updateConsolidationPathListMapping(mapping);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);	
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CONSOLIDATION_MAPPING, method = RequestMethod.POST)
	@ResponseBody public String deleteConsolidationMapping(@RequestParam(value = "mappingId", required=true) String mappingIds,
			HttpServletRequest request){
		logger.debug("Delete Consolidation Mapping. " + mappingIds);
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = pathlistService.deleteConsolidationPathListMapping(mappingIds, staffId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);	
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CONSOLIDATION_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteConsolidationPathList(@RequestParam(value = "pathlistId",required=true) int pathlistId,
			HttpServletRequest request) throws SMException{
		ResponseObject responseObject = null;
		try {
			responseObject = pathlistService.deletePathListDetails(pathlistId, eliteUtils.getLoggedInStaffId(request),false);
		} catch (CloneNotSupportedException e) {
			logger.error(e);
		}
		AjaxResponse  ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

}
