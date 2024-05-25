/**
 * 
 */
package com.elitecore.sm.common.constants;


/**
 * @author Sunil Gulabani Mar 27, 2015
 */
public interface ControllerConstants {
	String ROOT_PATH = "/";
	String WELCOME = "welcome";
	String SPRING_SECURITY_LOGOUT = "j_spring_security_logout";

	String HOME = "/home";
	String SSOHOME = "/sso";
	String RSA_HOME = "rsaHome";

	String CHANGE_LANGUAGE = "changeLanguage";

	String LOGIN_FAIL = "login-fail";
	String LOGIN = "/api/login";
	String LOGOUT = "logout";
	String SSOLOGOUT = "ssologout";

	String DOWNLOAD_VERSION_CONFIG_XML="downloadVersionConfigXML";
	String COMPARE_VERSION_CONFIG_XML="compareVersionConfigXML";
	String GET_VERSION_CONFIG_DETAIL="getVersionConfigDetail";
	
	String LICENSE_DETAILS = "licenseDetails";	
	String STAFF_MANAGER = "staffManager";
	String INIT_ADD_ACCESS_GROUP = "initAddAccessGroup";
	String GET_ACCESS_GROUP_LIST = "getAccessGroupList";
	String ADD_ACCESS_GROUP = "addAccessGroup";
	String VIEW_ACCESS_GROUP = "viewAccessGroup";
	String INIT_UPDATE_ACCESS_GROUP = "initUpdateAccessGroup";
	String UPDATE_ACCESS_GROUP = "updateAccessGroup";
	String REDIRECT_AFTER_ADD_ACCESS_GROUP = "redirectAfterAddAccessGroup";
	String DELETE_ACCESS_GROUP = "deleteAccessGroup";
	String CHANGE_ACCESS_GROUP_STATE = "changeAccessGroupState";
	String VIEW_STAFF_DETAILS = "viewStaffDetails";

	String GET_STAFF_LIST = "getStaffList";
	String INIT_ADD_STAFF = "initAddStaff";
	String ADD_STAFF = "addStaff";
	String ADD_STAFF_AND_ASSIGN_ACCESS_GROUP = "addStaffAndAssignAccessGroup";
	String GO_BACK_TO_ADD_STAFF_FROM_ASSIGN_ACCESS_GROUP = "goBackToAddStaffFromAssignAccessGroup";
	String GET_STAFF_ID_AND_USERNAME = "getStaffIdAndUsername";
	String DELETE_STAFF = "deleteStaff";
	String LOCK_UNLOCK_STAFF = "lockUnlockStaff";
	String CHANGE_STAFF_STATE = "changeStaffState";
	String UPDATE_STAFF = "updateStaff";
	String UPDATE_STAFF_PASSWORD = "updateStaffPassword"; // NOSONAR

	String GET_MENU_TAB_ACTION_LIST = "getMenuTabActionList";

	String INIT_CHANGE_PASSWORD = "initChangePassword"; // NOSONAR
	String CHANGE_PASSWORD = "changePassword"; // NOSONAR
	String RESET_PASSWORD = "resetPassword"; // NOSONAR
	String INIT_FORGOT_PASSWORD = "initForgotPassword"; // NOSONAR
	String VERIFY_DETAILS_FOR_FORGOT_PASSWORD = "verifyDetailsForForgotPassword"; // NOSONAR
	String VERIFY_USER_FOR_FORGOT_PASSWORD="verifyUserForForgotPassword"; // NOSONAR
	String FORGOT_PASSWORD = "forgotPassword"; // NOSONAR
	String RESET_PASSWORD_FOR_FORGOT_PASSWORD = "resetPasswordForForgotPassword"; // NOSONAR

	String ACCESS_DENIED = "403";
	String PAGE_NOT_FOUND = "error404";
	String REQUESTED_METHOD_NOT_SUPPORTED = "error405";
	String INTERNAL_ERROR = "error405";
	

	// added for system parameter module
	String EDIT_SYSTEM_PARAMETER = "editSystemParameter";
	String EDIT_GENERAL_SYSTEM_PARAMETER = "editGenSystemParameter";
	String EDIT_PASSWORD_SYSTEM_PARAMETER = "editPwdSystemParameter"; // NOSONAR
	String EDIT_CUSTOMER_SYSTEM_PARAMETER = "editCustSystemParameter";
	String INIT_EDIT_SYSTEM_PARAMETER = "initEditSystemParameter";
	String EDIT_EMAIL_PARAMETER = "editEmailParameter";

	String SERVER_CONTROLLER_PATH = "/server";
	String INIT_ADD_SERVER = "initAddServer";
	String ADD_SERVER = "addServer";
	String UPDATE_SERVER = "updateServer";
	String DELETE_SERVER = "deleteServer";
	String DELETE_SERVER_CHECK = "deleteServerCheck";
	
	String SERVER_INSTANCE_CONTROLLER_PATH = "/serverInstance";
	String INIT_ADD_SERVER_INSTANCE = "initAddServerInstance";
	String ADD_SERVER_INSTANCE = "addServerInstance";
	String SYNC_SERVER_INSTANCE="syncServerInstance";
	String UPDATE_SERVER_INSTANCE_ADVANCE_CONFIG = "updateServerInstanceAdvConfig";
	String SERVER_INSTANCE_RELOAD_CONFIG = "serverInstaneReloadConfig";
	String GET_SERVER_INSTANCE_LIST = "getServerInstaneList";
	String GET_SERVER_INSTANCE_MAP = "getServerInstanceMap";
	String UPLOAD_IMPORT_FILE = "uploadImportFile";
	String SERVER_INSTANCE_RELOAD_CACHE = "serverInstaneReloadCache";
	
	String INIT_ALL_SYSTEM_PARAMETER = "initAllSystemParameter";
	String MODIFY_SYSTEM_PARAMETER = "modifySystemParameter";
	String MIGRATE_STAFF_TO_KEYCLOAK = "migrateStaffToKeycloak";
	String INIT_UPLOAD_XSLT="inituploadXSLT";
	String UPLOAD_XSLT="uploadXSLT";
	
	String INIT_SERVER_MANAGER = "initServerManager";
	
	String MY_PROFILE = "myProfile";
	String UPDATE_PROFILE = "updateProfile";
	
	String SOFT_RESTART_INSTANCE="softReStartInstance";
	String SYNC_PUBLISH_INSTANCE="syncPublishInstance";
	String RESTORE_SYNC_INSTANCE="restoreSyncPublishServerInstance";
	String IMPORT_SERVER_INSTANCE_CONFIG="importInstanceConfig";
	String EXPORT_SERVER_INSTANCE_CONFIG="exportInstanceConfig";
	String STOP_SERVER_INSTANCE="stopServerInstance";
	String START_SERVER_INSTANCE="startServerInstance";
	String RESTART_SERVER_INSTANCE="restartServerInstance";
	String INIT_UPDATE_SERVER_INSTANCE = "initUpdateServerInstance";
	String INIT_UPDATE_SERVER = "initUpdateServer";
	String UPDATE_ADVANCE_CONFIG ="updateAdvanceConfig";
	String UPDATE_SYSTEM_LOG_CONFIG ="updateSystemLogConfig";
	String UPDATE_STATISTIC_CONFIG ="updateStatisticConfig";
	String UPDATE_SERVER_INSTANCE ="updateServerInstance";
	String COPY_SERVER_INSTANCE_CONFIG ="copyInstanceConfig";
	String INIT_DELETE_SERVER_INSTANCE="initdeleteServerInstance";
	String DELETE_SERVER_INSTANCE="deleteServerInstance";
	String UPDATE_SNMP_ALERT_STATUS="updateSnmpStatus";
	String UPDATE_FILE_STATE_DB="updateFileState";
	String UPDATE_WEB_SERVICE_STATE="updateWebServiceState";
	String UPDATE_REST_WEB_SERVICE_STATE="updateRestWebServiceState";
	String GET_SERVR_INSTANCE_LIST="getServerInstanceList";
	String UPDATE_SERVER_INSTANCE_STATUS = "updateServerInstaneStatus";
	String CHECK_PORT_AVAILIBILITY = "checkPortAvailibility";
	//MEDSUP 2202 - Support for to get Count for MIS missing sequence entries 
	String GET_DYNAMIC_REPORT_COUNT = "getDynamicReportCount";
	
	
	String GET_SERVICE_LIST="getServiceList";
	String UPDATE_INSTANCE_STATISTIC="updateInstanceStatistic";
	String GET_SERVICE_LIST_SUMMARY="getSummaryServiceList";
	String GET_SERVER_DETAIL_LIST_FOR_FILE_BASED_SERVICE="getServerDetailListForFileBasedService";
	String GET_SERVER_DETAIL_LIST_FOR_PACKET_BASED_SERVICE="getServerDetailListForPacketBasedService";
	
	String GET_AGENT_LIST_SUMMARY="getSummaryAgentList";
	
	String INIT_SERVICE_MANAGER="initServiceManager";
	String GET_SERVICE_INSTANCE_LIST="getServiceInstanceList";
	String INIT_SELECT_SERVER_INSTANCE="selectServerInstance";
	
	String INIT_PARSER_CONFIG_MANAGER="initParserConfigManager";
	String INIT_ADD_SERVICE="initAddService";
	String ADD_SERVICE="addService";

