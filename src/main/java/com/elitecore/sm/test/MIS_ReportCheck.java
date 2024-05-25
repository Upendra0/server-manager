/**
 * 
 */
package com.elitecore.sm.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.mis.model.MISReportData;
import com.elitecore.sm.mis.service.MISReportTableData;
import com.elitecore.sm.util.MapCache;

/**
 * @author vandana.awatramani
 *
 */
public class MIS_ReportCheck {

	private static final String SERVERID = "serverId";
	private static final String SERVICEID = "serviceId";
	private static final String REPORT_START_DATE = "reportstartdate";
	private static final String REPORT_END_DATE = "reportenddate";
	private static final String SERVICE_TYPE = "servicetype";
	private static final String SERVER_NAME = "serverName";
	private static final String DATE_FORMAT = "dd/MM/yyyy";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
		//queryHql();
		
		queryCriteria();
		    

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		System.out.println("finally");
		System.exit(0);
	}
	}
	
	private static void queryCriteria() throws ParseException{
		

			Session session = HibernateUtil.getSessionFactory().openSession();
			
			  SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			    String myMinDate = "17-04-2011";
			    // Create date 17-04-2011 - 00h00
			    Date minDate = formatter.parse(myMinDate);
			    // Create date 18-04-2011 - 00h00 
			    // -> We take the 1st date and add it 1 day in millisecond thanks to a useful and not so known class
			    //Date maxDate = new Date(minDate.getTime() + TimeUnit.DAYS.toMillis(1));
			    String myMaxDate = "17-12-2016";
			    // Create date 17-04-2011 - 00h00
			    Date maxDate = formatter.parse(myMaxDate);
			    System.out.println("Min date is " +minDate);
			    System.out.println("Max date is " +maxDate);
			  
			

			List<MISReportTableData> dataList = new ArrayList<>();
		Criteria crit=	session.createCriteria(MISReportData.class,"misReportData")
			.setFetchMode("misReportData.reportCallDetail", FetchMode.JOIN)
			.createAlias("misReportData.reportCallDetail", "misCallDetail")
			.setProjection( Projections.projectionList()
							.add(Projections.property("misReportData.reportStartTime"))
							.add(Projections.sum("misReportData.receivedPackets") )
							.add(Projections.sum("misReportData.successPackets"))
							.add(Projections.sum("misReportData.droppedPackets"))
							.add(Projections.sum("misReportData.receivedFiles"))
							.add(Projections.sum("misReportData.successFiles"))
							.add(Projections.sum("misReportData.failFiles"))     
							.add(Projections.sum("misReportData.totalRecords"))  
							.add(Projections.sum("misReportData.successRecords"))
                           .add(Projections.sum("misReportData.failRecords"))
                            //  .add(Projections.groupProperty("reportStartTime"))
                            //  .setProjection(Projections.sqlGroupProjection("date({alias}.beginExam) as beginDate", "beginDate", new String[] { "beginDate" }, new Type[] { StandardBasicTypes.DATE }))
                           .add(Projections.sqlGroupProjection("date(reportStartTime) as reportStartTime ", "reportStartTime", new String[] { "reportStartTime" }, new Type[] { StandardBasicTypes.DATE }))
                           )
            .add(Restrictions.eq("misReportData.serviceType", "PARSING_SERVICE"))
            .add(Restrictions.eq("misCallDetail.serverId", 2))
           . add( Restrictions.ge("misReportData.reportStartTime", minDate) )
           .add( Restrictions.le("misReportData.reportStartTime", maxDate) ) 
           // .add(and)
          /* .add(Restrictions.ge("misReportData.reportStartTime", new Timestamp(reportStartDate.getTime())))
			.add(Restrictions.le("misReportData.reportStartTime", new Timestamp(reportEndDate.getTime())))*/
             .addOrder(Order.asc("reportStartTime"));
		
		
		List<Object> result = crit.list();
		MISReportTableData reportData;
		System.out.println("Result is " + result);
		if (result != null && !result.isEmpty()) {
			Iterator<Object> iterator = result.iterator();
			while (iterator.hasNext()) {
				Object[] tuple = (Object[]) iterator.next();
				reportData = new MISReportTableData();

				//reportData.setHour((Integer) tuple[0]);
				setReportData(tuple, reportData);
				dataList.add(reportData);
			}
		}
		
	}
	
	
	private static void queryHql(){

		

			Session session = HibernateUtil.getSessionFactory().openSession();

			List<MISReportTableData> dataList = new ArrayList<>();

			StringBuffer sb = new StringBuffer();
			sb.append(" select to_date(to_char(a.reportStartTime,'"
					+ DATE_FORMAT + "'),'" + DATE_FORMAT + "'),");
			sb.append(" sum(a.receivedPackets), sum(a.successPackets), sum(a.droppedPackets), sum(a.receivedFiles), sum(a.successFiles), ");
			sb.append(" sum(a.failFiles), sum(a.totalRecords), sum(a.successRecords), sum(a.failRecords) ");
			sb.append(" from MISReportData a, MISDetail b ");
			sb.append(" where b.serverId = :serverId ");
			sb.append(" and a.serviceType = :serviceType ");
			sb.append(" and b.callId = a.reportCallDetail.callId ");
			sb.append(" and to_date(to_char(a.reportStartTime,'"
					+ DATE_FORMAT
					+ "'),'"
					+ DATE_FORMAT
					+ "')  >= to_date( :reportstartdate , '"
					+ MapCache.getConfigValueAsString(
							SystemParametersConstant.DATE_FORMAT, DATE_FORMAT)
					+ "' ) ");
			sb.append(" and to_date(to_char(a.reportStartTime,'"
					+ DATE_FORMAT
					+ "'),'"
					+ DATE_FORMAT
					+ "')  <= to_date( :reportenddate , '"
					+ MapCache.getConfigValueAsString(
							SystemParametersConstant.DATE_FORMAT, DATE_FORMAT)
					+ "' ) ");
			sb.append(" group by to_date(to_char(a.reportStartTime,'"
					+ DATE_FORMAT + "'),'" + DATE_FORMAT + "') ");
			sb.append(" order by to_date(to_char(a.reportStartTime,'"
					+ DATE_FORMAT + "'),'" + DATE_FORMAT + "') ");

			Query query = session.createQuery(sb.toString());
			query.setInteger(SERVERID, Integer.parseInt("2"));
			query.setString("serviceType", "PARSING_SERVICE");
			query.setString("reportstartdate", "01/01/2012");
			query.setString("reportenddate", "01/12/2016");
			MISReportTableData reportData;
			List<Object> result = query.list();
			
			System.out.println("Result is " + result);
			if (result != null && !result.isEmpty()) {
				Iterator<Object> iterator = query.list().iterator();
				while (iterator.hasNext()) {
					Object[] tuple = (Object[]) iterator.next();
					reportData = new MISReportTableData();

					//reportData.setHour((Integer) tuple[0]);
					setReportData(tuple, reportData);
					dataList.add(reportData);
				}
			}

	}
	

	private static void setReportData(Object[] tuple,
			MISReportTableData reportData) {
		System.out.println(" Tuple data is " + tuple[0]);
		System.out.println(" Tuple data is " + tuple[1]);
		System.out.println(" Tuple data is " + tuple[2]);
		System.out.println(" Tuple data is " + tuple[3]);
		System.out.println(" Tuple data is " + tuple[4]);
		System.out.println(" Tuple data is " + tuple[5]);
		System.out.println(" Tuple data is " + tuple[6]);
		
		System.out.println(" Tuple data is " + tuple[7]);
		System.out.println(" Tuple data is " + tuple[8]);
		System.out.println(" Tuple data is " + tuple[9]);
	
		reportData.setReceivedPackets((Long) tuple[1]);
		reportData.setSuccessPackets((Long) tuple[2]);
		reportData.setDroppedPackets((Long) tuple[3]);
		reportData.setReceivedFiles((Long) tuple[4]);
		reportData.setSuccessFiles((Long) tuple[5]);
		reportData.setFailFiles((Long) tuple[6]);
		reportData.setTotalRecords((Long) tuple[7]);
		reportData.setSuccessRecords((Long) tuple[8]);
		reportData.setFailRecords((Long) tuple[9]);
	}

}
