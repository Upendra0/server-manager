package com.elitecore.sm.configmanager.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.configmanager.model.VersionConfig;
import com.elitecore.sm.configmanager.service.VersionConfigService;
import com.elitecore.sm.util.EliteUtils;


@Controller
public class VersionConfigController extends BaseController {

	@Autowired
	VersionConfigService versionConfigService;

	@Autowired
	private ServletContext servletContext;
	
	@PreAuthorize("hasAnyAuthority('VIEW_CONFIG_HISTORY')")
	@RequestMapping(value = ControllerConstants.GET_VERSION_CONFIG_DETAIL, method = RequestMethod.POST)
	@ResponseBody public  String getVersionConfigDetail(
			@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId
				) {
		logger.debug(">> Going to fetch version config details by serverInstanceId"); 
		
		int count=this.versionConfigService.getVersionConfigCount(Integer.parseInt(serverInstanceId));
		
		List<VersionConfig> resultList = new ArrayList<>();
		
		if(count > 0){
			resultList = this.versionConfigService.getVersionConfigList(serverInstanceId);
		}
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		if (resultList != null) {
			for (VersionConfig versionConfigList : resultList) {
				row = new HashMap<>();
				row.put("id", versionConfigList.getId());
				row.put("name", versionConfigList.getName());
				row.put("description",versionConfigList.getDescription());
				row.put("createdByStaffId",versionConfigList.getPublishedBy());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, 0),	1,(int)count,	rowList).getJsonString();		
	}
	
	/*@PreAuthorize("hasAnyAuthority('COMPARE_VERSION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.COMPARE_VERSION_CONFIG_XML, method = RequestMethod.POST)
	@ResponseBody 
	public ModelAndView compareVersionConfigXML(
			@RequestParam(value="versionConfigId1") String versionConfigId1,
			@RequestParam(value="versionConfigId2") String versionConfigId2,HttpServletResponse response){
		
		VersionConfig versionConfigObj1 = versionConfigService.getVersionConfigObj(Integer.parseInt(versionConfigId1));
		VersionConfig versionConfigObj2 = versionConfigService.getVersionConfigObj(Integer.parseInt(versionConfigId2));
		
		String tempPathForSyncPublish=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
        ServletOutputStream outputStream = null;
        InputStream is = null;
        File file = null, file1 = null, file2 = null;
        try{
        	file1 = EliteUtils.convertBlobToFileContent(versionConfigObj1,tempPathForSyncPublish);
    		file2 = EliteUtils.convertBlobToFileContent(versionConfigObj2,tempPathForSyncPublish);
    		file = EliteUtils.compareXmlFile(file1, file2, tempPathForSyncPublish);
    		response.setContentType("application/xml");
    		response.reset();
    		String headerKey = "Content-Disposition";
    		String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
    		response.setHeader(headerKey, headerValue);
    		response.setCharacterEncoding("UTF-8");
        	outputStream = response.getOutputStream();
            is = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            while(is.read(buffer)!=-1){
                outputStream.write(buffer);
            }
            outputStream.flush();
            outputStream.close();
                      
        } catch (Exception e) {
        	logger.error(e);
        } finally {
               try {
	                 if(is!=null){
					 	is.close();
	                 }
	                 if(outputStream!=null){
	                	 outputStream.flush();
	    				 outputStream.close();
	                 }
	                 if(file.exists()){
	                	 file.delete();
	                 }
	                 if(file1.exists()){
	                	 file1.delete(); 
	                 }if(file2.exists()){
	                	 file2.delete();
	                 }
				} catch (IOException e) {
					logger.error(e);
				}
        }
        return null;
	}*/
	
	@PreAuthorize("hasAnyAuthority('COMPARE_VERSION_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.COMPARE_VERSION_CONFIG_XML, method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public String compareVersionConfigXML(
			@RequestParam(value="versionConfigId1") String versionConfigId1,
			@RequestParam(value="versionConfigId2") String versionConfigId2,HttpServletResponse response){
		
		ResponseObject responseObject = new ResponseObject();
		VersionConfig versionConfigObj1 = versionConfigService.getVersionConfigObj(Integer.parseInt(versionConfigId1));
		VersionConfig versionConfigObj2 = versionConfigService.getVersionConfigObj(Integer.parseInt(versionConfigId2));
		
		String tempPathForSyncPublish=servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
        File file1 = null, file2 = null;
        String compareString = "";
        try{
        	file1 = EliteUtils.convertBlobToFileContent(versionConfigObj1,tempPathForSyncPublish);
    		file2 = EliteUtils.convertBlobToFileContent(versionConfigObj2,tempPathForSyncPublish);
    		compareString = EliteUtils.compareXmlFile(file1, file2, tempPathForSyncPublish);
    		responseObject.setObject(compareString);
    		responseObject.setSuccess(true);
        } catch (Exception e) {
        	responseObject.setObject(null);
    		responseObject.setSuccess(false);
        	logger.error(e);
        }
        AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		logger.info("ajaxResponse: " + ajaxResponse);
		return ajaxResponse.toString();
	}
		
	@PreAuthorize("hasAnyAuthority('VIEW_CONFIG_HISTORY')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_VERSION_CONFIG_XML, method = RequestMethod.POST)
	@ResponseBody 
	public ModelAndView downloadVersionConfigXML(
			@RequestParam(value="sid") String id,HttpServletResponse response){
		VersionConfig versionConfigObj = versionConfigService.getVersionConfigObj(Integer.parseInt(id));
		response.setContentType("application/xml");
		response.reset();
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", versionConfigObj.getName()+".xml");
		response.setHeader(headerKey, headerValue);
		response.setCharacterEncoding("UTF-8");
		
        ServletOutputStream outputStream = null;
        InputStream is = null;
        try{
        	outputStream = response.getOutputStream();
            Blob blob = versionConfigObj.getFile();
            is = blob.getBinaryStream();
            byte[] buffer = new byte[(int) blob.length()];
            while(is.read(buffer)!=-1){
                outputStream.write(buffer);
            }
            outputStream.flush();
            outputStream.close();
                      
        } catch (Exception e) {
        	logger.error(e);
        } finally {
               try {
	                 if(is!=null){
					 	is.close();
	                 }
	                 if(outputStream!=null){
	                	 outputStream.flush();
	    				 outputStream.close();
	                 }
				} catch (IOException e) {
					logger.error(e);
				}
      }
	return null;
}
}
