/**
 * 
 */
package com.elitecore.sm.common.constants;

import com.elitecore.passwordutil.PasswordEncryption;

/**
 * BaseConstants defines all set of constants
 * 
 * @author Sunil Gulabani Mar 17, 2015
 */
public final class BaseConstants {

	public static final String RESPONSE_JSON_CONTENT_TYPE = "application/json";
	public static final String RESPONSE_MSG = "RESPONSE_MSG";
	public static final String ERROR_MSG = "ERROR_MSG";
	public static final String USER_SELECTED_LANGUAGE = "lang";
	public static final String ADMIN_USERNAME = "admin";
	public static final String DEFAULT_ENCRYPTION_TYPE = "MD5";
	public static final String IS_LOGIN_FIRST_TIME = "IS_LOGIN_FIRST_TIME";

	public static final String VERIFIED_USERNAME_FOR_FORGOT_PASSWORD = "VERIFIED_USERNAME_FOR_FORGOT_PASSWORD";//NOSONAR
	public static final String VERIFIED_DETAIL_FOR_FORGOT_PASSWORD = "VERIFIED_DETAIL_FOR_FORGOT_PASSWORD";//NOSONAR
	public static final String VERIFIED_USER_QUESTION_LIST = "VERIFIED_USER_QUESTION_LIST";

	public static final String STAFF_DETAILS = "STAFF_DETAILS";
	
	public static final String CONFIG_HISTORY_SUMMARY = "CONFIG_HISTORY_SUMMARY";

	public static final String REQUEST_ACTION_TYPE = "REQUEST_ACTION_TYPE";
	public static final String ADD_ACCESS_GROUP = "ADD_ACCESS_GROUP";
	public static final String VIEW_ACCESS_GROUP = "VIEW_ACCESS_GROUP";
	public static final String UPDATE_ACCESS_GROUP = "UPDATE_ACCESS_GROUP";
	public static final String ADD_STAFF = "ADD_STAFF";
	public static final String ADD_STAFF_AND_ASSIGN_ACCESS_GROUP = "ADD_STAFF_AND_ASSIGN_ACCESS_GROUP";
	public static final String CHANGE_STAFF_STATE = "CHANGE_STAFF_STATE";
	public static final String LOCK_UNLOCK_STAFF_STATE = "LOCK_UNLOCK_STAFF_STATE";
	public static final String DELETE_STAFF_STATE = "DELETE_STAFF_STATE";

	public static final String DELETED_MODEL_SUFFIX = "_DEL";

	public static final String LOGIN_IP_UNAUTHORIZED = "LOGIN_IP_UNAUTHORIZED";
	public static final String ACCOUNT_LOCKED_BY_ADMIN = "ACCOUNT_LOCKED_BY_ADMIN";

	public static final String UPDATE_STAFF_BASIC_DETAIL = "UPDATE_STAFF_BASIC_DETAIL";
	public static final String UPDATE_STAFF_ACCESS_GROUP = "UPDATE_STAFF_ACCESS_GROUP";
	public static final String UPDATE_STAFF_CHANGE_PASSWORD = "UPDATE_STAFF_CHANGE_PASSWORD";//NOSONAR

	public static final String PORT_IS_FREE = "PORT_IS_FREE";
	public static final String SERVER_SCRIPT_ALREADY_CREATED = "SERVER_SCRIPT_ALREADY_CREATED";
	public static final String ANOTHER_SERVICE_RUNNING_ON_SAME_PORT = "ANOTHER_SERVICE_RUNNING_ON_SAME_PORT";
	public static final String SERVER_ALREADY_RUNNING = "SERVER_ALREADY_RUNNING";
	public static final String SERVER_SCRIPT_CREATED = "SERVER_SCRIPT_CREATED";
	public static final String SERVER_STARTED = "SERVER_STARTED";

	public static final String JMX_CONNECTION_FAILURE = "JMX_CONNECTION_FAILURE";
	public static final String JMX_API_FAILURE = "JMX_API_FAILURE";
	public static final String KEYSTORE_FILE_SYNC_FAILURE = "KEYSTORE_FILE_SYNC_FAILURE";

	public static final String SERVER_HOME_NOT_SET = "SERVER_HOME_NOT_SET";
	public static final String UNABLE_TO_START_SERVER_SCRIPT = "UNABLE_TO_START_SERVER_SCRIPT";
	public static final String CRESTEL_P_ENGINE_HOME = "CRESTEL_P_ENGINE_HOME";
	public static final String JAVA_HOME = "JAVA_HOME";

	public static final String XSLT_CONTENT_TYPE = "application/xslt+xml";

	public static final String SYNC_SERVERINSTANCE_ENGINE_SUCCESS = "Server Configuration Successfully Saved";
	public static final String SYNC_SERVICE_INSTANCE_ENGINE_SUCCESS = "success";

	public static final String SERVER_MANAGEMENT = "SERVER_MANAGEMENT";
	public static final String CREATE_SERVER = "CREATE_SERVER";
	public static final String SERVER_TYPE_LIST = "SERVER_TYPE_LIST";
	public static final String SERVER_LIST = "SERVER_LIST";
	public static final String DICT_FILE_PATH ="DICT_FILE_PATH";
	public static final String SERVER_INSTANCE_LIST = "SERVER_INSTANCE_LIST";
	public static final String SERVICE_LIST = "SERVICE_LIST";
	public static final String CONFIGURED_SERVER_INSTANCE_LIST = "CONFIGURED_SERVER_INSTANCE_LIST";

	public static final String STAFF_MANAGEMENT = "STAFF_MANAGEMENT";
	public static final String ACCESS_GROUP_MANAGEMENT = "ACCESS_GROUP_MANAGEMENT";
	public static final String STAFF_AUDIT_MANAGEMENT = "STAFF_AUDIT_MANAGEMENT";

	public static final String USERNAME_BLANK = "USERNAME_BLANK";
	public static final String PASSWORD_BLANK = "PASSWORD_BLANK";//NOSONAR
	public static final String USERNAME_PASSWORD_BLANK = "USERNAME_PASSWORD_BLANK";//NOSONAR

	public static final String COLLECTION_SERVICE = "Collection Service";
	public static final String PARSING_SERVICE = "Parsing Service";
	public static final String DISTRIBUTION_SERVICE = "Distribution Service";
	public static final String PROCESSING_SERVICE = "Processing Service";
	public static final String AGGREGATION_SERVICE = "Aggregation Service";
	public static final String IPLOG_PARSING_SERVICE = "IpLog Parsing Service";
	public static final String NATFLOW_COLLECTION_SERVICE = "Natflow Collection Service";
	public static final String NATFLOW_BINARY_COLLECTION_SERVICE = "Natflow Binary Collection Service";
	public static final String SYSLOG_COLLECTION_SERVICE = "Syslog Collection Service";
	public static final String MQTT_COLLECTION_SERVICE = "Mqtt Collection Service";
	public static final String COAP_COLLECTION_SERVICE = "CoAP Collection Service";
	public static final String HTTP2_COLLECTION_SERVICE = "Http2 Collection Service";
	public static final String GTPPRIME_COLLECTION_SERVICE = "GTPPrime Collection Service";
	public static final String DIAMETER_COLLECTION_SERVICE = "Diameter Collection Service";
	public static final String COLLECTION_DRIVER = "Collection Driver";
	public static final String FTP_COLLECTION_DRIVER = "FTP Collection Driver";
	public static final String FTP_DISTRIBUTION_DRIVER = "FTP Distribution Driver";
	public static final String DATA_CONSOLIDATION_SERVICE = "Data Consolidation Service";
	public static final String SERVICE_JAXB_OBJECT = "SERVICE_JAXB_OBJECT";
	public static final String SERVICE_JAXB_FILE = "SERVICE_JAXB_FILE";

	public static final int SERVER_MGMT_JMX_PORT = 1617;
	public static final int SERVER_MGMT_TRY_TO_CONNECT = 1;
	public static final int SERVER_MGMT_CONNECTION_TIMEOUT = 15;
	public static final int SERVER_CONNECTION_INTERVAL = 5000;
	public static final String SERVER_VERSION_INSTALLED = "6.2.3";
	public static final String SERVER_MGMT_JMX_OBJECT_NAME = "ServerMgmt:name=IServerManagementMBean";

	public static final String SERVER_INSTANCE_EXPORT_FILE = "SERVER_INSTANCE_EXPORT_FILE";
	public static final String SERVICE_INSTANCE_EXPORT_FILE = "SERVICE_INSTANCE_EXPORT_FILE";

	public static final String MEDIATION_SERVER_XML = "mediation_server";
	public static final String ORACLE_DATABASE_CONFIGURATION_XML = "oracle_database_conf";
	public static final String IMPORT_FILE_CONTENT_TYPE = "text/xml";
	public static final String LICENSE_KEY_FILE = "application/octet-stream";
	public static final String LICENSE_KEY_FILE_EXT = "key";
	public static final String UPDATE_INSTANCE_ADVANCE_CONFIG = "UPDATE_INSTANCE_ADVANCE_CONFIG";
	public static final String UPDATE_INSTANCE_SYSTEM_LOG = "UPDATE_INSTANCE_SYSTEM_LOG";
	public static final String UPDATE_INSTANCE_STATISTIC = "UPDATE_INSTANCE_STATISTIC";
	public static final String UPDATE_INSTANCE_SUMMARY = "UPDATE_INSTANCE_SUMMARY";
	public static final String UPDATE_SNMP_CONFIG = "UPDATE_SNMP_CONFIG";
	public static final String UPDATE_SYSTEM_AGENT_CONFIG = "UPDATE_SYSTEM_AGENT_CONFIG";

