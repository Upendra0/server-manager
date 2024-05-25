package com.elitecore.sm.parser.model;



public enum EnumParserAttributeHeader {
	    SOURCE_FIELD ("Source Field"),
	    SOURCE_FIELD_NAME ("Source Field Name"),	
	    UNIFIED_FIELD ("Unified Field"),
	    DESCRIPTION ("Description"),
	    DEFAULT_VALUE ("Default Value"),
	    TRIM_CHAR ("Trim Char"),
	    TRIM_POSITION ("Trim Position"),
	    SOURCE_FIELD_FORMAT ("Source Field Format"),
	    DATE_FORMAT ("Date Format"),
	    IP_PORT_UNIFIED_FIELD ("IP-Port Unified field (port)"),
        IP_PORT_SEPERATOR ("IP-Port separator"),
		START_LENGTH("Start Length"),
		END_LENGTH("End Length"),
		READ_AS_BITS("Read As Bits"),
		BIT_START_LENGTH("Bit Start Length"),
		BIT_END_LENGTH("Bit End Length"),
		PREFIX("Prefix"),
		POSTFIX("Postfix"),
		LENGTH("Length"),
		RIGHT_DELIMETER("Right Delimiter"),
		ASN1_DATA_TYPE("ASN1 Data type"),
		CHILD_ATTRIBUTES("Child Attributes"),
		IS_RECORD_INITIALIZER("Is Record Initializer"),
		CHOICEID_HOLDER_UNIFIED_fIELD("ChoiceId Holder Unified field"),
		SOURCE_FIELD_DATA_FORMAT("Source Field Data Format"),
		SOURCE_FIELD_FORMAT_BINARY ("Source Field Format Binary"),
		FIELD_IDENTIFIER("Field Identifier"),
		FIELD_EXTRACTION_METHOD("Field Extraction Method"),
		FILD_SECTION_ID("Field Section Id"),
		CONTAINS_FIELD_ATTRIBUTE("Contains Field Attribute"),
		LOCATION("Location"),
		COLUMN_START_LOCATION("Column Start Location"),
		COLUMN_IDENTIFIER("Column Identifier"),
		REFERENCE_ROW("Reference Row"),
		COLUMN_ENDS_WITH("Column Ends With"),
		MANDATORY("Mandatory"),
		PAGE_NUMBER("Page Number"),
		COLUMN_STARTS_WITH("Column Starts With"),
		TD_NO("Td No"),
		REFERENCE_COL("Reference Col"),
		TABLE_FOOTER("Table Footer"),
		ROW_TEXT_ALIGHMENT("Row Text Alignment"),
		MULTILINE_ATTRIBUTE("Multiline Attribute"),
		MULTIPLE_VALUES("Multiple Values"),
		STARTS_WITH("Starts With"),
		EXCEL_ROW("Excel Row"),
		EXCEL_COL("Excel Col"),
		RELATIVE_EXCEL_ROW("Relative Excel Row"),
		COLUMN_CONTAINS("Column Contains"),
		TABLE_ROW_ATTRIBUTE("Table Row Attribute"),
		VALUE_SEPARATOR("Value Separator"),
		VALUE_INDEX("Value Index"),
		PARSE_AS_JSON("ParseAsJson"),
		GROUP_NAME("Group Name"),
		SUB_GROUP_NAME("Sub Group Name"),
		MULTI_RECORD("Multi Record"),
	    	DESTINATION_DATE_FORMAT ("Destination Date Format");
		
	private String name;

	private EnumParserAttributeHeader(String name) {
		this.name = name;
	}

	public String getEnumParserAttributeHeader() {
		return this.name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
