package com.elitecore.sm.errorreprocess.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.errorreprocess.service.AutoErrorReprocessService;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;
import com.elitecore.sm.rulelookup.validator.AutoErrorReprocessingConfigValidator;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.services.model.ProcessingService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.trigger.service.TriggerService;
import com.elitecore.sm.util.EliteUtils;
/**
 * @author Urvashi Varsani
 *
 */
@RestController
public class AutoErrorReprocessController extends BaseController {
	
	@Autowired
	AutoErrorReprocessService autoReprocessService;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	private AutoErrorReprocessingConfigValidator autoErrorReprocessingConfigValidator;
	
	@Autowired
	private IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	private QuartJobSchedulingListener listner;
	
	@PreAuthorize("hasAnyAuthority('CONFIG_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = "getProcessingServiceInstance", method = RequestMethod.POST)
	@ResponseBody
	public String getProcessingServiceInstance(@RequestParam(value = "serverId", required = true) String serverId) {
		ResponseObject responseObject = autoReprocessService.getServiceByServerId(Integer.parseInt(serverId));
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	@PreAuthorize("hasAnyAuthority('CONFIG_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = ControllerConstants.ADD_NEW_AUTO_ERROR_REPROCESS_CONFIG, method = RequestMethod.POST)
	@ResponseBody public  String addNewAutoErrorReprocessConfig(@RequestParam(value="triggerId") String triggerId,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_PATHLIST_CONFIGURATION_FORM_BEAN) AutoErrorReprocessDetail autoErrorReprocessDetail,//NOSONAR
			BindingResult result,HttpServletRequest request){
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));
		CrestelSMJob job = new CrestelSMJob();
		CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
		job.setTrigger(trigger);
		autoErrorReprocessDetail.setJob(job);
		autoErrorReprocessingConfigValidator.validateAutoErrorReprocessing(autoErrorReprocessDetail, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = autoReprocessService.addNewAutoReprocessConfig(autoErrorReprocessDetail,staffId);
			listner.createAndScheduleJob(job);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = "getAutoConfiguredServiceByRule", method = RequestMethod.POST)
	@ResponseBody
	public String getAutoConfiguredServiceByRule(
			@RequestParam(value = "serviceInstanceIds", required = false) String serviceInstanceIds,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "severity", required = false) String severity,
			@RequestParam(value = "reasonCategory", required = false) String reasonCategory,
			@RequestParam(value = "rule", required = false) String rule,
			@RequestParam(value = "errorCode", required = false) String errorCode,
			@RequestParam(value = "isSearch",required = false) boolean isSearch,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord
			) {
		List<AutoErrorReprocessDetail> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = autoReprocessService.getAutoErrorReprocessListCount(isSearch,serviceInstanceIds,category,severity,reasonCategory,rule,errorCode);
		resultList  = autoReprocessService.getPaginatedList( eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,serviceInstanceIds,category,severity,reasonCategory,rule,errorCode,isSearch);
		if (resultList != null) { 
			for (AutoErrorReprocessDetail obj : resultList) {
				row = new HashMap<>();
				row.put("id", obj.getId());
				row.put("serviceName",obj.getService().getName());
				row.put("category",obj.getCategory());
				if(obj.getRule() != null) {
					row.put("ruleName", policyService.getPolicyRuleById(Integer.parseInt(obj.getRule())).getName());
				}else {
					row.put("ruleName","N/A");
				}
				//get Filter/Invalid path value from service config
				ProcessingService ps =(ProcessingService)servicesService.getAllServiceDepedantsByServiceId(obj.getService().getId());
				String path=null;
				if(obj.getCategory().equals("Invalid")) {
					path=ps.getFileGroupingParameter().getInvalidDirPath();
				}else if(obj.getCategory().equals("Filter")) {
					path=ps.getFileGroupingParameter().getFilterDirPath();
				}else {
					path="N/A";
				}
				if(!StringUtils.isEmpty(path)) {
					row.put("path",path);
				}
				if(obj.getJob().getTrigger().getStartAtDate()!=null){
					String startDate = obj.getJob().getTrigger().getStartAtDate().toString();
					startDate=EliteUtils.formatDate(startDate.substring(0, startDate.indexOf(" ")));
					
					String startHour = EliteUtils.addPaddingToString(obj.getJob().getTrigger().getStartAtHour().toString());
					String startMinute = EliteUtils.addPaddingToString(obj.getJob().getTrigger().getStartAtMinute().toString());
					String executionStart=startDate+" "+startHour+":"+startMinute;
					row.put("schedule",executionStart);
				}
				row.put("triggerId",obj.getJob().getTrigger().getID());
				row.put("triggerName",obj.getJob().getTrigger().getTriggerName());
				row.put("jobId",obj.getJob().getID());
				row.put("serviceId",obj.getService().getId());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = ControllerConstants.DELETE_AUTO_REPROCESS_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String deleteAutoReprocessFiles(@RequestParam(value = "ids",required = true)String ids,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = autoReprocessService.deleteAutoReprocessFiles(ids, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = ControllerConstants.GET_AUTO_ERROR_REPROCESS_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public String getAutoErrorReprocessById(@RequestParam(value = "id",required = true)int id,
			HttpServletRequest request){
		ResponseObject responseObject  = autoReprocessService.getAutoErrorReprocessById(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_AUTO_REPROCESS_FILE')")
	@RequestMapping(value = "updateNewAutoErrorReprocessConfig", method = RequestMethod.POST)
	@ResponseBody
	public String updateNewAutoErrorReprocessConfig(@RequestParam(value = "triggerId") String triggerId,
			@RequestParam(value = "jobId") String jobId, @RequestParam(value = "id") String autoErrorReprocessId,
			@ModelAttribute AutoErrorReprocessDetail autoErrorReprocessDetail, BindingResult result,//NOSONAR
			HttpServletRequest request) {
		int staffId = eliteUtils.getLoggedInStaffId(request);

		CrestelSMJob job = autoReprocessService.getJobByAutoReprocessJob(Integer.parseInt(jobId));

		ResponseObject responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));
		CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();

		job.setTrigger(trigger);
		autoErrorReprocessDetail.setJob(job);

		autoErrorReprocessingConfigValidator.validateAutoErrorReprocessing(autoErrorReprocessDetail, result, null, null,
				false);

		AjaxResponse ajaxResponse = new AjaxResponse();
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errorMsgs.put(error.getField(), error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			responseObject = autoReprocessService.updateAutoErrorReprocess(autoErrorReprocessDetail, staffId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
}
