/**
 * 
 */
package com.elitecore.sm.pathlist.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.composer.dao.ComposerDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.services.model.DistributionService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class DistributionDriverPathListController  extends BaseController {

	
	@Autowired
	ServicesService servicesService; 
	
	@Autowired
	DriversService driverService;
	
	@Autowired
	PathListService pathListService;
	
	@Autowired
	PathListValidator pathlistValidator;
	
	@Inject
	private ComposerDao composerDao;
	
	@Inject
	private PathListHistoryService pathListHistoryService;
	
	
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
	
	
	/** 
	 * Method will fetch all distribution driver path list with associated all plug-in list. 
	 * @param driverId
	 * @param driverType
	 * @return ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.INIT_DISTRIBUTION_DRIVER_PATHLIST_MANAGER, method = RequestMethod.POST)
	public ModelAndView initDistributionDriverPathlistManager(
			@RequestParam(value = BaseConstants.DRIVER_ID, required=true) int driverId,
			@RequestParam(value = "driverTypeAlias", required=true) String driverTypeAlias,
			@RequestParam(value = BaseConstants.SERVICE_ID, required=true) int serviceId,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
			@RequestParam(value = BaseConstants.SERVICE_NAME, required=true) String serviceName,
			@RequestParam(value = BaseConstants.INSTANCE_ID, required=true) int serverInstanceId,
			@RequestParam(value = "serviceDBStats", required=true) String serviceDBStats,
			@RequestParam(value = BaseConstants.DRIVER_NAME, required=true) String driverName) {

		ModelAndView model = new ModelAndView();
		
		model.setViewName(ViewNameConstants.DISTRIBUTION_DRIVER_CONFIG_MANAGER);
		
		DistributionService service = (DistributionService) servicesService.getAllServiceDepedantsByServiceId(serviceId);
		ResponseObject responseObject = pathListService.getDistributionPathListAndPluginDetails(driverId, driverTypeAlias);
		if(responseObject.isSuccess()){
			model.addObject("pathList",responseObject.getObject());
		}
		
		int serverTypeId=service.getServerInstance().getServer().getServerType().getId();
		List<PluginTypeMaster> composerType=(List<PluginTypeMaster>) eliteUtils.fetchProfileEntityStatusFromCache(serverTypeId,BaseConstants.COMPOSER_PLUGIN_TYPE);
		logger.debug("Final Composer  type  list" +composerType  );
		
		DistributionDriverPathList distributionDriverPathlist = (DistributionDriverPathList) SpringApplicationContext.getBean(DistributionDriverPathList.class);
		distributionDriverPathlist.setCompressInFileEnabled(true); 
		
		
		model.addObject(FormBeanConstants.PATHLIST_FORM_BEAN, distributionDriverPathlist);
		model.addObject(FormBeanConstants.COMPOSER_PLUGIN_FORM_BEAN, (Composer)SpringApplicationContext.getBean(Composer.class));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DISTRIBUTION_DRIVER_PATHLIST_CONFIGURATION);
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject("positionEnum", java.util.Arrays.asList(PositionEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		model.addObject(BaseConstants.DRIVER_ID,driverId);
		model.addObject("driverTypeAlias",driverTypeAlias);
		model.addObject(BaseConstants.INSTANCE_ID,serverInstanceId);
		model.addObject("composerTypeList",composerType);
		model.addObject("serviceDbStats", serviceDBStats);
		model.addObject(BaseConstants.DRIVER_NAME,driverName);
		model.addObject("fileGroupEnable", service.getFileGroupingParameter().isFileGroupEnable());
		model.addObject("fileMergeEnable", service.isFileMergeEnabled());		
		
		return model;
	}
	
	
	/**
	 * Method will create new distribution driver path list and validate all required parameters.
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_DISTRIBUTION_DRIVER_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String createDistributionDriverPathlist(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) DistributionDriverPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,
			HttpServletRequest request){
	
		logger.debug("Create new distribution driver path list. " + pathList.getName());
		
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			logger.info("Validation done successfully for distribution driver pathlist parameters.");
			pathList.setCreatedDate(new Date());
			pathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			responseObject = pathListService.addDistributionDriverPathList(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(pathList, null);
			}
		}
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Update parsing path list in database
	 * @param pathList
	 * @param result
	 * @param counter
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_DISTRIBUTION_DRIVER_PATH_LIST, method = RequestMethod.POST)
	@ResponseBody public String updateParsingPathList(
			@ModelAttribute(value=FormBeanConstants.PATHLIST_FORM_BEAN) DistributionDriverPathList pathList,//NOSONAR
			BindingResult result,
			@RequestParam(value="pathListCount",required=true) String counter,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		ResponseObject responseObject ;
		
		pathlistValidator.validatePathListParams(pathList,result,null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(counter+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(counter+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
	}else{
			DistributionDriverPathList distribution_pathlist = (DistributionDriverPathList) pathListService.getPathListById(pathList.getId());
			pathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			pathList.setLastUpdatedDate(new Date());
			pathList.setCreatedByStaffId(distribution_pathlist.getCreatedByStaffId());
			pathList.setCreatedDate(distribution_pathlist.getCreatedDate());
			
			responseObject = pathListService.updatePathListDetail(pathList);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if(responseObject.isSuccess()) {
				List<Composer> composerWrappers = composerDao.getComposerListByPathListId(pathList.getId());
				if(CollectionUtils.isEmpty(composerWrappers)) {
					pathListHistoryService.save(pathList, null);
				} else {
					composerWrappers.forEach(composer -> pathListHistoryService.save(pathList, composer));
				}
			}
		}
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will delete Distribution Driver path-list  
	 * @param pathlistId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_DISTRIBUTION_DRIVER')")
	@RequestMapping(value = ControllerConstants.DELETE_DISTRIBUTION_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public String deleteDistributionDriverPathList(@RequestParam(value = "pathlistId",required=true) int pathlistId,
						HttpServletRequest request) throws SMException{
			
			ResponseObject  responseObject = pathListService.deleteParsingPathListDetails(pathlistId, eliteUtils.getLoggedInStaffId(request));
			AjaxResponse  ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
		return ajaxResponse.toString();
	}
}
