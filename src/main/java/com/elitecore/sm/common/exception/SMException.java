package com.elitecore.sm.common.exception;

public class SMException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3149692589993352101L;

	public SMException(String exceptionMessage) {
		super(exceptionMessage);
	}

	/**
	 * Wraps the underlying exception.
	 * @param e
	 */
	public SMException(Exception e) {
		super(e);
	}
}
