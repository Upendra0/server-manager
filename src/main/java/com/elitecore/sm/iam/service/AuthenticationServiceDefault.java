package com.elitecore.sm.iam.service;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.util.MD5EncryptionDecryption;

/**
 * Service responsible for all around authentication, token checks, etc.
 * This class does not care about HTTP protocol at all.
 */
public class AuthenticationServiceDefault implements AuthenticationService {
	
	private static Logger logger = Logger.getLogger(AuthenticationServiceDefault.class);

	@Autowired
	private ApplicationContext applicationContext;

	private final AuthenticationManager authenticationManager;
	private final TokenManager tokenManager;

	public AuthenticationServiceDefault(AuthenticationManager authenticationManager, TokenManager tokenManager) {
		this.authenticationManager = authenticationManager;
		this.tokenManager = tokenManager;
	}

	@PostConstruct
	public void init() {
		logger.info(" *** AuthenticationServiceImpl.init with: " + applicationContext);
	}

	@Override
	public JSONObject authenticate(String login, String password) throws SMException {
		JSONObject jsonObject = new JSONObject();
		final String message = "message";
		final String success = "success";
		final String token = "token";
		logger.info(" *** AuthenticationServiceImpl.authenticate");

		// Here principal=username, credentials=password
		String encryptedPassword = MD5EncryptionDecryption.encrypt(password);		
		Authentication authentication = new UsernamePasswordAuthenticationToken(login, encryptedPassword);
		try {
			authentication = authenticationManager.authenticate(authentication);
			// Here principal=UserDetails (UserContext in our case), credentials=null (security reasons)
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.getPrincipal() != null) {
				UserDetails userContext = (UserDetails) authentication.getPrincipal();
				TokenInfo tokenInfo = tokenManager.createNewToken(userContext);
				jsonObject.put(success, true);
				jsonObject.put(message, "Login successful");
				jsonObject.put(token, tokenInfo.getToken());
				return jsonObject;
			} else {
				jsonObject.put(success, false);
				jsonObject.put(message, "Invalid username or password");
			}
		} catch (AuthenticationException e) {
			jsonObject.put(success, false);
			jsonObject.put(message, "Invalid username or password");
			logger.error(" *** AuthenticationServiceImpl.authenticate - FAILED: " + e);
		}
		return jsonObject;
	}

	@Override
	public JSONObject checkToken(String token) {
		JSONObject jsonObject = new JSONObject();
		final String success = "success";
		final String message = "message";
		logger.info("AuthenicationServiceDefaul checking validating request token.");
		TokenInfo tokenInfo = tokenManager.getTokenInfo(token);
		if(tokenInfo != null) {
			if(tokenInfo.getExpiredDate().before(new Date())) {
				logger.info("token is expired");
				jsonObject.put(success, false);
				jsonObject.put(message, "Token is expired");
			} else {
				
				UserDetails userDetails = tokenManager.getUserDetails(token);
				
				if(userDetails != null){
					Authentication securityToken = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(securityToken);
					jsonObject.put(success, true);
					jsonObject.put(message, "Token validated successfully");
				}else{
					jsonObject.put(success, false);
					jsonObject.put(message, "No user found for this token");
				}
			}
		} else {
			jsonObject.put(success, false);
			jsonObject.put(message, "Invalid token found");
		}
		return jsonObject;
	}

	@Override
	public void logout(String token) {
		UserDetails logoutUser = tokenManager.removeToken(token);
		logger.info(" *** AuthenticationServiceImpl.logout: " + logoutUser);
		SecurityContextHolder.clearContext();
	}

	@Override
	public UserDetails currentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return (UserDetails) authentication.getPrincipal();
	}
}
