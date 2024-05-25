/**
 * 
 */
package com.elitecore.sm.job.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.job.dao.JobDao;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;


@Service(value="jobService")
public class JobServiceImpl implements JobService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	JobDao jobDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<CrestelSMJob> getAllJobList() {
		if (logger.isDebugEnabled()) {
			logger.debug("Going to fetch all Job list.");
		}
		return jobDao.getAllJobList();
	}

	@Transactional
	@Override
	public void save(CrestelSMJob job) {
		jobDao.save(job);
	}

	@Transactional
	@Override
	public void update(CrestelSMJob job) {
		jobDao.update(job);
	}
	
	@Transactional
	@Override
	public void merge(CrestelSMJob job) {
		jobDao.merge(job);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<CrestelSMJob> getJobListByTriggerId(CrestelSMTrigger crestelSMTrigger){
		if (logger.isDebugEnabled()) {
			logger.debug("Going to fetch all Job list by given TriggerId.");
		}
		return jobDao.getJobListByTriggerId(crestelSMTrigger.getID());
	}
	
}