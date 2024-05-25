package com.elitecore.sm.dsconfig.controller;

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
import com.elitecore.sm.common.model.DBSourceTypeEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.datasource.service.DataSourceService;
import com.elitecore.sm.datasource.validator.DataSourceConfigurationValidator;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.kafka.datasource.service.KafkaDataSourceService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;

/*
 * 
 * Keyur Raval 
 */

@Controller
public class DBConfigurationController extends BaseController{

	@Autowired
	@Qualifier(value = "dataSourceService")
	private DataSourceService dataSourceService;
	
	@Autowired
	@Qualifier(value = "kafkaDataSourceService")
	private KafkaDataSourceService kafkaDataSourceService;

	@Autowired
	private DataSourceConfigurationValidator validator;
	
	/**
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}

	/**
	 * Listing the DataSource Config-Value  
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DATASOURCE','VIEW_KAFKA_DATASOURCE')")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.INIT_CONFIGURATION_MANAGER,method= RequestMethod.GET)
	public ModelAndView initConfigurationManager(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType){	
		ModelAndView model = new ModelAndView(ViewNameConstants.INIT_CONFIGURATION_MANAGER);
		logger.debug("Req Action Type ="+requestActionType);
		if(requestActionType != null){
			if (requestActionType.equals(BaseConstants.DATABASE_CONFIGURATION)){
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DATABASE_CONFIGURATION);
				ResponseObject responseObject = dataSourceService.getDSConfigList();
				if(responseObject.isSuccess())
				{
					List<DataSourceConfig> dsList = (List<DataSourceConfig>)responseObject.getObject();
					if(dsList!=null){
						for(DataSourceConfig dataSourceConfig : dsList){
							if(dataSourceConfig.getPassword()!=null){
								dataSourceConfig.setPassword(EliteUtils.decryptData(dataSourceConfig.getPassword()));
							}
						}
					}
					model.addObject("dbDetailList",dsList);
				}
				model.addObject("dbSourceType",Arrays.asList(DBSourceTypeEnum.values()));

			} else if(BaseConstants.KAFKA_CONFIGURATION.equals(requestActionType)) {
				ResponseObject responseObject = kafkaDataSourceService.getKafkaDataSourceConfigList();
				if(responseObject.isSuccess())
				{
					List<KafkaDataSourceConfig> kafkaDataSourceList = (List<KafkaDataSourceConfig>)responseObject.getObject();
					model.addObject("kafkaDetailList",kafkaDataSourceList);
				}
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.KAFKA_CONFIGURATION);
			}
		}else{
			if(eliteUtils.isAuthorityGranted("DATABASE_CONFIGURATION")){
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DATABASE_CONFIGURATION);
				ResponseObject responseObject = dataSourceService.getDSConfigList();
				if(responseObject.isSuccess())
				{
					List<DataSourceConfig> dsList = (List<DataSourceConfig>)responseObject.getObject();
					if(dsList!=null){
						for(DataSourceConfig dataSourceConfig : dsList){
							if(dataSourceConfig.getPassword()!=null){
								dataSourceConfig.setPassword(EliteUtils.decryptData(dataSourceConfig.getPassword()));
							}
						}
					}
					model.addObject("dbDetailList",dsList);
				}
				model.addObject("dbSourceType",Arrays.asList(DBSourceTypeEnum.values()));

			} else if(eliteUtils.isAuthorityGranted(BaseConstants.KAFKA_CONFIGURATION)) {
				ResponseObject responseObject = kafkaDataSourceService.getKafkaDataSourceConfigList();
				if(responseObject.isSuccess())
				{
					List<KafkaDataSourceConfig> kafkaDataSourceList = (List<KafkaDataSourceConfig>)responseObject.getObject();
					model.addObject("kafkaDetailList",kafkaDataSourceList);
				}
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.KAFKA_CONFIGURATION);
			}
		}
		return model;
	}


	/**
	 * Create the DataSource
	 * @param DataSourceCounter
	 *@param DatSourceConfig
	 *@param result 
	 *@param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.CREATE_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public String createDataSourceConfiguration(
			@RequestParam(value="dbDetailListCounter") String dbDetailListCounter ,
			@ModelAttribute(value=FormBeanConstants.DS_CONFIG_BEAN) DataSourceConfig dataSourceConfig,//NOSONAR
			BindingResult result){


		AjaxResponse ajaxResponse=new AjaxResponse();

		validator.validateDataSourceConfigurationParameters(dataSourceConfig, result, null, null, false);

		if(result.hasErrors()){

			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){

				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){

					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));

				}else{
					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			ResponseObject responseObject =dataSourceService.addNewDataSourceConfig(dataSourceConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	/**
	 * Update DataSourceConfigurationDetails 
	 * @param DataSourceCounter
	 * @param DatSourceConfig
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('EDIT_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public  String updateDataSourceConfiguration(@RequestParam(value="dbDetailListCounter") String dbDetailListCounter,
			@ModelAttribute(value=FormBeanConstants.DS_CONFIG_BEAN) DataSourceConfig dataSourceConfig,//NOSONAR
			BindingResult result,HttpServletRequest request){

		AjaxResponse ajaxResponse=new AjaxResponse();

		validator.validateDataSourceConfigurationParameters(dataSourceConfig, result, null, null, false);

		if(result.hasErrors()){

			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			dataSourceConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));

			ResponseObject responseObject=dataSourceService.updateDataSourceConfigurationDetails(dataSourceConfig);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}

		return ajaxResponse.toString();
	}


	/**
	 * Method will delete the dataSourceDetails  
	 * @param DataSourceID
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_DATASOURCE')")
	@RequestMapping(value = ControllerConstants.DELETE_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody public String deleteDataSourceConfiguration(@RequestParam(value = BaseConstants.DATASOURCE_ID,required=true) String deleteDataSourceId){

		int idataSourceId=Integer.parseInt(deleteDataSourceId);
		ResponseObject responseObject=dataSourceService.deleteDataSourceDetails(idataSourceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	/**
	 * Create the DataSource
	 * @param DataSourceCounter
	 *@param DatSourceConfig
	 *@param result 
	 *@param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.TEST_DATASOURCE_CONFIGURATION, method = RequestMethod.POST)
	@ResponseBody
	public String testDataSourceConfigurations(@RequestParam(value = "dbDetailListCounter") String dbDetailListCounter,
			@ModelAttribute(value = FormBeanConstants.DS_CONFIG_BEAN) DataSourceConfig dataSourceConfig,BindingResult result,HttpServletRequest request) {//NOSONAR
		
		ResponseObject responseObject=null;
		AjaxResponse ajaxResponse=new AjaxResponse();

		validator.validateDataSourceConfigurationParameters(dataSourceConfig, result, null, null, false);

		if(result.hasErrors()){

			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){

				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){

					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));

				}else{
					errorMsgs.put(dbDetailListCounter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject =dataSourceService.getDataBaseConnection(dataSourceConfig);
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);

		}
		return ajaxResponse.toString();
	}
	
	
}