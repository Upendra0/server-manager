package com.elitecore.sm.iam.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.exceptions.LoginIPUnauthorizedException;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.util.EliteUtils;

public class LimitLoginAuthenticationServiceImpl extends DaoAuthenticationProvider {

	private Logger loggerLogin = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private StaffService staffService;
		
	@Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
	
	/**
	 * Authenticates the user credentials
	 */
	@SuppressWarnings("serial")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
							
		String userName = authentication.getName();
		if(staffService.isLDAPStaff(userName)) {
			loggerLogin.info("This might be a LDAP user, therefore database authentication is not supported for this staff.");
			eliteUtils.addLoginAuditDetails(0, userName,AuditConstants.LOGIN_AS_LDAP,null);
			throw new AuthenticationException(userName + " might be a LDAP user, therefore database authentication is not supported for this staff.") {};
		}				
		StaffService staffServiceImpl = (StaffService) SpringApplicationContext.getBean("staffService"); // getting spring bean for aop context issue.	
		try {
			validateCredentialsAreBlank();			
			Authentication auth = super.authenticate(authentication);			
			// if reach here, means login success, else exception will be thrown
			// reset the wrong attempts
			Staff staff = staffService.getStaffDetails(authentication.getName());
			staffServiceImpl.resetFailAttempts(staff);
			eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.UPDATE_STAFF_DETAILS, BaseConstants.UPDATE_ACTION );
			eliteUtils.addLoginAuditDetails(staff.getId(), staff.getUsername(),AuditConstants.LOGIN_ACTION,null);
			HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			session.setAttribute("userName", staff.getUsername());
			return auth;
		} catch (LoginIPUnauthorizedException  e) {
			//using Java 7 feature of multiple catch exceptions
			loggerLogin.info(e.getMessage());
			if(e.getMessage()!=null && !e.getMessage().equals(BaseConstants.ACCOUNT_LOCKED_BY_ADMIN)){
				try {
					staffServiceImpl.updateFailAttempts(authentication.getName());
				} catch (CloneNotSupportedException e1) {
					loggerLogin.error(e1);
				}catch (LockedException exp) {
					loggerLogin.info(exp.getMessage());
					eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.LOCKED_LOGIN_DENIED,null); // No staff id for invalid credentials. Adding only invalid attempts action done by user.
					throw exp;
				}
			}
			eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.UNAUTHORIZED_IP_LOGIN_DENIED,null); 
			throw e;
	
		}catch (LockedException exp) {//NOSONAR
			String error;
			Staff staffdetails = staffService.getStaffDetails(authentication.getName());
			int wrongAttempts = staffdetails.getWrongAttempts();
			loggerLogin.info(exp.getMessage());
			if(wrongAttempts == 0){
				error = BaseConstants.ACCOUNT_LOCKED_BY_ADMIN;
			}else{
				error = exp.getMessage();
			}
			eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.LOCKED_LOGIN_DENIED,null); // No staff id for invalid credentials. Adding only invalid attempts action done by user.
			throw new LockedException(error);
			
		}
		catch (DisabledException exp) {
			loggerLogin.info(exp.getMessage());
			eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.DISABLED_LOGIN_DENIED,null); // No staff id for invalid credentials. Adding only invalid attempts action done by user.
			throw exp;
		}		
		catch (InternalAuthenticationServiceException | BadCredentialsException  e) {
			loggerLogin.info(e.getMessage());
			ResponseObject response=null;
			if(e.getMessage()!=null && e.getMessage().equals(BaseConstants.LOGIN_IP_UNAUTHORIZED)){
				eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.UNAUTHORIZED_IP_LOGIN_DENIED,null); 
				throw e;
			}else{
				try {
					 response=staffServiceImpl.updateFailAttempts(authentication.getName());
				
				} catch (CloneNotSupportedException e1) {
					loggerLogin.error(e1);
				}
			
				if(response!=null){
					if(ResponseCode.USER_ACCOUNT_LOCKED.equals(response.getResponseCode())){
						eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.LOCKED_LOGIN_DENIED,null); // No staff id for invalid credentials. Adding only invalid attempts action done by user.
					}else{
						eliteUtils.addLoginAuditDetails(0, authentication.getName(),AuditConstants.INVALID_LOGIN,null); // No staff id for invalid credentials. Adding only invalid attempts action done by user.
					}
				}
			
			throw e;
			}
		} catch (CloneNotSupportedException e) {
			loggerLogin.info(e.getMessage());
			try {
				throw new SMException(e);
			} catch (SMException e1) {
				loggerLogin.error(e1);
			}
			return authentication; 
		}		
	}
	
	private void validateCredentialsAreBlank() throws AuthenticationException {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(request.getParameter("username").length()==0 && request.getParameter("password").length()==0){
			throw new BadCredentialsException(BaseConstants.USERNAME_PASSWORD_BLANK);
		}else if(request.getParameter("username").length()==0){
			throw new BadCredentialsException(BaseConstants.USERNAME_BLANK);
		}else if(request.getParameter("password").length()==0){
			throw new BadCredentialsException(BaseConstants.PASSWORD_BLANK);
		}
	}
	
	public StaffService getStaffService() {
		return staffService;
	}

	public void setStaffService(StaffService staffService) {
		this.staffService = staffService;
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return super.supports(authentication);
	}
}