package com.elitecore.sm.iam.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.service.RSAAuthenticationService;

@Controller
public class RSAAuthenticationController extends BaseController{

	@Autowired
	private RSAAuthenticationService authenticationService;
	
	@RequestMapping(value = ControllerConstants.RSA_HOME, method = RequestMethod.POST)
	@ResponseBody
	public String verifyRSAToken(@RequestParam(value = "otp") String otp,HttpServletRequest request){
		ResponseObject responseObject = new ResponseObject();
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.LOGIN_PAGE);
		try {			
			if(otp!=null && !otp.equals("")) {				
				String loginUserName= (String) request.getSession().getAttribute("userName");
				Map<String, String> responseMap = authenticationService.initialize(loginUserName, otp);
				String isInitialized=responseMap.get("IS_INITIALIZED");
				if(isInitialized.equals("TRUE")) {
					if(authenticationService.verify()) {						
						responseObject.setObject(null);
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.RSA_SUCCESS);
					}else {
						responseObject.setObject(null);
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.RSA_INVALID_OTP);
					}
				}else if(isInitialized.equals("FALSE")){
					responseObject.setObject(null);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.INVALID_RSA_URL);
				}
			}else {
				responseObject.setObject(null);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.RSA_INVALID_OTP);
			}
		} catch (Exception e) {
			logger.error("Error occured in RSAAuthentication action " + e.getMessage(), e);			
		}		
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

}
