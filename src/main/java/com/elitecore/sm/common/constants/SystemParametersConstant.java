/**
 * 
 */
package com.elitecore.sm.common.constants;


/**
 * @author Sunil Gulabani
 * Apr 15, 2015
 */
public final class SystemParametersConstant {
	
	public static final String MAX_WRONG_PASSWORD_ATTEMPTS = "MAX_WRONG_PASSWORD_ATTEMPTS"; //NOSONAR
	
	public static final String RELEASE_LOCK_ON_WRONG_ATTEMPTS_IN_MINUTES = "RELEASE_LOCK_ON_WRONG_ATTEMPTS_IN_MINUTES" ;
	
	public static final String LOCK_ADMIN_ON_WRONG_ATTEMPTS = "LOCK_ADMIN_ON_WRONG_ATTEMPTS" ;

	public static final String SESSION_TIMEOUT_IN_MINUTES = "SESSION_TIMEOUT_IN_MINUTES" ;

	public static final String PASSWORD_EXPIRY_DAYS = "PASSWORD_EXPIRY_DAYS" ; //NOSONAR
	public static final String NEW_PASSWORD_LAST_N_CHECK = "NEW_PASSWORD_LAST_N_CHECK" ; //NOSONAR
	
	public static final String ENCRYPTION_POLICY_PLAIN="PLAIN";
	public static final String ENCRYPTION_POLICY_AES="AES";
	public static final String ENCRYPTION_POLICY_MD5="MD5";
	
	

	public static final String ACCESSGROUP_NAME 	=	 					"AccessGroup.name" ;
	public static final String ACCESSGROUP_DESCRIPTION = 					"AccessGroup.description";
	public static final String ACCESSGROUP_REASON_TO_CHANGE = 				"AccessGroup.reason.to.change" ;
	
	public static final String STAFF_USERNAME =								"Staff.username" ;
	public static final String STAFF_FIRSTNAME =					 		"Staff.firstName" ;
	public static final String STAFF_LASTNAME = 							"Staff.lastName" ;
	public static final String STAFF_MIDDLENAME = 							"Staff.middleName" ;
	public static final String STAFF_DOB = 									"Staff.birthDate" ;
	public static final String STAFF_ADDRESS = 								"Staff.address" ;
	public static final String STAFF_ADDRESS2 = 							"Staff.address2" ;
	public static final String STAFF_CITY = 								"Staff.city" ;
	public static final String STAFF_STATE = 								"Staff.state" ;
	public static final String STAFF_COUNTRY = 								"Staff.country" ;
	public static final String STAFF_PINCODE = 								"Staff.pincode" ;
	public static final String STAFF_MOBILENO = 							"Staff.mobileNo" ;
	public static final String STAFF_LANDLINENO = 							"Staff.landlineNo" ;
	public static final String STAFF_STAFFCODE = 							"Staff.staffCode" ;
	public static final String STAFF_REASON_FOR_CHANGE_REGEX = 				"STAFF_REASON_FOR_CHANGE_REGEX";
	
	public static final String SERVER_IPADDRESS_REGEX = 			 		"SERVER_IPADDRESS_REGEX";
	
	public static final String SERVER_NAME =					 			"Server.name" ;
	public static final String SERVER_SERVERID =					 			"Server.serverId" ;
	public static final String SERVER_GROUPSERVERID =					 	"Server.groupServerId" ;
	public static final String SERVER_DESCRIPTION = 			 			"Server.description" ;
	public static final String SERVER_UTILITYPORT = 			 			"Server.utilityPort";
	public static final String SERVER_IPADDRESS = 			 				"Server.ipAddress";
	
	public static final String SERVERINSTANCE_LOG_FILEPATH_REGEX = 			"SERVERINSTANCE_LOG_FILEPATH_REGEX" ;
	public static final String SERVERINSTANCE_FILESTORAGENAME_REGEX = 		"SERVERINSTANCE_FILESTORAGENAME_REGEX" ;
	
	public static final String SERVERINSTANCE_NAME = 						"ServerInstance.name" ;
	public static final String SERVERINSTANCE_DESCRIPTION = 				"ServerInstance.description" ;
	public static final String SERVERINSTANCE_PORT = 						"ServerInstance.port" ;
	public static final String SERVERINSTANCE_MINMEMORYALLOCATION = 		"ServerInstance.minMemoryAllocation" ;
	public static final String SERVERINSTANCE_MAXMEMORYALLOCATION = 		"ServerInstance.maxMemoryAllocation" ;
	public static final String SERVERINSTANCE_MAXCONNECTIONRETRY = 			"ServerInstance.maxConnectionRetry" ;
	
	public static final String SERVERINSTANCE_RETRYINTERVAL = 				"ServerInstance.retryInterval" ;
	public static final String SERVERINSTANCE_CONNECTIONTIMEOUT = 			"ServerInstance.connectionTimeout" ;
	public static final String SERVERINSTANCE_MINDISKSPACE = 				"ServerInstance.minDiskSpace" ;
	public static final String SERVERINSTANCE_SCRIPTNAME = 					"ServerInstance.scriptName" ;
	public static final String SERVERINSTANCE_LOGSDETAIL_LOGPATHLOCATION = 	"ServerInstance.logsDetail.logPathLocation" ;
	public static final String SERVERINSTANCE_FILESTORAGELOCATION = 		"ServerInstance.fileStorageLocation" ;
	public static final String SERVERINSTANCE_DATASOURCECONFIG_DSPOJO_NAME ="ServerInstance.datasourceConfig.dsPojo.name" ;
	public static final String SERVERINSTANCE_THRESHOLDTIMEINTERVAL = 		"ServerInstance.thresholdTimeInterval" ;
	public static final String SERVERINSTANCE_THRESHOLDMEMORY = 			"ServerInstance.thresholdMemory" ;
	public static final String SERVERINSTANCE_LOADAVERAGE = 				"ServerInstance.loadAverage" ;
	public static final String SERVERINSTANCE_LOGSDETAIL_MAXROLLINGUNIT =	"ServerInstance.logsDetail.maxRollingUnit" ;
	public static final String SERVERINSTANCE_LOGSDETAIL_ROLLINGVALUE = 	"ServerInstance.logsDetail.rollingValue" ;
	public static final String SERVERINSTANCE_MEDIATIONROOT = 	"ServerInstance.mediationRoot";
	public static final String SERVERINSTANCE_REPROCESSING_BACKUP_PATH = 	"ServerInstance.reprocessingBackupPath";
	public static final String DATE_FORMAT = "DATE_FORMAT" ;
	
	public static final String DATE_TIME_FORMAT = "DATE_TIME_FORMAT" ;

	public static final String TOTAL_ROWS_TO_DISPLAY_IN_GRID = "TOTOAL_ROWS_TO_DISPLAY_IN_GRID" ;

	public static final String IMAGE_CONTENT_TYPE="image/";
	public static final String JPG="jpg";
	public static final String JPEG="jpeg";
	public static final String PNG="png";
	public static final String GENERAL_PARAMETERS = "General Parameters";
	public static final String PASSWORD_PARAMETERS = "Password Policy"; //NOSONAR
	public static final String CUSTOMER_PARAMETERS = "Customer Details";
	public static final String CUSTOMER_LOGO_PARAMETERS = "Customer Logo Details";
	public static final String FILE_REPROCESSING_PARAMETERS = "File Reprocessing";
	public static final String SYSTEM_PARAM_VALUE_POOL="systemParamValuePool";
	public static final String EDIT_SYSTEM_PARAMETER = "editSystemParameter";
	public static final String PASSWORD_TYPE = "PASSWORD_TYPE"; //NOSONAR
	public static final String STAFF_PASSWORD = "Staff.password"; //NOSONAR
	public static final String PASSWORD_TYPE_REGEX = "PASSWORD_TYPE_REGEX"; //NOSONAR
	public static final String CUSTOMER_LOGO = "CUSTOMER_LOGO"; 
	public static final String CUSTOMER_LOGO_LARGE = "CUSTOMER_LOGO_LARGE";
	public static final String CUSTOMER_ADDRESS="CUSTOMER_ADDRESS";
	public static final String ROAMING_PARAMETERS = "Roaming Parameters";
	public static final String HOST_CONFIGURATION= "Host Configuration";
	public static final String RAP_TAP_CONFIGURATION="Rap Tap Configuration";
	public static final String FILE_SEQUENCE_MANAGEMNT = "File Sequence Management";
	public static final String TEST_SIM_MANAGEMENT = "Test Sim Management";
	public static final String EMAIL_PARAM = "Email Parameters";
	public static final String EDIT_EMAIL_PARAMETER = "editEmailParameter";
	public static final String LOGIN_PARAM = "Login Parameters";
	public static final String EDIT_LOGIN_PARAMETER = "editLoginParameter";
	public static final String SSO_PARAM = "Sso Parameters";
    public static final String EDIT_SSO_PARAMETER = "editSsoParameter";

	
	public static final String VIEW_SYSTEM_PARAM = "VIEW_SYSTEM_PARAM";
	public static final String EDIT_GEN_SYSTEM_PARAM = "EDIT_GEN_SYSTEM_PARAM"; //NOSONAR
	public static final String EDIT_PWD_SYSTEM_PARAM = "EDIT_PWD_SYSTEM_PARAM"; //NOSONAR
	public static final String EDIT_CUST_SYSTEM_PARAM = "EDIT_CUST_SYSTEM_PARAM";
	public static final String EDIT_CUST_LOGO_SYSTEM_PARAM = "EDIT_CUST_LOGO_SYSTEM_PARAM";
	public static final String EDIT_FILE_REPROCESSING_PARAM = "EDIT_FILE_REPROCESSING_PARAM";
	public static final String EDIT_ROAMING_PARAMETERS = "EDIT_ROAMING_PARAMETERS";
	public static final String EDIT_HOST_CONFIGURATION = "EDIT_HOST_CONFIGURATION";
	public static final String EDIT_RAP_TAP_CONFIGURATION="EDIT_RAP_TAP_CONFIGURATION";
	
	
	public static final String AUTO_SYNC_TIME = "AUTO_SYNC_TIME";
	public static final String LOCKBYSSO = "LOCKBYSSO" ;
	public static final String REST_API_CALL = "REST_API_CALL" ;
	public static final String RSA_CALL = "RSA_CALL" ;
	public static final String RSA_SERVER_URL = "RSA_SERVER_URL" ;
	public static final String CLIENT_IP = "CLIENT_IP" ;
	
