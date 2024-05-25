package com.elitecore.sm.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.rulelookup.dao.IAutoReloadCacheConfigurationDao;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;

@Component
public class AutoReloadCacheJob extends QuartzJobBean {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IAutoReloadCacheConfigurationDao autoReloadCacheDao;
	
	@Autowired
	IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	QuartJobSchedulingListener joblistener;
	
	@Autowired
	JobService jobService;
	
	@Override
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		CrestelSMJob job = null;
		try {
			logger.info("AutoReloadCacheJob job execution started");
			JobDetail jobDetail = jobExecutionContext.getJobDetail();
			if (jobDetail != null) {
				int jobId = joblistener.getCrestelJobIdByQuartzJobName(jobDetail.getKey().getName());
				logger.info("Job : "+jobId +" started successfully.");
				AutoReloadJobDetail autoReloadcache = autoReloadCacheDao.getAutoReloadJobDetailByJobId(jobId);
				if (autoReloadcache != null) {
					logger.info("AutoUpload Job : "+autoReloadcache.getId()+" started successfully.");
					job = autoReloadcache.getScheduler();
					AutoJobStatistic autoJobStatistic = ruleDataLookUpService.saveAutoJobStatistic(autoReloadcache);
					try {
						ResponseObject responseObject = ruleDataLookUpService.doLookupTableDataReload(autoReloadcache,
										autoJobStatistic);
						if (responseObject.isSuccess()) {
							autoJobStatistic.setJobStatus(BaseConstants.COMPLETED);
							job.setLastRunTime(new Date());
						}
					} catch(Exception e){
						autoJobStatistic.setReloadRecordCount("0");
						logger.debug("Error while maintaing auto reload cache statistics " + e);
					}finally {
						ruleDataLookUpService.updateAutoJobStatistic(autoJobStatistic);
					}
					logger.info("AutoUpload Job : "+autoReloadcache.getId()+" ended successfully.");
				}
				logger.info("Job : "+jobId +" ended successfully.");
			}
			logger.info("AutoReloadCacheJob job execution ended");
		} catch (Exception e) {
			logger.debug("Error while executing JOB" + e);
		} finally {
			if (job != null) {
				job.setNextRunTime(jobExecutionContext.getNextFireTime());
				jobService.update(job);
			}
		}
	}
}
