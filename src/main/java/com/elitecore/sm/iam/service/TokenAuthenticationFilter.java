package com.elitecore.sm.iam.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Takes care of HTTP request/response pre-processing for login/logout and token
 * check.
 * Login can be performed on any URL, logout only on specified
 * {@link #logoutLink}.
 * All the interaction with Spring Security should be performed via
 * {@link AuthenticationService}.
 * <p>
 * {@link SecurityContextHolder} is used here only for debug outputs. While this
 * class is configured to be used by Spring Security (configured filter on
 * FORM_LOGIN_FILTER position), but it doesn't really depend on it at all.
 */
public final class TokenAuthenticationFilter extends GenericFilterBean {

	private static final String HEADER_TOKEN = "X-Auth-Token";

	/**
	 * Request attribute that indicates that this filter will not continue with
	 * the chain.
	 * Handy after login/logout, etc.
	 */
	private static final String REQUEST_ATTR_DO_NOT_CONTINUE = "MyAuthenticationFilter-doNotContinue";

	@SuppressWarnings("unused")
	private final String logoutLink;
	private final AuthenticationService authenticationService;

	public TokenAuthenticationFilter(AuthenticationService authenticationService, String logoutLink) {
		this.authenticationService = authenticationService;
		this.logoutLink = logoutLink;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestURL = httpRequest.getRequestURL().toString();
		final String message = "message";
		final String success = "success";

		if (requestURL.indexOf("login") >= 0) {
			logger.info("Login api called");
			chain.doFilter(request, response);
		}else if(requestURL.indexOf("api/nfv") >= 0){
				logger.info("NFV api called");
				chain.doFilter(request, response);
		} else {
			JSONObject jsonObject = checkToken(httpRequest);
			if (jsonObject.getBoolean(success)) {
				if (canRequestProcessingContinue(httpRequest)) {
					chain.doFilter(request, response);
				} else {
					httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, jsonObject.getString(message));
				}
			} else {
				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, jsonObject.getString(message));
			}
		}
	}

	/** Returns true, if request contains valid authentication token. */
	private JSONObject checkToken(HttpServletRequest httpRequest) throws IOException {
		JSONObject jsonObject = new JSONObject();
		final String message = "message";
		final String success = "success";
		String token = httpRequest.getHeader(HEADER_TOKEN);
		if (token == null) {
			jsonObject.put(success, false);
			jsonObject.put(message, "No token found");
			return jsonObject;
		}
		jsonObject = authenticationService.checkToken(token);
		if (jsonObject.getBoolean(success)) {
			logger.info(HEADER_TOKEN + " valid for: "
					+ SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			return jsonObject;
		} else {
			logger.info(" *** Invalid " + HEADER_TOKEN + ' ' + token);
			doNotContinueWithRequestProcessing(httpRequest);
			return jsonObject;
		}
	}

	/**
	 * This is set in cases when we don't want to continue down the filter
	 * chain. This occurs
	 * for any {@link HttpServletResponse#SC_UNAUTHORIZED} and also for login or
	 * logout.
	 */
	private void doNotContinueWithRequestProcessing(HttpServletRequest httpRequest) {
		httpRequest.setAttribute(REQUEST_ATTR_DO_NOT_CONTINUE, "");
	}

	private boolean canRequestProcessingContinue(HttpServletRequest httpRequest) {
		return httpRequest.getAttribute(REQUEST_ATTR_DO_NOT_CONTINUE) == null;
	}
}