	public static final String CUSTOMER_NAME = "CUSTOMER_NAME" ;
	public static final String CUSTOMER_CONTACT = "CUSTOMER_CONTACT" ;
	public static final String DEFAULT_ACCESS_GROUP = "DEFAULT_ACCESS_GROUP" ;
	public static final String GROUP_ROLE_ATTRIBUTE_NAME = "GROUP_ROLE_ATTRIBUTE_NAME" ;
	public static final String DEFAULT_SSO_ACCESS_GROUP = "DEFAULT_SSO_ACCESS_GROUP" ;
	public static final String LDAP_SECURITY_CERTIFICATE_LOCATION = "LDAP_SECURITY_CERTIFICATE_LOCATION" ;
	public static final String DOMAIN_NAME = "DOMAIN_NAME" ;
	public static final String SEARCH_PATTERN = "SEARCH_PATTERN" ;
	public static final String LDAP_SERVER_URL = "LDAP_SERVER_URL" ;
	public static final String LDAP_AUTH_ENABLE = "LDAP_AUTH_ENABLE" ;
	public static final String ROOT_DOMAIN_NAME = "ROOT_DOMAIN_NAME" ;
	public static final String MAIL_HOST_NAME = "MAIL_HOST_NAME" ;
	public static final String PASSWORD = "PASSWORD" ;//NOSONAR
	public static final String EMAIL_USER = "ROOT_DOMAIN_NAME" ;
	public static final String PORT = "ROOT_DOMAIN_NAME" ;
	
	public static final String CONTACT_PERSON = "CONTACT_PERSON" ;
	public static final String CONTACT_MOBILENO = "MobileNo" ;
	public static final String CONTACT_EMAILID = "EmailId" ;
	public static final String CUSTOMER_WEB_SITE = "CUSTOMER_WEB_SITE" ;
	public static final String IMAGE_MODEL="IMAGE_MODEL";
	public static final String IMAGE_MODEL_LARGE="IMAGE_MODEL_LARGE";
	
	public static final int DEFAULT_IMAGE_ID=1;
	public static final int DEFAULT_IMAGE_ID_LARGE=2;
	public static final String IMAGE_MODEL_STRING="IMAGE_MODEL_STRING";
	public static final String IMAGE_MODEL_STRING_LARGE="IMAGE_MODEL_STRING_LARGE";
	public static final String SYSTEM_PARAM_DB_WRAPPER="system_param_db_wrapper";
	public static final String CUSTOMER_SMALL_LOGO_DB="CUSTOMER_SMALL_LOGO_DB";
	public static final String CUSTOMER_LARGE_LOGO_DB="CUSTOMER_LARGE_LOGO_DB";
	public static final String DEFAULT_LOGO_SET="DEFAULT_LOGO_SET";
	public static final String NMS_LINK="NMS_LINK";

	public static final String PROFILE_IMAGE_SIZE = "PROFILE_IMAGE_SIZE" ;
	public static final String UPLOAD_FILE_SIZE = "UPLOAD_FILE_SIZE" ;
	public static final String SYSTEM_BACKUP_FILE_PATH = "SYSTEM_BACKUP_FILE_PATH" ;
	public static final String SYSTEM_BACKUP_FILE_PATH_VALUE = "$TOMCAT_HOME/backup" ;
	
	public static final String DATASOURCECONFIG_NAME_REGEX = "DataSourceConfig.name" ;
	public static final String DATASOURCECONFIG_CONNURL_REGEX = "DataSourceConfig.connURL" ;
	public static final String DATASOURCECONFIG_TYPE_REGEX = "DataSourceConfig.type" ;
	public static final String DATASOURCECONFIG_USERNAME_REGEX = "DataSourceConfig.username" ; //NOSONAR
	public static final String DATASOURCECONFIG_PASSWORD_REGEX = "DataSourceConfig.password" ; //NOSONAR
	public static final String DATASOURCECONFIG_MINPOOLSIZE_REGEX = "DataSourceConfig.minPoolSize" ;
	public static final String DATASOURCECONFIG_MAXPOOLSIZE_REGEX = "DataSourceConfig.maxPoolsize" ;
	public static final String DATASOURCECONFIG_FAIL_TIMEOUT = "DataSourceConfig.failTimeout" ;
	
	public static final String KAFKADATASOURCECONFIG_NAME_REGEX = "KafkaDataSourceConfig.name" ;
	public static final String KAFKADATASOURCECONFIG_KAFKASERVERPORT_REGEX = "KafkaDataSourceConfig.kafkaServerPort" ;
	public static final String KAFKADATASOURCECONFIG_MAXRETRYCOUNT_REGEX = "KafkaDataSourceConfig.maxRetryCount" ;
	public static final String KAFKADATASOURCECONFIG_MAXRESPONSEWAIT_REGEX = "KafkaDataSourceConfig.maxResponseWait" ;
	public static final String KAFKADATASOURCECONFIG_KAFKAPRODUCERRETRYCOUNT_REGEX = "KafkaDataSourceConfig.kafkaProducerRetryCount" ;
	public static final String KAFKADATASOURCECONFIG_KAFKAPRODUCERREQUESTTIMEOUT_REGEX = "KafkaDataSourceConfig.kafkaProducerRequestTimeout" ;
	public static final String KAFKADATASOURCECONFIG_KAFKAPRODUCERRETRYBACKOFF_REGEX = "KafkaDataSourceConfig.kafkaProducerRetryBackoff" ;
	public static final String KAFKADATASOURCECONFIG_KAFKAPRODUCERDELIVERYTIMEOUT_REGEX = "KafkaDataSourceConfig.kafkaProducerDeliveryTimeout" ;
	
	public static final String SERVICE_NAME ="Service.name" ;
	public static final String SERVICE_DESCRIPTION ="Service.description" ;
	public static final String SERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL ="Service.svcExecParams.executionInterval" ;
	public static final String SERVICE_SVCEXECPARAMS_QUEUESIZE ="Service.svcExecParams.queueSize" ;
	public static final String SERVICE_SVCEXECPARAMS_MINTHREAD ="Service.svcExecParams.minThread" ;
	public static final String SERVICE_SVCEXECPARAMS_MAXTHREAD ="Service.svcExecParams.maxThread" ;
	public static final String SERVICE_SVCEXECPARAMS_FILEBATCHSIZE ="Service.svcExecParams.fileBatchSize" ;
	public static final String SERVICE_SERVICESCHEDULINGPARAMS_TIME ="Service.serviceSchedulingParams.time" ;
	
	public static final String ALL_SERVER_TYPE_LIST="ALL_SERVER_TYPE_LIST";
	public static final String ACTIVE_SERVER_TYPE_LIST="ACTIVE_SERVER_TYPE_LIST";
	public static final String SERVICE_TYPE_LIST="SERVICE_TYPE_LIST";
	public static final String PARSER_PLUGIN_TYPE_LIST="PARSER_PLUGIN_TYPE_LIST";
	
