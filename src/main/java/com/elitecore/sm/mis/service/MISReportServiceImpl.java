package com.elitecore.sm.mis.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.core.commons.util.data.MISReportData;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.MISReportUtils;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.mis.dao.MISDetailDao;
import com.elitecore.sm.mis.dao.MISReportDataDao;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.mis.model.ReportTypeParameters;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.MapCache;

/**
 * 
 * @author Neha Kochhar- Runs the scheduler and Makes entries to two tables
 *         TBLTMISREPORTDATA and TBLTMISREPORTAGENTCALLDETAIL to generate MIS
 *         Reports
 */
@org.springframework.stereotype.Service(value = "misReportService")
public class MISReportServiceImpl implements MISReportService {
	@Autowired
	private MISDetailDao mISDetailDao;

	@Autowired
	private MISReportDataDao mISReportDataDao;

	@Autowired
	private ServerInstanceService serverInstanceService;

	@Autowired
	private ServicesService servicesService;

	@Autowired
	private ServicesDao servicesDao;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());

	public MISReportServiceImpl() {
		// This is default constructor required by spring
	}

	/**
	 * Method called by the scheduler at hourly basis (default).
	 */
	@Override
	public void run() {
		try {
			// Get records of Server and Services from DB
			logger.debug("Inside MISReportAgent Run method.");
			logger.debug("Getting server instance list from DB.");
			List<ServerInstance> serverInstanceList = getServerInstanceList();
			if (serverInstanceList != null && !serverInstanceList.isEmpty()) {
				ThreadPoolTaskExecutor taskExecutor;
				for (ServerInstance serverInstance : serverInstanceList) {
					List<Service> serviceInstanceList = getServiceListForEachServerInstance(serverInstance.getId());
					if (serviceInstanceList != null && !serviceInstanceList.isEmpty()) {
						// for thread pooling
						taskExecutor = (ThreadPoolTaskExecutor) SpringApplicationContext.getBean("taskExecutor");
						CrestelReportClientService crestelReportClientService = (CrestelReportClientService) SpringApplicationContext
								.getBean("crestelReportClient");
						crestelReportClientService.setInstanceData(serverInstance);
						crestelReportClientService.setServiceDataList(serviceInstanceList);
						taskExecutor.execute(crestelReportClientService);
						logger.debug("Starting MIS Report Agent thread from Server: " + serverInstance.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * Returns the list of all active Server Instances avaialable in DB
	 */
	@Override
	public List<ServerInstance> getServerInstanceList() {
		return serverInstanceService.getServerInstanceList();
	}

	/**
	 * Returns the list of all Services mapped to the serverId
	 */
	@Override
	public List<Service> getServiceListForEachServerInstance(int serverId) {
		return servicesService.getServiceList(serverId);
	}

	/**
	 * Returns the startTime to the server Instance for MIS scheduler
	 */
	@Override
	@Transactional(readOnly = true)
	public Timestamp getReportStartTime(int serverId) throws SMException {
		return mISDetailDao.getReportStartTime(serverId);
	}

	/**
	 * Inserts data into tables TBLTMISREPORTDATA and
	 * TBLTMISREPORTAGENTCALLDETAIL
	 */
	@Override
	@Transactional
	public void insertReportAgentCallDetail(MISDetail callDetail) throws SMException {
		mISDetailDao.insertReportAgentCallDetail(callDetail);
	}

	/**
	 * Returns the list of all Services mapped to the serviceAlias
	 */
	@Override
	public ResponseObject getServiceListByAlias(String serviceName){
		List<Service> serviceList = servicesService.getServiceListByAlias(serviceName);
		Set<String> serversLst = null;
		
		if(serviceList != null && !serviceList.isEmpty()){
			Iterator<Service> itr = serviceList.iterator();
			serversLst = new HashSet<>();
			while (itr.hasNext()) {
				serversLst.add(itr.next().getServerInstance().getName());
			}
		}
		
		ResponseObject responseObject = new ResponseObject();
		if (serversLst != null && !serversLst.isEmpty()) {
			responseObject.setSuccess(true);
			JSONArray jsonArray = new JSONArray(serversLst);
			responseObject.setObject(jsonArray);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.NO_SERVER_AVAILABLE);
		}
		return responseObject;
	}

	/**
	 * Gets Data for MIS report as Summary
	 */
	@Override
	@Transactional(readOnly = true)
	@Auditable(auditActivity = AuditConstants.GENERATE_SERVICEWISE_SUMMARY, actionType = BaseConstants.GENERATE_REPORTS, currentEntity = ReportTypeParameters.class ,ignorePropList= "")
	public List<MISReportTableData> getServerSummaryData(List<String> serverIdList, ReportTypeParameters reportTypeParams){
		Date reportStartDate;
		Date reportEndDate;
		String serviceTypeId = reportTypeParams.getServiceInstanceId();
		String reportType = reportTypeParams.getReportType();
		
		if(StringUtils.equals(reportType, BaseConstants.HOURLY)){
	        reportStartDate = setStartDate(reportTypeParams.getHourlyReportDate());
	        reportEndDate = setEndDate(reportTypeParams.getHourlyReportDate());
			logger.info("Start- Date " +reportStartDate + " End- Date " + reportEndDate + "for Hourly Summary Report");
		}else{
	        reportStartDate = setStartDate(reportTypeParams.getStartDate());
			reportEndDate = setEndDate(reportTypeParams.getEndDate());
		}
		logger.info("Start- Date " +reportStartDate + "End- Date for Monthly/Daily Summary Report :: "+reportEndDate);
		List<MISReportTableData> dataList = mISReportDataDao.getServerSummaryData(serverIdList, serviceTypeId, reportStartDate, reportEndDate);

		dataList = setMISReportTableData(dataList);
		return dataList;
	}
	/**
	 * Append time to Start date
	 * @param date
	 * @return
	 */
	private Date setStartDate(Date date){
		Calendar cal = Calendar.getInstance(); 
		Date finalDate;
		cal.setTime(date); 
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);  
        cal.set(Calendar.SECOND, 0);  
        cal.set(Calendar.MILLISECOND, 0); 
        finalDate = cal.getTime();
		
		return finalDate;
	}
	
	/**
	 * Append time to End date
	 * @param date
	 * @return
	 */
	private Date setEndDate(Date date){
		
		 Calendar cal = Calendar.getInstance();
		 Date finalDate;
		 cal.setTime(date); 
		 cal.add(Calendar.DATE, 1);
		 cal.set(Calendar.HOUR_OF_DAY, 0);
		 cal.set(Calendar.MINUTE, 0);  
		 cal.set(Calendar.SECOND, 0);  
		 cal.set(Calendar.MILLISECOND, 0); 
	     finalDate = cal.getTime();
		return finalDate;
	}
	/**
	 * Set various parameters to the dataList obtained from DataBase
	 * @param dataList
	 * @return
	 */
	private List<MISReportTableData> setMISReportTableData(List<MISReportTableData> dataList){

		if(!dataList.isEmpty()){
			for(int i = 0; i < dataList.size(); i++) {
				MISReportTableData data = dataList.get(i);
				Long droppedPackets = data.getDroppedPackets();
				Long receivedPackets = data.getReceivedPackets();
				Long failFiles = data.getFailFiles();
				Long receivedFiles = data.getReceivedFiles();
				Long failRecords = data.getFailRecords();
				Long totalRecords = data.getTotalRecords();

				Double droppedPacketsPercentage = receivedPackets != 0 ? Math.round((droppedPackets * 10000.0) / receivedPackets) / 100.0 : 0;
				Double failedFilesPercentage = receivedFiles != 0 ? Math.round((failFiles * 10000.0) / receivedFiles) / 100.0 : 0;
				Double failedRecordsPercentage = totalRecords != 0 ? Math.round((failRecords * 10000.0) / totalRecords) / 100.0 : 0;

				data.setDroppedPacketsPercentage(droppedPacketsPercentage);
				data.setFailedFilesPercentage(failedFilesPercentage);
				data.setFailedRecordsPercentage(failedRecordsPercentage);
				dataList.set(i, data);
			}
		}else{
			logger.info("Data list returned from DB is empty");
		}
		return dataList;
	}
	/**
	 * Creates report data to be downloaded in MISReportData type Object
	 * @param logoPath
	 * @param headerList
	 * @param dataList
	 * @param startDate
	 * @param endDate
	 * @param loggedInStaffName
	 * @param paramList
	 * @return
	 */
	@Override
	public MISReportData createReportDataForDownload(ReportTypeParameters reportTypeParams, String loggedInStaffName,Boolean detailFlag){
		List<String> paramList = new ArrayList<>();
		paramList.add("Service Type : " + reportTypeParams.getServiceInstanceId());
		String logoPath = BaseConstants.CRESTEL_IMAGE_PATH;

		MISReportData reportData = new MISReportData();
		reportData.setAuthor(BaseConstants.REPORT_HEADER);
		reportData.setDataList(reportTypeParams.getDataList());
		reportData.setFooter(BaseConstants.REPORT_HEADER);
		reportData.setGeneratedBy(loggedInStaffName);
		reportData.setGeneratedDate(new Date());
		reportData.setSearchStartValue(MISReportUtils.getSystemFormattedDate(reportTypeParams.getStartDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT, BaseConstants.DATE_FORMAT_SHORT)));
		reportData.setSearchEndValue(MISReportUtils.getSystemFormattedDate(reportTypeParams.getEndDate(), MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT, BaseConstants.DATE_FORMAT_SHORT)));
		reportData.setHeaderList(reportTypeParams.getHeaderList());
		reportData.setLogoPath(logoPath);
		reportData.setDateFormat(MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT, BaseConstants.DATE_FORMAT_SHORT));
		reportData.setShortDateFormat(MapCache.getConfigValueAsString(SystemParametersConstant.DATE_FORMAT, BaseConstants.DATE_FORMAT_SHORT));
		reportData.setDataAlign("center");
		reportData.setParamList(paramList);
		if(detailFlag){
			reportData.setHeader(BaseConstants.SERVICE_WISE_DETAIL_REPORT);
			reportData.setReportName(BaseConstants.SERVICE_WISE_DETAIL_REPORT);
			reportData.setTitle(BaseConstants.SERVICE_WISE_DETAIL_REPORT);
		}else{
			reportData.setHeader(BaseConstants.SERVICE_WISE_SUMMARY_REPORT);
			reportData.setReportName(BaseConstants.SERVICE_WISE_SUMMARY_REPORT);
			reportData.setTitle(BaseConstants.SERVICE_WISE_SUMMARY_REPORT);
		}

		return reportData;
	}


	/**
	 * Gets Data for MIS report in Detail
	 */
	@Override
	@Transactional(readOnly = true)
	public List<MISReportTableData> getServiceDetailData(ReportTypeParameters reportTypeParams)
	{
		Date reportStartDate;
		Date reportEndDate;
		List<MISReportTableData> dataList;
		String serverId = reportTypeParams.getServerInstanceId();
		String serviceId = reportTypeParams.getServiceInstanceId();
		String reportType = reportTypeParams.getReportType();

		if(StringUtils.equals(reportType, BaseConstants.HOURLY)){
	        reportStartDate = setStartDate(reportTypeParams.getHourlyReportDate());
			reportEndDate = setEndDate(reportTypeParams.getHourlyReportDate());
			
			logger.info("Start Date " +reportStartDate + "End Date for Hourly Detail Report :: "+reportStartDate);
			dataList = mISReportDataDao.getServiceDetailHourlyData(serverId, serviceId, reportStartDate,reportEndDate);
		}else{
	        reportStartDate = setStartDate(reportTypeParams.getStartDate());
			reportEndDate = setEndDate(reportTypeParams.getEndDate());
			logger.info("Start Date " +reportStartDate + "End Date for Monthly/Daily Detail Report :: "+reportEndDate);
			if(StringUtils.equals(reportType, BaseConstants.DAILY)) {
				dataList = mISReportDataDao.getServiceDetailDailyData(serverId, serviceId, reportStartDate, reportEndDate);
			}else{
				dataList = mISReportDataDao.getServiceDetailMonthlyData(serverId, serviceId, reportStartDate, reportEndDate);
			}
		}
		dataList = setMISReportTableData(dataList);
		return dataList;
	}

	/**
	 * Fetch ServiceList based on ServerInstanceId
	 * 
	 * @param serverInstanceId
	 * @return ResponseObject
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject fetchServiceListBySI(int serverInstanceId) {
		ResponseObject responseObject = new ResponseObject();
		List<Service> serverInstanceList = servicesDao.getServicesforServerInstance(serverInstanceId);
		JSONArray jAllSvcArr = new JSONArray();
		JSONObject jSvcObj;

		if (serverInstanceList != null && !serverInstanceList.isEmpty()) {
			for (Service service : serverInstanceList) {
				logger.debug("Found service , Name: " + service.getName());

				jSvcObj = new JSONObject();
				jSvcObj.put("alias",service.getSvctype().getAlias());
				jSvcObj.put("name", service.getName());
				jAllSvcArr.put(jSvcObj);
			}
			JSONObject allSvc=new JSONObject();
			allSvc.put("svcList", jAllSvcArr);
			responseObject.setObject(allSvc);
			responseObject.setSuccess(true);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.NO_SERVICE_AVAILABLE);
			logger.info("No Service found for the server");
		}
		return responseObject;
	}
}