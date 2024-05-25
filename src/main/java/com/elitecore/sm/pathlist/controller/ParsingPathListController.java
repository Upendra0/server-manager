package com.elitecore.sm.pathlist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
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
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.parser.dao.ParserDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.services.model.HashDataTypeEnum;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ParsingPathListController extends BaseController {

	@Autowired
	ServicesService servicesService;
	
	@Autowired
	PathListService pathlistService;
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	PathListValidator pathlistValidator;
	
	@Inject
	private PathListHistoryService pathListHistoryService;
	
	@Inject
	private ParserDao parserDao;
	
	@Autowired(required = true)
	@Qualifier(value = "licenseService")
	private LicenseService licenseService;
	
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
	
	/** Open iplog parsing service pathlist configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init update parsing hash config response as model object
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_IPLOG_PARSING_PATHLIST_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtIplogParsingPathlistConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId
			) {

		ModelAndView model = new ModelAndView();
		
		IPLogParsingService iplogParsingService ;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.IPLOG_PARSING_SERVICE_MANAGER);
		
		iplogParsingService = (IPLogParsingService) servicesService.getServiceandServerinstance(iserviceId);
		
		int serverTypeId=iplogParsingService.getServerInstance().getServer().getServerType().getId();
		List<PluginTypeMaster> pluginType=(List<PluginTypeMaster>) eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.PARSER_PLUGIN_TYPE);
		logger.debug("Final Plugin type  list" +pluginType  );
		
		ResponseObject responseObject = pathlistService.getParsingPathListUsingServiceId(iserviceId);
		
		if(responseObject.isSuccess()){
			List<ParsingPathList> parsingPathlist =  (List<ParsingPathList>)responseObject.getObject();
			for (ParsingPathList path : parsingPathlist) {
				path.setService(iplogParsingService);
			}
			logger.debug("parsingPathlist " + parsingPathlist);
			model.addObject("parsingPathList",parsingPathlist);
		}
		
		model.addObject(FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN,iplogParsingService);
	
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.IPLOG_SERVICE_PATHLIST_CONFIGURATION);
		
		model.addObject(FormBeanConstants.PARSER_FORM_BEAN,new Parser());
		
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("dataTypeEnum",java.util.Arrays.asList(HashDataTypeEnum.values()));
		model.addObject("pluginType",pluginType);
		model.addObject("enableFileStats",iplogParsingService.isEnableFileStats());
		model.addObject("enableDBStats",iplogParsingService.isEnableDBStats());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(iplogParsingService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE, iplogParsingService.getSvctype().getAlias());
		model.addObject(BaseConstants.SERVICE_NAME, iplogParsingService.getName());
		model.addObject(BaseConstants.SERVICE_INST_ID, iplogParsingService.getServInstanceId());
		model.addObject(BaseConstants.INSTANCE_ID, iplogParsingService.getServerInstance().getId());
		
		return model;
	}
	
	
	/**
	 * Add iplog parsing pathlist in database
	 * @param pathListCount
	 * @param parserWrapper
	 * @param serviceId
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_IPLOG_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public  String createIplogParsingPathList(@RequestParam(value="pathListCount") String pathListCount,
			@ModelAttribute(value=FormBeanConstants.PARSER_FORM_BEAN) Parser parserWrapper,//NOSONAR
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
			BindingResult result){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		pathlistValidator.validateParserWrapperForIPLogParsing(parserWrapper,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(pathListCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(pathListCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = pathlistService.addIpLogParsingPathList(parserWrapper,serviceId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(parserWrapper.getParsingPathList(), null);
			}
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Update Iplog parsing path list in database
	 * @param pathListCount
	 * @param parserWrapper
	 * @param serviceId
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_IPLOG_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String updateIplogParsingPathList(@RequestParam(value="pathListCount") String pathListCount,
			@ModelAttribute(value=FormBeanConstants.PARSER_FORM_BEAN) Parser parserWrapper,//NOSONAR
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
			BindingResult result){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		pathlistValidator.validateParserWrapperForIPLogParsing(parserWrapper,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(pathListCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(pathListCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = pathlistService.updateIpLogParsingPathList(parserWrapper,serviceId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if(responseObject.isSuccess()) {
				Parser parser = parserDao.getParserByPathListId(parserWrapper.getParsingPathList().getId());
				if(parser == null) {
					pathListHistoryService.save(parserWrapper.getParsingPathList(), null);
				} else {
					pathListHistoryService.save(parserWrapper.getParsingPathList(), parser);
				}
			}
		}
		return ajaxResponse.toString();
	}
	
	/** Open parsing service pathlist configuration page
	 * @param serviceId
	 * @param serviceType
	 * @return Init update parsing service pathlist response as model object
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_PARSING_PATHLIST_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView initUpdtParsingPathlistConfig(
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) String serviceId
			) {

		ModelAndView model = new ModelAndView();
		
		ParsingService parsingService ;
		int iserviceId=Integer.parseInt(serviceId);
		
		model.setViewName(ViewNameConstants.PARSING_SERVICE_MANAGER);
		
		ResponseObject responseObject=pathlistService.getParsingPathListByServiceId(iserviceId);
		if(responseObject.isSuccess()){
			model.addObject("pathList",responseObject.getObject());
			logger.info("Final path list is : " + responseObject.getObject());
		}
		
		parsingService = (ParsingService) servicesService.getAllServiceDepedantsByServiceId(iserviceId);
		int serverTypeId=parsingService.getServerInstance().getServer().getServerType().getId();
		List<PluginTypeMaster> pluginType=(List<PluginTypeMaster>) eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.PARSER_PLUGIN_TYPE);
		logger.debug("Final Plugin type  list" +pluginType  );
		
		model.addObject(FormBeanConstants.IPLOG_PARSING_SERVICE_CONFIGURATION_FORM_BEAN,parsingService);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.PARSING_SERVICE_PATHLIST_CONFIGURATION);
		
		model.addObject(FormBeanConstants.PARSER_FORM_BEAN,new Parser());
	
		model.addObject("trueFalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("pluginType",pluginType);
		
		model.addObject("enableFileStats",parsingService.isEnableFileStats());
		model.addObject("enableDBStats",parsingService.isEnableDBStats());
		model.addObject("lastUpdateTime",DateFormatter.formatDate(parsingService.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE, parsingService.getSvctype().getAlias());
		model.addObject(BaseConstants.SERVICE_NAME, parsingService.getName());
		model.addObject(BaseConstants.SERVICE_INST_ID, parsingService.getServInstanceId());
		model.addObject(BaseConstants.INSTANCE_ID, parsingService.getServerInstance().getId());
		model.addObject("fileGroupEnable", parsingService.getFileGroupingParameter().isFileGroupEnable());
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("positionEnum", java.util.Arrays.asList(PositionEnum.values()));
		model.addObject("circleList", licenseService.getCircleList().getObject());

		return model;
	}

	
	/**
	 * Add parsing path list in database
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String saveParsingPathList(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) ParsingPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,HttpServletRequest request
			){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			pathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedDate(new Date());
			responseObject=licenseService.getCircleDetailsById(pathList.getId());
			if(responseObject.isSuccess()) {
				pathList.setCircle((Circle) responseObject.getObject());
			}
			responseObject = pathlistService.addParsingPathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if(responseObject.isSuccess()){
				pathListHistoryService.save(pathList, null);
			}
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Update parsing path list in database
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public  String updateParsingPathList(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) ParsingPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,HttpServletRequest request
			){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject;
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			
			ParsingPathList path_list = (ParsingPathList) pathlistService.getPathListById(pathList.getId());
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setWriteFilePath(path_list.getWriteFilePath());
			pathList.setFileNamePattern(path_list.getFileNamePattern());
			pathList.setReadFilenamePrefix(path_list.getReadFilenamePrefix());
			pathList.setWriteFilenamePrefix(path_list.getWriteFilenamePrefix());
			pathList.setReadFilenameSuffix(path_list.getReadFilenameSuffix());
 			pathList.setReadFilenameContains(path_list.getReadFilenameContains());
			pathList.setReadFilenameExcludeTypes(path_list.getReadFilenameExcludeTypes());
			pathList.setCompressInFileEnabled(path_list.isCompressInFileEnabled());
			pathList.setCompressOutFileEnabled(path_list.isCompressOutFileEnabled());
			pathList.setWriteFileSplit(path_list.isWriteFileSplit());			
			pathList.setMaxFileCountAlert(path_list.getMaxFileCountAlert());
			pathList.setLastUpdatedDate(new Date());
			pathList.setCreatedDate(path_list.getCreatedDate());
			pathList.setPathId(path_list.getPathId());
			//responseObject=licenseService.getCircleDetailsById(pathList.getId());
			//if(responseObject.isSuccess()) {
			//	pathList.setCircle((Circle) responseObject.getObject());
			//}
						
			responseObject = pathlistService.updateParsingPathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if(responseObject.isSuccess()) {
				Parser parser = parserDao.getParserByPathListId(pathList.getId());
				if(parser == null) {
					pathListHistoryService.save(pathList, null);
				} else {
					pathListHistoryService.save(pathList, parser);
				}
			}
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will delete Iplog Parsing  pathlist  
	 * @param pathlistId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteParsingPathList(
						@RequestParam(value = "pathlistId",required=true) String pathlistId,
						HttpServletRequest request) throws SMException {
		return deleteCommonParsingPathList(request, pathlistId, false);
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_IPLOG_PARSING_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteIpLogParsingPathList(
						@RequestParam(value = "pathlistId",required=true) String pathlistId,
						HttpServletRequest request) throws SMException {
		return deleteCommonParsingPathList(request, pathlistId, true);
	}
	
	public String deleteCommonParsingPathList(HttpServletRequest request, String pathlistId, boolean isIpLogParsingPathList) throws SMException {
		AjaxResponse  ajaxResponse = new AjaxResponse();
		if(!StringUtils.isEmpty(pathlistId)){
			int ipathlistId=Integer.parseInt(pathlistId);
			ResponseObject responseObject;
			if(isIpLogParsingPathList) {
				responseObject = pathlistService.deleteIpLogParsingPathListDetails(ipathlistId, eliteUtils.getLoggedInStaffId(request));
			} else {
				responseObject = pathlistService.deleteParsingPathListDetails(ipathlistId, eliteUtils.getLoggedInStaffId(request));
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
}
