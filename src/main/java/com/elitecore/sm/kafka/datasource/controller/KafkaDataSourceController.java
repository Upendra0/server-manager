package com.elitecore.sm.kafka.datasource.controller;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.kafka.datasource.service.KafkaDataSourceService;
import com.elitecore.sm.kafka.datasource.validator.KafkaDataSourceValidator;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class KafkaDataSourceController extends BaseController {
	
	@Autowired
	@Qualifier(value = "kafkaDataSourceService")
	private KafkaDataSourceService kafkaDataSourceService;

	@Autowired
	private KafkaDataSourceValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}
	
	/**
	 * Create Kafka DataSource 
	 * @param kafkaDetailListCounter
	 * @param kafkaDataSourceConfig
	 * @param result 
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_KAFKA_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.CREATE_KAFKA_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public String createKafkaDataSourceConfiguration(
			@RequestParam(value="kafkaDetailListCounter") String kafkaDetailListCounter ,
			@ModelAttribute(value=FormBeanConstants.KAFKA_DS_CONFIG_BEAN) KafkaDataSourceConfig kafkaDataSourceConfig,//NOSONAR
			BindingResult result){


		AjaxResponse ajaxResponse=new AjaxResponse();

		validator.validateKafkaDataSourceConfigParameters(kafkaDataSourceConfig, result, null, null, false);

		if(result.hasErrors()){

			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){

				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){

					errorMsgs.put(kafkaDetailListCounter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));

				}else{
					errorMsgs.put(kafkaDetailListCounter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			ResponseObject responseObject = kafkaDataSourceService.addKafkaDataSourceConfig(kafkaDataSourceConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Update KafkaDataSource Details 
	 * @param kafkaDetailListCounter
	 * @param kafkaDataSourceConfig
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_KAFKA_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_KAFKA_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public  String updateKafkaDataSourceConfiguration(@RequestParam(value="kafkaDetailListCounter") String kafkaDetailListCounter,
			@ModelAttribute(value=FormBeanConstants.KAFKA_DS_CONFIG_BEAN) KafkaDataSourceConfig kafkaDataSourceConfig,//NOSONAR
			BindingResult result,HttpServletRequest request){

		AjaxResponse ajaxResponse=new AjaxResponse();

		validator.validateKafkaDataSourceConfigParameters(kafkaDataSourceConfig, result, null, null, false);

		if(result.hasErrors()){

			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(kafkaDetailListCounter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(kafkaDetailListCounter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			kafkaDataSourceConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));

			ResponseObject responseObject=kafkaDataSourceService.updateKafkaDataSourceConfig(kafkaDataSourceConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}


	/**
	 * Delete KafkaDataSource Details  
	 * @param deleteKafkaDataSourceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_KAFKA_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.DELETE_KAFKA_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public String deleteKafkaDataSourceConfiguration(@RequestParam(value = BaseConstants.DATASOURCE_ID,required=true) String deleteKafkaDataSourceId){

		int iKafkaDataSourceId=Integer.parseInt(deleteKafkaDataSourceId);
		ResponseObject responseObject=kafkaDataSourceService.deleteKafkaDataSourceConfig(iKafkaDataSourceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.GET_KAFKA_DATASOURCE_ASSOCIATED_CLIENT_LIST, method = RequestMethod.GET)
	@ResponseBody public  String getServerInstanceList(
			@RequestParam(value = "rows",defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx",required = true) String sidx, 
			@RequestParam(value = "sord", required = true) String sord,
			@RequestParam(value="id",required = false) int kafkaDSId
			) {
		
		int count = kafkaDataSourceService.getClientCountByKafkaDSId(kafkaDSId);

		logger.info("Associated client with KafkaDataSource " + count);
		List<NetflowClient> resultList = new ArrayList<>();
		if (count > 0)
			resultList = kafkaDataSourceService.getClientListByKafkaDSId(kafkaDSId,eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),limit);

		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (NetflowClient client : resultList) {
				row = new HashMap<>();
				
				row.put("id", client.getId());
				row.put("kafkaDataSourceName", client.getKafkaDataSourceConfig().getName());
				row.put("serviceName", client.getService().getName());
				row.put("serverInstanceName", client.getService().getServerInstance().getName());
				row.put("name", client.getName());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}


}
