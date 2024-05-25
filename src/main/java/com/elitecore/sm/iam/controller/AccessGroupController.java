/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.exceptions.AccessGroupNotFoundException;
import com.elitecore.sm.iam.exceptions.AccessGroupUniqueContraintException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.service.AccessGroupService;
import com.elitecore.sm.iam.validator.AccessGroupValidator;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani
 * Apr 7, 2015
 */
@Controller
public class AccessGroupController extends BaseController{

	@Autowired(required=true)
	@Qualifier(value="accessGroupService")
	private AccessGroupService accessGroupService;
	
	@Autowired
	private AccessGroupValidator validator;
	
	@Autowired(required = true) @Qualifier(value = "systemParameterService") 
	private SystemParameterService systemParamService;
	
	/**
	 * This function will be invoked to convert the Date to specified format.
	 * So we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	    binder.setValidator(validator);
	}
	
	/**
	 * It initializes the ViewNameConstants.ADD_ACCESS_GROUP the page with form bean.
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.INIT_ADD_ACCESS_GROUP, method = RequestMethod.GET)
	public ModelAndView initAddAccessGroup() {
		ModelAndView model = new ModelAndView();
		model.addObject(FormBeanConstants.ACCESS_GROUP_FORM_BEAN, (AccessGroup) SpringApplicationContext.getBean(AccessGroup.class));
		model.setViewName(ViewNameConstants.ADD_ACCESS_GROUP);
		ArrayList<String> accessGroupTypeList = new ArrayList<>();
		accessGroupTypeList.add("LOCAL");
		accessGroupTypeList.add("LDAP");
		accessGroupTypeList.add("SSO");
		model.addObject("accessGroupList",accessGroupTypeList);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ADD_ACCESS_GROUP);
		return model;
	}
	
	/**
	 * It adds the access group.
	 * @param accessGroup
	 * @param result
	 * @param status
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws AccessGroupUniqueContraintException
	 */
	@PreAuthorize("hasAnyAuthority('ADD_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.ADD_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView addAccessGroup(
			@Validated @ModelAttribute(FormBeanConstants.ACCESS_GROUP_FORM_BEAN) AccessGroup accessGroup ,//NOSONAR
			BindingResult result,
			SessionStatus status,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes
			) throws AccessGroupUniqueContraintException {
		ModelAndView model = new ModelAndView();
		//Check validation errors
		if (result.hasErrors()) {
			model.setViewName(ViewNameConstants.ADD_ACCESS_GROUP);
			model.addObject(FormBeanConstants.ACCESS_GROUP_FORM_BEAN, accessGroup);
		}else{
			model = new ModelAndView(new RedirectView(ControllerConstants.STAFF_MANAGER));
			
			accessGroup.initializeValues(eliteUtils.getLoggedInUser(request),false);

			ResponseObject responseObject = this.accessGroupService.save(accessGroup);
			if(responseObject.isSuccess()){
				redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG, getMessage("access.group.save.success"));
				status.setComplete();
				try {
					if(accessGroup.getAccessGroupType().equalsIgnoreCase("SSO"))
						responseObject = systemParamService.createRoleInKeycloak(accessGroup.getName());
				} catch (Exception e) {
					logger.trace(e);
					redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG,"Error while creating role in keycloak server. Please contact system administrator.");
				}

			}else{
				if(responseObject.getResponseCode() == ResponseCode.DUPLICATE_ACCESS_GROUP){
					throw new AccessGroupUniqueContraintException(accessGroup,ViewNameConstants.ADD_ACCESS_GROUP, BaseConstants.ADD_ACCESS_GROUP, result);
				}else {
					redirectAttributes.addFlashAttribute(BaseConstants.ERROR_MSG, getMessage("access.group.save.failure"));
				}
			}
			redirectAttributes.addFlashAttribute(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ADD_ACCESS_GROUP);
		}
		ArrayList<String> accessGroupTypeList = new ArrayList<>();
		accessGroupTypeList.add("LOCAL");
		accessGroupTypeList.add("LDAP");
		accessGroupTypeList.add("SSO");
		model.addObject("accessGroupList",accessGroupTypeList);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ADD_ACCESS_GROUP);
		return model;
	}
	
	/**
	 * Viewing of the access group based on the id specified.
	 * @param requestAccessGroupId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.VIEW_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView viewAccessGroup(
			@RequestParam(value = "accessGroupId",required=true) String requestAccessGroupId) {
		ModelAndView model = new ModelAndView();
		
		int iAccessGroupId=Integer.parseInt(requestAccessGroupId);
		AccessGroup accessGroup = accessGroupService.getAccessGroup(iAccessGroupId);
		if(accessGroup!=null){
			model.addObject("access_group_form_bean", accessGroup);
			model.setViewName(ViewNameConstants.ADD_ACCESS_GROUP); // same page of add access group is used in View and Update
		}else{
			model.setViewName(ViewNameConstants.STAFF_MANAGER);
			model.addObject(BaseConstants.ERROR_MSG, getMessage("access.group.is.null"));
		}
		ArrayList<String> accessGroupTypeList = new ArrayList<>();
		accessGroupTypeList.add("LOCAL");
		accessGroupTypeList.add("LDAP");
		accessGroupTypeList.add("SSO");
		model.addObject("accessGroupList",accessGroupTypeList);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.VIEW_ACCESS_GROUP);
		model.addObject("readOnly", true);
		return model;
	}
	
	/**
	 * Fetches the access group from database based on the id specified.
	 * @param requestAccessGroupId
	 * @param requestAfterChangeStatus
	 * @param changeStatusMsg
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView initUpdateAccessGroup(
			@RequestParam(value = "accessGroupId",required=true) String requestAccessGroupId,
			@RequestParam(value = "requestAfterChangeStatus",required=false,defaultValue="false") String requestAfterChangeStatus,
			@RequestParam(value = "changeStatusMsg",required=false,defaultValue="") String changeStatusMsg) {
		ModelAndView model = new ModelAndView();
		
		int iAccessGroupId=Integer.parseInt(requestAccessGroupId);
		AccessGroup accessGroup = accessGroupService.getAccessGroup(iAccessGroupId);
		if(accessGroup!=null){
			List<Integer> accessGroupStaffRelUniqueIdsList = accessGroupService.getAccessGroupStaffRelUniqueIds(accessGroup.getId());
			if(accessGroupStaffRelUniqueIdsList!= null && !accessGroupStaffRelUniqueIdsList.isEmpty())
				model.addObject("ASSIGNED_UNASSIGNED_TO_STAFF", "Assigned");
			else
				model.addObject("ASSIGNED_UNASSIGNED_TO_STAFF", "Unassigned");

			model.addObject("ACCESS_GROUP_STATE", accessGroup.getAccessGroupState());
			
			model.addObject(FormBeanConstants.ACCESS_GROUP_FORM_BEAN, accessGroup);
			model.setViewName(ViewNameConstants.ADD_ACCESS_GROUP); // same page of add access group is used in View and Update
		}else{
			model.setViewName(ViewNameConstants.STAFF_MANAGER);
			model.addObject(BaseConstants.ERROR_MSG, getMessage("access.group.is.null"));
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.UPDATE_ACCESS_GROUP);
		model.addObject("IS_REQUEST_AFTER_CHANGE_STATUS",requestAfterChangeStatus);
		model.addObject("CHANGE_STATUS_MSG",changeStatusMsg);
		model.addObject("readOnly", false);
		ArrayList<String> accessGroupTypeList = new ArrayList<>();
		accessGroupTypeList.add("LOCAL");
		accessGroupTypeList.add("LDAP");
		accessGroupTypeList.add("SSO");
		model.addObject("accessGroupList",accessGroupTypeList);
		return model;
	}
	
	/**
	 * Updates the access group.
	 * @param accessGroup
	 * @param result
	 * @param assignedUnassignedToStaff
	 * @param status
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws AccessGroupUniqueContraintException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.UPDATE_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView updateAccessGroup(
			@Validated @ModelAttribute(FormBeanConstants.ACCESS_GROUP_FORM_BEAN) AccessGroup accessGroup ,//NOSONAR
			BindingResult result,
			@RequestParam(value = "assignedUnassignedToStaff",required=false) String assignedUnassignedToStaff,
			SessionStatus status,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws AccessGroupUniqueContraintException{
		
		ModelAndView model = new ModelAndView();
		//Check validation errors
		if (result.hasErrors()) {
			model.addObject("ASSIGNED_UNASSIGNED_TO_STAFF", assignedUnassignedToStaff);
			model.setViewName(ViewNameConstants.ADD_ACCESS_GROUP);
			model.addObject(FormBeanConstants.ACCESS_GROUP_FORM_BEAN, accessGroup);
		}else{
			
			RedirectView view = new RedirectView(ControllerConstants.STAFF_MANAGER);
			model = new ModelAndView(view);
			
			accessGroup.initializeValues(eliteUtils.getLoggedInUser(request),true);
			ResponseObject responseObject = this.accessGroupService.update(accessGroup);
			if(responseObject.isSuccess()){
				redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG, getMessage("access.group.update.success"));
				status.setComplete();
			}else{
				if(responseObject.getResponseCode() == ResponseCode.DUPLICATE_ACCESS_GROUP){
					throw new AccessGroupUniqueContraintException(accessGroup,ViewNameConstants.ADD_ACCESS_GROUP, BaseConstants.UPDATE_ACCESS_GROUP, result);
				}else {
					redirectAttributes.addFlashAttribute(BaseConstants.ERROR_MSG, getMessage("access.group.update.failure"));
				}	
			}
			redirectAttributes.addFlashAttribute(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ADD_ACCESS_GROUP);
		}
		ArrayList<String> accessGroupTypeList = new ArrayList<>();
		accessGroupTypeList.add("LOCAL");
		accessGroupTypeList.add("LDAP");
		accessGroupTypeList.add("SSO");
		model.addObject("accessGroupList",accessGroupTypeList);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.UPDATE_ACCESS_GROUP);
		return model;
	}
	
	
	/**
	 * Deletes the access group.
	 * @param requestAccessGroupIds
	 * @param request
	 * @return
	 * @throws AccessGroupNotFoundException
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.DELETE_ACCESS_GROUP, method = RequestMethod.POST)
	@ResponseBody public String deleteAccessGroup(
			@RequestParam(value = "accessGroupIds",required=true) String requestAccessGroupIds,
			HttpServletRequest request)  throws AccessGroupNotFoundException {
		AjaxResponse ajaxResponse ;
		String accessGroupIds[] = requestAccessGroupIds.split(",");
		
		ResponseObject responseObject  = accessGroupService.deleteAccessGroups(accessGroupIds,eliteUtils.getLoggedInStaffId(request));
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Change Access Group state - Active-Inactive
	 * @param accessGroupId
	 * @param state
	 * @param reason
	 * @param request
	 * @return
	 * @throws AccessGroupNotFoundException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_ACCESS_GROUP')")
	@RequestMapping(value = ControllerConstants.CHANGE_ACCESS_GROUP_STATE, method = RequestMethod.POST)
	@ResponseBody public  String changeAccessGroupState(
			@RequestParam(value = "accessGroupId",required=true) String accessGroupId,
			@RequestParam(value = "state",required=true) StateEnum state,
			@RequestParam(value = "reason",required=false,defaultValue="") String reason,
			HttpServletRequest request) throws AccessGroupNotFoundException{
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		int iAccessGroupId=Integer.parseInt(accessGroupId);
		if(validator.validateReasonForChange(reason)){
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg(getMessage("access.group.state.changed.reason.regex.invalid").replace("[REGEX]", Regex.get(SystemParametersConstant.ACCESSGROUP_REASON_TO_CHANGE)));
		}else{
			ResponseObject responseObject = accessGroupService.changeAccessGroupState(iAccessGroupId, state, eliteUtils.getLoggedInStaffId(request));
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Fetches the Access Group List for Searching.
	 * @param searchCreatedByStaffId
	 * @param searchAccessGroupName
	 * @param searchStatus
	 * @param searchActiveInactiveStatus
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_ACCESS_GROUP')") // In SEARCH_STAFF, Access Group List is displayed in dropdown.
	@RequestMapping(value = ControllerConstants.GET_ACCESS_GROUP_LIST, method = RequestMethod.GET)
	@ResponseBody public  String getAccessGroupList(
    		@RequestParam(value = "created_by_staff_id",required=false,defaultValue="") String searchCreatedByStaffId,
    		@RequestParam(value = "access_group_name",required=false,defaultValue="") String searchAccessGroupName,
    		@RequestParam(value = "search_status",required=false,defaultValue="ALL") String searchStatus,
    		@RequestParam(value = "search_active_inactive_status",required=false,defaultValue="ALL") String searchActiveInactiveStatus,
    		@RequestParam(value = "rows", defaultValue = "10") int limit,
    		@RequestParam(value = "page", defaultValue = "1") int currentPage,
    		@RequestParam(value = "sidx", required=true) String sidx,
    		@RequestParam(value = "sord", required=true) String sord
    		) {
		int isearchCreatedByStaffId=0;
		if(!StringUtils.isEmpty(searchCreatedByStaffId)){
			isearchCreatedByStaffId=Integer.parseInt(searchCreatedByStaffId);	
		}
	
		long count = this.accessGroupService.getPaginatedListTotalCount(isearchCreatedByStaffId, searchAccessGroupName,searchStatus,searchActiveInactiveStatus);
		List<Map<String,Object>> resultList = new ArrayList<>();
		if(count>0)
			resultList = this.accessGroupService.getPaginatedList(
													isearchCreatedByStaffId,
													searchAccessGroupName,
													searchStatus,
													searchActiveInactiveStatus,													
													eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit))
													, limit, 
													sidx, 
													sord);

		return new JqGridData<Map<String, Object>>(
				eliteUtils.getTotalPagesCount(count, limit)
				, currentPage, 
				(int)count, 
				resultList).getJsonString();
    }
	
	/**
	 * Handles Access Group Not Found Exception.
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AccessGroupNotFoundException.class)
	@ResponseBody public  String handleAccessGroupNotFoundException(AccessGroupNotFoundException exception, HttpServletRequest request) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ajaxResponse.setResponseCode("400");
		ajaxResponse.setResponseMsg(getMessage("access.group.delete.fail"));
		return ajaxResponse.toString();		
	}

	/**
	 * Handles Access Group Unique Constraint Violation Exception.
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(AccessGroupUniqueContraintException.class)
	public ModelAndView handleAccessGroupUniqueException(AccessGroupUniqueContraintException exception, HttpServletRequest request) {

		BindingResult bindingResult = ((AccessGroupUniqueContraintException) exception).getBindingResult();
		
		ModelAndView model = new ModelAndView(exception.getRedirectURL(), bindingResult.getModel());
		logger.error("Requested URL="+request.getRequestURL());
		logger.error("Exception Raised="+exception);

		bindingResult.rejectValue("name", "error.access.group.name.already.exists", getMessage("access.group.name.already.exists"));
		model.addObject(FormBeanConstants.ACCESS_GROUP_FORM_BEAN, exception.getAccessGroup());
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, exception.getRequestActionType());
		return model;
	}

	
	public AccessGroupService getAccessGroupService() {
		return accessGroupService;
	}

	public void setAccessGroupService(AccessGroupService accessGroupService) {
		this.accessGroupService = accessGroupService;
	}
}