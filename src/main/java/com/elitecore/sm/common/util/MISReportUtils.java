package com.elitecore.sm.common.util;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.elitecore.core.commons.util.data.MISReportResponseData;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.mis.model.MISDetail;
import com.elitecore.sm.mis.model.MISReportData;
import com.elitecore.sm.mis.model.ReportTypeParameters;
import com.elitecore.sm.mis.service.MISHourlyData;
import com.elitecore.sm.mis.service.MISReportTableData;
import com.elitecore.sm.util.DateFormatter;

public class MISReportUtils 
{
	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private static Logger			logger 						= Logger.getLogger(LicenseUtility.class);

	private MISReportUtils(){
		//default constructor
	}

	public static Set<MISReportData> getReportData(MISDetail callDetail, List<MISReportResponseData> callResponseData)
	{
		Set<MISReportData> reportDataList = new HashSet<>();


		for(MISReportResponseData responseData : callResponseData) {

			List<MISHourlyData> hourlyDataList = new ArrayList<>();
			List<String> packetStatDataList = responseData.getPacketStatDataList();

			MISHourlyData hourlyData = new MISHourlyData(responseData.getServiceType(), Integer.parseInt(responseData.getServiceId()), responseData.getServiceName());
			hourlyData.setLastHourRecord(responseData.getLastRecord());
			Date startDate;
			Date endDate = null;

			for(int i = 0; i < packetStatDataList.size(); i++) {

				JSONObject obj = new JSONObject();
				Date recordTime = null;
				try {
					obj  = new JSONObject(packetStatDataList.get(i));
					recordTime = DateFormatter.stringToDate(obj.getString("RECORD_TIME"), DATE_FORMAT);
				} catch(JSONException je) {
					logger.error(je.getMessage(),je);
					continue;
				}

				if(i == 0) {

					Calendar cal = new GregorianCalendar();
					cal.setTime(recordTime);
					Calendar startCal = cal;
					startCal.set(Calendar.MINUTE, 0);
					startCal.set(Calendar.SECOND, 0);
					startCal.set(Calendar.MILLISECOND, 0);
					startDate = startCal.getTime();
					Calendar endCal = startCal;
					endCal.add(Calendar.HOUR, 1);
					endDate = endCal.getTime();
					hourlyData.setStartDate(startDate);
					hourlyData.setEndDate(endDate);
				}

				if(recordTime.compareTo(endDate) >= 0) {

					hourlyData.setData(packetStatDataList.get(i - 1));
					hourlyDataList.add(hourlyData);
					hourlyData = new MISHourlyData(responseData.getServiceType(), Integer.parseInt(responseData.getServiceId()), responseData.getServiceName());
					// Set dataList(size -1) record in new Hour data before initializing it

					Calendar cal = new GregorianCalendar();
					cal.setTime(recordTime);
					Calendar startCal = cal;
					startCal.set(Calendar.MINUTE, 0);
					startCal.set(Calendar.SECOND, 0);
					startCal.set(Calendar.MILLISECOND, 0);
					startDate = startCal.getTime();
					Calendar endCal = startCal;
					endCal.add(Calendar.HOUR, 1);
					endDate = endCal.getTime();
					hourlyData.setStartDate(startDate);
					hourlyData.setEndDate(endDate);
					hourlyData.setLastHourRecord(packetStatDataList.get(i - 1));
				}

				if(i == packetStatDataList.size() - 1) {
					hourlyData.setData(packetStatDataList.get(i));
					hourlyDataList.add(hourlyData);
				}

			}
			getDataList(hourlyDataList,callDetail,reportDataList);
		}
		List<MISReportData> dataList1 = new ArrayList<>();
		dataList1.addAll(reportDataList);
		Collections.sort(dataList1);
		return reportDataList;
	}
	/**
	 * Creates a list of type ReportData on every loop iteration over callResponseData received 
	 * in method getReportData()
	 * @param hourlyDataList
	 * @param callDetail
	 * @param reportDataList
	 */
	private static void getDataList(List<MISHourlyData> hourlyDataList,MISDetail callDetail,Set<MISReportData> reportDataList){
		Collections.sort(hourlyDataList);
		for(MISHourlyData data : hourlyDataList) {
			MISReportData reportData = new MISReportData();

			Long receivedPackets = 0L;
			Long successPackets = 0L;
			Long droppedPackets = 0L;
			Long receivedFiles = 0L;
			Long successFiles = 0L;
			Long failFiles = 0L;
			Long totalRecords = 0L;
			Long successRecords = 0L;
			Long failRecords = 0L;

			Long lastReceivedPackets = 0L;
			Long lastDroppedPackets = 0L;
			Long lastReceivedFiles = 0L;
			Long lastFailFiles = 0L;
			Long lastTotalRecords = 0L;
			Long lastSuccessRecords = 0L;
			Long lastFailRecords = 0L;

			Double droppedPacketsPercentage = 0.0;
			Double failedFilesPercentage = 0.0;
			Double failedRecordsPercentage = 0.0;

			reportData.setServiceId(data.getServiceId());
			reportData.setServiceName(data.getServiceName());
			reportData.setServiceType(data.getServiceType());

			try {

				if(!(data.getLastHourRecord().isEmpty())) {
					Calendar cal = new GregorianCalendar();
					cal.setTime(data.getStartDate());

					JSONObject lastObj = new JSONObject(data.getLastHourRecord());

					Calendar lastRecordCal = new GregorianCalendar();
					lastRecordCal.setTime(DateFormatter.stringToDate(lastObj.getString("RECORD_TIME"), DATE_FORMAT));

					if (cal.get(Calendar.DATE) == lastRecordCal.get(Calendar.DATE))
					{
						//MED-4621 - added below check since Key is different for GTP collection service
						if (StringUtils.equals(data.getServiceType(), BaseConstants.GTPPRIME_COLLECTION_SERVICE_ALIAS)) {
							lastReceivedPackets = lastObj.getLong("TOTAL_REQUEST");
							lastDroppedPackets = lastObj.getLong("TOTAL_DROPPED_REQUEST");
						} else if (isServicePacketBased(data.getServiceType())) {
							lastReceivedPackets = lastObj.getLong("TOTAL_RECIEVED_PACKETS");
							lastDroppedPackets = lastObj.getLong("TOTAL_PACKETS_DROPPED");
						} else {
							lastReceivedFiles = lastObj.getLong("TOTAL_FILES");
							lastFailFiles = lastObj.getLong("TOTAL_FAILED_FILES");
							lastTotalRecords = lastObj.getLong("TOTAL_RECORDS_RECIEVED");
							lastFailRecords = lastObj.getLong("TOTAL_FAILED_RECORDS");
							lastSuccessRecords = lastObj.getLong("TOTAL_SUCCESS_RECORDS");
						}
					}
				}
				JSONObject obj = new JSONObject(data.getData());

				//MED-4621 - added below check since Key is different for GTP collection service
				if (StringUtils.equals(data.getServiceType(), BaseConstants.GTPPRIME_COLLECTION_SERVICE_ALIAS)) {
					receivedPackets = obj.getLong("TOTAL_REQUEST");
					droppedPackets = obj.getLong("TOTAL_DROPPED_REQUEST");
				} else if (isServicePacketBased(data.getServiceType())) {
					receivedPackets = obj.getLong("TOTAL_RECIEVED_PACKETS") - lastReceivedPackets;
					droppedPackets = obj.getLong("TOTAL_PACKETS_DROPPED") - lastDroppedPackets;
				} else {
					receivedFiles = obj.getLong("TOTAL_FILES") - lastReceivedFiles;
					failFiles = obj.getLong("TOTAL_FAILED_FILES") - lastFailFiles;
					totalRecords = obj.getLong("TOTAL_RECORDS_RECIEVED") - lastTotalRecords;
					failRecords = obj.getLong("TOTAL_FAILED_RECORDS") - lastFailRecords;
					successRecords = obj.getLong("TOTAL_SUCCESS_RECORDS") - lastSuccessRecords;
				}
				successPackets = receivedPackets - droppedPackets;
				successFiles = receivedFiles - failFiles;
				droppedPacketsPercentage = Math.round((droppedPackets * 10000.0) / receivedPackets) / 100.0;
				failedFilesPercentage = Math.round((failFiles * 10000.0) / receivedFiles) / 100.0;
				failedRecordsPercentage = Math.round((failRecords * 10000.0) / totalRecords) / 100.0;
			} catch(JSONException e) {
				logger.warn("Error in parsing JSON data: " + e.getMessage(),e);
			} catch(NumberFormatException  | ArithmeticException e ) {
				logger.warn("Error in processing JSON data: " + e.getMessage(),e);
			}
			reportData.setDroppedPackets(droppedPackets);
			reportData.setDroppedPacketsPercentage(droppedPacketsPercentage);
			reportData.setFailedFilesPercentage(failedFilesPercentage);
			reportData.setFailedRecordsPercentage(failedRecordsPercentage);
			reportData.setFailFiles(failFiles);
			reportData.setFailRecords(failRecords);
			reportData.setReceivedFiles(receivedFiles);
			reportData.setReceivedPackets(receivedPackets);
			reportData.setReportStartTime(new Timestamp(data.getStartDate().getTime()));
			reportData.setReportEndTime(new Timestamp(data.getEndDate().getTime()));
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(data.getStartDate()); 
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);  
			cal.set(Calendar.SECOND, 0);  
			cal.set(Calendar.MILLISECOND, 0);
			reportData.setReportStartDate(cal.getTime());
			reportData.setSuccessFiles(successFiles);
			reportData.setSuccessPackets(successPackets);
			reportData.setSuccessRecords(successRecords);
			reportData.setTotalRecords(totalRecords);
			reportData.setReportCallDetail(callDetail);

			reportDataList.add(reportData);
		}
	}
	/**
	 * Checks if service is service based or packet based
	 * @param serviceAlias
	 * @return
	 */
	public static boolean isServicePacketBased(String serviceAlias) {
		if(StringUtils.equals(serviceAlias, BaseConstants.RADIUS_COLLECTION_SERVICE_ALIAS) ||
				StringUtils.equals(serviceAlias, BaseConstants.GTPPRIME_COLLECTION_SERVICE_ALIAS) ||
				StringUtils.equals(serviceAlias, BaseConstants.NATFLOW_COLLECTION_SERVICE_ALIAS) ||
				StringUtils.equals(serviceAlias, BaseConstants.NATFLOWBINARY_COLLECTION_SERVICE_ALIAS) ||
				StringUtils.equals(serviceAlias, BaseConstants.SYSLOG_COLLECTION_SERVICE_ALIAS) ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Generates Detail Data for Grid/Download
	 * @param dataList
	 * @param serviceType
	 * @return
	 */
	public static List<List<String>> getStaticServiceWiseDetailReportList(List<MISReportTableData> dataList, ReportTypeParameters reportTypeParams) {

		String serviceType = reportTypeParams.getServiceInstanceId();
		String reportType = reportTypeParams.getReportType();
		Date startDate = reportTypeParams.getStartDate();
		Date endDate = reportTypeParams.getEndDate();

		List<List<String>> returnList = new ArrayList<>();
		List<String> dataRow = new ArrayList<>();

		dataRow.add(BaseConstants.SERIAL_NO);
		if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.HOURLY)) {
			dataRow.add(BaseConstants.HOUR);
		} else if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.DAILY)) {
			dataRow.add(BaseConstants.DATE);
		} else  {
			dataRow.add(BaseConstants.MONTH);
		}
		addHeader(serviceType,dataRow);
		returnList.add(dataRow);
		List<MISReportTableData> tableDataList = new ArrayList<>();
		if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.HOURLY)) {

			for(int hour = 0; hour < 24; hour++) {
				MISReportTableData data;
				if(!dataList.isEmpty()) {
					data = dataList.get(0);
					if(hour != data.getHour()) {
						data = new MISReportTableData();
						data.setHour(hour);
					} else {
						dataList.remove(0);
					}
				} else {
					data = new MISReportTableData();
					data.setHour(hour);
				}
				tableDataList.add(data);
			}

		} else if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.DAILY)) {

			Calendar cal = Calendar.getInstance();
			Date date = startDate;
			cal.setTime(date);
			while(true) {
				MISReportTableData data;
				if(!dataList.isEmpty()) {
					data = dataList.get(0);
					if(cal.get(Calendar.DATE) != data.getDate() || cal.get(Calendar.MONTH) != data.getMonth()) {
						data = new MISReportTableData();
						data.setDate(cal.get(Calendar.DATE));
						data.setMonth(cal.get(Calendar.MONTH));
					} else {
						dataList.remove(0);
					}
				} else {
					data = new MISReportTableData();
					data.setDate(cal.get(Calendar.DATE));
					data.setMonth(cal.get(Calendar.MONTH));
				}
				tableDataList.add(data);
				cal.add(Calendar.DATE, 1);
				if(cal.getTime().after(endDate)) {
					break;
				}
			}
		} else  {
			Calendar cal = Calendar.getInstance();
			Date date = startDate;
			cal.setTime(date);
			while(true) {
				MISReportTableData data;
				if(!dataList.isEmpty()) {
					data = dataList.get(0);
					if(cal.get(Calendar.MONTH) != data.getMonth() || cal.get(Calendar.YEAR) != data.getYear()) {
						data = new MISReportTableData();
						data.setMonth(cal.get(Calendar.MONTH));
						data.setYear(cal.get(Calendar.YEAR));
					} else {
						dataList.remove(0);
					}
				} else {
					data = new MISReportTableData();
					data.setMonth(cal.get(Calendar.MONTH));
					data.setYear(cal.get(Calendar.YEAR));
				}
				tableDataList.add(data);
				cal.add(Calendar.MONTH, 1);
				if(cal.getTime().after(endDate)) {
					break;
				}
			}
		}

		int counter = 1;
		for(MISReportTableData data : tableDataList) {
			dataRow = new ArrayList<>();
			dataRow.add(Integer.toString(counter)+"");
			if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.HOURLY)) {
				dataRow.add(getHourStr(data.getHour()));
			} else if(StringUtils.equalsIgnoreCase(reportType, BaseConstants.DAILY)) {
				dataRow.add(getDateStr(data.getDate(), data.getMonth()));
			} else  {
				dataRow.add(getMonthStr(data.getMonth(), data.getYear()));
			}
			addData(serviceType,dataRow,data);
			returnList.add(dataRow);
			counter++;
		}
		return returnList;
	}

	/**
	 * Converts Hour value to String Format
	 * @param hour
	 * @return
	 */
	public static String getHourStr(int hour) {
		return String.format("%02d", hour) + ":00";
	}

	/**
	 * Converts Date value to String Format
	 * @param hour
	 * @return
	 */
	public static String getDateStr(int date, int month) {
		return date + "-" + new DateFormatSymbols().getShortMonths()[month];
	}
	/**
	 * Converts Month value to String Format
	 * @param hour
	 * @return
	 */
	public static String getMonthStr(int month, int year) {
		return new DateFormatSymbols().getShortMonths()[month] + "-" + year;
	}

	/**
	 * returns the passed field value from the calendar
	 * @param date
	 * @param field
	 * @return
	 */
	public static int getCalendarField(Date date, int field) {
		if(date == null) {
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(field);
	}

	/**
	 * Returns last day of the month of a particular year
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getLastDayOfMonth(int month, int year) {
		Calendar cal = new GregorianCalendar(year, month, 1);

		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

	}
	/**
	 * Checks the difference between the two dates passed as argument
	 * @param startDate
	 * @param endDate
	 * @param field
	 * @param duration
	 * @return
	 */
	public static boolean checkDuration(Date startDate, Date endDate, int field, int duration)
	{
		Calendar startCal = new GregorianCalendar();
		Calendar endCal = new GregorianCalendar();
		startCal.setTime(startDate);
		endCal.setTime(endDate);
		if(field == Calendar.MONTH)
		{
			startCal.add(Calendar.MONTH, duration);
		}
		else
		{
			startCal.add(Calendar.YEAR, duration);
		}
		if(endCal.getTime().getTime() >= startCal.getTime().getTime())
		{
			return false;
		}
		return true;
	}
	/**
	 * Converts date passed to the dateFormat provided
	 * @param date
	 * @param dateFormat
	 * @return
	 */
	public static String getSystemFormattedDate(Date date, String dateFormat)
	{
		if(date == null)
		{
			return StringUtils.EMPTY;
		}
		else
		{
			String format = dateFormat;
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			return formatter.format(date);
		}
	}

	/**
	 * Creates Header for the MIS report on the basis of service type
	 * @param serviceType
	 * @param dataRow
	 * @param data
	 */
	private static void addHeader(String serviceType,List<String> dataRow){
		if(isServicePacketBased(serviceType)) {
			dataRow.add(BaseConstants.TOTAL_PACKETS);
			dataRow.add(BaseConstants.SUCCESS_PACKETS);
			dataRow.add(BaseConstants.MALFORMED_PACKETS);
			dataRow.add(BaseConstants.MALFORMED_PACKETS_PERCENTAGE);

		} else {
			dataRow.add(BaseConstants.TOTAL_FILES);
			dataRow.add(BaseConstants.SUCCESS_FILES);
			dataRow.add(BaseConstants.FAIL_FILES);
			dataRow.add(BaseConstants.FAIL_FILES_PERCENTAGE);
		}

		if(!StringUtils.equals(serviceType, BaseConstants.COLLECTION_SERVICE)) {
			dataRow.add(BaseConstants.TOTAL_RECORDS);
			dataRow.add(BaseConstants.SUCCESS_RECORDS);
			dataRow.add(BaseConstants.FAIL_RECORDS);
			dataRow.add(BaseConstants.FAIL_RECORDS_PERCENTAGE);
		}
	}

	/**
	 * Creates Data for the MIS report on the basis of service type
	 * @param serviceType
	 * @param dataRow
	 * @param data
	 */
	private static void addData(String serviceType, List<String> dataRow,MISReportTableData data){
		if(isServicePacketBased(serviceType)) {
			dataRow.add(data.getReceivedPackets() + "");
			dataRow.add(data.getSuccessPackets() + "");
			dataRow.add(data.getDroppedPackets() + "");
			dataRow.add(data.getDroppedPacketsPercentage() + "");

		} else {
			dataRow.add(data.getReceivedFiles() + "");
			dataRow.add(data.getSuccessFiles() + "");
			dataRow.add(data.getFailFiles() + "");
			dataRow.add(data.getFailedFilesPercentage() + "");
		}

		if(!StringUtils.equalsIgnoreCase(serviceType, BaseConstants.COLLECTION_SERVICE)) {
			dataRow.add(data.getTotalRecords() + "");
			dataRow.add(data.getSuccessRecords() + "");
			dataRow.add(data.getFailRecords() + "");
			dataRow.add(data.getFailedRecordsPercentage() + "");
		}
	}

	/**
	 * Generates Summary Data for Grid/Download
	 * @param dataList
	 * @param serviceType
	 * @return
	 */
	public static List<List<String>> getStaticServiceWiseSummaryReportList(List<MISReportTableData> dataList, String serviceType) {

		List<List<String>> returnList = new ArrayList<>();
		List<String> dataRow = new ArrayList<>();

		dataRow.add(BaseConstants.SERIAL_NO);
		dataRow.add(BaseConstants.SERVER_INSTANCE);
		dataRow.add(BaseConstants.SERVICE_INSTANCE);
		addHeader(serviceType,dataRow);
		returnList.add(dataRow);
		int counter = 1;
		for(MISReportTableData data : dataList) {
			dataRow = new ArrayList<>();
			dataRow.add(Integer.toString(counter) + "");
			dataRow.add(data.getServerName());
			dataRow.add(data.getServiceName());
			addData(serviceType,dataRow,data);
			returnList.add(dataRow);
			counter++;
		}
		return returnList;
	}
}