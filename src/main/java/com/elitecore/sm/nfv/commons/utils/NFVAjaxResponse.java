/**
 * 
 */
package com.elitecore.sm.nfv.commons.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONValue;
import org.springframework.util.StringUtils;

/**
 * The Class NFVAjaxResponse.
 *
 * @author sagar shah
 * July 13, 2017
 */
public class NFVAjaxResponse {
	
	/** The response code. */
	private String responseCode ;
	
	/** The response msg. */
	private String responseMsg;
	
	/** The object. */
	private Object object;
	
	/** The module name. */
	private String moduleName;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>(); // sonar  L-24
		map.put("responseCode", responseCode);
		map.put("responseMessage", responseMsg);

		if(object!=null){
			map.put("object", object);
		}
		
		if(!StringUtils.isEmpty(moduleName)){
			map.put("module", moduleName);
		}
		return JSONValue.toJSONString(map);
	}
	
	/**
	 * Gets the response code.
	 *
	 * @return the response code
	 */
	public String getResponseCode() {
		return responseCode;
	}
	
	/**
	 * Sets the response code.
	 *
	 * @param responseCode the new response code
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	/**
	 * Gets the response msg.
	 *
	 * @return the response msg
	 */
	public String getResponseMsg() {
		return responseMsg;
	}
	
	/**
	 * Sets the response msg.
	 *
	 * @param responseMsg the new response msg
	 */
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	
	/**
	 * Gets the object.
	 *
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	
	/**
	 * Sets the object.
	 *
	 * @param object the new object
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	
	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * Sets the module name.
	 *
	 * @param moduleName the new module name
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
}
