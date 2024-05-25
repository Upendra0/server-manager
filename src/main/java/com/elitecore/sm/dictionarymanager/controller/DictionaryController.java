package com.elitecore.sm.dictionarymanager.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.util.mbean.data.filereprocess.DictionaryFilePathEnum;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.dictionarymanager.service.DictionaryConfigService;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.google.gson.JsonObject;

@Controller
public class DictionaryController extends BaseController {
	
	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	ServerService serverService;
	
	@Autowired
	DictionaryConfigService dictionaryConfigService;
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;
	
	@Autowired(required=true)
	@Qualifier(value="serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So+
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	

	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_DICTIOANRY_CONFIG, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initRuleDataConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,defaultValue = BaseConstants.UPDATE_RULE_DATA_CONFIG)String requestActionType
			){
		Map<String,List<Server>> serverListMap=new HashMap<>();
		List<Server> tempInstaceList;		
		List<Server> serverList=serverService.getServerList();
		List<Server> runningUtilityPortServerList = new ArrayList<Server>();
		
		for (Server server : serverList) {
			if(Boolean.TRUE.toString().equals((String)MapCache.getConfigValueAsObject(BaseConstants.KUBERNETES_ENV))) {
				runningUtilityPortServerList.add(server);
			}else {
//    MEDSUP-1783 DMC | Dictionary page not opening in SM change in the connection / retry and read timeout values
				RemoteJMXHelper serverMgmtRemoteJMXCall = new RemoteJMXHelper(server.getIpAddress(), server.getUtilityPort(),1,1000,1);
				String crestelHome = serverMgmtRemoteJMXCall.getCrestelPEngineHome();
				if(crestelHome!=null) {
					runningUtilityPortServerList.add(server);
				}				
			}
		}
		
	   for(Server server:runningUtilityPortServerList){
			if(serverListMap.get(server.getServerType().getAlias()) ==null){
				tempInstaceList=new ArrayList<>();
				tempInstaceList.add(server);
				serverListMap.put(server.getServerType().getAlias(), tempInstaceList);	
			}else{
				tempInstaceList=serverListMap.get(server.getServerType().getAlias());
				tempInstaceList.add(server);
				serverListMap.put(server.getServerType().getAlias(), tempInstaceList);	
			}
			
		}
		ModelAndView model = new ModelAndView(
				ViewNameConstants.DICTIOANRY_CONFIG);
			model.addObject(FormBeanConstants.RULE_DATA_TABLE_FORM_BEAN,
					(RuleLookupTableData) SpringApplicationContext
							.getBean(RuleLookupTableData.class));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			model.addObject(BaseConstants.SERVER_LIST, serverListMap);
			model.addObject(BaseConstants.DICT_FILE_PATH, java.util.Arrays.asList(DictionaryFilePathEnum.values()));
			
		return model;
	 }
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.INIT_DICTIONARY_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String initRuleTableList(
			@RequestParam(value = "rows", defaultValue = "5") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = "isSearch" ,required=false) boolean isSearch,
			@RequestParam(value = "ipAddress", required=false) String ipAddress,@RequestParam(value = "utilityPort", required=false) String utilityPort,HttpServletRequest request){
		List<DictionaryConfig> resultList = null;
		List<Map<String, Object>> rowList = new ArrayList<>();
		long count=0;
		if(!ipAddress.equals("0") && !utilityPort.equals("0")) {
		count = dictionaryConfigService.getAllTableListCount(isSearch,ipAddress,Integer.parseInt(utilityPort));
		if(count==0) {
			List<DictionaryConfig> dictionaryConfigList=(List<DictionaryConfig>)dictionaryConfigService.getDefaultDictionaryConfigObj();
			for(DictionaryConfig dictionaryConfig : dictionaryConfigList) {
				Server server=new Server();
				server.setIpAddress(ipAddress);
				server.setUtilityPort(Integer.parseInt(utilityPort));
				ResponseObject dictResponseObject=dictionaryConfigService.createDictionaryData(dictionaryConfig,server);
				if(dictResponseObject.isSuccess()){
					dictionaryConfig.setId(0);
					dictionaryConfig.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					dictionaryConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					logger.debug("INSERT SUCCESS : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
				}else{
					logger.debug("INSERT FAIL : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
				}
			}
		}
		count = dictionaryConfigService.getAllTableListCount(isSearch,ipAddress,Integer.parseInt(utilityPort));
		resultList  = this.dictionaryConfigService.getPaginatedList( eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, "path",BaseConstants.ASC,ipAddress,Integer.parseInt(utilityPort),isSearch);
		Map<String, Object> row ;
		if (resultList != null) {
			for (DictionaryConfig dicConfigList : resultList) {
				row = new HashMap<>();
				row.put("id", dicConfigList.getId());
				row.put("path", dicConfigList.getPath());
				row.put("filename",dicConfigList.getFilename());
				rowList.add(row);
			}
		 }
		   return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_DICTIONARY_CONFIG_XML, method = RequestMethod.POST)
	@ResponseBody 
	public ModelAndView downloadDictionaryConfigData(
			@RequestParam(value="did") String id,HttpServletResponse response){
		DictionaryConfig dictionaryConfigObj = dictionaryConfigService.getDictionaryConfigObj(Integer.parseInt(id));
		response.setContentType("application/xml");
		response.reset();
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", dictionaryConfigObj.getFilename());
		response.setHeader(headerKey, headerValue);
		response.setCharacterEncoding("UTF-8");
		
        ServletOutputStream outputStream = null;
        try{
        	outputStream = response.getOutputStream();
//            Blob blob = dictionaryConfigObj.getDicFile();
  //          is = blob.getBinaryStream();
            byte[] buffer = dictionaryConfigObj.getDicFile();// new byte[(int) blob.length()];
        //    while(is.read(buffer)!=-1){
                outputStream.write(buffer);
          //  }
            outputStream.flush();
            outputStream.close();
                      
        } catch (Exception e) {
        	logger.error(e);
        } finally {
               try {
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
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPLOAD_DICTIONARY_FILE_DATA_SYNC, method = RequestMethod.POST)
	@ResponseBody
	public  String uploadDictionaryDataFileSync(
			@RequestParam(value = "id",required = true)String id,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException, SerialException, SQLException{
		    ResponseObject responseObject = new ResponseObject();
			JSONArray jsonArray = new JSONArray();
			if (!multipartFile.isEmpty() ){
				logger.debug("File object found.Going to insert data");
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					try {
						multipartFile.transferTo(lookupDataFile);
						DictionaryConfig dictionaryConfigObj = dictionaryConfigService.getDictionaryConfigObj(Integer.parseInt(id));
						if(dictionaryConfigObj!=null){
							if(dictionaryConfigObj.getFilename().equals(multipartFile.getOriginalFilename())) {
								dictionaryConfigObj.setDicFile(EliteUtils.convertFileToByteArray(lookupDataFile));
								dictionaryConfigObj.setFilename(multipartFile.getOriginalFilename());
								try{
									responseObject=dictionaryConfigService.uploadDictionaryDataFileAndSync(dictionaryConfigObj);
								}catch(Exception e){//NOSONAR
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_UPLOAD_SYNC_FAILURE);
									logger.info("upload and sync failed !!");
								}
							} else {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.DICTIONARY_FILENAME_MISMATCH_FAILURE);
								logger.error("Upload File Name should be "+dictionaryConfigObj.getFilename());
							}
						}
					} catch (IllegalStateException | IOException e) {//NOSONAR
						logger.trace("Problem occured while uploading data file", e);
						responseObject.setObject(null);
						responseObject.setSuccess(false);
					}
					if (responseObject.isSuccess()) {
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("fileName", lookupDataFile.getName());
						jsonArray.put(jsonObject);
						responseObject.setObject(jsonArray);
						responseObject.setSuccess(true);
					}
			}
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.ADD_NEW_FILE_TO_DICTIONARY_SYNC, method = RequestMethod.POST)
	@ResponseBody
	public  String addNewFileToDictionarySync(
			@RequestParam(value = "utilityPort",required = true)int utilityPort,
			@RequestParam(value = "ServerIp",required = true)String ServerIp,
			@RequestParam(value = "filePath",required = true)String filePath,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException, SerialException, SQLException, IOException{
		    ResponseObject responseObject = new ResponseObject();
			JSONArray jsonArray = new JSONArray();
			if (!multipartFile.isEmpty() ){
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					multipartFile.transferTo(lookupDataFile);
					try {
						responseObject=dictionaryConfigService.addNewFileToDictionaryAndSync(multipartFile.getOriginalFilename(),filePath, ServerIp, utilityPort, lookupDataFile);
						if (responseObject.isSuccess()) {
							JsonObject jsonObject = new JsonObject();
							jsonObject.addProperty("fileName", lookupDataFile.getName());
							jsonArray.put(jsonObject);
							responseObject.setObject(jsonArray);
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SYNC_SUCCESS);
						}
					}catch (DataIntegrityViolationException e) {//NOSONAR
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DICTIONARY_SAME_FILENAME_AT_SAMEPATH);
						logger.info("same file name exist at this uploaded path !! failed to add & sync !!");
					} 
					catch (Exception e) {//NOSONAR
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SYNC_FAILURE);
						logger.info("failed to add & sync !!");
					}
					
			}
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.UPLOAD_DICTIONARY_FILE_DATA, method = RequestMethod.POST)
	@ResponseBody
	public  String uploadDictionaryDataFile(
			@RequestParam(value = "id",required = true)String id,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException, SerialException, SQLException{
		    ResponseObject responseObject = new ResponseObject();
			JSONArray jsonArray = new JSONArray();
			if (!multipartFile.isEmpty() ){
				logger.debug("File object found.Going to insert data");
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					try {
						multipartFile.transferTo(lookupDataFile);
						DictionaryConfig dictionaryConfigObj = dictionaryConfigService.getDictionaryConfigObj(Integer.parseInt(id));
						if(dictionaryConfigObj!=null){
							if(dictionaryConfigObj.getFilename().equals(multipartFile.getOriginalFilename())) {
								dictionaryConfigObj.setDicFile(EliteUtils.convertFileToByteArray(lookupDataFile));
								dictionaryConfigObj.setFilename(multipartFile.getOriginalFilename());
								try{
									responseObject=dictionaryConfigService.uploadDictionaryDataFile(dictionaryConfigObj);
								}catch(Exception e){//NOSONAR
									responseObject.setSuccess(false);
									responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_UPLOAD_FAILURE);
									logger.info("upload and sync failed !!");
								}
							} else {
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.DICTIONARY_FILENAME_MISMATCH_FAILURE);
								logger.error("Upload File Name should be "+dictionaryConfigObj.getFilename());
							}
						}
					} catch (IllegalStateException | IOException e) {
						logger.trace("Problem occured while uploading data file", e);
						responseObject.setObject(null);
						responseObject.setSuccess(false);
					}
					if (responseObject.isSuccess()) {
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("fileName", lookupDataFile.getName());
						jsonArray.put(jsonObject);
						responseObject.setObject(jsonArray);
						responseObject.setSuccess(true);
					}
			}
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('VIEW_DICTIONARY_CONFIG')")
	@RequestMapping(value = ControllerConstants.ADD_NEW_FILE_TO_DICTIONARY, method = RequestMethod.POST)
	@ResponseBody
	public  String addNewFileToDictionary(
			@RequestParam(value = "utilityPort",required = true)int utilityPort,
			@RequestParam(value = "ServerIp",required = true)String ServerIp,
			@RequestParam(value = "filePath",required = true)String filePath,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException, SerialException, SQLException, IOException{
		    ResponseObject responseObject = new ResponseObject();
			JSONArray jsonArray = new JSONArray();
			if (!multipartFile.isEmpty() ){
					String repositoryPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					File lookupDataFile = new File(repositoryPath + multipartFile.getOriginalFilename());
					multipartFile.transferTo(lookupDataFile);
					try {
						responseObject=dictionaryConfigService.addNewFileToDictionary(multipartFile.getOriginalFilename(),filePath, ServerIp, utilityPort, lookupDataFile);
						if (responseObject.isSuccess()) {
							JsonObject jsonObject = new JsonObject();
							jsonObject.addProperty("fileName", lookupDataFile.getName());
							jsonArray.put(jsonObject);
							responseObject.setObject(jsonArray);
							responseObject.setSuccess(true);
							responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_SUCCESS);
						}
					}catch (DataIntegrityViolationException e) {//NOSONAR
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DICTIONARY_SAME_FILENAME_AT_SAMEPATH);
						logger.info("same file name exist at this uploaded path !! failed to add & sync !!");
					} 
					catch (Exception e) {//NOSONAR
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.DICTIONARY_FILE_ADD_FAILURE);
						logger.info("failed to add & sync !!");
					}
					
			}
			AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			return ajaxResponse.toString();
	}
}