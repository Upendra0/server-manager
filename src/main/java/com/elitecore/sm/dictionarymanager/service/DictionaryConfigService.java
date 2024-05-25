package com.elitecore.sm.dictionarymanager.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.serial.SerialException;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.server.model.Server;

public interface DictionaryConfigService {
	public List<DictionaryConfig> getDefaultDictionaryConfigObj();
	public ResponseObject createDictionaryData(DictionaryConfig dictionaryConfig,Server server);
	public List<DictionaryConfig> getDictionaryConfigList(String ipAddress,int utilityPort);
	public long getAllTableListCount(boolean isSearch,String ipAddress,int utilityPort);
	public List<DictionaryConfig> getPaginatedList(int startIndex, int limit, String sidx,
			String sord,String ipAddress,int utilityPort, boolean isSearch);
	public DictionaryConfig getDictionaryConfigObj(int id);
	public ResponseObject uploadDictionaryDataFile(DictionaryConfig dictionaryConfig) throws Exception;//NOSONAR
	public ResponseObject deleteDictionaryData(DictionaryConfig dictionaryConfig);
	public ResponseObject addNewFileToDictionary(String fileName,String filePath,String ServerIp,int utilityPort,File lookupDataFile) throws SerialException, SQLException, IOException,Exception;//NOSONAR
	public ResponseObject addNewFileToDictionaryAndSync(String fileName,String filePath,String ServerIp,int utilityPort,File lookupDataFile) throws SerialException, SQLException, IOException,Exception;//NOSONAR
	public ResponseObject uploadDictionaryDataFileAndSync(DictionaryConfig dictionaryConfig) throws Exception;//NOSONAR
	public long getTotalDictionaryEntries();
}