	String INIT_UPDATE_SERVICE="initUpdateService";
	String INIT_UPDATE_COLLECTION_SERVICE="initUpdateCollectionService";
	String INIT_COLLECTION_SERVICE_MANAGER = "initCollectionServiceManager";
	String INIT_COLLECTION_SERVICE_CONFIGURATION = "initCollectionServiceConfiguration";
	String GET_COLLECTION_AGENT_LIST = "getCollectionAgentList";
	String GET_COLLECTION_DRIVER_LIST="getCollectionDriverList";
	String ADD_COLLECTION_DRIVER="addCollectionDriver";
	
	String INIT_UPDATE_PARSING_SERVICE_CONFIGURATION="initUpdateParsingServiceConfiguration";
	String UPDATE_PARSING_SERVICE_CONFIGURATION="updateParsingServiceConfiguration";
	String GET_ATTRIBUTE_LIST="getAttributeList";
	String INIT_ADD_PARSER_ATTRIBUTE="initAddParserAttribute";
	String INIT_PARSING_SERVICE_MANAGER = "initParsingServiceManager";
	String GET_PARSING_AGENT_LIST = "getParsingAgentList";
	String GET_PARSING_PLUG_IN_LIST_BY_ID = "getParsingPluginListById";
	
	String INIT_DRIVER_CONFIGURATION="initDriverConfig";

	String INIT_FTP_PATH_CONFIGURATION="initFtpPathConfig";

	String GET_ALL_PARSING_PLUGIN_LIST="getAllParsingPluginList";
	
	String INIT_LICENSE_AGREEMENT = "licenseAgreement";
	
	String REDIRECT_LICENSE_AGREEMENT = "redirectToLicenseAgreement";
	
	String INIT_LICENSE_ACTIVATION = "licenseActivation";
	
	String LICENSE_ACTIVATION_REDIRECT = "redirectToLicenseActivation";

	String INIT_ABOUT_US="aboutUs";

	String LICENSE_MANAGER = "licenseMgmt";
	
	String INIT_AGGREGATION_SERVICE_MANAGER="initAggregationServiceManager";
	String INIT_AGGREGATION_PATHLIST_CONFIGURATION="initAggregationPathlistConfiguration";
	String INIT_AGGREGATION_SERVICE_CONFIGURATION="initAggregationServiceConfiguration";
	String INIT_AGGREGATION_DEFINITION_CONFIGURATION = "initAggregationDefinitionConfiguration";
	String GET_AGGREGATION_SERVICE_COUNTER_STATUS="getAggregationServiceCounterStatus";
	String LOAD_AGGREGATION_DEFINITION_DATA = "loadAggregationDefinitionData";
	String CREATE_AGGREGATION_DEFINITION_LIST = "createAggregationDefinitionList";
	String UPDATE_AGGREGATION_SERVICE_CONFIGURATION="updateAggregationServiceConfiguration";
	String UPDATE_AGGREGATION_DEFINITION_LIST = "updateAggregationDefinitionList";	
	String CREATE_AGGREGATION_SERVICE_PATH_LIST = "createAggregationServicePathlist";
	String UPDATE_AGGREGATION_SERVICE_PATH_LIST = "updateAggregationServicePathlist";
	String DELETE_AGGREGATION_PATHLIST = "deleteAggregationPathList";
	
	String UPDATE_DISTRIBUTION_SERVICE_CONFIGURATION="updateDistributionServiceConfiguration";
	String INIT_DISTRIBUTION_SERVICE_CONFIGURATION="initDistributionServiceConfiguration";
	String INIT_DISTRIBUTION_SERVICE_MANAGER="initDistributionSummary";
	String INIT_DISTRIBUTION_DRIVER_MANAGER="initDistributionDriverManager";
	
	String GET_DISTRIBUTION_DRIVER_LIST="getDistributionDriverList";
	String GET_DISTRIBUTION_DRIVER_PLUGIN_LIST="getDistributionDriverPluginList";
	String INIT_DITRIBUTION_PLUGIN_MANAGER="initDistributionPluginManager";
	
	String INIT_PROCESSING_SERVICE_MANAGER="initProcessingServiceManager";
	String UPDATE_PROCESSING_SERVICE_CONFIGURATION="updateProcessingServiceConfiguration";
	String INIT_UPDATE_PROCESSING_SERVICE_CONFIGURATION="initUpdateProcessingServiceConfiguration";
	String INIT_UPDATE_PROCESSING_PATHLIST_CONFIGURATION="initUpdtProcessingPathlistConfiguration";
	String CREATE_PROCESSING_SERVICE_PATH_LIST = "createProcessingServicePathlist";
	String UPDATE_PROCESSING_SERVICE_PATH_LIST = "updateProcessingServicePathlist";
	String DELETE_PROCESSING_PATHLIST = "deleteProcessingPathList";
	
	String GET_PROCESSING_POLICY_PATH_LIST = "getProcessingPolicyPathList";
	String GET_PROCESSING_POLICY_LIST = "getProcessingPolicy";
	
	String INIT_CONFIGURATION_MANAGER="initConfigurationManager";
	String INIT_KAFKA_CONFIGURATION_MANAGER="initKafkaConfigurationManager";
	
	String INIT_MIS_REPORT_MANAGER="initMisReportManager";
	
	String MIS_GET_SERVER_SUMMARY_LIST_FOR_PACKET_BASED_SERVICE="misGetServerSummaryListForPacketBasedService";
	
	String MIS_GET_SERVER_SUMMARY_LIST_FOR_FILE_BASED_SERVICE="misGetServerSummaryListForFileBasedService";
	
	String DOWNLOAD_MIS_REPORT="downloadMISReport";
	
	String DOWNLOAD_DYNAMIC_REPORT="downloadDynamicReport";
	
	String DOWNLOAD_MIS_REPORT_IN_DETAIL="downloadMISReportInDetail";
	
	String GET_SERVER_SUMMARY_LIST_FOR_DAILY="getServerSummaryListForDaily";
	
	String MIS_GET_SERVER_LIST_FOR_SERVICE="misGetServerListForService";
	
	String UPDATE_CONFIGURATION_MANAGER="updateConfigurationManager";
	
	String INIT_PRODUCT_CONFIGURATION="initProductConfiguration";
	
	String INIT_BUSINESS_POLICY_MGMT="initBusinessPolicyManagement";
	String GET_POLICY_RULE_LIST="getPolicyRuleList";
	String GET_POLICY_RULE_ACTION_LIST="getPolicyRuleActionList";
	String ADD_POLICY_RULE_ACTION="addPolicyRuleAction";
	String DELETE_POLICY_RULE_ACTION="deletePolicyRuleAction";
	String INIT_POLICY_RULE_MANAGER="initPolicyRuleManager";

	String INIT_POLICY_RULE_LIST_ACTION_MANAGER="initPolicyRuleListActionManager";
	String INIT_POLICY_RULE_ACTION_MANAGER="initPolicyRuleActionManager";

	String GET_POLICY_RULE_CONDITION_LIST="getPolicyRuleConditionList";
	String INIT_POLICY_RULE_CONDITION_MANAGER="initPolicyRuleConditionManager";
	String ADD_POLICY_RULE_CONDITION="addPolicyRuleCondition";
	String DELETE_POLICY_RULE_CONDITION="deletePolicyRuleCondition";
	String VALIDATE_CONDITION_EXPRESSION= "validateConditionExpression";
	String VALIDATE_ACTION_EXPRESSION= "validateActionExpression";
	String GET_RULE_GROUP_LIST="getRuleGroupList";
	String GET_POLICY_LIST="getPolicyList";
	String INIT_CREATE_POLICY="initCreatepolicy";
	String INIT_CREATE_RULE_GROUP="initCreateRuleGroup";
	String GET_POLICY_RULE_LIST_BY_RULE_GROUP_ID="getPolicyRuleListByRuleGroupId";
	String GET_POLICY_RULE_GRID_BY_RULE_GROUP_ID="getPolicyRuleGridByRuleGroupId";
	String GET_RULE_GROUP_LIST_BY_POLICY_ID="getRuleGroupListByPolicyId";
	String CREATE_RULE_GROUP="createRuleGroup";
	String CREATE_POLICY="createPolicy";
	String UPDATE_POLICY="updatePolicy";
	String UPDATE_RULE_GROUP="updateRuleGroup";

	String INIT_DASHBOARD_MANAGER = "initDashboardManager";
	String DASHBOARD_REDIRECT_VIEW = "initDashboardManager";
	String INIT_WORKFLOW_MANAGER = "initWorkFlowManager";
	
	String LOAD_SERVER_INSTANCE_STATUS="loadServerInstanceStatus";
	String LOAD_SERVICE_STATUS="loadServiceStatus";
	
	String UPDATE_COLLECTION_SERVICE_CONFIGURATION="updateCollectionServiceConfiguration";
	String INIT_COLLECTION_DRIVER_MANAGER="initCollectionDriverManager";
	String CREATE_COLLECTION_DRIVER="createCollectionDriver";
	String UPDATE_FTP_COLLECTION_DRIVER="updateFTPCollectionDriver";
	String UPDATE_SFTP_COLLECTION_DRIVER="updateSFTPCollectionDriver";
	String UPDATE_LOCAL_COLLECTION_DRIVER="updateLocalCollectionDriver";
	
	
	String UPDATE_COLLECTION_DRIVER_STATUS="updateCollectionDriverStatus";
	
