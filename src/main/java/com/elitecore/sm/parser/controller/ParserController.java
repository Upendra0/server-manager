package com.elitecore.sm.parser.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.parser.validator.ParserValidator;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class ParserController extends BaseController {
	
	
	@Autowired
	ParserValidator parserValidator;
	
	@Autowired
	ParserService parserService;
	
	@Inject
	private PathListHistoryService pathListHistoryService;
	
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
	 * When click on plug-in name link , redirect to configuration page 
	 * @param requestParamMap
	 * @param request
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PARSER')")
	@RequestMapping(value = ControllerConstants.INIT_PARSER_CONFIGURATION, method = RequestMethod.GET)
	public ModelAndView initParserConfig(@RequestParam Map<String,String> requestParamMap){
		
		ModelAndView model = new ModelAndView();
		RedirectView view=null;
		String  plugInType=requestParamMap.get("plugInType");
		model.addObject(BaseConstants.SERVICE_ID, requestParamMap.get(BaseConstants.SERVICE_ID));
		model.addObject(BaseConstants.SERVICE_NAME, requestParamMap.get(BaseConstants.SERVICE_NAME));
		model.addObject(BaseConstants.SERVICE_TYPE, requestParamMap.get(BaseConstants.SERVICE_TYPE));
		model.addObject(BaseConstants.SERVER_INSTANCE_ID,requestParamMap.get(BaseConstants.SERVER_INSTANCE_ID) );
		model.addObject(BaseConstants.SERVICE_INST_ID, requestParamMap.get(BaseConstants.SERVICE_INST_ID));
		model.addObject(BaseConstants.PLUGIN_ID, requestParamMap.get(BaseConstants.PLUGIN_ID));
		model.addObject(BaseConstants.PLUGIN_NAME, requestParamMap.get(BaseConstants.PLUGIN_NAME));
		model.addObject(BaseConstants.PLUGIN_TYPE,requestParamMap.get(BaseConstants.PLUGIN_TYPE));
		if(EngineConstants.NATFLOW_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_NATFLOW_PARSER_MANAGER);
		}else if(EngineConstants.REGEX_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_REGEX_PARSER_CONFIG);
		}else if(EngineConstants.NATFLOW_ASN_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_NATFLOW_PARSER_MANAGER);
		}else if(EngineConstants.ASCII_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_ASCII_PARSER_CONFIG);
		}else if(EngineConstants.DETAIL_LOCAL_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){ // saumil.vachheta // code start
			view=new RedirectView(ControllerConstants.INIT_DETAIL_LOCAL_PARSER_CONFIG);
		}else if(EngineConstants.ASN1_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_ASN1_PARSER_CONFIG);
		}else if(EngineConstants.XML_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_XML_PARSER_CONFIG);
		}else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_FIXED_LENGTH_ASCII_PARSER_CONFIG);
		}else if(EngineConstants.RAP_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_RAP_PARSER_CONFIG);
		}else if(EngineConstants.TAP_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_TAP_PARSER_CONFIG);
		}else if(EngineConstants.NRTRDE_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_NRTRDE_PARSER_CONFIG);
		}else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_FIXED_LENGTH_BINARY_PARSER_CONFIG);
		}else if(EngineConstants.HTML_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_HTML_PARSER_CONFIG);
		}else if(EngineConstants.PDF_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_PDF_PARSER_CONFIG);
		}else if(EngineConstants.XLS_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_XLS_PARSER_CONFIG);
		}else if(EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_VAR_LENGTH_ASCII_PARSER_CONFIG);
		}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_VAR_LENGTH_BINARY_PARSER_CONFIG);
		}else if(EngineConstants.JSON_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_JSON_PARSER_CONFIG);
		}else if(EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN.equalsIgnoreCase(plugInType)){
			view=new RedirectView(ControllerConstants.INIT_MTSIEMENS_PARSER_CONFIG);
		}

		model.setView(view);
		return model;
	}
	/**
	 * Create parser in database
	 * @param mode
	 * @param parser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PARSER')")
	@RequestMapping(value = ControllerConstants.CREATE_PARSER, method = RequestMethod.POST)
	@ResponseBody public String createParser(
			@RequestParam(value="mode") String mode,
			@ModelAttribute(value=FormBeanConstants.PARSER_FORM_BEAN) Parser parser,//NOSONAR
			BindingResult result){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		parserValidator.validateParserForParsing(parser,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(mode+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(mode+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = parserService.createParser(parser);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			 if(responseObject.isSuccess()) {
				 PathList pathList = parser.getParsingPathList();
				 int parserId = parser.getId();
				 Parser par = parserService.getParserById(parserId);
				 pathListHistoryService.save(pathList, par);
			 }
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Update parser detail in database
	 * @param mode
	 * @param parser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_PARSER, method = RequestMethod.POST)
	@ResponseBody public  String updateParser(
			@RequestParam(value="mode") String mode,HttpServletRequest request,
			@ModelAttribute(value=FormBeanConstants.PARSER_FORM_BEAN) Parser parser,//NOSONAR
			BindingResult result){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		parser.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		parserValidator.validateParserForParsing(parser,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(mode+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(mode+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = parserService.updateParser(parser);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			 if(responseObject.isSuccess()) {
				 pathListHistoryService.save(parser.getParsingPathList(), parser);
			 }
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Delete parser from database
	 * @param parserIdList
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PARSER')")
	@RequestMapping(value = ControllerConstants.DELETE_PARSER, method = RequestMethod.POST)
	@ResponseBody public  String deleteParser(
			@RequestParam(value="parserIdList") String parserIdList	){
		 ResponseObject responseObject = parserService.deleteParserList(parserIdList);
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will return the list of all Snmp Server by serverInstance id
	 * 
	 * @param sererInstanceId
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return Response body
	 */
	@RequestMapping(value = ControllerConstants.GET_PARSER_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getParserList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord) {
		
		long count = this.parserService
				.getTotalParserCount();
		List<Object[]> resultList = null;
		if (count > 0) {
			resultList = this.parserService.getPaginatedParserList(
					 eliteUtils.getStartIndex(limit,
							currentPage,
							eliteUtils.getTotalPagesCount(count, limit)),
					limit, sidx, sord);
		}

		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		//count = 0;
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
	 * Create parser in database
	 * @param mode
	 * @param parser
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PARSER')")
	@RequestMapping(value = ControllerConstants.CLONE_PARSER, method = RequestMethod.POST)
	@ResponseBody public String cloneParser(
			@RequestParam(value="parserId") String parserId,			
			@RequestParam(value="serviceId") String serviceId,
			@RequestParam(value="pathListId") String pathListId){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		int parserID =0, pathListID=0, serviceID=0;
		
		if(!StringUtils.isEmpty(parserId) && !StringUtils.isEmpty(pathListId) && !StringUtils.isEmpty(serviceId)){
			parserID = Integer.parseInt(parserId);
			serviceID = Integer.parseInt(serviceId);
			pathListID = Integer.parseInt(pathListId);
		}
			
		Parser parserById = parserService.getParserById(parserID);
		responseObject = parserService.cloneParser(parserById, serviceID, pathListID);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
}
