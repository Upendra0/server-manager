/**
 * 
 */
package com.elitecore.sm.device.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.DecodeTypeEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.device.service.DeviceService;
import com.elitecore.sm.device.service.DeviceTypeService;
import com.elitecore.sm.device.service.VendorTypeService;
import com.elitecore.sm.device.validator.DeviceTypeValidator;
import com.elitecore.sm.device.validator.DeviceValidator;
import com.elitecore.sm.device.validator.VendorTypeValidator;
import com.elitecore.sm.parser.service.ParserMappingService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class DeviceController extends BaseController {

	@Autowired
	private DeviceValidator deviceValidator;
	
	@Autowired
	private DeviceTypeValidator deviceTypeValidator;
	
	@Autowired
	private VendorTypeValidator vendorTypeValidator;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private DeviceTypeService deviceTypeService;
	
	@Autowired
	private VendorTypeService  vendorTypeService;
	
	@Autowired
	private ParserMappingService  parserMappingService;
	
	
	
	
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

	
	
	/**
	 * Method will redirect and set all required basic params.
	 * @param requestActionType
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.INIT_DEVICE_MANAGER, method = RequestMethod.POST)
	public ModelAndView initDeviceManager(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType) {
		ModelAndView model = new ModelAndView(ViewNameConstants.DEVICE_MANAGER);
		
		ResponseObject responseObject  = deviceTypeService.getAllDeviceType();
		List<DeviceType> deviceTypeList = null;
		Device device = (Device) SpringApplicationContext.getBean(Device.class);
		if(responseObject.isSuccess()){
			logger.debug("All Device type list found successfully.");
			deviceTypeList = (List<DeviceType>) responseObject.getObject();
			model.addObject("deviceTypeList",deviceTypeList);
		}else{
			logger.debug("Device type list found null.");
			model.addObject("deviceTypeList",deviceTypeList);
		}
		
		model.addObject(FormBeanConstants.DEVICE_FORM_BEAN, device );
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
		model.addObject("decodeTypeEnum",Arrays.asList(DecodeTypeEnum.values())); 
		
	return model;
}	
	
	/**
	 * Method will create and update device , also it will create device type and vendor type based on selected front end option.
	 * @param device
	 * @param currentAction
	 * @param result
	 * @param request
	 * @return ResponseBody(JsonObject String)
	 */
	@PreAuthorize("hasAnyAuthority('ADD_DEVICE','EDIT_DEVICE')")
	@RequestMapping(value = ControllerConstants.CREATE_DEVICE, method = RequestMethod.POST)
	@ResponseBody public   String createDevice (
			   @ModelAttribute (value=FormBeanConstants.DEVICE_FORM_BEAN) Device device,//NOSONAR 
			   @RequestParam(value="deviceCurrentAction",required=true) String currentAction,
			   BindingResult result,
			   HttpServletRequest request) throws SMException{
		
			
			AjaxResponse ajaxResponse = new AjaxResponse();
			String flag = BaseConstants.NONE;
			
			if(device.getDeviceType().getId() == 0){ // It will validate device type name if Other option is selected from drop down while create device
				deviceTypeValidator.validateDeviceType(device.getDeviceType(), result, null, false, null);
				flag = BaseConstants.CREATE_DEVICE_TYPE;
			}
			
			if (device.getVendorType().getId() == 0){ // It will validate vendor type name if Other option is selected from drop down while create device
				vendorTypeValidator.validateVendorType(device.getVendorType(), result, null, false, null);
				flag = BaseConstants.CREATE_VENDOR_TYPE;
			}
			
			if(device.getDeviceType().getId() == 0 && device.getVendorType().getId() == 0){ //Setting flag to create both device type and vendor type . selected other option from front end for both
				flag = BaseConstants.BOTH;
			}
					
			deviceValidator.validateDevice(device, result,null,false, null); // It will validate the device mandatory field.
			
			 if(result.hasErrors()){
				  ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
				  eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
				  logger.info("Failed to validate device details.");
			 }else{
				 logger.info("Device details has been validated successfully going to create or update device details.");
				 int staffId = eliteUtils.getLoggedInStaffId(request);
				 ResponseObject responseObject = this.deviceService.createDevice(device, staffId, flag, currentAction);
				 ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			 }
		return ajaxResponse.toString();
	}
	
	/**
	 *  Method will fetch all service instances by service Search Parameters. 
	 * @param service
	 * @param result
	 * @param limit
	 * @param currentPage
	 * @param sidx
	 * @param sord
	 * @param request
	 * @return JsonAjax response
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_DEVICE_MAPPING_LIST, method = RequestMethod.POST)
	@ResponseBody public  String getDeviceAndMappingList(
			SearchDeviceMapping searchDeviceMapping, 
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord
				) {
		
		logger.debug(">> getDeviceAndMappingList in DeviceController " + searchDeviceMapping); 
		long count = this.deviceService.getTotalDeviceMappingCount(searchDeviceMapping,sidx, sord);
		logger.debug("Device count found " + count);
		
		List<Object[]> resultList = new ArrayList<>(); 
		if (count > 0){
			resultList = this.deviceService.getPaginatedList(searchDeviceMapping,  eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}

		Map<String, Object> row ;

		List<Map<String, Object>> rowList = new ArrayList<>();
	
		if (resultList != null) {
			
			for (Object[] object : resultList) {
				row = new HashMap<>();
				
				row.put("id",object[0]+"_"+(object[5]!=null?object[5]:"-1"));
				row.put("mappingId", object[5]);
				row.put("deviceType", object[3]);
				row.put("deviceName", object[1]);
				row.put("vendorType", object[4]);
				row.put("decodeType", object[2]);
				row.put("isPreConfigured", object[8]);
				row.put("mappingType", object[9]);
				row.put("pluginType", object[10]);
			   
				if(object[6] != null)
				   {row.put("mappingName", object[6]);}
			    else{row.put("mappingName", BaseConstants.NO_MAPPING);}  // ADDING NO Mapping for device which do not have mapping.
			  
			    if(Integer.parseInt(object[7].toString()) > 0 )  // Checking mapping is associated with any plugin or not.
				    {row.put("isAssociated",true); }
        	    else {row.put("isAssociated",false);}
				row.put("deviceId",object[0]);
			   rowList.add(row);
			}
		}
		
		logger.debug("<< getDeviceAndMappingList in DeviceController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceTypeId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_DEVICE_LIST_BY_VENDOR, method = RequestMethod.POST)
	@ResponseBody public  String getDeviceListByVendor(
				@RequestParam(value = "deviceTypeId",required = true) int deviceTypeId,
				@RequestParam(value = "vendorTypeId",required = true) int vendorTypeId,
				@RequestParam(value = "decodeType",required = true) String decodeType) {
		logger.debug(">> getDeviceListByVendor in DeviceController " + deviceTypeId  + " "  + vendorTypeId  + " " +  decodeType); 
		ResponseObject responseObject  = this.deviceService.getAllDeviceByVendorAndDeviceType(deviceTypeId, vendorTypeId, decodeType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceTypeId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_DEVICE_LIST_BY_DECODETYPE, method = RequestMethod.POST)
	@ResponseBody public  String getDeviceListByDecodeType(
				SearchDeviceMapping searchDeviceMapping,
				@RequestParam(value = "rows", defaultValue = "10") int limit,
				@RequestParam(value = "page", defaultValue = "1") int currentPage,
				@RequestParam(value = "sidx", required = true) String sidx, 
				@RequestParam(value = "sord", required = true) String sord) {
		
		logger.debug(">> getDeviceListByDecodeType in DeviceController " + searchDeviceMapping); 
		long count = this.deviceService.getTotalDeviceCountByDecodeType(searchDeviceMapping,sidx, sord);
		logger.debug("Device count found " + count);
		
		List<Device> resultList = new ArrayList<>(); 
		if (count > 0){
			resultList = this.deviceService.getPaginatedListForDevice(searchDeviceMapping,  eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (Device deviceList : resultList) {
				
				row = new HashMap<>();
				
				row.put("id",deviceList.getId());
				row.put("deviceType", deviceList.getDeviceType().getName());
				row.put("deviceName",deviceList.getName());
				row.put("vendorType",deviceList.getVendorType().getName());
				row.put("decodeType", deviceList.getDecodeType());
				rowList.add(row);
			}
		}
		
		
		
		logger.debug("<< getDeviceListByDecodeType in DeviceController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_DEVICE_DETAILS, method = RequestMethod.POST)
	@ResponseBody public  String getDeviceDetails(
				@RequestParam(value = "deviceId",required = true) int deviceId,
				@RequestParam(value = "decodeType",required = true) String decodeType) {
		logger.debug(">> getDeviceDetails in DeviceController " + deviceId); 
		ResponseObject responseObject  = this.deviceService.getDeviceDetails(deviceId, decodeType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_DEVICE')")
	@RequestMapping(value = ControllerConstants.GET_DEVICE_BY_ID, method = RequestMethod.POST)
	@ResponseBody public  String getDeviceById(
				@RequestParam(value = "deviceId",required = true) int deviceId,
				@RequestParam(value = "decodeType",required = true) String decodeType ) {
		logger.debug(">> getDeviceById in DeviceController " + deviceId); 
		ResponseObject responseObject  = this.deviceService.getDeviceByDeviceId(deviceId, BaseConstants.JSON);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will delete all selected mappings for that particular decodeType.
	 * @param mappingIdList, decodeType
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.DELETE_DEVICES_MAPPINGS, method = RequestMethod.POST)
	@ResponseBody public  String deleteDevicesAndMappings(
				@RequestParam(value = "deviceIds",required = true) String deviceIds,
				@RequestParam(value = "decodeType",required = true) String decodeType,
				HttpServletRequest request) {
		logger.debug(">>deleteDevicesAndMappings  in DeviceController " + deviceIds);
		ResponseObject responseObject ;
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		
		responseObject = this.deviceService.deleteDevicesAndMappings(staffId, convertStringArrayToInt(deviceIds), decodeType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
	
	/**
	 * Method will delete all selected mappings for that particular decodeType.
	 * @param mappingIdList, decodeType
	 * @param request
	 * @return AJax response.
	 */
	@RequestMapping(value = ControllerConstants.DELETE_MAPPINGS, method = RequestMethod.POST)
	@ResponseBody public  String deleteMappings(
				@RequestParam(value = "mappingIdList",required = true) String mappingIdList,
				@RequestParam(value = "decodeType",required = true) String decodeType,
				HttpServletRequest request) {
		logger.debug(">> deleteMappings in DeviceController " + mappingIdList);
		ResponseObject responseObject ;
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		responseObject = this.deviceService.deleteMappings(staffId, convertStringArrayToInt(mappingIdList), decodeType);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}
	
	
	/** Method will convert comma seperated String to integer array.
	 * @param parserMappingIds
	 * @return
	 */
	private  Integer[] convertStringArrayToInt(String parserMappingIds){
		Integer[] numbers = null;
		if (!StringUtils.isEmpty(parserMappingIds)){
			String [] ids = parserMappingIds.split(",");
			 numbers = new Integer[ids.length];
			for(int i = 0;i < ids.length;i++){
			   numbers[i] = Integer.parseInt(ids[i]);
			}
			return numbers;
		}else{
			return numbers;
		}
		
	}
}