	public static final String DISTRIBUTION_PLUGIN_TYPE_LIST="PLUGIN_TYPE_LIST";

	
	public static final String DRIVERS_NAME ="Drivers.name" ;
	public static final String DRIVERS_TIMEOUT ="Drivers.timeout" ;
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_IPADDRESSHOST="FTPCollectionDriver.ftpConnectionParams.iPAddressHost" ;
	public static final String FTPCONNECTIONPARAMS_IPADDRESSHOST="ftpConnectionParams.iPAddressHost";
	public static final String FTPCONNECTIONPARAMS_IPADDRESSHOST_INVALID="error.Server.ipAddress.invalid";
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PORT="FTPCollectionDriver.ftpConnectionParams.port" ;
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_USERNAME="FTPCollectionDriver.ftpConnectionParams.username" ;
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_TIMEOUT="FTPCollectionDriver.ftpConnectionParams.timeout" ;
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD="FTPCollectionDriver.ftpConnectionParams.password" ; //NOSONAR
	public static final String FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_FILESEPARATOR="FTPCollectionDriver.ftpConnectionParams.fileSeparator" ;
	
	public static final String COLLECTIONDRIVER_NOFILEALERT ="CollectionDriver.noFileAlert" ;
	public static final String DRIVERS_MINFILERANGE ="Drivers.minFileRange" ;
	public static final String DRIVERS_MAXFILERANGE ="Drivers.maxFileRange" ;
	public static final String DRIVERS_MAXRETRYCOUNT ="Drivers.maxRetrycount" ;
	public static final String DRIVERS_CONTROLFILELOCATION ="Drivers.controlFileLocation" ;
	public static final String DRIVERS_ROLLINGTIME ="Drivers.rollingTime" ;
	public static final String DRIVERS_ROLLINGSTARTTIME ="Drivers.rollingStartTime" ;
	public static final String DRIVERS_CONTROLFILENAME ="Drivers.controlFileName" ;
	public static final String DRIVERS_CONTROLFILEMINSEQ ="Drivers.controlFileMinSeq" ;
	public static final String DRIVERS_CONTROLFILEMAXSEQ ="Drivers.controlFileMaxSeq" ;
	public static final String DRIVERS_CONTROLFILEATTRIBUTES ="Drivers.controlFileAttributes";
	
	public static final String FTPCOLLECTIONDRIVER_MYFILEFETCHPARAMS_FILEFETCHINTERVALMIN="FTPCollectionDriver.myFileFetchParams.FileFetchIntervalMin" ;
	public static final String FTPCOLLECTIONDRIVER_MYFILEFETCHPARAMS_TIMEZONE="FTPCollectionDriver.myFileFetchParams.timeZone" ;
	
	public static final String COLLECTIONDRIVER_FILEGROUPINGPARAMETER_ARCHIVEPATH="CollectionDriver.fileGroupingParameter.archivePath" ;
	public static final String COLLECTIONDRIVER_FILEGROUPINGPARAMETER_DUPLICATEPATH="CollectionDriver.fileGroupingParameter.duplicateDirPath";

	public static final String COLLLECTION_CHAR_RENAME_SRCDATEFORMAT  ="CollectionCharRename.srcDateFormat" ;
	public static final String COLLECTION_DRIVER_TYPE_LIST="COLLECTION_DRIVER_TYPE_LIST";
	public static final String DISTRIBUTION_DRIVER_TYPE_LIST="DISTRIBUTION_DRIVER_TYPE_LIST";
	
	public static final String PATHLIST_NAME="PathList.name";
	public static final String PATHLIST_READFILEPATH="PathList.readFilePath";
	public static final String COMMONPATHLIST_READFILENAMEPREFIX="CommonPathList.readFilenamePrefix";
	public static final String COMMONPATHLIST_READFILENAMESUFFIX="CommonPathList.readFilenameSuffix";
	public static final String DISTRIBUTIONDRIVERPATHLIST_DBREADFILENAMEEXTRASUFFIX="DistributionDriverPathList.dbReadFileNameExtraSuffix";
	public static final String COMMONPATHLIST_READFILENAMECONTAINS="CommonPathList.readFilenameContains";
	public static final String COMMONPATHLIST_WRITEFILEPATH="CommonPathList.writeFilePath";
	public static final String COMMONPATHLIST_WRITEFILENAMEPREFIX="CommonPathList.writeFilenamePrefix";
	public static final String COLLECTIONDRIVERPATHLIST_MAXFILESCOUNTALERT="CollectionDriverPathList.maxFilesCountAlert";
	public static final String COLLECTIONDRIVERPATHLIST_REMOTEFILEACTIONVALUE="CollectionDriverPathList.remoteFileActionValue";
	public static final String COLLECTIONDRIVERPATHLIST_DATEFORMAT="CollectionDriverPathList.dateFormat";
	public static final String COLLECTIONDRIVERPATHLIST_STARTINDEX="CollectionDriverPathList.startIndex";
	public static final String COLLECTIONDRIVERPATHLIST_ENDINDEX="CollectionDriverPathList.endIndex";
	public static final String COLLECTIONDRIVERPATHLIST_SEQSTARTINDEX="CollectionDriverPathList.seqStartIndex";
	public static final String COLLECTIONDRIVERPATHLIST_SEQENDINDEX="CollectionDriverPathList.seqEndIndex";
	public static final String COLLECTIONDRIVERPATHLIST_MAXCOUNTERLIMIT="CollectionDriverPathList.maxCounterLimit";
	public static final String COLLECTIONDRIVERPATHLIST_REFERENCEDEVICE="CollectionDriverPathList.referenceDevice";
	public static final String COLLECTIONDRIVERPATHLIST_TIMEINTERVAL="CollectionDriverPathList.timeInterval";
	public static final String COLLECTIONDRIVERPATHLIST_VALIDFILETIMEINTERVAL="CollectionDriverPathList.validFileTimeInterval";
	public static final String COLLECTIONDRIVERPATHLIST_MINVALUE="CollectionDriverPathList.minValue";
	public static final String COLLECTIONDRIVERPATHLIST_MAXVALUE="CollectionDriverPathList.maxValue";
	public static final String COLLECTIONDRIVERPATHLIST_DEVICE="CollectionDriverPathList.device";
	public static final String COLLECTIONDRIVERPATHLIST_FILESIZECHECKMINVALUE="CollectionDriverPathList.fileSizeCheckMinValue";
	public static final String COLLECTIONDRIVERPATHLIST_FILESIZECHECKMAXVALUE="CollectionDriverPathList.fileSizeCheckMaxValue";
	
	public static final String SERVICE_OPTIONTEMPLATEID 			=	"Service.optionTemplateId" ;
	public static final String SERVICE_OPTIONTEMPLATEKEY 			=	"Service.optionTemplateKey" ;
	public static final String SERVICE_OPTIONTEMPLATEVALUE 			=	"Service.optionTemplateValue" ;
	public static final String SERVICE_OPTIONCOPYTOTEMPLATEID 		=	"Service.optionCopytoTemplateId" ;
	public static final String SERVICE_OPTIONCOPYTOFIELD 			=	"Service.optionCopyTofield" ;
	public static final String SERVICE_BINARYFILELOCATION 			=	"Service.binaryfileLocation" ;
	
	public static final String SERVICE_NETFLOWIP			=			"Service.netFlowIP" ;
	public static final String SERVICE_NETFLOWPORT 					=	"Service.netFlowPort" ;
	public static final String SERVICE_SKTRCVBUFFERSIZE 			=	"Service.sktRcvBufferSize" ;
	public static final String SERVICE_SKTSENDBUFFERSIZE 			=	"Service.sktSendBufferSize" ;
	public static final String SERVICE_NEWLINECHARAVAILABLE 		=	"Service.newLineCharAvailable" ;
	public static final String SERVICE_BULKWRITELIMIT 				=	"Service.bulkWriteLimit" ;
	public static final String SERVICE_MAXPKTSIZE 					=	"Service.maxPktSize" ;
	public static final String SERVICE_MAXWRITEBUFFERSIZE 			=	"Service.maxWriteBufferSize" ;
	public static final String SERVICE_PARALLELFILEWRITECOUNT 		=	"Service.parallelFileWriteCount" ;
	public static final String SERVICE_SNMPTIMEINTERVAL 			=	"Service.snmpTimeInterval" ;
	public static final String SERVICE_MAXIDELCOMMUTIME 			=	"Service.maxIdelCommuTime" ;
	public static final String SERVICE_MAXREADRATE 					=	"Service.maxReadRate" ;
	public static final String SERVICE_RECEIVERBUFFERSIZE 			=	"Service.receiverBufferSize" ;
	public static final String SERVICE_CONNECTMAXATTEMPT 			=	"Service.connectMaxAttempt" ;
	public static final String SERVICE_RECONNECTMAXATTEMPT			=	"Service.reconnectMaxAttempt" ;
	public static final String SERVICE_RECONNECTDELAY		    	=	"Service.reconnectDelay" ;

