package com.elitecore.sm.common.constants;

public interface MigrationConstants {
	
	
	public static String TEMP_PATH_FOR_UNZIP = "/home/elitecore/Documents/migration/temp";
	public static String ZIP_PATH = "/home/harsh/online_services/migration/extract/6666.zip";
	public static String SERVER_INSTANCE_PORT_IN_ZIP = "6666";

	public static String ZIP_PATH_READ_PATH = "/home/elitecore/projects/mediation/smrevamp/crestelmediation/repository/modules/mediation/config/9999.zip";

	public static String ZIP_EXTRACT_PATH= "/home/elitecore/temporaryMigration";
	public static String SERVICES_DIR = "SERVICES";
	public static String AGENT_DIR = "AGENT";
	public static String DATABASE_DIR = "DATABASE";
	public static String POLICIES_DIR = "POLICIES";
	public static String SYSTEM_DIR = "SYSTEM";
	public static String AGGREGATION_DIR = "AGGREGATION";
	public static String ALERT_DIR = "ALERT";
	public static String WEBSERVICE_DIR = "WEBSERVICE";
	public static String DRIVERS_DIR = "DRIVERS";

	public static String AGGREGATION_SERVICE_DIR = "AGGREGATIONSERVICE";
	public static String COLLECTION_SERVICE_DIR = "COLLECTIONSERVICE";
	public static String CORRELATION_SERVICE_DIR = "CORRELATIONSERVICE";
	public static String DATA_CONSOLIDATION_SERVICE_DIR = "DATACONSOLIDATIONSERVICE";
	public static String DISTRIBUTION_SERVICE_DIR = "DISTRIBUTIONSERVICE";
	public static String GTP_PRIME_COLLECTION_SERVICE_DIR = "GTPPRIMECOLLECTIONSERVICE";
	public static String IPLOG_CORRELATION_SERVICE_DIR = "IPLOGCORRELATIONSERVICE";
	public static String IPLOG_PARSING_SERVICE_DIR = "IPLOGPARSINGSERVICE";
	public static String NATFLOW_BINARY_COLLECTION_SERVICE_DIR = "NATFLOWBINARYCOLLECTIONSERVICE";
	public static String NATFLOW_COLLECTION_SERVICE_DIR = "NATFLOWCOLLECTIONSERVICE";
	public static String PARSING_SERVICE_DIR = "PARSINGSERVICE";
	public static String PROCESSING_SERVICE_DIR = "PROCESSINGSERVICE";
	public static String RADIUS_COLLECTION_SERVICE_DIR = "RADIUSCOLLECTIONSERVICE";
	public static String SYSLOG_COLLECTION_SERVICE_DIR = "SYSLOGCOLLECTIONSERVICE";

	public static String COLLECTION_SERVICE_XML = "collection-service.xml";
	public static String DISTRIBUTION_SERVICE_XML = "distribution-service.xml";
	public static String PARSING_SERVICE_XML = "parsing-service.xml";
	public static String IPLOG_PARSING_SERVICE_XML = "iplog-parsing-service.xml";
	
	public static String SFTP_COLLECTION_DRIVER_XML = "sftp-collection-driver.xml";
	public static String LOCAL_COLLECTION_DRIVER_XML = "local-collection-driver.xml";
	public static String FTP_COLLECTION_DRIVER_XML = "ftp-collection-driver.xml";
	
	public static String SFTP_DISTRIBUTION_DRIVER_XML = "sftp-distribution-driver.xml";
	public static String LOCAL_DISTRIBUTION_DRIVER_XML = "local-distribution-driver.xml";
	public static String FTP_DISTRIBUTION_DRIVER_XML = "ftp-distribution-driver.xml";
	
	public static String ASCII_COMPOSER_PLUGIN_XML = "ascii-composer-plugin.xml";
	
	public static String MEDIATION_SERVER_XML = "mediation-server.xml";
	public static String NATFLOW_COLLECTION_SERVICE_XML = "natflow-collection-service.xml";
	
	public static String DATA_SOURCE_XML = "oracle-database-conf.xml";

	public static String LOCAL_COLLECTION_DRIVER = "LOCAL_COLLECTION_DRIVER";
	public static String FTP_COLLECTION_DRIVER = "FTP_COLLECTION_DRIVER";
	public static String SFTP_COLLECTION_DRIVER = "SFTP_COLLECTION_DRIVER";

	public static String MAP_JAXB_CLASS_KEY = "JAXB_CLASS";
	public static String MAP_SM_CLASS_KEY = "SM_CLASS";

	public static String MIN_FILE_RANGE = "MIN_FILE_RANGE";
	public static String MAX_FILE_RANGE = "MAX_FILE_RANGE";
	
