package com.elitecore.sm.services.model;

public enum HashSeparatorEnum {
	UNDERSCORE("_")/*,HYPHEN("-"),DOT("."),PIPE("|")*/;
	
	private String value;

	HashSeparatorEnum(String value) {
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
