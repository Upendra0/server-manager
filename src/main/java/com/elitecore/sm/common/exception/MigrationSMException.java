package com.elitecore.sm.common.exception;

import com.elitecore.sm.common.util.ResponseObject;

public class MigrationSMException extends SMException {

	private static final long serialVersionUID = 2010629025918798019L;

	private  ResponseObject responseObject; //NOSONAR
	
	public ResponseObject getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(ResponseObject responseObject) {
		this.responseObject = responseObject;
	}
	
	public MigrationSMException(ResponseObject responseObject, String exceptionMessage) {
		super(exceptionMessage);
		this.responseObject = responseObject;
	}

	public MigrationSMException(String exceptionMessage) {
		super(exceptionMessage);
	}

	/**
	 * Wraps the underlying exception.
	 * @param e
	 */
	public MigrationSMException(Exception e) {
		super(e);
	}

}
