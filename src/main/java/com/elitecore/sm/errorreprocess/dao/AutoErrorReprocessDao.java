package com.elitecore.sm.errorreprocess.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;

public interface AutoErrorReprocessDao extends GenericDAO<AutoErrorReprocessDetail> {
	
	public Map<String, Object> getProcessingServiceDetailsByRule();
	
	public Map<String, Object> getProcessingServiceDetailsBySearchRule(String serviceInstanceIds, String category,
			String severity, String reasonCategory, String rule, String errorCode);

	List<AutoErrorReprocessDetail> getAllList();
	
	public AutoErrorReprocessDetail getErrorReprocessJobById(int jobId);
		
}
