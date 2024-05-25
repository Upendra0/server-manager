package com.elitecore.sm.mis.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.util.MISReportUtils;
import com.elitecore.sm.mis.model.MISReportData;
import com.elitecore.sm.mis.service.MISReportTableData;

@Repository(value = "misReportDataDao")
public class MISReportDataDaoImpl extends GenericDAOImpl<MISReportData> implements MISReportDataDao{

	private static final String SERVERID="serverId";
	private static final String SERVICETYPE="serviceType";
	private static final String REPORT_START_DATE="reportStartDate";
	private static final String REPORT_END_DATE="reportEndDate";
	private static final String REPORT_CALL_DETAIL = "misReportData.reportCallDetail";
	private static final String MIS_REPORT_SERVICETYPE = "misReportData.serviceType";

	/**
	 * Creates query for Hourly Detail Data
	 * @return
	 */
	private StringBuilder createQueryForHourlyDataDetail(){
		StringBuilder sb = new StringBuilder();
		sb.append(" select extract ( hour from a.reportStartTime), ");
		sb.append(" sum(a.receivedPackets), sum(a.successPackets), sum(a.droppedPackets), sum(a.receivedFiles), sum(a.successFiles), ");
		sb.append(" sum(a.failFiles), sum(a.totalRecords), sum(a.successRecords), sum(a.failRecords) " );
		sb.append(" from MISReportData a, MISDetail b ");
		sb.append(" where b.serverId = :serverId ");
		sb.append(" and a.serviceType = :serviceType ");
		sb.append(" and b.callId = a.reportCallDetail.callId ");
		sb.append(" and a.reportStartTime >= :reportStartDate ");
		sb.append(" and a.reportStartTime < :reportEndDate ");
		sb.append(" group by extract ( hour from a.reportStartTime) ");
		sb.append(" order by extract ( hour from a.reportStartTime)");
		return sb;
	}

	/**
	 * Get Hourly Detail data from DataBase 
	 * returns  DataList of type MISReportTableData
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MISReportTableData> getServiceDetailHourlyData(String serverId, String serviceType, Date reportStartDate,Date reportEndDate) {

		List<MISReportTableData> dataList = new ArrayList<>();
		StringBuilder sb = createQueryForHourlyDataDetail();
		Query query = getCurrentSession().createQuery(sb.toString());
		query.setInteger(SERVERID, Integer.parseInt(serverId));
		query.setString(SERVICETYPE, serviceType);
		query.setTimestamp(REPORT_START_DATE, new Timestamp(reportStartDate.getTime()));
		query.setTimestamp(REPORT_END_DATE, new Timestamp(reportEndDate.getTime()));
		MISReportTableData reportData;
		List<Object> result = query.list();
		if(result != null && !result.isEmpty()){
			Iterator<Object> iterator= result.iterator();
			while(iterator.hasNext()) {
				Object[] tuple= (Object[]) iterator.next();
				reportData = new MISReportTableData();

				reportData.setHour((Integer)tuple[0]);
				setReportData(tuple, reportData);
				dataList.add(reportData);
			}
		}
		return dataList;
	}
	
	/**
	 * Set DataList data for various rows in Detail report for Daily and Hourly
	 * @param tuple
	 * @param reportData
	 */
	private void setReportData(Object[] tuple, MISReportTableData reportData){

		reportData.setReceivedPackets((Long)tuple[1]);
		reportData.setSuccessPackets((Long)tuple[2]);
		reportData.setDroppedPackets((Long)tuple[3]);
		reportData.setReceivedFiles((Long)tuple[4]);
		reportData.setSuccessFiles((Long)tuple[5]);
		reportData.setFailFiles((Long)tuple[6]);
		reportData.setTotalRecords((Long)tuple[7]);
		reportData.setSuccessRecords((Long)tuple[8]);
		reportData.setFailRecords((Long)tuple[9]);
	}
	/**
	 * Creates query for Daily Detail Data
	 * @return
	 */

