package com.elitecore.sm.parser.model;

public enum AsciiFileHeaderFooterSummaryFunctionEnum {
	
	FILE_CREATION_DATETIME("FILE_CREATION_DATETIME","{FILE_CREATION_DATETIME,YYYYMMDDHHMMSS}"),
    FILE_AVAILABLE_TIMESTAMP("FILE_AVAILABLE_TIMESTAMP","{FILE_AVAILABLE_TIMESTAMP,YYYYMMDDHHMMSS}"),
    ORIGINAL_FILE_NAME("ORIGINAL_FILE_NAME","{ORIGINAL_FILE_NAME}"),
    TOTAL_SUCCESS_RECORDS("TOTAL_SUCCESS_RECORDS","{TOTAL_SUCCESS_RECORDS}"),
    TOTAL_ERROR_RECORDS("TOTAL_ERROR_RECORDS","{TOTAL_ERROR_RECORDS}"),
    TOTAL_DISCARDED_RECORDS("TOTAL_DISCARDED_RECORDS","{TOTAL_DISCARDED_RECORDS}"),
    TOTAL_RECORDS("TOTAL_RECORDS","{TOTAL_RECORDS}"),
    BLANK_SPACES("BLANK_SPACES","{BLANK_SPACES,LENGTH}"),
    MIN("MIN","{MIN(GENERAL_FIELD_NAME)}"),
    MINDATE("MINDATE","{MINDATE(GENERAL_FIELD_NAME),YYYYMMDDHHMMSS}"),
    MAX("MAX","{MAX(GENERAL_FIELD_NAME)}"),
    MAXDATE("MAXDATE","{MAXDATE(GENERAL_FIELD_NAME),YYYYMMDDHHMMSS}"),
    GETVALUEOF("GETVALUEOF","{GETVALUEOF(GENERAL_FIELD_NAME)}"),
    FIRSTVALUEOF("FIRSTVALUEOF","{FIRSTVALUEOF(GENERAL_FIELD_NAME)}"),
    LASTVALUEOF("LASTVALUEOF","{LASTVALUEOF(GENERAL_FIELD_NAME)}");
	private AsciiFileHeaderFooterSummaryFunctionEnum() {

	}

	private String name;
	private String value;

	private AsciiFileHeaderFooterSummaryFunctionEnum(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
