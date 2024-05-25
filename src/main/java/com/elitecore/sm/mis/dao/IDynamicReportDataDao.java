package com.elitecore.sm.mis.dao;

import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.mis.model.DynamicReportData;

/**
 * 
 * @author chetan.kaila
 *
 */

public interface IDynamicReportDataDao extends GenericDAO<DynamicReportData>{
	
	public Map<String, Object> getRuleForSearchTableConditionList(String searchName, String searchDesc);
	
	public DynamicReportData getDynamicReportDataByReportName(String reportName);
	
}
