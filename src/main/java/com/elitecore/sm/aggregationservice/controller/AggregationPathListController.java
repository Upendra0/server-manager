package com.elitecore.sm.aggregationservice.controller;

import java.util.Arrays;
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class AggregationPathListController extends BaseController{

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
	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_AGGREGATION_PATHLIST_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initAggregationPathlistConfiguration(@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId){

		ModelAndView model = new ModelAndView(ViewNameConstants.AGGREGATION_SERVICE_MANAGER);
		int iserviceId = Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		addCommonParametersToModal(model,service);
		ResponseObject responseObject = pathlistService.getAggregationPathListByServiceId(iserviceId);
		if(responseObject.isSuccess()){
			model.addObject("pathList",responseObject.getObject());
		}
		model.addObject("lastUpdateTime", DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AGGREGATION_PATHLIST_CONFIGURATION);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_AGGREGATION_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String createAggregationServicePathlist(	@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) AggregationServicePathList pathList,//NOSONAR
																		BindingResult result,
																		@RequestParam(value="pathListCount",required=true) String counter,
																		@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
																		HttpServletRequest request){
		
		logger.debug("Create Aggregation Service Path list. " + pathList.getName());
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		int iserviceId = Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		pathlistValidator.validateAggregationPathlistParams(pathList,result,null, null, false,service);
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
			pathList.setPathId("000");
			pathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.addAggregationServicePathList(pathList);
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();		
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_AGGREGATION_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String updateAggregationServicePathlist(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) AggregationServicePathList pathList,//NOSONAR
			BindingResult result,@RequestParam(value="pathListCount",required=true) String counter,
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			HttpServletRequest request){
		
		logger.debug("Update Aggregation Service path list. " + pathList.getName());
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject ;
		int iserviceId = Integer.parseInt(serviceId);
		AggregationService service = (AggregationService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		pathlistValidator.validateAggregationPathlistParams(pathList,result,null, null, false,service);
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
			logger.info("Validation done successfully for aggregation service pathlist parameters.");
			pathList.setLastUpdatedDate(new Date());
			pathList.setPathId("000");
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.updateAggregationServicePathList(pathList);
			if(responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_AGGREGATION_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteAggregationPathList(@RequestParam(value = "pathlistId",required=true) int pathlistId,
		HttpServletRequest request) throws SMException{
		logger.debug("Delete Aggregation Service path list. " + pathlistId);
		ResponseObject responseObject = null;
		try {
			responseObject = pathlistService.deletePathListDetails(pathlistId, eliteUtils.getLoggedInStaffId(request),false);
		} catch (CloneNotSupportedException e) {
			logger.error(e);
		}
		AjaxResponse  ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	private void addCommonParametersToModal(ModelAndView model, AggregationService service){
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
		model.addObject("trueFalseEnum", TrueFalseEnum.values());
	}

}
