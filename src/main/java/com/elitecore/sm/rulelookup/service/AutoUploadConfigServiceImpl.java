package com.elitecore.sm.rulelookup.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.job.dao.JobDao;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.rulelookup.dao.IAutoUploadConfigurationDao;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.scheduler.QuartJobSchedulingListener;



@Service(value="autoUploadConfigService")
public class AutoUploadConfigServiceImpl implements IAutoUploadConfigService {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IAutoUploadConfigurationDao  autoUploadConfigurationDao;
	
	@Autowired
	JobService jobService;
	
	@Autowired
	JobDao jobDao;
	
	@Autowired
	QuartJobSchedulingListener joblistener;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getAutoUploadConfigCount(String searchSourceDir,String searchTableName,String searchScheduler) {
		Map<String, Object> autoReloadConditions = autoUploadConfigurationDao.getAutoUploadSearchConditionList(searchSourceDir ,searchTableName , searchScheduler );
		return autoUploadConfigurationDao.getQueryCount(AutoUploadJobDetail.class, (List<Criterion>) autoReloadConditions.get("conditions"),
				(HashMap<String, String>) autoReloadConditions.get("aliases"));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<AutoUploadJobDetail> getAutoUploadConfigPaginatedList(int startIndex, int limit, String sidx,String sord,String searchSourceDir,String searchTableName,
			String searchScheduler) {
		Map<String,Object> autoReloadConditionList ;
		autoReloadConditionList = autoUploadConfigurationDao.getAutoUploadSearchConditionList(searchSourceDir ,searchTableName , searchScheduler );
		
		List<AutoUploadJobDetail>  autoUploadJobDetail =  autoUploadConfigurationDao.getPaginatedList(AutoUploadJobDetail.class, (List<Criterion>) autoReloadConditionList.get("conditions"),
				(HashMap<String, String>) autoReloadConditionList.get("aliases"), startIndex, limit, sidx, sord);		
		for(AutoUploadJobDetail  obj: autoUploadJobDetail){
			Hibernate.initialize(obj.getRuleLookupTableData());
			Hibernate.initialize(obj.getScheduler());
			Hibernate.initialize(obj.getScheduler().getTrigger());
		}
		 return autoUploadJobDetail;
		
	}
	
	@Override
	@Transactional
	public ResponseObject createAutoUpload(AutoUploadJobDetail autoUpload, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		autoUpload.setCreatedByStaffId(staffId);
		autoUpload.setLastUpdatedByStaffId(staffId);
		autoUpload.setCreatedDate(new Date());
		autoUpload.setLastUpdatedDate(new Date());
		 
			CrestelSMJob job = autoUpload.getScheduler();
			job.setJobName("AUTOUPLOAD_"+autoUpload.getRuleLookupTableData().getId()+"_job_"+UUID.randomUUID());
			job.setDescription("AUTOUPLOAD_"+autoUpload.getRuleLookupTableData().getId()+"_job");
			job.setParentTrigger(BaseConstants.SCHEDULER_PARENT_TRIGGER_PREFIX+job.getTrigger().getID());
			job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX);
			job.setJobType(JobTypeEnum.AutoUpload.name());
			jobDao.save(job);
			job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX+job.getID());
			jobDao.merge(job);
			autoUpload.setScheduler(job);
			autoUploadConfigurationDao.save(autoUpload);
		if(autoUpload.getId()>0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_ADD_SUCCESS);
			responseObject.setObject(autoUpload);			
		}else{
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_ADD_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject updateAutoUpload(AutoUploadJobDetail autoReloadCache, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		autoReloadCache.setLastUpdatedByStaffId(staffId);
		
		autoReloadCache.setLastUpdatedDate(new Date());
			 
			CrestelSMJob job = autoReloadCache.getScheduler();
			job.setLastUpdatedByStaffId(staffId);
			job.setLastUpdatedDate(new Date());
			
			job.setJobName("AUTOUPLOAD_"+autoReloadCache.getRuleLookupTableData().getId()+"_job_"+UUID.randomUUID());
			job.setDescription("AUTOUPLOAD_"+autoReloadCache.getRuleLookupTableData().getId()+"_job");
			job.setParentTrigger(BaseConstants.SCHEDULER_PARENT_TRIGGER_PREFIX+job.getTrigger().getID());
			
			job.setJobType(JobTypeEnum.AutoUpload.name());
			/** jobService.update(job); **/
			job.setOriginalTrigger(BaseConstants.SCHEDULER_ORIGINAL_TRIGGER_PREFIX+job.getID());
			/** jobService.update(job); **/
			autoReloadCache.setScheduler(job);
			jobDao.merge(job);
			
			autoUploadConfigurationDao.merge(autoReloadCache);
		if(autoReloadCache.getId()>0){
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_UPDATE_SUCESS);
			responseObject.setObject(autoReloadCache);			
		}else{
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_UPDATE_FAILURE);
			responseObject.setSuccess(false);
			responseObject.setObject(null);
		}
		return responseObject;
	}
	
	@Override
	@Transactional
	public ResponseObject deleteMultipleAutoUploadJob(String ids, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(ids)){
			String [] idList = ids.split(",");
			
			for(int i = 0; i < idList.length; i ++ ){
				responseObject = deleteAutoUpload(Integer.parseInt(idList[i]), staffId);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_DELETE_SUCESS);
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_DELETE_FAILURE);
		}
		
		return responseObject;
	}
	
	private ResponseObject deleteAutoUpload(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(id > 0){
			AutoUploadJobDetail jobDetail = autoUploadConfigurationDao.getJobDetailById(id);
			if(jobDetail != null){
				CrestelSMJob job = jobDetail.getScheduler();
				if(job!=null){
					joblistener.deleteQuartzJob(job);
					job.setLastUpdatedByStaffId(staffId);
					job.setLastUpdatedDate(new Date());
					job.setStatus(StateEnum.DELETED);
					jobDao.merge(job);
				}
				jobDetail.setLastUpdatedByStaffId(staffId);
				jobDetail.setLastUpdatedDate(new Date());
				jobDetail.setStatus(StateEnum.DELETED);
				autoUploadConfigurationDao.merge(jobDetail);
				
				responseObject.setSuccess(true);
				responseObject.setObject(jobDetail);
				responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_DELETE_SUCESS);
			}else{
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_DELETE_FAILURE);
			}
		}else{
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.AUTO_UPLOAD_DELETE_FAILURE);
		}
		
		return responseObject;
	}

	@Override
	public AutoUploadJobDetail getAutoUploadByJobId(Integer id) {
		return autoUploadConfigurationDao.getAutoUploadJobDetailByJobId(id);
	}

	@Override
	@Transactional
	public CrestelSMJob getJobByAutoUploadJob(int id) {
		AutoUploadJobDetail autoUploadJob = autoUploadConfigurationDao.getJobDetailById(id);
		return autoUploadJob.getScheduler();
	}

}