	String GET_COLLECTION_SERVICE_COUNTER_STATUS="getCollectionServiceCounterStatus";
	String RESET_COLLECTION_SERVICE_COUNTER_STATUS="resetCollectionServiceCounterStatus";
	String UPDATE_FTP_COLLECTION_DRIVER_CONFIGURATION="updateCollectionDriverConfiguration";
	String UPDATE_SFTP_COLLECTION_DRIVER_CONFIGURATION="updateSFTPCollectionDriverConfiguration";
	String TEST_FTP_SFTP_CONNECTION_FOR_COLLECTION="testFtpSftpConnectionForCollection";
	String UPDATE_LOCAL_COLLECTION_DRIVER_CONFIGURATION="updateLocalCollectionDriverConfiguration";
	String INIT_PATHLIST_CONFIGURATION="initPathListConfiguration";
	String CREATE_COLLECTIONDRIVER_PATHLIST="createCollectionDriverPathList";
	String UPDATE_COLLECTIONDRIVER_PATHLIST="updateCollectionDriverPathList";
	String INIT_FTP_CONFIGURATION="initFtpConfig";
	
	String DELETE_SERVICE_INSTANCE="deleteServiceInstance";
	String IMPORT_SERVICE_INSTANCE_CONFIG="importServiceInstanceConfig";
	String EXPORT_SERVICE_INSTANCE_CONFIG="exportServiceInstanceConfig";
	String SYNC_SERVICE_INSTANCE="syncServiceInstance";
	String SYNC_PUBLISH_SERVICE_INSTANCE="syncPublishServiceInstance";
	String START_SERVICE_INSTANCE="startServiceInstance";
	String STOP_SERVICE_INSTANCE="stopServiceInstance";
	String VALIDATE_ADDTIONAL_COLLECTIONPATHLIST_PARAM="validateAddtionalCollectionPathListParam";
	
	String DELETE_COLLECTION_DRIVER ="deleteCollectionDriver";
	
	String UPDATE_FTP_COLLECTION_DRIVER_ORDER="updateFtpCollectionDriverOrder";
	String DELETE_COLLECTION_DRIVER_PATHLIST="deleteCollectionDriverPathlist";
	
	String INIT_NETFLOW_COLLECTION_SERVICE_MANAGER = "initNetflowCollectionServiceManager";
	String GET_NETFLOW_COLLECTION_CLIENT_LIST = "getNetflowCollectionClientList";
	
	String INIT_UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION="initUpdtnNetflowCollectionServiceConfiguration";
	String UPDATE_NETFLOWCOLLECTION_SERVICE_CONFIGURATION="updtnNetflowCollectionServiceConfiguration";
	String UPDATE_NETFLOWBINARY_COLLECTION_SERVICE_CONFIGURATION="updtnNetflowBinaryCollectionServiceConfiguration";
	String UPDATE_SYSLOG_COLLECTION_SERVICE_CONFIGURATION="updtnSyslogCollectionServiceConfiguration";
	String UPDATE_MQTT_COLLECTION_SERVICE_CONFIGURATION="updtnMqttCollectionServiceConfiguration";
	String UPDATE_COAP_COLLECTION_SERVICE_CONFIGURATION="updtnCoAPCollectionServiceConfiguration";
	String UPDATE_HTTP2_COLLECTION_SERVICE_CONFIGURATION="updtnHttp2CollectionServiceConfiguration";
	String UPDATE_GTPPRIME_COLLECTION_SERVICE_CONFIGURATION="updtnGtpprimeCollectionServiceConfiguration";
	
	String GET_NETFLOW_CLIENT_FOR_SERVICE="getNetflowClientForService";
	String UPDATE_NETFLOW_COLLECTION_CLIENT="updateNetflowCollectionClient";
	String CREATE_NETFLOW_COLLECTION_CLIENT="createNetflowCollectionClient";
	String DELETE_NETFLOW_CLIENT ="deleteNetflowClient";
	String UPDATE_NETFLOW_CLIENT_STATUS ="updtClientStatus";
	
	String STAFF_AUDIT ="staffAudit";
	String VIEW_STAFF_AUDIT_DETAILS ="viewStaffAuditDetails";
	String GET_AUDIT_ENTITY="getAuditEntity";
	
	String GET_STAFF_AUDIT_LIST="getStaffAuditList";
	
	String INIT_IPLOG_PARSING_SERVICE_MANAGER = "initIplogParsingServiceManager";
	String INIT_UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION="initUpdtnIplogParsingServiceConfiguration";
	String UPDATE_IPLOG_PARSING_SERVICE_CONFIGURATION="updtnIplogParsingServiceConfiguration";
	String INIT_UPDATE_PARSING_HASH_CONFIGURATION="initUpdtParsingHashConfiguration";
	String INIT_UPDATE_IPLOG_PARSING_PATHLIST_CONFIGURATION="initUpdtIplogParsingPathlistConfiguration";
	String IMPORT_TEMPLATE_FILE="importTemplateFile";
	String GET_PARTITION_PARAM_LIST="getPartitionParamList";
	String UPDATE_PARSING_HASH_CONFIGURATION="updtHashConfiguration";
	
	String INIT_NATFLOW_PARSER_MANAGER="initNatflowParser";
	String INIT_NETFLOW_PARSER_ATTRIBUTE="initNetflowParserAttribute";
	String UPLOAD_ATTRIBUTE_FILE="uploadAttributeFile";
	String GET_DEVICE_CONFIG = "getDeviceConfig";
	String GET_ATTRIBUTE_BY_DEVICE_CONFIG = "getAttributeByDeviceConfig";
	String UPDATE_NATFLOW_PARSER_CONFIGURATION="updateNatflowParserConfiguration";
	String UPDATE_NATFLOW_ASN_PARSER_CONFIGURATION="updateNatflowASNParserConfiguration";
	
	String UPDATE_DEFAULT_DEVICE_CONFIGURATION = "updateDefaultDeviceConfiguration";
	
	String CREATE_DEVICE_CONFIGURATION = "createDeviceAndDeviceConfiguration";

	String VALIDATE_PARSER_ATTRIBUTE_PARAM="validateParserAttributeParameters";
	
	String VALIDATE_PARTITION_PARAM="validatePartitionParam";
	
	String CREATE_IPLOG_PARSING_PATHLIST="createIplogParsingPathList";
	String UPDATE_IPLOG_PARSING_PATHLIST="updateIplogParsingPathList";
	
	String GET_SERVICE_PARSER_LIST = "getServerParserList";
	String GET_SERVICE_HASH_CONFIG_LIST = "getServiceHashConfigList";
	String INIT_UPDATE_PARSING_PATHLIST_CONFIGURATION="initUpdtParsingPathlistConfiguration";
	
	

	String INIT_PARSER_CONFIGURATION = "initParserConfiguration";
	String INIT_REGEX_PARSER_CONFIG="initRegExParserConfig";
	String INIT_REGEX_PARSER_ATTRIBUTE="initRegExParserAttribute";
	String GENERATE_REGEX_PATTERN_ATTRIBUTE="generateRegExPatternToken";
	String ADD_REGEX_ATTR_BASIC_DETAIL="addRegExAttrBasicDetail";

	String GET_DEVICE_MAPPING_LIST = "getDeviceAndMappingList";
	String INIT_DEVICE_MANAGER = "initDeviceManager";
	String GET_VENDOR_LIST_BY_DEVICE_TYPE = "getVendorListByDeviceType";
	String GET_DEVICE_LIST_BY_VENDOR = "getDeviceListByVendor";
	String GET_DEVICE_LIST_BY_DECODETYPE = "getDeviceListByDecodeType";
	String GET_DEVICE_DETAILS = "getDeviceDetails";
	String GET_ATTRIBUTE_LIST_BY_MAPPING_ID= "getAttributeListByMappingId";
	String GET_MAPPING_ASSOCIATION_DETAILS= "getMappingAssociationDetails";
	String GET_DEVICE_BY_ID = "getDeviceById";
	String DELETE_DEVICES_MAPPINGS = "deleteDevicesAndMappings";
	String DELETE_MAPPINGS = "deleteMappings";
	String CREATE_DEVICE = "createDevice";
	
	String CREATE_PARSING_PATHLIST="createParsingPathList";
	String UPDATE_PARSING_PATHLIST="updateParsingPathList";
	
	String CREATE_PARSER="createParser";
	String UPDATE_PARSER="updateParser";
	String DELETE_PARSER="deleteParser";
	String CLONE_PARSER="cloneParser";	
	
	String GET_MAPPING_LIST_BY_DEVICE= "getMappingListByDevice";
	
	String GET_MAPPING_LIST_BY_DEVICE_TYPE = "getMappingListByDeviceType";
	
	String GET_MAPPING_DETAILS_ID= "getMappingDetailsById";
	String ADD_REGEX_PATTERN_AND_ATTRIBUTE="addRegExPatternAndAttribute";
	
	String GET_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="getAttributeGridListByMappingId";
	
	String GET_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getAttributeGridListByGroupId";
	
	String GET_PAGE_CONFIG_LIST_BY_GROUP_ID="getPageConfigListByGroupId";
	String DELETE_GROUP_ATTRIBUTE_WITH_HIERARCHY="deleteGroupWithHierarchyByGroupId";
	String REORDER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="reorderAttributeGridListByMappingId";

	String ADD_EDIT_PARSER_ATTRIBUTE="addEditParserAttributeDetails";
	String ADD_EDIT_ASCII_PARSER_ATTRIBUTE="addEditAsciiParserAttributeDetails";
	String ADD_EDIT_NATFLOW_PARSER_ATTRIBUTE="addEditNatFlowParserAttributeDetails";
	
