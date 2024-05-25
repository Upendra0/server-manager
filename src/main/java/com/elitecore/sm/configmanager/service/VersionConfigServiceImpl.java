package com.elitecore.sm.configmanager.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.configmanager.dao.VersionConfigDao;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.EliteUtils;


@org.springframework.stereotype.Service(value = "versionConfigService")
public class VersionConfigServiceImpl implements VersionConfigService {
	
	@Autowired
	private VersionConfigDao versionConfigDao;
	
	@Autowired
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	private ServerInstanceDao serverInstanceDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<VersionConfig> getVersionConfigList(String serverInstanceId) {
		return versionConfigDao.getVersionConfigList(serverInstanceId);
	}
	
	@Transactional(readOnly = true)
	@Override
	public int getVersionConfigCount(int serverInstanceId) {
		return versionConfigDao.getVersionConfigCount(serverInstanceId);
	}

	@Transactional(readOnly = true)
	@Override
	public VersionConfig getVersionConfigObj(int id) {
		VersionConfig versionConfigObj=versionConfigDao.getVersionConfigObj(id);
		return versionConfigObj;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	@Auditable(auditActivity = AuditConstants.RESTORE_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION,
	currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject restoreSyncPublishServerInstance(int serverInstanceId, int versionConfigId, String description, String servInstanceStatus, String tempPathForSyncPublish, String jaxbXmlPath, int staffId, Map<String,String> syncInputMap, ResponseObject oldResponseObject) throws Exception {
		ResponseObject responseObject = new ResponseObject();
		ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
		VersionConfig versionConfig = this.getVersionConfigObj(versionConfigId);
		File exportedFile = EliteUtils.convertBlobToFileContent(versionConfig, tempPathForSyncPublish);
		responseObject = serverInstanceService.importServerInstanceConfig(serverInstanceId, exportedFile,staffId,BaseConstants.IMPORT_MODE_OVERWRITE,jaxbXmlPath,false);
		if(responseObject.isSuccess()){
			responseObject.setObject(serverInstance);
			responseObject = this.syncPublishServerInstance(serverInstanceId,description,tempPathForSyncPublish,staffId,syncInputMap,oldResponseObject);
			if(!responseObject.isSuccess()){
				if(responseObject.getResponseCode() == ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL){
					oldResponseObject.setResponseCode(ResponseCode.RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
					responseObject.setResponseCode(ResponseCode.RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
					responseObject.setSuccess(false);
					responseObject.setObject("RESTORE_SYNC_PUBLIC_SUCCESS");
				} else {
					throw new Exception();//NOSONAR
				}
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.RESTORE_SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
			responseObject.setObject(null);
		}
		return responseObject;
	}	
	
	@Transactional
	@Override
	public ResponseObject syncPublishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId, Map<String,String> syncInputMap, ResponseObject oldResponseObject) throws Exception {//NOSONAR
		ResponseObject responseObject = new ResponseObject();
		Map<String, ResponseObject> responseMap = serverInstanceService.syncServerInstance(syncInputMap);
		ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
		if (responseMap != null) {
			for (Map.Entry<String, ResponseObject> response1 : responseMap.entrySet()) {
				if (response1.getValue().isSuccess()) {
					responseObject = serverInstanceService.publishServerInstance(serverInstanceId,description,tempPathForSyncPublish, staffId);
					responseObject.setObject(serverInstance);
				} else if (response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_UPDATE_SCRIPT_FAIL){
					responseObject = serverInstanceService.publishServerInstance(serverInstanceId,description,tempPathForSyncPublish, staffId);
					oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
					responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_UPDATE_SCRIPT_FAIL);
					responseObject.setSuccess(false);
				} else {
					responseObject.setSuccess(false);
					if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_CONNECTION_FAIL) {
						oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_JMX_CONNECTION_FAIL);
						responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_JMX_CONNECTION_FAIL);
					} else if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_JMX_API_FAIL) {
						oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_JMX_API_FAIL);
						responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_JMX_API_FAIL);
					} else if (response1.getValue().getResponseCode() == ResponseCode.SERVERINSTANCE_SYNC_FAIL_INACTIVE_STATUS) {
						oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_INACTIVE_STATUS_FAIL);
						responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_INACTIVE_STATUS_FAIL);
					} else if(response1.getValue().getResponseCode() == ResponseCode.SERVER_INSTANCE_SYNC_FAIL_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT){
						oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT);
						responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_DUE_TO_EMPTY_STORAGE_LOCATION_OF_PACKET_STATISTICS_AGENT);
					}else {
						oldResponseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
						responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
					}
				}
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
		}
		return responseObject;
	}
	
	/*@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.PUBLISH_SERVER_INSTANCE, actionType = BaseConstants.SM_ACTION,
	currentEntity = ServerInstance.class, ignorePropList = "")
	public ResponseObject publishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId) {

		ResponseObject responseObject = new ResponseObject();
		String tempSyncPublishXmlPath = null;
		logger.info("Call for Sync & Publish Server Instance ");
		try{
			ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
			String serverInstanceName;
			if (serverInstance != null) {
				DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.SYNC_PUBLISH_DATE_TIME_FORMATTER);
				serverInstanceName = serverInstance.getName().replaceAll(" ", "_");
				tempSyncPublishXmlPath = tempPathForSyncPublish + File.separator + serverInstanceName + "_" + serverInstance.getPort() + "_"
						+ dateFormatter.format(new Date()) + ".xml";
				String dateFormate = dateFormatter.format(new Date());
				responseObject = serverInstanceService.getServerInstanceJAXB(tempSyncPublishXmlPath, serverInstance.getId());
				Map<String, Object> serverInstaceMap = (Map<String, Object>) responseObject.getObject();
				File serverInstXmlFile = (File) serverInstaceMap.get(BaseConstants.SERVER_INSTANCE_EXPORT_FILE);
				VersionConfig config = new VersionConfig();
				String versionConfigName =  this.getVersionConfigName(serverInstance.getId(),serverInstance.getName(),dateFormate);
				config.setFile(new javax.sql.rowset.serial.SerialBlob(EliteUtils.convertFileContentToBlob(serverInstXmlFile)));
				config.setServerInstance(serverInstance);
				config.setDescription(description);
				config.setName(versionConfigName);
				config.setCreatedByStaffId(staffId);
				config.setLastUpdatedByStaffId(staffId);
				Staff staff = staffDAO.getStaffDetailsById(staffId);
				config.setPublishedBy(staff.getUsername());
				config.setCreatedDate(EliteUtils.getDateForSyncPublish(new Date()));
				config.setLastUpdatedDate(EliteUtils.getDateForSyncPublish(new Date()));
				versionConfigDao.save(config);
				serverInstXmlFile.delete();
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_SUCCESS);
				responseObject.setObject(serverInstance);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
				responseObject.setObject(null);
			}
		} catch(Exception e) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SYNC_PUBLISH_SERVER_INSTANCE_FAIL);
			responseObject.setObject(null);
		}
		return responseObject;
	}*/
}