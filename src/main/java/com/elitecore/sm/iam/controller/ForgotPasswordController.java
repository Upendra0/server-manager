/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.iam.validator.StaffValidator;
import com.elitecore.sm.util.AESEncryptionDecryption;
import com.elitecore.sm.util.SendMail;

/**
 * @author Sunil Gulabani
 * May 12, 2015
 */
@Controller
public class ForgotPasswordController extends BaseController{
	@Autowired(required=true)
	@Qualifier(value="staffService")
	private StaffService staffService;
	
	@Autowired
	private StaffValidator validator;
	
	/**
	 * Initializes the Forgot Password page.
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.INIT_FORGOT_PASSWORD, method = RequestMethod.GET)
	public ModelAndView initForgotPassword(
			HttpServletRequest request, 
			HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.FORGOT_PASSWORD);
		request.getSession().removeAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
		request.getSession().removeAttribute(BaseConstants.VERIFIED_DETAIL_FOR_FORGOT_PASSWORD);
		model.addObject("FORGOTPWDOPTION","username");
		return model;
	}
	
	
/**
 * Verifies user for Forgot Password.
 * @param username
 * @param emailId
 * @param forgotPWdOption
 * @param request
 * @param response
 * @return
 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.VERIFY_USER_FOR_FORGOT_PASSWORD, method = RequestMethod.POST)
	public ModelAndView forgotPasswordVerifyUser(
			@RequestParam(value = "username",required=false,defaultValue="") String username,
			@RequestParam(value = "emailId",required=false,defaultValue="") String emailId,
			@RequestParam(value="forgotPWdOption",required=true) String forgotPWdOption,
			@RequestParam(value="tokenExpirationTime",required=true) String tokenExpirationTime,
			HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.FORGOT_PASSWORD);
		logger.debug("username::"+username);
		logger.debug("forgotPWdOption::"+forgotPWdOption);
		boolean resetLinkExpire = false;
		if(tokenExpirationTime == null || tokenExpirationTime.trim().isEmpty()) {
			resetLinkExpire = true;
		}else {
			try {
				long resetLinkGenerationTime = Long.parseLong(AESEncryptionDecryption.decrypt(tokenExpirationTime));
				if((System.currentTimeMillis() - resetLinkGenerationTime)/60000 > BaseConstants.LINK_EXPIRATION_TIME_IN_MINUTE)
					resetLinkExpire = true;
			} catch (Exception e) {
				logger.debug("username::"+e);
				resetLinkExpire = true;
			}
		}
		Map<String,String> validationMap = validator.validateForForgotPassword(username, emailId,forgotPWdOption);
		if(validationMap.size() == 0 && !resetLinkExpire){
			ResponseObject responseobject = this.staffService.verifyUserForForgotPassword(username, emailId);
			logger.info("response: " + responseobject);
			
			if(responseobject.isSuccess()){
				Map<String,String> responsemodel = (Map<String,String>) responseobject.getObject();
				
				if(!StringUtils.isEmpty(forgotPWdOption) && ("username").equals(forgotPWdOption)){
					logger.debug("responsemodel-BUSERNAME"+responsemodel.get(BaseConstants.USERNAME));
					logger.debug("responsemodel-USERNAME"+responsemodel.get("USERNAME"));
					model.addObject("USERNAME", responsemodel.get(BaseConstants.USERNAME));	
				}else{
					model.addObject(BaseConstants.MAIL_ID, responsemodel.get(BaseConstants.MAIL_ID));
				}
				
				model.addObject("QUESTION_LIST", responsemodel.get("QUESTION_LIST"));	
				
				request.getSession().setAttribute(BaseConstants.VERIFIED_USER_QUESTION_LIST,responsemodel.get("QUESTION_LIST"));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
				request.getSession().setAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD,responsemodel.get(BaseConstants.USERNAME));
			
			}else{
				if(responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_WRONG_USER){
					if(username.length()>0)
						validationMap.put("USERNAME_ERROR", getMessage("forgotPassword.username.invalid"));
					else if(emailId.length()>0)
						validationMap.put("MAIL_ID_ERROR", getMessage("forgotPassword.emailId.invalid"));
				}else if(responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_FIRST_LOGIN){
					validationMap.put(BaseConstants.ERROR_MSG, getMessage("forgotPassword.firstTime.login.error"));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
				} else if(responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_LDAP_USER) {
					validationMap.put(BaseConstants.ERROR_MSG, getMessage("forgotPassword.contact.system.administrator.error"));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
				}
				
				model.addObject("USERNAME", username);
				model.addObject(BaseConstants.MAIL_ID, emailId);
			}
		}
		if(resetLinkExpire) {
			validationMap.put(BaseConstants.ERROR_MSG, getMessage("reset.password.link.expired"));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_LINK_EXPIRED);
		}
		if(validationMap.size() > 0){ // Validation failed.
			logger.info("Validation size="+validationMap.size());
			for(String key : validationMap.keySet()){
				model.addObject(key, validationMap.get(key));
			}
			model.addObject("USERNAME", username); 
			model.addObject(BaseConstants.MAIL_ID, emailId);
			model.addObject("FORGOTPWDOPTION",forgotPWdOption);
		}
		return model;
	}
	
	/**
	 * Verifies user for Forgot Password.
	 * @param username
	 * @param emailId
	 * @param forgotPWdOption
	 * @param request
	 * @param response
	 * @return
	 */
		@RequestMapping(value = ControllerConstants.RESET_PASSWORD_LINK, method = RequestMethod.POST)
		public ModelAndView resetPasswordLink(
				@RequestParam(value = "username",required=false,defaultValue="") String username,
				@RequestParam(value = "emailId",required=false,defaultValue="") String emailId,
				@RequestParam(value="forgotPWdOption",required=true) String forgotPWdOption,
				HttpServletRequest request,
				HttpServletResponse response){
			ModelAndView model = new ModelAndView();
			model.setViewName(ViewNameConstants.FORGOT_PASSWORD);
			logger.debug("username::"+username);
			logger.debug("forgotPWdOption::"+forgotPWdOption);
			
			Map<String,String> validationMap = validator.validateForForgotPassword(username, emailId,forgotPWdOption);
			if(validationMap.size() == 0){
				ResponseObject responseobject = this.staffService.getStaffByUsernameOrEmail(username);
				logger.info("response: " + responseobject);
				
				if(responseobject.isSuccess()){
					Staff staff= (Staff) responseobject.getObject();
					if(staff == null) {
						validationMap.put(BaseConstants.ERROR_MSG, getMessage("forgotPassword.contact.system.administrator.error"));
						model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
						responseobject.setSuccess(false);
					}else {
						// code for sending mail if found staff details
						boolean isSent = SendMail.sendMailForPasswordReset(staff,request);
						if(isSent) {
						validationMap.put(BaseConstants.RESPONSE_MSG, getMessage("resetPassword.link.sent.success"));
						model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_LINK_SENT);
						}else {
							validationMap.put(BaseConstants.ERROR_MSG, getMessage("resetPassword.link.sent.error"));
							model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_LINK_SENT);
						}
					}
				}else{
					 if(responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_LDAP_USER) {
						validationMap.put(BaseConstants.ERROR_MSG, getMessage("forgotPassword.contact.system.administrator.error"));
						model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
					}
					model.addObject("USERNAME", username);
					model.addObject(BaseConstants.MAIL_ID, emailId);
				}
			}

			if(validationMap.size() > 0){ // Validation failed.
				logger.info("Validation size="+validationMap.size());
				for(String key : validationMap.keySet()){
					model.addObject(key, validationMap.get(key));
				}
				model.addObject("USERNAME", username); 
				model.addObject(BaseConstants.MAIL_ID, emailId);
				model.addObject("FORGOTPWDOPTION",forgotPWdOption);
			}
			return model;
		}
	
	/**
	 * verify detail for forgot password
	 * @param username
	 * @param emailId
	 * @param question
	 * @param answer
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.VERIFY_DETAILS_FOR_FORGOT_PASSWORD, method = RequestMethod.POST)
	public ModelAndView forgotPasswordVerifyDetails(
			@RequestParam(value = "username",required=false,defaultValue="") String username,
			@RequestParam(value = "emailId",required=false,defaultValue="") String emailId,
			@RequestParam(value = "question1",required=false,defaultValue="") String question,
			@RequestParam(value = "answer1",required=false,defaultValue="") String answer,
			HttpServletRequest request,
			HttpServletResponse response){
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.FORGOT_PASSWORD);
		
		Map<String,String> validationMap = validator.validateQuestionForForgotPwd(question, answer);
		if(validationMap.size() == 0){
			ResponseObject responseobject = this.staffService.verifyDetailsForForgotPassword(username, emailId,question,answer);
			logger.info("response: " + responseobject);
			
			if(responseobject.isSuccess()){
				
				model.addObject("USERNAME", username);
				model.addObject(BaseConstants.MAIL_ID, emailId);
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("resetPassword.page.verification.success"));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_DETAIL_FOR_FORGOT_PASSWORD);
				
			}else{
				if(responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_WRONG_USER){
					if(username.length()>0){
						validationMap.put("USERNAME_ERROR", getMessage("forgotPassword.username.invalid"));
					}else if(emailId.length()>0){
						validationMap.put("EMAIL_ID_ERROR", getMessage("forgotPassword.username.invalid"));
					}
				}else if (responseobject.getResponseCode() == ResponseCode.FORGOT_PASSWORD_WRONG_SECURITY_QUESTION){
					
					validationMap.put("ANSWER1_ERROR",getMessage("forgotPassword.security.question.invalid"));
					model.addObject("QUESTION_LIST", request.getSession().getAttribute(BaseConstants.VERIFIED_USER_QUESTION_LIST));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
				}
			}
		}

		if(validationMap.size() > 0){ // Validation failed.
			for(String key : validationMap.keySet()){
				model.addObject(key, validationMap.get(key));
			}
			model.addObject("QUESTION_LIST", request.getSession().getAttribute(BaseConstants.VERIFIED_USER_QUESTION_LIST));
			model.addObject("USERNAME", username);
			model.addObject(BaseConstants.MAIL_ID, emailId);
			model.addObject("QUESTION1", question);
			model.addObject("ANSWER1", answer);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
		}
		return model;
	}

	/**
	 * Reset Staff's Password
	 * @param newPassword
	 * @param confirmNewPassword
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.RESET_PASSWORD_FOR_FORGOT_PASSWORD, method = RequestMethod.POST)
	public ModelAndView resetPassword(
			@RequestParam(value = "newPassword",required=true) String newPassword,
			@RequestParam(value = "confirmNewPassword",required=true) String confirmNewPassword,
			HttpServletRequest request, 
			RedirectAttributes redirectAttributes) {
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.FORGOT_PASSWORD);

		Map<String,String> validationMap = validator.validateForResetPassword(newPassword, confirmNewPassword);
		
		if(validationMap.size() == 0){
			if(request.getSession().getAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD)!=null){
				String username = (String) request.getSession().getAttribute(BaseConstants.VERIFIED_USERNAME_FOR_FORGOT_PASSWORD);
				
				ResponseObject responseObject = this.staffService.resetPassword(username, newPassword,0);
				if(responseObject.isSuccess() && "reset.password.success".equalsIgnoreCase(responseObject.getResponseCode().toString())  ){
					model = new ModelAndView(new RedirectView(ControllerConstants.WELCOME));
					redirectAttributes.addFlashAttribute(BaseConstants.RESPONSE_MSG, getMessage("reset.password.success"));
				}else{
					model.addObject(BaseConstants.ERROR_MSG, getMessage(responseObject.getResponseCode().toString()));
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_DETAIL_FOR_FORGOT_PASSWORD);
				}
			}
		}else{
			for(String key : validationMap.keySet()){
				model.addObject(key, validationMap.get(key));
			}
			model.addObject("NEW_PASSWORD", newPassword);
			model.addObject("CONFIRM_NEW_PASSWORD", confirmNewPassword);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.VERIFIED_DETAIL_FOR_FORGOT_PASSWORD);
		}
		
		return model;
	}
}