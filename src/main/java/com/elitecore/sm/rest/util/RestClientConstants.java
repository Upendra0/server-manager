package com.elitecore.sm.rest.util;

public final class RestClientConstants {
	
	public static final String REST_API_FAILURE = "REST_API_FAILURE";
	
	public static final String REST_CONNECTION_FAILURE = "REST_CONNECTION_FAILURE";

	/**
	 * A string variable indicating rest web service base context path.
	 */
	public static final String DEFAULT_REST_SERVICE_CONTEXT_PATH = "/restMediation";
	
	/**
	 * A string variable indicating rest web service connection timeout.
	 */
	public static final int DEFAULT_REST_SERVICE_CON_TIMEOUT = 10000;
	
	/**
	 * A string variable indicating rest web service search service path.
	 */
	public static final String DEFAULT_REST_SERVICE_SEARCH_SERVICE_PATH = "/search/getSearchResults";
	
	/**
	 * A string variable indicating rest web service property alias.
	 */
	public static final String REST_SERVICE_ENABLED_PROPERTY_ALIAS = "general.rest-service-enabled";
	
	/**
	 * A string variable indicating web service configuration.
	 */
	public  static final String KEY = "REST_WEB_SERVICE_CONFIGURATION";

	/**
	 * A string variable indicating web service URL.
	 */
	public static final String WEB_SERVICE_URL = "WEB_SERVICE_URL";
	
	/**
	 * A string variable indicating web service DEFAUTL PORT OFFSET.
	 */
	public static final int DEFAULT_REST_SERVICE_PORT_OFFSET = 1000;
	
	/**
	 * A string variable indicating web service DEFAUTL REST SERVICE PROTOCOL.
	 */
	public static final String DEFAULT_REST_SERVICE_PROTOCOL = "http://";
	
	/**
	 * A string variable indicating map for web service configuration.
	 */
	public static final String WEB_SERVICE_CONFIGURATION_MAP = "WEB_SERVICE_CONFIGURATION_MAP";
	
	/**
	 * A string variable indicating Server Context.
	 */
	public static final String SERVER_CONTEXT = "SERVER_CONTEXT";
	
	/**
	 * A string variable indicating Success Response Code.
	 */
	public static final int SUCCESS_RESPONSE_CODE = 201;
	
	/**
	 * A string variable indicating Failure Response Code.
	 */
	public static final int FAILURE_RESPONSE_CODE = 500;
	
	/**
	 * A string variable indicating Success Response Message.
	 */
	public static final String SUCCESS_RESPONSE_MESSAGE = "Request Submitted Successfully";
	
	/**
	 * A string variable indicating CONTECT TYPE.
	 */
	public static final String REST_SERVICE_CONTENT_TYPE = "Content-Type";
	
	/**
	 * A string variable indicating CONTECT TYPE VALUE.
	 */
	public static final String REST_SERVICE_CONTENT_TYPE_VALUE = "text/plain; charset=UTF-8";
	
	/**
	 * A string variable indicating REQUEST METHOD.
	 */
	public static final String REST_SERVICE_REQUEST_METHOD = "POST";
	
	/**
	 * A string variable indicating REQUEST METHOD.
	 */
	public static final String DEFAULT_CHAR_SET = "UTF-8";
}
