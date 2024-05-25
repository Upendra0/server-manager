package com.elitecore.sm.scripteditor.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.scripteditor.constants.ScriptControllerConstants;
import com.elitecore.sm.scripteditor.constants.ScriptViewNameConstants;
import com.elitecore.sm.scripteditor.model.FileConfiguration;
import com.elitecore.sm.scripteditor.model.ServerConfiguration;
import com.elitecore.sm.scripteditor.service.ScriptManagerService;

/**
 * 
 * @author hiral.panchal
 *
 */
@Controller
public class ScriptMgrController extends BaseController{ 
	
	@Autowired
	ScriptManagerService service;
	/**
	 * When click on Script Manager menu 
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.INIT_SCRIPT_MANAGER, method = RequestMethod.GET)
	public ModelAndView initScriptManager() {
		
		ModelAndView model = new ModelAndView(ScriptViewNameConstants.SCRIPT_MANAGER);
		
		ResponseObject responseObject = service.getServerList();
		Map<Long,String> serverMap = (Map<Long,String>) responseObject.getObject();
		
		model.addObject("serverList",serverMap);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SCRIPT_MANAGEMENT);
		
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.FILE_LIST_FOR_SERVER, method = RequestMethod.GET)
	public @ResponseBody String getFileListForServer(@RequestParam int serverId) {
		
		ResponseObject responseObject = service.getFileList(serverId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse.toString():"+ajaxResponse.toString());
		return ajaxResponse.toString();
		
	}
	
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.SEARCH_SCRIPT_MANAGER, method = RequestMethod.GET)
	public @ResponseBody String getSearchResult(@RequestParam(value = "serverId", required = true) String serverId, 
			@RequestParam(value = "fileId",required = false) String fileId,
			@RequestParam(value = "rows",defaultValue = "1") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage){

		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		int count = 0;
		
		logger.info("serverId:"+serverId);
		if(Integer.parseInt(serverId) > 0){
			
			count = service.getSearchResultCount(Integer.parseInt(serverId), Integer.parseInt(fileId));
			logger.info("Result count :"+count);
			ResponseObject responseObject = service.getSearchResult(Integer.parseInt(serverId), Integer.parseInt(fileId),
					eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit);
			List<FileConfiguration> fileList = (List<FileConfiguration>) responseObject.getObject();

			if(count > 0){

				for(FileConfiguration fileConfig : fileList){

					row = new HashMap<>();
					row.put("fileid",fileConfig.getFileId());
					row.put("filename",fileConfig.getFileName());
					row.put("filepath",fileConfig.getFilePath());
					row.put("alias",fileConfig.getFileAlias()+"("+fileConfig.getTblmserverconfigmst().getServerIp()+")");
					row.put("excommand",fileConfig.getExenCommand());
					row.put("filedesc",fileConfig.getDescription());
					rowList.add(row);
				}

				logger.info("rowList.size:"+rowList.size());
			}
		}
		//Change the hardcode value
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.EDIT_FILE, method = RequestMethod.GET)
	public ModelAndView openFileInEditor(@RequestParam(value = "fileId" ) int fileId){

		logger.info("[IN] ScriptMgrController.openFileInEditor() called:"+fileId);
		ModelAndView model = new ModelAndView(ScriptViewNameConstants.SCRIPT_EDIT_PAGE);
		
		ResponseObject responseObject = service.readFile(fileId);
		Map<String,Object> fileMap =  (Map<String, Object>) responseObject.getObject();
		
		model.addObject("FILENAME_CONTENT_MAP",fileMap);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SCRIPT_MANAGEMENT);
		logger.info("[OUT] ScriptMgrController.openFileInEditor() executed");
		return model;
		}
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.UPDATE_FILE, method = RequestMethod.POST)
	public @ResponseBody String saveChangesInFile(
			@RequestParam(value = "fileId" ) int fileId, 
			@RequestParam(value = "fileName" ) String fileName, 
			@RequestParam(value = "fileContent" ) String fileContent, 
			@RequestParam(value = "serverAlias" ) String serverAlias,
			HttpServletRequest request){

		logger.info("[IN] ScriptMgrController.saveChangesInFile() fileId:"+fileId+",fileName:"+fileName+",serverAlias:"+serverAlias);
		
		Staff staff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);
		String staffName = staff.getFirstName() +" " + staff.getLastName();
		logger.info("staffName:"+staffName);

		FileConfiguration fileConfiguration = new FileConfiguration();
		fileConfiguration.setFileId(fileId);
		fileConfiguration.setFileName(fileName);
		fileConfiguration.setFileContent(fileContent);
		fileConfiguration.setStaffName(staffName);
		
		ServerConfiguration serverConfiguration = new ServerConfiguration();
		serverConfiguration.setServerName(serverAlias);
		fileConfiguration.setTblmserverconfigmst(serverConfiguration);
		fileConfiguration.setScriptOperation("updated");
		
		ResponseObject responseObject = service.updateFile(fileConfiguration);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse.toString():"+ajaxResponse.toString());
		return ajaxResponse.toString();
		}
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.EXECUTE_FILE, method = RequestMethod.POST )
	public @ResponseBody String executeFile(
			@RequestParam int fileId, 
			@RequestParam(value = "fileName" ) String fileName, 
			@RequestParam(value = "serverAlias" ) String serverAlias,
			HttpServletRequest request){

		logger.info("[IN] ScriptMgrController.executeFile() called:"+fileId);
		
		Staff staff = (Staff) request.getSession().getAttribute(BaseConstants.STAFF_DETAILS);
		String staffName = staff.getFirstName() +" " + staff.getLastName();
		
		FileConfiguration fileConfiguration = new FileConfiguration();
		fileConfiguration.setFileId(fileId);
		fileConfiguration.setFileName(fileName);
		fileConfiguration.setStaffName(staffName);
		ServerConfiguration serverConfiguration = new ServerConfiguration();
		serverConfiguration.setServerName(serverAlias);
		fileConfiguration.setTblmserverconfigmst(serverConfiguration);
		fileConfiguration.setScriptOperation("executed");
		
		ResponseObject responseObject = service.executeFile(fileConfiguration);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse.toString():"+ajaxResponse.toString());
		logger.info("[OUT] ScriptMgrController.executeFile() called:"+ajaxResponse.toString());
		return ajaxResponse.toString();
		}
	
	@PreAuthorize("hasAnyAuthority('SCRIPT_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ScriptControllerConstants.DOWNLOAD_FILE, method = RequestMethod.POST )
	public void downloadFile(
			@RequestParam int fileId,
			HttpServletResponse response){

		logger.info("[IN] ScriptMgrController.downloadFile() called:"+fileId);
		
		try {
			ResponseObject responseObject = service.downloadLog(fileId, response.getOutputStream());
			String logFileName = (String)responseObject.getObject();
			
			response.setContentType("application/octet-stream");
	        response.addHeader("Content-Disposition", "attachment; filename="+logFileName);
			response.getOutputStream().flush();
			
		} catch (IOException e) {
			logger.error(e);
		}
		}
}
