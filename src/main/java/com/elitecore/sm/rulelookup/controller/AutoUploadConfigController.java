package com.elitecore.sm.rulelookup.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
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
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.rulelookup.model.ScheduleTypeEnum;
import com.elitecore.sm.rulelookup.service.IAutoUploadConfigService;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;
import com.elitecore.sm.rulelookup.validator.AutoUploadConfigValidator;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.trigger.service.TriggerService;
import com.elitecore.sm.util.DateFormatter;


@Controller
public class AutoUploadConfigController extends BaseController {

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	private IAutoUploadConfigService autoUploadConfigService;
	
	@Autowired
	private TriggerService triggerService;
	
	@Autowired
	private AutoUploadConfigValidator autoUploadConfigValidator;
	
	@Autowired
	private QuartJobSchedulingListener quartJobSchedulingListener;
	
	@Autowired
	JobService jobService;
	
		
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	/** getLookUpData Count By ViewName **/
	/** @PreAuthorize("hasAnyAuthority('VIEW_RULE_DATA_CONFIG','UPDATE_RULE_DATA_CONFIG')")
	@RequestMapping(value = "getLookUpDataCountByViewName", method = RequestMethod.GET)
	@ResponseBody
	public String getLookUpDataCountByViewName(
			@RequestParam(value = "viewName", required=true) String viewName,
			@RequestParam(value = "viewID", required=false) String viewID){
		 
			long viewRecordsSize = ruleDataLookUpService.getSearchLookupDataListCountById(viewName,Integer.parseInt(viewID), null);
		
			Map<String, Object> row = new HashMap<>();
			List<Map<String, Object>> rowList = new ArrayList<>();
			row.put("viewRecordsSize", viewRecordsSize);
			row.put("viewName", viewRecordsSize);
			
			rowList.add(row);
			
		return rowList.toString();
	}*/
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_AUTO_UPLOAD_CONFIG_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initAutoUploadConfigList(				
		@RequestParam(value = "rows", defaultValue = "5") int limit,
		@RequestParam(value = "page", defaultValue = "1") int currentPage,
		@RequestParam(value = "sidx", required = true) String sidx,
		@RequestParam(value = "sord" , required=false) String sord,
		@RequestParam(value = "searchSourceDir" , required=false) String searchSourceDir,
		@RequestParam(value = "searchTableName" , required=false) String searchTableName,
		@RequestParam(value = "searchScheduler" , required=false) String searchScheduler){
	
		List<AutoUploadJobDetail> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = autoUploadConfigService.getAutoUploadConfigCount( searchSourceDir , searchTableName ,searchScheduler );
		resultList = autoUploadConfigService.getAutoUploadConfigPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
				limit, sidx, sord, searchSourceDir , searchTableName ,searchScheduler );
	 	
		if (resultList != null) { 
			for(AutoUploadJobDetail autoUploadJob : resultList){
				row = new HashMap<>();				
				row.put("id",autoUploadJob.getId());
				row.put("sourceDirectory",autoUploadJob.getSourceDirectory());
				row.put("tableName",autoUploadJob.getRuleLookupTableData().getViewName());
				/** row.put("scheduleName",lookupFieldDetailData.getScheduler().getJobName()); **/
				row.put("scheduleName",autoUploadJob.getScheduler().getTrigger().getTriggerName());
				row.put("lastRunTime",DateFormatter.dateToString(autoUploadJob.getScheduler().getLastRunTime(), BaseConstants.DATE_FORMAT_FOR_JOBSTATISTIC));
				row.put("nextRunTime",DateFormatter.dateToString(autoUploadJob.getScheduler().getNextRunTime(), BaseConstants.DATE_FORMAT_FOR_JOBSTATISTIC));
				row.put("filePrefix",autoUploadJob.getFilePrefix());
				row.put("fileContains",autoUploadJob.getFileContains());
				row.put("action",autoUploadJob.getAction());
				row.put("jobId",autoUploadJob.getScheduler().getID());
				row.put("triggerId",autoUploadJob.getScheduler().getTrigger().getID());
				row.put("lookuptableId",autoUploadJob.getRuleLookupTableData().getId());
				
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')")
	@RequestMapping(value = "addAutoUploadConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public String addAutoUploadConfiguration(		
		@RequestParam(value = "triggerId") String triggerId ,
		@ModelAttribute AutoUploadJobDetail autoUpload,//NOSONAR
		BindingResult result,HttpServletRequest request ){	
	
		int staffId = eliteUtils.getLoggedInStaffId(request);
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));
		
		CrestelSMJob job = new CrestelSMJob();
		CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
		job.setTrigger(trigger);
		autoUpload.setScheduler(job);
		
		autoUploadConfigValidator.validateAutoUpload(autoUpload, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = autoUploadConfigService.createAutoUpload( autoUpload ,staffId); 
			if(job!=null && job.getJobType()!=null && !job.getJobType().equals(ScheduleTypeEnum.Immediate.toString())){
				quartJobSchedulingListener.createAndScheduleJob(job);
			}
			job.setLastRunTime(null);
			jobService.merge(job);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
		
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')")
	@RequestMapping(value = "updateAutoUploadConfiguration", method = RequestMethod.POST)
	@ResponseBody
	public String updateAutoUploadConfiguration(	
		@RequestParam(value = "triggerId") String triggerId ,
		@RequestParam(value = "jobId") String jobId ,
		@RequestParam(value = "id") String autoUploadId ,
		@ModelAttribute AutoUploadJobDetail autoUpload,//NOSONAR
		BindingResult result,HttpServletRequest request ){	
	
		int staffId = eliteUtils.getLoggedInStaffId(request);
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		responseObject = triggerService.getTriggerById(Integer.parseInt(triggerId));		
		
		CrestelSMJob job;
		
		autoUpload.setId( Integer.parseInt( autoUploadId));
		
		job = autoUploadConfigService.getJobByAutoUploadJob(autoUpload.getId());
		CrestelSMTrigger trigger = (CrestelSMTrigger) responseObject.getObject();
		job.setTrigger(trigger);
		job.setLastRunTime(new Date());
		autoUpload.setScheduler(job);
		
		
		autoUploadConfigValidator.validateAutoUpload(autoUpload, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			for(FieldError error:result.getFieldErrors()){
				errorMsgs.put(error.getField(),error.getDefaultMessage());
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = autoUploadConfigService.updateAutoUpload( autoUpload ,staffId);
			if(job!=null && job.getJobType()!=null && !job.getJobType().equals(ScheduleTypeEnum.Immediate.toString())){
				quartJobSchedulingListener.updateAndRescheduleJob(job);
				jobService.merge(job);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
		
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUTO_UPLOAD_CONFIGURATION')")
	@RequestMapping(value = "deleteAutoUploadConfig", method = RequestMethod.POST)
	@ResponseBody
	public String deleteAutoUploadConfig(
		@RequestParam(value = "id") String autoUploadIds,		
		HttpServletRequest request ){
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = autoUploadConfigService.deleteMultipleAutoUploadJob( autoUploadIds , staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();		
	}
}
