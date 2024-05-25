package com.elitecore.sm.trigger.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.elitecore.sm.common.model.TriggerTypeEnum;
import com.elitecore.sm.common.model.WeekEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.trigger.service.TriggerService;
import com.elitecore.sm.trigger.validator.TriggerValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;


@Controller
public class TriggerController extends BaseController {
	
	@Autowired
	TriggerService triggerService;
	
	@Autowired
	TriggerValidator triggerValidator;
	
	@Autowired
	JobService jobService;

	@Autowired
	QuartJobSchedulingListener quartzJobSchedulingListener;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}

	
	@PreAuthorize("hasAnyAuthority('VIEW_TRIGGER_CONFIG','UPDATE_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_TRIGGER_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initTriggerConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.UPDATE_TRIGGER_CONFIG)String requestActionType
			){
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_TRIGGER_CONFIG);
		model.addObject(FormBeanConstants.TRIGGER_FORM_BEAN,(CrestelSMTrigger) SpringApplicationContext
						.getBean(CrestelSMTrigger.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VIEW_TRIGGER_RECORDS);
		model.addObject("triggerTypeEnum",TriggerTypeEnum.getAllValues());
		model.addObject("weekEnum",WeekEnum.values());
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.GET_TRIGGER_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public String getTriggerById(@RequestParam(value = "id",required = true)int id,
			HttpServletRequest request){
		logger.debug(">> getTriggerById in TriggerController " + id); 
		ResponseObject responseObject  = triggerService.getTriggerById(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	@PreAuthorize("hasAnyAuthority('UPDATE_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.CREATE_TRIGGER_CONFIG, method = RequestMethod.POST)
	@ResponseBody
	public String createTrigger(
			@ModelAttribute(value=FormBeanConstants.TRIGGER_FORM_BEAN)CrestelSMTrigger trigger,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		triggerValidator.validateTrigger(trigger, result, null, false);
		ResponseObject responseObject;
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			 responseObject = triggerService.createTrigger(trigger,staffId);
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPDATE_TRIGGER_CONFIG, method = RequestMethod.POST)
	@ResponseBody
	public String updateTrigger(
			@ModelAttribute(value=FormBeanConstants.TRIGGER_FORM_BEAN)CrestelSMTrigger trigger,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		AjaxResponse ajaxResponse = new AjaxResponse();
		triggerValidator.validateTrigger(trigger, result, null, false);
		ResponseObject responseObject;
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			 responseObject = triggerService.updateTrigger(trigger,staffId);
			 quartzJobSchedulingListener.updaterQuartzTrigger(trigger, jobService);
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.DELETE_TRIGGER_CONFIG, method = RequestMethod.POST)
	@ResponseBody
	public String deleteTriggers(@RequestParam(value = "ids",required = true)String ids,
			HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = triggerService.deleteTriggers(ids, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_TRIGGER_CONFIG','UPDATE_TRIGGER_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_TRIGGER_DATA_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initTriggerTableList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "isSearch" ,required=false) boolean isSearch,
			@RequestParam(value = "searchName", required=false) String searchName,
			@RequestParam(value = "searchType", required=false) String searchType){
	
		List<CrestelSMTrigger> resultList;
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count = triggerService.getTriggerListCount(isSearch,searchName,searchType);
		resultList  = triggerService.getPaginatedList( eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,searchName,searchType,isSearch);
		if (resultList != null) { 
			for (CrestelSMTrigger trigger : resultList) {
				row = new HashMap<>();
				row.put("id", trigger.getID());
				row.put("triggerName",trigger.getTriggerName());
				row.put("description",trigger.getDescription());
				row.put("type",trigger.getRecurrenceType());
				
				if(trigger.getStartAtDate()!=null){
					String startDate = DateFormatter.dateToString(trigger.getStartAtDate(), BaseConstants.DATE_FORMAT_SHORT);
					String startHour = EliteUtils.addPaddingToString(trigger.getStartAtHour().toString());
					String startMinute = EliteUtils.addPaddingToString(trigger.getStartAtMinute().toString());
					String executionStart=startDate+" "+startHour+":"+startMinute;
					row.put("executionStart",executionStart);
				}
				if(trigger.getEndAtDate()!=null){
					String endDate = DateFormatter.dateToString(trigger.getEndAtDate(), BaseConstants.DATE_FORMAT_SHORT);
					String endHour = EliteUtils.addPaddingToString(trigger.getEndAtHour().toString());
					String endMinute = EliteUtils.addPaddingToString(trigger.getEndAtMinute().toString());
					String executionEnd=endDate+" "+endHour+":"+endMinute;
					row.put("executionEnd",executionEnd);
				}
				row.put("association",triggerService.isAssociatedWithJob(trigger.getID()));
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();

	}
	
	@RequestMapping(value = "getAssociationDetails", method = RequestMethod.POST)
	@ResponseBody
	public String getMappingAssociationDetails(@RequestParam(value="id") int id){
		logger.debug(">> getAssociationDetails for Scheduler " + id ); 
		ResponseObject responseObject  = triggerService.getMappingAssociationDetails(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}