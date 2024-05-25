/**
 * 
 */
package com.elitecore.sm.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;

/**
 * @author Sunil Gulabani
 * Apr 29, 2015
 */
public class ResponseObject {
	private boolean success; // true - success, false - fail
	private ResponseCode responseCode;
	private Object object;
	private Object auditOldObject;
	private Object auditNewObject;
	private int objectId;
	private NFVResponseCode responseCodeNFV;
	private String msg;
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	private List<Object> auditObjectList;
	
	private Object[] args = null;
	private String moduleName=null;

	public ResponseObject(){
		//Default Constructor
	}
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public ResponseCode getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
	
	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] arg) {
		this.args = arg;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return the auditOldObject
	 */
	public Object getAuditOldObject() {
		return auditOldObject;
	}

	/**
	 * @param auditOldObject the auditOldObject to set
	 */
	public void setAuditOldObject(Object auditOldObject) {
		this.auditOldObject = auditOldObject;
	}

	/**
	 * @return the auditNewObject
	 */
	public Object getAuditNewObject() {
		return auditNewObject;
	}

	/**
	 * @param auditNewObject the auditNewObject to set
	 */
	public void setAuditNewObject(Object auditNewObject) {
		this.auditNewObject = auditNewObject;
	}
	
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>();
		map.put("success", success);
		map.put("responseCode", responseCode);
		map.put("object", object);
		map.put("args", args);
		map.put("moduleName", moduleName);
		return JSONValue.toJSONString(map);
	}

	/**
	 * @return the objectId
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	/**
	 * @return the auditObjectList
	 */
	public List<Object> getAuditObjectList() {
		return auditObjectList;
	}

	/**
	 * @param auditObjectList the auditObjectList to set
	 */
	public void setAuditObjectList(List<Object> auditObjectList) {
		this.auditObjectList = auditObjectList;
	}

	/**
	 * Gets the response code NFV.
	 *
	 * @return the response code NFV
	 */
	public NFVResponseCode getResponseCodeNFV() {
		return responseCodeNFV;
	}

	/**
	 * Sets the response code NFV.
	 *
	 * @param responseCodeNFV the new response code NFV
	 */
	public void setResponseCodeNFV(NFVResponseCode responseCodeNFV) {
		this.responseCodeNFV = responseCodeNFV;
	}

	
}