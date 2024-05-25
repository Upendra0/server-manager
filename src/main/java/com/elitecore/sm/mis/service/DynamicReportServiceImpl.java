package com.elitecore.sm.mis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.mis.dao.IDynamicReportDataDao;
import com.elitecore.sm.mis.model.DynamicReportData;

/**
 * 
 * @author chetan.kaila
 *
 */

@Service(value="dynamicReportService")
public class DynamicReportServiceImpl implements IDynamicReportService{

	@Autowired
	IDynamicReportDataDao dynamicReportDataDao;
	
	/**
	 * 
	 * return total number of records of dynamic report based on condition provided
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getAllTableListCount(String reportName, String reportDesc){
		Map<String, Object> reportTableDataConditions = dynamicReportDataDao.getRuleForSearchTableConditionList(reportName,reportName);
		return dynamicReportDataDao.getQueryCount(DynamicReportData.class, (List<Criterion>) reportTableDataConditions.get("conditions"),
				(HashMap<String, String>) reportTableDataConditions.get("aliases"));
	}
	
	/**
	 * 
	 * return actual data of dynamic report based on condition provided
	 * 
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<DynamicReportData> getPaginatedList(int startIndex, int limit, String sidx,String sord,String reportName, String reportDesc) {
		Map<String,Object> tableConditionList = dynamicReportDataDao.getRuleForSearchTableConditionList(reportName,reportDesc);
		return dynamicReportDataDao.getPaginatedList(DynamicReportData.class, (List<Criterion>) tableConditionList.get("conditions"),
				(HashMap<String, String>) tableConditionList.get("aliases"), startIndex, limit, sidx, sord);
	}
	
	@Override
	@Transactional(readOnly = true)
	public DynamicReportData getDynamicReportDataByReportName(String reportName){
		return dynamicReportDataDao.getDynamicReportDataByReportName(reportName);
	}
	
}
