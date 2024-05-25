package com.elitecore.sm.configmanager.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.configmanager.model.VersionConfig;

public interface VersionConfigDao extends GenericDAO<VersionConfig> {

	public VersionConfig getVersionConfigIndex(int serverInstanceId);
    public List<VersionConfig> getVersionConfigList(String serverInstanceId);
	public VersionConfig getVersionConfigObj(int id);
	public int getVersionConfigCount(int id);
}
