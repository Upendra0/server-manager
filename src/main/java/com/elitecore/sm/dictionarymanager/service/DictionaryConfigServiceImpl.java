package com.elitecore.sm.dictionarymanager.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.rowset.serial.SerialException;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.dictionarymanager.dao.DictionaryConfigDao;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.Utilities;


@org.springframework.stereotype.Service(value = "dictionaryConfigService")
public class DictionaryConfigServiceImpl implements DictionaryConfigService {

	@Autowired
	DictionaryConfigDao dictionaryConfigDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<DictionaryConfig> getDefaultDictionaryConfigObj() {
		List<DictionaryConfig> dictionaryConfigObjList=dictionaryConfigDao.getDefaultDictionaryConfigObj();
		return dictionaryConfigObjList;
	}
	
	@Override
	@Transactional
	public ResponseObject createDictionaryData(DictionaryConfig dictionaryConfig,Server server) {
		dictionaryConfig.setIsDefault(false);
		dictionaryConfig.setIpAddress(server.getIpAddress());
		dictionaryConfig.setUtilityPort(server.getUtilityPort());
		dictionaryConfig.setCreatedDate(new Date());
		dictionaryConfig.setLastUpdatedDate(new Date());
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.save(dictionaryConfig);
		if (dictionaryConfig.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_FAILURE);
		}
		return responseObject;
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addNewFileToDictionaryAndSync(String fileName,String filePath,String ServerIp,int utilityPort,File lookupDataFile) throws SerialException, SQLException, IOException,Exception {//NOSONAR
		DictionaryConfig dictionaryConfigObj=new DictionaryConfig();
		/*if(filePath!=null && !filePath.isEmpty()) {
			dictionaryConfigObj.setPath(filePath);	
		}else {
			filePath="/dictionary";
			dictionaryConfigObj.setPath("/dictionary");
		}*/
		if(filePath==null || filePath.isEmpty()) {
			filePath="/dictionary";
		}
		dictionaryConfigObj.setPath(filePath);
		dictionaryConfigObj.setIpAddress(ServerIp);	
		dictionaryConfigObj.setUtilityPort(utilityPort);
		dictionaryConfigObj.setLastUpdatedDate(new Date());
		dictionaryConfigObj.setUpdated(false);
		dictionaryConfigObj.setIsDefault(false);
		dictionaryConfigObj.setFilename(fileName);
		dictionaryConfigObj.setStatus(StateEnum.ACTIVE);
		dictionaryConfigObj.setDicFile(EliteUtils.convertFileToByteArray(lookupDataFile));
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.save(dictionaryConfigObj);
		if (dictionaryConfigObj.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_SUCCESS);
			//JMX call here for sync operation
			RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(ServerIp, utilityPort,3,5000,20);
			byte[] dicFile = Utilities.convertBlobToByteArray(new javax.sql.rowset.serial.SerialBlob(EliteUtils.convertFileContentToBlob(lookupDataFile)));
			Boolean isSyncSuccess=serverMgmtRemoteJMXCall.syncDictionaryData(fileName, filePath,ServerIp, utilityPort,dicFile);
			if(isSyncSuccess){
			   responseObject.setSuccess(true);
			   responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SYNC_SUCCESS);
			}else{
				throw new Exception();//NOSONAR
			}
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_FAILURE);
		}
		return responseObject;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject uploadDictionaryDataFileAndSync(DictionaryConfig dictionaryConfig) throws Exception{//NOSONAR
		dictionaryConfig.setLastUpdatedDate(new Date());
		dictionaryConfig.setUpdated(true);
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.merge(dictionaryConfig);
		byte[] dicFile = dictionaryConfig.getDicFile();
	
		//JMX call here for sync operation
//	    MEDSUP-1783 DMC | Dictionary page not opening in SM change in the connection / retry and read timeout values
		RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(dictionaryConfig.getIpAddress(), dictionaryConfig.getUtilityPort(),1,1000,1);
		Boolean isSyncSuccess=serverMgmtRemoteJMXCall.syncDictionaryData(dictionaryConfig.getFilename(), dictionaryConfig.getPath(),dictionaryConfig.getIpAddress(), dictionaryConfig.getUtilityPort(),dicFile);
		if(isSyncSuccess){
		   responseObject.setSuccess(true);
		   responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_UPLOAD_SYNC_SUCCESS);
		}else{
			throw new Exception();//NOSONAR
		}
	
		return responseObject;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getAllTableListCount(boolean isSearch,String ipAddress,int utilityPort){
		Map<String, Object> ruleTableDataConditions = null;
			ruleTableDataConditions = dictionaryConfigDao.getRuleForSearchTableConditionList(ipAddress,utilityPort);
		return dictionaryConfigDao.getQueryCount(DictionaryConfig.class, (List<Criterion>) ruleTableDataConditions.get("conditions"),
				(HashMap<String, String>) ruleTableDataConditions.get("aliases"));
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<DictionaryConfig> getDictionaryConfigList(String ipAddress,int utilityPort) {
		return dictionaryConfigDao.getDictionaryConfigList(ipAddress,utilityPort);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DictionaryConfig> getPaginatedList(int startIndex, int limit, String sidx,
			String sord,String ipAddress,int utilityPort, boolean isSearch) {
		Map<String,Object> tableConditionList;
			tableConditionList = dictionaryConfigDao.getRuleForSearchTableConditionList(ipAddress,utilityPort);
		return dictionaryConfigDao.getPaginatedList(DictionaryConfig.class, (List<Criterion>) tableConditionList.get("conditions"),
				(HashMap<String, String>) tableConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@Transactional(readOnly = true)
	@Override
	public DictionaryConfig getDictionaryConfigObj(int id) {
		DictionaryConfig dictionaryConfig=dictionaryConfigDao.getDictionaryConfigObj(id);
		return dictionaryConfig;
	}
	
	@Override
	@Transactional
	public ResponseObject deleteDictionaryData(DictionaryConfig dictionaryConfig) {
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.deleteObject(dictionaryConfig);
		responseObject.setSuccess(true);
		return responseObject;
	}
	
	

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject uploadDictionaryDataFile(DictionaryConfig dictionaryConfig) throws Exception{
		dictionaryConfig.setLastUpdatedDate(new Date());
		dictionaryConfig.setUpdated(true);
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.merge(dictionaryConfig);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SUCCESS);
		return responseObject;
	}
	
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseObject addNewFileToDictionary(String fileName,String filePath,String ServerIp,int utilityPort,File lookupDataFile) throws SerialException, SQLException, IOException,Exception {
		DictionaryConfig dictionaryConfigObj=new DictionaryConfig();
		if(filePath==null || filePath.isEmpty()) {
			filePath="/dictionary";
		}
		dictionaryConfigObj.setPath(filePath);
		dictionaryConfigObj.setIpAddress(ServerIp);	
		dictionaryConfigObj.setUtilityPort(utilityPort);
		dictionaryConfigObj.setLastUpdatedDate(new Date());
		dictionaryConfigObj.setUpdated(false);
		dictionaryConfigObj.setIsDefault(false);
		dictionaryConfigObj.setFilename(fileName);
		dictionaryConfigObj.setStatus(StateEnum.ACTIVE);
		dictionaryConfigObj.setDicFile(EliteUtils.convertFileToByteArray(lookupDataFile));
		ResponseObject responseObject = new ResponseObject();
		dictionaryConfigDao.save(dictionaryConfigObj);
		if (dictionaryConfigObj.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_SUCCESS);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SUCCESS);
			
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DICTIONARY_ADD_FAILURE);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public long getTotalDictionaryEntries(){
		long cnt = 0;
		cnt = dictionaryConfigDao.getQueryCount(DictionaryConfig.class, null, null);
		return cnt;
	}
}