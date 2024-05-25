package com.elitecore.sm.iam.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests. Login-like function is featured
 * in {@link TokenAuthenticationFilter} and this does not perform or suggests any redirection.
 * This object is hit whenever user is not authorized (anonymous) and secured resource is requested.
 */
public final class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		logger.debug("Unauthorized Entry point URL :: " + request.getRequestURI());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}