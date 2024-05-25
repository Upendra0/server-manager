package com.elitecore.sm.common.exception;

import org.dozer.MappingException;

public class BooleanException extends MappingException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public BooleanException(String exceptionMessage) {
		super(exceptionMessage);
	}

	/**
	 * Wraps the underlying exception.
	 * @param e
	 */
	public BooleanException(Exception e) {
		super(e);
	}

}
