package com.elitecore.sm.rulelookup.dao;



import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;

public interface IAutoUploadConfigurationDao extends GenericDAO<AutoUploadJobDetail>{

	public List<AutoUploadJobDetail> getListOfAutoUploadConfig();
	
	public Map<String,Object> getAutoUploadConditionList();
	
	public AutoUploadJobDetail getJobDetailById(int id);

	Map<String, Object> getAutoUploadSearchConditionList(
			String searchSourceDir, String searchTableName,
			String searchScheduler);	
	
	public AutoUploadJobDetail getAutoUploadJobDetailByJobId(int id);
}
