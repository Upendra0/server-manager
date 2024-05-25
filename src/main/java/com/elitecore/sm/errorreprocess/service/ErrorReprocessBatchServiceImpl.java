/**
 * 
 */
package com.elitecore.sm.errorreprocess.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.core.util.mbean.data.filereprocess.ErrorReprocessCategoryEnum;
import com.elitecore.core.util.mbean.data.filereprocess.FileDetailsData;
import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessConstants;
import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.core.util.mbean.data.filereprocess.PathDetailsData;
import com.elitecore.core.util.mbean.data.filereprocess.PluginDetailsData;
import com.elitecore.core.util.mbean.data.filereprocess.RuleDetailsData;
import com.elitecore.core.util.mbean.data.filereprocess.SearchFileCriteriaData;
import com.elitecore.core.util.mbean.data.filereprocess.ServicewiseFileDetailsData;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.service.ComposerService;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.errorreprocess.dao.ErrorReprocessBatchDao;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.CollectionService;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author Ranjitsinh Reval	
 *
 */
@org.springframework.stereotype.Service(value = "errorReprocessBatchService")
public class ErrorReprocessBatchServiceImpl  implements ErrorReprocessBatchService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ParserService parserService;
	
	@Autowired
	ComposerService composerService;
	
	@Autowired
	ReprocessDetailsService reprocessDetailsService;
	
	@Autowired
	ErrorReprocessBatchDao errorReprocessBatchDao;
	
	@Autowired
	private RuleActionConditionService actionConditionService;
	
	@Autowired
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	private Gson gson;
	
	/**
	 * 
	 * Method will add error re-process batch and batch details entry in database.
	 * @param errorReprocessingBatch
	 * @return 
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DIRECT_REPROCESS, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject addErrorReprocessBatch(ErrorReprocessingBatch errorReprocessingBatch) {
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.REPROCESS_FILES,null,true, null);
	}
	
	/**
	 * 
	 * Method will add archive restore batch and batch details entry in database.
	 * @param errorReprocessingBatch
	 * @return 
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_ARCHIVE_RESTORE, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject addArchiveRestoreBatch(ErrorReprocessingBatch errorReprocessingBatch) {
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.RESTORE_FILES,null,true, null);
	}
	
	/** 
	 * Method will re-process and call engine api according to action type. 
	 * @param errorReprocessingBatch
	 * @param actionType
	 * @param actionConditionList
	 * @param isCreateBatch
	 * @param fileContent
	 * @return
	 */
	public ResponseObject reprocessingServiceJMXCommonConfiguration(ErrorReprocessingBatch errorReprocessingBatch, String actionType, List<RuleConditionDetails> actionConditionList,boolean isCreateBatch, byte[] fileContent){
		
		List<Integer> serviceIdList = new ArrayList<>();
		ResponseObject responseObject = new ResponseObject();
		if(isCreateBatch){
			ErrorReprocessBatchService reprocessServiceImpl = (ErrorReprocessBatchService) SpringApplicationContext.getBean("errorReprocessBatchService"); // getting spring bean for aop context issue.
			responseObject  = reprocessServiceImpl.createBatch(errorReprocessingBatch, serviceIdList);
			responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
		}else{
			responseObject.setSuccess(true);
			if(errorReprocessingBatch != null) {
				responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
				List<ErrorReprocessDetails> reprocessDetails = errorReprocessingBatch.getReprocessDetailList();
				if(reprocessDetails != null && !reprocessDetails.isEmpty()) {
					int reprocessDetailsLength = reprocessDetails.size();
					for(int i = reprocessDetailsLength-1; i >= 0; i--) {
						serviceIdList.add(reprocessDetails.get(i).getService().getId());
					}
				}
			}
		}
		
		if(responseObject.isSuccess() && errorReprocessingBatch != null){
			logger.debug("Batch details created successfully now going to call engine reprocess api.");
			List<Integer> uniqueServiceIdList = serviceIdList.stream().distinct().collect(Collectors.toList()); 
			responseObject = commonErrorReprocessApi(responseObject, uniqueServiceIdList, errorReprocessingBatch.getReprocessDetailList(), actionConditionList, fileContent, errorReprocessingBatch, actionType);
			
		}
		return responseObject;
	}
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchService#commonErrorReprocessApi(com.elitecore.sm.common.util.ResponseObject, java.util.List, java.util.List, java.util.List, byte[], com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch, java.lang.String)
	 */
	@Override
	public ResponseObject commonErrorReprocessApi(ResponseObject responseObject, List<Integer> serviceIdList, List<ErrorReprocessDetails> errorReprocessDetailList, List<RuleConditionDetails> actionConditionList, byte[] fileContent, ErrorReprocessingBatch errorReprocessingBatch, String actionType) {
		
		List<ServicewiseFileDetailsData> serviceWiseDetailsList = setEngineErrorReprocessData(errorReprocessDetailList, actionConditionList, fileContent);
		
		if(serviceWiseDetailsList != null && !serviceWiseDetailsList.isEmpty()){
			Object[] serviceIdsObj = serviceIdList.toArray();
			Integer[] serviceIds = Arrays.copyOf(serviceIdsObj, serviceIdsObj.length, Integer[].class);
			
			Map<Integer, List<Service>> serverInstanceWiseServiceListMap = getMapForServerInstanceToServiceList(serviceIds);
			
			for (Map.Entry<Integer, List<Service>> entry : serverInstanceWiseServiceListMap.entrySet()){
				//Call engine api for every entry
				logger.info("key:"+entry.getKey());
				
				Service service  = entry.getValue().get(0);
				ServerInstance serverInstance = service.getServerInstance();
				RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				
				boolean jmxResponse = false;
				if(BaseConstants.REPROCESS_FILES.equalsIgnoreCase(actionType)){
					jmxResponse  = jmxConnection.reprocessErrorFiles(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}else if(BaseConstants.RESTORE_FILES.equalsIgnoreCase(actionType)){
				    jmxResponse  = jmxConnection.restoreArchiveFiles(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}else if(BaseConstants.DELETE_REPROCESS_FILES.equalsIgnoreCase(actionType)){
					jmxResponse  = jmxConnection.deleteErrorFiles(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}else if(BaseConstants.REVERT_REPROCESS_FILES.equalsIgnoreCase(actionType)){
					jmxResponse  = jmxConnection.revertToOriginalFiles(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}else if(BaseConstants.MODIFY_REPROCESS_FILES.equalsIgnoreCase(actionType)){
					jmxResponse  = jmxConnection.modifyErrorFiles(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}else if(BaseConstants.UPLOAD_AND_REPROCESS_FILES.equalsIgnoreCase(actionType)){
					jmxResponse  = jmxConnection.uploadAndReprocessFile(String.valueOf(errorReprocessingBatch.getId()), serviceWiseDetailsList);
				}
				
				if (jmxResponse) {
					responseObject.setSuccess(true);
				}else if (jmxConnection.getErrorMessage() != null) {
					logger.info(jmxConnection.getErrorMessage());
					
					if(BaseConstants.REVERT_REPROCESS_FILES.equalsIgnoreCase(actionType)){
						responseObject.setSuccess(false);	
					}else{
						responseObject.setSuccess(true);
					}
					setBatchFailStatus(errorReprocessingBatch, jmxConnection.getErrorMessage(),serverInstance.getId()); // Setting failure status for batch details.
				}else {
					if(BaseConstants.REVERT_REPROCESS_FILES.equalsIgnoreCase(actionType)){
						responseObject.setSuccess(false);	
					}else{
						responseObject.setSuccess(true);
					}
					setBatchFailStatus(errorReprocessingBatch, jmxConnection.getErrorMessage(),serverInstance.getId()); // Setting failure status for batch details.
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will change list batch details status to fail for failed operation. 
	 * @param errorReprocessingBatch
	 */
	private void setBatchFailStatus(ErrorReprocessingBatch errorReprocessingBatch, String failMessage, int serverInstanceId){
			
		logger.debug("Setting failure status for batch and server instance id :: " + serverInstanceId);
		if(errorReprocessingBatch != null && (errorReprocessingBatch.getReprocessDetailList() != null && !errorReprocessingBatch.getReprocessDetailList().isEmpty())){
			
			List<ErrorReprocessDetails> reprocessDetailList = errorReprocessingBatch.getReprocessDetailList();
			int size = reprocessDetailList.size();
			for (int i = size -1 ; i >= 0; i--) {
				ErrorReprocessDetails errorDetailObj = reprocessDetailList.get(i);
				if(errorDetailObj.getServerInstance() != null && errorDetailObj.getServerInstance().getId() == serverInstanceId){
					errorDetailObj.setFailureReason(failMessage);
					errorDetailObj.setReprocessStartTime(new Date());
					errorDetailObj.setReprocessEndTime(new Date());
					errorDetailObj.setErrorReprocessStatus(FileReprocessStatusEnum.FAILED);
				}
			}
			
			this.errorReprocessBatchDao.updateBatchDetails(errorReprocessingBatch);
		}
	} 
	
	/**
	 * Method will create batch and set all service ids in list.
	 * @param errorReprocessingBatch
	 * @param serviceIdList
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ResponseObject createBatch(ErrorReprocessingBatch errorReprocessingBatch, List<Integer> serviceIdList){
		ResponseObject responseObject = new ResponseObject();
		if(errorReprocessingBatch != null) {
			List<ErrorReprocessDetails> reprocessDetails = errorReprocessingBatch.getReprocessDetailList();
			if(reprocessDetails != null && !reprocessDetails.isEmpty()) {
				int reprocessDetailsLength = reprocessDetails.size();
				for(int i = reprocessDetailsLength-1; i >= 0; i--) {
					serviceIdList.add(reprocessDetails.get(i).getService().getId());
					updateErrorReprocessDetail(errorReprocessingBatch, reprocessDetails.get(i));
				}
			}
		}
		
		errorReprocessBatchDao.save(errorReprocessingBatch);
		if(errorReprocessingBatch != null && errorReprocessingBatch.getId() != 0) { 
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new Hibernate5Module());
			responseObject.setSuccess(true);
			try {
				responseObject.setObject(mapper.writeValueAsString(errorReprocessingBatch));
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage() , e);
			}
		} else {
			responseObject.setSuccess(false);	
			responseObject.setResponseCode(ResponseCode.SERVICE_REPROCESS_FAIL);
		}
		
		return responseObject;
	}
	/**
	 * 
	 * Method will create batch and batch details object also call engine api for processing service.
	 * @param errorReprocessingBatch
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DIRECT_REPROCESS, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject reprocessProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch) {
		logger.debug("Going to reprocess processing files.");
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.REPROCESS_FILES,null,true, null);
	}
	
	/**
	 * 
	 * Method will create batch and batch details object also call engine api for processing service.
	 * @param errorReprocessingBatch
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_ARCHIVE_RESTORE, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject restoringProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch) {
		logger.debug("Going to restore processing files.");
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.RESTORE_FILES,null,true, null);
	}
	
	@Override
	@Transactional
	public ResponseObject autoReprocessProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch) {
		logger.debug("Going to reprocess processing files.");
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.REPROCESS_FILES,null,true, null);
	}
	
	/**
	 * 
	 * Method will apply rules to all selected files and create batch with its details.
	 * @param errorReprocessingBatch
	 * @return
	 * @throws SMException 
	 */
	@Override
	@Transactional(rollbackFor = {SMException.class })
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_BULK_EDIT, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject applyRuleAndCreateBatch(String ruleActionConditionList, String erroReprocessJSON,int staffId) throws SMException {
		logger.debug("Inside create,apply rule and create batch with bulk edit batch.");
		ResponseObject responseObject = new ResponseObject();
		
		if((ruleActionConditionList != null && !"".equals(ruleActionConditionList))  && (erroReprocessJSON != null && !"".equals(erroReprocessJSON))){
			JSONArray jsonArray = new JSONArray(ruleActionConditionList);
			List<RuleConditionDetails> ruleConditionList = new ArrayList<>();
			for(int i=0; i< jsonArray.length();i++){
				RuleConditionDetails ruleCondition = new RuleConditionDetails();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				
				ruleCondition.setId(jsonObject.getInt("id"));
				ruleCondition.setActionExpression(jsonObject.getString("actionExpression"));
				ruleCondition.setConditionExpression(jsonObject.getString("conditionExpression"));
				ruleCondition.setApplicationOrder(jsonObject.getInt("applicationOrder"));
				ruleCondition.setCreatedDate(new Date());
				ruleCondition.setCreatedByStaffId(staffId);
				ruleConditionList.add(ruleCondition);
			}
			
			if(!ruleConditionList.isEmpty()){
				for (RuleConditionDetails ruleConditionDetails : ruleConditionList) {
					responseObject = actionConditionService.createActionCondition(ruleConditionDetails);
					if(!responseObject.isSuccess()){
						throw new SMException("Failed to create action condition.");
					}
				}
				ObjectMapper mapper = new ObjectMapper();
				ErrorReprocessingBatch errorReprocessingBatch;
				try {
					errorReprocessingBatch = mapper.readValue(erroReprocessJSON, ErrorReprocessingBatch.class);
					responseObject = reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.MODIFY_REPROCESS_FILES,ruleConditionList,true, null);
					responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
					return responseObject;
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new SMException("Failed to convert batch details to model. Please check logs for more details.");
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_APPLY_RULE_BULK_EDIT);
			}
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_APPLY_RULE_BULK_EDIT);
		}
		return responseObject;
	}
	
	/**
	 * 
	 * Method will apply rules to all selected files and create batch with its details.
	 * @param errorReprocessingBatch
	 * @return
	 * @throws SMException 
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DELETE_FILE, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject deleteFileReprocessDetails(ErrorReprocessingBatch errorReprocessingBatch) {
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.DELETE_REPROCESS_FILES,null,true, null);
	}
	
	/**
	 * 
	 * Method will delete processing files.
	 * @param errorReprocessingBatch
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DELETE_FILE, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject deleteProcessingFileDetails(ErrorReprocessingBatch errorReprocessingBatch) {
		return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.DELETE_REPROCESS_FILES,null,true, null);
	}
	
	/**
	 * Method will get unique server instance for given list of service ids.
	 * @param serviceIds
	 * @return
	 */
	public Map<Integer, List<Service>> getMapForServerInstanceToServiceList(Integer[] serviceIds) {
		ResponseObject responseObject;
		Map<Integer, List<Service>> serverInstanceWiseServiceListMap = new HashMap<>();
		if(serviceIds != null && serviceIds.length > 0) {
			responseObject = servicesService.getAllServiceByIds(serviceIds);
			if(responseObject != null && responseObject.isSuccess()) {
				@SuppressWarnings("unchecked")
				List<Service> serviceList = (List<Service>) responseObject.getObject();
				if(serviceList != null && !serviceList.isEmpty()) {
					int serviceLength = serviceList.size();
					for(int i = serviceLength-1; i >= 0; i--) {
						List<Service> mapValue = serverInstanceWiseServiceListMap.get(serviceList.get(i).getServerInstance().getId());
						if(mapValue == null) {
							List<Service> newMapServiceList = new ArrayList<>();
							newMapServiceList.add(serviceList.get(i));
							serverInstanceWiseServiceListMap.put(serviceList.get(i).getServerInstance().getId()
									, newMapServiceList);
						} else {
							mapValue.add(serviceList.get(i));
							serverInstanceWiseServiceListMap.put(serviceList.get(i).getServerInstance().getId()
									, mapValue);
						}
					}
				}
			}
		}
		return serverInstanceWiseServiceListMap;
	}
	
	/**
	 * Method will update error re-process details
	 * @param errorReprocessingBatch
	 * @param errorReprocessDetails
	 */
	public void updateErrorReprocessDetail(ErrorReprocessingBatch errorReprocessingBatch, ErrorReprocessDetails errorReprocessDetails) {
		errorReprocessDetails.setReprocessingBatch(errorReprocessingBatch);
		
		//set service object to batch detail
		Service service = servicesService.getServiceById(errorReprocessDetails.getService().getId());
		errorReprocessDetails.setService(service);
		
		//set service type object to batch detail
		errorReprocessDetails.setSvctype(service.getSvctype());
		
		//set server instance object to batch detail
		errorReprocessDetails.setServerInstance(service.getServerInstance());
		
		if(errorReprocessDetails.getParser() != null && errorReprocessDetails.getParser().getId() > 0) {
			Parser parser = parserService.getParserById(errorReprocessDetails.getParser().getId());
			errorReprocessDetails.setParser(parser);
		} else {
			errorReprocessDetails.setParser(null);
		}
		if(errorReprocessDetails.getComposer() != null && errorReprocessDetails.getComposer().getId() > 0) {
			Composer composer = composerService.getComposerById(errorReprocessDetails.getComposer().getId());
			errorReprocessDetails.setComposer(composer);
		} else {
			errorReprocessDetails.setComposer(null);
		}	
	}

	/**
	 * Method will get grid row data from database for give service ids.  
	 */
	@Override
	public ResponseObject getFileReprocessingJsonForGrid(Integer[] serviceIds, SearchErrorReprocessDetails reprocessDetails) {
		ResponseObject responseObject = null;
		JSONObject jsonObject = new JSONObject();
		
		JSONArray jsonArray = new JSONArray();
		if(serviceIds != null && serviceIds.length > 0) {
			responseObject = servicesService.getAllServiceByIds(serviceIds);
			if(responseObject != null && responseObject.isSuccess()) {
				@SuppressWarnings("unchecked")
				List<Service> services = (List<Service>) responseObject.getObject();
				if(services != null && !services.isEmpty()) {
					int length = services.size();
					for(int i = length-1; i >= 0; i--) {
						getJSONObjectFromService(services.get(i), jsonArray, reprocessDetails);
					}
				}
				jsonObject.put("rows", jsonArray);
				jsonObject.put("records", jsonArray.length());
				responseObject.setObject(jsonObject);
			} 
		}
		return responseObject;
	}
	
	/**
	 * Method will create json object for given service object.
	 * @param service
	 * @param jsonArray
	 */
	public void getJSONObjectFromService(Service service, JSONArray jsonArray, SearchErrorReprocessDetails reprocessDetails) {
		if(service instanceof ParsingService) {
			List<BaseModel> basePathList = EliteUtils.getActiveListFromGivenList(service.getSvcPathList());
			if(basePathList != null && !basePathList.isEmpty()) {
				int pathLength = basePathList.size();
				for(int i = pathLength-1; i >= 0; i--) {
					if(basePathList.get(i) instanceof ParsingPathList) {
						ParsingPathList parsingPathList = (ParsingPathList) basePathList.get(i);
						if(parsingPathList != null && !parsingPathList.getStatus().equals(StateEnum.DELETED)) {
							List<Parser> parserList = parsingPathList.getParserWrappers();
							String readFilePath = parsingPathList.getReadFilePath();
							if("Error".equalsIgnoreCase(reprocessDetails.getCategory())){ 
								JSONObject rowDefaultObject = new JSONObject();
								rowDefaultObject.put("id", "0_"+parsingPathList.getPathId()+"_DEFAULT");
								rowDefaultObject.put("ServiceId", service.getId());
								rowDefaultObject.put("ServerInstanceId", service.getServerInstance().getId());
								rowDefaultObject.put("ServiceTypeId", service.getSvctype().getId());
								rowDefaultObject.put("ServerInstance", service.getServerInstance().getName());
								rowDefaultObject.put("ServiceInstance", service.getName());
								rowDefaultObject.put("ServerIP:Port", service.getServerInstance().getServer().getIpAddress()+":"+service.getServerInstance().getPort());
								rowDefaultObject.put("Port", service.getServerInstance().getPort());
								rowDefaultObject.put("FilePath", "");
								rowDefaultObject.put("ReadFilePath", readFilePath);
								rowDefaultObject.put("PluginId", "0_"+parsingPathList.getPathId()+"_DEFAULT");
								rowDefaultObject.put("FileDetails", "");
								rowDefaultObject.put("PluginName", "DEFAULT");
								rowDefaultObject.put("PluginType", "DEFAULT");
								rowDefaultObject.put("FileCount", "");
								jsonArray.put(rowDefaultObject);
							}
							if(parserList != null && !parserList.isEmpty()) {
								int parserLength = parserList.size();
								for(int j = parserLength-1; j >= 0; j--) {
									Parser parser = parserList.get(j);
									if(parser != null && !parser.getStatus().equals(StateEnum.DELETED)) {
										
										JSONObject rowObject = new JSONObject();
										rowObject.put("id", parser.getId());
										rowObject.put("ServiceId", service.getId());
										rowObject.put("ServerInstanceId", service.getServerInstance().getId());
										rowObject.put("ServiceTypeId", service.getSvctype().getId());
										rowObject.put("ServerInstance", service.getServerInstance().getName());
										rowObject.put("ServiceInstance", service.getName());
										rowObject.put("ServerIP:Port", service.getServerInstance().getServer().getIpAddress()+":"+service.getServerInstance().getPort());
										rowObject.put("Port", service.getServerInstance().getPort());
										rowObject.put("FilePath", "");
										rowObject.put("ReadFilePath", readFilePath);
										rowObject.put("PluginId", parser.getId());
										rowObject.put("FileDetails", "");
										rowObject.put("PluginName", parser.getName());
										rowObject.put("PluginType", parser.getParserType().getAlias());
										rowObject.put("FileCount", "");
										jsonArray.put(rowObject);
									}
								}
							}
						}
					}
				}
			}
		} else if(service instanceof DistributionService) {
			List<Drivers> driverList = service.getMyDrivers();
			if(driverList != null && !driverList.isEmpty()) {
				int driverLength = driverList.size();
				for(int i = driverLength-1; i >= 0; i--) {
					Drivers driver = driverList.get(i);
					if(driver != null && !driver.getStatus().equals(StateEnum.DELETED)) {
						List<PathList> pathList = driver.getDriverPathList();
						if(pathList != null && !pathList.isEmpty()) {
							int pathLength = pathList.size();
							for(int j = pathLength-1; j >= 0; j--) {
								PathList path = pathList.get(j);
								if(path != null && path instanceof DistributionDriverPathList && !path.getStatus().equals(StateEnum.DELETED)) {
									DistributionDriverPathList distributionDriverPathList = (DistributionDriverPathList) path;
									if(distributionDriverPathList != null) {
										String readFilePath = distributionDriverPathList.getReadFilePath();
										
										if("Error".equalsIgnoreCase(reprocessDetails.getCategory()) || (!"Output".equalsIgnoreCase(reprocessDetails.getCategory()) && EngineConstants.DATABASE_DISTRIBUTION_DRIVER.equalsIgnoreCase(driver.getDriverType().getAlias()))){  
											JSONObject rowDefaultObject = new JSONObject();
											rowDefaultObject.put("id", driver.getApplicationOrder()+"_"+distributionDriverPathList.getPathId()+"_DEFAULT");
											rowDefaultObject.put("ServiceId", service.getId());
											rowDefaultObject.put("ServerInstanceId", service.getServerInstance().getId());
											rowDefaultObject.put("ServiceTypeId", service.getSvctype().getId());
											rowDefaultObject.put("ServerInstance", service.getServerInstance().getName());
											rowDefaultObject.put("ServiceInstance", service.getName());
											rowDefaultObject.put("ServerIP:Port", service.getServerInstance().getServer().getIpAddress()+":"+service.getServerInstance().getPort());
											rowDefaultObject.put("Port", service.getServerInstance().getPort());
											rowDefaultObject.put("FilePath", "");
											rowDefaultObject.put("ReadFilePath", readFilePath);
											rowDefaultObject.put("ComposerId", driver.getApplicationOrder()+"_"+distributionDriverPathList.getPathId()+"_DEFAULT");
											rowDefaultObject.put("ComposerName", "DEFAULT");
											rowDefaultObject.put("ComposerType", "DEFAULT");
											rowDefaultObject.put("FileDetails", "");
											rowDefaultObject.put("FileCount", "");
											rowDefaultObject.put("DriverName", driver.getName());
											rowDefaultObject.put("DriverType", driver.getDriverType().getAlias());
											jsonArray.put(rowDefaultObject);
										}
										
										List<Composer> composerList = distributionDriverPathList.getComposerWrappers();
										if(composerList != null && !composerList.isEmpty()) {
											int composerLength = composerList.size();
											for(int k = composerLength-1; k >= 0; k--) {
												Composer composer = composerList.get(k);
												if(composer != null && !composer.getStatus().equals(StateEnum.DELETED)) {
													
													JSONObject rowObject = new JSONObject();
													rowObject.put("id", composer.getId());
													rowObject.put("ServiceId", service.getId());
													rowObject.put("ServerInstanceId", service.getServerInstance().getId());
													rowObject.put("ServiceTypeId", service.getSvctype().getId());
													rowObject.put("ServerInstance", service.getServerInstance().getName());
													rowObject.put("ServiceInstance", service.getName());
													rowObject.put("ServerIP:Port", service.getServerInstance().getServer().getIpAddress()+":"+service.getServerInstance().getPort());
													rowObject.put("Port", service.getServerInstance().getPort());
													rowObject.put("FilePath", "");
													rowObject.put("ReadFilePath", readFilePath);
													rowObject.put("ComposerId", composer.getId());
													rowObject.put("ComposerName", composer.getName());
													rowObject.put("ComposerType", composer.getComposerType().getAlias());
													rowObject.put("FileDetails", "");
													rowObject.put("FileCount", "");
													rowObject.put("DriverName", driver.getName());
													rowObject.put("DriverType", driver.getDriverType().getAlias());
													jsonArray.put(rowObject);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} else if(service instanceof CollectionService) {
			List<Drivers> driverList = service.getMyDrivers();
			if(!CollectionUtils.isEmpty(driverList)) {
				int driverLength = driverList.size();
				for(int i = driverLength-1; i >= 0; i--) {
					Drivers driver = driverList.get(i);
					if(driver != null && !driver.getStatus().equals(StateEnum.DELETED)) {
						List<PathList> pathList = driver.getDriverPathList();
						if(pathList != null && !pathList.isEmpty()) {
							int pathLength = pathList.size();
							for(int j = pathLength-1; j >= 0; j--) {
								PathList path = pathList.get(j);
								if(path != null && path instanceof CollectionDriverPathList && !path.getStatus().equals(StateEnum.DELETED)) {
									CollectionDriverPathList collectionDriverPathList = (CollectionDriverPathList) path;
									if(collectionDriverPathList != null) {
										String readFilePath = collectionDriverPathList.getReadFilePath();
										JSONObject rowObject = new JSONObject();
										rowObject.put("id", collectionDriverPathList.getId());
										rowObject.put("ServiceId", service.getId());
										rowObject.put("ServerInstanceId", service.getServerInstance().getId());
										rowObject.put("ServiceTypeId", service.getSvctype().getId());
										rowObject.put("ServerInstance", service.getServerInstance().getName());
										rowObject.put("ServiceInstance", service.getName());
										rowObject.put("ServerIP:Port", service.getServerInstance().getServer().getIpAddress()+":"+service.getServerInstance().getPort());
										rowObject.put("Port", service.getServerInstance().getPort());
										rowObject.put("FilePath", "");
										rowObject.put("ReadFilePath", readFilePath);
										rowObject.put("FileDetails", "");
										rowObject.put("FileCount", "");
										rowObject.put("PathId", collectionDriverPathList.getPathId());
										rowObject.put("DriverName", driver.getName());
										rowObject.put("DriverType", driver.getDriverType().getAlias());
										jsonArray.put(rowObject);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * Method will get json object details from engine and set details accordingly.
	 * @see com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchService#getFileReprocessingJsonFromEngine(com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails, java.lang.Integer[])
	 */
	@Override
	public ResponseObject getFileReprocessingJsonFromEngine(SearchErrorReprocessDetails searchCriateria, Integer[] serviceIds) {
		
		ServerInstance serverInstance ;
		List<String> serviceInstanceIds = new ArrayList<>(); ;
		List<Integer> serviceUniqueId =  new ArrayList<>();  ;
		String serviceType ;
		Service service = new Service();
		ResponseObject responseObject = setServiceListDetails(serviceInstanceIds, serviceUniqueId, serviceIds, service);
		if(responseObject != null && responseObject.isSuccess()){
			 serviceType = service.getSvctype().getAlias(); //NOSONAR
			 serverInstance = service.getServerInstance();  //NOSONAR
			 if(serverInstance != null) {
					SearchFileCriteriaData searchFileCriateriaData  = getFinalEngineSearchCriateriaObject(serviceInstanceIds, serviceUniqueId, serviceType, searchCriateria);
					RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
					String jmxResponse = jmxConnection.getServicesDetailsBySearchCriteria("1", searchFileCriateriaData);
					
					if (jmxResponse != null && jmxConnection.getErrorMessage() == null) {
						
						Type collectionType = new TypeToken<List<ServicewiseFileDetailsData>>(){}.getType();
						List<ServicewiseFileDetailsData> listOfServiceWiseDetailData = gson.fromJson(jmxResponse, collectionType);
						
						JSONArray jsonArray = getAPIResponseDetailsObj(listOfServiceWiseDetailData,searchCriateria); // Setting json object for all service based on engine api response.
						responseObject.setObject(jsonArray);
						responseObject.setSuccess(true);
					} else if (jmxConnection.getErrorMessage() != null) {
						logger.info(jmxConnection.getErrorMessage());
						responseObject.setSuccess(false);
						JSONArray jsonArray = finalizeFailureResponse(serviceIds, BaseConstants.SERVER_INSTANCE_NOT_RUNNING); // setting failure message.
						responseObject.setObject(jsonArray);
					} else {
						responseObject.setSuccess(false);
						JSONArray jsonArray = finalizeFailureResponse(serviceIds, BaseConstants.SERVER_INSTANCE_NOT_RUNNING );
						responseObject.setObject(jsonArray);
					}
			 }
		}
		
		return responseObject;
	}
	
	/**
	 * Method will generate json object for all service grid data.
	 * @param servicewiseFileDetailsData
	 */
	public JSONArray getAPIResponseDetailsObj(List<ServicewiseFileDetailsData> listOfServiceWiseDetailData,SearchErrorReprocessDetails searchCriateria ){
		logger.debug("Inside setting final service jsong response.");
		JSONArray jsonArray = new JSONArray();
		if(listOfServiceWiseDetailData != null && !listOfServiceWiseDetailData.isEmpty()){
			int size = listOfServiceWiseDetailData.size();
			
 			for(int i = size -1; i >= 0; i--){
				
				ServicewiseFileDetailsData serviceWiseFileDetails = listOfServiceWiseDetailData.get(i);
				List<PathDetailsData> pathDetailsList = serviceWiseFileDetails.getPathDetailsDatas();
						 
						if(pathDetailsList != null && !pathDetailsList.isEmpty()){
							 
							 int pathListSize = pathDetailsList.size();
							 
							 for(int j = pathListSize-1; j >= 0; j--){
								 
								 PathDetailsData pathDetail = pathDetailsList.get(j);
								 
								 if( EngineConstants.COLLECTION_SERVICE.equalsIgnoreCase(serviceWiseFileDetails.getServiceType())){
									 
									 List<FileDetailsData> fileDetailList = pathDetail.getFileDetailsDatas();
									 	int fileDetailSize = 0;
									 	
									 	if(!CollectionUtils.isEmpty(fileDetailList)){
									 		fileDetailSize = fileDetailList.size();
									 	}
									 	
									 	JSONObject object = new JSONObject();
									     // Output
										 if(ErrorReprocessCategoryEnum.OUTPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
									 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId() + "_" + pathDetail.getPathInstanceId() + "_" + pathDetail.getPathId(), pathDetail.getOutputPath());
									     }
										 // Duplicate
										 else{
									 	 	object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId() + "_" + pathDetail.getPathInstanceId() + "_" + pathDetail.getPathId(), pathDetail.getDuplicatePath());
									 	 } 
										 object.put("fileCount_" + serviceWiseFileDetails.getServiceUniqueId() + "_" + pathDetail.getPathInstanceId() + "_" + pathDetail.getPathId(), fileDetailSize);
										 object.put("fileDetails_" + serviceWiseFileDetails.getServiceUniqueId() + "_" + pathDetail.getPathInstanceId() + "_" + pathDetail.getPathId(), fileDetailList);
										 object.put("errorReprocessCheckbox_" + serviceWiseFileDetails.getServiceUniqueId() + "_" + pathDetail.getPathInstanceId() + "_" +  pathDetail.getPathId(), "");
										 object.put("isFileLimitExceed", serviceWiseFileDetails.isFileLimitExceed());
										 jsonArray.put(object);
									 
								 } else if (EngineConstants.PARSING_SERVICE.equalsIgnoreCase(serviceWiseFileDetails.getServiceType()) 
										 || EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(serviceWiseFileDetails.getServiceType())){
									 
									 List<PluginDetailsData> pluginDetailList = pathDetail.getPluginDetailsDatas();
									 if(pluginDetailList != null && !pluginDetailList.isEmpty()){
									
										 int pluginSize = pluginDetailList.size();
										 for(int k = pluginSize -1; k>= 0; k-- ){
											 PluginDetailsData pluginDetails = pluginDetailList.get(k);
											 
											 List<FileDetailsData> fileDetailList;
											 if(ErrorReprocessCategoryEnum.INPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())
													 ||ErrorReprocessCategoryEnum.ARCHIVE.getValue().equalsIgnoreCase(searchCriateria.getCategory())) {
												 fileDetailList = pathDetail.getFileDetailsDatas();
											 } else {
												 fileDetailList = pluginDetails.getFileDetailsDatas();
											 }
											 
											 int fileDetailSize = 0;
											 if(!CollectionUtils.isEmpty(fileDetailList)){
											 		fileDetailSize = fileDetailList.size();
											 }
											 JSONObject object = new JSONObject();
										     if(ErrorReprocessCategoryEnum.INPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
										    	object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), pathDetail.getSourcePath());
										 	 }else if(ErrorReprocessCategoryEnum.OUTPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
										 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), pluginDetails.getErrorPath());
										 	 }else if(ErrorReprocessCategoryEnum.ARCHIVE.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
										 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), pathDetail.getArchivePath());
										     }else{
										 	 	object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), pathDetail.getErrorPath());
										 	 } 
											 object.put("fileCount_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), fileDetailSize);
											 object.put("fileDetails_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), fileDetailList);
											 object.put("errorReprocessCheckbox_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pluginDetails.getPluginId(), "");
											 object.put("isFileLimitExceed", serviceWiseFileDetails.isFileLimitExceed());
											 jsonArray.put(object);
										 }
									 }
								 }else if(EngineConstants.PROCESSING_SERVICE.equalsIgnoreCase(serviceWiseFileDetails.getServiceType())){
									 	
								 	List<FileDetailsData> fileDetailList = pathDetail.getFileDetailsDatas();
								 	int fileDetailSize = 0;
								 	
								 	if(!CollectionUtils.isEmpty(fileDetailList)){
								 		fileDetailSize = fileDetailList.size();
								 	}
								 	
								 	JSONObject object = new JSONObject();
								 	
								 	if(ErrorReprocessCategoryEnum.INVALID.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getInvalidPath());
								 	}else if(ErrorReprocessCategoryEnum.FILTER.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getFilterPath());
								 	}else if(ErrorReprocessCategoryEnum.DUPLICATE.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getDuplicatePath());
								 	}else if(ErrorReprocessCategoryEnum.INPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getSourcePath());
								 	}else if(ErrorReprocessCategoryEnum.OUTPUT.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getOutputPath());
								 	}else if(ErrorReprocessCategoryEnum.ARCHIVE.getValue().equalsIgnoreCase(searchCriateria.getCategory())){
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getArchivePath());
								 	}else {
								 		object.put("errorPath_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), pathDetail.getErrorPath());
								 	}
									
									object.put("fileCount_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), fileDetailSize);
									object.put("fileDetails_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(),fileDetailList);
									object.put("errorReprocessCheckbox_" + serviceWiseFileDetails.getServiceUniqueId()+"_" + pathDetail.getPathId(), "");
									object.put("isFileLimitExceed", serviceWiseFileDetails.isFileLimitExceed());
									jsonArray.put(object);
								 }
							 }
						 }else{
							 if("-1".equals(serviceWiseFileDetails.getServiceId()) ){
								 jsonArray.put(setServiceFailureResponse(serviceWiseFileDetails.getServiceUniqueId()));	 
							 }
						 }
					}
		}
		logger.info("Final service response is :: " + jsonArray);
		
		return jsonArray;
	}
	
	/**
	 * Method will disable and show failure message in front end.
	 * @param serviceId
	 * @param errorMessage
	 * @return
	 */
	public JSONArray finalizeFailureResponse(Integer[] serviceIds, String errorMessage) {
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < serviceIds.length; i++) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("errorReprocessCheckbox_"+serviceIds[i], errorMessage);
			jsonObject.put("errorPath_" + serviceIds[i], errorMessage);
			jsonObject.put("fileCount_" + serviceIds[i], errorMessage);
			jsonObject.put("fileDetails_" + serviceIds[i], errorMessage);
			jsonArray.put(jsonObject);
		}
		return jsonArray;
	}
	
	/**
	 * Method will set failure response for service id.
	 * @param serviceid
	 * @param errorMessage
	 * @return
	 */
	public JSONObject setServiceFailureResponse(int serviceid){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("errorReprocessCheckbox_"+serviceid, -1);
		jsonObject.put("errorPath_" + serviceid, -1);
		jsonObject.put("fileCount_" + serviceid, -1);
		jsonObject.put("fileDetails_" + serviceid, -1);
		return jsonObject;
	}

	@Override
	@Transactional
	public ResponseObject getProcessingServiceDetailsByRule(SearchErrorReprocessDetails details) {
		logger.debug("Fetchnig all grid details for service and rule details.");
		ResponseObject responseObject = new ResponseObject();
		Integer [] serviceIds = Utilities.convertStringArrayToInt(details.getServiceInstanceIds());
		List<Object[]> objectList ;
		if(serviceIds != null){
			objectList = this.errorReprocessBatchDao.getProcessingServiceDetailsByRule(serviceIds, details.getRuleId(),details.getCategory());
			if(objectList != null && !objectList.isEmpty()){
				JSONObject finalJson = new JSONObject();
				finalJson.put("total", "5");
				finalJson.put("records", "10");
				finalJson.put("page", "1");
				JSONArray jsonArray = new JSONArray();
				logger.info("processing service details found successfully!");
				for (Object[] objects : objectList) {
					JSONObject jsonObj = new JSONObject();
					
					
					if("Filter".equalsIgnoreCase(details.getCategory()) || "Invalid".equalsIgnoreCase(details.getCategory())){
						jsonObj.put("ruleId", objects[9]);
						jsonObj.put("ruleAlias", objects[10]);
					}
					
					jsonObj.put("id", objects[0] + "_" + objects[8]);
					jsonObj.put("serviceId", objects[0]);
					jsonObj.put("serverInstanceId", objects[1]);
					jsonObj.put("serverInstanceName", objects[3]);
					jsonObj.put("serviceName", objects[2]);
					jsonObj.put("serverIpPort", objects[4] + ":"+ objects[5]);
					jsonObj.put("serverInstancePort", objects[5]);
					
					jsonObj.put("filePath", "");
					jsonObj.put("fileCount", "");
					jsonObj.put("fileDetails", "");
					jsonObj.put("serviceTypeId", objects[6]);
					jsonObj.put("readFilePath", objects[7]);
					jsonObj.put("pathId", objects[8]);
					
					jsonArray.put(jsonObj);
				}
				finalJson.put("rows", jsonArray);
				responseObject.setObject(finalJson);
				responseObject.setSuccess(true);
			}
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getProcessingServiceErrorDetails(SearchErrorReprocessDetails searchCriateria, Integer[] serviceIds) {
		logger.debug("Fetching processing service error details."); 
		
		ServerInstance serverInstance ;
		List<String> serviceInstanceIds = new ArrayList<>(); ;
		List<Integer> serviceUniqueId =  new ArrayList<>();  ;
		String serviceType ;
		Service service = new Service();
		ResponseObject responseObject = setServiceListDetails(serviceInstanceIds, serviceUniqueId, serviceIds, service);
		
		if(responseObject != null && responseObject.isSuccess()){
			 serviceType = service.getSvctype().getAlias(); 
			 serverInstance = service.getServerInstance();  
	
			 if(serverInstance != null) {
				SearchFileCriteriaData searchFileCriateriaData  = getFinalEngineSearchCriateriaObject(serviceInstanceIds, serviceUniqueId, serviceType, searchCriateria);
				RemoteJMXHelper jmxConnection = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(), serverInstance.getMaxConnectionRetry(), serverInstance.getRetryInterval(), serverInstance.getConnectionTimeout());
				String jmxResponse = jmxConnection.getServicesDetailsBySearchCriteria("1", searchFileCriateriaData);
				
				if (jmxResponse != null && jmxConnection.getErrorMessage() == null) {
					Type collectionType = new TypeToken<List<ServicewiseFileDetailsData>>(){}.getType();
					List<ServicewiseFileDetailsData> listOfServiceWiseDetailData = gson.fromJson(jmxResponse, collectionType);
					JSONArray jsonArray = getAPIResponseDetailsObj(listOfServiceWiseDetailData,searchCriateria);
					responseObject.setObject(jsonArray);
					responseObject.setSuccess(true);
				} else if (jmxConnection.getErrorMessage() != null) {
					logger.info(jmxConnection.getErrorMessage());
					responseObject.setSuccess(false);
					JSONArray jsonArray = finalizeFailureResponse(serviceIds,BaseConstants.SERVER_INSTANCE_NOT_RUNNING);
					responseObject.setObject(jsonArray);
				} else {
					responseObject.setSuccess(false);
					JSONArray jsonArray = finalizeFailureResponse(serviceIds, BaseConstants.SERVER_INSTANCE_NOT_RUNNING);
					responseObject.setObject(jsonArray);
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will get all services list by service ids and return all list of service instance and unique id.
	 * @param serviceInstanceIds
	 * @param serviceUniqueId
	 * @param serviceIds
	 * @param serviceObj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ResponseObject setServiceListDetails(List<String> serviceInstanceIds,List<Integer> serviceUniqueId,Integer[] serviceIds, Service serviceObj){
		ResponseObject responseObject = new ResponseObject();
		if(serviceIds != null && serviceIds.length > 0) {
			responseObject = servicesService.getAllServiceByIds(serviceIds);
			if(responseObject != null && responseObject.isSuccess()) {
				List<Service> services = (List<Service>) responseObject.getObject();
				 if(services != null && !services.isEmpty()) {
					 serviceObj.setServerInstance(services.get(0).getServerInstance());
					 serviceObj.setSvctype(services.get(0).getSvctype());
					 
					 int size = services.size();
					  for(int i = size-1; i >= 0; i--) {
							serviceInstanceIds.add(services.get(i).getServInstanceId());
							serviceUniqueId.add(services.get(i).getId());
							
					  }
				  }
				 responseObject.setSuccess(true);
			}
		}else{
			logger.error("server id arraylist found null.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FOUND_SERVICE_ID_LIST_NULL);
		}
		return responseObject;
	}
	
	/**
	 * Method will set all SearchFileCriateria details for get all service object. 
	 * @return
	 */
	private SearchFileCriteriaData getFinalEngineSearchCriateriaObject(List<String> serviceInstanceIds,List<Integer> serviceUniqueId,String serviceType, SearchErrorReprocessDetails searchCriateria){
		logger.debug("Setting SearchFileCriteriaData details for service type " + serviceType);
		
		SearchFileCriteriaData searchFileCriateriaData = new SearchFileCriteriaData();
		searchFileCriateriaData.setServiceType(serviceType);
		searchFileCriateriaData.setServiceUniqueIdList(serviceUniqueId);
		searchFileCriateriaData.setServiceIdList(serviceInstanceIds);
		searchFileCriateriaData.setFileNameContains(searchCriateria.getFileNameContains());
		searchFileCriateriaData.setFilterCategory(searchCriateria.getCategory());
		searchFileCriateriaData.setRuleAlias(searchCriateria.getRuleAlias());
		searchFileCriateriaData.setFromDate(searchCriateria.getFromDate());
		searchFileCriateriaData.setToDate(searchCriateria.getToDate());
		return searchFileCriateriaData;
	}

	/* (non-Javadoc)
	 * @see com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchService#getFileRecordDetails(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public ResponseObject getFileRecordDetails(int serverInstanceId, String absoluteFilePath, String fileName, String fileDownloadPath, String fileType, boolean isOverite,boolean isDisplayLimitedRecord ) {
		logger.debug("Inside get file record details to fetch all file details of " + fileName);
		
		File file = new File(fileDownloadPath + File.separator + fileName);
		ResponseObject responseObject= downloadFile(serverInstanceId, absoluteFilePath, fileName, fileType, fileDownloadPath) ;
		
		if(!responseObject.isSuccess()){
			return responseObject;
		}
			
		if(BaseConstants.FILE_GZ_COMPRESS_EXT.equalsIgnoreCase(fileType)){
			try(FileInputStream fileInputStream = new FileInputStream(file);
				BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
				GZIPInputStream gzipInputStream = new GZIPInputStream(bufferedInputStream);
				InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream,Charset.defaultCharset()); 
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
				responseObject = generateDynamicGridDetails(bufferedReader,isDisplayLimitedRecord);
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_READ_FILE_DATA);
			}
		}else{
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
				responseObject = generateDynamicGridDetails(bufferedReader,isDisplayLimitedRecord);
			}catch (IOException e) {
				logger.error(e.getMessage(), e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_READ_FILE_DATA);
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will read file details and make dynamic grid column details.
	 * @param bufferedReader
	 * @return
	 * @throws IOException
	 */
	public ResponseObject generateDynamicGridDetails(BufferedReader bufferedReader,boolean isDisplayLimitedRecord) throws IOException {
		logger.debug("Inside generate dynamicgrid details");

		ResponseObject responseObject = new ResponseObject();
		JSONArray columnName = new JSONArray();
		JSONArray columnNameData = new JSONArray();
		JSONArray columnModel = new JSONArray();
		JSONArray gridData = new JSONArray();

		String line;
		String cvsSplitBy = ",";

		String headerString = bufferedReader.readLine();
		String[] headerArray = headerString.split(cvsSplitBy);
		for (int i = 0; i < headerArray.length; i++) {
			columnNameData.put(headerArray[i]);
			JSONObject colModelJson = new JSONObject();
			colModelJson.put("name", headerArray[i]);
			colModelJson.put("index", headerArray[i]);
			colModelJson.put("editable", true);
			columnModel.put(colModelJson);
		}
		columnName.put(columnNameData);
		int count = 0;
		int viewRecordLimit = Integer.parseInt(String.valueOf(MapCache.getConfigValueAsObject(SystemParametersConstant.RECORDS_FOR_INLINE_FILE_VIEW)))   ;
		while ((line = bufferedReader.readLine()) != null) {
			JSONObject jsonGridObj = new JSONObject();
			String[] lineArray = line.split(cvsSplitBy);

			for (int i = 0; i < lineArray.length; i++) {
				jsonGridObj.put(headerArray[i], lineArray[i]);
			}
			gridData.put(jsonGridObj);
			count++;
			
			if(isDisplayLimitedRecord && count == viewRecordLimit){
				break;
			}
		}

		JSONObject finalObj = new JSONObject();
		finalObj.put("columnName", columnNameData);
		finalObj.put("columnModel", columnModel);
		finalObj.put("gridData", gridData);

		responseObject.setSuccess(true);
		responseObject.setObject(finalObj);

		return responseObject;
	}

	/**
	 * Method will re-process selected file using calling engine api. 
	 * @return
	 * @throws SMException 
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_UPLOAD_REPROCESS, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject uploadAndReprocessFile(String errorReprocessingDetails, File file) throws SMException {
		logger.debug("Inside upload and reprocess uploaded file details.");
		ResponseObject responseObject = new ResponseObject();
		ObjectMapper mapper = new ObjectMapper();
		try {
			ErrorReprocessingBatch errorReprocessingBatch = mapper.readValue(errorReprocessingDetails, ErrorReprocessingBatch.class);
			byte[] fileData = Files.readAllBytes(file.toPath());
			responseObject = reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.UPLOAD_AND_REPROCESS_FILES,null,true, fileData);
			responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_UPLOAD_REPROCESS_FILE);
		} 
		return responseObject;
	}

	/**
	 * Method will download file from engine API and store it to 
	 * 
	 */
	@Override
	@Transactional
	public ResponseObject downloadFile(int serverInstanceId, String absoluteFilePath, String fileName,String fileType, String fileDownloadPath) {
		logger.debug("download file " + fileName+ " from file path " + absoluteFilePath);
		
		ResponseObject responseObject = new ResponseObject();
		
		if(serverInstanceId > 0){
			ServerInstance serverInstance  = this.serverInstanceService.getServerInstance(serverInstanceId);
			if(serverInstance != null ){
				
				FileDetailsData fileDetailsData = new FileDetailsData();
				
				fileDetailsData.setFileName(fileName); 
				fileDetailsData.setAbsoluteFileName(absoluteFilePath);
				fileDetailsData.setFileType(fileType);
				
				File file = new File(fileDownloadPath + File.separator + fileDetailsData.getFileName());
				
				if(file.exists()){
					try {
						file.delete();
						file.createNewFile();
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_FILE);
						return responseObject;
					}
				}
				responseObject = callDownloadFileAPI(serverInstance, fileDetailsData,file,null,null);
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_FILE);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_DOWNLOAD_FILE);
		}
		return responseObject;
	}
	
	/**
	 * Method will recursively call engine download API and get data based on batch size.
	 * @param serverInstance
	 * @param fileDetailsData
	 * @param file
	 * @param writer
	 * @param output
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ResponseObject callDownloadFileAPI(ServerInstance serverInstance,FileDetailsData fileDetailsData,File file, Writer writer,FileOutputStream output ) {
		ResponseObject responseObject = new ResponseObject();
		
		if("NA".equals(fileDetailsData.getFileType())) {
			RemoteJMXHelper remoteJmxHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),
					serverInstance.getMaxConnectionRetry() ,serverInstance.getRetryInterval(),serverInstance.getConnectionTimeout());
			Map<String, Object>  resultDataMap1 = remoteJmxHelper.getBytesOfFile(fileDetailsData);
			Boolean isFailed = (Boolean) resultDataMap1.get(FileReprocessConstants.DOWNLOAD_ERROR) ;
			if (!isFailed) {
				FileOutputStream fileOutputStream1 = null;
				try {
					byte[] fileBytes = (byte[]) resultDataMap1.get(FileReprocessConstants.FILE_BYTES);
					fileOutputStream1 = new FileOutputStream(file);
					fileOutputStream1.write(fileBytes);
					responseObject.setObject(file);
					responseObject.setSuccess(true);
				} catch (IOException io) {
					logger.error(io.getMessage(), io);
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FAIL_ERROR_FILE_DOWNLOAD);
				} finally {
					if (fileOutputStream1 != null) {
						try {
							fileOutputStream1.close();
						} catch (IOException e) {
							logger.error("Failed to close the stream", e);
						}
					}
				}
			} else {
				logger.error("Error occured at engine side while getting file.");
				responseObject.setSuccess(false);
				Integer errorCode =  Integer.parseInt(String.valueOf(resultDataMap1.get(FileReprocessConstants.DOWNLOAD_ERROR_CODE)));
				setErrorMessage(responseObject, errorCode);
			}
			return responseObject;
		}
		RemoteJMXHelper remoteJmxHelper = new RemoteJMXHelper(serverInstance.getServer().getIpAddress(), serverInstance.getPort(),serverInstance.getMaxConnectionRetry() ,serverInstance.getRetryInterval(),serverInstance.getConnectionTimeout());
		int batchRecordSize = Integer.parseInt(MapCache.getConfigValueAsObject(SystemParametersConstant.FILE_REPROCESS_FILE_RECORDS_BATCH_SIZE).toString());
		Map<String, Object>  resultDataMap = remoteJmxHelper.downloadFile(fileDetailsData,batchRecordSize);  // No need for batch id need to remove it from engine api.
		
		if (remoteJmxHelper.getErrorMessage() != null) {
			logger.info(remoteJmxHelper.getErrorMessage());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_JMX_DONWLOAD_API);
		}else{
			if(resultDataMap != null && !resultDataMap.isEmpty()){
				
				Boolean isFailed = (Boolean) resultDataMap.get(FileReprocessConstants.DOWNLOAD_ERROR) ;
				
				if(!isFailed){
					List<String[]> fileRecordsList = (List<String[]>) resultDataMap.get(FileReprocessConstants.FILE_DATA_LIST);
					
					Boolean callAPIAgain = (Boolean) resultDataMap.get(FileReprocessConstants.DOWNLOAD_COMPLETED);
					if(fileRecordsList != null && !fileRecordsList.isEmpty()){
				
						if(BaseConstants.FILE_GZ_COMPRESS_EXT.equalsIgnoreCase(fileDetailsData.getFileType())){
							try(FileOutputStream fileOutputStream = output == null ? new FileOutputStream(file):output; 
									Writer fileWriter = writer == null ? (new OutputStreamWriter(new GZIPOutputStream(fileOutputStream), Charset.defaultCharset())) : writer){ //NOSONAR
								
								StringBuilder stringBuilder = new StringBuilder();
								int fileRecordsSize = fileRecordsList.size();
								for(int i = 0; i< fileRecordsSize; i++){
									writeFileRecords(fileRecordsList.get(i),stringBuilder);
								}
								
								fileWriter.write(stringBuilder.toString());
								
								if(!callAPIAgain){
									callDownloadFileAPI(serverInstance, fileDetailsData,file,fileWriter,fileOutputStream);
								}
								responseObject.setObject(file);
								responseObject.setSuccess(true);
								
							}catch(IOException io){
								logger.error(io.getMessage(), io);
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.FAIL_ERROR_FILE_DOWNLOAD);
							}
						}else{
							try( Writer fileWriter =  writer == null ?  new FileWriter(file):writer; ){ //NOSONAR
								
								StringBuilder stringBuilder = new StringBuilder();
								int fileRecordsSize = fileRecordsList.size();
								for(int i = 0; i< fileRecordsSize; i++){
									writeFileRecords(fileRecordsList.get(i), stringBuilder);
								}
								
								fileWriter.write(stringBuilder.toString());

								if(!callAPIAgain){
									callDownloadFileAPI(serverInstance, fileDetailsData,file,fileWriter,null);
								}
								responseObject.setObject(file);
								responseObject.setSuccess(true);
								
							}catch(IOException io){
								logger.error(io.getMessage(), io);
								responseObject.setSuccess(false);
								responseObject.setResponseCode(ResponseCode.FAIL_ERROR_FILE_DOWNLOAD);
							}
						}
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FAIL_ZERO_SIZE_FILE_DOWNLOAD);
					}
				}else{
					logger.error("Error occured at engine side while getting file.");
					responseObject.setSuccess(false);
					Integer errorCode =  Integer.parseInt(String.valueOf(resultDataMap.get(FileReprocessConstants.DOWNLOAD_ERROR_CODE)));
					setErrorMessage(responseObject, errorCode);
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will set error message based on engine exception.
	 * @param responseObject
	 */
	public void setErrorMessage(ResponseObject responseObject, Integer errorCode){
		if(errorCode == 1){
			responseObject.setResponseCode(ResponseCode.DOWNLOAD_FAIL_FILE_NOT_EXIST);
		}else if(errorCode == 2 || errorCode == 3){
			responseObject.setResponseCode(ResponseCode.DOWNLOAD_FAIL_FILE_CONFIG_WRONG);
		}else if(errorCode == 4 ){
			responseObject.setResponseCode(ResponseCode.FAIL_ERROR_FILE_DOWNLOAD);
		}else{
			responseObject.setResponseCode(ResponseCode.FAIL_ERROR_FILE_DOWNLOAD);
		}
	}
	
	/**
	 *  Method will write records in file using file writer stream 
	 * @param fileRecords
	 * @param writer
	 * @param isStreamOpen
	 * @throws IOException
	 */
	private void writeFileRecords(String [] fileRecords, StringBuilder stringBuilder) throws IOException{
		if(fileRecords != null ){
			int totalLength = fileRecords.length;
			for(int i = 0; i< totalLength; i++){
				stringBuilder.append(fileRecords[i] + ",");
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("\n");
		}
	}

	/**
	 * @param fileDetailJson
	 * @param errorReprocessBatch
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DIRECT_REPROCESS, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject reprocessFileDetailRecord(String fileDetailJson, String errorReprocessBatch, String filePath, String fileName,String fileType,String gridHeader) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		ObjectMapper mapper = new ObjectMapper();
		File file =  createUpdatedRecordFile(fileName, filePath, fileDetailJson,fileType,gridHeader); 
		if(file != null){
			try {	
				ErrorReprocessingBatch errorReprocessingBatch = mapper.readValue(errorReprocessBatch, ErrorReprocessingBatch.class);
				byte[] fileData = Files.readAllBytes(file.toPath());
				return reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.UPLOAD_AND_REPROCESS_FILES,null,true, fileData);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_REPROCESS_EDITED_FILE);
			}
		}else{
			logger.info("Faile to create csv file " + fileName);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_CREATE_CSV_FILE);
		}
		return responseObject;
	}
	
	/**
	 * Method will create new updated file from updated grid data.
	 * @param fileName
	 * @param filePath
	 * @return
	 */
	private File createUpdatedRecordFile(String fileName, String filePath, String fileData, String fileType,String gridHeader){
		logger.debug("inside create new file " + fileName + " on path " + filePath);
		try {
			JSONArray gridData = new JSONArray(fileData);
			File file = new File(filePath + File.separator + fileName);
			if(file.exists()){
				file.delete();
				file.createNewFile();
			}
			String csvData = CDL.toString(gridData);
			if (BaseConstants.FILE_GZ_COMPRESS_EXT.equals(fileType)) {
				try (GZIPOutputStream zip = new GZIPOutputStream(new FileOutputStream(file));
						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zip, Charset.defaultCharset()))) {
					writer.append(csvData);
					return file;
				} catch (IOException ex) {
					logger.error(ex.getMessage(), ex);
					return null;
				}
			} else {
				FileUtils.writeStringToFile(file, csvData);
				return file;
			}
		} catch (JSONException | IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Method will revert all modified file to original.
	 * @param batchid
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_REVERT_TO_ORIGINAL, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject revertModifiedFiles(int batchId) {
		ErrorReprocessingBatch erroreRerprocessBatch = this.errorReprocessBatchDao.findByPrimaryKey(ErrorReprocessingBatch.class, batchId);
		ResponseObject responseObject = new ResponseObject();
		if(erroreRerprocessBatch != null){
			Hibernate.initialize(erroreRerprocessBatch.getReprocessDetailList());
			responseObject = reprocessingServiceJMXCommonConfiguration(erroreRerprocessBatch, BaseConstants.REVERT_REPROCESS_FILES,null,false, null);
			if(responseObject.isSuccess()){
				responseObject.setResponseCode(ResponseCode.REVERT_MODIFIED_FILE_SUCESS);	
			}else{
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FAIL_REVERT_FILES);
			}
			responseObject.setResponseCode(ResponseCode.REVERT_MODIFIED_FILE_SUCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_REVERT_FILES);
		}
		return responseObject;
	}
	
	/**
	 * Method will convert error re-process batch details child object to engine input pojo.
	 * @param reprocessDetailsList
	 * @param action condition list
	 */
	@Override
	public List<ServicewiseFileDetailsData> setEngineErrorReprocessData(List<ErrorReprocessDetails> reprocessDetailList, List<RuleConditionDetails> actionConditionList, byte[] fileContent) {
		logger.debug("Inside set engine error reprocess details for making ");
		Map<Integer,ServicewiseFileDetailsData> engineInputData = new HashMap<>();
		if(reprocessDetailList != null && !reprocessDetailList.isEmpty()){
			
			ServicewiseFileDetailsData serviceWiseFileDetailsData  ;
			for (ErrorReprocessDetails reprocessDetails : reprocessDetailList) {
				
				Service service = reprocessDetails.getService();
				ServiceType serviceType = service.getSvctype();
				
				String serviceId = serviceType.getAlias()+"-"+service.getServInstanceId();
				int id = service.getId();
				
				if(engineInputData.get(id) != null){
					
					serviceWiseFileDetailsData  = engineInputData.get(id);
					serviceWiseFileDetailsData.setServiceType(serviceType.getAlias());
					serviceWiseFileDetailsData.setServiceId(serviceId);
					
					setPathListDetails(serviceWiseFileDetailsData, reprocessDetails, fileContent);
					
				}else{
					 serviceWiseFileDetailsData = new ServicewiseFileDetailsData();
					 serviceWiseFileDetailsData.setServiceUniqueId(id);
					 serviceWiseFileDetailsData.setServiceType(serviceType.getAlias());
					 serviceWiseFileDetailsData.setServiceId(serviceId);
					 setPathListDetails(serviceWiseFileDetailsData, reprocessDetails, fileContent);
					 
					 setRuleConditionDetails(actionConditionList, serviceWiseFileDetailsData);
					 
					 engineInputData.put(id, serviceWiseFileDetailsData);
				}
			}
		}
		logger.info("Final service wise details data is:: " + engineInputData);
		return getServiceWiseFileDetailsList(engineInputData);
	}

	/**
	 * 
	 * Method will print ans make service wise file details data.
	 * @param engineInputData
	 */
	private List<ServicewiseFileDetailsData> getServiceWiseFileDetailsList(Map<Integer,ServicewiseFileDetailsData> engineInputData ){
		List<ServicewiseFileDetailsData> serviceWiseDetailList = new ArrayList<>();
		if(engineInputData != null && !engineInputData.isEmpty()){
			for (Map.Entry<Integer,ServicewiseFileDetailsData> entry : engineInputData.entrySet()){
				logger.info("Service id :: " + entry.getKey());
				serviceWiseDetailList.add(entry.getValue());
			}
		}
		return serviceWiseDetailList;
	}
	
	/**
	 * Method will set condition and action details in service wise details engine request
	 * @param actionConditionList
	 */
	public void setRuleConditionDetails(List<RuleConditionDetails> actionConditionList,ServicewiseFileDetailsData serviceWiseFileDetailsData){
		
		logger.debug("Inside setRuleConditionDetails method for setting rule condition details list.");
		if (actionConditionList != null && !actionConditionList.isEmpty()) {
			List<RuleDetailsData> ruleDetailsDatas = serviceWiseFileDetailsData.getRuleDetailsDatas();
			if(ruleDetailsDatas == null){
				ruleDetailsDatas = new ArrayList<>();
			}
			
			for (RuleConditionDetails actionConditionObj : actionConditionList) {
				RuleDetailsData ruleData = new RuleDetailsData();
				ruleData.setCondition(actionConditionObj.getConditionExpression());
				ruleData.setAction(actionConditionObj.getActionExpression());
				ruleDetailsDatas.add(ruleData);
			}
			
			serviceWiseFileDetailsData.setRuleDetailsDatas(ruleDetailsDatas);
		}
	}
	
	/**
	 * Method will set path details for services.
	 * @param serviceWiseFileDetailsData
	 * @param reprocessDetails
	 */
	public void setPathListDetails(ServicewiseFileDetailsData serviceWiseFileDetailsData, ErrorReprocessDetails reprocessDetails,byte[] fileContent){
		List<PathDetailsData> pathDataList = serviceWiseFileDetailsData.getPathDetailsDatas();
		
		if(pathDataList == null ){
			pathDataList = new ArrayList<>();
		}
		
		PathDetailsData pathDetails = checkUniquePathDetails(pathDataList, reprocessDetails.getReadFilePath());
		
		if(pathDetails == null){
			pathDetails = new PathDetailsData();
			pathDetails.setPathId(null);
			pathDetails.setReprocessfilePath(reprocessDetails.getReadFilePath());
			pathDetails.setErrorPath(reprocessDetails.getFilePath());
			
			pathDataList.add(pathDetails);
		}		
	
		if(EngineConstants.PROCESSING_SERVICE.equalsIgnoreCase(serviceWiseFileDetailsData.getServiceType()) || EngineConstants.COLLECTION_SERVICE.equalsIgnoreCase(serviceWiseFileDetailsData.getServiceType()) ){
			setFileDetails(pathDetails, reprocessDetails,null,fileContent);
		}else if(EngineConstants.PARSING_SERVICE.equalsIgnoreCase(serviceWiseFileDetailsData.getServiceType()) || EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(serviceWiseFileDetailsData.getServiceType())){
			setPluginDetails(pathDetails, reprocessDetails, fileContent);
		}
		serviceWiseFileDetailsData.setPathDetailsDatas(pathDataList);
	}
	
	
	/**
	 * Method will check unique plugin object.
	 * @param pluginDetailList
	 * @param pluginName
	 * @return
	 */
	private PluginDetailsData checkUniquePluginDetails(List<PluginDetailsData> pluginDetailList,String pluginName){
		if(pluginDetailList != null && !pluginDetailList.isEmpty()){
			int size = pluginDetailList.size();
			for (int i = size -1; i >= 0; i--) {
				if(pluginName.equalsIgnoreCase(pluginDetailList.get(i).getPluginName())){
					return pluginDetailList.get(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Method will check current error details object is for same path of another path object.
	 * @param pathDataList
	 * @param pathSourcePath
	 * @return
	 */
	private PathDetailsData  checkUniquePathDetails(List<PathDetailsData> pathDataList, String pathSourcePath){
		if(pathDataList != null && !pathDataList.isEmpty()){
			int size = pathDataList.size();
			for (int i = size -1; i >= 0; i--) {
				if(pathSourcePath.equalsIgnoreCase(pathDataList.get(i).getReprocessfilePath())){
					return pathDataList.get(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Method will set plug-in details for distribution and parsing service.
	 * @param path
	 * @param reprocessDetails
	 */
	public void setPluginDetails(PathDetailsData path, ErrorReprocessDetails reprocessDetails,byte[] fileContent){
		logger.debug("Inside set plug-in details");
		
		List<PluginDetailsData> pluginDetailList = path.getPluginDetailsDatas();
		
		if(pluginDetailList == null ){
			pluginDetailList = new ArrayList<>();
		}
		
		PluginDetailsData pluginData ;
		
		if(EngineConstants.PARSING_SERVICE.equalsIgnoreCase(reprocessDetails.getService().getSvctype().getAlias())){
			
			logger.debug("Setting plugin details for parsing service.");
			
			Parser parser = reprocessDetails.getParser();
			if(parser == null) {
				pluginData = checkUniquePluginDetails(pluginDetailList, "DEFAULT") ;
				if(pluginData == null){
					
					pluginData = new PluginDetailsData();
				
					pluginData.setPluginId("DEFAULT");
					pluginData.setErrorPath(reprocessDetails.getFilePath());
					pluginData.setPluginName("DEFAULT");
					
					pluginDetailList.add(pluginData);
				}
				
			} else {
				pluginData = checkUniquePluginDetails(pluginDetailList, parser.getName()) ;
				if(pluginData == null){
					
					pluginData = new PluginDetailsData();
				
					pluginData.setPluginId(String.valueOf(parser.getId()));
					pluginData.setErrorPath(reprocessDetails.getFilePath());
					pluginData.setPluginName(parser.getName());
					
					pluginDetailList.add(pluginData);
				}
			}
			setFileDetails(null, reprocessDetails,pluginData,fileContent); // setting all file details object values.

		}else if(EngineConstants.DISTRIBUTION_SERVICE.equalsIgnoreCase(reprocessDetails.getService().getSvctype().getAlias())){
			logger.debug("Setting plugin details for Distribution service.");
			Composer composer = reprocessDetails.getComposer();
			if(composer == null) {
				pluginData = checkUniquePluginDetails(pluginDetailList, "DEFAULT") ;
				if(pluginData == null){
					pluginData = new PluginDetailsData();
				
					pluginData.setPluginId(String.valueOf("DEFAULT"));
					pluginData.setErrorPath(reprocessDetails.getFilePath());
					pluginData.setPluginName("DEFAULT");
					
					pluginDetailList.add(pluginData);
				}
				
			} else {
				pluginData = checkUniquePluginDetails(pluginDetailList, composer.getName()) ;
				if(pluginData == null){
					pluginData = new PluginDetailsData();
				
					pluginData.setPluginId(String.valueOf(composer.getId()));
					pluginData.setErrorPath(reprocessDetails.getFilePath());
					pluginData.setPluginName(composer.getName());
					
					pluginDetailList.add(pluginData);
				}
			}
			setFileDetails(null, reprocessDetails,pluginData,fileContent); // setting all file details object values.
		}
		path.setPluginDetailsDatas(pluginDetailList);
	}
	
	/**
	 * Method will set file details object in path/plugin based on service.
	 * @param path
	 * @param reprocessDetails
	 * @param pluginData
	 */
	public void setFileDetails(PathDetailsData path, ErrorReprocessDetails reprocessDetails, PluginDetailsData pluginData,byte[] fileContent){
		
		logger.debug("Inside set file details ");
		List<FileDetailsData> fileDetailList = null;
		
		if(path != null){
			fileDetailList  = path.getFileDetailsDatas();
			if(fileDetailList == null ){
				fileDetailList = new ArrayList<>();
			}
		}
		
		if(pluginData != null){
			fileDetailList  = pluginData.getFileDetailsDatas();
			if(fileDetailList == null ){
				fileDetailList = new ArrayList<>();
			}
		}
		
		
		String sourceType ;
		if(reprocessDetails.isInputSourceCompress()){
			sourceType = BaseConstants.FILE_GZ_COMPRESS_EXT ;
		}else{
			sourceType = BaseConstants.FILE_CSV_COMPRESS_EXT ; 

		}
		FileDetailsData  fileObj = new FileDetailsData(reprocessDetails.getAbsoluteFilePath(), reprocessDetails.getFileName(), null, reprocessDetails.getFileSize().longValue(),sourceType);
		fileObj.setReprocessDetailId(reprocessDetails.getId());
		fileObj.setBackupFileAbsoluteName(reprocessDetails.getFileBackUpPath());
		fileObj.setFileContent(fileContent);
		fileObj.setReprocessCategory(reprocessDetails.getReprocessingBatch().getErrorCategory());
		
		if(reprocessDetails.isCompress()){
			fileObj.setFileType(BaseConstants.FILE_GZ_COMPRESS_EXT);
		}else{
			fileObj.setFileType(BaseConstants.FILE_CSV_COMPRESS_EXT );
		}
	
		if(fileDetailList != null){
			fileDetailList.add(fileObj);
		}
		
		
		if(path != null){
			logger.info("Setting file details for path ");
			path.setFileDetailsDatas(fileDetailList);
			
		}else if(pluginData != null){  //NOSONAR
			pluginData.setFileDetailsDatas(fileDetailList);
		}
	}


	/**
	 * List<Integer> reprocessIdsLis
	 * @param errorReprocessJSON
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	@Transactional(rollbackFor = SMException.class)
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_DIRECT_REPROCESS, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject reprocessModifiedFileList(String errorReprocessJSON, Integer[] reprocessIds,int staffId) {
		logger.debug("inside reprocessing modified files list.");
		ObjectMapper mapper = new ObjectMapper();
		ResponseObject responseObject = new ResponseObject();
		ErrorReprocessingBatch errorReprocessingBatch;
		try {
			errorReprocessingBatch = mapper.readValue(errorReprocessJSON, ErrorReprocessingBatch.class);
			
			responseObject = reprocessingServiceJMXCommonConfiguration(errorReprocessingBatch, BaseConstants.REPROCESS_FILES,null,true, null);
			
			if(responseObject.isSuccess()){
				
				String errorReprocessBatch = (String) responseObject.getObject();
				
				logger.info("Files are reporcessed successfully now going to change status for selected reprocess details id.");
				responseObject = this.reprocessDetailsService.getAllBatchDetailsByIds(reprocessIds);
				responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
				if(responseObject.isSuccess()){
					
					List<ErrorReprocessDetails> reprocessDetailsList  = (List<ErrorReprocessDetails>) responseObject.getObject();
					int size = reprocessDetailsList.size();
					for(int i=0; i < size; i++ ){
						ErrorReprocessDetails errorReprocessDetails = reprocessDetailsList.get(i);
						
						errorReprocessDetails.setLastUpdatedByStaffId(staffId);
						errorReprocessDetails.setLastUpdatedDate(new Date());
						errorReprocessDetails.setModifyFileReprocessed(true);
						
						this.reprocessDetailsService.updateReprocessDetails(errorReprocessDetails);
					}
					responseObject.setSuccess(true);
					responseObject.setObject(errorReprocessBatch);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_REPROCESS_MODIFIED_FILES);
		}
		return responseObject;
	}
	
}