	// saumil.vachheta // start // 01 March 2022 
	String ADD_EDIT_DETAIL_LOCAL_PARSER_ATTRIBUTE="addEditDetailLocalParserAttributeDetails";
	// saumil.vachheta // end // 01 March 2022 

	
	String ADD_EDIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE="addEditVarLengthAsciiParserAttributeDetails";
	
	String ADD_EDIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE="addEditVarLengthBinaryParserAttributeDetails";
	
	String ADD_EDIT_XML_PARSER_ATTRIBUTE="addEditXMLParserAttributeDetails";
	
	String ADD_EDIT_ASN1_PARSER_ATTRIBUTE = "addEditAsn1ParserAttributeDetails";
	
	String ADD_EDIT_HTML_PARSER_ATTRIBUTE = "addEditHtmlAttributeDetails";
	
	String ADD_EDIT_XLS_PARSER_ATTRIBUTE = "addEditXlsAttributeDetails";
	
	String ADD_EDIT_JSON_PARSER_ATTRIBUTE="addEditJsonParserAttributeDetails";
	
	String ADD_EDIT_MTSIEMENS_BINARY_PARSER_ATTRIBUTE="addEditMTSiemensBinaryParserAttributeDetails";
	
	String GET_ATTRIBUTE_BY_ID= "getAttributeById";
	String DELETE_ATTRIBTE = "deleteAttribute";
	String DELETE_PAGE = "deletePage";
	String DELETE_PARSING_PATHLIST="deleteParsingPathList";
	String DELETE_IPLOG_PARSING_PATHLIST="deleteIpLogParsingPathList";
	
	String CREATE_UPDATE_MAPPING_DETAILS = "createOrUpdateMappingDetails";
	
	String GET_ALL_DEVICE_TYPE_LIST= "getAllDeviceTypeList";

	String INIT_SNMP_CONFIG="initSnmpConfig";
	String CREATE_SNMP_SERVERLIST= "createSnmpServerList";
	String UPDATE_SNMP_SERVERLIST= "updateSnmpServerList";
	
	String DELETE_REGEX_PATTERN_ATTRIBUTE= "deleteRegExPatternAttribute";
	String UPDATE_REGEX_PATTERN_ATTRIBUTE="updateRegExParserAttribute";
	String VALIDATE_REGEX_PATTERN_ATTRIBUTE="validateRegExParserAttribute";
	String UPDATE_REGEX_PARSER_CONFIGURATION="updateRegExParserConfiguration";
	String DOWNLOAD_REGEX_SAMPLE_DATA_FILE="downloadRegExSampleDataFile";
	String UPDATE_REGEX_PATTERN_DETAIL="updateRegExPatternDetail";
	String DELETE_REGEX_PATTERN="deleteRegExPattern";
	
	String CREATE_DISTRIBUTION_DRIVER = "createDistributionDriver";
	String UPDATE_FTP_DISTRIBUTION_DRIVER = "updateFTPDistributionDriver";
	String UPDATE_SFTP_DISTRIBUTION_DRIVER = "updateSFTPDistributionDriver";
	String UPDATE_LOCAL_DISTRIBUTION_DRIVER = "updateLocalDistributionDriver";
	
	String UPDATE_DISTRIBUTION_DRIVER_STATUS = "updateDistributionDriverStatus";
	String DELETE_DISTRIBUTION_DRIVER ="deleteDistributionDriver";
	String UPDATE_DISTRIBUTION_DRIVER_ORDER="updateDistributionDriverOrder";
	String INIT_DISTRIBUTION_DRIVER_CONFIGURATION="distributionDriverConfiguration";
	String TEST_FTP_SFTP_CONNECTION_FOR_DISTRIBUTION="testFtpSftpConnectionForDistribution";
	String UPDATE_FTP_DISTRIBUTION_DRIVER_CONFIGURATION="updateFTPDistributionDriverConfiguration";
	String UPDATE_SFTP_DISTRIBUTION_DRIVER_CONFIGURATION="updateSFTPDistributionDriverConfiguration";
	String UPDATE_LOCAL_DISTRIBUTION_DRIVER_CONFIGURATION="updateLocalDistributionDriverConfiguration";
	String INIT_DISTRIBUTION_DRIVER_PATHLIST_MANAGER = "initDistributionDriverPathlistManager";
	
	String CREATE_DISTRIBUTION_DRIVER_PATH_LIST = "createDistributionDriverPathlist";
	String UPDATE_DISTRIBUTION_DRIVER_PATH_LIST = "updateDistributionDriverPathlist";

	String INIT_ASCII_PARSER_CONFIG="initAsciiParserConfig";
	String INIT_ASCII_PARSER_ATTRIBUTE="initAsciiParserAttribute";
	String UPDATE_ASCII_PARSER_CONFIGURATION="updateAsciiParserConfiguration";
	String UPDATE_ASCII_PARSER_MAPPING="updateAsciiParserMapping";

	// saumil.vachheta start
	String INIT_DETAIL_LOCAL_PARSER_CONFIG="initDetailLocalParserConfig";
	String INIT_DETAIL_LOCAL_PARSER_ATTRIBUTE="initDetailLocalParserAttribute";
	String UPDATE_DETAIL_LOCAL_PARSER_CONFIGURATION="updateDetailLocalParserConfiguration";
	String UPDATE_DETAIL_LOCAL_PARSER_MAPPING="updateDetailLocalParserMapping";
	// saumil.vachheta end

	
	String INIT_MTSIEMENS_PARSER_CONFIG="initMtsiemensParserConfig";
	String INIT_MTSIEMENS_PARSER_ATTRIBUTE="initMtsiemensParserAttribute";
	String UPDATE_MTSIEMENS_PARSER_CONFIGURATION="updateMtsiemensParserConfiguration";
	String UPDATE_MTSIEMENS_PARSER_MAPPING="updateMtsiemensParserMapping";

	String INIT_VAR_LENGTH_ASCII_PARSER_CONFIG="initVarLengthAsciiParserConfig";
	String INIT_VAR_LENGTH_ASCII_PARSER_ATTRIBUTE="initVarLengthAsciiParserAttribute";
	String UPDATE_VAR_LENGTH_ASCII_PARSER_CONFIGURATION="updateVarLengthAsciiParserConfiguration";
	String UPDATE_VAR_LENGTH_ASCII_PARSER_MAPPING="updateVarLengthAsciiParserMapping";
	String UPLOAD_DATA_DEFINITION_FILE="uploadDataDefinitionFile";
	String GET_DATA_DEFINITION_FILE_NAME="getDataDefinitionFileName";
	
	String INIT_VAR_LENGTH_BINARY_PARSER_CONFIG="initVarLengthBinaryParserConfig";
	String INIT_VAR_LENGTH_BINARY_PARSER_ATTRIBUTE="initVarLengthBinaryParserAttribute";
	String UPDATE_VAR_LENGTH_BINARY_PARSER_CONFIGURATION="updateVarLengthBinaryParserConfiguration";
	String UPDATE_VAR_LENGTH_BINARY_PARSER_MAPPING="updateVarLengthBinaryParserMapping";
	
	
	String INIT_ASN1_PARSER_CONFIG="initAsn1ParserConfig";
	String INIT_ASN1_PARSER_ATTRIBUTE="initAsn1ParserAttribute";
	String UPDATE_ASN1_PARSER_CONFIGURATION="updateAsn1ParserConfiguration";
	String UPDATE_ASN1_PARSER_MAPPING="updateAsn1ParserMapping";
	

	String INIT_XML_PARSER_CONFIG="initXMLParserConfig";
	String INIT_XML_PARSER_ATTRIBUTE="initXMLParserAttribute";
	String UPDATE_XML_PARSER_CONFIGURATION="updateXMLParserConfiguration";
	String UPDATE_XML_PARSER_MAPPING="updateXMLParserMapping";
	
	String INIT_HTML_PARSER_CONFIG="initHtmlParserConfig";
	String INIT_HTML_PARSER_ATTRIBUTE="initHtmlParserAttribute";
	String INIT_HTML_PARSER_GROUP_ATTRIBUTE="initHtmlParserGroupAttribute";
	String UPDATE_HTML_PARSER_CONFIGURATION="updateHtmlParserConfiguration";
	String UPDATE_HTML_PARSER_MAPPING="updateHtmlParserMapping";
	String UPDATE_HTML_PARSER_GROUP_MAPPING="updateHtmlParserGroupMapping";
	
	String INIT_XLS_PARSER_CONFIG="initXlsParserConfig";
	String INIT_XLS_PARSER_ATTRIBUTE="initXlsParserAttribute";
	String INIT_XLS_PARSER_GROUP_ATTRIBUTE="initXlsParserGroupAttribute";
	String UPDATE_XLS_PARSER_CONFIGURATION="updateXlsParserConfiguration";
	String UPDATE_XLS_PARSER_MAPPING="updateXlsParserMapping";
	String UPDATE_XLS_PARSER_GROUP_MAPPING="updateXlsParserGroupMapping";
	
	String INIT_JSON_PARSER_CONFIG="initJsonParserConfig";
	String INIT_JSON_PARSER_ATTRIBUTE="initJsonParserAttribute";
	String UPDATE_JSON_PARSER_CONFIGURATION="updateJsonParserConfiguration";
	String UPDATE_JSON_PARSER_MAPPING="updateJsonParserMapping";

		
	String GET_SNMP_SERVERLIST="getSnmpServerList";
	String UPDATE_SNMP_SERVER_STATUS="updateSnmpServerStatus";
	
	
	String CREATE_COMPOSER_PLUGIN = "createComposerPlugin";
	String UPDATE_COMPOSER_PLUGIN = "updateComposerPlugin";
	String DELETE_COMPOSER_PLUGIN = "deleteComposerPlugin";
	
