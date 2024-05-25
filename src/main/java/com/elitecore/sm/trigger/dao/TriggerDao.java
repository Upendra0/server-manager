package com.elitecore.sm.trigger.dao;



import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;

public interface TriggerDao extends GenericDAO<CrestelSMTrigger> {
	
	public Map<String, Object> getTriggerCount(String searchName, String searchType);
	
	public List<CrestelSMTrigger> getAllTriggerList();
	
	public int getCountByName(String name);
	
	public CrestelSMTrigger getTriggerByName(String name);
	
	public int getCountByID(int id);
	
}