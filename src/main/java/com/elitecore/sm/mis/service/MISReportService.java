package com.elitecore.sm.mis.service;

import java.sql.Timestamp;
import java.util.List;

import com.elitecore.core.commons.util.data.MISReportData;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.mis.model.ReportTypeParameters;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

public interface MISReportService {
	public List<ServerInstance> getServerInstanceList();
	public List<Service> getServiceListForEachServerInstance(int serverId);
	public void run();
	public Timestamp getReportStartTime(int serverId) throws SMException;
	public void insertReportAgentCallDetail(MISDetail callDetail) throws SMException;
	public ResponseObject getServiceListByAlias(String serviceName);
	public List<MISReportTableData> getServerSummaryData(List<String> serverIdList,ReportTypeParameters reportTypeParams);
	public ResponseObject fetchServiceListBySI(int serverInstanceId);
	public List<MISReportTableData> getServiceDetailData(ReportTypeParameters reportTypeParams);
	public MISReportData createReportDataForDownload(ReportTypeParameters reportTypeParams, String loggedInStaffName,Boolean detailFlag);
}
