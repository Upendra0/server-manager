/**
 * 
 */
package com.elitecore.sm.samples;

import java.util.ArrayList;
import java.util.List;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.config.model.EntitiesRegex;
import com.elitecore.sm.config.service.ConfigService;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.CommonPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
	
/**	
 * @author Sunil Gulabani
 * Aug 6, 2015
 */
public class EntitiesRegexSamples {
	
	private void setAccessGroupEntitiesRegex(List<EntitiesRegex> systemParametersList ){
		systemParametersList.add(new EntitiesRegex(
				AccessGroup.class.getSimpleName(),
				SystemParametersConstant.ACCESSGROUP_NAME,
				"^[([a-zA-Z]+\\s+)*[a-zA-Z]+]{1,60}$",
				"Access Group Name Regex"));
		systemParametersList.add(new EntitiesRegex(
				AccessGroup.class.getSimpleName(),
				SystemParametersConstant.ACCESSGROUP_DESCRIPTION,
				"^[a-zA-Z\\s]{0,250}$", "Access Group Description Regex"));
		systemParametersList.add(new EntitiesRegex(
				AccessGroup.class.getSimpleName(),
				SystemParametersConstant.ACCESSGROUP_REASON_TO_CHANGE,
				"^[a-zA-Z\\s]{1,250}$",
				"Access Group Change Status - Reason for Change"));
	}
	