	String CREATE_CHAR_RENAME_PARAMS = "createPluginCharRenameParams";
	String UPDATE_CHAR_RENAME_PARAMS = "updatePluginCharRenameParams";
	String DELETE_CHAR_RENAME_PARAMS = "deletePluginCharRenameParams";
	
	String CREATE_COLLECTION_SERVICE_CHAR_RENAME_PARAMS = "createCollectionCharRenameParams";
	String UPDATE_COLLECTION_SERVICE_CHAR_RENAME_PARAMS = "updateCollectionCharRenameParams";
	
	String GET_CHAR_RENAME_BY_ID = "getCharRenameOperationById";
	String GET_COLLECTION_CHAR_RENAME_BY_PATHID = "getCollectionCharRenameOperationById";

	String DELETE_SNMP_SERVER="deleteSnmpServer";

	String INIT_COMPOSER_CONFIGURATION="initComposerConfiguration";
	String INIT_ASCII_COMPOSER_MANGER="initAsciiComposerManager";
	String INIT_ASCII_COMPOSER_ATTRIBUTE="initAsciiComposerAttribute";
	String GET_COMPOSER_MAPPING_LIST_BY_DEVICE="getComposerMappingListByDevice";
	String CREATE_UPDATE_COMPOSER_MAPPING_DETAIL="createUpdateComposerMappingDetail";
	String UPDATE_ASCII_COMPOSER_CONFIGURATION="updateAsciiComposerConfiguration";
	String GET_COMPOSER_MAPPING_DETAILS_ID="getComposerMappingDetailsById";
	String GET_COMPOSER_MAPPING_ASSOCIATION_DETAILS="getComposerMappingAssociationDetail";
	String UPDATE_ASCII_COMPOSER_MAPPING="updateAsciiComposerMapping";
	String ADD_EDIT_ASCII_COMPOSER_ATTRIBUTE="addEditAsciiComposerAttribute";
	String GET_COMPOSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="getComposerAttrListByMappingId";
	String DELETE_COMPOSER_ATTRIBTE = "deleteComposerAttribute";
	
	// saumil.vachheta
	String INIT_DETAIL_LOCAL_COMPOSER_MANAGER="initDetailLocalComposerManager";
	String INIT_DETAIL_LOCAL_COMPOSER_ATTRIBUTE="initDetailLocalComposerAttribute";
	String UPDATE_DETAIL_LOCAL_COMPOSER_MAPPING="updateDetailLocalComposerMapping";
	String UPDATE_DETAIL_LOCAL_COMPOSER_CONFIGURATION="updateDetailLocalComposerConfiguration";
	// saumil.vachheta

	
	String INIT_ASN1_COMPOSER_MANGER="initAsn1ComposerManager";
	String INIT_ASN1_COMPOSER_ATTRIBUTE="initAsn1ComposerAttribute";
	String UPDATE_ASN1_COMPOSER_CONFIGURATION="updateAsn1ComposerConfiguration";
	String UPDATE_ASN1_COMPOSER_MAPPING="updateAsn1ComposerMapping";
	String ADD_EDIT_ASN1_COMPOSER_ATTRIBUTE="addEditAsn1ComposerAttribute";
	
	String INIT_TAP_COMPOSER_MANGER="initTapComposerManager";
	String INIT_TAP_COMPOSER_ATTRIBUTE="initTapComposerAttribute";
	String UPDATE_TAP_COMPOSER_CONFIGURATION="updateTapComposerConfiguration";
	String UPDATE_TAP_COMPOSER_MAPPING="updateTapComposerMapping";
	String ADD_EDIT_TAP_COMPOSER_ATTRIBUTE="addEditTapComposerAttribute";
	
	String INIT_RAP_COMPOSER_MANGER="initRapComposerManager";
	String INIT_RAP_COMPOSER_ATTRIBUTE="initRapComposerAttribute";
	String UPDATE_RAP_COMPOSER_CONFIGURATION="updateRapComposerConfiguration";
	String UPDATE_RAP_COMPOSER_MAPPING="updateRapComposerMapping";
	String ADD_EDIT_RAP_COMPOSER_ATTRIBUTE="addEditRapComposerAttribute";
	
	String INIT_NRTRDE_COMPOSER_MANGER="initNrtrdeComposerManager";
	String INIT_NRTRDE_COMPOSER_ATTRIBUTE="initNrtrdeComposerAttribute";
	String UPDATE_NRTRDE_COMPOSER_CONFIGURATION="updateNrtrdeComposerConfiguration";
	String UPDATE_NRTRDE_COMPOSER_MAPPING="updateNrtrdeComposerMapping";
	String ADD_EDIT_NRTRDE_COMPOSER_ATTRIBUTE="addEditNrtrdeComposerAttribute";

	String DELETE_DISTRIBUTION_PATHLIST = "deleteDistributionDriverPathList";
	String DELETE_DISTRIBUTION_PLUGIN = "deleteDistributionPlugin";
	String CREATE_SNMP_CLIENT= "createSnmpClient";
	String ADD_ALERTS_TO_CLIENT= "addAlertstoClient";
	String UPDATE_ALERTS_TO_CLIENT= "updateAlertstoClient";
	String GET_SNMP_CLIENTLIST="getSnmpClientList";
	String UPDATE_SNMP_CLIENT= "updateSnmpClient";
	String ADD_ALERTS_TO_UPDATE_CLIENT="addAlertstoUpdateClient";
	
	String GET_SNMP_ALERTLIST="getSnmpAlertList";
	String GET_SNMP_ALERTLIST_SEARCH="getSnmpAlertListSearch";
	String GET_SNMP_ALERTLIST_SERVICETHRESHOLD="getSnmpAlertListServiceThreshold";
	String UPDATE_SNMP_ALERT_SERVICETHRESHOLD="updateSnmpAlertServiceThreshold";
	String UPDATE_SNMP_CLIENT_STATUS="updateSnmpClientStatus";
	String DELETE_SNMP_CLIENT="deleteSnmpClient";
	String GET_CONFIGURED_ALERT_LIST="getConfiguredAlertList";
	String UPDATE_SERVICE_THRESHOLD="updateServiceThreshold";
	String UPDATE_SNMP_ALERT= "updateSnmpAlert";

	String ACTIVATE_TRIAL_LICENSE="activateTrialLicense";

	String GET_DEFAULT_PRODUCT_CONFIGURATION="getDefaultProductConfiguration";
	String GET_TRIAL_LICENSE="redirectToLogIn";


	String GET_CUSTOM_PRODUCT_CONFIGURATION="getCustomProductConfiguration";
	String UPDATE_PRODUCT_CONFIGURATION="updateProductconfiguration";
	String RESET_TO_DEFAULT_PRODUCT_CONFIGURATION="resetProductConfiguration";
	String GET_SERVICE_LIST_BY_SERVER_TYPE="getServiceTypeDetail";
	
	String DOWNLOAD_LICENSE_FORM = "downloadLicenseForm" ;

	String INIT_PROFILE_CONFIGURATION = "initProfileConfiguration";
	String GET_PROFILE_CONFIGURATION="getProfileConfiguration";
	String UPDATE_PROFILE_CONFIGURATION="updateProfileConfiguration";
	
	String ACTIVATE_FULL_LICENSE =  "activateFullLicense";
	String UPDATE_SERVICE_ENABLE_STATUS =  "updateServiceEnableStatus";
	String UPGRADE_ENGINE_DEFAULT_LICENSE = "upgradeEngineDefaultLicense";
	
	String DOWNLOAD_ENGINE_LICENSE_FORM = "downloadEngineLicense";
	String ACTIVATE_ENGINE_FULL_LICENSE =  "activateEngineFullLicense";
	String VIEW_SERVER_INSTANCE_LICENSE = "displayServerInstanceLicenseDetails";

	String INIT_SYSTEM_AGENT_CONFIG="initSystemAgentConfig";
	String SPECIFIC_SYSTEM_AGENT_CONFIG="specificSystemAgentConfig";

	String CREATE_DATASOURCE_CONFIGURATION="createDataSourceConfiguration";
	String UPDATE_DATASOURCE_CONFIGURATION="updateDataSourceConfiguration";
	String DELETE_DATASOURCE_CONFIGURATION="deleteDataSourceConfiguration";
	String TEST_DATASOURCE_CONFIGURATION="testDataSourceConfiguration";

	String CREATE_KAFKA_DATASOURCE_CONFIGURATION="createKafkaDataSourceConfiguration";
	String UPDATE_KAFKA_DATASOURCE_CONFIGURATION="updateKafkaDataSourceConfiguration";
	String DELETE_KAFKA_DATASOURCE_CONFIGURATION="deleteKafkaDataSourceConfiguration";
	String GET_KAFKA_DATASOURCE_ASSOCIATED_CLIENT_LIST="getKafkaDataSourceAssociatedClientList";
	
	String UPDATE_PACKET_STATASTIC_AGENT= "updatePacketStatasticAgent";
	
	String GET_AGENT_SERVICELIST="getAgentServicelist";
	String LOAD_AGENT_INFORMATION="loadAgentInformation";

	String UPDATE_PACKET_STATASTIC_AGENT_STATUS="updatePacketStatasticAgentStatus";		

	String UPDATE_AGENT_STATUS="updateAgentStatus";
	
