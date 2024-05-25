package com.elitecore.sm.rest.client;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.elitecore.core.commons.util.Logger;
import com.elitecore.core.commons.util.restservice.RestServiceConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.util.MapCache;

public class RSARestClient {

	private static final String MODULE = "RSARestClient";
	private static ObjectMapper objectMapper;
		
	public static String restCall(String restURL, String request) throws Exception {//NOSONAR
		try {						
			Logger.logInfo(MODULE, "Rest Service URL =  "+ restURL);
	        URL httpRestURL = new URL(restURL);
	        HttpURLConnection httpRestConn = (HttpURLConnection)httpRestURL.openConnection();
	        
	        httpRestConn.setConnectTimeout(RestServiceConstants.DEFAULT_REST_SERVICE_CON_TIMEOUT);
	        httpRestConn.setRequestProperty(RestServiceConstants.REST_SERVICE_CONTENT_TYPE, RestServiceConstants.REST_SERVICE_CONTENT_TYPE_VALUE);
	        httpRestConn.setDoOutput(true);
	        httpRestConn.setDoInput(true);
	        httpRestConn.setRequestProperty("client-key", MapCache.getConfigValueAsString(BaseConstants.CLIENT_KEY,""));	        
	        httpRestConn.setRequestProperty("Content-Type", RestServiceConstants.APPLICATION_JSON);
	        httpRestConn.setRequestMethod(RestServiceConstants.REST_SERVICE_REQUEST_METHOD);

	        // write the request
	        OutputStream os = httpRestConn.getOutputStream();
	        if(request != null) {
	        	os.write(request.getBytes(RestServiceConstants.DEFAULT_CHAR_SET));
	        }
	        os.close();
	        
	        // read the response
	        InputStream in = new BufferedInputStream(httpRestConn.getInputStream());
	        int responseCode = httpRestConn.getResponseCode();
	        if (responseCode != RestServiceConstants.SUCCESS_RESPONSE_CODE) {
				throw new Exception("Failed : HTTP error code : "+ responseCode);//NOSONAR
			}
	        String result = IOUtils.toString(in, "UTF-8");
	        in.close();
	        httpRestConn.disconnect();
	        return result;
		} catch(Exception ex) {
			Logger.logTrace(MODULE, "Error - in server call operation,Reason : " + ex);
			throw new Exception(ex);//NOSONAR
		}
	}
	
	public static ObjectMapper getObjectMapper(){
		if(objectMapper != null){
			return objectMapper;
		}
		objectMapper = new ObjectMapper();
		objectMapper.enableDefaultTyping();
		return objectMapper;
	}
	
}
