package com.elitecore.sm.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.elitecore.sm.rulelookup.dao.IAutoUploadConfigurationDao;
import com.elitecore.sm.rulelookup.model.AutoJobStatistic;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.rulelookup.service.IAutoUploadConfigService;
import com.elitecore.sm.rulelookup.service.IRuleDataLookUpService;


@Component
public class AutoUploadJob extends QuartzJobBean {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	IRuleDataLookUpService ruleDataLookUpService;
	
	@Autowired
	IAutoUploadConfigService autoUploadConfigService;
	
	@Autowired
	IAutoUploadConfigurationDao autoUploadConfigurationDao;
	
	@Autowired
	QuartJobSchedulingListener joblistener;
	
	@Autowired
	JobService jobService;
	
	@Override
	public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		CrestelSMJob job = null;
		try{
			JobDetail jobDetail = jobExecutionContext.getJobDetail();
			int jobId = joblistener.getCrestelJobIdByQuartzJobName(jobDetail.getKey().getName());
			logger.info("Job : "+jobId +" started successfully.");
			AutoUploadJobDetail autoUploadJobDetail =autoUploadConfigurationDao.getAutoUploadJobDetailByJobId(jobId);
			if (autoUploadJobDetail != null) {
				logger.info("AutoUpload Job : "+autoUploadJobDetail.getId()+" started successfully.");
				List<File> fileList = this.fileListFromRepositoryPath(autoUploadJobDetail);
				job = autoUploadJobDetail.getScheduler();
					if (fileList != null && !fileList.isEmpty()) {
						AutoJobStatistic autoJobStatistic = ruleDataLookUpService.saveAutoJobStatistic(autoUploadJobDetail);;
						try {
							
							int tableID = autoUploadJobDetail.getRuleLookupTableData().getId();
		
							String header = ruleDataLookUpService.getCsvHeader(tableID);
							RuleLookupTableData lookUpData = ruleDataLookUpService.getLookUpTableData(Integer.toString(tableID));
		
							String repositoryPath = autoUploadJobDetail.getSourceDirectory();
		
							String mode = autoUploadJobDetail.getAction();
							int staffId = 1;
		
							ResponseObject responseObject = null;
		
							for (File file : fileList) {
								responseObject = ruleDataLookUpService.insertDataIntoLookupView(header,
												lookUpData, file, repositoryPath,Integer.toString(tableID), mode,staffId, autoJobStatistic);
							}
							autoJobStatistic.setJobStatus(BaseConstants.COMPLETED);
						} catch (Exception e) {
							logger.debug("Error while maintaing auto upload job statistics " + e);
							autoJobStatistic.setReason("Auto Upload Job Failed");
						} finally {
							ruleDataLookUpService.updateAutoJobStatistic(autoJobStatistic);
						}
				}
				logger.info("AutoUpload Job : "+autoUploadJobDetail.getId()+" ended successfully.");
			}
			logger.info("Job : "+jobId +" ended successfully.");
		} catch (Exception e) {
			logger.debug("Error while executing JOB" + e);
		} finally {
			if (job != null) {
				job.setLastRunTime(new Date());
				job.setNextRunTime(jobExecutionContext.getNextFireTime());
				jobService.update(job);
			}
		}
	}
	
	private List<File> fileListFromRepositoryPath(AutoUploadJobDetail autoUploadJobDetail){
		List<File> fileList = new ArrayList<File>();
		try{
			String repositoryPath = autoUploadJobDetail.getSourceDirectory();
			String filePrefix = autoUploadJobDetail.getFilePrefix();
			String fileContains = autoUploadJobDetail.getFileContains();
			File dir = new File(repositoryPath);
			File[] fileArray = dir.listFiles();
			for(File file : fileArray){
				if(file.isFile() && filePrefix==null && fileContains==null){
					if(file.isFile() && file.getName().endsWith(".csv"))
					 fileList.add(file);
				}
				else if(file.isFile() && (file.getName().startsWith(filePrefix) || file.getName().contains(fileContains))){
					if(file.isFile() && file.getName().endsWith(".csv"))
					 fileList.add(file);
				}
			}
		} catch(Exception e){
			logger.error(e);
			logger.info("Error in fileListFromRepositoryPath method of AutoUploadJob");
		}
		return fileList;
	}
	
}
