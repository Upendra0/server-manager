package com.elitecore.sm.license.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
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
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.LicenseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.model.HourlyCDRCount;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.migration.model.FTPCollectionDriverEntity.PathList.Path;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;
import com.elitecore.sm.version.wrapper.VersionWrapper;

/*
 * Author Keyur Raval 2 july 2016
 * Modified  Ranjitsinh reval
 */
@Controller
public class LicenseController extends BaseController {

	@Autowired
	LicenseService	licenservice;

	@Autowired
	ServletContext servletContext;  // to get class path location
	
	@Autowired
	@Qualifier(value="licenseUtilityQualifier")
	LicenseUtility licenseUtility;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ServerService serverService;
	/**
	 * Method will create trail license 
	 * @param license
	 * @return
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.ACTIVATE_TRIAL_LICENSE, method = RequestMethod.POST)
	@ResponseBody
	public String createTrialLicense(@ModelAttribute(value = FormBeanConstants.LICENSE_FORM_BEAN) License license) throws SMException {//NOSONAR
		logger.debug("Creating  trail license.");
		String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH) + File.separator; // License folder path
		String systemPath = servletContext.getRealPath(BaseConstants.SM_SYSTEM_PATH) + File.separator;  // path where we need to create system folder 
		
		String ipAddress = licenseUtility.getIpAddress();
		license.setHostName(licenseUtility.getHostName(ipAddress));
		license.setSmServerId(licenseUtility.getServerId());

		ResponseObject responseObject = this.licenservice.createTrialLicense(license,repositoryPath, systemPath);  // create trail license
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();

	}

	/**
	 * Method will remove user session and redirect to login page.
	 * @return
	 */
	@RequestMapping(value = { ControllerConstants.GET_TRIAL_LICENSE }, method = RequestMethod.GET)
	public ModelAndView initTrialLicenseActivation(HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		
		HttpSession httpSession = request.getSession();
		httpSession.invalidate();
		RedirectView view = new RedirectView(ControllerConstants.ROOT_PATH); 
		model.setView(view);
		return model;
	}

	@RequestMapping(value = ControllerConstants.INIT_LICENSE_AGREEMENT, method = RequestMethod.GET)
	public ModelAndView redirectToLicenseAgreement() {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.LICENSE_AGREEMENT);
	