	public static final String TOMCAT_HOME = "TOMCAT_HOME";
	public static final String IS_VALID_EXPORT_BACKUP_PATH = "IS_VALID_EXPORT_BACKUP_PATH";
	public static final String EXPORT_SERVER_INSTANCE_BEFORE_DELETE = "EXPORT_SERVER_INSTANCE_BEFORE_DELETE";
	public static final String EXPORT_ZIP_NAME = "ServerInstance_Config_";

	public static final String DELETE_EXPORT_DATE_TIME_FORMATTER = "dd-MM-yyyy-hh-mm-ss";
	public static final String SYNC_PUBLISH_DATE_TIME_FORMATTER = "yyyyMMddhhmmss";

	public static final String SERVICE_MANAGEMENT = "SERVICE_MANAGEMENT";
	public static final String CREATE_SERVICE = "CREATE_SERVICE";

	public static final String UPDATE_PARSER_CONFIGURATION = "UPDATE_PARSER_CONFIGURATION";
	public static final String UPDATE_PARSER_ATTR_LIST = "UPDATE_PARSER_ATTR_LIST";

	public static final String PARSING_SERVICE_SUMMARY = "PARSING_SERVICE_SUMMARY";
	public static final String PARSING_SERVICE_CONFIGURATION = "PARSING_SERVICE_CONFIGURATION";
	public static final String PARSING_SERVICE_PLUGIN_CONFIGURATION = "PARSING_SERVICE_PLUGIN_CONFIGURATION";
	public static final String PARSING_SERVICE_PATHLIST_CONFIGURATION = "PARSING_SERVICE_PATHLIST_CONFIGURATION";

	public static final String COLLECTION_SERVICE_SUMMARY = "COLLECTION_SERVICE_SUMMARY";
	public static final String COLLECTION_SERVICE_CONFIGURATION = "COLLECTION_SERVICE_CONFIGURATION";
	public static final String COLLECTION_DRIVER_CONFIGURATION = "COLLECTION_DRIVER_CONFIGURATION";

	public static final String PARSER_TYPE = "Parser";
	public static final String COMPOSERTYPE = "Composer";

	public static final String FTP_CONFIGURATION = "FTP_CONFIGURATION";
	public static final String PATH_LIST_CONFIGURATION = "PATH_LIST_CONFIGURATION";
	public static final String APPLY_LICENSE = "APPLY_LICENSE";
	public static final String RENEW_LICENSE = "RENEW_LICENSE";
	public static final String TRIAL_LICENSE = "TRIAL_LICENSE";

	public static final String CREATE_TRIAL_LICENSE = "CREATE_TRIAL_LICENSE";

	public static final String DISTRIBUTION_SERVICE_SUMMARY = "DISTRIBUTION_SERVICE_SUMMARY";
	public static final String DISTRIBUTION_SERVICE_CONFIGURATION = "DISTRIBUTION_SERVICE_CONFIGURATION";
	public static final String DISTRIBUTION_DRIVER_CONFIGURATION = "DISTRIBUTION_DRIVER_CONFIGURATION";
	public static final String DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION = "DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION";
	
	public static final String PROCESSING_SERVICE_SUMMARY = "PROCESSING_SERVICE_SUMMARY";
	public static final String PROCESSING_SERVICE_CONFIGURATION = "PROCESSING_SERVICE_CONFIGURATION";
	public static final String PROCESSING_PATHLIST_CONFIGURATION = "PROCESSING_PATHLIST_CONFIGURATION";

	public static final String AGGREGATION_SERVICE_SUMMARY = "AGGREGATION_SERVICE_SUMMARY";
	public static final String AGGREGATION_SERVICE_CONFIGURATION = "AGGREGATION_SERVICE_CONFIGURATION";
	public static final String AGGREGATION_DEFINITION_CONFIGURATION = "AGGREGATION_DEFINITION_CONFIGURATION";
	public static final String AGGREGATION_PATHLIST_CONFIGURATION = "AGGREGATION_PATHLIST_CONFIGURATION";
	
	public static final String DATABASE_CONFIGURATION = "DATABASE_CONFIGURATION";
	public static final String KAFKA_CONFIGURATION = "KAFKA_CONFIGURATION";
	public static final String SERVICE_WISE_SUMMARY = "SERVICE_WISE_SUMMARY";
	public static final String SERVICE_WISE_DETAIL = "SERVICE_WISE_DETAIL";
	public static final String DYNAMIC_REPORT = "DYNAMIC_REPORT";
	
	public static final String FILE_STORAGE_CONFIGURATION = "FILE_STORAGE_CONFIGURATION";

	public static final String DISTRIBUTION_FTP_DRIVER_CONFIGURATION = "DISTRIBUTION_FTP_DRIVER_CONFIGURATION";
	public static final String DISTRIBUTION_FTP_DRIVER_PLUGIN = "DISTRIBUTION_FTP_DRIVER_PLUGIN";

	public static final String PRODUCT_CONFIGURATION = "PRODUCT_CONFIGURATION";
	public static final String PRODUCT_SERVICE_CONFIGURATION = "PRODUCT_SERVICE_CONFIGURATION";

	public static final String CONSOLIDATION_DEFINITION = "CONSOLIDATION_DEFINITION";
	public static final String CONSOLIDATION_SERVICE_MAPPING = "CONSOLIDATION_SERVICE_MAPPING";

	public static final String DISTRIBUTION_PLUGIN_CONFIGURATION = "DISTRIBUTION_PLUGIN_CONFIGURATION";
	public static final String DISTRIBUTION_PLUGIN_ATTRIBUTES = "DISTRIBUTION_PLUGIN_ATTRIBUTES";
	public static final String CREATE_NEW_RULE = "CREATE_NEW_RULE";

	public static final String DASHBOARD = "DASHBOARD";
	public static final String WORKFLOW = "WORKFLOW";
	public static final String WORKFLOW_MANAGEMENT = "WORKFLOW_MANAGEMENT";

	public static final String AJAX_RESPONSE_SUCCESS = "200";
	public static final String AJAX_RESPONSE_FAIL = "400";
	public static final String AJAX_RESPONSE_FAIL_EMPTY_PKT_STATISTICS_PATH = "700";
	public static final String AJAX_RESPONSE_FAIL_DATASOURCE_DISABLED = "701";
	public static final String AJAX_RESPONSE_FAIL_INVALID_LICENSE = "702";

	public static final String JAXB_XML_PATH = "/WEB-INF/resources/xml";
	public static final String XSLT_PATH = "/WEB-INF/resources/xslt";
	public static final String ENGINE_SAMPLE_XML_PATH = "/WEB-INF/resources/sample-xmls";
	public static final String TEMP_PATH_FOR_EXPORT = "/WEB-INF/resources/temp";
	public static final String BASE_PATH_FOR_EXPORT = "/WEB-INF/resources";
	public static final String LICENSE_PATH = "license";
	public static final String SM_SYSTEM_PATH = "system";
	public static final String LICENSE_WARNING = "LICENSE_WARNING";
	public static final String SM_LICENSE_FILE = "LicenseRegistrationFormSM.xls";
	public static final String SM_LICENSE_TEMPLATE = "LicenseRegistrationSM.xls";
	public static final String CONTENT_TYPE_FOR_EXPORT_CONFIG = "application/octet-stream";
	
	public static final String LICENSE_CONTENT_TYPE = "application/ms-excel";
	public static final String SERVER_INSTANCES_ID = "serverInstanceId";
	public static final String SERVER_INSTANCES_STATUS = "serverInstancesStatus";

	public static final String SERVICE_INSTANCES_ID = "serviceInstanceId";
	public static final String SERVICE_INSTANCES_STATUS = "serviceInstancesStatus";

	public static final String JAXB_XML_PATH_CONSTANT = "JAXB_XML_PATH";
	public static final String XSLT_PATH_CONSTANT = "XSLT_PATH";
	public static final String ENGINE_SAMPLE_XML_PATH_CONSTANT = "ENGINE_SAMPLE_XML_PATH";
	public static final String SUFFIX_FOR_DELETE = "_DEL_";
	public static final String ACTION_DELETE = "DELETE";

	public static final String ERROR_MSG_KEY_SUFFIX = ".invalid";
	public static final String REGEX_KEY_SUFFIX = "_REGEX";

	public static final int IMPORT_MODE_ADD = 1;
	public static final int IMPORT_MODE_OVERWRITE = 2;
	public static final int IMPORT_MODE_UPDATE = 3;
	public static final int IMPORT_MODE_KEEP_BOTH = 4;

	public static final String RUNNING_SERVICE_STATUS_ENGINE = "Running";
	public static final String SERVICE_TYPE_LIST = "SERVICE_TYPE_LIST";
	public static final String SERVERINSTANCE_LIST = "SERVERINSTANCE_LIST";
	public static final String MAIN_SERVICE_TYPE_LIST = "MAIN_SERVICE_TYPE_LIST";
	public static final String ADDITIONAL_SERVICE_TYPE_LIST = "ADDITIONAL_SERVICE_TYPE_LIST";
	public static final String SUFFIX_FOR_IMPORT = "_IMPORT_";
	public static final String ACTION_IMPORT = "IMPORT";
	public static final String COLLECTION_DRIVER_TYPE_LIST = "COLLECTION_DRIVER_TYPE_LIST";
	public static final String DISTRIBUTION_DRIVER_TYPE_LIST = "DISTRIBUTION_DRIVER_TYPE_LIST";

