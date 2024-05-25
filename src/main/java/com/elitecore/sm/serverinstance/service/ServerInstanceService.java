package com.elitecore.sm.serverinstance.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.elitecore.core.util.mbean.data.config.CrestelNetServerData;
import com.elitecore.sm.agent.model.ServicePacketStatsConfig;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.datasource.model.DataSourceConfig;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author avani.panchal
 *
 */ 
public interface ServerInstanceService {
	
	public ServerInstance getIDByIpPortUtility(String ip,int port,int utilityPort);
	
	public boolean isServerInstanceUnique(ServerInstance serverInstance);

	public ResponseObject addServerInstance(ServerInstance serverInstance,String createMode,Map<String,Object> mReqParam) throws SMException;

	public ServerInstance getServerInstance(int id);
	
	public List<ServerInstance> getServerInstanceList();

	public ResponseObject updateServerInstance(ServerInstance serverInstance);
	
	public ResponseObject getServerInstanceDepedants(ServerInstance serverInstance);

	public List<ServerInstance> getServerInstanceByServerId(int serverId);

	@SuppressWarnings("rawtypes")
	public Map<String, List> getAllServerAndItsInstance();
	
	public ResponseObject resetSyncFlagForServiceandSI(List<Service> serviceList,ServerInstance serverInstance);
	
	public ResponseObject getServerInstanceFullHierarchy(int serverInstanceId,boolean isExportForDelete,String tempPathForExport) throws SMException;
	
	public ResponseObject importServerInstanceConfig(int serverInstanceId, File importFile,int staffId,int importMode,String jaxbXmlPath,boolean isCopy) throws SMException;
	
	public ResponseObject updateInstanceAdvanceConfig(ServerInstance serverInstance);
	
	public ResponseObject updateInstanceSystemLog(ServerInstance serverInstance);
	
	public ResponseObject updateInstanceStatistic(ServerInstance serverInstance,String serviceList);
	
	public ResponseObject deleteServerInstance(int serverInstancesId,int staffid,String jaxbXmlPath) throws SMException;
	
	public Map<String, ResponseObject> copyServerInstanceConfig(int copyFromId,String copyToIds,int staffId,String tempPathForExport,String jaxbXmlPath) throws SMException;
	
	public ResponseObject exportServerInstanceConfig(int serverInstanceId,boolean isExportBeforeDelete,String tempPathForExport) throws SMException;
	
	public ResponseObject updateSNMPStatus(int serverInstanceId,boolean status);
	
	public ResponseObject deleteServerInstanceConfig(ServerInstance serverInstance);
	
	public ResponseObject updateFileStateInDB(int serverInstanceId,boolean status);
	
	public ResponseObject updateWebServiceStatus(int serverInstanceId,boolean status);
	
	public ResponseObject updateRestWebServiceStatus(int serverInstanceId,boolean status);
	
	public ResponseObject reloadConfiguration(int serverInstanceId);

	public ResponseObject softRestartInstance(int serverInstanceId);
	
	public ResponseObject stopInstance(int serverInstanceId);
	
	public ResponseObject startInstance(int serverInstanceId);
	
	public ResponseObject restartInstance(int serverInstanceId);
	
	public ResponseObject addServerInstanceInDB(ServerInstance serverInstance,Map<String,Object> mReqParam) throws SMException;
	
	public Map<String, ResponseObject> syncServerInstance(Map<String,String> syncInputMap);
	
	public ResponseObject loadInstanceStatus(int serverInstanceId);
	
	public ResponseObject addFreePort(ServerInstance serverInstance) throws SMException;
	
	public ResponseObject updateFreePort(ServerInstance serverInstance) throws SMException;
	
	public ResponseObject addUsedPort(ServerInstance serverInstance);
	
	public ResponseObject uploadImportFile(MultipartFile file,String tempImportPath) ;
	
	public ResponseObject reloadCache(int serverInstanceId, String reloadType, String databaseQuery);

	public ResponseObject updateStatus(int serverInstanceId) throws SMException;
	
	public ResponseObject checkPortAvailibility(int serverInstanceId,int port);
	
	public ResponseObject getAllInstancesByServerType(int serverInstanceId, boolean isCreate);

	public List<ServerInstance> getServerList();
	
	public ResponseObject validateMigrationDetails(ServerInstance serverInstance);
	
	public ResponseObject addMigratedServerInstance(ServerInstance serverInstance);

	public void updateSynchStatus(ServerInstance serverInstance);
	
	public boolean  isInstancePortUnique(int port,String ipaddress);
	
	public ResponseObject  getServerInstanceByIPAndPort(String ipaddress, int port);
	
	public ResponseObject addServerInstance(ServerInstance serverInstance);
	
	public ResponseObject validateMigrationServerInstance(ServerInstance serverInstance);
	
	public ServerInstance getServerInstanceByName(String name);
	
	public DataSourceConfig updateDataSourceDetails(DataSourceConfig dsConfig, int staffId, String ipAddress);
	
	public boolean checkDBConfigUniqueName(String name);

	public long getTotalServerInstanceCount(String serverTypeId, String searchInstanceName, String searchHost, String searchServerName, String searchPort,
			String searchSyncStatus, String dsid);

	public List<ServerInstance> getPaginatedList(String serverTypeId, String searchInstanceName, String searchHost, String searchServerName,
			String searchPort, String searchSyncStatus, int startIndex, int limit, String sidx, String sord, String dsid);
	
	public Service getServiceFromServerInstance(ServerInstance serverInstance, String serviceName);
	
	public Service getServiceFromServerInstance(ServerInstance serverInstance, ServicePacketStatsConfig servicePacketStatsConfig);
	
	public ServerInstance getServerInstanceListBySerInsId(int serverInstanceId);
	
	public ResponseObject getServerInstanceJAXB(String exportXmlPath, int serverInstanceId) throws SMException;
	
	public ResponseObject publishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId);
	
	public void migrateAllPassword();
	
	public void saveOrUpdateAutoConfigSI(CrestelNetServerData serverData, int staffId,String ipAddress, int utilityPort, int port);
	
	public void generateNetServerDataFromServerInsance(ServerInstance serverInstance, CrestelNetServerData serverData );
	
	public ResponseObject migrateExistingServerInstanceConfigInDB(Map<String, String> syncInputMap, ServerInstance serverInstance);
	
	public ResponseObject updateSyncFlagForServiceandSI(List<Service> serviceList, ServerInstance serverInstance, boolean flag);
	
	public ResponseObject migrateAllServerInstanceConfigInDB();
	
	public void migrateVersion();
	
	public ResponseObject deleteServerInstanceOnlyInSM(int serverInstanceId, int staffId, String jaxbXmlPath) throws SMException;
}