		return model;
	}

	@RequestMapping(value = ControllerConstants.REDIRECT_LICENSE_AGREEMENT, method = RequestMethod.POST)
	public ModelAndView redirectLicenceAgreement(@RequestParam(value="disableTrialButton",required=false) boolean disableTrialButton) {
		ModelAndView model = new ModelAndView();
		
		
		model.addObject("disableTrialButton", disableTrialButton);
		model.setViewName(ViewNameConstants.LICENSE_AGREEMENT);
		return model;
	}
	
	@RequestMapping(value = ControllerConstants.INIT_LICENSE_ACTIVATION, method = RequestMethod.GET)
	public ModelAndView initLicenceActivation() {
		ModelAndView model = new ModelAndView();
		return setLicenseActivationDetails(model);
	}
	
	
	@RequestMapping(value = ControllerConstants.LICENSE_ACTIVATION_REDIRECT, method = RequestMethod.POST)
	public ModelAndView redirectToLicenseActivation(@RequestParam(value="disableTrialButton",required=false) boolean disableTrialButton) {
		ModelAndView model = new ModelAndView();
		model.addObject("disableTrialButton", disableTrialButton);
		return setLicenseActivationDetails(model);
		
	}
	
	/**
	 * Method will set license activation page parameters details.
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ModelAndView setLicenseActivationDetails(ModelAndView model){
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ALL_SERVER_TYPE_LIST);
		//remove below code for add server type , currently add only IPLMS
		List<ServerType> iplmsServerType=new ArrayList<>();
		iplmsServerType.addAll(serverType);
				/*Iterator<ServerType> itr=iplmsServerType.iterator();
				while(itr.hasNext()){
					ServerType tempserverType=itr.next();
					if(!BaseConstants.IPLMS.equals(tempserverType.getAlias())){
						itr.remove();
					}
				}*/
		model.addObject(LicenseConstants.LICENSE_TYPE, "RENEW_LICENSE");
		model.setViewName(ViewNameConstants.LICENSE_ACTIVATION);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, iplmsServerType);
		
		return model;
	}

	/**
	 * Method will redirect to license management page and based on 
	 * @param componentType
	 * @param licenseAction
	 * @param serverInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.LICENSE_MANAGER, method = RequestMethod.POST)
	public ModelAndView initLicenceManager(
				@RequestParam(value="componentType") String componentType,
				@RequestParam(value="licenseAction") String licenseAction,
				@RequestParam(value="license_server_instance_id") int serverInstanceId,
				@RequestParam(value="hostIP" , defaultValue = "0") String hostIP
				) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.LICENSE_MANAGER);
		if(LicenseConstants.APPLY_LICENESE.equals(licenseAction)){
			model.addObject(LicenseConstants.LICENSE_TYPE, BaseConstants.APPLY_LICENSE);
			model.addObject("licenseType", BaseConstants.APPLY_LICENSE);
		}else{
			model.addObject(LicenseConstants.LICENSE_TYPE, BaseConstants.RENEW_LICENSE);
			model.addObject("licenseType", BaseConstants.RENEW_LICENSE);
		}
		
		model.addObject("componentType", componentType);
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.APPLY_LICENSE);
		model.addObject("serverTypeId", serverInstanceId);
		model.addObject("hostIP", hostIP);
		
		return model;

	}


	/**
	 * Method will download license registration form.
	 * @return
	 * @throws SMException 
	 */
	@RequestMapping(value = ControllerConstants.DOWNLOAD_LICENSE_FORM, method = RequestMethod.POST)
	public ModelAndView downloadLicenseForm(HttpServletResponse response) throws SMException {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.LICENSE_MANAGER);
		String licenseRegistrationFormPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
			File file = new File(licenseRegistrationFormPath + BaseConstants.SM_LICENSE_TEMPLATE);
			String host ="";
			String serverId = "";
			List<String> ipList = licenseUtility.getIpList();
    		if(!ipList.isEmpty()) {
    			String macId = licenseUtility.getMacAddressByIP(ipList.get(0)).toUpperCase();
        		host=licenseUtility.getHostName(ipList.get(0));
        		serverId = licenseUtility.generateServerId(macId, host);
        		
        		licenseUtility.generateLicenseTemplate(licenseRegistrationFormPath,serverId,host,BaseConstants.SM_LICENSE_FILE,BaseConstants.SM_LICENSE_TEMPLATE);
    		}
    		
		try(FileInputStream inputStream = new FileInputStream(file);ServletOutputStream outStream = response.getOutputStream();){
			response.reset();
			response.setContentType(BaseConstants.LICENSE_CONTENT_TYPE);
			response.setHeader("Content-Disposition", "attachment; filename=\"LicenseRegistration.xls");
	        byte[] buffer = new byte[(int) file.length()];
	        int bytesRead ;
	        // write bytes read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }
		}catch(Exception e){
			logger.error("Exception Occured during download sample file " + e);
			throw new  SMException(e.getMessage());
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.APPLY_LICENSE);
		return model;
	}

	
	
	/**
	 * Method will download license registration form.
	 * @return
	 * @throws SMException 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_ENGINE_LICENSE_FORM, method = RequestMethod.POST)
	public ModelAndView downloadEngineLicenseForm(
				@RequestParam(value = "serverTypeId", required=true) int serverTypeId,
				HttpServletResponse response) throws SMException {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.LICENSE_MANAGER);
		String licenseRegistrationFormPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
			File file = new File(licenseRegistrationFormPath + LicenseConstants.MED_LICENSE_TEMPLATE);
			
			ResponseObject responseObject = licenservice.getEngineLicenseDetailsByInstance(serverTypeId);
			AjaxResponse ajaxResponse = new AjaxResponse();
			if(responseObject.isSuccess()){
				Map<String, String> serverDetailMap  = (Map<String, String>) responseObject.getObject();
				
				licenseUtility.generateLicenseTemplate(licenseRegistrationFormPath,serverDetailMap.get(LicenseUtility.MACID),serverDetailMap.get(LicenseUtility.HOSTNAME),LicenseConstants.MED_SAMPLE_LICENSE_TEMPLATE,LicenseConstants.MED_LICENSE_TEMPLATE);
				
				try (FileInputStream inputStream = new FileInputStream(file);
						ServletOutputStream outStream = response.getOutputStream();) {
					response.reset();
					response.setContentType(BaseConstants.CONTENT_TYPE_FOR_EXPORT_CONFIG);
					response.setContentType(BaseConstants.LICENSE_CONTENT_TYPE);
					response.setHeader("Content-Disposition", "attachment; filename=\"LicenseRegistration.xls");

					byte[] buffer = new byte[(int) file.length()];
					int bytesRead;
					// write bytes read from the input stream into the output stream
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, bytesRead);
					}
				model.addObject("fileDownloadAction", true);	
				}catch(Exception e){
					logger.error("Exception Occured during download engine form sample. " + e);
					throw new  SMException(e.getMessage());
				}
				
			}else{
				model.addObject("fileDownloadAction", false);
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				model.addObject("failMessage", ajaxResponse.getResponseMsg());
			}
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.APPLY_LICENSE);
		model.addObject("componentType", "ENGINE");
		model.addObject("licenseType", BaseConstants.APPLY_LICENSE);
		model.addObject("serverTypeId", serverTypeId);
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		
		return model;
	}
	/**
	 * Method will apply full license.
	 * @param productTypes
	 * @param multipartFile
	 * @return
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.ACTIVATE_FULL_LICENSE, method = RequestMethod.POST)
	@ResponseBody public  String activateFullLicense(
					@RequestParam(value = "productTypes", required = true) String productTypes,
					@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws SMException {
		
				AjaxResponse ajaxResponse = new AjaxResponse();
				if (!multipartFile.isEmpty() ){
					logger.debug("File object found. Going to apply full license.");
					String fileName = multipartFile.getOriginalFilename();
					if(fileName.indexOf(BaseConstants.LICENSE_KEY_FILE_EXT)  >=0 && fileName.contains(".")  ) { // will check file type content type
						String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
						File licenseFile = new File(repositoryPath + LicenseUtility.LICENSEFILE);
						try {
							multipartFile.transferTo(licenseFile);
							ResponseObject responseObject =  this.licenservice.applyFullLicense(licenseFile, productTypes, repositoryPath);
							ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						} catch (Exception e) {
							logger.error("Exception Occured: " + e);
							throw new  SMException(e.getMessage());
						} 
					}else{
						logger.debug("Failed to validate file name.");
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage(ResponseCode.INVALID_LICENSE_FILE.toString()));
					}
				}else{
					logger.debug("File Object found null.");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage(ResponseCode.NO_LICENSE_FILE.toString()));
				}
				return ajaxResponse.toString();
	}
	
	/**
	 * Method will apply full license.
	 * @param productTypes
	 * @param multipartFile
	 * @return
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.ACTIVATE_ENGINE_FULL_LICENSE, method = RequestMethod.POST)
	@ResponseBody public  String activateEngineFullLicense(
					@RequestParam(value = "productTypes", required = true) String productTypes,
					@RequestParam(value = "serverInstanceId",required = true) int serverInstanceId,
					@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws SMException {
		
				AjaxResponse ajaxResponse = new AjaxResponse();
				if (!multipartFile.isEmpty() ){
					logger.debug("File object found. Going to apply full license.");
					String fileName = multipartFile.getOriginalFilename();
					if(fileName.indexOf(BaseConstants.LICENSE_KEY_FILE_EXT)  >=0 && fileName.contains(".")  ) { // will check file type content type is XML and empty
						String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
						File licenseFile = new File(repositoryPath + multipartFile.getOriginalFilename());
						
						try {
							multipartFile.transferTo(licenseFile);
							ResponseObject responseObject =  this.licenservice.applyEngineFullLicense(licenseFile, productTypes, repositoryPath, serverInstanceId);
						
							if(licenseFile.exists()){
								licenseFile.delete();//NOSONAR
							}
							
							ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						} catch (Exception e) {
							logger.error("Exception Occured: " + e);
							throw new  SMException(e.getMessage());
						} 
					}else{
						logger.debug("Failed to validate file name.");
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage(ResponseCode.INVALID_LICENSE_FILE.toString()));
					}
				}else{
					logger.debug("File Object found null.");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage(ResponseCode.NO_LICENSE_FILE.toString()));
				}
				return ajaxResponse.toString();
		}
	
	
	@RequestMapping(value = ControllerConstants.LICENSE_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getLicenseDetailsForServers(
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord
				) {
		
		long count =  this.licenservice.getLicenseDetailsCount();
		
		logger.debug(">> getLicenseDetailsCount in LicenseController count " + count); 
		
		List<License> resultList = new ArrayList<>();
		
		if(count > 0){
			resultList = this.licenservice.getPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
															   limit, sidx,sord);
		}
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			for (License licenseList : resultList) {
				row = new HashMap<>();

				String licenseTypeVar = licenseList.getLicenceType().toString();
				String status = licenseList.getStatus().toString();
				ServerInstance serverInstance = licenseList.getServerInstance();
				String serverInstanceName = serverInstance.getName();
				int serverInstanceId = serverInstance.getId();
				Server server = licenseList.getServerInstance().getServer();
				
				row.put("serverInstanceId", serverInstanceId);
				row.put("serverUtility",server.getUtilityPort());
				row.put("serverType",server.getServerType().getName());
				row.put("id", licenseList.getId());
				row.put("hostName", licenseList.getHostName());	
				row.put("licenceType",licenseTypeVar); 
				row.put("tps", licenseList.getTps());
				row.put("serverInstanceName", serverInstanceName);
				row.put("startDate", DateFormatter.formatDate(licenseList.getStartDate()));				
				row.put("endDate", DateFormatter.formatDate(licenseList.getEndDate())); 
				row.put("status", status);
				row.put("serverId", server.getId());
				
				rowList.add(row);
			}
		}

		logger.debug("<< getLicenseDetailsList in LicenseController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();		
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@RequestMapping(value = ControllerConstants.SERVICES_TPS_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getServicesListwithTPS(
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord,
				@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId
				) {
		
		logger.debug("<< Get tps for Service LicenseController >> getServicesListwithTPS ");
		Map<String, Object> row;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		long count =  servicesService.getTotalServiceCount(serverInstanceId);
		
		List<Service> services = new ArrayList<>();
		
		if(count > 0){
			services = servicesService.getPaginatedList(serverInstanceId, eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),limit, sidx, sord);
		}
		
		ResponseObject responseObject = licenservice.getLicenseDetailsByInstanceId(serverInstanceId);
		License license = new License();
		int serviceId;
		if (responseObject.isSuccess()) {
			license = (License) responseObject.getObject();
		} 
		
		Map<String, String> tpsMap = new HashMap<>();
		if ((license.getTps() != null) && !("".equals(license.getTps())) && !("0".equals(license.getTps()))) { //NOSONAR
			String[] tpsArray = Arrays.toString(license.getTps()).split(",");
			for(String tps : tpsArray) {
				tpsMap.put(tps.split(":")[0], tps.split(":")[1]);
			}
		}
		
		String serviceAlias;
		String serviceInstanceId;
		for (Service ser : services) {
			row = new HashMap<>();
			serviceId = ser.getId();
			serviceInstanceId = ser.getSvctype().getAlias()+"-"+ser.getServInstanceId();
			row.put("serviceId", serviceId);
			row.put("serviceName", ser.getName());
			row.put("serviceInstanceId", serviceInstanceId);
			serviceAlias = ser.getSvctype().getAlias();
			if (tpsMap.containsKey(serviceAlias)) {
				row.put("licenseTps", tpsMap.get(serviceAlias));
			}
			else {
				row.put("licenseTps", "-");
			}
			rowList.add(row);
		}
		
		logger.debug("<< get Services Tps from license key in LicenseController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();		
	}
	
	/**
	 *  Method will give the details of license and version for aboutUs page
	 * @return ModelAndView
	 */
	@RequestMapping(value = ControllerConstants.INIT_ABOUT_US, method = RequestMethod.GET)
	public ModelAndView aboutUsConfig() {

		logger.info("Fetching data for aboutus page, inside aboutUsConfig ");

		ModelAndView model = new ModelAndView(ViewNameConstants.ABOUT_US);

		ResponseObject responseObject = licenservice.getVersionDetails();

		if (responseObject.isSuccess()) {
			VersionWrapper versionWrapper = (VersionWrapper) responseObject.getObject();
			model.addObject("versionData", versionWrapper);
			
			logger.info("Successfully retrieved details for Aboutus page");
			
		} else {
			logger.info("Failed to get license Details for aboutUs page");
			model.addObject(BaseConstants.ERROR_MSG, getMessage(responseObject.getResponseCode().toString()) );
			
		}
		return model;
	}
	
	@RequestMapping(value = ControllerConstants.UPGRADE_ENGINE_DEFAULT_LICENSE, method = RequestMethod.POST)
	@ResponseBody public  String upgradeEngineDefaultLicense(
					/*@RequestParam(value = "productTypes", required = true) String productTypes,*/
					@RequestParam(value = "serverId",required = true) int serverId,
					@RequestParam(value = "file", required = true) MultipartFile multipartFile) throws SMException {
		
				AjaxResponse ajaxResponse = new AjaxResponse();
				if (!multipartFile.isEmpty() ){
					logger.debug("File object found. Going to apply full license.");
					String fileName = multipartFile.getOriginalFilename();
					if(fileName.indexOf(BaseConstants.LICENSE_KEY_FILE_EXT)  >=0 && fileName.contains(".")  ) { // will check file type content type is XML and empty
						String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
						File licenseFile = new File(repositoryPath + multipartFile.getOriginalFilename());
						try {
							multipartFile.transferTo(licenseFile);
							ResponseObject responseObject =  this.licenservice.upgradeEngineDefaultLicense(licenseFile,repositoryPath, serverId);
						
							if(licenseFile.exists()){
								licenseFile.delete();
							}
							
							ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						} catch (Exception e) {
							logger.error("Exception Occured: " + e);
							throw new  SMException(e.getMessage());
						} 
					}else{
						logger.debug("Failed to validate file name.");
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage(ResponseCode.INVALID_LICENSE_FILE.toString()));
					}
				}else{
					logger.debug("File Object found null.");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage(ResponseCode.NO_LICENSE_FILE.toString()));
				}
				return ajaxResponse.toString();
		}
	
	@RequestMapping(value = ControllerConstants.CONTAINER_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getCreatedContainersForHost(
				@RequestParam(value = "hostIp", required = true) String hostIp
				) {
		
		logger.debug(">> getCreatedContainersForHost in LicenseController"); 
		long count = 0;
		ResponseObject responseObject =  serverService.getServerByHostAddress(hostIp);
		if (responseObject.isSuccess()) {
			count = (long) responseObject.getObject();
		}
		
		String hostIPName = hostIp.replaceAll("\\.", "_");
		String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
		String containerLicPath = repositoryPath + hostIPName + File.separator;
		
		int licensedContainers = licenservice.getLicensedContainerForHost(repositoryPath , containerLicPath);
		
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row = new HashMap<>();
	
		row.put("hostIP", hostIp);
		row.put("containerCount",count);
		row.put("licenseCount",licensedContainers);
		
		rowList.add(row);
		
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(0, 0),	0,0,rowList).getJsonString();
	}
	
	
	/**
	 * Method will upgrade container license
	 * @param productTypes
	 * @param multipartFile
	 * @param hostIP
	 * @return
	 * @throws SMException
	 */
	@RequestMapping(value = ControllerConstants.ACTIVATE_LICENSE_CONTAINER, method = RequestMethod.POST)
	@ResponseBody public  String activateLicenseforContainer(
					@RequestParam(value = "file", required = true) MultipartFile multipartFile,
					@RequestParam(value = "hostIP", required = true) String hostIP) throws SMException {
		
				AjaxResponse ajaxResponse = new AjaxResponse();
				if (!multipartFile.isEmpty() ){
					logger.debug("File object found. Going to upgrade license for container .");
					String fileName = multipartFile.getOriginalFilename();
					if(fileName.indexOf(BaseConstants.LICENSE_KEY_FILE_EXT)  >=0 && fileName.contains(".")  ) { // will check file type content type
						String hostIPName = hostIP.replaceAll("\\.", "_");
						String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
						String containerLicPath = repositoryPath + hostIPName + File.separator;
						File containerLic = new File(containerLicPath);
						if (!containerLic.exists()) {
							containerLic.mkdir();
						}
						File licenseFile = new File(containerLicPath + LicenseUtility.LICENSEFILE);
						try {
							multipartFile.transferTo(licenseFile);
							ResponseObject responseObject =  this.licenservice.upgradeContainerLicense(licenseFile, repositoryPath, hostIP, containerLicPath);
							ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
						} catch (Exception e) {
							logger.error("Exception Occured: " + e);
							throw new  SMException(e.getMessage());
						} 
					}else{
						logger.debug("Failed to validate file name.");
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage(ResponseCode.INVALID_LICENSE_FILE.toString()));
					}
				}else{
					logger.debug("File Object found null.");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage(ResponseCode.NO_LICENSE_FILE.toString()));
				}
				return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.GET_LICENSE_UTILIZATION_DATA, method = RequestMethod.GET)
	@ResponseBody 
	public String getHourWiseLicenseUtilizationData(HttpServletRequest request) throws SMException {		
		
		AjaxResponse ajaxResponse = new AjaxResponse();				
		Date currentDate = Utilities.getLastHourDate();
		Date lastUpdatedDate = (Date) servletContext.getAttribute(BaseConstants.LAST_UPDATED_DATE_OF_HOUR_WISE_MAP);
		if(lastUpdatedDate!=null && currentDate!=null && lastUpdatedDate.before(currentDate)) {
			List<HourlyCDRCount> list = licenservice.getHourlyCDRDCountByProcessDate(currentDate);
			licenservice.updateLicenseUtilizationMap(list);
			servletContext.setAttribute(BaseConstants.LAST_UPDATED_DATE_OF_HOUR_WISE_MAP, currentDate);	
			logger.info("License Utilization Maps are successfully updated on date = "+currentDate);
		}
		ResponseObject responseObject  = new ResponseObject();
		responseObject.setSuccess(true);		
		JSONObject json = new JSONObject();
		json.put(BaseConstants.CURRENT_LICENSE_TPS, MapCache.getConfigValueAsObject(BaseConstants.CURRENT_LICENSE_TPS));
		json.put(BaseConstants.MAX_LICENSE_TPS, MapCache.getConfigValueAsObject(BaseConstants.MAX_LICENSE_TPS));
		json.put(BaseConstants.APPLIED_LICENSE_TPS, 50000);
		responseObject.setObject(json);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
}
