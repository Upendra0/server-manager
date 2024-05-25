package com.elitecore.sm.netflowclient.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.ContentFormatTypeEnum;
import com.elitecore.sm.common.model.MessageTypeEnum;
import com.elitecore.sm.common.model.ProxySchemaTypeEnum;
import com.elitecore.sm.common.model.RequestTypeEnum;
import com.elitecore.sm.common.model.RollingTypeEnum;
import com.elitecore.sm.common.model.SecurityTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.kafka.datasource.service.KafkaDataSourceService;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.service.NetflowClientService;
import com.elitecore.sm.netflowclient.validator.NetflowClientValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class NetflowClientController extends BaseController{
	
	
	@Autowired
	@Qualifier(value="netfloewClientService")
	private NetflowClientService clientService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private NetflowClientValidator validator;
	
	@Autowired
	@Qualifier(value = "kafkaDataSourceService")
	private KafkaDataSourceService kafkaDataSourceService;
	
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
		binder.setValidator(validator);
	}
	
	
	/**
	 * Netflow client list for netflow collection service
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param serviceId
	 * @param request
	 * @return Ajax netflow client list
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.GET_NETFLOW_COLLECTION_CLIENT_LIST, method = RequestMethod.GET)
	public @ResponseBody String getNetflowClientList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx") String sidx,
			@RequestParam(value = "sord") String sord,
			@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId,
			HttpServletRequest request) {
		
		int iserviceId=Integer.parseInt(serviceId);
		long count = this.clientService.getTotalClientCount(iserviceId);

		logger.info("count: " + count);
		List<NetflowClient> resultList = new ArrayList<>();
		if (count > 0)
			resultList = this.clientService.getPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,iserviceId);

		Map<String, Object> row = null;

		List<Map<String, Object>> rowList = new ArrayList<>();
		count = 0;
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (NetflowClient client : resultList) {
				row = new HashMap<>();
				
				count++;
				row.put("id",client.getId());
				row.put("clientName", client.getName());
				row.put("clientIp", client.getClientIpAddress());
				row.put("clientPort", client.getClientPort());
				row.put("fileLoc", client.getOutFileLocation());
				row.put("status", String.valueOf(client.getStatus()));
				if(client.getTopicName()!=null && !client.getTopicName().isEmpty()) {
					row.put("topicName", String.valueOf(client.getTopicName()));
				}
				if(client.getResourcesName()!=null && !client.getResourcesName().isEmpty()) {
					row.put("resourcesName", String.valueOf(client.getResourcesName()));
				}
				
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	
	/**
	 * Return collection client list for service
	 * @param serviceId
	 * @param request
	 * @return Client list response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.GET_NETFLOW_CLIENT_FOR_SERVICE,method= RequestMethod.POST)
	public ModelAndView getNetfloeClientList(
			@RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.NETFLOW_COLLECTION_SERVICE_MANAGER);
		
		int iserviceId=Integer.parseInt(serviceId);
		
		ResponseObject responseObject=clientService.getClientListForService(iserviceId);
		model.addObject("clientList",responseObject.getObject());
		
		Service service = servicesService.getServiceandServerinstance(iserviceId);

		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject("client", new NetflowClient());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.STATE_ENUM, java.util.Arrays.asList(StateEnum.values()));
		model.addObject("rollingTypeEnum", java.util.Arrays.asList(RollingTypeEnum.values()));
		model.addObject("requestTypeEnum", java.util.Arrays.asList(RequestTypeEnum.values()));
		model.addObject("contentFormatTypeEnum", java.util.Arrays.asList(ContentFormatTypeEnum.values()));
		model.addObject("messageTypeEnum", java.util.Arrays.asList(MessageTypeEnum.values()));
		model.addObject("securityTypeEnum", java.util.Arrays.asList(SecurityTypeEnum.values()));
		model.addObject("proxySchemaTypeEnum", java.util.Arrays.asList(ProxySchemaTypeEnum.values()));
		model.addObject("kafkaDataSourceList", kafkaDataSourceService.getKafkaDataSourceConfigList().getObject());
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.NETFLOW_CLIENT_CONFIGURATION);
		model.addObject(BaseConstants.INSTANCE_ID,service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			
		return model;		
	}
	
	
	/**
	 * Update collection client detail in database
	 * @param clientCount
	 * @param serviceType
	 * @param netflowClient
	 * @param result
	 * @param request
	 * @return Update client response as Ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_NETFLOW_COLLECTION_CLIENT, method = RequestMethod.POST)
	public @ResponseBody String updateCollectionClient(
			@RequestParam(value="clientCount") String clientCount,
			@RequestParam(value=BaseConstants.SERVICE_TYPE) String serviceType,
			@RequestParam(value="kafkaDataSourceId") String kafkaDataSourceId,
		    @ModelAttribute(value=FormBeanConstants.NETFLOW_COLLECTION_CLIENT_FORM_BEAN) NetflowClient netflowClient,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		int kafkaDataSourceID = 0;
		try {
			kafkaDataSourceID = Integer.parseInt(kafkaDataSourceId);
		} catch (Exception e) {
			logger.error(e);
			kafkaDataSourceID = 0;
		}
		if(kafkaDataSourceID>0) {
			if(netflowClient.isEnableKafka()) {
				KafkaDataSourceConfig kafkaDSConfig = kafkaDataSourceService.getKafkaDataSourceConfigById(kafkaDataSourceID);
				netflowClient.setKafkaDataSourceConfig(kafkaDSConfig);
			} else {
				netflowClient.setKafkaDataSourceConfig(null);
			}
		}
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateNetflowClient(netflowClient, serviceType,result,null,null,false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(clientCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(clientCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			netflowClient.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=clientService.updateNetflowClient(netflowClient,serviceType);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Create Collection client for service
	 * @param clientCount
	 * @param netflowClient
	 * @param result
	 * @param request
	 * @return Create client response as Ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.CREATE_NETFLOW_COLLECTION_CLIENT, method = RequestMethod.POST)
	public @ResponseBody String createNetflowCollectionClient(
			@RequestParam(value="clientCount") String clientCount,
			@RequestParam(value=BaseConstants.SERVICE_TYPE) String serviceType,
			@RequestParam(value="kafkaDataSourceId") String kafkaDataSourceId,
			@ModelAttribute(value=FormBeanConstants.NETFLOW_COLLECTION_CLIENT_FORM_BEAN) NetflowClient client,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		int kafkaDataSourceID = 0;
		try {
			kafkaDataSourceID = Integer.parseInt(kafkaDataSourceId);
		} catch (Exception e) {
			logger.error(e);
			kafkaDataSourceID = 0;
		}
		if(kafkaDataSourceID>0) {
			if(client.isEnableKafka()) {
				KafkaDataSourceConfig kafkaDSConfig = kafkaDataSourceService.getKafkaDataSourceConfigById(kafkaDataSourceID);
				client.setKafkaDataSourceConfig(kafkaDSConfig);
			}
		}
		AjaxResponse ajaxResponse=new AjaxResponse();
	
		validator.validateNetflowClient(client, serviceType,result,null,null,false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(clientCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(clientCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			client.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			client.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			client.setCreatedDate(new Date());
			ResponseObject responseObject=clientService.addCollectionClient(client,serviceType);
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Delete Collection client from database
	 * @param DriverId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.DELETE_NETFLOW_CLIENT, method = RequestMethod.POST)
	public @ResponseBody String deleteNetflowClient(
						@RequestParam(value = "clientId",required=true) String clientId
						){
		
		int iclientId=Integer.parseInt(clientId);
		ResponseObject responseObject = clientService.deleteClientDetails(iclientId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
	
	/**
	 * Update collection client status in database
	 * @param DriverId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_NETFLOW_CLIENT_STATUS, method = RequestMethod.POST)
	public @ResponseBody String updateNetflowClientSts(
						@RequestParam(value = "clientId",required=true) String clientId,
						@RequestParam(value = "status",required=true) String status
						){
		
		int iclientId=Integer.parseInt(clientId);
		ResponseObject responseObject = clientService.updtClientStatus(iclientId,status);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
		
}
