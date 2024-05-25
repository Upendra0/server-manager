package com.elitecore.sm.composer.model;



public enum EnumComposerAttributeHeader {
	    SEQ_NO ("Seq.No"),
        DESTINATION_FIELD_NAME ("Destination Field Name"),
        UNIFIED_FIELD_NAME ("Unified Field Name"),
        DESCRIPTION ("Description"),
        DATA_TYPE ("Data Type"),
        DEFAULT_VALUE ("Default Value"),
        DATE_FORMAT ("Date Format"),
        TRIM_CHARACTER ("Trim Character"),
        TRIM_POSITION ("Trim Position"),
        REPLACE_CONDITION_LIST ("Replace Condition List"),
        ENABLE_PADDING ("Enable Padding"),
        PADDING_LENGTH ("Padding Length"),
        TYPE ("Type"),
        PADDING_CHARACTER ("Padding Character"),
        PREFIX ("Prefix"),
        SUFFIX ("Suffix"),
	    ASN1_DATATYPE("ASN1 Data type"),
	    DESTINATION_FIELD_FORMAT("Destination Field Format"),
	    ARGUMENT_DATATYPE("Argument Data Type"),
	    CHOICE_ID("Choice Id"),
	    CHILD_ATTRIBUTES("Child Attributes"),
		LENGTH("Length");
		
	private String name;

	private EnumComposerAttributeHeader(String name) {
		this.name = name;
	}

	public String getEnumComposerAttributeHeader() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
