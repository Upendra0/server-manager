/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.exceptions.StaffNotFoundException;
import com.elitecore.sm.iam.exceptions.StaffUniqueContraintException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.iam.validator.StaffValidator;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.PasswordProcessor;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani
 * Apr 1, 2015
 */
@Controller
public class StaffController extends BaseController{
	
	@Autowired(required=true)
	@Qualifier(value="staffService")
	private StaffService staffService;
	
	@Autowired
	private StaffValidator validator;
	
	@Autowired
	SystemParameterService systemParamService;
	
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
	 * Initializes the Add Staff JSP ViewNameConstants.ADD_STAFF with form model.
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_STAFF')")
	@RequestMapping(value = ControllerConstants.INIT_ADD_STAFF, method = RequestMethod.GET)
	public ModelAndView initAddStaff(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		
		Staff staff = (Staff) SpringApplicationContext.getBean(Staff.class);
		staff.copyStaffProperties(eliteUtils.getLoggedInUser(request));

		staff.setLoginIPRestriction("");
		model.addObject(FormBeanConstants.STAFF_FORM_BEAN , staff);
		model.setViewName(ViewNameConstants.ADD_STAFF);
	
		return model;
	}
	
	/**
	 * Adds the staff in memory and redirects user to Assign Access Group Page
	 * @param staff
	 * @param request
	 * @param result
	 * @param status
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_STAFF')")
	@RequestMapping(value = ControllerConstants.ADD_STAFF, method = RequestMethod.POST)
	public ModelAndView addStaffInMemory(
			@Validated @ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			BindingResult result,
			HttpServletRequest request
			){
		
		
		ModelAndView model = new ModelAndView();
		
	    model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
	    
	    //Check validation errors
	    if (result.hasErrors()) {
	    	model.setViewName(ViewNameConstants.ADD_STAFF);
	    }else{
	    	String profilePicAsString = "img/staff_default_profile_pic.png";
	    	staff.setPassword(PasswordProcessor.encryptPassword(staff.getPassword(), BaseConstants.DEFAULT_ENCRYPTION_TYPE));
	    	staff.setProfilePic(profilePicAsString);

    		ResponseObject responseObject = staffService.addStaff(staff);
    		if(responseObject.isSuccess()){
    			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("staff.saved.success"));
    			model.setViewName(ViewNameConstants.ADD_STAFF_AND_ASSIGN_ACCESS_GROUP);
    		} else{
    			staff.setPassword("");
    			staff.setConfirmPassword("");
    			if(responseObject.getResponseCode() == ResponseCode.DUPLICATE_STAFF){
    				model.addObject(BaseConstants.ERROR_MSG, getMessage("username.already.exists"));
    			}else{
    				model.addObject(BaseConstants.ERROR_MSG, getMessage("staff.saved.failed"));
    			}
    			model.setViewName(ViewNameConstants.ADD_STAFF);
    		}
	    }
		return model;
	}
	
	/**
	 * It persists the Staff Data that logged-in staff entered and redirects staff to Add Staff Screen. 
	 * @param staff
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_STAFF')")
	@RequestMapping(value = ControllerConstants.GO_BACK_TO_ADD_STAFF_FROM_ASSIGN_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView goBackToAddStaffFromAssignACLScreen(
			@ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			HttpServletRequest request
			){
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.ADD_STAFF);
		model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
		return model;
	}
	
	/**
	 * Adds the Staff in database alonwith Assigned Access Group.
	 * @param staff
	 * @param result
	 * @param request
	 * @param status
	 * @return
	 * @throws StaffUniqueContraintException
	 * @throws SQLException 
	 * @throws SerialException 
	 */
	@PreAuthorize("hasAnyAuthority('ADD_STAFF')")
	@RequestMapping(value = ControllerConstants.ADD_STAFF_AND_ASSIGN_ACCESS_GROUP, method = RequestMethod.POST)
	public ModelAndView addStaffAndAssignAccessGroup(
			@ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			BindingResult result,
			HttpServletRequest request
			) throws StaffUniqueContraintException,  SQLException ,SMException{
		ModelAndView model = new ModelAndView();
		
	    	validator.validateAccessGroup(staff, result);
	    	if (result.hasErrors()){
	    		model.setViewName(ViewNameConstants.ADD_STAFF_AND_ASSIGN_ACCESS_GROUP);
	    	}else{
	    		
	    		logger.info("Staff Access Group: " + staff.getAccessGroupList());

	    		ResponseObject responseObject = staffService.updateStaffAccessGroup(staff.getAccessGroupList(),staff.getId());
	    		if(responseObject.isSuccess()){
	    			
	    			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("staff.access.group.assign.success"));
	    			model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
	    			model.setViewName(ViewNameConstants.CHANGE_STAFF_PROFILE_PIC);
	    		} else{
	    			
	    				model.addObject(BaseConstants.ERROR_MSG, getMessage("staff.saved.failed"));
	    				model.setViewName(ViewNameConstants.ADD_STAFF_AND_ASSIGN_ACCESS_GROUP);
	    		}
	    	}
	    

