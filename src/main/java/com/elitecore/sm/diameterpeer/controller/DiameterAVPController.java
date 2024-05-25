package com.elitecore.sm.diameterpeer.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.diameterpeer.service.DiameterAVPService;
import com.elitecore.sm.diameterpeer.validator.DiameterAVPValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class DiameterAVPController extends BaseController {
	
	@Autowired
	DiameterAVPValidator avpValidator;
	
	@Autowired
	DiameterAVPService avpService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	/**
	 * Create AVP in database
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.CREATE_AVP, method = RequestMethod.POST)
	@ResponseBody public String createAVP(
			@ModelAttribute(value=FormBeanConstants.DIAMETER_COLLECTION_AVP_FORM_BEAN) DiameterAVP diameterAVP,//NOSONAR
			BindingResult result){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		avpValidator.validateAVPforPeer(diameterAVP,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					if (error.getField() != null && error.getField().equals("value")) {
						errorMsgs.put("attributeValue",error.getDefaultMessage());
					} else {
						errorMsgs.put(error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = avpService.createAVP(diameterAVP);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Update AVP detail in database
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_AVP, method = RequestMethod.POST)
	@ResponseBody public  String updateAVP(
			@ModelAttribute(value=FormBeanConstants.DIAMETER_COLLECTION_AVP_FORM_BEAN) DiameterAVP diameterAVP,//NOSONAR
			BindingResult result, HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		diameterAVP.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		avpValidator.validateAVPforPeer(diameterAVP,result,null,false,null);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					if (error.getField() != null && error.getField().equals("value")) {
						errorMsgs.put("attributeValue",error.getDefaultMessage());
					} else {
						errorMsgs.put(error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			responseObject = avpService.updateAVP(diameterAVP);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Delete AVP from database
	 * @param avpList
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.DELETE_AVP, method = RequestMethod.POST)
	@ResponseBody public  String deleteAVP(
			@RequestParam(value="avpIdList") String avpIdList){
		 ResponseObject responseObject = avpService.deleteAVPList(avpIdList);
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
}
