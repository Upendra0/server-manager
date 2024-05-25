/**
 * 
 */
package com.elitecore.sm.common.constants;

/**
 * ViewNameConstants defines all controller and view names that will be used in *Controller classes.
 * 
 * @author Sunil Gulabani
 * Mar 17, 2015
 */
public interface ViewNameConstants {
	
	public static final String LOGIN_PAGE = "login";
	public static final String SSO_REDIRECT = "redirect:/sso";
	public static final String HOME_PAGE = "iam/index";
	public static final String RSA_OTP_PAGE = "iam/rsaOtpPage";
	public static final String STAFF_MANAGER = "iam/staffManager";
	public static final String VIEW_STAFF_DETAILS = "iam/viewStaffDetails";
	public static final String ADD_ACCESS_GROUP = "iam/addAccessGroup";
	
	public static final String ADD_STAFF = "iam/addStaff";
	public static final String ADD_STAFF_AND_ASSIGN_ACCESS_GROUP = "iam/addStaffAndAssignAccessGroup";
	public static final String CHANGE_STAFF_PROFILE_PIC = "iam/changeStaffProfilePic";
	
	public static final String CHANGE_PASSWORD = "iam/change-password";//NOSONAR
	public static final String RESET_PASSWORD = "iam/resetPassword";//NOSONAR
	public static final String FORGOT_PASSWORD = "iam/forgotPassword";//NOSONAR
	
	public static final String GENERIC_ERROR_PAGE = "error/error";
	public static final String ACCESS_DENIED = "error/403";
	public static final String PAGE_NOT_FOUND = "error/404";
	public static final String REQUEST_METHOD_NOT_SUPPORTED = "error/405";
	public static final String INTERNAL_SERVER_ERROR = "error/500";
	
	// added for system parameter module
	public static final String MODIFY_SYSTEM_PARAM = "systemParam/systemParameterManager";

	public static final String AJAX_SESSION_TIMEOUT = "error/ajax-timeout";
	public static final String AJAX_ACCESS_DENIED = "error/ajax-access-denied";
	public static final String AJAX_REQUEST_METHOD_NOT_SUPPORTED = "error/ajax-request-method-not-supported";
	public static final String AJAX_PAGE_NOT_FOUND = "error/ajax-page-not-found";
	public static final String AJAX_ERROR_PAGE = "error/ajax-error";
	public static final String SERVER_MANAGER = "server/serverManager";
	public static final String CREATE_SERVER = "server/addServerDetailsPopUp";
	public static final String UPDATE_SERVER = "server/updtServerDetailsPopUp";
	public static final String CREATE_SERVER_INSTANCE = "server/addServerInstancePopUp";
	public static final String UPDATE_SERVER_INSTANCE = "server/updateServerInstance";
	public static final String UPDATE_SNMP_CONFIG = "snmp/snmpConfigMgmt";
	public static final String UPDATE_SYSTEM_AGENT_CONFIG = "systemAgent/systemAgentMgmt";
	
	public static final String MY_PROFILE = "iam/myProfile";
	public static final String SERVICE_MANAGER = "service/serviceManager";
	public static final String SELECT_SERVER_INSTANCE="service/selectServerInstancePopUp";
	
	public static final String PARSER_CONFIG_MANAGER = "service/parser/parserConfigManager";
	public static final String CREATE_SERVICE="service/addServicePopUp";
	public static final String COLLECTION_SERVICE_MANAGER="service/collection/collectionServiceManager";

	public static final String ADD_PARSER_ATTRIBUTE="service/parser/addParserAttributePopup";

	public static final String PARSING_SERVICE_MANAGER="service/parsing/parsingServiceManager";
	
	public static final String FTP_CONFIG_MANAGER="service/collection/ftp/ftpDriverManager";
	
	public static final String ABOUT_US="aboutUs";
	public static final String LICENSE_AGREEMENT = "license/licenseAgreement";
	public static final String LICENSE_ALERTS = "license/licenseAlertMessage";
	public static final String LICENSE_ACTIVATION = "license/licenseActivation";

