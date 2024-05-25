package com.elitecore.sm.parser.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.service.RegExParserService;
import com.elitecore.sm.parser.validator.RegexPatternValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class RegExPatternController extends BaseController{

	@Autowired
	RegexPatternValidator validator;
	
	@Autowired
	RegExParserService regExParserService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder()
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		binder.setValidator(validator);
	}
	
	/**
	 * Add Regex Pattern and attribute to DB
	 * @param patternAttributeList
	 * @param patternListCounter
	 * @param regExPattern
	 * @param result
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PARSER')")
	@RequestMapping(value=ControllerConstants.ADD_REGEX_PATTERN_AND_ATTRIBUTE,method=RequestMethod.POST)
	public @ResponseBody String addRegExPatternAndAttribute(@RequestParam(value="patternAttributeList") String patternAttributeList,
			@RequestParam(value="patternListCounter") String patternListCounter,
			 @ModelAttribute(value=FormBeanConstants.REGEX_PATTERN_CONFIGURATION_FORM_BEAN) RegExPattern regExPattern,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		validator.validateRegExPatternParameter(regExPattern, result, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				
				errorMsgs.put(patternListCounter+"_"+error.getField(),error.getDefaultMessage());
				
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			ResponseObject responseObject=regExParserService.addRegExPatternAndAttribute(regExPattern,patternAttributeList);
			
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	
	}
	
	/**
	 * Update regex pattern detail 
	 * @param patternAttributeList
	 * @param isDeleteAttribute
	 * @param patternListCounter
	 * @param regExPattern
	 * @param result
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PARSER')")
	@RequestMapping(value=ControllerConstants.UPDATE_REGEX_PATTERN_DETAIL,method=RequestMethod.POST)
	public @ResponseBody String updateRegExPatternDetail(@RequestParam(value="patternAttributeList") String patternAttributeList,
			@RequestParam(value="isDeleteAttribute") boolean isDeleteAttribute,
			@RequestParam(value="patternListCounter") String patternListCounter,
			 @ModelAttribute(value=FormBeanConstants.REGEX_PATTERN_CONFIGURATION_FORM_BEAN) RegExPattern regExPattern,//NOSONAR
			 BindingResult result,
			 HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		validator.validateRegExPatternParameter(regExPattern, result, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				
					errorMsgs.put(patternListCounter+"_"+error.getField(),error.getDefaultMessage());
				
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			regExPattern.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=regExParserService.updateRegExPatternDetail(regExPattern,patternAttributeList,isDeleteAttribute);
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	
	}
	
	/**
	 * Delete regex pattern 
	 * @param patternId
	 * @param request
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PARSER')")
	@RequestMapping(value=ControllerConstants.DELETE_REGEX_PATTERN,method=RequestMethod.POST)
	public @ResponseBody String deleteRegExPatternDetail(@RequestParam(value="patternId") String patternId,
			 HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		if(!StringUtils.isEmpty(patternId)){
			int ipatternId=Integer.parseInt(patternId);
			ResponseObject responseObject=regExParserService.deleteRegExPattern(ipatternId,eliteUtils.getLoggedInStaffId(request));
			ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	
	}
	

}
