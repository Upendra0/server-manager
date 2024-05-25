package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ParseConnFunctionEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.services.model.DateRangeEnum;
import com.elitecore.sm.services.model.HashDataTypeEnum;
import com.elitecore.sm.services.model.HashSeparatorEnum;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.IndexTypeEnum;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;


/**
 * @author vishal.lakhyani
 *
 */
@Controller
public class IPLogParsingServiceController  extends BaseController{

	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private ServiceValidator validator;
	
	@Autowired
	private ParserService parserService;
	
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
	 * Redirect to iplog parsing service summary page 
	 * @param serviceId
	 * @param serviceType
	 * @param serviceName
	 * @param instanceId
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_IPLOG_PARSING_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initIplogParsingService(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId) {
		
		ModelAndView model = new ModelAndView();
		
		IPLogParsingService iplogParsingService ;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		iplogParsingService = (IPLogParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject(FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN,iplogParsingService);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_SUMMARY);
		
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject("syncStatus",iplogParsingService.isSyncStatus());
		
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.INSTANCE_ID,instanceId);
		model.addObject(BaseConstants.SERVICE_INST_ID, iplogParsingService.getServInstanceId());
		model.addObject(BaseConstants.SERVICE_TYPE,serviceType);
		model.addObject("lastUpdateTime",DateFormatter.formatDate(iplogParsingService.getLastUpdatedDate()));
		model.addObject("enableFileStats",iplogParsingService.isEnableFileStats());
		model.addObject("enableDBStats",iplogParsingService.isEnableDBStats());
		