	private Criteria createQueryForDailyDetailData(int serverId, String serviceType, Date reportStartDate, Date reportEndDate){

		return getCurrentSession().createCriteria(MISReportData.class,"misReportData")
				.setFetchMode(REPORT_CALL_DETAIL, FetchMode.JOIN)
				.createAlias(REPORT_CALL_DETAIL, "misCallDetail")
				.setProjection( Projections.projectionList()
								.add(Projections.property("misReportData.reportStartDate"))
								.add(Projections.sum("misReportData.receivedPackets") )
								.add(Projections.sum("misReportData.successPackets"))
								.add(Projections.sum("misReportData.droppedPackets"))
								.add(Projections.sum("misReportData.receivedFiles"))
								.add(Projections.sum("misReportData.successFiles"))
								.add(Projections.sum("misReportData.failFiles"))     
								.add(Projections.sum("misReportData.totalRecords"))  
								.add(Projections.sum("misReportData.successRecords"))
	                            .add(Projections.sum("misReportData.failRecords"))
	                            .add(Projections.groupProperty("misReportData.reportStartDate"))
	                            )
	             .add(Restrictions.eq(MIS_REPORT_SERVICETYPE, serviceType))
	             .add(Restrictions.eq("misCallDetail.serverId", serverId))
	             .add(Restrictions.ge("misReportData.reportStartTime", new Timestamp(reportStartDate.getTime())))
				 .add(Restrictions.le("misReportData.reportStartTime", new Timestamp(reportEndDate.getTime())))
	             .addOrder(Order.asc("misReportData.reportStartDate"));
	}

	/**
	 * Get Daily Detail data from DataBase 
	 * returns  DataList of type MISReportTableData
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MISReportTableData> getServiceDetailDailyData(String serverId, String serviceType, Date reportStartDate, Date reportEndDate){
		List<MISReportTableData> dataList = new ArrayList<>();
		Criteria criteria = createQueryForDailyDetailData(Integer.parseInt(serverId),serviceType,reportStartDate,reportEndDate);
		MISReportTableData reportData;
		List<Object> result = criteria.list();
		if(result != null && !result.isEmpty()){
			Iterator<Object> iterator= result.iterator();
			while(iterator.hasNext()) {
				Object[] tuple= (Object[]) iterator.next();
				reportData = new MISReportTableData();
				
				reportData.setDate(MISReportUtils.getCalendarField((Date)tuple[0], Calendar.DATE));
				reportData.setMonth(MISReportUtils.getCalendarField((Date)tuple[0], Calendar.MONTH));
				reportData.setYear(MISReportUtils.getCalendarField((Date)tuple[0], Calendar.YEAR));
				setReportData(tuple, reportData);
				dataList.add(reportData);
			}
		}
		return dataList;
	}

	/**
	 * Creates query for Monthly Detail Data
	 * @return
	 */
	private StringBuilder createQueryForMonthlyDetailData(){
		StringBuilder sb = new StringBuilder();
		sb.append(" select extract (month from a.reportStartDate), extract ( year from a.reportStartDate), ");
		sb.append(" sum(a.receivedPackets), sum(a.successPackets), sum(a.droppedPackets), sum(a.receivedFiles), sum(a.successFiles), ");
		sb.append(" sum(a.failFiles), sum(a.totalRecords), sum(a.successRecords), sum(a.failRecords) " );
		sb.append(" from MISReportData a, MISDetail b ");
		sb.append(" where b.serverId = :serverId ");
		sb.append(" and a.serviceType = :serviceType ");
		sb.append(" and b.callId = a.reportCallDetail.callId ");
		sb.append(" and a.reportStartTime >= :reportStartDate");
		sb.append(" and a.reportStartTime <= :reportEndDate");
		sb.append(" group by extract ( month from a.reportStartDate), extract ( year from a.reportStartDate) ");
		sb.append(" order by extract ( month from a.reportStartDate), extract ( year from a.reportStartDate) ");

		return sb;
	}