	public static final String SERVICE_ID = "serviceId";
	public static final String ID = "id";
	public static final String SERV_INSTANCE_ID = "servInstanceId";
	public static final String SERVICE_INST_ID = "serviceInstanceId";
	public static final String SERVICE_NAME = "serviceName";
	public static final String SERVICE_TYPE = "serviceType";
	public static final String SERVICE_TYPE_NAME = "serviceTypeName";
	public static final String SERVICE_INSTANCE_NAME = "serverInstanceName";
	public static final String SERVICE_INSTANCE_ID = "serverInstanceId";
	public static final String SERVICE_ENABLE_STATUS = "enableStatus";
	public static final String SERVICE_STATUS = "serviceStatus";
	public static final String SERVICE_SYNC_STATUS = "sync_status";
	public static final String STOP_SERVICE = "stop";
	public static final String START_SERVICE = "start";
	public static final String LAST_UPDATE_TIME = "lastUpdateTime";
	public static final String SERVER_IP_PORT = "serverIpPort";	

	public static final String INSTANCE_ID = "instanceId";
	public static final String INSTANCE_NAME = "instanceName";
	public static final String BOOTSTRAP_JS_FLAG = "isBootstrapDisable";

	public static final String DRIVER_ID = "driverId";
	public static final String DRIVER_NAME = "driverName";
	public static final String DRIVER_TYPE = "driveType";
	public static final String DRIVER_TYPE_NAME = "driveTypeName";

	public static final String HOST = "host";
	public static final String PORT = "port";
	public static final String IPADDRESS = "ipAddress";
	public static final String APPLICATION_ORDER = "applicationOrder";
	public static final String ENABLE = "enable";

	public static final String STATE_ENUM = "stateEnum";
	public static final String FILE_MERGE_GROUPINGBY_ENUM = "fileMergeGroupingByEnum";

	public static final String BACKUP_DIRECTORY = "backup";
	public static final String CREATE_MODE_NEW = "CREATE_MODE_NEW";
	public static final String CREATE_MODE_IMPORT = "CREATE_MODE_IMPORT";
	public static final String CREATE_MODE_COPY = "CREATE_MODE_COPY";

	
	public static final String  POLICY_RULE_ACTION_NAME =  "ruleActionName";
	public static final String  POLICY_ACTION_DESCRIPTION = "description";
	public static final String  POLICY_ACTION_TYPE = "actionType";
	public static final String  POLICY_ACTION_ACTION = "action";
	public static final String  POLICY_RULE_ACTION = "POLICY_RULE_ACTION";
	public static final String  POLICY_RULE_CONDITION = "POLICY_RULE_CONDITION";
	public static final String  PROCESSING_POLICY_RULE_CATEGORY = "policyRuleCategory";
	public static final String  PROCESSING_POLICY_RULE_SEVERITY = "policyRuleSeverity";
	public static final String XML_FILE_EXT = ".xml";
	public static final String SERVICE_INSTANCE_JMX_RESPONSE = "success";
	public static final String SCRIPT_FILE_SH_EXT = ".sh";
	public static final String SCRIPT_FILE_BAT_EXT = ".bat";
	public static final int MIN_MOMORY_JVM_ALLOCATION = 8;
	public static final String NETFLOW_COLLECTION_SERVICE_SUMMARY = "NETFLOW_COLLECTION_SERVICE_SUMMARY";
	public static final String NETFLOW_COLLECTION_SERVICE_CONFIGURATION = "NETFLOW_COLLECTION_SERVICE_CONFIGURATION";
	public static final String NETFLOW_CLIENT_CONFIGURATION = "NETFLOW_CLIENT_CONFIGURATION";
	public static final int INITIAL_NETFLOW_COLLECTION_MIN_THREAD = 50;
	public static final int INITIAL_NETFLOW_COLLECTION_MAX_THREAD = 100;
	public static final int INITIAL_NETFLOW_COLLECTION_QUEUE_SIZE = 1500000;
	public static final int INITIAL_NETFLOW_GTP_COLLECTION_QUEUE_SIZE = 1000000;
	public static final String TYPEMISMATCH_ERROR = "typeMismatch";
	public static final String LANGUAGE_PROP_LIST = "languagePropsList";
	public static final String CURRENT_LANGUAGE_LOCALE = "currentLocale";
	public static final String CLASS_PATH = "/WEB-INF/classes";
	public static final String MODULE_COMMON_FOLDER = "common";
	public static final String MODULE_SERVER_FOLDER = "server";
	public static final String MODULE_SERVICE_FOLDER = "service";
	public static final String IPLOG_SERVICE_SUMMARY = "IPLOG_SERVICE_SUMMARY";
	public static final String IPLOG_SERVICE_CONFIGURATION = "IPLOG_SERVICE_CONFIGURATION";
	public static final String IPLOG_SERVICE_HASH_CONFIGURATION = "IPLOG_SERVICE_HASH_CONFIGURATION";
	public static final String IPLOG_SERVICE_PATHLIST_CONFIGURATION = "IPLOG_SERVICE_PATHLIST_CONFIGURATION";
	public static final int APP_LANGUAGE_FILE_COUNT = 9;
	public static final String APP_DEFAULT_LANG = "en";
	public static final String CREATE_ACTION = "CREATE_ACTION";
	public static final String UPDATE_ACTION = "UPDATE_ACTION";
	public static final String UPDATE_LIST_ACTION = "UPDATE_LIST_ACTION";
	public static final String UPDATE_CUSTOM_ACTION = "UPDATE_CUSTOM_ACTION";
	public static final String EXPORT_ACTION = "EXPORT_ACTION";
	public static final String IMPORT_ACTION = "IMPORT_ACTION";
	public static final String DELETE_ACTION = "DELETE_ACTION";
	public static final String DELETE_CUSTOM_ACTION = "DELETE_CUSTOM_ACTION";
	public static final String DELETE_MULTIPLE_ACTION = "DELETE_MULTIPLE_ACTION";
	public static final String SM_ACTION = "SM_ACTION";
	public static final String SM_UPDATE_ACTION_BULK_LIST = "SM_UPDATE_ACTION_BULK_LIST";
	public static final String SM_ACTION_BULK_MAP = "SM_ACTION_BULK_MAP";
	public static final String SERVERINSTANCE = "ServerInstance";
	public static final String THRESHOLD_MEMORY = "Threshold Memory";
	public static final String THRESHOLD_TIME_INTERVAL = "Threshold Time Interval";
	public static final String LOAD_AVERAGE = "Load Average";
	public static final String SERVICE = "Service";
	public static final String SERVICE_NAME_IMPORT = "Service Name";
	public static final String SERVICE_DESCRIPTION_IMPORT = "Service Description";
	public static final String DRIVER = "Driver";
	public static final String PARSER_ATTRIBUTE = "ParserAttribute";
	public static final String PATHLIST = "PathList";
	public static final String NETFLOW_CLIENT = "Netflow Client";
	public static final String KAFKADATASOURCECONFIG = "Kafka DataSource Config";
	public static final String NETFLOW_PARSER_CONFIGURATION = "NETFLOW_PARSER_CONFIGURATION";
	public static final String NETFLOW_PARSER_ATTRIBUTE = "NETFLOW_PARSER_ATTRIBUTE";
	public static final String IMPORT = "IMPORT";
	public static final String IMPORT_VALIDATION_XSD = "Import_validation_schema.xsd";

	public static final int USER_DEFINED_DEVICE = 1;
	public static final int SYSTEM_DEFINED_DEVICE = 0;
	public static final int USER_DEFINED_MAPPING = 1;
	public static final int SYSTEM_DEFINED_MAPPING = 0;
	public static final String DEVICE_VALIDATION = "DEVICE_VALIDATION";
	public static final String DEVICE_CONFIG_VALIDATION = "DEVICE_CONFIG_VALIDATION";
	public static final String PARSER_ATTRIBUTE_VALIDATION = "PARSER_ATTRIBUTE_VALIDATION";
	public static final String PARSER = "Parser";
	public static final String PARSERWRAPPER = "ParserWrapper";
	public static final String REGEX_PARSER_CONFIGURATION = "REGEX_PARSER_CONFIGURATION";
	public static final String REGEX_PARSER_ATTRIBUTE = "REGEX_PARSER_ATTRIBUTE";
	public static final String TOMCAT_REGEX_PARSER_SAMPLEFILE_DIR_NAME = "regExParser";
	public static final String TOMCAT_MIGRATION_DIR_NAME = "migration";
	public static final String TOMCAT_MIGRATION_ZIP_FILE_DIR_NAME = "zip";
	public static final String TOMCAT_MIGRATION_EXTRACT_FILE_DIR_NAME = "extract";
	public static final String TOMCAT_DELETE_BACKUP_DIR_NAME = "export";
	public static final String TOMCAT_CUSTOM_IMAGE_DIR_NAME = "image";
	public static final String TOMCAT_STAFF_IMAGE_DIR_NAME="staff";
	public static final String TOMCAT_SYSTEMPARAM_IMAGE_DIR_NAME="systemParameter";
	public static final String CREATE_DEVICE_TYPE = "CREAETE_DEVICE_TYPE";
	public static final String CREATE_VENDOR_TYPE = "CREATE_VENDOR_TYPE";
	public static final String BOTH = "BOTH";
	public static final String NONE = "NONE";
	public static final String VIEW_MODE = "view";
	public static final String CREATE_MODE = "create";
	public static final String UPDATE_MODE = "update";
	public static final String DELETE_MODE = "delete";
	public static final int STATUS_INSERT = 1;
	public static final int STATUS_UPDATE = 2;
	public static final int STATUS_DELETE = 3;
	public static final int STATUS_VIEW = 4;
	public static final String NO_MAPPING = "NO_MAPPING";
	public static final String NO_ACTION = "NO_ACTION";
	public static final String CREATE = "CREATE";
	public static final String CUSTOM_CREATE = "CUSTOM_CREATE";
	public static final String JSON = "JSON";
	public static final String ACTION_TYPE_ADD = "ADD";
	public static final String HQL_QUERY_COUNT = "COUNT";
	public static final String HQL_QUERY_LIST = "LIST";
	public static final String SNMP_CONFIG = "SNMP_CONFIG";
	public static final String DS_NAME_SUFFIX = "_DS";
	public static final String INIT_BINDER_METHOD_NAME = "initBinder";
	public static final String PLUGIN_ID = "plugInId";
	public static final String DEVICE_TYPE_LIST = "deviceTypeList";
	public static final String SERVER_INSTANCE_ID = BaseConstants.INSTANCE_ID;
	public static final String PLUGIN_NAME = "plugInName";
	public static final String PLUGIN_TYPE = "plugInType";
	public static final String PARSER_MAPPING_ID = "mappingId";
	public static final String DEVICE_NAME = "deviceName";
	public static final String DEVICE_ID = "deviceId";
	public static final String PARSER_MAPPING_NAME = "mappingName";
	public static final String SELECTED_DEVICE_ID = "selDeviceId";
	public static final String SELECTED_DEVICE_NAME = "selecteDeviceName";
	public static final String SELECTED_MAPPING_NAME = "selecteMappingName";

