package com.elitecore.sm.dictionarymanager.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;

public interface DictionaryConfigDao extends GenericDAO<DictionaryConfig> {

	public List<DictionaryConfig> getDefaultDictionaryConfigObj();
	public int getDictionaryConfigCount(String ipAddress,int utilityPort);
	public List<DictionaryConfig> getDictionaryConfigList(String ipAddress,int port);
	public Map<String, Object> getRuleForSearchTableConditionList(String ipAddress, int utilityPort);
	public DictionaryConfig getDictionaryConfigObj(int id);
	
	public List<DictionaryConfig> getCustomDictionaryConfigList(String ipAddress, int utilityPort);
	
}