	/**
	 * Get Monthly data from DataBase 
	 * returns  DataList of type MISReportTableData
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MISReportTableData> getServiceDetailMonthlyData(String serverId, String serviceType, Date reportStartDate, Date reportEndDate){
		List<MISReportTableData> dataList = new ArrayList<>();
		StringBuilder sb = createQueryForMonthlyDetailData();
		Query query = getCurrentSession().createQuery(sb.toString());
		query.setInteger(SERVERID, Integer.parseInt(serverId));
		query.setString(SERVICETYPE, serviceType);
		query.setTimestamp(REPORT_START_DATE, new Timestamp(reportStartDate.getTime()));
		query.setTimestamp(REPORT_END_DATE, new Timestamp(reportEndDate.getTime()));

		MISReportTableData reportData;
		List<Object> result = query.list();
		if(result != null && !result.isEmpty()){
			Iterator<Object> iterator= result.iterator();
			while(iterator.hasNext()) {
				Object[] tuple= (Object[]) iterator.next();
				reportData = new MISReportTableData();

				reportData.setMonth((Integer)tuple[0] - 1);
				reportData.setYear((Integer)tuple[1]);
				setReportDataForSummary(tuple, reportData);
				dataList.add(reportData);
			}
		}
		return dataList;
	}

	/**
	 * Creates query for Summary Report Data
	 * @return
	 */
	private Criteria createQueryForServerSummaryData(Date reportStartDate, Date reportEndDate, String serviceType,List<String> serverIdList){
		return getCurrentSession().createCriteria(MISReportData.class,"misReportData")
				.setFetchMode(REPORT_CALL_DETAIL, FetchMode.JOIN)
				.createAlias(REPORT_CALL_DETAIL, "misCallDetail")
				.setProjection( Projections.projectionList()
								.add(Projections.property(MIS_REPORT_SERVICETYPE), MIS_REPORT_SERVICETYPE)
								.add(Projections.property("misCallDetail.serverName"), "misCallDetail.serverName")
								.add(Projections.sum("misReportData.receivedPackets") )
								.add(Projections.sum("misReportData.successPackets"))
								.add(Projections.sum("misReportData.droppedPackets"))
								.add(Projections.sum("misReportData.receivedFiles"))
								.add(Projections.sum("misReportData.successFiles"))
								.add(Projections.sum("misReportData.failFiles"))     
								.add(Projections.sum("misReportData.totalRecords"))  
								.add(Projections.sum("misReportData.successRecords"))
	                            .add(Projections.sum("misReportData.failRecords"))
	                            .add(Projections.groupProperty(MIS_REPORT_SERVICETYPE))
	                            .add(Projections.groupProperty("misCallDetail.serverName"))
	                            )
	             .add(Restrictions.eq(MIS_REPORT_SERVICETYPE, serviceType))
	             .add(Restrictions.in("misCallDetail.serverName", serverIdList))
	             .add(Restrictions.ge("misReportData.reportStartTime", new Timestamp(reportStartDate.getTime())))
				 .add(Restrictions.le("misReportData.reportStartTime", new Timestamp(reportEndDate.getTime())))
	             .addOrder(Order.asc("misCallDetail.serverName"))
	             .addOrder(Order.asc(MIS_REPORT_SERVICETYPE));
	}
	
	/**
	 * Set DataList data for various rows in Summary report and for Monthly Detail Report
	 * @param tuple
	 * @param reportData
	 */
	private void setReportDataForSummary(Object[] tuple, MISReportTableData reportData){
		reportData.setReceivedPackets((Long)tuple[2]);
		reportData.setSuccessPackets((Long)tuple[3]);
		reportData.setDroppedPackets((Long)tuple[4]);
		reportData.setReceivedFiles((Long)tuple[5]);
		reportData.setSuccessFiles((Long)tuple[6]);
		reportData.setFailFiles((Long)tuple[7]);
		reportData.setTotalRecords((Long)tuple[8]);
		reportData.setSuccessRecords((Long)tuple[9]);
		reportData.setFailRecords((Long)tuple[10]);
	}

	/**
	 * Get Summary Data from DataBase 
	 * returns  DataList of type MISReportTableData
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<MISReportTableData> getServerSummaryData(List<String> serverIdList, String serviceType, Date reportStartDate, Date reportEndDate) 
	{
		List<MISReportTableData> dataList = new ArrayList<>();
		Criteria criteria = createQueryForServerSummaryData(reportStartDate,reportEndDate,serviceType,serverIdList);
		MISReportTableData reportData;
		List<Object> result = criteria.list();
		if(result != null && !result.isEmpty()){
			Iterator<Object> iterator= result.iterator();
			while(iterator.hasNext()) {
				Object[] tuple= (Object[]) iterator.next();
				reportData = new MISReportTableData();

				reportData.setServiceName((String)tuple[0]);
				reportData.setServerName((String)tuple[1]);
				setReportDataForSummary(tuple, reportData);
				dataList.add(reportData);
			}
		}
		return dataList;
	}
}
