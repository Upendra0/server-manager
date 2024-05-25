package com.elitecore.sm.mis.service;

import java.util.List;

import com.elitecore.sm.mis.model.DynamicReportData;

/**
 * 
 * @author chetan.kaila
 *
 */

public interface IDynamicReportService{
	
	public long getAllTableListCount(String reportName, String reportDesc);
	
	public List<DynamicReportData> getPaginatedList(int startIndex, int limit, String sidx,String sord,String reportName, String reportDesc);
	
	public DynamicReportData getDynamicReportDataByReportName(String reportName);
	
}
