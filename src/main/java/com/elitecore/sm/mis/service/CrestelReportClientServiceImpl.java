package com.elitecore.sm.mis.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.elitecore.core.commons.util.data.MISReportRequestData;
import com.elitecore.core.commons.util.data.MISReportResponseData;
import com.elitecore.core.commons.util.data.MISReportServiceData;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.MISReportUtils;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.mis.dao.MISDetailDao;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.mis.model.MISReportData;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

/**
 * 
 * @author Neha Kochhar- Scheduler spawns a thread for each active server Instance in DB
 * This Thread class then gets services Data for a particular server Instance and this service List
 * is passed to Mediation Engine to return the data from packet statistics for all these services 
 * which will be stored in SM REWAMP DB to generate reports later.
 */
@Component(value="crestelReportClient")
public class CrestelReportClientServiceImpl implements CrestelReportClientService
{
	static final String MODULE = "MISReportAgent-CrestelReportMBeanClient";
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private ServerInstance instanceData = null;
	private List<Service> serviceDataList = null;
	private Date callTime = new Date();
	@Autowired
	private MISDetailDao mISDetailDao;
	@Autowired
	private MISReportService misReportService;

	public CrestelReportClientServiceImpl()
	{	//default constructor 
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() 
	{
		makePacketStatsCall(instanceData, serviceDataList);		
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.mis.service.CrestelReportClientService#makePacketStatsCall(com.elitecore.sm.serverinstance.model.ServerInstance, java.util.List)
	 */
	private void makePacketStatsCall(ServerInstance instanceData, List<Service> serviceDataList) 
	{
		List<MISReportResponseData> responseData = null;
		MISDetail callDetail = new MISDetail();
		List<MISReportServiceData> serviceList = new ArrayList<>();
		try	{
			MISReportRequestData requestData = new MISReportRequestData();
			Calendar cal = new GregorianCalendar();
			cal.setTime(callTime);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date reportEndTime = cal.getTime();
			cal.add(Calendar.HOUR_OF_DAY, -1);
			Timestamp reportStartTime = getReportStartTime(instanceData.getId());

			callDetail.setCallTime(new Timestamp(callTime.getTime()));
			callDetail.setReportStartTime(reportStartTime);
			callDetail.setReportEndTime(new Timestamp(reportEndTime.getTime()));
			callDetail.setServerId(instanceData.getId());
			callDetail.setServerName(instanceData.getName());
			RemoteJMXHelper remoteJMXHelper = new RemoteJMXHelper(instanceData.getServer().getIpAddress(), instanceData.getPort(),
					instanceData.getMaxConnectionRetry(), instanceData.getRetryInterval(), instanceData.getConnectionTimeout());
			requestData.setReportStartDate(reportStartTime);
			requestData.setReportEndDate(reportEndTime);

			for(Service service : serviceDataList) {
				MISReportServiceData data = new MISReportServiceData();
				data.setServiceId(String.valueOf(service.getId()));
				data.setServiceInstanceId(service.getServInstanceId());
				data.setServiceName(service.getName());
				data.setServiceTypeAlias(service.getSvctype().getAlias());
				serviceList.add(data);
			}
			requestData.setServiceDataList(serviceList);

			logger.debug("Making call to Server: " + instanceData.getName() + " :: " + instanceData.getServer().getIpAddress() + ":" + instanceData.getPort());
			logger.debug("Making MIS Agent call with request data: " + requestData.toString());
			responseData = remoteJMXHelper.getPacketStatsData(requestData);

			if((responseData == null) || (responseData.isEmpty())) {
				logger.error("No Data Found");
				throw new SMException("No Data Found");
			}
			logger.debug("No of response data: " + responseData.size());
			Set<MISReportData> reportDataSet = MISReportUtils.getReportData(callDetail, responseData);
			if(!reportDataSet.isEmpty()){
				callDetail.setReportDataSet(reportDataSet);
				callDetail.setReportStatus(BaseConstants.CALL_SUCCESS_STATUS);
			}else{
				callDetail.setReportStatus(BaseConstants.CALL_FAIL_STATUS);
			}
		} catch (Exception e) {
			logger.error("Error - in server call operation,inserting status as FAIL in DB :: Reason : " +e.getMessage(),e);
			callDetail.setReportStatus(BaseConstants.CALL_FAIL_STATUS);
		}finally{
			serviceList.clear();
			serviceList = null;
		}
		try {
			logger.debug("Inserting Call Detail in DB.");
			logger.debug(callDetail.toString());
			insertReportAgentCallDetail(callDetail);
		} catch(Exception e) {
			logger.error("Could not insert Remote call detail in Database. Reason: " + e.getMessage(), e);
		}

	}
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.mis.service.CrestelReportClientService#getReportStartTime(int)
	 */
	@Override
	public Timestamp getReportStartTime(int serverId) throws SMException{
		return misReportService.getReportStartTime(serverId);
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.mis.service.CrestelReportClientService#insertReportAgentCallDetail(com.elitecore.sm.mis.model.MISDetail)
	 */
	@Override
	public void insertReportAgentCallDetail(MISDetail callDetail) throws SMException{
		misReportService.insertReportAgentCallDetail(callDetail);
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.mis.service.CrestelReportClientService#setInstanceData(com.elitecore.sm.serverinstance.model.ServerInstance)
	 */
	@Override
	public void setInstanceData(ServerInstance instanceData) {
		this.instanceData = instanceData;
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.mis.service.CrestelReportClientService#setServiceDataList(java.util.List)
	 */
	@Override
	public void setServiceDataList(List<Service> serviceDataList) {
		this.serviceDataList = serviceDataList;

	}

}
