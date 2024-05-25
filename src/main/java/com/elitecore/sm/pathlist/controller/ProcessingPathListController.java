package com.elitecore.sm.pathlist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TimeUnitEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.services.model.DuplicateRecordPolicyTypeEnum;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ProcessingPathListController extends BaseController {

	@Autowired
	ServicesService servicesService;
	
	@Autowired
	PathListService pathlistService;
	
	@Autowired
	PathListValidator pathlistValidator;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	SnmpService snmpService;
	
	@Inject
	private PathListHistoryService pathListHistoryService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	/** Open Processing Service pathlist configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init update Processing Service pathlist response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_PROCESSING_PATHLIST_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtProcessingPathlistConfig(@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId) {
		
		ModelAndView model = new ModelAndView();
		int iserviceId=Integer.parseInt(serviceId);
		model.setViewName(ViewNameConstants.PROCESSING_SERVICE_MANAGER);
		ProcessingService processingService  = (ProcessingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		ResponseObject responseObject = pathlistService.getProcessingPathListByServiceId(iserviceId);
		if(responseObject.isSuccess()){
			model.addObject("pathList",responseObject.getObject());
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PROCESSING_PATHLIST_CONFIGURATION);
		addCommonParametersToModal(model, processingService);
		addGenericAlertList(model);
		return model;
	}
	
	/**
	 * Method will create new distribution driver path list and validate all required parameters.
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_PROCESSING_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String createProcessingServicePathlist(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) ProcessingPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,
			HttpServletRequest request){
	
		logger.debug("Create Processing path list. " + pathList.getName());
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		if(pathList.getPolicy() == null || pathList.getPolicy().getId() <= 0){
			pathList.setPolicy(null);
		}else{
			pathList.setPolicy(policyService.getPolicyById(pathList.getPolicy().getId()));
		}
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		
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
			pathList.setCreatedDate(new Date());
			pathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.addProcessingServicePathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if(responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
		}
		
		return ajaxResponse.toString();
	}


	/**
	 * Method will create new distribution driver path list and validate all required parameters.
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_PROCESSING_SERVICE_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String updateProcessingServicePathlist(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) ProcessingPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,
			HttpServletRequest request){
	
		logger.debug("Update processing path list. " + pathList.getName());
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		if(pathList.getPolicy() == null || pathList.getPolicy().getId() <= 0){
			pathList.setPolicy(null);
		}else{
			pathList.setPolicy(policyService.getPolicyById(pathList.getPolicy().getId()));  //For Audit Display.
		}
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		
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
			ProcessingPathList processing_pathlist = (ProcessingPathList) pathlistService.getPathListById(pathList.getId());
			pathList.setCreatedDate(processing_pathlist.getCreatedDate());
			pathList.setCreatedByStaffId(processing_pathlist.getCreatedByStaffId());
			pathList.setLastUpdatedDate(new Date());
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathlistService.updateProcessingServicePathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if(responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
		}
		
		return ajaxResponse.toString();
	}

	
	private void addCommonParametersToModal(ModelAndView model, ProcessingService service){
		model.addObject(BaseConstants.SERVICE_ID, service.getId());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, service.getServInstanceId());
		model.addObject("enableFileStats", service.isEnableFileStats());
		model.addObject("enableDBStats", service.isEnableDBStats());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("positionEnum", java.util.Arrays.asList(PositionEnum.values()));
		model.addObject("fileGroupEnable", service.getFileGroupingParameter().isFileGroupEnable());
		model.addObject("unifiedField",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("duplicateRecordPolicyType",java.util.Arrays.asList(DuplicateRecordPolicyTypeEnum.values()));
		model.addObject("acrossFileDuplicateDateIntervalType",java.util.Arrays.asList(TimeUnitEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
	}
	
	/**
	 * Method will delete Processing path-list  
	 * @param pathlistId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_PROCESSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteProcessingPathList(@RequestParam(value = "pathlistId",required=true) int pathlistId,
						HttpServletRequest request) throws SMException{
			ResponseObject responseObject = null;
			try {
				responseObject = pathlistService.deletePathListDetails(pathlistId, eliteUtils.getLoggedInStaffId(request),true);
			} catch (CloneNotSupportedException e) {
				logger.error(e);
			}
			AjaxResponse  ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	private void addGenericAlertList(ModelAndView model){
		List<SNMPAlert> genericAlertList = snmpService.getAlertsByCategory(SNMPAlertTypeEnum.GENERIC);
		if(genericAlertList != null && !genericAlertList.isEmpty()){
			 SNMPAlert dummyAlert = new SNMPAlert();
			 dummyAlert.setName("N/A");
			 genericAlertList.add(0, dummyAlert);
		}
		model.addObject("alertList", genericAlertList);
	}

}
