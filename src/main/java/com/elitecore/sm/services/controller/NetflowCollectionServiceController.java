package com.elitecore.sm.services.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.CoAPCollectionService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.Http2CollectionService;
import com.elitecore.sm.services.model.MqttCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.RadiusCollectionService;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.Utilities;


/**
 * @author vishal.lakhyani
 *
 */
/**
 * @author elitecore
 *
 */
@Controller
public class NetflowCollectionServiceController extends BaseController {
	
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired
	private ServiceValidator validator;
	
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
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));

	}
	
	/**
	 * Redirect to netflow collection service summary page 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_NETFLOW_COLLECTION_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initCollectionService(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId
    		) {
		
		logger.info("Comes to netflow coll service manager");
		ModelAndView model = new ModelAndView();
		boolean notNetflowsubclass=false;
		
		NetflowBinaryCollectionService netflowService = null;
		 int iserviceId=Integer.parseInt(serviceId);
		if(EngineConstants.NATFLOW_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_COLLECTION_SERVICE_MANAGER);
			netflowService = (NetflowCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);
			
		} else if(EngineConstants.SYSLOG_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.SYSLOG_COLLECTION_SERVICE_MANAGER);
			netflowService = (SysLogCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);
		
		} else if(EngineConstants.MQTT_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.MQTT_COLLECTION_SERVICE_MANAGER);
			netflowService = (MqttCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);			

		} else if(EngineConstants.COAP_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.COAP_COLLECTION_SERVICE_MANAGER);
			netflowService = (CoAPCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);			
	
		} else if(EngineConstants.HTTP2_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.HTTP2_COLLECTION_SERVICE_MANAGER);
			netflowService = (Http2CollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);			
	
		} else if(EngineConstants.GTPPRIME_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.GTP_PRIME_COLLECTION_SERVICE_MANAGER);
			netflowService = (GTPPrimeCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);
			
		} else if(EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (NetflowBinaryCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.NETFLOW_BINARY_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,netflowService);
		} else if(EngineConstants.RADIUS_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.RADIUS_COLLECTION_SERVICE_MANAGER);
			netflowService = (RadiusCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.RADIUS_COLLECTION_SERVICE_FORM_BEAN,netflowService);
		}else {
			notNetflowsubclass=true;
		}
		
		if(notNetflowsubclass){
			model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
			model.addObject(BaseConstants.ERROR_MSG, "Service type not matching with this page, received service type is" + serviceType);
				
		} else {
		// one of Netflow  collection supported class
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject("lastUpdateTime",DateFormatter.formatDate(netflowService.getLastUpdatedDate()));
		model.addObject("syncStatus",netflowService.isSyncStatus());
		model.addObject("enableFileStats",netflowService.isEnableFileStats());
		model.addObject("enableDBStats",netflowService.isEnableDBStats());
		model.addObject(BaseConstants.SERVICE_INST_ID, netflowService.getServInstanceId());
		}
		
		return model;
	}
	
	/** Open netflow coll / netflow binary coll service configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init service configuration update response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initIpdtNetflowCollectionConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType
			) {

		ModelAndView model = new ModelAndView();
		boolean notNetflowsubclass=false;
		
		NetflowBinaryCollectionService netflowService = null;
		 int iserviceId=Integer.parseInt(serviceId);
		
		if (EngineConstants.NATFLOW_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_COLLECTION_SERVICE_MANAGER);
			netflowService = (NetflowCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);
			
		} else if(EngineConstants.SYSLOG_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (SysLogCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);
		
		} else if(EngineConstants.MQTT_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (MqttCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);			
		
		} else if(EngineConstants.COAP_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (CoAPCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);			
	
		} else if(EngineConstants.HTTP2_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			Http2CollectionService http2CollService = (Http2CollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			if(http2CollService.getKeystorePassword()!=null) {
				http2CollService.setKeystorePassword(Utilities.decryptData(http2CollService.getKeystorePassword()));
			}
			if(http2CollService.getKeymanagerPassword()!=null) {
				http2CollService.setKeymanagerPassword(Utilities.decryptData(http2CollService.getKeymanagerPassword()));
			}
			netflowService = http2CollService;
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);			
	
		} else if (EngineConstants.NATFLOWBINARY_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)) {

			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (NetflowBinaryCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);
			
		}else if(EngineConstants.GTPPRIME_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (GTPPrimeCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);
			
		}else if(EngineConstants.RADIUS_COLLECTION_SERVICE.equalsIgnoreCase(serviceType)){
			
			model.setViewName(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
			netflowService = (RadiusCollectionService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,netflowService);
			
		}  else {
			notNetflowsubclass = true;
		}

		if (notNetflowsubclass) {
			model = new ModelAndView(ViewNameConstants.GENERIC_ERROR_PAGE);
			model.addObject(BaseConstants.ERROR_MSG,
					"Service type not matching with this page, received service type is"
							+ serviceType);

		} else {
			// one of Netflow collection supported class

			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			model.addObject("truefalseEnum",java.util.Arrays.asList(TrueFalseEnum.values()));
			model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
			model.addObject("enableFileStats",netflowService.isEnableFileStats());
			model.addObject("enableDBStats", netflowService.isEnableDBStats());
			model.addObject("lastUpdateTime", DateFormatter.formatDate(netflowService.getLastUpdatedDate()));
			model.addObject(BaseConstants.SERVICE_ID, serviceId);
			model.addObject(BaseConstants.SERVICE_TYPE, netflowService.getSvctype().getAlias());
			model.addObject(BaseConstants.SERVICE_NAME, netflowService.getName());
			model.addObject(BaseConstants.INSTANCE_ID, netflowService.getServerInstance().getId());
		}
	return model;
	}
	
	/**
	 * Update Netflow Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") NetflowCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(collectionService,result,null,false);
		NetflowCollectionService locCollectionServiceObj=collectionService;
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionServiceObj);
		}else{
			locCollectionServiceObj.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateNetflowCollectionServiceConfiguration(locCollectionServiceObj);
			
			if(responseObject.isSuccess()){
				locCollectionServiceObj=(NetflowCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionServiceObj.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionServiceObj);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update Netflow Binary Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_NETFLOWBINARY_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateBinaryColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") NetflowBinaryCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		 NetflowBinaryCollectionService localCollectionService=collectionService;
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(collectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,localCollectionService);
		}else{
			localCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateNetflowBinaryCollectionServiceConfiguration(localCollectionService);
			
			if(responseObject.isSuccess()){
				localCollectionService=(NetflowBinaryCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,localCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,localCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update Syslog Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_SYSLOG_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateSyslogColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") SysLogCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		SysLogCollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateSyslogCollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(SysLogCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update Mqtt Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_MQTT_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateMqttColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") MqttCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		MqttCollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateMqttCollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(MqttCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update CoAP Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_COAP_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateCoAPColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") CoAPCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		CoAPCollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateCoAPCollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(CoAPCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update Http2 Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_HTTP2_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateHttp2ColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") Http2CollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		Http2CollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			Http2CollectionService dbservice = (Http2CollectionService) servicesService.getServiceById(collectionService.getId());
			if(locCollectionService.getKeystorePassword()!=null) {
				locCollectionService.setKeystorePassword(Utilities.encryptData(locCollectionService.getKeystorePassword()));
			}
			if(locCollectionService.getKeymanagerPassword()!=null) {
				locCollectionService.setKeymanagerPassword(Utilities.encryptData(locCollectionService.getKeymanagerPassword()));
			}
			locCollectionService.setKeystoreFile(dbservice.getKeystoreFile());
			locCollectionService.setKeystoreFileName(dbservice.getKeystoreFileName());
			locCollectionService.setKeystoreFilePath(dbservice.getKeystoreFilePath());
			ResponseObject responseObject=servicesService.updateHttp2CollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(Http2CollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPLOAD_KEY_STORE_FILE, method = RequestMethod.POST)
	@ResponseBody
	public  String uploadKeyStoreFile(
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			@RequestParam(value = "serviceId", required = true) int serviceId,
			HttpServletRequest request ) throws SMException, SerialException, SQLException, IOException{
		    ResponseObject responseObject = new ResponseObject();
			if (!multipartFile.isEmpty() ){
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File keyStoreFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					multipartFile.transferTo(keyStoreFile);
					try {
						Http2CollectionService http2CollService = (Http2CollectionService) servicesService.getServiceById(serviceId);
						int serverInstancePort = http2CollService.getServerInstance().getPort();
						String keystoreFilePath = File.separator + BaseConstants.KEYSTORE_FOLDER + File.separator + BaseConstants.HTTP2_COLLECTION_SERVICE_FOLDER + File.separator + serverInstancePort + File.separator + http2CollService.getServInstanceId() + File.separator + multipartFile.getOriginalFilename(); 
						http2CollService.setKeystoreFile(EliteUtils.convertFileToByteArray(keyStoreFile));
						http2CollService.setKeystoreFileName(multipartFile.getOriginalFilename());
						http2CollService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
						http2CollService.setKeystoreFilePath(keystoreFilePath);
						responseObject=servicesService.updateHttp2CollectionServiceConfiguration(http2CollService);
						if (responseObject.isSuccess()) {
							responseObject.setObject(keyStoreFile.getName());
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.KEY_STORE_FILE_UPLOAD_SUCCESS);
						}
					} catch (Exception e) {//NOSONAR
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.KEY_STORE_FILE_UPLOAD_FAILURE);
						logger.info("failed to upload key store file !!");
					}
					
			}
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
	}
	
	/**
	 * Update GTPPrime Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_GTPPRIME_COLLECTION_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateGtpprimeColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") GTPPrimeCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		GTPPrimeCollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateGtpPrimeCollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(GTPPrimeCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/**
	 * Update Syslog Collection service configuration
	 * @param collectionService
	 * @param result
	 * @param status
	 * @param request
	 * @return Update service configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_RADIUS_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateRadiusColServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value="service_config_form_bean") RadiusCollectionService collectionService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		RadiusCollectionService locCollectionService=collectionService;
		
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(locCollectionService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION_FORM_BEAN,locCollectionService);
		}else{
			locCollectionService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateRadiusCollectionServiceConfiguration(locCollectionService);
			
			if(responseObject.isSuccess()){
				locCollectionService=(RadiusCollectionService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,locCollectionService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,locCollectionService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_COLLECTION_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/** Add common service param to model object
	 * @param model
	 * @param collectionService
	 */
	private void addCommonParamToModel(ModelAndView model,NetflowBinaryCollectionService collectionService){
		
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("startupMode", java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("lastUpdateTime",DateFormatter.formatDate(collectionService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, collectionService.getId());
		model.addObject(BaseConstants.SERVICE_TYPE, collectionService.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, collectionService.getServerInstance().getId());
		model.addObject("enableFileStats",collectionService.isEnableFileStats());
		model.addObject("enableDBStats",collectionService.isEnableDBStats());
		model.addObject(BaseConstants.SERVICE_INST_ID, collectionService.getServInstanceId());
	}
}
