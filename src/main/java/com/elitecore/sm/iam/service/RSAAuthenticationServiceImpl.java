package com.elitecore.sm.iam.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.rest.client.RSARestClient;
import com.elitecore.sm.util.MapCache;

@Service
public class RSAAuthenticationServiceImpl implements RSAAuthenticationService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private String userName;
	private String token;
	private String clientIP;	
	private final String INITIALIZE_METHOD="initialize";
	private final String VERIFY_METHOD="verify";
	private String authnAttemptId, messageId;
		
	public RSAAuthenticationServiceImpl() {
	}
				
	public Map<String, String> initialize(String userName, String token) throws Exception {
		
		Map<String, String> responseMap = new HashMap<>();
		try {
			String RSA_URL = MapCache.getConfigValueAsString(BaseConstants.RSA_SERVER_URL,"");
			String request = getInitializeRequest();			
			String response = RSARestClient.restCall(RSA_URL+INITIALIZE_METHOD, request);
			if(response != null) {
				JSONObject jsonResponse = new JSONObject(response);
				if(jsonResponse!=null && jsonResponse.has("attemptReasonCode")) {
					String attemptReasonCode = jsonResponse.getString("attemptReasonCode");
					if(StringUtils.isNotEmpty(attemptReasonCode) && attemptReasonCode.equalsIgnoreCase("AUTHENTICATION_REQUIRED")) {
						if(jsonResponse.has("context")) {
							JSONObject  requestContext = jsonResponse.getJSONObject("context");
							authnAttemptId = requestContext.getString("authnAttemptId");
							messageId = requestContext.getString("messageId");
							responseMap.put("IS_INITIALIZED", "TRUE");														
						}
					}else{
						responseMap.put("IS_INITIALIZED", "FALSE");
						responseMap.put("ERROR_MESSAGE", "User might not be created in RSA server. Please contact your administrator.");						
					}
				}
			}
		} catch (JSONException e) {
			logger.error("Error occured in initialize rsa user method :  " + e.getMessage(), e);
			responseMap.put("IS_INITIALIZED", "FALSE");
			responseMap.put("ERROR_MESSAGE", "Invalid rsa server url configured. Please contact your administrator.");
		} catch (Exception e) {
			logger.error("Error occured in initialize rsa user method :  " + e.getMessage(), e);
			responseMap.put("IS_INITIALIZED", "FALSE");
			responseMap.put("ERROR_MESSAGE", "Invalid rsa server url configured. Please contact your administrator.");
		}        
		return responseMap;		
	}
	
	public boolean verify() throws Exception {
		
		try {
			String RSA_URL = MapCache.getConfigValueAsString(BaseConstants.RSA_SERVER_URL,"");
			String request = getVerifyRequest();
			String response = RSARestClient.restCall(RSA_URL+VERIFY_METHOD, request);
			
			if(response != null) {	
				JSONObject jsonResponse = new JSONObject(response);
				if(jsonResponse!=null && jsonResponse.has("subjectCredentials")) {							
					JSONArray subjectCredentials = (JSONArray) jsonResponse.get("subjectCredentials");
					for (int i=0; i < subjectCredentials.length(); i++) {
						JSONObject json = subjectCredentials.getJSONObject(i);
						if(json != null && json.has("methodResponseCode")) {
							String methodResponseCode =  json.getString("methodResponseCode");
							if(methodResponseCode.equalsIgnoreCase("SUCCESS")) {
								return true;
							}
						}
					}
				}
			}			
		}catch (JSONException e) {
			logger.error("Error occured in initialize verify user method :  " + e.getMessage(), e);				
		} catch (Exception e) {
			logger.error("Error occured in initialize verify user method :  " + e.getMessage(), e);	
		}		
		return false;
	}
	
	public boolean initiateAndVerify() {
		//TODO
		return false;
	}

	private String getInitializeRequest() throws JSONException {
		JSONObject jsonObject = new JSONObject(BaseConstants.RSA_INITIATE_REQUEST);
		jsonObject.put("clientId", clientIP);
		jsonObject.put("subjectName", userName);		
		JSONObject context = (JSONObject) jsonObject.get("context");
		context.put("messageId", String.valueOf(System.currentTimeMillis()));
		return jsonObject.toString();
	}
	
	private String getVerifyRequest() throws JSONException {
		JSONObject jsonObject = new JSONObject(BaseConstants.RSA_VERIFY_REQUEST);			
		JSONArray subjectCredentials = (JSONArray) jsonObject.get("subjectCredentials");
		for (int i=0; i < subjectCredentials.length(); i++) {
				JSONObject json = subjectCredentials.getJSONObject(i);
				JSONArray collArr = (JSONArray) json.get("collectedInputs");
				for (int j=0; j < collArr.length(); j++) {
				JSONObject collectedInputs = (JSONObject) collArr.getJSONObject(j);				
				collectedInputs.put("value", token);
			}
		}
		JSONObject context = (JSONObject) jsonObject.get("context");
		context.put("messageId", String.valueOf(System.currentTimeMillis()));
		JSONObject jsonContextChild = new JSONObject();
		jsonContextChild.put("authnAttemptId", authnAttemptId);
		jsonContextChild.put("messageId", String.valueOf(System.currentTimeMillis()));
		jsonContextChild.put("inResponseTo", messageId);
		JSONObject jsonContext = new JSONObject();
		jsonContext.put("context", jsonContextChild);
		
		return jsonObject.toString();
	}
	
	private String getInitializeVerifyRequest() throws JSONException {
		//TODO
		return null;
	}
		
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getClientIP() {
		return clientIP;

	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	@Override
	public boolean status() {
		// TODO Auto-generated method stub
		return false;
	}
	
}