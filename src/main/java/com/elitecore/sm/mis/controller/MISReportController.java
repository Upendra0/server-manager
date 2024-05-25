package com.elitecore.sm.mis.controller;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.commons.util.data.MISReportData;
import com.elitecore.core.commons.util.report.MISExcelReportUtils;
import com.elitecore.core.commons.util.report.MISPDFReportsUtils;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.MisReportTypeEnum;
import com.elitecore.sm.common.model.MonthEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.MISReportUtils;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.mis.dao.MISReportDataDao;
import com.elitecore.sm.mis.model.ReportTypeParameters;
import com.elitecore.sm.mis.service.MISReportService;
import com.elitecore.sm.mis.service.MISReportTableData;
import com.elitecore.sm.mis.validator.MISReportValidator;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;

/*
 * 
 * Neha Kochhar
 */

@Controller
public class MISReportController extends BaseController {

	@Autowired
	private MISReportService mISReportService;

	@Autowired
	private MISReportDataDao mISReportDataDao;

	@Autowired
	private ServerInstanceService serverInstanceService;

	@Autowired
	private MISReportValidator validator;

	private static final  String DATEFORMATSTRING = "MM/dd/yyyy";

	private DateFormat formatter = new SimpleDateFormat(DATEFORMATSTRING);

	private static final int STARTINGYEAR = 2011;

