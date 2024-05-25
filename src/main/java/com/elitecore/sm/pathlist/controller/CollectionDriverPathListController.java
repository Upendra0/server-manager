package com.elitecore.sm.pathlist.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
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
import com.elitecore.sm.common.model.DuplicateCheckParamEnum;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FileFetchTypeEnum;
import com.elitecore.sm.common.model.FileGroupEnum;
import com.elitecore.sm.common.model.FileTransferModeEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.model.MissingFileFrequencyEnum;
import com.elitecore.sm.common.model.PositionEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.parser.model.SourceDateFormatEnum;
import com.elitecore.sm.pathlist.model.CharRenameDateFormatEnum;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.service.PathListHistoryService;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.services.model.DateTypeEnum;
import com.elitecore.sm.util.DateFormatter;

/**
 * 
 * @author avani.panchal
 *
 */
@Controller
public class CollectionDriverPathListController extends BaseController{
	
	@Autowired
	PathListService pathListService;
	
	@Autowired
	PathListValidator validator;
	
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
		binder.setValidator(validator);
	}
	
	/**
	 * When click on PathList Configuration tab , this method call
	 * @param driverId
	 * @param driverName
	 * @param driverTypeAlias
	 * @param serviceId
	 * @param serviceName
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.INIT_PATHLIST_CONFIGURATION,method= RequestMethod.POST)
	public ModelAndView initPathListConfig(@RequestParam(value=BaseConstants.DRIVER_ID,required=true) String driverId,
															  	    @RequestParam(value=BaseConstants.DRIVER_NAME,required=true) String driverName,
															  	    @RequestParam(value="driverTypeAlias",required=true) String driverTypeAlias,
															  	    @RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
															  	    @RequestParam(value=BaseConstants.SERVICE_NAME,required=true) String serviceName,
															  	  @RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
															  	    @RequestParam(value="serviceDBStats",required=true) String serviceDBStats,
															  	  @RequestParam(value="forDuplicatEnabled",required=true) String forDuplicatEnabled,
															        HttpServletRequest request){
	
		ModelAndView model = new ModelAndView(ViewNameConstants.FTP_CONFIG_MANAGER);
		
		int idriverId=Integer.parseInt(driverId);
		
		ResponseObject responseObject=pathListService.getPathListByDriverId(idriverId);
		if(responseObject.isSuccess()){
			model.addObject("pathList",(List<CollectionDriverPathList>)responseObject.getObject());
		}
		
		CollectionDriverPathList collectionDriverPathList=(CollectionDriverPathList) SpringApplicationContext.getBean(CollectionDriverPathList.class);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.PATH_LIST_CONFIGURATION);
		model.addObject("fileGroupType",FileGroupEnum.getValues());
		model.addObject("fileFetchType",Arrays.asList(FileFetchTypeEnum.values()));
		model.addObject("trueFalseEnum",Arrays.asList(TrueFalseEnum.values()));
		model.addObject(FormBeanConstants.COLLECTION_PATHLIST_CONFIGURATION_FORM_BEAN,collectionDriverPathList);
		model.addObject("fileFilterActionEnum",Arrays.asList(FilterActionEnum.values()));
		model.addObject("missingFileFrequency",Arrays.asList(MissingFileFrequencyEnum.values()));
		model.addObject("fileFilterActionTypeEnum",Arrays.asList(FileActionParamEnum.values()));
		model.addObject("duplicateCheckParamEnum",Arrays.asList(DuplicateCheckParamEnum.values()));
		model.addObject("positionEnum",Arrays.asList(PositionEnum.values()));
		model.addObject("transferModeEnum",Arrays.asList(FileTransferModeEnum.values()));
		model.addObject("sourceDateFormetEnum", Arrays.asList(CharRenameDateFormatEnum.values()));
		model.addObject("dateTypeEnum", Arrays.asList(DateTypeEnum.values()));
		model.addObject(BaseConstants.SERVICE_ID,serviceId);
		model.addObject(BaseConstants.SERVICE_NAME,serviceName);
		model.addObject(BaseConstants.DRIVER_ID,driverId);
		model.addObject(BaseConstants.DRIVER_NAME,driverName);
		model.addObject("driverTypeAlias",driverTypeAlias);
		//model.addObject("maxCounterLimit",collectionDriverPathList!=null?collectionDriverPathList.getMaxCounterLimit():"");
		model.addObject("maxFilesCountAlert",collectionDriverPathList!=null?collectionDriverPathList.getMaxFilesCountAlert():"");
		model.addObject("serviceDbStats", serviceDBStats);
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
		model.addObject("forDuplicatEnabled", forDuplicatEnabled);
		return model;
	}
	
	/**
	 * Add pathlist into database
	 * @param collectionDriverPathList
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_COLLECTIONDRIVER_PATHLIST, method = RequestMethod.POST)
	@ResponseBody public  String createCollectionDriverPathList(@RequestParam(value="pathListCount") String pathListCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_PATHLIST_CONFIGURATION_FORM_BEAN) CollectionDriverPathList collectionDriverPathList,//NOSONAR
			BindingResult result,HttpServletRequest request){
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		validator.validatePathListParams(collectionDriverPathList, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(pathListCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(pathListCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
		}else{
			if(collectionDriverPathList.getDuplicateCheckParamName() != null && collectionDriverPathList.getDuplicateCheckParamName().contains(",")){
				String [] dupParams = 	collectionDriverPathList.getDuplicateCheckParamName().split(",");
				Arrays.sort(dupParams);
				collectionDriverPathList.setDuplicateCheckParamName(StringUtils.join(dupParams,','));
			}
			 
			ResponseObject responseObject;
			if(collectionDriverPathList.getFileSeqAlertEnabled()){
				collectionDriverPathList.getMissingFileSequenceId().setMissingFileEndIndex(collectionDriverPathList.getSeqEndIndex());
				collectionDriverPathList.getMissingFileSequenceId().setMissingFileStartIndex(collectionDriverPathList.getSeqStartIndex());
				collectionDriverPathList.getMissingFileSequenceId().setReferenceDevice(collectionDriverPathList.getReferenceDevice());
				collectionDriverPathList.getMissingFileSequenceId().setElementType(BaseConstants.UPSTREAM);
				collectionDriverPathList.getMissingFileSequenceId().setParentDevice(collectionDriverPathList.getParentDevice());
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.DAY_OF_MONTH, 1);
				collectionDriverPathList.getMissingFileSequenceId().setResetTime(cal.getTime());
				collectionDriverPathList.getMissingFileSequenceId().setNextValue(collectionDriverPathList.getMissingFileSequenceId().getMinValue());
				
				collectionDriverPathList.getMissingFileSequenceId().setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				collectionDriverPathList.getMissingFileSequenceId().setStatus(StateEnum.ACTIVE);
				collectionDriverPathList.getMissingFileSequenceId().setCreatedDate(new Date());
				collectionDriverPathList.getMissingFileSequenceId().setLastUpdatedDate(new Date());
				collectionDriverPathList.getMissingFileSequenceId().setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				
				responseObject=pathListService.createPathList(collectionDriverPathList);
			}else{
				collectionDriverPathList.setSeqEndIndex(0);
				collectionDriverPathList.setSeqStartIndex(0);
				collectionDriverPathList.setMissingFileSequenceId(null);
				collectionDriverPathList.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				collectionDriverPathList.setCreatedDate(new Date());
				collectionDriverPathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				responseObject=pathListService.createPathList(collectionDriverPathList);
			}
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if (responseObject.isSuccess()) {
				pathListHistoryService.save(collectionDriverPathList, null);
			}
		}
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Update pathlist parameter
	 * @param pathListCount
	 * @param collectionDriverPathList
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_COLLECTIONDRIVER_PATHLIST, method = RequestMethod.POST)
	public @ResponseBody String updateCollectionDriverPathList(@RequestParam(value="pathListCount") String pathListCount,
			@RequestParam(value="fileSeqId") String fileSeqId,@ModelAttribute(value=FormBeanConstants.COLLECTION_PATHLIST_CONFIGURATION_FORM_BEAN) CollectionDriverPathList collectionDriverPathList,//NOSONAR
			BindingResult result,HttpServletRequest request){
			AjaxResponse ajaxResponse=new AjaxResponse();
		
		validator.validatePathListParams(collectionDriverPathList, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(pathListCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(pathListCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			if(collectionDriverPathList.getDuplicateCheckParamName() != null && collectionDriverPathList.getDuplicateCheckParamName().contains(",")){
				String [] dupParams = 	collectionDriverPathList.getDuplicateCheckParamName().split(",");
				Arrays.sort(dupParams);
				collectionDriverPathList.setDuplicateCheckParamName(StringUtils.join(dupParams,','));
			}
			ResponseObject responseObject;
			CollectionDriverPathList collection_pathlist = (CollectionDriverPathList) pathListService.getPathListById(collectionDriverPathList.getId());
			collectionDriverPathList.setCreatedDate(collection_pathlist.getCreatedDate());
			collectionDriverPathList.setCreatedByStaffId(collection_pathlist.getCreatedByStaffId());
			
			if(collectionDriverPathList.getFileSeqAlertEnabled()){				
				collectionDriverPathList.getMissingFileSequenceId().setMissingFileEndIndex(collectionDriverPathList.getSeqEndIndex());
				collectionDriverPathList.getMissingFileSequenceId().setMissingFileStartIndex(collectionDriverPathList.getSeqStartIndex());
				collectionDriverPathList.getMissingFileSequenceId().setReferenceDevice(collectionDriverPathList.getReferenceDevice());
				collectionDriverPathList.getMissingFileSequenceId().setElementType(BaseConstants.UPSTREAM);
				collectionDriverPathList.getMissingFileSequenceId().setParentDevice(collectionDriverPathList.getParentDevice());

				CollectionDriverPathList cpdl = (CollectionDriverPathList) pathListService.getPathListById(collectionDriverPathList.getId());
				if(cpdl.getMissingFileSequenceId() != null){
					collectionDriverPathList.getMissingFileSequenceId().setId(cpdl.getMissingFileSequenceId().getId());	
					collectionDriverPathList.getMissingFileSequenceId().setResetTime(cpdl.getMissingFileSequenceId().getResetTime());
					collectionDriverPathList.getMissingFileSequenceId().setNextValue(cpdl.getMissingFileSequenceId().getNextValue());
					collectionDriverPathList.getMissingFileSequenceId().setStatus(cpdl.getMissingFileSequenceId().getStatus());
					collectionDriverPathList.getMissingFileSequenceId().setPartner(cpdl.getMissingFileSequenceId().getPartner());
					collectionDriverPathList.getMissingFileSequenceId().setCreatedDate(cpdl.getMissingFileSequenceId().getCreatedDate());
					collectionDriverPathList.getMissingFileSequenceId().setCreatedByStaffId(cpdl.getMissingFileSequenceId().getCreatedByStaffId());
				}else{
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DAY_OF_MONTH, 1);
					collectionDriverPathList.getMissingFileSequenceId().setResetTime(cal.getTime());
					collectionDriverPathList.getMissingFileSequenceId().setStatus(StateEnum.ACTIVE);
					collectionDriverPathList.getMissingFileSequenceId().setNextValue(collectionDriverPathList.getMissingFileSequenceId().getMinValue());
					collectionDriverPathList.getMissingFileSequenceId().setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					collectionDriverPathList.getMissingFileSequenceId().setCreatedDate(new Date());
				}
				collectionDriverPathList.getMissingFileSequenceId().setLastUpdatedDate(new Date());
				collectionDriverPathList.getMissingFileSequenceId().setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				responseObject=pathListService.updatePathListDetail(collectionDriverPathList);
			}else{
				collectionDriverPathList.setSeqEndIndex(0);
				collectionDriverPathList.setSeqStartIndex(0);
				collectionDriverPathList.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				responseObject=pathListService.updatePathListDetail(collectionDriverPathList);
			}
			
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
			if (responseObject.isSuccess()) {
				
				pathListHistoryService.save(collectionDriverPathList, null);
			}
		}
		
		return ajaxResponse.toString();

	}
	
	/**
	 * Validate addtional parameter 
	 * @param pathListCount
	 * @param collectionDriverPathList
	 * @param result
	 * @param request
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.VALIDATE_ADDTIONAL_COLLECTIONPATHLIST_PARAM, method = RequestMethod.POST)
	public @ResponseBody String validateAddtionalCollectionPathList(@RequestParam(value="pathListCount") String pathListCount,
			@ModelAttribute(value=FormBeanConstants.COLLECTION_PATHLIST_CONFIGURATION_FORM_BEAN) CollectionDriverPathList collectionDriverPathList,//NOSONAR
			BindingResult result,HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		
		validator.validateAddtionalCollectionPathListParam(collectionDriverPathList, result,null,null,false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(pathListCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(pathListCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_SUCCESS);
			//ajaxResponse.setResponseMsg(getMessage("pathlist.validate.addtional.param.success"));
		}
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will delete Collection Driver pathlist  
	 * @param pathlistId
	 * @return AJAX responseObj
	 * @throws CloneNotSupportedException 
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_COLLECTION_DRIVER_PATHLIST, method = RequestMethod.POST)
	public @ResponseBody String updateCollectionDriverStatus(
						@RequestParam(value = "pathlistId",required=true) String pathlistId,
						HttpServletRequest request) throws CloneNotSupportedException{
		
			int ipathlistId=Integer.parseInt(pathlistId);
			ResponseObject  responseObject = pathListService.deletePathListDetails(ipathlistId, eliteUtils.getLoggedInStaffId(request),false);
			AjaxResponse  ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
			
		return ajaxResponse.toString();
	}
	

}
