/**
 * 
 */
package com.elitecore.sm.iam.exceptions;

import org.springframework.security.authentication.LockedException;

import com.elitecore.sm.common.constants.BaseConstants;


/**
 * @author Vishal Lakhyani
 *
 */
public class StaffAccountLockedException extends LockedException{
	private static final long serialVersionUID = -8019599834007611508L;

	public StaffAccountLockedException(){
		super(BaseConstants.ACCOUNT_LOCKED_BY_ADMIN);
	}
}