	/**
	 * MIS Report manager
	 * 
	 * @return ModelAndView
	 */
	@PreAuthorize("hasAnyAuthority('MIS_REPORTS_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_MIS_REPORT_MANAGER, method = RequestMethod.GET)
	public ModelAndView initConfigurationManager(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false) String requestActionType) {
		ModelAndView model = new ModelAndView(
				ViewNameConstants.INIT_MIS_REPORT_MANAGER);

		// Request Action type is not null when tabs already visited once
		if (requestActionType != null) {
			if (requestActionType.equals(BaseConstants.SERVICE_WISE_SUMMARY)) {
				getDataForSummary(model);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
				model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
				model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
			} else if (requestActionType.equals(BaseConstants.SERVICE_WISE_DETAIL)) {
				getDataForDetail(model);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
				model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
				model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
				getYearListForMis(model);
			} else {
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
			}

		} // Request Action type is null , when click on left menu
		else {
			// If user has individual tab rights
			if (eliteUtils.isAuthorityGranted(BaseConstants.SERVICE_WISE_SUMMARY)) {
				getDataForSummary(model);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVICE_WISE_SUMMARY);
				model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
				model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
			} else if (eliteUtils.isAuthorityGranted(BaseConstants.SERVICE_WISE_DETAIL)) {
				getDataForDetail(model);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVICE_WISE_DETAIL);
				model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
				model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
				getYearListForMis(model);
			}
		}
		model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG, false);
		return model;
	}

	/**
	 * Get list of all services from DB for summary page
	 * @param model
	 */
	@SuppressWarnings("unchecked")
	private void getDataForSummary(ModelAndView model) {

		List<ServiceType> serviceTypeList = new ArrayList<>();
		List<ServerType> activeServerTypeList = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);

		if(activeServerTypeList != null && !activeServerTypeList.isEmpty()){
			for (ServerType serverType : activeServerTypeList) {
				List<ServiceType> mainServiceTypeList = (List<ServiceType>) eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),
						BaseConstants.MAIN_SERVICE_TYPE);
				List<ServiceType> additionalServiceTypeList=(List<ServiceType>)eliteUtils.fetchProfileEntityStatusFromCache(serverType.getId(),
						BaseConstants.ADDITIONAL_SERVICE_TYPE);

				for (ServiceType serviceType : mainServiceTypeList) {
					if (!serviceTypeList.contains(serviceType)) {
						serviceTypeList.add(serviceType);
					}
				}
				//display additional services
				for(ServiceType serviceType:additionalServiceTypeList)
				{	if(!serviceTypeList.contains(serviceType)){
					serviceTypeList.add(serviceType); 
				} 
				}
			}
			model.addObject(BaseConstants.SERVICE_TYPE_LIST, serviceTypeList);
		}
		getYearListForMis(model);
	}

	/**
	 * Gets total number of years from 2011 to current year
	 * @param model
	 */
	private void getYearListForMis(ModelAndView model) {
		Calendar today = new GregorianCalendar();
		today.setTime(new Date());
		List<String> years = new ArrayList<>();
		for (int i = STARTINGYEAR; i <= today.get(Calendar.YEAR); i++) {
			years.add(Integer.toString(i));
		}
		model.addObject("years", years);
	}

	/**
	 * Gets list of servers which have configured serviceAlias type service
	 * @param serviceAlias
	 * @return ajaxReponse
	 */
	@RequestMapping(value = ControllerConstants.MIS_GET_SERVER_LIST_FOR_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String misGetServerListForService(
			@RequestParam(value = "serviceAlias") String serviceAlias) {
		AjaxResponse ajaxResponse;
		ResponseObject responseObject;
		responseObject = mISReportService.getServiceListByAlias(serviceAlias);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	/**
	 * Downloads report in format specified in fileType(pdf/excel) for summary page
	 * @param fileType
	 * @param loggedInStaffName
	 * @param reportTypeParams
	 * @param response
	 * @return ModelAndView
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('DOWNLOAD_SERVICE_WISE_SUMMARY_REPORT')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_MIS_REPORT, method = RequestMethod.POST)
	public ModelAndView downloadMISReport(
			@RequestParam(value = "fileType", required = true) String fileType,
			ReportTypeParameters reportTypeParams, HttpServletResponse response, HttpServletRequest request)
	{
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.INIT_MIS_REPORT_MANAGER);
		// Get all Data from DB and set into ReportTypeParameters Object
		getMISReportDataFromDB(reportTypeParams);
		if ((reportTypeParams.getDataList() == null) ||  reportTypeParams.getDataList().isEmpty()){
			getDataForSummary(model);
			model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
			model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
			model.addObject(BaseConstants.ERROR_MSG,"No Result Found!!");
		}else {
			getDataForDownload(reportTypeParams, response, request , fileType, false);
			return null; //TO AVOID RESPONSE COMMIT ERROR. We are stopping here to spring for changing response through view resolver.
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVICE_WISE_SUMMARY);
		return model;
	}
	/**
	 * Method to collect all the data for the report to be downloaded
	 * @param reportTypeParams
	 * @param response
	 * @param request
	 * @param fileType
	 */
	private void getDataForDownload(ReportTypeParameters reportTypeParams, HttpServletResponse response, HttpServletRequest request ,String fileType,Boolean detailFlag){
		MISReportData reportData = new MISReportData();
		boolean isPDF = StringUtils.equals(BaseConstants.PDF_FILE_TYPE,fileType);
		String loggedInStaffName=eliteUtils.getUserNameOfUser(request);
		//Get the data to pass to the core server to generate the report
		if (reportTypeParams.getDataList() != null && !reportTypeParams.getDataList().isEmpty()) {
			List<String> paramList = new ArrayList<>();
			paramList.add("Service Type : " + reportTypeParams.getServiceInstanceId());
			if (BaseConstants.HOURLY.equals(reportTypeParams.getReportType())) {
				reportTypeParams.setStartDate(reportTypeParams.getHourlyReportDate());
				reportTypeParams.setEndDate(null);
			}else if (BaseConstants.DAILY.equals(reportTypeParams.getReportType())) {
				reportTypeParams.setStartDate(reportTypeParams.getDailyReportStartDate());
				reportTypeParams.setEndDate(reportTypeParams.getDailyReportEndDate());
			}else if (BaseConstants.MONTHLY.equals(reportTypeParams.getReportType())) {
				reportTypeParams.setStartDate(reportTypeParams.getMonthlyReportStartDate());
				reportTypeParams.setEndDate(reportTypeParams.getMonthlyReportEndDate());
			}
			reportData = mISReportService.createReportDataForDownload(reportTypeParams, loggedInStaffName,detailFlag);
		}
		downloadFile(reportData, response, isPDF);
	}
	/**
	 * Downloads report in format specified in fileType(pdf/excel) for Detail page
	 * @param fileType
	 * @param loggedInStaffName
	 * @param reportTypeParams
	 * @param response
	 * @return ModelAndView
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('DOWNLOAD_SERVICE_WISE_DETAIL_REPORT')")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_MIS_REPORT_IN_DETAIL, method = RequestMethod.POST)
	public ModelAndView downloadMISReportInDetail(
			@RequestParam(value = "fileType", required = true) String fileType,
			ReportTypeParameters reportTypeParams, HttpServletResponse response, HttpServletRequest request)
	{	boolean detailFlag=true;
		ModelAndView model = new ModelAndView();
		model.setViewName(ViewNameConstants.INIT_MIS_REPORT_MANAGER);

		// Get all Data from DB and set to ReportTYpe Parameters object
		getMISReportDataFromDBInDetail(reportTypeParams);

		if ((reportTypeParams.getDataList() == null) ||  reportTypeParams.getDataList().isEmpty()){
			getDataForDetail(model);
			model.addObject(BaseConstants.MISREPORTTYPEENUM,Arrays.asList(MisReportTypeEnum.values()));
			model.addObject(BaseConstants.MONTHENUM, Arrays.asList(MonthEnum.values()));
			getYearListForMis(model);
			model.addObject(BaseConstants.ERROR_MSG,"No Result Found!!");
		}else {
			getDataForDownload(reportTypeParams, response, request , fileType, detailFlag);
			return null; //TO AVOID RESPONSE COMMIT ERROR. We are stopping here to spring for changing response through view resolver.
		}
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVICE_WISE_DETAIL);
		return model;
	}

	/**
	 * Gets all the MISReportType Data into a file format and downloads the same.
	 * @param reportData
	 * @param response
	 * @param isPDF
	 */
	private void downloadFile(MISReportData reportData, HttpServletResponse response, boolean isPDF){
		File file;
		if (isPDF) {
			file = MISPDFReportsUtils.generatePDFReport(reportData);
				logger.info(StringUtils.replace(reportData.getReportName(), " ", StringUtils.EMPTY) + "_" + System.currentTimeMillis() + ".pdf");
			if (file != null) {
				response.setHeader(BaseConstants.MIS_DOWNLOAD_HEADER_STRING_1,BaseConstants.MIS_DOWNLOAD_HEADER_STRING_2+ reportData.getReportName()+ "_" + System.currentTimeMillis() + ".pdf");
				response.setContentType("application/pdf");
				response.setContentLength((int) file.length());
			}
		} else {
			file = MISExcelReportUtils.generateExcelReport(reportData);
			if (file != null) {
				response.setHeader(BaseConstants.MIS_DOWNLOAD_HEADER_STRING_1,BaseConstants.MIS_DOWNLOAD_HEADER_STRING_2+ reportData.getReportName()+ "_" + System.currentTimeMillis() + ".xls");
				response.setContentType("application/vnd.ms-excel");
				response.setContentLength((int) file.length());
			}
		}
		try(FileInputStream fileInputStream = new FileInputStream(file);DataInputStream in = new DataInputStream(fileInputStream);ServletOutputStream outStream = response.getOutputStream())
		{
			byte[] byteBuffer = new byte[4096];
			int length;
			while ((length = in.read(byteBuffer)) != -1)  {
				outStream.write(byteBuffer, 0, length);
			}
		}catch (Exception e) {
			logger.error("Exception Occured during download sample file " + e);
		}
	}

	/**
	 * On the basis of passed input parameters from jsp, it creates startDate 
	 * and endDate for all types of reports and provides the data to DB query.
	 * 
	 * @param reportTypeParams
	 */
	private void getMISReportDataFromDB(ReportTypeParameters reportTypeParams) {
		List<String> serverInstanceList = new ArrayList<>();
		String[] serverInstanceValues;
		if(reportTypeParams.getServerInstancelst() != null) {
			serverInstanceValues = reportTypeParams.getServerInstancelst().split(",");
			for (int i = 0, len = serverInstanceValues.length; i < len; i++) {
				serverInstanceList.add(serverInstanceValues[i]);
			}
			if (StringUtils.equals(BaseConstants.DAILY,reportTypeParams.getReportType())) {
				getStartDateAndEndDateForDaily(reportTypeParams);
				getServerSummaryForReport(reportTypeParams, serverInstanceList);
			}else if (StringUtils.equals(BaseConstants.HOURLY,reportTypeParams.getReportType())) {
				getServerSummaryForHourly(reportTypeParams, serverInstanceList);
			}else if (StringUtils.equals(BaseConstants.MONTHLY,reportTypeParams.getReportType())){
				getStartDateAndEndDateForMonthly(reportTypeParams);
				getServerSummaryForReport(reportTypeParams, serverInstanceList);
			}
		}
	}

	/**
	 * Views the requested time report data from DB on jqGrid for all packet based services on summary page
	 * @param reportTypeParams
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('GENERATE_SERVICE_WISE_SUMMARY_REPORT')")
	@RequestMapping(value = ControllerConstants.MIS_GET_SERVER_SUMMARY_LIST_FOR_PACKET_BASED_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String misGetServerSummaryListForPacketBasedService(ReportTypeParameters reportTypeParams) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();

		validator.validateReportParameters(importErrorList, reportTypeParams);
		if (!importErrorList.isEmpty()) {
			ajaxResponse = getErrorMsgList(importErrorList,ajaxResponse);
		}
		else{
			List<String> serverInstanceList = new ArrayList<>();
			String[] serverInstanceValues = reportTypeParams.getServerInstancelst().split(",");
			for (int i = 0, len = serverInstanceValues.length; i < len; i++) {
				serverInstanceList.add(serverInstanceValues[i]);
			}
			getMISReportDataFromDB(reportTypeParams);
			// jqgrid data
			Map<String, Object> row;
			List<Map<String, Object>> rowList = new ArrayList<>();
			if(reportTypeParams.getDataList()!=null && !reportTypeParams.getDataList().isEmpty()){
				ListIterator<List<String>> itr=reportTypeParams.getDataList().listIterator();  
				while(itr.hasNext()){	
					int index=0;
					row = new HashMap<>();
					List<String> reportResult = itr.next();
					row.put(BaseConstants.SRNO_GRID, reportResult.get(index++));
					row.put(BaseConstants.SERVERNAME_GRID,reportResult.get(index++));
					row.put(BaseConstants.SERVICENAME_GRID,reportResult.get(index++));
					row = getDataInRowsForPacketBasedServices(row,reportResult,index);
					rowList.add(row);
				}
				responseObject.setObject(rowList);
			}
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}	
		return ajaxResponse.toString();
	}

	/**
	 * Views the requested time report data from DB on jqGrid for all file based services on summary page
	 * @param reportTypeParams
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('GENERATE_SERVICE_WISE_SUMMARY_REPORT')")
	@RequestMapping(value = ControllerConstants.MIS_GET_SERVER_SUMMARY_LIST_FOR_FILE_BASED_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String misGetServerSummaryListForFileBasedService(
			ReportTypeParameters reportTypeParams) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();

		validator.validateReportParameters(importErrorList, reportTypeParams);
		if (!importErrorList.isEmpty()) {
			ajaxResponse = getErrorMsgList(importErrorList,ajaxResponse);
		}
		else{
			List<String> serverInstanceList = new ArrayList<>();
			String[] serverInstanceValues = reportTypeParams.getServerInstancelst()
					.split(",");
			for (int i = 0, len = serverInstanceValues.length; i < len; i++) {
				serverInstanceList.add(serverInstanceValues[i]);
			}
			getMISReportDataFromDB(reportTypeParams);
			Map<String, Object> row;
			List<Map<String, Object>> rowList = new ArrayList<>();
			if(reportTypeParams.getDataList()!=null && !reportTypeParams.getDataList().isEmpty()){
				ListIterator<List<String>> itr=reportTypeParams.getDataList().listIterator();  
				while(itr.hasNext()){	
					int index=0;
					row = new HashMap<>();
					List<String> reportResult = itr.next();
					row.put(BaseConstants.SRNO_GRID, reportResult.get(index++));
					row.put(BaseConstants.SERVERNAME_GRID,reportResult.get(index++));
					row.put(BaseConstants.SERVICENAME_GRID,reportResult.get(index++));
					row = getDataInRowsForFileBasedServices(row,reportResult,index);
					rowList.add(row);
				}
				responseObject.setObject(rowList);
			}
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}

	/**
	 * Method to compute end date and start date for Daily as an input by user
	 * @param reportTypeParams
	 */
	private void getStartDateAndEndDateForDaily(ReportTypeParameters reportTypeParams){
		if(reportTypeParams.getDailyDuration() == BaseConstants.SELECT){
			// For Daily Month and Year, get startDate and endDate
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH,Integer.parseInt(reportTypeParams.getDailyMonth()));
			cal.set(Calendar.YEAR,Integer.parseInt(reportTypeParams.getDailyYear()));
			cal.set(Calendar.DATE, 1);
			reportTypeParams.setStartDate(cal.getTime());
			reportTypeParams.setDailyReportStartDate(cal.getTime());
			cal.set(Calendar.DATE, MISReportUtils.getLastDayOfMonth(Integer.parseInt(reportTypeParams.getDailyMonth()),Integer.parseInt(reportTypeParams.getDailyYear())));
			reportTypeParams.setEndDate(cal.getTime());
			reportTypeParams.setDailyReportEndDate(cal.getTime());
		}else if (reportTypeParams.getDailyDuration() == BaseConstants.CUSTOM){
			Date date = null;
			try {
				date = formatter.parse(reportTypeParams.getDailyCustomFromDate());
			} catch (ParseException e) {
				logger.error("Error while date parsing From date:: " +e);
			}
			reportTypeParams.setStartDate(date);
			try {
				date = formatter.parse(reportTypeParams.getDailyCustomToDate());
			} catch (ParseException e) {
				logger.error("Error while date parsing To date:: " +e);
			}
			reportTypeParams.setEndDate(date);
			reportTypeParams.setDailyReportStartDate(reportTypeParams.getStartDate());
			reportTypeParams.setDailyReportEndDate(reportTypeParams.getEndDate());
		}

	}

	/**
	 * Method to compute end date and start date for Monthly as an input by user
	 * @param reportTypeParams
	 */
	private void getStartDateAndEndDateForMonthly(ReportTypeParameters reportTypeParams){
		if(reportTypeParams.getMonthlyDuration() == BaseConstants.SELECT){
			Calendar startCal = Calendar.getInstance();
			startCal.set(Calendar.MONTH, Calendar.JANUARY);
			startCal.set(Calendar.YEAR,Integer.parseInt(reportTypeParams.getMonthlyYear()));
			startCal.set(Calendar.DATE, 1);
			reportTypeParams.setStartDate(startCal.getTime());
			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.MONTH, Calendar.DECEMBER);
			endCal.set(Calendar.YEAR,Integer.parseInt(reportTypeParams.getMonthlyYear()));
			endCal.set(Calendar.DATE, MISReportUtils.getLastDayOfMonth(Calendar.DECEMBER,Integer.parseInt(reportTypeParams.getMonthlyYear())));
			reportTypeParams.setEndDate(endCal.getTime());
			reportTypeParams.setMonthlyReportStartDate(reportTypeParams.getStartDate());
			reportTypeParams.setMonthlyReportEndDate(reportTypeParams.getEndDate());
		}else if(reportTypeParams.getMonthlyDuration() == BaseConstants.CUSTOM){
			Calendar startCal = Calendar.getInstance();
			startCal.set(Calendar.MONTH,Integer.parseInt(reportTypeParams.getMonthlyStartMonth()));
			startCal.set(Calendar.YEAR,Integer.parseInt(reportTypeParams.getMonthlyStartYear()));
			startCal.set(Calendar.DATE, 1);
			reportTypeParams.setStartDate(startCal.getTime());
			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.MONTH,Integer.parseInt(reportTypeParams.getMonthlyEndMonth()));
			endCal.set(Calendar.YEAR,Integer.parseInt(reportTypeParams.getMonthlyEndYear()));
			endCal.set(Calendar.DATE, MISReportUtils.getLastDayOfMonth(Integer.parseInt(reportTypeParams.getMonthlyEndMonth()),Integer.parseInt(reportTypeParams.getMonthlyEndYear())));
			reportTypeParams.setEndDate(endCal.getTime());
			reportTypeParams.setMonthlyReportStartDate(reportTypeParams.getStartDate());
			reportTypeParams.setMonthlyReportEndDate(reportTypeParams.getEndDate());
		}

	}
	/**
	 * On the basis of passed input parameters from jsp, it creates startDate 
	 * and endDate for all types of reports and provides the data to DB query.
	 * @param reportTypeParams
	 */
	private void getMISReportDataFromDBInDetail(ReportTypeParameters reportTypeParams) {
		if (StringUtils.equals(BaseConstants.DAILY,reportTypeParams.getReportType())) {
			getStartDateAndEndDateForDaily(reportTypeParams);
			getServiceDetailForDailyAndMonthly(reportTypeParams);
		}else if (StringUtils.equals(BaseConstants.HOURLY,reportTypeParams.getReportType())) {
			getServiceDetailForHourly(reportTypeParams);
		}else if (StringUtils.equals(BaseConstants.MONTHLY,reportTypeParams.getReportType())){
			getStartDateAndEndDateForMonthly(reportTypeParams);
			getServiceDetailForDailyAndMonthly(reportTypeParams);
		}
	}

	/**
	 * DataBase call to get report data between the time specified by user from GUI for Detail report(monthly/daily)
	 * @param reportTypeParams
	 * @param downloadFlag
	 * @return
	 */
	private void getServiceDetailForDailyAndMonthly(ReportTypeParameters reportTypeParams) {
		List<MISReportTableData> reportDataList;
		reportDataList = mISReportService.getServiceDetailData(reportTypeParams);
		getDataFromResultListForDetail(reportDataList, reportTypeParams);
	}

	/**
	 * DataBase call to get report data between the time specified by user from GUI for Detail report(hourly)
	 * @param reportTypeParams
	 * @param downloadFlag
	 * @return
	 */
	private void getServiceDetailForHourly(ReportTypeParameters reportTypeParams) {
		Date reportStartDate = null;
		try {
			reportStartDate = formatter.parse(reportTypeParams.getHourlyDate());
		} catch (ParseException e) {
			logger.error("Error while date parsing To date:: " +e);
		}
		List<MISReportTableData> reportDataList;
		reportTypeParams.setHourlyReportDate(reportStartDate);
		reportDataList = mISReportService.getServiceDetailData(reportTypeParams);
		getDataFromResultListForDetail(reportDataList, reportTypeParams);
	}

	/**
	 * DataBase call to get report data between the time specified by user from GUI for Summary report(hourly)
	 * @param reportTypeParams
	 * @param serverInstanceList
	 */
	private void getServerSummaryForHourly(ReportTypeParameters reportTypeParams, List<String> serverInstanceList) {
		Date reportStartDate = null;
		try {
			reportStartDate = formatter.parse(reportTypeParams.getHourlyDate());
		} catch (ParseException e) {
			logger.error("Error while date parsing To date:: " +e);
		}
		reportTypeParams.setHourlyReportDate(reportStartDate);
		List<MISReportTableData> resultList;
		resultList = mISReportService.getServerSummaryData(serverInstanceList,reportTypeParams);
		getDataFromResultListForSummary(resultList, reportTypeParams);
	}

	/**
	 * DataBase call to get report data between the time specified by user from GUI for Summary report(monthly/daily)
	 * @param reportTypeParams
	 * @param serverInstanceList
	 */
	private void getServerSummaryForReport( ReportTypeParameters reportTypeParams, List<String> serverInstanceList) {
		List<MISReportTableData> resultList;
		resultList = mISReportService.getServerSummaryData(serverInstanceList, reportTypeParams);
		getDataFromResultListForSummary(resultList, reportTypeParams);
	}

	/**
	 * Method to get collect data for both download/grid for detail page
	 * @param resultList
	 * @param reportTypeParams
	 */
	private void getDataFromResultListForDetail(List<MISReportTableData> resultList, ReportTypeParameters reportTypeParams){
		List<List<String>> staticReportDataList;
		if(!resultList.isEmpty()){
			staticReportDataList = MISReportUtils.getStaticServiceWiseDetailReportList(resultList,reportTypeParams);
			if (staticReportDataList != null && !staticReportDataList.isEmpty()) {
				reportTypeParams.setHeaderList(staticReportDataList.get(0));
				staticReportDataList.remove(0);
				reportTypeParams.setDataList(staticReportDataList);
			}
			long count = reportTypeParams.getDataList().size();
			reportTypeParams.setRowCount(count);
		}
	}

	/**
	 * Method to get collect data for both download/grid for summary page
	 * @param resultList
	 * @param reportTypeParams
	 */
	private void getDataFromResultListForSummary(List<MISReportTableData> resultList, ReportTypeParameters reportTypeParams){
		List<List<String>> staticReportDataList;
		if(!resultList.isEmpty()){
			staticReportDataList = MISReportUtils.getStaticServiceWiseSummaryReportList(resultList, reportTypeParams.getServiceInstanceId());
			if (staticReportDataList != null && !staticReportDataList.isEmpty()) {
				reportTypeParams.setHeaderList(staticReportDataList.get(0));
				staticReportDataList.remove(0);
				reportTypeParams.setDataList(staticReportDataList);
			}
			long count = reportTypeParams.getDataList().size();
			reportTypeParams.setRowCount(count);
		}
	}
	/**
	 * Gets list of all serverInstances in DB for detail report page
	 * @param model
	 */
	public void getDataForDetail(ModelAndView model) {
		List<ServerInstance> serverInstanceList = serverInstanceService.getServerInstanceList();
		model.addObject(BaseConstants.SERVERINSTANCE_LIST, serverInstanceList);
	}

	/**
	 * Method to get all services mapped to the passed serverInstanceId
	 * @param serverInstanceId
	 * @return
	 * @throws CloneNotSupportedException
	 */
	@RequestMapping(value = ControllerConstants.FETCH_SERVICELIST_BY_SI, method = RequestMethod.POST)
	@ResponseBody
	public String fetchServiceListBySI(
			@RequestParam(value = "serverInstanceId", required = true) String serverInstanceId)
	{
		int iServerInstanceId = 0;
		if (!StringUtils.isEmpty(serverInstanceId)) {
			iServerInstanceId = Integer.parseInt(serverInstanceId);
		}
		ResponseObject responseObject = mISReportService.fetchServiceListBySI(iServerInstanceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	/**
	 * Views the requested time report data from DB on jqGrid for all packet based services on detail page
	 * @param reportTypeParams
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('GENERATE_SERVICE_WISE_DETAIL_REPORT')")
	@RequestMapping(value = ControllerConstants.GET_SERVER_DETAIL_LIST_FOR_PACKET_BASED_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String getServerDetailListForPacketBasedService(
			ReportTypeParameters reportTypeParams) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateReportParameters(importErrorList, reportTypeParams);

		if (!importErrorList.isEmpty()) {
			ajaxResponse = getErrorMsgList(importErrorList,ajaxResponse);
		}
		else{
			getMISReportDataFromDBInDetail(reportTypeParams);
			Map<String, Object> row;
			List<Map<String, Object>> rowList = new ArrayList<>();
			if(reportTypeParams.getDataList()!= null && !reportTypeParams.getDataList().isEmpty()){
				ListIterator<List<String>> itr=reportTypeParams.getDataList().listIterator();  
				while(itr.hasNext()){
					int index=0;
					row = new HashMap<>();
					List<String> reportResult = itr.next();
					row.put(BaseConstants.SRNO_GRID, Integer.parseInt(reportResult.get(index++)));
					row.put(BaseConstants.MONTH_GRID,reportResult.get(index++));
					row = getDataInRowsForPacketBasedServices(row,reportResult,index);
					rowList.add(row);
				}
			}
			if(!rowList.isEmpty()){
				responseObject.setObject(rowList);
			}
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}

	/**
	 * Method to complete row data for packetBasedServices common for both summary and detail
	 * @param row
	 * @param reportResult
	 * @param listIndex
	 * @return
	 */
	private Map<String, Object> getDataInRowsForPacketBasedServices(Map<String, Object> row,List<String> reportResult, int listIndex ){
		row.put(BaseConstants.TOTAL_PACKETS_GRID, reportResult.get(listIndex++));
		row.put(BaseConstants.SUCCESSPACKETS_GRID, reportResult.get(listIndex++));
		row.put(BaseConstants.DROPPEDPACKETS_GRID, reportResult.get(listIndex++));
		row.put(BaseConstants.DROPPEDPACKETSPERC_GRID,reportResult.get(listIndex++));
		row.put(BaseConstants.TOTAL_RECORDS_GRID, reportResult.get(listIndex++));
		row.put(BaseConstants.SUCCESS_RECORDS_GRID,reportResult.get(listIndex++));
		row.put(BaseConstants.FAIL_RECORDS_GRID, reportResult.get(listIndex++));
		row.put(BaseConstants.FAIL_RECORDS_PERC_GRID,reportResult.get(listIndex++));//NOSONAR

		return row;

	}

	/**
	 * Method to complete row data for fileBasedServices common for both summary and detail
	 * @param row
	 * @param reportResult
	 * @param listIndex
	 * @return
	 */
	private Map<String, Object> getDataInRowsForFileBasedServices(Map<String, Object> row,List<String> reportResult, int index){
		row.put(BaseConstants.TOTALFILES_GRID, reportResult.get(index++));
		row.put(BaseConstants.SUCCESSFILES_GRID, reportResult.get(index++));
		row.put(BaseConstants.FAILFILES_GRID, reportResult.get(index++));
		row.put(BaseConstants.FAILFILESPERC_GRID,reportResult.get(index++));
		row.put(BaseConstants.TOTAL_RECORDS_GRID, reportResult.get(index++));
		row.put(BaseConstants.SUCCESS_RECORDS_GRID,reportResult.get(index++));
		row.put(BaseConstants.FAIL_RECORDS_GRID, reportResult.get(index++));
		row.put(BaseConstants.FAIL_RECORDS_PERC_GRID,reportResult.get(index++));//NOSONAR

		return row;
	}

	/**
	 * Add validation errors to ajax response
	 * @param importErrorList
	 * @param ajaxResponse
	 * @return ajaxResponse
	 */
	private AjaxResponse getErrorMsgList(List<ImportValidationErrors> importErrorList, AjaxResponse ajaxResponse){
		ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
		Map<String, String> errorMsgs = new HashMap<>();
		for (ImportValidationErrors error : importErrorList) {
			if (error.getErrorMessage().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)) {
				errorMsgs.put(error.getPropertyName(),error.getErrorMessage());
			} else {
				errorMsgs.put(error.getPropertyName(),error.getErrorMessage());
			}
		}
		ajaxResponse.setObject(errorMsgs);
		return ajaxResponse;
	}
	/**
	 * Views the requested time report data from DB on jqGrid for all file based services on detail page
	 * @param reportTypeParams
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('GENERATE_SERVICE_WISE_DETAIL_REPORT')")
	@RequestMapping(value = ControllerConstants.GET_SERVER_DETAIL_LIST_FOR_FILE_BASED_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public String getServerDetailListForFileBasedService(
			ReportTypeParameters reportTypeParams) {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		List<ImportValidationErrors> importErrorList = new ArrayList<>();

		validator.validateReportParameters(importErrorList, reportTypeParams);
		if (!importErrorList.isEmpty()) {
			ajaxResponse = getErrorMsgList(importErrorList,ajaxResponse);
		}
		else{
			getMISReportDataFromDBInDetail(reportTypeParams);
			Map<String, Object> row;
			List<Map<String, Object>> rowList = new ArrayList<>();
			if(reportTypeParams.getDataList()!=null && !reportTypeParams.getDataList().isEmpty()){
				ListIterator<List<String>> itr=reportTypeParams.getDataList().listIterator();  
				while(itr.hasNext()){	
					int index=0;
					row = new HashMap<>();
					List<String> reportResult = itr.next();
					row.put(BaseConstants.SRNO_GRID, reportResult.get(index++));
					row.put(BaseConstants.MONTH_GRID,reportResult.get(index++));
					row = getDataInRowsForFileBasedServices(row,reportResult,index);
					rowList.add(row);
				}
				responseObject.setObject(rowList);
			}
			responseObject.setSuccess(true);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}	
}