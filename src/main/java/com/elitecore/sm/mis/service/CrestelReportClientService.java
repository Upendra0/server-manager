package com.elitecore.sm.mis.service;

import java.sql.Timestamp;
import java.util.List;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;

public interface CrestelReportClientService extends Runnable{
	public Timestamp getReportStartTime(int serverId) throws SMException;
	public void insertReportAgentCallDetail(MISDetail callDetail) throws SMException;
	public void setInstanceData(ServerInstance instanceData);
	public void setServiceDataList(List<Service> serviceDataList);
}
