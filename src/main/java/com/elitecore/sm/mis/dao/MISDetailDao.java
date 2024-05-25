package com.elitecore.sm.mis.dao;

import java.sql.Timestamp;
import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;

public interface MISDetailDao extends GenericDAO<MISDetail>
{
	public void insertReportAgentCallDetail(MISDetail callDetail);
	public Timestamp getReportStartTime(int serverId);
	public List<ServerInstance> getReportServerList();
}