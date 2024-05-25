package com.elitecore.sm.mis.dao;

import java.util.Date;
import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.mis.model.MISReportData;
import com.elitecore.sm.mis.service.MISReportTableData;

public interface MISReportDataDao extends GenericDAO<MISReportData>
{
	public List<MISReportTableData> getServiceDetailHourlyData(String serverId, String serviceId, Date reportDate, Date reportEndDate);
	
	public List<MISReportTableData> getServiceDetailDailyData(String serverId, String serviceId, Date reportStartDate, Date reportEndDate);
	
	public List<MISReportTableData> getServiceDetailMonthlyData(String serverId, String serviceId, Date reportStartDate, Date reportEndDate);
	
	public List<MISReportTableData> getServerSummaryData(List<String> serverIdList, String serviceType, Date reportStartDate, Date reportEndDate);

}

