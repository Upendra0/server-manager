package com.elitecore.sm.mis.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.serverinstance.model.ServerInstance;

@Repository(value = "mISDetailDao")
public class MISDetailDaoImpl extends GenericDAOImpl<MISDetail> implements MISDetailDao{
	/**
	 * Saves Data into TBLTMISREPORTAGENTCALLDETAIL and TBLTMISREPORTDATA
	 */
	@Override
	public void insertReportAgentCallDetail(MISDetail callDetail) {
		getCurrentSession().save(callDetail);
	}

	/**
	 * Get report start time from TBLTMISREPORTAGENTCALLDETAIL
	 */
	@Override
	public Timestamp getReportStartTime(int serverId){
		Timestamp startTime;
		Criteria criteria = getCurrentSession().createCriteria(MISDetail.class);
		criteria.add(Restrictions.eq("serverId", serverId));
		criteria.add(Restrictions.eq("reportStatus", BaseConstants.CALL_SUCCESS_STATUS));
		criteria.setProjection(Projections.max("reportEndTime"));
		startTime = (Timestamp)criteria.uniqueResult();

		if(startTime == null) {
			Calendar cal = new GregorianCalendar();
			cal.set(BaseConstants.MIN_YEAR, Calendar.JANUARY, 1, 0, 0, 0);
			startTime = new Timestamp(cal.getTimeInMillis());
		}
		return startTime;
	}

	/**
	 * Get list of all servers from DataBase
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<ServerInstance> getReportServerList(){
		List<ServerInstance> serverList = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct serverId, serverName from MISReportAgentCallDetail");
		Query query = getCurrentSession().createQuery(sb.toString());
		ServerInstance netServerInstanceData;
		List<Object> result = query.list();
		if(result != null && !result.isEmpty()){
		Iterator<Object> iterator= query.list().iterator();
		while(iterator.hasNext()) {
			Object[] tuple= (Object[]) iterator.next();
			netServerInstanceData = new ServerInstance();

			netServerInstanceData.setId((int)tuple[0]);
			netServerInstanceData.setName((String)tuple[1]);

			serverList.add(netServerInstanceData);
		}
		}
		return serverList;
	}
}