	public static final String SELECTED_MAPPING_ID = "selMappingId";
	public static final String SELECTED_VENDOR_TYPE_ID = "selVendorTypeId";
	public static final String SELECTED_DEVICE_TYPE_ID = "selDeviceTypeId";
	
	public static final String LOOKUP_TABLE_ID = "ruleLookUpTable.id"; 
	
	public static final String ASCII_PARSER_CONFIGURATION = "ASCII_PARSER_CONFIGURATION";
	public static final String ASCII_PARSER_ATTRIBUTE = "ASCII_PARSER_ATTRIBUTE";

	// saumil.vachheta start 
	public static final String DETAIL_LOCAL_PARSER_CONFIGURATION = "DETAIL_LOCAL_PARSER_CONFIGURATION";
	public static final String DETAIL_LOCAL_PARSER_ATTRIBUTE = "DETAIL_LOCAL_PARSER_ATTRIBUTE";
	// saumil.vachheta end 
	
	public static final String JSON_PARSER_CONFIGURATION = "JSON_PARSER_CONFIGURATION";
	public static final String JSON_PARSER_ATTRIBUTE = "JSON_PARSER_ATTRIBUTE";

	public static final String MTSIEMENS_PARSER_CONFIGURATION = "MTSIEMENS_PARSER_CONFIGURATION";
	public static final String MTSIEMENS_PARSER_ATTRIBUTE = "MTSIEMENS_PARSER_ATTRIBUTE";

	public static final String VAR_LENGTH_BINARY_PARSER_CONFIGURATION = "VAR_LENGTH_BINARY_PARSER_CONFIGURATION";
	public static final String VAR_LENGTH_BINARY_PARSER_ATTRIBUTE = "VAR_LENGTH_BINARY_PARSER_ATTRIBUTE";
	public static final String VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE = "VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE";
	
	public static final String VAR_LENGTH_ASCII_PARSER_CONFIGURATION = "VAR_LENGTH_ASCII_PARSER_CONFIGURATION";
	public static final String VAR_LENGTH_ASCII_PARSER_ATTRIBUTE = "VAR_LENGTH_ASCII_PARSER_ATTRIBUTE";
	
	public static final String XML_PARSER_CONFIGURATION = "XML_PARSER_CONFIGURATION";
	public static final String XML_PARSER_ATTRIBUTE = "XML_PARSER_ATTRIBUTE";

	public static final String ASN1_PARSER_CONFIGURATION = "ASN1_PARSER_CONFIGURATION";
	public static final String ASN1_PARSER_ATTRIBUTE = "ASN1_PARSER_ATTRIBUTE";
	public static final String ASN1_HEADER_PARSER_ATTRIBUTE = "ASN1_HEADER_PARSER_ATTRIBUTE";
	public static final String ASN1_TRAILER_PARSER_ATTRIBUTE = "ASN1_TRAILER_PARSER_ATTRIBUTE";

	public static final String ASN1_PARSER_ATTRIBUTE_TYPE = "attributeType";
    
	public static final String HTML_PARSER_ATTRIBUTE = "HTML_PARSER_ATTRIBUTE";
	public static final String HTML_PARSER_GROUP_ATTRIBUTE = "HTML_PARSER_GROUP_ATTRIBUTE";
	public static final String HTML_PARSER_CONFIGURATION = "HTML_PARSER_CONFIGURATION";
	
	public static final String XLS_PARSER_ATTRIBUTE = "XLS_PARSER_ATTRIBUTE";
	public static final String XLS_PARSER_CONFIGURATION = "XLS_PARSER_CONFIGURATION";
	public static final String XLS_PARSER_GROUP_ATTRIBUTE = "XLS_PARSER_GROUP_ATTRIBUTE";
	

	public static final String SNMP_SERVER_ID = "SnmpServerId";
	public static final String SNMP_SERVER_NAME = "SnmpServerName";
	public static final String SNMP_SERVER_IP = "SnmpServerHostIP";
	public static final String SNMP_SERVER_PORT = "SnmpServerPort";
	public static final String SNMP_SERVER_OFFSET = "SnmpServerPortOffset";
	public static final String SNMP_SERVER_COMMUNITY = "SnmpServerCommunity";
	public static final String SNMP_SERVER_ENABLE = "enable";
	public static final String SNMP_SERVER_V3_ENGINE_ID = "snmpV3EngineId";
	public static final String EDIT = "Edit";

	public static final String KEY_VALUE_RECORD = "KEY_VALUE_RECORD";
	public static final String FILE_HEADER_FOOTER = "FILE_HEADER_FOOTER";
	public static final String RECORD_HEADER = "RECORD_HEADER";
	public static final String DELIMITER = "DELIMITER";
	public static final String LINEAR_KEY_VALUE_RECORD = "LINEAR_KEY_VALUE_RECORD";

	public static final String COMPOSER_ID = "composerId";
	public static final String COMPOSER_NAME = "composerName";
	public static final String COMPOSER_TYPE = "composerType";
	public static final String COMPOSER_MAPPING_ID = "composerMappingId";

	public static final String ASCII_COMPOSER_CONFIGURATION = "ASCII_COMPOSER_CONFIGURATION";
	public static final String ASCII_COMPOSER_ATTRIBUTE = "ASCII_COMPOSER_ATTRIBUTE";

	// saumil.vachheta
	public static final String DETAIL_LOCAL_COMPOSER_CONFIGURATION = "DETAIL_LOCAL_COMPOSER_CONFIGURATION";
	public static final String DETAIL_LOCAL_COMPOSER_ATTRIBUTE = "DETAIL_LOCAL_COMPOSER_ATTRIBUTE";
	// saumil.vachheta
		
	
	public static final String ASN1_COMPOSER_CONFIGURATION = "ASN1_COMPOSER_CONFIGURATION";
	public static final String ASN1_COMPOSER_ATTRIBUTE = "ASN1_COMPOSER_ATTRIBUTE";
	public static final String ASN1_COMPOSER_HEADER_ATTRIBUTE = "ASN1_COMPOSER_HEADER_ATTRIBUTE";
	public static final String ASN1_COMPOSER_TRAILER_ATTRIBUTE = "ASN1_COMPOSER_TRAILER_ATTRIBUTE";

	public static final String ROAMING_COMPOSER_CONFIGURATION = "ROAMING_COMPOSER_CONFIGURATION";
	public static final String ROAMING_COMPOSER_ATTRIBUTE = "ROAMING_COMPOSER_ATTRIBUTE";

	public static final String SNMP_CLIENT_ID = "snmpClientId";
	public static final String SNMP_CLIENT_NAME = "snmpClientName";
	public static final String SNMP_CLIENT_IP = "snmpClientHostIP";
	public static final String SNMP_CLIENT_PORT = "snmpClientPort";
	public static final String SNMP_CLIENT_VERSION = "snmpClientVersion";
	public static final String SNMP_CLIENT_COMMUNITY = "snmpClientCommunity";
	public static final String SNMP_CLIENT_ADVANCE = "snmpClientAdvance";
	
	public static final String SNMP_CLIENT_V3_AUTH_ALGO = "snmpV3AuthAlgorithm";
	public static final String SNMP_CLIENT_V3_AUTH_PASSWORD = "snmpV3AuthPassword";//NOSONAR
	public static final String SNMP_CLIENT_V3_PRIV_ALGO = "snmpV3PrivAlgorithm";
	public static final String SNMP_CLIENT_V3_PRIV_PASSWORD = "snmpV3PrivPassword";//NOSONAR
	public static final String SNMP_CLIENT_V3_ENGINE_ID = "snmpV3EngineId";
	

	public static final String SNMP_ALERT_ID = "alertId";
	public static final String SNMP_ALERT_NAME = "name";
	public static final String SNMP_ALERT_SERVICETHRESHOLD = "threshold";
	public static final String SNMP_ALERT_DESCRIPTION = "desc";
	public static final String SNMP_ALERT_SERVICETHRESHOLD_LABEL = "serviceThreshold";
	public static final String SNMP_ALERT_TYPE = "alertType";