	public static final String NETFLOWCLIENT_NAME 					= 	"NetflowClient.name" ;
	public static final String NETFLOWCLIENT_TOPICNAME 				= 	"NetflowClient.topicName" ;
	public static final String NETFLOWCLIENT_CLIENTIPADDRESS 		= 	"NetflowClient.clientIpAddress" ;
	public static final String NETFLOWCLIENT_CLIENTPORT 			= 	"NetflowClient.clientPort" ;
	public static final String NETFLOWCLIENT_FILENAMEFORMAT 		= 	"NetflowClient.fileNameFormat" ;
	public static final String NETFLOWCLIENT_MINFILESEQVALUE 		= 	"NetflowClient.minFileSeqValue" ;
	public static final String NETFLOWCLIENT_MAXFILESEQVALUE 		= 	"NetflowClient.maxFileSeqValue" ;
	public static final String NETFLOWCLIENT_OUTFILELOCATION 		= 	"NetflowClient.outFileLocation" ;
	public static final String NETFLOWCLIENT_VOLLOGROLLINGUNIT 		= 	"NetflowClient.volLogRollingUnit" ;
	public static final String NETFLOWCLIENT_TIMELOGROLLINGUNIT		= 	"NetflowClient.timeLogRollingUnit" ;
	public static final String NETFLOWCLIENT_BKPBINARYFILELOCATION	= 	"NetflowClient.bkpBinaryfileLocation" ;
	public static final String NETFLOWCLIENT_ALERT_INTERVAL	= 	"NetflowClient.alertInterval" ;
	
	public static final String NETFLOWCLIENT_RESOURCESNAME 				= 	"NetflowClient.resourcesName" ;
	public static final String NETFLOWCLIENT_OBSERVERTIMEOUT 			= 	"NetflowClient.observerTimeout" ;
	public static final String NETFLOWCLIENT_REQUESTTIMEOUT				= 	"NetflowClient.requestTimeout" ;
	public static final String NETFLOWCLIENT_REQUESTRETRYCOUNT			= 	"NetflowClient.requestRetryCount" ;
	public static final String NETFLOWCLIENT_REQEXECUTIONINTERVAL		= 	"NetflowClient.reqExecutionInterval" ;
	public static final String NETFLOWCLIENT_REQEXECUTIONFREQ			= 	"NetflowClient.reqExecutionFreq" ;
	public static final String NETFLOWCLIENT_SECURITYIDENTITY			= 	"NetflowClient.securityIdentity" ;
	public static final String NETFLOWCLIENT_SECURITYKEY 				= 	"NetflowClient.securityKey" ;
	public static final String NETFLOWCLIENT_SECCERLOCATION 			= 	"NetflowClient.secCerLocation" ;
	public static final String NETFLOWCLIENT_SECCERPASSWD 				= 	"NetflowClient.secCerPasswd" ;
	public static final String NETFLOWCLIENT_PROXYRESOURCES 			= 	"NetflowClient.proxyResources" ;
	public static final String NETFLOWCLIENT_PROXYSERVERIP 				= 	"NetflowClient.proxyServerIp" ;
	public static final String NETFLOWCLIENT_PROXYSERVERPORT 			= 	"NetflowClient.proxyServerPort" ;
	

	
	public static final String LANGUAGE_PROP_LIST = "lang.props.values" ;
	
	public static final String AUDIT_ENTITY_LIST = "entityList" ;
	public static final String AUDIT_SUB_ENTITY_LIST = "subEntityList" ;
	public static final String AUDIT_ACTIVITY_LIST = "activityList" ;
	public static final String AUDIT_ACTIVITY_ALIAS_LIST = "activityAliasList" ;
	
	
	public static final String DEFAULT_ATTRIBUTE_LIST = "attributeList" ;
	public static final String PARSER_MAPPING_NAME ="ParserMapping.name" ;
	public static final String NATFLOW_PARSER_OPTION_TEMPLATE_ID ="NATFlowParserMapping.optionTemplateId" ;
	public static final String NATFLOW_PARSER_OPTION_TEMPLATE_KEY ="NATFlowParserMapping.optionTemplateKey" ;
	public static final String NATFLOW_PARSER_OPTION_TEMPLATE_VALUE ="NATFlowParserMapping.optionTemplateValue" ;
	public static final String NATFLOW_PARSER_OPTION_COPY_TO_TEMPLATE_ID ="NATFlowParserMapping.optionCopytoTemplateId" ;
	public static final String NATFLOW_PARSER_OPTION_COPY_TO_FIELD ="NATFlowParserMapping.optionCopyTofield" ;
	
	public static final String NATFLOW_PARSER_FILTER_PROTOCOL="NATFlowParserMapping.filterProtocol" ;
	public static final String NATFLOW_PARSER_FILTER_TRANSPORT ="NATFlowParserMapping.filterTransport" ;
	public static final String NATFLOW_PARSER_FILTER_PORT ="NATFlowParserMapping.filterPort" ;
	
	
	public static final String DEVICE_NAME="Device.name" ;
	public static final String DEVICE_DESCRIPTION="Device.description" ;
	public static final String DEVICE_DECODE_TYPE="Device.decodeType" ;
	public static final String DEVICE_CONFIG_NAME="DeviceConfiguration.name" ;

	public static final String DEVICE_TYPE_NAME="DeviceType.name" ;
	public static final String VENDOR_TYPE_NAME="VendorType.name" ;
	
	public static final String ATTR_SOURCE_FIELD="ParserAttribute.sourceField" ;
	public static final String ATTR_UNIFIED_FIELD="ParserAttribute.unifiedField" ;
	public static final String ATTR_DEFAULT_VALUE="ParserAttribute.defaultValue" ;
	public static final String ATTR_TRIM_CHARS="ParserAttribute.trimChars" ;
	public static final String ATTR_DESCRIPTION="ParserAttribute.description" ;
	public static final String ATTR_ASCII_SOURCE_FIELD="ParserAttribute.asciiSourceField" ;
	public static final String ATTR_ASCII_IP_PORT_SEPERATOR="AsciiParserAttribute.ipPortSeperator" ;
	public static final String ATTR_ASCII_PORT_UNIFIED_FIELD="AsciiParserAttribute.ipPortUnifiedfield" ;
	
	
	public static final String SERVICE_EQUALCHECKFIELD ="Service.equalCheckField" ;
	public static final String SERVICE_EQUALCHECKVALUE ="Service.equalCheckValue" ;
	public static final String SERVICE_RECORDBATCHSIZE ="Service.recordBatchSize" ;
	public static final String SERVICE_ERROR_PATH ="Service.errorPath" ;
	public static final String SERVICE_PURGEINTERVAL ="Service.purgeInterval" ;
	public static final String SERVICE_PURGEDELAYINTERVAL ="Service.purgeDelayInterval" ;
	public static final String SERVICE_OUTFILEHEADERS ="Service.outFileHeaders" ;
	public static final String SERVICE_FILESTATSLOC ="Service.fileStatsLoc" ;
	public static final String SERVICE_DESTPORTFIELD ="Service.destPortField" ;
	public static final String SERVICE_DESTPORTFILTER ="Service.destPortFilter" ;
	public static final String SERVICE_CREATERECDESTPATH ="Service.createRecDestPath" ;
	public static final String SERVICE_DELETERECDESTPATH ="Service.deleteRecDestPath" ;
	public static final String SERVICE_FILEGROUPPARAM_ARCHIVEPATH ="Service.fileGroupParam.archivePath" ;
	public static final String SERVICE_ERRORPATH = "Service.errorPath";
	public static final String SERVICE_DATEFIELDFORSUMMARY ="Service.dateFieldForSummary" ;
	
	public static final String PARTITIONPARAM_PARTITIONRANGE 		= 	"PartitionParam.partitionRange" ;
	
	public static final String PARSER_PARSINGPATHLIST_NAME 			= "Parser.parsingPathList.name";
	public static final String PARSER_PARSINGPATHLIST_READFILEPATH 	= "Parser.parsingPathList.readFilePath";
	public static final String PARSER_WRITEFILEPATH 				= "Parser.writeFilePath";
	public static final String PARSER_READFILENAMEPREFIX 			= "Parser.readFilenamePrefix" ;
	public static final String PARSER_READFILENAMESUFFIX 			= "Parser.readFilenameSuffix";
	public static final String PARSER_READFILENAMECONTAINS 			= "Parser.readFilenameContains";
	public static final String PARSER_READFILENAMEEXCLUDETYPES 		= "Parser.readFilenameExcludeTypes";
	public static final String PARSER_WRITEFILENAMEPREFIX 			= "Parser.writeFilenamePrefix";
	public static final String PARSER_NAME 							= "Parser.name";
	public static final String PARSER_MAXFILECOUNTALERT 				= "Parser.maxFileCountAlert";
	
