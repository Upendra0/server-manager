package com.elitecore.sm.services.controller;

import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.PartitionParam;
import com.elitecore.sm.services.service.PartitionParamService;
import com.elitecore.sm.services.validator.PartitionParamValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author vishal.lakhyani
 *
 */
@Controller
public class PartitionParamController extends BaseController {
	
	@Autowired
	PartitionParamService paramService;
	
	@Autowired
	PartitionParamValidator paramValidator;

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
		binder.setValidator(paramValidator);
	}
	
	/**
	 * Validate partition param object
	 * @param iplogService
	 * @param result
	 * @param request
	 * @return Validate partition param object response as response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_INSTANCE')")
	@RequestMapping(value = ControllerConstants.VALIDATE_PARTITION_PARAM,method= RequestMethod.POST)
	@ResponseBody public  String validatePartitionParamObject(
			@ModelAttribute (value=FormBeanConstants.PARTITION_PARAM_FORM_BEAN) PartitionParam partitionParam,//NOSONAR
			BindingResult result) {
		
		paramValidator.validatePartitionParam(partitionParam, result, null,false,null);
		AjaxResponse ajaxResponse=new AjaxResponse();
		if(result.hasErrors()){
			ajaxResponse.setResponseCode("500");
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			ResponseObject responseObject = new ResponseObject();
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();		
	}
}