	public static final String LICENSE_MANAGER ="license/licenseMgmt";
	public static final String UPGRADE_ENGINE_LICENCE ="license/upgradeLicense";
	
	public static final String TRIAL_LICENSE_ACTIVATION ="license/trialLicenseActivation";
	
	public static final String DISTRIBUTION_SERVICE_MANAGER="service/distribution/distributionServiceMgmt";
	
	public static final String PROCESSING_SERVICE_MANAGER="service/processing/processingServiceMgmt";
	
	public static final String AGGREGATION_SERVICE_MANAGER="service/aggregation/aggregationServiceMgmt";
	
	public static final String INIT_CONFIGURATION_MANAGER="dbconfig/configManager";
	
	public static final String INIT_MIS_REPORT_MANAGER="misreports/reportMgmt";
	
	public static final String DISTRIBUTION_DRIVER_CONFIG_MANAGER="service/distribution/driver/distributionDriverConfigManager";

	public static final String PRODUCT_CONFIGURATION_MANAGER="productconfiguration/productConfigurationManager";
	
	public static final String CONSOLIDATION_CONFIG_MANAGER="service/dataconsolidation/consolidationMgmt";
	
	public static final String INIT_DITRIBUTION_PLUGIN_MANAGER="service/distribution/plugin/pluginManager";
	
	public static final String BUSINESS_POLICY_MANAGEMENT="policy/businessPolicyMgmt";
	public static final String POLICY_RULE_MANAGER="policy/ruleManager";
	public static final String POLICY_RULE_LIST_ACTION_MANAGER="policy/policyActionList";
	public static final String POLICY_RULE_ACTION_MANAGER="policy/ruleActionManager";
	public static final String POLICY_RULE_CONDITION_MANAGER="policy/ruleConditionManager";

	public static final String CREATE_RULE_GROUP="policy/ruleGroupManager";
	public static final String CREATE_POLICY="policy/policyManager";
	
	public static final String INIT_DASHBOARD_MANAGER="dashboard/dashboardManager";
	public static final String WORKFLOW_MANAGER="workflow/workFlow";
	
	public static final String NETFLOW_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/netflowcollection/netflowCollServiceManager";
	public static final String NETFLOW_BINARY_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/netflowbinarycollection/netflowBinaryCollServiceManager";
	public static final String SYSLOG_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/syslogcollection/syslogCollServiceManager";
	public static final String MQTT_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/mqttcollection/mqttCollServiceManager";
	public static final String COAP_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/coapCollectionService/coapCollServiceManager";
	public static final String HTTP2_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/http2collectionservice/http2CollServiceManager";
	public static final String GTP_PRIME_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/gtpprimecollection/gtpPrimeCollServiceManager";
	
	public static final  String AUDIT_DETAIL_VIEW = "iam/auditDetails";
	
	public static final  String IPLOG_PARSING_SERVICE_MANAGER="service/parsing/iplogparsingservice/iplogParsingServiceManager";
	public static final  String NATFLOW_PARSER_CONFIGURATION = "service/parser/natflowParser/natflowParserConfigManager";
	public static final  String REGEX_PARSER_CONFIGURATION = "service/parser/regExParser/regExParserConfigManager";
	
	public static final String DEVICE_MANAGER = "device/deviceConfigurationManager";
	
	public static final String ASCII_PARSER_CONFIGURATION="service/parser/asciiParser/asciiParserConfigManager";
	public static final String ASCII_COMPOSER_MANAGER="service/composer/asciiComposer/asciiComposerConfigManager";
	public static final String PROFILE_CONFIGURATION_MANAGER="profileconfiguration/profileConfigurationManager";

	// saumil.vachheta
	public static final String DETAIL_LOCAL_PARSER_CONFIGURATION="service/parser/detailLocalParser/detailLocalParserConfigManager";
	public static final String DETAIL_LOCAL_COMPOSER_MANAGER="service/composer/detailLocalComposer/detailLocalComposerConfigManager";
	// saumil.vachheta
	
	
	public static final String ASN1_COMPOSER_MANAGER="service/composer/asn1Composer/asn1ComposerConfigManager";
	public static final String ASN1_PARSER_CONFIGURATION="service/parser/asn1Parser/asn1ParserConfigManager";

