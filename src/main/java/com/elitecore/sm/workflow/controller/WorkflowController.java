package com.elitecore.sm.workflow.controller;

import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class WorkflowController extends BaseController{
	
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
	
	@RequestMapping(value = ControllerConstants.INIT_WORKFLOW_MANAGER, method = RequestMethod.GET)
	public ModelAndView initWorkFlowManager() {
		ModelAndView model = new ModelAndView(ViewNameConstants.WORKFLOW_MANAGER);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.WORKFLOW_MANAGEMENT);
		
		
		return model;
	}

}
