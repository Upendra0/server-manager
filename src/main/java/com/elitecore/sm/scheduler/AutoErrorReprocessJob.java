package com.elitecore.sm.scheduler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.elitecore.core.util.Constant;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessingBatch;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.service.AutoErrorReprocessService;
import com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchService;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.ErrorReprocessingActionEnum;
import com.elitecore.sm.services.service.ServicesService;

@Component
public class AutoErrorReprocessJob extends QuartzJobBean {

	private static Logger logger = Logger.getLogger(AutoErrorReprocessJob.class);

	private static final String MODULE = "AUTO_ERROR_REPROCESS_JOB";

	@Autowired
	private QuartJobSchedulingListener listner;

	@Autowired
	private AutoErrorReprocessService errorReprocessService;

	@Autowired
	private ErrorReprocessBatchService errorReprocessBatchService;

	@Autowired
	private ServerInstanceService serverInstanceService;

	@Autowired
	private ServicesService servicesService;

	@Autowired
	private IPolicyService policyService;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private PathListService pathListService;

	private SimpleDateFormat sdf = new SimpleDateFormat(Constant.SHORT_DATE_FORMAT);

	@Override
	protected void executeInternal(JobExecutionContext ctx)
			throws JobExecutionException {
		logger.info("Executing " + MODULE);
		String jobName = ctx.getJobDetail().getKey().getName();
		CrestelSMJob job = null;
		SearchErrorReprocessDetails searchErrorReprocessDetails = null;
		Date currentDate = new Date();
		try {
			int jobId = listner.getCrestelJobIdByQuartzJobName(jobName);		
			AutoErrorReprocessDetail jobDetail = errorReprocessService.getErrorReprocessJobById(jobId);						
			if(jobDetail!=null){
				job = jobDetail.getJob();
				searchErrorReprocessDetails = new SearchErrorReprocessDetails();
				searchErrorReprocessDetails.setFromDate((job==null || job.getLastRunTime()==null)?sdf.format(currentDate):sdf.format(job.getLastRunTime()));
				prepareSearchCriteria(jobDetail, searchErrorReprocessDetails);	
				//Not required to search on SM side with given search criteria, since it was for view only details
				/*ResponseObject smResponseObject = errorReprocessBatchService.getProcessingServiceDetailsByRule(searchErrorReprocessDetails);*/
				ResponseObject engineResponseObject = errorReprocessBatchService.getProcessingServiceErrorDetails(searchErrorReprocessDetails, new Integer[] {jobDetail.getService().getId()});
				if(engineResponseObject!=null && engineResponseObject.isSuccess()){
					engineResponseObject = doAutoErroFileReporcess(prepareErrorReprocessingFileDetails(engineResponseObject, jobDetail));
					if(engineResponseObject!=null && engineResponseObject.isSuccess() && job!=null)
						job.setLastRunTime(currentDate);						
				}else{
					logger.info(jobName + " has not ran successfully");
				}
			}else{
				logger.error("Job was deleted but not removed from scheduler : " + jobName);				
			}
		} catch (Exception e) {
			logger.error("Error while running job : " + jobName, e);			
		} finally {
			if(job!=null){
				job.setNextRunTime(ctx.getNextFireTime());
				jobService.update(job);
			}
		}
	}

	private synchronized void prepareSearchCriteria(AutoErrorReprocessDetail jobDetail, SearchErrorReprocessDetails searchErrorReprocessDetails) {
		
		if(searchErrorReprocessDetails!=null){
			searchErrorReprocessDetails.setToDate(sdf.format(new Date()));
			searchErrorReprocessDetails.setCategory(jobDetail.getCategory());
			int serviceInstanceId = jobDetail.getService().getId();
			searchErrorReprocessDetails.setServiceInstanceIds(String.valueOf(serviceInstanceId));
			int ruleId = Integer.parseInt(jobDetail.getRule());
			searchErrorReprocessDetails.setRuleId(ruleId);
			searchErrorReprocessDetails.setRuleAlias(policyService.getPolicyRuleById(ruleId).getAlias());
			searchErrorReprocessDetails.setServiceType(jobDetail.getService().getSvctype().getAlias());
		}
	}
	
