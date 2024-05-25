package com.elitecore.sm.errorreprocess.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.AutoErrorReprocessDetail;
import com.elitecore.sm.job.model.CrestelSMJob;

public interface AutoErrorReprocessService {
	
	public ResponseObject getServiceByServerId(int serverId);
	long getAutoErrorReprocessListCount(boolean isSearch, String serviceInstanceIds, String category, String severity,
			String reasonCategory, String rule, String errorCode);
	List<AutoErrorReprocessDetail> getPaginatedList(int startIndex, int limit, String sidx, String sord,
			String serviceInstanceIds, String category, String severity, String reasonCategory, String rule,
			String errorCode, boolean isSearch);
	ResponseObject deleteAutoReprocessFiles(String ids, int staffId);
	public AutoErrorReprocessDetail getErrorReprocessJobById(int jobId);	
	public ResponseObject addNewAutoReprocessConfig(AutoErrorReprocessDetail autoErrorReprocessDetail, int staffId);
	ResponseObject getAutoErrorReprocessById(int id);
	ResponseObject updateAutoErrorReprocess(AutoErrorReprocessDetail autoErrorReprocessDetail, int staffId);	
	public CrestelSMJob getJobByAutoReprocessJob(int id);
}
