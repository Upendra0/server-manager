/**
 * 
 */
package com.elitecore.sm.integration.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.elitecore.core.agent.CrestelAgentData;
import com.elitecore.core.commons.util.data.MISReportRequestData;
import com.elitecore.core.commons.util.data.MISReportResponseData;
import com.elitecore.core.commons.util.restservice.RestServiceConstants;
import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.core.util.mbean.data.config.CrestelNetDriverData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.core.util.mbean.data.config.CrestelNetServiceData;
import com.elitecore.core.util.mbean.data.filereprocess.FileDetailsData;
import com.elitecore.core.util.mbean.data.filereprocess.SearchFileCriteriaData;
import com.elitecore.core.util.mbean.data.filereprocess.ServicewiseFileDetailsData;
import com.elitecore.core.util.mbean.data.live.CrestelNetServerDetails;
import com.elitecore.core.util.mbean.data.live.CrestelNetServiceDetails;
import com.elitecore.coreradius.util.mbean.data.ClientData;
import com.elitecore.passwordutil.DecryptionNotSupportedException;
import com.elitecore.passwordutil.NoSuchEncryptionException;
import com.elitecore.passwordutil.PasswordEncryption;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.util.MapCache;

/**
 * @author Sunil Gulabani
 * Jul 22, 2015
 */

public class RemoteJMXHelper  extends BaseClient{
	
	
	public static final String EC_ENG_LIC="Elitecore:type=-CrestelLicense";
	public static final String EC_ENG_COUNTER_CMD="Elitecore:type=-CrestelSerivceCountercmd";
	public static final String EC_ENG_CONFIGSERVER ="Elitecore:type=CrestelConfigServer";
	public static final String EC_ENG_SERVER ="Elitecore:type=CrestelServer";
	public static final String EC_ENG_SERVICE="Elitecore:type=CrestelService";
	public static final String EC_ENG_STRINGCLASS="java.lang.String";
	public static final String EC_ENG_DATECLASS="java.util.Date";
	public static final String EC_ENG_MIS_PKTSTATS= "Elitecore:type=-CrestelMISReportController";
	public static final String EC_ENG_FILE_REPROCESSING="Elitecore:type=-CrestelFileReprocessing";
	public static final String EC_ENG_LOOKUP_DATA_RELOAD_CACHE="Elitecore:type=-CrestelLookupDataController";
	
	public static final String EC_LIST_TYPE  = "java.util.List"; 
	public static final String EC_ENG_EXPRESSION_VALIDATE= "Elitecore:type=-CrestelProcessingRulesCacheController";
	public static final String SNMP_V3_ENGINE_ID= "Elitecore:type=CrestelSNMPService";
			
	/**
	 * Constructor
	 * @param ipAddress
	 * @param port
	 */
	public RemoteJMXHelper (String ipAddress, int port,int maxConnRetry, int retryInterval, int conTimeout) {
		super(ipAddress,port,maxConnRetry,retryInterval,conTimeout);
	}
	
	/**
	 * check whether required port is free or not via JMX call
	 * @param port
	 * @return 
	 */
	public boolean portAvailable(Integer port){
		Object portAvailable = invoke(
							BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
							"portAvailable", 
							new Object[]{port}, 
							new String[]{Integer.class.getName()}
							);
		
		if(portAvailable!=null){
			logger.debug("portAvailable : "+portAvailable);
			return (Boolean) portAvailable;
		} else {
			logger.debug("portAvailable : false");
			return false;
		}
	}
	
