/**
 * 
 */
package com.elitecore.sm.scripteditor.service;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.scripteditor.constants.ScriptControllerConstants;
import com.elitecore.sm.scripteditor.dao.FileConfigurationDao;
import com.elitecore.sm.scripteditor.dao.ServerConfigurationDao;
import com.elitecore.sm.scripteditor.model.FileConfiguration;
import com.elitecore.sm.scripteditor.model.ServerConfiguration;
import com.elitecore.sm.scripteditor.util.RemoteServerUtils;

/**
 * @author hiral.panchal
 *
 */

@Service
public class ScriptManagerServiceImpl implements ScriptManagerService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ServerConfigurationDao serverDao; 
	
	@Autowired
	FileConfigurationDao fileDao;
	
	@Autowired
	RemoteServerUtils utils;
	
	
	
	@Transactional(readOnly = true)
	@Override
	public ResponseObject getSearchResult(long serverId, long fileId, int startIndex, int limit) {
		
		logger.info("ScriptManagerServiceImpl.getSearchResult() called");
		ResponseObject responseObject = new ResponseObject();
		
//		if(fileId > 0){
			
			List<FileConfiguration> file = fileDao.getPaginatedSearchResult(serverId,fileId,startIndex, limit);
			responseObject.setSuccess(true);
			responseObject.setObject(file);
//		}
	/*else{
			
			responseObject = this.getFileList(serverId);
		}*/
		
		logger.info("ScriptManagerServiceImpl.getSearchResult() end.:"+responseObject);
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject readFile(int fileId) {
		
		logger.info("ScriptManagerServiceImpl.readFile() started.:"+fileId);
		
		ResponseObject responseObject = new ResponseObject();
		Map<String,Object> fileMap = new HashMap<>();
		boolean isEditable = false;
		boolean isExecutable = false;
		boolean isReadOnly = false;
		boolean SCRIPT_STATUS = false; 
		StringBuilder scriptStatus = null;
		
		List<FileConfiguration> fileList = fileDao.getFileDetails(0L, fileId);
		
		FileConfiguration file = fileList.get(0);
		if(file == null){

			responseObject.setSuccess(false);
			responseObject.setObject(null);
			return responseObject;
		}
		
		String hostName = file.getTblmserverconfigmst().getServerIp();
		int port = Integer.parseInt(file.getTblmserverconfigmst().getServerPort());
		String userName = file.getTblmserverconfigmst().getUserName();
		String password = file.getTblmserverconfigmst().getPassword();
		String remoteFile = file.getFilePath() + file.getFileName();
		int fileType = file.getFileType();
		String statusCommand = file.getStatusCommand();
		String statusContainsText  = file.getStatusContains();
		statusContainsText = (statusContainsText != null) ? statusContainsText.trim() : "";
		
		if(fileType == ScriptControllerConstants.FILE_EXECUTABLE_ONLY){

			isExecutable = true;
		}else if(fileType == ScriptControllerConstants.FILE_EXECUTABLE_EDITABLE_BOTH){

			isExecutable = true;
			isEditable = true;
		}else if(fileType == ScriptControllerConstants.FILE_EDITABLE_ONLY){

			isEditable = true;
		}
		else if(fileType == ScriptControllerConstants.FILE_READ_ONLY){

			isReadOnly = true;
		}
		
		StringBuilder fileContent = utils.readFile(hostName, port, userName, password, remoteFile);
		
		fileMap.put("FILECONTENT", fileContent);
		fileMap.put("FILENAME", file.getFileAlias());
		fileMap.put("IS_EDITABLE", isEditable);
		fileMap.put("IS_EXECUTABLE", isExecutable);
		fileMap.put("IS_READONLY", isReadOnly);
		fileMap.put("LOGFILENAME", file.getLogFileName());
		fileMap.put("SERVERALIAS", file.getTblmserverconfigmst().getServerName());
		
		
		if(isExecutable){
			
			scriptStatus = utils.executeFile(hostName, port, userName, password, file.getFilePath(), statusCommand);
			logger.info("scriptStatus:"+scriptStatus);
		}
		
		if(scriptStatus != null && scriptStatus.indexOf(statusContainsText) != -1){
			
			logger.info("Script is running...");
			SCRIPT_STATUS = true;
			
		}else{
		
			logger.info("Script is either executed or execution yet to be started ...");
			SCRIPT_STATUS = false;
		}
		fileMap.put("SCRIPT_STATUS", SCRIPT_STATUS);
		
		responseObject.setSuccess(true);
		responseObject.setObject(fileMap);
		logger.info("ScriptManagerServiceImpl.readFile() end.");
		return responseObject;
	}
	
	@Transactional(readOnly = true)
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SCRIPT,actionType = BaseConstants.UPDATE_ACTION , currentEntity= FileConfiguration.class, ignorePropList= "")
	public ResponseObject updateFile(FileConfiguration fileConfiguration) {
		
		logger.info("ScriptManagerServiceImpl.updateFile() started.:");
		long fileId = fileConfiguration.getFileId();
		String fileContent = fileConfiguration.getFileContent();
		
		boolean isFileSaved = false;
		ResponseObject responseObj = new ResponseObject();
		
		List<FileConfiguration> fileList = fileDao.getFileDetails(0L,fileId);
		FileConfiguration file = fileList.get(0);
		
		if(file == null){

			responseObj.setSuccess(false);
			responseObj.setObject(isFileSaved);
			return responseObj;
		}
		
		String hostName = file.getTblmserverconfigmst().getServerIp();
		int port = Integer.parseInt(file.getTblmserverconfigmst().getServerPort());
		String userName = file.getTblmserverconfigmst().getUserName();
		String password = file.getTblmserverconfigmst().getPassword();
		String remoteFile = file.getFilePath() + file.getFileName();
		
		isFileSaved = utils.saveFile(hostName, port, userName, password, remoteFile, fileContent);
		
		logger.info("ScriptManagerServiceImpl.readFile() end.");
		responseObj.setSuccess(true);
		responseObj.setObject(isFileSaved);
		return responseObj;
	}

	@Transactional(readOnly = true)
	@Override
	@Auditable(auditActivity = AuditConstants.EXECUTE_SCRIPT,actionType = BaseConstants.UPDATE_ACTION , currentEntity= FileConfiguration.class, ignorePropList= "")
	public ResponseObject executeFile(FileConfiguration fileConfiguration) {
		
		long fileId = fileConfiguration.getFileId();
		logger.info("ScriptManagerServiceImpl.executeFile() started.:"+fileId);
		
		ResponseObject responseObject = new ResponseObject();
		
		List<FileConfiguration> fileList = fileDao.getFileDetails(0L, fileId);
		FileConfiguration file = fileList.get(0);
		
		if(file == null){

			responseObject.setSuccess(false);
			responseObject.setObject(null);
			return responseObject;
		}
		
		String hostName = file.getTblmserverconfigmst().getServerIp();
		int port = Integer.parseInt(file.getTblmserverconfigmst().getServerPort());
		String userName = file.getTblmserverconfigmst().getUserName();
		String password = file.getTblmserverconfigmst().getPassword();
		String remoteFile = file.getFilePath();
		String exenCommand = file.getExenCommand();
		
		StringBuilder exenOutput = utils.executeFile(hostName, port, userName, password, remoteFile, exenCommand);
		
		responseObject.setSuccess(true);
		responseObject.setObject(exenOutput.toString());
		logger.info("ScriptManagerServiceImpl.executeFile() end.:"+exenOutput.toString());
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getServerList() {

		Map<Long,String> serverMap = new HashMap<>();
		logger.info("ScriptManagerServiceImpl.getServerList() called.");
		ResponseObject responseObject = new ResponseObject();
		List<ServerConfiguration> serverList = serverDao.getAllActiveServers();
		
		if(serverList != null && serverList.size() > 0){
			
			logger.info("serverList.size():"+serverList.size());
			ListIterator<ServerConfiguration> itr = serverList.listIterator();
			while(itr.hasNext()){
				
				ServerConfiguration server = itr.next();
				serverMap.put(server.getServerId(), server.getServerName());
			}
			responseObject.setSuccess(true);
			responseObject.setObject(serverMap);
		}else{
			
			logger.info("serverMap:"+serverMap);
			responseObject.setSuccess(false);
		}
		
		logger.info("ScriptManagerServiceImpl.getServerList() end.");
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getFileList(long serverId) {

		Map<Long,String> filesMap = new HashMap<>();
		logger.info("ScriptManagerServiceImpl.getFileList() called."+serverId);
		ResponseObject responseObject = new ResponseObject();
		List<FileConfiguration> fileList = fileDao.getAllActiveFilesForServer(serverId);
		
		if(fileList != null){
			
			logger.debug("fileList.size():"+fileList.size());
			ListIterator<FileConfiguration> itr = fileList.listIterator();
			while(itr.hasNext()){
				
				FileConfiguration file = itr.next();
				filesMap.put(file.getFileId(), file.getFileAlias());
			}
			responseObject.setSuccess(true);
			responseObject.setObject(filesMap);
		}else{
			
			logger.debug("filesMap:"+filesMap);
			responseObject.setSuccess(false);
		}
		
		logger.info("ScriptManagerServiceImpl.getFileList() called.");
		return responseObject;
	}

	@Transactional(readOnly = true)
	@Override
	public int getSearchResultCount(int serverId, int fileId) {

		logger.info("[IN]ScriptManagerServiceImpl.getSearchResultCount() called");
		return fileDao.getSearchResultCount(serverId,fileId);
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject downloadLog(long fileId, ServletOutputStream outputStream) {
		
		List<FileConfiguration> fileList = fileDao.getFileDetails(0, fileId);
		FileConfiguration file = fileList.get(0);
		
		String hostName = file.getTblmserverconfigmst().getServerIp();
		int port = Integer.parseInt(file.getTblmserverconfigmst().getServerPort());
		String userName = file.getTblmserverconfigmst().getUserName();
		String password = file.getTblmserverconfigmst().getPassword();
		//String remoteFile = file.getFilePath() + file.getFileName();
		String remoteLogFile = file.getLogPath() + file.getLogFileName();
		//utils.downloadFile("10.151.1.147", 22, "mso", "mso@qa", "/opt/msosetup/test/while.sh", outputStream);
		utils.downloadFile(hostName, port, userName,password, remoteLogFile, outputStream);
		
		ResponseObject responseObj = new ResponseObject();
		responseObj.setSuccess(true);
		responseObj.setObject(file.getLogFileName());
		
		return responseObj;
	}

}
