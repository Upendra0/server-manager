package com.elitecore.sm.drivers.model;



public enum EnumDbDriverAttributeHeader {
	    DATABASE_FIELD_NAME ("Database Field Name"),
        UNIFIED_FIELD_NAME ("Unified Field"),
        DATA_TYPE ("Data Type"),
        DEFAULT_VALUE ("Default Value"),
        ENABLE_PADDING ("Enable Padding"),
		PADDING_LENGTH ("Padding Length"),
		TYPE ("Type"),
		PADDING_CHARACTER ("Padding Character"),
		PREFIX("Prefix"),
		SUFFIX("Suffix");
		
        
		
	private String name;

	private EnumDbDriverAttributeHeader(String name) {
		this.name = name;
	}

	public String getEnumDbDriverAttributeHeader() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
