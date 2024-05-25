/**
 * 
 */
package com.elitecore.sm.drivers.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.ControlFileAttributesEnum;
import com.elitecore.sm.common.model.ControlFileResetFrequency;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FileFetchTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FileTransferModeEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.DriverDataTypeEnum;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPDistributionDriver;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.drivers.validator.DriverValidator;
import com.elitecore.sm.parser.model.SeparatorEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class DistributionDriverController extends BaseController {

	
	@Autowired
	@Qualifier(value="driversService")
	DriversService driversService;
	
	@Autowired
	DriverValidator validator;
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;

	@Autowired
	ServerInstanceService serverInstanceService;
	
	@Autowired
	ServletContext servletContext;
	
	
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
	 * Method will return the list of all collection drivers by service id 
	 * @param serviceInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Response body
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.GET_DISTRIBUTION_DRIVER_LIST, method = RequestMethod.GET)
	@ResponseBody public String getDistributionDriverList(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord
			) {
		logger.debug(">> Get getDistributionDriverList in DistributionDriverController " +serviceId); 
		
		long count =  this.driversService.getDriversTotalCount(serviceId);
		List<Map<String, Object>> rowList = null ;
		if(count > 0){
			rowList = this.driversService.getDistributionDriverPaginatedList(serviceId, 
																	eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
																	limit, sidx,sord); 
		}
		logger.debug("<< getDistributionDriverList in DistributionDriverController ");
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();
	}
	
	
	/**
	 * Method will get distribution driver plug-in list
	 * @param driverId
	 * @return 
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_DISTRIBUTION_DRIVER_PLUGIN_LIST, method = RequestMethod.POST)
	@ResponseBody public String getDirstributionDriverPluginList(@RequestParam(value = "id", required = true) int driverId) {
		
		logger.debug(">> getDirstributionDriverPluginList in DistributionDriverController  "  + driverId);
		ResponseObject responseObject  = this.driversService.getAllDistributionPluginbyDriverId(driverId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	

	/**
	 * Method will update Distribution Driver status from distribution service summary page. 
	 * @param DriverId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_DISTRIBUTION_DRIVER_STATUS, method = RequestMethod.POST)
	@ResponseBody public  String updateDistributionDriverStatus(
						@RequestParam(value = BaseConstants.DRIVER_ID,required=true) int driverId,
						@RequestParam(value = "driverType",required=true) String driverType,
						@RequestParam(value = "driverStatus",required=true) String driverStatus) throws CloneNotSupportedException{
		
		 logger.debug(">> updateDistributionDriverStatus in DistributionDriverController  "  + driverId + " " + driverType + " " + driverStatus);
		 ResponseObject responseObject = driversService.updateDriverStatus(driverId, driverType, driverStatus);
		 AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		   
		return ajaxResponse.toString();
	}
	
	
	
	/**
	 * Method will redirect the page to Collection Driver Page.
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('CONFIGURE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_DRIVER_MANAGER, method = RequestMethod.POST)
	public ModelAndView initDistributionDriverManager(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=false) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=false) String serviceName,
    		@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId
    		) {
		
		logger.debug("Loading distribution driver configuration manager page.");
		ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_SERVICE_MANAGER);
		
		List<Drivers> distributionDriverList = null;
	
		ResponseObject responseObject = driversService.getDriversByServiceId(serviceId);
		if(responseObject.isSuccess()){
			distributionDriverList = (List<Drivers>)responseObject.getObject();
			model.addObject("distributionDriverList", distributionDriverList);
		}else{
			model.addObject("distributionDriverList", distributionDriverList);
		}
	
		Service service =  servicesService.getServiceandServerinstance(serviceId);
		
		int serverTypeId=service.getServerInstance().getServer().getServerType().getId();
		List<DriverType> driverTypeList=(List<DriverType>) eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.DISTRIBUTION_DRIVER_TYPE);
		logger.debug("Final distribution driver list" +driverTypeList  );
		
		model.addObject(BaseConstants.DISTRIBUTION_DRIVER_TYPE_LIST,driverTypeList);
		model.addObject(BaseConstants.LAST_UPDATE_TIME,DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.STATE_ENUM,java.util.Arrays.asList(StateEnum.values()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject("timeout",new DistributionDriver().getTimeout());
		model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG,false);
		return model;
	}
	
	
	/**
	 * Create Distribution Driver
	 * @param driverCount
	 * @param distributionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.CREATE_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String createDistributionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.DISTRIBUTION_DRIVER_FORM_BEAN) DistributionDriver distributionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateDriverParameter(distributionDriver, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(driverCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(driverCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			distributionDriver.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			distributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			distributionDriver.setCreatedDate(new Date());
			ResponseObject responseObject = new ResponseObject();
			String driverType = distributionDriver.getDriverType().getAlias();
			
			if (EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverType)  || EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driverType) || EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driverType) || EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driverType)) {
				responseObject = driversService.createDistributionDriver(distributionDriver);
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DRIVER_ADD_FAIL);
			}
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * it will update FTP Distribution Driver basic details.
	 * @param driverCount
	 * @param distributionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_FTP_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String updateFTPDistributionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.DISTRIBUTION_DRIVER_FORM_BEAN) DistributionDriver ftpDistributionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
		AjaxResponse ajaxResponse =  updateDistributionDriverBasicDetails(ftpDistributionDriver, result, driverCount, request);
		return ajaxResponse.toString();
	}

	
	
	/**
	 * It will update SFTP Distribution Driver basic details.
	 * @param driverCount
	 * @param distributionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_SFTP_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String updateSFTPDistributionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.DISTRIBUTION_DRIVER_FORM_BEAN) DistributionDriver sftpDistributionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
	
		AjaxResponse ajaxResponse =  updateDistributionDriverBasicDetails(sftpDistributionDriver, result, driverCount, request);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * it will update local distribution driver basic details.
	 * @param driverCount
	 * @param distributionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_LOCAL_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String updateLocalDistributionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.DISTRIBUTION_DRIVER_FORM_BEAN) DistributionDriver localDistributionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
	
		AjaxResponse ajaxResponse =  updateDistributionDriverBasicDetails(localDistributionDriver, result, driverCount, request);
		return ajaxResponse.toString();
	}
	
	/**
	 * It will update all distribution driver basic details.
	 * @param collectionDriver
	 * @param result
	 * @param driverCount
	 * @param request
	 * @return
	 */
	private AjaxResponse updateDistributionDriverBasicDetails(DistributionDriver distributionDriver, BindingResult result, String driverCount, HttpServletRequest request){
		
		String distributionDriverType=null;
		ResponseObject responseObject=null;
		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateDriverParameter(distributionDriver, result, null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(driverCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(driverCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		} else {
			distributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			if (distributionDriver != null) {
				distributionDriverType = distributionDriver.getDriverType().getAlias();
			}
			if (EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(distributionDriver.getDriverType().getAlias())) {
				
				responseObject = driversService.updateDatabaseDistributionDriver(distributionDriver);
			} else {
				responseObject = driversService.updateDistributionDriver(distributionDriver);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse;
	}
	
	/**
	 * Method will delete Distribution Driver and update its application orders
	 * @param DriverId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.DELETE_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String deleteDistributionDriver(
						@RequestParam(value = BaseConstants.DRIVER_ID,required=true) String driverId,
						@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
		
			int iDriverId=Integer.parseInt(driverId);
			int iServiceId=Integer.parseInt(serviceId);
		
			 ResponseObject responseObject = driversService.deleteDriverDetails(iDriverId,iServiceId);
			  AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPLOAD_DISTRIBUTION_DB_DRIVER_ATTR_DATA, method = RequestMethod.POST)
	@ResponseBody
	public String uploadDistributionDbDriverAttrData(@RequestParam(value = "driverId",required=true) String driverId,
										@RequestParam(value = "file", required = true) MultipartFile multipartFile,
										HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject;
		String responseStr = "";
		multipartFile.getName();
		multipartFile.getSize();
		int staffId = eliteUtils.getLoggedInStaffId(request);

		if (!multipartFile.isEmpty()) {
			logger.debug("File object found.Going to insert data");
			// check if file is a valid csv file
			if (BaseConstants.CSV_FILE_CONTENT.equalsIgnoreCase(multipartFile.getContentType())
					|| "application/octet-stream".equalsIgnoreCase(multipartFile.getContentType())
					|| multipartFile.getOriginalFilename().endsWith(".csv")) {
				logger.debug("Valid CSV file found");
				// Check if the file headers match
				String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT) + File.separator;
				File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
				try {
					multipartFile.transferTo(lookupDataFile);
					
					logger.debug("Fetching all attribute for device configuration : " + driverId);
					
					List<DatabaseDistributionDriverAttribute> oldAttributeList =  (List<DatabaseDistributionDriverAttribute>) driversService.getAttributeListByDriverId(Integer.parseInt(driverId));
					String delAttrIds = getAttributeIdsFromList(oldAttributeList);
					if(!StringUtils.isEmpty(delAttrIds)) {
						driversService.deleteDriverAttributes(delAttrIds, staffId);
					}		
					responseObject = driversService.uploadDriverAttributesFromCSV(lookupDataFile, Integer.parseInt(driverId), staffId);
					if(!responseObject.isSuccess()) {						
						for(DatabaseDistributionDriverAttribute oldAttrs : oldAttributeList) {
							oldAttrs.setStatus(StateEnum.ACTIVE);
							driversService.updateDriverAttributes(oldAttrs, Integer.parseInt(driverId));	
						}
					}
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
					JSONArray jsonArray = new JSONArray("[" + ajaxResponse.toString() + "]");
					responseStr = jsonArray.toString();
				} catch (Exception e) {
					logger.trace("Problem occured while uploading lookup data file", e);
				}
			} else {
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg("Please upload a valid CSV file");
			}
		}
		return responseStr;
	}	
	private String getAttributeIdsFromList(List<DatabaseDistributionDriverAttribute> list) {
		String atttributeIds = null;
		if(list != null && !list.isEmpty()) {			
			for(DatabaseDistributionDriverAttribute attribute : list) {
				if(atttributeIds == null)
					atttributeIds = String.valueOf(attribute.getId());
				else
					atttributeIds = atttributeIds +BaseConstants.COMMA_SEPARATOR+ attribute.getId();
			}
		}
		return atttributeIds;
	}	
	
	
	
	/**
	 * Update distribution driver order for service	
	 * @param driverOrderList
	 * @return @ResponseBody
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_DISTRIBUTION_DRIVER_ORDER,method= RequestMethod.POST)
	@ResponseBody public String updateDistributionDriverOrder(@RequestParam(value="driverOrderList",required=true) String driverOrderList) throws CloneNotSupportedException{
		
		AjaxResponse ajaxResponse ;
		logger.debug("driverOrderList::"+driverOrderList);
		ResponseObject responseObject = driversService.updateDriversApplicationOrder(driverOrderList);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch and display distribution driver detailed configuration page.
	 * @param driverId
	 * @param driverTypeAlias
	 * @param serviceId
	 * @param serviceName
	 * @param serverInstanceId
	 * @param request
	 * @return
	 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView distributionDriverConfiguration(@RequestParam(value=BaseConstants.DRIVER_ID,required=true) int driverId,
																  	    @RequestParam(value=BaseConstants.DRIVER_TYPE_ALIAS,required=true) String driverTypeAlias,
																  	    @RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
																  	    @RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
																  	  @RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
																  	    @RequestParam(value=BaseConstants.INSTANCE_ID,required=true) String serverInstanceId){
		
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ResponseObject responseObject = driversService.getDriverByTypeAndId(driverId,driverTypeAlias);
			ServerInstance serverInstance = serverInstanceService.getServerInstanceListBySerInsId(Integer.valueOf(serverInstanceId));
			
			if(responseObject.isSuccess()){

				if(EngineConstants.FTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)){
					
					FTPDistributionDriver ftpDistributionDriver = (FTPDistributionDriver) responseObject.getObject();
					if(ftpDistributionDriver != null){
						if(ftpDistributionDriver.getFtpConnectionParams().getPassword()!=null){
							ftpDistributionDriver.getFtpConnectionParams().setPassword(driversService.decryptData(ftpDistributionDriver.getFtpConnectionParams().getPassword()));
						}
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, ftpDistributionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,ftpDistributionDriver.getName());
					model.addObject(BaseConstants.SERVICE_DB_STAT, ftpDistributionDriver.getService().isEnableDBStats());
					}
				}else if (EngineConstants.SFTP_DISTRIBUTION_DRIVER.equals(driverTypeAlias)){
					SFTPDistributionDriver sftpDistributionDriver = (SFTPDistributionDriver) responseObject.getObject();
					if(sftpDistributionDriver != null){
						if(sftpDistributionDriver.getFtpConnectionParams().getPassword()!=null){
							sftpDistributionDriver.getFtpConnectionParams().setPassword(driversService.decryptData(sftpDistributionDriver.getFtpConnectionParams().getPassword()));
						}
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, sftpDistributionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,sftpDistributionDriver.getName());
					model.addObject(BaseConstants.SERVICE_DB_STAT, sftpDistributionDriver.getService().isEnableDBStats());
					}
				}else if(EngineConstants.LOCAL_DISTRIBUTION_DRIVER.equals(driverTypeAlias)){
					LocalDistributionDriver localDistributionDriver = (LocalDistributionDriver) responseObject.getObject();
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, localDistributionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,localDistributionDriver.getName());
					model.addObject(BaseConstants.SERVICE_DB_STAT, localDistributionDriver.getService().isEnableDBStats());
					
				}else if(EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equals(driverTypeAlias)){
					DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) responseObject.getObject();
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, databaseDistributionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,databaseDistributionDriver.getName());
					model.addObject(BaseConstants.SERVICE_DB_STAT, databaseDistributionDriver.getService().isEnableDBStats());
				}else{
					//add code for not matched alias
				}
				
			}else{
				// Add code for failed condition.
			}
			
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.FILE_FETCH_TYPE,Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_ACTION,Arrays.asList(FilterActionEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_TYPE_ACTION,Arrays.asList(FileActionParamEnum.values()));
			model.addObject(BaseConstants.POSITION_ENUM,Arrays.asList(PositionEnum.values()));
			model.addObject( BaseConstants.TRANSFER_MODE,Arrays.asList(FileTransferModeEnum.values()));
			model.addObject("separatorEnum",Arrays.asList(SeparatorEnum.values()));
			model.addObject("resetFreqEnum",Arrays.asList(ControlFileResetFrequency.values()));
			model.addObject("attributesEnum",Arrays.asList(ControlFileAttributesEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,serviceId);
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,driverId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,driverTypeAlias);
			model.addObject(BaseConstants.INSTANCE_ID,serverInstanceId);
			model.addObject("databaseInit",  serverInstance.isDatabaseInit());
		
			return model;
		}
		
		/**
		 * Method will FTP distribution driver configuration.
		 * @param ftpDistributionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_FTP_DISTRIBUTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateFTPDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value="oldDriverName") String driverName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value=BaseConstants.INSTANCE_ID) int instanceId,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) FTPDistributionDriver ftpDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ServerInstance serverInstance = serverInstanceService.getServerInstanceListBySerInsId(Integer.valueOf(instanceId));
			
			logger.debug("found driver type is " + ftpDistributionDriver.getDriverType().getType());
			validator.validateDriverConfiguration(ftpDistributionDriver, result,null,null,false);
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, ftpDistributionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				ftpDistributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateDistributionDriverConfiguration(ftpDistributionDriver);
				
				if(responseObject.isSuccess()){
					FTPDistributionDriver	distributionDriver = (FTPDistributionDriver)responseObject.getObject();
					model.addObject(BaseConstants.DRIVER_NAME,distributionDriver.getName());
					
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
			}
			
			model.addObject(BaseConstants.SERVICE_DB_STAT, ftpDistributionDriver.getService().isEnableDBStats());
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.FILE_FETCH_TYPE,Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_ACTION,Arrays.asList(FilterActionEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_TYPE_ACTION,Arrays.asList(FileActionParamEnum.values()));
			model.addObject(BaseConstants.POSITION_ENUM,Arrays.asList(PositionEnum.values()));
			model.addObject(BaseConstants.TRANSFER_MODE,Arrays.asList(FileTransferModeEnum.values()));
			model.addObject("separatorEnum",Arrays.asList(SeparatorEnum.values()));
			model.addObject("resetFreqEnum",Arrays.asList(ControlFileResetFrequency.values()));
			model.addObject("attributesEnum",Arrays.asList(ControlFileAttributesEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,ftpDistributionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,ftpDistributionDriver.getId());
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,ftpDistributionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			model.addObject("databaseInit",  serverInstance.isDatabaseInit());
			
			return model;
		}	
		
		
		/**
		 * Method will FTP distribution driver configuration.
		 * @param ftpDistributionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_SFTP_DISTRIBUTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateSFTPDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value="oldDriverName") String driverName,
				@RequestParam(value=BaseConstants.INSTANCE_ID) int instanceId,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) SFTPDistributionDriver sftpDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ServerInstance serverInstance = serverInstanceService.getServerInstanceListBySerInsId(Integer.valueOf(instanceId));
			
			validator.validateDriverConfiguration(sftpDistributionDriver, result,null,null,false);
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, sftpDistributionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				sftpDistributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateDistributionDriverConfiguration(sftpDistributionDriver);
				
				if(responseObject.isSuccess()){
					SFTPDistributionDriver	distributionDriver = (SFTPDistributionDriver)responseObject.getObject();
					model.addObject(BaseConstants.DRIVER_NAME,distributionDriver.getName());
					
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
			}
			
			model.addObject(BaseConstants.SERVICE_DB_STAT, sftpDistributionDriver.getService().isEnableDBStats());
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.FILE_FETCH_TYPE,Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_ACTION,Arrays.asList(FilterActionEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_TYPE_ACTION,Arrays.asList(FileActionParamEnum.values()));
			model.addObject(BaseConstants.POSITION_ENUM,Arrays.asList(PositionEnum.values()));
			model.addObject(BaseConstants.TRANSFER_MODE,Arrays.asList(FileTransferModeEnum.values()));
			model.addObject("separatorEnum",Arrays.asList(SeparatorEnum.values()));
			model.addObject("resetFreqEnum",Arrays.asList(ControlFileResetFrequency.values()));
			model.addObject("attributesEnum",Arrays.asList(ControlFileAttributesEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,sftpDistributionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,sftpDistributionDriver.getId());
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,sftpDistributionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			model.addObject("databaseInit",  serverInstance.isDatabaseInit());
			
			return model;
		}	
		
		/**
		 * Method will FTP distribution driver configuration.
		 * @param ftpDistributionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_LOCAL_DISTRIBUTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateLocalDistributionDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value="oldDriverName") String driverName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value=BaseConstants.INSTANCE_ID) int instanceId,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) LocalDistributionDriver localDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ServerInstance serverInstance = serverInstanceService.getServerInstanceListBySerInsId(Integer.valueOf(instanceId));
			
			logger.debug("driver type " + localDistributionDriver.getDriverType().getType());
			validator.validateDriverConfiguration(localDistributionDriver, result,null,null,false);
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, localDistributionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				localDistributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateDistributionDriverConfiguration(localDistributionDriver);
				
				if(responseObject.isSuccess()){
					LocalDistributionDriver	distributionDriver = (LocalDistributionDriver)responseObject.getObject();
					model.addObject(BaseConstants.DRIVER_NAME,distributionDriver.getName());
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
			}
			
			model.addObject(BaseConstants.SERVICE_DB_STAT, localDistributionDriver.getService().isEnableDBStats());
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,localDistributionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject("separatorEnum",Arrays.asList(SeparatorEnum.values()));
			model.addObject("resetFreqEnum",Arrays.asList(ControlFileResetFrequency.values()));
			model.addObject("attributesEnum",Arrays.asList(ControlFileAttributesEnum.values()));
			model.addObject(BaseConstants.DRIVER_ID,localDistributionDriver.getId());
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,localDistributionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			model.addObject("databaseInit",  serverInstance.isDatabaseInit());
			
			return model;
		}
		
		/**
		 * it will update database distribution driver basic details.
		 * @param driverCount
		 * @param distributionDriver
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_DATABASE_DISTRIBUTION_DRIVER, method = RequestMethod.POST)
		@ResponseBody public String updateDatabaseDistributionDriver(@RequestParam(value="driverCount") String driverCount,
				@ModelAttribute(value=FormBeanConstants.DISTRIBUTION_DRIVER_FORM_BEAN) DatabaseDistributionDriver databaseDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
	
				AjaxResponse ajaxResponse =  updateDistributionDriverBasicDetails(databaseDistributionDriver, result, driverCount, request);
				return ajaxResponse.toString();
		}
	
		/**
		 * Method will Database distribution driver configuration.
		 * @param ftpDistributionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_DATABASE_DISTRIBUTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateDatabaseDistributionDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value="oldDriverName") String driverName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value=BaseConstants.INSTANCE_ID) int instanceId,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) DatabaseDistributionDriver databaseDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			
			logger.debug("driver type " + databaseDistributionDriver.getDriverType().getType());
			validator.validateDriverConfiguration(databaseDistributionDriver, result,null,null,false);
			ResponseObject responseObject = new ResponseObject();
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, databaseDistributionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				databaseDistributionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				String driverType = databaseDistributionDriver.getDriverType().getAlias();
				
				if(EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equalsIgnoreCase(driverType)){
				 responseObject = driversService.updateDataBaseDistributionDriverConfiguration(databaseDistributionDriver);
				}
				else{
				 responseObject = driversService.updateDistributionDriverConfiguration(databaseDistributionDriver);
				}
				if(responseObject.isSuccess()){
					DatabaseDistributionDriver	distributionDriver = (DatabaseDistributionDriver)responseObject.getObject();
					model.addObject(BaseConstants.DRIVER_NAME,distributionDriver.getName());
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
			}
			
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,databaseDistributionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,databaseDistributionDriver.getId());
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,databaseDistributionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			
			return model;
		}
		
		/**
		 * Method will redirect the page to Database Attribute List Page.
		 * @param serviceId
		 * @param serviceType
		 * @param serviceName
		 * @param instanceId
		 * @return ModelAndView
		 */
		@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_DRIVER_ATTRLIST_MANAGER,method= RequestMethod.POST)
		public ModelAndView initDistributionDriverAttrlistManager(@RequestParam(value=BaseConstants.DRIVER_ID,required=true) int driverId,
																  	    @RequestParam(value=BaseConstants.DRIVER_TYPE_ALIAS,required=true) String driverTypeAlias,
																  	    @RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
																  	    @RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
																  	  @RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
																  	    @RequestParam(value=BaseConstants.INSTANCE_ID,required=true) String serverInstanceId,
																		@ModelAttribute (value=FormBeanConstants.DRIVER_ATTRIBUTE_FORM_BEAN)DatabaseDistributionDriverAttribute driverAttr){//NOSONAR
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ResponseObject responseObject = driversService.getDriverByTypeAndId(driverId,driverTypeAlias);
			DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) responseObject.getObject();
			model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, databaseDistributionDriver);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION);
			model.addObject(BaseConstants.DRIVER_NAME,databaseDistributionDriver.getName());
			model.addObject("serviceDbStats", databaseDistributionDriver.getService().isEnableDBStats());
			model.addObject("unifiedFieldEnum",UnifiedFieldEnum.values());
			model.addObject("driverDataTypeEnum",java.util.Arrays.asList(DriverDataTypeEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,serviceId);
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,driverId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,driverTypeAlias);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.INSTANCE_ID,serverInstanceId);
			model.addObject(BaseConstants.TRUE_FALSE_ENUM, java.util.Arrays.asList(TrueFalseEnum.values()));	
			model.addObject("paddingTypeEnum",java.util.Arrays.asList(PositionEnum.values()));

			return model;
		}
		/**
		 * Method will add and update database driver attribute.
		 */
		@RequestMapping(value = ControllerConstants.ADD_EDIT_DATABASE_DRIVER_ATTRIBUTE, method = RequestMethod.POST)
		@ResponseBody public  String addEditDatabaseDriverAttribute(
							@ModelAttribute (value=FormBeanConstants.DRIVER_ATTRIBUTE_FORM_BEAN) DatabaseDistributionDriverAttribute driverAttribute, //NOSONAR
							@RequestParam(value = "actionType",required=true) String actionType,
							BindingResult result,
							HttpServletRequest request) throws SMException{
			AjaxResponse ajaxResponse = new AjaxResponse();

			ResponseObject responseObject ;
			validator.validateDriverAttributeParameter(driverAttribute, result, null, false,null);       
			
			if(result.hasErrors()){
				 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			}else{
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject = driversService.createDriverAttributes(driverAttribute, staffId);
				}else{
					responseObject = driversService.updateDriverAttributes(driverAttribute, staffId);
				}
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}
			return ajaxResponse.toString();
		}
		
		/**
		 *  Method will fetch all Driver attribute by driver id
		 * @param service
		 * @param result
		 * @param driverId
		 * @param request
		 * @return Json Ajax response
		 */		
		@RequestMapping(value = ControllerConstants.GET_DRIVER_ATTRIBUTE_GRID_LIST, method = RequestMethod.POST)
		@ResponseBody public  String getDriverAttributeGridList(@RequestParam(value = "driverId" , required = true) int driverId) {
			
			long count =  this.driversService.getAttributeListCountByDriverId(driverId);
			ResponseObject responseObject = new ResponseObject();
			
			List<DatabaseDistributionDriverAttribute> resultList = new ArrayList<>();
			if(count > 0){
				resultList = this.driversService.getAttributeListByDriverId(driverId);
			}
			
			JSONObject jAttributeList = new JSONObject();
			JSONObject jAttrObj;
			JSONArray jAllAttrArr = new JSONArray();
			
			if (resultList != null) {
				for (DatabaseDistributionDriverAttribute attribute : resultList) {
					jAttrObj = new JSONObject();
					jAttrObj.put("id", attribute.getId());
					jAttrObj.put("databaseFieldName", attribute.getDatabaseFieldName());
					jAttrObj.put("unifiedFieldName", attribute.getUnifiedFieldName());
					jAttrObj.put("defualtValue", attribute.getDefualtValue());
					jAttrObj.put("dataType", attribute.getDataType());
					jAttrObj.put("paddingEnable", attribute.isPaddingEnable());	
					jAttrObj.put("length", attribute.getLength());	
					jAttrObj.put("paddingType", attribute.getPaddingType());	
					jAttrObj.put("paddingChar", attribute.getPaddingChar());	
					jAttrObj.put("prefix", attribute.getPrefix());	
					jAttrObj.put("suffix", attribute.getSuffix());
					//jAttrObj.put("dbDisDriver", attribute.getDbDisDriver());
					
					jAllAttrArr.put(jAttrObj);
				}
			}
			
			jAttributeList.put("attributeList", jAllAttrArr);
			responseObject.setObject(jAttributeList);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
					
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
		}
		
		/**
		 * Method will delete attribute by attribute id.
		 * @param attributeid
		 * @return ResponseBody
		 */
		@RequestMapping(value = ControllerConstants.DELETE_DATABASE_DRIVER_ATTRIBUTE, method = RequestMethod.POST)
		@ResponseBody public  String deleteDatabaseDriverAttribute(
							@RequestParam(value = "attributeId", required=true) String attributeIds,
							HttpServletRequest request){
			int staffId = eliteUtils.getLoggedInStaffId(request);
			ResponseObject responseObject = driversService.deleteDriverAttributes(attributeIds, staffId);	
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
		}
		
		
		@PreAuthorize("hasAnyAuthority('UPDATE_DISTRIBUTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.TEST_FTP_SFTP_CONNECTION_FOR_DISTRIBUTION,method= RequestMethod.POST)
		public ModelAndView testFtpSftpConnectionForDistribution(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value="oldDriverName") String driverName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value=BaseConstants.INSTANCE_ID) int instanceId,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) FTPDistributionDriver ftpDistributionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
			ServerInstance serverInstance = serverInstanceService.getServerInstanceListBySerInsId(Integer.valueOf(instanceId));
			String ipAddress=serverInstance.getServer().getIpAddress();
			int utilityPort=serverInstance.getServer().getUtilityPort();
			logger.debug("found driver type is " + ftpDistributionDriver.getDriverType().getType());
			validator.validateConnectionParameter(ftpDistributionDriver.getFtpConnectionParams(),result, null,null,false,driverName);
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, ftpDistributionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				ResponseObject responseObject=driversService.testFtpSftpConnection(ftpDistributionDriver.getFtpConnectionParams(),ftpDistributionDriver.getMaxRetrycount(),ipAddress,utilityPort,ftpDistributionDriver.getDriverType().getAlias());
				if(responseObject.isSuccess()){
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					if(responseObject.getMsg()!=null && !responseObject.getMsg().isEmpty()) {
						model.addObject(BaseConstants.ERROR_MSG,"("+responseObject.getMsg()+") "+getMessage(responseObject.getResponseCode().toString()));
					}else {
						model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
					}
				}
			}
			model.addObject(BaseConstants.SERVICE_DB_STAT, ftpDistributionDriver.getService().isEnableDBStats());
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject(BaseConstants.FILE_FETCH_TYPE,Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject(BaseConstants.TRUE_FALSE_ENUM,Arrays.asList(TrueFalseEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_ACTION,Arrays.asList(FilterActionEnum.values()));
			model.addObject(BaseConstants.FILE_FILTER_TYPE_ACTION,Arrays.asList(FileActionParamEnum.values()));
			model.addObject(BaseConstants.POSITION_ENUM,Arrays.asList(PositionEnum.values()));
			model.addObject(BaseConstants.TRANSFER_MODE,Arrays.asList(FileTransferModeEnum.values()));
			model.addObject("separatorEnum",Arrays.asList(SeparatorEnum.values()));
			model.addObject("resetFreqEnum",Arrays.asList(ControlFileResetFrequency.values()));
			model.addObject("attributesEnum",Arrays.asList(ControlFileAttributesEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,ftpDistributionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,ftpDistributionDriver.getId());
			model.addObject(BaseConstants.INSTANCE_ID,instanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject(BaseConstants.DRIVER_TYPE_ALIAS,ftpDistributionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.DISTRIBUTION_DRIVER_CONFIGURATION);
			model.addObject("databaseInit",  serverInstance.isDatabaseInit());
			
			return model;
		}	

}