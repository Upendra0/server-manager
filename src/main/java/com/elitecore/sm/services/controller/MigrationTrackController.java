package com.elitecore.sm.services.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.migration.service.MigrationTrackService;
import com.elitecore.sm.migration.validator.MigrationValidator;
import com.elitecore.sm.server.service.ServerService;

@Controller
public class MigrationTrackController extends BaseController {
	
	@Autowired
	ServerService serverService;
	
	@Autowired
	MigrationTrackService migrationTrackService;
	
	@Autowired
	MigrationValidator validator;
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.GET_SERVER_LIST_BY_SERVER_TYPE, method = RequestMethod.POST)
	@ResponseBody
	public String getServerListByServerType(@RequestParam(value = BaseConstants.SERVER_TYPE, required = true) int serverType) throws SMException { 
		ResponseObject responseObject = serverService.getServerListByServerType(serverType);
		return eliteUtils.convertToAjaxResponse(responseObject).toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.IS_MIGRATION_IN_PROCESS, method = RequestMethod.POST)
	@ResponseBody
	public String isMigrationInProcess() throws SMException { 
		ResponseObject responseObject = migrationTrackService.isMigrationInProcess();
		return eliteUtils.convertToAjaxResponse(responseObject).toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.UPDATE_FAILED_MIGRATION_STATUS, method = RequestMethod.POST)
	@ResponseBody
	public String updateFailedMigrationStatus(@RequestParam(value = BaseConstants.MIGRATION_TRACK_DETAIL_ID, required = true) int migrationTrackDetailId) throws SMException { 
		ResponseObject responseObject = migrationTrackService.updateFailedMigrationStatus(migrationTrackDetailId);
		return eliteUtils.convertToAjaxResponse(responseObject).toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.GET_MIGRATION_TRACK_DETAIL_BY_ID, method = RequestMethod.POST)
	@ResponseBody
	public String getMigrationTrackDetailById(@RequestParam(value = BaseConstants.MIGRATION_TRACK_DETAIL_ID, required = true) int migrationTrackDetailId) throws SMException { 
		ResponseObject responseObject = migrationTrackService.getMigrationTrackDetailById(migrationTrackDetailId);
		return eliteUtils.convertToAjaxResponse(responseObject).toString();
	}
	
	@RequestMapping(value = ControllerConstants.DELETE_MIGRATION_TRACK_DETAIL, method = RequestMethod.POST)
	@ResponseBody public  String migrationTrackIdList(
				@RequestParam(value = BaseConstants.MIGRATION_TRACK_ID_LIST,required = true) String migrationTrackIdList,
				HttpServletRequest request) throws SMException {
		logger.debug(">> deleteMigrationTrackDetail in MigraitonTrackController " + migrationTrackIdList);
		
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = migrationTrackService.deleteMigrationTrackDetailByIds(convertStringArrayToInt(migrationTrackIdList), staffId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.SAVE_MIGRATION_TRACK_DETAIL, method = RequestMethod.POST)
	@ResponseBody 
	public String saveMigrationTrackDetail(@ModelAttribute MigrationTrackDetails migrationTrackDetails,//NOSONAR
			BindingResult result, HttpServletRequest request) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		migrationTrackDetails.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
		if(!(migrationTrackDetails.getId() > 0)) {
			migrationTrackDetails.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			migrationTrackDetails.setMigrationStartDate(new Date());
		}
		validator.validateMigrationTrackDetails(migrationTrackDetails, result, null, null, false);
		if (result.hasErrors()) {
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
			logger.info("Failed to save migration track details.");
		} else {
			logger.info("Migration track details has been validated successfully going to create or update migration track details.");
			ResponseObject responseObject = migrationTrackService.saveMigrationTrackDetail(migrationTrackDetails);
			ajaxResponse =  eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('MIGRATION_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.GET_MIGRATION_TRACK_DETAIL_LIST, method = RequestMethod.POST)
	@ResponseBody
	public String getMigrationTrackDetailList(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = false) String sidx,
			@RequestParam(value = "sord", required = false) String sord) {
		logger.debug(" getMigrationTrackDetailList in MigrationTrackController : " + id);
		int migrationTrackId = 0;
		if (!StringUtils.isEmpty(id)) {
			migrationTrackId = Integer.parseInt(id);
		}
		long count = this.migrationTrackService.getMigrationTrackTotalCount();
		List<Map<String, Object>> rowList = null;
		if (count > 0) {
			rowList = this.migrationTrackService.getMigrationTrackDetailsPaginatedList(
					migrationTrackId, eliteUtils.getStartIndex(limit,
							currentPage,
							eliteUtils.getTotalPagesCount(count, limit)),
					limit, sidx, sord);
		}
		logger.debug("<< getMigrationTrackDetailList in MigrationTrackController ");
		return new JqGridData<Map<String, Object>>(
				eliteUtils.getTotalPagesCount(count, limit), currentPage,
				(int) count, rowList).getJsonString();
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
