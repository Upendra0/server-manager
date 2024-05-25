/**
 * 
 */
package com.elitecore.sm.license.exceptions;

import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.exception.BaseException;
import com.elitecore.sm.license.model.Circle;

/**
 * @author Sunil Gulabani
 * Apr 29, 2015
 */
public class CircleUniqueContraintException extends BaseException{
	private static final long serialVersionUID = 2058328696730309803L;
	private Circle circle;
	
	public CircleUniqueContraintException(Circle circle, String redirectURL, String requestActionType, BindingResult bindingResult){
		super(redirectURL, requestActionType, bindingResult);
		this.circle = circle;
	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

}
