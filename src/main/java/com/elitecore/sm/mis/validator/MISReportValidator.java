package com.elitecore.sm.mis.validator;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.MISReportUtils;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.mis.model.ReportTypeParameters;

@Component
public class MISReportValidator extends BaseValidator{
	private static final String ERROR_MESSAGE_FORMAT_DURATION_INTERVAL="mis.search.duration.greater.than.max.duration";
	private static final  String DATEFORMATSTRING = "MM/dd/yyyy";
	private DateFormat formatter = new SimpleDateFormat(DATEFORMATSTRING);
	/**
	 * Validates MIS report parameters startDate and End Date for both daily and 
	 * monthly type report
	 * 
	 * @param importErrorList
	 * @param reportTypeParams
	 */
	public void validateReportParameters(List<ImportValidationErrors> importErrorList,ReportTypeParameters reportTypeParams)
	{
		if((StringUtils.equals(reportTypeParams.getReportType(), BaseConstants.DAILY)) && (reportTypeParams.getDailyDuration() == 2)){
			Date startdate = null;
			Date enddate = null ;
			try {
				startdate = formatter.parse(reportTypeParams.getDailyCustomFromDate());
				enddate = formatter.parse(reportTypeParams.getDailyCustomToDate());
				if(startdate.after(enddate)){
					ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "dailyCustomDateFrom"
							, String.valueOf(reportTypeParams.getDailyCustomFromDate()), getMessage("mis.daily.custom.startDate.greater.endDate"));
					importErrorList.add(importErrors);
				}else if(!MISReportUtils.checkDuration(startdate, enddate, Calendar.YEAR, BaseConstants.MAX_DUARATION)) {
					ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "dailyCustomDateFrom"
							, String.valueOf(reportTypeParams.getDailyCustomFromDate()), getMessage(ERROR_MESSAGE_FORMAT_DURATION_INTERVAL));
					importErrorList.add(importErrors);
				}
			} catch (ParseException e) {
				logger.error("Error while date parsing To date:: " +e);
			}
		}else if(StringUtils.equals(reportTypeParams.getReportType(), BaseConstants.MONTHLY) && (reportTypeParams.getMonthlyDuration() == 2)){
			validateMonthlyData(importErrorList, reportTypeParams);
		}
	}

	/**
	 * Validates Monthly Data for MIS Report
	 * @param importErrorList
	 * @param reportTypeParams
	 */
	private void validateMonthlyData(List<ImportValidationErrors> importErrorList,ReportTypeParameters reportTypeParams){
		if(Integer.parseInt(reportTypeParams.getMonthlyStartYear()) > Integer.parseInt(reportTypeParams.getMonthlyEndYear())){
			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "monthlyyear1id"
					, String.valueOf(reportTypeParams.getMonthlyStartYear()), getMessage("mis.monthly.custom.startYear.greater.endYear"));
			importErrorList.add(importErrors);
		}
		else if(Integer.parseInt(reportTypeParams.getMonthlyStartYear()) == Integer.parseInt(reportTypeParams.getMonthlyEndYear()) &&
				Integer.parseInt(reportTypeParams.getMonthlyStartMonth()) > Integer.parseInt(reportTypeParams.getMonthlyEndMonth()) ) {
			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "monthlymonth1id"
					, String.valueOf(reportTypeParams.getMonthlyStartMonth()), getMessage("mis.monthly.custom.startMonth.greater.endMonth"));
			importErrorList.add(importErrors);
		}else {
			Calendar startCal = Calendar.getInstance();
			startCal.set(Calendar.MONTH,
					Integer.parseInt(reportTypeParams.getMonthlyStartMonth()));
			startCal.set(Calendar.YEAR,
					Integer.parseInt(reportTypeParams.getMonthlyStartYear()));
			startCal.set(Calendar.DATE, 1);
			Date startDate = startCal.getTime();
			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.MONTH,
					Integer.parseInt(reportTypeParams.getMonthlyEndMonth()));
			endCal.set(Calendar.YEAR,
					Integer.parseInt(reportTypeParams.getMonthlyEndYear()));
			endCal.set(Calendar.DATE, MISReportUtils.getLastDayOfMonth(
					Integer.parseInt(reportTypeParams.getMonthlyEndMonth()),
					Integer.parseInt(reportTypeParams.getMonthlyEndYear())));
			Date endDate =endCal.getTime();
			if(!MISReportUtils.checkDuration(startDate, endDate, Calendar.YEAR, BaseConstants.MAX_DUARATION)) {
				ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "monthlyyear2id"
						, String.valueOf(reportTypeParams.getMonthlyStartYear()), getMessage(ERROR_MESSAGE_FORMAT_DURATION_INTERVAL));
				importErrorList.add(importErrors);
				importErrors = new ImportValidationErrors(BaseConstants.SERVICE_WISE_DETAIL, BaseConstants.SERVICE_WISE_DETAIL, "monthlyyear1id"
						, String.valueOf(reportTypeParams.getMonthlyStartYear()), getMessage(ERROR_MESSAGE_FORMAT_DURATION_INTERVAL));
				importErrorList.add(importErrors);
			}
		}
	}
}
