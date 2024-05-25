package com.elitecore.sm.license.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.exceptions.StaffNotFoundException;
import com.elitecore.sm.license.exceptions.CircleNotFoundException;
import com.elitecore.sm.license.model.Circle;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.license.service.CircleService;
import com.elitecore.sm.license.service.LicenseService;
import com.elitecore.sm.license.util.LicenseUtility;
import com.elitecore.sm.license.validator.CircleValidator;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;
import com.google.gson.JsonObject;

/**
 * 
 * @author sterlite
 *
 */
@Controller
public class CircleController extends BaseController {

	protected Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServletContext servletContext;

	@Autowired
	ServerService serverService;

	@Autowired
	CircleService circleService;
	
	@Autowired
	@Qualifier(value = "servicesService")
	private ServicesService servicesService;

	@Autowired(required = true)
	@Qualifier(value = "serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	@Qualifier(value = "licenseUtilityQualifier")
	LicenseUtility licenseUtility;
	
	@Autowired
	private CircleValidator validator;
	
	@Autowired
	LicenseService licenseService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}

	@PreAuthorize("hasAnyAuthority('VIEW_CIRCLE_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.INIT_CIRCLE_CONFIG_MANAGER, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView initCircleConfig(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false,
			defaultValue = BaseConstants.CIRCLE_CONFIGURATION) String requestActionType) {

		ModelAndView model = new ModelAndView(ViewNameConstants.CIRCLE_CONFIG_MANAGER);
		Circle circle = (Circle) SpringApplicationContext.getBean(Circle.class);
		model.addObject(FormBeanConstants.CIRCLE_FORM_BEAN, circle);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CIRCLE_CONFIGURATION);
		return model;
	}

	@PreAuthorize("hasAnyAuthority('VIEW_CIRCLE')")
	@RequestMapping(value = ControllerConstants.GET_CIRCLE_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getCircleList(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false, defaultValue = BaseConstants.CIRCLE_CONFIGURATION) String requestActionType) {
		ResponseObject responseObject  = circleService.getAllCirclesList();
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('ADD_CIRCLE')")
	@RequestMapping(value = ControllerConstants.CREATE_CIRCLE, method = RequestMethod.POST)
	@ResponseBody public String createCircle (@ModelAttribute (value=FormBeanConstants.CIRCLE_FORM_BEAN) Circle circle,//NOSONAR 
			   @RequestParam(value="circleCurrentAction",required=true) String currentAction,
			   BindingResult result, HttpServletRequest request) throws SMException{
		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateCircleDetailsParam(circle, result, null, false);
		if(result.hasErrors()){
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			logger.info("Device details has been validated successfully going to create or update device details.");
			int staffId = eliteUtils.getLoggedInStaffId(request);
			ResponseObject responseObject = circleService.createNewCircle(circle, staffId, currentAction);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('EDIT_CIRCLE')")
	@RequestMapping(value = ControllerConstants.UPDATE_CIRCLE, method = RequestMethod.POST)
	@ResponseBody public String updateCircle (@ModelAttribute (value=FormBeanConstants.CIRCLE_FORM_BEAN) Circle circle,//NOSONAR			   
			   BindingResult result, HttpServletRequest request) throws SMException{
		AjaxResponse ajaxResponse = new AjaxResponse();		
		validator.validateCircleDetailsParam(circle, result, null, false);
		if(result.hasErrors()){
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			int staffId = eliteUtils.getLoggedInStaffId(request);
			ResponseObject responseObject = circleService.updateCircle(circle, staffId);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_CIRCLE')")	
	@RequestMapping(value = ControllerConstants.DELETE_CIRCLE, method = RequestMethod.POST)
	@ResponseBody	public  String deleteCircle(@RequestParam(value = "id",required=true) String id,
			HttpServletRequest request) throws StaffNotFoundException, CircleNotFoundException{
		AjaxResponse ajaxResponse ;
		ResponseObject responseObject ;
		int circleId = Integer.parseInt(id);
		int staffId = eliteUtils.getLoggedInStaffId(request);
		responseObject = circleService.deleteCircle(circleId, staffId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	@PreAuthorize("hasAnyAuthority('UPLOAD_CIRCLE_DEVICE_LICENSE')")
	@RequestMapping(value = ControllerConstants.UPLOAD_LICENSE_KEY, method = RequestMethod.POST)
	@ResponseBody
	public  String uploadDictionaryDataFileSync(
			@RequestParam(value = "id",required = true)String id,
			@RequestParam(value = "file", required = true) MultipartFile multipartFile,
			HttpServletRequest request ) throws SMException, SerialException, SQLException{
	    ResponseObject responseObject = new ResponseObject();
	    AjaxResponse ajaxResponse = new AjaxResponse();
		JSONArray jsonArray = new JSONArray();
		int circleId = 0;
		if (!multipartFile.isEmpty() && id!=null && !StringUtils.isEmpty(id)){	
			String fileName = multipartFile.getOriginalFilename();
			if(fileName.indexOf(BaseConstants.LICENSE_KEY_FILE_EXT)  >=0 && fileName.contains(".")  ) { // will check file type content type is XML and empty
				try {
					circleId = Integer.parseInt(id);
					String tempUploadDir = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
					String repositoryPath = servletContext.getRealPath(BaseConstants.LICENSE_PATH)+File.separator;
					File licenseKeyFile = new File(tempUploadDir + multipartFile.getOriginalFilename());
					multipartFile.transferTo(licenseKeyFile);
					Map<String, String> licenseDetails = licenseUtility.getData(licenseUtility.decrypt(tempUploadDir + multipartFile.getOriginalFilename(), repositoryPath));
					ResponseObject tempObj = circleService.getCircleById(circleId);
					if(tempObj!=null && tempObj.isSuccess() && tempObj.getObject()!=null) {
						License license = circleService.prepareLicenseDetails((Circle) tempObj.getObject(), licenseDetails);
						tempObj = circleService.saveOrUpdateLicenseInfo(license);
						if(tempObj!=null && tempObj.isSuccess() && tempObj.getObject()!=null) {
							responseObject.setSuccess(true);
						}else {
							responseObject.setSuccess(false);
						}
					}else {
						responseObject.setSuccess(false);
					}						
				} catch (Exception e) {
					logger.error("Exception Occured: " + e);
					throw new  SMException(e.getMessage());
				} 
				if (responseObject.isSuccess()) {
					JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("fileName", multipartFile.getOriginalFilename());
					jsonArray.put(jsonObject);
					responseObject.setObject(jsonArray);
					responseObject.setSuccess(true);
					ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
				}else {
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage(ResponseCode.LICENSE_DETAILS_FAILURE.toString()));
				}				
			}else{
				logger.debug("Failed to validate file name.");
				ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				ajaxResponse.setResponseMsg(getMessage(ResponseCode.INVALID_LICENSE_FILE.toString()));
			}
		}else{
			logger.debug("File Object found null.");
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage(ResponseCode.NO_LICENSE_FILE.toString()));
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CIRCLE_DEVICE_LICENSE_INFO')")
	@RequestMapping(value = ControllerConstants.LICENSE_INFO, method = RequestMethod.GET)
	@ResponseBody
	public String getLicenseInfo(@RequestParam(value = "id",required=true) String id,
			HttpServletRequest request) {
		int circleId = Integer.parseInt(id);
		ResponseObject responseObject  = licenseService.getLicenseByCircleId(circleId);
		if(responseObject.isSuccess()) {
			License license = (License) responseObject.getObject();
			responseObject = licenseService.getLicenseUtilization(license);
		}
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_CIRCLE')")
	@RequestMapping(value = ControllerConstants.GET_MAPPED_DEVICES_INFO, method = RequestMethod.GET)
	@ResponseBody
	public String getAllMappedDevicesInfo(@RequestParam(value = "id",required=true) String id,
			HttpServletRequest request) {
		int circleId = Integer.parseInt(id);
		ResponseObject responseObject  = circleService.getAllMappedDevicesInfo(circleId);		
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}