	public static final String STATUS = "status";
	public static final String ALIASES = "aliases";
	public static final String CONDITIONS = "conditions";
	public static final String AUDITE_DATE = "auditDate";
	public static final String DESC = "desc";
	public static final String ASC = "asc";
	public static final String USER_NAME = "userName";
	public static final String STAFF_NAME = "staffName";

	public static final String GROUP_TYPE_ENUM = "groupingType";

	public static final String REGEX_MAP_CACHE = "REGEX MAP CACHE";

	public static final String THRESHOLD = "threshold";
	public static final String THRESHOLDID = "thresholdId";
	public static final String THRESHOLD_TYPE = "thresholdType";
	public static final String SVCLIST = "svcList";

	public static final String SERVER_INSTANCE = "SERVER_INSTANCE";
	public static final String WRAPPER_ID = "wrapper.id";
	public static final String GENERIC = "GENERIC";
	public static final String CLIENT_PORT_REQUIRED = "SNMPclientlist.port.required";
	public static final String PORT_OR_OFFSET_REQUIRED = "SNMPserverlist.port.or.offset.required";
	public static final String PORTOFFSET = "portOffset";
	public static final String SNMP_COMMUNITY_REQUIRED = "SNMPclientlist.community.required";
	public static final String OFFSET_OUT_OF_RANGE = "SNMPserverlist.offset.outOfRange";

	public static final String DRIVER_FULL_CLASS_NAME = "com.elitecore.sm.drivers.model.Drivers";

	public static final String FULL_STOP = ".";

	public static final String SERVICE_TYPE_PROFILING = "SERVICE_TYPE";
	public static final String DRIVER_TYPE_PROFILING = "DRIVER_TYPE";
	public static final String PLUGIN_TYPE_PROFILING = "PLUGIN_TYPE";
	public static final String GENERAL_TYPE_PROFILING = "GENERAL";
	public static final String MAIN_SERVICE_TYPE = "MAIN_SERVICE_TYPE";
	public static final String ADDITIONAL_SERVICE_TYPE = "ADDITIONAL_SERVICE_TYPE";
	public static final String COLLECTION_DRIVER_TYPE = "COLLECTION_DRIVER_TYPE";
	public static final String DISTRIBUTION_DRIVER_TYPE = "DISTRIBUTION_DRIVER_TYPE";
	public static final String PARSER_PLUGIN_TYPE = "PARSER_PLUGIN_TYPE";
	public static final String COMPOSER_PLUGIN_TYPE = "COMPOSER_PLUGIN_TYPE";
	public static final String AGENT_TYPE_PROFILING = "AGENT";
	public static final String AGENT_TYPE= "AGENT_TYPE";
	public static final String AGENT= "Agent";	

	public static final String SM_LICENSE_REPOISTORY_PATH = "repository_path";
	public static final String MEDIATION = "MEDIATION";
	public static final String CGF = "CGF";
	public static final String IPLMS = "IPLMS";
	public static final String TRUE_FALSE_ENUM = "trueFalseEnum";
	public static final String READ_ONLY_FLAG = "readOnlyFlag";
	public static final String CRITERIA_COMPOSER_ID = "myComposer.id";
	public static final String SOURCE_DATE_FORMAT_ENUM = "sourceDateFormatEnum";
	public static final String MODULE_ADMIN_USERNAME = "moduleadmin";
	public static final String PROFILE_ADMIN_USERNAME = "profileadmin";
	public static final String PROFILE_CONFIGURATION = "PROFILE_CONFIGURATION";
	public static final String MODULE_ADMIN_ACCCESS_GROUP = "Module Admin Access Group";
	public static final String PROFILE_ADMIN_ACCCESS_GROUP = "Profile Admin Access Group";
	public static final String POLICIES_CONFIGURATION = "POLICIES_CONFIGURATION";
	public static final String RELOAD_CACHE = "RELOAD_CACHE";
	
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";

	public static final String DATASOURCE_ID= "id";
	

	public static final String PACKETSTATASTICAGENT = "PACKETSTATASTICAGENT";
	public static final String EXECUTION_INTERVAL = "executionInterval";
	public static final String EXECUTION_INTERVAL_OUT_OF_RANGE = "agent.executionInterval.outOfRange";
	
	public static final String SERVICEPACKETSTATS_ID = "servicePacketStatsId";
	public static final String SERVICEPACKETSTATS_NAME = "servicePacketStatsSvcName";
	public static final String SERVICEPACKETSTATS_TYPE = "servicePacketStatsSvcType";
	public static final String SERVICEPACKETSTATS_CATEGORY = "servicePacketStatsSvcCategory";
	
	public static final String PACKET_STATISTICS_AGENT="PACKET_STATISTICS_AGENT";
	public static final String PACKET_STATISTICS_AGENT_JMX_CALL_NAME="Packet Statistics Agent";
	public static final String LAST_EXECUATION_DATE="lastExecutionDate";
	public static final String NEXT_EXECUATION_DATE="nextExecutionDate";
	public static final String SERVER_INSTANCE_CONSTANT="serverInstance";
	public static final String SYNC_STATUS="syncStatus";
	public static final String SVCTYPE="svctype";
	public static final String SVCTYPE_TYPE="svctype.type";
	public static final String SVCTYPE_ID="svctype.id";
	public static final String SERVERINSTANCE_ID="serverInstance.id";
	public static final String SERVER_ID="server.id";

	public static final String KAFKA_SERVER_IP_INVALID="KafkaDataSourceConfig.kafkaServerIpAddress.invalid";
	
	public static final String NETFLOW_CLIENT_IP_INVALID="NetflowClient.clientIpAddress.invalid";
	public static final String NETFLOW_REDIRECTION_IP_INVALID="NetflowClient.redirectionIp.invalid";
	public static final String NETFLOW_PROXY_SERVER_IP_INVALID="NetflowClient.proxyServerIp.invalid";
	public static final String FILE_NAME_FORMAT="fileNameFormat";

	public static final String IMAGE_BYTE_CONSTANT="data:image/jpeg;base64,";
	public static final String SMALL_LOGO_TYPE="small";
	public static final String LARGE_LOGO_TYPE="large";
	public static final String STAFF_PROFILE_PIC="STAFF_PROFILE_PIC";
	public static final String EDIT_STAFF_PROFILE_PIC="EDIT_STAFF_PROFILE_PIC";
	public static final String UPDATE_STAFF_PROFILE_PIC = "UPDATE_STAFF_PROFILE_PIC";
	public static final String CHANGE_STAFF_PROFILE_PIC = "CHANGE_STAFF_PROFILE_PIC";
	public static final String UPDATE_MY_PROFILE_PIC = "UPDATE_MY_PROFILE_PIC";
	public static final String STAFF_LOGO = "STAFF_LOGO";
	public static final String TRIAL_BUTTON_ACTION = "disableTrialButton";
	

	//Audit remark constants
	public static final String ENTITY_NAME = "entityName";
	public static final String SERVER_TYPE = "serverType";
	public static final String SUB_ENTITY_NAME = "subEntityName";
	public static final String SERVER_INSTANCE_NAME = "serverInstanceName";
	
	
	public static final String ACCOUNTSTATE = "accountState";
	public static final String LAST_NAME ="lastName";
	public static final String USERNAME ="username";
	public static final String STAFF_TYPE ="stafftype";
	public static final String EMAIL_ID ="emailId";
	public static final String MAIL_ID ="EMAIL_ID";
	
	public static final String FIRST_NAME ="firstName";
	public static final String EMAILID ="EMAILID";
	public static final String USER ="user: ";
	public static final String MODULE_NAME ="ServerManager";
	public static final String MODULE_NAME_SM ="ServerManagerRevamp";
	
	public static final String LOB = "lob";
	
