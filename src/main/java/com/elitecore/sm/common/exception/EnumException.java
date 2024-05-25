package com.elitecore.sm.common.exception;

import org.dozer.MappingException;

public class EnumException extends MappingException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public EnumException(String exceptionMessage) {
		super(exceptionMessage);
	}

	/**
	 * Wraps the underlying exception.
	 * @param e
	 */
	public EnumException(Exception e) {
		super(e);
	}

}