	String EDIT_CUSTOMER_LOGO="editCustomerLogo";
	String CHANGE_STAFF_PROFILE_PIC="changeStaffProfilePic";
	String GET_INSTANCE_LICENSE_DETAILS = "getServerInstanceLicenseDetails";
	String COMPONENT_TYPE="componentType";
	
	String GET_INSTANCE_LIST_BY_SERVER_TYPE = "getAllInstancesByServerType";
	String GET_INSTANCE_LIST_BY_SERVER_TYPE_FOR_CREATE = "getAllInstancesByServerTypeForCreate";
	String FETCH_SERVICELIST_BY_SI="fetchServiceListBySI";


	String GET_POLICY_GROUP_LIST="getPolicyGroupList";
	String GET_GROUP_LIST = "getGroupList";
	String REMOVE_POLICY = "removePolicy";

	String GET_SERVICE_LIST_FOR_FILERENAME_AGENT = "getServiceListForFileRenameAgent";
	String ADD_SERVICE_TO_FILERENAME_AGENT = "addServiceToFileRenamingAgent";
	String UPDATE_SERVICE_TO_FILERENAME_AGENT = "updateServiceToFileRenamingAgent";	
	String DELETE_SERVICE_FILE_RENAME_CONFIG = "deleteServiceFileRenameAgentConfig";
	
	String UPDATE_FILE_RENAME_AGENT_DETAILS = "updateFileRenameAgentDetails";
	String CREATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT = "createCharRenameParamsFileRenameAgent";
	String UPDATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT = "updateCharRenameOperationParamsForFileRenameAgent";
	String GET_CHAR_RENAME_BY_SERVICE_FILE_RENAME_AGENT_ID = "getAllRenameOperationsBySvcFileRenConfigId";	
	String DELETE_CHAR_RENAME_PARAMS_AGENT = "deleteCharRenameParamsFromAgent";


	String LOAD_DROP_DOWN_SERVICE_LIST = "loadDropDownServiceList";
	
	String INIT_CONSOLIDATION_MANAGER="initConsolidationManager";
	String INIT_CONSOLIDATION_SERVICE_CONFIGURATION = "initConsolidationServiceConfiguration";
	String INIT_UPDATE_CONSOLIDATION_CONFIGURATION="initUpdateConsolidationConfiguration";
	String INIT_CONSOLIDATION_DEFINITION = "initConsolidationDefinition";
	String INIT_CONSOLIDATION_SUMMARY="initConsolidationSummary";
	String INIT_CONSOLIDATION_PATHLIST= "initConsolidationPathlist";
	String GET_CONSOLIDATION_DEFINITION_LIST="getConsolidationDefinitionList";
	String CREATE_CONSOLIDATION_SERVICE_PATH_LIST = "createConsolidationServicePathlist";
	String UPDATE_CONSOLIDATION_SERVICE_PATH_LIST = "updateConsolidationServicePathlist";
	String DELETE_CONSOLIDATION_PATHLIST = "deleteConsolidationPathList";
	String ADD_CONSOLIDATION_MAPPING = "addConsolidationMapping";
	String UPDATE_CONSOLIDATION_MAPPING = "updateConsolidationMapping";
	String DELETE_CONSOLIDATION_MAPPING = "deleteConsolidationMapping";
	String CREATE_CONSOLIDATION_DEFINITION_LIST = "createConsolidationDefinitionList";
	String UPDATE_CONSOLIDATION_DEFINITION_LIST = "updateConsolidationDefinitionList";
	String DELETE_CONSOLIDATION_DEFINITION_LIST = "deleteConsolidationDefinitionList";
	String CREATE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST = "createConsolidationGroupAttributeList";
	String UPDATE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST = "updateConsolidationGroupAttributeList";
	String DELETE_CONSOLIDATION_GROUP_ATTRIBUTE_LIST = "deleteConsolidationGroupAttributeList";
	String CREATE_CONSOLIDATION_ATTRIBUTE_LIST = "createConsolidationAttributeList";
	String UPDATE_CONSOLIDATION_ATTRIBUTE_LIST = "updateConsolidationAttributeList";
	String DELETE_CONSOLIDATION_ATTRIBUTE_LIST = "deleteConsolidationAttributeList";
	String GET_CONSOLIDATION_DEFINITION = "getConsolidationDefinition";
	
	String UPDATE_CONSOLIDATION_SERVICE_CONFIGURATION="updateConsolidationServiceConfiguration";
	String INIT_MIGRATION="initMigration";
	String DO_MIGRATION="doMigration";
	String REPROCESS_MIGRATION="reProcessMigration";
	String SM_MIGRATION="serverManagerMigration";
	String GET_SERVER_LIST_BY_SERVER_TYPE = "getServerListByServerType";
	String SAVE_MIGRATION_TRACK_DETAIL = "saveMigrationTrackDetail";
	String GET_MIGRATION_TRACK_DETAIL_LIST = "getMigrationTrackDetailList";
	String UPDATE_FAILED_MIGRATION_STATUS = "updateFailedMigrationStatus";
	String FETCH_ALL_SERVICE_LIST_BY_PORT="fetchAllServiceListByPort";
	String VALIDATE_MIGRATIONDETAILS_AND_FETCH_ALL_SERVICE_LIST_BY_PORT="ValidateMigrationDetailsAndfetchAllServiceListByPort";
	
	String GET_MIGRATION_TRACK_DETAIL_BY_ID = "getMigrationTrackDetailById";
	String DELETE_MIGRATION_TRACK_DETAIL = "deleteMigrationTrackDetail";
	String IS_MIGRATION_IN_PROCESS = "isMigrationInProcess";

	String SERVER_MIGRATION_UTILITY_CHECK="serverMigrationUtilityCheck";
	String SERVERINSTANCE_MIGRATIONCHECK="serverInstanceMigrationCheck";
	String SERVERINSTANCE_ZIP_UPLOAD="serverInstancezipUpload";

	String REMOVE_RULE_GROUP = "removeRuleGroup";
	String INIT_CREATE_RULE = "initCreateRule";

	String CREATE_RULE = "createRule";
	String UPDATE_RULE = "updateRule";
	String REMOVE_RULE = "removeRule";
	String GET_POLICY_CONDITION_LIST_BY_RULE_ID = "getPolicyConditionListByRuleId";
	String GET_POLICY_ACTION_LIST_BY_RULE_ID = "getPolicyActionListByRuleId";


	String ADD_EDIT_FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE = "addEditFixedLengthASCIIAttributeByMappingId";
	
	String UPDATE_FIXED_LENGTH_ASCII_PARSER_MAPPING="updateFixedLengthASCIIParserMapping";

	String INIT_FIXED_LENGTH_ASCII_PARSER_CONFIG="initFixedLengthASCIIParserConfig";
	String INIT_FIXED_LENGTH_ASCII_PARSER_ATTRIBUTE="initFixedLengthASCIIParserAttribute";
	String UPDATE_FIXED_LENGTH_ASCII_PARSER_CONFIGURATION="updateFixedLengthASCIIParserConfiguration";

	
	String INIT_XML_COMPOSER_MANAGER="initXMLComposerManager";
	String INIT_XML_COMPOSER_ATTRIBUTE="initXMLComposerAttribute";
	String UPDATE_XML_COMPOSER_MAPPING="updateXMLComposerMapping";
	String UPDATE_XML_COMPOSER_CONFIGURATION="updateXMLComposerConfiguration";
	String ADD_EDIT_XML_COMPOSER_ATTRIBUTE="addEditXMLComposerAttribute";
	
	
	String UPDATE_DATABASE_DISTRIBUTION_DRIVER = "updateDatabaseDistributionDriver";
	String UPDATE_DATABASE_DISTRIBUTION_DRIVER_CONFIGURATION="updateDatabaseDistributionDriverConfiguration";
	String INIT_DISTRIBUTION_DRIVER_ATTRLIST_MANAGER = "initDistributionDriverAttrlistManager";
	String ADD_EDIT_DATABASE_DRIVER_ATTRIBUTE="addEditDatabaseDriverAttribute";
	String GET_DRIVER_ATTRIBUTE_GRID_LIST = "getDriverAttributeGridList";
	String DELETE_DATABASE_DRIVER_ATTRIBUTE = "deleteDatabaseDriverAttribute";
	
	String REORDER_COMPOSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID = "reorderComposerAttributeGridListByMappingId";
	
	String UPDATE_DATABASE_QUERY = "updateDatabaseQuery";
	String INIT_DATABASE_QUERY_LIST = "initDatabaseQueryList";
	String GET_DATABASE_QUERY_LIST = "getDatabaseQueryList";
	String CREATE_DATABASE_QUERY = "createDatabaseQuery";
	String INIT_DATABASE_QUERY_CONFIG = "initDatabaseQueryConfig";
	String DELETE_DATABASE_QUERY="deleteQuery";
	String CREATE_UPDATE_DATABASE_ACTIONS="createUpdateDatabaseActions";
	String CREATE_UPDATE_DATABASE_CONDITIONS = "createUpdateDatabaseConditions";
	String INIT_DATABASE_QUERY_CONDITIONS="initDatabaseQueryConditions";
	String INIT_DATABASE_QUERY_ACTIONS = "initDatabaseQueryActions";
	
	String CREATE_ERROR_REPROCESS_BATCH = "createErrorReprocessBatch";
	String CREATE_ARCHIVE_RESTORE_BATCH = "createArchiveRestoreBatch";
	
	String GET_BATCH_DETAILS = "getAllBatchDetails";
	
