/**
 * 
 */
package com.elitecore.sm.errorreprocess.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.util.mbean.data.filereprocess.ErrorReprocessCategoryEnum;
import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.MisReportTypeEnum;
import com.elitecore.sm.common.model.MonthEnum;
import com.elitecore.sm.common.model.PolicyRuleCategoryEnum;
import com.elitecore.sm.common.model.PolicyRuleSeverityEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.services.model.ErrorReprocessingActionEnum;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;


/**
 * @author Ranjitsinh Reval
 *
 */
@RestController
public class ErrorReprocessBatchController extends BaseController {
	
	@Autowired
	ErrorReprocessBatchService errorReprocessBatchService;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	private ServletContext servletContext;

	
	
	
	/**
	 * Method will load re-processing file details and redirect to re-process file page. 
	 * @param requestActionType
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_FILE_STATUS','REPROCESS_FILE','AUTO_REPROCESS_FILE')")
	@RequestMapping(value = {ControllerConstants.INIT_REPROCESSING_DETAILS }, method = RequestMethod.GET)
	public ModelAndView initReprocessingStatus(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType) {
		ModelAndView model = new ModelAndView(ViewNameConstants.ININT_ERROR_REPROCESSING);
		
		List<ServiceType> serviceTypeList = getActiveServiceTypeList(); 
		if(serviceTypeList != null && !serviceTypeList.isEmpty()) {
			int length = serviceTypeList.size();
			for(int i = length-1; i >= 0; i--) {

				String alias = serviceTypeList.get(i).getAlias();
				if(!(alias.equalsIgnoreCase(EngineConstants.DISTRIBUTION_SERVICE) 
						|| alias.equalsIgnoreCase(EngineConstants.PARSING_SERVICE)
						|| alias.equalsIgnoreCase(EngineConstants.PROCESSING_SERVICE)
						|| alias.equalsIgnoreCase(EngineConstants.COLLECTION_SERVICE)
					/*	|| alias.equalsIgnoreCase(EngineConstants.GTPPRIME_COLLECTION_SERVICE)
						|| alias.equalsIgnoreCase(EngineConstants.DATA_CONSOLIDATION_SERVICE)
						|| alias.equalsIgnoreCase(EngineConstants.AGGREGATION_SERVICE)*/)) {
					
					serviceTypeList.remove(i);
				}
			}
		}
		RuleConditionDetails ruleConditionObj = (RuleConditionDetails) SpringApplicationContext.getBean(RuleConditionDetails.class);
		model.addObject("serviceTypeList", serviceTypeList);
		model.addObject(FormBeanConstants.RULE_CONDITION_FORM_BEAN, ruleConditionObj );
		model.addObject("fileProcessingStatusEnum",java.util.Arrays.asList(FileReprocessStatusEnum.values()));
		model.addObject("fileProcessingActionEnum",java.util.Arrays.asList(ErrorReprocessingActionEnum.values()));
		model.addObject("fileProcessingCategoryEnum",java.util.Arrays.asList(ErrorReprocessCategoryEnum.values()));
		model.addObject("fileProcessingRuleCategory",java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject("fileProcessingRuleSeverity",java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		
		if (eliteUtils.isAuthorityGranted("REPROCESS_FILE")){
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REPROCESSING_FILE);
		} else if(eliteUtils.isAuthorityGranted("VIEW_FILE_STATUS")) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.REPROCESSING_STATUS);
		} else if (eliteUtils.isAuthorityGranted("AUTO_REPROCESS_FILE")) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.AUTOREPROCESSING_FILE);
		}
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('AUTO_REPROCESS_FILE')")
	@RequestMapping(value = {ControllerConstants.INIT_AUTOREPROCESSING_DETAILS }, method = RequestMethod.GET)
	public ModelAndView initAutoReprocessingStatus(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType) {
		ModelAndView model = new ModelAndView(ViewNameConstants.ININT_ERROR_REPROCESSING);
		List<ServiceType> serviceTypeList = getActiveServiceTypeList(); 
		if(serviceTypeList != null && !serviceTypeList.isEmpty()) {
			int length = serviceTypeList.size();
			for(int i = length-1; i >= 0; i--) {

				String alias = serviceTypeList.get(i).getAlias();
				if(!(alias.equalsIgnoreCase(EngineConstants.PROCESSING_SERVICE) 
					)) {
					serviceTypeList.remove(i);
				}
			}
		}
		RuleConditionDetails ruleConditionObj = (RuleConditionDetails) SpringApplicationContext.getBean(RuleConditionDetails.class);
		model.addObject("serviceTypeList", serviceTypeList);
		model.addObject(FormBeanConstants.RULE_CONDITION_FORM_BEAN, ruleConditionObj );
		model.addObject("fileProcessingStatusEnum",java.util.Arrays.asList(FileReprocessStatusEnum.values()));
		model.addObject("fileProcessingActionEnum",java.util.Arrays.asList(ErrorReprocessingActionEnum.values()));
		model.addObject("fileProcessingCategoryEnum",java.util.Arrays.asList(ErrorReprocessCategoryEnum.FILTER,ErrorReprocessCategoryEnum.INVALID));
		model.addObject("fileProcessingRuleCategory",java.util.Arrays.asList(PolicyRuleCategoryEnum.values()));
		model.addObject("fileProcessingRuleSeverity",java.util.Arrays.asList(PolicyRuleSeverityEnum.values()));
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
		return model;
	}
	
	/**
	 * Method will get active service type list.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ServiceType> getActiveServiceTypeList(){
		List<ServiceType> serviceTypeList=new ArrayList<>();
		
		List<ServerType> activeServerTypeList=(List<ServerType>)MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		
		for(ServerType serverType:activeServerTypeList){
			List<ServiceType> mainServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),BaseConstants.MAIN_SERVICE_TYPE);
			List<ServiceType> additionalServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),BaseConstants.ADDITIONAL_SERVICE_TYPE);
			
			for(ServiceType serviceType:mainServiceTypeList){
				if(!serviceTypeList.contains(serviceType)){
					logger.debug("Main Service To be added " +serviceType.getAlias() );
					serviceTypeList.add(serviceType);
				}
			}
			 
			 for(ServiceType serviceType:additionalServiceTypeList){
				if(!serviceTypeList.contains(serviceType)){
					logger.debug("Additional Service To be added " +serviceType.getAlias() );
					serviceTypeList.add(serviceType);
				}
			}
		}
		
		return serviceTypeList;	 
	}
	
	/**
	 * Method will add new error re-process batch.
	 * @param serviceId
	 * @param pluginId
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DIRECT_REPROCESS')")
	@RequestMapping(value = ControllerConstants.CREATE_ERROR_REPROCESS_BATCH, method = RequestMethod.POST)
	public String createErrorReprocessBatch(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) {//NOSONAR
		ResponseObject responseObject = errorReprocessBatchService.addErrorReprocessBatch(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will add new archive restore batch.
	 * @param serviceId
	 * @param pluginId
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('RESTORE_FILES')")
	@RequestMapping(value = ControllerConstants.CREATE_ARCHIVE_RESTORE_BATCH, method = RequestMethod.POST)
	public String createArchiveRestoreBatch(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) {//NOSONAR
		ResponseObject responseObject = errorReprocessBatchService.addArchiveRestoreBatch(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will reprocess modified files 
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('DIRECT_REPROCESS')")
	@RequestMapping(value = ControllerConstants.REPROCESS_MODIFIED_FILES, method = RequestMethod.POST)
	public String reprocessModifiedFiles(
			@RequestParam(value = "fileListIds", required = true) String selectedFileIds , 
			@RequestParam(value = "errorReprocess", required = true) String errorReprocessJSON,
			HttpServletRequest request){
		
		Integer[] reprocessIdList = Utilities.convertStringArrayToInt(selectedFileIds);
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = errorReprocessBatchService.reprocessModifiedFileList(errorReprocessJSON, reprocessIdList,staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_FILE')")
	@RequestMapping(value = ControllerConstants.DELETE_FILE_REPROCESS_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String deleteFileReprocessDetails(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) throws SMException {//NOSONAR
		ResponseObject responseObject = errorReprocessBatchService.deleteFileReprocessDetails(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('DIRECT_REPROCESS')")
	@RequestMapping(value = ControllerConstants.REPROCESS_PROCESSING_FILES, method = RequestMethod.POST)
	public String reporcessingProcessingFiles(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) {//NOSONAR
		ResponseObject responseObject = this.errorReprocessBatchService.reprocessProcessingFiles(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('RESTORE_FILES')")
	@RequestMapping(value = ControllerConstants.RESTORE_PROCESSING_FILES, method = RequestMethod.POST)
	public String restoringProcessingFiles(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) {//NOSONAR
		ResponseObject responseObject = this.errorReprocessBatchService.restoringProcessingFiles(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MODIFY_FILES')")
	@RequestMapping(value = ControllerConstants.APPLY_RULE_TO_FILE, method = RequestMethod.POST)
	public String applyRulesToFiles(
				@RequestParam(value = "ruleList") String ruleActionConditionList, 
				@RequestParam(value = "errorReprocessBatch") String erroReprocessJSON,
				HttpServletRequest request) throws SMException {
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = this.errorReprocessBatchService.applyRuleAndCreateBatch(ruleActionConditionList, erroReprocessJSON, staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('MODIFY_FILES')")
	@RequestMapping(value = ControllerConstants.REVERT_MODIFIED_FILES, method = RequestMethod.POST)
	public String revertModifiyFiles(@RequestParam(value="batchId",required=true) int batchId) {
		ResponseObject responseObject = this.errorReprocessBatchService.revertModifiedFiles(batchId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('DELETE_FILE')")
	@RequestMapping(value = ControllerConstants.DELETE_PROCESSING_FILE_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String deleteProcessingFiles(@RequestBody ErrorReprocessingBatch errorReprocessingBatch) throws SMException {//NOSONAR
		ResponseObject responseObject = errorReprocessBatchService.deleteProcessingFileDetails(errorReprocessingBatch);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.REPROCESS_FAILED_FILE_LIST, method = RequestMethod.POST)
	@ResponseBody public  String reprocessFailedFileList(
				@RequestParam(value = BaseConstants.FILE_REPROCESS_ID_LIST,required = true) String fileReprocessIdList) throws SMException {
		logger.debug(">> reprocessFailedFileList in ErrorReprocessBatchController " + Utilities.convertStringArrayToInt(fileReprocessIdList));
		return null;
	}
	
	
	@PreAuthorize("hasAnyAuthority('SEARCH_FILES')")
	@RequestMapping(value = ControllerConstants.GET_REPROCESS_FILES_FROM_SM, method = RequestMethod.POST)
	@ResponseBody
	public String getReprocessFilesFromSM(SearchErrorReprocessDetails reprocessDetails) {

		logger.debug(">> getReprocessFilesFromSM in ErrorReprocessDetailController " + reprocessDetails); 
		Integer[] serviceIds = Utilities.convertStringArrayToInt(reprocessDetails.getServiceInstanceIds());
		ResponseObject responseObject = errorReprocessBatchService.getFileReprocessingJsonForGrid(serviceIds, reprocessDetails);
		JSONObject jsonObject = new JSONObject();
		if(responseObject != null && responseObject.isSuccess()) {
			jsonObject = (JSONObject) responseObject.getObject();
		}
		return jsonObject.toString();
	}
	
	
	
	/**
	 * Method will get processing service by rule and service id combination.
	 * @param reprocessDetails
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('SEARCH_FILES')")
	@RequestMapping(value = ControllerConstants.GET_PROCESSING_DETAILS_BY_RULE, method = RequestMethod.POST)
	@ResponseBody
	public String getProcessingServiceByRule(SearchErrorReprocessDetails reprocessDetails) {
		logger.debug(">> getProcessingServiceDetailsBuRule in ErrorReprocessDetailController " ); 
		ResponseObject responseObject = this.errorReprocessBatchService.getProcessingServiceDetailsByRule(reprocessDetails);
		JSONObject jsonObject = new JSONObject();
		if(responseObject != null && responseObject.isSuccess()) {
			jsonObject = (JSONObject) responseObject.getObject();
		}
		return jsonObject.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('SEARCH_FILES')")
	@RequestMapping(value = ControllerConstants.GET_REPROCESS_FILE_DETAIL_FROM_ENGINE, method = RequestMethod.POST)
	@ResponseBody
	public String getReprocessFileDetailsFromEngine(SearchErrorReprocessDetails reprocessDetails) {
		logger.debug(">> getReprocessFileDetailsFromEngine in ErrorReprocessDetailController " + reprocessDetails); 
		Integer[] serviceIds = Utilities.convertStringArrayToInt(reprocessDetails.getServiceInstanceIds());
		ResponseObject responseObject = errorReprocessBatchService.getFileReprocessingJsonFromEngine(reprocessDetails, serviceIds);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('SEARCH_FILES')")
	@RequestMapping(value = ControllerConstants.GET_PROCESSING_ERROR_DETAILS, method = RequestMethod.POST)
	@ResponseBody
	public String getProcessingServiceErrorDetails(SearchErrorReprocessDetails reprocessDetails) {
		logger.debug(">> getProcessingServiceErrorDetails in ErrorReprocessDetailController " + reprocessDetails); 
		Integer[] serviceIds = Utilities.convertStringArrayToInt(reprocessDetails.getServiceInstanceIds());
		ResponseObject responseObject = errorReprocessBatchService.getProcessingServiceErrorDetails(reprocessDetails, serviceIds);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('SEARCH_FILES')")
	@RequestMapping(value = ControllerConstants.GET_ALL_RULE_LIST_BY_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public String getAllRuleList(@RequestParam(value = "serviceIdList") String serviceIds,
								 @RequestParam(value = "category") String category,
								 @RequestParam(value = "reasonCategory") String reasonCategory,
								 @RequestParam(value = "reasonSeverity") String reasonSeverity,
								 @RequestParam(value = "reasonErrorCode") String reasonErrorCode) {
		logger.debug(">> getAllRuleList in ErrorReprocessDetailController " ); 
		Integer[] idList= Utilities.convertStringArrayToInt(serviceIds);
		ResponseObject responseObject = this.policyService.getAllRuleByServiceAndAction(idList, category,reasonCategory,reasonSeverity,reasonErrorCode);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_FILE')")
	@RequestMapping(value = ControllerConstants.GET_FILE_RECORD_DETAILS, method = RequestMethod.POST)
	@ResponseBody
	public String getFileRecordDetails(
			@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "filePath", required = true) String filePath,
			@RequestParam(value = "fileType", required = true) String fileType,
			@RequestParam(value = "isOverite", required = false) boolean isOverite,
			@RequestParam(value = "isDisplayLimitedRecord", required = false) boolean isDisplayLimitedRecord) {
		
		logger.debug(">> getFileRecordDetails in ErrorReprocessDetailController " ); 
		String fileDownloadPath = this.servletContext.getRealPath(BaseConstants.ERROR_REPROCESS_FILE_DOWNLOAD_PATH);
		ResponseObject responseObject = this.errorReprocessBatchService.getFileRecordDetails(serverInstanceId, filePath, fileName, fileDownloadPath, fileType,isOverite,isDisplayLimitedRecord);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DIRECT_REPROCESS')")
	@RequestMapping(value = ControllerConstants.REPROCESS_PROCESSING_FILE_RECORDS, method = RequestMethod.POST)
	@ResponseBody
	public String reprocessProcessingFileRecords(
			@RequestParam(value = "fileRecords", required = true) String fileJsonObj,
			@RequestParam(value = "errorReprocessBatch", required = true) String errorReprocessBatch,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "fileType", required = true) String fileType,
			@RequestParam(value = "gridHeader", required = true) String gridHeader
			) throws SMException {
		
		String fileDownloadPath = this.servletContext.getRealPath(BaseConstants.ERROR_REPROCESS_FILE_DOWNLOAD_PATH);
		logger.debug(">> reprocessProcessingFileRecords in ErrorReprocessDetailController " ); 
		ResponseObject responseObject = this.errorReprocessBatchService.reprocessFileDetailRecord(fileJsonObj, errorReprocessBatch,fileDownloadPath,fileName,fileType,gridHeader);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_FILE')")
	@RequestMapping(value = ControllerConstants.VIEW_ERROR_FILE_DETAILS, method = RequestMethod.POST)
	@ResponseBody
	public String viewFileDetails(
			@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "filePath", required = true) String filePath,
			@RequestParam(value = "fileType", required = true) String fileType,
			@RequestParam(value = "isOverite", required = false) boolean isOverite
			) throws SMException {
		
		String fileDownloadPath = this.servletContext.getRealPath(BaseConstants.ERROR_REPROCESS_FILE_DOWNLOAD_PATH);
		ResponseObject responseObject = new ResponseObject() ;
		AjaxResponse ajaxResponse = new AjaxResponse();
		File file = new File(fileDownloadPath + File.separator + fileName);
		
		if(!file.exists() || isOverite){
			responseObject = this.errorReprocessBatchService.downloadFile(serverInstanceId, filePath, fileName,fileType,fileDownloadPath);
			
			if(responseObject.isSuccess()){
				file = (File) responseObject.getObject();
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
			}else{
				ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}	
		}else{
			responseObject.setSuccess(true);
			
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
		}
		
		if(responseObject.isSuccess()){
			
			if(".gz".equals(fileType)){
				try(FileInputStream fileInputStream = new FileInputStream(file);
						BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
						GZIPInputStream gzipInputStream = new GZIPInputStream(bufferedInputStream);
						InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream,Charset.defaultCharset()); 
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader)){
					
					setFileData(bufferedReader, ajaxResponse);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FAIL_VIEW_FILE);
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				}
			}else{
				try(FileReader fileReader = new FileReader(file); BufferedReader bufferedReader = new BufferedReader(fileReader);){
					setFileData(bufferedReader, ajaxResponse);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FAIL_VIEW_FILE);
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				}
			}
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will read file details and set in response.
	 * @param bufferedReader
	 * @param ajaxResponse
	 * @throws IOException
	 */
	private void setFileData(BufferedReader bufferedReader, AjaxResponse ajaxResponse ) throws IOException{
		StringBuilder sb = new StringBuilder();
		String line = bufferedReader.readLine();
	    int count = 0;
	    
	    int viewRecordLimit = Integer.parseInt(String.valueOf(MapCache.getConfigValueAsObject(SystemParametersConstant.RECORDS_FOR_INLINE_FILE_VIEW)))   ;
	    
		while (line != null) {
	        sb.append(line);
	        sb.append(System.lineSeparator());
	        line = bufferedReader.readLine();
	        count++;
	        if(count == (viewRecordLimit+1)){
	        	break;
	        }
	    }
	    String allFileDetails = sb.toString();
	    ajaxResponse.setObject(allFileDetails);
	}
	
	@PreAuthorize("hasAnyAuthority('DOWNLOAD_FILE')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_PROCESSING_FILE, method = RequestMethod.POST)
	@ResponseBody
	public String downloadProcessingFile(
			@RequestParam(value = "serverInstanceId", required = true) int serverInstanceId,
			@RequestParam(value = "fileName", required = true) String fileName,
			@RequestParam(value = "filePath", required = true) String filePath,
			@RequestParam(value = "fileType", required = true) String fileType,
			@RequestParam(value = "isOverite", required = false) boolean isOverite,
			HttpServletRequest request) throws SMException {
		
		
		String fileDownloadPath = this.servletContext.getRealPath(BaseConstants.ERROR_REPROCESS_FILE_DOWNLOAD_PATH);
		ResponseObject responseObject ;
		AjaxResponse ajaxResponse = new AjaxResponse();
		String dynamiURL = request.getScheme() + "://"+request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + File.separator + BaseConstants.ERROR_REPROCESS_FOLDER + File.separator + fileName;
		responseObject = this.errorReprocessBatchService.downloadFile(serverInstanceId, filePath, fileName,fileType,fileDownloadPath);
		if(responseObject.isSuccess()){
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
				ajaxResponse.setObject(dynamiURL);
		}else{
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}	
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPLOAD_REPROCESS_FILE')")
	@RequestMapping(value = ControllerConstants.UPLOAD_PROCESSING_FILE, method = RequestMethod.POST)
	@ResponseBody 
	public  String uploadProcessingFile(
				@RequestParam(value = "file", required = true) MultipartFile file, 
				@RequestParam(value = "errorReprocess", required = true) String errorReprocess,
				@RequestParam(value = "serviceType", required = false) String serviceType) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		if (!file.isEmpty() ){
			
			boolean isValidFile = true;
			//File validation only for processing and distribution
			if(EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(serviceType) || EngineConstants.PROCESSING_SERVICE.equalsIgnoreCase(serviceType)){
				String fileName = file.getOriginalFilename();
				isValidFile  = validationFileReprocessingExtention(fileName);
			}
			
			if(isValidFile){
				String tempPath =  this.servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
				File importFile = new File(tempPath + file.getOriginalFilename());
				try {
					file.transferTo(importFile);
				} catch (Exception e) {
					logger.error("Exception Occured : " + e);
					throw new  SMException(e.getMessage());
				} 
			ResponseObject responseObject = this.errorReprocessBatchService.uploadAndReprocessFile(errorReprocess, importFile);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			}else{
				logger.error("Invalid file extenstion for uploaded files.");
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage("invalid.upload.file"));
			}
		}else{
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("server.instance.import.no.file.select"));
		}
		return ajaxResponse.toString();
	}
	

	/**
	 * Method will validate file extenstion for uploaded file.
	 * @param fileName
	 * @return
	 */
	private boolean  validationFileReprocessingExtention(String fileName){
		
		String fileExtension = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
		if("csv".equalsIgnoreCase(fileExtension) || "gz".equalsIgnoreCase(fileExtension)){
			return true;
		}else{
			return false;
		}
	}
}
