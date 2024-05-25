/**
 * 
 */
package com.elitecore.sm.iam.service;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * @author Ranjitsinh Reval
 *
 */
public class NoOpAuthenticationManager implements AuthenticationManager {
	
	 @Override
	    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	        return authentication;
	    }

}
