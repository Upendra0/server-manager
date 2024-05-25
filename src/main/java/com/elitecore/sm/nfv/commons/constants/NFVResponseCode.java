/**
 * 
 */
package com.elitecore.sm.nfv.commons.constants;

/**
 * The Enum NFVResponseCode.
 *
 * @author sagar shah
 * July 13, 2017
 */

public enum NFVResponseCode {
	
	/** The success. */
	SUCCESS(1, "Success"),
	
	/** The required param missing. */
	REQUIRED_PARAM_MISSING(-1001,"Required Parameter missing"),
	
	/** The invalid ip address. */
	INVALID_IP_ADDRESS(-1002,"Invalid IP Address"),
	
	/** The invalid login credential. */
	INVALID_LOGIN_CREDENTIAL(-1003,"Invalid username or password"),
	
	/** The invalid field value. */
	INVALID_FIELD_VALUE(-1004,"Invalid Field Value"),
	
	/** The required header param missing. */
	REQUIRED_HEADER_PARAM_MISSING(-1005,"Required Header Parameter missing"),
	
	//ADD SERVER INSTANCE API
	
	/** The server insert fail. */
	SERVER_INSERT_FAIL(-11001,"Server creation failed!!"),
	
	/** The p engine not running. */
	P_ENGINE_NOT_RUNNING(-11002,"Utility Service on port '1617' is not reachable."),
	
	/** The server get ipaddress fail. */
	SERVER_GET_IPADDRESS_FAIL(-11003,"Failed to get server details"),
	
	/** The server instance port across not unique servername. */
	SERVER_INSTANCE_PORT_ACROSS_NOT_UNIQUE_SERVERNAME(-11004,"Server instance port is not unique across the IpAddress.So Can not create the Server Instance"),
	
	/** The duplicate server instance name. */
	DUPLICATE_SERVER_INSTANCE_NAME(-11005, "Failed to create Server Instance due to Duplication of name."),
	
	/** The server instance init fail. */
	SERVER_INSTANCE_INIT_FAIL(-11006,"Server Instance initialization has been failed."),
	
	/** The server instance insert fail. */
	SERVER_INSTANCE_INSERT_FAIL(-11007,"Server Instance creation failed!!"),
	
	SERVER_INSTANCE_CHECK_STATUS_FAIL(-21008,"Server Instance check status failed!!"),
		
	SERVER_INSTANCE_CHECK_STATUS_TRY_COUNT_FAIL(-21009,"Server Instance check status retry count exceeds. It is 15 !!"),
	
	/** The server instance insert jmx conn fail. */
	SERVER_INSTANCE_INSERT_JMX_CONN_FAIL(-11008,"Failed to add Server Instance. Cannot obtain JMX connection to engine."),
	
	/** The server instance insert jmx api fail. */
	SERVER_INSTANCE_INSERT_JMX_API_FAIL(-11009,"Failed to add Server Instance. Error while invoking JMX API to engine."),
	
	/** The server instance inactive insert success. */
	SERVER_INSTANCE_INACTIVE_INSERT_SUCCESS(-11010,"Server instance is created in INACTIVE state as the system didn't get the response from the Engine."),
	
	/** The server instance create script jmx conn fail. */
	SERVER_INSTANCE_CREATE_SCRIPT_JMX_CONN_FAIL(-11011,"Server Instance script create failed. Cannot obtain JMX connection to engine."),
	
	/** The server instance create script jmx api fail. */
	SERVER_INSTANCE_CREATE_SCRIPT_JMX_API_FAIL(-11012,"Server Instance script create failed. Error while invoking JMX API to engine."),
	
	/** The server instance create script fail. */
	SERVER_INSTANCE_CREATE_SCRIPT_FAIL(-11013,"Shell Script creation has been failed due to wrong configuration of server instance parameter values."),
	
	/** The server instance already avaliable. */
	SERVER_INSTANCE_ALREADY_AVALIABLE(-11014,"Server Instance is already running on port. Shutdown first than try again."),
	
	/** The unknown service run on port. */
	UNKNOWN_SERVICE_RUN_ON_PORT(-11015,"Given port is already in used. Try with another port!!"),
	
	//APPLY ENGINE LICENSE API  
	
	/** The get server details fail. */
	GET_SERVER_DETAILS_FAIL(-12001,"Failed to get server instance detail."),
	
