package com.elitecore.sm.job.service;

import java.util.List;

import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;


public interface JobService {
	
	public List<CrestelSMJob> getAllJobList();
	public void save(CrestelSMJob job);
	public void update(CrestelSMJob job);
	public void merge(CrestelSMJob job);
	public List<CrestelSMJob> getJobListByTriggerId(CrestelSMTrigger crestelSMTrigger);

}