	public static final String SERVICE_MINFILERANGE ="Service.minFileRange" ;
	public static final String MAXFILERANGE="maxFileRange";
	public static final String SERVICE_MAXFILERANGE ="Service.maxFileRange" ;
	public static final String SERVICE_NOFILEALERT ="Service.noFileAlert" ;
	public static final String SERVICE_DELIMITER = "Service.delimiter";
	
	public static final String PARSERMAPPING_SRCDATEFORMAT  ="ParserMapping.srcDateFormat" ;
	public static final String REGEXPARSERMAPPING_LOGPATTERNREGEX  ="RegExParserMapping.logPatternRegex" ;
	public static final String PATHLIST_ARCHIVEPATH ="PathList.archivePath" ;
	
	public static final String IPLOG_HASHBASEDCONF_PARTITIONRANGE="Iplog.HashBasedConf.partitionRange";
	
	public static final String REGEXPARSERATTRIBUTE_REGEX  ="RegExParserAttribute.regex" ;
	public static final String SNMPSERVERLIST_NAME ="SNMPServerConfig.name" ;
	public static final String SNMPSERVERLIST_IP ="SNMPServerConfig.hostIP" ;
	public static final String SNMPSERVERLIST_PORT ="SNMPServerConfig.port" ;
	public static final String SNMPSERVERLIST_OFFSET ="SNMPServerConfig.portOffset" ;
	public static final String SNMPSERVERLIST_PORTOROFFSETREQUIRED="SNMPserverlist.port.or.offset.required";
	
	public static final String DISTRIBUTION_PROCESS_RECORD_LIMIT = "DistributionService.processRecordLimit";
	public static final String DISTRIBUTION_TIMESTEN_DATASOURCE_NAME = "DistributionService.timestenDatasourceName";
	public static final String DISTRIBUTION_WRITE_RECORD_LIMIT = "DistributionService.writeRecordLimit";
	

	public static final String REGEXPATTERN_PATTERNREGEXNAME="RegExPattern.patternRegExName";
	public static final String REGEXPATTERN_PATTERNREGEX="RegExPattern.patternRegEx";
	public static final String REGEXPATTERN_PATTERNREGEXID="RegExPattern.patternRegExId";
	
	public static final String COMPOSER_PLUGIN_NAME = "Composer.name";
	public static final String COMPOSER_READ_FILE_PREFIX = "Composer.readFilenamePrefix";
	public static final String COMPOSER_READ_FILE_SUFFIX = "Composer.readFilenameSuffix";
	public static final String COMPOSER_READ_FILE_CONTAINS = "Composer.readFilenameContains";
	public static final String COMPOSER_READ_FILE_EXCLUDE_TYPES = "Composer.readFilenameExcludeTypes";
	public static final String COMPOSER_WRITE_FILE_PATH = "Composer.destPath";
	public static final String COMPOSER_WRITE_FILE_PREFIX = "Composer.writeFilenamePrefix";
	public static final String COMPOSER_FILE_BACKUP_PATH = "Composer.fileBackupPath";
	public static final String COMPOSER_FILE_EXTENSION = "Composer.fileExtension";
	
	public static final String CHAR_OPERATION_QUERY = "CharRenameOperation.query";
	public static final String CHAR_OPERATION_START_INDEX = "CharRenameOperation.startIndex";
	public static final String CHAR_OPERATION_END_INDEX = "CharRenameOperation.endIndex";
	public static final String CHAR_OPERATION_DEFAULT_VALUE = "CharRenameOperation.defaultValue";
	public static final String CHAR_OPERATION_LENGTH = "CharRenameOperation.length";

	public static final String ASCIIPARSERMAPPING_FIELDSEPARATOR="AsciiParserMapping.fieldSeparator";
	public static final String ASCIIPARSERMAPPING_FIND="AsciiParserMapping.find";
	public static final String ASCIIPARSERMAPPING_REPLACE="AsciiParserMapping.replace";
	public static final String ASCIIPARSERMAPPING_FILEFOOTERCONTAINS="AsciiParserMapping.fileFooterContains";
	public static final String ASCIIPARSERMAPPING_RECORDHEADERLENGTH="AsciiParserMapping.recordHeaderLength";
	
	public static final String PARSERATTRIBUTE_SOURCEFIELDFORMAT="ParserAttribute.sourceFieldFormat";
	public static final String PARSERATTRIBUTE_DESTDATEFORMAT="ParserAttribute.destDateFormat";
	
	public static final String COMPOSER_MAPPING_NAME ="ComposerMapping.name" ;
	public static final String ASCIICOMPOSERMAPPING_FIELDSEPARATOR="AsciiComposerMapping.fieldSeparator";
	public static final String COMPOSERMAPPING_DESTDATEFORMAT="ComposerMapping.destDateFormat";
	public static final String COMPOSERMAPPING_DESTFILEEXTENSION="ComposerMapping.destFileExt";
	public static final String COMPOSERATTRIBUTE_DESTINATIONFIELD="ComposerAttribute.destinationField";
	public static final String COMPOSERATTRIBUTE_DEFAULTVALUE="ComposerAttribute.defualtValue";
	public static final String COMPOSERATTRIBUTE_DESCRIPATION="ComposerAttribute.description";
	public static final String COMPOSERATTRIBUTE_TRIMCHARS="ComposerAttribute.trimchars";
	public static final String COMPOSERATTRIBUTE_DATEFORMAT="ComposerAttribute.dateFormat";
	public static final String ASCIICOMPOSERATTRIBUTE_REPALCECONDITIONLIST="ASCIIComposerAttr.replaceConditionList";
	public static final String ASCIICOMPOSERATTRIBUTE_LENGTH="ASCIIComposerAttr.length";
	public static final String ASCIICOMPOSERATTRIBUTE_PADDINGCHAR="ASCIIComposerAttr.paddingChar";
	public static final String ASCIICOMPOSERATTRIBUTE_PREFIX="ASCIIComposerAttr.prefix";
	public static final String ASCIICOMPOSERATTRIBUTE_SUFFIX="ASCIIComposerAttr.suffix";
	
	public static final String DISTRIBUTION_PATHLIST_WRITE_PATH ="DistributionDriverPathList.writeFilePath";
	public static final String COMPOSER_WRITE_FILE_SUFFIX = "Composer.writeFilenameSuffix";
	public static final String SNMPALERT_NAME ="SNMPAlert.name" ;
	public static final String SNMPALERT_DESCRIPTION ="SNMPAlert.desc" ;
	
	public static final String SERVICEEXEC_QUEUESIZE_PARAM_NAME="svcExecParams.queueSize";
	public static final String DEFAULT_PRODUCT_CONFIGURATION_LIST="DEFAULT_PRODUCT_CONFIGURATION_LIST";
	public static final String CUSTOM_PRODUCT_CONFIGURATION_LIST="CUSTOM_PRODUCT_CONFIGURATION_LIST";
	public static final String CUSTOM_PRODUCT_CONFIGURATION_ENTITY_STATUS="CUSTOM_PRODUCT_CONFIGURATION_ENTITY_STATUS";
	
	public static final String LICENSE_PRODUCT = "LICENSE_PRODUCT";
	public static final String LICENSE_DETAILS = "LICENSE_DETAILS";
	
	public static final String PACKETSTATASTICAGENT_FILESTORAGEPATH ="PacketStatisticsAgent.storageLocation" ;
	public static final String PACKETSTATASTICAGENT_EXECUTIONINTERVAL ="Agent.executionInterval" ;
	
	public static final String PROCESSING_RECORD_CACHE_LIMIT = "ProcessingService.acrossFileDuplicateCDRCacheLimit";
	public static final String PROCESSING_PURGE_CACHE_INTERVAL = "ProcessingService.acrossFileDuplicatePurgeCacheInterval";
	public static final String PROCESSING_CHECK_INTERVAL = "ProcessingService.acrossFileDuplicateDateInterval";
	public static final String PROCESSING_GLOBAL_DEVICE = "ProcessingService.globalSeqDeviceName";
	public static final String PROCESSING_GLOBAL_MAX_LIMIT = "ProcessingService.globalSeqMaxLimit";
	public static final String PROCESSING_CHECK_UNIFIEDFIELDS = "ProcessingService.unifiedFields";
	public static final String PROCESSING_ERROR_PATH ="ProcessingService.errorPath" ;
	public static final String PROCESSINGPATHLIST_MAXFILESCOUNTALERT="ProcessingPathList.maxFilesCountAlert";
	public static final String PROCESSING_FILEGROUPING_ARCHIVEPATH="ProcessingService.fileGroupingParameter.archivePath";
	public static final String PROCESSING_FILEGROUPING_FILTERPATH="ProcessingService.fileGroupingParameter.filterDirPath";
	public static final String PROCESSING_FILEGROUPING_INVALIDPATH="ProcessingService.fileGroupingParameter.invalidDirPath";
	public static final String PROCESSING_FILEGROUPING_DUPLICATEPATH="ProcessingService.fileGroupingParameter.duplicateDirPath";
	public static final String PROCESSING_DUPLICATECHECK_ALERTID="ProcessingService.duplicateRecordCheck.alertId";
	public static final String PROCESSING_DUPLICATECHECK_ALERTDESCRIPTION="ProcessingService.duplicateRecordCheck.alertDescription";
	