	//MIS REport Constants
	public static final String PDF_FILE_TYPE = "pdf";
	public static final String DATE_FORMAT = "DD-MM-YYYY HH24:MI:SS";
	public static final String DATE_FORMAT_FOR_JOBSTATISTIC = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_SHORT = "dd/MM/yyyy";
	public static final String CRESTEL_IMAGE_PATH="../resources/images/jispbilling.jpg";
	public static final String HOURLY = "HOURLY";
	public static final String DAILY = "DAILY";
	public static final String MONTHLY = "MONTHLY";
	public static final String HOUR = "Hour";
	public static final String DATE = "Date";
	public static final String MONTH = "Month";
	public static final String MONTHENUM = "months";
	public static final String MISREPORTTYPEENUM = "misReportType";
	public static final int MIN_YEAR = 2011;
	public static final String TOTAL_PACKETS = "Total Packets";
	public static final String SUCCESS_PACKETS = "Success Packets";
	public static final String MALFORMED_PACKETS = "Malformed Packets";
	public static final String MALFORMED_PACKETS_PERCENTAGE = "Malformed Packets %";
	public static final String TOTAL_FILES = "Total Files";
	public static final String SUCCESS_FILES = "Success Files";
	public static final String FAIL_FILES = "Fail Files";
	public static final String FAIL_FILES_PERCENTAGE = "Fail Files %";
	public static final String TOTAL_RECORDS = "Total Records";
	public static final String SUCCESS_RECORDS = "Success Records";
	public static final String FAIL_RECORDS = "Fail Records";
	public static final String FAIL_RECORDS_PERCENTAGE = "Fail Records %";
	public static final String TOTAL_RECORDS_GRID = "totalRecords";
	public static final String SUCCESS_RECORDS_GRID = "successRecords";
	public static final String FAIL_RECORDS_GRID = "failRecords";
	public static final String FAIL_RECORDS_PERC_GRID = "failedRecordsPercentage";
	public static final String SERIAL_NO = "Sr. No.";
	public static final String SRNO_GRID = "srno";
	public static final String SERVERNAME_GRID = "serverName";
	public static final String SERVICENAME_GRID ="serviceName";
	public static final String TOTAL_PACKETS_GRID="totalPackets";
	public static final String SUCCESSPACKETS_GRID="successPackets";
	public static final String DROPPEDPACKETS_GRID="droppedPackets";
	public static final String DROPPEDPACKETSPERC_GRID="droppedPacketsPercentage";
	public static final String MONTH_GRID="month";
	public static final String TOTALFILES_GRID="totalFiles";
	public static final String SUCCESSFILES_GRID="successFiles";
	public static final String FAILFILES_GRID="failFiles";
	public static final String FAILFILESPERC_GRID="failFilesPercentage";
	public static final String MIS_DOWNLOAD_HEADER_STRING_1 ="Content-Disposition";
	public static final String MIS_DOWNLOAD_HEADER_STRING_2 = "attachment;filename=\"";
	public static final int CUSTOM = 2;
	public static final int SELECT = 1;
	public static final int MAX_DUARATION = 1;
	public static final String REPORT_HEADER = "Elitecore Technologies Pvt. Ltd.";
	public static final String REPORT_FOOTER = "This is System generated report, and needs no signature";
	public static final String SERVICE_WISE_DETAIL_REPORT = "ServiceWiseDetailReport";
	public static final String SERVICE_WISE_SUMMARY_REPORT = "ServiceWiseSummaryReport";
	public static final String SERVER_INSTANCE_MIS = "Server Instance";
	public static final String SERVICE_INSTANCE = "Service Instance";
	public static final String SHORT_DATE_FORMAT = "SHORT_DATE_FORMAT";
		
	public static final String  RADIUS_COLLECTION_SERVICE_ALIAS = "RADIUS_COLLECTION_SERVICE";
	public static final String NATFLOW_COLLECTION_SERVICE_ALIAS = "NATFLOW_COLLECTION_SERVICE";
	public static final String SYSLOG_COLLECTION_SERVICE_ALIAS = "SYSLOG_COLLECTION_SERVIE";
	public static final String MQTT_COLLECTION_SERVICE_ALIAS = "MQTT_COLLECTION_SERVIE";
	public static final String COAP_COLLECTION_SERVICE_ALIAS = "COAP_COLLECTION_SERVIE";
	public static final String HTTP2_COLLECTION_SERVIE_ALIAS = "HTTP2_COLLECTION_SERVIE";
	public static final String  GTPPRIME_COLLECTION_SERVICE_ALIAS = "GTPPRIME_COLLECTION_SERVICE";
	public static final String  NATFLOWBINARY_COLLECTION_SERVICE_ALIAS = "NATFLOWBINARY_COLLECTION_SERVICE";
	public static final String  AGGREGATION_SERVICE_ALIAS = "AGGREGATION_SERVICE";
	public static final String  CORRELATION_SERVICE_ALIAS = "CORRELATION_SERVICE";
	public static final String  DATA_CONSOLIDATION_SERVICE_ALIAS = "DATA_CONSOLIDATION_SERVICE";
	public static final int CALL_SUCCESS_STATUS = 1;
	public static final int CALL_FAIL_STATUS = 0;
	public static final int SERVER_ID_ENCRYPTION_MODE = PasswordEncryption.ELITECRYPT;
	public static final String  CUSTOMER_LOGO_ERROR ="systemParameter.customerLogo.error";
	public static final String  SYSTEMPARAM_GEN_LIST_VALUE ="].value";
	public static final String  PD_PARAMLIST_STRING ="pwdParamList[";
	public static final String  STAFF_PD ="Staff.password";
	
	public static final String  DEVICE_TYPE = "deviceType";
	public static final String  VENDOR_TYPE = "vendorType";
	public static final String  PARSER_NAME = "parserName";
	
	public static final String  AGENT_STATUS = "agentStatus";
	public static final String  ALERT_NAME = "alertName";	
	
	public static final String  PATHLIST_NAME = "pathListName";	
	
	public static final String ALL = "ALL";
	public static final String ASSOCIATED = "Associated";
	public static final String NONASSOCIATED = "Non-Associated";
	
	public static final String POLICY_NAME = "policyName";
	public static final String POLICY_ID = "policyId";
	public static final String POLICY_DESCRIPTION = "policyDescription";
	public static final String DESCRIPTION = "description";
	public static final String GROUPS = "groups";
	public static final String CATEGORY = "category";
	public static final String SEVERITY = "severity";
	public static final String ERRORCODE = "errorCode";
	public static final String ASSOCIATION_STATUS = "associationStatus";
	// Server Instance Request Parameters
	public static final String SERVER_INSTANCE_NAME_PARAM = "server-instance-name";
	public static final String SERVER_INSTANCE_HOST_PARAM = "server-instance-host";
	public static final String SERVER_INSTANCE_PORT_PARAM = "server-instance-port";
	public static final String SERVER_INSTANCE_ID_PARAM = "server-instance-id";
	public static final String PASS_INVALID_MSG ="changePassword.new.password.invalid";
	public static final String PASS_ERROR ="NEW_PASSWORD_ERROR";
	public static final String CHANGE_PASS_ERROR ="changePassword.confirm.new.password.invalid";
	public static final String REGEX="[REGEX]";
	public static final String CONFORM_PASS_ERROR="CONFIRM_NEW_PASSWORD_ERROR";
	public static final String OLD_PASS_ERROR="OLD_PASSWORD_ERROR";
	public static final String CLIENT_IPADDRESS= "clientIpAddress";
	public static final String FIELD_SEPARATOR="fieldSeparator";
	public static final String SERVERTYPE_ID="serverType.id";
	public static final String UPDATED_PROP_LIST = "updatedPropList";
	public static final String NEW_PROP_LIST = "newAddedPropList";
	public static final String REMOVED_PROP_LIST = "removedPropList";
	public static final String NULL = "null";
	public static final String NA = "NA";

	public static final String OBJECT_NAME = "objectName";
	public static final String OLD_OBJECT = "oldObject";

	public static final String PD_POLICY_DESCRIPTIONDB ="pwdPolicyDescriptionDB";
	public static final String IS_SSO_ENABLED ="isSSOEnabled";
	public static final String MAXCONNECTIONRETRY ="maxConnectionRetry";
	public static final String RETRY_INTERVAL ="retryInterval";
	public static final String SCRIPT_NAME ="scriptName";
	public static final String SCRIPT_NAME_INVALID ="error.ServerInstance.scriptName.invalid";
	public static final String CONNECTION_TIMEOUT ="connectionTimeout";

	// Policy
	public static final String POLICY_ID_PARAM = "policy-id";
	public static final String POLICY_NAME_PARAM = "policy-name";
	public static final String POLICY_DESCRIPTION_PARAM = "policy-description";
	public static final String NAME = "name";
	public static final String POLICY_GROUP_REL_SET = "policyGroupRelSet";
	public static final String POLICY = "POLICY";
	public static final String RULE_GROUP_NAME = "ruleGroupName";
	public static final String RULE_NAME = "ruleName";
	public static final String STATE="state";
	public static final String ASSIGNEDUNASSIGNEDSTATUS="assignedUnassignedStatus";
	public static final String POLICY_GROUP_RULE_REL_SET = "policyGroupRuleRelSet";
	public static final String RULE_GROUP_ID = "ruleGroupId";
	public static final String RULE_GROUP_DESC = "ruleGroupDesc";
	public static final String RULE_GROUP = "RULE_GROUP";		
	public static final String RULE_ID = "ruleId";
	public static final String QUERY_NAME = "queryName";
	public static final String DYNAMIC = "dynamic";
	
	//Trigger
	public static final String TRIGGER = "CrestelSMTrigger";
	public static final String TRIGGERID = "id";
	public static final String TRIGGERNAME = "triggerName";
	public static final String RECURRENCETYPE = "recurrenceType";
	public static final String DEFAULTRECURRENCETYPE = "SELECT SCHEDULER TYPE";
	
	//Consolidation
	public static final String CONSOLIDATION_SERVICE_SUMMARY = "CONSOLIDATION_SERVICE_SUMMARY";
	public static final String CONSOLIDATION_SERVICE_CONFIGURATION = "CONSOLIDATION_SERVICE_CONFIGURATION";
	public static final String CONSOLIDATION_SOURCE_PATH_MAPPING = "CONSOLIDATION_SOURCE_PATH_MAPPING";
	
	
	public static final String INITIAL_DELAY = "initialDelay";
	public static final String FILE_RENAME_AGENT="FILE_RENAME_AGENT";
	
	public static final String MIGRATION = "MIGRATION";
	public static final String POLICY_RULE = "POLICY_RULE";
	public static final String FIXED_LENGTH_ASCII_PARSER_CONFIGURATION="FIXED_LENGTH_ASCII_PARSER_CONFIGURATION";
	public static final String FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE = "FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE";
			
	public static final String XML_COMPOSER_CONFIGURATION = "XML_COMPOSER_CONFIGURATION";
	public static final String XML_COMPOSER_ATTRIBUTE = "XML_COMPOSER_ATTRIBUTE";
	public static final String DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION = "DISTRIBUTION_DRIVER_ATTRLIST_CONFIGURATION";	
	public static final String DBDISDRIVER_ID = "dbDisDriver.id";
	
