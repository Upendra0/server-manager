package com.elitecore.sm.services.model;


/**
 * @author ranjitsinh.reval
 *
 */
public enum CDRFileDateTypeEnum {
	MAXIMUM("MAXIMUM"), MINIMUM("MINIMUM");
	
	private String value;
	

	CDRFileDateTypeEnum(String value) {
		this.value = value;
	}

	/**
	 * 
	 * @return constant value
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