	/**
     * Checks for the port is running or not via JMX call
     * @return
     * @throws SMException
     */
    public boolean checkPortRunningOfServerInstance() throws SMException{
    	String versionInfo = null;
    	try {
    		versionInfo = versionInformation();
		} catch (Exception e) {
			logger.error("Error while fetching Version Information via JMX Call", e);
			versionInfo = null;
		}
    	if(versionInfo!=null){
    		logger.debug("checkPortRunningOfServerInstance : true");
    		return true;
    	}
    	logger.debug("checkPortRunningOfServerInstance : false");
    	return false;
    }
    
    
    /**
     * Get P-Enginehome via JMX call
     * @return
     */
    public String getCrestelPEngineHome(){
    	Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"crestelPEngineHome", 
									new Object[]{}, 
									new String[]{}
									);
    	if(response!=null)
    		return (String)response;
    	return null;
    }
    
    /**
     * Check conf folder with given port exists
     * @param port
     * @return
     */
    public boolean confFolderExists(int port){
    	Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"confFolderExists", 
									new Object[]{port}, 
									new String[]{Integer.class.getName()}
									);
    	
    	if(response!=null){
    		logger.debug("confFolderExists : "+(Boolean)response);
    		return (Boolean)response;
    	} else {
    		logger.debug("confFolderExists : false");
    		return false;
    	}
    }
        
    
    /**
     * Get java home path for remote machine via JMX call 
     * @return 
     */
    public String getJavaHome(){
    	Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"javaHome", 
									new Object[]{}, 
									new String[]{});
    	if(response!=null)
    		return (String) response;
    	return null;
    }
	
	/**
	 * create script for given port with specified scriptName via JMX call
	 * @param scriptName
	 * @param port
	 * @param minMemory
	 * @param maxMemory
	 * @return 
	 */
	public boolean createServerInstanceScript(String scriptName,Integer port,Integer  minMemory,Integer maxMemory,String ipAddress){
		Object fileCreated = invoke(
							BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
							"createServerInstanceScript", 
							new Object[]{scriptName,port, minMemory, maxMemory, ipAddress}, 
							new String[]{String.class.getName(),Integer.class.getName(),Integer.class.getName(),Integer.class.getName(),String.class.getName()});
		
		if(fileCreated!=null){
			logger.debug("createServerInstanceScript : "+(Boolean)fileCreated);
			return (Boolean)fileCreated;
		} else {
			logger.debug("createServerInstanceScript : false");
			return false;
		}
	}
	
	/**
	 * Run startup script via JMX call
	 * @param port
	 * @return
	 */
	public String runScript(Integer port){
		Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"runScript", 
									new Object[]{port}, 
									new String[]{Integer.class.getName()}
									);
		if(response!=null)
			return (String)response;
		return null;
	}
	
	
	/**
	 * Run startup script via JMX call
	 * @param script
	 * @return
	 */
	public String runScript(String script){
		Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"runStartScript", 
									new Object[]{script}, 
									new String[]{String.class.getName()});
		if(response!=null)
			return (String)response;
		return null;
	}
	
	/**
	 * check script with name avaliable or not via JMX call
	 * @param script
	 * @return 
	 */
	public boolean checkScriptExists(String script){
		Object fileExists = invoke(
							BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
							"checkScriptExists", 
							new Object[]{script}, 
							new String[]{String.class.getName()}
							);
		
		if(fileExists!=null){
			logger.debug("checkScriptExists : "+(Boolean)fileExists);
			return (Boolean)fileExists;
		} else {
			logger.debug("checkScriptExists : false");
			return false;
		}
	}
	 
	/**
	 * rename port folder via JMX call
	 * @param port
	 * @return
	 */
	public boolean renamePortFolder(int port){
		Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"renamePortFolder", 
									new Object[]{port}, 
									new String[]{Integer.class.getName()});
		
		if(response!=null){
			logger.debug("renamePortFolder : "+(Boolean)response);
			return (Boolean)response;
		} else {
			logger.debug("renamePortFolder : false");
			return false;
		}
	}
	
	/**
	 * rename instance startup file with timestamp and delete via JMX call
	 * @param port
	 * @return
	 */
	public boolean renameStartupFile(int port){
		Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"renameStartupFile", 
									new Object[]{port}, 
									new String[]{Integer.class.getName()});
		if(response!=null){
			logger.debug("renameStartupFile : "+(Boolean)response);
			return (Boolean)response;
		} else {
			logger.debug("renameStartupFile : false");
			return false;
		}
	}
	
	/**
	 * Synchronize Full Server
	 * @param serverId
	 * @param serverName
	 * @param configurationList
	 * @param serviceList
	 * @return
	 */
	public String syncFullServerToEngine(int serverId, String serverName, List<CrestelNetConfigurationData> configurationList,
			List<CrestelNetServiceData> serviceList, CrestelNetServerData serverData){
		serverData.setNetServerId(String.valueOf(serverId));
		serverData.setNetServerName(serverName);
		serverData.setNetConfigurationList(configurationList);
		serverData.setNetServiceList(serviceList);
		for(CrestelNetConfigurationData data : configurationList){
			logger.debug("********************Print configuration key going in sync to SI" + data.getNetConfigurationKey());
		}
		return updateServerConfiguration(serverData, versionInformation());
		 
	}
	
	/**
	 * Synchronize List of Service
	 * @param serviceList
	 * @return
	 */
	public String syncListOfServicesToEngine(List<CrestelNetServiceData> serviceList){
		if(serviceList!=null){
			for(CrestelNetServiceData eliteNetServiceData : serviceList){
				syncServiceConfiguration(eliteNetServiceData);
			}
			return "success";
		}
		return "fail";
	}
	
	/**
	 * Synchronize Service JMX Call
	 * @param eliteNetServiceData
	 * @return
	 */
	public String syncServiceConfiguration(CrestelNetServiceData eliteNetServiceData){
		Object[] objArgValues = { eliteNetServiceData, versionInformation()};
        String[] strArgTypes = { "com.elitecore.core.util.mbean.data.config.CrestelNetServiceData", EC_ENG_STRINGCLASS };

        String response = (String) invoke(EC_ENG_CONFIGSERVER, RestServiceConstants.UPDATE_SERVICE_CONFIGURATION, objArgValues, strArgTypes);
        logger.debug("syncServiceConfiguration response: " + response);
        return response;
	}
	
	/**
	 * Update Server Config JMX Call
	 * @param eliteNetServerData
	 * @param serverInstanceVersionId
	 * @return
	 */
	private String updateServerConfiguration(CrestelNetServerData eliteNetServerData, String serverInstanceVersionId){
		
		Object response ;
		Object[] methodParamValues = {eliteNetServerData,serverInstanceVersionId};
		String[] methodParamDataTypes = {"com.elitecore.core.util.mbean.data.config.CrestelNetServerData",String.class.getName()};
		response = (String) invoke(EC_ENG_CONFIGSERVER, RestServiceConstants.UPDATE_SERVER_CONFIGURATION, methodParamValues, methodParamDataTypes);
		logger.debug("updateServerConfiguration response: " + response);
		if(response!=null)
			return (String)response;
		return null;
	}
	
	/**
	 * Get the Version Number of Engine 
	 * @return
	 */
	public String versionInformation(){
		String strVersion =Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV)) ? null : (String) getAttribute(EC_ENG_CONFIGSERVER,RestServiceConstants.VERSION_INFORMATION);
		logger.debug("strVersion: " + strVersion);
		return strVersion;
	}
	
	/**
	 * Reads the Whole Server Instance Configuration
	 * @return
	 */
	public CrestelNetServerData readServerConfiguration(){
		CrestelNetServerData eliteNetServerData  ;
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
		eliteNetServerData = (CrestelNetServerData) invoke( EC_ENG_SERVER, RestServiceConstants.READ_SERVER_CONFIGURATION, methodParamValues, methodParamDataTypes);
		return eliteNetServerData;
	}
	
	
	/**
	 * Soft restart server instance 
	 */
	public void softRestart(){
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
        invoke(EC_ENG_SERVER,RestServiceConstants.SOFT_RESTART, methodParamValues, methodParamDataTypes);
	}
	
	/**
	 * Reload server instance configuration
	 */
	public void reloadConfiguration(){
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
        invoke(EC_ENG_SERVER, RestServiceConstants.RELOAD_CONFIGURATION, methodParamValues, methodParamDataTypes);
	}
	
	/**
	 * Stop server instance running at port
	 */
	public void stopServer(){
		invoke(EC_ENG_SERVER, RestServiceConstants.STOP_SERVER, null, null);
	}
	
	/**
	 * Get's the clients from the Mediation Engine
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ClientData> clients(){
		List<ClientData> clientsDetailsList = (List<ClientData>) invoke("Elitecore:type=-CrestelCLICommand", "clients", null, null);
		for(ClientData client : clientsDetailsList ){
			logger.debug("ClientIP: " + client.getClientIP());
			logger.debug("RequestExpiryTime: " + client.getRequestExpiryTime());
			logger.debug("SecurityMode: " + client.getSecurityMode());
			logger.debug("SharedSecret: " + client.getSharedSecret());
			logger.debug("TimeZone: " + client.getTimeZone());
			logger.debug("VendorID: " + client.getVendorID());
			logger.debug("VendorName: " + client.getVendorName());
			logger.debug("VendorType: " + client.getVendorType());
		}
		return clientsDetailsList;
	}
	
	/**
	 * @deprecated
	 * @return 
	 */
	@Deprecated
	public String immediateCounterDetails(){
		String strMessage = (String) invoke("Elitecore:type=-CrestelImidiateCountercmd", RestServiceConstants.IMMEDIATE_COUNTER_DETAILS,null, null);
		logger.debug("strMessage : " + strMessage);
		return strMessage;
	}
	
	/**
	 * Checks for License exist or not 
	 * @return
	 */
	public String isLicenseExist(){
		String licenseStatus = (String) invoke(EC_ENG_LIC,RestServiceConstants.IS_LICENCE_EXISTS,null,null);
		logger.debug("isLicenseExist response: " + licenseStatus);
		return licenseStatus;
	}
	
	/**
	 * Provides the license duration left to expired.
	 * @return
	 */
	public int licenseDuration(){
		int dateDiff = (Integer) invoke(EC_ENG_LIC,RestServiceConstants.LICENCE_DURATION,null,null);
		logger.debug("licenseDuration response : " + dateDiff);
		return dateDiff;
	}
	
	/**
	 * server id details as map via JMX call
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> serverIdDetails(){
		Map<String, String> serverDetailMap = (Map<String, String>) invoke(EC_ENG_LIC, RestServiceConstants.SERVER_ID_DETAILS, null,null);
		logger.debug("serverIdDetails response: " + serverDetailMap);
		return serverDetailMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> licenseInfo(){
		Map<String, String> serverLicenseInfoMap = (HashMap<String, String>) invoke(EC_ENG_LIC,RestServiceConstants.LICENCE_INFO,null,null);
		logger.debug("licenseInfo response : " + serverLicenseInfoMap);
		return serverLicenseInfoMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> licenseFullInfo(){
		Map<String, String> serverLicenseInfoMap = (HashMap<String, String>) invoke(EC_ENG_LIC,RestServiceConstants.LICENCE_FULL_INFO,null,null);
		logger.debug("licenseFullInfo response : " + serverLicenseInfoMap);
		return serverLicenseInfoMap;
	}
	
	public boolean activateFullVersion(byte[] buffer){
		Object[] methodParamValues = {buffer};
		String[] methodParamDataTypes = {"[B"};
		String result = (String) invoke(EC_ENG_LIC, RestServiceConstants.ACTIVATE_FULL_VERSION , methodParamValues, methodParamDataTypes);
		logger.debug("activateFullVersion response : " + result);
		if(result!=null && "success".equalsIgnoreCase(result)) {
			return true;
		}
		return false;
	}
	
	public String resetServiceCounterDetails(String netServiceAlias, String instanceId ){
		Object[] objArgValues = { netServiceAlias + "-" + instanceId };
		String[] strArgTypes = { EC_ENG_STRINGCLASS };
		String counterStatusMessage = (String) invoke("Elitecore:type=-CrestelResetSerivceCountercmd",RestServiceConstants.RESET_SERVICE_COUNTER_DETAILS_JSON, objArgValues, strArgTypes );
		logger.debug("resets the service Counter Details response :"+counterStatusMessage);
		return counterStatusMessage;
	}
	
	/*
	 * for all instances of the same service type 
	 */
	public String serviceCounterDetails(String netServiceAlias){
		Object[] objArgValues = {netServiceAlias};
		String[] strArgTypes =  { EC_ENG_STRINGCLASS };
		String counterStatusMessage = (String) invoke(EC_ENG_COUNTER_CMD,RestServiceConstants.SERVICE_COUNTER_DETAILS, objArgValues, strArgTypes);
		logger.debug("serviceCounterDetails(netServiceAlias) response :"+counterStatusMessage);
		return counterStatusMessage;
	}
	
	/*
	 * to be called when counter stats of all services is needed.
	 * return data will be in format 
	 */
	public String serviceCounterDetails(){
		Object[] objArgValues = { "-" };
		String[] strArgTypes = { EC_ENG_STRINGCLASS };
		String strMessage = (String) invoke(EC_ENG_COUNTER_CMD, "ServiceCounterDetails",objArgValues, strArgTypes);
		logger.debug(" all serviceCounterDetails response  :"+strMessage);
		return strMessage;
	}
	
	/*
	 * per service instance  id 
	 */
	public String serviceCounterDetails(String netServiceAlias, int instanceId ){
		Object[] objArgValues = { netServiceAlias + "-" + instanceId };
		String[] strArgTypes = { EC_ENG_STRINGCLASS };
		String counterStatusMessage = (String) invoke(EC_ENG_COUNTER_CMD,RestServiceConstants.SERVICE_COUNTER_DETAILS, objArgValues, strArgTypes);
		logger.debug("serviceCounterDetails response :"+counterStatusMessage);
		return counterStatusMessage;
	}
	
	
	/**
	 * Method will get service counter details by service id and alias.
	 * @param netServiceAlias
	 * @param instanceId
	 * @return
	 */
	public String getServiceCounterDetailsJSONData(String netServiceAlias, String instanceId ){
		Object[] objArgValues = { netServiceAlias + "-" + instanceId };
		String[] strArgTypes = { EC_ENG_STRINGCLASS };
		String jsonCounterDetails = (String) invoke("Elitecore:type=-CrestelSerivceSimpleCountercmd",RestServiceConstants.SERVICE_SIMPLE_COUNTER_DETAILS_JSON, objArgValues, strArgTypes);
		logger.debug("serviceCounterDetails response for single service :" + jsonCounterDetails);
		return jsonCounterDetails;
	}
	
	public CrestelNetServerDetails readServerDetails(){
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
		CrestelNetServerDetails eliteServerDetails = (CrestelNetServerDetails) invoke(EC_ENG_SERVER,RestServiceConstants.READ_SERVER_DETAILS,methodParamValues,methodParamDataTypes);
		logger.debug("server details are " + eliteServerDetails);
		return eliteServerDetails;
	}
	
	/**
	 * Appends the Server Instance Id in File at below location
	 * /home/elitecore/Mediation/workspace/mediation_engine/repository/modules/mediation/system/_sys.info{PORT}
	 * EX-: /home/elitecore/Mediation/workspace/mediation_engine/repository/modules/mediation/system/_sys.info4434
	 * 
	 * Called from CreateServerAction.java
	 * 
	 * 
	 * @param netServerCode
	 * @throws NoSuchEncryptionException
	 */
	public void writeServerInstanceId(String netServerCode) throws NoSuchEncryptionException{
		logger.debug(PasswordEncryption.crypt(netServerCode,PasswordEncryption.ELITECRYPT));
		Object[] methodParamValues = {PasswordEncryption.crypt(netServerCode,PasswordEncryption.ELITECRYPT),port};
		String[] methodParamDataTypes = {String.class.getName(), String.class.getName()};
		invoke(EC_ENG_SERVER, RestServiceConstants.WRITE_SERVER_INSTANCE_ID, methodParamValues, methodParamDataTypes);
	}
	
	/**
	 * Reads the Server Instance Id from File at below location
	 * /home/elitecore/Mediation/workspace/mediation_engine/repository/modules/mediation/system/_sys.info{PORT}
	 * EX-: /home/elitecore/Mediation/workspace/mediation_engine/repository/modules/mediation/system/_sys.info4434
	 * 
	 * @return
	 * @throws NoSuchEncryptionException
	 * @throws DecryptionNotSupportedException
	 */
	public String[] readServerInstanceId() throws NoSuchEncryptionException, DecryptionNotSupportedException{
		Object[] methodParamValues = {port};
		String[] methodParamDataTypes = {String.class.getName()};

        String[] serverInstanceIdArrays = (String[]) invoke(EC_ENG_SERVER, RestServiceConstants.READ_SERVER_INSTANCE_ID, methodParamValues, methodParamDataTypes);
		
        if(serverInstanceIdArrays!=null){
        	
        	for(int i=0;i<serverInstanceIdArrays.length;i++){
        		logger.debug("\t" + (i+1) + ": " + serverInstanceIdArrays[i] + " - " + PasswordEncryption.decrypt(serverInstanceIdArrays[i], PasswordEncryption.ELITECRYPT));
        	}
        }
        return serverInstanceIdArrays;
	}
	
	public void reloadCache(){
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
		invoke(EC_ENG_SERVER, RestServiceConstants.RELOAD_CACHE , methodParamValues, methodParamDataTypes);
	}
	
	@SuppressWarnings("unchecked")
	public List<CrestelAgentData> readAgentTasks(){
		List<CrestelAgentData> lstNetServerAgentData  ;
		Object[] methodParamValues = {};
		String[] methodParamDataTypes = {};
		lstNetServerAgentData = (ArrayList<CrestelAgentData>) invoke(EC_ENG_SERVER, RestServiceConstants.READ_AGENT_TASKS, methodParamValues, methodParamDataTypes);
		logger.debug("list of agents " +lstNetServerAgentData );
		return lstNetServerAgentData;
	}
	
	public void stopService(String serviceInstanceId){
		Object[] methodParamValues = {serviceInstanceId};
        String[] methodParamDataTypes = {String.class.getName()};
        invoke(EC_ENG_SERVER, RestServiceConstants.STOP_SERVICE, methodParamValues, methodParamDataTypes);
        	
	}
	
	public void startExistingService(String serviceInstanceId){
		Object[] methodParamValues = {serviceInstanceId};
		String[] methodParamDataTypes = {String.class.getName()};
		invoke(EC_ENG_SERVER, RestServiceConstants.START_EXISTING_SERVICE, methodParamValues, methodParamDataTypes);
	}
	
	public void stopAgent(String agentName){
		Object[] methodParamValues = {agentName};
		String[] methodParamDataTypes = {String.class.getName()};
        invoke(EC_ENG_SERVER, "stopAgent", methodParamValues, methodParamDataTypes);
	}
	
	public CrestelNetServiceData readServiceConfiguration(String alias, String instanceId, String serverInstanceVersionId){
		Object[] objArgValues = {alias, instanceId,serverInstanceVersionId};
		String[] strArgTypes = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		CrestelNetServiceData eliteNetServiceData = (CrestelNetServiceData)  invoke(EC_ENG_SERVICE,RestServiceConstants.READ_SERVICE_CONFIGURATION,objArgValues,strArgTypes);
		logger.debug(" read svc config from engine is " +eliteNetServiceData );
		return eliteNetServiceData;
	}
	
	public String readServiceConfiguration(CrestelNetDriverData eliteNetDriverData, String instanceId, String serverInstanceVersionId){
		String response ;
		Object[] methodParamValues = {eliteNetDriverData,instanceId,serverInstanceVersionId};
		String[] methodParamDataTypes = {CrestelNetDriverData.class.getName(),String.class.getName(),String.class.getName()};
		response = (String) invoke(EC_ENG_SERVICE, RestServiceConstants.READ_SERVICE_CONFIGURATION, methodParamValues, methodParamDataTypes);
		logger.debug("readServiceConfiguration response :"+response);
		return response;
	}

	public void stopService(String netServiceName, String netServiceInstance){
		Object[] argValue = {netServiceName,netServiceInstance};
		String[] argType = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		invoke(EC_ENG_SERVICE,"stopService",argValue,argType);
	}
	
	public void startService(String netServiceName, String netServiceInstance){
		Object[] argValue = {netServiceName,netServiceInstance};
		String[] argType = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		invoke(EC_ENG_SERVICE,"startService",argValue,argType);
	}
	
	public void reloadServiceCache(String netServiceName, String netServiceInstance){
		Object[] argValue = {netServiceName,netServiceInstance};
		String[] argType = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		invoke(EC_ENG_SERVICE,RestServiceConstants.RELOAD_SERVICE_CACHE,argValue,argType);
	}
	
	public void reloadServiceConfigurations(String netServiceName, String netServiceInstance){
		Object[] argValue = {netServiceName,netServiceInstance};
		String[] argType = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		invoke(EC_ENG_SERVICE,RestServiceConstants.RELOAD_SERVICE_CONFIGURATIONS,argValue,argType);
	}
	
	public CrestelNetServiceDetails readServiceDetails(String netServiceTypeAlias, String instanceId){
		Object[] argValue = {netServiceTypeAlias,instanceId};
		String[] argName = {EC_ENG_STRINGCLASS,EC_ENG_STRINGCLASS};
		CrestelNetServiceDetails eliteServiceDetails = (CrestelNetServiceDetails) invoke(EC_ENG_SERVICE,RestServiceConstants.READ_SERVICE_DETAILS,argValue,argName);
		logger.debug("read svc details " +eliteServiceDetails);
		return eliteServiceDetails;
	}
	
	/**
	 * Reads the Whole Server Instance Configuration
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<MISReportResponseData> getPacketStatsData(MISReportRequestData requestData){
		List<MISReportResponseData> responseData;
		String[] strArgTypes = { "com.elitecore.core.commons.util.data.MISReportRequestData" };
		Object[] objArgs = { requestData };
		responseData = (List<MISReportResponseData>) invoke( EC_ENG_MIS_PKTSTATS,RestServiceConstants.GET_PACKET_STATS_DATA, objArgs, strArgTypes);
		return responseData;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, ArrayList<String>> getServicesListByPort(int port,List<String> serviceTypeList){
		Object[] argValue = {port,serviceTypeList};
		String[] argName = {Integer.class.getName(),List.class.getName()};
		Object response = invoke(
									BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, 
									"getServicesListByPort", 
									argValue, 
									argName);
		if(response!=null){
			logger.debug("getServicesListByPort : "+(Map<String, ArrayList<String>>)response);
			return (Map<String, ArrayList<String>>)response;
		} else {
			logger.debug("getServicesListByPort : null");
			return null;
		}
	}
	
	
	/**
	 * Method will get all service details for give search criteria and batch id.
	 * @param batchId
	 * @param searchFileCriateriaData
	 * @return
	 */
	public String getServicesDetailsBySearchCriteria(String batchId, SearchFileCriteriaData searchFileCriateriaData ){
		logger.debug("Inside getServicesDetailsBySearchCriteria API call.");
		String response ;
		Object[] methodParamValues = {batchId,searchFileCriateriaData};
		String[] methodParamDataTypes = {String.class.getName(),SearchFileCriteriaData.class.getName()};
		response = (String) invoke(EC_ENG_FILE_REPROCESSING,RestServiceConstants.GET_LIST_OF_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("getServicesDetailsBySearchCriteria response : " + response);
		
		return response;
	}
	
	
	/**
	 * Method will reprocessErrorFiles for given configuration.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean reprocessErrorFiles(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside reprocessErrorFiles call.");
		boolean response ;
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		response = (Boolean) invoke(EC_ENG_FILE_REPROCESSING, RestServiceConstants.REPROCESS_LIST_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("reprocessErrorFiles response : " + response);
		return response;
	}
	
	/**
	 * Method will reprocessErrorFiles for given configuration.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean restoreArchiveFiles(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside resotreArchiveFiles call.");
		boolean response ;
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		response = (Boolean) invoke(EC_ENG_FILE_REPROCESSING, RestServiceConstants.RESTORE_LIST_ARCHIVE_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("resotreArchiveFiles response : " + response);
		return response;
	}
	
	
	/**
	 * Method will upload and reprocess file details.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean uploadAndReprocessFile(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside uploadAndReprocessFile API call.");
		boolean response ;
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		response = (Boolean) invoke(EC_ENG_FILE_REPROCESSING, RestServiceConstants.UPLOAD_AND_REPROCESS_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("reprocessErrorFiles response : " + response);
		return response;
	}
	
	
	/**
	 * Method will delete list of provided files from re-processing list.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean deleteErrorFiles(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside deleteErrorFiles API call.");
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		boolean response  = (Boolean) invoke(EC_ENG_FILE_REPROCESSING,RestServiceConstants.DELETE_LIST_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("deleteErrorFiles response : " + response);
		return response;
	}
	
	/**
	 * Method will modify error file list.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean modifyErrorFiles(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside modifyErrorFiles API call.");
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		boolean response  = (Boolean) invoke(EC_ENG_FILE_REPROCESSING,RestServiceConstants.MODIFY_LIST_OF_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("modifyErrorFiles response : " + response);
		return response;
	}
	
	
	/**
	 * Method will revert all files to original and remove all applied rule condition changes.
	 * @param batchId
	 * 
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	public boolean revertToOriginalFiles(String batchId, List<ServicewiseFileDetailsData> servicewiseFileDetailsData){
		logger.debug("Inside revertToOriginalFiles API method.");
		Object[] methodParamValues = {batchId,servicewiseFileDetailsData};
		String[] methodParamDataTypes = {String.class.getName(),EC_LIST_TYPE};
		boolean response  = (Boolean) invoke(EC_ENG_FILE_REPROCESSING,RestServiceConstants.REVERT_TO_ORIGINAL_LIST_OF_ERROR_FILES, methodParamValues, methodParamDataTypes);
		logger.debug("revertToOriginalFiles response : " + response);
		return response;
	}
	
	/**
	 * Method will revert all files to original and remove all applied rule condition changes.
	 * @param batchId
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> downloadFile(FileDetailsData fileDetailsData, Integer batchRecordSize){
		logger.debug("Inside downloadFile API method.");
		Map<String, Object> response ;
		Object[] methodParamValues = {fileDetailsData,batchRecordSize};
		String[] methodParamDataTypes = {FileDetailsData.class.getName(),Integer.class.getName()};
		response = (Map<String, Object>) invoke(EC_ENG_FILE_REPROCESSING,RestServiceConstants.DOWNLOAD_FILE, methodParamValues, methodParamDataTypes);
		return response;
	}
	
	/**
	 * @param servicewiseFileDetailsData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getBytesOfFile(FileDetailsData fileDetailsData){
		logger.debug("Inside getBytesOfFile API method.");
		Object[] methodParamValues = {fileDetailsData};
		String[] methodParamDataTypes = {FileDetailsData.class.getName()};
		return (Map<String, Object>) invoke(EC_ENG_FILE_REPROCESSING, "getBytesOfFile", methodParamValues, methodParamDataTypes);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Map<String,Integer>> reloadLookupDataCache(List<String> dbQueryList, Date lookupReloadDate, String reloadOption){
		Object[] argValue = {dbQueryList,lookupReloadDate, reloadOption};
		String[] argType = {EC_LIST_TYPE,EC_ENG_DATECLASS,EC_ENG_STRINGCLASS};
		return (Map<String, Map<String,Integer>>) invoke(EC_ENG_LOOKUP_DATA_RELOAD_CACHE,RestServiceConstants.LOOKUP_DATA_RELOAD,argValue,argType);
	}
	
	public boolean isConditionExpressionValid(String expressionStr){
		Object[] argValue = {expressionStr};
		String[] argType = {EC_ENG_STRINGCLASS};
		boolean isValid = (boolean) invoke(EC_ENG_EXPRESSION_VALIDATE,RestServiceConstants.IS_CONDITION_EXP_VALID,argValue,argType);
		return isValid;
	}
	
	public boolean isActionExpressionValid(String expressionStr){
		Object[] argValue = {expressionStr};
		String[] argType = {EC_ENG_STRINGCLASS};
		boolean isValid = (boolean) invoke(EC_ENG_EXPRESSION_VALIDATE,RestServiceConstants.IS_ACTION_EXP_VALID,argValue,argType);
		return isValid;
	}
	
	 /**
     * Get Snmp V3 Engine ID via JMX call
     * @return
     */
    public String[] getSNMPV3EngineId(String ipAddress, int port){
    	Object response = invoke(SNMP_V3_ENGINE_ID, RestServiceConstants.RETRIVE_SNMPV3_ENGINE_ID, new Object[]{ipAddress,port}, 
			new String[]{String.class.getName(),Integer.class.getName()});

    	if(response!=null)
    		return (String[])response;
    	return null;
    }
   	
    public boolean upgradeDefaultLicense(byte[] buffer){
		Object[] methodParamValues = {buffer};
		String[] methodParamDataTypes = {"[B"};
		String result = (String) invoke(BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, "upgradeDefaultLicense" , methodParamValues, methodParamDataTypes);
		logger.debug("upgradeDefaultLicense response : " + result);
		if(result!=null && "success".equalsIgnoreCase(result)) {
			return true;
		}
		return false;
	}
    
    /**
     * Get activateDefaultFullLicence via JMX call
     * @return
     */
    public boolean activateDefaultFullLicence(){
    	Object response = invoke(BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME, "activateDefaultFullLicence", new Object[]{}, new String[]{});
    	logger.debug("activateDefaultFullLicence response : " + response.toString());
    	return (boolean)response;
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, String> getLicenseInfo(){
		Map<String, String> serverLicenseInfoMap = (HashMap<String, String>) invoke(BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME,"licenseInfo",null,null);
		logger.debug("licenseInfo response : " + serverLicenseInfoMap);
		return serverLicenseInfoMap;
	}
    
    public boolean syncDictionaryData(String fileName, String filePath, String ipAddress,int port, byte[] dicFile){
		Object isSyncSuccess = invoke(
							BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME,
							"syncDictionaries", 
							new Object[]{ipAddress,port, dicFile, fileName, filePath}, 
							new String[]{String.class.getName(),Integer.class.getName(), Object.class.getName(), String.class.getName(), String.class.getName()});
		if(isSyncSuccess!=null){
			logger.debug("isSyncSuccess : "+isSyncSuccess);
			return (Boolean) isSyncSuccess;
		} else {
			logger.debug("isSyncSuccess : false");
			return false;
		}
	}
    
    public boolean syncKeystoreFile(String fileName, String filePath, byte[] file){
		Object isSyncSuccess = invoke(
							EC_ENG_SERVICE,
							"syncKeystoreFile", 
							new Object[]{fileName, filePath, file}, 
							new String[]{String.class.getName(), String.class.getName(), Object.class.getName()});
		if(isSyncSuccess!=null){
			logger.debug("isSyncSuccess : "+isSyncSuccess);
			return (Boolean) isSyncSuccess;
		} else {
			logger.debug("isSyncSuccess : false");
			return false;
		}
	}
    
    public boolean testFtpSftpConnection(String ipAddress, Integer port, String username,String password,String strKeyFileLocation,Integer maxRetrycount,String driverType){
		Object isConnected = invoke(
							BaseConstants.SERVER_MGMT_JMX_OBJECT_NAME,
							"testFtpSftpConnection", 
							new Object[]{ipAddress,port, username,password,strKeyFileLocation,maxRetrycount,driverType}, 
							new String[]{String.class.getName(),Integer.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(),Integer.class.getName(),String.class.getName()});
		if(isConnected!=null){
			logger.debug("isConnected : "+isConnected);
			return (Boolean) isConnected;
		} else {
			logger.debug("isConnected : false");
			return false;
		}
	}

    
}