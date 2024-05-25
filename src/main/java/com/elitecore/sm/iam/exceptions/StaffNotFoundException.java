/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import com.elitecore.sm.common.exception.BaseException;


/**
 * @author Sunil Gulabani
 * May 5, 2015
 */
public class StaffNotFoundException extends BaseException{
	
	private static final long serialVersionUID = -2460901558068892935L;
	private int staffId;
	private String operation;

	public StaffNotFoundException(int staffId, String operation){
		super();
		this.staffId  = staffId;
		this.operation = operation;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
}