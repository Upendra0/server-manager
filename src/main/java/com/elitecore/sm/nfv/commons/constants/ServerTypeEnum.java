/**
 * 
 */
package com.elitecore.sm.nfv.commons.constants;

/**
 * The Enum ServerTypeEnum.
 *
 * @author sagar shah
 * July 13, 2017
 */

public enum ServerTypeEnum {
	
	/** The mediation. */
	MEDIATION(1),
	
	/** The cgf. */
	CGF(2),
	
	/** The iplms. */
	IPLMS(3)
	;
	
	/** The code. */
	private final int code;

	/**
	 * Instantiates a new server type enum.
	 *
	 * @param code the code
	 */
	private ServerTypeEnum(int code) {
		this.code = code;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
}