	public static final String ASN1_HEADER_COMPOSER_ATTRIBUTE = "ASN1_HEADER_COMPOSER_ATTRIBUTE";
	public static final String ASN1_TRAILER_COMPOSER_ATTRIBUTE = "ASN1_TRAILER_COMPOSER_ATTRIBUTE";
	
	public static final String ASN1_COMPOSER_ATTRIBUTE_TYPE="attributeType";
	
	public static final String MIGRATION_TRACK_DETAIL_ID = "migrationTrackDetailId";
	public static final String GENERIC_TYPE= "GENERIC";
	public static final String SERVINSTANCEID="servInstanceId";

	public static final String SERVER_INSTANC_ID =  "serverInstance.id";
	public static final String  IS_SEARCH = "isSearch";
	public static final String DATABASE_QUERIES = "DATABASE_QUERIES";
	public static final String DATABASE_QUERY_ID = "databaseQuery.id";
	
	public static final String MIGRATION_TRACK_ID_LIST = "migrationTrackIdList";
	public static final String IS_MIGRATION_IN_PROCESS = "isMigrationInProcess";
	
	public static final String HEADER_USERNAME = "X-Username";
	public static final String HEADER_PASS_PARAM = "X-Password";
	public static final String HEADER_TOKEN = "X-Auth-Token";
	
	public static final String REPROCESSING_STATUS = "REPROCESSING_STATUS";
	public static final String REPROCESSING_FILE = "REPROCESSING_FILE";
	public static final String AUTOREPROCESSING_FILE = "AUTOREPROCESSING_FILE";
	
	public static final String FILE_REPROCESS_ID_LIST = "fileReprocessIdList";
	public static final String FIXED_LENGTH_ASCII_COMPOSER_CONFIGURATION = "FIXED_LENGTH_ASCII_COMPOSER_CONFIGURATION";
	public static final String FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE = "FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE";
	
	public static final String SERVICE_DB_STAT = "serviceDbStats";
	
	public static final String FILE_FETCH_TYPE = "fileFetchType";
	public static final String FILE_FILTER_ACTION = "fileFilterActionEnum";
													 
	public static final String POSITION_ENUM = "positionEnum";
	public static final String FILE_FILTER_TYPE_ACTION = "fileFilterActionTypeEnum";
	public static final String TRANSFER_MODE = "transferModeEnum";
	public static final String DRIVER_TYPE_ALIAS = "driverTypeAlias";

	public static final String MIGRATION_STATUS = "migrationStatus";
	
	public static final String   READ_FILE_PREFIX = "readFilePrefix" ;
	
	public static final String   READ_FILE_SUFFIX = "readFileSuffix" ;
	
	public static final String   READ_PATH = "readPath" ;
	public static final String   ARCHIVE_PATH = "archivePath" ;
	
	public static final String  MAX_FILE_COUNT_ALERT =  "maxFilesCountAlert";
	
	public static final String  MAX_SEQ_RANGE =  "maxSeqRange";
	public static final String  READ_FILE_NAME_CONTAIN =  "readFilenameContains";
	public static final String  READ_FILE_NAME_EXCLUDE =  "readFilenameExcludeTypes";
	public static final String  SERVER_STARTTIME ="serverStartTime";
	public static final String  BATCH_ID ="batchId";
	
	public static final String TABLE_NAME = "viewName";

	public static final String UPDATE_RULE_DATA_CONFIG = "UPDATE_RULE_DATA_CONFIG";
	public static final String VIEW_RULE_DATA_RECORDS = "VIEW_RULE_DATA_RECORDS";
	public static final String CSV_FILE_CONTENT = "text/csv";

	public static final String DIST_DRIVER_ID = "driverId";
	public static final String DIST_DRIVER_NAME = "driverName";
	public static final String DIST_DRIVERTYPE_ALIAS = "driverTypeAlias";
	
	public static final String KEYSTORE_FOLDER = "keystore";
	public static final String HTTP2_COLLECTION_SERVICE_FOLDER = "http2collectionservice";
	
	public static final String RESTORE_FILES = "restoreFiles";
	public static final String REPROCESS_FILES = "reprocessFiles";
	public static final String DELETE_REPROCESS_FILES = "deleteReprocessFiles";
	public static final String MODIFY_REPROCESS_FILES = "modifiyReprocessFiles";
	public static final String REVERT_REPROCESS_FILES = "revertReprocessFiles" ;
	public static final String UPLOAD_AND_REPROCESS_FILES = "uploadAndReprocessFiles" ;
	
	public static final String SERVER_INSTANCE_NOT_RUNNING = "Server instance not running." ;
	public static final String SERVICE_INSTANCE_NOT_RUNNING = "Service instance not running." ;
	
	public static final String ERROR_REPROCESS_FILE_DOWNLOAD_PATH = "/WEB-INF/resources/reprocessfiles";
	public static final String KEYCLOAK_JSON_FILE_PATH = "/WEB-INF/keycloak.json";

	public static final String ERROR_REPROCESS_FOLDER = "reprocessfiles";
	
	public static final String FILE_GZ_COMPRESS_EXT = ".gz";
	public static final String FILE_CSV_COMPRESS_EXT = ".csv";
	public static final String POLICY_EXPORT_FILE = "POLICY_EXPORT_FILE";
	
	public static final String POLICY_ACTION_TYPE_STATIC = "static";
	public static final String POLICY_ACTION_TYPE_DYNAMIC = "dynamic";
	public static final String POLICY_ACTION_TYPE_EXPRESSION = "expression";
	
	public static final String EMPTY_STRING = "";
	public static final String UPSTREAM = "UPSTREAM";
	public static final String DOWNSTREAM = "DOWNSTREAM";
	public static final String UNDEFINED_POLICY = "No Policy associated";
	public static final int UTILITY_PORT_MIN_LIMIT = 1023;
	public static final int UTILITY_PORT_MAX_LIMIT = 65536;
	public static final String GENERATE_REPORTS = "GENERATE_REPORTS";
	public static final String RAP_IN_PARSER_CONFIGURATION ="RAP_IN_PARSER_CONFIGURATION";
	public static final String ROAMING_PARSER_CONFIGURATION="ROAMING_PARSER_CONFIGURATION";
	public static final String ROAMING_PARSER_ATTRIBUTE = "ROAMING_PARSER_ATTRIBUTE";
	public static final String ROAMING_PARSER_ATTRIBUTE_TYPE = "attributeType";

	

	public static final String ROAMING_CONFIGURATION = "ROAMING_CONFIGURATION";
	public static final String HOST_CONFIGURATION = "HOST_CONFIGURATION"; 
	public static final String RAP_TAP_CONFIGURATION= "RAP_TAP_CONFIGURATION";
	public static final String ROAMING_PARAMETER="ROAMING_PARAMETER";
	public static final String FILE_MANAGEMENT="FILE_MANAGEMENT";
	public static final String TEST_SIM_MANAGEMENT="TEST_SIM_MANAGEMENT";
	public static final String FILE_SEQUENCE_MANAGEMENT="FILE_SEQUENCE_MANAGEMENT";
	
	public static final String UPLOAD_FILE_AND_REPROCESS = "UPLOAD_FILE_AND_REPROCESS";
	public static final String FILE_REPROCESS = "FILE_REPROCESS";
	public static final String FILE_RESTORE = "FILE_RESTORE";
	public static final String LICENSE_ACTION = "LICENSE_ACTION";
	
	public static final String PARTNER_ID="partner.id";
	public static final String SUBSCRIBER_TYPE ="type";
	public static final String CURRENT_PAGE = "currentPage";
	
	public static final String FIXED_LENGTH_BINARY_PARSER_CONFIGURATION="FIXED_LENGTH_BINARY_PARSER_CONFIGURATION";
	public static final String FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE = "FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE";
	public static final String PDF_PARSER_CONFIGURATION="PDF_PARSER_CONFIGURATION";
	public static final String PDF_PARSER_ATTRIBUTE = "PDF_PARSER_ATTRIBUTE";
	public static final String PDF_PARSER_GROUP_ATTRIBUTE = "PDF_PARSER_GROUP_ATTRIBUTE";
	public static final String BINARY_PARSER_CONFIGURATION = "BINARY_PARSER_CONFIGURATION";
	
	
	public static final String PIPE_DELIMITER = "|";
	public static final String ORACLE_JDBS_DRIVER_ORACLEDRIVER="oracle.jdbc.driver.OracleDriver";
	public static final String ORG_POSTGRESQL_DRIVER="org.postgresql.Driver";
	public static final String COM_MYSQL_JDBC_DRIVER="com.mysql.jdbc.Driver";
		
	public static final String DIAMETER_COLLECTION_SERVICE_SUMMARY = "DIAMETER_COLLECTION_SERVICE_SUMMARY";
	public static final String DIAMETER_COLLECTION_SERVICE_CONFIGURATION = "DIAMETER_COLLECTION_SERVICE_CONFIGURATION";
	public static final String DIAMETER_COLLECTION_PEER_CONFIGURATION = "DIAMETER_COLLECTION_PEER_CONFIGURATION";
	public static final String COMMA_SEPARATOR 		 = ",";
	public static final String EQUALTO_SEPARATOR  	 = "=";
	public static final String HASH_SEPARATOR		 = "#";
	public static final String DIAMETER_PEER		 = "Diameter Peer";
	
	public static final String VIEW_DYNAMIC_REPORT = "VIEW_DYNAMIC_REPORT";
	public static final String VIEW_DYNAMIC_REPORT_TABLE_RECORDS = "VIEW_DYNAMIC_REPORT_TABLE_RECORDS";
	
