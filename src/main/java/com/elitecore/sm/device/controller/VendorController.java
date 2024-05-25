/**
 * 
 */
package com.elitecore.sm.device.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.device.service.VendorTypeService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class VendorController  extends BaseController {

	@Autowired
	VendorTypeService vendorTypeService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceTypeId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_VENDOR_LIST_BY_DEVICE_TYPE, method = RequestMethod.POST)
	public @ResponseBody String getVendorListByDeviceType(
				@RequestParam(value = "deviceTypeId",required = true) int deviceTypeId,
				HttpServletRequest request) {
		logger.debug(">> getVendorListByDeviceType in VendorController " + deviceTypeId); 
		ResponseObject responseObject  = this.vendorTypeService.getVendorListByDeviceTypeId(deviceTypeId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
}
