/**
 * 
 */
package com.elitecore.sm.trigger.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.job.dao.JobDao;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.dao.IAutoReloadCacheConfigurationDao;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.scheduler.CronExpressionGenerator;
import com.elitecore.sm.trigger.dao.TriggerDao;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.util.EliteUtils;


@Service(value="triggerService")
public class TriggerServiceImpl implements TriggerService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private TriggerDao triggerDao;
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private IAutoReloadCacheConfigurationDao autoReloadCacheJobDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTriggerListCount(boolean isSearch, String searchName, String searchType) {
		Map<String, Object> triggerTableDataConditions = triggerDao.getTriggerCount(searchName,searchType);
		return triggerDao.getQueryCount(CrestelSMTrigger.class, (List<Criterion>) triggerTableDataConditions.get("conditions"),null);

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<CrestelSMTrigger> getPaginatedList(int startIndex, int limit,
			String sidx, String sord, String searchName, String searchType, boolean isSearch) {
		Map<String,Object> tableConditionList = triggerDao.getTriggerCount(searchName,searchType);
		return triggerDao.getPaginatedList(CrestelSMTrigger.class, (List<Criterion>) tableConditionList.get("conditions"),
				null, startIndex, limit, sidx, sord);

	}
	
	@Transactional(readOnly = true)
	@Override
	public List<CrestelSMTrigger> getAllTriggerList() {
		if (logger.isDebugEnabled()) {
			logger.debug("Going to fetch all Triggers list.");
		}
		return triggerDao.getAllTriggerList();
	}

	@Override
	@Transactional
	public ResponseObject createTrigger(CrestelSMTrigger trigger, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		trigger.setCreatedByStaffId(staffId);
		trigger.setLastUpdatedByStaffId(staffId);
		trigger.setCreatedDate(new Date());
		trigger.setLastUpdatedDate(new Date());
		String cronExpression = CronExpressionGenerator.generateCronExpression(trigger);
		if(cronExpression != null && CronExpressionGenerator.isCronExpressionValid(cronExpression)) {
			trigger.setCroneExpression(cronExpression);
		}else {
			handleFailedCronExpression(trigger);
		}
		
		int count = triggerDao.getCountByName(trigger.getTriggerName());
		if(count>0){
			logger.info("Duplicate Trigger Name Found");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_TRIGGER_FOUND);
		}else{
			triggerDao.save(trigger);
			if(trigger.getID()>0){
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.TRIGGER_ADD_SUCCESS);
				responseObject.setObject(trigger);
			}else{
				responseObject.setResponseCode(ResponseCode.TRIGGER_ADD_FAIL);
				responseObject.setSuccess(false);
				responseObject.setObject(null);
			}
		}
		
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject deleteTriggers(String ids, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(!StringUtils.isEmpty(ids)){
			String [] idList = ids.split(",");
			
			for(int i = 0; i < idList.length; i ++ ){
				responseObject = deleteTrigger(Integer.parseInt(idList[i]), staffId);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.TRIGGER_DELETE_SUCESS);
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.TRIGGER_DELETE_FAILURE);
		}
		
		return responseObject;
	}
	
	private ResponseObject deleteTrigger(int id, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		
		if(id > 0){
			CrestelSMTrigger trigger = triggerDao.findByPrimaryKey(CrestelSMTrigger.class, id);
			if(trigger != null){
				trigger.setLastUpdatedByStaffId(staffId);
				trigger.setLastUpdatedDate(new Date());
				trigger.setStatus(StateEnum.DELETED);
				trigger.setTriggerName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE, trigger.getTriggerName()));
				triggerDao.merge(trigger);
				responseObject.setSuccess(true);
				responseObject.setObject(trigger);
				responseObject.setResponseCode(ResponseCode.TRIGGER_DELETE_SUCESS);
			}else{
				responseObject.setSuccess(true);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.TRIGGER_DELETE_FAILURE);
			}
		}else{
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.TRIGGER_DELETE_FAILURE);
		}
		
		return responseObject;
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseObject getTriggerById(int id) {
		ResponseObject responseObject = new ResponseObject();
		
		logger.debug("Going to fetch trigger details for Id " + id );
		CrestelSMTrigger trigger = triggerDao.findByPrimaryKey(CrestelSMTrigger.class, id);
		
		if(trigger != null){
			logger.info("Trigger details found successfully for Id " + id);
			responseObject.setObject(trigger);  // set trigger object in response object.
			responseObject.setSuccess(true);
			
		}else{
			logger.info("Failed to fetch trigger details for Id " + id);
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_TRIGGER_BY_ID);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject updateTrigger(CrestelSMTrigger trigger, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		trigger.setLastUpdatedByStaffId(staffId);
		trigger.setLastUpdatedDate(new Date());
		String cronExpression = CronExpressionGenerator.generateCronExpression(trigger);
		if(cronExpression != null && CronExpressionGenerator.isCronExpressionValid(cronExpression)) {
			trigger.setCroneExpression(cronExpression);
		}else {
			handleFailedCronExpression(trigger);
		}
		int count = triggerDao.getCountByID(trigger.getID());
		CrestelSMTrigger dbTrigger = triggerDao.getTriggerByName(trigger.getTriggerName());
		if(count>0){
			if(dbTrigger==null||trigger.getID()==dbTrigger.getID()){
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.TRIGGER_UPDATE_SUCCESS);
				responseObject.setObject(trigger);
				triggerDao.merge(trigger);
			}else{
				logger.info("Trigger not found");
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.TRIGGER_UPDATE_FAILURE);
			}
			
		}else{
			logger.info("Trigger not found");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.TRIGGER_UPDATE_FAILURE);
		}
		
		return responseObject;
	}
	
	public void handleFailedCronExpression(CrestelSMTrigger trigger) {
		logger.info("Cron Expression is invalid hence setting the default schedule for executing every hour");
		trigger.setCroneExpression("0 0 0/1 * * ?");
	}

	@Override
	@Transactional
	public boolean isAssociatedWithJob(int triggerId) {
		List<CrestelSMJob> jobList = jobDao.getJobListByTriggerId(triggerId);
		if(jobList!=null && !jobList.isEmpty()){
			for(CrestelSMJob job : jobList){
				if(JobTypeEnum.AutoUpload.name().equalsIgnoreCase(job.getJobType())){
					List<Object> objectList = jobDao.getAutoJobListByJobId(AutoUploadJobDetail.class,job.getID());
					if(objectList!=null && !objectList.isEmpty()){
						return true;
					}
				} else if(JobTypeEnum.AutoReloadCache.name().equalsIgnoreCase(job.getJobType())){
					List<Object> objectList = jobDao.getAutoJobListByJobId(AutoReloadJobDetail.class,job.getID());
					if(objectList!=null && !objectList.isEmpty()){
						return true;
					}
				} else if(JobTypeEnum.AutoErrorReprocess.name().equalsIgnoreCase(job.getJobType())){
					List<Object> objectList = jobDao.getAutoJobListByJobId(AutoErrorReprocessDetail.class,job.getID());
					if(objectList!=null && !objectList.isEmpty()){
						return true;
					}
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	@Override
	public ResponseObject getMappingAssociationDetails(int triggerId) {
		logger.debug("Going to fetch all association for schedulerId : " + triggerId);
		ResponseObject responseObject = new ResponseObject();
		
		List<CrestelSMJob> jobList = jobDao.getJobListByTriggerId(triggerId);

		if (jobList != null && !jobList.isEmpty()) {
			responseObject.setObject(iterateMappingAssocitionDetailsForScheduler(jobList));
			responseObject.setSuccess(true);
		} else {
			logger.info("Failed t o fetch mapping details for schedulerId " + triggerId);
			responseObject.setObject(jobList);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}
	
	private JSONArray iterateMappingAssocitionDetailsForScheduler(List<CrestelSMJob> jobList) {

		JSONArray jsonArray = new JSONArray();

		for (CrestelSMJob job : jobList) {
			logger.debug("iterateMappingAssocitionDetailsForScheduler");
			JSONObject jsonObject = null;
			if(JobTypeEnum.AutoReloadCache.name().equalsIgnoreCase(job.getJobType())){
				List<Object> autoReloadcacheJoblist = jobDao.getAutoJobListByJobId(AutoReloadJobDetail.class,job.getID());
				for(Object object : autoReloadcacheJoblist){
					AutoReloadJobDetail autoReloadcacheJob = (AutoReloadJobDetail) object;
					jsonObject = new JSONObject();
					jsonObject.put("jobId",autoReloadcacheJob.getId());
					jsonObject.put("jobType", job.getJobType());
					jsonArray.put(jsonObject);
				}
			} else if(JobTypeEnum.AutoUpload.name().equalsIgnoreCase(job.getJobType())){
				List<Object> autoUploadJoblist = jobDao.getAutoJobListByJobId(AutoUploadJobDetail.class,job.getID());
				for(Object object : autoUploadJoblist){
					AutoUploadJobDetail autoUploadJob = (AutoUploadJobDetail) object;
					jsonObject = new JSONObject();
					jsonObject.put("jobId",autoUploadJob.getId());
					jsonObject.put("jobType", job.getJobType());
					jsonArray.put(jsonObject);
				}
			} else if(JobTypeEnum.AutoErrorReprocess.name().equalsIgnoreCase(job.getJobType())){
				List<Object> autoErrorReprocesslist = jobDao.getAutoJobListByJobId(AutoErrorReprocessDetail.class,job.getID());
				for(Object object : autoErrorReprocesslist){
					AutoErrorReprocessDetail autoErrorReprocess = (AutoErrorReprocessDetail) object;
					jsonObject = new JSONObject();
					jsonObject.put("jobId",autoErrorReprocess.getId());
					jsonObject.put("jobType", job.getJobType());
					jsonArray.put(jsonObject);
				}
			}
		}

		return jsonArray;
	}

}