		return model;
	}
	
	/** Open iplog parsing service configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init service configuration update response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtIplogParsingConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType
			) {

		ModelAndView model = new ModelAndView();
		
		IPLogParsingService iplogParsingService ;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		
		iplogParsingService = (IPLogParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		
		model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,iplogParsingService);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_CONFIGURATION);

		addCommonParamToModel(model,iplogParsingService);
		model.addObject(BaseConstants.SERVICE_NAME, iplogParsingService.getName());
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("parseConnFunctionEnum",java.util.Arrays.asList(ParseConnFunctionEnum.values()));
		model.addObject("unifiedFieldList",servicesService.getUnifiedFieldConfigInPluging(iserviceId));
		model.addObject("outputFileHeader",iplogParsingService.getOutputFileHeader());
		return model;
	}
	
	/**
	 * Update Iplog Parsing service configuration
	 * @param serviceName
	 * @param iplogService
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateIplogServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value=FormBeanConstants.SERVICE_CONFIG_FORM_BEAN) IPLogParsingService iplogService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(iplogService, result,null,false);
		
		if(result.hasErrors()){
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,iplogService);
		}else{
			iplogService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			
			ResponseObject responseObject=servicesService.updateIplogParsingServiceConfiguration(iplogService);
			
			if(responseObject.isSuccess()){
				iplogService=(IPLogParsingService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,iplogService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		}
		
		addCommonParamToModel(model,iplogService);
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getValues());
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("parseConnFunctionEnum",java.util.Arrays.asList(ParseConnFunctionEnum.values()));
		model.addObject("unifiedFieldList",servicesService.getUnifiedFieldConfigInPluging(iplogService.getId()));
		model.addObject("outputFileHeader",iplogService.getOutputFileHeader());
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_CONFIGURATION);
			
		return model;		
	}
	
	/** Add common service param to model object
	 * @param model
	 * @param Iplog parsing service
	 */
	private void addCommonParamToModel(ModelAndView model,IPLogParsingService ipLogService){
		
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("lastUpdateTime",DateFormatter.formatDate(ipLogService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, ipLogService.getId());
		model.addObject(BaseConstants.SERVICE_TYPE, ipLogService.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, ipLogService.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, ipLogService.getServInstanceId());
		model.addObject("enableFileStats",ipLogService.isEnableFileStats());
		model.addObject("enableDBStats",ipLogService.isEnableDBStats());
	}
	
	/** 
	 * Open iplog parsing service hash configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init update parsing hash config response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_PARSING_HASH_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtParsingHashConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType
			) {

		ModelAndView model = new ModelAndView();
		
		IPLogParsingService iplogParsingService ;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		
		iplogParsingService = (IPLogParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		
		model.addObject(FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN,iplogParsingService);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_HASH_CONFIGURATION);
		
		addCommonParamToModel(model,iplogParsingService);
		model.addObject(BaseConstants.SERVICE_NAME, iplogParsingService.getName());
		
		model.addObject("indexBaseEnum",java.util.Arrays.asList(IndexTypeEnum.values()));
		model.addObject("hashSeparatorEnum",java.util.Arrays.asList(HashSeparatorEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(HashDataTypeEnum.values()));
		model.addObject("dateRangeEnum",java.util.Arrays.asList(DateRangeEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("dateRangeEnum",java.util.Arrays.asList(DateRangeEnum.values()));
		
		return model;
	}
	
	/**
	 * Update iplog parsing service hash configuration page
	 * @param iplogService
	 * @param partitionParamList
	 * @param request
	 * @return Update service hash configuration response as model object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.UPDATE_PARSING_HASH_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView updtParsingHashConfig(
			@ModelAttribute (value=FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN) IPLogParsingService iplogService,//NOSONAR
			@RequestParam(value = "partitionParamList", required=true) String partitionParamList,
			HttpServletRequest request
			) {

		ModelAndView model = new ModelAndView();
		iplogService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		
		try {
			ResponseObject responseObject = servicesService.updateIplogParsingServiceHashConfiguration(iplogService, partitionParamList);
			if(responseObject.isSuccess()){
				
				iplogService=(IPLogParsingService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,iplogService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
		} catch (CloneNotSupportedException e) {
			logger.error(e);
			model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
		}
		
		model.addObject(FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN,iplogService);
		model.setViewName(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_HASH_CONFIGURATION);
		
		addCommonParamToModel(model, iplogService);
		model.addObject(BaseConstants.SERVICE_NAME, iplogService.getName());
		
		model.addObject("indexBaseEnum",java.util.Arrays.asList(IndexTypeEnum.values()));
		model.addObject("hashSeparatorEnum",java.util.Arrays.asList(HashSeparatorEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(HashDataTypeEnum.values()));
		model.addObject("dateRangeEnum",java.util.Arrays.asList(DateRangeEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("dateRangeEnum",java.util.Arrays.asList(DateRangeEnum.values()));
		
		return model;
	}
	
	/**
	 * Get service parser list
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param serviceId
	 * @param request
	 * @return Ajax iplog parsing service parser list
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_PARSER_LIST, method = RequestMethod.GET)
	@ResponseBody public String getServiceParserList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx") String sidx,
			@RequestParam(value = "sord") String sord,
			@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId) {
		
		long count = this.parserService.getTotalParserCount(serviceId);
		
		List<Object[]> resultList = new ArrayList<>();
		if (count > 0)
			resultList =  this.parserService.getPaginatedParserList(serviceId,eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);

		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
		// count = 0;
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (Object[] parser : resultList) {
				row = new HashMap<>();
				
			//	count++;
				row.put("id",parser[0]);
				row.put("name", parser[1]);
				row.put("type", parser[2]);
				row.put("alias", parser[3]);
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	/**
	 * Get service parser list
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param serviceId
	 * @param request
	 * @return Ajax iplog parsing service parser list
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_HASH_CONFIG_LIST, method = RequestMethod.GET)
	@ResponseBody public String getServiceHashConfigList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx") String sidx,
			@RequestParam(value = "sord") String sord,
			@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId) {
		
		long count = this.parserService.getTotalHashParamCount(serviceId);
		
		List<PartitionParam> resultList = new ArrayList<>();
		if (count > 0)
			resultList = this.parserService.getPaginatedHashConfigList(serviceId,eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);

		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
		count = 0;
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (PartitionParam param : resultList) {
				row = new HashMap<>();
				count++;
				row.put("id",param.getId());
				row.put("partitionField",String.valueOf(param.getPartitionField()));
				row.put("unifiedField", String.valueOf(param.getUnifiedField()));
				row.put("partitionRange", param.getPartitionRange());
				row.put("baseUnifiedField", param.getBaseUnifiedField());
				row.put("netMask", param.getNetMask());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
}
