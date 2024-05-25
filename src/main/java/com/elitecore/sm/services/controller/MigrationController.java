package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.migration.validator.MigrationValidator;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.services.model.MigrationStatusEnum;
import com.elitecore.sm.services.service.MigrationAsyncProcessService;
import com.elitecore.sm.services.service.MigrationService;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.EliteExceptionUtils;
import com.elitecore.sm.util.MapCache;

@Controller
public class MigrationController extends BaseController {

	@Autowired
	private MigrationService migrationService;

	@Autowired
	SystemParameterService systemParamService;
	
	@Autowired
	MigrationAsyncProcessService migrationAsyncProcessService;

	@Autowired
	MigrationValidator validator;
	
	@Autowired
	ServerService serverService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	/*@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}*/
	
	/**
	 * Method will redirect to Migration Page
	 * 
	 * @param requestActionType
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_MIGRATION, method = RequestMethod.POST)
	public ModelAndView initMigration(@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false) String requestActionType) throws MigrationSMException { 
		ModelAndView model = new ModelAndView(ViewNameConstants.MIGRATION_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		@SuppressWarnings("unchecked")
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		return model;
	}
	
	/**
	 * Method will start re-processing for all instances with status re-process.
	 * @return
	 * 
	 */
	@RequestMapping(value = ControllerConstants.REPROCESS_MIGRATION, method = RequestMethod.POST)
	@ResponseBody 
	public String reProcessMigration() throws MigrationSMException{
		logger.debug("Migration reprocessing start.");
		ResponseObject responseObject = migrationAsyncProcessService.doMigration(MigrationStatusEnum.REPROCESS) ;
		AjaxResponse ajaxResponse =  eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will do migration as async process for all open status instances.
	 */
	@RequestMapping(value = ControllerConstants.DO_MIGRATION, method = RequestMethod.POST)
	@ResponseBody 
	public String doMigration() throws MigrationSMException{
		logger.debug("Migration process start for open status instances.");
		ResponseObject responseObject = migrationAsyncProcessService.doMigration(MigrationStatusEnum.OPEN);
		AjaxResponse ajaxResponse =  eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Validate server details such as utility running or not on IP
	 * 
	 * @param serverIp
	 * @param utilityPort
	 * @param serverType
	 * @param request
	 * @return String
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(value = ControllerConstants.SERVER_MIGRATION_UTILITY_CHECK, method = RequestMethod.POST)
	@ResponseBody
	public String serverMigrationUtilityCheck(@RequestParam(value = "serverIp", required = true) String serverIp, @RequestParam(
			value = "utilityPort", required = true) String utilityPort, @RequestParam(value = "serverType", required = true) String serverType) throws CloneNotSupportedException {

		int iUtilityPort = 0;
		int iServerType = 0;
		if (!StringUtils.isEmpty(utilityPort)) {
			iUtilityPort = Integer.parseInt(utilityPort);
		}
		if (!StringUtils.isEmpty(serverType)) {
			iServerType = Integer.parseInt(serverType);
		}
		AjaxResponse ajaxResponse = new AjaxResponse();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateServerDetails(importErrorList, serverIp, iUtilityPort);
		if (!importErrorList.isEmpty()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();

			for (ImportValidationErrors error : importErrorList) {
				if (error.getErrorMessage().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getPropertyName(), error.getErrorMessage());
				} else {
					errorMsgs.put(error.getPropertyName(), error.getErrorMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			ResponseObject responseObject = migrationService.checkServerAvailability(serverIp, iServerType, iUtilityPort);

			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		logger.debug("serverIp::" + serverIp);
		logger.debug("utilityPort::" + utilityPort);
		logger.debug("serverType::" + serverType);

		return ajaxResponse.toString();
	}

	/**
	 * Validate ServerInstance details such as port running or not, version
	 * check, scriptFile present or not and SI with same IP & port already
	 * present in DB etc.
	 * 
	 * @param serverIp
	 * @param utilityPort
	 * @param serverType
	 * @param namingPrefix
	 * @param port
	 * @param scriptName
	 * @param serverInstanceCount
	 * @param request
	 * @return String
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(value = ControllerConstants.SERVERINSTANCE_MIGRATIONCHECK, method = RequestMethod.POST)
	@ResponseBody
	public String serverInstanceMigrationCheck(@RequestParam(value = "serverIp", required = true) String serverIp, @RequestParam(
			value = "utilityPort", required = true) String utilityPort, @RequestParam(value = "serverType", required = true) String serverType,
			@RequestParam(value = "namingPrefix", required = true) String namingPrefix, @RequestParam(value = "port", required = true) String port,
			@RequestParam(value = "scriptName", required = true) String scriptName,
			@RequestParam(value = "serverInstanceCount", required = true) String serverInstanceCount)
			throws CloneNotSupportedException {
		AjaxResponse ajaxResponse = new AjaxResponse();

		int iPort = 0;
		int iserverTypeId = 0;
		int iServerInstanceCount = 0;
		if (!StringUtils.isEmpty(port)) {
			iPort = Integer.parseInt(port);
		}
		int iUtilityPort = 0;
		if (!StringUtils.isEmpty(utilityPort)) {
			iUtilityPort = Integer.parseInt(utilityPort);
		}
		if (!StringUtils.isEmpty(serverType)) {
			iserverTypeId = Integer.parseInt(serverType);
		}
		if (!StringUtils.isEmpty(serverInstanceCount)) {
			iServerInstanceCount = Integer.parseInt(serverInstanceCount);
		}
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateServerInstanceDetails(importErrorList, namingPrefix, iPort, scriptName, iServerInstanceCount);
		if (!importErrorList.isEmpty()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();

			for (ImportValidationErrors error : importErrorList) {
				if (error.getErrorMessage().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getPropertyName(), error.getErrorMessage());
				} else {
					errorMsgs.put(error.getPropertyName(), error.getErrorMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {

			ResponseObject responseObject = migrationService.checkServerInstanceDetails(serverIp, iserverTypeId, iUtilityPort, iPort, scriptName);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

			logger.debug("serverIp::" + serverIp);
			logger.debug("namingPrefix::" + namingPrefix);
			logger.debug("port::" + port);
			logger.debug("scriptName::" + scriptName);
			logger.debug("serverInstanceCount::" + serverInstanceCount);
		}
		return ajaxResponse.toString();
	}

	
	/**
	 * Handles Migration SM Exception.
	 * @param exception
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@ExceptionHandler({MigrationSMException.class})
	@ResponseBody 
	public String handleMigrationSMException(MigrationSMException exception) {
		MapCache.addConfigObject(BaseConstants.IS_MIGRATION_IN_PROCESS, false);
		logger.error(exception.getMessage(), exception);
		logger.trace(EliteExceptionUtils.getRootCauseStackTraceAsString(exception));
		return eliteUtils.convertToAjaxResponse(exception.getResponseObject()).toString();
	}
	
	/**
	 * Validate serverInstance details and fetch services list
	 * @param migrationTrackDetails
	 * @param result
	 * @param request
	 * @return String
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.VALIDATE_MIGRATIONDETAILS_AND_FETCH_ALL_SERVICE_LIST_BY_PORT, method = RequestMethod.POST)
	@ResponseBody 
	public String ValidateMigrationDetailsAndfetchAllServiceListByPort(@ModelAttribute MigrationTrackDetails migrationTrackDetails,//NOSONAR
			BindingResult result, HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject=new ResponseObject();
		validator.validateMigrationTrackDetails(migrationTrackDetails, result, null, null, false);
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			logger.info("Failed to save migration track details.");
		} else {
			logger.info("Inside  fetchAllServiceListByPort");
			 responseObject = migrationService.getAllServicesList(migrationTrackDetails);
		}
			ajaxResponse =  eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * While Edit, fetch services list for specific port on specific ip
	 * @param serverId
	 * @param port
	 * @return String
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.FETCH_ALL_SERVICE_LIST_BY_PORT, method = RequestMethod.POST)
	@ResponseBody 
	public String fetchAllServiceListByPort(@RequestParam(value = "serverId", required = true) String serverId, 
		 @RequestParam(value = "port", required = true) String port) throws SMException {
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject;
	
			logger.info("Inside fetchAllServiceListByPort");
			
		
	int iserverId=0;
	int iPort=0;
	if(!StringUtils.isEmpty(serverId)){
		iserverId=Integer.parseInt(serverId);
	}
	
	if(!StringUtils.isEmpty(port)){
		iPort=Integer.parseInt(port);
	}
			logger.info("Inside fetchAllServiceListByPort");
			 responseObject = migrationService.getAllServicesListForUpdate(iserverId, iPort);
			ajaxResponse =  eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
}
