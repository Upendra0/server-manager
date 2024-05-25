package com.elitecore.sm.rulelookup.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;

public interface IAutoReloadCacheConfigurationDao extends GenericDAO<AutoReloadJobDetail>{

	public Map<String, Object> getAutoReloadConditionList();
	
	public Map<String, Object> getAutoReloadSearchConditionList(String searchName, String searchServerInstance,
			String searchDBQuery);
	
	public Integer isJobAssociated(int id);
	
	public AutoReloadJobDetail getAutoReloadJobDetailById(int id);
	
	public AutoReloadJobDetail getAutoReloadJobDetailByJobId(int id);
	
	public List<AutoReloadJobDetail> getListOfImmediateExecutionByViewName(int viewId);
	
}
