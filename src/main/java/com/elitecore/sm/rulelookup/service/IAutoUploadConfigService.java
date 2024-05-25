package com.elitecore.sm.rulelookup.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;

public interface IAutoUploadConfigService {
	
	/** public long getAutoUploadConfigCount(); 
	public List<AutoUploadJobDetail> getAutoUploadConfigPaginatedList(int startIndex, int limit, String sidx,String sord); **/
	
	ResponseObject createAutoUpload(AutoUploadJobDetail autoReloadCache,
			int staffId);
	
	public ResponseObject updateAutoUpload(AutoUploadJobDetail autoReloadCache, int staffId);
	
	public ResponseObject deleteMultipleAutoUploadJob(String ids, int staffId);
	long getAutoUploadConfigCount(String searchSourceDir,
			String searchTableName, String searchScheduler);
	List<AutoUploadJobDetail> getAutoUploadConfigPaginatedList(int startIndex,
			int limit, String sidx, String sord, String searchSourceDir,
			String searchTableName, String searchScheduler);

	public AutoUploadJobDetail getAutoUploadByJobId(Integer id);
	
	public CrestelSMJob getJobByAutoUploadJob(int id);
	
}
