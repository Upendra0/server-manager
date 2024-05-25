/**
 * 
 */
package com.elitecore.sm.common.exception;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.constants.ViewNameConstants;

/**
 * @author Sunil Gulabani May 4, 2015
 */
public abstract class BaseException extends Exception  {
	private static final long serialVersionUID = 1L;
	private final String redirectURL;
	private final String requestActionType;
	private final transient BindingResult bindingResult; 

	public BaseException(String redirectURL, String requestActionType,
			BindingResult bindingResult) {
		this.redirectURL = redirectURL;
		this.requestActionType = requestActionType;
		this.bindingResult = bindingResult;
	}

	public BaseException() {
		this.redirectURL = ViewNameConstants.GENERIC_ERROR_PAGE;
		this.requestActionType = null;
		this.bindingResult = null;
	}

	public BaseException(String message) {
		super(message);
		this.redirectURL = ViewNameConstants.GENERIC_ERROR_PAGE;
		this.requestActionType = null;
		this.bindingResult = null;

	}

	public BaseException(Exception e) {
		super(e);
		this.redirectURL = ViewNameConstants.GENERIC_ERROR_PAGE;
		this.requestActionType = null;
		this.bindingResult = null;

	}

	public String getRedirectURL() {

		if (StringUtils.isEmpty(redirectURL))

			return ViewNameConstants.GENERIC_ERROR_PAGE;

		else
			return redirectURL;
	}

	public String getRequestActionType() {
		return requestActionType;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

}