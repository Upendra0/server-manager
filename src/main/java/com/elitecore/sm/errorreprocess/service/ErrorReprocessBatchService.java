package com.elitecore.sm.errorreprocess.service;

import java.io.File;
import java.util.List;

import com.elitecore.core.util.mbean.data.filereprocess.ServicewiseFileDetailsData;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;


public interface ErrorReprocessBatchService {

	
	public ResponseObject addErrorReprocessBatch(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject deleteFileReprocessDetails(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject getFileReprocessingJsonForGrid(Integer[] serviceIds, SearchErrorReprocessDetails reprocessDetails);
	
	public ResponseObject getFileReprocessingJsonFromEngine(SearchErrorReprocessDetails details, Integer[] serviceIds);
	
	/**
	 * Method  will get all grid details for processing service for selected service and rule .
	 * @param details
	 * @return
	 */
	public ResponseObject getProcessingServiceDetailsByRule(SearchErrorReprocessDetails details);
	
	
	/**
	 * Method will get Processing service error details by services and rule
	 * @param details
	 * @param serviceIds
	 * @return
	 */
	public ResponseObject getProcessingServiceErrorDetails(SearchErrorReprocessDetails details, Integer[] serviceIds);
	
	public ResponseObject getFileRecordDetails(int serverInstanceId, String filePath, String fileName,String smPath,String fileType,boolean isOverite,boolean isDisplayLimitedRecord);
	
	public ResponseObject reprocessFileDetailRecord(String fileDetailJson, String errorReprocessBatch, String filePath, String fileName,String fileType,String gridHeader) throws SMException;
	
	public ResponseObject uploadAndReprocessFile(String errorReprocessingBatch,File file) throws SMException ;

	public ResponseObject reprocessProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject applyRuleAndCreateBatch(String ruleActionConditionList, String erroReprocessJSON, int staffId) throws SMException;
	
	public ResponseObject deleteProcessingFileDetails(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject downloadFile(int serverInstanceId, String filePath, String fileName, String fileType, String fileDownloadPath);
	
	public ResponseObject revertModifiedFiles(int batchId);

	public List<ServicewiseFileDetailsData> setEngineErrorReprocessData(List<ErrorReprocessDetails> reprocessDetailList,List<RuleConditionDetails> actionConditionList,byte[] fileContent);
	
	public ResponseObject commonErrorReprocessApi(ResponseObject responseObject, List<Integer> serviceIdList, List<ErrorReprocessDetails> errorReprocessDetailList, List<RuleConditionDetails> actionConditionList, byte[] fileContent, ErrorReprocessingBatch errorReprocessingBatch, String actionType);
	
	public ResponseObject createBatch(ErrorReprocessingBatch errorReprocessingBatch, List<Integer> serviceIdList);
	
	public ResponseObject reprocessModifiedFileList(String errorReprocessJSON, Integer[] reprocessIdsLis,int staffId);
	
	public ResponseObject autoReprocessProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject addArchiveRestoreBatch(ErrorReprocessingBatch errorReprocessingBatch);
	
	public ResponseObject restoringProcessingFiles(ErrorReprocessingBatch errorReprocessingBatch);
	
}
