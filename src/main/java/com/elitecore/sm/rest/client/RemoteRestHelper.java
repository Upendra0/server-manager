package com.elitecore.sm.rest.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.elitecore.sm.rest.model.RequestObject;
import com.elitecore.sm.rest.model.ResponseObject;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoteRestHelper {

	private String ipAddress;

	private int port;
	
	private int offSet = 10000;

	private String methodName;

	private Object[] methodParamValues;
	
	private RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
	
	private ResponseObject<Object> responseObject =  null;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private int connectionTimeout = 5000;
	
	private int readTimeout = 50000;
	
	public RemoteRestHelper() {
	}

	public RemoteRestHelper(String ipAddress, int port) {
		this.ipAddress = ipAddress;
		this.port = port;
	}

	public RemoteRestHelper(String ipAddress, int port, String methodName, Object[] methodParamValues) {
		this.ipAddress = ipAddress;
		this.port = port;
		this.methodName = methodName;
		this.methodParamValues = methodParamValues;
	}

	public Object invoke() {
    	return postServiceCallWithSimpleJSON();
	}

	public Object postServiceCallWithSimpleJSON() {	
				
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);		

		HttpEntity<String> entity = new HttpEntity<String>(prepareRequestData(), headers);
		String response = restTemplate.postForObject("http://" + ipAddress + ":" + (port+offSet)
				+ "/mediation/rest/services/"+methodName, entity, String.class);
		if(!StringUtils.isEmpty(response)) {
			getReponseData(response);
		}
		return responseObject.getData();
	}
	
	private String prepareRequestData() {
		String jsonRequestData = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		if(methodParamValues!=null && methodParamValues.length>0) {
			Map<String,Object> requestDataMap =  new HashMap<>();
			for(int idx=0; idx<methodParamValues.length;idx++) {
				requestDataMap.put("KEY#"+(idx+1), methodParamValues[idx]);
			}
			if(requestDataMap!=null && requestDataMap.size()>0) {
				try {					
					RequestObject<Object> requestObj =  new RequestObject<>();
					requestObj.setData(requestDataMap);
					jsonRequestData = mapper.writeValueAsString(requestObj);
				} 
				//catch (JsonGenerationException|JsonMappingException e) {
				//	logger.error(e);
				//}
				catch (IOException e) {
					logger.error(e);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
		return jsonRequestData;					
	}
	
	@SuppressWarnings("unchecked")
	private void getReponseData(String response) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping();
		try {
			responseObject = mapper.readValue(response, ResponseObject.class);
		} 
		//catch (JsonParseException e) {
		//	logger.error(e);
		//}
		catch (JsonMappingException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
	}
	
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getMethodParamValues() {
		return methodParamValues;
	}

	public void setMethodParamValues(Object[] methodParamValues) {
		this.methodParamValues = methodParamValues;
	}
	
    //  MEDSUP-1783 DMC | Dictionary page not opening in SM change in the connection / retry and read timeout values by overriding ClinetHttpRequestFactory
	//  Override timeouts in request factory
	private SimpleClientHttpRequestFactory getClientHttpRequestFactory() 
	{
	    SimpleClientHttpRequestFactory clientHttpRequestFactory
	                      = new SimpleClientHttpRequestFactory();
	    //Connect timeout
	    clientHttpRequestFactory.setConnectTimeout(connectionTimeout);
	     
	    //Read timeout
	    clientHttpRequestFactory.setReadTimeout(readTimeout);
	    return clientHttpRequestFactory;
	}

}
