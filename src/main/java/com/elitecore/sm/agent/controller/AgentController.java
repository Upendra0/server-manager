package com.elitecore.sm.agent.controller;

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

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.agent.service.AgentService;
import com.elitecore.sm.agent.validator.AgentValidator;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.service.filerenameconfig.validator.ServiceFileRenameConfigValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author avani.panchal
 *
 */
@Controller
public class AgentController extends BaseController {

	@Autowired
	@Qualifier(value = "agentService")
	private AgentService agentService;

	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	private AgentValidator validator;
	
	@Autowired
	private ServicesService servicesService;

	@Autowired
	private ServiceFileRenameConfigValidator serviceFileRenameConfigValidator;
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


	
	/**
	 * to set view model for System Agent
	 * @param serverInstanceId
	 * @param requestActionType
	 * @return
	 * @throws SMException
	 */

	@PreAuthorize("hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.INIT_SYSTEM_AGENT_CONFIG, method = RequestMethod.POST)
	public ModelAndView initSystemAgentConfig(
			@RequestParam(value = "agent_serverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false, defaultValue = BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG) String requestActionType)
			throws SMException {
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		logger.debug("ServerInstance Id in initSystemAgentConfig::" + serverInstanceId);
		if (!StringUtils.isEmpty(serverInstanceId)) {
			
			
			int iserverInstanceId = Integer.parseInt(serverInstanceId);
			
			ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
			List<Service> serviceList = servicesService.getServicesforServerInstance(iserverInstanceId);
			
			List<AgentType> agentTypeList = (List<AgentType>) eliteUtils.fetchProfileEntityStatusFromCache(serverInstance.getServer().getServerType()
					.getId(), BaseConstants.AGENT_TYPE);
			model.addObject("instanceName", serverInstance.getName());
			model.addObject("lastUpdateTime", DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
			model.addObject("instancePort", serverInstance.getPort());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			model.addObject("serverInstanceId", serverInstanceId);
			model.addObject("serverInstanceIp", serverInstance.getServer().getIpAddress());
			model.addObject("lblInstanceHost", serverInstance.getServer().getIpAddress());
			model.addObject("agentList", agentTypeList);
			model.addObject("serviceList", serviceList);
			model.addObject("isPolicyActive","ACTIVE");
			model.addObject("status",serverInstance.getStatus());
		}

		return model;
	}


	/**
	 * Fetch Agent for server instance and service summary
	 * 
	 * @param serverInstanceId
	 * @param serviceId
	 * @param isServerInstanceSummary
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')")

	@RequestMapping(value = ControllerConstants.GET_AGENT_LIST_SUMMARY, method = RequestMethod.POST)
	@ResponseBody
	public String getAgentListForSummary(@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId, @RequestParam(
			value = "serviceId", required = false) String serviceId,
			@RequestParam(value = "isServerInstanceSummary", required = true) String isServerInstanceSummary, @RequestParam(value = "rows",
					defaultValue = "10") int limit, @RequestParam(value = "page", defaultValue = "1") int currentPage, @RequestParam(value = "sidx",
					required = true) String sidx, @RequestParam(value = "sord", required = true) String sord) {
		int iserverInstanceId = 0;
		int iServiceId = 0;
		int serverTypeId=0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iserverInstanceId = Integer.parseInt(serverInstanceId);
			ServerInstance serverInstance=serverInstanceService.getServerInstance(iserverInstanceId);
			if(serverInstance!=null){
			 serverTypeId=serverInstance.getServer().getServerType().getId();
			}
		}
		
		if (!StringUtils.isEmpty(serviceId)) {
			iServiceId = Integer.parseInt(serviceId);
		}
		List<AgentType> agentTypeList=(List<AgentType>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.AGENT_TYPE);	
		logger.debug("Agent Type List found from Cache is " + agentTypeList );
		long count = this.agentService.getTotalAgentCount(iserverInstanceId,agentTypeList);

		logger.info("count: " + count);

		List<Map<String, Object>> rowList = null;
		if (count > 0)
			rowList = this.agentService.getAgentPaginatedList(iserverInstanceId, iServiceId, isServerInstanceSummary,
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,agentTypeList);

		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}

	/**
	 * On selection of drop down, specific agent jsp included
	 * 
	 * @param serverInstanceId
	 * @param systemAgentTypeId
	 * @param requestActionType
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')")
	@SuppressWarnings("unchecked")	
	@RequestMapping(value = ControllerConstants.SPECIFIC_SYSTEM_AGENT_CONFIG, method = RequestMethod.POST)
	public ModelAndView specificSystemAgentConfig(
			@RequestParam(value = "agent_server_Instance_Id", required = true) String serverInstanceId,
			@RequestParam(value = "systemAgentTypeId", required = true) String systemAgentTypeId,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false, defaultValue = BaseConstants.UPDATE_SYSTEM_AGENT_CONFIG) String requestActionType)
			throws SMException {
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		logger.debug("ServerInstance Id in initSystemAgentConfig::" + serverInstanceId);
		int iserverInstanceId = 0;
		int iagentTypeId = 0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iserverInstanceId = Integer.parseInt(serverInstanceId);
		}
		if (!StringUtils.isEmpty(systemAgentTypeId)) {
			iagentTypeId = Integer.parseInt(systemAgentTypeId);
		}
		if(iserverInstanceId!=0 && iagentTypeId!=0){
		ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
		//gets all services on the agent
		ResponseObject agentObj = agentService.getAgentByServerInstanceIdAndAgentTypeID(iserverInstanceId, iagentTypeId);
		Agent agent = (Agent) agentObj.getObject();
		
		//all services of instance
		List<Service> serviceList = servicesService.getServicesforServerInstance(iserverInstanceId);
		if(agent != null) {
			logger.debug("Agent Id in initSystemAgentConfig::" + agent.getId());
			model.addObject(FormBeanConstants.AGENT_FORM_BEAN, agent);
			model.addObject(FormBeanConstants.FILE_RENAME_AGENT_FORM_BEAN, agent);
			model.addObject("selectedAgentType", agent.getAgentType().getAlias());
			model.addObject("selectedAgentTypeId", agent.getAgentType().getId());
			model.addObject("agentId", agent.getId());
		}
		
		model.addObject(FormBeanConstants.FILE_RENAME_AGENT_ADD_SERVICE_FORM_BEAN, (ServiceFileRenameConfig) SpringApplicationContext
				.getBean(ServiceFileRenameConfig.class));		
		
		List<AgentType> agentTypeList = (List<AgentType>) eliteUtils.fetchProfileEntityStatusFromCache(serverInstance.getServer().getServerType()
				.getId(), BaseConstants.AGENT_TYPE);

		model.addObject("instanceName", serverInstance.getName());
		model.addObject("lastUpdateTime", DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		model.addObject("instancePort", serverInstance.getPort());
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		model.addObject("serverInstanceId", serverInstanceId);
		model.addObject("serverInstanceIp", serverInstance.getServer().getIpAddress());
		model.addObject("lblInstanceHost", serverInstance.getServer().getIpAddress());
		model.addObject("agentList", agentTypeList);
		
		model.addObject("serviceList", serviceList);
		model.addObject("systemAgentTypeId", systemAgentTypeId);
		model.addObject("positionEnum", java.util.Arrays.asList(PositionEnum.values()));
		model.addObject("stateEnum", java.util.Arrays.asList(StateEnum.values()));
		model.addObject("trueFalseEnum",java.util.Arrays.asList(TrueFalseEnum.values()));
		
		}
		return model;
	}
	
	/**
	 * Update Agent Details
	 * 
	 * @param packetStatasticagent
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_PACKET_STATASTIC_AGENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_PACKET_STATASTIC_AGENT, method = RequestMethod.POST)
	@ResponseBody
	public String updatePacketStatasticAgent(

	@ModelAttribute(value = FormBeanConstants.AGENT_FORM_BEAN) PacketStatisticsAgent packetStatasticagent, BindingResult result,//NOSONAR
			HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateAgentParam(packetStatasticagent, result, null, null, false);
		if (result.hasErrors()) {
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
			Map<String, String> errorMsgs = new HashMap<>();

			for (FieldError error : result.getFieldErrors()) {
				if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
					errorMsgs.put(error.getField(), getMessage(error.getCode() + "." + error.getField()));
				} else {
					errorMsgs.put(error.getField(), error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			packetStatasticagent.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));

			ResponseObject responseObject = agentService.updatePacketStatasticDetail(packetStatasticagent);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}

	/**
	 * Method will return the list of all Services by Agent id
	 * 
	 * @param serverInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return ResponseBody
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PACKET_STATASTIC_AGENT')")
	@RequestMapping(value = ControllerConstants.GET_AGENT_SERVICELIST, method = RequestMethod.POST)
	@ResponseBody
	public String getAgentServicelist(@RequestParam(value = "agentId", required = false) String agentId, @RequestParam(value = "rows",
			defaultValue = "10") int limit, @RequestParam(value = "page", defaultValue = "1") int currentPage, @RequestParam(value = "sidx",
			required = false) String sidx, @RequestParam(value = "sord", required = false) String sord) {
		logger.debug(" getAgentServicelist in AgentController " + agentId);

		int iAgentId = 0;
		if (!StringUtils.isEmpty(agentId)) {
			iAgentId = Integer.parseInt(agentId);
		}
		long count = this.agentService.getPacketStatasticServiceListtotalCount(iAgentId);
		List<Map<String, Object>> rowList = null;
		if (count > 0) {
			rowList = this.agentService.getPacketStatasticServicePaginatedList(iAgentId,
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		logger.debug("<< getAgentServicelist in AgentController ");
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();

	}

	/**
	 * Load Agent run information
	 * 
	 * @param serverInstanceId
	 * @param agentType
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.LOAD_AGENT_INFORMATION, method = RequestMethod.POST)
	@ResponseBody
	public String loadAgentInformation(@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId, @RequestParam(
			value = "agentType", required = false) String agentType) {
		int iserverInstanceId = 0;

		if (!StringUtils.isEmpty(serverInstanceId)) {
			iserverInstanceId = Integer.parseInt(serverInstanceId);
		}
		logger.debug("serverInstanceId::*::"+serverInstanceId);
		ResponseObject responseObject = agentService.loadAgentInfomation(iserverInstanceId, agentType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();

	}

	/**
	 * Update Packet statastic Agent Status
	 * 
	 * @param svcAgentId
	 * @param svcAgentStatus
	 * @return
	 * @throws CloneNotSupportedException
	 */

	@PreAuthorize("hasAnyAuthority('EDIT_PACKET_STATASTIC_AGENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_PACKET_STATASTIC_AGENT_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public String updatePacketStatasticAgentStatus(@RequestParam(value = "svcAgentId", required = true) String svcAgentId, @RequestParam(
			value = "svcAgentStatus", required = true) String svcAgentStatus) throws CloneNotSupportedException {

		int iSvcAgentId = 0;
		if (!StringUtils.isEmpty(svcAgentId)) {
			iSvcAgentId = Integer.parseInt(svcAgentId);
		}

		ResponseObject responseObject = agentService.changePacketStatasticStatus(iSvcAgentId, svcAgentStatus);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	/**
	 * Method will update Service status
	 * 
	 * @param DriverId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException
	 */
	@PreAuthorize("hasAnyAuthority('SYSTEM_AGENT_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.UPDATE_AGENT_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public String updateAgentStatus(@RequestParam(value = "agentId", required = true) String agentId, @RequestParam(value = "agentStatus",
			required = true) String agentStatus) throws CloneNotSupportedException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		if (!StringUtils.isEmpty(agentId)) {
			int iAgentId = Integer.parseInt(agentId);

			ResponseObject responseObject = agentService.updateAgentStatus(iAgentId, agentStatus);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	/**
	 *  Method will fetch all services configured with file rename agent. 
	 * @param service
	 * @param result
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return JsonAjax response
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_FILE_RENAME_AGENT_DETAILS')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_LIST_FOR_FILERENAME_AGENT, method = RequestMethod.POST)
	@ResponseBody public  String getServiceListForFileRenameAgent(
				@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId,
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord
				) {
		
		long count =  this.agentService.getFileRenameAgentDetailsCount();
		logger.debug(">> getFileRenameAgentDetailsCount in AgentController count " + count); 
		List<ServiceFileRenameConfig> resultList = new ArrayList<>();
		if(count > 0){
			resultList = this.agentService.getPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
															   limit, sidx,sord,serverInstanceId);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (ServiceFileRenameConfig serviceFileRenameConfigList : resultList) {
				row = new HashMap<>();

				Service service = serviceFileRenameConfigList.getService();
				
				row.put("serviceFileRenameConfigId", serviceFileRenameConfigList.getId());
				row.put("extAfterRename", serviceFileRenameConfigList.getExtAfterRename());
				row.put("destinationPath", serviceFileRenameConfigList.getDestinationPath());
				row.put("fileExtensitonList", serviceFileRenameConfigList.getFileExtensitonList());			
				row.put("serviceName", service.getName());	
				row.put("serviceId", service.getId());	
				row.put("charRename", "charRename");
				row.put("edit", serviceFileRenameConfigList);				
				row.put("charRenameOperationEnable", serviceFileRenameConfigList.isCharRenameOperationEnable());
				rowList.add(row);
			}
		}

		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();
	}

	/**
	 * Method will add service configuration to file rename agent.
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('ADD_SERVICE_TO_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.ADD_SERVICE_TO_FILERENAME_AGENT, method = RequestMethod.POST)
	@ResponseBody public  String addServiceToFileRenamingAgent(
			@ModelAttribute (value = FormBeanConstants.FILE_RENAME_AGENT_ADD_SERVICE_FORM_BEAN) ServiceFileRenameConfig serviceFileRenameConfig,//NOSONAR
			BindingResult result, HttpServletRequest request)  {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		serviceFileRenameConfigValidator.validateServiceFileRenameAgentParam(serviceFileRenameConfig, result, null, null, false);
	
		if (result.hasErrors()) {
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			  logger.info("Failed to validate service file rename configuration details.");
		}
		else{
			logger.info(">> Adding service to File rename agent :" ); 
			serviceFileRenameConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request)); 		
			ResponseObject responseObject = agentService.addServiceToFileRenamingAgent(serviceFileRenameConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will update service configuration of file rename agent.
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_TO_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_SERVICE_TO_FILERENAME_AGENT, method = RequestMethod.POST)
	@ResponseBody public  String updateServiceToFileRenamingAgent(
			@ModelAttribute (value = FormBeanConstants.FILE_RENAME_AGENT_ADD_SERVICE_FORM_BEAN) ServiceFileRenameConfig serviceFileRenameConfig,//NOSONAR
			BindingResult result, HttpServletRequest request)  {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		serviceFileRenameConfigValidator.validateServiceFileRenameAgentParam(serviceFileRenameConfig, result, null, null, false);
		
		if(result.hasErrors()){
			  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			  logger.info("Failed to validate service file rename configuration details.");
		}
		else{
			logger.debug(">> updateServiceToFileRenamingAgent in AgentController " ); 
			serviceFileRenameConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject = agentService.updateServiceToFileRenamingAgent(serviceFileRenameConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}

	
	/**
	 * Update File Rename Agent Details(initial delay, execution interval)
	 * 
	 * @param fileRenameAgent
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_FILE_RENAME_AGENT_DETAILS')")
	@RequestMapping(value = ControllerConstants.UPDATE_FILE_RENAME_AGENT_DETAILS, method = RequestMethod.POST)
	@ResponseBody
	public String updateFileRenameAgentDetails(

	@ModelAttribute(value = FormBeanConstants.FILE_RENAME_AGENT_FORM_BEAN) FileRenameAgent fileRenameAgent, BindingResult result, //NOSONAR
	HttpServletRequest request) {

		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateAgentParam(fileRenameAgent, result, null, null, false);
		
		if (result.hasErrors()) {
			if (result.hasErrors()) {
				eliteUtils.transferErrorsToAjaxResponse(ajaxResponse, result);
				Map<String, String> errorMsgs = new HashMap<>();

				for (FieldError error : result.getFieldErrors()) {
					if (error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
						errorMsgs.put(error.getField(), getMessage(error.getCode() + "." + error.getField()));
					} else {
						errorMsgs.put(error.getField(), error.getDefaultMessage());
					}
				}
				ajaxResponse.setObject(errorMsgs);
			}
		} else {
			logger.info("Updating File rename agent details::" );
			fileRenameAgent.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject = agentService.updateFileRenameAgentDetail(fileRenameAgent);
			responseObject.setObject(fileRenameAgent.getServerInstance());
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
		
	}		
	
	/**
	 * Delete service from file rename configuration
	 * @param serviceFileRenameConfigId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SERVICE_FROM_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.DELETE_SERVICE_FILE_RENAME_CONFIG, method = RequestMethod.POST)
	@ResponseBody public  String deleteServiceFileRenameAgentConfig(
			@RequestParam(value="serviceFileRenameConfigId" ,required = true) int serviceFileRenameConfigId, HttpServletRequest request){
		 ResponseObject responseObject = agentService.deleteServiceFileRenameAgentConfig(serviceFileRenameConfigId,eliteUtils.getLoggedInStaffId(request));
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}

	/**
	 * Method will load service which are not already configured with file rename agent
	 * @param serviceFileRenameConfigId
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.LOAD_DROP_DOWN_SERVICE_LIST, method = RequestMethod.POST)
	@ResponseBody public  String loadDropDownServiceList(
			@RequestParam(value = "agent_server_Instance_Id", required = true) int serverInstanceId){
		 ResponseObject responseObject = agentService.getServicesforFileRenameAgent(serverInstanceId);		
		 AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();		
	}

}	