/**
 * 
 */
package com.elitecore.sm.common.util;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONValue;
import org.springframework.util.StringUtils;

/**
 * @author Sunil Gulabani
 * Apr 13, 2015
 */
public class AjaxResponse {
	private String responseCode ;
	private String responseMsg;
	private Object object;
	private String moduleName;
	
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>(); // sonar  L-24
		map.put("code", responseCode);
		map.put("msg", responseMsg);

		if(object!=null){
			map.put("object", object);
		}
		
		if(!StringUtils.isEmpty(moduleName)){
			map.put("module", moduleName);
		}
		return JSONValue.toJSONString(map);
	}
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
}
