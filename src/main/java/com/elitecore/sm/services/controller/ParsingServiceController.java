package com.elitecore.sm.services.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import com.elitecore.sm.common.model.FileGroupDateTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ParseConnFunctionEnum;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.services.model.CDRFileDateTypeEnum;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.SortingCriteriaEnum;
import com.elitecore.sm.services.model.SortingTypeEnum;
import com.elitecore.sm.services.model.StartUpModeEnum;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.services.validator.ServiceValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.services.model.CDRDateSummaryTypeEnum;

/**
 * @author vishal.lakhyani
 *
 */
@Controller
public class ParsingServiceController extends BaseController {

	@Autowired
	ServicesService servicesService; 
	
	@Autowired
	ServiceValidator validator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}

	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_PARSING_SERVICE_MANAGER, method = RequestMethod.GET)
	public ModelAndView initParsingService(@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
    		@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType,
    		@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
    		@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) String instanceId,
    		HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		ParsingService parsingService = null;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.PARSING_SERVICE_MANAGER);
		parsingService = (ParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject(FormBeanConstants.PARSING_SERVICE_CONFIGURATION_FORM_BEAN,parsingService);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PARSING_SERVICE_SUMMARY);
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject("syncStatus",parsingService.isSyncStatus());
		
		addCommonParamToModel(model,parsingService);
		
		return model;
	}
	
	/** Add common service param to model object
	 * @param model
	 * @param Iplog parsing service
	 */
	private void addCommonParamToModel(ModelAndView model,ParsingService parsingService){
		
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("lastUpdateTime",DateFormatter.formatDate(parsingService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, parsingService.getId());
		model.addObject(BaseConstants.SERVICE_TYPE, parsingService.getSvctype().getAlias());
		model.addObject(BaseConstants.INSTANCE_ID, parsingService.getServerInstance().getId());
		model.addObject("enableFileStats",parsingService.isEnableFileStats());
		model.addObject("enableDBStats",parsingService.isEnableDBStats());
		model.addObject(BaseConstants.SERVICE_INST_ID, parsingService.getServInstanceId());
	}
	
	/** Open iplog parsing service configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init service configuration update response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_PARSING_SERVICE_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtParsingConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_TYPE, required=true) String serviceType
			) {

		ModelAndView model = new ModelAndView();
		
		ParsingService parsingService = null;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.PARSING_SERVICE_MANAGER);
		
		parsingService = (ParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,parsingService);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PARSING_SERVICE_CONFIGURATION);
		model.addObject(BaseConstants.SERVICE_NAME, parsingService.getName());
		addCommonParamToModel(model,parsingService);
		model.addObject("startupMode", java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getAllValues());
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("parseConnFunctionEnum",java.util.Arrays.asList(ParseConnFunctionEnum.values()));
		model.addObject("overrideFileDateTypeEnum",java.util.Arrays.asList(CDRFileDateTypeEnum.values()));
		model.addObject("cdrDataSummaryTypeEnum",java.util.Arrays.asList(CDRDateSummaryTypeEnum.values()));
		
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
	@RequestMapping(value = ControllerConstants.UPDATE_PARSING_SERVICE_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView updateParsingServiceConfiguration(
			@RequestParam(value=BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@ModelAttribute (value=FormBeanConstants.SERVICE_CONFIG_FORM_BEAN) ParsingService parsingService,//NOSONAR
			BindingResult result,
			HttpServletRequest request) {
		
		ModelAndView model = new ModelAndView(ViewNameConstants.PARSING_SERVICE_MANAGER);
		validator.validateServiceConfigurationParameter(parsingService, result,null,false);
		
		if(result.hasErrors()){
			logger.debug(" Validation error occurs");
			model.addObject(BaseConstants.SERVICE_NAME,serviceName);
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,parsingService);
		}else{
			parsingService.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=servicesService.updateParsingServiceConfiguration(parsingService);
			
			if(responseObject.isSuccess()){
				parsingService=(ParsingService)responseObject.getObject();
				model.addObject(BaseConstants.SERVICE_NAME,parsingService.getName());
				model.addObject(BaseConstants.RESPONSE_MSG,getMessage("Service.configuration.update.success"));
			}else{
				model.addObject(BaseConstants.ERROR_MSG,getMessage("Service.configuration.update.fail"));
			}
			model.addObject(FormBeanConstants.SERVICE_CONFIG_FORM_BEAN,parsingService);
		}
		
		addCommonParamToModel(model,parsingService);
		model.addObject("startupMode",java.util.Arrays.asList(StartUpModeEnum.values()));
		model.addObject("sortingType",java.util.Arrays.asList(SortingTypeEnum.values()));
		model.addObject("sortingCriteria",java.util.Arrays.asList(SortingCriteriaEnum.values()));
		model.addObject(BaseConstants.GROUP_TYPE_ENUM,FileGroupEnum.getAllValues());
		model.addObject("groupDateTypeEnum",Arrays.asList(FileGroupDateTypeEnum.values()));
		model.addObject("unifiedFieldEnum",java.util.Arrays.asList(UnifiedFieldEnum.values()));
		model.addObject("parseConnFunctionEnum",java.util.Arrays.asList(ParseConnFunctionEnum.values()));
		model.addObject("overrideFileDateTypeEnum",java.util.Arrays.asList(CDRFileDateTypeEnum.values()));
		model.addObject("cdrDataSummaryTypeEnum",java.util.Arrays.asList(CDRDateSummaryTypeEnum.values()));
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PARSING_SERVICE_CONFIGURATION);
			
		return model;		
	}

	// @PreAuthorize("hasAnyAuthority('"+BusinessModelConstants.SERVICE_MANAGER_MENU_ALIAS+"')")
	@RequestMapping(value = ControllerConstants.INIT_ADD_PARSER_ATTRIBUTE, method = RequestMethod.GET)
	public ModelAndView initAddParserAttribute() {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.ADD_PARSER_ATTRIBUTE);
		return model;
	}


	// @PreAuthorize("hasAnyAuthority('CREATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.GET_PARSING_AGENT_LIST, method = RequestMethod.GET)
	@ResponseBody public  String getSystemAgentList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx") String sidx,
			@RequestParam(value = "sord") String sord,
			HttpServletRequest request) {
		
		Map<String, Object> row = null;
		List<Map<String, Object>> rowList = new ArrayList<>();
		row = new HashMap<>();
		row.put("agentId", "101");
		row.put("typeOfAgent", "Packet Statistic Agent");
		row.put("lastExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("nextExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("syncAgentStatus", true);
		rowList.add(row);

		row = new HashMap<>();
		row.put("agentId", "102");
		row.put("typeOfAgent", "File Rename Agent");
		row.put("lastExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("nextExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("syncAgentStatus", true);
		rowList.add(row);

		row = new HashMap<>();
		row.put("agentId", "103");
		row.put("typeOfAgent", "File Distribution Agent");
		row.put("lastExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("nextExecutionDate", DateFormatter.formatDate(new Date()));
		row.put("syncAgentStatus", true);
		rowList.add(row);

return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(3, 10), 1, (int) 3, rowList).getJsonString();
}
	
	
}