	private synchronized ErrorReprocessingBatch prepareErrorReprocessingFileDetails(ResponseObject engineResponseObject, AutoErrorReprocessDetail jobDetail){
		
		ErrorReprocessingBatch errorReprocessingBatch = new ErrorReprocessingBatch();
		List<ErrorReprocessDetails> reprocessDetailList = new ArrayList<>(0);
		ServerInstance serverInstance = serverInstanceService.getServerInstance(jobDetail.getServerInstanceId());
		JSONArray jsonArr = (JSONArray) engineResponseObject.getObject();
		for(int iArr=0; iArr<jsonArr.length();iArr++){
			JSONObject jsonBase = (JSONObject) jsonArr.get(iArr);
			Iterator<?> keys = jsonBase.keys();
			String serviceId=null;
			String pathId=null;
			String filePath = null;
			PathList pathList=null;
			while (keys.hasNext()) {
			    String key = (String)keys.next();			    
			    if(key!=null && key.contains("errorPath_")){
			    	String[] temp = key.split("_");
			    	if(temp!=null && temp.length==3){
			    		serviceId=temp[1];
			    		pathId=temp[2];
			    	}
			    	pathList = pathListService.getPathListByServiceAndPathId(Integer.parseInt(serviceId), pathId);
			    	filePath = jsonBase.getString(key);
			    }		    	
		    	if (key!=null && key.contains("fileDetails_")){
		    		JSONArray jArr = (JSONArray) jsonBase.get(key);		    	
		    		if (jArr!=null){
		    			for (int idx=0; idx<jArr.length();idx++){
		    				JSONObject fileDetailJSON = jArr.getJSONObject(idx);
		    				if (fileDetailJSON!=null){
		    					ErrorReprocessDetails errorReprocessDetails = new ErrorReprocessDetails();
		    					errorReprocessDetails.setInputSourceCompress(false);
		    					errorReprocessDetails.setCompress(false);
		    					errorReprocessDetails.setServerInstance(serverInstance);
		    					errorReprocessDetails.setService(jobDetail.getService());
		    					errorReprocessDetails.setSvctype(jobDetail.getService().getSvctype());
		    					errorReprocessDetails.setComposer(null);
		    					errorReprocessDetails.setAbsoluteFilePath(fileDetailJSON.getString("absoluteFileName"));
		    					errorReprocessDetails.setFileName(fileDetailJSON.getString("fileName"));
		    					errorReprocessDetails.setFileSize(new BigDecimal(fileDetailJSON.getInt("fileSize")));
		    					errorReprocessDetails.setReadFilePath((pathList!=null)?pathList.getReadFilePath():"");
		    					errorReprocessDetails.setFilePath(filePath);
		    					errorReprocessDetails.setFileReprocessType("AUTO");
		    					reprocessDetailList.add(errorReprocessDetails);
		    				}
		    			}
		    		}
			    }
			}
		}
		errorReprocessingBatch.setErrorProcessAction(ErrorReprocessingActionEnum.DIRECT_REPROCESS);
		errorReprocessingBatch.setUserComment("Automatic Error File Reporcessing");
		errorReprocessingBatch.setErrorCategory(jobDetail.getCategory());
		errorReprocessingBatch.setReprocessDetailList(reprocessDetailList);
		return errorReprocessingBatch;
	}

	private synchronized ResponseObject doAutoErroFileReporcess(ErrorReprocessingBatch errorReprocessingBatch) {
		if(errorReprocessingBatch!=null && errorReprocessingBatch.getReprocessDetailList()!=null && errorReprocessingBatch.getReprocessDetailList().size()>0){
			return errorReprocessBatchService.autoReprocessProcessingFiles(errorReprocessingBatch);	
		}else{
			logger.debug("Job ran successfully, but no files are found for reprocessing");
			ResponseObject responseObject = new ResponseObject();
			responseObject.setSuccess(true);
			return responseObject;
		}
	}

}