	private void setStaffEntitiesRegex(List<EntitiesRegex> systemParametersList ){
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_USERNAME, 
				"^(?![_\\-.])(?!.*[_.-]$)[a-zA-Z0-9.\\-_][a-zA-Z0-9.\\-_]{1,255}+$",
				"Username regex"));
		
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_FIRSTNAME,
				"^[a-zA-Z\\s]{1,255}$", 
				"First Name regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_LASTNAME,
				"^[a-zA-Z\\s]{1,255}$",
				"Last Name regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_STAFFCODE,
				"^[a-zA-Z0-9]{1,255}$",
				"Staff Code regex"));

		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_REASON_FOR_CHANGE_REGEX,
				"^[a-zA-Z\\s]{1,250}$",
				"Staff Change Status - Reason for Change"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_MIDDLENAME,
				"^[a-zA-Z\\s]{0,255}$",
				"Middle name regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_ADDRESS,
				"^[a-zA-Z0-9\\s]{1,500}$",
				"Address regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_ADDRESS2,
				"^[a-zA-Z0-9\\s]{0,500}$",
				"Address2 regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_CITY,
				"^[a-zA-Z]{1,255}$",
				"City regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_STATE,
				"^[a-zA-Z]{0,255}$",
				"State regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_COUNTRY,
				"^[a-zA-Z]{0,255}$",
				"Country regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_PINCODE,
				"^[0-9]{1,255}$",
				"Pincode regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_LANDLINENO,
				"^[0-9]{0,20}$",
				"Landline regex"));
		systemParametersList.add(new EntitiesRegex(
				Staff.class.getSimpleName(),
				SystemParametersConstant.STAFF_MOBILENO, 
				"^[0-9]{1,20}$",
				"Mobile Number regex"));
	}
	
	private void setServerEntitiesRegex(List<EntitiesRegex> systemParametersList ){
		systemParametersList.add(new EntitiesRegex(
				Server.class.getSimpleName(),
				SystemParametersConstant.SERVER_NAME,
				"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,500}+$",
				"Server Name"));

		systemParametersList.add(new EntitiesRegex(
				Server.class.getSimpleName(),
				SystemParametersConstant.SERVER_DESCRIPTION,
				"^[a-zA-Z0-9\\s./-]{0,2000}$",
				"Server Description"));
		
		systemParametersList.add(new EntitiesRegex(
				Server.class.getSimpleName(),
				SystemParametersConstant.SERVER_UTILITYPORT,
				"(102[3-9]|10[3-9][0-9]|1[1-9][0-9]{2}|[2-9][0-9]{3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-6])",
				"Server Utility Port"));
	}
	
	private void setServerInstanceEntitiesRegex(List<EntitiesRegex> systemParametersList ){
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_LOG_FILEPATH_REGEX,
				"^[0-9a-zA-Z_/]{1,200}$",
				"Server Instance Logfile Path"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_FILESTORAGENAME_REGEX,
				"^[0-9a-zA-Z_/]{1,200}$",
				"Server Instance File Storage Destination Path"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_NAME,
				"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,500}+$",
				"Server Instance Name"));

		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_DESCRIPTION,
				"^[a-zA-Z0-9\\s./-]{0,2000}$",
				"Server Instance Description"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_PORT,
				"(102[4-9]|10[3-9][0-9]|1[1-9][0-9]{2}|[2-9][0-9]{3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])",
				"Server Instance port"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_MAXCONNECTIONRETRY,
				"^[0-9]{1,5}+$",
				"Server Instance Try To Connect Count"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_RETRYINTERVAL,
				"^[0-9]{1,5}+$",
				"Server Instance Connection Retry Interval"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_CONNECTIONTIMEOUT,
				"^[0-9]{1,20}+$",
				"Server Instance Connection Timeout"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_FILESTORAGELOCATION,
				"^[0-9a-zA-Z_/]{1,200}$",
				"Server Instance File Storage Location Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_DATASOURCECONFIG_DSPOJO_NAME,
				"^[0-9a-zA-Z_/]{1,200}$",
				"Server Instance DsConfig Pojo name"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_MINMEMORYALLOCATION,
				"^[0-9]{1,20}+$",
				"Server Instance Min Memory Allocation"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_MAXMEMORYALLOCATION,
				"^[0-9]{1,20}+$",
				"Server Instance Max Memory Allocation"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_THRESHOLDTIMEINTERVAL,
				"^[0-9]{1,10}$",
				"Server Instance Threshold Time Interval Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_THRESHOLDMEMORY,
				"^[0-9]{1,10}$",
				"Server Instance Threshold Memory Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_LOADAVERAGE,
				"^[0-9]{1,10}$",
				"Server Instance Load Average Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_LOGSDETAIL_LOGPATHLOCATION,
				"^[0-9a-zA-Z_/]{0,200}$",
				"Server Instance LogsDetail Log Path Location Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_LOGSDETAIL_MAXROLLINGUNIT,
				"^[0-9]{1,200}$",
				"Server Instance LogsDetail Max Rolling Unit Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_LOGSDETAIL_ROLLINGVALUE,
				"^[0-9]{1,200}$",
				"Server Instance LogsDetail Rolling Value Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_MINDISKSPACE,
				"-1|([0-9]{1,10})$",
				"Server Instance Min Disk Space Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				ServerInstance.class.getSimpleName(),
				SystemParametersConstant.SERVERINSTANCE_SCRIPTNAME,
				"^[0-9a-zA-Z._]{0,100}+$",
				"Server Instance Script Name"));
	}
	
	private void setDataSourceConfigEntitiesRegex(List<EntitiesRegex> systemParametersList ){
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_NAME_REGEX,
				"^([a-zA-Z1-9@_:.]+){0,255}$",
				"Datasourceconfig Name Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_CONNURL_REGEX,
				"^([a-zA-Z1-9@_:.]+){0,255}$",
				"Datasourceconfig Connection URL Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_TYPE_REGEX,
				"^(1-9){0,255}$",
				"Datasourceconfig Type Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_USERNAME_REGEX,
				"^([a-zA-Z1-9@_]+){0,255}$",
				"Datasourceconfig Username Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_PASSWORD_REGEX,
				"^([a-zA-Z1-9@_]+){0,255}$",
				"Datasourceconfig Password Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_MINPOOLSIZE_REGEX,
				"^[1-9]{1,10}+$",
				"Datasourceconfig Min Pool Size Regex"));
		
		systemParametersList.add(new EntitiesRegex(
				DataSourceConfig.class.getSimpleName(),
				SystemParametersConstant.DATASOURCECONFIG_MAXPOOLSIZE_REGEX,
				"^[1-9]{1,10}+$",
				"Datasourceconfig Max Pool Size Regex"));
	}
	
		
	private void setServiceEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_NAME,"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,500}+$","Service Name"));

		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_DESCRIPTION,"^[a-zA-Z0-9\\s./-_,]{0,2000}$","Service Description"));
		
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SERVICESCHEDULINGPARAMS_TIME,
																					"^(([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]){1,8}$","Service Scheduling time"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SVCEXECPARAMS_EXECUTIONINTERVAL,
																					"^[0-9]{1,5}$","Service Excecuation interval"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SVCEXECPARAMS_FILEBATCHSIZE,
																					"^[0-9]{1,3}$","Service File Batch size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SVCEXECPARAMS_MAXTHREAD,
																					"^[0-9]{1,3}$","Service Max thread"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SVCEXECPARAMS_MINTHREAD,
																					"^[0-9]{1,3}$","Service Min Thread"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SVCEXECPARAMS_QUEUESIZE,
																					"^[0-9]{1,7}$","Service Queue Size"));

	}
	
	private void setNetflowCollectionServiceEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OPTIONTEMPLATEID,
																					"^[0-9]{0,50}$","Service Option Template Id"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OPTIONTEMPLATEKEY,
																					"^[a-zA-Z0-9]{0,50}$","Service Option Template Key"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OPTIONTEMPLATEVALUE,
																					"^[a-zA-Z0-9]{0,50}$","Service Option Template Value"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OPTIONCOPYTOTEMPLATEID,
																					"^[0-9,]{0,50}$","Service Option Copy To Template Id"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OPTIONCOPYTOFIELD,
																					"^[0-9a-zA-Z]{0,50}$","Service Option Copy To Field"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_BINARYFILELOCATION,
																						"^[0-9a-zA-Z_/]{0,200}$","Binary File Location"));
		}
	
	private void setNetflowBinaryCollectionServiceEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_NETFLOWPORT,
																					"^[0-9]{0,10}$","Service Netflow Port"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SKTRCVBUFFERSIZE,
																					"^[0-9]{0,19}$","Service Socket Receive Buffer Size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SKTSENDBUFFERSIZE,
																					"^[0-9]{0,19}$","Service Socket Send Buffer Size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_BULKWRITELIMIT,
																					"^[0-9]{0,10}$","Service Bulk Write Limit"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_MAXPKTSIZE,
																					"^[0-9]{0,19}$","Max Packet Size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_MAXWRITEBUFFERSIZE,
																					"^[0-9]{0,19}$","Service Max Write Buffer size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_PARALLELFILEWRITECOUNT,
																					"-1|([0-9]{0,10})$","Service Parallel File Write Count"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_SNMPTIMEINTERVAL,
																					"^[0-9]{0,10}$","Service SNMP Time Interval"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_MAXIDELCOMMUTIME,
																					"^[0-9]{0,10}$","Service Max Idel Communication Time"));
	}
	
	private void setIplogParsingServiceEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_EQUALCHECKFIELD,
																					"[^a-zA-Z0-9-]{0,20}$","Service Equal Check Field"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_EQUALCHECKVALUE,
																					"^[^a-zA-Z0-9]{0,100}$","Service Equal Check Value"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_FILEGROUPPARAM_ARCHIVEPATH,
																					"^[0-9a-zA-Z_/]{0,600}$","Service Archive Path"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_RECORDBATCHSIZE,
																					"^[0-9]{1,10}$","Service Record Batch Size"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_PURGEINTERVAL,
																					"^[0-9]{1,5}$","Service Purge Interval"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_PURGEDELAYINTERVAL,
																					"^[0-9]{1,5}$","Service Purge Delay Interval"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_OUTFILEHEADERS,
																					"^[^a-zA-Z0-9-]{0,500}$","Service Outfile Headers"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_FILESTATSLOC,
																					"^[0-9a-zA-Z_/]{0,600}$","Service File State Location"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_DESTPORTFIELD,
																					"^[0-9]{0,10}$","Service Destination Port Field"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_DESTPORTFILTER,
																					"^[0-9]{0,10}$","Service Destination Port Filter"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_CREATERECDESTPATH,
																					"^[0-9a-zA-Z_/]{0,600}$","Service Create Record Dest Path"));
		systemParametersList.add(new EntitiesRegex(Service.class.getSimpleName(),SystemParametersConstant.SERVICE_DELETERECDESTPATH,
																					"^[0-9a-zA-Z_/]{0,600}$","Service Delete Record Dest Path"));
	}
	
	private void setDriversEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(Drivers.class.getSimpleName(),SystemParametersConstant.DRIVERS_NAME,"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,255}+$","Driver Name"));

		systemParametersList.add(new EntitiesRegex(Drivers.class.getSimpleName(),SystemParametersConstant.DRIVERS_TIMEOUT,"^[0-9]{1,7}$","Driver TimeOut"));

		systemParametersList.add(new EntitiesRegex(Drivers.class.getSimpleName(),SystemParametersConstant.DRIVERS_MINFILERANGE,"^[0-9]{1,10}$","Driver Min file range"));
		
		systemParametersList.add(new EntitiesRegex(Drivers.class.getSimpleName(),SystemParametersConstant.DRIVERS_MAXFILERANGE,"^[0-9]{1,10}$","Driver Max file range"));
		
		systemParametersList.add(new EntitiesRegex(Drivers.class.getSimpleName(),SystemParametersConstant.DRIVERS_MAXRETRYCOUNT,"^[0-9]{1,10}$","Driver Max Retry count"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriver.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVER_NOFILEALERT,"^[0-9-]{0,5}$","Collection Driver No file alert interval"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriver.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVER_FILEGROUPINGPARAMETER_ARCHIVEPATH,"^[\\a-zA-Z_$0-9:/]{1,500}$","Collection Driver Archive path"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PORT,"^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$","FTP Collection Driver port"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_USERNAME,"^.{1,200}$","FTP Collection Driver username"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,"^.{1,200}$","FTP Collection Driver password"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_TIMEOUT,"^[0-9]{1,7}$","FTP Collection Driver timeout"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_FILESEPARATOR,"^[:/\\\\;]{1}$","FTP Collection Driver fileSeparator"));
		
		systemParametersList.add(new EntitiesRegex(FTPCollectionDriver.class.getSimpleName(),SystemParametersConstant.FTPCOLLECTIONDRIVER_MYFILEFETCHPARAMS_FILEFETCHINTERVALMIN,"^[0-9]{1,3}$","FTP Collection Driver file fetch interval"));

	}
	
	private void setPathListEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(PathList.class.getSimpleName(),SystemParametersConstant.PATHLIST_NAME,"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,500}+$","PathList Name"));

		systemParametersList.add(new EntitiesRegex(PathList.class.getSimpleName(),SystemParametersConstant.PATHLIST_READFILEPATH,"^[\\a-zA-Z_$0-9:/]{1,500}$","PathList read File Path"));
		
		systemParametersList.add(new EntitiesRegex(CommonPathList.class.getSimpleName(),SystemParametersConstant.COMMONPATHLIST_WRITEFILEPATH,"^[\\a-zA-Z_$0-9:/]{1,500}$","PathList write File Path"));
		
		systemParametersList.add(new EntitiesRegex(CommonPathList.class.getSimpleName(),SystemParametersConstant.COMMONPATHLIST_READFILENAMEPREFIX,"^[^<>:\"/\\|?*]{0,100}$","PathList ReadFileName Prefix"));
		
		systemParametersList.add(new EntitiesRegex(CommonPathList.class.getSimpleName(),SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX,"^[^<>:\"/\\|?*]{0,100}$","PathList ReadFileName Suffix"));
		
		systemParametersList.add(new EntitiesRegex(CommonPathList.class.getSimpleName(),SystemParametersConstant.COMMONPATHLIST_READFILENAMECONTAINS,"^[^<>:\"/\\|?*]{0,100}$","PathList ReadFileName Containts"));
		
		systemParametersList.add(new EntitiesRegex(CommonPathList.class.getSimpleName(),SystemParametersConstant.COMMONPATHLIST_WRITEFILENAMEPREFIX,"^[^<>:\"/\\|?.*]{0,100}$","PathList WriteFileName Prefix"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXFILESCOUNTALERT,"^[0-9-]{1,7}$","PathList Max File Count Alert"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,"([dMyhms0-9//:-]+){1,30}","PathList Date Format"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX,"^[0-9]{0,7}$","PathList Start Index"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX,"^[0-9]{0,7}$","PathList End Index"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQSTARTINDEX,"^[0-9]{0,7}$","PathList Seq. Start Index"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQENDINDEX,"^[0-9]{0,7}$","PathList Seq. End Index"));
		
		systemParametersList.add(new EntitiesRegex(CollectionDriverPathList.class.getSimpleName(),SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXCOUNTERLIMIT,"^[0-9]{0,7}$","PathList max Counter Limit"));
		
	}
	
	private void setNetflowClientEntitiesRegex(List<EntitiesRegex> systemParametersList){
		
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_NAME,"^[a-zA-Z0-9.-_,][a-zA-Z0-9\\s.-_,]{1,250}+$","Client Client Name"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_CLIENTPORT,"^[0-9]{0,10}+$","Netflow Client Port"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_FILENAMEFORMAT,"^[a-zA-Z0-9{}.-_\\s]{1,50}+$","Client File Name Format"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_MINFILESEQVALUE,"^[0-9]{0,19}+$","Client Min File Sequence"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_MAXFILESEQVALUE,"^[0-9]{0,19}+$","Client Max File Sequence"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_OUTFILELOCATION,"^[\\a-zA-Z0-9_$:/]{1,500}+$","Output File Location"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_VOLLOGROLLINGUNIT,"-1|([0-9]{0,10})+$","Volume Rolling Unit"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_TIMELOGROLLINGUNIT,"-1|([0-9]{0,10})+$","Time Rolling Unit"));
		systemParametersList.add(new EntitiesRegex(NetflowClient.class.getSimpleName(),
				SystemParametersConstant.NETFLOWCLIENT_BKPBINARYFILELOCATION,"^[\\a-zA-Z0-9_$:/]{1,500}+$","Backup Binary File Path"));

	}
		
	public void addEntitiesRegex(ConfigService configService){
		List<EntitiesRegex> systemParametersList = new ArrayList<>();
		
		setAccessGroupEntitiesRegex(systemParametersList);
		setStaffEntitiesRegex(systemParametersList);
		setServerEntitiesRegex(systemParametersList );
		setServerInstanceEntitiesRegex(systemParametersList);
		setDataSourceConfigEntitiesRegex(systemParametersList);
		setServiceEntitiesRegex(systemParametersList);
		setDriversEntitiesRegex(systemParametersList);
		setPathListEntitiesRegex(systemParametersList);
		setNetflowCollectionServiceEntitiesRegex(systemParametersList);
		setNetflowBinaryCollectionServiceEntitiesRegex(systemParametersList);
		setNetflowClientEntitiesRegex(systemParametersList);
		setIplogParsingServiceEntitiesRegex(systemParametersList);
		
		configService.addEntitiesRegex(systemParametersList);
	}
}
