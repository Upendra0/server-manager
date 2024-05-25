package com.elitecore.sm.scheduler;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
//import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.JobTypeEnum;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import static org.quartz.TriggerKey.*;
import static org.quartz.JobKey.*;


/**
 * @author elitecore
 *
 */
@SuppressWarnings("rawtypes")
@PropertySource("classpath:quartz.properties")
public class QuartJobSchedulingListener implements ApplicationListener
{    
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
    @Autowired
    private Scheduler scheduler;

    @Autowired(required=true)
	@Qualifier(value="jobService")
	private JobService jobService;
 
    @Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			try{
	            ApplicationContext applicationContext = ((ApplicationContextEvent) event).getApplicationContext();
	            List<CronTriggerFactoryBean> cronTriggerBeans = this.loadAllCronTriggerBeans(applicationContext);
	            this.startScheduler();
	            this.scheduleJobs(cronTriggerBeans);
	        }
	        catch (Exception e) {
	        	logger.info("could not complete contextRefreshEvents...");
	        	logger.error(e);
	        }
		} else if(event instanceof ContextClosedEvent) {
			logger.info("Context is about to distroy. Shutting down Quartz scheduler...");
			 try {
		            interruptJobs();
		            // Tell the scheduler to shutdown allowing jobs to complete
		            scheduler.shutdown(true);
		            logger.info("allowing jobs to complete...");
		        } catch (SchedulerException e) {//NOSONAR
		            try {
		                // Something has gone wrong so tell the scheduler to shutdown without allowing jobs to complete.
		                scheduler.shutdown(true);
		            } catch (SchedulerException ex) {
		                logger.error("Unable to shutdown the Quartz scheduler.", ex);
		            }
		        }
		}
		
	}

    private List<CronTriggerFactoryBean> loadAllCronTriggerBeans(ApplicationContext applicationContext) {
    	List<CrestelSMJob> jobsList = jobService.getAllJobList();
		List<CronTriggerFactoryBean> cronTriggerBeans = new ArrayList<CronTriggerFactoryBean>();
		if(jobsList != null && jobsList.size() > 0) {
			for (CrestelSMJob crestelSMJob : jobsList) {
				if(crestelSMJob != null) {
					CronTriggerFactoryBean cronTriggerBean = null;
						cronTriggerBean = buildCronTriggerBean(crestelSMJob);
					if(cronTriggerBean != null)
					{
						cronTriggerBeans.add(cronTriggerBean);
					}
				}
			}
		}
        return cronTriggerBeans;
    }

    public CronTriggerFactoryBean buildCronTriggerBean(CrestelSMJob crestelSMJob) {
    	CronTriggerFactoryBean cronTriggerBean = new CronTriggerFactoryBean();
    	CrestelSMTrigger crestelSMTrigger = crestelSMJob.getTrigger();
        	logger.info("building crontrigger for scheduling job with jobKey: " + BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
        	if(crestelSMJob != null) {
            	//set quartz job details
        		JobDetailFactoryBean jobDetail = new JobDetailFactoryBean();
            	jobDetail.setName(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
            	jobDetail.setGroup("DEFAULT");
            	// apply job class 
            	Class<? extends Job> jobClass = getJobImplementationFromJobType(String.valueOf(JobTypeEnum.valueOf(crestelSMJob.getJobType()).getValue()));
            	if(jobClass != null) {
            		jobDetail.setJobClass(jobClass);
            	}
            	// set cron trigger details
            	setQuartzTriggerCronExpression(crestelSMJob, cronTriggerBean);               
            	cronTriggerBean.setName(crestelSMJob.getOriginalTrigger());
            	setStartAndEndAtTriggerTimes(cronTriggerBean, crestelSMTrigger);

            	cronTriggerBean.setJobDetail(jobDetail.getObject()); 
            	cronTriggerBean.setGroup("DEFAULT");
        	}
            
       
        return cronTriggerBean;
    }

    public void startScheduler() {
    	try {
			logger.info("Starting Scheduler... ");
			scheduler.start();
			logger.info("Scheduler has been started and ready to schedule jobs... ");
		} catch (SchedulerException e) {
			logger.info("error while starting a scheduler");
			logger.error(e);
		}
    }
    
    protected void scheduleJobs(List<CronTriggerFactoryBean> cronTriggerBeans) {
    	String listOfTriggers = "";
		String listOfJobs = "";
		if(cronTriggerBeans != null && cronTriggerBeans.size()> 0) {
			for (CronTriggerFactoryBean cronTriggerBean : cronTriggerBeans) {
				if(cronTriggerBean != null && cronTriggerBean.getObject() != null) {
					listOfTriggers +=  cronTriggerBean.getObject().getKey().getName() + " with cron:" + cronTriggerBean.getObject().getCronExpression() + " , ";
					listOfJobs += cronTriggerBean.getObject().getKey().getName() +  ", ";
				}
				scheduleSingleJob(cronTriggerBean);
			}
			logger.info("all retrieved Scheduler Triggers in Cache from DB are: " + listOfTriggers);
			logger.info("all retrieved Scheduler Jobs in Cache from DB are: " + listOfJobs);
		}
    }
    
    /**
     * Method to get job class dynamically from jobtype
     *
     */
    @SuppressWarnings("unchecked")
	public Class<? extends Job> getJobImplementationFromJobType(String jobType) {
    	Class<? extends Job> returnClass = null;
		try {
			returnClass = (Class<? extends Job>)Class.forName(jobType);
		} catch (ClassNotFoundException e) {
			logger.info("could not find class value for Jobtype enum: " + jobType);
			logger.error(e);
		}
    	
    	return returnClass;
    }
    
    
    public void updaterQuartzTrigger(CrestelSMTrigger crestelSMTrigger,JobService jobService) {
    	// get list of jobs associated with that trigger
    	logger.info("calling updateTrigger");
    	List<CrestelSMJob> crestelSMJobList = jobService.getJobListByTriggerId(crestelSMTrigger) ;
    	
    	if(crestelSMJobList != null && crestelSMJobList.size() > 0) {
    		for (CrestelSMJob crestelSMJob : crestelSMJobList) {
    			try {
    				CronTriggerFactoryBean cronTriggerBean = new CronTriggerFactoryBean();
    				// set cron trigger details
    				setQuartzTriggerCronExpression(crestelSMJob, cronTriggerBean);
    				this.jobService.merge(crestelSMJob);
    				cronTriggerBean.setName(crestelSMJob.getOriginalTrigger());
    				setStartAndEndAtTriggerTimes(cronTriggerBean,crestelSMTrigger);
    				
    				//cronTriggerBean.setJobName(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
    				cronTriggerBean.setBeanName(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
                	//reschedule job
    				scheduler.rescheduleJob(triggerKey(crestelSMJob.getOriginalTrigger(), "DEFAULT"),cronTriggerBean.getObject());
    				logger.info("Trigger rescheduled with cron: " + crestelSMTrigger.getCroneExpression());
    			} catch (SchedulerException e) {
					logger.info("rescheduling job failed for Trigger: " + crestelSMJob.getOriginalTrigger());
					logger.error(e);
				}
    		}
    	}
    	
    	
    }
    
    public void createAndScheduleJob(CrestelSMJob crestelSMJob) {
    	CronTriggerFactoryBean cronTriggerBean = buildCronTriggerBean(crestelSMJob);
		scheduleSingleJob(cronTriggerBean);
    }
    
    public void scheduleSingleJob(CronTriggerFactoryBean cronTriggerBean) {
    	if(cronTriggerBean != null && cronTriggerBean.getObject() != null) {
			try {
				JobDetailFactoryBean jobDetailFactoryBean = (JobDetailFactoryBean) cronTriggerBean.getJobDataMap().get("jobDetail");
				scheduler.scheduleJob(jobDetailFactoryBean.getObject(), cronTriggerBean.getObject());
				logger.info("scheduled Job having JobKey: " + cronTriggerBean.getObject().getKey() + " with Trigger having TriggerKey : " + cronTriggerBean.getObject().getKey() + " with cron : " + cronTriggerBean.getObject().getCronExpression());
			} catch (SchedulerException e) {
				logger.info("error while scheduling job having Jobkey: " + cronTriggerBean.getObject().getKey() + " with Trigger having TriggerKey : " + cronTriggerBean.getObject().getKey());
				logger.error(e);
			}
		}
    }
    
    public void setQuartzTriggerCronExpression(CrestelSMJob crestelSMJob, CronTriggerFactoryBean cronTriggerBean) {
    	try {
			cronTriggerBean.setCronExpression(crestelSMJob.getTrigger().getCroneExpression());
			crestelSMJob.setNextRunTime(cronTriggerBean.getObject().getFireTimeAfter(new Date()));
		//} catch (ParseException e) {
		} catch (Exception e) {
			logger.info("error while applying cron expression to trigger for: " + crestelSMJob.getOriginalTrigger());
			logger.error(e);
		}
    }
    
    public void updateAndRescheduleJob(CrestelSMJob crestelSMJob) {
    	try {
			logger.info("uncheduling the previous job having jobkey: "+BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID()+" from the associated schedule: "+ crestelSMJob.getOriginalTrigger() +" and jobdetails ");
			scheduler.pauseJob(jobKey(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID(), "DEFAULT"));// scheduler.pauseTrigger(crestelSMJob.getOriginalTrigger(),"DEFAULT");
			
			scheduler.unscheduleJob(triggerKey(crestelSMJob.getOriginalTrigger(),"DEFAULT"));			
		} catch (SchedulerException e) {
			logger.info("error while unscheduling job having jobkey: "+BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID()+" from the associated schedule: "+ crestelSMJob.getOriginalTrigger() +" and jobdetails " );
			logger.error(e);
		}
		logger.info("Rescheduling the edited Job with its associated Schedule: " + crestelSMJob.getOriginalTrigger());
		createAndScheduleJob(crestelSMJob);
		logger.info("Job Rescheduled with schedule cron:" + crestelSMJob.getTrigger().getCroneExpression());
    }
    
    public void deleteQuartzJob(CrestelSMJob crestelSMJob) {
    	
    	logger.info("deleting Quartz Job:  " + BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
		try {
			scheduler.pauseJob(jobKey(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID(), "DEFAULT"));//scheduler.pauseTrigger(crestelSMJob.getOriginalTrigger(),"DEFAULT");
			scheduler.deleteJob(jobKey(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID(),"DEFAULT"));
		} catch (SchedulerException e) {
			logger.info("error while deleting quartz job: " +  BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
			logger.error(e);
		}
    	
    }
    
    public int getCrestelJobIdByQuartzJobName(String quartzJobName) {
    	int crestelJobId = 0 ; 
    	if(quartzJobName != null) {
    		try {
    			crestelJobId = Integer.parseInt(quartzJobName.split("J")[1]);
    		}catch (Exception e) {
    			logger.error(e);
				logger.info("error while retrieving crestelJobId(int)  from quartzJobName: " + quartzJobName);
			}
    	}
    	return crestelJobId;
    }
    
    /**
     * Method to derive date with given date, hour and minute
     *
     */
    public static Date getDateByGivenDateHourMinute(Date dateInput,Integer hourInput, Integer minuteInput) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(dateInput);
    	cal.set(Calendar.HOUR, hourInput != null ? hourInput : 00 );
    	cal.set(Calendar.MINUTE,minuteInput != null ? minuteInput : 00);
    	cal.set(Calendar.SECOND, 00);
    	cal.set(Calendar.MILLISECOND, 000);
    	return cal.getTime();
    }
    
    public void setStartAndEndAtTriggerTimes(CronTriggerFactoryBean cronTriggerBean, CrestelSMTrigger crestelSMTrigger) {
    	Date startDate = null, endDate = null;
    	if(crestelSMTrigger.getStartAtDate() != null) {
        	startDate = getDateByGivenDateHourMinute(crestelSMTrigger.getStartAtDate(), crestelSMTrigger.getStartAtHour(), crestelSMTrigger.getStartAtMinute());
    	}
    	
    	if(crestelSMTrigger.getEndAtDate() != null) {
	    	endDate = getDateByGivenDateHourMinute(crestelSMTrigger.getEndAtDate(), crestelSMTrigger.getEndAtHour(), crestelSMTrigger.getEndAtMinute());
    	}
    	if(startDate != null) {    		
    		
    		//cronTriggerBean.setStartTime(startDate);    		
    		CronTriggerImpl cti = (CronTriggerImpl) cronTriggerBean.getObject();
    		cti.setStartTime(startDate);
    		
    	}
    	if(endDate != null) {
    		
    		//cronTriggerBean.setEndTime(endDate);
    		CronTriggerImpl cti = (CronTriggerImpl) cronTriggerBean.getObject();
    		cti.setEndTime(endDate);    		
    	}
    }

    private void interruptJobs() throws SchedulerException {
    	List<CrestelSMJob> jobsList = jobService.getAllJobList();
		for (CrestelSMJob crestelSMJob : jobsList) {
			try {
				scheduler.interrupt(jobKey(BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID(), "DEFAULT"));
				logger.info("interrupted Job with jobKey: " + BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
			} catch (UnableToInterruptJobException e) {
				logger.info("error while interrupting job with jobkey:" + BaseConstants.SCHEDULER_JOB_PREFIX+crestelSMJob.getID());
				logger.error(e);
			}
		}
    }
    
}