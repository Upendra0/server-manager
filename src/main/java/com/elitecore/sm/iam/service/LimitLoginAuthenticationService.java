package com.elitecore.sm.iam.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


public interface LimitLoginAuthenticationService{
	public Authentication authenticate(Authentication authentication) throws AuthenticationException ;
}