	String INIT_REPROCESSING_STATUS = "initReprocessingStatus";
	String INIT_REPROCESSING_DETAILS = "initReprocessingDetails";
	String INIT_AUTOREPROCESSING_DETAILS = "initAutoReprocessingDetails";
	String DELETE_FILE_REPROCESS_DETAILS = "deleteFileReprocessDetails";
	String GET_REPROCESS_FILE_LIST = "getReprocessFileList";
	String REPROCESS_FAILED_FILE_LIST = "reprocessFailedFileList";
	String GET_REPROCESS_FILES_FROM_SM = "getReprocessFilesFromSM";
	String GET_REPROCESS_FILE_DETAIL_FROM_ENGINE = "getReprocessFileDetailsFromEngine";
	String ADD_NEW_AUTO_ERROR_REPROCESS_CONFIG = "addNewAutoErrorReprocessConfig";
	String UPDATE_NEW_AUTO_ERROR_REPROCESS_CONFIG = "updateNewAutoErrorReprocessConfig";
	
	String GET_SERVICE_BY_TYPE = "getServiceByType";
	
	String INIT_FIXED_LENGTH_ASCII_COMPOSER_MANAGER = "initFixedLengthAsciiComposer";
	String UPDATE_FIXED_LENGTH_ASCII_COMPOSER_CONFIGURATION = "updateFixedLengthAsciiComposerConfiguration";
	String INIT_FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE = "initFixedLengthAsciiComposerAttribute";
	String ADD_EDIT_FIXED_LENGTH_ASCII_COMPOSER_ATTRIBUTE = "addEditFixedLengthAsciiComposerAttribute";
	String UPDATE_FIXED_LENGTH_ASCII_BASIC_DETAIL = "updateFixedLengthAsciiBasicDetail";
	
	String GET_ALL_RULE_LIST_BY_TYPE = "getAllRuleByTypeAndServiceId";
	
	String GET_PROCESSING_DETAILS_BY_RULE = "getProcessingServiceByRule";
	
	String GET_PROCESSING_ERROR_DETAILS = "getProcessingErrorDetails";
	
	String GET_FILE_RECORD_DETAILS = "getFileRecordDetails";

	String DOWNLOAD_PROCESSING_FILE = "downloadProcessingFile";
	
	String VIEW_ERROR_FILE_DETAILS = "viewErrorFileDetails";
	
	String UPLOAD_PROCESSING_FILE = "uploadProcessingFile";
	
	String REPROCESS_PROCESSING_FILES = "reporcessingProcessingFiles";
	
	String RESTORE_PROCESSING_FILES = "restoringProcessingFiles";
	
	String APPLY_RULE_TO_FILE = "applyRulesToFiles";
	
	String DELETE_PROCESSING_FILE_DETAILS = "deleteProcessingFiles";
	String REPROCESS_PROCESSING_FILE_RECORDS = "reprocessProcessingFileRecords";
	
	String INIT_RULE_DATA_CONFIG = "initRuleDataConfig";
	String INIT_DICTIOANRY_CONFIG="initDict";
	String CREATE_RULE_DATA_CONFIG = "createRuleDataConfig";
	String UPDATE_RULE_DATA_CONFIG = "updateRuleDataConfig";
	String INIT_RULE_TABLE_DATA_LIST = "initRuleTableList";
	String INIT_DICTIONARY_LIST="intDictionaryList";
	String INIT_RULE_FIELD_LIST = "initRuleFieldList";
	String DELETE_RULE_LOOKUP_TABLE = "deleteRuleLookupTable";
	String DOWNLOAD_SAMPLE_CSV_FILE = "downloadSampleFile";
	String UPLOAD_RULE_DATA = "uploadRuleData";
	String GENERATE_JSON_PARSER_ATTR_DATA="generateJsonParserAttrData";
	String UPLOAD_PARSER_ATTR_DATA="uploadParserAttrData";
	String UPLOAD_PARSER_DICTIONARY_DATA="uploadParserDictionaryData";
	String UPLOAD_COMPOSER_ATTR_DATA="uploadComposerAttrData";
	String UPLOAD_DISTRIBUTION_DB_DRIVER_ATTR_DATA="uploadDistributionDbDriverAttrData";        
	String DOWNLOAD_LOOKUP_DATA_FILE = "downloadRuleLookupFile";
	String DOWNLOAD_SAMPLE_ATTRIBUTE = "downloadSampleAttribute";
	String DOWNLOAD_SAMPLE_COMPOSER_ATTRIBUTE = "downloadSampleComposerAttribute";
	String DOWNLOAD_DISTRIBUTION_DB_DRIVER_ATTRIBUTE="downloadSampleDbDistributionDriverAttribute";
	
	String INIT_LOOKUP_DATA_CONFIG = "initLookupDataConfig";
	String INIT_LOOKUP_DATA_LIST = "initLookupDataList";
	String RULE_LOOKUP_FUNCTION = "ruleLookupFunction";
	String DELETE_RULE_LOOKUP_TABLE_RECORDS = "deleteRuleLookupTableRecords";
	String CREATE_RULE_LOOKUP_RECORD = "createRuleLookupRecord";
	String UPDATE_RULE_LOOKUP_RECORD = "updateRuleLookupRecord";
	String INIT_AUTO_RELOAD_CACHE_CONFIG = "initAutoReloadCacheConfig";
	String INIT_AUTO_RELOAD_CACHE_CONFIG_LIST = "initAutoReloadCacheConfigList";
	String GET_RULE_LOOKUP_TABLE_LIST = "getRuleLookUpTableTableList"; 
	
	String GET_DS_LIST_BY_TYPE="getDSListByType";
	
	String CRAETE_RULE_ACTION_CONDITION = "createRuleActionCondition";
	String UPDATE_RULE_ACTION_CONDITION = "updateRuleActionCondition";
	String DELETE_RULE_ACTION_CONDITION = "deleteRuleActionCondition";

	String GET_ALL_ACTION_CONDITION_LIST= "getAllActionCondtionListDetails";
	
	String UPDATE_RULE_CONDITION_APPLICATION_ORDER = "updateRuleConditionApplicationOrder";
	
	String REVERT_MODIFIED_FILES = "revertModifiedFiles";
	String REVERT_MULTIPLE_MODIFIED_FILES = "revertMultipleModifiedFiles";
	
	String IMPORT_POLICY_CONFIG = "importPolicyConfig";
	String EXPORT_POLICY_CONFIG = "exportPolicyConfig";
	String REPROCESS_MODIFIED_FILES = "reprocessModifiedFiles";
	String GET_PARSER_LIST="getParserList";
	
	String NFV_ACTIVATE_ENGINE_FULL_LICENSE =  "api/nfv/activateEngineFullLicense";
	String NFV_ADD_SERVER = "api/nfv/addServer";
	String NFV_ADD_SERVER_INSTANCE = "api/nfv/addServerInstance";
	String NFV_SYNC_SERVER_INSTANCE = "api/nfv/syncServerInstance";
	String NFV_CHECK_STATUS="api/nfv/checkStatus";
	String NFV_COPY_SERVER_INSTANCE = "api/nfv/copyServerInstance";
	String NFV_IMPORT_SERVER_INSTANCE = "api/nfv/importServerInstance";
	String NFV_DELETE_SERVER_INSTANCE="api/nfv/deleteServerInstance";
	String NFV_DELETE_SERVER="api/nfv/deleteServer";
	String NFV_DELETE_SERVER_INSTANCE_ONLY_IN_SM="api/nfv/deleteServerInstanceOnlyInSM";
	String NFV_RESTART_SERVER_INSTANCE = "api/nfv/restartServerInstance";
	String NFV_START_SERVER_INSTANCE = "api/nfv/startServerInstance";
	String NFV_STOP_SERVER_INSTANCE = "api/nfv/stopServerInstance";
	String NFV_ADD_CLIENT = "api/nfv/addClient";
	String NFV_LOGIN = "api/nfv/login";

	String INIT_ROAMING_CONFIGURATION = "initRoamingConfiguration";
	String MODIFY_HOST_CONFIGURATION="modifyHostConfiguration";
	String MODIFY_ROAMING_PARAMETER="modifyRoamingParameter";
	String MODIFY_FILE_SEQUENCE_MANAGEMENT="modifyFileSequenceManagement";
	String VIEW_FILE_SEQUENCE_MANAGEMENT="viewFileSequenceManagement";
	String MODIFY_TEST_SIM_MANAGEMENT="modifyTestSimManagement";
	String VIEW_TEST_SIM_MANAGEMENT="viewTestSimManagement";
	String MODIFY_FILE_MANAGEMENT = "modifyFileManagement";
	String VIEW_FILE_MANAGEMENT = "viewFileManagement";
	
	String INIT_RAP_PARSER_CONFIG="initRapParserConfig";
	String INIT_RAP_PARSER_ATTRIBUTE="initRapParserAttribute";
	String UPDATE_RAP_PARSER_CONFIGURATION ="updateRapsParserConfiguration";
	String UPDATE_RAP_PARSER_MAPPING ="updateRapParserMapping";
	String ADD_EDIT_RAP_PARSER_ATTRIBUTE="addEditRapParserAttributeDetails";
	
	String INIT_TAP_PARSER_CONFIG="initTapParserConfig";
	String INIT_TAP_PARSER_ATTRIBUTE="initTapParserAttribute";
	String UPDATE_TAP_PARSER_CONFIGURATION ="updateTapsParserConfiguration";
	String UPDATE_TAP_PARSER_MAPPING ="updateTapParserMapping";
	String ADD_EDIT_TAP_PARSER_ATTRIBUTE="addEditTapParserAttributeDetails";
	
