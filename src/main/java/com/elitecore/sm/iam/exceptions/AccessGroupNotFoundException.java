/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import com.elitecore.sm.common.exception.BaseException;


/**
 * @author Sunil Gulabani
 * May 5, 2015
 */
public class AccessGroupNotFoundException extends BaseException{
	
	private static final long serialVersionUID = 1268236760600470662L;
	private int accessGroupId;

	public AccessGroupNotFoundException(int accessGroupId){
		super();
		this.accessGroupId  = accessGroupId;
	}

	public int getAccessGroupId() {
		return accessGroupId;
	}

	public void setAccessGroupId(int accessGroupId) {
		this.accessGroupId = accessGroupId;
	}
}