	public static final String AUTO_RELOAD_CACHE_CONFIG = "AUTO_RELOAD_CACHE_CONFIG";	
	
	public static final String UPDATE_TRIGGER_CONFIG = "UPDATE_TRIGGER_CONFIG";
	public static final String VIEW_TRIGGER_RECORDS = "VIEW_TRIGGER_RECORDS";
	public static final String SCHEDULER_JOB_PREFIX = "J";
	public static final String SCHEDULER_ORIGINAL_TRIGGER_PREFIX = "JT";
	public static final String SCHEDULER_PARENT_TRIGGER_PREFIX = "T";
	public static final String AUTO_UPLOAD_CONFIG = "AUTO_UPLOAD_CONFIG";
	public static final String AUTO_UPLOAD_RELOAD_STATUS = "AUTO_UPLOAD_RELOAD_STATUS";
	
	public static final String PDF_APPLICATION_CONTENT_TYPE = "application/pdf";	
	public static final String EXCEL_APPLICATION_CONTENT_TYPE = "application/vnd.ms-excel";
	
	public static final String SCHEDULER_TYPE = "scheduleType";
	public static final String COMPLETED = "Completed";
	public static final String FAILED = "Failed";
	public static final String SCHEDULED = "Scheduled";
	
	public static final String RADIUS_COLLECTION_SERVICE_SUMMARY = "RADIUS_COLLECTION_SERVICE_SUMMARY";
	public static final String RADIUS_COLLECTION_SERVICE_CONFIGURATION = "RADIUS_COLLECTION_SERVICE_CONFIGURATION";
	public static final String RADIUS_COLLECTION_PEER_CONFIGURATION = "RADIUS_COLLECTION_PEER_CONFIGURATION";
	public static final String TABLE_FIELD_UNIQUE_SEPERATOR = "#";
	
	public static final String SSO_STAFF="SSO";
	public static final String LDAP_STAFF="LDAP";
	public static final String LOCAL_STAFF="LOCAL";
	
	
	public static final String LICENSE_KEY_SPILTER=",";
	public static final String LICENSE_KEY_NAME_SEPERATER=":";
	
	public static final String VERSIONCONFIGID = "versionConfigId";
	
	public static final String VERIFIED_LINK_SENT = "VERIFIED_LINK_SENT";		
	public static final String VERIFIED_LINK_EXPIRED = "VERIFIED_LINK_EXPIRED";
	public static final long LINK_EXPIRATION_TIME_IN_MINUTE=30l;
	public static final String ACCESS_GROUP_TYPE="accessGroupType";
	
	public static final String ENABLE_RSA_AUTHENTICATION = "ENABLE_RSA_AUTHENTICATION";
    public static final String RSA_SERVER_URL = "RSA_SERVER_URL";
    public static final String CLIENT_KEY = "CLIENT_KEY";
    public static String RSA_INITIATE_REQUEST = "{\n" + "    \"clientId\": \"apihost\",\n"//NOSONAR
			+ "    \"subjectName\": \"test01\",\n" + "    \"context\": {\n" + "        \"authnAttemptId\": \"\",\n"
			+ "        \"messageId\": \"test4726375261635\",\n" + "        \"inResponseTo\": \"\"\n" + "    }\n" + "}";	
    public static String RSA_VERIFY_REQUEST = "{\n" + //NOSONAR
			"    \"subjectCredentials\": [\n" + 
			"        {\n" + 
			"            \"methodId\": \"SECURID\",\n" + 
			"            \"collectedInputs\": [\n" + 
			"                {\n" + 
			"                    \"name\": \"SECURID\",\n" + 
			"                    \"value\": \"222222\"\n" + 
			"                }\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ],\n" + 
			"    \"context\": {\n" + 
			"        \"authnAttemptId\": \"213f5ca0-2654-4733-acbc-9065c3da3ad7\",\n" + 
			"        \"messageId\": \"test7177617189202\",\n" + 
			"        \"inResponseTo\": \"afc6350d-ceef-454a-b012-fb1b0fad6456\"\n" + 
			"    }\n" + 
			"}";
	// its final class 
    public static final String REC_MAIN_ATTR_SYMBOL = "$$$";
    
    public static final String SOURCE_CHARSET_NAME = "sourceCharsetName";
    public static final String SOURCE_FIELD = "sourceField";
    public static final String UNIFIED_FIELD = "unifiedField";
    public static final String TRIM_POSITION = "trimPosition";
    public static final String DEFAULT_TEXT = "defaultText";
    public static final String TRIM_CHAR = "trimChar";
    public static final String ATTRIBUTE_ORDER = "attributeOrder";
    public static final String COLUMN_STARTS_WITH = "columnStartsWith";
    public static final String TABLE_FOOTER = "tableFooter";
    public static final String EXCEL_ROW = "excelRow";
    public static final String EXCEL_COL = "excelCol";
    public static final String RELATIVE_EXCEL_ROW = "relativeExcelRow";
    public static final String STARTS_WITH = "startsWith";
    public static final String COLUMN_CONTAINS = "columnContains";
    public static final String TABLE_ROW_ATTRIBUTE = "tableRowAttribute";
    
    
    public static final String VALIDATION_FAIL = "isValidationFail";
    public static final String SEL_DEVICE_ID = "selDeviceId";
    public static final String SEL_MAPPING_ID = "selMappingId";
    public static final String SEL_VENDOR_TYPE_ID = "selVendorTypeId";
    public static final String SEL_DEVICE_TYPE_ID = "selDeviceTypeId";
    public static final String SELECT_DEVICE_NAME = "selecteDeviceName";
    public static final String SELECT_MAPPING_NAME = "selecteMappingName";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String READ_ONLY_FLAG_GROUP = "readOnlyFlagGroup";
    public static final String TABLE_START_IDENTIFIER = "tableStartIdentifier";
    public static final String TABLE_END_IDENTIFIER = "tableEndIdentifier";
    public static final String TABLE_START_IDENTIFIER_COL = "tableStartIdentifierCol";
    public static final String TABLE_END_IDENTIFIER_COL = "tableEndIdentifierCol";
    public static final String GROUP_ATTRIBUTE = "groupAttribute";
    public static final String ATTRIBUTE_LIST = "attributeList";
    public static final String SOURCE_FIELD_FORMAT = "sourceFieldFormat";
    public static final String FILETYPEENUM = "fileTypeEnum";
    public static final String SEPERATORENUM = "SeparatorEnum";
    public static final String HEADERFOOTERTYPEENUM = "headerFooterTypeEnum";
    public static final String TRUEFALSEENUM = "trueFalseEnum";
    public static final String FIND = "find";
    public static final String REPLACE = "replace";
    public static final String ACTION_TYPE = "actionType";
    public static final String UPLOADEDDATADEFINITIONFILENAME = "uploadedDataDefinitionFileName";
    public static final String DATADEFINITIONFILENAME = "dataDefinitionFileName";
    public static final String IMAGE_NAME = "IMAGE_NAME";
    public static final String QUOTES="<quotes>";
    
    public static final String DISTRIBUTION_DB_DRIVER ="Distribution_Db_Driver";
    public static final String SAMPLE_ATTRIBUTE_CSV = "_sample_attribute.csv";
    public static final String APPLICATION_CSV= "application/csv";
    public static final String ATTRIBUTEMAPPING= "AttributeMapping_";
    public static final String CONTENT_DISPOSITION="Content-Disposition";
    public static final String YES="YES";
    public static final String UTF="UTF-8";
    
    public static final String DATADEFINITIONFILEENUM = "dataDefinitionFileEnum";
    
    public static final String DRIVER_TYPE_SFTP_COLLECTION_DRIVER = "SFTPCollectionDriver";
   	public static final String DRIVER_TYPE_FTP_COLLECTION_DRIVER = "FTPCollectionDriver";
   	public static final String DRIVER_TYPE_SFTP_DISTRIBUTION_DRIVER = "SFTP_DISTRIBUTION_DRIVER";
   	public static final String DRIVER_TYPE_FFTP_DISTRIBUTION_DRIVER = "FTPDistributionDriver";
   	public static final String CRESTEL_P_ENGINE_HOME_PATH = "/opt/crestelsetup/crestelpengine";
   	public static final String JAVA_HOME_PATH= "/opt/crestelsetup/jdk1.8.0_65";
   	public static final String KUBERNETES_ENV= "KUBERNETES_ENV"; 
	public static final String SKIP_LICENSE= "SKIP_LICENSE"; 
   	public static final Object SCRIPT_MANAGEMENT = "SCRIPT_MANAGEMENT";
   	
   	public static final String MAX_LICENSE_TPS = "MAX_LICENSE_TPS";
   	public static final String HOUR_WISE_TOTAL_TPS = "HOUR_WISE_TOTAL_TPS";
   	public static final String CURRENT_LICENSE_TPS = "CURRENT_LICENSE_TPS";
   	public static final String APPLIED_LICENSE_TPS = "APPLIED_LICENSE_TPS";
   	
   	public static final String HOUR_WISE_TOTAL_TPS_MAP = "HOUR_WISE_TOTAL_TPS_MAP";
   	public static final String HOUR_WISE_CURRENT_TPS_MAP = "HOUR_WISE_CURRENT_TPS_MAP";
   	
   	public static final String LAST_UPDATED_DATE_OF_HOUR_WISE_MAP = "LAST_UPDATED_DATE_OF_HOUR_WISE_MAP";
   	
   	public static final String CIRCLE_CONFIGURATION = "CIRCLE_CONFIGURATION";
   	public static final String CIRCLE_NAME = "circleName";
   	
	private BaseConstants() {

	}
}