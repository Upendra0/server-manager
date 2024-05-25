/**
 * 
 */
package com.elitecore.sm.errorreprocess.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.core.util.mbean.data.filereprocess.FileReprocessStatusEnum;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.errorreprocess.model.SearchErrorReprocessDetails;
import com.elitecore.sm.errorreprocess.service.ReprocessDetailsService;
import com.elitecore.sm.services.model.ErrorReprocessingActionEnum;
import com.elitecore.sm.services.model.ServiceType;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Utilities;


/**
 * @author Ranjitsinh Reval
 *
 */

@RestController
public class ErrorReprocessDetailController extends BaseController {

	
	@Autowired
	ReprocessDetailsService reprocessDetailsService; 
	
	
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_FILE_STATUS','REPROCESS_FILE')")
	@RequestMapping(value = {ControllerConstants.INIT_REPROCESSING_STATUS }, method = RequestMethod.POST)
	public ModelAndView initReprocessingStatus(@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType
			,@RequestParam(value=BaseConstants.BATCH_ID,required=false) Integer batchId) {
		ModelAndView model = new ModelAndView(ViewNameConstants.ININT_ERROR_REPROCESSING);
		
		List<ServiceType> serviceTypeList = (List<ServiceType>) MapCache.getConfigValueAsObject(SystemParametersConstant.SERVICE_TYPE_LIST);
		if(serviceTypeList != null && !serviceTypeList.isEmpty()) {
			int length = serviceTypeList.size();
			for(int i = length-1; i >= 0; i--) {
				if(!(serviceTypeList.get(i).getAlias().equalsIgnoreCase(EngineConstants.DISTRIBUTION_SERVICE) 
						|| serviceTypeList.get(i).getAlias().equalsIgnoreCase(EngineConstants.PARSING_SERVICE)
						|| serviceTypeList.get(i).getAlias().equalsIgnoreCase(EngineConstants.PROCESSING_SERVICE))) {
					serviceTypeList.remove(i);
				}
			}
		}
		model.addObject("serviceTypeList", serviceTypeList);
		
		model.addObject("fileProcessingStatusEnum",java.util.Arrays.asList(FileReprocessStatusEnum.values()));
		model.addObject("fileProcessingActionEnum",java.util.Arrays.asList(ErrorReprocessingActionEnum.values()));
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,requestActionType);
		model.addObject(BaseConstants.BATCH_ID, batchId);
		return model;
	}
	
	@PreAuthorize("hasAnyAuthority('REPROCESS_FILE','AUTO_REPROCESS_FILE','VIEW_FILE_STATUS')")
	@RequestMapping(value = ControllerConstants.GET_SERVICE_BY_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public String getServiceByType(@RequestParam(value = "serviceAlias", required = false) String serviceAlias) {
		ResponseObject responseObject = reprocessDetailsService.getServiceByType(serviceAlias);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will add new error re-process batch.
	 * @param serviceId
	 * @param pluginId
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_FILE_STATUS','SEARCH_FILE')")
	@RequestMapping(value = ControllerConstants.GET_BATCH_DETAILS, method = RequestMethod.POST)
	@ResponseBody
	public String getAllBatchDetails(SearchErrorReprocessDetails reprocessDetails, 
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx, 
			@RequestParam(value = "sord", required = true) String sord) {
			

		logger.debug(">> getAllBatchDetails in ErrorReprocessDetailController " + reprocessDetails); 
		
		long count =  this.reprocessDetailsService.getTotalReprocessBatchCount(reprocessDetails);
		
		List<Map<String, Object>> rowList = null;
		if(count > 0){
			rowList = this.reprocessDetailsService.getPaginatedList(reprocessDetails,eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),limit, sidx,sord);
		}
		
		logger.debug("<< getAllBatchDetails in ErrorReprocessDetailController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit),	currentPage,(int)count,	rowList).getJsonString();
	}
	
	@PreAuthorize("hasAnyAuthority('MODIFY_FILES')")
	@RequestMapping(value = ControllerConstants.REVERT_MULTIPLE_MODIFIED_FILES, method = RequestMethod.POST)
	public String revertMultipleModifiyFiles(@RequestParam(value="detailIdList",required=true) String detailIdList) {
		logger.debug(">> revertMultipleModifiyFiles in ErrorReprocessDetailController " + Utilities.convertStringArrayToInt(detailIdList));
		ResponseObject responseObject = this.reprocessDetailsService.revertMultipleModifiyFiles(Utilities.convertStringArrayToInt(detailIdList));
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	
}
