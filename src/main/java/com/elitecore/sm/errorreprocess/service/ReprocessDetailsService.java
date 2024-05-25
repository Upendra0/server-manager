package com.elitecore.sm.errorreprocess.service;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.ErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface ReprocessDetailsService {

	public long getTotalReprocessBatchCount(SearchErrorReprocessDetails reprocessDetails);

	public  List<Map<String, Object>> getPaginatedList(SearchErrorReprocessDetails reprocessDetails, int startIndex, int limit, String sidx, String sord);
	
	public ResponseObject getServiceByType(String servicAlias);
	
	public ResponseObject getAllBatchDetailsById(int batchId);
	
	public ResponseObject getAllBatchDetailsByIds(Integer[] ids);
	
	public ResponseObject revertMultipleModifiyFiles(Integer[] detailIds);
	
	public void updateReprocessDetails(ErrorReprocessDetails reprocessDetails);
}
