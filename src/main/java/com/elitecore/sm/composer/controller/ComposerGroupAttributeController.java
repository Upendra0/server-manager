package com.elitecore.sm.composer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
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

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;
import com.elitecore.sm.composer.service.ComposerGroupAttributeService;
import com.elitecore.sm.composer.validator.ComposerMappingValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class ComposerGroupAttributeController extends BaseController{

	@Autowired
	ComposerGroupAttributeService composerGroupAttributeService;
	
	@Autowired
	ComposerMappingValidator composerMappingValidator;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.ADD_EDIT_GROUP_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody public  String addEditGroupAttributeByMappingId(
						@ModelAttribute (value=FormBeanConstants.GROUP_ATTRIBUTE_FORM_BEAN) ComposerGroupAttribute composerGroupAttribute,//NOSONAR 
						@RequestParam(value = "actionType",required=true) String actionType,
						@RequestParam(value = "plugInType",required=true) String plugInType,
						@RequestParam(value = "mappingId",required=true) int mappingId,
						@RequestParam(value = "groupAttrLists",required=false) String groupAttrLists,
						@RequestParam(value = "attrLists",required=true) String attrLists,
						BindingResult result,
						HttpServletRequest request) throws SMException{
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		
		composerMappingValidator.validateComposerGroupAttributeName(composerGroupAttribute, result, null, false);       
		
		if(result.hasErrors()){
			 ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			 eliteUtils.transferErrorsToAjaxResponse(ajaxResponse,result);
		}else{
			if(attrLists != null && !attrLists.isEmpty()){
				int staffId = eliteUtils.getLoggedInStaffId(request);
				if(BaseConstants.ACTION_TYPE_ADD.equals(actionType)){
					responseObject = composerGroupAttributeService.createGroupAttribute(composerGroupAttribute, groupAttrLists, attrLists, mappingId, plugInType, staffId);
				}else{
					responseObject = composerGroupAttributeService.updateGroupAttribute(composerGroupAttribute, groupAttrLists, attrLists, mappingId, plugInType,staffId);
				}
			}else{
				responseObject.setSuccess(false);
				responseObject.setObject(null);
				responseObject.setResponseCode(ResponseCode.COMPOSER_GROUP_ATTRIBUTE_ADD_FAIL);
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		return ajaxResponse.toString();
	}
    
    @SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_GROUP_ATTRIBUTE_GRID_LIST_BY_MAPPING_ID, method = RequestMethod.POST)
	@ResponseBody public  String getGroupAttributeGridListByMappingId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId){
		
    	logger.debug("getGroupAttributeListByMappingId in TapComposerController " + mappingId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> resultList = new ArrayList<>();
		responseObject = composerGroupAttributeService.getGroupAttributeListByMappingId(mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerGroupAttribute>) responseObject.getObject();
		}
		
		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		JSONArray jAllGrpAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerGroupAttribute gruopAttribute : resultList) {
				jGrpAttrObj = new JSONObject();
				jGrpAttrObj.put("id", gruopAttribute.getId());
				jGrpAttrObj.put("name", gruopAttribute.getName());
								
				jAllGrpAttrArr.put(jGrpAttrObj);
			}
		}
		
		jGrpAttributeList.put("groupAttributeList", jAllGrpAttrArr);
		responseObject.setObject(jGrpAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
    
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.DELETE_GROUP_ATTRIBTE, method = RequestMethod.POST)
	@ResponseBody public  String deleteGroupAttribute(
						@RequestParam(value = "mappingId" , required = true) int mappingId,
						@RequestParam(value = "attributeId", required=true) String groupAttributeIds,
						HttpServletRequest request){
		int staffId = eliteUtils.getLoggedInStaffId(request);
		ResponseObject responseObject = composerGroupAttributeService.deleteGroupAttributes(groupAttributeIds, staffId, mappingId);	
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_VIEW_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getViewGroupAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId,
				@RequestParam(value = "groupId" , required = true) int groupId){
		
    	logger.debug("getGroupAttributeListByGroupId in ComposerGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> resultList = new ArrayList<>();
		responseObject = composerGroupAttributeService.getGroupAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerGroupAttribute>) responseObject.getObject();
		}
		
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerGroupAttribute composerGroupAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerGroupAttribute.getId());
				jAttrObj.put("groupName", composerGroupAttribute.getName());
								
				jAllAttrArr.put(jAttrObj);
			}
		}
		
		jAttributeList.put("groupAttributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_VIEW_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getViewAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId,
				@RequestParam(value = "groupId" , required = true) int groupId){
		
    	logger.debug("getAttributeListByGroupId in ComposerGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> resultList = new ArrayList<>();
		responseObject = composerGroupAttributeService.getAttachedAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerAttribute>) responseObject.getObject();
		}
		
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerAttribute composerAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerAttribute.getId());
				jAttrObj.put("destinationField", composerAttribute.getDestinationField());
				jAttrObj.put("unifiedField", composerAttribute.getUnifiedField());
								
				jAllAttrArr.put(jAttrObj);
			}
		}
		
		jAttributeList.put("attributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_ADD_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getAddGroupAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId){
		
    	logger.debug("getAttributeListBymappingId in ComposerGroupAttributeController " + mappingId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> resultList = new ArrayList<>();
		responseObject = composerGroupAttributeService.getGroupAttributeListEligibleToAttachWithGroupByMappingId(mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerGroupAttribute>) responseObject.getObject();
		}
		
		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		JSONArray jGrpAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerGroupAttribute composerGroupAttribute : resultList) {
				jGrpAttrObj = new JSONObject();
				jGrpAttrObj.put("id", composerGroupAttribute.getId());
				jGrpAttrObj.put("groupName", composerGroupAttribute.getName());
								
				jGrpAllAttrArr.put(jGrpAttrObj);
			}
		}
		
		jGrpAttributeList.put("groupAttributeList", jGrpAllAttrArr);
		responseObject.setObject(jGrpAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_ADD_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getAddAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId){
		
    	logger.debug("getAttributeListBymappingId in ComposerGroupAttributeController " + mappingId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> resultList = new ArrayList<>();
		responseObject = composerGroupAttributeService.getAttributeListEligibleToAttachWithGroup(mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerAttribute>) responseObject.getObject();
		}
		
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		JSONArray jAllAttrArr = new JSONArray();
		
		if (resultList != null) {
			for (ComposerAttribute composerAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerAttribute.getId());
				jAttrObj.put("destinationField", composerAttribute.getDestinationField());
				jAttrObj.put("unifiedField", composerAttribute.getUnifiedField());
								
				jAllAttrArr.put(jAttrObj);
			}
		}
		
		jAttributeList.put("attributeList", jAllAttrArr);
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_UPDATE_GROUP_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getUpdateGroupAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId,
				@RequestParam(value = "groupId" , required = true) int groupId){
		
    	logger.debug("getGroupAttributeListByGroupId in ComposerGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerGroupAttribute> resultList = new ArrayList<>();
		JSONObject jGrpAttributeList = new JSONObject();
		JSONObject jGrpAttrObj;
		
		/* getting all attribute list attached with the group and setting it on json object*/
		JSONArray jAllAtchGrpAttrArr = new JSONArray();
		responseObject = composerGroupAttributeService.getAttachedGroupAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerGroupAttribute>) responseObject.getObject();
		}
		if (resultList != null) {
			for (ComposerGroupAttribute composerGroupAttribute : resultList) {
				jGrpAttrObj = new JSONObject();
				jGrpAttrObj.put("id", composerGroupAttribute.getId());
				jGrpAttrObj.put("groupName", composerGroupAttribute.getName());
								
				jAllAtchGrpAttrArr.put(jGrpAttrObj);
			}
		}
		jGrpAttributeList.put("attachedGroupAttributeList", jAllAtchGrpAttrArr);
		
		/* getting all attribute list which are eligible to attached with the group and setting it on json object*/
		JSONArray jAllEllGrpAttrArr = new JSONArray();
		responseObject = null;
		resultList = null;
		responseObject = composerGroupAttributeService.getGroupAttributeListEligibleToAttachWithGroupByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerGroupAttribute>) responseObject.getObject();
		}
		if (resultList != null) {
			for (ComposerGroupAttribute composerGroupAttribute : resultList) {
				jGrpAttrObj = new JSONObject();
				jGrpAttrObj.put("id", composerGroupAttribute.getId());
				jGrpAttrObj.put("groupName", composerGroupAttribute.getName());
								
				jAllEllGrpAttrArr.put(jGrpAttrObj);
			}
		}
		jGrpAttributeList.put("eligibleGroupAttributeList", jAllEllGrpAttrArr);
		
		responseObject.setObject(jGrpAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('UPDATE_COMPOSER')")
	@RequestMapping(value = ControllerConstants.GET_UPDATE_ATTRIBUTE_GRID_LIST_BY_GROUP_ID, method = RequestMethod.POST)
	@ResponseBody public  String getUpdateAttributeListByGroupId(
			    @RequestParam(value = "plugInType" , required = true) String composerType,
				@RequestParam(value = "mappingId" , required = true) int mappingId,
				@RequestParam(value = "groupId" , required = true) int groupId){
		
    	logger.debug("getAttributeListByGroupId in ComposerGroupAttributeController " + groupId); 
		ResponseObject responseObject = new ResponseObject();
		List<ComposerAttribute> resultList = new ArrayList<>();
		JSONObject jAttributeList = new JSONObject();
		JSONObject jAttrObj;
		
		/* getting all attribute list attached with the group and setting it on json object*/
		JSONArray jAllAtchAttrArr = new JSONArray();
		responseObject = composerGroupAttributeService.getAttachedAttributeListByGroupId(groupId, mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerAttribute>) responseObject.getObject();
		}
		if (resultList != null) {
			for (ComposerAttribute composerAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerAttribute.getId());
				jAttrObj.put("destinationField", composerAttribute.getDestinationField());
				jAttrObj.put("unifiedField", composerAttribute.getUnifiedField());
								
				jAllAtchAttrArr.put(jAttrObj);
			}
		}
		jAttributeList.put("attachedAttributeList", jAllAtchAttrArr);
		
		/* getting all attribute list which are eligible to attached with the group and setting it on json object*/
		JSONArray jAllEllAttrArr = new JSONArray();
		responseObject = null;
		resultList = null;
		responseObject = composerGroupAttributeService.getAttributeListEligibleToAttachWithGroup(mappingId);
		if(responseObject.isSuccess() && responseObject.getObject()!=null) {					
			resultList = (List<ComposerAttribute>) responseObject.getObject();
		}
		if (resultList != null) {
			for (ComposerAttribute composerAttribute : resultList) {
				jAttrObj = new JSONObject();
				jAttrObj.put("id", composerAttribute.getId());
				jAttrObj.put("destinationField", composerAttribute.getDestinationField());
				jAttrObj.put("unifiedField", composerAttribute.getUnifiedField());
								
				jAllEllAttrArr.put(jAttrObj);
			}
		}
		jAttributeList.put("eligibleAttributeList", jAllEllAttrArr);
		
		responseObject.setObject(jAttributeList);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PARSER_ATTRIBUTE_ADD_SUCCESS);
				
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
				
	}
	
}