	//public static final String RAP_IN_PARSER_CONFIGURATION="service/parser/asn1Parser/asn1ParserConfigManager";

	public static final String HTML_PARSER_CONFIGURATION="service/parser/htmlParser/htmlParserConfigManager";
	public static final String HTML_PARSER_GROUP_ATTRIBUTE="service/parser/htmlParser/htmlParserGroupAttribute";
	public static final String XLS_PARSER_CONFIGURATION="service/parser/xlsParser/xlsParserConfigManager";
	public static final String VAR_LENGTH_ASCII_PARSER_CONFIGURATION="service/parser/varLengthAsciiParser/varLengthAsciiParserConfigManager";
	
	public static final String VAR_LENGTH_BINARY_PARSER_CONFIGURATION="service/parser/varLengthBinaryParser/varLengthBinaryParserConfigManager";
	public static final String JSON_PARSER_CONFIGURATION="service/parser/jsonParser/jsonParserConfigManager";
	
	public static final String MTSIEMENS_PARSER_CONFIGURATION="service/parser/mtsiemensParser/mtsiemensParserConfigManager";
	
	public static final String ROAMING_COMPOSER_MANAGER="service/composer/roamingComposers/roamingComposerConfigManager";

	public static final String MIGRATION_MANAGER="migration/migrationManager";
	
	public static final String FIXED_LENGTH_ASCII_PARSER_CONFIGURATION = "service/parser/fixedLengthASCIIParser/fixedLengthASCIIParserConfigManager";
	public static final String XML_COMPOSER_MANAGER="service/composer/xmlComposer/xmlComposerConfigManager";
	public static final String XML_PARSER_CONFIGURATION="service/parser/xmlParser/xmlParserConfigManager";

	public static final String INIT_DATABASE_QUERY_CONFIG="policy/databaseQueriesList";
	
	public static final String ININT_ERROR_REPROCESSING = "errorreprocessing/errorReprocessingManager";
	public static final String REPROCESSING_STATUS = "errorreprocessing/reprocessingStatus";
	public static final String REPROCESSING_FILE = "errorreprocessing/reprocessingFile";
	public static final String FIXED_LENGTH_ASCII_COMPOSER_CONFIGURATION = "service/composer/fixedLengthAsciiComposer/fixedLengthAsciiComposerConfigManager";
	public static final String UPDATE_RULE_DATA_CONFIG = "ruleDataDictionary/ruleDataDictionaryMain";
	public static final String DICTIOANRY_CONFIG = "ruleDataDictionary/DictionaryMain";
	public static final String ROAMING_CONFIGURATION ="roaming/roamingConfiguration";
	public static final String HOST_CONFIGURATION="roaming/hostConfiguration";
	public static final String ROAMING_PARSER_CONFIGURATION="service/parser/roamingParser/roamingParserConfigManager";
	
	public static final String FIXED_LENGTH_BINARY_PARSER_CONFIGURATION = "service/parser/fixedLengthBinaryParser/fixedLengthBinaryParserConfigManager";
	
	public static final String PDF_PARSER_CONFIGURATION = "service/parser/pdfParser/pdfParserConfigManager";
	
	public static final String DIAMETER_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/diametercollection/diameterCollServiceManager";

	public static final String AUTO_RELOAD_CACHE_CONFIG = "ruleDataDictionary/autoReloadCacheConfig";
	
	public static final String UPDATE_TRIGGER_CONFIG="trigger/triggerManager";
	
	public static final String RADIUS_COLLECTION_SERVICE_MANAGER="service/onlinecollectionServices/radiusCollectionService/radiusCollectionServiceManager";
	
	public static final String CIRCLE_CONFIG_MANAGER = "license/circleMain";
}