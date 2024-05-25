/**
 * 
 */
package com.elitecore.sm.serverinstance.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.agent.model.AgentType;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.DBSourceTypeEnum;
import com.elitecore.sm.common.model.FileStorageEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.configmanager.service.VersionConfigService;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.datasource.service.DataSourceService;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.policy.model.DatabaseQuery;
import com.elitecore.sm.policy.service.IDatabaseQueryService;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.model.LogLevelEnum;
import com.elitecore.sm.serverinstance.model.LogRollingTypeEnum;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.serverinstance.validator.ServerInstanceValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemparam.dao.SystemParamDataDao;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * @author Sunil Gulabani Jul 8, 2015
 */

@Controller
public class ServerInstanceController extends BaseController {	

	@Autowired(required = true)
	@Qualifier(value = "serverService")
	private ServerService serverService;

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;

	@Autowired
	@Qualifier(value = "dataSourceService")
	private DataSourceService dataSourceService;
	
	@Autowired
	private ServerInstanceValidator instanceValidator;
	
	@Autowired
	LicenseService  licenseService;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	IDatabaseQueryService databaseQueryService;
	
	@Autowired
	VersionConfigService versionConfigService;

	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setRequiredFields(new String[]{"minMemoryAllocation","maxMemoryAllocation","maxConnectionRetry","retryInterval","connectionTimeout","logsDetail.rollingValue","logsDetail.maxRollingUnit","thresholdTimeInterval","thresholdMemory","loadAverage"});
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(instanceValidator);
	}
	
	/**
	 * @return server instance list as map 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_SERVER_INSTANCE_MAP, method = RequestMethod.GET)
	public ModelAndView loadServerInstanceList() {
		ModelAndView model = new ModelAndView(ViewNameConstants.SERVER_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CREATE_SERVER);
		
		Map<String,List> serverAndServerInstanceMap = serverInstanceService.getAllServerAndItsInstance();
		if(serverAndServerInstanceMap!=null && serverAndServerInstanceMap.size()>0){
		
			for(String key : serverAndServerInstanceMap.keySet()){
				model.addObject(key, serverAndServerInstanceMap.get(key));
			}
		}else{
			logger.info("Server and Server Instance Map is null or zero");
		}
		
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN,(ServerInstance) SpringApplicationContext.getBean(ServerInstance.class));
		return model;
	}

	/**
	 * Reloads the Server Instance Config using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return reload config result
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_RELOAD_CONFIG')")
	@RequestMapping(value = ControllerConstants.SERVER_INSTANCE_RELOAD_CONFIG, method = RequestMethod.POST)
	@ResponseBody public String reloadConfiguration(@RequestParam(value = "id", required = true) String serverInstanceId) {
		AjaxResponse ajaxResponse ;
		
		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.reloadConfiguration(iserverInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	/**
	 * Add Server Instance in database
	 * 
	 * @param serverInstance
	 * @param result
	 * @param status
	 * @param request
	 * @return add instance result as ajax response object
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.ADD_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String addServerInstance(
			@ModelAttribute(value = FormBeanConstants.SERVER_INSTANCE_FORM_BEAN) ServerInstance serverInstance, BindingResult result,//NOSONAR
			@RequestParam(value = "createMode", required = true) String createMode,
			@RequestParam(value = "copyFromId", required = false) String copyFromInstanceId,
			@RequestParam(value = "importFile", required = false) String importFileName,
			HttpServletRequest request) throws SMException  {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject ;
		Map<String,Object> mObj = new HashMap<>();
		Map<String,Object> mParam = new HashMap<>();
		
		Server server=serverService.getServer(serverInstance.getServer().getId());
		if(server !=null) {
			int serverTypeId=server.getServerType().getId();
			logger.debug("Server Type Id found " +serverTypeId);
			List<AgentType> agentTypeList=(List<AgentType>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.AGENT_TYPE);	
			logger.debug("Agent Type List found from Cache is " + agentTypeList );
			
			mParam.put("copyFromId",copyFromInstanceId);
			mParam.put("exportPath",servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT));
			mParam.put("xmlPath",servletContext.getRealPath(BaseConstants.JAXB_XML_PATH));
			mParam.put("importFileName",importFileName);
			mParam.put("activeAgentList", agentTypeList);
			
			instanceValidator.validateServerInstanceParameter(serverInstance, result, null, false);
			// Check validation errors
			if (result.hasErrors()) {
				ajaxResponse.setResponseCode("500");
				ajaxResponse.setResponseMsg(getMessage("server.instance.add.failed"));
				eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			} else {
				serverInstance.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				responseObject = serverInstanceService.addServerInstance(serverInstance,createMode,mParam);
				if(responseObject!=null && responseObject.getResponseCode() == ResponseCode.SERVERiNSTANCE_UNMARSHAL_FAIL){
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage("server.instance.import.failed",new Object[]{getMessage("server.instance.success.import.copy.fail")}) +" : "+responseObject.getObject());
					ajaxResponse.setModuleName(responseObject.getModuleName());
				}else{
					if(responseObject!=null){
						if((BaseConstants.CREATE_MODE_IMPORT.equals(createMode)) && BaseConstants.IMPORT.equals(responseObject.getModuleName())){
							responseObject.setArgs(new Object[]{getMessage("server.instance.success.import.copy.fail")});
					}
					ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
					}
				}
				mObj.put("obj",ajaxResponse.getObject());
			}
		}
	
		
		mObj.put("model",(ServerInstance) SpringApplicationContext.getBean(ServerInstance.class));
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Upload instance import file on server
	 * @param file
	 * @param request
	 * @return upload file result as ajax response object
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPLOAD_IMPORT_FILE, method = RequestMethod.POST)
	@ResponseBody public String uploadInstanceConfig(
		   @RequestParam(value = "file", required = true) MultipartFile file
			) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject ;
		
		if(file==null){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("server.instance.import.file.missing"));
		} else {
			if( BaseConstants.IMPORT_FILE_CONTENT_TYPE.equals(file.getContentType()) ) { // will check file type content type is XML and empty
				String tempPathForImport = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
				logger.info("path ="+tempPathForImport);
				responseObject = serverInstanceService.uploadImportFile(file, tempPathForImport);
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);	
			}else{
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage("server.instance.import.wrong.file.select",new Object[]{""}));
			}
		}
		
		logger.info("ajaxResponse of addServerInstance: " + ajaxResponse);
		return ajaxResponse.toString();
	}

	/**
	 * Sync Server Instance with Engine
	 * 
	 * @param serverInstancesId
	 * @param serverInstancesStatus
	 * @param request
	 * @return synch instance response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_SYNCHRONIZATION')")
	@RequestMapping(value = ControllerConstants.SYNC_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String syncServerInstance(@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID, required = true) String serverInstancesId,
												   @RequestParam(value =BaseConstants.SERVER_INSTANCES_STATUS, required = true) String serverInstancesStatus 
												) throws SMException{

		Map<String, AjaxResponse> ajaxresponseMap = new HashMap<>();
		AjaxResponse finalajaxResponse = new AjaxResponse();
		boolean bSuccess = false;
		boolean bSyncFail = false;

		// For multiple server sync comma-separated string of all serverInstanceId is coming

		if (serverInstancesId != null) {

			logger.info("Synchronization request for serverInstances: " + serverInstancesId);
			
			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
			String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
			
			Map<String,String> syncInputMap=new HashMap<>();
			
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, serverInstancesId);
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, serverInstancesStatus);
			syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
			syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
			syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
			
			Map<String, ResponseObject> responseMap = serverInstanceService.syncServerInstance(syncInputMap);

			if (responseMap != null) {
				for (Map.Entry<String, ResponseObject> response1 : responseMap.entrySet()) {
					AjaxResponse ajaxResponse = new AjaxResponse();

					if (response1.getValue().isSuccess()) {
						bSuccess = true;
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
						if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_ALREADY_SYNC) {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.already.sync"));
						} else {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.success"));
						}
						ajaxresponseMap.put(response1.getKey(), ajaxResponse);
					} else {
						bSyncFail = true;
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL) {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed.jmx.connection.failed"));
						} else if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL) {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed.jmx.api.failed"));
						} else if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS) {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed.inactive.status"));
						} else if (response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL){
							ajaxResponse.setResponseMsg(getMessage("server.instance.update.script.error"));
						} else if(response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT){
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.fail.empty.storage.path"));
							ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL_EMPTY_PKT_STATISTICS_PATH);
						} else if(response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_DATASOURCE_DISABLED_FOR_LICENSE){
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.fail.datasource.disabled.license"));
							ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL_DATASOURCE_DISABLED);
						}else if(response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_INVALID_LICENSE){
							ajaxResponse.setResponseMsg(response1.getValue().getMsg());
							ajaxResponse.setResponseCode(BaseConstants. AJAX_RESPONSE_FAIL_INVALID_LICENSE);
						} else {
							ajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed"));
						}
						ajaxresponseMap.put(response1.getKey(), ajaxResponse);
					}
				}

				if (bSyncFail && bSuccess) {
					finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
					finalajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed.partial"));
					finalajaxResponse.setObject(ajaxresponseMap);
				} else if (bSyncFail) {
					finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
					finalajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed.all"));
					finalajaxResponse.setObject(ajaxresponseMap);
				} else {
					finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
					finalajaxResponse.setResponseMsg(getMessage("server.instance.sync.success.all"));
					finalajaxResponse.setObject(ajaxresponseMap);
				}
			}else{
				finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				finalajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed"));
			}
		} else {
			finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			finalajaxResponse.setResponseMsg(getMessage("server.instance.sync.failed"));
		}

		return finalajaxResponse.toString();
	}
	
	/**
	 * 
	 * @param searchInstanceName
	 * @param searchHost
	 * @param searchInstanceStatus
	 * @param searchServerName
	 * @param searchPort
	 * @param searchSyncStatus
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Get list of all server instance in db
	 */
	/*@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")*/
	@RequestMapping(value = ControllerConstants.GET_SERVER_INSTANCE_LIST, method = RequestMethod.GET)
	@ResponseBody public  String getServerInstanceList(
			@RequestParam(value = "serverType", required = false, defaultValue = "") String serverType, 
			@RequestParam(value = "instanceName", required = false, defaultValue = "") String searchInstanceName, 
			@RequestParam(value = "host",required = false, defaultValue = "") String searchHost, 
			@RequestParam(value = "serverName", required = false, defaultValue = "") String searchServerName,
			@RequestParam(value = "port",required = false, defaultValue = "") String searchPortVal,
			@RequestParam(value = "sync_status", required = false, defaultValue = "") String searchSyncStatus, 
			@RequestParam(value = "rows",defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx",required = true) String sidx, 
			@RequestParam(value = "sord", required = true) String sord,
			@RequestParam(value="id",required = false) String dsid
			) {
		String searchPort=searchPortVal;
		if(!StringUtils.isEmpty(searchPortVal)){
		try {
			int searchPortInt = Integer.parseInt(searchPortVal);
			logger.debug("Port is valid integer:"+searchPortInt);
		} catch (NumberFormatException e) {
		    logger.debug("Not a number");
		    searchPort ="0";
		}
		}
		
		long count = this.serverInstanceService.getTotalServerInstanceCount(serverType,searchInstanceName, searchHost, searchServerName, searchPort,
				searchSyncStatus,dsid);

		logger.info("Active Instance Count: " + count);
		List<ServerInstance> resultList = new ArrayList<>();
		if (count > 0)
			resultList = this.serverInstanceService.getPaginatedList(serverType,searchInstanceName, searchHost, searchServerName, searchPort, searchSyncStatus,
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,dsid);

		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (ServerInstance instance : resultList) {
				row = new HashMap<>();
				
				row.put("status",String.valueOf(instance.getStatus()));
				row.put("id", instance.getId());
				row.put("servername", instance.getServer().getName());
				row.put("serverType", instance.getServer().getServerType().getName());
				row.put("instanceName", instance.getName());
				row.put("host", instance.getServer().getIpAddress());
				row.put("port", instance.getPort());
				row.put("serverStartTime", "-");
				row.put("license", "3 Day Remain");
				if (!instance.isSyncSIStatus() || !instance.isSyncChildStatus()) {
					row.put("syncStatus", false);
				} else {
					row.put("syncStatus", true);
				}
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}

	/**
	 * Soft restart server instance using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return soft restart response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_SOFT_RESTART')")
	@RequestMapping(value = ControllerConstants.SOFT_RESTART_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String softRestartInstance(@RequestParam(value = "id", required = true) String serverInstanceId) {
		AjaxResponse ajaxResponse ;

		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.softRestartInstance(iserverInstanceId);
		ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/** 
	 * Sync & Publish Server Instance using JMX Call.
	 * @param serverInstanceId
	 * @param description
	 * @param serverInstancesStatus
	 * @return
	 * @throws SMException
	 */
	
	@PreAuthorize("hasAnyAuthority('SYNC_PUBLISH_VERSION')")
	@RequestMapping(value = ControllerConstants.SYNC_PUBLISH_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String syncPublishInstance(@RequestParam(value = BaseConstants.ID, required = true) String serverInstanceId,
			@RequestParam(value =BaseConstants.DESCRIPTION) String description,
			@RequestParam(value =BaseConstants.SERVER_INSTANCES_STATUS) String serverInstancesStatus, HttpServletRequest request) throws SMException {
		
		ResponseObject responseObject = new ResponseObject();
		int iserverInstanceId=Integer.parseInt(serverInstanceId);

		logger.info("Synch & Publish request for serverInstance: " + iserverInstanceId);
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		String tempPathForSyncPublish=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
		String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
		
		Map<String,String> syncInputMap=new HashMap<>();
		
		syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, serverInstanceId);
		syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, serverInstancesStatus);
		syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
		syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
		syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
		
		try {
			responseObject = versionConfigService.syncPublishServerInstance(iserverInstanceId,description,tempPathForSyncPublish,staffId,syncInputMap,responseObject);
		} catch (Exception e) {//NOSONAR
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
		}
		
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/** 
	 * Restore & Sync & Publish Server Instance using JMX Call.
	 * @param serverInstanceId
	 * @param versionConfigId
	 * @param description
	 * @param serverInstancesStatus
	 * @param request
	 * @return
	 * @throws SMException
	 * @throws IOException
	 */
	
	@PreAuthorize("hasAnyAuthority('RESTORE_SYNC_VERSION')")
	@RequestMapping(value = ControllerConstants.RESTORE_SYNC_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String restoreSyncPublishServerInstance(@RequestParam(value = BaseConstants.ID, required = true) String serverInstanceId,
			@RequestParam(value =BaseConstants.VERSIONCONFIGID) String versionConfigId,
			@RequestParam(value =BaseConstants.DESCRIPTION) String description,
			@RequestParam(value =BaseConstants.SERVER_INSTANCES_STATUS) String serverInstancesStatus, HttpServletRequest request) throws SMException, IOException {
		
		ResponseObject responseObject = new ResponseObject();
		if(!StringUtils.isEmpty(serverInstanceId) && !StringUtils.isEmpty(versionConfigId)){
			int iserverInstanceId=Integer.parseInt(serverInstanceId);
			int iversionConfigId=Integer.parseInt(versionConfigId);
			logger.info("Restore & Sync request for serverInstance: " + serverInstanceId + " VesrionConfig: "+versionConfigId);
			
			int staffId = eliteUtils.getLoggedInStaffId(request);

			String tempPathForSyncPublish=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
			String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
			
			Map<String,String> syncInputMap=new HashMap<>();
			
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, serverInstanceId);
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, serverInstancesStatus);
			syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
			syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
			syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
			try {
				responseObject = versionConfigService.restoreSyncPublishServerInstance(iserverInstanceId, iversionConfigId, description, serverInstancesStatus, tempPathForSyncPublish, jaxbXmlPath, staffId, syncInputMap, responseObject);
			} catch (Exception e) {//NOSONAR
				logger.info("Restore & Sync of Server Instance Failed.");
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
			responseObject.setObject(null);
		}
		
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Stop server instance using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return stop server response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_STOP')")
	@RequestMapping(value = ControllerConstants.STOP_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String stopInstance(@RequestParam(value = "id", required = true) String serverInstanceId) {
		AjaxResponse ajaxResponse ;

		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.stopInstance(iserverInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}

	/**
	 * Start server instance using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return start server response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_START')")
	@RequestMapping(value = ControllerConstants.START_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String startInstance(@RequestParam(value = "id", required = true) String serverInstanceId) {
		AjaxResponse ajaxResponse ;
		
		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.startInstance(iserverInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Restart server instance using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return restart server response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_START')")
	@RequestMapping(value = ControllerConstants.RESTART_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String restartInstance(@RequestParam(value = "id", required = true) String serverInstanceId) {
		AjaxResponse ajaxResponse;
		
		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.restartInstance(iserverInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}

/**
 * Import server instance configuration.
 * @param serverInstanceId
 * @param file
 * @param importMode
 * @param request
 * @return String
 * @throws SMException
 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_IMPORT_CONFIG')")
	@RequestMapping(value = ControllerConstants.IMPORT_SERVER_INSTANCE_CONFIG, method = RequestMethod.POST)
	@ResponseBody public String importInstanceConfig(@RequestParam(value = "importInstanceId", required = true) String serverInstanceId,
																			 @RequestParam(value = "configFile", required = true) MultipartFile file,
																			 @RequestParam(value = "importMode", required = true) int importMode,
																			 HttpServletRequest request) throws SMException {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if(serverInstanceId !=null){
			int iserverInstanceId=Integer.parseInt(serverInstanceId);
			
			try {
			
			if (!file.isEmpty()) {
				// check for content type
				if (BaseConstants.IMPORT_FILE_CONTENT_TYPE.equals(file.getContentType())) {
					logger.debug("Valid file found for import , process import functionality");
					String tempPath=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File importFile = new File(tempPath+file.getOriginalFilename());
					
					file.transferTo(importFile);
						
					String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
					
					ResponseObject responseObject = serverInstanceService.importServerInstanceConfig(iserverInstanceId, importFile,eliteUtils.getLoggedInStaffId(request),importMode,jaxbXmlPath,false);
					
					if(responseObject!=null && responseObject.getResponseCode() == ResponseCode.SERVERiNSTANCE_UNMARSHAL_FAIL){
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage("server.instance.import.failed",new Object[]{""})+" : "+responseObject.getObject());
						
					}else{
						if(responseObject!=null){
							responseObject.setArgs(new Object[]{""});
							ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
						}
					}
				}else{
					logger.debug("InValid file found for import");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage("server.instance.import.wrong.file.select",new Object[]{""}));
				}
			}else{
				logger.debug("Blank file found for import");
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage("server.instance.import.no.file.select"));
			}
		}	catch (Exception e) {
				logger.error("Exception Occured in Import server instance config:"+e);
				throw new  SMException(e.getMessage());
			} 
		}else{
			logger.debug("Serverinstance id not availabal in request");
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("server.instance.import.failed",new Object[]{""}));
		}
	return ajaxResponse.toString();

	}

	/**
	 * Export server instance configuration using JMX Call.
	 * 
	 * @param serverInstancesId
	 * @param requestActionType
	 * @param isExportForDelete
	 * @param status
	 * @param request
	 * @param response
	 * @return export instance response as model object
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_EXPORT_CONFIG','DELETE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.EXPORT_SERVER_INSTANCE_CONFIG, method = RequestMethod.POST)
	public ModelAndView exportInstanceConfig(@RequestParam(value = "exportInstancesId", required = true) String serverInstanceId,
																			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = true) String requestActionType, 
																			@RequestParam(value = "isExportForDelete",required = true) boolean isExportForDelete, 
																			@RequestParam(value="exportPath" , required=false) String exportFilePath,
																			 HttpServletResponse response) throws SMException{
		
		ModelAndView model = new ModelAndView();
		File exportXml=null;
		boolean isSuccess=false;
		int iserverInstanceId=0;
		
		if(!StringUtils.isEmpty(serverInstanceId)){
			iserverInstanceId =Integer.parseInt(serverInstanceId);	
		}
	
		if (StringUtils.isEmpty(exportFilePath)) {
				logger.debug("Call Simple Export Server Instance functionality");
				
				String tempPathForExport=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
				ResponseObject responseObject = serverInstanceService.getServerInstanceFullHierarchy(iserverInstanceId,isExportForDelete,tempPathForExport);
				
				if (responseObject.isSuccess() && responseObject.getObject() != null) {
					
					Map<String, Object> serverInstanceJAXB = (Map<String, Object>) responseObject.getObject();
					exportXml = (File) serverInstanceJAXB.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
					isSuccess=true;
				}
		}else{
			logger.debug("Call Download Export File in Delete Server Instance");
			exportXml=new File(exportFilePath);
			isSuccess=true;
		}
		
		if(isSuccess){
			downloadExportedServerInstancefile(exportXml,response);
		}else {
			model.addObject(BaseConstants.ERROR_MSG, getMessage("serverMgmt.export.config.fail"));
		}
		
				if (BaseConstants.UPDATE_INSTANCE_SUMMARY.equals(requestActionType)) {
					
					redirectToServerInstanceSummaryPage(iserverInstanceId,model);
					model.setViewName(ViewNameConstants.UPDATE_SERVER_INSTANCE);
					
				} else {
					model.setViewName(ViewNameConstants.SERVER_MANAGER);
				}
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			return model;
		}
	
	

	
	/**
	 * @param serverInstancePort
	 * @return script file name for port as per naming convention
	 * @throws SMException
	 */
	public String getScriptFileName(Integer serverInstancePort) throws SMException {
		logger.info("==============================================");
		logger.info("ScriptFileName: " + "startServer_" + serverInstancePort + ".sh");
		logger.info("==============================================");
		return "startServer_" + serverInstancePort + ".sh";
	}

	/**
	 * Opens the update server instance page
	 * 
	 * @return update instance response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES','UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_SERVER_INSTANCE, method = RequestMethod.POST)
	public ModelAndView initUpdateServerInstance(
			@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_INSTANCE_SUMMARY) String requestActionParam
			) throws SMException 
	{
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		 int iserverInstanceId=0;
		 if(!StringUtils.isEmpty(serverInstanceId)){
			 iserverInstanceId=Integer.parseInt(serverInstanceId);
		 }
		 
		 redirectToServerInstanceSummaryPage(iserverInstanceId,model);
		
		 model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionParam);
		return model;
	}

	
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES','UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = "initUpdateServerInstanceAjax", method = RequestMethod.POST)
	@ResponseBody public String initUpdateServerInstanceAjax(
			@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_INSTANCE_SUMMARY) String requestActionParam
			) throws SMException {
		JSONObject jsonObject = new JSONObject();
		int iserverInstanceId=0;
		if(!StringUtils.isEmpty(serverInstanceId)){
			 iserverInstanceId=Integer.parseInt(serverInstanceId);
		}
		loadJsonObjectForSummaryPage(iserverInstanceId, jsonObject);
		return jsonObject.toString();
	}
	
	/**
	 * Update server instance advance config tab detail
	 * @param serverInstance
	 * @param result
	 * @param requestActiontype
	 * @param request
	 * @return update advance config tab response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_ADVANCE_CONFIG, method = RequestMethod.POST)
	public ModelAndView updateServerInstance(
			@ModelAttribute(value = FormBeanConstants.SERVER_INSTANCE_FORM_BEAN) ServerInstance serverInstance,//NOSONAR
			BindingResult result,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG) String requestActiontype,
			@RequestParam(value="instanceName",required=true) String name
			
			){
		
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		
		instanceValidator.getValidateAdvanceConfigTab(serverInstance, result,null,false);
		if (requestActiontype.equals(BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG)) {
			
			if (result.hasErrors()) {
				for(FieldError error:result.getFieldErrors()){
					logger.info("field="+error.getField()+ "message="+error.getDefaultMessage());
				}
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
				model.addObject("instanceName",name);
			} else {
				ResponseObject responseObject = serverInstanceService.updateInstanceAdvanceConfig(serverInstance);
				if (responseObject.isSuccess()) {
					model.addObject(BaseConstants.RESPONSE_MSG, getMessage("server.instance.updated.success"));
					model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, responseObject.getObject());
					model.addObject("instanceName",((ServerInstance)responseObject.getObject()).getName());
				} else {
					model.addObject(BaseConstants.ERROR_MSG, getMessage("server.instance.name.duplicate.failed"));
					model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
					model.addObject("instanceName",name);
				}
			}
		} 
		
		model.addObject("lastUpdateTime",DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		addCommonParamForUpdate(model);
		
		model.setViewName(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActiontype);
		return model;
	}
	
	
	/**
	 * Add common param in model object for update instance call
	 * @param model
	 */
	private void addCommonParamForUpdate(ModelAndView model){
		ResponseObject responseobject = dataSourceService.getDSConfigList();
		if (responseobject.isSuccess()) {
			List<DataSourceConfig> dsList = (List<DataSourceConfig>)responseobject.getObject();
			if(dsList!=null){
				for(DataSourceConfig dataSourceConfig : dsList){
					if(dataSourceConfig.getPassword()!=null){
						dataSourceConfig.setPassword(EliteUtils.decryptData(dataSourceConfig.getPassword()));
					}
				}
			}
			model.addObject("dsConfigList", responseobject.getObject());
		}
		model.addObject("dbSourcetypeEnumVal", java.util.Arrays.asList(DBSourceTypeEnum.values()));
		model.addObject("fileStorageType",java.util.Arrays.asList(FileStorageEnum.values()));
		model.addObject("logType",java.util.Arrays.asList(LogLevelEnum.values()));
		model.addObject("rollingType",java.util.Arrays.asList(LogRollingTypeEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
	}
	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = "updateSystemLogConfigAjax", method = RequestMethod.POST)
	@ResponseBody public  String updateSystemLogAjax(
			@RequestBody ServerInstance serverInstance, BindingResult result) throws SMException{//NOSONAR
		AjaxResponse ajaxResponse=new AjaxResponse();
		instanceValidator.getValidateSystemLogTab(serverInstance, result,null,false);
		if(result.hasErrors()){
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			
		}else{
			ResponseObject responseObject = serverInstanceService.updateInstanceSystemLog(serverInstance);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Update server instance statistic tab detail
	 * @param serverInstance
	 * @param result
	 * @param requestActiontype
	 * @param serviceList
	 * @param request
	 * @return update statistic tab response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_STATISTIC_CONFIG, method = RequestMethod.POST)
	public ModelAndView updateStatistic(
			@ModelAttribute(value = FormBeanConstants.SERVER_INSTANCE_FORM_BEAN) ServerInstance serverInstance,//NOSONAR
			BindingResult result,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_INSTANCE_STATISTIC) String requestActionType,
			@RequestParam(value="servicesList",required=false) String serviceList
			
			){
		
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		logger.info("Request type =" + requestActionType);
		
		instanceValidator.getValidateStatisticTab(serverInstance, result,null,false);
		
		if (result.hasErrors()) {
			model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
		} else {
			ResponseObject responseObject = serverInstanceService.updateInstanceStatistic(serverInstance, serviceList);
			
			if (responseObject.isSuccess()) {
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("server.instance.updated.success"));
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, responseObject.getObject());
			} else {
				model.addObject(BaseConstants.ERROR_MSG, getMessage("server.instance.update.failed"));
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
			}
		}
		model.addObject("dbInit",serverInstance.isDatabaseInit());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		addCommonParamForUpdate(model);

		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}
	
	/**
	 * Update server instance system log tab detail
	 * @param serverInstance
	 * @param result
	 * @param requestActiontype
	 * @param request
	 * @return update system log tab response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_SYSTEM_LOG_CONFIG, method = RequestMethod.POST)
	public ModelAndView updateSystemLog(
			@ModelAttribute(value = FormBeanConstants.SERVER_INSTANCE_FORM_BEAN) ServerInstance serverInstance,//NOSONAR
			BindingResult result,
			@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false,defaultValue=BaseConstants.UPDATE_INSTANCE_ADVANCE_CONFIG) String requestActionType
			
			){
		
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER_INSTANCE);
		logger.info("Request type =" + requestActionType);
		
		instanceValidator.getValidateSystemLogTab(serverInstance, result,null,false);
		
		if (result.hasErrors()) {
			model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
		} else {
			ResponseObject responseObject = serverInstanceService.updateInstanceSystemLog(serverInstance);
			
			if (responseObject.isSuccess()) {
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("server.instance.updated.success"));
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, responseObject.getObject());
			} else {
				model.addObject(BaseConstants.ERROR_MSG, getMessage("server.instance.update.failed"));
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
			}
		}
		
		model.addObject("lastUpdateTime",DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		addCommonParamForUpdate(model);

		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}

	/**
	 * Copy Server Instance configuration
	 * 
	 * @param copyFromId
	 * @param copyToIds
	 * @param request
	 * @return copy instance response as ajax response object
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_COPY_CONFIG')")
	@RequestMapping(value = ControllerConstants.COPY_SERVER_INSTANCE_CONFIG, method = RequestMethod.POST)
	@ResponseBody public String copyServerInstanceConfig(@RequestParam(value = "copyFromId", required = true) String copyFromId, 
																								  @RequestParam(value = "copyToIds", required = true) String copyToIds, 
																								  HttpServletRequest request) throws SMException{

		Map<String, AjaxResponse> ajaxresponseMap = new HashMap<>();
		AjaxResponse finalajaxResponse = new AjaxResponse();

			if (copyFromId != null && copyToIds != null) {

				logger.info("Copy Config  request form serverInstance: " + copyFromId + " to serverInstances: " + copyToIds);
				boolean bSuccess = false;
				boolean bCopyFail = false;
				String tempPathForExport=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
				String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
				 int icopyFromId=Integer.parseInt(copyFromId);
				Map<String, ResponseObject> responseMap =serverInstanceService.copyServerInstanceConfig(icopyFromId, 
																																	 copyToIds,
																																	 eliteUtils.getLoggedInStaffId(request), 
																																	 tempPathForExport,
																																	 jaxbXmlPath);

				if (responseMap != null) {
					for (Map.Entry<String, ResponseObject> response1 : responseMap.entrySet()) {
						AjaxResponse ajaxResponse = new AjaxResponse();

						if (response1.getValue().isSuccess()) {
							bSuccess = true;
							ajaxResponse.setResponseCode("200");
							ajaxResponse.setResponseMsg(getMessage("server.instance.copy.success"));
							ajaxresponseMap.put(response1.getKey(), ajaxResponse);
						} else {
							bCopyFail = true;
							ajaxResponse.setResponseCode("400");
							if (response1.getValue().getResponseCode() == ResponseCode.SERVICE_RUNNING) {
								ajaxResponse.setResponseMsg(getMessage("server.instance.copy.service.running"));
							} else if(response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_INACTIVE_COPY_CONFIG_FAIL){
								ajaxResponse.setResponseMsg(getMessage("server.instance.inactive.copy.fail"));
							} else {
								ajaxResponse.setResponseMsg(getMessage("server.instance.copy.failed"));
							}
							ajaxresponseMap.put(response1.getKey(), ajaxResponse);
						}
					}

					if (bCopyFail && bSuccess) {
						finalajaxResponse.setResponseCode("200");
						finalajaxResponse.setResponseMsg(getMessage("server.instance.copy.failed.partial"));
						finalajaxResponse.setObject(ajaxresponseMap);
					} else if (bCopyFail) {
						finalajaxResponse.setResponseCode("200");
						finalajaxResponse.setResponseMsg(getMessage("server.instance.copy.failed.all"));
						finalajaxResponse.setObject(ajaxresponseMap);
					} else {
						finalajaxResponse.setResponseCode("200");
						finalajaxResponse.setResponseMsg(getMessage("server.instance.copy.success.all"));
						finalajaxResponse.setObject(ajaxresponseMap);
					}
				} else {
					finalajaxResponse.setResponseCode("400");
					finalajaxResponse.setResponseMsg(getMessage("server.instance.copy.failed"));
				}
			} else {
				finalajaxResponse.setResponseCode("400");
				finalajaxResponse.setResponseMsg(getMessage("server.instance.copy.failed"));
			}
		return finalajaxResponse.toString();
	}

	/**
	 * Method will delete  Server Instance
	 * @param serverInstacesId
	 * @param request
	 * @return String
	 * @throws SMException
	 */
	
	@PreAuthorize("hasAnyAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.DELETE_SERVER_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String deleteServerInstance(@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId,
																							HttpServletRequest request) throws SMException{

		AjaxResponse ajaxResponse = new AjaxResponse();
		
		String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
		int iserverInstanceId;
		if(!StringUtils.isEmpty(serverInstanceId)){
			 iserverInstanceId=Integer.parseInt(serverInstanceId);	
		
			 ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
		try{
			
			ResponseObject responseObject = serverInstanceService.deleteServerInstance(iserverInstanceId,eliteUtils.getLoggedInStaffId(request),jaxbXmlPath);
			ajaxResponse =eliteUtils.convertToAjaxResponse(responseObject);
		}catch(SMException e){
			logger.error("Exception Occured: "+e);
		
			if(ResponseCode.P_ENGINE_NOT_RUNNING.getDescription().equals(e.getMessage())){
				ajaxResponse.setResponseCode("400");
				ajaxResponse.setResponseMsg(messageSource.getMessage(ResponseCode.P_ENGINE_NOT_RUNNING.getDescription(),
						new Object[] { serverInstance.getServer().getIpAddress(),String.valueOf(serverInstance.getServer().getUtilityPort())},
						LocaleContextHolder.getLocale()));
			}else if(ResponseCode.SERVERINSTANCE_DELETE_FAIL_JMX_API_FAIL.getDescription().equals(e.getMessage())){
				ajaxResponse.setResponseCode("400");
				ajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVERINSTANCE_DELETE_FAIL_JMX_API_FAIL.getDescription()));
			}
			}
		}	
		return ajaxResponse.toString();
	}


	/**
	 * Update SNMP alert status for server instance
	 * 
	 * @param serverInstanceId
	 * @param status
	 * @return update SNMP state response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_SNMP_ALERT_STATUS, method = RequestMethod.POST)
	@ResponseBody public String updateSNMPAlertStatus(@RequestParam(value = "id", required = true) String serverInstanceId, @RequestParam(
			value = "status", required = true) boolean status) {
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject ;
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		responseObject = serverInstanceService.updateSNMPStatus(iserverInstanceId,status);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Update file state in db status for server instance
	 * @param serverInstanceId
	 * @param status
	 * @return pdate file state response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_FILE_STATE_DB, method = RequestMethod.POST)
	@ResponseBody public String updateFileStateInDB(
		   @RequestParam(value = "id",required=true) String serverInstanceId,
		   @RequestParam(value = "status",required=true) boolean status){
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject ;
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		responseObject = serverInstanceService.updateFileStateInDB(iserverInstanceId,status);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Update web service state for server instance 
	 * @param serverInstanceId
	 * @param status
	 * @return pdate webservice state response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_WEB_SERVICE_STATE, method = RequestMethod.POST)
	@ResponseBody public String updateWebServiceState(
		   @RequestParam(value = "id",required=true) String serverInstanceId,
		   @RequestParam(value = "status",required=true) boolean status){
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject;
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		responseObject = serverInstanceService.updateWebServiceStatus(iserverInstanceId,status);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Update rest web service state for server instance 
	 * @param serverInstanceId
	 * @param status
	 * @return pdate restwebservice state response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_REST_WEB_SERVICE_STATE, method = RequestMethod.POST)
	@ResponseBody public String updateRestWebServiceState(
		   @RequestParam(value = "id",required=true) String serverInstanceId,
		   @RequestParam(value = "status",required=true) boolean status){
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject;
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		responseObject = serverInstanceService.updateRestWebServiceStatus(iserverInstanceId,status);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * check server instance status whether its running or not
	 * @param serverInstanceId
	 * @return server instance running status as ajax response
	 */
	@RequestMapping(value = ControllerConstants.LOAD_SERVER_INSTANCE_STATUS, method = RequestMethod.POST)
	@ResponseBody public String loadServerInstanceStatus(@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId) {
		int iserverInstanceId = Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.loadInstanceStatus(iserverInstanceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * check server instance status whether its running or not
	 * @param serverInstanceId
	 * @return server instance running status as ajax response
	 */
	@RequestMapping(value = ControllerConstants.VIEW_SERVER_INSTANCE_LICENSE, method = RequestMethod.POST)
	@ResponseBody
	public String displayServerInstanceLicenseDetails(@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId) {
		logger.debug("fetching license details for server instance " + serverInstanceId);
		ResponseObject responseObject = licenseService.getLicenseDetailsByServerInstance(serverInstanceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * @return server instance list as ajax response
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_SERVR_INSTANCE_LIST, method = RequestMethod.POST)
	@ResponseBody public String getServerInstanceList() {
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		List<ServerInstance> liServerInstance = serverInstanceService.getServerInstanceList();
		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (liServerInstance != null && !liServerInstance.isEmpty()) {
			for (ServerInstance instance : liServerInstance) {
				row = new HashMap<>();
				
				row.put("id", instance.getId());
				row.put("servername", instance.getServer().getName());
				row.put("instanceName", instance.getName());
				row.put("host", instance.getServer().getIpAddress());
				row.put("port", instance.getPort());
				rowList.add(row);
			}
		}
		ajaxResponse.setResponseCode("200");;
		ajaxResponse.setObject(rowList);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	
	}
	
	/**
	 * Reloads the Server Instance Cache using JMX Call.
	 * 
	 * @param serverInstanceId
	 * @return server instance reload cache response as ajax response
	 */
	// To do access right for reload cache
	@PreAuthorize("hasAnyAuthority('SERVER_INSTANCE_RELOAD_CACHE')")
	@RequestMapping(value = ControllerConstants.SERVER_INSTANCE_RELOAD_CACHE, method = RequestMethod.POST)
	@ResponseBody public String reloadCache(@RequestParam(value = "id", required = true) String serverInstanceId,
			@RequestParam(value = "reloadType", required = true) String reloadType,
			@RequestParam(value = "databaseQuery",required = false) String databaseQuery) {
		AjaxResponse ajaxResponse ;

		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.reloadCache(iserverInstanceId,reloadType,databaseQuery);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Update server instance status in database.
	 * 
	 * @param serverInstanceId
	 * @return server instance update status as ajax response
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES','CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_SERVER_INSTANCE_STATUS, method = RequestMethod.POST)
	@ResponseBody public String updateInstanceStatus(@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId) throws SMException{
		AjaxResponse ajaxResponse ;
		
		int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.updateStatus(iserverInstanceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Check whether port is free or not for given server
	 * @param serverInstanceId
	 * @param port
	 * @return Check port is free or not for given server response as Ajax response object 
	 */
	@RequestMapping(value = ControllerConstants.CHECK_PORT_AVAILIBILITY, method = RequestMethod.POST)
	@ResponseBody public String checkPortAvailibility(
			@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId,
			@RequestParam(value = "port", required = true) int port,
			@RequestParam(value = "isProxyPort", required = false) boolean isProxyPort
			) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		ResponseObject responseObject = serverInstanceService.checkPortAvailibility(iserverInstanceId,port);
		
		if(!responseObject.isSuccess()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			String portId = isProxyPort ? "service-proxyServicePort" : "service-netFlowPort";
			errorMsgs.put(portId, getMessage(String.valueOf(responseObject.getResponseCode()),null));
			ajaxResponse.setObject(errorMsgs);
		}else{
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * write Exported file in http response
	 * @param exportXml
	 * @param response
	 * @throws SMException
	 */
	public void downloadExportedServerInstancefile(File exportXml,HttpServletResponse response) throws SMException{
		
		try(FileInputStream inputStream = new FileInputStream(exportXml);ServletOutputStream outStream = response.getOutputStream();){
		
			response.reset();
			response.setContentType(BaseConstants.CONTENT_TYPE_FOR_EXPORT_CONFIG);
			response.setHeader("Content-Disposition", "attachment; filename=\"" +exportXml.getName());
	        byte[] buffer = new byte[(int) exportXml.length()];
	        int bytesRead ;
	        
	        // write bytes read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }

	        outStream.flush();

		}catch(Exception e){
			logger.error("Exception Occured:"+e);
			throw new  SMException(e.getMessage());
		}
	}
	
	/**
	 * Redirect to server instance summary page
	 * @param iserverInstanceId
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	public void redirectToServerInstanceSummaryPage(int iserverInstanceId,ModelAndView model){
		
		ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
		model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);

		ResponseObject responseobject = dataSourceService.getDSConfigList();
		if (responseobject.isSuccess()) {
			List<DataSourceConfig> dsList = (List<DataSourceConfig>)responseobject.getObject();
			if(dsList!=null){
				for(DataSourceConfig dataSourceConfig : dsList){
					if(dataSourceConfig.getPassword()!=null){
						dataSourceConfig.setPassword(EliteUtils.decryptData(dataSourceConfig.getPassword()));
					}
				}
			}
			model.addObject("dsConfigList", responseobject.getObject());
		}
		
		 int serverTypeId=serverInstance.getServer().getServerType().getId();
		
		 List <DatabaseQuery> databaseQueries = databaseQueryService.getAssociatedQueriesByServerId(iserverInstanceId);
		 List <String> databaseQueryNameList = new ArrayList<>();
		 for (DatabaseQuery databaseQuery : databaseQueries)
		 {
			 databaseQueryNameList.add(databaseQuery.getQueryName());
		 }
		 model.addObject("databaseQueryList", databaseQueryNameList);
		 
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
		model.addObject("instanceName",serverInstance.getName());
		model.addObject("dbInit",serverInstance.isDatabaseInit());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		model.addObject("logType", java.util.Arrays.asList(LogLevelEnum.values()));
		model.addObject("rollingType", java.util.Arrays.asList(LogRollingTypeEnum.values()));
		model.addObject("fileStorageType", java.util.Arrays.asList(FileStorageEnum.values()));
		model.addObject("dbSourcetypeEnumVal", java.util.Arrays.asList(DBSourceTypeEnum.values()));
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
	}
	
	@SuppressWarnings("unchecked")
	public void loadJsonObjectForSummaryPage(int iserverInstanceId,JSONObject jsonObject){
		ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
		
		jsonObject.put(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN, serverInstance);
		JSONObject formJson = new JSONObject();
		

		ResponseObject responseobject = dataSourceService.getDSConfigList();
		if (responseobject.isSuccess()) {
			List<DataSourceConfig> dsList = (List<DataSourceConfig>)responseobject.getObject();
			if(dsList!=null){
				for(DataSourceConfig dataSourceConfig : dsList){
					if(dataSourceConfig.getPassword()!=null){
						dataSourceConfig.setPassword(EliteUtils.decryptData(dataSourceConfig.getPassword()));
					}
				}
			}
			formJson.put("dsConfigList", responseobject.getObject());
		}
		
		int serverTypeId=serverInstance.getServer().getServerType().getId();
		
		List<String> generalEntityStatus=(List<String>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.GENERAL_TYPE_PROFILING);
		for(String generalEntity:generalEntityStatus){
			if(BaseConstants.POLICIES_CONFIGURATION.equals(generalEntity)){
				 logger.debug("Policy configuration is active");
				 formJson.put("isPolicyActive","ACTIVE");
			 }else if(BaseConstants.RELOAD_CACHE.equals(generalEntity)){
				 logger.debug("Reload Cache  is active");
				 formJson.put("isReloadCache","ACTIVE");
			 }
		}
		formJson.put("instanceName",serverInstance.getName());
		formJson.put("lastUpdateTime",DateFormatter.formatDate(serverInstance.getLastUpdatedDate()));
		formJson.put("logType", EliteUtils.getStringArrayFromEnum(LogLevelEnum.class));
	    formJson.put("rollingType", EliteUtils.getStringArrayFromEnum(LogRollingTypeEnum.class));
	    formJson.put("fileStorageType", EliteUtils.getStringArrayFromEnum(FileStorageEnum.class));
	    jsonObject.put("formData", formJson);
	}
	
	/**
	 * Method will get all instances by server type or profiling.
	 * @param serverInstanceId
	 * @return 
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_INSTANCE_LIST_BY_SERVER_TYPE, method = RequestMethod.POST)
	@ResponseBody 
	public String getAllInstancesByServerType(@RequestParam(value = "serverInstanceId", required = false) int serverInstanceId){
		ResponseObject responseObject = serverInstanceService.getAllInstancesByServerType(serverInstanceId, false);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("return ajax response id : " + ajaxResponse);
		return ajaxResponse.toString();
	}	
	
	/**
	 * Method will get all instances by server type or profiling.
	 * @param serverInstanceId
	 * @return 
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_INSTANCE_LIST_BY_SERVER_TYPE_FOR_CREATE, method = RequestMethod.POST)
	@ResponseBody 
	public String getAllInstancesByServerTypeForCreate(@RequestParam(value = "serverId", required = true) int serverId){
		ResponseObject responseObject = serverInstanceService.getAllInstancesByServerType(serverId, true);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("return ajax response id : " + ajaxResponse);
		return ajaxResponse.toString();
	}	
	
	/**
	 * @author brijesh.soni
	 * @param serverInstanceId
	 * @return ServerInstance JSON string
	 * @throws SMException
	 * This method will return server instance object as json object. 
	 * This method used jackson mapping to get json from bidirectional reference
	 * Please skip ToStringProcesser for ServerInstance to get json object
	 */
	@RequestMapping(value = "getServerInstanceJson", method = RequestMethod.POST)
	@ResponseBody public String getServerInstanceJson(
			@RequestParam(value = "serverInstanceId",required=true) String serverInstanceId)
					throws SMException {
		int iserverInstanceId=0;
		if(!StringUtils.isEmpty(serverInstanceId)){
			 iserverInstanceId=Integer.parseInt(serverInstanceId);
		}
		ServerInstance serverInstance = serverInstanceService.getServerInstance(iserverInstanceId);
		ObjectMapper mapper = new ObjectMapper();
		
		/*
		 * to support JSON serialization and deserialization of Hibernate,
		 * specific datatypes and properties; 
		 * especially lazy-loading aspects.
 		 * Reference : https://github.com/FasterXML/jackson-datatype-hibernate
 		 */
		mapper.registerModule(new Hibernate5Module()); 
	
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(serverInstance);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage() , e);
		}
		logger.info("ServerInstance JSON : "+jsonInString);
		
		return jsonInString;
	}
	
	/**
	 * @author brijesh.soni
	 * @param serviceId
	 * @return Service JSON string
	 * @throws SMException
	 * This method will return service object as json object. 
	 * This method used jackson mapping to get json from bidirectional reference
	 * Please skip ToStringProcesser for Service to get json object
	 */
	@RequestMapping(value = "getServiceJson", method = RequestMethod.POST)
	@ResponseBody public String getServiceInstanceJson(
			@RequestParam(value = "serviceId",required=true) String serviceId) throws SMException {
		String jsonInString = "";
		int iServiceId = 0;
		if(!StringUtils.isEmpty(serviceId)){
			iServiceId=Integer.parseInt(serviceId);
		}
		Service service = servicesService.getServiceById(iServiceId);
		
		ObjectMapper mapper = new ObjectMapper();
		
		/*
		 * to support JSON serialization and deserialization of Hibernate,
		 * specific datatypes and properties; 
		 * especially lazy-loading aspects.
 		 * Reference : https://github.com/FasterXML/jackson-datatype-hibernate
 		 */
		mapper.registerModule(new Hibernate5Module());
		try {
			jsonInString = mapper.writeValueAsString(service);
			logger.info("Service JSON : "+jsonInString);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage() , e);
		}
		
		return jsonInString;
	}
	
	/**
	 * Get the Alert List mapped with client
	 * 
	 * @param snmpClientId
	 * @param request
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(value = ControllerConstants.GET_DS_LIST_BY_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public String getDSListByType(
			@RequestParam(value = "dsType", required = true) String dsType,
			HttpServletRequest request) throws CloneNotSupportedException {
		
		ResponseObject responseObject = dataSourceService
				.getDataSourceListByType(dsType);
		AjaxResponse ajaxResponse = eliteUtils
				.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
	
	/**
	 * @return server instance list as map 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.MIGRATEALLSERVERCONFIG, method = RequestMethod.GET)
	public @ResponseBody String migrateAllServerConfig() {
		
		ResponseObject responseObject = new ResponseObject();
		
		Map<String,List> serverAndServerInstanceMap = serverInstanceService.getAllServerAndItsInstance();
		if(serverAndServerInstanceMap!=null && serverAndServerInstanceMap.size()>0){				
			List<ServerInstance> listSI = serverAndServerInstanceMap.get(BaseConstants.SERVER_INSTANCE_LIST);
			serverInstanceService.migrateAllServerInstanceConfigInDB();

		}else{
			logger.info("Server and Server Instance Map is null or zero");
		}
	
		serverInstanceService.migrateVersion();
		responseObject.setSuccess(true);
		return responseObject.toString();
	}
}
