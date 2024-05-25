/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.exception.BaseException;
import com.elitecore.sm.iam.model.AccessGroup;

/**
 * @author Sunil Gulabani
 * Apr 29, 2015
 */
public class AccessGroupUniqueContraintException extends BaseException{
	private static final long serialVersionUID = -2399875783790411857L;
	private AccessGroup accessGroup;

	public AccessGroupUniqueContraintException(){}
	
	public AccessGroupUniqueContraintException(AccessGroup accessGroup, String redirectURL, String requestActionType, BindingResult bindingResult){
		super(redirectURL, requestActionType, bindingResult);
		this.accessGroup = accessGroup;
	}

	public AccessGroup getAccessGroup() {
		return accessGroup;
	}

	public void setAccessGroup(AccessGroup accessGroup) {
		this.accessGroup = accessGroup;
	}
}