/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.exception.BaseException;
import com.elitecore.sm.iam.model.Staff;

/**
 * @author Sunil Gulabani
 * Apr 29, 2015
 */
public class StaffUniqueContraintException extends BaseException{
	private static final long serialVersionUID = 2058328696730309803L;
	private Staff staff;
	
	public StaffUniqueContraintException(Staff staff, String redirectURL, String requestActionType, BindingResult bindingResult){
		super(redirectURL, requestActionType, bindingResult);
		this.staff = staff;
	}

	public Staff getStaff() {
		return staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

}
