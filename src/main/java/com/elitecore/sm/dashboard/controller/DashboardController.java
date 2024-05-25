/**
 * 
 */
package com.elitecore.sm.dashboard.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.policy.controller.TempAlert;
import com.elitecore.sm.policy.controller.TempFileStatistic;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author elitecore
 *
 */
@Controller
public class DashboardController extends BaseController{

	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	@RequestMapping(value = ControllerConstants.INIT_DASHBOARD_MANAGER, method = RequestMethod.GET)
	public ModelAndView initDashboardManager() {
		ModelAndView model = new ModelAndView();
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DASHBOARD);
		//model.setViewName(ViewNameConstants.INIT_DASHBOARD_MANAGER);
		model.setViewName(ViewNameConstants.HOME_PAGE);
		setModelData(model);  // setting all dash-board static data.
		return model;
	}
	
	
	
	/**
	 * Method will redirect to dash-board page using post method.
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.DASHBOARD_REDIRECT_VIEW, method = RequestMethod.POST)
	public ModelAndView dashbordRedirectView(
			@RequestParam(value = "isMessageShow", required = true) String isMessageShow,
			@RequestParam(value = "isRenew", required = true) String isRenew,
			@RequestParam(value = "licenseShortReminder", required = true) String licenseShortReminder) {
		ModelAndView model = new ModelAndView();
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DASHBOARD);
		//model.setViewName(ViewNameConstants.INIT_DASHBOARD_MANAGER);
		model.setViewName(ViewNameConstants.HOME_PAGE);
		model.addObject("isMessageShow", isMessageShow);
		model.addObject("isRenew", isRenew);
		model.addObject("licenseShortReminder", licenseShortReminder);
		setModelData(model);  // setting all dash-board static data.
		return model;
	}
	
	/**
	 * Method will set dash-board static data.
	 * @param model
	 */
	private void setModelData(ModelAndView model){
		List<TempAlert> alertList = new ArrayList<>();
		TempAlert alert = new TempAlert();
		alert.setIntanceName("Mediation Server-192.168.0.146");//NOSONAR
		alert.setStage("critical");
		alert.setAlert("Disk Utilization is high(98%) for Server Intance.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("CGF Server-192.168.1.19");//NOSONAR
		alert.setStage("critical");
		alert.setAlert("Distribution Service goes down.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("IPLMS_Server - 192.168.1.168 ");//NOSONAR
		alert.setStage("info");
		alert.setAlert("102 file moved to Error directory for Parsing.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setIntanceName("Mediation Server-192.168.1.198");//NOSONAR
		alert.setStage("normal");
		alert.setAlert("GTP Collection Service packets not found in last 10 min.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		alert = new TempAlert();
		alert.setStage("info");
		alert.setIntanceName("Mediation Server-192.168.4.20");//NOSONAR
		alert.setAlert("998 files are found duplicate in Collection Service.");
		alert.setTime(DateFormatter.formatDate(new Date()));
		alertList.add(alert);
		
		List<TempFileStatistic> statisticList = new ArrayList<>();
		TempFileStatistic statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.0.146");//NOSONAR
		statistic.setProcessedCDR("28000000");
		statistic.setProcessedFileSize("118");
		statistic.setRawCDR("30000000");
		statistic.setRawFileSize("120");
		statistic.setDeviation(9.33);
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("CGF Server-192.168.1.19");//NOSONAR
		statistic.setProcessedCDR("15000000");
		statistic.setProcessedFileSize("131");
		statistic.setRawCDR("40000000");
		statistic.setDeviation(3.75);
		statistic.setRawFileSize("135");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("IPLMS_Server - 192.168.1.168 ");//NOSONAR
		statistic.setProcessedCDR("22000000");
		statistic.setProcessedFileSize("121");
		statistic.setRawCDR("30000000");
		statistic.setDeviation(6);
		statistic.setRawFileSize("130");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.1.198");//NOSONAR
		statistic.setProcessedCDR("18000000");
		statistic.setProcessedFileSize("123");
		statistic.setRawCDR("20000000");
		statistic.setDeviation(9);
		statistic.setRawFileSize("125");
		statisticList.add(statistic);
		
		statistic = new TempFileStatistic();
		statistic.setIntanceName("Mediation Server-192.168.4.20");//NOSONAR
		statistic.setProcessedCDR("30000000");
		statistic.setProcessedFileSize("118");
		statistic.setRawCDR("35000000");
		statistic.setDeviation(8.5);
		statistic.setRawFileSize("128");
		
		statisticList.add(statistic);
		
		
		List<TempFileStatistic> statisticInstaceList = new ArrayList<>();
		TempFileStatistic statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Collection Service");
		statisticInstace.setProcessedCDR("28000000");
		statisticInstace.setProcessedFileSize("118");
		statisticInstace.setRawCDR("30000000");
		statisticInstace.setRawFileSize("120");
		statisticInstace.setDeviation(9.33);
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Distribution Service");
		statisticInstace.setProcessedCDR("15000000");
		statisticInstace.setProcessedFileSize("131");
		statisticInstace.setRawCDR("40000000");
		statisticInstace.setDeviation(3.75);
		statisticInstace.setRawFileSize("135");
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Parsing Service");
		statisticInstace.setProcessedCDR("22000000");
		statisticInstace.setProcessedFileSize("121");
		statisticInstace.setRawCDR("30000000");
		statisticInstace.setDeviation(6);
		statisticInstace.setRawFileSize("130");
		statisticInstaceList.add(statisticInstace);
		
		statisticInstace = new TempFileStatistic();
		statisticInstace.setIntanceName("Processing Service");
		statisticInstace.setProcessedCDR("18000000");
		statisticInstace.setProcessedFileSize("123");
		statisticInstace.setRawCDR("20000000");
		statisticInstace.setDeviation(9);
		statisticInstace.setRawFileSize("125");
		statisticInstaceList.add(statisticInstace);
		
		
		model.addObject("alertList",alertList);
		model.addObject("statisticList",statisticList);
		model.addObject("iStatisticList",statisticInstaceList);
		
		
		
	}
	
	
}
