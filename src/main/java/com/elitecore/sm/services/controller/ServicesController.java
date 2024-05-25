package com.elitecore.sm.services.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.configmanager.service.VersionConfigService;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.SearchServices;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ServicesController extends BaseController{ 
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired(required=true)
	@Qualifier(value="serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	private DriversService driversService;
	
	@Autowired
	private ServiceValidator validator;
	
	@Autowired
	private ServletContext servletContext;
	
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
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}
	
	/**
	 * When click on Service Manager menu 
	 * @param requestActionType
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SERVICE_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initServiceManager(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType) {
		ModelAndView model = new ModelAndView(ViewNameConstants.SERVICE_MANAGER);
				
		//Request Action type is not null when click on create service tab
		if(requestActionType != null){
			if (requestActionType.equals(BaseConstants.SERVICE_MANAGEMENT)){
				getServiceTypeForSearchTab(model);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
			} else if (requestActionType.equals(BaseConstants.CREATE_SERVICE)) {
				getDataForCreateServiceTab(model,0,true);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
					
			} 

		} //Request Action type is null , when click on left menu 
		else {
				//If user has SERVICE_MANAGEMENT tab rights 
				if(eliteUtils.isAuthorityGranted(BaseConstants.SERVICE_MANAGEMENT)){
					getServiceTypeForSearchTab(model);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVICE_MANAGEMENT);
				}else if(eliteUtils.isAuthorityGranted(BaseConstants.CREATE_SERVICE)){
					//If user has CREATE_SERVICE tab rights 
					getDataForCreateServiceTab(model,0,true);
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CREATE_SERVICE);
				}
		}
		
		//add for service type super set
		model.addObject(FormBeanConstants.SERVICE_FORM_BEAN, new Service());
		model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG,false);

	return model;
}	
	
	/**
	 * Fetch Service List for Server Instance summary page 
	 * @param serverInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_LIST, method = RequestMethod.GET)
	@ResponseBody public String getServiceListByInstanceList(
			@RequestParam(value = "serverInstanceId", required=true) String serverInstanceId,
    		@RequestParam(value = "rows", defaultValue = "10") int limit,
    		@RequestParam(value = "page", defaultValue = "1") int currentPage,
    		@RequestParam(value = "sidx", required=true) String sidx,
    		@RequestParam(value = "sord", required=true) String sord
    		
			){
		
				int iserverInstanceId=Integer.parseInt(serverInstanceId);
				long count = this.servicesService.getTotalServiceCount(iserverInstanceId);				
				
				logger.info("count: " + count);
				List<Service> resultList = new ArrayList<>();
				if(count>0)
					resultList = this.servicesService.getPaginatedList(
								iserverInstanceId,
								eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit))
								, limit, 
								sidx, 
								sord
							);
		
					Map<String, Object> row ;
		
					List<Map<String, Object>> rowList = new ArrayList<>();
		
					if(resultList!=null){
						logger.info("resultList size: " + resultList.size());
						for(Service service : resultList){
							row = new HashMap<>();

							logger.info("Instance :"+service.getId() + " : Active");
							row.put("id", service.getId());
							row.put(BaseConstants.SERV_INSTANCE_ID, service.getServInstanceId());
							row.put("name", service.getName());
							row.put("svcType", service.getSvctype().getType());
							row.put("fileState",service.isEnableFileStats());
							row.put("dbState",service.isEnableDBStats());
							row.put(BaseConstants.SERVICE_TYPE,service.getSvctype().getAlias());
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
	 * Used to display service list in server instance summary page 
	 * @param serverInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_LIST_SUMMARY, method = RequestMethod.GET)
	@ResponseBody public  String getServiceListByInstanceList_Summary(
			@RequestParam(value = "serverInstanceId", required=true) String serverInstanceId,
    		@RequestParam(value = "rows", defaultValue = "10") int limit,
    		@RequestParam(value = "page", defaultValue = "1") int currentPage,
    		@RequestParam(value = "sidx", required=true) String sidx,
    		@RequestParam(value = "sord", required=true) String sord){
				int iserverInstanceId=Integer.parseInt(serverInstanceId);
				long count = this.servicesService.getTotalServiceCount(iserverInstanceId);				
				
				logger.info("count: " + count);
				List<Service> resultList = new ArrayList<>();
				if(count>0)
					resultList = this.servicesService.getPaginatedList(
								iserverInstanceId,
								eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit))
								, limit, 
								sidx, 
								sord
							);
		
					Map<String, Object> row ;
		
					List<Map<String, Object>> rowList = new ArrayList<>();
		
					if(resultList!=null){
						logger.info("resultList size: " + resultList.size());
						for(Service service : resultList){
							row = new HashMap<>();

							logger.info("Instance :"+service.getId() + " : Active");
							row.put("id", service.getId());
							row.put(BaseConstants.SERV_INSTANCE_ID, service.getServInstanceId());
							row.put("name", service.getName());
							row.put(BaseConstants.SERVICE_TYPE,service.getSvctype().getAlias());
							row.put("serviceTypeName",service.getSvctype().getType());
							row.put("state",service.getStatus()+"");
							row.put("status","INACTIVE");
							
							row.put("enableStatus",service.getStatus().toString().trim());
							
							row.put("lastUpdatedDate",DateFormatter.formatDate(service.getLastUpdatedDate())+"");
							row.put("syncStatus",service.isSyncStatus());
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
	 *  Method will fetch all service instances by service Search Parameters. 
	 * @param service
	 * @param result
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Json Ajax response
	 */
	/*@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")*/
	@RequestMapping(value = ControllerConstants.GET_SERVICE_INSTANCE_LIST, method = RequestMethod.POST)
	@ResponseBody public  String getServiceInstanceList(
				SearchServices searchService, 
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord
				) {
		logger.debug(">> getServiceInstanceList in ServiceController " + searchService); 
		
		long count =  this.servicesService.getTotalServiceInstancesCount(searchService);
		logger.debug(">> getServiceInstanceList in ServiceController count " + count); 
		
		List<Service> resultList = new ArrayList<>();
		if(count > 0){
			resultList = this.servicesService.getPaginatedList(searchService,
								   eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
								   limit, sidx,sord);
		}
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (Service serviceList : resultList) {
				
				row = new HashMap<>();
				
				String port = Integer.toString(serviceList.getServerInstance().getPort());
				String ipAddress = serviceList.getServerInstance().getServer().getIpAddress();
				
				String serverIp = ipAddress+":"+port;
				
				row.put(BaseConstants.SERVICE_ID, serviceList.getId());
				row.put(BaseConstants.ID, serviceList.getId());
				row.put(BaseConstants.SERV_INSTANCE_ID, serviceList.getServInstanceId());
				row.put(BaseConstants.SERVICE_NAME, serviceList.getName());
				row.put(BaseConstants.SERVICE_TYPE, serviceList.getSvctype().getAlias());
				row.put(BaseConstants.SERVICE_TYPE_NAME, serviceList.getSvctype().getType());
				row.put(BaseConstants.SERVER_IP_PORT, serverIp);				
				row.put(BaseConstants.SERVICE_INSTANCE_NAME, serviceList.getServerInstance().getName());
				row.put(BaseConstants.SERVICE_INSTANCE_ID, serviceList.getServerInstance().getId());
				row.put(BaseConstants.SERVICE_ENABLE_STATUS, serviceList.getStatus().toString().trim());
				row.put(BaseConstants.SERVICE_STATUS,serviceList.getStatus().toString().trim());
				row.put(BaseConstants.SERVICE_SYNC_STATUS, serviceList.isSyncStatus());
				rowList.add(row);
			}
		}
		logger.debug("<< getServiceInstanceList in ServiceController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();		
	}
	
	
	/**
	 * Add service into Database
	 * @param service
	 * @param result
	 * @param status
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.ADD_SERVICE, method = RequestMethod.POST)
	@ResponseBody public  String createService(@ModelAttribute (value=FormBeanConstants.SERVICE_FORM_BEAN) Service service,//NOSONAR 
																			  BindingResult result, HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateServiceBasicParam(service, result, null, false);
		if(result.hasErrors()){ 
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			
		}else{
			service.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			service.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));

			ResponseObject responseObject=servicesService.addService(service);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}
	
	/**
	 * When click on service name link , redirect to summary page 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param request
	 * @return ModelAndView
	 */
	/*@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")*/
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_SERVICE, method = RequestMethod.GET)
	public ModelAndView initUpdateService(	@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    																	@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    																	@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    																	@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String serverInstanceId
    																	){
		
		ModelAndView model = new ModelAndView();
		RedirectView view=null;
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.INSTANCE_ID, serverInstanceId);
		
		int iServiceId=0;
		if (!StringUtils.isEmpty(serviceId)) {
			iServiceId=Integer.parseInt(serviceId); 
		}
		Service serviceObj=servicesService.getServiceById(iServiceId);
		if(serviceObj!=null){
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceObj.getServInstanceId());
		}
		
		if(EngineConstants.COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_COLLECTION_SERVICE_MANAGER);
		}else if(EngineConstants.PARSING_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_PARSING_SERVICE_MANAGER);
		}else if(EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_DISTRIBUTION_SERVICE_MANAGER);
		}else if(EngineConstants.PROCESSING_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_PROCESSING_SERVICE_MANAGER);
		} else if(EngineConstants.NATFLOW_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.SYSLOG_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.MQTT_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.COAP_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);	
		} else if(EngineConstants.HTTP2_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);	
		} else if(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.IPLOG_PARSING_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_IPLOG_PARSING_SERVICE_MANAGER);
		} else if(EngineConstants.GTPPRIME_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.DATA_CONSOLIDATION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_CONSOLIDATION_MANAGER);
		} else if(EngineConstants.AGGREGATION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_AGGREGATION_SERVICE_MANAGER);
		} else if(EngineConstants.DIAMETER_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_DIAMETER_COLLECTION_SERVICE_MANAGER);
		} else if(EngineConstants.RADIUS_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			view=new RedirectView(ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER);
		}
		model.setView(view);
		
		return model;
	}
	
	/**
	 * check service status whether its running or not
	 * @param serverInstanceId
	 * @return ResponseBody
	 */
	/*@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")*/
	@RequestMapping(value = ControllerConstants.LOAD_SERVICE_STATUS, method = RequestMethod.POST)
	@ResponseBody public String loadServiceStatus(@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
		
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject ;
		 int iserviceId=Integer.parseInt(serviceId);
		responseObject = servicesService.loadServiceStatus(iserviceId);
		
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will import service instance configuration using uploaded XML file.
	 * @param serviceInstanceId
	 * @param file
	 * @param REQUEST_ACTION_TYPE_FROM_FORM
	 * @param request
	 * @return Model view
	 * @throws SMException
	 */
	
	@PreAuthorize("hasAnyAuthority('IMPORT_SERVICE_INSTANCE_CONFIG')")
	@RequestMapping(value = ControllerConstants.IMPORT_SERVICE_INSTANCE_CONFIG, method = RequestMethod.POST)
	@ResponseBody public  String importServiceInstanceConfig(
				@RequestParam(value = "importServiceInstanceId", required = true) String serviceInstanceId,
				@RequestParam(value = "file", required = true) MultipartFile file,
				@RequestParam(value = "serverId", required = false) int serverId,
				@RequestParam(value = "importMode", required = false) int importMode,
				HttpServletRequest request) throws SMException {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if (!file.isEmpty() ){
			if( BaseConstants.IMPORT_FILE_CONTENT_TYPE.equals(file.getContentType()) ) { // will check file type content type is XML and empty
				String tempPath=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
				File importFile = new File(tempPath+file.getOriginalFilename());
				try {
					file.transferTo(importFile);
				} catch (Exception e) {
					logger.error("Exception Occured:"+e);
					throw new  SMException(e.getMessage());
				} 
				
				String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
				int iserviceId=Integer.parseInt(serviceInstanceId);
				ResponseObject responseObject = this.servicesService.importServiceInstanceConfig(iserviceId, importFile,eliteUtils.getLoggedInStaffId(request),jaxbXmlPath,serverId, importMode);
				
				if(responseObject!=null && responseObject.getResponseCode() == ResponseCode.SERVICE_UNMARSHALL_FAIL){
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage("server.instance.import.failed",new Object[]{""}));
					
				}else{
					if(responseObject!=null){
						if(!(responseObject.getResponseCode() == ResponseCode.IMPORT_SERVICE_UNMARSHALL_FAIL_CLASSCAST) && !(responseObject.getResponseCode() == ResponseCode.IMPORT_SERVICE_UNMARSHALL_FAIL_CLASSCAST_FILE_NAME)){
							responseObject.setArgs(new Object[]{""});
						}
						ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
						request.getSession().setAttribute("responseMsg", getMessage("service.instance.import.success"));
					}
				}
				
			}else{
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage("server.instance.import.wrong.file.select",new Object[]{""}));
			}
		}else{
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("server.instance.import.no.file.select"));
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will export service instance configuration and send XML file as response
	 * @param serviceInstanceId
	 * @param requestActionType
	 * @param isExportForDelete
	 * @param request
	 * @param response
	 * @return Model View Object
	 * @throws SMException
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('EXPORT_SERVICE_INSTANCE_CONFIG','DELETE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.EXPORT_SERVICE_INSTANCE_CONFIG, method = RequestMethod.POST)
	public ModelAndView exportServiceInstanceConfig(
						@RequestParam(value = "exportServiceInstanceId", required = true) String serviceInstanceId,
						@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = true) String requestActionType, 
						@RequestParam(value = "isExportForDelete",required = true) boolean isExportForDelete, 
						@RequestParam(value="exportPath" , required=false) String exportFilePath,
						HttpServletResponse response) throws SMException{
		   logger.debug(">> exportServiceInstanceConfig in ServiceController " ); 
		  
		    ModelAndView model = new ModelAndView();
		    ResponseObject responseObject = new ResponseObject();
			
		    File exportXml = null;
			boolean isSuccess=false;
			int serviceId = Integer.parseInt(serviceInstanceId);
			if (StringUtils.isEmpty(exportFilePath) || "".equals(exportFilePath)) {
				String exportTempPath = this.servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
				responseObject =  this.servicesService.getServiceInstaceJAXBXML(serviceId,isExportForDelete,exportTempPath);
				
				if (responseObject.isSuccess() && responseObject.getObject() != null) {
					 Map<String, Object> serviceInstanceJAXB = (Map<String, Object>) responseObject.getObject();
					 exportXml = (File) serviceInstanceJAXB.get(BaseConstants.SERVICE_JAXB_FILE);
					 isSuccess=true;
				}
				
			}else{
				exportXml = new File(exportFilePath);
				isSuccess=true;
			}
			
			if(isSuccess){
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
			 }else{
			   model.addObject(BaseConstants.ERROR_MSG, getMessage(responseObject.getResponseCode().toString()));
		   }
		   
		   getDataForCreateServiceTab(model,0,true); // It will set all required all data for both services tabs.
		   model.setViewName(ViewNameConstants.SERVICE_MANAGER);
		   model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);

		   logger.debug("<< exportServiceInstanceConfig in ServiceController "); 
		return model;
	}
			
	
	
	/**
	 * Method will delete service instance and export service config
	 * @param serviceInstanceId
	 * @param request
	 * @return ResponseBody
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.DELETE_SERVICE_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String deleteServiceInstance(
						 @RequestParam(value = "exportServiceInstanceId", required = true) String serviceInstanceId,
						 HttpServletRequest request) throws SMException {

			AjaxResponse ajaxResponse ;
			
			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			int iserviceId=Integer.parseInt(serviceInstanceId);
			ResponseObject responseObject = servicesService.deleteServiceInstance(iserviceId,eliteUtils.getLoggedInStaffId(request),jaxbXmlPath);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will synchronize the service instance using JMX call to P-ENGINE.
	 * @param serviceInstancesId
	 * @param serviceInstancesStatus
	 * @param request
	 * @return Response body
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('SYNCHRONIZE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.SYNC_SERVICE_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String syncServiceInstance(
					@RequestParam(value = BaseConstants.SERVICE_INSTANCES_ID, required = true) String serviceInstancesId,
					@RequestParam(value = BaseConstants.SERVICE_INSTANCES_STATUS, required = true) String serviceInstancesStatus 
					){
		
		Map<String, AjaxResponse> ajaxresponseMap = new HashMap<>();
		AjaxResponse finalajaxResponse = new AjaxResponse();
		boolean bSuccess = false;
		boolean bSyncFail = false;
		
		// For multiple service sync comma-separated string of all serverInstanceId is coming

			if (serviceInstancesId != null && serviceInstancesStatus != null) {

				logger.info("Synchronization request for serverInstances: " + serviceInstancesId);
				
				String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
				String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
				String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
				
				Map<String,String> serviceInstanceDetailMap = new HashMap<>();
				 
				serviceInstanceDetailMap.put(BaseConstants.SERVICE_INSTANCES_ID, serviceInstancesId);
				serviceInstanceDetailMap.put(BaseConstants.SERVICE_INSTANCES_STATUS, serviceInstancesStatus);
				serviceInstanceDetailMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
				serviceInstanceDetailMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
				serviceInstanceDetailMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);

				Map<String, ResponseObject> responseMap = servicesService.syncServiceInstance(serviceInstanceDetailMap);

				if (responseMap != null) {
					for (Map.Entry<String, ResponseObject> responseObj : responseMap.entrySet()) {
						AjaxResponse ajaxResponse = new AjaxResponse();

						if (responseObj.getValue().isSuccess()) {
							bSuccess = true;
							ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
							ajaxResponse.setResponseMsg(getMessage(responseObj.getValue().getResponseCode().toString()));
							ajaxresponseMap.put(responseObj.getKey(), ajaxResponse);
						} else {
							bSyncFail = true;
							
							if(responseObj.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_INVALID_LICENSE){
								ajaxResponse.setResponseMsg(responseObj.getValue().getMsg());
								ajaxResponse.setResponseCode(BaseConstants. AJAX_RESPONSE_FAIL_INVALID_LICENSE);
							}else {
								ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
								ajaxResponse.setResponseMsg(getMessage(responseObj.getValue().getResponseCode().toString()));
							}
							ajaxresponseMap.put(responseObj.getKey(), ajaxResponse);
						}
					}
					if (bSyncFail && bSuccess) {
						finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
						finalajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVICE_INSTANCES_SYNC_PARSIAL.toString()));
						finalajaxResponse.setObject(ajaxresponseMap);
					} else if (bSyncFail) {
						finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
						finalajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVICE_INSTANCES_SYNC_ALL__FAIL.toString()));
						
						
						finalajaxResponse.setObject(ajaxresponseMap);
					} else {
						finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
						finalajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVICE_INSTANCES_SYNC_ALL_SUCCESS.toString()));
						finalajaxResponse.setObject(ajaxresponseMap);
					}
				}else{
					finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					finalajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL.toString()));
				}
			} else {
				finalajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				finalajaxResponse.setResponseMsg(getMessage(ResponseCode.SERVICE_INSTANCE_SYNCHRONIZATION_FAIL.toString()));
			}
		
		return finalajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('SYNC_PUBLISH_VERSION')")
	@RequestMapping(value = ControllerConstants.SYNC_PUBLISH_SERVICE_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public  String syncPublishServiceInstance(@RequestParam(value = BaseConstants.ID, required = true) String serviceId,
			@RequestParam(value =BaseConstants.DESCRIPTION) String description,
			@RequestParam(value =BaseConstants.SERVER_INSTANCES_STATUS) String serverInstancesStatus, HttpServletRequest request) throws SMException {
		
		ResponseObject responseObject = new ResponseObject();
		
		if(serviceId!=null && !serviceId.isEmpty()){
			
			int iserviceId=Integer.parseInt(serviceId);
			Service service = servicesService.getServiceandServerinstance(iserviceId);
			int serverInstanceId = service.getServerInstance().getId();
			logger.info("Synch & Publish request for serverInstance: " + serverInstanceId);
			int staffId = eliteUtils.getLoggedInStaffId(request);
			String tempPathForSyncPublish=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
			String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
			String xsltFilePath = servletContext.getRealPath(BaseConstants.XSLT_PATH);
			String engineSampleXmlPath=servletContext.getRealPath(BaseConstants.ENGINE_SAMPLE_XML_PATH);
			
			Map<String,String> syncInputMap=new HashMap<>();
			
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_ID, String.valueOf(serverInstanceId));
			syncInputMap.put(BaseConstants.SERVER_INSTANCES_STATUS, serverInstancesStatus);
			syncInputMap.put(BaseConstants.JAXB_XML_PATH_CONSTANT, jaxbXmlPath);
			syncInputMap.put(BaseConstants.XSLT_PATH_CONSTANT, xsltFilePath);
			syncInputMap.put(BaseConstants.ENGINE_SAMPLE_XML_PATH_CONSTANT, engineSampleXmlPath);
			
			try {
				responseObject = versionConfigService.syncPublishServerInstance(serverInstanceId,description,tempPathForSyncPublish,staffId,syncInputMap,responseObject);
			} catch (Exception e) {//NOSONAR
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
			}
			
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
		}
		
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will start service instance using JMX call to P-ENGINE.
	 * @param serviceInstanceId
	 * @return Response body
	 */
	@PreAuthorize("hasAnyAuthority('START_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.START_SERVICE_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String startServiceInstance(@RequestParam(value = "id", required = true) String serviceInstanceId) {
		AjaxResponse ajaxResponse ;
		
		int iserviceId=Integer.parseInt(serviceInstanceId);
		ResponseObject responseObject = servicesService.startServiceInstance(iserviceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will sent stop service instance using JMX call to P-ENGINE.. 
	 * @param serviceInstanceId
	 * @return Response body
	 */
	@PreAuthorize("hasAnyAuthority('STOP_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.STOP_SERVICE_INSTANCE, method = RequestMethod.POST)
	@ResponseBody public String stopServiceInstance(@RequestParam(value = "id", required = true) String serviceInstanceId) {
		AjaxResponse ajaxResponse ;
		
		int iserviceId=Integer.parseInt(serviceInstanceId);
		ResponseObject responseObject = servicesService.stopServiceInstance(iserviceId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
	
	/**
	 * Fetch all required data for services tabs. 
	 * @param model
	 */
	private void getDataForCreateServiceTab(ModelAndView model,int serverInstanceId,boolean allServerInstance){
		
		Map<Integer,ServerInstance> configuredInstanceMap=new TreeMap<>();
		Map<String,List<ServerInstance>> serverTypeMap=new HashMap<>();
		List<ServerInstance> tempInstaceList;
		
		List<ServerInstance> serverInstanceList=serverInstanceService.getServerInstanceList();
		List<Service> serviceList=servicesService.getServicesforServerInstance(serverInstanceId);
		List<ServiceType> serviceTypeList = servicesService.getServiceTypeList();
		
		if(allServerInstance){
			for(Service service:serviceList){
				if(!configuredInstanceMap.containsKey(service.getServerInstance().getId())){
					configuredInstanceMap.put(service.getServerInstance().getId(), service.getServerInstance());
				}
			}
		}else{
			logger.debug("No service is bind with instance then display only instance");
			for(ServerInstance serverInstance:serverInstanceList){
				if(serverInstance.getId() == serverInstanceId){
					configuredInstanceMap.put(serverInstanceId, serverInstance);
				}
			}
			
		}
		
		for(ServerInstance serverInstance:serverInstanceList){
			if(serverTypeMap.get(serverInstance.getServer().getServerType().getAlias()) ==null){
				tempInstaceList=new ArrayList<>();
				tempInstaceList.add(serverInstance);
				serverTypeMap.put(serverInstance.getServer().getServerType().getAlias(), tempInstaceList);	
			}else{
				tempInstaceList=serverTypeMap.get(serverInstance.getServer().getServerType().getAlias());
				tempInstaceList.add(serverInstance);
				serverTypeMap.put(serverInstance.getServer().getServerType().getAlias(), tempInstaceList);	
			}
			
		}
		
		model.addObject(FormBeanConstants.SERVICE_FORM_BEAN, new Service());
		model.addObject(BaseConstants.SERVER_INSTANCE_LIST, serverTypeMap);
		model.addObject(BaseConstants.SERVICE_LIST, serviceList);
		model.addObject(BaseConstants.CONFIGURED_SERVER_INSTANCE_LIST, configuredInstanceMap);
		model.addObject(BaseConstants.SERVICE_TYPE_LIST, serviceTypeList);
		
	}
	
	/**
	 * Fetch Service Type list based on server type
	 * @param serverInstanceId
	 * @return ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('CREATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_LIST_BY_SERVER_TYPE, method = RequestMethod.POST)
	public ModelAndView getServiceTypeByServerType(@RequestParam(value="profileserverInstanceId",required=false) String serverInstanceId){
		
		int iServerInstanceid;
		ModelAndView model = new ModelAndView(ViewNameConstants.SERVICE_MANAGER);
		if(!StringUtils.isEmpty(serverInstanceId)){
			iServerInstanceid=Integer.parseInt(serverInstanceId);
			if(iServerInstanceid == -1){
				logger.debug("Fetch All server Instance data");
				getDataForCreateServiceTab(model,0,true);
			}else{
				ServerInstance serverInstance=serverInstanceService.getServerInstance(iServerInstanceid);
				if(serverInstance !=null){
					int serverTypeId=serverInstance.getServer().getServerType().getId();
					getDataForCreateServiceTab(model,iServerInstanceid,false);
					List<ServiceType> mainServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.MAIN_SERVICE_TYPE);
					List<ServiceType> additionalServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.ADDITIONAL_SERVICE_TYPE);
					
				
					model.addObject(BaseConstants.MAIN_SERVICE_TYPE_LIST, mainServiceTypeList);
					model.addObject(BaseConstants.ADDITIONAL_SERVICE_TYPE_LIST, additionalServiceTypeList);
					model.addObject("selectedServerInstanceId",iServerInstanceid);
				}
			}
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CREATE_SERVICE);
			model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG,false);
			
		}
		return model;
		
	}
	
	/**
	 * Prepare service type super set for multiple product type
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	public void getServiceTypeForSearchTab(ModelAndView model){
		
		List<ServiceType> serviceTypeList=new ArrayList<>();
		List<ServerType> activeServerTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		
		for(ServerType serverType:activeServerTypeList){
			List<ServiceType> mainServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),BaseConstants.MAIN_SERVICE_TYPE);
			List<ServiceType> additionalServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),BaseConstants.ADDITIONAL_SERVICE_TYPE);
			if(mainServiceTypeList != null) {
				for(ServiceType serviceType:mainServiceTypeList){
					if(!serviceTypeList.contains(serviceType)){
						logger.debug("Main Service To be added " +serviceType.getAlias() );
						serviceTypeList.add(serviceType);
					}
				}
			}
			
     		if(additionalServiceTypeList != null) {
				 for(ServiceType serviceType:additionalServiceTypeList){
					if(!serviceTypeList.contains(serviceType)){
						logger.debug("Additional Service To be added " +serviceType.getAlias() );
						serviceTypeList.add(serviceType);
					}
				 }
			}
		}
		model.addObject(BaseConstants.SERVICE_TYPE_LIST, serviceTypeList);
	}
	
	/**
	 * Method will update Service status 
	 * @param DriverId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_SERVICE_ENABLE_STATUS, method = RequestMethod.POST)
	@Transactional
	@ResponseBody public  String updateServiceStatus (
						@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId,
						@RequestParam(value = "serviceStatus",required=true) String serviceStatus) throws CloneNotSupportedException{
		
		 AjaxResponse ajaxResponse =new AjaxResponse();
		if(!StringUtils.isEmpty(serviceId)){
			int iServiceId=Integer.parseInt(serviceId);
			
			ResponseObject responseObject = servicesService.updateServiceStatus(iServiceId, serviceStatus);
			 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		   
		return ajaxResponse.toString();
	}
}
