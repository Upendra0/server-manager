package com.elitecore.sm.trigger.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;


public interface TriggerService {
	
	public long getTriggerListCount(boolean isSearch, String searchName, String searchType);
	
	public List<CrestelSMTrigger> getPaginatedList(int startIndex, int limit, String sidx,
			String sord, String searchName, String searchType, boolean isSearch);
	
	public List<CrestelSMTrigger> getAllTriggerList();
	
	public ResponseObject createTrigger(CrestelSMTrigger trigger, int staffId);
	
	public ResponseObject updateTrigger(CrestelSMTrigger trigger, int staffId);
	
	public ResponseObject deleteTriggers(String ids, int staffId);
	
	public ResponseObject getTriggerById(int id);
	
	public boolean isAssociatedWithJob(int triggerId);
	
	public ResponseObject getMappingAssociationDetails(int triggerId);
	
}