/**
 * 
 */
package com.elitecore.sm.errorreprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.dao.ErrorReprocessBatchDao;
import com.elitecore.sm.errorreprocess.dao.ErrorReprocessDetailDao;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;


/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value="reprocessDetailService")
public class ReprocessDetailServiceImpl implements ReprocessDetailsService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ErrorReprocessDetailDao reprocessDetailDao;
	
	@Autowired
	ErrorReprocessBatchDao errorReprocessBatchDao;
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	ErrorReprocessBatchService errorReprocessBatchService;
	
	/**
	 * @param reprocessDetails
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public long getTotalReprocessBatchCount(SearchErrorReprocessDetails reprocessDetails) {
		Map<String, Object> serviceConditions = reprocessDetailDao.getBatchDetailsBySearchParameters(reprocessDetails);
		return reprocessDetailDao.getQueryCount(ErrorReprocessDetails.class, (List<Criterion>) serviceConditions.get("conditions"),
				(HashMap<String, String>) serviceConditions.get("aliases"));
	}
	
	
	/**
	 * @param reprocessDetails
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getPaginatedList(SearchErrorReprocessDetails reprocessDetails, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> serviceConditions =  reprocessDetailDao.getBatchDetailsBySearchParameters(reprocessDetails);
		
		List<ErrorReprocessDetails>  errorReprocessingList =  reprocessDetailDao.getErrorReprocessDetailsPaginatedList(ErrorReprocessDetails.class, (List<Criterion>) serviceConditions.get("conditions"),
				(HashMap<String, String>) serviceConditions.get("aliases"), startIndex, limit, sidx, sord);
		
		if(errorReprocessingList != null && !errorReprocessingList.isEmpty()){
			for (ErrorReprocessDetails errorReprocessDetails : errorReprocessingList) {
				row = new HashMap<>();
				row.put("id", errorReprocessDetails.getId());
				row.put("batchId", errorReprocessDetails.getReprocessingBatch().getId());
				row.put("serviceType", errorReprocessDetails.getSvctype().getAlias());
				
				row.put("serviceId", errorReprocessDetails.getService().getId());
				if(errorReprocessDetails.getParser() != null) {
					row.put("pluginId", errorReprocessDetails.getParser().getId());
				} else {
					row.put("pluginId", 0);
				}
				
				if(errorReprocessDetails.getComposer() != null) {
					row.put("composerId", errorReprocessDetails.getComposer().getId());
				} else {
					row.put("composerId", 0);
				}
				row.put("fileBackupPath", errorReprocessDetails.getFileBackUpPath());
				row.put("isCompress", errorReprocessDetails.isCompress());
				row.put("serviceTypeId", errorReprocessDetails.getSvctype().getId());
				row.put("readFilePath", errorReprocessDetails.getReadFilePath());
				row.put("errorCategory", errorReprocessDetails.getReprocessingBatch().getErrorCategory());
				
				//row.put("fileName", errorReprocessDetails.getFileName());
				row.put("action", errorReprocessDetails.getReprocessingBatch().getErrorProcessAction().toString());
				row.put("status", errorReprocessDetails.getErrorReprocessStatus().toString());
				row.put("type", errorReprocessDetails.getFileReprocessType().toString());
				row.put("viewDetails", "");
				
				row.put("action", errorReprocessDetails.getReprocessingBatch().getErrorProcessAction().toString());
				row.put("actionDetail",errorReprocessDetails.getReprocessingBatch().getUserComment());
				row.put("failReason", errorReprocessDetails.getFailureReason());
				row.put("startTime", DateFormatter.formatDate(errorReprocessDetails.getReprocessStartTime()) );
				row.put("endTime", DateFormatter.formatDate(errorReprocessDetails.getReprocessEndTime()));
				row.put("serverInstance", errorReprocessDetails.getServerInstance().getName());
				row.put("serverInstanceId", errorReprocessDetails.getServerInstance().getId());
				row.put("serviceInstance", errorReprocessDetails.getService().getName());
				row.put("absoluteFilePath", errorReprocessDetails.getAbsoluteFilePath());
				row.put("isModifiedFileReprocessed", errorReprocessDetails.isModifyFileReprocessed());
				
				String pluginName = "";
				String pluginType = "";
				
			    if(errorReprocessDetails.getParser() != null ){
			    	pluginName = errorReprocessDetails.getParser().getName();
			    	pluginType = errorReprocessDetails.getParser().getParserType().getAlias(); 
			    }else if(errorReprocessDetails.getComposer() != null){
			    	pluginName = errorReprocessDetails.getComposer().getName();
			    	pluginType = errorReprocessDetails.getComposer().getComposerType().getAlias();
			    }
			    
				row.put("pluginName", pluginName);
				row.put("pluginType", pluginType);
				row.put("filePath",errorReprocessDetails.getFilePath());
				row.put("fileName", errorReprocessDetails.getFileName());
				row.put("fileSize", errorReprocessDetails.getFileSize());
				row.put("category", errorReprocessDetails.getReprocessingBatch().getErrorCategory());
				row.put("compress",  String.valueOf(errorReprocessDetails.isCompress()));
				
				rowList.add(row);
			}
		}
		return  rowList;
	}


	@Override
	@Transactional
	public ResponseObject getServiceByType(String serviceAlias) {
		ResponseObject responseObject = new ResponseObject();
		
		List<Service> serviceList  = servicesService.getServiceListByAlias(serviceAlias);
		if(serviceList != null && !serviceList.isEmpty()){
			logger.info("Service list found successfully for type " + serviceAlias);
			responseObject.setObject(serviceList);
			responseObject.setSuccess(true);
		}else{
			logger.info("no service found for service type " + serviceAlias);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.NO_SERVICE_FOUND_BY_TYPE);
			
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getAllBatchDetailsById(int batchId) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Inside get all batch details for batch id :: " + batchId);
		
		List<ErrorReprocessDetails> reprocessDetailsList = this.reprocessDetailDao.getAllFileDetailsByBatchId(batchId);
		if(reprocessDetailsList != null && !reprocessDetailsList.isEmpty()){
			responseObject.setSuccess(true);
			responseObject.setObject(reprocessDetailsList);
		}else{
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.FILE_REPROCESS_REVERT_TO_ORIGINAL, actionType = BaseConstants.FILE_REPROCESS, currentEntity = ErrorReprocessingBatch.class, ignorePropList= "")
	public ResponseObject revertMultipleModifiyFiles(Integer[] detailIds) {
		ResponseObject responseObject = new ResponseObject();
		if(detailIds != null && detailIds.length > 0) {
			responseObject = getAllBatchDetailsByIds(detailIds);
			if(responseObject != null && responseObject.isSuccess()) {
				List<Integer> serviceIdList = new ArrayList<>();
				ErrorReprocessingBatch errorReprocessingBatch = null;
				List<ErrorReprocessDetails> errorReprocessDetailList = (List<ErrorReprocessDetails>) responseObject.getObject();
				if(errorReprocessDetailList != null && !errorReprocessDetailList.isEmpty()) {
					int detailLength = errorReprocessDetailList.size();
					for(int i = 0; i < detailLength; i++) {
						if(i == 0) {
							errorReprocessingBatch = errorReprocessDetailList.get(i).getReprocessingBatch();
						}
						serviceIdList.add(errorReprocessDetailList.get(i).getService().getId());
						errorReprocessDetailList.get(i).setErrorReprocessStatus(FileReprocessStatusEnum.IN_QUEUE);
						this.reprocessDetailDao.update(errorReprocessDetailList.get(i));
					}
				}
				if(errorReprocessingBatch != null) {
					responseObject.setModuleName(String.valueOf(errorReprocessingBatch.getId()));
				}
				responseObject = this.errorReprocessBatchService.commonErrorReprocessApi(responseObject, serviceIdList, errorReprocessDetailList, null, null, errorReprocessingBatch, BaseConstants.REVERT_REPROCESS_FILES);
			}
		}
		return responseObject;
	}
	
	/**
	 * Method will get all batch detail list details by selected id's using HQL criteria.
	 * 
	 * @param ids
	 * @return responseObject
	 */
	@Override
	@Transactional(readOnly=true)
	public ResponseObject getAllBatchDetailsByIds(Integer[] ids) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Going to fetch batch detail list");
		List<ErrorReprocessDetails> errorReprocessDetailList = this.reprocessDetailDao.getAllErrorReprocessDetailByIds(ids);

		if (errorReprocessDetailList != null && !errorReprocessDetailList.isEmpty()) {
			responseObject.setSuccess(true);
			responseObject.setObject(errorReprocessDetailList);
			logger.info(errorReprocessDetailList.size() + "  batch detail list found.");
		} else {
			logger.info("Failed to batch detail list for id ");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_BATCH_DETAIL_BY_IDS);
		}
		return responseObject;
	}


	/**
	 * Method will update errorreprocess details object 
	 */
	@Override
	@Transactional
	public void updateReprocessDetails(ErrorReprocessDetails reprocessDetails) {
		this.reprocessDetailDao.merge(reprocessDetails);
	}
	

}