	    model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
	    model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ADD_STAFF);
	    
		logger.info("staff: " + staff);
		return model;
	}
	
	/**
	 * Intializes the Staff Manager Page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('STAFF_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.STAFF_MANAGER, method = RequestMethod.GET)
	public ModelAndView viewStaffManager(
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String REQUEST_ACTION_TYPE) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.STAFF_MANAGER);
		
		if(REQUEST_ACTION_TYPE != null && REQUEST_ACTION_TYPE.length()>0){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,REQUEST_ACTION_TYPE);
		}else  if(eliteUtils.isAuthorityGranted("VIEW_STAFF")){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.STAFF_MANAGEMENT);
		}else if(eliteUtils.isAuthorityGranted("VIEW_ACCESS_GROUP")){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ACCESS_GROUP_MANAGEMENT);
		}else{
			// Default Tab when clicked from left menu.
			if(eliteUtils.isAuthorityGranted("VIEW_STAFF"))
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.STAFF_MANAGEMENT);
			else if(eliteUtils.isAuthorityGranted("VIEW_ACCESS_GROUP")){
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ACCESS_GROUP_MANAGEMENT);
			}
		}
		return model;
	}
	
	/**
	 * Intializes the Staff Manager Page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_STAFF','EDIT_STAFF')")
	@RequestMapping(value = ControllerConstants.VIEW_STAFF_DETAILS, method = RequestMethod.POST)
	public ModelAndView viewStaffDetails(
			@RequestParam(value = "staffId",required=true,defaultValue="") String staffId,
			HttpServletRequest request) throws SMException{
		ModelAndView model = new ModelAndView();
		
		logger.info("ViewNameConstants.UPDATE_STAFF_DETAILS: " + ViewNameConstants.VIEW_STAFF_DETAILS);
		model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
		
		int istaffId=0;
		if(!(StringUtils.isEmpty(staffId))){
			istaffId=Integer.parseInt(staffId);
		}
		Staff staff = staffService.getFullStaffDetailsById(istaffId);
		model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
		model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.UPDATE_STAFF_BASIC_DETAIL);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF')")
	@RequestMapping(value = ControllerConstants.UPDATE_STAFF, method = RequestMethod.POST)
	public ModelAndView updateStaff(
			@Validated @ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			BindingResult result,
			HttpServletRequest request,
			@RequestParam(value="profilePicFile",required=false) MultipartFile file,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_STAFF_BASIC_DETAIL) String REQUEST_ACTION_TYPE_FROM_FORM
			) throws SMException, CloneNotSupportedException{
		ModelAndView model = new ModelAndView();

	    //Check validation errors
	    if (result.hasErrors()) {
	    	model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
	    }else{
	    	validator.validateAccessGroup(staff, result);
	    	logger.info("result.getAllErrors(): " + result.getAllErrors());

	    	if (result.hasErrors()) {
		    	model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
		    }else{
		    	if (result.hasErrors()) {
			    	model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
			    }else{
			    	ResponseObject responseObject = staffService.updateStaffDetails(staff);
			    	if(responseObject.isSuccess()){
		    			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("staff.updated.success"));
		    		} else{
		    			if(responseObject.getResponseCode() == ResponseCode.DUPLICATE_STAFF_EMAIL_ID){
		    				model.addObject(BaseConstants.ERROR_MSG, getMessage("email.already.exists"));
		    			}else{
		    				model.addObject(BaseConstants.ERROR_MSG, getMessage("staff.update.failed"));
		    			}
		    		}
			    }
		    	model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
		    	model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
		    }
	    }

	    
	    model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, REQUEST_ACTION_TYPE_FROM_FORM);
		return model;
	}
	
	
	/**
	 * This method provides the list of Staff Id and Username
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.GET_STAFF_ID_AND_USERNAME, method = RequestMethod.POST)
	@ResponseBody   public  String getStaffIdAndUsername(){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		Map<String,String> staffIdAndUsername = staffService.getAllStaffIdAndUsername();
		if(staffIdAndUsername!=null && staffIdAndUsername.size()>0){
			
			List<Map<String,String>> responseList = new ArrayList<>();
			Map<String,String> row ;
			for(String staffIdKey : staffIdAndUsername.keySet()){
				row = new HashMap<>();
				row.put("id", staffIdKey);
				row.put("username", staffIdAndUsername.get(staffIdKey));
				responseList.add(row);
			}
			
			ajaxResponse.setObject(responseList);
			ajaxResponse.setResponseCode("200");
		}else{
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg(getMessage("staff.list.is.empty"));
		}
		return ajaxResponse.toString();
    }

	/**
	 * This method provides the List of Staff based on search criteria provided.
	 * @param searchCreatedByStaffId
	 * @param searchUsername
	 * @param searchName
	 * @param searchStaffCode
	 * @param startDate
	 * @param endDate
	 * @param searchAccountState
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_STAFF')")
	@RequestMapping(value = ControllerConstants.GET_STAFF_LIST, method = RequestMethod.GET)
	@ResponseBody  public  String getStaffList(
    		@RequestParam(value = "firstName",required=false,defaultValue="") String searchFirstName,
    		@RequestParam(value = "lastName",required=false,defaultValue="") String searchLastName,
    		@RequestParam(value = "email_id",required=false,defaultValue="") String searchEmailId,
    		@RequestParam(value = "staff_code",required=false,defaultValue="") String searchStaffCode,
    		@RequestParam(value = "account_status",required=false,defaultValue="") String searchAccountState,
    		@RequestParam(value = "access_group_id",required=false,defaultValue="") String searchAccessGroupId,
    		@RequestParam(value = "lock_status",required=false,defaultValue="") String searchLockStatus,
    		@RequestParam(value = "created_by_staff_id",required=false,defaultValue="") String searchCreatedByStaffId,
    		@RequestParam(value = "username",required=false,defaultValue="") String searchUsername,
    		@RequestParam(value = "start_date",required=false) Date startDate,
    		@RequestParam(value = "end_date",required=false) Date endDate,
    		@RequestParam(value = "rows", defaultValue = "10") int limit,
    		@RequestParam(value = "page", defaultValue = "1") int currentPage,
    		@RequestParam(value = "sidx", required=true) String sidx,
    		@RequestParam(value = "sord", required=true) String sord,
    		HttpServletRequest request
    		) {
		int isearchCreatedByStaffId=0;
		if(!StringUtils.isEmpty(searchCreatedByStaffId)){
			isearchCreatedByStaffId=Integer.parseInt(searchCreatedByStaffId);	
		}
		long count = this.staffService.getTotalStaffCount(
													isearchCreatedByStaffId,
													searchUsername, 
													searchFirstName, searchLastName,
													searchStaffCode,
													startDate, 
													endDate,
													searchAccountState,
													searchEmailId,
													searchAccessGroupId,
													searchLockStatus,
													eliteUtils.getLoggedInUser(request).getUsername() + "," + BaseConstants.ADMIN_USERNAME + "," +BaseConstants.MODULE_ADMIN_USERNAME+ "," +BaseConstants.PROFILE_ADMIN_USERNAME
												);
		logger.info("count: " + count);
		List<Staff> resultList = new ArrayList<>();
		if(count>0)
			resultList = this.staffService.getPaginatedList(
													isearchCreatedByStaffId,
													searchUsername, 
													searchFirstName, searchLastName,
													searchStaffCode,
													startDate, 
													endDate,
													searchAccountState,
													searchEmailId,
													searchAccessGroupId,
													searchLockStatus,
													eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit))
													, limit, 
													sidx, 
													sord,
													eliteUtils.getLoggedInUser(request).getUsername() + "," + BaseConstants.ADMIN_USERNAME + "," +BaseConstants.MODULE_ADMIN_USERNAME+ "," +BaseConstants.PROFILE_ADMIN_USERNAME
												);

		Map<String, Object> row ;
		
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		StringBuilder accessGroupNames ;
		if(resultList!=null){
			logger.info("resultList size: " + resultList.size());
			for(Staff staff : resultList){
					row = new HashMap<>();

					row.put("id", staff.getId());
					row.put("username", staff.getUsername());
					row.put("staffCode", staff.getStaffCode());
					row.put("name", staff.getName());
					row.put("emailId", staff.getEmailId());
					accessGroupNames = new StringBuilder();
					if(staff.getAccessGroupList()!=null && !staff.getAccessGroupList().isEmpty()){
						for(AccessGroup accessGroup : staff.getAccessGroupList()){
							accessGroupNames.append(accessGroup.getName()).append(", ");
						}
						accessGroupNames = new StringBuilder(accessGroupNames.subSequence(0, accessGroupNames.length()-2));
					}else{
						accessGroupNames.append("");
					}
					row.put("accessGroupName", accessGroupNames.toString());
					row.put("accountState",staff.getAccountState().toString());
					row.put("isAccountLock", staff.isAccountLocked());
					row.put("stafftype", staff.getStafftype());
					rowList.add(row);
			}
		}
		
		return new JqGridData<Map<String, Object>>(
				eliteUtils.getTotalPagesCount(count, limit), 
				currentPage,
				(int)count,
				rowList).getJsonString();
    }
	
	/**
	 * Deletes the staff.
	 * @param requestStaffIds
	 * @param request
	 * @return
	 * 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_STAFF')")
	@RequestMapping(value = ControllerConstants.DELETE_STAFF, method = RequestMethod.POST)
	@ResponseBody	public  String deleteStaff(
			@RequestParam(value = "staffIds",required=true) String requestStaffIds,
			HttpServletRequest request) throws StaffNotFoundException{
		AjaxResponse ajaxResponse ;
		String staffIds[] = requestStaffIds.split(",");
		ResponseObject responseObject ;
		responseObject = staffService.deleteStaff(staffIds,eliteUtils.getLoggedInStaffId(request));
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
	
		return ajaxResponse.toString();
	}
	
	/**
	 * Lock Unlock the staff.
	 * @param requestStaffIds
	 * @param request
	 * @return
	 * 
	 */
	@PreAuthorize("hasAnyAuthority('LOCK_UNLOCK_STAFF')")
	@RequestMapping(value = ControllerConstants.LOCK_UNLOCK_STAFF, method = RequestMethod.POST)
	@ResponseBody public  String lockUnlockStaff(
			@RequestParam(value = "staffId",required=true) String requestStaffId,
			@RequestParam(value = "accountLocked",required=true) boolean accountLocked,
			HttpServletRequest request) throws StaffNotFoundException{
		AjaxResponse ajaxResponse ;
		
		int irequestStaffId=Integer.parseInt(requestStaffId);
		
		ResponseObject responseObject = staffService.lockUnlockStaff(irequestStaffId,eliteUtils.getLoggedInStaffId(request),accountLocked);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Change Staff state - Active-Inactive
	 * @param staffId
	 * @param state
	 * @param reason
	 * @param request
	 * @return
	 * @throws StaffNotFoundException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF')")
	@RequestMapping(value = ControllerConstants.CHANGE_STAFF_STATE, method = RequestMethod.POST)
	@ResponseBody public  String changeAccessGroupState(
			@RequestParam(value = "staffId",required=true) String staffId,
			@RequestParam(value = "state",required=true) StateEnum state,
			@RequestParam(value = "reason",required=false,defaultValue="") String reason,
			HttpServletRequest request) throws StaffNotFoundException{
		AjaxResponse ajaxResponse = new AjaxResponse();

		int istaffId=Integer.parseInt(staffId);
		
		if(validator.validateReasonForChange(reason)){
			ajaxResponse.setResponseCode("400");
			ajaxResponse.setResponseMsg(getMessage("staff.state.changed.reason.regex.invalid").replace("[REGEX]", Regex.get(SystemParametersConstant.STAFF_REASON_FOR_CHANGE_REGEX)));
		}else{
			ResponseObject responseObject = staffService.changeStaffState(istaffId, state, eliteUtils.getLoggedInStaffId(request));
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Initializes the Add Staff JSP ViewNameConstants.ADD_STAFF with form model.
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF','UPDATE_PROFILE')")
	@RequestMapping(value = ControllerConstants.MY_PROFILE, method = RequestMethod.GET)
	public ModelAndView initMyProfile (
		HttpServletRequest request,
		HttpServletResponse response,
		RedirectAttributes redirectAttributes) throws SMException{
		ModelAndView model = new ModelAndView();
		
		logger.info("ViewNameConstants.MY_PROFILE: " + ViewNameConstants.MY_PROFILE);
		model.setViewName(ViewNameConstants.MY_PROFILE);
	
		//Staff staff = staffService.getFullStaffDetailsById(eliteUtils.getLoggedInStaffId(request));
		Staff staff = eliteUtils.getLoggedInUser(request);
		logger.debug("Staff :" + staff);
		if(staff!=null){
			model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
			
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.UPDATE_STAFF_BASIC_DETAIL);
		}else{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			model = new ModelAndView(new RedirectView(ControllerConstants.WELCOME));
			redirectAttributes.addFlashAttribute(BaseConstants.ERROR_MSG, getMessage("invalid.session"));
		}
		if(staff!=null){
		model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
		}
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF','UPDATE_PROFILE')")
	@RequestMapping(value = ControllerConstants.UPDATE_PROFILE, method = RequestMethod.POST)
	public ModelAndView updateProfile(
			@Validated @ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			BindingResult result,
			HttpServletRequest request,
			@RequestParam(value="profilePicFile",required=false) MultipartFile file,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_STAFF_BASIC_DETAIL) String REQUEST_ACTION_TYPE_FROM_FORM
			) throws SMException, CloneNotSupportedException{
		ModelAndView model = new ModelAndView();
		
	    //Check validation errors
	    if (result.hasErrors()) {
	    	model.setViewName(ViewNameConstants.MY_PROFILE);
	    }else{
	    	validator.validateAccessGroup(staff, result);
	    	logger.info("After result.getAllErrors(): " + result.getAllErrors());
	    	if (result.hasErrors()) {
		    	model.setViewName(ViewNameConstants.MY_PROFILE);
		    }else{
		    	if (result.hasErrors()) {
			    	model.setViewName(ViewNameConstants.MY_PROFILE);
			    }else{
			    	ResponseObject responseObject = staffService.updateStaffDetails(staff);
			    	if(responseObject.isSuccess()){
			    		request.getSession().removeAttribute("add_staff_profile_pic_bytes");
			    		request.getSession().setAttribute(BaseConstants.STAFF_DETAILS,staff);
		    			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("profile.updated.success"));
		    		} else{
		    			if(responseObject.getResponseCode() == ResponseCode.DUPLICATE_STAFF_EMAIL_ID){
		    				model.addObject(BaseConstants.ERROR_MSG, getMessage("email.already.exists"));
		    			}else{
		    				model.addObject(BaseConstants.ERROR_MSG, getMessage("profile.update.failed"));
		    			}
		    		}
			    }
		    	model.setViewName(ViewNameConstants.MY_PROFILE);
		    	model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
		    }
	    }
	    Staff dbStaff = staffService.getFullStaffDetailsById(staff.getId());
	    staff.setAccessGroupList(dbStaff.getAccessGroupList());
	    model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, REQUEST_ACTION_TYPE_FROM_FORM);
		logger.info("staff: " + staff);
		logger.info("file: " + file);
		return model;
	}
	
	/**
	 * Handles Staff Not Found Exception.
	 * @param exception
	 * @param request
	 * @return
	 */
	@ExceptionHandler(StaffNotFoundException.class)
	@ResponseBody public  String handleStaffNotFoundException(StaffNotFoundException exception) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ajaxResponse.setResponseCode("400");
		
		if(exception.getOperation().equals(BaseConstants.CHANGE_STAFF_STATE))
			ajaxResponse.setResponseMsg(getMessage("staff.change.state.fail"));
		else if(exception.getOperation().equals(BaseConstants.DELETE_STAFF_STATE))
			ajaxResponse.setResponseMsg(getMessage("staff.delete.fail"));
		else if(exception.getOperation().equals(BaseConstants.LOCK_UNLOCK_STAFF_STATE))
			ajaxResponse.setResponseMsg(getMessage("staff.lock.unlock.fail"));
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Handles the Staff Unique Constraint Violation Exception.
	 * @param staff
	 * @param request
	 * @param result
	 * @param exception
	 * @return
	 */
	@ExceptionHandler({
			DataIntegrityViolationException.class
		})
    public ModelAndView handleDataIntegrityViolationException(
    		HttpServletRequest request, 
    		Exception exception){
		logger.error("Requested URL="+request.getRequestURL());
		logger.error("Exception Raised="+exception);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception", exception);
		modelAndView.addObject("url", request.getRequestURL());
		
		modelAndView.setViewName(ViewNameConstants.GENERIC_ERROR_PAGE);
		return modelAndView;
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_STAFF','UPDATE_PROFILE')")
	@RequestMapping(value = ControllerConstants.CHANGE_STAFF_PROFILE_PIC, method = RequestMethod.POST)
	public ModelAndView changeStaffProfilePic(	
			@ModelAttribute(FormBeanConstants.STAFF_FORM_BEAN) Staff staff,//NOSONAR
			BindingResult result,
			@RequestParam("profilePicFile") MultipartFile file,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE) String requestActionType,
			HttpServletRequest request
			) throws SMException{
		ModelAndView model = new ModelAndView();
		try{
			logger.debug("Request action type in changeStaffProfilePic" +requestActionType);
			if (!file.isEmpty() ) {
				
				validator.validateStaffProfilePic(file,result);
				
				if (result.hasErrors()) {
					logger.debug("Validation fail for uploaded file");
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.EDIT_STAFF_PROFILE_PIC);
					staff = staffService.getFullStaffDetailsById(staff.getId());
					if(BaseConstants.UPDATE_STAFF_PROFILE_PIC.equals(requestActionType)){
						model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
					}else if(BaseConstants.UPDATE_MY_PROFILE_PIC.equals(requestActionType)){
						model.setViewName(ViewNameConstants.MY_PROFILE);
					}else {
						model.setViewName(ViewNameConstants.CHANGE_STAFF_PROFILE_PIC);		
					}
				}else{
					staff = staffService.getFullStaffDetailsById(staff.getId());
					ResponseObject responseObject=systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.STAFFIMAGE);
					if(responseObject.isSuccess()){
						String	samplePath=(String)responseObject.getObject();
						logger.debug("Going to store staff logo  file at location: "+samplePath);
				
						responseObject=staffService.getStaffProfilePicFile(staff.getId());
						if(responseObject.isSuccess()){
							logger.debug("If file exists then remove it");
							File uploadedFile=(File)responseObject.getObject();
							uploadedFile.delete();
						}
						File uploadedFile;
						String fileName;
				
							fileName=file.getOriginalFilename();
							uploadedFile = new File(samplePath+File.separator+staff.getId()+"_"+fileName);
							file.transferTo(uploadedFile);	
					
						if(uploadedFile.exists()){
							logger.debug("Staff Profile Pic Updated successfully in file system");
						
							responseObject=staffService.updateStaffProfilePic(staff.getId(),staff.getId()+"_"+fileName,eliteUtils.getLoggedInStaffId(request),request);
							if(responseObject.isSuccess()){
								model.addObject(BaseConstants.RESPONSE_MSG, getMessage("staff.profile.pic.change.success"));
								
								
								if(BaseConstants.UPDATE_STAFF_PROFILE_PIC.equals(requestActionType)){
									model.setViewName(ViewNameConstants.VIEW_STAFF_DETAILS);
									model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
								}else if(BaseConstants.UPDATE_MY_PROFILE_PIC.equals(requestActionType)){
									model.setViewName(ViewNameConstants.STAFF_MANAGER);
									model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ADD_STAFF);
								}else{
									model.setViewName(ViewNameConstants.STAFF_MANAGER);	
									model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.ADD_STAFF);
								}
							
							}
						}
					}
				}
			
				
			model.addObject(FormBeanConstants.STAFF_FORM_BEAN, staff);
   		
   			model.addObject(BaseConstants.STAFF_PROFILE_PIC,staffService.getStaffProfilePicPath(staff.getId()));
				
			}else{
				logger.debug("Uploaded file is empty");
			}
		
		}catch(Exception e){
			logger.error("Exception Occured:"+e);
			throw new  SMException(e);
		}
		return model;
	}
	
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	
	
	
}