	public static final String POLICY_NAME = "Policy.name";
	public static final String POLICY_DESCRIPTION = "Policy.description";
	
	public static final String POLICY_ACTION_NAME = "Policy.action.name";
	public static final String POLICY_ACTION_DESCRIPTION = "Policy.action.description";
	public static final String POLICY_ACTION_EXPRESSION = "Policy.action.actionExpression";

	public static final String SERVICE_FILE_RENAME_DESTINATION_PATH="ServiceFileRenameConfig.destinationPath";
	public static final String FILE_RENAME_AGENT_INITIAL_DELAY ="Agent.initialDelay" ;

	public static final String FILE_RENAME_AGENT_EXTENTION_AFTER_RENAME ="ServiceFileRenameConfig.extAfterRename" ;
	public static final String FILE_RENAME_AGENT_FILE_EXTENTION_LIST ="ServiceFileRenameConfig.fileExtensitonList" ;
	
	public static final String POLICY_CONDITION_NAME = "Policy.condition.name";
	public static final String POLICY_CONDITION_DESCRIPTION = "Policy.condition.description";
	public static final String POLICY_CONDITION_EXPRESSION = "Policy.condition.conditionExpression";
	
	public static final String POLICY_GROUP_NAME = "Policygroup.name";
	public static final String POLICY_GROUP_DESCRIPTION = "Policygroup.description";
	public static final String FTPDRIVER_FILE_FETCH_INTERVAL ="FTPCollectionDriver.myFileFetchParams.FileFetchIntervalMin";
	
	public static final String POLICY_RULE_NAME = "Policyrule.name";
	public static final String POLICY_RULE_DESCRIPTION = "Policyrule.description";
	public static final String POLICY_RULE_ALERT_DESCRIPTION = "Policyrule.alert.description";
	public static final String POLICY_RULE_GLOBAL_SEQUENCE_RULE = "Policyrule.globalSequenceRule";
	public static final String POLICY_RULE_ERROR_CODE="Policyrule.errorcode";
	
	
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_DATABASEFIELDNAME = "DatabaseDistributionDriverAttribute.databaseFieldName";
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_UNIFIEDFIELDNAME = "DatabaseDistributionDriverAttribute.unifiedFieldName";
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_DATATYPE = "DatabaseDistributionDriverAttribute.dataType";
	public static final String DATABASE_DIS_DRIVER_TABLE_NAME = "DatabaseDistributionDriver.tableName";
	
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_LENGTH="DatabaseDistributionDriverAttribute.length";	
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_PADDINGCHAR="DatabaseDistributionDriverAttribute.paddingChar";	
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_PREFIX="DatabaseDistributionDriverAttribute.prefix";	
	public static final String DATABASE_DIS_DRIVER_ATTRIBUTE_SUFFIX="DatabaseDistributionDriverAttribute.suffix";
	
	public static final String ASN1_COMPOSER_ASN1_DATA_TYPE = "ASN1ComposerAttribute.asn1DataType";
	public static final String ASN1_COMPOSER_DESTINATION_FIELD = "ASN1ComposerAttribute.destinationField";
	public static final String ASN1_COMPOSER_CHOICE_ID = "ASN1ComposerAttribute.choiceId";
	public static final String ASN1_COMPOSER_CHILD_ATTRIBUTES = "ASN1ComposerAttribute.childAttributes";
	public static final String ASN1_REC_MAIN_ATTRIBUTE = "ASN1ComposerMapping.recMainAttribute";
	public static final String ASN1_MULTI_CONTAINER_DELIMITER = "ASN1ComposerAttribute.multiContainerDelimiter";

	public static final String CONSOLIDATION_ACROSSFILE_PARTITION = "DataConsolidationService.acrossFilePartition";
	public static final String CONSOLIDATION_ACROSSFILE_MINFILERANGE ="DataConsolidationService.acrossFileMinBatchSize" ;
	public static final String CONSOLIDATION_ACROSSFILE_MAXFILERANGE ="DataConsolidationService.acrossFileMaxBatchSize" ;
	public static final String CONSOLIDATION_DEF_GROUP_REGEX_EXP = "DataConsolidationGroupAttribute.regExExpression";
	public static final String CONSOLIDATION_DEF_LOOKUP_TABLE_NAME = "DataConsolidationGroupAttribute.lookUpTableName";
	public static final String CONSOLIDATION_DEF_LOOKUP_TABLE_COLNAME = "DataConsolidationGroupAttribute.lookUpTableColumnName";
	public static final String CONSOLIDATION_PATH_MAPPING_PROCESS_RECORD = "DataConsolidationMapping.processRecordLimit";
	public static final String CONSOLIDATION_PATH_MAPPING_FILE_MIN_RANGE = "DataConsolidationMapping.minSeqRange";
	public static final String CONSOLIDATION_PATH_MAPPING_FILE_MAX_RANGE = "DataConsolidationMapping.maxSeqRange";
	public static final String CONSOLIDATION_PATH_MAPPING_NAME =  "DataConsolidationMapping.mappingName";
	public static final String CONSOLIDATION_DEFINITION_ACROSSFILE_PARTITION = "DataConsolidation.acrossFilePartition";
	public static final String CONSOLIDATION_DEFINITION_NAME = "DataConsolidation.consName";
	
	public static final String DATABASE_QUERY_NAME = "DatabaseQuery.name";
	public static final String DATABASE_QUERY_VALUE = "DatabaseQuery.value";
	public static final String DATABASE_QUERY_DESC = "DatabaseQuery.description";
	public static final String DATABASE_QUERY_OUTPUTDB = "DatabaseQuery.outputDbField";
	public static final String DATABASE_QUERY_CONDITIONEXPRESSION = "DatabaseQuery.conditionExpression";
	

	public static final String FIXEDLENGTH_ASCII_COMPOSER_DATEFORMAT = "FixedLengthASCIIComposerAttribute.fixedLengthDateFormat";
	public static final String FIXEDLENGTH_ASCII_COMPOSER_LENGTH = "FixedLengthASCIIComposerAttribute.fixedLength";
	public static final String FIXEDLENGTH_ASCII_COMPOSER_SEQNUMBER = "FixedLengthASCIIComposerAttribute.sequenceNumber";

	public static final String RULE_LOOKUP_TABLE_NAME = "RuleLookupTableData.viewName";
	public static final String RULE_LOOKUP_TABLE_DESCRIPTION = "RuleLookupTableData.description";
	public static final String RULE_LOOKUP_FIELD_NAME = "LookupFieldDetailData.fieldName";
	public static final String RULE_LOOKUP_DISPLAY_NAME = "LookupFieldDetailData.displayName";
	public static final String KEY_FILE_OR_PASS = "key.file.location.or.password";
	public static final String FILE_REPROCESS_MAX_DURATION_FOR_FILE_SEARCH = "MAX_DURATION_FOR_FILE_SEARCH";
	public static final String FILE_REPROCESS_MAX_CSV_FILE_SIZE = "MAX_CSV_FILE_SIZE";
	public static final String FILE_REPROCESS_MAX_COMPRESS_FILE_SIZE = "MAX_COMPRESSED_FILE_SIZE";
	public static final String FILE_REPROCESS_FILE_RECORDS_BATCH_SIZE = "FILE_RECORDS_BATCH_SIZE";
	public static final String POLICY_UNIQUE_FAILED = "Policy.uniqueName";
	public static final String PARTITIONPARAM_NETMASK = "PartitionParam.netMask" ;
	
	public static final String RECORDS_FOR_INLINE_FILE_VIEW = "RECORDS_FOR_INLINE_FILE_VIEW";
	public static final String CUSTOM_CONFIG_SERVICE_LIST = "CUSTOM_CONFIG_SERVICE_LIST";

	
	
	public static final String FIXEDLENGTHASCIIPARSERATTRIBUTE_START_LENGTH = "FixedLengthASCIIParserAttribute.startLength" ;
	public static final String FIXEDLENGTHASCIIPARSERATTRIBUTE_END_LENGTH = "FixedLengthASCIIParserAttribute.endLength" ;
	public static final String FIXEDLENGTHASCIIPARSERATTRIBUTE_LENGTH = "FixedLengthASCIIParserAttribute.length" ;
	
