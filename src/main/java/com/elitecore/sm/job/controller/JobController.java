/**
 * 
 */
package com.elitecore.sm.job.controller;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.job.service.JobService;
import com.elitecore.sm.util.DateFormatter;


@Controller
public class JobController extends BaseController {
	
	
	@Autowired
	JobService jobService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
}