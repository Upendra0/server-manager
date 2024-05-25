/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

import com.elitecore.sm.common.constants.BaseConstants;

/**
 * @author Sunil Gulabani
 * Jun 9, 2015
 */
public class LoginIPUnauthorizedException extends BadCredentialsException{
	private static final long serialVersionUID = -8019599834007611508L;

	public LoginIPUnauthorizedException(){
		super(BaseConstants.LOGIN_IP_UNAUTHORIZED);
	}
}
