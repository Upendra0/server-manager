package com.elitecore.sm.errorreprocess.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.dao.AutoErrorReprocessDao;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.job.dao.JobDao;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.EliteUtils;

@org.springframework.stereotype.Service(value="autoErrorReprocessServiceImpl")
public class AutoErrorReprocessServiceImpl implements AutoErrorReprocessService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	ServicesService servicesService;
	
	@Autowired
	AutoErrorReprocessDao autoErrorReprocessDao;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	QuartJobSchedulingListener joblistener;
	
	@Autowired
	JobDao jobDao;
	
	EliteUtils eliteUtils = new EliteUtils();
	
	@Autowired
	QuartJobSchedulingListener quartzListener;
	
	@Override
	@Transactional
	public ResponseObject getServiceByServerId(int serverId) {
		ResponseObject responseObject = new ResponseObject();
		List<Service> serviceList  = servicesService.getServiceList(serverId);
		if(serviceList != null && !serviceList.isEmpty()){
			logger.info("Service list found successfully for server " + serverId);
			responseObject.setObject(serviceList);
			responseObject.setSuccess(true);
		}else{
			logger.info("no service found for server Id " + serverId);
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.NO_SERVICE_FOUND_BY_SERVER);
		}
		return responseObject;
	}


	@Transactional
	@Override
	public ResponseObject addNewAutoReprocessConfig(AutoErrorReprocessDetail autoErrorReprocessDetail, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		CrestelSMJob job = autoErrorReprocessDetail.getJob();
		job.setJobName("AUTOREPROCESS_"+autoErrorReprocessDetail.getServerInstanceId()+"_job_"+UUID.randomUUID());
		job.setDescription("AUTOREPROCESS_"+autoErrorReprocessDetail.getServerInstanceId()+"_job");
		job.setParentTrigger(BaseConstants.SCHEDULER_PARENT_TRIGGER_PREFIX+job.getTrigger().getID());
		job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX);
		job.setJobType(JobTypeEnum.AutoErrorReprocess.name());
		jobService.save(job);
		job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX+job.getID());
		jobService.update(job);
		autoErrorReprocessDetail.setJob(job);
		autoErrorReprocessDao.save(autoErrorReprocessDetail);
		quartzListener.createAndScheduleJob(job);
		if(autoErrorReprocessDetail.getId()>0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_SUCCESS);
			responseObject.setObject(autoErrorReprocessDetail);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_FAILURE);
			responseObject.setObject(null);
		}
		logger.info(autoErrorReprocessDetail.toString());
		return responseObject;
		
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public long getAutoErrorReprocessListCount(boolean isSearch,String serviceInstanceIds,String category,String severity,String reasonCategory,String rule,String errorCode) {
		Map<String, Object> autoErrorReprocessTableDataConditions;
		if(isSearch) {
			autoErrorReprocessTableDataConditions = autoErrorReprocessDao.getProcessingServiceDetailsBySearchRule(serviceInstanceIds,category,severity,reasonCategory,rule,errorCode);
		}
		else {
			autoErrorReprocessTableDataConditions = autoErrorReprocessDao.getProcessingServiceDetailsByRule();
		}
		return autoErrorReprocessDao.getQueryCount(AutoErrorReprocessDetail.class, (List<Criterion>) autoErrorReprocessTableDataConditions.get("conditions"),null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<AutoErrorReprocessDetail> getPaginatedList(int startIndex, int limit,
			String sidx, String sord, String serviceInstanceIds, String category,String severity,String reasonCategory,String rule,String errorCode, boolean isSearch) {
		Map<String,Object> tableConditionList;
		if(!isSearch) {
			tableConditionList = autoErrorReprocessDao.getProcessingServiceDetailsByRule();
		}else {
			tableConditionList = autoErrorReprocessDao.getProcessingServiceDetailsBySearchRule(serviceInstanceIds,category,severity,reasonCategory,rule,errorCode);
		}
		return autoErrorReprocessDao.getPaginatedList(AutoErrorReprocessDetail.class, (List<Criterion>) tableConditionList.get("conditions"),
				null, startIndex, limit, sidx, sord);
	}
	
	@Override
	@Transactional
	public ResponseObject deleteAutoReprocessFiles(String ids, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(ids)){
			String [] idList = ids.split(",");
			for(int i = 0; i < idList.length; i ++ ){
				responseObject = deleteAutoReprocessFile(Integer.parseInt(idList[i]), staffId);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_DELETE_SUCCESS);
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_DELETE_FAILURE);
		}
		return responseObject;
	}
	private ResponseObject deleteAutoReprocessFile(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		if(id > 0){
			AutoErrorReprocessDetail autoErrorReprocessFile = autoErrorReprocessDao.findByPrimaryKey(AutoErrorReprocessDetail.class, id);
			if(autoErrorReprocessFile != null){
				CrestelSMJob job=autoErrorReprocessFile.getJob();
				if(job!=null){
					joblistener.deleteQuartzJob(job);
					job.setLastUpdatedByStaffId(staffId);
					job.setLastUpdatedDate(new Date());
					job.setStatus(StateEnum.DELETED);
					jobDao.merge(job);
				}
				autoErrorReprocessFile.setLastUpdatedByStaffId(staffId);
				autoErrorReprocessFile.setLastUpdatedDate(new Date());
				autoErrorReprocessFile.setStatus(StateEnum.DELETED);
				autoErrorReprocessDao.merge(autoErrorReprocessFile);
				quartzListener.deleteQuartzJob(autoErrorReprocessFile.getJob());
				responseObject.setSuccess(true);
				responseObject.setObject(autoErrorReprocessFile);
				responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_DELETE_SUCCESS);
			}else{
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_DELETE_FAILURE);
			}
		}else{
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_DELETE_FAILURE);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public AutoErrorReprocessDetail getErrorReprocessJobById(int jobId){
		return autoErrorReprocessDao.getErrorReprocessJobById(jobId);		
	}

	@Override
	@Transactional
	public ResponseObject getAutoErrorReprocessById(int id) {
		ResponseObject responseObject = new ResponseObject();
		AutoErrorReprocessDetail autoErrorReprocessDetail = autoErrorReprocessDao.findByPrimaryKey(AutoErrorReprocessDetail.class, id);
		
		if(autoErrorReprocessDetail != null){
			Hibernate.initialize(autoErrorReprocessDetail.getService());
			autoErrorReprocessDetail.setService(autoErrorReprocessDetail.getService());
			//logger.info(""+autoErrorReprocessDetail.setService(servicesService.getServiceById(autoErrorReprocessDetail.getService()));.getService().getId());
			responseObject.setObject(autoErrorReprocessDetail);  // set autoErrorReprocessDetail object in response object.
			responseObject.setSuccess(true);
			
		}else{
			logger.info("Failed to fetch autoErrorReprocessDetail details for Id " + id);
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_AUTO_ERROR_REPROCESS_DETAIL_BY_ID);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject updateAutoErrorReprocess(AutoErrorReprocessDetail autoErrorReprocessDetail, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		autoErrorReprocessDetail.setLastUpdatedByStaffId(staffId);
		autoErrorReprocessDetail.setLastUpdatedDate(new Date());

		CrestelSMJob job = autoErrorReprocessDetail.getJob();
		job.setLastUpdatedByStaffId(staffId);
		job.setLastUpdatedDate(new Date());
		// set parent trigger
		job.setParentTrigger(BaseConstants.SCHEDULER_PARENT_TRIGGER_PREFIX + job.getTrigger().getID());
		autoErrorReprocessDetail.setJob(job);
		jobService.merge(job);
		autoErrorReprocessDao.merge(autoErrorReprocessDetail);
		if (autoErrorReprocessDetail.getId() > 0) {
			//QuartJobSchedulingListener
			if(job!=null){
				joblistener.updateAndRescheduleJob(job);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_UPDATE_SUCCESS);
			responseObject.setObject(autoErrorReprocessDetail);
		} else {
			responseObject.setResponseCode(ResponseCode.AUTO_ERROR_REPROCESSING_UPDATE_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}


	@Override
	@Transactional
	public CrestelSMJob getJobByAutoReprocessJob(int id) {
		AutoErrorReprocessDetail autoErrorReprocessJob = autoErrorReprocessDao.getErrorReprocessJobById(id);
		return autoErrorReprocessJob.getJob();
	}
}
