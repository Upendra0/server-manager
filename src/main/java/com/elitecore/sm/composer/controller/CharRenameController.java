/**
 * 
 */
package com.elitecore.sm.composer.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.service.CharRenameOperationService;
import com.elitecore.sm.composer.validator.ComposerValidator;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.validator.PathListValidator;
import com.elitecore.sm.util.DateFormatter;

/**
 * @author Ranjitsinh Reval
 *
 */
@Controller
public class CharRenameController extends BaseController {


	
	@Autowired
	CharRenameOperationService charRenameOperationService;
	
	@Autowired
	ComposerValidator  composerValidator;
	
	@Autowired
	PathListValidator pathListValidator;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}
	
	/**
	 * Method will create new plug-in character rename operation parameters.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.CREATE_CHAR_RENAME_PARAMS, method = RequestMethod.POST)
	@ResponseBody public String createPluginCharRenameParams(@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		composerValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}
				}else{
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),error.getDefaultMessage());
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);
			

		}else{
			charRenameOperation.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setCreatedDate(new Date());
			
			ResponseObject responseObject = this.charRenameOperationService.addCharRenameOperationParams(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will create new plug-in character rename operation parameters.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.UPDATE_CHAR_RENAME_PARAMS, method = RequestMethod.POST)
	@ResponseBody public String updatePluginCharRenameParams(@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		composerValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}
				}else{
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),error.getDefaultMessage());
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			charRenameOperation.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setLastUpdatedDate(new Date());


			ResponseObject responseObject = this.charRenameOperationService.updateCharRenameOperationParams(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_CHAR_RENAME_BY_ID, method = RequestMethod.POST)
	@ResponseBody public  String getCharRenameOperationById(
				@RequestParam(value = "id",required = true) int id) {
		logger.debug(">> getCharRenameOperationById in CharRenameController " + id); 
		ResponseObject responseObject  = this.charRenameOperationService.getCharRenameParamsById(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}

	/**
	 * Method will fetch service file rename config. list by file rename agent service id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_CHAR_RENAME_BY_SERVICE_FILE_RENAME_AGENT_ID, method = RequestMethod.POST)
	@ResponseBody public  String getAllRenameOperationsBySvcFileRenConfigId(
				@RequestParam(value = "id",required = true) int id) {
		logger.debug(">> getAllRenameOperationsBySvcFileRenConfigId in CharRenameController " + id); 
		ResponseObject responseObject  = this.charRenameOperationService.getAllRenameOperationsBySvcFileRenConfigId(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_COMPOSER','UPDATE_COLLECTION_DRIVER','UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.DELETE_CHAR_RENAME_PARAMS, method = RequestMethod.POST)
	@ResponseBody public  String deletePluginCharRenameParams(
				@RequestParam(value = "id",required = true) int id) {
		logger.debug(">> deletePluginCharRenameParams in CharRenameController " + id); 
		ResponseObject responseObject  = this.charRenameOperationService.deleteCharRenameOperationParams(id, false);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	

	/**
	 * Method will delete charRenameOperation params by id from file renaming agent service.
	 * @param id
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SERVICE_FROM_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.DELETE_CHAR_RENAME_PARAMS_AGENT, method = RequestMethod.POST)
	@ResponseBody public  String deleteCharRenameParamsFromAgent(
				@RequestParam(value = "id",required = true) int id) {
		logger.debug(">> deletePluginCharRenameParams in CharRenameController " + id); 
		ResponseObject responseObject  = this.charRenameOperationService.deleteCharRenameOperationParams(id, true);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}	
	
	/**
	 * Method will create new character rename operation parameters in service configured with file renaming agent.
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('ADD_SERVICE_TO_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.CREATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT, method = RequestMethod.POST)
	@ResponseBody public String createCharRenameParamsFileRenameAgent(
			@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		composerValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, true);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			Map<String, String> errorMsgs = new HashMap<>();
			
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
			

		}else{
			charRenameOperation.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setCreatedDate(new Date());
			
			ResponseObject responseObject = this.charRenameOperationService.addCharRenameOperationToFileRenameAgent(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will update character rename operation parameters of service configured with file renaming agent.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVICE_TO_FILE_RENAME_AGENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_CHAR_RENAME_PARAMS_FILE_RENAME_AGENT, method = RequestMethod.POST)
	@ResponseBody public String updateCharRenameOperationParamsForFileRenameAgent(
			@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		composerValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, true);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			charRenameOperation.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setLastUpdatedDate(new Date());

			ResponseObject responseObject = this.charRenameOperationService.updateCharRenameOperationParamsForFileRenameAgent(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will fetch all vendor list by device type id.
	 * @param deviceId
	 * @param request
	 * @return AJax response.
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_PATHLIST')")
	@RequestMapping(value = ControllerConstants.GET_COLLECTION_CHAR_RENAME_BY_PATHID, method = RequestMethod.POST)
	@ResponseBody public  String getCollectionCharRenameOperationById(
				@RequestParam(value = "id",required = true) int id) {
		logger.debug(">> getCharRenameOperationById in CharRenameController " + id); 
		ResponseObject responseObject  = this.charRenameOperationService.getCollectionCharRenameParamsById(id);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Method will create new plug-in character rename operation parameters.
	 * @param driverCount
	 * @param collectionDriver
	 * @param result
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.CREATE_COLLECTION_SERVICE_CHAR_RENAME_PARAMS, method = RequestMethod.POST)
	@ResponseBody public String createControllerCharRenameParams(@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		pathListValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}
				}else{
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),error.getDefaultMessage());
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);
			

		}else{
			charRenameOperation.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setCreatedDate(new Date());
			int startIndex = charRenameOperation.getStartIndex();
			if(startIndex>0){
				startIndex--;
				charRenameOperation.setStartIndex(startIndex);
			}
			ResponseObject responseObject = this.charRenameOperationService.addCharRenameOperationParams(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_PATHLIST')")
	@RequestMapping(value = ControllerConstants.UPDATE_COLLECTION_SERVICE_CHAR_RENAME_PARAMS, method = RequestMethod.POST)
	@ResponseBody public String updateCollectionCharRenameParams(@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.CHAR_RENAME_OPERATION_FORM_BEAN) CharRenameOperation charRenameOperation,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		pathListValidator.validateChareRenameParameters(charRenameOperation, result, null, false,null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
					}
				}else{
					if(error.getField().equalsIgnoreCase("startIndex") || error.getField().equalsIgnoreCase("endIndex")){
						errorMsgs.put(blockCount+"_char_"+error.getField(),error.getDefaultMessage());
					}else{
						errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
					}
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			charRenameOperation.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			charRenameOperation.setLastUpdatedDate(new Date());
			int startIndex = charRenameOperation.getStartIndex();
			if(startIndex>0){
				startIndex--;
				charRenameOperation.setStartIndex(startIndex);
			}
			ResponseObject responseObject = this.charRenameOperationService.updateCharRenameOperationParams(charRenameOperation);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
}