	public static String MEDIATION_SERVER_XSD = "mediation-server.xsd";
	public static String DATA_SOURCE_XSD = "data-source.xsd";
	public static String SNMP_ALERT_LISTENERS_XSD = "snmp-alert-listeners.xsd";
	public static String SNMP_CONFIG_XSD = "snmp-conf.xsd";
	public static String ALERT_CONF_XSD = "alert-conf.xsd";
	
	public static String DISTRIBUTION_SERVICE_XSD = "distribution-service.xsd";
	public static String FTP_DISTRIBUTION_DRIVER_XSD = "ftp-distribution-driver.xsd";
	public static String SFTP_DISTRIBUTION_DRIVER_XSD = "sftp-distribution-driver.xsd";
	public static String LOCAL_DISTRIBUTION_DRIVER_XSD = "local-distribution-driver.xsd";
	public static String ASCII_COMPOSER_PLUGIN_XSD = "ascii-compoer-mapping.xsd";
	public static String PARSING_SERVICE_XSD = "parsing-service.xsd";
	public static String ASCII_PARSER_PLUGIN_XML = "ascii-parser-plugin.xml";
	public static String JSON_PARSER_PLUGIN_XML = "json-parser-plugin.xml";
	public static String REGEX_PARSER_PLUGIN_XML = "regex-parser-plugin.xml";
	public static String NATFLOW_PARSING_PLUGIN_XML = "natflow-parser-plugin.xml";
	
	public static String ENGINE_SERVICE_FOLDER = "services";
	public static String ENGINE_DISTRIBUTION_SERVICE_FOLDER = "distributionservice";
	public static String ENGINE_COLLECTION_SERVICE_FOLDER = "collectionservice";
	public static String ENGINE_DATA_SOURCE_FOLDER = "database";
	public static String ENGINE_DRIVER_FOLDER = "drivers";
	public static String ENGINE_ALERT_FOLDER = "alert";

	public static String ENGINE_PLUGIN_FOLDER = "plugins";
	public static String ENGINE_NETFLOW_COLLECTION_SERVICE_FOLDER = "natflowcollectionservice";
	public static String ENGINE_NETFLOW_BINARY_COLLECTION_SERVICE_FOLDER = "natflowbinarycollectionservice";
	public static String ENGINE_PARSING_SERVICE_FOLDER = "parsingservice";
	
	public static String NATFLOW_BINARY_COLLECTION_SERVICE_XML = "natflow-binary-collection-service.xml";
	public static String NATFLOW_BINARY_COLLECTION_SERVICE_XSD = "natflow-binary-collection-service.xsd";

	public static String NATFLOW_COLLECTION_SERVICE_XSD = "natflow-collection-service.xsd";
	public static String COLLECTION_SERVICE_XSD = "collection-service.xsd";
	
	public static String DOZER_TYPE = "CUSTOM";

	public static int BUFFER_SIZE = 4096;

	public static String VOLUME_BASED = "VOLUME_BASED";
	public static String SIZE_BASED = "SIZE-BASED";
	
	public static String TIME_BASED = "TIME_BASED";
	public static String TIME_D_BASED = "TIME-BASED";

	
	public static String SNMP_SERVER_CONFIG_XML = "Snmp-conf.xml";
	
	public static String SNMP_ALERT_LISTENERS_XML = "Snmp-alert-listeners.xml";
	public static String ALERT_CONF_XML = "alert-conf.xml";


	public static String SYSLOG_COLLECTION_SERVICE_XML = "syslog-collection-service.xml";
	public static String SYSLOG_COLLECTION_SERVICE_XSD = "syslog-collection-service.xsd";
	public static String ENGINE_SYSLOG_COLLECTION_SERVICE_FOLDER = "syslogcollectionservice";

	public static String MQTT_COLLECTION_SERVICE_XML = "mqtt-collection-service.xml";
	public static String MQTT_COLLECTION_SERVICE_XSD = "mqtt-collection-service.xsd";
	public static String ENGINE_MQTT_COLLECTION_SERVICE_FOLDER = "mqttcollectionservice";
	
	public static String HTTP2_COLLECTION_SERVICE_XML = "http2-collection-service.xml";
	public static String HTTP2_COLLECTION_SERVICE_XSD = "http2-collection-service.xsd";
	public static String ENGINE_HTTP2_COLLECTION_SERVICE_FOLDER = "http2collectionservice";
	
	public static String PACKET_STATISTICS_AGENT_XML = "packet-statistics-agent.xml";
	public static String FILE_RENAME_AGENT_XML = "file-rename-agent.xml";
	
	public static String FILE_RENAMING_AGENT = "FILE_RENAMING_AGENT";
	
	
}
