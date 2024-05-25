package com.elitecore.sm.drivers.controller;

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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FileFetchTypeEnum;
import com.elitecore.sm.common.model.FileGroupDateTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FileTransferModeEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.DriverType;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.LocalCollectionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.drivers.validator.DriverValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 * Controller class for collection driver
 *
 */
@Controller
public class CollectionDriverController extends BaseController{
	
	@Autowired
	@Qualifier(value="driversService")
	DriversService driversService;
	
	@Autowired
	DriverValidator validator;
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService; 
	
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
	 * Method will redirect the page to Collection Driver Page.
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.INIT_COLLECTION_DRIVER_MANAGER, method = RequestMethod.POST)
	public ModelAndView initCollectionService(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=false) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=false) String serviceName,
    		@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.COLLECTION_SERVICE_MANAGER);
		
		int iserviceId=Integer.parseInt(serviceId);
		ResponseObject responseObject = driversService.getDriversByServiceId(iserviceId);
		
		if(responseObject.isSuccess()){
			model.addObject("driverList",(List<Drivers>)responseObject.getObject());
		}
	
		Service service =  servicesService.getServiceandServerinstance(iserviceId);
		int serverTypeId=service.getServerInstance().getServer().getServerType().getId();
	
		List<DriverType> driverTypeList=(List<DriverType>)eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.COLLECTION_DRIVER_TYPE);
	
		logger.debug("Final collection driver list" +driverTypeList  );
		model.addObject(BaseConstants.COLLECTION_DRIVER_TYPE_LIST,driverTypeList);
		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.STATE_ENUM,java.util.Arrays.asList(StateEnum.values()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.COLLECTION_DRIVER_CONFIGURATION);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		model.addObject("timeout",new CollectionDriver().getTimeout());
		model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG,false);
		
		return model;
	}
	    
	
	
	
	/**
	 * Create Collection Driver
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.CREATE_COLLECTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String createCollectionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_DRIVER_FORM_BEAN) CollectionDriver collectionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateDriverParameter(collectionDriver, result, null, null, false);
		
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
			collectionDriver.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			collectionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			collectionDriver.setCreatedDate(new Date());
			ResponseObject responseObject=driversService.addCollectionDriver(collectionDriver);
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}

	/**
	 * Update FTP Collection Driver basic details.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public  String updateFTPCollectionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_DRIVER_FORM_BEAN) CollectionDriver ftpCollectionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
		AjaxResponse ajaxResponse =	updateCollectionDriverBasicDetails(ftpCollectionDriver, result, driverCount, request);
		return ajaxResponse.toString();
	}
		
	/**
	 * Update SFTP Collection Driver basic details.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_SFTP_COLLECTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public  String updateSFTPCollectionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_DRIVER_FORM_BEAN) CollectionDriver sftpCollectionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
			AjaxResponse ajaxResponse =	updateCollectionDriverBasicDetails(sftpCollectionDriver, result, driverCount, request);
			return ajaxResponse.toString();
		}
	
	/**
	 * Update LOCAL Collection Driver basic details.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_LOCAL_COLLECTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public  String updateLocalCollectionDriver(@RequestParam(value="driverCount") String driverCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_DRIVER_FORM_BEAN) CollectionDriver localCollectionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
		AjaxResponse ajaxResponse =	updateCollectionDriverBasicDetails(localCollectionDriver, result, driverCount, request);
		return ajaxResponse.toString();
	}
	
	
	
	/**
	 * It will update all collection driver basic details.
	 * @param collectionDriver
	 * @param result
	 * @param driverCount
	 * @param request
	 * @return
	 */
	private AjaxResponse updateCollectionDriverBasicDetails(CollectionDriver collectionDriver, BindingResult result, String driverCount, HttpServletRequest request){
		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateDriverParameter(collectionDriver, result, null, null, false);
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
			collectionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=driversService.updateCollectionDriver(collectionDriver);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse;
	}
	
	/**
	 * Call when click on FTP Driver Configuration tab
	 * @param driverId
	 * @param driverTypeAlias
	 * @param serviceId
	 * @param serviceName
	 * @param serverInstanceId
	 * @param request
	 * @return
	 */
		@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.INIT_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView initDriverConfig(@RequestParam(value=BaseConstants.DRIVER_ID,required=true) String driverId,
																  	    @RequestParam(value="driverTypeAlias",required=true) String driverTypeAlias,
																  	    @RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
																  	    @RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
																  	  @RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
																  	   @RequestParam(value="serverInstanceId",required=true) String serverInstanceId
																        ){
		
			ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
			int iDriverId=Integer.parseInt(driverId);
			
			ResponseObject responseObject = driversService.getDriverByTypeAndId(iDriverId,driverTypeAlias);
			if(responseObject.isSuccess()){
				if(EngineConstants.FTP_COLLECTION_DRIVER.equalsIgnoreCase(driverTypeAlias)){
					
					FTPCollectionDriver ftpCollectionDriver=(FTPCollectionDriver)responseObject.getObject();
					if(ftpCollectionDriver!=null){
						if(ftpCollectionDriver.getFtpConnectionParams().getPassword()!=null){
								ftpCollectionDriver.getFtpConnectionParams().setPassword(driversService.decryptData(ftpCollectionDriver.getFtpConnectionParams().getPassword()));
						}
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, ftpCollectionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,ftpCollectionDriver.getName());
					model.addObject("serviceDbStats", ftpCollectionDriver.getService().isEnableDBStats());
					model.addObject("forDuplicatEnabled", ftpCollectionDriver.getFileGroupingParameter().isEnableForDuplicate());
					}
				}else if (EngineConstants.SFTP_COLLECTION_DRIVER.equalsIgnoreCase(driverTypeAlias)){
					
					SFTPCollectionDriver sftpCollectionDriver=(SFTPCollectionDriver)responseObject.getObject();
					if(sftpCollectionDriver!=null){
						if(sftpCollectionDriver.getFtpConnectionParams().getPassword()!=null){
							sftpCollectionDriver.getFtpConnectionParams().setPassword(driversService.decryptData(sftpCollectionDriver.getFtpConnectionParams().getPassword()));
						}
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, sftpCollectionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,sftpCollectionDriver.getName());
					model.addObject("serviceDbStats", sftpCollectionDriver.getService().isEnableDBStats());
					model.addObject("forDuplicatEnabled", sftpCollectionDriver.getFileGroupingParameter().isEnableForDuplicate());
					}
				}else if(EngineConstants.LOCAL_COLLECTION_DRIVER.equalsIgnoreCase(driverTypeAlias)){
					
					LocalCollectionDriver localCollectionDriver=(LocalCollectionDriver)responseObject.getObject();
					model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN, localCollectionDriver);
					model.addObject(BaseConstants.DRIVER_NAME,localCollectionDriver.getName());
					model.addObject("serviceDbStats", localCollectionDriver.getService().isEnableDBStats());
					model.addObject("forDuplicatEnabled", localCollectionDriver.getFileGroupingParameter().isEnableForDuplicate());
				}
				
			}else{
				//add message
			}
			
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FTP_CONFIGURATION);
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
			model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
			model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
			model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
			model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,serviceId);
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,driverId);
			model.addObject("driverTypeAlias",driverTypeAlias);
			model.addObject("serverInstanceId",serverInstanceId);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		
			return model;
		}
		
		/**
		 * Update FTP Driver configuration
		 * @param collectionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value="oldDriverName") String driverName,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) FTPCollectionDriver collectionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
			
			logger.debug("collection driver type"+collectionDriver.getDriverType().getType());
			validator.validateDriverConfiguration(collectionDriver, result,null,null,false);
			if(result.hasErrors()){
				logger.debug("collection driver type"+collectionDriver.getDriverType().getType());
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN,collectionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				collectionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateCollectionDriverConfiguration(collectionDriver);
				
				if(responseObject.isSuccess()){
					collectionDriver=(FTPCollectionDriver)responseObject.getObject();
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
				model.addObject(BaseConstants.DRIVER_NAME,collectionDriver.getName());
			}
			
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
			model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
			model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
			model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
			model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,collectionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,collectionDriver.getId());
			model.addObject("driverTypeAlias",collectionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FTP_CONFIGURATION);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject("serviceDbStats", collectionDriver.getService().isEnableDBStats());
			model.addObject("forDuplicatEnabled", collectionDriver.getFileGroupingParameter().isEnableForDuplicate());
			return model;
			
		}
		
		
		/**
		 * Update FTP Driver configuration
		 * @param collectionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_SFTP_COLLECTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateSFTPCollectionDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value="oldDriverName") String driverName,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) SFTPCollectionDriver collectionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
			
			logger.debug("driver type"+collectionDriver.getDriverType().getType());
			validator.validateDriverConfiguration(collectionDriver, result,null,null,false);
			if(result.hasErrors()){
				logger.debug("driver type"+collectionDriver.getDriverType().getType());
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN,collectionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);

			}else{
				collectionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateCollectionDriverConfiguration(collectionDriver);
				
				if(responseObject.isSuccess()){
					collectionDriver= (SFTPCollectionDriver)responseObject.getObject();
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
					
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
				model.addObject(BaseConstants.DRIVER_NAME,collectionDriver.getName());

			}
			
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
			model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
			model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
			model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
			model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,collectionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,collectionDriver.getId());
			model.addObject("driverTypeAlias",collectionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FTP_CONFIGURATION);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject("serviceDbStats", collectionDriver.getService().isEnableDBStats());
			model.addObject("forDuplicatEnabled", collectionDriver.getFileGroupingParameter().isEnableForDuplicate());
			return model;
			
		}
		
		/**
		 * Update Local Driver configuration
		 * @param collectionDriver
		 * @param serviceName
		 * @param result
		 * @param request
		 * @return
		 */
		@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
		@RequestMapping(value = ControllerConstants.UPDATE_LOCAL_COLLECTION_DRIVER_CONFIGURATION,method= RequestMethod.POST)
		public ModelAndView updateLocalDriverConfiguration(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
				@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
				@RequestParam(value="oldDriverName") String driverName,
				@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) LocalCollectionDriver collectionDriver,//NOSONAR
				BindingResult result,HttpServletRequest request){
			
			ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
			
			validator.validateDriverConfiguration(collectionDriver, result,null,null,false);
			
			if(result.hasErrors()){
				model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN,collectionDriver);
				model.addObject(BaseConstants.DRIVER_NAME,driverName);
			}else{
				collectionDriver.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				ResponseObject responseObject = driversService.updateCollectionDriverConfiguration(collectionDriver);
				
				if(responseObject.isSuccess()){
					collectionDriver=(LocalCollectionDriver)responseObject.getObject();
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
					
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
				model.addObject(BaseConstants.DRIVER_NAME,collectionDriver.getName());
				
			}
			
			model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
			model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
			model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
			model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
			model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
			model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
			model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
			model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
			model.addObject(BaseConstants.SERVICE_ID,collectionDriver.getService().getId());
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(BaseConstants.DRIVER_ID,collectionDriver.getId());
			model.addObject("driverTypeAlias",collectionDriver.getDriverType().getAlias());
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FTP_CONFIGURATION);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject("serviceDbStats", collectionDriver.getService().isEnableDBStats());
			model.addObject("forDuplicatEnabled", collectionDriver.getFileGroupingParameter().isEnableForDuplicate());
			return model;
			
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
		@RequestMapping(value = ControllerConstants.GET_COLLECTION_DRIVER_LIST, method = RequestMethod.GET)
		@ResponseBody public  String getCollectionDriverList(
				@RequestParam(value = BaseConstants.SERVICE_ID, required=false) String serviceInstanceId,
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx" , required=false) String sidx,
				@RequestParam(value = "sord" , required=false) String sord
				) {
			logger.debug(">> getCollectionDriverList in CollectionDriverController " +serviceInstanceId); 
			
			int iServiceId=0;
			if(!StringUtils.isEmpty(serviceInstanceId)){
				iServiceId=Integer.parseInt(serviceInstanceId);
			}
			long count =  this.driversService.getDriversTotalCount(iServiceId);
			List<Map<String, Object>> rowList = null ;
			if(count > 0){
				rowList = this.driversService.getDriversPaginatedList(iServiceId, 
																		eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
																		limit, sidx,sord); 
			}
			logger.debug("<< getCollectionDriverList in CollectionDriverController ");
			return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();
		}
	
		
	/**
	 * Update collection driver order for service	
	 * @param driverOrderList
	 * @return Update collection driver application order response as ajax response
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_FTP_COLLECTION_DRIVER_ORDER,method= RequestMethod.POST)
	@ResponseBody public String updateDriverConfigurationOrder(@RequestParam(value="driverOrderList",required=true) String driverOrderList
			) throws CloneNotSupportedException{
		
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject = driversService.updateDriversApplicationOrder(driverOrderList);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will delete Collection Driver and update its application orders
	 * @param DriverId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.DELETE_COLLECTION_DRIVER, method = RequestMethod.POST)
	@ResponseBody public String deleteCollectionDriver(
						@RequestParam(value = BaseConstants.DRIVER_ID,required=true) String driverId,
						@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId){
		
			int iDriverId=Integer.parseInt(driverId);
			int iServiceId=Integer.parseInt(serviceId);
		
			 ResponseObject responseObject = driversService.deleteDriverDetails(iDriverId,iServiceId);
			  AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will update Collection Driver status 
	 * @param DriverId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.UPDATE_COLLECTION_DRIVER_STATUS, method = RequestMethod.POST)
	@ResponseBody public  String updateCollectionDriverStatus(
						@RequestParam(value = BaseConstants.DRIVER_ID,required=true) String driverId,
						@RequestParam(value = "driverType",required=true) String driverType,
						@RequestParam(value = "driverStatus",required=true) String driverStatus) throws CloneNotSupportedException{
		
		int iDriverId=Integer.parseInt(driverId);
		
		 ResponseObject responseObject = driversService.updateDriverStatus(iDriverId, driverType, driverStatus);
		 AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		   
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.TEST_FTP_SFTP_CONNECTION_FOR_COLLECTION,method= RequestMethod.POST)
	public ModelAndView testFtpSftpConnectionForCollection(@RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
			@RequestParam(value="oldDriverName") String driverName,
			@ModelAttribute(value=FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN) FTPCollectionDriver collectionDriver,//NOSONAR
			BindingResult result,HttpServletRequest request){
		
		ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
		Service service =  servicesService.getServiceandServerinstance(collectionDriver.getService().getId());
		int utilityPort=service.getServerInstance().getServer().getUtilityPort();
		String ipAddress=service.getServerInstance().getServer().getIpAddress();
		validator.validateConnectionParameter(collectionDriver.getFtpConnectionParams(),result, null,null,false,driverName);
		
		if(result.hasErrors()){
			model.addObject(FormBeanConstants.DRIVER_CONFIGURATION_FORM_BEAN,collectionDriver);
			model.addObject(BaseConstants.DRIVER_NAME,driverName);
		}else{
				ResponseObject responseObject=driversService.testFtpSftpConnection(collectionDriver.getFtpConnectionParams(),collectionDriver.getMaxRetrycount(),ipAddress,utilityPort,collectionDriver.getDriverType().getAlias());
				if(responseObject.isSuccess()){
					model.addObject(BaseConstants.RESPONSE_MSG,getMessage(responseObject.getResponseCode().toString()));
				}else{
					model.addObject(BaseConstants.ERROR_MSG,getMessage(responseObject.getResponseCode().toString()));
				}
				model.addObject(BaseConstants.DRIVER_NAME,collectionDriver.getName());
		}
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
		model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
		model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
		model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
		model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID,collectionDriver.getService().getId());
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.DRIVER_ID,collectionDriver.getId());
		model.addObject("driverTypeAlias",collectionDriver.getDriverType().getAlias());
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FTP_CONFIGURATION);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		model.addObject("serviceDbStats", collectionDriver.getService().isEnableDBStats());
		model.addObject("forDuplicatEnabled", collectionDriver.getFileGroupingParameter().isEnableForDuplicate());
		return model;
	}

	
}
