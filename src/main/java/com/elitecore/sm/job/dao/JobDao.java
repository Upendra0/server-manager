package com.elitecore.sm.job.dao;



import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.job.model.CrestelSMJob;

public interface JobDao extends GenericDAO<CrestelSMJob> {
	
	public List<CrestelSMJob> getAllJobList();
	
	public List<CrestelSMJob> getJobListByTriggerId(int triggerId);
	
	public List<Object> getAutoJobListByJobId(Class<? extends BaseModel> kclass, int id);
	
}