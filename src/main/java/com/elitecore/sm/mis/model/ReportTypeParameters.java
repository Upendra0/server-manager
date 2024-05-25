package com.elitecore.sm.mis.model;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Neha Kochhar
 * Used to store Form values and pass to controller for 
 * serviceWiseSummary.jsp and serviceWiseDetail.jsp
 *
 */
public class ReportTypeParameters {		
	private String reportType;
	private String serverInstancelst;
	private String serviceInstanceId;
	private String dailyMonth;
	private String dailyYear;
	private String hourlyDate;
	private Date startDate;
	private Date endDate;
	private int dailyDuration;
	private String dailyCustomToDate;
	private String dailyCustomFromDate;
	private int monthlyDuration;
	private String monthlyYear;
	private String monthlyStartMonth;
	private String monthlyEndMonth;
	private String monthlyStartYear;
	private String monthlyEndYear;
	private List<String> headerList;
	private List<List<String>> dataList;
	private Date dailyReportStartDate;
	private Date dailyReportEndDate;
	private Date hourlyReportDate;
	private Date monthlyReportStartDate;
	private Date monthlyReportEndDate;
	private long rowCount;
	private String serverInstanceId;

	/**
	 * Stores Date for Hourly report
	 * @return
	 */
	public String getHourlyDate() {
		return hourlyDate;
	}
	public void setHourlyDate(String hourlyDate) {
		this.hourlyDate = hourlyDate;
	}

	/**
	 * Stores Report Type
	 * @return
	 */
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * Stores the multiple servers chosen by user on Summary page
	 * @return
	 */
	public String getServerInstancelst() {
		return serverInstancelst;
	}
	public void setServerInstancelst(String serverInstancelst) {
		this.serverInstancelst = serverInstancelst;
	}

	/**
	 * Stores Start Month for Monthly report
	 * @return
	 */
	public String getMonthlyStartMonth() {
		return monthlyStartMonth;
	}
	public void setMonthlyStartMonth(String monthlyStartMonth) {
		this.monthlyStartMonth = monthlyStartMonth;
	}

	/**
	 * Stores End Month for Monthly report
	 * @return
	 */
	public String getMonthlyEndMonth() {
		return monthlyEndMonth;
	}
	public void setMonthlyEndMonth(String monthlyEndMonth) {
		this.monthlyEndMonth = monthlyEndMonth;
	}

	/**
	 * Stores Start Year for Monthly report
	 * @return
	 */
	public String getMonthlyStartYear() {
		return monthlyStartYear;
	}
	public void setMonthlyStartYear(String monthlyStartYear) {
		this.monthlyStartYear = monthlyStartYear;
	}

	/**
	 * Stores End Year for Monthly report
	 * @return
	 */
	public String getMonthlyEndYear() {
		return monthlyEndYear;
	}
	public void setMonthlyEndYear(String monthlyEndYear) {
		this.monthlyEndYear = monthlyEndYear;
	}

	/**
	 * Stores Service Instance Id chosen by user on both report jsp pages
	 * @return
	 */
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}
	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	/**
	 * Stores Daily Month for the 
	 * @return
	 */
	public String getDailyMonth() {
		return dailyMonth;
	}
	public void setDailyMonth(String month) {
		this.dailyMonth = month;
	}

	/**
	 * Stores year for Daily Report
	 * @return
	 */
	public String getDailyYear() {
		return dailyYear;
	}
	public String getDailyCustomToDate() {
		return dailyCustomToDate;
	}

	/**
	 * Stores Custom To-Date for Daily Report
	 * @param dailyCustomToDate
	 */
	public void setDailyCustomToDate(String dailyCustomToDate) {
		this.dailyCustomToDate = dailyCustomToDate;
	}
	public String getDailyCustomFromDate() {
		return dailyCustomFromDate;
	}

	/**
	 * Stores custom-from date for daily report
	 * @param dailyCustomFromDate
	 */
	public void setDailyCustomFromDate(String dailyCustomFromDate) {
		this.dailyCustomFromDate = dailyCustomFromDate;
	}
	public void setDailyYear(String year) {
		this.dailyYear = year;
	}

	/**
	 * Stores final computed startDate on the basis of input provided by the user
	 * for all types of reports
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Stores final computed endDate on the basis of input provided by the user
	 * for all types of reports 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * Stores Duration value for Daily(Select/Custom)
	 * @return
	 */
	public int getDailyDuration() {
		return dailyDuration;
	}
	public void setDailyDuration(int dailyDuration) {
		this.dailyDuration = dailyDuration;
	}
	/**
	 * Stores Duration value for Monthly(Select/Custom)
	 * @return
	 */
	public int getMonthlyDuration() {
		return monthlyDuration;
	}
	public void setMonthlyDuration(int monthlyDuration) {
		this.monthlyDuration = monthlyDuration;
	}
	/**
	 * Stores year for monthly report
	 * @return
	 */
	public String getMonthlyYear() {
		return monthlyYear;
	}
	public void setMonthlyYear(String monthlyYear) {
		this.monthlyYear = monthlyYear;
	}
	/**
	 * Stores header list for different reports on basis of 
	 * file based/packet based services
	 * @return
	 */
	public List<String> getHeaderList() {
		return headerList;
	}
	public void setHeaderList(List<String> headerList) {
		this.headerList = headerList;
	}
	/**
	 * Stores the Data list for different reports on basis of
	 * file based/ packet based services
	 * @return
	 */
	public List<List<String>> getDataList() {
		return dataList;
	}
	public void setDataList(List<List<String>> dataList) {
		this.dataList = dataList;
	}
	/**
	 * Stores start date for Daily report
	 * @return
	 */
	public Date getDailyReportStartDate() {
		return dailyReportStartDate;
	}
	public void setDailyReportStartDate(Date dailyReportStartDate) {
		this.dailyReportStartDate = dailyReportStartDate;
	}
	/**
	 * Stores end date for daily report
	 * @return
	 */
	public Date getDailyReportEndDate() {
		return dailyReportEndDate;
	}
	public void setDailyReportEndDate(Date dailyReportEndDate) {
		this.dailyReportEndDate = dailyReportEndDate;
	}
	/**
	 * stores hourly report date
	 * @return
	 */
	public Date getHourlyReportDate() {
		return hourlyReportDate;
	}
	public void setHourlyReportDate(Date hourlyReportDate) {
		this.hourlyReportDate = hourlyReportDate;
	}
	/**
	 * Stores monthly report start date
	 * @return
	 */
	public Date getMonthlyReportStartDate() {
		return monthlyReportStartDate;
	}
	public void setMonthlyReportStartDate(Date monthlyReportStartDate) {
		this.monthlyReportStartDate = monthlyReportStartDate;
	}
	/**
	 * Stores monthly report end date
	 * @return
	 */
	public Date getMonthlyReportEndDate() {
		return monthlyReportEndDate;
	}
	public void setMonthlyReportEndDate(Date monthlyReportEndDate) {
		this.monthlyReportEndDate = monthlyReportEndDate;
	}
	/**
	 * Stores row count for the total number of pages
	 * @return
	 */
	public long getRowCount() {
		return rowCount;
	}
	public void setRowCount(long rowCount) {
		this.rowCount = rowCount;
	}
	/**
	 * Stores ServerInstance id chosen by user for Service Wise Detail Report
	 * @return
	 */
	public String getServerInstanceId() {
		return serverInstanceId;
	}
	public void setServerInstanceId(String serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

}