	public static final String FIXEDLENGTHBINARYPARSERATTRIBUTE_START_LENGTH = "FixedLengthBinaryParserAttribute.startLength" ;
	public static final String FIXEDLENGTHBINARYPARSERATTRIBUTE_END_LENGTH = "FixedLengthBinaryParserAttribute.endLength" ;
	public static final String FIXEDLENGTHBINARYPARSERATTRIBUTE_LENGTH = "FixedLengthBinaryParserAttribute.length" ;
	public static final String FIXEDLENGTHBINARYPARSERATTRIBUTE_RECORD_LENGTH = "FixedLengthBinaryParserAttribute.recordLength" ;
	
	public static final String PDFPARSERATTRIBUTE_LOCATION = "PDFParserAttribute.location";
	public static final String PDFPARSERATTRIBUTE_COL_START_LOCATION = "PDFParserAttribute.columnStartLocation";
	public static final String PDFPARSERATTRIBUTE_COL_IDENTIFIER = "PDFParserAttribute.columnIdentifier";
	public static final String PDFPARSERATTRIBUTE_REFERENCE_ROW = "PDFParserAttribute.referenceRow";
	public static final String PDFPARSERATTRIBUTE_COL_STARTS_WITH = "PDFParserAttribute.columnStartsWith";

	public static final String ASN1_PARSER_HEADER_OFFSET_LENGTH = "ASN1ParserMapping.headerOffset";
	public static final String ASN1_PARSER_RECORD_OFFSET_LENGTH = "ASN1ParserMapping.recOffset";
	public static final String ASN1_PARSER_RECORD_START_IDS = "ASN1ParserMapping.recordStartIds";
	public static final String ASN1_PARSER_REC_MAIN_ATTRIBUTE = "ASN1ParserMapping.recMainAttribute";
	public static final String ASN1_PARSER_ROOT_NODE_NAME = "ASN1ParserMapping.rootNodeName";
	public static final int ASN1_PARSER_DEFAULT_BUFFER_SIZE = 1024;
	
	public static final String AGGREGATIONSERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL ="AggregationService.svcExecParams.executionInterval" ;
	public static final String AGGREGATION_DEFINITION_NAME = "AggregationDefinition.aggDefName";
	public static final String AGGREGATION_DEFINITION_NOOFPARTITION = "AggregationDefinition.noOfPartition";
	public static final String AGGREGATION_DEFINITION_AGGINTERVAL = "AggregationDefinition.aggInterval";
	public static final String AGGREGATION_DEFINITION_FLEG = "AggregationDefinition.fLegVal";
	public static final String AGGREGATION_DEFINITION_LLEG = "AggregationDefinition.lLegVal";
	public static final String AGGREGATION_PATH_OMINFILERANGE ="Aggregation.path.oFileMinRange";
	public static final String AGGREGATION_PATH_OMAXFILERANGE="Aggregation.path.oFileMaxRange";
	public static final String AGGREGATION_PATH_OUTPUTFILENAME="PathList.path.outputfilename";
	
	public static final String HOST_CONFIGURATION_NAME= "HostConfiguration.name";
	public static final String HOST_CONFIGURATION_PMN_CODE="HostConfiguration.pmncode";
	public static final String HOST_CONFIGURATION_TADIG_CODE="HostConfiguration.tadigcode";
	
	
	public static final String ROAMING_PARAMETER_TAP_IN_FREQUENCY= "RoamingParameter.tapinFrequency";
	public static final String ROAMING_PARAMETER_NRTRDE_IN_FREQUENCY="RoamingParameter.nrtrdeInFrequency";
	public static final String ROAMING_PARAMETER_NRTRDE_OUT_FREQUENCY="RoamingParameter.nrtrdeOutFrequency";
	
	public static final String FILE_SEQUENCE_TEST_TAP_IN="FileSequenceManagement.testTapIn";
	public static final String FILE_SEQUENCE_TEST_TAP_OUT="FileSequenceManagement.testTapOut";
	public static final String FILE_SEQUENCE_TEST_RAP_IN="FileSequenceManagement.testRapIn";
	public static final String FILE_SEQUENCE_TEST_RAP_OUT="FileSequenceManagement.testRapOut";
	public static final String FILE_SEQUENCE_NRTRDE_IN="FileSequenceManagement.nrtrdeIn";
	public static final String FILE_SEQUENCE_COMMERCIAL_TAP_IN="FileSequenceManagement.commercialTapIn";
	public static final String FILE_SEQUENCE_COMMERCIAL_TAP_OUT="FileSequenceManagement.commercialTapOut";
	public static final String FILE_SEQUENCE_COMMERCIAL_RAP_IN="FileSequenceManagement.commercialRapIn";
	public static final String FILE_SEQUENCE_COMMERCIAL_RAP_OUT="FileSequenceManagement.commercialRapOut";
	public static final String FILE_SEQUENCE_NRTRDE_OUT="FileSequenceManagement.nrtrdeOut";
	
	public static final String TEST_TAP_IN = "TD";
	public static final String TEST_TAP_OUT="TD";
	public static final String TEST_RAP_IN= "RT";
	public static final String TEST_RAP_OUT = "RT";
	public static final String COMMERICAL_TAP_IN = "CD";
	public static final String COMMERICAL_TAP_OUT= "CD";
	public static final String COMMERICAL_RAP_IN = "RC";
	public static final String COMMERICAL_RAP_OUT= "RC";
	public static final String COMMERICAL_NRTRDE_IN = "NT";
	public static final String COMMERICAL_NRTRDE_OUT= "NT";
 
	public static final String TAP_IN = "TAPIN";
	public static final String TAP_OUT="TAPOUT";
	public static final String RAP_IN= "RAPIN";
	public static final String RAP_OUT = "RAPOUT";
	public static final String NRTRDE_IN = "NRTRDEIN";
	public static final String NRTRDE_OUT= "NRTRDEOUT";
 
	public static final String TEST_SIM_PMN_CODE="TestSimManagementData.pmnCode";
	public static final String TEST_SIM_IMSI="TestSimManagementData.imsi";
	public static final String TEST_SIM_MSISDN="TestSimManagementData.msisdn";
	public static final String INBOUND="Inbound";
	public static final String OUTBOUND="Outbound";
	public static final String TEST_SIM_SERVICES="TestSimManagement.services";
	public static final String INBOUND_PMNCODE="inboundPmncode";
	public static final String INBOUND_IMSI="inboundImsi";
	public static final String INBOUND_MSISDN="inboundMsisdn";
	public static final String INBOUND_TODATE="inboundTodate";
	public static final String INBOUND_FROMDATE="inboundFromDate";
	public static final String INBOUND_SERVICES="inboundServices";
	public static final String OUTBOUND_PMNCODE="outboundPmncode";
	public static final String OUTBOUND_IMSI="outboundImsi";
	public static final String OUTBOUND_MSISDN="outboundMsisdn";
	public static final String OUTBOUND_TODATE="outboundTodate";
	public static final String OUTBOUND_FROMDATE="outboundFromDate";
	public static final String OUTBOUND_SERVICES="outboundServices";
	public static final String MAX_RECORDS_TAP_OUT = "FileManagement.maxrecordsTapOut";
	public static final String MAX_FILE_SIZE_TAP_OUT = "FileManagement.maxFileSizeTapOut";
	public static final String MAX_RECORDS_NRTRDE_OUT = "FileManagement.maxRecordsNrtrdeOut";
	public static final String MAX_FILE_SIZE_NRTRDE_OUT = "FileManagement.maxFileSizeNrtrdeOut";
	public static final String TEST_SERVICE = "Test Service";
	public static final String COMMERCIAL_SERVICE = "Commercial Service";
	
