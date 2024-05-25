package com.elitecore.sm.configmanager.service;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.configmanager.model.VersionConfig;

public interface VersionConfigService {
	public List<VersionConfig> getVersionConfigList(String serverInstanceId);
	public VersionConfig getVersionConfigObj(int serverInstanceId);
	public int getVersionConfigCount(int serverInstanceId);
	
	public ResponseObject restoreSyncPublishServerInstance(int serverInstanceId, int versionConfigId, String description, String servInstanceStatus, String tempPathForSyncPublish, String jaxbXmlPath, int staffId, Map<String,String> syncInputMap, ResponseObject oldResponseObject) throws Exception;//NOSONAR
	
	public ResponseObject syncPublishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId, Map<String,String> syncInputMap, ResponseObject oldResponseObject) throws Exception;//NOSONAR
	
	//public ResponseObject publishServerInstance(int serverInstanceId, String description, String tempPathForSyncPublish, int staffId);
}