	String INIT_NRTRDE_PARSER_CONFIG="initNRTRDEParserConfig";
	String INIT_NRTRDE_PARSER_ATTRIBUTE="initNrtrdeParserAttribute";
	String UPDATE_NRTRDE_PARSER_CONFIGURATION ="updateNrtrdesParserConfiguration";
	String UPDATE_NRTRDE_PARSER_MAPPING ="updateNrtrdeParserMapping";
	String ADD_EDIT_NRTRDE_PARSER_ATTRIBUTE="addEditNrtrdeParserAttributeDetails";
	String DOWNLOAD_EXCEL_PARSER_COMPOSER_ATTRIBUTE_LIST = "downloadExcelForParserComposerAttributeList";
	
	String GET_GROUP_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="getGroupAttrListByMappingId";
	String GET_PARSER_GROUP_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="getParserGroupAttrListByMappingId";
	String DELETE_GROUP_ATTRIBTE = "deleteGroupAttribute";
	String ADD_EDIT_GROUP_ATTRIBUTE="addEditGroupAttribute";
	
	String GET_VIEW_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getViewGroupAttrListByGroupId";
	String GET_VIEW_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getViewAttrListByGroupId";
	
	String GET_UPDATE_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getUpdateGroupAttrListByGroupId";
	String GET_UPDATE_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getUpdateAttrListByGroupId";
	
	String GET_ADD_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getAddGroupAttrListByGroupId";
	String GET_ADD_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getAddAttrListByGroupId";
	
	String INIT_FIXED_LENGTH_BINARY_PARSER_CONFIG="initFixedLengthBinaryParserConfig";
	String INIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE="initFixedLengthBinaryParserAttribute";
	String UPDATE_FIXED_LENGTH_BINARY_PARSER_CONFIGURATION="updateFixedLengthBinaryParserConfiguration";
	String UPDATE_FIXED_LENGTH_BINARY_PARSER_MAPPING="updateFixedLengthBinaryParserMapping";
	String ADD_EDIT_FIXED_LENGTH_BINARY_PARSER_ATTRIBUTE = "addEditFixedLengthBinaryAttributeDetails";
	
	String INIT_PDF_PARSER_CONFIG="initPDFParserConfig";
	String INIT_PDF_PARSER_ATTRIBUTE="initPDFParserAttribute";
	String INIT_PDF_PARSER_GROUP_ATTRIBUTE="initPDFParserGroupAttribute";
	String UPDATE_PDF_PARSER_CONFIGURATION="updatePDFParserConfiguration";
	String UPDATE_PDF_PARSER_MAPPING="updatePDFParserMapping";
	String ADD_EDIT_PDF_PARSER_ATTRIBUTE = "addEditPDFAttributeDetails";
	String ADD_EDIT_PAGE_CONFIG_DETAILS = "addEditPageConfigurationDetails";
	String ADD_EDIT_PDF_PARSER_GROUP_ATTRIBUTE = "addEditPDFGroupAttributeDetails";
	String ADD_EDIT_PARSER_GROUP_BASIC_DETAILS_ATTRIBUTE = "addEditGroupAttributeBasicDetails";
	
	String GET_PARTNER_LIST_BY_NAME = "getPartnerListByName";

	
	String GET_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID="getParserGroupAttrListByMappingId";
	String ADD_EDIT_PARSER_GROUP_ATTRIBUTE="addEditParserGroupAttribute";
	String DELETE_GROUP_PARSER_ATTRIBTE = "deleteParserGroupAttribute";

	
	String GET_ADD_PARSER_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getAddParserGroupAttrListByGroupId";
	String GET_ADD_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getAddParserAttrListByGroupId";
	
	String GET_UPDATE_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getUpdateParserGroupAttrListByGroupId";
	String GET_UPDATE_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getParserUpdateAttrListByGroupId";
	
	String GET_VIEW_GROUP_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getViewParserGroupAttrListByGroupId";
	String GET_VIEW_PARSER_ATTRIBUTE_GRID_LIST_BY_GROUP_ID="getViewParserAttrListByGroupId";

	String INIT_DIAMETER_COLLECTION_SERVICE_MANAGER = "initDiameterCollectionServiceManager";
	String INIT_UPDATE_DIAMETER_COLLECTION_SERVICE_CONFIGURATION="initUpdtDiameterCollectionServiceConfiguration";
	String UPDATE_DIAMETER_SERVICE_CONFIGURATION="updtDiameterCollectionServiceConfiguration";
	
	String GET_DIAMETER_PEER_FOR_SERVICE="getDiameterPeerForService";
	String CREATE_DIAMETER_COLLECTION_PEER="createDiameterCollectionPeer";
	String UPDATE_DIAMETER_COLLECTION_PEER="updateDiameterCollectionPeer";
	String DELETE_DIAMETER_COLLECTION_PEER="deleteDiameterCollectionPeer";
	String UPDATE_DIAMETER_PEER_STATUS="updateDiameterPeerStatus";
	String GET_DIAMETER_COLLECTION_PEER_LIST = "getDiameterCollectionPeerList";
	
	String CREATE_AVP ="createAVP";
	String UPDATE_AVP ="updateAVP";
	String DELETE_AVP ="deleteAVP";
	
	String INIT_DYNAMIC_REPORT_CONFIG = "initDynamicReportConfig";
	String INIT_DYNAMIC_REPORT_LIST = "initDynamicReportList";
	String INIT_DYNAMIC_REPORT_HEADER_LIST = "initDynamicReportHeaderList";
	String INIT_DYNAMIC_REPORT_RECORD_LIST = "initDynamicReportRecordList";
	
	String INIT_TRIGGER_CONFIG = "initTriggerConfig";
	String CREATE_TRIGGER_CONFIG = "createTriggerConfig";
	String INIT_TRIGGER_DATA_LIST = "initTriggerTableList";
	String UPDATE_TRIGGER_CONFIG="updateTriggerConfig";
	String DELETE_TRIGGER_CONFIG = "deleteTriggerConfig";
	String GET_TRIGGER_BY_ID = "getTriggerById";

	String INIT_AUTO_UPLOAD_CONFIG = "initAutoUploadConfig";
	String INIT_AUTO_UPLOAD_CONFIG_LIST = "initAutoUploadConfigList";
	
	String DELETE_AUTO_REPROCESS_FILE="deleteAutoErrorReprocessFile";
	String GET_AUTO_ERROR_REPROCESS_BY_ID="getAutoErrorReprocessById";
	
	String INIT_VIEW_AUTO_UPLOAD_RELOAD_STATUS = "viewAutoUploadReloadStatus";
	String VIEW_AUTO_JOB_LIST = "viewAutoJobList";
	
	String UPDATE_RADIUS_SERVICE_CONFIGURATION = "UPDATE_RADIUS_SERVICE_CONFIGURATION";
	
	String UPLOAD_RULELOOKUP_DATA_FILE= "uploadRuleLookupDataFile";
	
	String RESET_PASSWORD_LINK = "resetPasswordLink";//NOSONAR
	
	String EDIT_EMAIL_FOOTER_IMAGE="editEmailFooterImage";
	
	String SERVICES_TPS_DETAILS = "servicesTpsDetails";
	
	String NATFLOW_PROXY_CLIENT_LIST_BY_SERVICEID = "getNatFlowClientListByServiceId";
	String ADD_PROXY_CLIENT = "addProxyClientParams";
	String DELETE_PROXY_CLIENT = "deleteProxyConfigParams";
	String UPDATE_PROXY_CLIENT = "updateProxyParams";
	
	String CONTAINER_DETAILS = "containerDetails";
	String ACTIVATE_LICENSE_CONTAINER = "activateLicenseforContainer";
	String INIT_HTML_PARSER_GROUP_ATTRIBUTE_GET ="initHtmlParserGroupAttributeGet";
    String GET_PARSER_GROUP_ATTRIBUTE_GRID_LIST="getParserGroupAttributeGridList";
    
    String DOWNLOAD_DICTIONARY_CONFIG_XML="downloadDictionaryConfigData";
    String UPLOAD_DICTIONARY_FILE_DATA="uploadDictionaryDataFile";
    
    String ADD_NEW_FILE_TO_DICTIONARY="addNewFileToDictionary";
    String UPLOAD_DICTIONARY_FILE_DATA_SYNC="uploadDictionaryDataFileSync";
    String ADD_NEW_FILE_TO_DICTIONARY_SYNC="addNewFileToDictionarySync";
   
    String UPLOAD_KEY_STORE_FILE="uploadKeyStoreFile";
    
    String GET_UNIFIED_FIELD_LIST="getUnifiedFieldList";
    String GET_FIXED_UNIFIED_FIELD="getFixedUnifiedField";
    String GET_All_UNIFIED_FIELD="getAllUnifiedField";
    String MIGRATEALLSERVERCONFIG="migrateAllServerConfig";
    
    String GET_LICENSE_UTILIZATION_DATA="getLicenseUtilizationData";
    String INIT_CIRCLE_CONFIG_MANAGER="initCircleConfigurationManager";    
    String GET_CIRCLE_LIST="getCircleList";
    String CREATE_CIRCLE="createCircle";
    String UPLOAD_LICENSE_KEY = "uploadLicenseKey";
    String UPDATE_CIRCLE="updateCircle";
    String DELETE_CIRCLE="deleteCircle";
    String LICENSE_INFO="getLicenseInfo";
    String GET_MAPPED_DEVICES_INFO="getAllMappedDevicesInfo";
}