	/** The get license details fail. */
	GET_LICENSE_DETAILS_FAIL(-12002,"Failed to get license detail from existing server."),
	
	/** The license creation fail. */
	LICENSE_CREATION_FAIL(-12003,"Unable to generate license for new server."),
	
	/** The unable to get server instance. */
	UNABLE_TO_GET_SERVER_INSTANCE(-12004,"Failed to get server instance details."),
	
	//SYNC SERVER API  

	UNABLE_TO_SYNC_SERVER_INSTANCE(-13001,"Failed to sync server instance details."),
	
	SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL(-13002,"Failed to sync server instance. Cannot obtain JMX connection to engine."),
	
	SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL(-13003,"Failed to sync server instance. Error while invoking JMX api to engine."),
	
	SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS(-13004,"Synchronization can perform for only running server instance."),
	
	//COPY CONFIG SERVER API
	
	SERVER_INSTANCE_COPY_CONFIG_FAIL(-14001,"Copy Configuration has been failed."),
	
	SERVER_INSTANCE_INACTIVE_COPY_CONFIG_FAIL(-14002,"Copy Configuration can be performed for active instance."),
	
	SERVICE_RUNNING(-14003,"Detected running services in this server instance, all service should be stopped to perform this action."),
	
	//RESTART SERVER API
	
	SERVER_INSTANCE_RESTART_FAIL(-15001,"Failed to restart server."),
	
	SERVER_INSTANCE_RESTART_JMX_CONN_FAIL(-15002,"Failed to restart server. Server utility service is down."),
	
	SERVER_INSTANCE_RESTART_JMX_API_FAIL(-15003,"Failed to restart server. Error while invoking JMX API to engine."),
	
	SERVER_INSTANCE_START_SCRIPT_UNAVALIABLE(-15004,"Fail to start server, Script file is missing."),
	
	SERVER_INSTANCE_UNAVALIABLE(-15005,"Server details not found in database."),
	
	SERVER_INSTANCE_ID_UNAVALIABLE(-15006,"Server Id not found in request."),
	
	//START SERVER API
	
	SERVER_INSTANCE_START_FAIL(-16001,"Failed to start server."),
	
	SERVER_INSTANCE_START_JMX_API_FAIL(-16002,"Failed to start server. Error while invoking JMX API to engine."),
	
	SERVER_INSTANCE_START_JMX_CONN_FAIL(-16003,"Failed to start server. Server utility service is down."),
	
	//STOP SERVER API
	
	SERVER_INSTANCE_STOP_JMX_CONN_FAIL(-17001,"Cannot obtain connection to engine for stop, Please verify if server is already stopped or connection parameters need tuning."),
	
	SERVER_INSTANCE_STOP_FAIL(-17002,"Failed to stop server."),
	
	//ADD CLIENT API
	
	ADD_CLIENT_FAIL(-18001,"Failed to add client."),
	
	NO_CLIENT_FOUND_IN_SERVICE(-18002,"No client found in service to copy."),
	
	NO_SERVICE_FOUND_IN_SERVER(-18003,"No service found in server for given type."),
	
	CLIENT_ALREADY_EXISTS(-18004,"Client already exists."),
	
	SERVER_INSTANCE_DELETE_FAIL(-18005,"Failed to delete server instance."),
	
	SERVER_DELETE_FAIL(-18006,"Failed to delete server."),
	
	SERVER_INSTANCE_UPDATE_SCRIPT_FAIL(585, "server.instance.update.script.error"),
	
	FROM_IP_PORT_NOT_AVAILABLE(-18007,"Copy from IpAddress and port not availbale !!"),
	
    TO_IP_PORT_NOT_AVAILABLE(-18008,"Copy to IpAddress and port not availbale !!"),
	
	SERVERINSTANCE_IMPORT_FAIL(159,"Imported entity validation fail."),

	SERVERINSTANCE_IMPORT_SUCCESS(158,"Selected configuration file has been imported successfully.");
	

	
	
		

	
	/** The code. */
	private final int code;
	
	/** The description. */
	private final String description;

	/**
	 * Instantiates a new NFV response code.
	 *
	 * @param code the code
	 * @param description the description
	 */
	private NFVResponseCode(int code, String description) {
		this.code = code;
		this.description = description;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return description;
	}
}