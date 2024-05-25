package com.elitecore.sm.snmp.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.util.StringUtils;
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

import com.elitecore.core.commons.alert.AuthAlgorithm;
import com.elitecore.core.commons.alert.PrivacyAlgorithm;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPCommunityType;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPVersionType;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.snmp.validator.SnmpValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Jui Purohit Apr 26, 2016
 */

@Controller
public class SnmpController extends BaseController {

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	private SnmpValidator validator;

	@Autowired
	@Qualifier(value = "snmpService")
	private SnmpService snmpService;

	@Autowired
	private SNMPServerConfig snmpServerConfig;

	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}

	/**
	 * Will return to the jsp for Snmp Config
	 * 
	 * @param serverInstanceId
	 * @param REQUEST_ACTION_PARAM
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SNMP_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_SNMP_CONFIG, method = RequestMethod.POST)
	public ModelAndView initsnmpConfig(
			@RequestParam(value = "snmpserverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false, defaultValue = BaseConstants.UPDATE_INSTANCE_SUMMARY) String requestActionType)
			throws SMException {
		ModelAndView model = new ModelAndView(
				ViewNameConstants.UPDATE_SERVER_INSTANCE);
		if (!StringUtils.isEmpty(serverInstanceId)) {

			int iserverInstanceId = Integer.parseInt(serverInstanceId);
			ServerInstance serverInstance = serverInstanceService
					.getServerInstance(iserverInstanceId);
			model.addObject(FormBeanConstants.SNMP_CONFIG_FORM_BEAN,
					(SNMPServerConfig) SpringApplicationContext
							.getBean(SNMPServerConfig.class));
			model.addObject("instanceName", serverInstance.getName());
			model.addObject("lastUpdateTime", DateFormatter
					.formatDate(serverInstance.getLastUpdatedDate()));
			model.addObject("instancePort", serverInstance.getPort());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,
					requestActionType);
			model.addObject("serverInstanceId", serverInstanceId);
			model.addObject("serverInstanceIp", serverInstance.getServer()
					.getIpAddress());
			model.addObject("lblInstanceHost", serverInstance.getServer()
					.getIpAddress());
			model.addObject("communityEnum",
					Arrays.asList(SNMPCommunityType.values()));
			model.addObject("versionEnum",
					Arrays.asList(SNMPVersionType.values()));
			model.addObject("advanceEnum",
					Arrays.asList(TrueFalseEnum.values()));
			model.addObject("authAlgo",
					Arrays.asList(AuthAlgorithm.values()));
			model.addObject("privAlgo",
					Arrays.asList(PrivacyAlgorithm.values()));
			
			int serverTypeId=serverInstance.getServer().getServerType().getId();
			
			 List<String> generalEntityStatus=(List<String>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.GENERAL_TYPE_PROFILING);
			 for(String generalEntity:generalEntityStatus){
				 if(BaseConstants.POLICIES_CONFIGURATION.equals(generalEntity)){
					 logger.debug("Policy configuration is active");
					 model.addObject("isPolicyActive","ACTIVE");
				 }
				 if(BaseConstants.RELOAD_CACHE.equals(generalEntity)){
					 logger.debug("Reload Cache  is active");
					 model.addObject("isReloadCache","ACTIVE");
				 }
			 }

		}

		return model;
	}

	/**
	 * Add Snmp Server into Database
	 * 
	 * @param Snmp
	 *            server
	 * @param resultdr
	 * @param status
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('ADD_SNMP_SERVER')")
	@RequestMapping(value = ControllerConstants.CREATE_SNMP_SERVERLIST, method = RequestMethod.POST)
	@ResponseBody
	public String createSnmpServerList(
			@RequestParam(value = "SnmpType") String snmpType,
			@ModelAttribute(value = FormBeanConstants.SNMP_CONFIG_FORM_BEAN) SNMPServerConfig snmpServerConfig,//NOSONAR
			BindingResult result, HttpServletRequest request)
			throws SMException {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateSnmpServerConfigParam(snmpServerConfig, result, null,
				null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs
							.put(snmpType + "_" + error.getField(),
									getMessage(error.getCode() + "."
											+ error.getField()));
				} else {
					errorMsgs.put(snmpType + "_" + error.getField(),
							error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			snmpServerConfig.setCreatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));
			snmpServerConfig.setLastUpdatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));

			ResponseObject responseObject = snmpService
					.addSnmpServerList(snmpServerConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}

	/**
	 * Method will return the list of all Snmp Server by serverInstance id
	 * 
	 * @param sererInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Response body
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SNMP_CONFIG')")
	@RequestMapping(value = ControllerConstants.GET_SNMP_SERVERLIST, method = RequestMethod.POST)
	@ResponseBody
	public String getSnmpServerList(
			@RequestParam(value = "snmpserverInstanceId", required = false) String serverInstanceId,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord,
			HttpServletRequest request) {
		logger.debug(" getSnmpServerList in SnmpController " + serverInstanceId);

		int iServerInstanceId = 0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iServerInstanceId = Integer.parseInt(serverInstanceId);
		}
		long count = this.snmpService
				.getSnmpServertotalCount(iServerInstanceId);
		List<Map<String, Object>> rowList = null;
		if (count > 0) {
			rowList = this.snmpService.getSnmpServerPaginatedList(
					iServerInstanceId, eliteUtils.getStartIndex(limit,
							currentPage,
							eliteUtils.getTotalPagesCount(count, limit)),
					limit, sidx, sord);
		}
		logger.debug("<< getSnmpServerList in SnmpController ");
		return new JqGridData<Map<String, Object>>(
				eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
	}

	/**
	 * Change the Status of Snmp Server
	 * 
	 * @param snmpServerId
	 * @param snmpServerStatus
	 * @param serverInstanceId
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_SERVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_SERVER_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public String updateSnmpServerStatus(
			@RequestParam(value = "SnmpServerId", required = true) String snmpServerId,
			@RequestParam(value = "snmpServerStatus", required = true) String snmpServerStatus,
			@RequestParam(value = "snmpServerInstanceId", required = true) String serverInstanceId)
			throws CloneNotSupportedException {

		int iServerInstanceId = 0;
		int iSnmpServerId = 0;
		if (!StringUtils.isEmpty(snmpServerId)) {
			iSnmpServerId = Integer.parseInt(snmpServerId);
		}
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iServerInstanceId = Integer.parseInt(serverInstanceId);
		}
		ResponseObject responseObject = snmpService.changeSnmpServerStatus(
				iSnmpServerId, snmpServerStatus, iServerInstanceId);
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Update Snmp Server into Database
	 * 
	 * @param Snmp
	 *            server
	 * @param resultdr
	 * @param status
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_SERVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_SERVERLIST, method = RequestMethod.POST)
	@ResponseBody
	public String updateSnmpServerList(
			@RequestParam(value = "SnmpType") String snmpType,
			@ModelAttribute(value = FormBeanConstants.SNMP_CONFIG_FORM_BEAN) SNMPServerConfig snmpServerConfig,//NOSONAR
			BindingResult result, HttpServletRequest request)
			throws SMException {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateSnmpServerConfigParam(snmpServerConfig, result, null,
				null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs
							.put(snmpType + "_" + error.getField(),
									getMessage(error.getCode() + "."
											+ error.getField()));
				} else {
					errorMsgs.put(snmpType + "_" + error.getField(),
							error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			snmpServerConfig.setLastUpdatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));

			ResponseObject responseObject = snmpService
					.updateSnmpServerDetail(snmpServerConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}

	/**
	 * Method will delete Snmp Server
	 * 
	 * @param snmpServerId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SNMP_SERVER')")
	@RequestMapping(value = ControllerConstants.DELETE_SNMP_SERVER, method = RequestMethod.POST)
	@ResponseBody
	public String deleteSnmpServer(
			@RequestParam(value = "delete_snmpServerId", required = true) String deletesnmpServerId,
			HttpServletRequest request) throws CloneNotSupportedException {
		int isnmpServerId = 0;
		if (!StringUtils.isEmpty(deletesnmpServerId)) {
			isnmpServerId = Integer.parseInt(deletesnmpServerId);
		}
		ResponseObject responseObject = snmpService.deleteSnmpServer(
				isnmpServerId, eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
	
	/**
	 * Add client to database
	 * @param snmpClientType
	 * @param snmpServerConfig
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.CREATE_SNMP_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String createSnmpClient(
			@RequestParam(value = "SnmpClientType") String snmpClientType,
			@ModelAttribute(value = FormBeanConstants.SNMP_CONFIG_FORM_BEAN) SNMPServerConfig snmpServerConfig,//NOSONAR
			BindingResult result, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateSnmpClientConfigParam(snmpServerConfig, result, null,
				null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs
							.put(snmpClientType + "_" + error.getField(),
									getMessage(error.getCode() + "."
											+ error.getField()));
				} else {
					errorMsgs.put(snmpClientType + "_" + error.getField(),
							error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			snmpServerConfig.setCreatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));
			snmpServerConfig.setLastUpdatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));
			ResponseObject responseObject = snmpService
					.addSnmpClient(snmpServerConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	/**
	 * Add alerts to client
	 * @param selectedAlertIdList
	 * @param clientId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.ADD_ALERTS_TO_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String addAlertstoClient(
			@RequestParam(value = "selectedAlertIdList") String selectedAlertIdList,
			@RequestParam(value = "clientId") String clientId,
			HttpServletRequest request) {

		AjaxResponse ajaxResponse;
		int iclientId = 0;
		if (!StringUtils.isEmpty(clientId)) {
			iclientId = Integer.parseInt(clientId);
		}

		ResponseObject responseObject = snmpService.addSnmpAlertsToClient(
				iclientId, selectedAlertIdList,
				eliteUtils.getLoggedInStaffId(request));

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();

	}
	
	 /**
	  * Update Alerts mapped to client
	  * @param selectedAlertIdList
	  * @param clientId
	  * @param request
	  * @return
	  */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_ALERTS_TO_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String updateAlertstoClient(
			@RequestParam(value = "selectedAlertIdList") String selectedAlertIdList,
			@RequestParam(value = "clientId") String clientId,
			HttpServletRequest request) {

		AjaxResponse ajaxResponse;
		int iclientId = 0;
		if (!StringUtils.isEmpty(clientId)) {
			iclientId = Integer.parseInt(clientId);
		}

		ResponseObject responseObject = snmpService.updateSnmpAlertsToClient(
				iclientId, selectedAlertIdList,
				eliteUtils.getLoggedInStaffId(request));

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();

	}

	/**
	 * Get Snmp Client List by serverInstance Id
	 * @param serverInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SNMP_CONFIG')")
	@RequestMapping(value = ControllerConstants.GET_SNMP_CLIENTLIST, method = RequestMethod.POST)
	@ResponseBody
	public String getSnmpClientList(
			@RequestParam(value = "snmpserverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord,
			HttpServletRequest request) {
		logger.debug(" getSnmpClientList in SnmpController " + serverInstanceId);

		int iServerInstanceId = 0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iServerInstanceId = Integer.parseInt(serverInstanceId);
		}
		long count = this.snmpService
				.getSnmpClienttotalCount(iServerInstanceId);
		List<Map<String, Object>> rowList = null;
		if (count > 0) {
			rowList = this.snmpService.getSnmpClientPaginatedList(
					iServerInstanceId, eliteUtils.getStartIndex( limit,
							currentPage,
							eliteUtils.getTotalPagesCount(count, limit)),
					limit, sidx, sord);
		}
		logger.debug("<< getSnmpClientList in SnmpController " + rowList);
		return new JqGridData<Map<String, Object>>(
				eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
	}

	/**
	 * Update Snmp Client
	 * @param snmpType
	 * @param snmpClientConfig
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String updateSnmpClient(
			@RequestParam(value = "SnmpType") String snmpType,
			@ModelAttribute(value = FormBeanConstants.SNMP_CONFIG_FORM_BEAN) SNMPServerConfig snmpClientConfig,//NOSONAR
			BindingResult result, HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateSnmpClientConfigParam(snmpClientConfig, result, null,
				null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs
							.put(snmpType + "_" + error.getField(),
									getMessage(error.getCode() + "."
											+ error.getField()));
				} else {
					errorMsgs.put(snmpType + "_" + error.getField(),
							error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			snmpClientConfig.setLastUpdatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));

			ResponseObject responseObject = snmpService
					.updateSnmpClientDetail(snmpClientConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	/**
	 * Alerts related mapped services with client for client popup
	 * @param clientId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.ADD_ALERTS_TO_UPDATE_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String fetchAlertsForUpdateClient(
			@RequestParam(value = "clientId") String clientId,
			HttpServletRequest request) {

		AjaxResponse ajaxResponse;
		int iclientId = 0;
		if (!StringUtils.isEmpty(clientId)) {
			iclientId = Integer.parseInt(clientId);
		}

		ResponseObject responseObject = snmpService
				.fetchAlertsForUpdateClient(iclientId);

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();

	}
	
	/**
	 * Get alert list from db
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SNMP_CONFIG')")
	@RequestMapping(value = ControllerConstants.GET_SNMP_ALERTLIST, method = RequestMethod.POST)
	@ResponseBody
	public String getSnmpAlertList(@RequestParam(value = "snmpserverInstanceId") String snmpserverInstanceId,
			HttpServletRequest request) {
		logger.debug(" getSnmpAlertList in SnmpController ");
		AjaxResponse ajaxResponse;
		int iServerInstanceId=0;		
		if (!StringUtils.isEmpty(snmpserverInstanceId)) {
			iServerInstanceId = Integer.parseInt(snmpserverInstanceId);
		}	
		//this.snmpService.getSnmpAlerttotalCount();
		ResponseObject responseObject = this.snmpService.getSnmpAlertPaginatedList(iServerInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	
	/**
	 * Get the service threshold for service for alerts
	 * @param serverInstanceId
	 * @param alertId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_SERVICE_THRESHOLD')")
	@RequestMapping(value = ControllerConstants.GET_SNMP_ALERTLIST_SERVICETHRESHOLD, method = RequestMethod.POST)
	@ResponseBody
	public String getSnmpAlertListServiceThreshold(
	@RequestParam(value = "serverInstanceId") String serverInstanceId,
			@RequestParam(value = "alertId") String alertId,
			HttpServletRequest request) {
		logger.debug(" getSnmpAlertListServiceThreshold in SnmpController "
				+ "serverInstanceId:" + serverInstanceId + "----" + "alertId:"
				+ alertId);

		AjaxResponse ajaxResponse;
		int iServerInstanceId = 0;
		int ialertId = 0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iServerInstanceId = Integer.parseInt(serverInstanceId);
		}
		if (!StringUtils.isEmpty(alertId)) {
			ialertId = Integer.parseInt(alertId);
		}
		ResponseObject responseObject = snmpService
				.getServiceWithServiceThreshold(iServerInstanceId, ialertId);

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Update Snmp Client Status
	 * 
	 * @param snmpClientId
	 * @param snmpClientStatus
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_CLIENT_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public String updateSnmpClientStatus(
			@RequestParam(value = "snmpClientId", required = true) String snmpClientId,
			@RequestParam(value = "snmpClientStatus", required = true) String snmpClientStatus)
			throws CloneNotSupportedException {

		int iSnmpClientId = 0;
		if (!StringUtils.isEmpty(snmpClientId)) {
			iSnmpClientId = Integer.parseInt(snmpClientId);
		}

		ResponseObject responseObject = snmpService.changeSnmpClientStatus(
				iSnmpClientId, snmpClientStatus);
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Delete Snmp Client
	 * 
	 * @param deleteSnmpClientId
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SNMP_CLIENT')")
	@RequestMapping(value = ControllerConstants.DELETE_SNMP_CLIENT, method = RequestMethod.POST)
	@ResponseBody
	public String deleteSnmpClient(
			@RequestParam(value = "delete_snmpClientId", required = true) String deleteSnmpClientId,
			HttpServletRequest request) throws CloneNotSupportedException {
		int isnmpClientId = 0;
		if (!StringUtils.isEmpty(deleteSnmpClientId)) {
			isnmpClientId = Integer.parseInt(deleteSnmpClientId);
		}
		ResponseObject responseObject = snmpService.deleteSnmpClient(
				isnmpClientId, eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Get the Alert List mapped with client
	 * 
	 * @param snmpClientId
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SNMP_CONFIG')")
	@RequestMapping(value = ControllerConstants.GET_CONFIGURED_ALERT_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String getConfiguredAlertList(
			@RequestParam(value = "selected_snmpClientId", required = true) String snmpClientId,
			HttpServletRequest request) throws CloneNotSupportedException {
		int isnmpClientId = 0;
		if (!StringUtils.isEmpty(snmpClientId)) {
			isnmpClientId = Integer.parseInt(snmpClientId);
		}
		ResponseObject responseObject = snmpService
				.getConfiguredAlertsByClientId(isnmpClientId);
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Update service threshold value
	 * 
	 * @param alertId
	 * @param serverInstanceId
	 * @param alertsvcList
	 * @param selectedAlertType
	 * @param configAlertThreshold
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException
	 */

	@PreAuthorize("hasAnyAuthority('ADD_SERVICE_THRESHOLD')")
	@RequestMapping(value = ControllerConstants.UPDATE_SERVICE_THRESHOLD, method = RequestMethod.POST)
	@ResponseBody
	public String updateServiceThreshold(
			@RequestParam(value = "alertId", required = true) String alertId,
			@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = "alertsvcList", required = true) String alertsvcList,
			@RequestParam(value = "selectedAlertType", required = true) String selectedAlertType,
			HttpServletRequest request) throws CloneNotSupportedException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateServiceThresholdParam(importErrorList, alertsvcList,
				selectedAlertType);
		if (!importErrorList.isEmpty()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();

			for (ImportValidationErrors error : importErrorList) {
				if (error.getErrorMessage().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getPropertyName(),
							error.getErrorMessage());
				} else {
					errorMsgs.put(error.getPropertyName(),
							error.getErrorMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		} else {
			int ialertId = 0;
			
			int iserverInstanceId = 0;
			if (!StringUtils.isEmpty(alertId)) {
				ialertId = Integer.parseInt(alertId);
			}
			if (!StringUtils.isEmpty(serverInstanceId)) {
				iserverInstanceId = Integer.parseInt(serverInstanceId);
			}
			
			ResponseObject responseObject = snmpService.updateServiceThreshold(
					iserverInstanceId, ialertId, alertsvcList,
					selectedAlertType);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}

	/**
	 * Update the alert details in master table for alert
	 * 
	 * @param alertDetail
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException
	 */

	@PreAuthorize("hasAnyAuthority('EDIT_ALERT_DETAILS')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_ALERT, method = RequestMethod.POST)
	@ResponseBody
	public String updateSnmpAlert(
			@ModelAttribute(value = FormBeanConstants.SNMP_ALERT_FORM_BEAN) SNMPAlert snmpAlert,//NOSONAR
			BindingResult result, HttpServletRequest request) throws CloneNotSupportedException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		validator.validateAlertDetailsParam(snmpAlert, result, null,
				null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(
						BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs
							.put( "update_" + error.getField(),
									getMessage(error.getCode() + "."
											+ error.getField()));
				} else {
					errorMsgs.put( "update_" + error.getField(),
							error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			snmpAlert.setLastUpdatedByStaffId(eliteUtils
					.getLoggedInStaffId(request));
		ResponseObject responseObject = snmpService
				.updateSnmpAlertDetails(snmpAlert);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		// }
		
	}
		return ajaxResponse.toString();
}
}