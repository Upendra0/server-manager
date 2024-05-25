/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.systemaudit.model.SearchStaffAudit;
import com.elitecore.sm.systemaudit.model.SystemAudit;
import com.elitecore.sm.systemaudit.model.SystemAuditDetails;
import com.elitecore.sm.systemaudit.service.AuditDetailsService;
import com.elitecore.sm.systemaudit.service.AuditEntityService;
import com.elitecore.sm.systemaudit.service.SystemAuditService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 * January 28, 2016
 */
@Controller
public class AuditController extends BaseController{

	
	@Autowired
	@Qualifier(value = "auditEntityService")
	private AuditEntityService auditEntityService;
	
	@Autowired
	@Qualifier(value = "systemAuditService")
	private SystemAuditService systemAuditService;
	
	@Autowired
	@Qualifier(value = "auditDetailsService")
	private AuditDetailsService auditDetailsService;
	
	
	
	/**
	 * This function will be invoked to convert the Date to specified format.
	 * So we don't need to convert the date manually.
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUDIT')")
	@RequestMapping(value = ControllerConstants.VIEW_STAFF_AUDIT_DETAILS, method = RequestMethod.POST)
	public ModelAndView viewStaffAuditDetails(
			HttpServletRequest request,
			@RequestParam(value="systemAuditId",required = true) int systemAuditId) {
			ModelAndView model = new ModelAndView();
			
			List<SystemAuditDetails> systemAuditDetailList = auditDetailsService.getAuditDetailsBySystemAuditId(systemAuditId);
			
			model.addObject("auditDetailList", systemAuditDetailList);
			model.setViewName(ViewNameConstants.AUDIT_DETAIL_VIEW);
			
		return model;
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_AUDIT')")
	@RequestMapping(value = ControllerConstants.STAFF_AUDIT, method = RequestMethod.POST)
	public ModelAndView staffAudit(
			HttpServletRequest request,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required = false) String REQUEST_ACTION_TYPE) {
		
			ModelAndView model = new ModelAndView();
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.STAFF_AUDIT_MANAGEMENT);
			model.setViewName(ViewNameConstants.STAFF_MANAGER);
		return model;
	}
	
	/**
	 * Method will delete Collection Driver and update its application orders
	 * @param DriverId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_AUDIT')")
	@RequestMapping(value = ControllerConstants.GET_AUDIT_ENTITY, method = RequestMethod.POST)
	public @ResponseBody String getStaffAuditEntity(
						@RequestParam(value = "entityId",required=true) int entityId,
						@RequestParam(value = "entityType",required=true) String entityType){
		
		ResponseObject responseObject = auditEntityService.getAuditEntity(entityType, entityId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	

	
	/**
	 * Method will fetch all staff audit list with all action associated.
	 * @param searchService
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Response body
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_AUDIT')")
	@RequestMapping(value = ControllerConstants.GET_STAFF_AUDIT_LIST, method = RequestMethod.POST)
	public @ResponseBody String getStaffAuditList(
				SearchStaffAudit searchStaffAudit, 
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord,
				HttpServletRequest request) {
		logger.debug(">> getStaffAuditList in AuditController " + searchStaffAudit); 
		
		long count =  this.systemAuditService.getTotalServiceInstancesCount(searchStaffAudit);
	
		List<SystemAudit> resultList = new ArrayList<>();
		if(count > 0){
			resultList = this.systemAuditService.getPaginatedList(searchStaffAudit,
															   eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
															   limit, sidx,sord);
		}
		Map<String, Object> row = null;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (SystemAudit auditList : resultList) {
				row = new HashMap<>();
				row.put("id", auditList.getId());
				row.put("staffName", auditList.getUserName());
				row.put("currentAction", auditList.getSystemAuditActivity().getAlias());
				row.put("remark", auditList.getRemark());
				row.put("ipAdress", auditList.getIPAddress());
				row.put("auditDate", DateFormatter.formatDate(auditList.getAuditDate()));
				row.put("actionType", auditList.getActionType());
				row.put("sysAuditDetailsAvailable", ((auditList.getSystemAuditDetails()!=null && auditList.getSystemAuditDetails().size()>0)?true:false));			
				rowList.add(row);
			}
		}
		logger.debug("<< getStaffAuditList in AuditControoler "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();		
	}
	
}