	public static final String TEST_TAPIN_VERSION = "testTapInVersion";
	public static final String TEST_TAPOUT_VERSION = "testTapOutVersion";
	public static final String TEST_MAX_RECORDS_TAPOUT = "testMaxRecordsInTapOut";
	public static final String TEST_MAXFILE_TAPOUT = "testMaxFileSizeOfTapOut";
	public static final String TEST_GENERAT_FILESEQUENCE = "testRegeneratedTapOutFileSequence";
	public static final String TEST_FILE_VALIDATION = "testFileValidation";
	public static final String TEST_SEND_NOTIFICATION = "testSendTapNotification";
	public static final String TEST_RAPIN_VERSION = "testRapInVersion";
	public static final String TEST_RAPOUT_VERSION = "testRapOutVersion";
	public static final String TEST_SENDNRTRDE = "testSendNrtrde";
	public static final String TEST_NRTRDE_IN_VERSION = "testNrtrdeInVersion";
	public static final String TEST_NRTRDE_OUT_VERSION = "testNrtrdeOutVersion";
	public static final String TEST_MAX_RECORD_NRTRDE = "testMaxRecordsInNrtrdeOut";
	public static final String TEST_MAX_FILESIZE_NRTRDE = "testMaxfileSizeOfnrtrdeOut";
	public static final String COMMERCIAL_TAPIN_VERSION = "commercialTapInVersion";
	public static final String COMMERCIAL_TAPOUT_VERSION = "commercialTapOutVersion";
	public static final String COMMERCIAL_MAX_RECORDS_TAPOUT = "commercialMaxRecordsInTapOut";
	public static final String COMMERCIAL_MAX_FILESIZE_TAPOUT = "commercialMaxFileSizeOfTapOut";
	public static final String COMMERCIAL_GENERATE_FILESEQUENCE = "commercialRegeneratedTapOutFileSequence";
	public static final String COMMERCIAL_FILE_VALIDATION = "commercialFileValidation";
	public static final String COMMERCIAL_TAP_NOTIFICATION = "commercialSendTapNotification";
	public static final String COMMERCIAL_RAPIN_VERSION = "commercialRapInVersion";
	public static final String COMMERCIAL_RAPOUT_VERSION = "commercialRapOutVersion";
	public static final String COMMERCIAL_SEND_NRTRDE = "commercialSendNrtrde";
	public static final String COMMERCIAL_NRTRDE_IN_VERSION = "commercialNrtrdeInVersion";
	public static final String COMMERCIAL_NRTRDE_OUT_VERSION = "commercialNrtrdeOutVersion";
	public static final String COMMERCIAL_MAX_RECORDS = "commercialMaxRecordsInNrtrdeOut";
	public static final String COMMERCIAL_MAX_FILESIZE_NRTRDE = "commercialMaxfileSizeOfnrtrdeOut";
	
	public static final String MISSINGFILESEQUENCEMGMT_MIN_VALUE="MissingFileSequenceMgmt.minValue";
	public static final String MISSINGFILESEQUENCEMGMT_MAX_VALUE="MissingFileSequenceMgmt.maxValue";
	public static final String ASCIIPARSERMAPPING_RECORDHEADERIDENTIFIER="AsciiParserMapping.recordHeaderIdentifier";
	public static final String ASCIIPARSERMAPPING_EXCLUDELINESSTART="AsciiParserMapping.excludeLinesStart";
	public static final String ASCIIPARSERMAPPING_EXCLUDEMINCHARACTERS="AsciiParserMapping.excludeMinCharacters";
	public static final String ASCIIPARSERMAPPING_EXCLUDEMAXCHARACTERS="AsciiParserMapping.excludeMaxCharacters";
	
	public static final String SERVICE_STACKPORT 							=	"Service.stackPort" ;
	public static final String SERVICE_SVCEXECPARAMS_MAINTHREAD_PRIORITY 	=	"Service.svcExecParams.mainThread.priority" ;
	public static final String SERVICE_SVCEXECPARAMS_WORKERTHREAD_PRIORITY 	=	"Service.svcExecParams.workerThread.priority" ;
	public static final String DIAMETER_SESSION_CLEAN_UP_INTERVAL			=	"Diameter.session.cleanUp.interval";
	public static final String DIAMETER_SESSION_TIMEOUT						=	"Diameter.session.timeOut";
	public static final String DIAMETER_STACK_REALM							=	"Diameter.stackRealm";
	public static final String DIAMETER_STACK_IDENTITY						=	"Diameter.stackIdentity";
	public static final String DIAMETER_SEPERATOR							=	"Diameter.seperator";
	public static final String DIAMETER_RESULT_CODE_ON_OVERLOAD				=	"Diameter.resultCodeOnOverload";
	public static final String DIAMETER_DUPLICATE_PURGE_INTERVAL			=	"Diameter.duplicatePurgeInterval";
	
	public static final String DIAMETERPEER_NAME 					= 	"DiameterPeer.name" ;
	public static final String DIAMETERPEER_IDENTITY		 		= 	"DiameterPeer.identity" ;
	public static final String DIAMETERPEER_REALMNAME 				= 	"DiameterPeer.realmName" ;
	public static final String DIAMETERPEER_WATCHDOGINTERVAL		= 	"DiameterPeer.watchDogInterval" ;
	public static final String DIAMETERPEER_REQUESTTIMEOUT			= 	"DiameterPeer.requestTimeOut" ;
	public static final String DIAMETERPEER_FILENAMEFORMAT 			= 	"DiameterPeer.fileNameFormat" ;
	public static final String DIAMETERPEER_OUTFILELOCATION 		= 	"DiameterPeer.outFileLocation" ;
	public static final String DIAMETERPEER_MINFILESEQVALUE 		= 	"DiameterPeer.minFileSeqValue" ;
	public static final String DIAMETERPEER_MAXFILESEQVALUE 		= 	"DiameterPeer.maxFileSeqValue" ;
	public static final String DIAMETERPEER_VOLLOGROLLINGUNIT 		= 	"DiameterPeer.volLogRollingUnit" ;
	public static final String DIAMETERPEER_TIMELOGROLLINGUNIT		= 	"DiameterPeer.timeLogRollingUnit" ;
		
	
	public static final String DIAMETERAVP_ATTRIBUTEVALUE 				= 	"DiameterAvp.attributeValue" ;
	
	public static final String LOOKUP_DUPLICATE_DATA_PATHDETAIL = 	"LOOKUP_DUPLICATE_DATA_PATHDETAIL";
	
	public static final String TRIGGER_NAME = 	"trigger.triggerName";
	public static final String TRIGGER_DESCRIPTION = 	"trigger.description";
	public static final String TRIGGER_ALTERATIONCOUNT_FOR_MINUTE	=	"trigger.alterationcount.for.minute";
	public static final String TRIGGER_ALTERATIONCOUNT_FOR_HOURLY	=	"trigger.alterationcount.for.hourly";
	public static final String TRIGGER_ALTERATIONCOUNT_FOR_DAILY	=	"trigger.alterationcount.for.daily";
	public static final String TRIGGER_ALTERATIONCOUNT_FOR_MONTHLY	=	"trigger.alterationcount.for.monthly";
	public static final String TRIGGER_DAYOFMONTH =	"trigger.dayofmonth";

	public static final String AUTOUPLOADJOBDETAIL_SOURCEDIRECTORY =	"AutoUploadJobDetail.sourceDirectory";
	public static final String EDIT_EMAIL_FOOTER_IMAGE = "editEmailFooterImage";		
	public static final String EMAIL_FOOTER_IMAGE_PARAM = "Email Footer Image Param";
	public static final String AUTHENTICATION_TYPE = "AUTHENTICATION_TYPE";	
	public static final String FOOTER_IMAGE = "FOOTER_IMAGE";	
	public static final String RESET_PASSWORD_MAIL_TEMPLET = "RESET_PASSWORD_MAIL_TEMPLET";	//NOSONAR
	public static final String RESET_PASSWORD_MAIL_SUBJECT = "RESET_PASSWORD_MAIL_SUBJECT";	//NOSONAR
	public static final String FROM_EMAIL_PASSWORD = "FROM_EMAIL_PASSWORD";	//NOSONAR
	public static final String FROM_EMAIL_ADDRESS  = "FROM_EMAIL_ADDRESS";	
	public static final String SMTP_MAIL_HOST_PORT  = "SMTP_MAIL_HOST_PORT";	
	public static final String SMTP_MAIL_HOST_IP = "SMTP_MAIL_HOST_IP";	
	public static final String SMTP_USER_PASSWORD = "SMTP_USER_PASSWORD";	//NOSONAR
	public static final String SMTP_USERNAME = "SMTP_USERNAME"; 	
	public static final String MIGRATE_ALL_PASSWORD = "MIGRATE_ALL_PASSWORD";//NOSONAR
	public static final String GROUP_ATTRIBUTE_NAME ="GroupAttribute.name" ;
	
	public static final String SSO_ADMIN_USERNAME = "SSO_ADMIN_USERNAME";
	public static final String SSO_ADMIN_PASSWORD = "SSO_ADMIN_PASSWORD";//NOSONAR
	public static final String SSO_ENABLE = "SSO_ENABLE";
	public static final String ENABLE_KUBERNATES = "ENABLE_KUBERNATES";
	public static final String VERSION_MIGRATE = "VERSION_MIGRATE";
	
	public static final String CIRCLE_NAME ="circle.name" ;
	public static final String CIRCLE_DESCRIPTION ="circle.description" ;
	
	private SystemParametersConstant(){
		// private constructor
	}

}
