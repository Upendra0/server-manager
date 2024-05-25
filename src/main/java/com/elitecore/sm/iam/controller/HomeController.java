package com.elitecore.sm.iam.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.exceptions.StaffUniqueContraintException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.AuthenticationService;
import com.elitecore.sm.iam.service.SpringLoginServiceImpl;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.policy.controller.TempAlert;
import com.elitecore.sm.policy.controller.TempFileStatistic;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;
/**
 * HomeController is the entry point for any user.
 * 
 * @author Sunil Gulabani
 * Mar 24, 2015
 */
@Controller
public class HomeController extends BaseController{

	@Autowired(required = true)
	@Qualifier(value = "staffService")
	private StaffService staffService;

	@Autowired(required = true)
	@Qualifier(value = "licenseService")
	private LicenseService licenseService;

	@Autowired(required = true)
	private License license;

	@Autowired
	ServletContext servletContext;
	
	
	@Autowired
	AuthenticationService authenticationService;
	
	/**
	 * Entry Point of the Application. It will redirect to the Login Page.
	 * @return
	 */
	@RequestMapping(value = { ControllerConstants.ROOT_PATH, ControllerConstants.WELCOME }, method = RequestMethod.GET)
	public ModelAndView entryPoint(Locale locale) {
		
		ModelAndView model = new ModelAndView();
		model.addObject(BaseConstants.CURRENT_LANGUAGE_LOCALE, locale);
		model.addObject(BaseConstants.LANGUAGE_PROP_LIST, MapCache.getConfigValueAsObject(SystemParametersConstant.LANGUAGE_PROP_LIST));

		//String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator; //repository path up-to license folder.
		//logger.info("repositoryPath for license folder = "+repositoryPath);
		
		
		//try {
			// Checking if version is 7.3.0 or above then perform one time operation
			//logger.info("going to perform one time operation for versrion 7.3.0 or above");
			//commenting below call due to 7.3.0 version is now in Image and upgrade from 7.x platform to 7.3 is not applicable
			//ResponseObject responseObject1 = licenseService.applyNewLicenseKeySM(repositoryPath);
			
			/*ResponseObject responseObject = licenseService.checkLicenseDetails(repositoryPath);
			if (responseObject.isSuccess()) {	
				if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){
					return new ModelAndView("redirect:"+"/sso");
				}
				else{
					model.setViewName(ViewNameConstants.LOGIN_PAGE);
				}
			} else {
				String systemPath = servletContext.getRealPath(BaseConstants.SM_SYSTEM_PATH) + File.separator; 
				boolean isFileExist = licenseService.checkSystemFile(systemPath);
				model.addObject(BaseConstants.TRIAL_BUTTON_ACTION, isFileExist);
				model.setViewName(ViewNameConstants.LICENSE_AGREEMENT);
			}*/
			if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){
				return new ModelAndView(ViewNameConstants.SSO_REDIRECT);
			}else{
				model.setViewName(ViewNameConstants.LOGIN_PAGE);
				return model;
			}
		//} catch (SMException e) {
		//	logger.error("Failed to validate license details due to exception " + e);
		//}
		//return model;
}
	
	
	 
	@RequestMapping(value = { ControllerConstants.LOGIN}, method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest httpRequest) throws SMException {
		logger.debug("login using rest api ");
		String username = httpRequest.getHeader(BaseConstants.HEADER_USERNAME);
		String password = httpRequest.getHeader(BaseConstants.HEADER_PASS_PARAM);
		return authenticationService.authenticate(username, password).toString();
	}
	
	
	
	/**
	 * After successful login, user will be redirected to the Home Page.
	 * @param request
	 * @return
	 * @throws StaffUniqueContraintException
	 */
	
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAnyAuthority('HOME')")
	@RequestMapping(value = { ControllerConstants.HOME,ControllerConstants.SSOHOME }, method = RequestMethod.GET)
	public ModelAndView home(@RequestParam(value = "isRsaSuccess", required=false) String isRsaSuccess,HttpServletRequest request)  throws StaffUniqueContraintException,SMException{
		ModelAndView model = new ModelAndView();
		if(isRsaSuccess==null)
			isRsaSuccess="no";
		User springUserBeanHome = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		if(Boolean.parseBoolean(MapCache.getConfigValueAsObject(SystemParametersConstant.SSO_ENABLE).toString())){
			springUserBeanHome=staffAutoritiesForSSO(springUserBeanHome);
		}
		request.getSession().setAttribute(BaseConstants.USER_NAME, springUserBeanHome.getUsername());
		if(staffService.isLDAPStaff(springUserBeanHome.getUsername()) && !isRsaSuccess.equals("yes") && Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(SystemParametersConstant.RSA_CALL))){
			model.setViewName(ViewNameConstants.RSA_OTP_PAGE);
		}else{
		
		model.setViewName(ViewNameConstants.HOME_PAGE);
		model.addObject("isRenew", false);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DASHBOARD);
		
		/* Bypassing License check MED-10060
		boolean isProcess;
		
		HttpSession httpSession =request.getSession();
		String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator; // License folder path
		
		// Checking if version is 7.3.0 or above then perform one time operation
		logger.info("going to perform one time operation for versrion 7.3.0 or above");
		//ResponseObject responseObject1 = licenseService.applyNewLicenseKeySM(repositoryPath);
		
		ResponseObject responseObject = licenseService.validateLicenseDetails(repositoryPath);
		if(responseObject.isSuccess()){
			isProcess = checkLicenseRedirectionAction(model, responseObject, httpSession);
		}else{
			isProcess = false;
			httpSession.invalidate();
			
			String systemPath = servletContext.getRealPath(BaseConstants.SM_SYSTEM_PATH) + File.separator; 
			boolean isFileExist = licenseService.checkSystemFile(systemPath);
			model.addObject(BaseConstants.TRIAL_BUTTON_ACTION, isFileExist);
			model.setViewName(ViewNameConstants.LICENSE_AGREEMENT); // Redirect to license agreement page.
			
		}*/
		
		//if(isProcess){
			User springUserBean= springUserBeanHome;
			Staff staff ;
			if(springUserBean != null && request.getSession().getAttribute(BaseConstants.STAFF_DETAILS) == null){
				model.addObject("username", springUserBean.getUsername());
				staff = staffService.getFullStaffDetails(springUserBean.getUsername());
				if(staff != null){
					staff.setLastLoginTime(new Date());
					staffService.updateStaff(staff);
					request.getSession().setAttribute(BaseConstants.STAFF_DETAILS, staff);
				}
				request.getSession().setMaxInactiveInterval(MapCache.getConfigValueAsInteger(SystemParametersConstant.SESSION_TIMEOUT_IN_MINUTES, 1) * 60);
			}else{
				staff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);
			}
			
			if(staff != null && !BaseConstants.MODULE_ADMIN_USERNAME.equalsIgnoreCase(staff.getUsername()) && !BaseConstants.PROFILE_ADMIN_USERNAME.equalsIgnoreCase(staff.getUsername())){
				//No Need to change and reset password for module and profile admin user
				if(staff.isFirstTimeLogin()){
					model.setViewName(ViewNameConstants.CHANGE_PASSWORD);
					request.getSession().setAttribute(BaseConstants.IS_LOGIN_FIRST_TIME, "true");
				}else if(staff.getPasswordExpiryDate()!=null && staff.getPasswordExpiryDate().compareTo(new Date()) <=0){
					model.setViewName(ViewNameConstants.RESET_PASSWORD);
				}
			}
			String dataStr = staffService.getStaffProfilePicPath(eliteUtils.getLoggedInStaffId(request));
			MapCache.addConfigObject(BaseConstants.STAFF_LOGO,dataStr);
			request.getSession().setAttribute(BaseConstants.STAFF_LOGO,dataStr);
		//}
 		
		// Added following temp data for dashboard high alerts and file statistic details
		List<TempAlert> alertList = new ArrayList<>();
		TempAlert alert = new TempAlert();
		alert.setIntanceName("Mediation Server-192.168.0.146");//NOSONAR
		alert.setStage("critical");
		alert.setAlert("Disk Utilization is high(98%) for Server Intance.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("CGF Server-192.168.1.19");//NOSONAR
		alert.setStage("critical");
		alert.setAlert("Distribution Service goes down.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("IPLMS_Server - 192.168.1.168 ");//NOSONAR
		alert.setStage("info");
		alert.setAlert("102 file moved to Error directory for Parsing.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("Mediation Server-192.168.1.198");//NOSONAR
		alert.setStage("normal");
		alert.setAlert("GTP Collection Service packets not found in last 10 min.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setStage("info");
		alert.setIntanceName("Mediation Server-192.168.4.20");//NOSONAR
		alert.setAlert("998 files are found duplicate in Collection Service.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		List<TempFileStatistic> statisticList = new ArrayList<>();
		TempFileStatistic statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.0.146");//NOSONAR
		statistic.setProcessedCDR("28000000");
		statistic.setProcessedFileSize("118");
		statistic.setRawCDR("30000000");
		statistic.setRawFileSize("120");
		statistic.setDeviation(9.33);
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("CGF Server-192.168.1.19");//NOSONAR
		statistic.setProcessedCDR("15000000");
		statistic.setProcessedFileSize("131");
		statistic.setRawCDR("40000000");
		statistic.setDeviation(3.75);
		statistic.setRawFileSize("135");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("IPLMS_Server - 192.168.1.168 ");//NOSONAR
		statistic.setProcessedCDR("22000000");
		statistic.setProcessedFileSize("121");
		statistic.setRawCDR("30000000");
		statistic.setDeviation(6);
		statistic.setRawFileSize("130");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.1.198");//NOSONAR
		statistic.setProcessedCDR("18000000");
		statistic.setProcessedFileSize("123");
		statistic.setRawCDR("20000000");
		statistic.setDeviation(9);
		statistic.setRawFileSize("125");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.4.20");//NOSONAR
		statistic.setProcessedCDR("30000000");
		statistic.setProcessedFileSize("118");
		statistic.setRawCDR("35000000");
		statistic.setDeviation(8.5);
		statistic.setRawFileSize("128");
		
		statisticList.add(statistic);
		
		List<TempFileStatistic> statisticInstaceList = new ArrayList<>();
		TempFileStatistic statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Collection Service");
		statisticInstace.setProcessedCDR("28000000");
		statisticInstace.setProcessedFileSize("118");
		statisticInstace.setRawCDR("30000000");
		statisticInstace.setRawFileSize("120");
		statisticInstace.setDeviation(9.33);
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Distribution Service");
		statisticInstace.setProcessedCDR("15000000");
		statisticInstace.setProcessedFileSize("131");
		statisticInstace.setRawCDR("40000000");
		statisticInstace.setDeviation(3.75);
		statisticInstace.setRawFileSize("135");
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Parsing Service");
		statisticInstace.setProcessedCDR("22000000");
		statisticInstace.setProcessedFileSize("121");
		statisticInstace.setRawCDR("30000000");
		statisticInstace.setDeviation(6.2);
		statisticInstace.setRawFileSize("130");
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Processing Service");
		statisticInstace.setProcessedCDR("18000000");
		statisticInstace.setProcessedFileSize("123");
		statisticInstace.setRawCDR("20000000");
		statisticInstace.setDeviation(9);
		statisticInstace.setRawFileSize("125");
		statisticInstaceList.add(statisticInstace);
		
		
		model.addObject("alertList",alertList);
		model.addObject("statisticList",statisticList);
		model.addObject("iStatisticList",statisticInstaceList);
		}
		return model;
	}
	
	
	
	/**
	 * Method will check and set license redirection action for license.
	 * @param model
	 * @param responseObject
	 */
	@SuppressWarnings("unchecked")
	public boolean checkLicenseRedirectionAction(ModelAndView model ,ResponseObject responseObject, HttpSession httpSession){
		boolean isValidLicese ;
		
		if(LicenseConstants.LICENSE_EXPIRED.equals(responseObject.getModuleName())){
			logger.debug("License is expired.");
			httpSession.invalidate();
			
			String systemPath = servletContext.getRealPath(BaseConstants.SM_SYSTEM_PATH) + File.separator; 
			boolean isFileExist = licenseService.checkSystemFile(systemPath);
			model.addObject(BaseConstants.TRIAL_BUTTON_ACTION, isFileExist);
			
			model.addObject(LicenseConstants.LICENSE_REMINDER_MESSAGE, getMessage(responseObject.getResponseCode().toString(),responseObject.getArgs()));
			model.addObject(LicenseConstants.LICENSE_EXPIRED_MESSAGE, true);
			model.addObject(LicenseConstants.LICENSE_RENEW_MESSAGE, false);
			isValidLicese = false;
			model.setViewName(ViewNameConstants.LICENSE_ALERTS); // Redirect to license agreement page.
		}else if(LicenseConstants.LICENSE_FULL_REMINDER.equals(responseObject.getModuleName())){
			logger.debug("License is about to expired. ");
			model.addObject(LicenseConstants.LICENSE_REMINDER_MESSAGE, getMessage(responseObject.getResponseCode().toString(),responseObject.getArgs())); // Setting message with remaining days.
			model.addObject(LicenseConstants.LICENSE_EXPIRED_MESSAGE, false);
			model.addObject(LicenseConstants.LICENSE_RENEW_MESSAGE, true);
			Map<String,String> licenseDetails = (Map<String, String>) responseObject.getObject();
			model.addObject("isEnableShortMessage",true);
			model.addObject("licenseShortReminder",getMessage("full.license.short.reminder", new Object[] {licenseDetails.get(LicenseUtility.END_DATE)}));
			httpSession.setAttribute("licenseShortReminder", getMessage("full.license.short.reminder", new Object[] {licenseDetails.get(LicenseUtility.END_DATE)}));
			model.setViewName(ViewNameConstants.LICENSE_ALERTS);
			isValidLicese = true;
		}else if(LicenseConstants.LICENSE_TRAIL_REMINDER.equals(responseObject.getModuleName())){
			logger.debug("License is about to expired. ");
			model.addObject(LicenseConstants.LICENSE_REMINDER_MESSAGE, getMessage(responseObject.getResponseCode().toString(),responseObject.getArgs())); // Setting message with remaining days.
			model.addObject(LicenseConstants.LICENSE_EXPIRED_MESSAGE, false);
			model.addObject(LicenseConstants.LICENSE_RENEW_MESSAGE, false);
			model.setViewName(ViewNameConstants.LICENSE_ALERTS);
			Map<String,String> licenseDetails = (Map<String, String>) responseObject.getObject();
			model.addObject("isEnableShortMessage",true);
			model.addObject("licenseShortReminder",getMessage("trail.license.short.reminder", new Object[] {licenseDetails.get(LicenseUtility.END_DATE)}));
			httpSession.setAttribute("licenseShortReminder", getMessage("full.license.short.reminder", new Object[] {licenseDetails.get(LicenseUtility.END_DATE)}));
			isValidLicese = true;
		}else{
			logger.debug("Found valid license details.");
			isValidLicese = true;
			model.addObject("isRenew", false);
		//	model.setViewName(ViewNameConstants.INIT_DASHBOARD_MANAGER);
			model.setViewName(ViewNameConstants.HOME_PAGE);
		}
		return isValidLicese;
	}
	/**
	 * Handles error 405.
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.REQUESTED_METHOD_NOT_SUPPORTED, method = RequestMethod.GET)
	public ModelAndView error405(HttpServletRequest request){
		ModelAndView model = new ModelAndView(ViewNameConstants.REQUEST_METHOD_NOT_SUPPORTED);
		model.addObject(BaseConstants.ERROR_MSG, getMessage("request.method.not.supported"));

		String ajaxHeader = request.getHeader("X-Requested-With");
		boolean isAjaxRequest = "XMLHttpRequest".equals(ajaxHeader);
		logger.info("ajaxHeader: " + ajaxHeader);
		logger.info("isAjaxRequest: " + isAjaxRequest);

		if(isAjaxRequest){
			model.setViewName(ViewNameConstants.AJAX_REQUEST_METHOD_NOT_SUPPORTED);
		}

		return model;
	}
	
	/**
	 * Handles error 404
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.PAGE_NOT_FOUND, method = RequestMethod.GET)
	public ModelAndView error404(HttpServletRequest request){
		ModelAndView model = new ModelAndView(ViewNameConstants.PAGE_NOT_FOUND);
		model.addObject(BaseConstants.ERROR_MSG, getMessage("page.not.found"));

		String ajaxHeader = request.getHeader("X-Requested-With");
		boolean isAjaxRequest = "XMLHttpRequest".equals(ajaxHeader);
		logger.info("ajaxHeader: " + ajaxHeader);
		logger.info("isAjaxRequest: " + isAjaxRequest);

		if(isAjaxRequest){
			model.setViewName(ViewNameConstants.AJAX_PAGE_NOT_FOUND);
		}
		return model;
	}
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	
	public User staffAutoritiesForSSO(User springUserBeanHome)  {
		
		List<SimpleGrantedAuthority> new_authorities = new ArrayList<SimpleGrantedAuthority>();	
		for (GrantedAuthority role  : springUserBeanHome.getAuthorities()) {
			new_authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		springUserBeanHome=  new User(springUserBeanHome.getUsername(), "NA", true, true, true, true, new_authorities);
		return springUserBeanHome;
	}
	
/*	@RequestMapping(value = ControllerConstants.INIT_ABOUT_US, method = RequestMethod.GET)
	public ModelAndView initAboutUs(){
		return  new ModelAndView(ViewNameConstants.ABOUT_US);
		
	}*/
	
}